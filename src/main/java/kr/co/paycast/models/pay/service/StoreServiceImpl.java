package kr.co.paycast.models.pay.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.User;
import kr.co.paycast.models.pay.PayComparator;
import kr.co.paycast.models.pay.SiteRegion;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreEtc;
import kr.co.paycast.models.pay.StoreOpt;
import kr.co.paycast.models.pay.StoreUser;
import kr.co.paycast.models.pay.dao.SiteRegionDao;
import kr.co.paycast.models.pay.dao.StoreDao;
import kr.co.paycast.models.pay.dao.StoreEtcDao;
import kr.co.paycast.models.pay.dao.StoreOptDao;
import kr.co.paycast.models.pay.dao.StoreUserDao;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.StoreInfoItem;
import kr.co.paycast.viewmodels.pay.StoreItem;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("storeService")
public class StoreServiceImpl implements StoreService {
	private static final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);	
	
	@Autowired
    private SessionFactory sessionFactory;

	@Autowired
	private MessageManager msgMgr;
	
    @Autowired
    private StoreDao storeDao;
    
    @Autowired
    private StoreEtcDao storeEtcDao;
    
    @Autowired
    private StoreOptDao storeOptDao;
    
    @Autowired
    private SiteRegionDao siteRegionDao;
    
    @Autowired
    private StoreUserDao storeUserDao;

    
	@Override
	public void flush() {
		
		sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public List<Store> get() {
		
		return storeDao.get();
	}
	
	@Override
	public Store getStore(int id) {
		
		return storeDao.get(id);
	}

	@Override
	public void saveOrUpdate(Store store) {
		
		storeDao.saveOrUpdate(store);
	}

	@Override
	public void deleteStore(Store store) {
		
		storeDao.delete(store);
	}

	@Override
	public void deleteStores(List<Store> stores) {
		
		storeDao.delete(stores);
	}

	@Override
	public DataSourceResult getStoreList(DataSourceRequest request) {
		
		return storeDao.getList(request);
	}

	@Override
	public DataSourceResult getStoreList(DataSourceRequest request, boolean isEffectiveMode) {
		
		return storeDao.getList(request, isEffectiveMode);
	}

	@Override
	public Store getStore(Site site, String shortName) {
		
		return storeDao.get(site, shortName);
	}

	@Override
	public Store getStoreBySiteIdStoreName(int siteId, String storeName) {
		
		return storeDao.getBySiteIdStoreName(siteId, storeName);
	}

	@Override
	public Store getStoreByStoreKey(String storeKey) {
		
		return storeDao.getByStoreKey(storeKey);
	}

	@Override
	public List<Store> getStoreListBySiteId(int siteId) {
		
		return storeDao.getListBySiteId(siteId);
	}

	@Override
	public List<Store> getEffectiveStoreList() {
		
		return storeDao.getEffectiveList();
	}

	@Override
	public List<Store> getEffectiveStoreListBySiteId(int siteId) {
		
		return storeDao.getEffectiveListBySiteId(siteId);
	}

	@Override
	public List<Store> getStoreListBySiteIdStoreName(int siteId, String storeName) {
		
		return storeDao.getListBySiteIdStoreName(siteId, storeName);
	}

	@Override
	public int getStoreCountBySiteId(int siteId) {
		
		return storeDao.getCountBySiteId(siteId);
	}

	
	@Override
	public void saveOrUpdate(StoreEtc storeEtc) {
		
	    storeEtcDao.saveOrUpdate(storeEtc);
	}


	@Override
	public void saveOrUpdate(StoreOpt storeOpt) {
		
	    storeOptDao.saveOrUpdate(storeOpt);
	}

	
	@Override
	public SiteRegion getSiteRegion(int id) {
		return siteRegionDao.get(id);
	}

	@Override
	public List<SiteRegion> getSiteRegionList() {
		return siteRegionDao.getList();
	}

	@Override
	public void saveOrUpdate(SiteRegion siteRegion) {
		siteRegionDao.saveOrUpdate(siteRegion);
	}

	@Override
	public void deleteSiteRegion(SiteRegion siteRegion) {
		siteRegionDao.delete(siteRegion);
	}

	@Override
	public void deleteSiteRegions(List<SiteRegion> siteRegions) {
		siteRegionDao.delete(siteRegions);
	}

	@Override
	public int getSiteRegionCount() {
		return siteRegionDao.getCount();
	}

	@Override
	public DataSourceResult getSiteRegionList(DataSourceRequest request) {
		return siteRegionDao.getList(request);
	}

	@Override
	public boolean isRegisteredSiteRegion(int siteId, int regionId) {
		return siteRegionDao.isRegistered(siteId, regionId);
	}

	@Override
	public List<SiteRegion> getSiteRegionListBySiteId(int siteId) {
		return siteRegionDao.getListBySiteId(siteId);
	}

	@Override
	public List<SiteRegion> getSiteRegionListByRegionId(int regionId) {
		return siteRegionDao.getListByRegionId(regionId);
	}

	@Override
	public List<SiteRegion> getSiteRegionDefaultValueListBySiteId(int siteId) {
		return siteRegionDao.getDefaultValueListBySiteId(siteId);
	}

	@Override
	public SiteRegion getSiteRegion(int siteId, String regionCode) {
		return siteRegionDao.get(siteId, regionCode);
	}


	@Override
	public StoreUser getStoreUser(int id) {
		
		return storeUserDao.get(id);
	}

	@Override
	public void saveOrUpdate(StoreUser storeUser) {
		
		storeUserDao.saveOrUpdate(storeUser);
	}

	@Override
	public void deleteStoreUser(StoreUser storeUser) {
		
		storeUserDao.delete(storeUser);
	}

	@Override
	public void deleteStoreUsers(List<StoreUser> storeUsers) {
		
		storeUserDao.delete(storeUsers);
	}

	@Override
	public DataSourceResult getStoreUserList(DataSourceRequest request) {
		
		return storeUserDao.getList(request);
	}

	@Override
	public boolean isRegisteredStoreUser(int storeId, int userId) {
		
		return storeUserDao.isRegistered(storeId, userId);
	}

	@Override
	public List<StoreUser> getStoreUserListByStoreId(int storeId) {
		
		return storeUserDao.getListByStoreId(storeId);
	}

	@Override
	public List<StoreUser> getStoreUserListByUserId(int userId) {
		
		return storeUserDao.getListByUserId(userId);
	}

	
	@Override
	public List<StoreItem> getStoreSwitcherListBySiteIdUserId(int siteId, int userId) {
		
		ArrayList<StoreItem> retList = new ArrayList<StoreItem>();
		List<Store> storeList = new ArrayList<Store>();
		
		if (Util.hasThisPriv(userId, "internal.ManageSiteJob")) {
			//
			// 사이트 관리자라면 해당 사이트에 등록된 상점 중 첫 자료만을 반환
			//
			
			storeList =  getEffectiveStoreListBySiteId(siteId);

			Collections.sort(storeList, PayComparator.StoreStoreNameComparator);
			
			if (storeList.size() > 0) {
				retList.add(new StoreItem(storeList.get(0)));
			}
		} else {
			//
			// 사이트 관리자가 아닌 점주 계정인 경우 접근 가능한 모든 상점을 반환
			//
			
			storeList = getStoreListBySiteIdUserId(siteId, userId);

			Collections.sort(storeList, PayComparator.StoreStoreNameComparator);
			
			for(Store store : storeList) {
				retList.add(new StoreItem(store));
			}
		}
		
		return retList;
	}

	@Override
	public List<User> getUserListByStoreId(int storeId) {
		
		ArrayList<User> retList = new ArrayList<User>();
		
		List<StoreUser> list = getStoreUserListByStoreId(storeId);
		
		for(StoreUser storeUser : list) {
			retList.add(storeUser.getUser());
		}
		
		return retList;
	}

	@Override
	public List<Store> getStoreListBySiteIdUserId(int siteId, int userId) {
		
		ArrayList<Store> retList = new ArrayList<Store>();
		
		List<StoreUser> list = getStoreUserListByUserId(userId);
		
		for(StoreUser storeUser : list) {
			if (storeUser.getStore().getSite().getId() == siteId) {
				retList.add(storeUser.getStore());
			}
		}
		
		return retList;
	}

	@Override
	public StoreInfoItem storeInfoUpdate(Map<String, Object> model, Locale locale, HttpSession session, String admYn) {
		Store target = null;
		
		boolean openTime = false;
		boolean hour24 = (boolean) model.get("hour24");
		boolean nextDayClose = (boolean) model.get("nextDayClose"); // 새벽 영업 관련  true / false
		
		String message = "";
		String startUp = timeChange((String)model.get("start"));
		String endUp = timeChange((String)model.get("end"));
		String oderSettingTime = Util.parseString((String) model.get("oderSetting"));
		String bizName = Util.parseString((String)model.get("bizName"));
		String bizRep = Util.parseString((String)model.get("bizRep"));
		String bizNum = Util.parseString((String)model.get("bizNum"));
		String phone = Util.parseString((String)model.get("phone"));
		String localCode = Util.parseString((String)model.get("localCode"));
		String localName = Util.parseString((String)model.get("localName"));
		String address = Util.parseString((String)model.get("address"));
		
    	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
    	try{
    		// 매장의 정보를 가져와서 확인
        	target = getStore((Integer)model.get("id"));
        	if(target != null){
        		
	    		// 예약시간값이  List 로 넘어와 해당 값을 String형식으로 변경
	    		String rsvpTime = "";
	    		List<String> rsvpTimeList = (ArrayList<String>)model.get("rsvpTime");
	    		if(rsvpTimeList != null && rsvpTimeList.size() > 0){
	    			for(String rsvpTimeOne : rsvpTimeList){
			    		if(Util.isNotValid(rsvpTime)){
			    			rsvpTime = rsvpTimeOne;
			    		}else{
			    			rsvpTime += ("," + rsvpTimeOne);
			    		}
		    		}	
	    		}
        		
            	// 영업 시작 - 마감 (다음날)에 대한 시간 계산을 하기 위해서 사용 
            	Calendar cal = Calendar.getInstance();
            	Date today = cal.getTime();		// 오늘 날짜
                cal.add(Calendar.DATE, 1);
                Date nextDay = cal.getTime();	// 내일 날짜
                
                // 영업 시작 시간 DATE
    			Date start = dateTimeFormat.parse(dateFormat.format(today)+" "+startUp + ":00");
    			// 다음날 영업 일 경우  아닌 경우에 따라서 영업 종료가 달라진다. 
    			// 다음날 영업종료 가 false 일 경우 CloseTime을 해당 내용으로 저장
    			Date todayEnd = dateTimeFormat.parse(dateFormat.format(today)+" "+ endUp + ":00");
    			// 다음날 영업종료 가 true 일 경우 CloseTime을 해당 내용으로 저장
    			Date nextDayEnd = dateTimeFormat.parse(dateFormat.format(nextDay)+" "+ endUp + ":00");
    			
    			// 1. 24시간일 경우 
    			// 이전에 저장되어 있는 시간을 그대로 유지 하지만 화면에서 넘어온 시간으로 저장
    			
	    		// 2. 24시간 영업이 아닐 경우 
	    		// 변경전 시간과 변경 후 시간을 비교하여 하나라도 바뀌여 있을 경우 
	    		// 주문 불가가 되어 있을때에도 주문 가
	    		if(!hour24){
    				// nextDayClose : 다음날까지  영업 여부 
    				// 다음날까지  영업 일 경우 open 시간이  close 시간 보다 이후 시간이 되어야 합니다.
    				// 당일 영업일 경우  open 시간이 close 시간 보다 이전 시간 이여 합니다.
	    			if(nextDayClose){
	    				// 다음날까지 영업일 경우 
	    				// 오픈 시간과 종료 시간을 비교 하기 위해서 당일을 기준으로 체크 한다. 
	    				if(start.compareTo(todayEnd) <= 0){
	    					message = msgMgr.message("storeinfo.msg.empty_dayTime", locale);
	    					
	    					throw new ServerOperationForbiddenException(message);
	    				}
	    			}else{
	    				// 당일 영업일 경우 
	    				if(start.compareTo(todayEnd) >= 0){
	    					message = msgMgr.message("storeinfo.msg.empty_dayTime", locale);
	    					
	    					throw new ServerOperationForbiddenException(message);
	    				}
	    			}
	    			
	        		// 이전 영업 시간과 다를 경우 저장하기 위해서 사용
	        		String beforeStart = timeFormat.format(target.getStartTime());
	        		String beforeEnd = timeFormat.format(target.getEndTime());
    				// 현재 시간이  startTime 과 endTime 사이 일 경우 
    				// 이전에 "주문 불가"여도 주문 가능 시간을 "언제나 가능"으로 변경
	    			if(!beforeStart.equals(startUp) || !beforeEnd.equals(endUp)){
	    				int startComp = today.compareTo(start);
	    				int endComp = today.compareTo(todayEnd);
	    				if(nextDayClose){
	    					if((startComp >= 0) && (endComp >= 0)){
	    						endComp = today.compareTo(nextDayEnd);
	    						if(endComp < 0){
			    					openTime = true;		
	    						}
		    				}
	    				}else{
	    					if((startComp >= 0) && (endComp <= 0)){
		    					openTime = true;
		    				}
	    				}
	    			}
	    		}
	    		
	            StoreEtc storeEtc = target.getStoreEtc();
	            if (storeEtc == null) {
	            	storeEtc = new StoreEtc(target, session);
	            }
	            int settingTime = 0;
	            switch (oderSettingTime) {
					case "on":
		    			storeEtc.setOderPossiblCheck("O");
		    			storeEtc.setOder_setting_Time(settingTime);	
		    			storeEtc.setOder_possible_Time(new Timestamp(today.getTime()));
						break;
					case "off":
		    			storeEtc.setOderPossiblCheck("C");
		    			// 영업시간이 변경되어 저장이 될경우 모바일 결제 언제나 가능으로 변경 
		    			if(openTime){
		    				storeEtc.setOderPossiblCheck("O");	
		    			}
		    			storeEtc.setOder_setting_Time(settingTime);
		    			storeEtc.setOder_possible_Time(new Timestamp(today.getTime()));
						break;
					default:
		    			storeEtc.setOderPossiblCheck("P");
		    			// P 일 경우 
		    			if(!oderSettingTime.split(":")[0].equals("D")){
		    				
		    				// 아래는 예시 > 60분 후 가능 선택시 
		    				// oderSettingTime [01:00] 
		    				// oderSettingTime.split [[01, 00]]
		    				// storeEtc.getOder_setting_Time() [0]
		    				// settingTime [60]
		    				settingTime = Integer.parseInt(oderSettingTime.split(":")[0])*60+Integer.parseInt(oderSettingTime.split(":")[1]);
		    				// 이전에 저장되어 있는 값과 같을 경우 새롭게 저장 하지 않는다. 
		    				// 같은 값을 계속 저장할 경우 주문 가능한 시간이 계속 늘어남
		    				if(storeEtc.getOder_setting_Time() != settingTime){
		    					storeEtc.setOder_setting_Time(settingTime);
								storeEtc.setOder_possible_Time(new Timestamp(today.getTime()));
		    				}
		    			}else{
		    				storeEtc.setOder_setting_Time(settingTime);
		    				
		    				// 아래는 예시
		    				// oderSettingTime >> [D:05:45 PM] 
		    				// oderSettingTime.length() >> [10]
		    				// oderSettingTime.substring(2,oderSettingTime.length()) >> [05:45 PM]
		    				// timeChange(oderSettingTime.substring(2,oderSettingTime.length())) >> [17:45]
	    					storeEtc.setOder_possible_Time(Timestamp.valueOf(dateFormat.format(today)+" "+timeChange(oderSettingTime.substring(2, oderSettingTime.length()))+":00")); 				
		    			}
						break;
				}
	            
	    		storeEtc.touchWho(session);
	    		saveOrUpdate(storeEtc);
	    		
	    		StoreOpt storeOpt = target.getStoreOpt();
	    		storeOpt.setRsvpTime(rsvpTime);
    				    		
	    		if(nextDayClose){
	    			storeOpt.setOpenTime(start);
	    			storeOpt.setCloseTime(nextDayEnd);	
	    		}else{
	    			
	    			boolean timeAfterEnd = true;
	    			// 영업이 종료된 후 영업시작 / 종료 시간이 변경 되었을 경우 
	    			if("C".equals(target.getOpenType())){
	    	    		//영업종료인데 오픈 시간과 종료시간이 DB 값보다 작을 경우
	    				if(start.compareTo(storeOpt.getOpenTime()) < 0 && todayEnd.compareTo(storeOpt.getCloseTime()) < 0){
	    					timeAfterEnd = false;
	    				}
	    				
	    				// 해당 영업 종료 시간이 이전에 등록된 영업시간보다 이후일 경우 
    					// 영업이 오픈 되기 때문에 당일 시간으로 변경을 해야 한다. 
    					if(openTime){
    						timeAfterEnd = true;
    					}
	    			}else{
	    				//고객이 직접 영업 중으로 바꾼다음에 영업시간을 변경 하였을 경우 
	    				if(storeOpt.isCustomOperation()){
	    					if(start.compareTo(storeOpt.getOpenTime()) < 0 && todayEnd.compareTo(storeOpt.getCloseTime()) < 0){
		    					timeAfterEnd = false;
		    				}	
	    				}
	    			}
	    			
	    			if( timeAfterEnd ){
	    				storeOpt.setOpenTime(start);
	    				storeOpt.setCloseTime(todayEnd);
	    			}else{
	    				Date nextDayStart = dateTimeFormat.parse(dateFormat.format(nextDay)+" "+ startUp + ":00");
	    				storeOpt.setOpenTime(nextDayStart);
	    				storeOpt.setCloseTime(nextDayEnd);
	    			}
	    		}
	    		storeOpt.setNextDayClose(nextDayClose);
	    		storeOpt.setCustomOperation(false);
	    		storeOpt.touchWho(session);
	    		saveOrUpdate(storeOpt);
	    		
	    		if("adm".equals(admYn)){
	    			target.setBizName(bizName);
	    			target.setBizRep(bizRep);
	    			target.setBizNum(bizNum);
	    			target.setPhone(phone);
	    			target.setLocalCode(localCode);
	    			target.setLocalName(localName);
	    			target.setAddr2(address);
	    		}
	    		
	    		target.setStartTime(new Timestamp(start.getTime()));
	    		if(nextDayClose){
	    			target.setEndTime(new Timestamp(nextDayEnd.getTime()));	
	    		}else{
	    			target.setEndTime(new Timestamp(todayEnd.getTime()));
	    		}
	    		target.setOpenHour_24(hour24);
	    		target.touchWho(session);
    			saveOrUpdate(target);
    			
        	}
    	}catch(Exception e){
    		logger.error("update", e);
    		
    		if(message != ""){
    			throw new ServerOperationForbiddenException(message);
    		}
    		
        	throw new ServerOperationForbiddenException("SaveError");
    	}
    	
		return target == null ? null : new StoreInfoItem(target);
	}

    //12시간형으로 되어 있는 시간을 24시간형으로 변환
    public String timeChange(String time){
    	String [] arrayTime = time.split(" ");
    	String hour, minute = null;
    	
    	if(arrayTime[1].equals("AM")){
    		hour = arrayTime[0].split(":")[0];
    		minute = arrayTime[0].split(":")[1];
    	}else{
    		hour = (Integer.parseInt(arrayTime[0].split(":")[0])+12)+"";
    		minute = arrayTime[0].split(":")[1];
    	}
    
    	return hour+":"+minute;
    }
}
