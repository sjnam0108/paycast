package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.CouponPolicy;
import kr.co.paycast.models.pay.StorePolicy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class CouponPolicyDaoImpl implements CouponPolicyDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(CouponPolicy cp) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(cp);
	}

	@Override
	public CouponPolicy getCouponPolicy(int couponPolicyId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<CouponPolicy> list = session.createCriteria(CouponPolicy.class)
				.add(Restrictions.eq("id", couponPolicyId)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	
    @SuppressWarnings("unchecked")
	@Override
	public List<CouponPolicy> getCouponPolicyList(int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(CouponPolicy.class)
				.createAlias("policy", "policy")
				.createAlias("coupon", "coupon")
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId)).list();
	}

}
