package kr.co.paycast.models.pay.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StoreEvent;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class CouponDaoImpl implements CouponDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public StoreCoupon get(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreCoupon> list = session.createCriteria(StoreCoupon.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	@Override
	public StoreEvent getId(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreEvent> list = session.createCriteria(StoreEvent.class)
		.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
    
	@SuppressWarnings("unchecked")
	@Override
	public List<StoreCoupon> getList(int storeId, int deleteState) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreCoupon.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("deleteState", deleteState))
				.add(Restrictions.eq("store.id", storeId)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StoreEvent> getEventList(int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
		Date time = new Date();
		
		Criterion rest1 = Restrictions.lt("effectiveStartDate", time);
		Criterion rest2 = Restrictions.isNull("effectiveEndDate");
		Criterion rest3 = Restrictions.gt("effectiveEndDate", time);
		
		return session.createCriteria(StoreEvent.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId))
				.add(Restrictions.and(rest1, Restrictions.or(rest2, rest3))).list();
	}
	
	@Override
	public void saveOrUpdate(StoreCoupon coupon) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(coupon);
	}
	
	@Override
	public void saveOrUpdate(StoreEvent event) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(event);
	}
	@Override
	public void delete(StoreEvent event) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(event);
	}
	
	@Override
	public DataSourceResult getRead(DataSourceRequest request) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("store", Store.class);
		map.put("store.site", Site.class);
		
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), StoreCoupon.class, map);
	}
	
	@Override
	public DataSourceResult getEventRead(DataSourceRequest request) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("store", Store.class);
		map.put("store.site", Site.class);
		return request.toDataSourceResult(sessionFactory.getCurrentSession(), StoreEvent.class, map);
	}


}
