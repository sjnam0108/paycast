package kr.co.paycast.models.store.service;

import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StoreSiteService {
	// Common
	public void flush();

	// 매장 정보 XML 만들기(키오스크 정보 전달)
	public void makeStoreInfoXmlFile(String storeId, Document document);
	
	//store Id 로 저장된 Montask 정보 가져오기
	public Document getMonListByStoreId(String storeId, String deviceId, Document document, HttpSession session);
	
	//명령실행에 대한 확인
	public boolean setMonTaskCommandUpdate(String rcCmdId, String result, HttpSession session);

}
