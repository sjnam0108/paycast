package kr.co.paycast.models.pay.dao;

import kr.co.paycast.models.store.StorePointLog;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StorePointLogDaoImpl implements StorePointLogDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(StorePointLog log) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(log);
	}

}
