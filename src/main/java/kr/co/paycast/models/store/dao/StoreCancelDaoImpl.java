package kr.co.paycast.models.store.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.co.paycast.models.store.StoreOrderCancel;
import kr.co.paycast.models.store.StoreOrderVerification;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StoreCancelDaoImpl implements StoreCancelDao {
	
    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public StoreOrderCancel get(int cancelId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderCancel> list = session.createCriteria(StoreOrderCancel.class)
				.add(Restrictions.eq("id", cancelId))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
    
	@Override
	public int save(StoreOrderVerification storeOrderVerification) {
		Session session = sessionFactory.getCurrentSession();
		
		Integer id = (Integer)session.save(storeOrderVerification);
		
		return id;
	}
	
	@Override
	public void saveOrUpdate(StoreOrderVerification storeOrderVerification) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(storeOrderVerification);
	}
	
	@Override
	public StoreOrderVerification getStoreCancelOneDao(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderVerification> list = session.createCriteria(StoreOrderVerification.class)
				.add(Restrictions.eq("id", id))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public StoreOrderVerification getStoreCancelOne(int storeId, int storeOrderId, int orderCancelId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderVerification> list = session.createCriteria(StoreOrderVerification.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.eq("storeOrderId", storeOrderId))
				.add(Restrictions.eq("id", orderCancelId))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public StoreOrderVerification checkVerifiCode(int storeId, String verifiCode) {
		Session session = sessionFactory.getCurrentSession();
		
		Calendar afterTime = Calendar.getInstance();
		afterTime.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
		afterTime.set(Calendar.HOUR_OF_DAY , 00);
		afterTime.set(Calendar.MINUTE, 00);
		afterTime.set(Calendar.SECOND, 00);
		afterTime.set(Calendar.MILLISECOND, 0);
    	Date fromDate1 = afterTime.getTime();
    	
		@SuppressWarnings("unchecked")
		List<StoreOrderVerification> list = session.createCriteria(StoreOrderVerification.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.eq("verificationCode", verifiCode))
				.add(Restrictions.ge("whoCreationDate", fromDate1))
				.addOrder(Order.desc("whoCreationDate"))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public int saveCancel(StoreOrderCancel storeOrderCancel) {
		Session session = sessionFactory.getCurrentSession();
		
		Integer id = (Integer)session.save(storeOrderCancel);
		
		return id;
	}

}
