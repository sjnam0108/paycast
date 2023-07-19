package kr.co.paycast.models.pay.dao;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.MenuGroup;

@Transactional
@Component
public class MenuGroupDaoImpl implements MenuGroupDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public MenuGroup get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<MenuGroup> list = session.createCriteria(MenuGroup.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(MenuGroup menuGroup) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(menuGroup);
	}

	@Override
	public void delete(MenuGroup menuGroup) {

		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(MenuGroup.class, menuGroup.getId()));
	}

	@Override
	public void delete(List<MenuGroup> menuGroups) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (MenuGroup menuGroup : menuGroups) {
            session.delete(session.load(MenuGroup.class, menuGroup.getId()));
        }
	}

	@Override
	public void saveAndReorder(MenuGroup menuGroup, HttpSession httpSession) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(menuGroup);
		session.flush();
		
		reorder(menuGroup, session, httpSession);
	}

	@SuppressWarnings("unchecked")
	private void reorder(MenuGroup menuGroup, org.hibernate.Session session, HttpSession httpSession) {
		
		List<MenuGroup> children = session.createCriteria(MenuGroup.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", menuGroup.getStore().getId())).list();
		
		Collections.sort(children, MenuGroup.SiblingSeqComparator);
		
		int cnt = 1;
		for (MenuGroup item : children) {
			item.setSiblingSeq((cnt++) * 10);
			item.touchWho(httpSession);
			
			session.saveOrUpdate(item);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MenuGroup> getListByStoreId(int storeId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(MenuGroup.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MenuGroup> getListByStoreIdByPublished(int storeId,String published) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(MenuGroup.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId))
				.add(Restrictions.eq("published", published)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void reorder(MenuGroup group, int index, HttpSession httpSession) {
		
		Session session = sessionFactory.getCurrentSession();
		
		List<MenuGroup> children = session.createCriteria(MenuGroup.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", group.getStore().getId())).list();
		
		Collections.sort(children, MenuGroup.SiblingSeqComparator);
		
		int cnt = 1;
		for (MenuGroup item : children) {
			if (group.getId() == item.getId()) {
				item.setSiblingSeq(index * 10 + 5);
			} else {
				item.setSiblingSeq((cnt++) * 10);
			}
			
			item.touchWho(httpSession);
			
			session.saveOrUpdate(item);
		}
	}
	
}
