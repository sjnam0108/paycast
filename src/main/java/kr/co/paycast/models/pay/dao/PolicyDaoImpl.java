package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.StorePolicy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class PolicyDaoImpl implements PolicyDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(StorePolicy policy) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(policy);
	}
	
	@Override
	public StorePolicy getPolicy(int policyId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StorePolicy> list = session.createCriteria(StorePolicy.class)
				.add(Restrictions.eq("id", policyId)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public List<StorePolicy> getPolicyList(int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StorePolicy.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId)).list();
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public List<StorePolicy> getPolicyList(int storeId, String type) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StorePolicy.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("type", type))
				.add(Restrictions.eq("store.id", storeId)).list();
	}

}
