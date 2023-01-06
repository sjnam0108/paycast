package kr.co.paycast.models.store.dao;

import java.util.List;

import kr.co.paycast.models.store.StoreOrderNumber;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StoreNumberDaoImpl implements StoreNumberDao {
	private static final Logger logger = LoggerFactory.getLogger(StoreNumberDaoImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(StoreOrderNumber orderNum) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(orderNum);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public StoreOrderNumber getOrderNumbyStoreID(int storeId) {
		Session session = sessionFactory.getCurrentSession();
		//LockMode.UPGRADE
		

		List<StoreOrderNumber> list = session.createCriteria(StoreOrderNumber.class)
				.add(Restrictions.eq("storeId", storeId)).list();
		
		StoreOrderNumber ras = null;
		if(!list.isEmpty()){
			StoreOrderNumber ordernum = list.get(0);
			logger.info("============================================");
			logger.info("ordernum.getOrderNum()[{}]", ordernum.getOrderNum());
			
			ordernum.setOrderNum(ordernum.getOrderNum()+1);
			session.saveOrUpdate(ordernum);
			
			logger.info("ordernum.getOrderNum() + 1[{}]", ordernum.getOrderNum());
			logger.info("============================================");
			
			ras = ordernum;
		}
		
		return ras;
	}

}
