package kr.co.paycast.models.store.dao;

import kr.co.paycast.models.store.StoreOrderCancel;
import kr.co.paycast.models.store.StoreOrderVerification;

public interface StoreCancelDao {
	
	public StoreOrderCancel get(int cancelId);

	/* StoreOrderVerification */
	public int save(StoreOrderVerification storeOrderVerification);
	
	public void saveOrUpdate(StoreOrderVerification storeOrderVerification);
	
	public StoreOrderVerification getStoreCancelOneDao(int id);

	// 생성된 승인번호를 사용하지 못하도록 변경한다. 
	public StoreOrderVerification getStoreCancelOne(int storeId, int storeOrderId, int orderCancelId);

	public StoreOrderVerification checkVerifiCode(int storeId, String verifiCode);

	//취소 완료된 테이블에 저장하기
	public int saveCancel(StoreOrderCancel storeOrderCancel);


	
}
