package kr.co.paycast.models.store.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import kr.co.paycast.info.PayGlobalInfo;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.calc.StoreCalcMenu;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.StoreOrderNumber;
import kr.co.paycast.models.store.dao.StoreNumberDao;
import kr.co.paycast.utils.Util;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("StoreNumberService")
public class StoreNumberServiceImpl implements StoreNumberService {
	private static final Logger logger = LoggerFactory.getLogger(StoreNumberServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;

	@Autowired
	private MessageManager msgMgr;
	
	@Autowired
	private StoreNumberDao storeNumberDao;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private DeviceService devService;
    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void saveOrUpdate(StoreOrderNumber orderNum) {
		storeNumberDao.saveOrUpdate(orderNum);
	}
	
	@Override
	public StoreOrderNumber getOrderNumbyStoreID(int storeId) {
		return storeNumberDao.getOrderNumbyStoreID(storeId);
	}

	@Override
	public int getOrderNumMobile(int storeId, HttpSession session, Locale locale) {
		int number = 9999;
		Store store = storeService.getStore(storeId);
		if(store == null){
        	logger.error("getOrderNum >> storeId [{}], mobile [{}]", storeId, "M");
        	logger.error("getOrderNum [{}]", msgMgr.message("error.store", locale));
        	return number;
		}
		
		number = orderNum(store, "M", session);
			
		return number;
	}
	
	/**
	 * 키오스크 조회 주문 번호  (KIOSK)
	 */
	@Override
	public int getOrderNum(int storeId, String deviceId, HttpSession session, Locale locale) {
		int number = 9999;
		Store store = storeService.getStore(storeId);
		if(store == null){
        	logger.error("getOrderNum >> storeId [{}], deviceId [{}]", storeId, deviceId);
        	logger.error("getOrderNum [{}]", msgMgr.message("error.store", locale));
        	return number;
		}
		
		Device device = devService.getDeviceByUkid(Util.parseString(deviceId));
		if (device == null) {
        	logger.error("getOrderNum >> storeId [{}], deviceId [{}]", storeId, deviceId);
        	logger.error("getOrderNum [{}]", msgMgr.message("error.deiviceId", locale));
        	return number;
		}
		
		// 상점 정보와 deviceID의 상점 정보가 같은 지 확인 하고 다를 경우 조회가 되지 않도록 한다.
		if(store.getId() != device.getStore().getId()){
			logger.error("getOrderNum >> storeId [{}], deviceId [{}] ", storeId, deviceId);
        	logger.error("getOrderNum (storeId !=  device.getStore().getId()) [{}]", msgMgr.message("error.storeDeiviceId", locale));
        	return number;
		}
		
		number = orderNum(store, deviceId , session);
			
		return number;
	}
	
	private int orderNum(Store store, String whereModule ,HttpSession session){
		int number = 100;
		try{
			// 2019.11.21 DB 조회 및 업데이트시 겹침 문제가 발생하여 PayGlobalInfo 형식으로 변경
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd");
			Date today = new Date();
			String oToday = mSimpleDateFormat.format ( today );
			today = mSimpleDateFormat.parse( oToday );
			
			if(PayGlobalInfo.OrderNumMap.get(store.getId()) != null && PayGlobalInfo.OrderNumDt.get(store.getId()) != null){
				logger.info("PayGlobalInfo.OrderNumDt.get(store.getId()) [{}], today [{}]", PayGlobalInfo.OrderNumDt.get(store.getId()), today);
				logger.info("today.compareTo(PayGlobalInfo.OrderNumDt.get(store.getId())) [{}]", today.compareTo(PayGlobalInfo.OrderNumDt.get(store.getId())));
				if(today.compareTo(PayGlobalInfo.OrderNumDt.get(store.getId())) == 0){
					int oldNum = (int)PayGlobalInfo.OrderNumMap.get(store.getId());
					logger.info("oldNum [{}], today [{}]", oldNum, today);
					PayGlobalInfo.OrderNumMap.put(store.getId(), oldNum+1);
					number = oldNum;	
				}else{
					// 날짜가 다를 경우 
					PayGlobalInfo.OrderNumMap.put(store.getId(), number+1);
					PayGlobalInfo.OrderNumDt.put(store.getId(), today);
				}
			}else{
				PayGlobalInfo.OrderNumMap.put(store.getId(), number+1);
				PayGlobalInfo.OrderNumDt.put(store.getId(), today);
			}
			
			
			// 1. storeId로 등록된 ordernum 정보가 있는지 확인
			// 2. 조회된 내용이 없을 경우 (null)일 경우 해당 store 정보로 insert진행 주문 번호는 1
			// 3. 조회된 내용이 있을 경우 
			// 3-1. 조회 된 날짜 를 비교 (compareTo)오늘 날짜보다 DB날짜가 이전 일 경우 날짜 업데이트 및 주문번호 1번 부터 다시 시작
			// 3-2. 조회 된 날짜 를 비교 (compareTo) 오늘 날짜와 같을 경우 해당 주문 번호 에서 +1 하여 update
			// 3-2-1. update할때 unique 값이 겹칠 경우 확인 필요 --- 이부분은 확인 하고 대응
//			StoreOrderNumber ordernum = getOrderNumbyStoreID(store.getId());
//			if(ordernum != null){
//				logger.info("ordernum.getStoreId() [{}], ordernum.getOrderNum() [{}]", ordernum.getStoreId(), ordernum.getOrderNum());
//				
//				Date userDate = ordernum.getUseDate();
//
//				int comp = today.compareTo(userDate);
//				logger.info("comp [{}]", comp);
//				if(comp > 0){
//					logger.info("ordernum.getOrderNum() 날짜 변경전 [{}]", ordernum.getOrderNum());
//					
//					ordernum.setOrderNum(number);
//					ordernum.setUseDate(today);
//					
//					logger.info("today [{}], ordernum.getOrderNum() [{}]", today, number);
//					
//					saveOrUpdate(ordernum);
//					
//					ordernum = getOrderNumbyStoreID(store.getId());
//					logger.info("ordernum.getOrderNum() 날짜 변경후  [{}]", ordernum.getOrderNum());
//					number = ordernum.getOrderNum();
//				}else{
//					number = ordernum.getOrderNum();
//				}
//				
//				logger.info("ordernum.getStoreId() [{}], ordernum.getOrderNum() 날짜 변경후  [{}]", ordernum.getStoreId(), number);
//			}else{
//				ordernum = new StoreOrderNumber(store.getId(), today, session);
//				saveOrUpdate(ordernum);
//			}
			
		}catch(Exception e){
			logger.error("getOrderNum >> storeId [{}], whereModule [{}]", store.getId(), whereModule);
			logger.error("getOrderNum [{}]", "주문 번호 조회에 대한 확인 필요");
			logger.error("getOrderNum [{}]", e);
			return 9999;
		}
		
		logger.info("getOrderNum >> storeId [{}], whereModule [{}]", store.getId(), whereModule);
		logger.info("getOrderNum >> whereModule [{}], ordernumber [{}]", whereModule, number);
		return number;
	}

}
