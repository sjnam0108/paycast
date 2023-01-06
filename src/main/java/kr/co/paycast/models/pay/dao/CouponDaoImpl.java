package kr.co.paycast.models.pay.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreCoupon;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    
	@SuppressWarnings("unchecked")
	@Override
	public List<StoreCoupon> getList(int storeId, int deleteState) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreCoupon.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("deleteState", deleteState))
				.add(Restrictions.eq("store.id", storeId)).list();
	}
	
	@Override
	public void saveOrUpdate(StoreCoupon coupon) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(coupon);
	}
	
	@Override
	public DataSourceResult getRead(DataSourceRequest request) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("store", Store.class);
		map.put("store.site", Site.class);
		
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), StoreCoupon.class, map);
	}


}
