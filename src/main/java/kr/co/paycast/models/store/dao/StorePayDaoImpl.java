package kr.co.paycast.models.store.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StorePayDaoImpl implements StorePayDao {
	
    @Autowired
    private SessionFactory sessionFactory;

	
}
