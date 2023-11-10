package kr.co.paycast.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.models.store.StoreAlimTalk;
import kr.co.paycast.models.store.service.StoreSiteService;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class PayUtil {
	
	@SuppressWarnings("unused")
	@Autowired
	private MessageManager msgMgr;

	@Autowired
	public void setStaticMsgMgr(MessageManager msgMgr) {
		PayUtil.sMsgMgr = msgMgr;
	}
	
	@Autowired
	public void setStaticSiteService(SiteService siteService) {
		PayUtil.sSiteService = siteService;
	}
	
	@Autowired
	public void setStaticStoreSiteService(StoreSiteService storeSiteService) {
		PayUtil.sStoreSiteService = storeSiteService;
	}
	
	static MessageManager sMsgMgr;
	static SiteService sSiteService;
	static StoreSiteService sStoreSiteService;
	
	/**
	 *seq 자릿수를 계산하여 "000" 형식으로 변경한다.
	 * 1 > 001  / 10 > 010 / 99 > 099 / 100이상은 그대로 유지
	 * group과 메뉴를 순서대로 가져오기 위해서 변경
	 */
	public static String seqChgCipherCh(String seq){
		 
		return seqChgCipher(Util.parseInt(seq));
	}
	
	public static String seqChgCipher(int seq){
		return String.format("%03d", seq);
	}
	
	
	/**
	 * 작업 관련 최후의 날짜 획득
	 */
	public static Date getMaxTaskHOUR(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		cal.add(Calendar.HOUR_OF_DAY, 1);
		
		return cal.getTime();
	}
	
    /**
	 * 모바일 여부 체크
	 */
	public static void getMobileCheck(Model model, HttpServletRequest request) {
    	String uAgent = request.getHeader("User-Agent").toLowerCase();
    	if (uAgent.matches(".*(android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||uAgent.substring(0,4).matches("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-")){
    		model.addAttribute("Mobile","Y");
    	}else{
    		model.addAttribute("Mobile","N");
    	}
	}
	
    /**
	 * 기간별 검색 날짜 선택
	 */
	public static void searchDay(Model model, Locale locale){
		Date date = new Date ();
		String currentTime = Util.toDateString(date);
		String fromDay = currentTime;
		// 월초 : beginningMonth, 1주일 : week , 한달 : month / 그외 값 month 고정
		String msgSearchDay = sMsgMgr.message("sales.searchDay", locale);
		if("beginningMonth".equals(msgSearchDay)){
			// 해당월의 월초값
			fromDay = Util.toSimpleString(date, "yyyy-MM") + "-01";
		}else if("week".equals(msgSearchDay)){
			// 일주일 전(7일)
			fromDay = Util.toDateString(Util.addDays(date, -7));
		}else{
			// 한달전 (30일)
			fromDay = Util.toDateString(Util.addDays(date, -30));
		}

		model.addAttribute("toDay",currentTime);
		model.addAttribute("fromDay",fromDay);
	}

	public static String phoneChange(String src) {
		if (src == null) {
			return "";
		}
		if (src.length() == 8) {
			return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
		} else if (src.length() == 12) {
			return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
		}
		return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
	}
	
    /**
	 * 선택한 메뉴의 옵션에서 옵션명 분리
	 */
	public static String separationistName(String[] menuArray) {
		int menuLen = menuArray.length;
		if(menuLen < 1){
			return "";
		}
		String name = menuArray[0];
		if(menuLen > 2){
			for(int i = 1; i < menuLen; i++){
				if(i != (menuLen-1)){
					name += "("+menuArray[i];
				}
			}
		}
		
		return name;
	}
	
	/**
	 * 선택한 메뉴의 옵션에서 가격 분리
	 */
	public static String separationistPrice(String[] menuArray) {
		int menuLen = menuArray.length;
		if(menuLen < 1){
			return "0";
		}
		return menuArray[menuLen-1].replace(")", "");
	}
	
	public static void testServiceApi(StoreAlimTalk alimTalk) throws Exception {


		try {
			JSONObject reqParams = new JSONObject();
			reqParams.put("ShortName", alimTalk.getShortName()); // body에 들어갈 내용을 담는다.
			reqParams.put("Name", alimTalk.getName()); // body에 들어갈 내용을 담는다.
			reqParams.put("Phone", alimTalk.getPhone()); // body에 들어갈 내용을 담는다.
			reqParams.put("OrderSeq", alimTalk.getOrderSeq()); // body에 들어갈 내용을 담는다.
			reqParams.put("allMenu", alimTalk.getAllMenu()); // body에 들어갈 내용을 담는다.
			reqParams.put("finMenu", alimTalk.getFinMenu()); // body에 들어갈 내용을 담는다.
			reqParams.put("telNumber", alimTalk.getTelNumber()); // body에 들어갈 내용을 담는다.
			reqParams.put("senderKey", alimTalk.getSenderKey()); // body에 들어갈 내용을 담는다.
			reqParams.put("tmplCd", alimTalk.getTmplCd()); // body에 들어갈 내용을 담는다.
			reqParams.put("subject", alimTalk.getSubject()); // body에 들어갈 내용을 담는다.
			reqParams.put("msg", alimTalk.getMsg()); // body에 들어갈 내용을 담는다.
			reqParams.put("smsMsg", alimTalk.getSmsmsg()); // body에 들어갈 내용을 담는다.

			URL url = new URL("https://www.test.com/test/open/order/possible-check"); // 호출할 외부 API 를 입력한다.

			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // header에 데이터 통신 방법을 지정한다.
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json; utf-8");

			// Post인 경우 데이터를 OutputStream으로 넘겨 주겠다는 설정
			conn.setDoOutput(true);

			// Request body message에 전송
			OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
			os.write(reqParams.toString());
			os.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
