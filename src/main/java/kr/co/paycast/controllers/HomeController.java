package kr.co.paycast.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.info.GlobalInfo;
import kr.co.paycast.models.ExcelDownloadView;
import kr.co.paycast.models.FormRequest;
import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayUserCookie;
import kr.co.paycast.models.fnd.LoginLog;
import kr.co.paycast.models.fnd.User;
import kr.co.paycast.models.fnd.service.UserService;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.UploadFile;
import kr.co.paycast.models.pay.service.MenuService;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

/**
 * 홈 컨트롤러
 */
@Controller("home-controller")
@RequestMapping(value="")
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired 
    private UserService userService;

	@Autowired 
    private StoreService storeService;
	
	@Autowired 
    private PayService payService;

	@Autowired
	private MessageManager msgMgr;
    
	@Autowired
	private ModelManager modelMgr;
	
    @Autowired
    private MenuService menuService;
	
	/**
	 * 웹 애플리케이션 컨텍스트 홈
	 */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpServletRequest request,
    		HttpSession session) {
    	
		String logoutType = Util.parseString(request.getParameter("forcedLogout"));
		if (Util.isValid(logoutType) && logoutType.equals("true")) {
	    	model.addAttribute("forcedLogout", true);
		}

		String appMode = Util.getAppModeFromRequest(request);
		
		if (Util.isValid(appMode) && (appMode.equals("A") || appMode.equals("I"))) {
			return "forward:/applogin";
		} else {
			return "forward:/home";
		}
	}
	
	/**
	 * 로그인 페이지
	 */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String toLogin(Model model, Locale locale, HttpServletRequest request,
    		HttpSession session) {
    	msgMgr.addCommonMessages(model, locale, session, request);
    	
    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
   					new Message("pageTitle", "home.title"),
       				new Message("label_username", "home.username"),
       				new Message("label_password", "home.password"),
    				new Message("tip_remember", "home.remember"),
    				new Message("btn_login", "home.login"),
    				new Message("msg_forcedLogout", "home.msg.forcedLogout"),
    			});

    	model.addAttribute("logoPathFile", Util.getLogoPathFile("login", request.getServerName()));
    	
    	Util.prepareKeyRSA(model, session);
    	
		return "home";
	}
	
	/**
	 * FavIcon 액션
	 */
    @RequestMapping(value = "/favicon.ico", method = RequestMethod.GET)
    public void favicon(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		response.sendRedirect("/resources/favicon.ico");
    	} catch (IOException e) {
    		logger.error("favicon", e);
    	}
    }

	/**
	 * 로그인 암호키 확인 액션
	 */
    @RequestMapping(value = "/loginkey", method = RequestMethod.POST)
    public @ResponseBody String checkLoginKey(@RequestBody Map<String, Object> model) {
    	
    	String clientKey = Util.parseString((String)model.get("key"));
    	
    	return (Util.isValid(clientKey) && clientKey.equals(GlobalInfo.RSAKeyMod)) ? "Y" : "N";
    }
    
	/**
	 * 로그인 프로세스
	 */
    private String doLogin(String username, String password, String appMode, HttpSession session, 
    		Locale locale, HttpServletRequest request, HttpServletResponse response) {
    	
    	if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
    		return "home.server.msg.wrongIdPwd";
    	}
    	
    	// RSA 인코딩되었을 때의 처리
    	if (username.length() == 512 && password.length() == 512) {
    		PrivateKey privateKey = (PrivateKey) session.getAttribute("rsaPrivateKey");
    		if (privateKey != null) {
    			username = Util.decryptRSA(privateKey, username);
    			password = Util.decryptRSA(privateKey, password);
    		}
    	}
    	//-
    	
    	User dbUser = userService.getUser(username);
    	if (dbUser == null || !Util.isSameUserPassword(password, dbUser.getSalt(), dbUser.getPassword())) {
    		logger.info("Login Error(WrongIdPwd): {}/{}", username, password);
    		
    		return "home.server.msg.wrongIdPwd";
    	}
    	
    	// 여기까지 오면 패스워드까지 일치
    	if (!userService.isEffectiveUser(dbUser)) {
    		logger.info("Login Error(EffectiveDate): {}/{}", username, password);
    		
    		return "home.server.msg.notEffectiveUser";
    	}
    	
    	LoginLog lastLoginLog = userService.getLastLoginLogByUserId(dbUser.getId());
    	if (lastLoginLog != null) {
        	DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        	
    		session.setAttribute("loginUserLastLoginTime", df.format(lastLoginLog.getWhoCreationDate()));
    	}
    	
    	LoginLog loginLog = new LoginLog(request.getRemoteAddr(), dbUser, session);

    	try {
        	userService.saveOrUpdate(loginLog);
        	
        	LoginUser successLoginUser = new LoginUser(dbUser, loginLog.getId());
        	successLoginUser.setUserSites(userService.getUserSites(dbUser.getId()));
        	
        	session.setAttribute("loginUser", successLoginUser);
    		
        	// [WAB] --------------------------------------------------------------------------
    		/*
        	session.setAttribute("userCookie", new UserCookie(request));
    		*/
        	// [WAB] --------------------------------------------------------------------------
        	// [PayCast] ext ----------------------------------------------------------- start
        	//
        	//
    		
        	PayUserCookie userCookie = new PayUserCookie(request);
        	if (Util.isValid(appMode)) {
        		userCookie.setAppMode(appMode);
        	}
        	
        	session.setAttribute("userCookie", userCookie);
        	
        	//
        	//
        	// [PayCast] ext ------------------------------------------------------------- end

        	String cookieKey = "currentSiteId_" + dbUser.getId();
        	String cookieValue = Util.cookieValue(request, cookieKey);
        	
        	if (cookieValue == null) {
        		// LoginUser의 dropdownlist의 첫 값 획득
        		// 있으면 변수에 설정하고 쿠키 저장
        		// 없으면 패스
        		cookieValue = successLoginUser.getFirstSiteIdInUserSites();
        		if (cookieValue != null) {
        			response.addCookie(Util.cookie(cookieKey, cookieValue));
        		}
        	} else {
        		// LoginUser의 dropdownlist의 값 존재 확인
        		// 있으면 패스
        		// 없으면 
        		// LoginUser의 dropdownlist의 첫 값 획득
        		// 있으면 변수에 설정하고 쿠키 저장
        		// 없으면 패스
        		if (!successLoginUser.hasSiteIdInUserSites(cookieValue)) {
            		cookieValue = successLoginUser.getFirstSiteIdInUserSites();
            		if (cookieValue != null) {
            			response.addCookie(Util.cookie(cookieKey, cookieValue));
            		}
        		}
        	}
        	
        	session.setAttribute("currentSiteId", cookieValue);
        	
        	// [PayCast] ext ----------------------------------------------------------- start
        	//
        	//

        	successLoginUser.setUserStores(storeService.getStoreSwitcherListBySiteIdUserId(
        			Util.parseInt(cookieValue), dbUser.getId()));
        	successLoginUser.setStoreSwitcherShown(successLoginUser.getUserStores() != null && 
        			successLoginUser.getUserStores().size() > 1);
        	
        	String storeCookieKey = "currentStoreId_" + dbUser.getId();
        	String storeCookieValue = Util.cookieValue(request, storeCookieKey);
        	
        	if (storeCookieValue == null) {
        		// LoginUser의 dropdownlist의 첫 값 획득
        		// 있으면 변수에 설정하고 쿠키 저장
        		// 없으면 패스
        		storeCookieValue = successLoginUser.getFirstStoreIdInUserStores();
        		if (storeCookieValue != null) {
        			response.addCookie(Util.cookie(storeCookieKey, storeCookieValue));
        		}
        	} else {
        		if (Util.hasThisPriv(session, "internal.ManageSiteJob")) {
					Store dest = SolUtil.getStore(storeCookieValue);
					if (dest == null) {
            			storeCookieValue = successLoginUser.getFirstStoreIdInUserStores();
                		if (storeCookieValue != null) {
                			response.addCookie(Util.cookie(storeCookieValue, storeCookieValue));
                		}
					}
        		} else {
            		if (!successLoginUser.hasStoreIdInUserStores(storeCookieValue)) {
            			storeCookieValue = successLoginUser.getFirstStoreIdInUserStores();
                		if (storeCookieValue != null) {
                			response.addCookie(Util.cookie(storeCookieValue, storeCookieValue));
                		}
            		}
        		}
        	}
        	
        	successLoginUser.setStoreIdName(SolUtil.getStore(storeCookieValue));
        	
        	session.setAttribute("currentStoreId", storeCookieValue);
        	
        	
        	//
        	//
        	// [PayCast] ext ------------------------------------------------------------- end
        	
        	
        	session.removeAttribute("mainMenuLang");
        	session.removeAttribute("mainMenuData");
        	
        	// 세션 무효화 권한 사용자 처리
        	if (Util.hasThisPriv(session, "internal.NoTimeOut")) {
        		session.setMaxInactiveInterval(-1);
        	}
        	
        	// 사용자의 View 설정
        	userService.setUserViews(successLoginUser, cookieValue, null, session, locale);
    	} catch (Exception e) {
    		logger.error("doLogin", e);
    		
    		return "common.server.msg.loginError";
    	}

        return "OK";
    }
    
	/**
	 * 로그인 액션
	 */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody String login(@RequestBody User loginUser, HttpSession session, 
    		Locale locale, HttpServletRequest request, HttpServletResponse response) {
    	
    	String username = loginUser.getUsername();
    	String password = loginUser.getPassword();
    	
    	String result = doLogin(username, password, "", session, locale, request, response);
    	
    	if (result.equals("OK")) {
    		return result;
    	} else {
    		throw new ServerOperationForbiddenException(msgMgr.message(result, locale));
    	}
    }
    
	/**
	 * 앱 로그인 액션
	 */
    @RequestMapping(value = "/applogin", method = RequestMethod.POST)
    public void appLogin(HttpServletRequest request, HttpSession session, 
    		Locale locale, HttpServletResponse response) throws ServletException, IOException {
    	
    	String username = Util.parseString(request.getParameter("username"));
    	String password = Util.parseString(request.getParameter("password"));
    	String appMode = Util.parseString(request.getParameter("appMode"));
    	
    	String result = doLogin(username, password, appMode, session, locale, request, response);
        
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
      		  response.getOutputStream(), "UTF-8"));
        
        response.setHeader("Cache-Control", "no-store");              // HTTP 1.1
        response.setHeader("Pragma", "no-cache, must-revalidate");    // HTTP 1.0
        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        out.print(result);
        out.close();
    }
    
	/**
	 * 로그아웃 액션
	 */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
    	userService.logout(session);
    	
    	return new ModelAndView("redirect:/");
    }
    
	/**
	 * 패스워드 변경 액션
	 */
    @RequestMapping(value = "/passwordupdate", method = RequestMethod.POST)
    public @ResponseBody String updatePassword(@RequestBody FormRequest form, HttpSession session, 
    		Locale locale, HttpServletRequest request) {
    	String currentPwd = form.getCurrentPassword();
    	String newPwd = form.getNewPassword();
    	String confirmPwd = form.getConfirmPassword();
    	
    	LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

    	if (currentPwd == null || currentPwd.isEmpty() || newPwd == null || newPwd.isEmpty() ||
    			confirmPwd == null || confirmPwd.isEmpty() || loginUser == null) {
    		throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
    	}
    	
    	// RSA 인코딩되었을 때의 처리
    	if (currentPwd.length() == 512 && newPwd.length() == 512 && confirmPwd.length() == 512) {
    		PrivateKey privateKey = (PrivateKey) session.getAttribute("rsaPrivateKey");
    		if (privateKey != null) {
    			currentPwd = Util.decryptRSA(privateKey, currentPwd);
    			newPwd = Util.decryptRSA(privateKey, newPwd);
    			confirmPwd = Util.decryptRSA(privateKey, confirmPwd);
    		}
    	}
    	//-
    	
    	if (!newPwd.equals(confirmPwd)) {
    		throw new ServerOperationForbiddenException(msgMgr.message("passwordupdate.msg.samePassword", locale));
    	}
    	
    	User dbUser = userService.getUser(loginUser.getId());
    	if (dbUser == null || !Util.isSameUserPassword(currentPwd, dbUser.getSalt(), dbUser.getPassword())) {
    		throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
    	}
    	
    	// 지금부터 새 패스워드 저장
    	dbUser.setPassword(Util.encrypt(newPwd, dbUser.getSalt()));
        
    	dbUser.touchWho(session);
    	
        try {
            userService.saveOrUpdate(dbUser);
        } catch (Exception e) {
    		logger.error("updatePassword", e);
        	throw new ServerOperationForbiddenException("SaveError");
        }

        return "OK";
    }
	
	/**
	 * 엑셀 다운로드 최종 페이지
	 */
	@RequestMapping(value = "/export", method = RequestMethod.GET)
    public View excelExportFile() {
    	return new ExcelDownloadView();
    }
	
	/**
	 * 사용자의 현재 사이트 변경 액션
	 */
	@RequestMapping(value = "/changesite", method = RequestMethod.GET)
    public ModelAndView changeSite(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session, Locale locale) {
		String userUrl = request.getParameter("uri");
		
		if (userUrl == null || userUrl.isEmpty()) {
			userUrl = "/userhome";
		}

		if (session != null) {
			LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
			if (loginUser != null) {
				String cookieKey = "currentSiteId_" + Util.loginUserId(session);
				String cookieValue = request.getParameter("siteId");

				if (!loginUser.hasSiteIdInUserSites(cookieValue)) {
		    		cookieValue = loginUser.getFirstSiteIdInUserSites();
				}

				response.addCookie(Util.cookie(cookieKey, cookieValue));
				
				session.setAttribute("currentSiteId", cookieValue);
			    
				// [PayCast] ext ----------------------------------------------------------- start
				//
				//

				session.removeAttribute("currentStoreId");
				
				//
				//
				// [PayCast] ext ------------------------------------------------------------- end
	        	
	        	// 사용자의 View 설정
	        	userService.setUserViews(loginUser, cookieValue, null, session, locale);
			}
		}
		
    	return new ModelAndView("redirect:" + userUrl);
    }
	
	/**
	 * 사용자의 현재 뷰 변경 액션
	 */
	@RequestMapping(value = "/changeview", method = RequestMethod.GET)
    public ModelAndView changeView(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session, Locale locale) {
		String userUrl = request.getParameter("uri");
		
		if (userUrl == null || userUrl.isEmpty()) {
			userUrl = "/userhome";
		}

		if (session != null) {
			LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
			if (loginUser != null) {
				String viewId = request.getParameter("viewId");

				if (!loginUser.hasViewIdInUserViews(viewId)) {
					viewId = loginUser.getFirstViewIdInUserViews();
				}
	        	
	        	// 사용자의 View 설정
	        	userService.setUserViews(loginUser, Util.getSessionSiteId(session) + "", 
	        			viewId, session, locale);
			}
		}
		
    	return new ModelAndView("redirect:" + userUrl);
    }
	
    /**
     * 로컬 파일 저장을 지원하지 않는 브라우저를 위한 프록시 기능 액션
     * 대상 브라우저: IE9 혹은 그 이하, Safari
     */
    
    @RequestMapping(value = "/proxySave", method = RequestMethod.POST)
    public @ResponseBody void save(String fileName, String base64, 
    		String contentType, HttpServletResponse response) throws IOException {

        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        response.setContentType(contentType);

        byte[] data = DatatypeConverter.parseBase64Binary(base64);

        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.flushBuffer();
    }
    
    /**
     * 로그인 후 엔트리 페이지로 이동 액션
     */
    @RequestMapping(value = "/userhome", method = RequestMethod.GET)
    public ModelAndView userhome(HttpSession session, Locale locale, HttpServletRequest request) {
    	
    	// getFirstReachableUrl의 로직을 먼저 실행 후 선택 필요
    	String url = modelMgr.getFirstReachableUrl(Util.getAppModeFromRequest(request), locale, session);
    	
    	String tmp = Util.parseString((String)request.getParameter("dst"));
    	if (Util.isValid(tmp)) {
    		url =  tmp;
    	}
		
		if (Util.isValid(url)) {
			return new ModelAndView("redirect:" + url);
		} else {
			return new ModelAndView("redirect:/common/passwordupdate");
		}
    }
	
	/**
	 * 앱 로그인 페이지
	 */
    @RequestMapping(value = "/applogin", method = RequestMethod.GET)
    public String appLogin(Model model, Locale locale, HttpServletRequest request,
    		HttpSession session) {
    	
    	// 하이브리드 앱에서의 접근 코드
    	model.addAttribute("appMode", Util.getAppModeFromRequest(request));
    	
		return "applogin";
	}
	
	//
	// [PayCast] ext -----------------------------------------------------------------
	//
	
	/**
	 * 사용자의 현재 상점 변경 액션
	 */
	@RequestMapping(value = "/changestore", method = RequestMethod.GET)
    public ModelAndView changeStore(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session, Locale locale) {
		
		//
		// 1) 일반 사용자(상점주)가 화면 우상단에 있는 StoreSwitcher를 통해 접근
		// 2) 사이트 관리 권한(ManageSiteJob)을 가진 사용자가 상점 선택 페이지에서 대상 상점 선택
		//
		String userUrl = request.getParameter("uri");
		
		if (userUrl == null || userUrl.isEmpty()) {
			userUrl = "/userhome";
		}

		if (session != null) {
			LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
			if (loginUser != null) {
				String cookieKey = "currentStoreId_" + Util.loginUserId(session);
				String cookieValue = request.getParameter("storeId");

				if (Util.hasThisPriv(session, "internal.ManageSiteJob")) {
					Store dest = SolUtil.getStore(cookieValue);
					if (dest == null) {
			    		cookieValue = loginUser.getFirstStoreIdInUserStores();
					}
				} else {
					if (!loginUser.hasStoreIdInUserStores(cookieValue)) {
			    		cookieValue = loginUser.getFirstStoreIdInUserStores();
					}
				}

				response.addCookie(Util.cookie(cookieKey, cookieValue));
				
				loginUser.setStoreIdName(SolUtil.getStore(cookieValue));
	        	
				session.setAttribute("currentStoreId", cookieValue);
				
				// 현재 URI로 접근을 위한 저장 삭제
		    	session.removeAttribute("desturi");
	        	
	        	// 사용자의 View 설정
	        	userService.setUserViews(loginUser, cookieValue, null, session, locale);
			}
		}
		
    	return new ModelAndView("redirect:" + userUrl);
    }
	
	/**
	 * 주방 패드 주문 관리 페이지
	 */
    @RequestMapping(value = "/kitchenpad", method = {RequestMethod.GET, RequestMethod.POST})
    public String kitchenPad(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	return "forward:/storecook";
    }
    
	/**
	 * 기기 초기 구동 시 서버에 등록된 기기 정보 반환(stb로 호출) - 가까운 장래에 삭제 예정
	 */
	@RequestMapping(value = "/info/stb", method = RequestMethod.GET)
    public String stbInfo(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/pay/agent/deviceinfo";
    }
    
	/**
	 * 기기 초기 구동 시 서버에 등록된 기기 정보 반환(device로 호출)
	 */
	@RequestMapping(value = "/info/device", method = RequestMethod.GET)
    public String deviceInfo(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/pay/agent/deviceinfo";
    }
	
    /**
	 * FCM 토큰 등록
	 */
	@RequestMapping(value = "/dsg/agent/token", method = {RequestMethod.GET, RequestMethod.POST})
    public String token(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/pay/agent/token";
    }
	
	//
	// [PayCast - kdk] 추가 메소드  20190619 ----------------------------------------------------------------- 시작
	//
	/**
	 * 주방용 패드 로그인 액션
	 * 2019.07.11 주방용 패드 로그인 방식 변경
	 * 2019.07.23 주방용 패드 로그인 방식 폐기 (해당 내용 사용 하지 않음)
	 */
//    @RequestMapping(value = "/logincook", method = RequestMethod.POST)
//    public void logincook(HttpServletRequest request, HttpSession session, 
//    		Locale locale, HttpServletResponse response) throws ServletException, IOException {
//    
//        String username = Util.parseString(request.getParameter("username"), "");
//        String password = Util.parseString(request.getParameter("password"), "");
//    	
//        logger.info("logincook >> username >>> [{}]", username);
//        logger.info("logincook >> password >>> [{}]", password);
//        
//    	String result = doLogin(username, password, "A", session, locale, request, response);
//		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
//		
//        response.setHeader("Cache-Control", "no-store");              // HTTP 1.1
//        response.setHeader("Pragma", "no-cache, must-revalidate");    // HTTP 1.0
//        response.setContentType("text/plain;charset=UTF-8");
//        response.setCharacterEncoding("UTF-8");
//    	
//        logger.info("logincook >> result >>> [{}]", result);
//        
//    	if (result.equals("OK")) {
//    		out.print("OK");
//    	} else {
//    		userService.logout(session);
//    		out.print(msgMgr.message(result, locale));
//    	}
//    	
//    	out.close();
//    }
    
	/**
	 * 메뉴 선택 화면
	 */
    @RequestMapping(value = "/menu", method = {RequestMethod.GET, RequestMethod.POST})
    public String menuChoose(Model model, Locale locale, HttpServletRequest request,
    		HttpSession session, HttpServletResponse response) {
    	return "forward:/smartropay/menu";
	}
    
	/**
	 * 매장 및 메뉴 중 변경된 정보에 대한 변경 수행 명령 내려주는 곳 
	 */
	@RequestMapping(value = "/info/storechgsync", method = RequestMethod.GET)
    public String stbStoreInfoSync(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/store/api/stbStoreInfo";
    }
	
	/**
	 * 매장 오픈 정보 및 키오스크 사용 가능 여부 정보 내려주는곳  
	 */
	@RequestMapping(value = "/info/storeState", method = RequestMethod.GET)
	public String stbStoreState(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/storeStateInfo";
	}
	
	/**
	 * 매장 오픈 정보 및 키오스크 사용 가능 여부 정보 내려주는곳  
	 */
	@RequestMapping(value = "/info/orderList", method = RequestMethod.GET)
	public String orderListInfo(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/orderListInfo";
	}
	
	/**
	 * 매장 및 메뉴 변경에 대한 명령이 수행되었다는 확인 URL
	 */
	@RequestMapping(value = "/info/storecomplete", method = RequestMethod.GET)
    public String storeSyncRe(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/store/api/storecomplete";
    }
    
	/**
	 * 기기에서 매장 정보 요청시 정보 반환
	 * 2019.05.26 해당 URL은 사용하지 않는다. - [kdk]
	 * 매장 정보 변경에 대한 내용은 XML 형식으로 내려주던 방식에서 json으로 내려주는 방식으로 변경
	 * 2019.05.28 해당 메소드 다시 사용(v1.2에서는 상태 유지)
	 * 명령어에 넣어 Json 방식으로 내려주는건 PayCast v1.3이라고 함 / 이후 삭제 필요
	 * 2019.07  위내용은 해당 사항 없음 XML 형식 유지
	 * Json변경은 키오스크 변경도 필요하기때문에 이후에 진행여부를 판단하여 하기로함 
	 */
	@RequestMapping(value = "/info/store", method = RequestMethod.GET)
	public String storeInfo(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/updateStoreInfo";
	}
	
	/**
	 * 메뉴 정보 반환(기기에서 메뉴 정보 요청)
	 * 2019.05.26 해당 URL은 사용하지 않는다. - [kdk]
	 * 매장 정보 변경에 대한 내용은 XML 형식으로 내려주던 방식에서 json으로 내려주는 방식으로 변경
	 * 2019.05.28 해당 메소드 다시 사용(v1.2에서는 상태 유지)
	 * 명령어에 넣어 Json 방식으로 내려주는건 PayCast v1.3이라고 함 / 이후 삭제 필요
	 * 2019.07 위내용은 해당 사항 없음 XML 형식 유지
	 * Json변경은 키오스크 변경도 필요하기때문에 이후에 진행여부를 판단하여 하기로함
	 */
	@RequestMapping(value = "/info/menuInfo", method = RequestMethod.GET)
	public String menuInfo(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/updateMenuInfo";
	}
	
	/**
	 * 컨텐츠 동기화 시 STB 존재 컨텐츠 파일 보고
	 */
	@RequestMapping(value = "/info/dctntreport", method = { RequestMethod.GET, RequestMethod.POST })
    public String dctntReport(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/store/api/dctntreport";
    }
	
	/**
	 * json 방식으로  주문 정보 프린트 내용 내려주기 
	 */
	@RequestMapping(value = "/info/printmenu", method = { RequestMethod.GET, RequestMethod.POST })
    public String printmenu(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/store/api/printmenu";
    }
	
	/**
	 * json 방식으로 주문 정보 프린트에 대한  완료 메시지 
	 */
	@RequestMapping(value = "/info/printcomplete", method = { RequestMethod.GET, RequestMethod.POST })
    public String printcomplete(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/store/api/printcomplete";
    }
	
	/**
	 * kiosk에서 json 방식으로 결제 완료된 내역 정리 및 DB 저장 
	 */
	@RequestMapping(value = "/info/paymentinfo", method = { RequestMethod.GET, RequestMethod.POST })
    public String paymentinfo(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/store/api/kioskpaymentinfo";
    }
	
	/**
	 * 기기에서 메뉴 정보 요청(smilepay 에서 사용)
	 */
	@RequestMapping(value = "/stopViewUrl", method = { RequestMethod.GET, RequestMethod.POST })
    public String stopViewUrl(Model model, HttpServletRequest request, Locale locale, 
    		HttpServletResponse response) throws ServletException, IOException {
		
    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
    				new Message("html_lang", Util.htmlLang(locale)),	
					new Message("errorAdmin", "smilepay.msg.errorAdmin"),
					new Message("pay_resultCode", "smilepay.resultCode"),
					
					
					new Message("approvalDigit", "approval.digit")
    			});
		
		String storeKey = (String)request.getAttribute("storeKey");
		String table = (String)request.getAttribute("table");
		String code = (String)request.getAttribute("code");
		String gubun = (String)request.getAttribute("gubun");
		
		// 사용 하는 code 에 따라 해당 에러 메시지를 출력한다.
		// 1. store 정보 중 스마일 페이 결제에 대한 정보가 없을 경우   SpStoreKey / SpAuthKey
		//    code M001 / gubun NF
		//    code M001 / gubun NF
		// 2. 작업 중 Exception이 발생했을 경우  step1 / code M991 / gubun E
		//    step2 / code M992 / gubun E
		
		
		String menuMsg = StringUtils.trim(msgMgr.message("smilepay."+code, locale));
		
		Store store = storeService.getStoreByStoreKey(storeKey);
    	if(store == null){
			logger.error("/stopViewUrl > payError > storeKey : [{}], table : [{}]", storeKey, table);
			logger.error("/stopViewUrl > payError > code : [{}], gubun : [{}]", code, gubun);
			model.addAttribute("code", "M9999");
    		model.addAttribute("title", msgMgr.message("smilepay.msg.errorNotStore", locale));
    		model.addAttribute("desc", msgMgr.message("smilepay.msg.errorAdmin", locale));
    		return "order/error";
    	}
		
		String info = "";
		if("B".equals(gubun)){
			info = (String)request.getAttribute("info");
			logger.info("/stopViewUrl > payError > storeKey : [{}], table : [{}]", storeKey, table);
			logger.info("/stopViewUrl > payError > code : [{}], info : [{}]", code, info);
		}else if("C".equals(gubun)){
			
			info = (String)request.getAttribute("info");
			String resultCode = (String)request.getAttribute("resultCode");
			String resultMsg = (String)request.getAttribute("resultMsg");
			
			logger.info("/stopViewUrl > payError > storeKey : [{}], table : [{}]", storeKey, table);
			logger.info("/stopViewUrl > payError > code : [{}], info : [{}]", code, info);
			logger.info("/stopViewUrl > payError > resultCode : [{}], resultMsg : [{}]", resultCode, resultMsg);
			
			String menuMsgRet = StringUtils.trim(msgMgr.message("smilepay."+resultCode, locale));
			if(!menuMsgRet.contains("smilepay.")){
				menuMsg = resultMsg;
			}
			
			model.addAttribute("resultCode", resultCode);
		}
		
		// 매장 화면 구성 데이터
		model.addAttribute("storeName", store.getStoreName());
	
		model.addAttribute("menuMsg", menuMsg);
		model.addAttribute("gubun", gubun);
		model.addAttribute("code", code);
		
		model.addAttribute("oriUrl","/menu");
		model.addAttribute("oriUrlparam", store.getStoreKey());
		model.addAttribute("orderTable", table);
		
		// 모바일 상점 로고 타입이 없을 경우 기본 "paycast" text 화면 출력
    	if(store.getStoreOpt() != null){
    		if(Util.isNotValid(store.getStoreOpt().getMobileLogoType())){
	    		// 로고가 없을 경우 
	    		model.addAttribute("mobileLogoType", "T");
	    		model.addAttribute("mobileLogoText","PayCast");
    		}else{
	    		String mLogoImageFilename = "";
	    		UploadFile mLogoImageFile = payService.getUploadFile(store.getStoreOpt().getMobileLogoImageId());
	    		if (mLogoImageFile != null) {
	    			mLogoImageFilename = mLogoImageFile.getFilename();
	    		}
	    		model.addAttribute("mobileLogoType", store.getStoreOpt().getMobileLogoType());
	    		model.addAttribute("mobileLogoText", store.getStoreOpt().getMobileLogoText());
	    		model.addAttribute("mLogoImageFilename", mLogoImageFilename);
				model.addAttribute("storeDownLocation", SolUtil.getUrlRoot("MobileTitle", store.getId()));
    		}
    	}else{
    		model.addAttribute("mobileLogoType", "T");
    		model.addAttribute("mobileLogoText","PayCast");
    	}

    	return "order/stopUrl";
    }
	
	/**
	 * 기기에서 서버 확인 URL
	 */
	@RequestMapping(value = "/info/infoServer", method = RequestMethod.GET)
	public void serverCheck(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) throws ServletException, IOException {
		
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
        		  response.getOutputStream(), "UTF-8"));
		
        response.setHeader("Cache-Control", "no-store");              // HTTP 1.1
        response.setHeader("Pragma", "no-cache, must-revalidate");    // HTTP 1.0
        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        out.print("Y");
        out.close();
	}
	
	/**
	 * 대기자 수를 조회 하기 위해서 사용되는 URL
	 */
	@RequestMapping(value = "/cookordercount", method = RequestMethod.GET)
    public String cookStayCntRead(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/store/api/cookStayCntRead";
    }
	
	/**
	 * DID 명령을 가져가기 위한 URL
	 */
	@RequestMapping(value = "/storedidinfo", method = RequestMethod.GET)
    public String storeDIDInfo(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/store/api/storedidinfo";
    }
	/**
	 * DID 명령을 가져가기 위한 URL
	 */
	@RequestMapping(value = "/storedidreport", method = RequestMethod.GET)
	public String storedidreport(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/storedidreport";
	}
	
	/**
	 * 주문 알림 된 내용 List 보내기(DID 목록 내려주기)
	 */
	@RequestMapping(value = "/cookalarmlist", method = RequestMethod.GET)
    public String cookAlramListRead(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/store/api/cookAlarmListRead";
    }
	
	/**
	 * 주문 완료 된 내용 List 보내기(DID 삭제 목록)
	 */
	@RequestMapping(value = "/cookcomlist", method = RequestMethod.GET)
	public String cookcomlisttRead(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/cookComListRead";
	}
	
	/**
	 *  주문 완료 된 내용 List DID에 대한  완료 메시지 
	 */
	@RequestMapping(value = "/cookdispcom", method = RequestMethod.GET)
    public String cookdispcom(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session) {
		return "forward:/store/api/cookdispcom";
    }
	
	/**
	 * 키오스크 주문 한 메뉴를 취소 하기위해서 승인번호가 맞는지 체크 하는 로직
	 */
	@RequestMapping(value = "/cancelVerifiCheck", method = RequestMethod.GET)
	public String cancelVerifiCheck(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/cancelVerifiCheck";
	}
	
	/**
	 * 키오스크 취소된 내역을 저장 및 처리
	 */
	@RequestMapping(value = "/cancelSuccess", method = { RequestMethod.GET, RequestMethod.POST })
	public String cancelSuccess(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/cancelSuccess";
	}
	
	/**
	 * 키오스크 취소된 내역을 저장 및 처리
	 */
	@RequestMapping(value = "/operEnd", method = { RequestMethod.GET, RequestMethod.POST })
	public String operEnd(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/operEnd";
	}
    
	/**
	 * 주문 번호 중앙 관리를 위한 키오스크용 API 
	 */
	@RequestMapping(value = "/api/ordernum", method = { RequestMethod.GET, RequestMethod.POST })
	public String ordernum(HttpServletRequest request, HttpServletResponse response, Locale locale, 
			HttpSession session) {
		return "forward:/store/api/ordernum";
	}
	
	/**
	 * 리필 가능 체크 키오스크용 API 
	 */
	@RequestMapping(value = "/api/rfChk", method = { RequestMethod.GET, RequestMethod.POST })
	public String rfChk(HttpServletRequest request, HttpServletResponse response, Locale locale, 
			HttpSession session) {
		return "forward:/store/api/refillCheck";
	}
	
	/**
	 * 바나나 포스 연동 - 주문 결제 내역 상세 
	 */
	@RequestMapping(value = "/api/pospayment", method = { RequestMethod.GET, RequestMethod.POST })
	public String pospayment(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/pospayment";
	}
	
	/**
	 * 바나나 포스 연동 - 메뉴 조회 
	 */
	@RequestMapping(value = "/api/posmenu", method = { RequestMethod.GET, RequestMethod.POST })
	public String posmenu(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "forward:/store/api/posmenu";
	}
	
	/**
	 * 주소 조회 
	 */
	@RequestMapping(value = "/popup/juso", method = { RequestMethod.GET, RequestMethod.POST })
	public String juso(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session) {
		return "/jusoPopup";
	}
	
	@RequestMapping(value="/m/menus/S{storeId:.+}/{file_name}", method= RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE) 
	  public @ResponseBody String getContentMediaVideo(@PathVariable("storeId")int storeId,@PathVariable("file_name")String file_name,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {

		  String filePath = SolUtil.getPhysicalRoot("Menu",storeId);
		  
		  System.out.println(filePath);
		  System.out.println(file_name);
		  
	    //progressbar 에서 특정 위치를 클릭하거나 해서 임의 위치의 내용을 요청할 수 있으므로
	    //파일의 임의의 위치에서 읽어오기 위해 RandomAccessFile 클래스를 사용한다.
	    //해당 파일이 없을 경우 예외 발생
		  
		  ServletOutputStream bout = response.getOutputStream();
	    
	    File file = new File(filePath+"/"+ file_name);
	    if( ! file.exists() ) new FileNotFoundException();
	    
	    String res = filePath + "/" + file_name;
	    FileInputStream in = new FileInputStream(res);
	    int length;
	    byte[] buffer = new byte[10];
	    while((length=in.read(buffer)) != -1){
	    	bout.write(buffer,0,length);
	    }
	    return null;
	  }
	
}
