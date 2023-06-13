package kr.co.paycast.models.store.dao;

import java.util.List;

import kr.co.paycast.models.store.StoreCookAlarm;
import kr.co.paycast.models.store.StoreOrderCook;

public interface StoreCookDao {

	// Cook 목록을 조회 한다.  
	public StoreOrderCook getStoreCookbyOne(int cookId);
	
	public StoreOrderCook getStoreCookbyStoreOrderId(int storeOrderId);
	
	public List<StoreOrderCook> getStoreCookStayList(int storeIdInt, String orderMenuComplete);
	
	// StoreOrderCook에 대기상태의 List 목록을 가져온다. 
	public List<StoreOrderCook> getStoreCookStayList(int storeIdInt, String orderMenuComplete, boolean today);

	// 결제된 내역을 저장 
	public void saveOrUpdate(StoreOrderCook storeOrderCook);

	// 알림 저장  (단건)
	public void saveOrUpdate(StoreCookAlarm storeCookAlarm);
	
	// 알림에 대한 내용 저장
	public void saveAlarm(List<StoreCookAlarm> cookAlarm);

	// Cook 대기 목록(StoreOrderCook에 대기상태의 List 목록) count를 조회
	public int getStayCntRead(int storeIdInt, String orderMenuComplete);
	
	// DID 알람에 사용될 LIST를 조회
	public List<StoreCookAlarm> getAlramListByStoreIdDeviceId(int storeId, String deviceId, String orderDIDComplete, String orderAlarmGubun);

	// StoreCookAlarm 1건 가져오기
	public StoreCookAlarm getDplyStoreCookAlarm(int cookAlarmId);
	
	public List<StoreOrderCook> getStoreOrderCookListByCreated (String createdBy, String orderMenuComplete);

}
