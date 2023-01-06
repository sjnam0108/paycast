package kr.co.paycast.models.pay.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.DeviceTask;

@Transactional
@Component
public class DeviceTaskDaoImpl implements DeviceTaskDao {

    @Autowired
    private SessionFactory sessionFactory;
    
	@Override
	public DeviceTask get(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<DeviceTask> list = session.createCriteria(DeviceTask.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(DeviceTask deviceTask) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(deviceTask);
	}

	@Override
	public void delete(DeviceTask deviceTask) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(DeviceTask.class, deviceTask.getId()));
	}

	@Override
	public void delete(List<DeviceTask> deviceTasks) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (DeviceTask deviceTask : deviceTasks) {
            session.delete(session.load(DeviceTask.class, deviceTask.getId()));
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceTask> getListByStoreId(int storeId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(DeviceTask.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceTask> getListByDeviceId(int deviceId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(DeviceTask.class)
				.createAlias("device", "device")
				.add(Restrictions.eq("device.id", deviceId)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceTask> getListByDeviceIdStatus(int deviceId, String status) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(DeviceTask.class)
				.createAlias("device", "device")
				.add(Restrictions.eq("status", status))
				.add(Restrictions.eq("device.id", deviceId)).list();
	}

}
