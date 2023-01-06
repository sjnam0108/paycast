package kr.co.paycast.models.store.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.StoreAlimTalk;
import kr.co.paycast.models.store.dao.StoreAlimTalkDao;
import kr.co.paycast.utils.Util;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("StoreAllimTalkService")
public class StoreAllimTalkServiceImpl implements StoreAllimTalkService {
	private static final Logger logger = LoggerFactory.getLogger(StoreAllimTalkServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
    
	@Autowired
	private StoreAlimTalkDao alimTalkDao;
	
    @Autowired 
    private StoreService storeService;
    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void save(StoreAlimTalk alimTalk) {
		alimTalkDao.save(alimTalk);
	}

	@Override
	public DataSourceResult getAllimTalkList(DataSourceRequest request, HttpSession session, Boolean isStoreChk) {
		SimpleDateFormat transMon = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat transMonDay = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dbSelectDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); // 날짜 포맷
		
		DataSourceResult res = new DataSourceResult();
		
		// D : 일별조회  / M : 월별조회 
  		// 조회 별 양식을 맞춰준후 조회 하도록 한다.. 값이 없을 경우 기본값 셋팅
  		String dayMonth = (String)request.getReqStrValue1(); 
  		String from = (String)request.getReqStrValue2(); 
  		String to = (String)request.getReqStrValue3(); 
  		
  		Date fromDate = new Date();
  		Date toDate = new Date();
  		
    	try {
    		if("M".equals(dayMonth)){
    			from += "-01"; 
    			fromDate = transMonDay.parse(from);
    			toDate = transMonDay.parse(to+"-01");
    			
    			Calendar cal = Calendar.getInstance();
    			//Date형의 입력받은 날짜를 Calendar형으로 변환한다.
    			cal.setTime(toDate);
    			int endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    			toDate = Util.setMaxTimeOfDate(transMonDay.parse(to+"-"+endDay));
    		}else if("D".equals(dayMonth)){
    			fromDate = transMonDay.parse(from);
    			toDate = Util.setMaxTimeOfDate(transMonDay.parse(to));
    		}
		} catch (ParseException e) {
			logger.error("ParseException >> dayMonth [{}], from [{}]", dayMonth, from);
			logger.error("ParseException >> dayMonth [{}], to [{}]", dayMonth, to);
		}
		
    	request.setReqStrValue2(dbSelectDay.format(fromDate));
    	request.setReqStrValue3(dbSelectDay.format(toDate));
    	
    	// 조회 하기전 dayMonth(ReqStrValue1) 값을 상점 ID 로 변경 하여 조회 값으로 사용
    	// 해당 내용은 매장주가 조회 할 경우 사용됨!!!!!!!! (mystorealimtalk read 시 사용)
		if(isStoreChk){
			// 상점별로 조회 하기 위해서 사용
			Store store = storeService.getStore(getStoreId(session));
			request.setReqStrValue1(store.getShortName());
		}
		
  		logger.info("isStoreChk [{}]", isStoreChk);
  		logger.info("reqStrValue1 [{}]", request.getReqStrValue1());
    	logger.info("DBfrom [{}], DBto [{}]", request.getReqStrValue2(), request.getReqStrValue3());
		// 알림톡 전송"성공"한 목록을 가져온다. 
		List<StoreAlimTalk> alimList = alimTalkDao.getAllimTalkList(request, isStoreChk);
		
		// SMS 전송"성공"한 목록을 가져온다. 
		List<StoreAlimTalk> smsList = alimTalkDao.getSMSList(request, isStoreChk);
		
		// 알림톡  / SMS 성공목록에서 날짜를 비교하여 값을 넣는다.
		// Map 키는 날짜_shortName으로 하여 구분 MAP에 넣는다.
		// Map 형식 : <날짜_shortName,StoreAlimTalk.java> 
		LinkedHashMap<String, StoreAlimTalk> totalMap = new LinkedHashMap<String, StoreAlimTalk>();
		if( alimList.size() > 0 ){
			for(StoreAlimTalk alim : alimList){
				if("M".equals(dayMonth)){
					
					String mapKey = "", dateChg = "";
					try {
						dateChg = transMon.format(transMon.parse(alim.getDate()));
						mapKey = dateChg + "_" + alim.getShortName();
					} catch (ParseException e) {
						logger.error("ParseException >> alim.getDate() [{}], alim.getShortName() [{}]", alim.getDate(), alim.getShortName());
					}
					if(totalMap.containsKey(mapKey)){
						StoreAlimTalk alimCnt = totalMap.get(mapKey);
						alimCnt.setAlimCnt(alimCnt.getAlimCnt() + alim.getAlimCnt());
						totalMap.put(mapKey, alimCnt);
					}else{
						alim.setDate(dateChg);
						totalMap.put(mapKey, alim);
					}
				}else if("D".equals(dayMonth)){
					String mapKey = alim.getDate() + "_" + alim.getShortName();
					
					totalMap.put(mapKey, alim);	
				}
			}
		}
		if( smsList.size() > 0 ){
			for(StoreAlimTalk sms : smsList){
				if("M".equals(dayMonth)){
					String mapKey = "", dateChg = "";
					try {
						dateChg = transMon.format(transMon.parse(sms.getDate()));
						mapKey = dateChg + "_" + sms.getShortName();
					} catch (ParseException e) {
						logger.error("ParseException >> sms.getDate() [{}], sms.getShortName() [{}]", sms.getDate(), sms.getShortName());
					}
					
					if(totalMap.containsKey(mapKey)){
						StoreAlimTalk alimCnt = totalMap.get(mapKey);
						alimCnt.setSmsCnt(alimCnt.getSmsCnt() + sms.getSmsCnt());
						totalMap.put(mapKey, alimCnt);
					}else{
						sms.setDate(dateChg);
						totalMap.put(mapKey, sms);
					}
				}else if("D".equals(dayMonth)){
					String mapKey = sms.getDate() + "_" + sms.getShortName();
					if(totalMap.containsKey(mapKey)){
						StoreAlimTalk alim = totalMap.get(mapKey);
						alim.setSmsCnt(sms.getSmsCnt());
						totalMap.put(mapKey, alim);
					}else{
						totalMap.put(mapKey, sms);
					}
				}
			}
		}
		
		
		int alimTotal = 0, smsTotal = 0, allTotal = 0;
		List<StoreAlimTalk> list = new ArrayList<StoreAlimTalk>(totalMap.values());
		Collections.sort(list, new CompareMenu()); // 순서를 오름 차순으로 변경
		for(StoreAlimTalk alimTalk : list){
			int total = alimTalk.getAlimCnt() + alimTalk.getSmsCnt();
			alimTalk.setTotalCnt(total);
//			logger.info("alimTalk.getDate() [{}]", alimTalk.getDate());
//			logger.info("alimTalk.getShortName() [{}], alimTalk.getName() [{}]", alimTalk.getShortName(), alimTalk.getName());
//			logger.info("alimTalk.getAlimCnt() [{}], alimTalk.getSmsCnt() [{}]", alimTalk.getAlimCnt(), alimTalk.getSmsCnt());
//			logger.info("==============================");
			
			alimTotal += alimTalk.getAlimCnt();
			smsTotal += alimTalk.getSmsCnt();
			allTotal += total;
		}
		
		// 알림톡 / SMS / 합계 에 대한 내용
		StoreAlimTalk alimOne = new StoreAlimTalk();
		alimOne.setName("합    계");
		alimOne.setAlimCnt(alimTotal);
		alimOne.setSmsCnt(smsTotal);
		alimOne.setTotalCnt(allTotal);
		list.add(alimOne);
		
		res.setTotal(list.size());
		res.setData(list);
		
		return res;
	}
	
    static class CompareMenu implements Comparator<StoreAlimTalk>{
        @Override
        public int compare(StoreAlimTalk o1, StoreAlimTalk o2) {
            return o2.getDate().compareTo(o1.getDate());
        }        
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
}
