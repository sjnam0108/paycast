package kr.co.paycast.models.pay.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StoreDeliveryPay;
import kr.co.paycast.models.pay.StoreDeliveryPolicy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class DeliveryDaoImpl implements DeliveryDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public StoreDeliveryPay getDeliveryPay(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreDeliveryPay> list = session.createCriteria(StoreDeliveryPay.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
    
	@SuppressWarnings("unchecked")
	@Override
	public List<StoreDeliveryPay> getDeliveryPayList(int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreDeliveryPay.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId)).list();
	}
	
	@Override
	public void saveOrUpdate(StoreDeliveryPay deliveryPay) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(deliveryPay);
	}
	
	@Override
	public void deliveryPayDelete(StoreDeliveryPay deliveryPay) {
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(deliveryPay);
	}
	
	@Override
	public DataSourceResult getDeliveryPayRead(DataSourceRequest request) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("store", Store.class);
		map.put("store.site", Site.class);
		
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), StoreDeliveryPay.class, map);
	}
	
	@Override
	public StoreDeliveryPolicy getDeliveryPolicy(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreDeliveryPolicy> list = session.createCriteria(StoreDeliveryPolicy.class)
				.add(Restrictions.eq("store.id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StoreDeliveryPolicy> getDeliveryPolicyList(int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreDeliveryPolicy.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId)).list();
	}
	
	@Override
	public void saveOrUpdate(StoreDeliveryPolicy minOderPay) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(minOderPay);		
	}

	@Override
	public DataSourceResult getgetMinOrderPayRead(DataSourceRequest request) {
		System.err.println(request);
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("store", Store.class);
		map.put("store.site", Site.class);
		
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), StoreDeliveryPolicy.class, map);
	}

}
