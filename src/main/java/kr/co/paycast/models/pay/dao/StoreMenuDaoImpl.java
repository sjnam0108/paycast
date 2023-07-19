package kr.co.paycast.models.pay.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.Menu;

@Transactional
@Component
public class StoreMenuDaoImpl implements StoreMenuDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public Menu get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Menu> list = session.createCriteria(Menu.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(Menu menu) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(menu);
	}

	@Override
	public void delete(Menu menu) {

		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(Menu.class, menu.getId()));
	}

	@Override
	public void delete(List<Menu> menus) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (Menu menu : menus) {
            session.delete(session.load(Menu.class, menu.getId()));
        }
	}

	@Override
	public void saveAndReorder(Menu menu, HttpSession httpSession) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(menu);
		session.flush();
		
		reorder(menu, session, httpSession);
	}

	@SuppressWarnings("unchecked")
	private void reorder(Menu menu, org.hibernate.Session session, HttpSession httpSession) {
		
		List<Menu> children = new ArrayList<Menu>();
		if (menu.getGroupId() == null) {
			children = session.createCriteria(Menu.class)
					.createAlias("store", "store")
					.add(Restrictions.eq("store.id", menu.getStore().getId()))
					.add(Restrictions.isNull("groupId")).list();
		} else {
			children = session.createCriteria(Menu.class)
					.add(Restrictions.eq("groupId", menu.getGroupId())).list();
		}
		
		Collections.sort(children, Menu.SiblingSeqComparator);
		
		int cnt = 1;
		for (Menu item : children) {
			item.setSiblingSeq((cnt++) * 10);
			item.touchWho(httpSession);
			
			session.saveOrUpdate(item);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Menu> getListByStoreIdGroupId(int storeId, Integer groupId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		if (groupId == null) {
			return session.createCriteria(Menu.class)
					.createAlias("store", "store")
					.add(Restrictions.eq("store.id", storeId))
					.add(Restrictions.isNull("groupId")).list();
		} else {
			return session.createCriteria(Menu.class)
					.add(Restrictions.eq("groupId", groupId)).list();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Menu> getListByStoreIdGroupIdPublished(int storeId, Integer groupId,String published) {
		
		Session session = sessionFactory.getCurrentSession();
		
		if (groupId == null) {
			return session.createCriteria(Menu.class)
					.createAlias("store", "store")
					.add(Restrictions.eq("store.id", storeId))
					.add(Restrictions.eq("published", published))
					.add(Restrictions.isNull("groupId")).list();
		} else {
			return session.createCriteria(Menu.class)
					.add(Restrictions.eq("groupId", groupId))
					.add(Restrictions.eq("published", published)).list();
		}
	}

	@Override
	public void reorder(int storeId, Integer groupId, HttpSession httpSession) {
		
		Session session = sessionFactory.getCurrentSession();
		
		List<Menu> children = getListByStoreIdGroupId(storeId, groupId);
		
		Collections.sort(children, Menu.SiblingSeqComparator);
		
		int cnt = 1;
		for (Menu item : children) {
			item.setSiblingSeq((cnt++) * 10);
			item.touchWho(httpSession);
			
			session.saveOrUpdate(item);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reorder(Menu menu, int index, HttpSession httpSession) {
		
		Session session = sessionFactory.getCurrentSession();
		
		List<Menu> children = new ArrayList<Menu>();
		
		if (menu.getGroupId() == null) {
			children = session.createCriteria(Menu.class)
					.createAlias("store", "store")
					.add(Restrictions.eq("store.id", menu.getStore().getId()))
					.add(Restrictions.isNull("groupId")).list();
		} else {
			children = session.createCriteria(Menu.class)
					.add(Restrictions.eq("groupId", menu.getGroupId())).list();
		}
		
		Collections.sort(children, Menu.SiblingSeqComparator);
		
		int cnt = 1;
		for (Menu item : children) {
			if (menu.getId() == item.getId()) {
				item.setSiblingSeq(index * 10 + 5);
			} else {
				item.setSiblingSeq((cnt++) * 10);
			}
			
			item.touchWho(httpSession);
			
			session.saveOrUpdate(item);
		}
	}
}
