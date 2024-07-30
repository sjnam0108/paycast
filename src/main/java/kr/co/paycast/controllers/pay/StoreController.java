package kr.co.paycast.controllers.pay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.PayUserCookie;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreEtc;
import kr.co.paycast.models.pay.StoreOpt;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.self.service.SelfService;
import kr.co.paycast.utils.Util;

/**
 * 전체 상점 컨트롤러
 */
@Controller("pay-store-controller")
@RequestMapping(value="/pay/store")
public class StoreController {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;

    @Autowired 
    private StoreService storeService;
    
    @Autowired 
    private SiteService siteService;
    
    @Autowired
    private SelfService selfService;
	
	/**
	 * 전체 상점 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "store.title"),
					
    				new Message("title_shortName", "store.shortName"),
    				new Message("title_storeName", "store.storeName"),
    				new Message("title_effectiveStartDate", "store.effectiveStartDate"),
    				new Message("title_effectiveEndDate", "store.effectiveEndDate"),
    				new Message("title_permission", "store.permission"),
    				
    				new Message("tab_basic", "store.tabBasic"),
    				new Message("tab_pay", "store.tabPay"),
    				new Message("tab_saving", "store.tabSaving"),

    				new Message("label_onlyEffective", "store.onlyEffective"),
    				new Message("label_storeKey", "store.storeKey"),
    				new Message("label_mobileOrderUrl", "store.mobileOrderUrl"),
    				new Message("label_memo", "store.memo"),
    				new Message("label_store", "store.store"),
    				
    				new Message("label_smilepay", "store.smilepay"),
    				new Message("label_smilepayStoreID", "store.smilepayStoreID"),
    				new Message("label_smilepayAuthKey", "store.smilepayAuthKey"),
    				new Message("label_smilepayCancelCode", "store.smilepayCancelCode"),
    				
    				new Message("label_easypay", "store.easypay"),
    				new Message("label_easypayStoreID", "store.easypayStoreID"),
    				
    				new Message("title_savepermission", "store.savepermission"),
    				new Message("label_savepoint", "store.savepoint"),
    				new Message("label_savecoupon", "store.savecoupon"),
    				new Message("label_saveunused", "store.saveunused"),
    				
    				new Message("label_alimTalk", "store.alimTalk"),
    				
    				new Message("msg_urlcopied", "store.msg.urlcopied"),
    				new Message("msg_notSupportedBrowser", "store.msg.notSupportedBrowser"),
    				
					new Message("tip_select", "store.select"),
					new Message("tip_selected", "store.selected")
    			});

    	
    	// 모바일 주문 URL
    	String orderUrl = Util.getFileProperty("url.mobileOrder");
    	if (Util.isNotValid(orderUrl)) {
    		//
    		// [PayCast - kdk] 모바일 주문 페이지 경로 table 추가  20190620 ----------------------------------------------------------------- 시작
    		//
    		
//    		orderUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() +
//    				"/menu?store={0}";
    		orderUrl = request.getScheme()+"app://"+request.getServerName()+":"+request.getServerPort() +
    				"/menu?store={0}&table=";
    		
    		//
    		// [PayCast - kdk] 모바일 주문 페이지 경로 table 추가  20190620 ----------------------------------------------------------------- 끝
    		//
    	}
    	model.addAttribute("orderURL", orderUrl);
    	//-
    	
    	// 페이지 메시지 처리
    	String msg = Util.parseString(request.getParameter("msg"));
    	if (Util.isValid(msg) && msg.equals("noStore")) {
        	model.addAttribute("notifMsg", msgMgr.message("store.server.msg.noStore", locale));
    	}
    	//-
    	
        return "pay/store";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request, 
    		HttpSession session, HttpServletRequest req, HttpServletResponse res) {

    	String viewMode = request.getReqStrValue1();
    	boolean isEffectiveMode = false;
    	
    	if (Util.isValid(viewMode) && viewMode.equals("E")) {
    		isEffectiveMode = true;
    	}
    	
		if (session != null) {
			PayUserCookie userCookie = (PayUserCookie) session.getAttribute("userCookie");
			if (userCookie == null) {
				userCookie = new PayUserCookie(req);
				session.setAttribute("userCookie", userCookie);
			}
			
			userCookie.setViewCodeStore(isEffectiveMode ? "E" : "A", res);
		}
		
    	try {
    		return storeService.getStoreList(request, isEffectiveMode);
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("readError");
    	}
    }
    
	/**
	 * 추가 액션
	 */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	
    	int siteId = Util.getSessionSiteId(session);
    	String shortName = (String)model.get("shortName");
    	String storeName = (String)model.get("storeName");
    	Date effectiveStartDate = Util.removeTimeOfDate(Util.parseZuluTime((String)model.get("effectiveStartDate")));
    	
    	// 파라미터 검증
    	if (Util.isNotValid(shortName) || Util.isNotValid(storeName)) {
        	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
        }

    	Site site = siteService.getSite(siteId);
    	if (site == null) {
        	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
    	}

    	Store target = new Store(site, storeName, shortName, Util.getRandomKey(8), effectiveStartDate, session);
    	StoreOpt opt = new StoreOpt(target, session);
    	StoreEtc etc = new StoreEtc(target, session);
    	
        saveOrUpdate(target, opt, etc, locale);

        return "OK";
    }
    
	/**
	 * 변경 액션
	 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {

    	Store target = storeService.getStore((int)model.get("id"));
    	if (target != null) {
        	String shortName = (String)model.get("shortName");
        	String storeName = (String)model.get("storeName");
        	Date effectiveStartDate = Util.removeTimeOfDate(Util.parseZuluTime((String)model.get("effectiveStartDate")));
        	
        	// 파라미터 검증
        	if (Util.isNotValid(shortName) || Util.isNotValid(storeName)) {
            	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
            }

        	Site site = siteService.getSite(Util.getSessionSiteId(session));
        	if (site == null) {
            	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
        	}
        	
        	// 키오스크 주문 허용 여부 관련 체크
        	boolean beforeEnable = target.isKioskOrderEnabled();
        	boolean afterEnable = false;
        	// 키오스크 알림톡 사용 관련 체크
        	boolean atBeforeEnable = target.isAlimTalkAllowed();
        	boolean atAfterEnable = false;

        	String storeKey = Util.parseString((String)model.get("storeKey"));
        	String memo = Util.parseString((String)model.get("memo"));
        	
        	target.setSite(site);
        	target.setShortName(shortName);
        	target.setStoreName(storeName);
        	target.setEffectiveStartDate(effectiveStartDate);
            target.setEffectiveEndDate(Util.setMaxTimeOfDate(Util.parseZuluTime((String)model.get("effectiveEndDate"))));
        	target.setStoreKey(storeKey);
        	target.setMemo(memo);
        	
        	target.setKioskOrderAllowed(Util.parseBoolean((String)model.get("kioskOrderAllowed")));
        	target.setMobileOrderAllowed(Util.parseBoolean((String)model.get("mobileOrderAllowed")));
        	target.setKitchenPadAllowed(Util.parseBoolean((String)model.get("kitchenPadAllowed")));
        	target.setAlimTalkAllowed(Util.parseBoolean((String)model.get("alimTalkAllowed")));
        	
        	target.setKioskOrderEnabled(target.isKioskOrderAllowed());
        	target.setMobileOrderEnabled(target.isMobileOrderAllowed());
            
            target.touchWho(session);
            
            StoreOpt opt = target.getStoreOpt();
            if (opt == null) {
            	opt = new StoreOpt(target);
            }
            
            StoreEtc etc = target.getStoreEtc();
            if (etc == null) {
            	etc = new StoreEtc(target, session);
            }
            
            etc.setSpStoreKey(Util.parseString((String)model.get("spStoreKey"), ""));
            etc.setSpAuthKey(Util.parseString((String)model.get("spAuthKey"), ""));
            etc.setSpCancelCode(Util.parseString((String)model.get("spCancelCode"), ""));
            
            etc.setEpStoreKey(Util.parseString((String)model.get("epStoreKey"), ""));
            etc.setStorePayGubun(Util.parseString((String)model.get("storePayGubun"), "ME"));
            etc.setPaymentType((String)model.get("paymentType"));
            etc.setSavingType(Util.parseString((String)model.get("savingType"), "NO"));
            
            saveOrUpdate(target, opt, etc, locale);
            
            selfService.setMonTask("", String.valueOf(target.getId()), session);
            
			// 키오스크 주문 가능 여부 관련하여 추가
            // 키오스크 알림톡 사용 관련 체크
            // 이전 데이터와 저장한 데이터가 다를 경우 명령어 추가
			afterEnable = target.isKioskOrderEnabled();
			atAfterEnable = target.isAlimTalkAllowed();
			
			if(beforeEnable != afterEnable || atBeforeEnable != atAfterEnable){
				selfService.setMonTask("KE", String.valueOf(target.getId()), session);
			}
    	}
    	
        return "OK";
    }
    
	/**
	 * 추가 / 변경 시의 자료 저장
	 */
    private void saveOrUpdate(Store target, StoreOpt opt, StoreEtc etc, Locale locale) 
    		throws ServerOperationForbiddenException {

    	// 비즈니스 로직 검증
        if (target.getEffectiveStartDate() != null && target.getEffectiveEndDate() != null
        		&& target.getEffectiveStartDate().after(target.getEffectiveEndDate())) {
        	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.effectivedates", locale));
        }
        
        // DB 작업 수행 결과 검증
        try {
            storeService.saveOrUpdate(target);
            storeService.saveOrUpdate(opt);
            storeService.saveOrUpdate(etc);
        } catch (DataIntegrityViolationException dive) {
    		logger.error("saveOrUpdate", dive);
        	throw new ServerOperationForbiddenException(msgMgr.message("store.server.msg.sameStoreName", locale));
        } catch (ConstraintViolationException cve) {
    		logger.error("saveOrUpdate", cve);
        	throw new ServerOperationForbiddenException(msgMgr.message("store.server.msg.sameStoreName", locale));
        } catch (Exception e) {
    		logger.error("saveOrUpdate", e);
        	throw new ServerOperationForbiddenException("SaveError");
        }
    }
    
	/**
	 * 삭제 액션
	 */
    @RequestMapping(value = "/destroy", method = RequestMethod.POST)
    public @ResponseBody String destroy(@RequestBody Map<String, Object> model) {

    	@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
    	
    	List<Store> stores = new ArrayList<Store>();
    	
    	for (Object id : objs) {
    		Store store = new Store();
    		
    		store.setId((int)id);
    		
    		stores.add(store);
    	}
    	
    	try {
        	storeService.deleteStores(stores);
    	} catch (Exception e) {
    		logger.error("destroy", e);
    		throw new ServerOperationForbiddenException("DeleteError");
    	}

        return "OK";
    }
    
	/**
	 * 새로운 보안키 획득 액션
	 */
	@RequestMapping(value = "/changeKey", method = RequestMethod.POST)
    public @ResponseBody String changeKey(Locale locale, HttpSession session) {
		
		return Util.getRandomKey(8);
    }
}
