package kr.co.paycast.controllers.pay;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * PayCast Agent 컨트롤러
 */
@Controller("pay-agent-controller")
@RequestMapping(value="/pay/agent")
public class AgentController {

	private static final Logger logger = LoggerFactory.getLogger(AgentController.class);
	
	@Autowired 
    private DeviceService devService;


    /**
	 * 기기 초기 구동 시 서버에 등록된 기기 정보 반환
	 */
    @RequestMapping(value = "/deviceinfo", method = RequestMethod.GET)
    public void deviceInfo(HttpServletRequest request, Locale locale,
    		HttpServletResponse response) 
    				throws ServletException, IOException {
    	Document document = DocumentHelper.createDocument();
    	
        Element rootEl = document.addElement("SignCast");
        Element stbEl = rootEl.addElement("Server");
        rootEl.addAttribute("generated", Util.toSimpleString(new Date()));
        
        String deviceId = request.getParameter("deviceId");
        String storeUkid = request.getParameter("store");
        if (Util.isNotValid(storeUkid)) {
        	storeUkid = request.getParameter("site");
        }
        
        String stbId = "-1";
        String stbName = "";
        String stbUdpPort = "11001";
        String stbServiceType = "I";
        String ftpHost = "www.signcast.co.kr";
        String ftpPort = "21";
        String ftpUser = "signcast";
        String ftpPassword = "signcast1234";
        String serverHost = "www.signcast.co.kr";
        String serverPort = "80";
        String serverUkid = "";
        String storeCatid = "";
        String koEnabled = "false";
        String atEnabled = "false";
        String openType = "C";
        
    	String storeId = "0";
    	String storeName = "signcast";
    	String storeAddr = "";
    	String businessNum = "";
    	String storeTel = "";
    	String merchantNum = "";
    	String represent = "";
    	String operatingTime = "";
    	String storeIntroduction = "";
        
    	Device device = null;

        if (Util.isValid(deviceId) && Util.isValid(storeUkid)) {
        	device = devService.getEffDeviceByUkidStoreShortName(deviceId, storeUkid);
        	
        	if (device != null) {
        		Store store = device.getStore();
        		
    	    	storeId = String.valueOf(store.getId());													//매장번호
    	    	storeName = Util.isValid(store.getBizName()) ? store.getBizName() : "";			//상호명
    	    	storeAddr = Util.isValid(store.getAddress()) ? store.getAddress() : "";			//매장주소
    	    	businessNum = Util.isValid(store.getBizNum()) ? store.getBizNum() : "";			//사업자번호
    	    	storeTel = Util.isValid(store.getPhone()) ? store.getPhone() : "";				//매장전화번호
    	    	represent = Util.isValid(store.getBizRep()) ? store.getBizRep() : ""; 			//대표자명
    	    	operatingTime = Util.isValid(store.getOpenHours()) ? store.getOpenHours() : ""; //운영시간

    	    	
    	    	// stbUdpPort, stbServiceType 기본값 반환
        		stbId = String.valueOf(device.getId());
        		stbName = SolUtil.getPubDeviceName(device, locale);
        		
        		
        		Site site = store.getSite();
        		
        		ftpHost = site.getFtpHost();
        		ftpPort = String.valueOf(site.getFtpPort());
        		ftpUser = site.getFtpUsername();
        		ftpPassword = site.getFtpPassword();
        		serverHost = site.getServerHost();
        		serverPort = String.valueOf(site.getServerPort());
        		
        		// store.shortName 을 넘겨줘야 하므로 변경
        		// serverUkid = site.getShortName();
        		serverUkid = device.getStore().getShortName();
        		
        		// 매장소개, 가맹점번호, CAT ID 는 기본값 반환
    	    	//storeIntroduction = "";
    	    	//merchantNum = "";
        		//storeCatid = "";
        		 
				koEnabled = String.valueOf(store.isKioskOrderEnabled());
				atEnabled = String.valueOf(store.isAlimTalkAllowed());
				openType = store.getOpenType();
        	}
        }
        
        stbEl.addAttribute("stbid", stbId);
        stbEl.addAttribute("stbname", stbName);
        stbEl.addAttribute("stbudpport", stbUdpPort);
        stbEl.addAttribute("stbservicetype", stbServiceType);
        stbEl.addAttribute("ftphost", ftpHost);
        stbEl.addAttribute("ftpport", ftpPort);
        stbEl.addAttribute("ftpuser", ftpUser);
        stbEl.addAttribute("ftppassword", ftpPassword);
        stbEl.addAttribute("serverhost", serverHost);
        stbEl.addAttribute("serverport", serverPort);
        stbEl.addAttribute("serverukid", serverUkid);
        stbEl.addAttribute("storeCatid", storeCatid);
        
		stbEl.addAttribute("storeId", storeId);//매장번호
		stbEl.addAttribute("storeName", storeName);//매장명
		stbEl.addAttribute("storeAddr", storeAddr);//매장주소
		stbEl.addAttribute("businessNum", businessNum);//사업자번호
		stbEl.addAttribute("storeTel", storeTel);//매장전화번호
		stbEl.addAttribute("merchantNum", merchantNum); //가맹점 번호
		stbEl.addAttribute("represent", represent); //대표자명
		stbEl.addAttribute("operatingTime", operatingTime); //운영시간
		stbEl.addAttribute("storeIntroduction", storeIntroduction);//매장소개
		stbEl.addAttribute("koEnabled", koEnabled); //키오스크 허용 및 주문 가능에 대한 true / false(defalt)
		stbEl.addAttribute("atEnabled", atEnabled); //알림톡 사용에 대한 true / false(defalt)
		stbEl.addAttribute("openType", openType); //영업 여부  O(영업중) / C(영업마감)
		
        response.setHeader("Cache-Control", "no-store");              // HTTP 1.1
        response.setHeader("Pragma", "no-cache, must-revalidate");    // HTTP 1.0
        response.setContentType("text/xml;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter pw = response.getWriter();

        document.write(pw);
	}
	
    /**
	 * FCM 토큰 등록
	 */
    @RequestMapping(value = "/token", method = {RequestMethod.GET, RequestMethod.POST})
    public void setFCMToken(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
        
        String ret = "F";
        
    	request.setCharacterEncoding("UTF-8");
    	
        String deviceId = Util.parseString(request.getParameter("deviceId"), "");
        String token = Util.parseString(request.getParameter("token"), "");
        
        if (!Util.isValid(deviceId) || !Util.isValid(token)) {
        	// 잘못된 인자 전달
        	// Pass: return "F"
        } else {
        	ret = "N";
        	
        	Device device = devService.getEffDeviceByUkid(deviceId);
        	if (device != null) {
        		try {
        			if (device.getFcmToken() == null || !device.getFcmToken().equals(token)) {
        				device.setFcmToken(URLDecoder.decode(token,"UTF-8"));
        				device.touchWho(null);
            			
        				devService.saveOrUpdate(device);
        			}
        		} catch (Exception e) {
                	logger.error("setFCMToken", e);
        		}
        		
        		ret = "Y";
        	}

    		String syncUrl = Util.getFileProperty("url.syncToken");
    		if (Util.isValid(syncUrl)) {
    			String result = Util.readResponseFromUrl(syncUrl.replace("{0}", deviceId).replace("{1}", token));
    			if (Util.isValid(result) && result.equals("N")) {
		        	logger.info("setFCMToken - wrong deviceID: " + deviceId);
    			}
    		}
        }
        
        response.setHeader("Cache-Control", "no-store");              // HTTP 1.1
        response.setHeader("Pragma", "no-cache, must-revalidate");    // HTTP 1.0
        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter pw = response.getWriter();

        pw.print(ret);
        pw.flush();
    }
}
