package kr.co.paycast.controllers.pay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import kr.co.paycast.models.pay.AppUser;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.DropDownListItem;

/**
 * 현재 사이트 앱 사용자 컨트롤러
 */
@Controller("pay-currsite-app-user-controller")
@RequestMapping(value="/pay/currappuser")
public class CurrAppUserController {
	
	private static final Logger logger = LoggerFactory.getLogger(CurrAppUserController.class);

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;

    @Autowired 
    private PayService payService;
	
	/**
	 * 앱 사용자 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "currappuser.title"),
					new Message("title_deviceName", "currappuser.deviceName"),
					new Message("title_username", "currappuser.username"),
					new Message("title_familiarName", "currappuser.familiarName"),
					new Message("title_os", "currappuser.os"),
					new Message("title_status", "currappuser.status"),
					new Message("title_lang", "currappuser.lang"),
					new Message("title_regDate", "currappuser.regDate"),
					new Message("cmd_activate", "currappuser.activate"),
					new Message("cmd_deactivate", "currappuser.deactivate"),
					new Message("label_fcmToken", "currappuser.fcmToken"),
					
					new Message("tip_statusR", "currappuser.statusR"),
					new Message("tip_statusA", "currappuser.statusA"),
					new Message("tip_statusU", "currappuser.statusU"),
					new Message("tip_statusD", "currappuser.statusD"),
					new Message("tip_statusS", "currappuser.statusS"),
    			});

    	// Device가 PC일 경우에만, 다중 행 선택 설정
    	Util.setMultiSelectableIfFromComputer(model, request);
    	
        return "pay/currappuser";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request, 
    		Locale locale) {
    	try {
    		return payService.getAppUserList(request);
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("readError");
    	}
    }
    
	/**
	 * 변경 액션
	 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	
    	AppUser target = payService.getAppUser((int)model.get("id"));
    	if (target != null) {
        	String status = (String)model.get("status");
        	String lang = (String)model.get("lang");
        	
        	// 파라미터 검증
        	if (Util.isNotValid(status) || Util.isNotValid(lang)) {
            	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
            }
    		
        	target.setStatus(status);
        	target.setLang(lang);
        	
            target.touchWho(session);
            
            try {
            	payService.saveOrUpdate(target);
            } catch (Exception e) {
        		logger.error("update", e);
            	throw new ServerOperationForbiddenException("SaveError");
            }
    	}
    	
    	return "OK";
    }

	/**
	 * 알림 활성화 액션
	 */
    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public @ResponseBody String activate(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	
    	@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
    	
    	for (Object id : objs) {
    		AppUser appUser = payService.getAppUser((int)id);
    		if (appUser != null) {
    			if (!appUser.getStatus().equals("A")) {
    				appUser.setStatus("A");
    				appUser.touchWho(session);
    	            
    				payService.saveOrUpdate(appUser);
    			}
    		}
    	}

        return "OK";
    }

	/**
	 * 알림 비활성화 액션
	 */
    @RequestMapping(value = "/deactivate", method = RequestMethod.POST)
    public @ResponseBody String deactivate(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	
    	@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
    	
    	for (Object id : objs) {
    		AppUser appUser = payService.getAppUser((int)id);
    		if (appUser != null) {
    			if (appUser.getStatus().equals("A")) {
    				appUser.setStatus("D");
    				appUser.touchWho(session);
    	            
    				payService.saveOrUpdate(appUser);
    			}
    		}
    	}

        return "OK";
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
	 * 읽기 액션 - 기기 OS DropDownList
	 */
    @RequestMapping(value = "/readDeviceOSTypes", method = RequestMethod.POST)
    public @ResponseBody List<DropDownListItem> readDeviceOSTypes(Locale locale, 
    		HttpSession session) {
		ArrayList<DropDownListItem> list = new ArrayList<DropDownListItem>();

		list.add(new DropDownListItem("fab fa-android text-green fa-fw", "Android", "A"));
		list.add(new DropDownListItem("fab fa-apple text-dark fa-fw", "iOS", "I"));
		
		return list;
    }
    
	/**
	 * 읽기 액션 - 알림 상태 DropDownList
	 */
    @RequestMapping(value = "/readStatusTypes", method = RequestMethod.POST)
    public @ResponseBody List<DropDownListItem> readStatusTypes(Locale locale, 
    		HttpSession session) {
		ArrayList<DropDownListItem> list = new ArrayList<DropDownListItem>();

		list.add(new DropDownListItem("fas fa-hourglass-half fa-fw", msgMgr.message("currappuser.statusR", locale), "R"));
		list.add(new DropDownListItem("fas fa-user fa-fw text-secondary", msgMgr.message("currappuser.statusU", locale), "U"));
		list.add(new DropDownListItem("fas fa-robot fa-fw text-secondary", msgMgr.message("currappuser.statusS", locale), "S"));
		list.add(new DropDownListItem("fas fa-user-tie fa-fw text-secondary", msgMgr.message("currappuser.statusD", locale), "D"));
		list.add(new DropDownListItem("fas fa-bell fa-fw text-blue", msgMgr.message("currappuser.statusA", locale), "A"));
		
		return list;
    }
}
