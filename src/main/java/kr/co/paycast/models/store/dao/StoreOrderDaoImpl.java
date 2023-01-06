package kr.co.paycast.models.store.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.co.paycast.models.store.StoreDelivery;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderBasket;
import kr.co.paycast.models.store.StoreOrderList;
import kr.co.paycast.models.store.StoreOrderPay;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StoreOrderDaoImpl implements StoreOrderDao {
	private static final Logger logger = LoggerFactory.getLogger(StoreOrderDaoImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(StoreOrder condFile) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(condFile);
	}
    
	@Override
	public void saveOrder(StoreOrder orderDao) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(orderDao);
	}

	@Override
	public void updateOrder(StoreOrder orderRes) {
		Session session = sessionFactory.getCurrentSession();
		
        session.update(orderRes);
	}
	
	@Override
	public void saveOrderList(List<StoreOrderList> orderList) {
		Session session = sessionFactory.getCurrentSession();
		
        for (StoreOrderList storeOrderList : orderList) {
        	session.saveOrUpdate(storeOrderList);
        }
	}

	@Override
	public StoreOrder getOrder(int storeId, String oID) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.eq("orderNumber", oID))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public StoreOrder getOrderOne(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("id", id))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public StoreOrder getOrderbyOrderNum(String orderNum) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("orderNumber", orderNum))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(StoreOrderPay orderPay) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(orderPay);
	}
	
	@Override
	public int saveOrderPay(StoreOrderPay orderPay) {
		Session session = sessionFactory.getCurrentSession();
		
		Integer id = (Integer)session.save(orderPay);
		
		return id;
	}

	@Override
	public int getOrderCount(int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        int size = 0;
        try {
        String newDate = transFormat.format(cal.getTime());
        Date toDate = transFormat.parse(newDate);

		@SuppressWarnings("unchecked")
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.ge("whoCreationDate", toDate))
				.list();
			size = list.size();
        } catch (Exception e) {
        	logger.error("getOrderCount", e);
        }
		
		return size;
	}

	@Override
	public void delete(StoreOrder storeOrder) {
		Session session = sessionFactory.getCurrentSession();
		
        session.delete(session.load(StoreOrder.class, storeOrder.getId()));
	}

	@Override
	public void saveOrUpdate(StoreOrderList storeOrderList) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(storeOrderList);
	}
	
	@Override
	public List<StoreOrderList> getOrderList(String order) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderList> list = session.createCriteria(StoreOrderList.class)
				.add(Restrictions.eq("orderNumber", order))
				.list();
		
		return list;
	}

	@Override
	public void deleteOrderList(List<StoreOrderList> resList) {
		Session session = sessionFactory.getCurrentSession();
		
        for (StoreOrderList storeOrderList : resList) {
            session.delete(session.load(StoreOrderList.class, storeOrderList.getId()));
        }
	}


	@Override
	public List<StoreOrder> getMenuOrderListStore(int storeId, String orderPrint) {
		Session session = sessionFactory.getCurrentSession();
		
		// 마감 정산을 시작 하면 오늘 전날 부터 지금 시간 까지 마감처리를 시작한다. 
		Calendar afterTime = Calendar.getInstance();
		afterTime.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
		afterTime.set(Calendar.HOUR_OF_DAY , 00);
		afterTime.set(Calendar.MINUTE, 00);
		afterTime.set(Calendar.SECOND, 00);
		afterTime.set(Calendar.MILLISECOND, 0);
		Date operDate = afterTime.getTime();
//		logger.info("/print >>> getMenuOrderListStore  >> 매장ID  > [{}] / operDate > [{}]", storeId, operDate);
		
		@SuppressWarnings("unchecked")
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.eq("orderPrint", orderPrint))
				.add(Restrictions.ge("whoCreationDate", operDate))
				.list();
		
		return list;
	}

	@Override
	public List<StoreOrder> getMenuCancelListStore(int storeId, String device, String cancelprintstatus) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.eq("orderDevice", device))
				.add(Restrictions.eq("orderCancelPrint", cancelprintstatus))
				.list();
		
		return list;
	}
	

	@Override
	public StoreOrderPay getOrderPayAuthDate(int orderPayId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderPay> list = session.createCriteria(StoreOrderPay.class)
				.add(Restrictions.eq("id", orderPayId))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public boolean isRegisteredPayAuthCode(int storeId, String authCode) {
		Session session = sessionFactory.getCurrentSession();
		
		// 마감 정산을 시작 하면 오늘 전날 부터 지금 시간 까지 마감처리를 시작한다. 
		Calendar afterTime = Calendar.getInstance();
		afterTime.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
		afterTime.set(Calendar.HOUR_OF_DAY , 00);
		afterTime.set(Calendar.MINUTE, 00);
		afterTime.set(Calendar.SECOND, 00);
		afterTime.set(Calendar.MILLISECOND, 0);
		Date operDate = afterTime.getTime();
		
		logger.info("/kioskpaymentinfo >>> isRegisteredPayAuthCode 겹침 확인  >> 매장ID  > [{}] ", storeId);
		logger.info("/kioskpaymentinfo >>> isRegisteredPayAuthCode 겹침 확인  >> payAuthCode  > [{}], operDate > [{}]", authCode, operDate);
		
		@SuppressWarnings("unchecked")
		List<StoreOrderPay> list = session.createCriteria(StoreOrderPay.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.eq("payAuthCode", authCode))
				.add(Restrictions.ge("whoCreationDate", operDate))
				.list();
		
		return (list.size() > 0);
	}
	
	@Override
	public List<StoreOrder> getOrderListDate(Date fromDate1, Date toDate2, int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		// orderPrint가 Error가 아닌 경우에만 조회 한다. 
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.gt("orderPayId", 0))
				.add(Restrictions.ge("whoCreationDate", fromDate1))
                .add(Restrictions.le("whoCreationDate", toDate2))
                .add(Restrictions.ne("orderPrint", "E"))
                .addOrder(Order.desc("whoCreationDate"))
				.list();
		
		return list;
	}

	@Override
	public StoreOrderList getOrderListOne(int storeOrderListId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrderList> list = session.createCriteria(StoreOrderList.class)
				.add(Restrictions.eq("id", storeOrderListId))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public List<StoreOrder> getMenuCancelListStorebyAllDevice(int storeId, String cancelprintstatus) {
		Session session = sessionFactory.getCurrentSession();
		
		// 마감 정산을 시작 하면 오늘 전날 부터 지금 시간 까지 마감처리를 시작한다. 
		Calendar afterTime = Calendar.getInstance();
		afterTime.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
		afterTime.set(Calendar.HOUR_OF_DAY , 00);
		afterTime.set(Calendar.MINUTE, 00);
		afterTime.set(Calendar.SECOND, 00);
		afterTime.set(Calendar.MILLISECOND, 0);
		Date operDate = afterTime.getTime();
//		logger.info("/print >>> getMenuCancelListStorebyAllDevice >> 매장ID  > [{}] / operDate > [{}]", storeId, operDate);
		
		@SuppressWarnings("unchecked")
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.eq("orderCancelPrint", cancelprintstatus))
				.add(Restrictions.ge("whoCreationDate", operDate))
				.list();
		
		return list;
	}
	
	@Override
	public List<StoreOrder> getMenuCancelListStorebyPadDevice(int storeId, String canceldidstatus) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
		.add(Restrictions.eq("storeId", storeId))
		.add(Restrictions.eq("orderCancelPad", canceldidstatus))
		.list();
		
		return list;
	}

	@Override
	public List<StoreOrder> getMenuOrderListTime(int storeId, Date time) {
		Session session = sessionFactory.getCurrentSession();
		
		// 주문이 완료된 내역 (orderPayId 0이 아닌 경우 결제 완료)
		@SuppressWarnings("unchecked")
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.ne("orderPayId", 0))
				.add(Restrictions.ge("whoLastUpdateDate", time))
				.list();
		
		return list;
	}
	
	@Override
	public List<StoreOrder> getMenuCancelListTime(int storeId, Date time) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.ne("orderPayId", 0))
				.add(Restrictions.eq("orderCancelStatus", "Y"))
				.add(Restrictions.ge("whoLastUpdateDate", time))
				.list();
		
		return list;
	}

	
	@Override
	public void saveOrUpdate(StoreDelivery deli) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(deli);
	}

	@Override
	public StoreDelivery getDeliveryOne(int deliveryId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreDelivery> list = session.createCriteria(StoreDelivery.class)
				.add(Restrictions.eq("id", deliveryId))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public List<StoreOrder> getOrderListDatebyTelPhone(Date fromDate,
			Date toDate, int storeId, String viewTel) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		// orderPrint가 Error가 아닌 경우에만 조회 한다. 
		List<StoreOrder> list = session.createCriteria(StoreOrder.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.gt("orderPayId", 0))
				.add(Restrictions.ge("whoCreationDate", fromDate))
                .add(Restrictions.le("whoCreationDate", toDate))
                .add(Restrictions.ne("orderPrint", "E"))
                .add(Restrictions.eq("telNumber", viewTel))
                .addOrder(Order.desc("whoCreationDate"))
				.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StoreOrder> telNumberbyTime(Date startDate, Date endDate) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreOrder.class)
				.add(Restrictions.ge("whoLastUpdateDate", startDate))
                .add(Restrictions.le("whoLastUpdateDate", endDate))
                .add(Restrictions.isNotNull("telNumber"))
                .add(Restrictions.ne("telNumber",""))
				.list();
	}

}
