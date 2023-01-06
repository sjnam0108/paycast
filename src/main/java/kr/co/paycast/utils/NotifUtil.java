package kr.co.paycast.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.viewmodels.NotifMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class NotifUtil {
	
	@Autowired
	public void setStaticDsgService(PayService payService) {
		NotifUtil.sPayService = payService;
	}
	
	static PayService sPayService;
	
	private static final Logger logger = LoggerFactory.getLogger(NotifUtil.class);
	
	private static String getMessage(String title, String body, List<String> to, boolean isNotification) {
		
		//
		// 알림 메시지 키
		//    참고: https://firebase.google.com/docs/cloud-messaging/http-server-ref?hl=ko
		//
		
	    JSONObject obj = new JSONObject();
	    obj.put("title", (isNotification ? "Notif:" : "") + title);
	    obj.put("body", body);
	    
    	JSONObject msg = new JSONObject();
    	msg.put("data", obj);
    	
    	if (to == null || to.size() == 0) {
    		return null;
    	} else if (to.size() == 1) {
        	msg.put("to", to.get(0));
    	} else {
    		JSONArray mobileTokens = new JSONArray();
    		for(String token : to) {
    			mobileTokens.add(token);
    		}
    		msg.put("registration_ids", mobileTokens);
    	}
    	
    	return msg.toString();
	}
	
    /**
	 * FCM 통지 전송
	 * 
	 *   결과:
	 *          0 - 성공(모두 성공),
	 *         -1 - 실패(모두 실패),
	 *         -2 - 전송 대상 수 0,
	 *         -3 - 잘못된 인자(title or key)
	 *         -4 - 예기치 않은 상태(예: 요청 건수와 결과 건수가 다름 등)
	 *         >0 - 부분 성공(성공한 수)
	 */
	public static int sendFcmNotif(NotifMessage nMsg, String to) {
	
		return sendFcmNotif(nMsg, to, "ko");
	}
	
	public static int sendFcmNotif(NotifMessage nMsg, String to, String lang) {
		
		if (Util.isValid(lang)) {
			return sendFcmNotif(nMsg.getLocalTitle(lang), nMsg.getLocalBody(), to, true);
		} else {
			return sendFcmNotif(nMsg.getLocalTitle(), nMsg.getLocalBody(), to, true);
		}
	}
	
	public static int sendFcmNotif(NotifMessage nMsg, List<String> to) {
		
		return sendFcmNotif(nMsg, to, "ko");
	}
	
	public static int sendFcmNotif(NotifMessage nMsg, List<String> to, String lang) {
		
		if (Util.isValid(lang)) {
			return sendFcmNotif(nMsg.getLocalTitle(lang), nMsg.getLocalBody(lang), to, true);
		} else {
			return sendFcmNotif(nMsg.getLocalTitle(), nMsg.getLocalBody(), to, true);
		}
	}
	
	public static int sendFcmNotif(String title, String body, String to, boolean isNotification) {
		
		return sendFcmNotif(title, body, to, Util.getFileProperty("key.fcm"), isNotification);
	}
	
	public static int sendFcmNotif(String title, String body, List<String> to, boolean isNotification) {
		
		return sendFcmNotif(title, body, to, Util.getFileProperty("key.fcm"), isNotification);
	}
	
	public static int sendFcmNotif(String title, String body, String to, String key, boolean isNotification) {
		
		List<String> list = new ArrayList<String>();
		list.add(to);
		
		return sendFcmNotif(title, body, list, key, isNotification);
	}
	
	public static int sendFcmNotif(String title, String to) {
		
		return sendFcmNotif(title, "", to);
	}
	
	public static int sendFcmNotif(String title, String body, String to) {
		
		return sendFcmNotif(title, body, to, Util.getFileProperty("key.fcm"));
	}
	
	public static int sendFcmNotif(String title, String body, List<String> to) {
		
		return sendFcmNotif(title, body, to, Util.getFileProperty("key.fcm"), false);
	}
	
	public static int sendFcmNotif(String title, String body, String to, String key) {
		
		List<String> list = new ArrayList<String>();
		list.add(to);
		
		return sendFcmNotif(title, body, list, key, false);
	}
	
	public static int sendFcmNotif(String title, String body, List<String> to, String key, boolean isNotification) {
	
        logger.info("");
        
        if (Util.isNotValid(title) || Util.isNotValid(key)) {
            logger.info("[FCM Notif] message: null, result: -3");

			return -3;
        }
        
		try {
			String msg = getMessage(title, body, to, isNotification);
			if (msg == null) {
	            logger.info("[FCM Notif] message: null, result: -2");

				return -2;
			}
            logger.info("[FCM Notif] message: {}",  msg);
        	
            URL url = new URL(Util.getFileProperty("url.sendFcmNotif"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "key=" + key);

            OutputStream os = conn.getOutputStream();
            os.write(msg.toString().getBytes("UTF-8"));
            os.flush();
            os.close();
            
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
            
            String result = builder.toString();

            int ret = -1;
            
            ObjectMapper m = new ObjectMapper();
            JsonNode rootNode = m.readTree(result);
            
            //long multicastId = rootNode.path("multicast_id").asLong();
            int success = rootNode.path("success").asInt();
            int failure = rootNode.path("failure").asInt();
            
            if (success == 0 && failure == 0) {
            	ret = -4;
            } else if (success == 0) {
            	ret = -1;
            } else if (success == to.size()) {
            	ret = 0;
            } else {
            	ret = failure;
            }

            if (ret == 0) {
                logger.info("[FCM Notif] result: Y");
            } else {
                JsonNode results = rootNode.path("results");
            	if (results != null) {
            		int idx = 0;
            		Iterator<JsonNode> iter = results.iterator();
            		while(iter.hasNext()){
            			JsonNode resultNode = iter.next();
            			JsonNode errorNode = resultNode.path("error");
            			if (errorNode != null) {
            				String errorMsg = errorNode.asText();
            				if (Util.isValid(errorMsg) && errorMsg.equals("InvalidRegistration")) {
            	                logger.info("[FCM Notif] error InvalidRegistration: " + to.get(idx));
            	                
            	                sPayService.deactivateAppUserBySystem(to.get(idx));
            				}
            			}
            			idx ++;
            		}
            	}
                logger.info("[FCM Notif] result code: " + ret + ", value: " + result);
            }
            
            return ret;
	    } catch (Exception e) {
    		logger.error("sendFcmNotif", e);
	    }
		
		return -1;
	}
}
