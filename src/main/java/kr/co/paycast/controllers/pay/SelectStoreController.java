package kr.co.paycast.controllers.pay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.pay.PayComparator;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.StoreItem;

/**
 * 상점 선택 컨트롤러
 */
@Controller("pay-select-store-controller")
@RequestMapping(value="/pay/selectstore")
public class SelectStoreController {
	
//	private static final Logger logger = LoggerFactory.getLogger(SelectStoreController.class);

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;

    @Autowired 
    private StoreService storeService;
	
	/**
	 * 상점 선택 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "selectstore.title"),
    				new Message("desc_chooseStore", "selectstore.chooseStore"),
    			});
    	
    	int siteId = Util.getSessionSiteId(session);
    	List<Store> storeList = storeService.getStoreListBySiteId(siteId);
    	Collections.sort(storeList, PayComparator.StoreStoreNameComparator);

    	// 상점 목록
    	ArrayList<StoreItem> retStores1 = new ArrayList<StoreItem>();
    	for(Store store : storeList) {
    		retStores1.add(new StoreItem(store));
    	}
    	
    	model.addAttribute("tempStoreList", retStores1);
    	//-
    	
    	// 대상 URL
    	String url = (String) session.getAttribute("desturi");
    	
    	model.addAttribute("nextUrl", url);
    	//-
    	
    	// 페이지 메시지 처리
    	String msg = Util.parseString(request.getParameter("msg"));
    	if (Util.isValid(msg) && msg.equals("notSelected")) {
        	model.addAttribute("notifMsg", msgMgr.message("selectstore.server.msg.notSelected", locale));
    	}
    	//-
    	
        return "pay/selectstore";
    }

	/**
	 * 상점 선택 사전 체크 액션
	 */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
    	
    	if (Util.hasThisPriv(session, "internal.ManageSiteJob")) {
	    	int storeId = -1;
	    	LoginUser loginUser = null;
	    	if (session != null) {
	    		loginUser = (LoginUser) session.getAttribute("loginUser");
	    		if (loginUser != null) {
	    			storeId = loginUser.getStoreId();
	    		}
	    	}
	    	
	    	int siteId = Util.getSessionSiteId(session);
	    	int storeCnt = storeService.getStoreCountBySiteId(siteId);
			String userUrl = Util.parseString(request.getParameter("uri"));
			
			List<Store> storeList = null;
			if (storeCnt == 1) {
				storeList = storeService.getStoreListBySiteId(siteId);
			}
	    	
	    	//logger.info("세션의 상점 Id: " + storeId);
	    	//logger.info("상점의 총 수: " + storeCnt);
	    	//logger.info("대상 URL: " + userUrl);
	    	
	    	// case cnt > 1: selectstore 페이지 출력(여러 상점 중 하나를 선택)
	    	if (storeCnt == 0) {
	    		//
	    		// 등록된 상점이 없으므로, [전체 상점] 페이지로 이동하여 상점을 먼저 등록토록 유도함
	    		//
	    		//   msg: 1) 등록된 상점이 없습니다. 먼저 상점을 등록해 주십시오.
	    		//
	    		return "redirect:/pay/store?msg=noStore";
	    	} else if (storeCnt == 1 && storeList != null && storeList.size() == 1 && loginUser != null) {
	    		//
	    		// 등록된 상점이 하나.
	    		//
	    		//   msg: 1) onlyOneSelected: [{0}] 상점만 선택 가능합니다.
	    		//        2) deffSelected: 기존 선택된 [{0}] 상점은 존재하지 않습니다. [{1}] 상점이 자동 선택되었습니다.
	    		//        3) autoSelected: [{0}] 상점이 자동 선택되었습니다.
	    		//
	    		if (storeId == storeList.get(0).getId()) {
	    			session.setAttribute("newStoreName", storeList.get(0).getStoreName());
	    			session.setAttribute("msg", "onlyOneSelected");
	    		} else {
	    			if (storeId > 0 && Util.isValid(loginUser.getDispStoreName())) {
	        			session.setAttribute("oldStoreName", loginUser.getDispStoreName());
	        			session.setAttribute("newStoreName", storeList.get(0).getStoreName());
	        			session.setAttribute("msg", "deffSelected");
	    			} else {
	        			session.setAttribute("newStoreName", storeList.get(0).getStoreName());
	        			session.setAttribute("msg", "autoSelected");
	    			}
	    		}
	
				response.addCookie(Util.cookie("currentStoreId_" + Util.loginUserId(session), 
						String.valueOf(storeList.get(0).getId())));
				
				loginUser.setStoreIdName(storeList.get(0));
	        	
				session.setAttribute("currentStoreId", String.valueOf(storeList.get(0).getId()));
	        	
	    		return "redirect:" + userUrl;
	    	} else {
        		//
        		// 등록된 상점이 하나 이상.(보통은 2이상이나 기타 이유로 1일수도 있음)
        		//
        		//   msg: 1) 선택된 상점이 없습니다. 먼저 상점을 선택해 주십시오.
        		//        2) 출력 메시지 없음
        		//
        		//   -> Good design을 위해 메시지 생략함
            	
            	session.setAttribute("desturi", userUrl);
        		
            	return "redirect:/pay/selectstore";
	    	}
    	} else {
    		//
    		// 사이트 관리 권한(ManageSiteJob) 이 없는 사용자는 이 메뉴 그룹 접근 금지
    		//
    		return "redirect:/pay/nomystore";
    	}
    }

}
