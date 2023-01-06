package kr.co.paycast.models.pay.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.User;
import kr.co.paycast.models.pay.AppUser;

@Transactional
@Component
public class AppUserDaoImpl implements AppUserDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public AppUser get(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<AppUser> list = session.createCriteria(AppUser.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppUser> getList() {
		return sessionFactory.getCurrentSession().createCriteria(AppUser.class).list();
	}

	@Override
	public void saveOrUpdate(AppUser appUser) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(appUser);
	}

	@Override
	public void delete(AppUser appUser) {
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(AppUser.class, appUser.getId()));
	}

	@Override
	public void delete(List<AppUser> appUsers) {
		Session session = sessionFactory.getCurrentSession();
		
        for (AppUser appUser : appUsers) {
            session.delete(session.load(AppUser.class, appUser.getId()));
        }
	}

	@Override
	public DataSourceResult getList(DataSourceRequest request) {
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("site", Site.class);
		map.put("user", User.class);
		
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), AppUser.class, map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppUser> getListBySiteIdUserId(int siteId, int userId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(AppUser.class)
				.createAlias("site", "site")
				.createAlias("user", "user")
				.add(Restrictions.eq("site.id", siteId))
				.add(Restrictions.eq("user.id", userId)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppUser> getActiveListBySiteId(int siteId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(AppUser.class)
				.createAlias("site", "site")
				.add(Restrictions.eq("site.id", siteId))
				.add(Restrictions.eq("status", "A")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppUser> getListByToken(String token) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(AppUser.class)
				.add(Restrictions.eq("fcmToken", token)).list();
	}
}
