package kr.co.paycast.models.store.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.co.paycast.models.store.StoreCookAlarm;
import kr.co.paycast.models.store.StoreOrderCook;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StoreCookDaoImpl implements StoreCookDao {
	private static final Logger logger = LoggerFactory.getLogger(StoreCookDaoImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<StoreOrderCook> getStoreCookStayList(int storeIdInt, String orderMenuComplete) { 
    	return getStoreCookStayList(storeIdInt, orderMenuComplete, false); 
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public List<StoreOrderCook> getStoreCookStayList(int storeIdInt, String orderMenuComplete, boolean today) {
		Session session = sessionFactory.getCurrentSession();
		
		List<StoreOrderCook> list = null;
		if("N".equals(orderMenuComplete)){
			// 1.조회된 일자 기준으로 하루전 날짜를 가져온다. 
			Calendar afterTime = Calendar.getInstance();
			if(today == false) {
				afterTime.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
			}else {
			afterTime.get(Calendar.DATE);
			}
			afterTime.set(Calendar.HOUR_OF_DAY , 00);
			afterTime.set(Calendar.MINUTE, 00);
			afterTime.set(Calendar.SECOND, 00);
			afterTime.set(Calendar.MILLISECOND, 0);
	    	Date fromDate1 = afterTime.getTime();
			System.out.println("fromDate"+fromDate1);
			list = session.createCriteria(StoreOrderCook.class)
					.add(Restrictions.eq("storeId", storeIdInt))
					.add(Restrictions.ne("orderMenuComplete", orderMenuComplete))
					.add(Restrictions.ge("whoCreationDate", fromDate1))
					.addOrder(Order.asc("whoLastUpdateDate"))
					.list();	
		}else{
			list = session.createCriteria(StoreOrderCook.class)
					.add(Restrictions.eq("storeId", storeIdInt))
					.add(Restrictions.ne("orderMenuComplete", orderMenuComplete))
					.list();	
		}
		
		return list;
	}

	@Override
	public void saveOrUpdate(StoreOrderCook storeOrderCook) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(storeOrderCook);
	}

	@Override
	public void saveOrUpdate(StoreCookAlarm storeCookAlarm) {
		Session session = sessionFactory.getCurrentSession();
		
        session.saveOrUpdate(storeCookAlarm);
	}
	
	@Override
	public void saveAlarm(List<StoreCookAlarm> cookAlarm) {
		Session session = sessionFactory.getCurrentSession();
		
        for (StoreCookAlarm storeCookAlarm : cookAlarm) {
        	session.saveOrUpdate(storeCookAlarm);
        }
	}

	@Override
	public StoreOrderCook getStoreCookbyStoreOrderId(int storeOrderId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderCook> list = session.createCriteria(StoreOrderCook.class)
				.add(Restrictions.eq("storeOrderId", storeOrderId))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public StoreOrderCook getStoreCookbyOne(int cookId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderCook> list = session.createCriteria(StoreOrderCook.class)
				.add(Restrictions.eq("id", cookId))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public int getStayCntRead(int storeIdInt, String orderMenuComplete) {
		return ((Long)sessionFactory.getCurrentSession().createCriteria(StoreOrderCook.class)
				.add(Restrictions.eq("storeId", storeIdInt))
				.add(Restrictions.ne("orderMenuComplete", orderMenuComplete))
				.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@Override
	public List<StoreCookAlarm> getAlramListByStoreIdDeviceId(int storeId, String deviceId, String orderDIDComplete, String orderAlarmGubun) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreCookAlarm> list = session.createCriteria(StoreCookAlarm.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.eq("deviceID", deviceId))
				.add(Restrictions.eq("orderDIDComplete", orderDIDComplete))
				.add(Restrictions.eq("orderAlarmGubun", orderAlarmGubun))
				.list();
		
		return list;
	}

	@Override
	public StoreCookAlarm getDplyStoreCookAlarm(int cookAlarmId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreCookAlarm> list = session.createCriteria(StoreCookAlarm.class)
				.add(Restrictions.eq("id", cookAlarmId))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public List<StoreOrderCook> getStoreOrderCookListByCreated(String createdBy, String orderMenuComplete) {
		Session session = sessionFactory.getCurrentSession();
		logger.info(createdBy);
		logger.info(orderMenuComplete);
		@SuppressWarnings("unchecked")
		List<StoreOrderCook> list = session.createCriteria(StoreOrderCook.class)
				.add(Restrictions.eq("whoCreatedBy", createdBy))
				.add(Restrictions.eq("orderMenuComplete", orderMenuComplete))
				.list();
		
		return list;
	}
	
}
