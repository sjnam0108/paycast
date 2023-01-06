package kr.co.paycast.models.store.service;

import java.util.Date;
import java.util.List;

import kr.co.paycast.models.store.StoreOrderBasket;
import kr.co.paycast.models.store.StoreOrderBasketList;
import kr.co.paycast.models.store.dao.StoreBasketDao;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.self.MenuPayItem;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("StoreBasketService")
public class StoreBasketServiceImpl implements StoreBasketService {
	private static final Logger logger = LoggerFactory.getLogger(StoreBasketServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
    
	@Autowired
	private StoreBasketDao basketDao;
    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	@Override
	public StoreOrderBasketList get(int basketLiId) {
		return basketDao.get(basketLiId);
	}
	
	@Override
	public void saveOrUpdate(StoreOrderBasket basket) {
		basketDao.saveOrUpdate(basket);
	}

	@Override
	public StoreOrderBasket getBasketKey(String basketKey) {
		return basketDao.getBasketKey(basketKey);
	}

	@Override
	public StoreOrderBasket getOrderNum(String orderNum) {
		return basketDao.getOrderNum(orderNum);
	}
	
	@Override
	public void saveOrUpdate(StoreOrderBasketList basketMenu) {
		basketDao.saveOrUpdate(basketMenu);
	}
	
	@Override
	public void firstSave(int storeId, String basketKey, int totalCount,
			int totalAmt, String table, String menuInTime, List<MenuPayItem> payitemList) {
		StoreOrderBasket basket = new StoreOrderBasket(storeId, basketKey, totalCount, totalAmt, table, menuInTime);
		saveOrUpdate(basket);
		if(payitemList.size() > 0){
			int i=0;
			for(MenuPayItem payItem : payitemList){
				StoreOrderBasketList basketList = new StoreOrderBasketList(basket, Util.parseInt(payItem.getId()), Util.parseInt(payItem.getOrderCount()), payItem.getName(),
						Util.parseInt(payItem.getPrice()), Util.parseInt(payItem.getToPrice()), PayUtil.seqChgCipher(i), payItem.getPacking(), payItem.getEssVal(),
						payItem.getEssName(), payItem.getAddVal(), payItem.getAddName(), basketKey, payItem.getCompSelect());
				saveOrUpdate(basketList);
				i++;
			}
		}
	}

	@Override
	public List<StoreOrderBasketList> getBasketList(int basketId) {
		return basketDao.getBasketList(basketId);
	}

	@Override
	public void delete(StoreOrderBasket storeOrderBasket) {
		basketDao.delete(storeOrderBasket);
	}
	
	@Override
	public void delete(StoreOrderBasketList storeOrderBasketList) {
		basketDao.delete(storeOrderBasketList);
	}

	@Override
	public void deleteBasketList(int basketLiId) {
		basketDao.deleteBasketList(basketLiId);
	}

	
	// startDate 부터 endDate 까지 조회 하여 전화 번호값이 있으면 삭제 
	@Override
	public void refiTelbyTime(Date startDate, Date endDate) {
		logger.info("==============================================================");
		List<StoreOrderBasket> basketList = basketDao.refiTelbyTime(startDate, endDate);
		
		if(basketList.size() > 0){
			for(StoreOrderBasket basket : basketList){
				basket.setRefiTel("");
				basket.touchWho("-1");
				
				logger.info("");
				logger.info("Delete refiTel > basket.getId() [{}]", basket.getId());
				
				basketDao.saveOrUpdate(basket);
			}
		}
		
		logger.info("basketList.size() [{}]", basketList.size());
		logger.info("==============================================================");

	}
	
}
