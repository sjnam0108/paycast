package kr.co.paycast.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.fnd.LoginLog;
import kr.co.paycast.models.fnd.service.PrivilegeService;
import kr.co.paycast.models.fnd.service.UserService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

    @Autowired 
    private PrivilegeService privService;

	@Autowired 
    private UserService userService;

    @Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

    	// request.getSession(false)가 null을 되돌려줄 수 있으나,
    	// 아래의 Util.isLoginUser(session)에서 null 처리가 되어 있음.
		HttpSession session = request.getSession(false);
		String loginUri = "/";
		
		String requestUri = request.getRequestURI();
		
    	// [WAB] --------------------------------------------------------------------------
    	/*
		// Agent 요청 접근일 때 "통과"
		if (requestUri != null && !requestUri.isEmpty()) {
			if (requestUri.startsWith("/ext/agent/")) {
				return true;
			}
		}
		*/
    	// [WAB] --------------------------------------------------------------------------
    	// [PayCast] ext ----------------------------------------------------------- start
    	//
    	//
		
		// Agent 요청 접근일 때 "통과"
		if (requestUri != null && !requestUri.isEmpty()) {
			if (requestUri.startsWith("/pay/agent/") || requestUri.startsWith("/pay/common/")) {
				return true;
			}
		}
    	
    	//
    	//
    	// [PayCast] ext ------------------------------------------------------------- end
		
		if (session == null) {
			response.sendRedirect(loginUri);
			return false;
		}
		
		session.removeAttribute("prevUri");
		session.removeAttribute("prevQuery");
		
		if (!Util.isLoginUser(session)) {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
			{
				throw new ServerOperationForbiddenException("ToLoginPage");
			} else {
				session.setAttribute("prevUri", requestUri);
				session.setAttribute("prevQuery", Util.parseString(request.getQueryString()));

				response.sendRedirect(loginUri);
			}
			
			return false;
		}
		
		LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
		if (loginUser == null) {
			response.sendRedirect(loginUri);
			return false;
		}

		String allowedRequestUri = "";
		if (Util.isValid(request.getMethod()) && request.getMethod().equals("GET")) {
			allowedRequestUri = requestUri;
		}
		
		// 동일 계정 동시 사용중 체크
    	if (!Util.hasThisPriv(session, "internal.NoConcurrentCheck")) {
    		LoginLog lastLoginLog = userService.getLastLoginLogByUserId(loginUser.getId());
    		if (lastLoginLog != null && lastLoginLog.getId() != loginUser.getLoginId()) {
    			userService.logout(session, true);
    			
    			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
    			{
    				throw new ServerOperationForbiddenException("ToLoginPage2");
    			} else {
    				response.sendRedirect("/common/loginAfterForcedLogout");
    			}
    			
    			return false;
    		}
    	}

    	// [PayCast] ext ----------------------------------------------------------- start
    	//
    	//
    	
    	
    	if (Util.isValid(requestUri)) {
    		if (requestUri.equals("/pay/storeinfo")|| requestUri.equals("/pay/storemenu")
    				|| requestUri.equals("/pay/storesetting") 
    				|| requestUri.equals("/pay/storedevice") || requestUri.equals("/pay/storeuser")) {
    			//
    			// 관리자 권한 있고, 세션의 상점Id 유효 및 실제 존재하면 ok
    			//
    			if (!SolUtil.canAccessToStoreAdminPage(session)) {
            		response.sendRedirect("/pay/selectstore/check?uri=" + requestUri);
            		return false;
    			}
    		}
    		if (requestUri.equals("/pay/mystoreinfo") || requestUri.equals("/pay/mystoremenu") || requestUri.equals("/pay/mystoresetting") ||
    				requestUri.equals("/store/sales") || requestUri.equals("/calc/statsDay") ||
    				requestUri.equals("/calc/statsMonth") || requestUri.equals("/calc/statsMenu")
    				) {
    			//
    			// 세션의 상점Id 유효 및 실제 존재하면서,
    			//    관리자 권한 있거나, 상점 사장님으로 등록되어 있을 경우 ok
    			//
    			if (!SolUtil.canAccessToStoreCustomerPage(session)) {
    				response.sendRedirect("/pay/nomystore");
            		return false;
    			}
       		}
    	}
    	
    	// 상점 선택 여부에 따라 이동되는 비메뉴트리 메뉴의 접근 허용
    	if (requestUri.startsWith("/pay/selectstore") || requestUri.startsWith("/pay/nomystore")) {
    		return true;
    	}
    	
    	//
    	//
    	// [PayCast] ext ------------------------------------------------------------- end

		// 모든 페이지 접근 권한 가질 때 "통과"
		if (loginUser.isAnyMenuAccessAllowed()) {
			privService.touchRecentAccessMenus(session, request.getLocale(), allowedRequestUri);
			return true;
		}

		// 패스워드 수정 페이지 접근일 때 "통과"
		if (requestUri.startsWith("/common/passwordupdate")) {
			privService.touchRecentAccessMenus(session, request.getLocale(), allowedRequestUri);
			return true;
		}
		
		if (!requestUri.endsWith("/")) {
			requestUri += "/";
		}
		
		//
		// [PayCast - kdk] 추가 메소드  20190619 ----------------------------------------------------------------- 시작
		//
		
		// 주방용 패드
	    if(requestUri.startsWith("/storecook")){
			privService.touchRecentAccessMenus(session, request.getLocale(), allowedRequestUri);
			return true;
	    }
	    
		//
		// [PayCast - kdk] 추가 메소드  20190619 ----------------------------------------------------------------- 종료
		//
		
		List<String> allowedUrlList = loginUser.getAllowedUrlList();
		for (String url : allowedUrlList) {
			String tmpUrl = url;
			if (!tmpUrl.endsWith("/")) {
				tmpUrl += "/";
			}
			
			if (requestUri.startsWith(tmpUrl)) {
				privService.touchRecentAccessMenus(session, request.getLocale(), allowedRequestUri);
				return true;
			}
		}
		
		// 허용되지 않은 페이지 접근
		response.sendRedirect(loginUri);
		
		return false;
	}
}
