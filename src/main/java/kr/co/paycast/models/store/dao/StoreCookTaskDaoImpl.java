package kr.co.paycast.models.store.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.co.paycast.models.store.StoreCookTask;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class StoreCookTaskDaoImpl implements StoreCookTaskDao {
	
    @Autowired
    private SessionFactory sessionFactory;


	@Override
	public void saveOrUpdate(StoreCookTask storeCookTask) {
		Session session = sessionFactory.getCurrentSession();
		
        session.saveOrUpdate(storeCookTask);
	}

	@Override
	public List<StoreCookTask> getTaskListByStoreIdDeviceId(int storeId, String deviceId, String status) {
		Session session = sessionFactory.getCurrentSession();

		Calendar afterTime = Calendar.getInstance();
		afterTime.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
		afterTime.set(Calendar.HOUR_OF_DAY , 00);
		afterTime.set(Calendar.MINUTE, 00);
		afterTime.set(Calendar.SECOND, 00);
		afterTime.set(Calendar.MILLISECOND, 0);
    	Date fromDate1 = afterTime.getTime();
		
		@SuppressWarnings("unchecked")
		List<StoreCookTask> list = session.createCriteria(StoreCookTask.class)
				.add(Restrictions.eq("storeId", storeId))
				.add(Restrictions.eq("deviceID", deviceId))
				.add(Restrictions.eq("status", status))
				.add(Restrictions.ge("whoCreationDate", fromDate1))
				.list();
		
		return list;
	}

	@Override
	public StoreCookTask getStoreCookTask(int taskId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreCookTask> list = session.createCriteria(StoreCookTask.class)
				.add(Restrictions.eq("id", taskId))
				.list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

}
