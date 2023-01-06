package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.store.StoreOrderPoint;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StoreOrderPointDaoImpl implements StoreOrderPointDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(StoreOrderPoint savePoint) {
    	Session session = sessionFactory.getCurrentSession();
    	
    	session.saveOrUpdate(savePoint);
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public List<StoreOrderPoint> getPointbyTel(int storeId, String tel) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreOrderPoint.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("tel", tel))
				.add(Restrictions.eq("store.id", storeId)).list();
	}
    
}
