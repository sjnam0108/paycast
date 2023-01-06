package kr.co.paycast.models.pay.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.StoreEtc;

@Transactional
@Component
public class StoreEtcDaoImpl implements StoreEtcDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(StoreEtc storeEtc) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(storeEtc);
	}

}
