package kr.co.paycast.models.store.service;

import kr.co.paycast.models.store.StoreOrderCancel;
import kr.co.paycast.models.store.StoreOrderVerification;
import kr.co.paycast.viewmodels.store.StoreCanCelView;

import org.dom4j.Document;
import org.json.simple.JSONArray;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StoreCancelService {
	// Common
	public void flush();

	public StoreOrderCancel get(int cancelId);
	
	//결제가 완료 되었을 때 저장을 진행 한다. 
	public int save(StoreOrderVerification storeOrderVerification);
	
	public void saveOrUpdate(StoreOrderVerification storeOrderVerification);

	public StoreOrderVerification getStoreCancelOne(int orderCancelId);
	
	public StoreOrderVerification getStoreCancelbystoreIdstoreOrderId(int storeId, int storeOrderId, int orderCancelId);
	
	//고객이 취소 요청했을 경우
	public StoreCanCelView checkVerifiCodebyStoreIdVerifiCode(int storeId, String verifiCode, String deviceGubun);
	
	//매출 조회 화면에서 관리자가 바로 취소 했을 경우 
	public StoreCanCelView immediateCanCel(StoreOrderVerification orderVerification, String verifiCode, String deviceGubun);

	//취소  완료 되었을 때 
	public void cancelSuccessSave(StoreOrderCancel storeOrderCancel, int storeId, String verifiCode);

	//취소가 실패했을 경우 
	public void cancelFailSave(StoreOrderCancel storeOrderCancel, int storeId, String verifiCode);

	//키오스크에서 취소 요청시 승인 번화 확인
	public Document checkVerifiCodebyStoreIdVerifiCodeDeviceID(int storeId, String deviceId, String cancelCode, Document document, String deviceGubun);

	//키오스크에서 취소 완료시 해당 결제 내역 취소 처리
	public boolean cancelSuccessbyStoreIdOrderIdDeviceID(JSONArray objectList);

}
