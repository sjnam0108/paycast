package kr.co.paycast.controllers.pay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.User;
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.models.fnd.service.UserService;
import kr.co.paycast.models.pay.AppUser;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.utils.Util;

/**
 * 앱 등록 컨트롤러
 */
@Controller("pay-app-user-controller")
@RequestMapping(value="/pay/appuser")
public class AppUserController {
	
	private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;

    @Autowired 
    private PayService payService;
    
    @Autowired 
    private SiteService siteService;

    @Autowired 
    private UserService userService;
	
	/**
	 * 앱 등록 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "appuser.title"),

    				new Message("title_deviceName", "appuser.deviceName"),
    				new Message("title_os", "appuser.os"),
    				new Message("title_ver", "appuser.ver"),
    				new Message("label_fcmToken", "appuser.fcmToken"),
    				new Message("tip_deviceNameTip", "appuser.deviceNameTip"),
    				new Message("tip_device", "appuser.deviceTip"),
    				
    				new Message("label_newApp", "appuser.newApp"),
    				new Message("tip_statusR", "appuser.statusR"),
    				new Message("tip_statusA", "appuser.statusA"),
    				new Message("tip_statusDSU", "appuser.statusDSU"),
    				
    				new Message("cmd_requestR", "appuser.requestR"),
    				new Message("cmd_requestA", "appuser.requestA"),
    				new Message("cmd_requestD", "appuser.requestD"),
    				
    				new Message("msg_currApp", "appuser.msg.currApp"),
    				new Message("msg_regApps", "appuser.msg.regApps"),
    				new Message("msg_currAppDesc", "appuser.msg.currAppDesc"),
    				new Message("msg_regAppsDesc", "appuser.msg.regAppsDesc"),
    				new Message("msg_regAppsNoData", "appuser.msg.regAppsNoData"),
    			});
    	
    	int siteId = Util.getSessionSiteId(session);
    	int userId = -1;
    	
    	LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
    	if (loginUser != null) {
    		userId = loginUser.getId();
    	}
    	
    	model.addAttribute("AppUsers", payService.getAppUserListBySiteIdUserId(siteId, userId));
    	
    	
        return "pay/appuser";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody List<AppUser> read(@RequestBody DataSourceRequest request, 
    		HttpSession session, Locale locale) {
    	
    	int siteId = Util.getSessionSiteId(session);
    	int userId = -1;
    	
    	LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
    	if (loginUser != null) {
    		userId = loginUser.getId();
    	}
    	
    	try {
    		return payService.getAppUserListBySiteIdUserId(siteId, userId);
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

    	String deviceName = (String)model.get("deviceName");
    	String fcmToken = (String)model.get("fcmToken");
    	String osType = (String)model.get("osType");
    	String osVer = (String)model.get("osVer");

    	Site site = siteService.getSite(Util.getSessionSiteId(session));
    	if (site == null) {
        	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
    	}
    	
    	User user = null;
    	LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
    	if (loginUser != null) {
    		user = userService.getUser(loginUser.getId());
    	}
    	
    	// 파라미터 검증
    	if (Util.isNotValid(deviceName) || Util.isNotValid(fcmToken) || Util.isNotValid(osType) || 
    			Util.isNotValid(osVer) || site == null || user == null || fcmToken.length() != 152) {
        	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
        }
    	
    	
    	boolean isKo = locale.getLanguage().equals(new Locale("ko").getLanguage());
    	
    	AppUser target = new AppUser(site, user, deviceName, fcmToken, osType, osVer, session);
    	target.setLang(isKo ? "ko" : "en");
    	
    	saveOrUpdate(target, locale);
    	
        return "OK";
    }
    
	/**
	 * 변경 액션
	 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	
    	AppUser target = payService.getAppUser((int)model.get("id"));
    	if (target != null) {
        	String deviceName = (String)model.get("deviceName");
        	String osVer = (String)model.get("osVer");
        	
        	// 파라미터 검증
        	if (Util.isNotValid(deviceName)) {
            	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
            }
    		
        	target.setDeviceName(deviceName);
        	target.setOsVer(osVer);
        	
            target.touchWho(session);
            
            saveOrUpdate(target, locale);
    	}
    	
    	return "OK";
    }
    
	/**
	 * 추가 / 변경 시의 자료 저장
	 */
    private void saveOrUpdate(AppUser target, Locale locale) throws ServerOperationForbiddenException {
    	// 비즈니스 로직 검증
    	// 해당 사항 없음
        
        // DB 작업 수행 결과 검증
        try {
        	payService.saveOrUpdate(target);
        } catch (DataIntegrityViolationException dive) {
    		logger.error("saveOrUpdate", dive);
        	throw new ServerOperationForbiddenException(msgMgr.message("appuser.server.msg.sameDeviceName", locale));
        } catch (ConstraintViolationException cve) {
    		logger.error("saveOrUpdate", cve);
        	throw new ServerOperationForbiddenException(msgMgr.message("appuser.server.msg.sameDeviceName", locale));
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
    	
    	List<AppUser> appUsers = new ArrayList<AppUser>();
    	
    	for (Object id : objs) {
    		AppUser appUser = new AppUser();
    		
    		appUser.setId((int)id);
    		
    		appUsers.add(appUser);
    	}
    	
    	try {
    		payService.deleteAppUsers(appUsers);
    	} catch (Exception e) {
    		logger.error("destroy", e);
    		throw new ServerOperationForbiddenException("DeleteError");
    	}

        return "OK";
    }
    
	/**
	 * 상태 변경
	 */
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public @ResponseBody String changeStatus(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	
    	AppUser target = payService.getAppUser((int)model.get("id"));
    	if (target != null) {
        	String status = (String)model.get("status");
        	
        	// 파라미터 검증
        	if (Util.isNotValid(status)) {
            	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
            }
    		
        	target.setStatus(status);
        	
            target.touchWho(session);
            
            saveOrUpdate(target, locale);
    	}
    	
    	return "OK";
    }
}
