package kr.co.paycast.models.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.store.StoreCookAlarm;
import kr.co.paycast.models.store.StoreCookTask;
import kr.co.paycast.models.store.StoreOrderCook;
import kr.co.paycast.viewmodels.store.CookJsonItem;

import org.dom4j.Document;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StoreCookService {
	// Common
	public void flush();

	//결제가 완료 되었을 때 저장을 진행 한다. 
	public void saveOrUpdate(StoreOrderCook storeOrderCook);
	
	public List<CookJsonItem> getStoreCookList(String storeId);
	
	public List<CookJsonItem> getStoreCookListRenew(String storeId);
	
	//주문 알림을 요청 
	public boolean alarmUpdate(String cookId, String storeId, String storeOrderId, ArrayList<Object> orderIDList, String complteYn, HttpSession session, Locale locale);

	//대기자 수 조회(대기 목록 조회 - DID, kiosk)
	public int getStayCntAPI(int storeId, String deviceId);
	
	//대기자 수 조회(대기 목록 조회 - 모바일)
	public int getStayCntMobile(int storeId);

	//대기자 수 조회(대기 목록 조회)
	public int getStayCntRead(int storeId);

	//DID에 실행할 명령 내려주기 
	public Document getTaskByStoreIdDeviceId(int storeId, String deviceId, Document document);

	//task에 한건 데이터 가져오기
	public StoreCookTask getStoreCookTask(int taskId);
	
	//주문 완료 된 내용 List를 보내준다(DID 목록 내려주기 . XML 형식)
	public Document getCookAlramListByStoreIdDeviceId(int parseInt,String deviceId, Document document);
	
	//DID에서 삭제할 주문 SEQ 보내주기(DID 목록 삭제 . XML 형식)
	public Document getCookComListByStoreIdDeviceId(int storeId, String deviceId, Document document);

	//주문 완료된 목록 내용 보여주기
	public List<CookJsonItem> getCookComListByStoreId(int storeId);

	public List<CookJsonItem> getStoreCookCancelRenew(String storeId, HttpSession session);
	
	//주문 완료 DID 표출 완료 보고를 위해 get
	public StoreCookAlarm getDplyStoreCookAlarm(int cookAlarmId);
	
	//알림을 저장한다.
	public void saveOrUpdate(StoreCookAlarm storeCookAlarm);

	//완료한 메뉴목록을 취소 한다.
	public void comCancelUpdate(int siteId, String cookId, String storeId, HttpSession session);

	public void saveOrUpdate(StoreCookTask storeCookTask);


}
