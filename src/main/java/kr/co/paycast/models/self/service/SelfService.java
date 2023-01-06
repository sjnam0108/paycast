package kr.co.paycast.models.self.service;

import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SelfService {
	// Common
	public void flush();

	// 메뉴 목록 가져와 XML 생성
	public void makeMenuInfoXmlFile(String storeId, String catId, Document document);
	
	// 매장정보 업데이트에 대한 정보 DB저장
	public void setMonTask(String page, String storeId, HttpSession session);

}
