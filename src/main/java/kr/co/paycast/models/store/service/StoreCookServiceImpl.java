package kr.co.paycast.models.store.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.StoreAlimTalk;
import kr.co.paycast.models.store.StoreCookAlarm;
import kr.co.paycast.models.store.StoreCookTask;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderCook;
import kr.co.paycast.models.store.StoreOrderList;
import kr.co.paycast.models.store.dao.StoreCookDao;
import kr.co.paycast.models.store.dao.StoreCookTaskDao;
import kr.co.paycast.models.store.dao.StoreOrderDao;
import kr.co.paycast.utils.FireMessage;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.store.CookJsonItem;
import kr.co.paycast.viewmodels.store.CookJsonList;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("StoreCookService")
public class StoreCookServiceImpl implements StoreCookService {
	private static final Logger logger = LoggerFactory.getLogger(StoreCookServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
    
	@Autowired
	private MessageManager msgMgr;
	
    @Autowired
    private StoreCookDao storeCookDao;
    
    @Autowired
    private StoreOrderDao storeOrderDao;
    
    @Autowired
    private SiteService siteService;
    
	@Autowired
	private StoreCookTaskDao storeCookTaskDao;

    @Autowired 
    private DeviceService devService;

    @Autowired 
    private StoreService storeService;
    
    @Autowired 
    private StoreAllimTalkService alimTalkService;
    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	//결제가 완료 되었을 때 저장을 진행  모바일 결제와 키오스크 결제 에서 확인 가능
	@Override
	public void saveOrUpdate(StoreOrderCook storeOrderCook) {
		storeCookDao.saveOrUpdate(storeOrderCook);
	}
	
	@Override
	public List<CookJsonItem> getStoreCookList(String storeId) {
		
		int storeIdInt = Util.parseInt(storeId);
		// 조회 할때 매장ID(StoreID)가 필요하다. 
		// 1. storecook table에 존재 하는 List를 가져온다.
		//   1-1. 대기상태인 List 만 조회해 온다. 
		//   1-2. 당일에 해당 하는 List만 가져온다.
		// 2. 조회된 값 중 storeorderid 값을 가지고 결제 내역을 조회 한다. 
		//   2-1. 조회된 내역중 주문번호를 가지고 storeorder 테이블을 조회 한다. 
		//   2-2. 주문seq,TID를 조회 한다. 
		// 3. 조회한 TID를 가지고 주문 목록을 조회 한다. (TABLE : STORE_ORDER_LIST)
		// 화면에서 사용 되는 List를 작성 하여 그 목록을 가져온다. 
		
		List<CookJsonItem> cookItemList = new ArrayList<CookJsonItem>();
		List<StoreOrderCook> resCook = storeCookDao.getStoreCookStayList(storeIdInt, "Y"); //Y(완료)가 아닌 모든 것을 가져온다.
		
		cookItemList = cookListItemCreate(resCook);
		
		return cookItemList;
	}
	
	@Override
	public List<CookJsonItem> getStoreCookListRenew(String storeId) {
		
		int storeIdInt = Util.parseInt(storeId);
		List<CookJsonItem> cookItemList = new ArrayList<CookJsonItem>();
		
		List<StoreOrderCook> resCook = storeCookDao.getStoreCookStayList(storeIdInt, "Y"); //Y(완료)가 아닌 모든 것을 가져온다.
		cookItemList = cookListItemCreate(resCook);
		
		return cookItemList;
	}
	
	@Override
	public List<CookJsonItem> getStoreCookCancelRenew(String storeId, HttpSession session) {
		
		int storeIdInt = Util.parseInt(storeId);
		List<CookJsonItem> cookItemList = new ArrayList<CookJsonItem>();
		
		//취소가 된 메뉴에 대해서는 구분값을 주어서 해당 주문건을 삭제 한다.
		// 추가로 조회된 내용을 가지고 목록에서 삭제 처리를 진행한다.
		// 2019.04.17 주문이 취소된 내역을 가져온다. 
		// 주방용 패드에서는 해당 메뉴를 바로 취소해 버린다.
		// 2019.04.23 주방용 패드 취소 여부에 대한 변수가 추가됨 
		// orderCancelPad  >> 취소 패드 여부 (N : default, 1 : 취소완료 후 프린트 요청, Y : 취소 프린트 완료)
		List<StoreOrder> storeOrderCancelList = storeOrderDao.getMenuCancelListStorebyPadDevice(storeIdInt, "1");
		if(storeOrderCancelList.size() > 0){
			cookItemList = cookListItemCreateCancel(storeOrderCancelList, cookItemList, session);
		}
		
		return cookItemList;
	}
	
	@Override
	public List<CookJsonItem> getCookComListByStoreId(int storeId) {
		
		List<CookJsonItem> cookItemList = new ArrayList<CookJsonItem>();
		
		List<StoreOrderCook> resCook = storeCookDao.getStoreCookStayList(storeId, "N"); //N(대기)가 아닌 목록(완료된 목록 보여주기)
		
		cookItemList = cookListItemCreate(resCook);
		
		return cookItemList;
	}
	
	public List<CookJsonItem> cookListItemCreate(List<StoreOrderCook> resCook) {
		List<CookJsonItem> cookItemList = new ArrayList<CookJsonItem>();
		
		for(StoreOrderCook storeOrderCook : resCook){
			StoreOrder storeOrderOne = storeOrderDao.getOrderOne(storeOrderCook.getStoreOrderId());
			
			if(!"Y".equals(storeOrderOne.getOrderCancelStatus())){
				CookJsonItem cookJsonItem = new CookJsonItem();
				
				cookJsonItem.setCookId(String.valueOf(storeOrderCook.getId()));
				cookJsonItem.setOrderMenuComplete(storeOrderCook.getOrderMenuComplete());
				cookJsonItem.setStoreId(String.valueOf(storeOrderCook.getStoreId()));
				cookJsonItem.setStoreOrderId(String.valueOf(storeOrderCook.getStoreOrderId()));
				cookJsonItem.setOrderNumber(storeOrderOne.getOrderNumber());
				cookJsonItem.setOrderSeq(storeOrderOne.getOrderSeq());
				cookJsonItem.setMenuCancelYN("N");
				
				List<CookJsonList> cookJsonList = new ArrayList<CookJsonList>();
				List<StoreOrderList> resList = storeOrderDao.getOrderList(storeOrderOne.getOrderNumber());
				for(StoreOrderList storeOrderListOne : resList){
					
					CookJsonList cookJsonListItem = new CookJsonList();
					cookJsonListItem.setOrderListID(String.valueOf(storeOrderListOne.getId()));
					cookJsonListItem.setProductID(String.valueOf(storeOrderListOne.getOrderMenuId()));
					cookJsonListItem.setOrderCount(String.valueOf(storeOrderListOne.getOrderMenuAmount()));
					cookJsonListItem.setOrderMenuNotice(storeOrderListOne.getOrderMenuNotice());
					cookJsonListItem.setOrderMenuPacking(String.valueOf(storeOrderListOne.getOrderMenuPacking()));
					String menu = "";
					if("I".equals(storeOrderOne.getOrderType()) && "RF".equals(storeOrderOne.getPayment())){
						menu = "(리필)<br>" + storeOrderListOne.getOrderMenuName();
					}else{
						menu = storeOrderListOne.getOrderMenuName();
					}
	        		
	        		if(!Util.isNotValid(storeOrderListOne.getOrderSelectEss())){
	        			menu+="<br>(필수 메뉴)";
						String[] menu1 = storeOrderListOne.getOrderSelectEss().split(",");
						for(int t=0; t < menu1.length; t++){
							if(!Util.isNotValid(menu1[t])){
								String[] menu2 = menu1[t].split("_");
								if(menu2.length > 0){
									String[] menu3 = menu2[1].split("\\(");
									if(!"".equals(menu) && menu3.length > 1){
										menu += "<br > &nbsp;&nbsp;&nbsp;- ";
									}
									menu += PayUtil.separationistName(menu3);
								}
							}
						}
	        		}
	        		if(!Util.isNotValid(storeOrderListOne.getOrderSelectAdd())){
	        			menu+="<br>(선택 메뉴)";
						String[] menu1 = storeOrderListOne.getOrderSelectAdd().split(",");
						for(int t=0; t < menu1.length; t++){
							if(!Util.isNotValid(menu1[t])){
								String[] menu2 = menu1[t].split("_");
								if(menu2.length > 0){
									String[] menu3 = menu2[1].split("\\|\\|");
									if(menu3.length > 0){
										for(int ttt=0; ttt < menu3.length; ttt++){
											String[] menu4 = menu3[ttt].split("\\(");
											if(!"".equals(menu) && menu4.length > 1){
												menu += "<br > &nbsp;&nbsp;&nbsp;- ";
											}
											menu += PayUtil.separationistName(menu4);
										}
									}
								}
							}
						}
	        		}
	        		cookJsonListItem.setProductName(menu);
					
					cookJsonList.add(cookJsonListItem);
				}
				cookJsonItem.setOrderMenuList(cookJsonList);
				
				cookItemList.add(cookJsonItem);
			}
		}
		
		return cookItemList;
	}
	
	public List<CookJsonItem> cookListItemCreateCancel(List<StoreOrder> storeOrderCancelList, List<CookJsonItem> cookItemList, HttpSession session) {
		
		for(StoreOrder storeOrder : storeOrderCancelList){
			CookJsonItem cookJsonItem = new CookJsonItem();
			StoreOrderCook storeOrderCook = storeCookDao.getStoreCookbyStoreOrderId(storeOrder.getId());
			
			//
			// [PayCast - kdk]  20190710 ----------------------------------------------------------------- start
			//
			// Null 체크 추가 - 20190710
			if(storeOrderCook != null){
				cookJsonItem.setCookId(String.valueOf(storeOrderCook.getId()));
				cookJsonItem.setMenuCancelYN("Y");
				cookItemList.add(cookJsonItem);
				
				storeOrderCook.setOrderMenuComplete("Y");
				storeOrderCook.touchWho(session);
				storeCookDao.saveOrUpdate(storeOrderCook);
				
				//2019.04.23  취소관련 패드와 프린트가 나누어 져서 이곳에서는 패드 취소에 대해서만 Y값으로 변경한다. 
				storeOrder.setOrderCancelStatus("Y");
				storeOrder.setOrderCancelPad("Y");
				storeOrder.touchWho(session);
				storeOrderDao.saveOrUpdate(storeOrder);
			}
		}
		
		return cookItemList;
	}
	
	@Override
	public Document getCookAlramListByStoreIdDeviceId(int storeId, String deviceId, Document document) {
		
		Store store = storeService.getStore(storeId);
		if(store == null){
        	logger.error("getMonListByStoreId [{}]", "매장을 조회 할수 없습니다. ");
        	throw new ServerOperationForbiddenException("매장을 조회 할수 없습니다. ");
		}
		
		logger.info("DID 목록 내려주기 . XML 만들기 자료 조회");
		
		// DID 전송이 아직 되지 않은 목록을 가져온다.
		// N : DID 전송이 되지 않는 상태
		// S : 대기 상태인 메뉴
		List<StoreCookAlarm> resAlarmList = storeCookDao.getAlramListByStoreIdDeviceId(storeId, deviceId, "N", "S"); 
		
		
		logger.info("DID 목록 내려주기 . XML 만들기 자료 조회 종료");
		Element root = document.addElement("PayCast")
				.addAttribute("version", "1")
				.addAttribute("generated", new Date().toString());
		
		logger.info("DID 목록 내려주기 . XML 형식 시작");
		
		for(StoreCookAlarm alarmOne : resAlarmList){
			StoreOrder storeOrderOne = storeOrderDao.getOrderOne(alarmOne.getStoreOrderId()); // 주문 ID(storeOrder ID)
			List<StoreOrderList> resList = storeOrderDao.getOrderList(storeOrderOne.getOrderNumber());
			
			Element storeElement = root.addElement("SEQ");
			storeElement.addAttribute("orderSeq", storeOrderOne.getOrderSeq()); //주문 번호
			storeElement.addAttribute("menucount", String.valueOf(resList.size())); //메뉴 리스트 수
			storeElement.addAttribute("cookAlarmId", String.valueOf(alarmOne.getId())); //알림 ID 번호
			storeElement.addAttribute("cookAlarmDate", String.valueOf(alarmOne.getWhoCreationDate())); //알림 ID 생성일자
			
			logger.info("orderSeq >> [{}]", storeOrderOne.getOrderSeq());
			logger.info("alarmOne.getId() >> [{}]", alarmOne.getId());
			
			for(StoreOrderList storeOrderList : resList){
				Element menuElement = storeElement.addElement("Menu");
				
				logger.info("storeOrderList.getOrderMenuName() >> [{}]", storeOrderList.getOrderMenuName());
				logger.info("storeOrderList.getOrderMenuNotice() >> [{}]", storeOrderList.getOrderMenuNotice());
				
				if("O".equals(storeOrderList.getOrderMenuNotice()) ){
					//새로운 알림에 대해서는 Y를 내려 주며 orderMenuNewAlarm을 추가 해준다. 
					menuElement.addAttribute("orderMenuNewAlarm", "Y"); //주문 명	
					menuElement.addAttribute("orderMenuNotice", "Y"); //주문알림 정보 수신 여부
				}else{
					menuElement.addAttribute("orderMenuNotice", storeOrderList.getOrderMenuNotice()); //주문알림 정보 수신 여부
				}
				menuElement.addAttribute("orderMenuPacking", String.valueOf(storeOrderList.getOrderMenuPacking()));// 포장 여부
				menuElement.addAttribute("orderMenuAmount", String.valueOf(storeOrderList.getOrderMenuAmount()));//주문 메뉴 수량
				menuElement.addAttribute("orderMenuName", storeOrderList.getOrderMenuName()); //주문 명
			}
		}
		
		
		logger.info("/cookAlarmListRead >> root.toString() >> [{}]", root.toString());
		logger.info("DID 목록 내려주기 . XML 형식 끝");
		
		return document;
	}
	
	@Override
	public Document getCookComListByStoreIdDeviceId(int storeId, String deviceId, Document document) {
		
		Store store = storeService.getStore(storeId);
		if(store == null){
			logger.error("getMonListByStoreId", "매장을 조회 할수 없습니다. ");
			throw new ServerOperationForbiddenException("매장을 조회 할수 없습니다. ");
		}
		
		// DID 전송이 아직 되지 않은 목록을 가져온다.
		// N : DID 전송이 되지 않는 상태
		// C : 주문이 모두 완료가 된 상태
		List<StoreCookAlarm> resAlarmList = storeCookDao.getAlramListByStoreIdDeviceId(storeId, deviceId, "N", "C"); 
		
		Element root = document.addElement("PayCast")
				.addAttribute("version", "1")
				.addAttribute("generated", new Date().toString());
		
		logger.info("DID 완료 목록 내려주기 . XML 형식  시작");
		
		for(StoreCookAlarm alarmOne : resAlarmList){
			StoreOrder storeOrderOne = storeOrderDao.getOrderOne(alarmOne.getStoreOrderId()); // 주문 ID(storeOrder ID)
			
			Element storeElement = root.addElement("SEQ");
			storeElement.addAttribute("orderSeq", storeOrderOne.getOrderSeq()); //주문 번호
			storeElement.addAttribute("cookAlarmId", String.valueOf(alarmOne.getId())); //알림 ID 번호
			
			logger.info("orderSeq >> [{}]", storeOrderOne.getOrderSeq());
			logger.info("alarmOne.getId() >> [{}]", alarmOne.getId());
			
		}
		
		logger.info("/cookComListRead >> root.toString() >> [{}]", root.toString());
		logger.info("DID 완료 목록 내려주기 . XML 형식  끝");
		
		return document;
	}
	
	//주방용 패드 
	@Override
	public boolean alarmUpdate(String cookId, String storeId, String storeOrderId, ArrayList<Object> orderIDList, String complteYn, HttpSession session, Locale locale) {
		logger.info("alarmUpdate >>> cookId [{}]", cookId);
		logger.info("alarmUpdate >>> storeId [{}]", storeId);
		logger.info("alarmUpdate >>> storeOrderId [{}]", storeOrderId);
		
		boolean success = false;
		// 1. orderIDList로 넘어온 id 값을 가지고 StoreOrderList 테이블의 알림(orderMenuNotice)을 Y로 변경
		// 2. 해당 주문건의 StoreOrderList(메뉴List)를 가져와서 알림 Table에 넣어준다. 
		// 3. 알림 TABLE 을 생성 하며 해당 테이블에는 STORE_ORDERLIST_ID, storeid, deviceid, 알림완료여부
		// 4. 알림을 위한 해당 매장에 있는 DID 의  FCM List를 가져와서 호출 
		try {
			// 1. orderIDList로 넘어온 id 값을 가지고 StoreOrderList 테이블의 알림(orderMenuNotice)을 Y로 변경
			StoreOrder storeOrderOne = storeOrderDao.getOrderOne(Util.parseInt(storeOrderId));
			
			logger.info("orderIDList >> " + orderIDList);
			logger.info("alarmUpdate >>> storeOrderOne.getOrderNumber() [{}]", storeOrderOne.getOrderNumber());
			logger.info("alarmUpdate >>> storeOrderOne.getOrderSeq() [{}]", storeOrderOne.getOrderSeq());
			
			List<StoreOrderList> orderList = new ArrayList<StoreOrderList>();
			// 해당 주문의 메뉴를 가져온다.
			List<StoreOrderList> resList = storeOrderDao.getOrderList(storeOrderOne.getOrderNumber());
			String allMenu = "";	// 알림톡 사용 - 주문메뉴
			String finMenu = "";		// 알림톡 사용 - 완료메뉴
			//TODO:  y 
			logger.info(complteYn);
			for(StoreOrderList storeOrderListOne : resList){
				boolean noticeChk = true;
				String listId = String.valueOf(storeOrderListOne.getId());
				
//				if("Y".equals(storeOrderListOne.getOrderMenuNotice())) {
//					continue;
//				}
//				
//				if(complteYn.equals("Y")) {
//					logger.info(storeOrderListOne.getOrderMenuNotice());
//					storeOrderListOne.setOrderMenuNotice("Y");
//					storeOrderListOne.setWhoLastUpdateDate(new Date());
//					continue;
//				}
				
				// 기존에 한번 newalarm으로 울렸을 경우 Y로 변경 하여 완료 로 만들어준다.
				if("O".equals(storeOrderListOne.getOrderMenuNotice())){
//					storeOrderListOne.setOrderMenuNotice("Y");
					storeOrderListOne.setWhoLastUpdateDate(new Date());
					noticeChk = false;
				}
				
				// 화면에서 선택한 메뉴와 DB에서 조회된 메뉴 ID가 같을 경우 OrderMenuNotice(알림)을 Y로 변경
				// 2019.04.23 기존의 O로 Noti 한내용을 Y로 변경했을 경우 해당 내용은 넘어간다. 
				// 2023.6.8 주문 완료처리된 메뉴는 다시 O으로 되돌리지 않는다.
				if(noticeChk){
					for(Object stbObj : orderIDList) {
						String stbObjSt = (String) stbObj;
						if( stbObjSt.equals(listId) ){
							storeOrderListOne.setOrderMenuNotice("O");
							storeOrderListOne.setWhoLastUpdateDate(new Date());
						}
					}
				}
				// 알림톡에서 사용될 주문 메뉴
				if("".equals(allMenu)){
					allMenu = storeOrderListOne.getOrderMenuName();	
				}else{
					allMenu += (","+storeOrderListOne.getOrderMenuName());	
				}
				// 기존에 한번 newalarm으로 울렸을 경우 Y로 변경 하여 완료 로 만들어준다.
				if(!"N".equals(storeOrderListOne.getOrderMenuNotice())){
					if("".equals(finMenu)){
						finMenu = storeOrderListOne.getOrderMenuName();	
					}else{
						finMenu += (","+storeOrderListOne.getOrderMenuName());	
					}
				}
				
				orderList.add(storeOrderListOne);
			}
			
			storeOrderDao.saveOrderList(orderList);
			
			List<StoreCookAlarm> cookAlarm = new ArrayList<StoreCookAlarm>();
			
			logger.info("alarmUpdate >>> 테이블에 메뉴들의 알림 완료 를 N에서 Y로 변경하는 작업이 끝났습니다.");
			logger.info("alarmUpdate >>> FCM 보내기 전 명령에 대한 작업에 들어 갑니다. *FCM 토큰 여기서 가져와서 사용 ");
			//site를 조회 하여 STB 그룹을 사용하는지 확인
			Store store = storeService.getStore(Util.parseInt(storeId));
			if(store != null){
        		List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
    			
    			logger.info("alarmUpdate>>  stbGroupStbs.size()>>> [{}]", deviceList.size());
    			
        		for (Device device : deviceList) {
        			if (device.getDeviceType().equals("N")) {	// 알s리미일 때
						StoreCookAlarm storeCookAlarm = new StoreCookAlarm(Util.parseInt(storeId), Util.parseInt(storeOrderId), storeOrderOne.getOrderNumber(), 
								device.getId(), device.getUkid(), session);
						
						storeCookAlarm.setOrderAlarmGubun("S");//대기 상태
    					if("Y".equals(complteYn)){
    						storeCookAlarm.setOrderAlarmGubun("C");// 완료상태
    					}
						cookAlarm.add(storeCookAlarm);
    					// 2019.04.11 DID에 알림 / 완료 되었을때도 seq 정보를 보내주어야 해서 TASK 작업 추가
    					// StoreStay.bbmc : 대기 목록을 가져가도록 한다.
    					// StoreComplete.bbmc : 완료 목록을 가져가도록 한다.
    					String cookMenuTask = "StoreStay.bbmc";
    					if("Y".equals(complteYn)){
    						cookMenuTask = "StoreComplete.bbmc";
    					}
    					
    					logger.info("cookMenuTask >>> [{}]", cookMenuTask);
    					
    					StoreCookTask storeCookTask = new StoreCookTask(store.getSite().getId(), Util.parseInt(storeId), device.getId(), device.getUkid(), cookMenuTask, "N", new Date(), Util.setMaxTimeOfDate(new Date()), session);
    					
    					storeCookTaskDao.saveOrUpdate(storeCookTask);
    					
						if(!Util.isNotValid(device.getFcmToken())){
							String token = URLDecoder.decode(device.getFcmToken(), "UTF-8");
							sendToTokenDid(token, cookMenuTask + "["+storeCookTask.getId()+"]", storeOrderOne.getOrderSeq());
						}	    					
        			} else if (device.getDeviceType().equals("D")) {	// 주방용패드일 때
        				if(!Util.isNotValid(device.getFcmToken())){
	    					String token = URLDecoder.decode(device.getFcmToken(), "UTF-8");
	    					sendToTokenCook(token, "COOK", storeOrderOne.getOrderSeq());
        				}
        			}
        		}
			}
			if(cookAlarm.size() > 0){
				storeCookDao.saveAlarm(cookAlarm);
			}
			
			success = true;
			
			//2019.04.11 
			//모든 메뉴에 알림을 하였더라도 완료를 하지 않는다.(로직 제외) - 김영종 팀장님
			//완료가 되는건 주문 완료를 했을때만 가능
			if("Y".equals(complteYn)){
				// 결제가 완료 되었을 때 저장을 진행 한다.
				// storeCook 에서 주문 목록 완료를 Y로 변경 해 준다. 
				StoreOrderCook orderCook = storeCookDao.getStoreCookbyOne(Util.parseInt(cookId)) ;
				orderCook.setOrderMenuComplete("Y");
				orderCook.touchWho(session);
				
				storeCookDao.saveOrUpdate(orderCook);
				// STORE ORDER LIST Y 작업 추가
				
				
				
			}else{
				// 1.알림톡 허용이 되어 있지 않을 경우   2. 전화 번호가 없을 경우   : 알림톡 전송 제외
				logger.info("store.getStoreName() [{}] / store.isAlimTalkAllowed() [{}]", store.getStoreName(), store.isAlimTalkAllowed());
				logger.info("store.getStoreName() [{}] / storeOrderOne.getTelNumber() [{}]", store.getStoreName(), storeOrderOne.getTelNumber());
				if(store.isAlimTalkAllowed() && !Util.isNotValid(storeOrderOne.getTelNumber())){
					
					String senderKey = msgMgr.message("alimTalk.senderKey", locale);
					String tmplCd = msgMgr.message("alimTalk.tmplCd", locale);
					String subject = msgMgr.message("alimTalk.subject", locale);
					String msg = msgMgr.message("alimTalk.msg", locale);
					msg = msg.replace("{0}", store.getStoreName());
					msg = msg.replace("{1}", storeOrderOne.getOrderSeq());
					msg = msg.replace("{2}", allMenu);
					msg = msg.replace("{3}", finMenu);
					msg = msg.replace("{4}", store.getPhone());
					String smsmsg = msgMgr.message("alimTalk.smsmsg", locale);
					smsmsg = smsmsg.replace("{0}", store.getStoreName());
					smsmsg = smsmsg.replace("{1}", storeOrderOne.getOrderSeq());
					smsmsg = smsmsg.replace("{2}", finMenu);
					
					logger.info("알림톡  >>> store.getStoreName() [{}], store.getShortName() [{}]", store.getStoreName(), store.getShortName());
					logger.info("알림톡  >>> store.getPhone() [{}], storeOrderOne.getOrderSeq() [{}]", store.getPhone(), storeOrderOne.getOrderSeq());
					logger.info("알림톡  >>> allMenu 주문메뉴 [{}], finMenu 완료메뉴 [{}]", allMenu, finMenu);
					logger.info("알림톡  >>> tel 전화번호. [{}], senderKey [{}]", storeOrderOne.getTelNumber(), senderKey);
					logger.info("알림톡  >>> tmplCd [{}], subject [{}]", tmplCd, subject);
					logger.info("알림톡  >>> msg [{}], smsmsg [{}]", msg, smsmsg);
					
					StoreAlimTalk alimTalk = new StoreAlimTalk(store.getShortName(), store.getStoreName(), store.getPhone(), storeOrderOne.getOrderSeq(), allMenu, 
							finMenu, storeOrderOne.getTelNumber(), senderKey, tmplCd, subject, msg, smsmsg);
					
//					alimTalkService.save(alimTalk);
					PayUtil.testServiceApi(alimTalk);
				}
			}
		}catch (Exception e) {
        	logger.error("alarmUpdate ERROR", e);
        	throw new ServerOperationForbiddenException("OperationError");
		}
		
		return success; 
	}

	@Override
	public int getStayCntAPI(int storeId, String deviceId) {
		
		Store store = storeService.getStore(storeId);
		Device device = devService.getEffDeviceByUkid(deviceId);
		
		if (store != null && device != null && device.getStore().getId() == store.getId()) {
			return getStayCntRead(storeId);
		} else {
			return 9999;
		}
	}
	
	@Override
	public int getStayCntMobile(int storeId) {
		
		Store store = storeService.getStore(storeId);
		if (store != null) {
			List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
			for(Device device : deviceList) {
				if (device.getDeviceType().equals("D")) {		// 주방용패드일 경우
					return getStayCntRead(storeId);
				}
			}
		}
		
		return 9999;
	}
	
	@Override
	public int getStayCntRead(int storeId) {
		return storeCookDao.getStayCntRead(storeId, "Y"); //Y(완료)가 아닌 모든 것을 가져온다.;
	}
	
	@Override
	public Document getTaskByStoreIdDeviceId(int storeId, String deviceId, Document document) {		
		Store store = storeService.getStore(storeId);
		if(store == null){
        	logger.error("getMonListByStoreId", "매장을 조회 할수 없습니다. ");
        	throw new ServerOperationForbiddenException("매장을 조회 할수 없습니다. ");
		}
		
		logger.info("/storedidinfo >> getTaskByStoreIdDeviceId >>> 명령어 조회  시작");
		
		List<StoreCookTask> resTaskList = storeCookTaskDao.getTaskListByStoreIdDeviceId(storeId, deviceId, "N"); //DID 전송에 대한 Task 내려주기
		
		logger.info("/storedidinfo >> getTaskByStoreIdDeviceId >>> 명령어 조회  종료");
		
		Date now = new Date();
		Element root = document.addElement("PayCast")
				.addAttribute("version", "1")
				.addAttribute("generated", new Date().toString());
		
		
		Element commandsElement = root.addElement("commands");
		if(resTaskList.size() > 0){
			for(StoreCookTask storeCookTask : resTaskList){
				if (now.after(storeCookTask.getDestDate()) && now.before(storeCookTask.getCancelDate())
						&& storeCookTask.getStatus().equals("N")){
					Element commandElement = commandsElement.addElement("command");
					commandElement.addAttribute("rcCommandId", Integer.toString(storeCookTask.getId()) );
					commandElement.addAttribute("command", storeCookTask.getCommand() );
					
					logger.info("/storedidinfo >> rcCommandId >>> [{}]",storeCookTask.getId());
					logger.info("/storedidinfo >> command >>> [{}]",storeCookTask.getCommand());
					
					if("StoreWaitPeople.bbmc".equals(storeCookTask.getCommand().trim()) || "StoreComplete.bbmc".equals(storeCookTask.getCommand().trim())){
						int stayCnt = getStayCntAPI(storeId, deviceId);
						Element waitElement = commandElement.addElement("wait");
						waitElement.addAttribute("waiting", String.valueOf(stayCnt));
						logger.info("/storedidinfo >> storeCookTask.getCommand() >>> [{}], stayCnt >>>[{}]",storeCookTask.getCommand().trim(), stayCnt);
					}
				}
			}
		}
		
		return document;
	}

	@Override
	public StoreCookTask getStoreCookTask(int taskId) {
		return storeCookTaskDao.getStoreCookTask(taskId);
	}
	
	//
	@Override
	public void saveOrUpdate(StoreCookTask storeCookTask) {
		storeCookTaskDao.saveOrUpdate(storeCookTask);
	}
	
	
	@Override
	public StoreCookAlarm getDplyStoreCookAlarm(int cookAlarmId) {
		return storeCookDao.getDplyStoreCookAlarm(cookAlarmId);
	}

	@Override
	public void saveOrUpdate(StoreCookAlarm storeCookAlarm) {
		storeCookDao.saveOrUpdate(storeCookAlarm);
	}

	@Override
	public void comCancelUpdate(int siteId, String cookId, String storeId, HttpSession session) {
		boolean success = false;
		
		try{
			StoreOrderCook orderCook = storeCookDao.getStoreCookbyOne(Util.parseInt(cookId)) ;
			String orderNum = orderCook.getWhoCreatedBy();
			List<StoreOrderList> orderList = storeOrderDao.getOrderList(orderNum);
			Date now = new Date();
			for (StoreOrderList list : orderList) {
//				list.setOrderMenuNotice("N");
				list.setWhoLastUpdateDate(now);
			}
			storeOrderDao.saveOrderList(orderList);
			orderCook.setOrderMenuComplete("N");
			orderCook.touchWho(session);
			storeCookDao.saveOrUpdate(orderCook);
			
			
			
			StoreOrder storeOrderOne = storeOrderDao.getOrderOne(orderCook.getStoreOrderId());
//			List<StoreCookAlarm> cookAlarm = new ArrayList<StoreCookAlarm>();
			
			logger.info("comCancelUpdate >>> storeOrderOne.getOrderSeq() [{}]", storeOrderOne.getOrderSeq());
			logger.info("comCancelUpdate >>> 테이블에 메뉴들의 알림 완료 를 N에서 Y로 변경하는 작업이 끝났습니다.");
			logger.info("comCancelUpdate >>> FCM 보내기 전 명령에 대한 작업에 들어 갑니다. *FCM 토큰 여기서 가져와서 사용 ");
			// 2. FCM이 전송되는 deviceID를 가져와서 알림 테이블에 저장 한다.
			// FCM 을 보낸다. 
			FireMessage fcm = new FireMessage();
			JSONArray mobileDidTokens = new JSONArray(); // 전송할 fcm 토큰 LIST
			JSONArray mobileCookTokens = new JSONArray(); // 전송할 fcm 토큰 LIST
	//		String currentSiteId = (String)session.getAttribute("currentSiteId");
			Store store = storeService.getStore(Integer.parseInt(storeId));
			Site site = store.getSite();
			int siteId2 = site.getId();
			logger.info("comCancelUpdate >>> siteId [{}]", siteId2);
			//site를 조회 하여 STB 그룹을 사용하는지 확인
//			Site site = siteService.getSite(siteId);
			if(site != null){
				store = storeService.getStore(Util.parseInt(storeId));
				if (store != null) {
	        		List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
	        		
	        		for (Device device : deviceList) {
	    				logger.info("comCancelUpdate>>  device.getDeviceType()>>> [{}]", device.getDeviceType());
	    				
	        			if (device.getDeviceType().equals("N")) {	// 알리미일 때
//    						StoreCookAlarm storeCookAlarm = new StoreCookAlarm(Util.parseInt(storeId), storeOrderOne.getId(), storeOrderOne.getOrderNumber(), 
//							stbGroupStb.getStb().getId(), stbGroupStb.getStb().getDeviceID(), session);
//					
//    						storeCookAlarm.setOrderAlarmGubun("S");//대기 상태
//    						cookAlarm.add(storeCookAlarm);
	        				
							logger.info("device.getDeviceType() >>> [{}]", device.getDeviceType());
					
//	    					mobileDidTokens.add(token);
					
							String cookMenuTask = "StoreWaitPeople.bbmc";
							
							StoreCookTask storeCookTask = new StoreCookTask(siteId, Util.parseInt(storeId), device.getId(), device.getUkid(), cookMenuTask, "N", new Date(), Util.setMaxTimeOfDate(new Date()), session);
							storeCookTaskDao.saveOrUpdate(storeCookTask);
							
							if(!Util.isNotValid(device.getFcmToken())){
								String token = URLDecoder.decode(device.getFcmToken(), "UTF-8");
								logger.info("token >>> [{}]", token);
								logger.info("[임시] comCancelUpdate >>> [{}]의 FCM 보내는 작업 시작 ", storeOrderOne.getOrderSeq());				
								logger.info("[임시] comCancelUpdate >>> [{}]의 storeCookTask.getId() [{}] ", storeCookTask.getId());				
								sendToTokenDid(token, cookMenuTask + "["+storeCookTask.getId()+"]", storeOrderOne.getOrderSeq());
							}
	        			} else if (device.getDeviceType().equals("D")) {	// 주방용패드일 때
							if(!Util.isNotValid(device.getFcmToken())){
		    					String token = URLDecoder.decode(device.getFcmToken(), "UTF-8");
	//	    					mobileCookTokens.add(token);
		    					
		    					sendToTokenCook(token, "COOK", storeOrderOne.getOrderSeq());
							}
	        			}
	        		}
				}

				success = true;
			}
		}catch (Exception e) {
			logger.error("comCancelUpdate", e);
        	logger.error("comCancelUpdate", "완료에서 대기로의 변경이 취소되었습니다.");
        	throw new ServerOperationForbiddenException("대기로 변환이 불가 합니다.");
		}
	}

	// 임시 작업임 삭제 필요
	public String sendToTokenDid(String token, String command, String OrderSeq) throws Exception {
		String serverKey = Util.getFileProperty("didfcm.authKey");
		logger.info("[임시] sendToTokenDid > OrderSeq [{}]", OrderSeq);
		logger.info("[임시] sendToTokenDid > serverKey [{}]", serverKey);
		logger.info("[임시] sendToTokenDid > token [{}]", token);
		logger.info("[임시] sendToTokenDid > command [{}]", command);
		
		JSONObject root = new JSONObject();
	    JSONObject data = new JSONObject();
	    data.put("title", command+"["+OrderSeq+"]");
	    data.put("sound","default");
	    data.put("body", "");
	    root.put("notification", data);
	    root.put("to", token);
	    
	    return sendPushNotification(false, serverKey, root, OrderSeq);
	}
	// 임시 작업임 삭제 필요
	public String sendToTokenCook(String token, String command, String OrderSeq) throws Exception {
		String serverKey = Util.getFileProperty("cookfcm.authKey");
		logger.info("[임시] sendToTokenCook > OrderSeq [{}]", OrderSeq);
		logger.info("[임시] sendToTokenCook > serverKey [{}]", serverKey);
		logger.info("[임시] sendToTokenCook > token [{}]", token);
		logger.info("[임시] sendToTokenCook > command [{}]", command);
		
		JSONObject root = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("title", command + OrderSeq);
		data.put("sound","default");
		data.put("body", "");
		root.put("notification", data);
		root.put("to", token);
		
		return sendPushNotification(false, serverKey, root, OrderSeq);
	}
    private String sendPushNotification(boolean toTopic, String serverKey, JSONObject root, String OrderSeq) throws Exception {
    	
    	String apiUrlFcm = Util.getFileProperty("url.sendFcmNotif");
    	
        URL url = new URL(apiUrlFcm);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "key=" + serverKey);

        try {
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            logger.info("[임시][{}] root.toString() [{}]", OrderSeq,  root.toString());
           
            wr.write(root.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
            String result = builder.toString();

            logger.info("[임시] [{}] result [{}]", OrderSeq, result);
            JSONParser resultParser = new JSONParser();
            JSONObject resultObject = (JSONObject)resultParser.parse(result);
            long success = (long)resultObject.get("success");
            logger.info("[임시] [{}] success [{}]", OrderSeq, success);
            
            if (success > 0) {
                return "SUCCESS";
            }else{
            	JSONArray jsonArray1 = (JSONArray)resultObject.get("results");
            	for(int i=0; i<jsonArray1.size(); i++){
            		JSONObject objectInArray = (JSONObject) jsonArray1.get(i);
            		String errorMsg = (String)objectInArray.get("error");
            		logger.info("[임시] [{}] FCM fail success [{}]", OrderSeq, errorMsg);
            	}
            	logger.info("[임시] [{}] FCM 전송메시지 fail >> [{}]",  OrderSeq, root.toString());
            }

        	logger.info("[임시] [{}] FCM 모두 종료 >> [{}]", OrderSeq, root.toString());
            
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
           return e.getMessage();
        }
    }
}
