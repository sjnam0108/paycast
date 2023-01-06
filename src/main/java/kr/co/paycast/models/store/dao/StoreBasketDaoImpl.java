package kr.co.paycast.models.store.dao;

import java.util.Date;
import java.util.List;

import kr.co.paycast.models.fnd.User;
import kr.co.paycast.models.store.StoreOrderBasket;
import kr.co.paycast.models.store.StoreOrderBasketList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StoreBasketDaoImpl implements StoreBasketDao {
	
    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public StoreOrderBasketList get(int basketLiId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderBasketList> list = session.createCriteria(StoreOrderBasketList.class)
				.add(Restrictions.eq("id", basketLiId))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
    
	@Override
	public void saveOrUpdate(StoreOrderBasket basket) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(basket);
	}

	@Override
	public void saveOrUpdate(StoreOrderBasketList basketMenu) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(basketMenu);
	}

	@Override
	public StoreOrderBasket getBasketKey(String basketKey) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderBasket> list = session.createCriteria(StoreOrderBasket.class)
				.add(Restrictions.eq("basketKey", basketKey))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public StoreOrderBasket getOrderNum(String orderNum) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderBasket> list = session.createCriteria(StoreOrderBasket.class)
		.add(Restrictions.eq("orderNumber", orderNum))
		.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StoreOrderBasketList> getBasketList(int basketId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreOrderBasketList.class)
				.createAlias("basket", "basket")
				.add(Restrictions.eq("basket.id", basketId)).list();
	}

	@Override
	public void delete(StoreOrderBasket storeOrderBasket) {
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(StoreOrderBasket.class, storeOrderBasket.getId()));
	}
	
	@Override
	public void delete(StoreOrderBasketList storeOrderBasketList) {
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(StoreOrderBasketList.class, storeOrderBasketList.getId()));
	}

	@Override
	public void deleteBasketList(int basketLiId) {
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(StoreOrderBasketList.class, basketLiId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StoreOrderBasket> refiTelbyTime(Date startDate, Date endDate) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreOrderBasket.class)
				.add(Restrictions.ge("whoLastUpdateDate", startDate))
                .add(Restrictions.le("whoLastUpdateDate", endDate))
                .add(Restrictions.isNotNull("refiTel"))
                .add(Restrictions.ne("refiTel",""))
				.addOrder(Order.desc("whoLastUpdateDate"))
				.list();
	}

}
