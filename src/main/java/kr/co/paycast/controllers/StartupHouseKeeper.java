package kr.co.paycast.controllers;


import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.info.GlobalInfo;
import kr.co.paycast.models.calc.service.CalculateService;
import kr.co.paycast.models.pay.Device;
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
import kr.co.paycast.models.store.service.StoreBasketService;
import kr.co.paycast.models.store.service.StoreOrderService;
import kr.co.paycast.utils.FireMessage;
import kr.co.paycast.utils.Util;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupHouseKeeper implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger logger = LoggerFactory.getLogger(StartupHouseKeeper.class);

    @Autowired
    private StoreService storeService;
	
    @Autowired
    private StoreBasketService storeBasketService;
    
    @Autowired
    private StoreOrderService storeOrderService;
    
    @Autowired
    private CalculateService calcService;
    
    @Autowired
    private SelfService selfService;
    
    @Autowired
    private StoreOrderDao storeOrderDao;
    
    @Autowired
    private StoreCookDao storeCookDao;
    
    @Autowired 
    private DeviceService devService;
    
    @Autowired
	private StoreCookTaskDao storeCookTaskDao;
	
	private static Timer bgKeyGenTimer;
	private static Timer bgStoreOnOffTimer;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		String appId = event.getApplicationContext().getId();
		logger.info("Enter onApplicationEvent() - id=" + appId);
		
		if (!appId.equals("org.springframework.web.context.WebApplicationContext:/" + GlobalInfo.AppId)) {
			return;
		}
		
		//
		// 역할: RSA 키 주기적 갱신
		// 주기: 구동 즉시부터 30분 단위
		// 
		if (bgKeyGenTimer == null) {
			bgKeyGenTimer = new Timer();
			bgKeyGenTimer.scheduleAtFixedRate(new TimerTask() {
		    	   public void run()
		 	       {
		    		   GlobalInfo.RSAKeyPair = Util.getKeyPairRSA();
		    		   GlobalInfo.RSAKeyMod = "";

		    		   logger.info("bgKeyGenTimer - KeyPair: " + (GlobalInfo.RSAKeyPair == null ? "null" : "new"));
			       }
		        }, 0, 30 * (60 * 1000));
		}
		
		//
		// 역할: 60초씩 확인하여 영업 시작 시간, 영업 종료 시간 으로 해당 영업을 자동화 한다. 
		// 주기: 
		// 24시간 영업인 매장일 경우 해당 되지 않는다. 
		// 
		if (bgStoreOnOffTimer == null) {
			bgStoreOnOffTimer = new Timer();
			bgStoreOnOffTimer.scheduleAtFixedRate(new TimerTask() {
	    	   public void run()
	 	       {
	    		   HttpSession session = null;
	    		   SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    		   List<Store> storeList = storeService.get();
	    		   if(storeList.size() > 0){
	    			   Date toDate = new Date();
	    			   
	    			   for(Store store : storeList){
	    				   //1. 24시간 인지 확인
	    				   //2. customOperation 을 조회 하여 auto(false) / manual(true)인지 확인
	    				   //3. manual true 인 경우  다음 시간까지 대기 
	    				   // 24시간 확인
	    				   if(!store.isOpenHour_24()){
	    					   String toDateSt = dateTimeFormat.format(toDate);
		    				   
	    					   StoreOpt optOne = store.getStoreOpt(); 
	    					   if(optOne != null){
	    						   String stOpenTime = dateTimeFormat.format(optOne.getOpenTime());
	    						   String stCloseTime = dateTimeFormat.format(optOne.getCloseTime());
	    						   
	    						   if(stOpenTime.equals(toDateSt)){
//	    							   logger.info("OPEN 오픈 시간이 현재시간과 같아요", optOne.isNextDayClose());
	    							   // 이미 오픈 중일 경우 상태 유지 
	    							   // 오픈이 아닐 경우 오픈 진행
	    							   if(!"O".equals(store.getOpenType())){
	    								   store.setOpenType("O");
		    							   if(store.isKioskOrderAllowed()){
		    								   store.setKioskOrderEnabled(true);
		    							   }
		    							   if(store.isMobileOrderAllowed()){
		    								   store.setMobileOrderEnabled(true);
		    							   }
		    							   store.touchWho(session);
		    							   storeService.saveOrUpdate(store);
		    							   
		    							   // 모바일 결제 : 주문 가능 시간 설정을 
		    							   StoreEtc storeEtc = store.getStoreEtc();
		    							   storeEtc.setOderPossiblCheck("O");
		    							   storeEtc.setOder_setting_Time(0);
		    							   storeEtc.touchWho(session);
		    							   storeService.saveOrUpdate(storeEtc);
		    							   
		    							   selfService.setMonTask("KE", String.valueOf(store.getId()), session);	    							   
	    							   }

	    							   // 고객 오픈에 대한 내용을 자동오픈으로 변경
	    							   StoreOpt storeOpt = store.getStoreOpt();
	    							   storeOpt.setCustomOperation(false);
	    							   storeOpt.touchWho(session);
	    							   storeService.saveOrUpdate(storeOpt);
	    							   
	    						   }else if(stCloseTime.equals(toDateSt)){
//	    							   logger.info("새벽 일경우 CLOSE 종료 시간이 현재시간과 같아요", optOne.isNextDayClose());
	    							   
	    							   Calendar cal = Calendar.getInstance();
	    							   cal.setTime(optOne.getOpenTime());
	    							   cal.add(Calendar.DATE, 1); // 다음날 날짜를 계산해서 넣어준다.
	    							   Date startNextDate = cal.getTime();
	    							   
	    							   cal.setTime(optOne.getCloseTime());
	    							   cal.add(Calendar.DATE, 1); // 다음날 날짜를 계산해서 넣어준다.
	    							   Date endNextDate = cal.getTime();
//	    							   logger.info("다음날 매장종료 startTestDate: [{}], endTestDate [{}]", dateTimeFormat.format(startNextDate) , dateTimeFormat.format(endNextDate));
	    							   
	    							   StoreOpt storeOpt = store.getStoreOpt();
	    							   storeOpt.setOpenTime(startNextDate);
	    							   storeOpt.setCloseTime(endNextDate);
	    							   storeOpt.setCustomOperation(false);
	    							   storeOpt.touchWho(session);
	    							   storeService.saveOrUpdate(storeOpt);
	    							   
	    							   //운영 시간 종료
	    							   store.setOpenType("C");
	    							   store.setKioskOrderEnabled(false);
	    							   store.setMobileOrderEnabled(false);
	    							   store.touchWho(session);
	    							   storeService.saveOrUpdate(store);
	    							   
	    							   boolean closeSucess = calcService.operEnd(String.valueOf(store.getId()), session);
	    							   if(closeSucess) {

	    			    					closeTask(store.getId());
	    			    				}
	    							   selfService.setMonTask("KE", String.valueOf(store.getId()), session);
	    						   }
	    					   }
	    				   }else{
	    					   logger.info("[{}]24시간 매장은 영업 체크 안함[{}]", store.getStoreName(), store.isOpenHour_24());
	    				   }
	    				   
	    				   
	    				   // 모바일 주문 가능 시간 설정 관련 해서 변경
						   StoreEtc storeEtc = store.getStoreEtc();
						   if("P".equals(storeEtc.getOderPossiblCheck())){
							   Date possibleDate = new Date(storeEtc.getOder_possible_Time().getTime());
							   GregorianCalendar calendarPu = new GregorianCalendar();
							   calendarPu.setTime(possibleDate);
							   calendarPu.add(Calendar.MINUTE, storeEtc.getOder_setting_Time());
							   Date calendarPuDate = calendarPu.getTime();
							   String timelog = dateTimeFormat.format(calendarPuDate);
							   if(toDate.compareTo(calendarPuDate) > 0){
//								   logger.info("[{}] 모바일 주문 가능 (주문 가능 변경 시작), calendarPuDate[{}]", store.getStoreName(), timelog);
								   store.setMobileOrderEnabled(true);
								   store.touchWho(session);
								   storeService.saveOrUpdate(store);
								   
								   storeEtc.setOderPossiblCheck("O");
								   storeEtc.setOder_setting_Time(0);
								   storeEtc.touchWho(session);
								   storeService.saveOrUpdate(storeEtc);
//								   logger.info("[{}] 모바일 주문 가능 (주문 가능 변경 종료), calendarPuDate[{}]" , store.getStoreName(), timelog);
							   }else{
								   logger.info("[{}] 주문 가능 시간 아직 안됨 (주문 불가능), calendarPuDate[{}]", store.getStoreName(), timelog);
							   }
						   }
	    			   }
	    		   }
	    		   
	    		   logger.info("========================================================");
		       }
	        }, 0, 60 * 1000);
		}
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

@SuppressWarnings("unchecked")
//@Override
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
//				storeCookService.saveOrUpdate(storeOrderCook);
				
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
