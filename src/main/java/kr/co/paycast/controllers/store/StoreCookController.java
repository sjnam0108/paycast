package kr.co.paycast.controllers.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.store.service.StoreCookService;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.store.CookJsonItem;

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
 * PayCast 주방용 패드 Controller
 */
@Controller("store-cook-controller")
@RequestMapping(value="/storecook")
public class StoreCookController {
	private static final Logger logger = LoggerFactory.getLogger(StoreCookController.class);

	@Autowired
	private MessageManager msgMgr;
    
	@Autowired
	private ModelManager modelMgr;
	
    @Autowired
    private StoreCookService storeCookService;

	@Autowired
	private PayMessageManager solMsgMgr;
    
    @Autowired 
    private DeviceService devService;

	/**
	 * 주방용 패드 List 조회
	 */
    @RequestMapping(value = {"", "/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			
    			//title_inorder:순번,title_ordernum:주문번호,title_package:포장,title_menu:메뉴,title_amount:수량,
    			//title_notice:알림,title_completeorder:주문완료,title_completecancel:완료취소,title_standby:대기
    			new Message[] {
					new Message("pageTitle", "cook.title"),
    				new Message("title_inorder", "cook.inorder"),
    				new Message("title_ordernum", "cook.ordernum"),
    				new Message("title_package", "cook.package"),
    				new Message("title_menu", "cook.menu"),
    				new Message("title_amount", "cook.amount"),
    				new Message("title_notice", "cook.notice"),
    				new Message("title_completeorder", "cook.completeorder"),
    				new Message("title_completecancel", "cook.completecancel"),
    				new Message("title_standby", "cook.standby"),
    				new Message("title_complete", "cook.complete"),
    				
    				new Message("title_completemsg", "cook.completemsg"),
    				
    				new Message("mag_store400", "error.store400"),
    				new Message("mag_errorAdmin", "smilepay.msg.errorAdmin")
    			});
    	
		String deviceId = (String)request.getParameter("deviceId");
		
		Device device = devService.getDeviceByUkid(Util.parseString(deviceId));
		if (device == null) {
        	logger.error("device [{}]", device);
        	logger.error("storecook", "기기를 조회 할수 없습니다. ");
        	model.addAttribute("padEnabled", false);
        	return "kitchenpad";
		}
		
    	Store store = device.getStore();
		if(store == null){
			logger.error("device [{}]", device);
        	logger.error("storecook", "매장을 조회 할수 없습니다. ");
        	model.addAttribute("padEnabled", false);
        	return "kitchenpad";
		}
		
    	model.addAttribute("deviceId", deviceId);
    	model.addAttribute("storeIdCo", store.getId());
    	model.addAttribute("storeNameCo", store.getStoreName());
    	// 2019.07.19 주방용 패드 사용 불가 
    	model.addAttribute("padEnabled", store.isKitchenPadAllowed());
    	if(!store.isKitchenPadAllowed()){
    		return "forward:/storecook/inactivekitchenpad";
    	}
    	
        return "kitchenpad";
    }
    
	/**
	 * 대기 목록 조회
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody List<CookJsonItem> read(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
    	
    	String storeId = (String) model.get("storeId");
    	
    	try {
            return storeCookService.getStoreCookList(storeId);
    	} catch (RuntimeException re) {
    		logger.error("read", re);
    		throw new ServerOperationForbiddenException("ReadError");
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    }
    
    /**
     * 다시 대기 목록 조회
     */
    @RequestMapping(value = "/renuwRead", method = RequestMethod.POST)
    public @ResponseBody List<CookJsonItem> renuwRead(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
    	String storeId = (String) model.get("storeId");
    	
    	try {
    		return storeCookService.getStoreCookListRenew(storeId);
    	} catch (RuntimeException re) {
    		logger.error("read", re);
    		throw new ServerOperationForbiddenException("ReadError");
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    }
    
    /**
     * 취소 목록 조회 후 삭제
     */
    @RequestMapping(value = "/renuwCancelRead", method = RequestMethod.POST)
    public @ResponseBody List<CookJsonItem> renuwCancelRead(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
    	
    	String storeId = (String) model.get("storeId");
    	
    	try {
    		return storeCookService.getStoreCookCancelRenew(storeId, session);
    	} catch (RuntimeException re) {
    		logger.error("read", re);
    		throw new ServerOperationForbiddenException("ReadError");
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    }
    
    /**
     * 주문 관리에서의 대기목록 알림
     */
    @RequestMapping(value = "/alarmUpdate", method = RequestMethod.POST)
    public @ResponseBody boolean alarmUpdate(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	int siteId = (int)model.get("siteId");
    	String cookId = (String)model.get("cookId");
    	String storeId = (String)model.get("storeId");
    	String complteYn = (String)model.get("complteYn");
    	String storeOrderId = (String)model.get("storeOrderId");
    	ArrayList<Object> orderIDList = (ArrayList<Object>) model.get("orderIDList");
    	
    	logger.info("/alarmUpdate >>> 대기목록 알림 시작 [{}] Y일 경우 완료", complteYn);
    	boolean success = false;
    	try {
    		success = storeCookService.alarmUpdate(cookId, storeId, storeOrderId, orderIDList, complteYn, session, locale);
    	}catch (RuntimeException re) {
    		logger.error("alarmUpdate [{}]", re);
    		throw new ServerOperationForbiddenException("알림 울리기가 실패했습니다.");
    	} catch (Exception e) {
    		logger.error("alarmUpdate [{}]", e);
    		throw new ServerOperationForbiddenException("알림 울리기가 실패했습니다.");
    	}
		
		logger.info("/alarmUpdate >>> 종료 [{}] Y일 경우 완료", complteYn);
        return success;
    }
    
	/**
	 * 대기목록 수 조회
	 */
    @RequestMapping(value = "/stayCntRead", method = RequestMethod.POST)
    public @ResponseBody int stayCntRead(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
    	
    	String storeId = (String) model.get("storeId");
    	
    	try {
    		
            return storeCookService.getStayCntRead(Integer.parseInt(storeId));
    	} catch (RuntimeException re) {
    		logger.error("stayCntRead [{}]", re);
    		throw new ServerOperationForbiddenException("ReadError");
    	} catch (Exception e) {
    		logger.error("stayCntRead [{}]", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    }
    
	/**
	 * 완료 목록 조회
	 */
    @RequestMapping(value = "/readCom", method = RequestMethod.POST)
    public @ResponseBody List<CookJsonItem> readCom(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
    	
    	String storeId = (String) model.get("storeId");
    	
    	try {
            return storeCookService.getCookComListByStoreId(Util.parseInt(storeId));
    	} catch (RuntimeException re) {
    		logger.error("readCom [{}]", re);
    		throw new ServerOperationForbiddenException("ReadError");
    	} catch (Exception e) {
    		logger.error("readCom [{}]", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    }
    
    /**
     * 완료 목록에서 취소할 경우  
     */
    @RequestMapping(value = "/comCancelUpdate", method = RequestMethod.POST)
    public @ResponseBody String comCancelUpdate(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	int siteId = (int)model.get("siteId");
    	String cookId = (String)model.get("cookId");
    	String storeId = (String)model.get("storeId");
    	
		storeCookService.comCancelUpdate(siteId, cookId, storeId, session);
		
		return "success";
    }
    
    
    /**
     * 주방용 패드 사용 불가 화면 이동
     */
    @RequestMapping(value = "/inactivekitchenpad", method = {RequestMethod.GET, RequestMethod.POST})
    public String inactive(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			
    			//title_inorder:순번,title_ordernum:주문번호,title_package:포장,title_menu:메뉴,title_amount:수량,
    			//title_notice:알림,title_completeorder:주문완료,title_completecancel:완료취소,title_standby:대기
    			new Message[] {
					new Message("pageTitle", "cook.title"),
					new Message("cook_noorder", "cook.noorder"),
    				
    				new Message("mag_store400", "error.store400"),
    				new Message("mag_errorAdmin", "smilepay.msg.errorAdmin")
    			});
    	
		String deviceId = (String)request.getParameter("deviceId");
		
    	model.addAttribute("deviceId", deviceId);
    	
		return "inactivekitchenpad";
    }
    
    /**
     * 주방용패드 사용 가능 체크
     */
    @RequestMapping(value = "/inactiveChk", method = RequestMethod.POST)
    public @ResponseBody String inactiveChk(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	String padChk = (String)model.get("padChk");		//'K':허용 중 체크 / 'I':비허용 중 체크
    	String deviceId = (String)model.get("deviceId");
    	String url = "";
		if("K".equals(padChk)){
			url = "/storecook/inactivekitchenpad?deviceId="+deviceId;
		}else{
			url = "NO";
		}
    	
		Device device = devService.getDeviceByUkid(Util.parseString(deviceId));
		if (device == null) {
        	logger.error("device [{}]", device);
        	logger.error("storecook", "기기를 조회 할수 없습니다. ");
        	return url;
		}
		
    	Store store = device.getStore();
		if(store == null){
			logger.error("device [{}]", device);
        	logger.error("storecook", "매장을 조회 할수 없습니다. ");
        	return url;
		}
		
		if(!store.isKitchenPadAllowed()){
			return url;
		}
		
		if("K".equals(padChk)){
			return "NO";
		}else{
			return "/storecook?deviceId="+deviceId;
		}
    }
}
