package kr.co.paycast.controllers.pay;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.store.service.StoreAllimTalkService;
import kr.co.paycast.utils.PayUtil;

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
 * 내 매장 알림톡 통계 컨트롤러
 */
@Controller("pay-my-store-alimtalk-controller")
@RequestMapping(value="/pay/mystorealimtalk")
public class MyStoreAlimTalkController {
	private static final Logger logger = LoggerFactory.getLogger(MyStoreAlimTalkController.class);
	
	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;

    @Autowired
    private StoreAllimTalkService alimTalkService;
	
	/**
	 * 내 매장 설정 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "storealimtalk.title"),
					
    				new Message("title_dayRadio", "storealimtalk.dayRadio"),
    				new Message("title_monthRadio", "storealimtalk.monthRadio"),
    				new Message("title_execl", "storealimtalk.execl"),
    				new Message("title_refer", "storealimtalk.refer"),

    				new Message("title_day", "storealimtalk.day"),
    				new Message("title_storeid", "storealimtalk.storeid"),
    				new Message("title_storenm", "storealimtalk.storenm"),
    				new Message("title_alim", "storealimtalk.alim"),
    				new Message("title_sms", "storealimtalk.sms"),
    				new Message("title_total", "storealimtalk.total"),
    				new Message("title_addtext", "storealimtalk.addtext")
    				
    			});
    	
    	PayUtil.searchDay(model, locale);

        return "pay/mystorealimtalk";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request, 
    		HttpSession session, HttpServletRequest req, HttpServletResponse res) {
  		
    	try {
    		return alimTalkService.getAllimTalkList(request, session, true);
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("readError");
    	}
    }
}
