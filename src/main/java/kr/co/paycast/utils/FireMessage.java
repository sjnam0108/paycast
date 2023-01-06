package kr.co.paycast.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class FireMessage {
	private static final Logger logger = LoggerFactory.getLogger(FireMessage.class);
	private JSONObject root;

	public FireMessage(){
	    root = new JSONObject();
	    JSONObject data = new JSONObject();
	    data.put("title", "");
	    data.put("sound","default");
	    data.put("body", "");
	    root.put("data", data);
	}

	public String sendToGroup(JSONArray mobileTokens) throws Exception {
		String serverKey = Util.getFileProperty("fcm.authKey");
//		logger.info("sendToGroup > serverKey [{}]", serverKey);
		
		if(mobileTokens.size() < 1){
			logger.info("sendToGroup TOKEN [{}]", "TOKEN 없음");
			return "SUCCESS";
		}
		
	    root.put("registration_ids", mobileTokens);
	    return sendPushNotification(false, serverKey);
	}
	
	public String sendToGroupCook(JSONArray mobileTokens) throws Exception {
		String serverKey = Util.getFileProperty("cookfcm.authKey");
//		logger.info("sendToGroupCook > serverKey [{}]", serverKey);
		
		if(mobileTokens.size() < 1){
			logger.info("sendToGroupCook TOKEN [{}]", "TOKEN 없음");
			return "SUCCESS";
		}
		
	    root.put("registration_ids", mobileTokens);
	    return sendPushNotification(false, serverKey);
	}
	
	public String sendToGroupDid(JSONArray mobileTokens) throws Exception { 
		String serverKey = Util.getFileProperty("didfcm.authKey");
//		logger.info("sendToGroupDid > serverKey [{}]", serverKey);
		
		if(mobileTokens.size() < 1){
			logger.info("sendToGroupDid TOKEN [{}]", "TOKEN 없음");
			return "SUCCESS";
		}
	    root.put("registration_ids", mobileTokens);
	    return sendPushNotification(false, serverKey);
	}
	
	public String sendToToken(String token) throws Exception {
		String serverKey = Util.getFileProperty("fcm.authKey");
//		logger.info("sendToToken > serverKey [{}]", serverKey);
//		logger.info("sendToToken > token [{}]", token);
		
		if(Util.isNotValid(token)){
			logger.info("sendToToken TOKEN [{}]", "TOKEN 없음");
			return "SUCCESS";
		}
	    root.put("to", token);
	    return sendPushNotification(false, serverKey);
	}
	
	public String sendToTokenCook(String token) throws Exception {
		String serverKey = Util.getFileProperty("cookfcm.authKey");
//		logger.info("sendToTokenCook > serverKey [{}]", serverKey);
//		logger.info("sendToTokenCook > token [{}]", token);
		
		if(Util.isNotValid(token)){
			logger.info("sendToToken TOKEN [{}]", "TOKEN 없음");
			return "SUCCESS";
		}
		
	    root.put("to", token);
	    return sendPushNotification(false, serverKey);
	}

	public String sendToTokenDid(String token) throws Exception {
		String serverKey = Util.getFileProperty("didfcm.authKey");
//		logger.info("sendToTokenDid > serverKey [{}]", serverKey);
//		logger.info("sendToTokenDid > token [{}]", token);
		
		if(Util.isNotValid(token)){
			logger.info("sendToToken TOKEN [{}]", "TOKEN 없음");
			return "SUCCESS";
		}
		
	    root.put("to", token);
	    return sendPushNotification(false, serverKey);
	}
	
    private String sendPushNotification(boolean toTopic, String serverKey) throws Exception {
    	
    	String apiUrlFcm = Util.getFileProperty("url.sendFcmNotif");
    	
        URL url = new URL(apiUrlFcm);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "key=" + serverKey);

        try {
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//            logger.info("[FCM Send] root.toString() [{}]",  root.toString());
           
            wr.write(root.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
            String result = builder.toString();
//            logger.info("[FCM Send] result [{}]", result);
            
            JSONParser resultParser = new JSONParser();
            JSONObject resultObject = (JSONObject)resultParser.parse(result);
            long success = (long)resultObject.get("success");
            
            if (success > 0) {
                return "SUCCESS";
            }else{
            	JSONArray jsonArray1 = (JSONArray)resultObject.get("results");
            	for(int i=0; i<jsonArray1.size(); i++){
            		JSONObject objectInArray = (JSONObject) jsonArray1.get(i);
            		String errorMsg = (String)objectInArray.get("error");
            		logger.info("[FCM Send] FCM fail success [{}]", errorMsg);
            	}
            	logger.info("[FCM Send] FCM 전송메시지 fail >> [{}]", root.toString());
            }

            return builder.toString();
        } catch (Exception e) {
        	logger.info("[FCM Send] root.toString() [{}]",  root.toString());
        	
        	
        	return e.getMessage();
        }
    }
}
