package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.store.StoreOrderCoupon;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StoreOrderCouponDaoImpl implements StoreOrderCouponDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveOrUpdate(StoreOrderCoupon compStamp) {
    	Session session = sessionFactory.getCurrentSession();
    	
    	session.saveOrUpdate(compStamp);
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public List<StoreOrderCoupon> getCouponStamp(int storeId, String tel, String type) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreOrderCoupon.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("type", type))
				.add(Restrictions.eq("tel", tel))
				.add(Restrictions.eq("store.id", storeId)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StoreOrderCoupon> getIssueCouponRead(int storeId, String tel, String type) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreOrderCoupon.class)
				.createAlias("store", "store")
				.createAlias("coupon", "coupon")
				.add(Restrictions.eq("type", type))
				.add(Restrictions.eq("tel", tel))
				.add(Restrictions.eq("useState", 0))
				.add(Restrictions.eq("store.id", storeId)).list();
	}

	@Override
	public StoreOrderCoupon getCoupon(int couponId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderCoupon> list = session.createCriteria(StoreOrderCoupon.class)
				.add(Restrictions.eq("id", couponId)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
}
