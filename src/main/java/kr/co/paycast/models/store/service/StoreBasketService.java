package kr.co.paycast.models.store.service;

import java.util.Date;
import java.util.List;

import kr.co.paycast.models.store.StoreOrderBasket;
import kr.co.paycast.models.store.StoreOrderBasketList;
import kr.co.paycast.viewmodels.self.MenuPayItem;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StoreBasketService {
	// Common
	public void flush();
	
	// 장바구니 ORDER
	public void saveOrUpdate(StoreOrderBasket basket);

	public StoreOrderBasket getBasketKey(String basketKey);
	
	public StoreOrderBasket getOrderNum(String orderNum);
	
	public void delete(StoreOrderBasket storeOrderBasket);
	
	// 장바구니 ORDER MENU LIST
	public void saveOrUpdate(StoreOrderBasketList basketMenu);
	
	public void firstSave(int storeId, String basketKey, int totalCount,
			int totalAmt, String table, String menuInTime, List<MenuPayItem> payitemList);
	
	// 장바구니 ORDER MENU LIST 불어오기
	public StoreOrderBasketList get(int basketLiId);
	
	public List<StoreOrderBasketList> getBasketList(int basketId);

	public void delete(StoreOrderBasketList storeOrderBasketList);

	public void deleteBasketList(int basketLiId);

	// startDate 부터 endDate 까지 조회 하여 전화 번호값이 있으면 삭제 
	public void refiTelbyTime(Date startDate, Date endDate);

}
