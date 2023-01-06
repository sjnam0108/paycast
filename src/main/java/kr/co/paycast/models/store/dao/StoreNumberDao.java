package kr.co.paycast.models.store.dao;

import kr.co.paycast.models.store.StoreOrderNumber;



public interface StoreNumberDao {

	public void saveOrUpdate(StoreOrderNumber orderNum);

	public StoreOrderNumber getOrderNumbyStoreID(int storeId);

}
