package kr.co.paycast.controllers.common;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.utils.Util;

/**
 * 공통 컨트롤러
 */
@Controller("common-controller")
@RequestMapping(value="/common")
public class CommonController {

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;
	
	/**
	 * 로그인 페이지(강제 로그아웃 메시지 처리 후)
	 */
    @RequestMapping(value = "/loginAfterForcedLogout", method = RequestMethod.GET)
    public String loginAfterForcedLogout(Model model, HttpServletRequest request, 
    		HttpServletResponse response, HttpSession session) {
    	model.addAttribute("forcedLogout", true);
		return "redirect:/";
    }
	
	/**
	 * 패스워드 변경 페이지
	 */
    @RequestMapping(value = "/passwordupdate", method = RequestMethod.GET)
    public String passwordUpdate(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	msgMgr.addCommonMessages(model, locale, session, request);
    	
    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
    				new Message("pageTitle", "passwordupdate.title"),
    				new Message("label_current", "passwordupdate.current"),
    				new Message("label_new", "passwordupdate.new"),
    				new Message("label_confirm", "passwordupdate.confirm"),
    				new Message("btn_save", "passwordupdate.save"),
    				new Message("msg_samePassword", "passwordupdate.msg.samePassword"),
    				new Message("msg_updateComplete", "passwordupdate.msg.updateComplete"),
    			});
        

    	Util.prepareKeyRSA(model, session);
    	
        return "common/passwordupdate";
    }
}
