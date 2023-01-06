package kr.co.paycast.controllers.fnd;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.utils.NotifUtil;
import kr.co.paycast.utils.Util;

/**
 * 사용자 홈 컨트롤러
 */
@Controller("fnd-user-home-controller")
@RequestMapping(value="/fnd/userhome")
public class UserHomeController {

    @Autowired
	private MessageManager msgMgr;
    
	@Autowired
	private ModelManager modelMgr;
	
	/**
	 * 사용자 홈 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	msgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "userhome.title"),
    			});
    	
    	String lastLogin = (String) session.getAttribute("loginUserLastLoginTime");
    	if (Util.isValid(lastLogin)) {
    		session.removeAttribute("loginUserLastLoginTime");
    		
        	model.addAttribute("LastLogin", msgMgr.message("userhome.lastLogin", locale).replace("{0}", lastLogin));
    	}
    	
        return "fnd/userhome";
    }
    
    /**
	 * FCM 메시지 발송 테스트 액션
	 */
    @RequestMapping(value = "/sendfcm", method = RequestMethod.POST)
    public @ResponseBody String sendFcmNotification(HttpSession session) {
		
    	NotifUtil.sendFcmNotif("통지 제목 title", "통지 바디 body: " + new Date().toString(), 
    			"ZeOru19YMKg:APA91bGT13qRpAjMUICNxUgBlCQxhCehnS-ORk44OXqKJxJh2p1vRE0kG-JKIR7QYVZaEouABQIhQlxz5jFjxcEpKWc9XceOHlZfy3UPmSRMePAb81W6bNDOtlUapIpZB4TPMX4YusFY" 
    			);

        return "OperationSuccess";
    }
}
