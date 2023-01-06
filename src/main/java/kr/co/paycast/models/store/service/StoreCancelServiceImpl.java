package kr.co.paycast.models.store.service;

import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.StoreCookAlarm;
import kr.co.paycast.models.store.StoreCookTask;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderCancel;
import kr.co.paycast.models.store.StoreOrderCook;
import kr.co.paycast.models.store.StoreOrderList;
import kr.co.paycast.models.store.StoreOrderPay;
import kr.co.paycast.models.store.StoreOrderVerification;
import kr.co.paycast.models.store.dao.StoreCancelDao;
import kr.co.paycast.models.store.dao.StoreCookDao;
import kr.co.paycast.models.store.dao.StoreCookTaskDao;
import kr.co.paycast.models.store.dao.StoreOrderDao;
import kr.co.paycast.utils.FireMessage;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.store.StoreCanCelView;

@Transactional
@Service("StoreCancelService")
public class StoreCancelServiceImpl implements StoreCancelService {
	private static final Logger logger = LoggerFactory.getLogger(StoreCancelServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
	private StoreCancelDao storeCancelDao;
    
    @Autowired
    private StoreOrderDao storeOrderDao;
    
    @Autowired
    private StorePayService storePayService;
    
    @Autowired
    private StoreCookDao storeCookDao;
    
    @Autowired
    private StoreCookTaskDao storeCookTaskDao;

    @Autowired 
    private DeviceService devService;

    @Autowired 
    private StoreService storeService;

    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public StoreOrderCancel get(int cancelId) {
		return storeCancelDao.get(cancelId);
	}
	
	//결제가 완료 되었을 때 저장을 진행  모바일 결제와 키오스크 결제 에서 확인 가능
	@Override
	public int save(StoreOrderVerification storeOrderVerification) {
		return storeCancelDao.save(storeOrderVerification);
	}

	@Override
	public void saveOrUpdate(StoreOrderVerification storeOrderVerification) {
		storeCancelDao.saveOrUpdate(storeOrderVerification);
	}

	@Override
	public StoreOrderVerification getStoreCancelOne(int orderCancelId) {
		return storeCancelDao.getStoreCancelOneDao(orderCancelId);
	}
	@Override
	public StoreOrderVerification getStoreCancelbystoreIdstoreOrderId(int storeId, int storeOrderId, int orderCancelId) {
		return storeCancelDao.getStoreCancelOne(storeId, storeOrderId, orderCancelId);
	}

	@Override
	public StoreCanCelView checkVerifiCodebyStoreIdVerifiCode(int storeId, String verifiCode, String deviceGubun) {
		Date now = new Date();
		DecimalFormat deChg = new DecimalFormat("###,###,###,###");
		SimpleDateFormat transF = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StoreCanCelView storeCanCelView = new StoreCanCelView();
		
		StoreOrderVerification storeOrderVerification = storeCancelDao.checkVerifiCode(storeId, verifiCode);
		if(storeOrderVerification != null && !"N".equals(storeOrderVerification.getStatus())){
			if (now.after(storeOrderVerification.getDestDate()) && now.before(storeOrderVerification.getCancelDate())){
				
				// 승인 번호가 맞을 경우 
				StoreOrder storeOrder = storeOrderDao.getOrderOne(storeOrderVerification.getStoreOrderId());
				// 스마일페이를 제외한 이지페이로 일단 취소 요청 
				if("M".equals(deviceGubun) ){
					deviceGubun = "ME";
				}
				if(deviceGubun.equals(storeOrder.getOrderDevice())){
					StoreOrderPay storeOrderPay = storePayService.getOrderPay(storeOrder.getOrderPayId());
					try{
						logger.info("DeviceGubun" + deviceGubun);
						storeCanCelView.setStoreOrderId(String.valueOf(storeOrderVerification.getStoreOrderId()));
						storeCanCelView.setVerificationId(String.valueOf(storeOrderVerification.getId()));
						storeCanCelView.setOrderSeq(storeOrder.getOrderSeq());
						storeCanCelView.setOrderDevice(storeOrder.getOrderDevice());
						// MS : 스마일페이 모바일  / ME : easypay
						//if("MS".equals( storeOrder.getOrderDevice() ) || "ME".equals( storeOrder.getOrderDevice() )){
						if("MS".equals( storeOrder.getOrderDevice() )){
							Calendar calendar = new GregorianCalendar(Locale.KOREA);
							int nYear = calendar.get(Calendar.YEAR);
							String nYearSt = String.valueOf(nYear);
							String authDate = nYearSt.substring(0, 2) + storeOrderPay.getPayAuthDate();	//결제 승인 일자
							Date test = transF.parse(authDate);
							storeCanCelView.setPayAuthDate(transFormat.format(test));	
						}else{
							Date test = transF.parse(storeOrderPay.getPayAuthDate());
							storeCanCelView.setPayAuthDate(transFormat.format(test));
						}
						storeCanCelView.setPayAuthCode(storeOrderPay.getPayAuthCode());
						storeCanCelView.setPayAmt(deChg.format(Util.parseInt(storeOrderPay.getPayAmt())));
						storeCanCelView.setPayAuthYN("Y");
					}catch(Exception e){
						logger.error("StoreSalesView Read", e);
						throw new ServerOperationForbiddenException("ReadError");
					}
					
					logger.info("주문 번호 : [{}]", storeOrder.getOrderSeq());// 주문번호
					logger.info("storeOrder.getOrderPayId() : [{}]", storeOrder.getOrderPayId());// 주문번호
					logger.info("결제일자 : [{}]", storeOrderPay.getPayAuthDate());// 승인일자(결제일자)
					logger.info("승인번호 : [{}]", storeOrderPay.getPayAuthCode());// 승인번호
					logger.info("결제 금액 : [{}]", storeOrderPay.getPayAmt());// 결제 금액
					
					//주문의 상태 변경
					storeOrder.setOrderCancelStatus("2"); // 고객이 취소 요청을 했을 경우 상태  : 2
					storeOrderDao.saveOrUpdate(storeOrder);
				}else{
					logger.info("mobile > verifiCode >> [{}] 해당내용은 모바일이아닌 다른기기에서 결제가 되었습니다. [{}]", verifiCode, "X");
		    		storeCanCelView.setPayAuthYN("X");
				}
        	}else{
        		logger.info("checkVerifiCode >> 사용 가능 시간이 지나 새로 발급 필요  [N] 이전 verifiCode[{}] , storeId = [{}]", verifiCode, storeId);
        		
        		storeCanCelView.setPayAuthYN("N");
        		// 사용 가능 시간이 지나는걸 확인 하고 없애도록 한다. 비밀번호를 없앤다.
    			StoreOrderVerification storeOrderVerification1 = storeCancelDao.getStoreCancelOneDao(storeOrderVerification.getId());
    			storeOrderVerification1.setCancelStoreAuth("");
    			storeOrderVerification1.setStatus("N"); //해당 코드 사용 불 가능으로 만들기
    			storeCancelDao.saveOrUpdate(storeOrderVerification1);
        	}
		}else{
			logger.info("mobile > verifiCode >> [{}] 승인번호를 알수 없습니다. [F]", verifiCode);
    		storeCanCelView.setPayAuthYN("F");
    	}
		
		return storeCanCelView;
	}

	@Override
	public StoreCanCelView immediateCanCel(StoreOrderVerification orderVerification, String verifiCode, String deviceGubun) {
		DecimalFormat deChg = new DecimalFormat("###,###,###,###");
		SimpleDateFormat transF = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StoreCanCelView storeCanCelView = new StoreCanCelView();
		
		StoreOrder storeOrder = storeOrderDao.getOrderOne(orderVerification.getStoreOrderId());
////		if("M".equals(deviceGubun) ){
//			deviceGubun = "ME";
//		}
		if(deviceGubun.equals(storeOrder.getOrderDevice())){
			StoreOrderPay storeOrderPay = storePayService.getOrderPay(storeOrder.getOrderPayId());
			try{
				storeCanCelView.setStoreOrderId(String.valueOf(orderVerification.getStoreOrderId()));
				storeCanCelView.setVerificationId(String.valueOf(orderVerification.getId()));
				storeCanCelView.setOrderSeq(storeOrder.getOrderSeq());
				storeCanCelView.setOrderDevice(storeOrder.getOrderDevice());
				// MS : 스마일페이 모바일  / ME : easypay
				if("MS".equals( storeOrder.getOrderDevice() )){
					Calendar calendar = new GregorianCalendar(Locale.KOREA);
					int nYear = calendar.get(Calendar.YEAR);
					String nYearSt = String.valueOf(nYear);
					String authDate = nYearSt.substring(0, 2) + storeOrderPay.getPayAuthDate();	//결제 승인 일자
					Date test = transF.parse(authDate);
					storeCanCelView.setPayAuthDate(transFormat.format(test));	
				}else{
					Date test = transF.parse(storeOrderPay.getPayAuthDate());
					storeCanCelView.setPayAuthDate(transFormat.format(test));
				}
				storeCanCelView.setPayAuthCode(storeOrderPay.getPayAuthCode());
				storeCanCelView.setPayAmt(deChg.format(Util.parseInt(storeOrderPay.getPayAmt())));
				storeCanCelView.setPayAuthYN("Y");
			}catch(Exception e){
				logger.error("StoreSalesView Read", e);
				throw new ServerOperationForbiddenException("ReadError");
			}
			
			//주문의 상태 변경
			storeOrder.setOrderCancelStatus("2"); // 고객이 취소 요청을 했을 경우 상태  : 2
			storeOrderDao.saveOrUpdate(storeOrder);
		}else{
			logger.info("mobile > verifiCode >> [{}] 해당내용은 모바일이아닌 다른기기에서 결제가 되었습니다. [{}]", verifiCode, "X");
    		storeCanCelView.setPayAuthYN("X");
		}
		
		return storeCanCelView;
	}
	
	@Override
	public void cancelSuccessSave(StoreOrderCancel storeOrderCancel, int storeId, String verifiCode) {
		
		logger.info("cancelSuccessSave >>> verifiCode[{}], storeId [{}]", verifiCode, storeId);
		
		StoreOrderVerification storeOrderVerification = storeCancelDao.checkVerifiCode(storeId, verifiCode);
		if(storeOrderVerification != null){
			int cancelId = storeCancelDao.saveCancel(storeOrderCancel);
			
			logger.info("cancelSuccessSave >>> cancelId[{}], storeId [{}]", cancelId, storeId);
			
			StoreOrderVerification storeOrderVerification1 = storeCancelDao.getStoreCancelOneDao(storeOrderVerification.getId());
			storeOrderVerification1.setCancelStoreAuth("");
			storeOrderVerification1.setStoreCancelId(cancelId);
			storeOrderVerification1.setStatus("N"); //해당 코드 사용 불 가능으로 만들기
			storeCancelDao.saveOrUpdate(storeOrderVerification1);
			
			StoreOrder storeOrder = storeOrderDao.getOrderOne(storeOrderVerification1.getStoreOrderId());
			//주문의 상태 변경
        	storeOrder.setOrderCancelStatus("Y"); // 취소가 완료 되었을 경우 상태 : Y
        	//주방용 프린트 및 주방용 패드에 대해서 취소를 조회 할수 있도록 변경
        	storeOrder.setOrderCancelPrint("1"); // 취소가 완료 되었을 경우 프린트 여부는 1로 변경
        	storeOrder.setOrderCancelPad("1"); // 취소가 완료 되었을 경우 패드 삭제 여부는 1로 변경
        	storeOrder.touchWho(storeOrder.getOrderNumber());
        	storeOrderDao.saveOrUpdate(storeOrder);
        	
        	Store store = storeService.getStore(storeOrder.getStoreId());
        	if (store != null) {
        		List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
        		for (Device device : deviceList) {
        			if (device.getDeviceType().equals("N")) {	// 알리미일 때
						StoreCookAlarm storeCookAlarm = new StoreCookAlarm(storeOrder.getStoreId(), storeOrder.getId(), storeOrder.getOrderNumber(), 
								 device.getId(), device.getUkid());
						
						storeCookAlarm.setOrderAlarmGubun("C");// 완료상태
						storeCookDao.saveOrUpdate(storeCookAlarm);
						
						String cookMenuTask = "StoreComplete.bbmc";
						StoreCookTask storeCookTask = new StoreCookTask(store.getSite().getId(), storeOrder.getStoreId(), device.getId(), device.getUkid(), cookMenuTask, "N", new Date(), Util.setMaxTimeOfDate(new Date()));
						storeCookTaskDao.saveOrUpdate(storeCookTask);
						
						cookMenuTask = "StoreWaitPeople.bbmc";
						StoreCookTask storeCookTask1 = new StoreCookTask(store.getSite().getId(), storeOrder.getStoreId(), device.getId(), device.getUkid(), cookMenuTask, "N", new Date(), Util.setMaxTimeOfDate(new Date()));
						storeCookTaskDao.saveOrUpdate(storeCookTask1);
        			}else if(device.getDeviceType().equals("D")){ // D: 주방용패드 
        				logger.info("cancelSuccessSave / D: 주방용패드  >> storeOrder.getId() [{}]",storeOrder.getId());
						// 2019.06.11 주문 취소일 경우 대기자 수가 줄어 들지 않는 문제가 있어서 먼저 complete를 해준다.  
						StoreOrderCook storeOrderCook = storeCookDao.getStoreCookbyStoreOrderId(storeOrder.getId());
						logger.info("cancelSuccessSave / D: 주방용패드  >> storeOrderCook!=null[{}]",(storeOrderCook!=null));
						if(storeOrderCook != null){
							storeOrderCook.setOrderMenuComplete("Y");
							storeOrderCook.touchWho(storeOrder.getOrderNumber());
							storeCookDao.saveOrUpdate(storeOrderCook);
						}
        			}
        		}
        	} else {
    			logger.info("Mobile >> 주문 취소 완료 시 DID 처리가 정상적이지 않습니다. cancelSuccessSave");
    			logger.info("storeId >> [{}]", storeId);
    			logger.info("storeOrderId >> [{}]", storeOrder.getId());
        	}
		}
		
		//취소가 완료가 되었을 경우 FCM 을 보낸다. 
		// 1. 주방용 패드가 없을 경우 주방용 프린트로 FCM을 전송하며
		// 2. 주방용 패드가 있을 경우 FCM을  패드로 전송한다.
		boolean success = fcmTransmission(storeId);
		
	}
	
	@Override
	public void cancelFailSave(StoreOrderCancel storeOrderCancel, int storeId, String verifiCode) {
		
		StoreOrderVerification storeOrderVerification = storeCancelDao.checkVerifiCode(storeId, verifiCode);
		if(storeOrderVerification != null){
			int cancelId = storeCancelDao.saveCancel(storeOrderCancel);
			
			StoreOrderVerification storeOrderVerification1 = storeCancelDao.getStoreCancelOneDao(storeOrderVerification.getId());
			storeOrderVerification1.setCancelStoreAuth("");
			storeOrderVerification1.setStoreCancelId(cancelId);
			storeOrderVerification1.setStatus("N");
			storeCancelDao.saveOrUpdate(storeOrderVerification1);
			
			StoreOrder storeOrder = storeOrderDao.getOrderOne(storeOrderVerification1.getStoreOrderId());
			//주문의 상태 변경
			//Y일 경우는 이미 취소 성공이 되었을 경우 이며 다시 그부분을 실패로 만들 필요가 없다.
			if(!"Y".equals(storeOrder.getOrderCancelStatus())){
				storeOrder.setOrderCancelStatus("F"); // 취소가 완료 되었을 경우 상태 : 취소 실패
				storeOrderDao.saveOrUpdate(storeOrder);
			}
		}
	}
	
	@Override
	public Document checkVerifiCodebyStoreIdVerifiCodeDeviceID(int storeId, String deviceId, String verifiCode, Document document, String deviceGubun) {
		
		Element root = document.addElement("PayCast")
				.addAttribute("version", "1.0")
				.addAttribute("generated", new Date().toString());
		
		Date now = new Date();
		StoreOrderVerification storeOrderVerification = storeCancelDao.checkVerifiCode(storeId, verifiCode);
		
		Element storeElement = root.addElement("verification");
		if(storeOrderVerification != null && !"N".equals(storeOrderVerification.getStatus())){
//			logger.info("storeOrderVerification.getStatus() 사용가능 상태 >>> [{}]", storeOrderVerification.getStatus());
//			logger.info("now.after(storeOrderVerification.getDestDate() >> [{}]", now.after(storeOrderVerification.getDestDate()));
//			logger.info("now.before(storeOrderVerification.getCancelDate()) >>> [{}]", now.before(storeOrderVerification.getCancelDate()));
			
			if(now.after(storeOrderVerification.getDestDate()) && now.before(storeOrderVerification.getCancelDate())){
				
				// 승인 번호가 맞을 경우 
				logger.info("storeOrderVerification.getStoreOrderId() >>> [{}]", storeOrderVerification.getStoreOrderId());
				StoreOrder storeOrder = storeOrderDao.getOrderOne(storeOrderVerification.getStoreOrderId());
				
				
				// 스마일페이를 제외하 이지페이로 일단 취소 요청 
				if(deviceGubun.equals("M") ){
					deviceGubun = "ME";
				}
//				logger.info("storeOrder.getOrderDevice() >>> [{}]", storeOrder.getOrderDevice());
//				logger.info("deviceGubun >>> [{}]", deviceGubun);
				if(deviceGubun.equals(storeOrder.getOrderDevice())){
					StoreOrderPay storeOrderPay = storePayService.getOrderPay(storeOrder.getOrderPayId());
					
//					logger.info("주문 번호 : [{}]", storeOrder.getOrderSeq());// 주문번호
//					logger.info("storeOrder.getOrderPayId() : [{}]", storeOrder.getOrderPayId());// 주문번호
//					logger.info("결제일자 : [{}]", storeOrderPay.getPayAuthDate());// 승인일자(결제일자)
//					logger.info("승인번호 : [{}]", storeOrderPay.getPayAuthCode());// 승인번호
//					logger.info("결제 금액 : [{}]", storeOrderPay.getPayAmt());// 결제 금액
					
					storeElement.addAttribute("verificationYN", "Y");
					storeElement.addAttribute("verifiMsg", "결제 내역 조회 성공");
					
					Element menuElement = storeElement.addElement("payInfo");
					menuElement.addAttribute("tid", storeOrderPay.getOrderTid());
					menuElement.addAttribute("mid", storeOrderPay.getPayMid());
					menuElement.addAttribute("totalindex", storeOrderPay.getPayAmt());
					menuElement.addAttribute("goodsAmt", storeOrderPay.getPayAmt());	
					menuElement.addAttribute("orderNumber", storeOrderPay.getOrderNumber());	
					menuElement.addAttribute("orderDate", storeOrderPay.getPayAuthDate());	
					menuElement.addAttribute("AuthCode", storeOrderPay.getPayAuthCode());	
					menuElement.addAttribute("fnCd", storeOrderPay.getPayFnCd());	
					menuElement.addAttribute("fnName", storeOrderPay.getPayFnName());	
					menuElement.addAttribute("fnCd1", storeOrderPay.getPayFnCd1());	
					menuElement.addAttribute("fnName1", storeOrderPay.getPayFnName1());
					menuElement.addAttribute("storeOrderId", String.valueOf(storeOrder.getId()));
					
					// DB에 저장되어 있는 메뉴 List
					List<StoreOrderList> resList = storeOrderDao.getOrderList(storeOrder.getOrderNumber());
					for(StoreOrderList storeOrderListOne : resList){
						Element orderMenuElement = menuElement.addElement("orderMenu");
						orderMenuElement.addAttribute("productID", String.valueOf(storeOrderListOne.getOrderMenuId()));
						orderMenuElement.addAttribute("productName", storeOrderListOne.getOrderMenuName());
						orderMenuElement.addAttribute("orderCount", String.valueOf(storeOrderListOne.getOrderMenuAmount()));
						orderMenuElement.addAttribute("orderPrice", String.valueOf(storeOrderListOne.getOrderMenuAmt()));
						orderMenuElement.addAttribute("orderMenuPacking", String.valueOf(storeOrderListOne.getOrderMenuPacking()));
						//필수 선택
						if(!Util.isNotValid(storeOrderListOne.getOrderSelectEss())){
							String[] menu1 = storeOrderListOne.getOrderSelectEss().split(",");
							for(int t=0; t < menu1.length; t++){
								if(!Util.isNotValid(menu1[t])){
									String[] menu2 = menu1[t].split("_");
									
									Element optMenuElement = orderMenuElement.addElement("optionMenu");
									optMenuElement.addAttribute("id", menu2[0]);
									optMenuElement.addAttribute("gubun", "0"); // 0 : 필수 선택 메뉴 / 1 : 추가 선택 메뉴
									if(menu2.length > 0){
										String[] menu3 = menu2[1].split("\\(");
										if(menu3.length > 0){
											String viewName = PayUtil.separationistName(menu3);
											String viewPrice = PayUtil.separationistPrice(menu3);
											
											Element optionElement = optMenuElement.addElement("option");
											optionElement.addAttribute("name", viewName);
											optionElement.addAttribute("price", viewPrice);
										}
									}
								}
							}
						}
						//추가 선택
						if(!Util.isNotValid(storeOrderListOne.getOrderSelectAdd())){
							Element optMenuElement = orderMenuElement.addElement("optionMenu");
							String[] menu1 = storeOrderListOne.getOrderSelectAdd().split(",");
							for(int t=0; t < menu1.length; t++){
								if(!Util.isNotValid(menu1[t])){
									String[] menu2 = menu1[t].split("_");
									optMenuElement.addAttribute("id", menu2[0]);
									optMenuElement.addAttribute("gubun", "1"); // 0 : 필수 선택 메뉴 / 1 : 추가 선택 메뉴
									if(menu2.length > 0){
										String[] menu3 = menu2[1].split("\\|\\|");
										if(menu3.length > 0){
											for(int ttt=0; ttt < menu3.length; ttt++){
												String[] menu4 = menu3[ttt].split("\\(");
												String viewName = PayUtil.separationistName(menu4);
												String viewPrice = PayUtil.separationistPrice(menu4);
												
												Element optionElement = optMenuElement.addElement("option");
												optionElement.addAttribute("name", viewName);
												optionElement.addAttribute("price", viewPrice);
											}
										}
									}
								}
							}
						}
					}
					
					//주문의 상태 변경
					storeOrder.setOrderCancelStatus("2"); // 고객이 취소 요청을 했을 경우 상태  : 2
					storeOrderDao.saveOrUpdate(storeOrder);
				}else{
					logger.info("kiosk > verifiCode >> [{}] 해당내용은 모바일이아닌 다른기기에서 결제가 되었습니다. [{}]", verifiCode, "X");
	    			storeElement.addAttribute("verificationYN", "X"); //
	    			storeElement.addAttribute("verifiMsg", "매장관리자에게 문의하세요."); //주문 번호
				}
        	}else{
        		logger.info("kiosk > verifiCode >> [{}] 승인번호사용 가능 시간을 초과 하였습니다.  ", verifiCode);
        		
        		// 사용 가능 시간이 지나는걸 확인 하고 없애도록 한다. 비밀번호를 없앤다.
    			StoreOrderVerification storeOrderVerification1 = storeCancelDao.getStoreCancelOneDao(storeOrderVerification.getId());
    			storeOrderVerification1.setCancelStoreAuth("");
    			storeOrderVerification1.setStatus("N"); //해당 코드 사용 불 가능으로 만들기
    			storeCancelDao.saveOrUpdate(storeOrderVerification1);
    			
    			storeElement.addAttribute("verificationYN", "N"); //
    			storeElement.addAttribute("verifiMsg", "매장관리자에게 문의하세요."); //주문 번호
        	}
		}else{
			logger.info("kiosk > verifiCode >> [{}] 승인번호를 알수 없습니다. ", verifiCode);
			storeElement.addAttribute("verificationYN", "F"); //
			storeElement.addAttribute("verifiMsg", "매장관리자에게 문의하세요."); //승인번호를 알수 없습니다. 
    	}
		
		logger.info("/cancelVerifiCheck >> 주문 한 메뉴를 취소 하기위해서 승인번호가 맞는지 체크");
		
		return document;
	}
	
	@SuppressWarnings("unchecked")
	public boolean fcmTransmission(int storeId) {
		
		//취소가 완료가 되었을 경우 FCM 을 보낸다. 
		// 1. 주방용 프린트 FCM을 전송
		// 2. 주방용 패드가 있을 경우 FCM 패드로 전송
		boolean success = false;
		
		Store store = storeService.getStore(storeId);
		if (store != null) {
			List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
			
			try{
				if(deviceList.size() > 0){
					FireMessage fcm = new FireMessage();
					JSONArray mobileTokensDid = new JSONArray();
					JSONArray mobileTokensCook = new JSONArray();
					JSONArray mobileTokens = new JSONArray();
					
					for(Device device : deviceList){
						String fcmToken = device.getFcmToken();
						if(!Util.isNotValid(fcmToken)){
							fcmToken = URLDecoder.decode(fcmToken, "UTF-8");
							
							if (device.getDeviceType().equals("D")) {			// D: 주방용패드
								mobileTokensCook.add(fcmToken);	
							} else if (device.getDeviceType().equals("N")) {	// N: 알리미
								mobileTokensDid.add(fcmToken);	
							} else {
								mobileTokens.add(fcmToken);	
							}
						}
					}
					
					if(!mobileTokens.isEmpty()){
						fcm.sendToGroup(mobileTokens);
					}
					if(!mobileTokensCook.isEmpty()){
						fcm.sendToGroupCook(mobileTokensCook);
					}
					if(!mobileTokensDid.isEmpty()){
						fcm.sendToGroupDid(mobileTokensDid);
					}
					success = true;
				}
			}catch(Exception e){
	        	logger.error("FCM ERROR", e);
	        	throw new ServerOperationForbiddenException("FCM을 전송할수 없습니다.");
			}
		}
		
		return success;
	}

	@Override
	public boolean cancelSuccessbyStoreIdOrderIdDeviceID(JSONArray objectList) {
		boolean success = false;
		// 키오스크 에 대한 취소 작업이 완료 되었을 경우 
		// 1. storeOrder table에 취소 상태를 Y로 변경 
		// 2. storeOrder table의 print / 주방용pad 에 대한 상태를 1로 변경후 FCM 전송 필요
		// 3. StoreOrderCancel Table에 모바일과 동일한 취소 내역을 생성
		if(objectList.size() > 0){
			for (int t = 0; t < objectList.size(); t ++) { 
				JSONObject object = (JSONObject)objectList.get(t);
				if(object != null){
					//2019.04.25 김선영 부장님과
					//아래와 같이 취소했을 경우 키오스크에서 데이터를 보내준다.
//					storeIdpay : 매장ID
//					deviceId : 기기번호
//					storeOrderId : 주문 번호(paycast번호)
//					cardNumber : 카드번호
//					cardName : 카드회사
//					tid : 거래고유번호 : shop_tid
//					mid : 가맹점 번호 : storeMerchantNum
//					goodsAmt : 메뉴 주문 금액 : payAmount
//					orderDate : 주문 날짜 시간 : tradingDate
//					AuthCode : 승인 번호 : approvalNum
//					fnCd1 : 매입사 : acquirer
//					fnName1 : 매입사명 : acquirerName
					
					String storeIdpay = (String)object.get("storeIdpay");			//매장ID
					String storeOrderId = (String)object.get("storeOrderId");		//주문 번호
					String cardNumber = (String)object.get("cardNumber");			//카드번호(키오스크 사용값)
					String cardName = (String)object.get("cardName");				//카드회사(키오스크 사용값)
					String tid = (String)object.get("shop_tid");					//거래고유번호 : shop_tid(키오스크 사용값)
					String mid = (String)object.get("storeMerchantNum");			//가맹점 번호 : storeMerchantNum(키오스크 사용값)
					String goodsAmt = (String)object.get("payAmount"); 				//메뉴 주문 금액 : payAmount(키오스크 사용값)
					String orderDate = (String)object.get("tradingDate");			//주문 날짜 시간 : tradingDate(키오스크 사용값)
					String authCode = (String)object.get("approvalNum"); 			//승인 번호 : approvalNum(키오스크 사용값)
					String fnCd1 = (String)object.get("acquirer"); 					//매입사 : acquirer(키오스크 사용값)
					String fnName1 = (String)object.get("acquirerName"); 			//매입사명 : acquirerName(키오스크 사용값)
					
					StoreOrder storeOrder = storeOrderDao.getOrderOne(Util.parseInt(storeOrderId));
					if(storeOrder != null){
				        StoreOrderCancel storeOrderCancel = new StoreOrderCancel();
				        storeOrderCancel.setPayMethod("CARD");
				        storeOrderCancel.setPayName("신용카드");
				        storeOrderCancel.setMid(mid);
				        storeOrderCancel.setTid(tid);
				        storeOrderCancel.setCancelAmt(goodsAmt);
				        storeOrderCancel.setCancelMSG("");
				        storeOrderCancel.setResultCode("");
				        storeOrderCancel.setResultMsg("취소 성공");
				        storeOrderCancel.setCancelDate(orderDate);
				        storeOrderCancel.setCancelTime("");
				        storeOrderCancel.setCancelNum(authCode);
				        storeOrderCancel.setMoid("");
				        storeOrderCancel.setCardNumber(cardNumber);
				        storeOrderCancel.setCardName(cardName);
				        storeOrderCancel.setFnCd(fnCd1);
				        storeOrderCancel.setFnName(fnName1);
				        storeOrderCancel.setResultCode("");
				        storeOrderCancel.touchWhoC("-1");
				        
				        int cancelId = storeCancelDao.saveCancel(storeOrderCancel);
				        
						StoreOrderVerification storeOrderVerification1 = storeCancelDao.getStoreCancelOneDao(storeOrder.getOrderCancelId());
						storeOrderVerification1.setCancelStoreAuth("");
						storeOrderVerification1.setStoreCancelId(cancelId);
						storeOrderVerification1.setStatus("N"); //해당 코드 사용 불 가능으로 만들기
						storeCancelDao.saveOrUpdate(storeOrderVerification1);
						
						//주문의 상태 변경
			        	storeOrder.setOrderCancelStatus("Y"); // 취소가 완료 되었을 경우 상태 : Y
			        	//주방용 프린트 및 주방용 패드에 대해서 취소를 조회 할수 있도록 변경
			        	storeOrder.setOrderCancelPrint("1"); // 취소가 완료 되었을 경우 프린트 여부는 1로 변경
			        	storeOrder.setOrderCancelPad("1"); // 취소가 완료 되었을 경우 패드 삭제 여부는 1로 변경
			        	storeOrder.touchWho(storeOrder.getOrderNumber());
			        	storeOrderDao.saveOrUpdate(storeOrder);
			        	
			        	// 2019.04.27
			        	// 메뉴가 취소 되었을 경우 DID 에 완료된 내용을 보내준다. 
			        	Store store = storeService.getStore(storeOrder.getStoreId());
			    		if(store != null){
			        		List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
			        		for (Device device : deviceList) {
			        			if (device.getDeviceType().equals("N")) {	// 알리미일 때
									// 2019.06.11 주문 취소일 경우 대기자 수가 줄어 들지 않는 문제가 있어서 먼서 complete를 해준다.  
									StoreOrderCook storeOrderCook = storeCookDao.getStoreCookbyStoreOrderId(storeOrder.getId());
									storeOrderCook.setOrderMenuComplete("Y");
									storeOrderCook.touchWho(storeOrder.getOrderNumber());
									storeCookDao.saveOrUpdate(storeOrderCook);
									
									StoreCookAlarm storeCookAlarm = new StoreCookAlarm(storeOrder.getStoreId(), storeOrder.getId(), storeOrder.getOrderNumber(), 
											device.getId(), device.getUkid());
									
									storeCookAlarm.setOrderAlarmGubun("C");// 완료상태
									storeCookDao.saveOrUpdate(storeCookAlarm);
									
									String cookMenuTask = "StoreComplete.bbmc";
									StoreCookTask storeCookTask = new StoreCookTask(store.getSite().getId(), storeOrder.getStoreId(), device.getId(), device.getUkid(), cookMenuTask, "N", new Date(), Util.setMaxTimeOfDate(new Date()));
									storeCookTaskDao.saveOrUpdate(storeCookTask);
									
		    						cookMenuTask = "StoreWaitPeople.bbmc";
		    						StoreCookTask storeCookTask1 = new StoreCookTask(store.getSite().getId(), storeOrder.getStoreId(), device.getId(), device.getUkid(), cookMenuTask, "N", new Date(), Util.setMaxTimeOfDate(new Date()));
		    						storeCookTaskDao.saveOrUpdate(storeCookTask1);
			        			}
			        		}
			    		}else{
			    			
			    			logger.info("kiosk >> 주문 취소 완료 처리가 정상적이지 않습니다. cancelSuccessbyStoreIdOrderIdDeviceID");
			    			logger.info("storeId >> [{}]", storeIdpay);
			    			logger.info("storeOrderId >> [{}]", storeOrderId);
			    		}
			        	
			        	//취소가 완료가 되었을 경우 FCM 을 보낸다. 
			        	// 1. 주방용 패드가 없을 경우 주방용 프린트로 FCM을 전송하며
			        	// 2. 주방용 패드가 있을 경우 FCM을  패드로 전송한다.
			        	success = fcmTransmission(Util.parseInt(storeIdpay));
					}
				}
			}
		}
		
		
		return success;
	}

}
