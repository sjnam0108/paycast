package kr.co.paycast.models.pay.dao;

import kr.co.paycast.models.store.StoreCouponLog;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StoreCouponLogDaoImpl implements StoreCouponLogDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(StoreCouponLog log) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(log);
	}

}
