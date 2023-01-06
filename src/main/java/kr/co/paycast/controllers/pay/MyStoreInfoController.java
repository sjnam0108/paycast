package kr.co.paycast.controllers.pay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.calc.service.CalculateService;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreEtc;
import kr.co.paycast.models.pay.StoreOpt;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.self.service.SelfService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.StoreInfoItem;
import kr.co.paycast.viewmodels.pay.StoreTime;

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
 * 내 매장 정보 컨트롤러
 */
@Controller("pay-my-store-info-controller")
@RequestMapping(value="/pay/mystoreinfo")
public class MyStoreInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(MyStoreInfoController.class);
	
	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;
    
    @Autowired 
    private StoreService storeService;
    
    @Autowired
    private CalculateService calcService;
    
    @Autowired
    private SelfService selfService;

	/**
	 * 내 매장 정보 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);
    	
    	solMsgMgr.checkStoreSelectionMessage(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "mystoreinfo.title"),
					
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
    			});

    	
    	// 상점 선택 스위치 표시(2개 이상일때)
    	model.addAttribute("isStoreSwitcherMode", true);
    	
    	// 상점 개요 정보
    	SolUtil.addStoreOverviewInfo(model, locale, session, request);
    	
    	// 스위치 초기값 지정을 위한 모델 지정
    	Store store = storeService.getStore(getStoreId(session));
    	
    	model.addAttribute("openType", store != null && store.getOpenType().equals("O"));
    	model.addAttribute("kioskOrderEnabled", store != null && store.isKioskOrderEnabled());
    	model.addAttribute("mobileOrderEnabled", store != null && store.isMobileOrderEnabled());
    	
    	// 120분 = 2시간
    	List<StoreTime> timeList = new ArrayList<StoreTime>();
    	for(int i=5; i <= 120; i+=5 ){
            String total =  "+" + i + "분";
    		StoreTime timeOne = new StoreTime(i, total, false);
    		timeList.add(timeOne);
    	}
    	model.addAttribute("timeList", timeList);
    	
        return "pay/mystoreinfo";
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
	 * 주문 시간 및 주문 가능 시간 업데이트
	 */
    @RequestMapping(value = "/updateOpenTimes", method = RequestMethod.POST)
    public @ResponseBody StoreInfoItem updateOpenTimes(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
    	
    	StoreInfoItem item = null;
    	try{
    		
    		item = storeService.storeInfoUpdate(model, locale, session, "MY");
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
    			
        		StoreOpt storeOpt = target.getStoreOpt();
        		storeOpt.setCustomOperation(custom);
        		storeOpt.touchWho(session);
        		storeService.saveOrUpdate(storeOpt);
    			
    			// 영업을 종료 하였을 경우 / 일별매출 /월별매출 / 메뉴통계 생성
    			if("C".equals(target.getOpenType())){
    				calcService.operEnd(String.valueOf(target.getId()), session);
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
    		} catch (Exception e) {
        		logger.error("updateStatus", e);
            	throw new ServerOperationForbiddenException("SaveError");
    		}
    	}
    	
    	return "OK";
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
