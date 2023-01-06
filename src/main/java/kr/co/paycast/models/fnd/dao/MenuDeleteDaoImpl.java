package kr.co.paycast.models.fnd.dao;

import java.util.List;

import kr.co.paycast.models.pay.MenuDelete;
import kr.co.paycast.models.pay.MenuGroupDelete;
import kr.co.paycast.models.pay.OptionalMenuDelete;
import kr.co.paycast.models.pay.OptionalMenuListDelete;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class MenuDeleteDaoImpl implements MenuDeleteDao {
    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveOrUpdate(MenuDelete menuDelete) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(menuDelete);
	}
    
	@SuppressWarnings("unchecked")
	@Override
	public List<MenuDelete> getMenuDeleteListByStoreIdGroupId(int storeId, Integer groupId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(MenuDelete.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId))
				.add(Restrictions.eq("groupId", groupId)).list();
	}

	
	@Override
	public void saveOrUpdate(MenuGroupDelete menuGroupDelete) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(menuGroupDelete);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MenuGroupDelete> getListByStoreId(int storeId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(MenuGroupDelete.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId)).list();
	}

	@Override
	public void saveOrUpdate(OptionalMenuDelete optionalMenuDelete) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(optionalMenuDelete);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OptionalMenuDelete> getOptionalMenuListDeleteByMenuDeleteId(int menuDeleteId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(OptionalMenuDelete.class)
				.createAlias("menuDelete", "menuDelete")
				.add(Restrictions.eq("menuDelete.id", menuDeleteId)).list();
	}

	@Override
	public void saveOrUpdate(OptionalMenuListDelete optionalMenuListDelete) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(optionalMenuListDelete);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OptionalMenuListDelete> getOptionalMenuListDeleteByOptDeleteMenuId(int optMenuDeleteId) {
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(OptionalMenuListDelete.class)
				.createAlias("optMenuDelete", "optMenuDelete")
				.add(Restrictions.eq("optMenuDelete.id", optMenuDeleteId)).list();
	}
}
