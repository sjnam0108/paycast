package kr.co.paycast.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.UploadFile;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.pay.service.OptService;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.models.pay.service.StoreService;

@Component
public class SolUtil {
	//private static final Logger logger = LoggerFactory.getLogger(SolUtil.class);

	/**
	 * 사이트 설정 String 값의 동일 여부 반환(session 값으로)
	 */
	public static boolean propEqVal(HttpSession session, String code, String value) {
		String tmp = getProperty(session, code);
		
		return (Util.isValid(tmp) && tmp.equals(value));
	}

	/**
	 * 사이트 설정 String 값의 동일 여부 반환(사이트 번호로)
	 */
	public static boolean propEqVal(int siteId, String code, String value) {
		String tmp = getProperty(siteId, code);
		
		return (Util.isValid(tmp) && tmp.equals(value));
	}
	
	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(HttpSession session, String code) {
		return getProperty(session, code, null, "");
	}

	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(HttpSession session, String code, Locale locale) {
		return getProperty(session, code, locale, "");
	}

	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(HttpSession session, String code, Locale locale, 
			String defaultValue) {
		String value = getPropertyValue(Util.getSessionSiteId(session), code, locale);
		
		return Util.isValid(value) ? value : defaultValue;
	}
	
	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(int siteId, String code) {
		return getProperty(siteId, code, null, "");
	}

	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(int siteId, String code, Locale locale) {
		return getProperty(siteId, code, locale, "");
	}

	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(int siteId, String code, Locale locale, 
			String defaultValue) {
		String value = getPropertyValue(siteId, code, locale);
		
		return Util.isValid(value) ? value : defaultValue;
	}
	
	private static String getPropertyValue(int siteId, String code, Locale locale) {
		
    	// [WAB] --------------------------------------------------------------------------
		/*
		if (Util.isValid(code) && 
				(code.equals("logo.title") || code.equals("quicklink.max.menu"))) {
			return Util.getFileProperty(code);
		} else {
			return "";
		}
		*/
    	// [WAB] --------------------------------------------------------------------------
    	// [PayCast] ext ----------------------------------------------------------- start
    	//
    	//
		
		return sOptService.getSiteOption(siteId, code, locale);
		
    	//
    	//
    	// [PayCast] ext ------------------------------------------------------------- end
	}
	
	//
	// [PayCast] ext -----------------------------------------------------------------
	//
	
	@Autowired
	public void setStaticMsgMgr(MessageManager msgMgr) {
		SolUtil.sMsgMgr = msgMgr;
	}
	
	@Autowired
	public void setStaticOptService(OptService optService) {
		SolUtil.sOptService = optService;
	}
	
	@Autowired
	public void setStaticStoreService(StoreService storeService) {
		SolUtil.sStoreService = storeService;
	}
	
	@Autowired
	public void setStaticDeviceService(DeviceService devService) {
		SolUtil.sDevService = devService;
	}
	
	@Autowired
	public void setStaticPayService(PayService payService) {
		SolUtil.sPayService = payService;
	}
	
	static MessageManager sMsgMgr;
	
	static OptService sOptService;
	static StoreService sStoreService;
	static DeviceService sDevService;
	static PayService sPayService;

	
	/**
	 * 작업 관련 최후의 날짜 획득
	 */
	public static Date getMaxTaskDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		cal.add(Calendar.YEAR, 5);
		
		return cal.getTime();
	}
	
	/**
	 * 작업 관련 최후의 날짜 획득
	 */
	public static Store getStore(String storeIdStr) {
		
		return sStoreService.getStore(Util.parseInt(storeIdStr));
	}
	
	/**
	 * 상점 관리자 페이지 접근 가능 여부 확인
	 */
	public static boolean canAccessToStoreAdminPage(HttpSession session) {
	
		if (Util.hasThisPriv(session, "internal.ManageSiteJob")) {
			String currentStoreId = (String) session.getAttribute("currentStoreId");
			
			return Util.isValid(currentStoreId) && getStore(currentStoreId) != null;
		}
		
		return false;
	}
	
	/**
	 * 상점 사용자 페이지 접근 가능 여부 확인
	 */
	public static boolean canAccessToStoreCustomerPage(HttpSession session) {
	
		String currentStoreId = (String) session.getAttribute("currentStoreId");
		
		if (Util.isNotValid(currentStoreId)) {
			return false;
		}
		
		Store store = getStore(currentStoreId);
		return store != null && (Util.hasThisPriv(session, "internal.ManageSiteJob") ||
					sStoreService.isRegisteredStoreUser(store.getId(), Util.loginUserId(session)));
	}
	
	/**
	 * 상점 개요 정보 모델 획득
	 */
	public static void addStoreOverviewInfo(Model model, Locale locale, 
			HttpSession session, HttpServletRequest request) {
    	
    	int storeId = -1;
    	LoginUser loginUser = null;
    	if (session != null) {
    		loginUser = (LoginUser) session.getAttribute("loginUser");
    		if (loginUser != null) {
    			storeId = loginUser.getStoreId();
    		}
    	}
		
    	String storeName = "", shortName = "";
    	String startTime="", endTime="",oderPosibleTime="";
    	
    	Boolean koAllowed = false, moAllowed = false, kpAllowed = false;
    	Integer kCnt = 0, dCnt = 0, nCnt = 0, pCnt = 0;
    	
    	Store store = sStoreService.getStore(storeId);
    	if (store != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a",Locale.US);
        	storeName = store.getStoreName();
        	shortName = store.getShortName();
        	startTime = sdf.format(store.getStartTime());
        	endTime = sdf.format(store.getEndTime());
    		Calendar cal = Calendar.getInstance();
   		
    		if(store.getStoreEtc().getOder_possible_Time()!=null){
    			cal.setTime(store.getStoreEtc().getOder_possible_Time());	
    		}
    		cal.add(Calendar.MINUTE, store.getStoreEtc().getOder_setting_Time());
    		
        	oderPosibleTime = sdf.format(cal.getTime());

        	koAllowed = store.isKioskOrderAllowed();
    		moAllowed = store.isMobileOrderAllowed();
    		kpAllowed = store.isKitchenPadAllowed();
    		
        	List<Device> list = sDevService.getDeviceListByStoreId(store.getId());
        	for (Device device : list) {
        		if (device.getDeviceType().equals("K")) { kCnt++; }
        		else if (device.getDeviceType().equals("D")) { dCnt++; }
        		else if (device.getDeviceType().equals("N")) { nCnt++; }
        		else if (device.getDeviceType().equals("P")) { pCnt++; }
        	}
    	}
    	
    	model.addAttribute("ovwStoreName", storeName);
    	model.addAttribute("ovwShortName", shortName);
    	
    	model.addAttribute("koAllowed", koAllowed);
    	model.addAttribute("moAllowed", moAllowed);
    	model.addAttribute("kpAllowed", kpAllowed);
    	
    	model.addAttribute("kCnt", kCnt);
    	model.addAttribute("dCnt", dCnt);
    	model.addAttribute("nCnt", nCnt);
    	model.addAttribute("pCnt", pCnt);
    	
    	model.addAttribute("startTime", startTime);
    	model.addAttribute("endTime", endTime);
    	model.addAttribute("oderPosibleTime", oderPosibleTime);
    	
    	
	}
	
	/**
	 * 기기의 일반 명칭 획득
	 */
	public static String getPubDeviceName(Device device, Locale locale) {
	
		if (device != null) {
			if (Util.isValid(device.getFamiliarName())) {
				return device.getFamiliarName();
			}
			
			if (Util.isValid(device.getDeviceType()) && device.getDeviceSeq() != null) {
				String ret = "";
				if (device.getDeviceType().equals("K")) {
					ret = sMsgMgr.message("deviceType.kiosk", locale);
				} else if (device.getDeviceType().equals("D")) {
					ret = sMsgMgr.message("deviceType.kitchenPad", locale);
				} else if (device.getDeviceType().equals("N")) {
					ret = sMsgMgr.message("deviceType.notifier", locale);
				} else if (device.getDeviceType().equals("P")) {
					ret = sMsgMgr.message("deviceType.printer", locale);
				}
				
				if (Util.isValid(ret)) {
					return ret + " #" + String.valueOf(device.getDeviceSeq());
				}
			}
		}
		
		return "";
	}

	/**
	 * 세션에 저장되어 있는 상점 id 획득
	 */
	public static int getSessionStoreId(HttpSession session) {
		int storeId = -1;
		
		if (session != null) {
			String storeIdStr = (String)session.getAttribute("currentStoreId");
			storeId = Util.parseInt(storeIdStr);
		}
		
		return storeId;
	}
	
	/**
	 * 물리적인 루트 디렉토리 획득
	 */
	public static String getPhysicalRoot(String ukid) {
		return getPhysicalRoot(ukid, -1);
	}

	/**
	 * 물리적인 루트 디렉토리 획득
	 */
	public static String getPhysicalRoot(String ukid, int storeId) {
		if (Util.isNotValid(ukid)) {
			return null;
		}
		
		String rootDirPath = Util.getFileProperty("dir.rootPath");
		String ftpDirName = Util.getFileProperty("dir.ftp");
		
		if (ukid.equals("KioskTitle")) {
			return Util.getValidRootDir(rootDirPath) + "titles/S" + storeId;
		} else if (ukid.equals("MobileTitle")) {
            return Util.getValidRootDir(rootDirPath) + "m/titles/S" + storeId;
		} else if (ukid.equals("Menu")) {
            return Util.getValidRootDir(rootDirPath) + "m/menus/S" + storeId;
		} else if (ukid.equals("DepContent")) {
            return Util.getValidRootDir(rootDirPath) + ftpDirName + "/contents";
		} else if (ukid.equals("UpTemp")) {
            return Util.getValidRootDir(rootDirPath) + "uptemp";
		} else if (ukid.equals("Ad")) {
            return Util.getValidRootDir(rootDirPath) + "ad";
		}

        return Util.getPhysicalRoot(ukid);
	}
	
	/**
	 * 웹 접근 혹은 상대경로 접근을 위한 루트 디렉토리 획득
	 */
	public static String getUrlRoot(String ukid) {
		
		return getUrlRoot(ukid, -1);
	}

	/**
	 * 웹 접근 혹은 상대경로 접근을 위한 루트 디렉토리 획득
	 */
	public static String getUrlRoot(String ukid, int storeId) {
		
		if (Util.isNotValid(ukid)) {
			return null;
		}
		
		if (ukid.equals("KioskTitle")) {
			return "/titles/S" + storeId;
		} else if (ukid.equals("MobileTitle")) {
            return "/m/titles/S" + storeId;
		} else if (ukid.equals("Menu")) {
            return "/m/menus/S" + storeId;
		} else if (ukid.equals("UpTemp")) {
            return "/uptemp";
		}

        return "";
	}
	
	/**
	 * 업로드 파일의 파일명 획득
	 */
	public static String getUploadFilename(Integer id, String defPathFilename) {
		
		return getUploadFilename(id, "", defPathFilename);
	}
	
	/**
	 * 업로드 파일의 파일명 획득
	 */
	public static String getUploadFilename(Integer id, String pathName, String defPathFilename) {
		
		UploadFile imgFile = sPayService.getUploadFile(id);
		if (imgFile == null) {
			return defPathFilename;
		} else {
			return pathName + imgFile.getFilename();
		}
	}
}
