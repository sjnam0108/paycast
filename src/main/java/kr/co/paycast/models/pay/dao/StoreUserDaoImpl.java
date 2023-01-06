package kr.co.paycast.models.pay.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.SiteUser;
import kr.co.paycast.models.fnd.User;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreUser;

@Transactional
@Component
public class StoreUserDaoImpl implements StoreUserDao {

    @Autowired
    private SessionFactory sessionFactory;
    
	@Override
	public StoreUser get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StoreUser> list = session.createCriteria(StoreUser.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(StoreUser storeUser) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(storeUser);
	}

	@Override
	public void delete(StoreUser storeUser) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(StoreUser.class, storeUser.getId()));
	}

	@Override
	public void delete(List<StoreUser> storeUsers) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (StoreUser storeUser : storeUsers) {
            session.delete(session.load(StoreUser.class, storeUser.getId()));
        }
	}

	@Override
	public DataSourceResult getList(DataSourceRequest request) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("store", Store.class);
		map.put("user", User.class);
		
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), StoreUser.class, map);
	}

	@Override
	public boolean isRegistered(int storeId, int userId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Criterion rest1 = Restrictions.eq("user.id", userId);
		Criterion rest2 = Restrictions.eq("store.id", storeId);
		
		@SuppressWarnings("unchecked")
		List<SiteUser> list = session.createCriteria(StoreUser.class)
				.createAlias("user", "user")
				.createAlias("store", "store")
				.add(Restrictions.and(rest1, rest2)).list();
		
		return !list.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StoreUser> getListByStoreId(int storeId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreUser.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StoreUser> getListByUserId(int userId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StoreUser.class)
				.createAlias("user", "user")
				.add(Restrictions.eq("user.id", userId)).list();
	}

}
