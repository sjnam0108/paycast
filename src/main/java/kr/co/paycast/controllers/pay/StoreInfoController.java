package kr.co.paycast.controllers.pay;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.CustomComparator;
import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.calc.service.CalculateService;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.SiteRegion;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreEtc;
import kr.co.paycast.models.pay.StoreOpt;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.self.service.SelfService;
import kr.co.paycast.models.store.StoreCookTask;
import kr.co.paycast.models.store.StoreOrderCook;
import kr.co.paycast.models.store.StoreOrderList;
import kr.co.paycast.models.store.dao.StoreCookDao;
import kr.co.paycast.models.store.dao.StoreCookTaskDao;
import kr.co.paycast.models.store.dao.StoreOrderDao;
import kr.co.paycast.models.store.service.StoreCookService;
import kr.co.paycast.models.store.service.StoreOrderService;
import kr.co.paycast.utils.FireMessage;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.DropDownListItem;
import kr.co.paycast.viewmodels.pay.StoreInfoItem;
import kr.co.paycast.viewmodels.pay.StoreTime;
import kr.co.paycast.viewmodels.store.CookJsonItem;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 상점 관리 - 상점 정보 컨트롤러
 */
@Controller("pay-store-info-controller")
@RequestMapping(value="/pay/storeinfo")
public class StoreInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreInfoController.class);

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;
    
    @Autowired 
    private SiteService siteService;
    
    @Autowired 
    private StoreService storeService;
    
    @Autowired
    private StoreCookDao storeCookDao;
     
    @Autowired
    private CalculateService calcService;
    
    @Autowired
    private SelfService selfService;
    
    @Autowired
    private StoreOrderService storeOrderService;
    
    @Autowired
    private StoreOrderDao storeOrderDao;
    
    @Autowired 
    private DeviceService devService;
    
    @Autowired
	private StoreCookTaskDao storeCookTaskDao;
    
	/**
	 * 상점 관리 - 상점 정보 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);
    	
    	solMsgMgr.checkStoreSelectionMessage(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "storeinfo.title"),
					
					new Message("tip_cardSlip", "storeinfo.cardSlip"),
					new Message("tip_cardMobileOrder", "storeinfo.cardMobileOrder"),
					new Message("desc_cardSlip", "storeinfo.msg.cardSlipDesc"),
					new Message("desc_cardMobileOrder", "storeinfo.msg.cardMobileOrderDesc"),

					new Message("label_bizName", "storeinfo.bizName"),
					new Message("label_bizRep", "storeinfo.bizRep"),
					new Message("label_bizNum", "storeinfo.bizNum"),
					new Message("label_phone", "storeinfo.phone"),
					new Message("label_local", "storeinfo.local"),
					new Message("label_address", "storeinfo.address"),
					new Message("label_openHoures", "storeinfo.openHoures"),
					
					new Message("labal_startTime", "storeinfo.startTime"),
					new Message("labal_endTime", "storeinfo.endTime"),
					new Message("labal_uptateOpenTime", "storeinfo.uptateOpenTime"),
					
					new Message("labal_storeOpenHoures", "storeinfo.storeOpenHoures"),
					new Message("labal_open24Houres", "storeinfo.open24Houres"),
					new Message("labal_nextDayClose", "storeinfo.nextDayClose"),
					new Message("labal_orderable", "storeinfo.orderable"),
					new Message("labal_set", "storeinfo.set"),
					new Message("labal_always", "storeinfo.always"),
					new Message("labal_firstMin", "storeinfo.firstMin"),
					new Message("labal_firstSetMin", "storeinfo.firstSetMin"),
					new Message("labal_secondMin", "storeinfo.secondMin"),
					new Message("labal_secondSetMin", "storeinfo.secondSetMin"),
					new Message("labal_thirdMin", "storeinfo.thirdMin"),
					new Message("labal_thirdSetMin", "storeinfo.thirdSetMin"),
					new Message("labal_directTime", "storeinfo.directTime"),
					new Message("labal_noOrder", "storeinfo.noOrder"),
					new Message("labal_appointment", "storeinfo.appointment"),
					new Message("labal_minute", "storeinfo.minute"),
					new Message("labal_time", "storeinfo.time"),
					
					new Message("msg_storeOpenHoures", "storeinfo.msg.storeOpenHoures"),
					new Message("msg_orderable", "storeinfo.msg.orderable"),
					new Message("msg_appointment", "storeinfo.msg.appointment"),
					new Message("msg_afterSelect", "storeinfo.msg.afterSelect"),
					
					new Message("empty_startTime", "storeinfo.msg.empty_startTime"),
					new Message("empty_endTime", "storeinfo.msg.empty_endTime"),
					new Message("empty_dayTime", "storeinfo.msg.empty_dayTime"),

					new Message("cmd_refresh", "storeinfo.refresh"),
					
					new Message("msg_updateComplete", "storeinfo.msg.updateComplete"),
					new Message("msg_refresh", "storeinfo.msg.refresh"),
    			});

    	// 상점 개요 정보
    	SolUtil.addStoreOverviewInfo(model, locale, session, request);
    	
    	// 지역 정보 모델
    	String initRegionCode = "-1";
    	List<SiteRegion> siteRegions = storeService.getSiteRegionDefaultValueListBySiteId(Util.getSessionSiteId(session));
    	if (siteRegions.size() > 0) {
    		initRegionCode = siteRegions.get(0).getRegion().getRegionCode();
    	}
    	model.addAttribute("initRegionCode", initRegionCode);
    	model.addAttribute("Regions", getRegionDropDownListBySiteId(Util.getSessionSiteId(session)));
    	//-
    	
    	// 스위치 초기값 지정을 위한 모델 지정
    	Store store = storeService.getStore(getStoreId(session));
    	
    	model.addAttribute("openType", store != null && store.getOpenType().equals("O"));
    	model.addAttribute("kioskOrderEnabled", store != null && store.isKioskOrderEnabled());
    	model.addAttribute("mobileOrderEnabled", store != null && store.isMobileOrderEnabled());
    	//-
    	
    	// 120분 = 2시간
    	List<StoreTime> timeList = new ArrayList<StoreTime>();
    	for(int i=5; i <= 120; i+=5 ){
    		String total =  "+" + i + "분";
    		StoreTime timeOne = new StoreTime(i, total, false);
    		timeList.add(timeOne);
    	}
    	model.addAttribute("timeList", timeList);
    	
        return "pay/storeinfo";
    }

	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody StoreInfoItem read(Locale locale, HttpSession session) {
		
		Store store = storeService.getStore(getStoreId(session));
		
		return store == null ? null : new StoreInfoItem(store);
    }
	
	/**
	 * 변경 액션
	 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody StoreInfoItem update(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
    	
    	StoreInfoItem item = null;
    	try{
    		
    		item = storeService.storeInfoUpdate(model, locale, session, "adm");
    	} catch (Exception e) {
    		logger.error("update", e);
    		
        	throw new ServerOperationForbiddenException("SaveError");
		}
    	
    	return item;
    }
	
	/**
	 * 상태 변경 액션
	 */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public @ResponseBody String updateStatus(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
    	
    	String oderPossiblCheck = "";
    	
    	Store target = storeService.getStore((Integer)model.get("id"));
    	String settingGroup = (String)model.get("settingGroup");
    	
    	if (target != null) {
        	StoreEtc storeEtc = target.getStoreEtc();
            if (storeEtc == null) {
            	storeEtc = new StoreEtc(target, session);
            }
    		
    		String beforeOpen = target.getOpenType();
    		String afterOpen = "C";
    		boolean beforeEnable = target.isKioskOrderEnabled();
    		boolean afterEnable = false;
    		boolean custom = (boolean)model.get("custom");
    		
    		target.setOpenType(Util.parseString((String)model.get("openType")));
    		target.setKioskOrderEnabled(Util.parseBoolean((String)model.get("kioskOrderEnabled")));
    		target.setMobileOrderEnabled(Util.parseBoolean((String)model.get("mobileOrderEnabled")));
    		target.setOpenHour_24((boolean)model.get("hour24"));
    		if(((String)model.get("mobileOrderEnabled")).equals("Y")){
    			if("O".equals(settingGroup)){
    				target.getStoreEtc().setOder_setting_Time(0);
        			target.getStoreEtc().setOderPossiblCheck("O");
        		}else if("C".equals(settingGroup)){
        			target.getStoreEtc().setOder_setting_Time(0);
        			target.getStoreEtc().setOderPossiblCheck("O");
        		}else{
        			target.getStoreEtc().setOderPossiblCheck("P");
        		}	
    		}else{
    			target.getStoreEtc().setOder_setting_Time(0);
    			target.getStoreEtc().setOderPossiblCheck("C");
    		}
    		
    		try {
        		target.touchWho(session);
    			storeService.saveOrUpdate(target);
    			
    			storeEtc.touchWho(session);
    			storeService.saveOrUpdate(storeEtc);
    			
    			logger.info("custom [{}]", custom);
        		StoreOpt storeOpt = target.getStoreOpt();
        		storeOpt.setCustomOperation(custom);
        		storeOpt.touchWho(session);
        		storeService.saveOrUpdate(storeOpt);
    			
    			// 영업을 종료 하였을 경우 / 일별매출 /월별매출 / 메뉴통계 생성
    			if("C".equals(target.getOpenType())){
    				
    				boolean closeSucess = calcService.operEnd(String.valueOf(target.getId()), session);
	
    				if(closeSucess) {

    					closeTask(target.getId());
    				}
    			}
    			
    			// 키오스크 주문 가능 여부 관련하여 추가
    			// 이전 데이터와 저장한 데이터가 다를 경우 명령어 추가
    			afterEnable = target.isKioskOrderEnabled();
    			afterOpen = target.getOpenType();
    			if(!(beforeOpen.equals(afterOpen)) || (beforeEnable != afterEnable)){
    				selfService.setMonTask("KE", String.valueOf(target.getId()), session);
    			}else{
    				// "" : MyStoreInfoController 와 다르게 매장 정보를 변경할수 있기 때문에 변경되었을 경우 "StoreInfoChg.bbmc" 명령어를 주기 위해 
    				selfService.setMonTask("", String.valueOf(target.getId()), session);
    			}
    			
    			oderPossiblCheck = storeEtc.getOderPossiblCheck();
    		} catch (Exception e) {
        		logger.error("updateStatus", e);
            	throw new ServerOperationForbiddenException("SaveError");
    		}
    	}
    	
    	return "OK"+"-"+oderPossiblCheck;
    }
    
    
private void closeTask(int storeId) {
    	
    	List<StoreOrderCook> cook = storeCookDao.getStoreCookStayList(storeId, "Y"); //Y(완료)가 아닌 모든 것을 가져온다.
    	
    	logger.info(cook.toString());
    	
    	for(StoreOrderCook Item : cook) {
    		
    		Item.getWhoCreatedBy();
    		
    		String ordernum = Item.getWhoCreatedBy();
    		logger.info(ordernum);
    		Item.setOrderMenuComplete("Y");
    		storeCookDao.saveOrUpdate(Item);
    		List<StoreOrderList> resList = storeOrderService.getOrderListbyNumber(ordernum);
    		for(StoreOrderList orderList : resList) {
    			System.out.println(resList);
    			orderList.setOrderMenuNotice("Y");
    			storeOrderDao.saveOrUpdate(orderList);
    		}
    	}
    	    	
    	fcmTransmission(storeId);
    }
    
    
    
	/**
	 * 지역 목록 획득
	 */
    public List<DropDownListItem> getRegionDropDownListBySiteId(int siteId) {
		Site site = siteService.getSite(siteId);

		ArrayList<DropDownListItem> retList = new ArrayList<DropDownListItem>();
		
		if (site != null) {
			List<SiteRegion> siteRegions = storeService.getSiteRegionListBySiteId(site.getId());
			
			for(SiteRegion siteRegion : siteRegions) {
				retList.add(new DropDownListItem(siteRegion.getRegion().getRegionName(), 
						siteRegion.getRegion().getRegionCode()));
			}
		}

		Collections.sort(retList, CustomComparator.DropDownListItemTextComparator);
		
		return retList;
    }

	/**
	 * 상점 번호 획득
	 */
    private int getStoreId(HttpSession session) {
    	
    	int storeId = -1;
    	LoginUser loginUser = null;
    	if (session != null) {
    		loginUser = (LoginUser) session.getAttribute("loginUser");
    		if (loginUser != null) {
    			storeId = loginUser.getStoreId();
    		}
    	}
		
    	return storeId;
    }
    
	/**
	 * 상점 정보 새로고침 액션
	 */
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public @ResponseBody String refreshContent(Locale locale, HttpSession session) {
    	
    	try {
           	selfService.setMonTask("", String.valueOf(getStoreId(session)), session);
            
    	} catch (Exception e) {
    		logger.error("refreshContent", e);
    		throw new ServerOperationForbiddenException("OperationError");
    	}
    	
    	return "OK";
    }
    
    @SuppressWarnings("unchecked")
//	@Override
    public boolean fcmTransmission(int storeId) {
		boolean cookPadCheck = false;
		boolean didCheck = false;
		boolean success = false;
		//1. 매장 ID를 가지고 등록된 매장의 stbGroupId 값을 가져온다.
		//2. stbGroup에 매핑 되어 있는 stb 목록을 조회한다.
		//3. 조회된 stb들의 fcmToken들을 조회 하여 FCM을 전송한다.
		//2019.04.06 FCM 전송시  D(DID)관련 FCM 은 제외 한다.
		//4. C(주방용 패드)fcm 을 보낼때 주방용 패드 목록에 추가 하기 위해서 FCM 을 보낸다. 
		//5. 주방용 패드가 없을 경우 StoreOrderCook 에 저장 하지 않기 위해서 체크 한다. 
		
		logger.info("[결제 완료]FCM 보내기 자료 만들기 >>>");
		
		Store store = storeService.getStore(storeId);
		if (store != null) {
			FireMessage fcm = new FireMessage();
			JSONArray mobileTokens = new JSONArray();
			JSONArray mobileTokensDid = new JSONArray();
			JSONArray mobileTokensCook = new JSONArray();
			
			logger.info("[결제 완료]FCM 에 들어가는 TOKEN 조회 및 모델 구분으로 task 생성 >>> ");
			
			List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
			
			try{
				List<StoreCookTask> storeCookTaskList = new ArrayList<StoreCookTask>();
				
				if(deviceList.size() > 0){
					for(Device device : deviceList){
						String fcmToken = device.getFcmToken();
						if(fcmToken != null){
							fcmToken = URLDecoder.decode(fcmToken, "UTF-8");
							if (device.getDeviceType().equals("D")) {			// D: 주방용패드
								logger.info("[결제 완료]주방용 패드 TOKEN [{}]", fcmToken);
								
								mobileTokensCook.add(fcmToken);	
								cookPadCheck = true;
							} else if (device.getDeviceType().equals("N")) {	// N: 알리미
								// 2019.04.22 DID에 FCM 보내기
								// StoreStay.bbmc : 대기 목록을 가져가도록 한다.( 알리미에서 사용 ) 
								// StoreComplete.bbmc : 완료 목록을 가져가도록 한다.
								// StoreWaitPeople.bbmc : DID에서 대기번호를 조회 한다. 
								String cookMenuTask = "StoreWaitPeople.bbmc";
								
								logger.info("[결제 완료]알리미 명령어 추가 [{}]", cookMenuTask);
								
								StoreCookTask storeCookTask = new StoreCookTask();
								Date dateNew = new Date();
								storeCookTask.setSiteId(device.getStore().getSite().getId());
								storeCookTask.setStoreId(storeId);
								storeCookTask.setStbId(device.getId());
								storeCookTask.setDeviceID(device.getUkid());
								storeCookTask.setCommand(cookMenuTask);
								storeCookTask.setStatus("N");
								storeCookTask.setDestDate(dateNew);
								storeCookTask.setCancelDate(Util.setMaxTimeOfDate(dateNew));
								storeCookTask.setWhoCreatedBy(-1);
								storeCookTask.setWhoCreationDate(dateNew);
								storeCookTask.setWhoLastUpdatedBy(-1);
								storeCookTask.setWhoLastUpdateDate(dateNew);
								storeCookTask.setWhoLastUpdateLogin(-1);
	
								storeCookTaskList.add(storeCookTask);
								
								logger.info("[결제 완료]알리미 명령어 추가 완료 [{}]", cookMenuTask);
								
								mobileTokensDid.add(fcmToken);
								didCheck = true;
							} else {
								logger.info("[결제 완료]프린트 FCM 추가 [{}]", fcmToken);
								mobileTokens.add(fcmToken);	
							}
						}
					}
						
					success = true;
				}

				logger.info("[결제 완료]FCM 전송 시작");
				
				logger.info("[결제 완료]패드 있는지 확인 [{}]", cookPadCheck);
				
				logger.info("[결제 완료]mobileTokens.size() [{}]", mobileTokens.size());
				if(mobileTokens.size() > 0){
					
					fcm.sendToGroup(mobileTokens);
				}
				
				if(cookPadCheck){
//					storeCookService.saveOrUpdate(storeOrderCook);
					
					fcm.sendToGroupCook(mobileTokensCook);
				}
				
				
				logger.info("[결제 완료]알리미 있는지 확인 [{}]", didCheck);
				//did 대기 번호 조회를 위해서 FCM을 전송
				if(didCheck){
					// 2019.04.22 DID에 FCM 보내기 (프린트 및 DID 가 전부 들어 갈수 있음)
					// StoreStay.bbmc : 대기 목록을 가져가도록 한다.
					// StoreComplete.bbmc : 완료 목록을 가져가도록 한다.
					// StoreWaitPeople.bbmc : DID에서 대기번호를 조회 한다. 
					if(storeCookTaskList.size() > 0){
						for(StoreCookTask storeCookTask : storeCookTaskList){
							storeCookTaskDao.saveOrUpdate(storeCookTask);
						}
					}
					
					logger.info("[결제 완료]mobileTokensDid.size() [{}]", mobileTokensDid.size());
					if(mobileTokensDid.size() > 0){
						fcm.sendToGroupDid(mobileTokensDid);
					}
				}
				
				logger.info("[모바일 결제 완료]FCM 전송 종료");
				
			}catch(Exception e){
	        	logger.error("FCM ERROR", e);
	        	throw new ServerOperationForbiddenException("FCM을 전송할수 없습니다.");
			}
		}
		
		return success;
	}
    
}
