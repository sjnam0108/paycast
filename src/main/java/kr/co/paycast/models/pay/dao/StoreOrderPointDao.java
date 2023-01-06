package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.store.StoreOrderPoint;

public interface StoreOrderPointDao {

	public void saveOrUpdate(StoreOrderPoint savePoint);
	
	public List<StoreOrderPoint> getPointbyTel(int storeId, String tel);

}
