package kr.co.paycast.controllers.calc;

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
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.models.store.service.StoreSiteService;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.calc.CalcStatsItem;

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
 * 무인 주문 결제 시스템 메뉴 통계
 */
@Controller("calc-menu-controller")
@RequestMapping(value="/calc/statsMenu")
public class CalcMenuController {
	private static final Logger logger = LoggerFactory.getLogger(CalcMenuController.class);

	@Autowired
	private MessageManager msgMgr;
    
	@Autowired
	private ModelManager modelMgr;
	
    @Autowired
    private SiteService siteService;
    
    @Autowired
    private StoreSiteService storeSiteService;
    
    @Autowired
    private CalculateService calculateService;

	@Autowired
	private PayMessageManager solMsgMgr;
	
	/**
	 *   메뉴 통계 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "statsMenu.title"),
					new Message("title_termdate", "statsDay.termdate"),
					new Message("title_day", "statsMenu.day"),
					new Message("title_menu", "statsMenu.menu"),
					new Message("title_amount", "statsMenu.amount"),
					new Message("title_amt", "statsMenu.amt"),
					new Message("title_salesamt", "statsMenu.salesamt"),
					new Message("title_totalamt", "statsMenu.totalamt")
    			});
    	
    	PayUtil.searchDay(model, locale);
    	// 모바일 여부체크
    	PayUtil.getMobileCheck(model, request);
    	
        return "calc/statsMenu";
    }
    
	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public @ResponseBody List<CalcStatsItem> read(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
		List<CalcStatsItem> resultList = new ArrayList<CalcStatsItem>();

		try {
			String fromDate = (String)model.get("from");
			String toDate = (String)model.get("to");
			int storeId = getStoreId(session);
			int page = (int)model.get("page");
			int skip = (int)model.get("skip");

			resultList = calculateService.getCalcMenuRead(fromDate, toDate, storeId);

		} catch (RuntimeException re) {
			logger.error("read", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
		return resultList;
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
