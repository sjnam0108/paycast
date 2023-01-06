package kr.co.paycast.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class SmilePayUtil {
	private static final Logger logger = LoggerFactory.getLogger(SmilePayUtil.class);
	
	public static final String encodeMD5HexBase64(String hashData) {
		return new String(Base64.encodeBase64(DigestUtils.md5Hex(hashData).getBytes()));
	}
	
	public static String urlEncodeEuckr(String str){
		try {
			str =  URLEncoder.encode(str, "euc-kr");
		} catch (UnsupportedEncodingException e) {
        	logger.error("payError", e);
        	throw new ServerOperationForbiddenException("OperationError");
		}
		return str;
	}
	
	public static String sendByPost(Hashtable<String,String> request, String actionURL) throws Exception{
		HttpClient client = null;
		HostConfiguration hc = null;
		NameValuePair[] params;
		PostMethod postMethod = null;

		final int CONNECTION_TIMEOUT = 60000;
		final int RECEIVE_TIMEOUT = 15000;

		int hashSize = request.size();
		int statusCode = 0;
		String key = "";
		Enumeration keys = request.keys();

		try{
			client = new HttpClient();
			hc = new HostConfiguration();
			
			client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT); // connection Timeout
			client.getHttpConnectionManager().getParams().setSoTimeout(RECEIVE_TIMEOUT);// receive Timeout

			postMethod = new PostMethod(actionURL);
			hc.setHost(postMethod.getURI().getHost(), postMethod.getURI().getPort());
			
			logger.info("Connect to Client : [{}]", actionURL);

			params = new NameValuePair[hashSize];
			for(int i=0; i<hashSize; i++){
				key = (String) keys.nextElement();
				params[i]= new NameValuePair(key, (String) request.get(key));
				System.out.println("param" + (i+1) + " : " + key + " : " + request.get(key));
			}
			
			postMethod.setRequestBody(params);
			
			try{
				statusCode = client.executeMethod(postMethod);
			}catch (Exception e){
				e.printStackTrace();
				return "ERROR:" + e.getMessage();
			}

			if( statusCode == 200 ){
				String result1 = postMethod.getResponseBodyAsString();
				logger.info("서버응답 : [{}]", statusCode);
				logger.info("Response from CLIENT : [{}]", nullCheck(result1).trim());
				return result1;
			}else{
				String result2 = postMethod.getResponseBodyAsString();
				logger.info("서버응답 : [{}]", statusCode);
				logger.info("Response from CLIENT : [{}]", nullCheck(result2).trim());
				return "ERROR:ClientServerError:" + statusCode;
			}
		}catch (Exception e){
			e.printStackTrace();
			return "ERROR:" + e.getMessage();
		}finally{
			if (postMethod != null){
				postMethod.releaseConnection();
			}
			client.getHttpConnectionManager().getConnection(hc).close();
			System.out.println("Release Connection success");
		}
	}
	
	/**
	* 스트링 null 체크
	* @param o
	* @return
	*/
	public static String nullCheck(Object o){
		String temp = "";
	
		if (o != null){
			  temp = (String) o;
			  try{
				  temp = (temp.length()>0 && "null".equals(temp.trim().toLowerCase()) )? "":temp;
			  }catch(Exception exx){
				  exx.printStackTrace();
			  }
		}
		return temp;
	}
	
	/**
	* 응답메세지 파싱
	* @param plainText
	* @param delim
	* @param delim2
	* @return
	*/
	public static Hashtable<String,String> parseMessage(String plainText, String delim, String delim2){
		Hashtable<String,String> retData = new Hashtable<String,String>();
		ArrayList<String> tokened_array = tokenizerWithBlanks(plainText, delim);
		String temp = "";
		for (int i = 0; i < tokened_array.size(); i++) {
			temp = tokened_array.get(i);
			if (StringUtils.isNotEmpty(temp)) {
				retData.put( temp.substring(0,temp.indexOf(delim2)),temp.substring(temp.indexOf(delim2)+1).trim() );
			}
		}
		return retData;
	}
	
	/**
	* 입력받은 스트링을 공백과 함께 delimiter로 자른다.
	*
	* @param input 파싱할 문자열
	* @param delimiter 구분자
	*/
	public static ArrayList<String> tokenizerWithBlanks(String input, String delimiter){
		ArrayList<String> array = new ArrayList<String>();
		String token;
		int pos;
		int delimiterSize = delimiter.length();
		do{
			pos = input.indexOf(delimiter);
			if (pos >= 0){
				token = input.substring(0, pos);
				input = input.substring(pos + delimiterSize);
			}else{
				token = input;
				input = "";
			}
			array.add(token);
		} while (pos >= 0);
		return array;
	}
	
	public static String urlDecodeEuckr(String str){
		try {
			str =  URLDecoder.decode(str, "euc-kr");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String getRemoteAddr(HttpServletRequest request) {
        String ip = null;
        ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
        return ip;
    }
	
}
