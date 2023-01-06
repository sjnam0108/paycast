package kr.co.paycast.models.pay.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.StoreOpt;

@Transactional
@Component
public class StoreOptDaoImpl implements StoreOptDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(StoreOpt storeOpt) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(storeOpt);
	}

}
