package kr.co.paycast.models.pay.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.utils.Util;

@Transactional
@Component
public class StoreDaoImpl implements StoreDao {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public Store getStoreByStoreKey(String storeKey) {
       Session session = sessionFactory.getCurrentSession();
       @SuppressWarnings("unchecked")
       List<Store> list = session.createCriteria(Store.class).add(Restrictions.eq("storeKey", storeKey)).list();

       return (list.isEmpty() ? null : list.get(0));
    }

    @SuppressWarnings("unchecked")
	@Override
	public List<Store> get() {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(Store.class).list();
	}
    
	@Override
	public Store get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Store> list = session.createCriteria(Store.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public Store getByCode(int code) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Store> list = session.createCriteria(Store.class)
		.add(Restrictions.eq("storeCode", code)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public Store getByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Store> list = session.createCriteria(Store.class)
		.add(Restrictions.eq("storeName", name)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public Store getAll() {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Store> list = session.createCriteria(Store.class).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public Store getStore(String storeKey) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Store> list = session.createCriteria(Store.class)
				.add(Restrictions.eq("storeKey", storeKey)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(Store store) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(store);
	}

	@Override
	public void delete(Store store) {

		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(Store.class, store.getId()));
	}

	@Override
	public void delete(List<Store> stores) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (Store store : stores) {
            session.delete(session.load(Store.class, store.getId()));
        }
	}

	@Override
	public DataSourceResult getList(DataSourceRequest request) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("site", Site.class);
		
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), Store.class, map);
	}

	@Override
	public DataSourceResult getList(DataSourceRequest request, boolean isEffectiveMode) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("site", Site.class);
		
        return request.toEffectiveDataSourceResult(sessionFactory.getCurrentSession(), Store.class, map, 
        		isEffectiveMode);
	}

	@Override
	public Store get(Site site, String shortName) {
		
		Session session = sessionFactory.getCurrentSession();
		
		if (site == null) {
			return null;
		}
		
		@SuppressWarnings("unchecked")
		List<Store> list = session.createCriteria(Store.class)
				.createAlias("site", "site")
				.add(Restrictions.eq("site.id", site.getId()))
				.add(Restrictions.eq("shortName", shortName)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public Store getBySiteIdStoreName(int siteId, String storeName) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Store> list = session.createCriteria(Store.class)
				.createAlias("site", "site")
				.add(Restrictions.eq("site.id", siteId))
				.add(Restrictions.eq("storeName", storeName)).list();
			
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public Store getByStoreKey(String storeKey) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Store> list = session.createCriteria(Store.class)
				.add(Restrictions.eq("storeKey", storeKey)).list();
			
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Store> getListBySiteId(int siteId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(Store.class)
				.createAlias("site", "site")
				.add(Restrictions.eq("site.id", siteId)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Store> getEffectiveList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Date time = new Date();
		
		Criterion rest1 = Restrictions.lt("effectiveStartDate", time);
		Criterion rest2 = Restrictions.isNull("effectiveEndDate");
		Criterion rest3 = Restrictions.gt("effectiveEndDate", time);
		
		return session.createCriteria(Store.class)
				.createAlias("site", "site")
				.add(Restrictions.and(rest1, Restrictions.or(rest2, rest3))).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Store> getEffectiveListBySiteId(int siteId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Date time = new Date();
		
		Criterion rest1 = Restrictions.lt("effectiveStartDate", time);
		Criterion rest2 = Restrictions.isNull("effectiveEndDate");
		Criterion rest3 = Restrictions.gt("effectiveEndDate", time);
		
		return session.createCriteria(Store.class)
				.createAlias("site", "site")
				.add(Restrictions.eq("site.id", siteId))
				.add(Restrictions.and(rest1, Restrictions.or(rest2, rest3))).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Store> getListBySiteIdStoreName(int siteId, String storeName) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Criterion rest = Restrictions.eq("site.id", siteId);
		
		if (Util.isValid(storeName)) {
			return session.createCriteria(Store.class)
					.createAlias("site", "site")
					.add(Restrictions.like("storeName", storeName, MatchMode.ANYWHERE))
					.add(rest).list();
		} else {
			return session.createCriteria(Store.class)
					.createAlias("site", "site")
					.add(rest).list();
		}
	}

	@Override
	public int getCountBySiteId(int siteId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return ((Long)session.createCriteria(Store.class)
				.createAlias("site", "site")
				.add(Restrictions.eq("site.id", siteId))
				.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

}
