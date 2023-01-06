package kr.co.paycast.models.pay.dao;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.PayComparator;
import kr.co.paycast.models.pay.Store;

@Transactional
@Component
public class DeviceDaoImpl implements DeviceDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public Device get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Device> list = session.createCriteria(Device.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(Device device) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(device);
	}

	@Override
	public void saveAndReorder(Device device, HttpSession httpSession) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(device);
		session.flush();
		
		reorder(device.getStore().getId(), device.getDeviceType(), httpSession);
	}

	@Override
	public void delete(Device device) {

		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(Device.class, device.getId()));
	}

	@Override
	public void delete(List<Device> devices) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (Device device : devices) {
            session.delete(session.load(Device.class, device.getId()));
        }
	}

	@Override
	public DataSourceResult getList(DataSourceRequest request) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("store", Store.class);
		map.put("store.site", Site.class);
		
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), Device.class, map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getListByStoreId(int storeId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(Device.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getListByStoreIdDeviceType(int storeId, String deviceType) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(Device.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("deviceType", deviceType))
				.add(Restrictions.eq("store.id", storeId)).list();
	}

	@Override
	public Device getLastByStoreIdDeviceType(int storeId, String deviceType) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Device> list = session.createCriteria(Device.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId))
				.add(Restrictions.eq("deviceType", deviceType))
				.addOrder(Order.desc("deviceSeq")).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void reorder(int storeId, String deviceType, HttpSession httpSession) {
		
		Session session = sessionFactory.getCurrentSession();
		
		List<Device> list = getListByStoreIdDeviceType(storeId, deviceType);
		Collections.sort(list, PayComparator.DeviceDeviceSeqComparator);
		
		int cnt = 1;
		for (Device item : list) {
			item.setDeviceSeq(cnt++);
			item.touchWho(httpSession);
			
			session.saveOrUpdate(item);
		}
	}

	@Override
	public Device getByUkid(String ukid) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Device> list = session.createCriteria(Device.class)
				.add(Restrictions.eq("ukid", ukid)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public Device getEffByUkid(String ukid) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Date time = new Date();
		
		Criterion rest1 = Restrictions.lt("store.effectiveStartDate", time);
		Criterion rest2 = Restrictions.isNull("store.effectiveEndDate");
		Criterion rest3 = Restrictions.gt("store.effectiveEndDate", time);

		@SuppressWarnings("unchecked")
		List<Device> list = session.createCriteria(Device.class)
				.createAlias("store", "store")
				.add(Restrictions.and(rest1, Restrictions.or(rest2, rest3)))
				.add(Restrictions.eq("ukid", ukid)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public Device getEffByUkidStoreShortName(String ukid, String shortName) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Date time = new Date();
		
		Criterion rest1 = Restrictions.lt("store.effectiveStartDate", time);
		Criterion rest2 = Restrictions.isNull("store.effectiveEndDate");
		Criterion rest3 = Restrictions.gt("store.effectiveEndDate", time);

		@SuppressWarnings("unchecked")
		List<Device> list = session.createCriteria(Device.class)
				.createAlias("store", "store")
				.add(Restrictions.and(rest1, Restrictions.or(rest2, rest3)))
				.add(Restrictions.eq("store.shortName", shortName))
				.add(Restrictions.eq("ukid", ukid)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

}
