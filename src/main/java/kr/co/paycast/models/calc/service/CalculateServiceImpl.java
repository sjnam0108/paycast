/*
 * IRIS(Integrated SW Repository for Information Sharing) version 1.0
 *
 * Copyright �� 2017 kt corp. All rights reserved.
 *
 * This is a proprietary software of kt corp, and you may not use this file except in
 * compliance with license agreement with kt corp. Any redistribution or use of this
 * software, with or without modification shall be strictly prohibited without prior written
 * approval of kt corp, and the copyright notice above does not evidence any actual or
 * intended publication of such software.
 *
 */

package kr.co.paycast.models.calc.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.calc.StoreCalcDay;
import kr.co.paycast.models.calc.StoreCalcMenu;
import kr.co.paycast.models.calc.StoreCalcStats;
import kr.co.paycast.models.calc.dao.CalculateDao;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.self.service.SelfService;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderList;
import kr.co.paycast.models.store.dao.StoreOrderDao;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.calc.CalcItem;
import kr.co.paycast.viewmodels.calc.CalcStatsItem;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("CalculateService")
public class CalculateServiceImpl implements CalculateService {
	private static final Logger logger = LoggerFactory.getLogger(CalculateServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private CalculateDao calculateDao;
    
    @Autowired
    private StoreOrderDao storeOrderDao;
    
    @Autowired
    private SelfService selfService;

    @Autowired 
    private StoreService storeService;
    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	// 정기적으로 월간 정산을 하는 로직으로 변경(2019.01.16)
	// defalt : 서버 실행 후 다음날 새벽 3시 부터 실행(단! 해당 시간은 변경 가능)
	@Override
	public void updateTermly() {
    	try{
    		List<Store> storeList = storeService.getEffectiveStoreList();
    		for(Store store : storeList) {
				operEnd(String.valueOf(store.getId()), null);
    		}
    	}catch(Exception e){
    		logger.error("updateTermly >> save", e);
    		throw new ServerOperationForbiddenException("SaveError");
    	}
	}

	private List<CalcItem> orderListDateFromTo(Date fromDate1, Date toDate2, int storeId){
		List<CalcItem> calcItemList = new ArrayList<CalcItem>();
        List<StoreOrder> orderList = storeOrderDao.getOrderListDate(fromDate1, toDate2, storeId);
        if(orderList.size() > 0){
        	for(StoreOrder order : orderList){
        		
        		//2019.04.24 조회시 orderCancelStatus : "Y" 완료된 상태가 되었을 경우 매출내역에서 제외
        		String cancelStatus = order.getOrderCancelStatus();
        		if(!"Y".equals(cancelStatus)){
	        		String orderNumber = order.getOrderNumber();
	        		
	        		List<StoreOrderList> orderMenuList = storeOrderDao.getOrderList(orderNumber);
	        		if(orderMenuList.size() > 0){
	        			for(StoreOrderList storeOrderList : orderMenuList){
	        				String compSelect = String.valueOf(storeOrderList.getOrderMenuId()); // 메뉴 ID + 옵션 ID로 구성된 값
	        				
	        				if(!Util.isNotValid(storeOrderList.getOrderSelectEss())){
								String[] menu = storeOrderList.getOrderSelectEss().split(",");
								for(int t=0; t < menu.length; t++){
									if(!Util.isNotValid(menu[t])){
										compSelect += "_"+ menu[t];
									}
								}
	        				}
	        				
	        				if(!Util.isNotValid(storeOrderList.getOrderSelectAdd())){
	        					String[] menu = storeOrderList.getOrderSelectAdd().split(",");
								for(int t=0; t < menu.length; t++){
									if(!Util.isNotValid(menu[t])){
										compSelect += "_"+ menu[t];
									}
								}
	        				}
	        				
	        				logger.info( "ReFill 변경전 >>> compSelect[{}]", compSelect);
	        				if("RF".equals(order.getPayment()) && "I".equals(order.getOrderType())){
	        					// 2019.11.19 각각의 메뉴별로 리필 자료를 모을 경우 사용
	        					// compSelect = "ReFill_"+compSelect;
	        					// 2019.11.19 리필의 모든 메뉴를 넣기위해서 사용
	        					compSelect = "ReFill";
	        				}
	        				logger.info( "ReFill 변경후 >>> compSelect[{}]", compSelect);
	        				CalcItem calcItem = new CalcItem(order, storeOrderList, orderNumber, compSelect);
	        				calcItemList.add(calcItem);
	        			}
	        		}
        		}
        	}
        }
		
		return calcItemList;
	}
	
	private HashMap<String, Object> storeCalcDayMap(List<CalcItem> calcItemList, SimpleDateFormat transFormat, 
					int storeId, SimpleDateFormat transFormatDay, HttpSession session){
		
		 HashMap<String, Object> monDayMap = new HashMap<String, Object>();
		
    	for(CalcItem calcItem : calcItemList){
    		StoreOrder storeOrder = calcItem.getStoreOrder();
    		StoreOrderList storeOrderList = calcItem.getStoreOrderList();
    		Date creaDate = storeOrder.getWhoCreationDate();
    		
    		// 날짜 MAP 에서 해당 날짜가 있는지 확인한다.
    		String orderMonthDay = transFormat.format(creaDate);
    		if(monDayMap.containsKey(orderMonthDay)){
    			// 해당 날짜의 MAP 값을 가져와서 메뉴 MAP에서 메뉴ID로 주문 수량을 가져온다.
    			List<StoreCalcDay> menuAmountList = (List<StoreCalcDay>)monDayMap.get(orderMonthDay);
    			
    			// 주문 수량을 가져와서 다른 값을 더해준다.
    			// 메뉴 MAP에 메뉴ID에 맞게 다시 값을 넣어준다.
    			String menu = calcItem.getCompSelect();
    			if(!"ReFill".equals(menu)){
    				menu += "_" + storeOrderList.getOrderMenuAmt();
    			}
    			boolean isIdCheck = true; 
    			if(menuAmountList.size() > 0){
    				for(StoreCalcDay oneCalcDay : menuAmountList){
    					String menuOne = oneCalcDay.getCompSelect();
    					
//    					logger.info("추가로 금액을 더해야 하는   menu [{}], 이전에 저장되어 있던 CompSelect [{}]", menu, menuOne);
    					
    					if(menu.equals(menuOne)){
    						int calcAmount = oneCalcDay.getOrderMenuAmount();
    						int orderAmount = storeOrderList.getOrderMenuAmount();
    						
    						oneCalcDay.setOrderMenuAmount(calcAmount+orderAmount);
    						isIdCheck = false;
    					}
    					
    					if(!isIdCheck){
    						break;
    					}
    				}
    			}
    			
    			if(isIdCheck){
    				String menuName = storeOrderList.getOrderMenuName();
					// 2019.11.19 리필의 모든 메뉴를 넣기위해서 사용
    				if("ReFill".equals(menu)){
    				 	menuName = "리필";
    				}
    				
//    				logger.info("calcItem.getCompSelect() [{}], menuName [{}]", calcItem.getCompSelect()+ "_" + storeOrderList.getOrderMenuAmt(), menuName);
        			StoreCalcDay storeCalcDayOne = new StoreCalcDay(storeId, creaDate, transFormat.format(creaDate), Util.parseInt(transFormatDay.format(creaDate)), storeOrderList.getOrderMenuId(), 
        					menuName, storeOrderList.getOrderMenuAmount(), storeOrderList.getOrderMenuAmt(), storeOrderList.getOrderSelectEss(), storeOrderList.getOrderSelectAdd(), session);
        			storeCalcDayOne.setCompSelect(calcItem.getCompSelect()+ "_" + storeOrderList.getOrderMenuAmt());
	        		
	        		menuAmountList.add(storeCalcDayOne);
    			}
    			
    			monDayMap.put(orderMonthDay, menuAmountList);
    			
    		}else{
    			List<StoreCalcDay> menuAmountList = new ArrayList<StoreCalcDay>();
    			
    			String compSelect = calcItem.getCompSelect();
				String menuName = storeOrderList.getOrderMenuName();
				// 2019.11.19 리필의 모든 메뉴를 넣기위해서 사용
				if("ReFill".equals(calcItem.getCompSelect())){
				 	menuName = "리필";
				}else{
					compSelect += "_" + storeOrderList.getOrderMenuAmt();
				}
				
//				logger.info("compSelect [{}], menuName [{}]", compSelect, menuName);
				
    			StoreCalcDay storeCalcDayOne = new StoreCalcDay(storeId, creaDate, transFormat.format(creaDate), Util.parseInt(transFormatDay.format(creaDate)), storeOrderList.getOrderMenuId(), 
    					menuName, storeOrderList.getOrderMenuAmount(), storeOrderList.getOrderMenuAmt(), storeOrderList.getOrderSelectEss(), storeOrderList.getOrderSelectAdd(), session);
    			storeCalcDayOne.setCompSelect(compSelect);
    			
        		menuAmountList.add(storeCalcDayOne);
        		
        		monDayMap.put(orderMonthDay, menuAmountList);
    		}
    	}
		
    	return monDayMap;
	}
	
	private HashMap<String, Object> storeCalcMonthMap(List<CalcItem> calcItemList, SimpleDateFormat transFormatMonth,
					int storeId, SimpleDateFormat transFormatMonthInt, HttpSession session) {
		HashMap<String, Object> monthMap = new HashMap<String, Object>();
		
    	for(CalcItem calcItem : calcItemList){
    		StoreOrder storeOrder = calcItem.getStoreOrder();
    		StoreOrderList storeOrderList = calcItem.getStoreOrderList();
    		Date creaDate = storeOrder.getWhoCreationDate();
    		// 날짜 MAP 에서 해당 날짜가 있는지 확인한다.
    		String orderMonthDay = transFormatMonth.format(creaDate);
    		if(monthMap.containsKey(orderMonthDay)){
    			// 해당 날짜의 MAP 값을 가져와서 메뉴 MAP에서 메뉴ID로 주문 수량을 가져온다.
    			List<StoreCalcStats> menuAmountList = (List<StoreCalcStats>)monthMap.get(orderMonthDay);
    			
    			// 주문 수량을 가져와서 다른 값을 더해준다.
    			// 메뉴 MAP에 메뉴ID에 맞게 다시 값을 넣어준다.
    			String menu = calcItem.getCompSelect();
    			if(!"ReFill".equals(menu)){
    				menu += "_" + storeOrderList.getOrderMenuAmt();
    			}
    			boolean isIdCheck = true; 
    			if(menuAmountList.size() > 0){
    				for(StoreCalcStats oneCalcMonth : menuAmountList){
    					String menuOne = oneCalcMonth.getCompSelect();
    					
    					if(menu.equals(menuOne)){
    						int calcAmount = oneCalcMonth.getOrderMenuAmount();
    						int orderAmount = storeOrderList.getOrderMenuAmount();
    						oneCalcMonth.setOrderMenuAmount(calcAmount+orderAmount);
    						isIdCheck = false;
    					}
    					
    					if(!isIdCheck){
    						break;
    					}
    				}
    			}
    			
    			if(isIdCheck){
    				String menuName = storeOrderList.getOrderMenuName();
					// 2019.11.19 리필의 모든 메뉴를 넣기위해서 사용
    				if("ReFill".equals(menu)){
    				 	menuName = "리필";
    				}

        			StoreCalcStats storeCalcMonthOne = new StoreCalcStats(storeId, creaDate, transFormatMonth.format(creaDate), Util.parseInt(transFormatMonthInt.format(creaDate)), storeOrderList.getOrderMenuId(), 
        					menuName, storeOrderList.getOrderMenuAmount(), storeOrderList.getOrderMenuAmt(), storeOrderList.getOrderSelectEss(), storeOrderList.getOrderSelectAdd(), calcItem.getCompSelect()+ "_" + storeOrderList.getOrderMenuAmt(), session);
        			
	        		menuAmountList.add(storeCalcMonthOne);
    			}
    			
    			monthMap.put(orderMonthDay, menuAmountList);
    			
    		}else{
    			List<StoreCalcStats> menuAmountList = new ArrayList<StoreCalcStats>();

    			String compSelect = calcItem.getCompSelect();
				String menuName = storeOrderList.getOrderMenuName();
				// 2019.11.19 리필의 모든 메뉴를 넣기위해서 사용
				if("ReFill".equals(compSelect)){
					menuName = "리필";
				}else{
					compSelect += "_" + storeOrderList.getOrderMenuAmt();
				}
				
    			StoreCalcStats storeCalcMonthOne = new StoreCalcStats(storeId, creaDate, transFormatMonth.format(creaDate), Util.parseInt(transFormatMonthInt.format(creaDate)), storeOrderList.getOrderMenuId(), 
    					menuName, storeOrderList.getOrderMenuAmount(), storeOrderList.getOrderMenuAmt(), storeOrderList.getOrderSelectEss(), storeOrderList.getOrderSelectAdd(), compSelect, session);
    			
        		menuAmountList.add(storeCalcMonthOne);
        		
        		monthMap.put(orderMonthDay, menuAmountList);
    		}
    	}
		
		return monthMap;
	}
	
	private HashMap<Integer, Object> storeCalcMenuMap(List<CalcItem> calcItemList, SimpleDateFormat transFormat,
			int storeId, HttpSession session) {
		HashMap<Integer, Object> idNameMap = new HashMap<Integer, Object>();
		
		for(CalcItem calcItem : calcItemList){
    		StoreOrder storeOrder = calcItem.getStoreOrder();
    		StoreOrderList storeOrderList = calcItem.getStoreOrderList();
    		
    		// 날짜 MAP 에서 해당 날짜가 있는지 확인한다.
    		int orderMenuId = storeOrderList.getOrderMenuId();
    		if(idNameMap.containsKey(orderMenuId)){
    			// 해당 매뉴 번호의  MAP 값을 가져와서 메뉴 MAP에서 메뉴ID로 주문 수량을 가져온다.
    			List<StoreCalcMenu> menuAmountList = (List<StoreCalcMenu>)idNameMap.get(orderMenuId);
    			
    			boolean isMenuCheck = true;
    			String menu = calcItem.getCompSelect();
    			if(!"ReFill".equals(menu)){
    				menu += "_" + storeOrderList.getOrderMenuAmt()+ "_" + storeOrderList.getOrderMenuName();
    			}
    			
    			String menuDate =  transFormat.format(storeOrderList.getWhoCreationDate()); 
    			if(menuAmountList.size() > 0){
    				for(StoreCalcMenu oneCalcMenu : menuAmountList){
    					String monthDay = oneCalcMenu.getOrderMonthDay();
    					String menuOne = oneCalcMenu.getCompSelect();
    					
    					if(menu.equals(menuOne) && menuDate.equals(monthDay)){
    						int calcAmount = oneCalcMenu.getOrderMenuAmount();
    						int orderAmount = storeOrderList.getOrderMenuAmount();
    						oneCalcMenu.setOrderMenuAmount(calcAmount+orderAmount);
    						isMenuCheck = false;
    					}
    					
    					if(!isMenuCheck){
    						break;
    					}
    				}
    			}
    			
    			if(isMenuCheck){
    				String menuName = storeOrderList.getOrderMenuName();
					// 2019.11.19 리필의 모든 메뉴를 넣기위해서 사용
    				logger.info("ReFill.equals(menu) [{}]", "ReFill".equals(menu));
    				if("ReFill".equals(menu)){
    				 	menuName = "리필";
    				}
    				
    				StoreCalcMenu storeCalcMenuOne = new StoreCalcMenu(storeId, storeOrder.getWhoCreationDate(), storeOrderList.getOrderMenuId(), menuName,  
    						transFormat.format(storeOrder.getWhoCreationDate()), storeOrderList.getOrderMenuAmount(), storeOrderList.getOrderMenuAmt(), storeOrderList.getOrderSelectEss(), storeOrderList.getOrderSelectAdd(), 
    						calcItem.getCompSelect(), session);
	        		
	        		menuAmountList.add(storeCalcMenuOne);
    			}
    			
    			idNameMap.put(orderMenuId, menuAmountList);
    			
    		}else{
				List<StoreCalcMenu> menuAmountList = new ArrayList<StoreCalcMenu>();
				String compSelect = calcItem.getCompSelect();
				String menuName = storeOrderList.getOrderMenuName();
				// 2019.11.19 리필의 모든 메뉴를 넣기위해서 사용
				logger.info("ReFill.equals(menu) [{}]", "ReFill".equals(calcItem.getCompSelect()));
				if("ReFill".equals(calcItem.getCompSelect())){
				 	menuName = "리필";
				}else{
					compSelect += "_" + storeOrderList.getOrderMenuAmt() + "_" + menuName;
				}
				
				StoreCalcMenu storeCalcMenuOne = new StoreCalcMenu(storeId, storeOrder.getWhoCreationDate(), storeOrderList.getOrderMenuId(), menuName,  
						transFormat.format(storeOrder.getWhoCreationDate()), storeOrderList.getOrderMenuAmount(), storeOrderList.getOrderMenuAmt(), storeOrderList.getOrderSelectEss(), storeOrderList.getOrderSelectAdd(), 
						compSelect, session);
				
	    		menuAmountList.add(storeCalcMenuOne);
	    		
	    		idNameMap.put(orderMenuId, menuAmountList);
			}
    	}
		
		return idNameMap;
	}

	@Override
	public List<CalcStatsItem> getCalcDayRead(String fromDate, String toDate, int storeId) {
		List<CalcStatsItem> calcStatsItemList = new ArrayList<CalcStatsItem>();
		SimpleDateFormat transMonDay = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat deChg = new DecimalFormat("###,###,###,###");    
		try {
	    	Date fromDate1 = transMonDay.parse(fromDate);
	    	Date toDate2 = Util.setMaxTimeOfDate(transMonDay.parse(toDate));
			List<StoreCalcDay> calcDayList = calculateDao.calcDayList(fromDate1, toDate2, storeId);
			
			String compareDate = "";
			int totalAmtCalC = 0;
			boolean firestCheck = false;
			if(calcDayList.size() > 0){
				for(StoreCalcDay calcDay : calcDayList){
					CalcStatsItem statsItemOne = new CalcStatsItem();
					if(compareDate.equals(calcDay.getOrderMonthDay())){
						statsItemOne.setStatsDay("");	
					}else{
						if(firestCheck){
							CalcStatsItem statsItemOne1 = new CalcStatsItem();
							statsItemOne1.setBoardYn("Y");
							statsItemOne1.setStatsDay("");
							statsItemOne1.setStatsMenu("");
							statsItemOne1.setStatsAmount("");
							statsItemOne1.setStatsAmt("매출합계");
							statsItemOne1.setStatsSalesamt(deChg.format(totalAmtCalC));
							calcStatsItemList.add(statsItemOne1);	
						}else{
							firestCheck = true;
						}
						
						statsItemOne.setStatsDay(calcDay.getOrderMonthDay());
						totalAmtCalC = 0;
						compareDate = calcDay.getOrderMonthDay();
					}
					
					
	        		String menu = calcDay.getOrderMenuName();
	        		if(!"ReFill".equals(calcDay.getCompSelect())){
	        			if(!Util.isNotValid(calcDay.getSelectEss())){
	        				String[] menu1 = calcDay.getSelectEss().split(",");
	        				for(int t=0; t < menu1.length; t++){
	        					if(!Util.isNotValid(menu1[t])){
	        						String[] menu2 = menu1[t].split("_");
	        						if(menu2.length > 0){
	        							String[] menu3 = menu2[1].split("\\(");
										if(!"".equals(menu) && menu3.length > 1){
											menu += "<br > - ";
										}
										menu += PayUtil.separationistName(menu3);
	        						}
	        					}
	        				}
	        			}
	        			if(!Util.isNotValid(calcDay.getSelectAdd())){
	        				String[] menu1 = calcDay.getSelectAdd().split(",");
	        				for(int t=0; t < menu1.length; t++){
	        					if(!Util.isNotValid(menu1[t])){
	        						String[] menu2 = menu1[t].split("_");
	        						if(menu2.length > 0){
	        							String[] menu3 = menu2[1].split("\\|\\|");
	        							if(menu3.length > 0){
	        								for(int ttt=0; ttt < menu3.length; ttt++){
	        									String[] menu4 = menu3[ttt].split("\\(");
	        									
												if(!"".equals(menu) && menu4.length > 1){
													menu += "<br > - ";
												}
												menu += PayUtil.separationistName(menu4);
	        								}
	        							}
	        						}
	        					}
	        				}
	        			}
	        		}
					
					statsItemOne.setStatsMenu(menu);
					statsItemOne.setStatsAmount(String.valueOf(calcDay.getOrderMenuAmount()));
					statsItemOne.setStatsAmt(deChg.format(calcDay.getOrderMenuAmt()));
					
					int multiply = calcDay.getOrderMenuAmount() * calcDay.getOrderMenuAmt();
					statsItemOne.setStatsSalesamt(deChg.format(multiply));
					
					totalAmtCalC = totalAmtCalC + multiply;
					calcStatsItemList.add(statsItemOne);
				}
				// 마지막 매출에 대한 합산 값 넣기
				CalcStatsItem statsItemOne1 = new CalcStatsItem();
				statsItemOne1.setBoardYn("Y");
				statsItemOne1.setStatsDay("");
				statsItemOne1.setStatsMenu("");
				statsItemOne1.setStatsAmount("");
				statsItemOne1.setStatsAmt("매출합계");
				statsItemOne1.setStatsSalesamt(deChg.format(totalAmtCalC));
				calcStatsItemList.add(statsItemOne1);
			}
		} catch (Exception e) {
    		logger.error("CalcStatsItem Read", e);
    		throw new ServerOperationForbiddenException("ReadError");
		}
		
		return calcStatsItemList;
	}

	@Override
	public List<CalcStatsItem> getCalcMonthRead(String fromDate, String toDateSt, int storeId) {
		List<CalcStatsItem> calcStatsItemList = new ArrayList<CalcStatsItem>();
		SimpleDateFormat transMonDay = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat deChg = new DecimalFormat("###,###,###,###");
		try {
	    	fromDate += "-01"; 
			Date fromDate1 = transMonDay.parse(fromDate);
			
			Date toDate = transMonDay.parse(toDateSt+"-01");
			Calendar cal = Calendar.getInstance();
			//Date형의 입력받은 날짜를 Calendar형으로 변환한다.
			cal.setTime(toDate);
			int endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			Date toDate2 = Util.setMaxTimeOfDate(transMonDay.parse(toDateSt+"-"+endDay));
	    	
			HashMap<String, Object> monthMap = new HashMap<String, Object>();
	    	List<StoreCalcStats> calcMonthListOri = calculateDao.calcMonthList(fromDate1, toDate2, storeId);
	    	
	    	//조회된  메뉴에 대한  개수를 더하는 작업을 한다. (메뉴별) - 시작
			for(StoreCalcStats list : calcMonthListOri){
	    		// 날짜 MAP 에서 해당 날짜가 있는지 확인한다.
	    		String compSelect = list.getOrderMonth() + "_" + list.getCompSelect();
	    		logger.info("compSelect [{}]", compSelect);
	    		if(monthMap.containsKey(compSelect)){
	    			StoreCalcStats monthList = (StoreCalcStats) monthMap.get(compSelect);
	    			
	    			monthList.setOrderMenuAmount(monthList.getOrderMenuAmount() + list.getOrderMenuAmount());
	    			
	    			monthMap.put(compSelect, monthList);
	    		}else{
	    			StoreCalcStats newData = new StoreCalcStats();
	    			newData.setCompSelect(list.getCompSelect());
	    			newData.setOrderCreation(list.getOrderCreation());
	    			newData.setStoreId(list.getStoreId());
	    			newData.setOrderMonth(list.getOrderMonth());
	    			newData.setOrderMonthInt(list.getOrderMonthInt());
	    			newData.setOrderMenuId(list.getOrderMenuId());
	    			newData.setOrderMenuName(list.getOrderMenuName());
	    			newData.setOrderMenuAmount(list.getOrderMenuAmount());
	    			newData.setOrderMenuAmt(list.getOrderMenuAmt());
	    			newData.setSelectEss(list.getSelectEss());
	    			newData.setSelectAdd(list.getSelectAdd());
	    			newData.setWhoCreationDate(list.getWhoCreationDate());
	    			newData.setWhoLastUpdateDate(list.getWhoLastUpdateDate());
	    			newData.setWhoCreatedBy(list.getWhoCreatedBy());
	    			newData.setWhoLastUpdatedBy(list.getWhoLastUpdatedBy());
	    			monthMap.put(compSelect, newData);
	    		}
			}
			
			List<StoreCalcStats> calcMonthList = new ArrayList<StoreCalcStats>();
        	Iterator<String> keysMonthMap = monthMap.keySet().iterator(); 
        	while( keysMonthMap.hasNext() ){ 
        		String key = keysMonthMap.next(); 
        		StoreCalcStats oneCalcStats = (StoreCalcStats)monthMap.get(key);
        		
    			calcMonthList.add(oneCalcStats);
        	}
        	Collections.sort(calcMonthList, new CompareSeqDesc()); // 순서를 오름 차순으로 변경

        	logger.info("================================================");
        	
	    	//조회된  메뉴에 대한  개수를 더하는 작업을 한다. (메뉴별) - 종료
	    	
        	//화면에 보여주는 부분을 만든다. - 시작
			String compareDate = "";
			int totalAmtCalC = 0;
			boolean firestCheck = false;
			if(calcMonthList.size() > 0){
				for(StoreCalcStats calcMonth : calcMonthList){
					CalcStatsItem statsItemOne = new CalcStatsItem();
					if(compareDate.equals(calcMonth.getOrderMonth())){
						statsItemOne.setStatsMonth("");	
					}else{
						if(firestCheck){
							CalcStatsItem statsItemOne1 = new CalcStatsItem();
							statsItemOne1.setBoardYn("Y");
							statsItemOne1.setStatsMonth("");
							statsItemOne1.setStatsMenu("");
							statsItemOne1.setStatsAmount("");
							statsItemOne1.setStatsAmt("매출합계");
							statsItemOne1.setStatsSalesamt(deChg.format(totalAmtCalC));
							calcStatsItemList.add(statsItemOne1);	
						}else{
							firestCheck = true;
						}
						
						statsItemOne.setStatsMonth(calcMonth.getOrderMonth());
						totalAmtCalC = 0;
						compareDate = calcMonth.getOrderMonth();
					}
					
	        		String menu = calcMonth.getOrderMenuName();
	        		
	        		if(!"ReFill".equals(calcMonth.getCompSelect())){
	        			if(!Util.isNotValid(calcMonth.getSelectEss())){
	        				String[] menu1 = calcMonth.getSelectEss().split(",");
	        				for(int t=0; t < menu1.length; t++){
	        					if(!Util.isNotValid(menu1[t])){
	        						String[] menu2 = menu1[t].split("_");
	        						if(menu2.length > 0){
	        							String[] menu3 = menu2[1].split("\\(");
	        							if(!"".equals(menu) && menu3.length > 1){
	        								menu += "<br > - ";
	        							}
	        							menu += PayUtil.separationistName(menu3);
	        						}
	        					}
	        				}
	        			}
	        			if(!Util.isNotValid(calcMonth.getSelectAdd())){
	        				String[] menu1 = calcMonth.getSelectAdd().split(",");
	        				for(int t=0; t < menu1.length; t++){
	        					if(!Util.isNotValid(menu1[t])){
	        						String[] menu2 = menu1[t].split("_");
	        						if(menu2.length > 0){
	        							String[] menu3 = menu2[1].split("\\|\\|");
	        							if(menu3.length > 0){
	        								for(int ttt=0; ttt < menu3.length; ttt++){
	        									String[] menu4 = menu3[ttt].split("\\(");
	        									
	        									if(!"".equals(menu) && menu4.length > 1){
	        										menu += "<br > - ";
	        									}
	        									menu += PayUtil.separationistName(menu4);
	        								}
	        							}
	        						}
	        					}
	        				}
	        			}
	        		}
					statsItemOne.setStatsMenu(menu);
					
					logger.info(" list.getOrderMenuName() >>>> [{}] /  calcMonth.getOrderMenuAmount() >>>> [{}]", calcMonth.getOrderMenuName(), calcMonth.getOrderMenuAmount());;
					statsItemOne.setStatsAmount(String.valueOf(calcMonth.getOrderMenuAmount()));
					statsItemOne.setStatsAmt(deChg.format(calcMonth.getOrderMenuAmt()));
					int multiply = calcMonth.getOrderMenuAmount() * calcMonth.getOrderMenuAmt();
					statsItemOne.setStatsSalesamt(deChg.format(multiply));
					statsItemOne.setCompSelect(calcMonth.getCompSelect());
					totalAmtCalC = totalAmtCalC + multiply;
					calcStatsItemList.add(statsItemOne);
				}
				// 마지막 매출에 대한 합산 값 넣기
				CalcStatsItem statsItemOne1 = new CalcStatsItem();
				statsItemOne1.setBoardYn("Y");
				statsItemOne1.setStatsMonth("");
				statsItemOne1.setStatsMenu("");
				statsItemOne1.setStatsAmount("");
				statsItemOne1.setStatsAmt("매출합계");
				statsItemOne1.setStatsSalesamt(deChg.format(totalAmtCalC));
				calcStatsItemList.add(statsItemOne1);
				
	        	//화면에 보여주는 부분을 만든다. - 종료
			}
			
		} catch (Exception e) {
    		logger.error("CalcStatsItem Read", e);
    		throw new ServerOperationForbiddenException("ReadError");
		}
		return calcStatsItemList;
	}

	@Override
	public List<CalcStatsItem> getCalcMenuRead(String fromDate, String toDate, int storeId) {
		List<CalcStatsItem> calcStatsItemList = new ArrayList<CalcStatsItem>();
		SimpleDateFormat transMonDay = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat deChg = new DecimalFormat("###,###,###,###");    
		try {
	    	Date fromDate1 = transMonDay.parse(fromDate);
	    	Date toDate2 = Util.setMaxTimeOfDate(transMonDay.parse(toDate));
		
        	List<StoreCalcMenu> calcMenuList = calculateDao.calcMenuList(fromDate1, toDate2, storeId);
        	Collections.sort(calcMenuList, new CompareMenu()); // 순서를 오름 차순으로 변경
        	
        	String compare = "";
			int totalAmtCalC = 0;
			boolean firestCheck = false;
			if(calcMenuList.size() > 0){
				for(StoreCalcMenu calcMenu : calcMenuList){
					
					CalcStatsItem statsItemOne = new CalcStatsItem();
					if(compare.equals(calcMenu.getCompSelect())){
						statsItemOne.setStatsMenu("");
					}else{
						if(firestCheck){
							CalcStatsItem statsItemOne1 = new CalcStatsItem();
							statsItemOne1.setBoardYn("Y");
							statsItemOne1.setStatsMenu("");
							statsItemOne1.setStatsDay("");
							statsItemOne1.setStatsAmount("");
							statsItemOne1.setStatsAmt("매출합계");
							statsItemOne1.setStatsSalesamt(deChg.format(totalAmtCalC));
							calcStatsItemList.add(statsItemOne1);	
						}else{
							firestCheck = true;
						}
						String menu = calcMenu.getOrderMenuName();
						if(!"ReFill".equals(calcMenu.getCompSelect())){
			        		if(!Util.isNotValid(calcMenu.getSelectEss())){
								String[] menu1 = calcMenu.getSelectEss().split(",");
								for(int t=0; t < menu1.length; t++){
									if(!Util.isNotValid(menu1[t])){
										String[] menu2 = menu1[t].split("_");
										if(menu2.length > 0){
											String[] menu3 = menu2[1].split("\\(");
											if(!"".equals(menu) && menu3.length > 1){
												menu += "<br > - ";
											}
											menu += PayUtil.separationistName(menu3);
										}
									}
								}
			        		}
			        		if(!Util.isNotValid(calcMenu.getSelectAdd())){
								String[] menu1 = calcMenu.getSelectAdd().split(",");
								for(int t=0; t < menu1.length; t++){
									if(!Util.isNotValid(menu1[t])){
										String[] menu2 = menu1[t].split("_");
										if(menu2.length > 0){
											String[] menu3 = menu2[1].split("\\|\\|");
											if(menu3.length > 0){
												for(int ttt=0; ttt < menu3.length; ttt++){
													String[] menu4 = menu3[ttt].split("\\(");
													if(!"".equals(menu) && menu4.length > 1){
														menu += "<br > - ";
													}
													menu += PayUtil.separationistName(menu4);
												}
											}
										}
									}
								}
			        		}
						}
						
						statsItemOne.setStatsMenu(menu);
						
						totalAmtCalC = 0;
						compare = calcMenu.getCompSelect();
					}
					statsItemOne.setStatsDay(calcMenu.getOrderMonthDay());
					statsItemOne.setStatsAmount(String.valueOf(calcMenu.getOrderMenuAmount()));
					statsItemOne.setStatsAmt(deChg.format(calcMenu.getOrderMenuAmt()));
					int multiply = calcMenu.getOrderMenuAmount() * calcMenu.getOrderMenuAmt();
					statsItemOne.setStatsSalesamt(deChg.format(multiply));
					
					totalAmtCalC = totalAmtCalC + multiply;
					calcStatsItemList.add(statsItemOne);
				}
				
				// 마지막 매출에 대한 합산 값 넣기
				CalcStatsItem statsItemOne1 = new CalcStatsItem();
				statsItemOne1.setBoardYn("Y");
				statsItemOne1.setStatsMenu("");
				statsItemOne1.setStatsDay("");
				statsItemOne1.setStatsAmount("");
				statsItemOne1.setStatsAmt("매출합계");
				statsItemOne1.setStatsSalesamt(deChg.format(totalAmtCalC));
				calcStatsItemList.add(statsItemOne1);
			}
		} catch (Exception e) {
    		logger.error("CalcStatsItem Read", e);
    		throw new ServerOperationForbiddenException("ReadError");
		}
		return calcStatsItemList;
	}
	
	
	@Override
	public boolean operEnd(String storeId, HttpSession session) {
		logger.info("[bbmc] storeId >>> [{}]", storeId);
		
		boolean success = false;
		Store store = storeService.getStore(Util.parseInt(storeId));
		if(store != null){
			try{
				SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat transFormatMonth = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat transFormatDay = new SimpleDateFormat("dd");
				SimpleDateFormat transFormatMonthInt = new SimpleDateFormat("MM");
				
				Calendar afterTime = Calendar.getInstance();
				Date toDate = afterTime.getTime();
				// 최종 마감 정산 시간
				Date operDate = store.getStoreOpt().getOperEndDate();
				
				// 1. 마감 정산을 할 경우 기준 점이 되는 처음 시작이 없을 경우 하루 전 시간을 기준으로 마감정산 작업을 시작 
				// 2. 마감 정산 시간이 10분 안으로 되어 있으면 마감 정산을 하지 않는다. 
				if(operDate != null){
					long operDateTime = operDate.getTime();
					long curDateTime = toDate.getTime();
					
			    	//분으로 표현
			    	long minute = (curDateTime - operDateTime) / 60000;
			    	logger.info("[bbmc] minute >>> [{}]분 차이", minute);
			    	if(minute < 10){
			    		return true;
			    	}else{
			    		afterTime.setTime(operDate);
						afterTime.set(Calendar.HOUR_OF_DAY , 00);
						afterTime.set(Calendar.MINUTE, 00);
						afterTime.set(Calendar.SECOND, 00);
						afterTime.set(Calendar.MILLISECOND, 0);
						operDate = afterTime.getTime();
						
			    	}
				}else{
					// 마감 정산을 시작 하면 오늘 전날 부터 지금 시간 까지 마감처리를 시작한다. 
					afterTime.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
					afterTime.set(Calendar.HOUR_OF_DAY , 00);
					afterTime.set(Calendar.MINUTE, 00);
					afterTime.set(Calendar.SECOND, 00);
					afterTime.set(Calendar.MILLISECOND, 0);
					operDate = afterTime.getTime();
				}
				
		    	// 정산 작업 관련 시간 저장 
		    	store.getStoreOpt().setOperEndDate(toDate);
		    	storeService.saveOrUpdate(store.getStoreOpt());
		    	
		    	
				// 해당 당월은 모든 데이틀 삭제 하고 다시 입력한다.
				List<StoreCalcDay> calcDayList = calculateDao.calcDayList(operDate, toDate, store.getId());
				logger.info("calcDayList [{}]", calcDayList.size());
				
				calculateDao.calcDayDelete(calcDayList);
				
				List<StoreCalcStats> calcMonthList = calculateDao.calcMonthList(operDate, toDate, store.getId());
				logger.info("calcMonthList [{}]", calcMonthList.size());
				
				calculateDao.calcMonthDelete(calcMonthList);
				
				List<StoreCalcMenu> calcMenuList = calculateDao.calcMenuList(operDate, toDate, store.getId());
				logger.info("calcMenuList [{}]", calcMenuList.size());
				
				calculateDao.calcMenuDelete(calcMenuList);

				List<CalcItem> calcItemList = orderListDateFromTo(operDate, toDate, store.getId());
				logger.info("[bbmc] calcItemList.size >>> [{}]", calcItemList.size());
				
		        // 날짜별로 일별매출 통계 조회를 할수 있게 만들어 준다.
		        if(calcItemList.size() > 0){
		        	logger.info("일별매출 통계 조회 시작 ");
		        	
		        	//1. 날짜별로 모을수 있는 MAP을 만든다. 
		        	HashMap<String, Object> monDayMap = storeCalcDayMap(calcItemList, transFormat, store.getId(), transFormatDay, session);
		        	
		        	Iterator<String> keys = monDayMap.keySet().iterator(); 
		        	while( keys.hasNext() ){ 
		        		String key = keys.next(); 
		        		List<StoreCalcDay> menuAmountList = (List<StoreCalcDay>)monDayMap.get(key);
		        		for(StoreCalcDay oneCalcDay : menuAmountList){
		        			calculateDao.saveOrUpdate(oneCalcDay);
		        		}
		        	}
		        	
		        	logger.info("일별매출 통계 조회 종료 ");
		        	logger.info("월별매출 통계 조회 시작 ");
		        	
		        	//2. 월별로 모을수 있는 MAP을 만든다. 
		        	HashMap<String, Object> monthMap = storeCalcMonthMap(calcItemList, transFormatMonth, store.getId(), transFormatMonthInt, session);
		        	Iterator<String> keysMonthMap = monthMap.keySet().iterator(); 
		        	while( keysMonthMap.hasNext() ){ 
		        		String key = keysMonthMap.next(); 
		        		List<StoreCalcStats> menuAmountList = (List<StoreCalcStats>)monthMap.get(key);
		        		for(StoreCalcStats oneCalcStats : menuAmountList){
		        			calculateDao.saveOrUpdate(oneCalcStats);
		        		}
		        	}
		        	
		        	logger.info("월별매출 통계 조회 종료 ");
		        	logger.info("메뉴별매출 통계 조회 시작 ");
		        	
		        	//3. 메뉴별로 모을수 있는 MAP을 만든다. 
		        	HashMap<Integer, Object> menuMap = storeCalcMenuMap(calcItemList, transFormat, store.getId(), session);
		        	Iterator<Integer> keysMenuMap = menuMap.keySet().iterator(); 
		        	while( keysMenuMap.hasNext() ){ 
		        		Integer key = keysMenuMap.next(); 
		        		List<StoreCalcMenu> menuAmountList = (List<StoreCalcMenu>)menuMap.get(key);
		        		for(StoreCalcMenu oneCalcMenu : menuAmountList){
		        			calculateDao.saveOrUpdate(oneCalcMenu);
		        		}
		        	}
		        	
		        	logger.info("메뉴별매출 통계 조회 종료 ");
		        }
		    	
		        success = true;
			}catch(Exception e){
	    		logger.error("operEnd save", e);
	    		throw new ServerOperationForbiddenException("SaveError");
			}
		}
		
		return success;
	}
	
    /**
     * int로 내림차순(Desc) 정렬
     * @author Administrator
     *
     */
    static class CompareSeqDesc implements Comparator<StoreCalcStats>{
 
        @Override
        public int compare(StoreCalcStats o1, StoreCalcStats o2) {
            return o1.getOrderCreation().compareTo(o2.getOrderCreation());
        }        
    }
    
    static class CompareMenu implements Comparator<StoreCalcMenu>{
   	 
        @Override
        public int compare(StoreCalcMenu o1, StoreCalcMenu o2) {
            // TODO Auto-generated method stub
            return o2.getCompSelect().compareTo(o1.getCompSelect());
        }        
    }
}
