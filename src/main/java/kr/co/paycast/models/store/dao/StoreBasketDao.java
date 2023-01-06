package kr.co.paycast.models.store.dao;

import java.util.Date;
import java.util.List;

import kr.co.paycast.models.store.StoreOrderBasket;
import kr.co.paycast.models.store.StoreOrderBasketList;


public interface StoreBasketDao {

	public StoreOrderBasketList get(int basketLiId);
	
	public void saveOrUpdate(StoreOrderBasket basket);

	public void saveOrUpdate(StoreOrderBasketList basketMenu);

	public StoreOrderBasket getBasketKey(String basketKey);
	
	public StoreOrderBasket getOrderNum(String orderNum);

	public List<StoreOrderBasketList> getBasketList(int basketId);

	public void delete(StoreOrderBasket storeOrderBasket);

	public void delete(StoreOrderBasketList storeOrderBasketList);

	public void deleteBasketList(int basketLiId);

	public List<StoreOrderBasket> refiTelbyTime(Date startDate, Date endDate);

}
