package kr.co.paycast.models.pay.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.Menu;
import kr.co.paycast.models.pay.OptionalMenuList;
import kr.co.paycast.models.pay.StorePolicy;
//import kr.co.signcast.utils.Util;
import kr.co.paycast.models.pay.service.CouponPointService;

@Transactional
@Component
public class StoreMenuDaoImpl implements StoreMenuDao {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private CouponPointService couponService;

	@Override
	public Menu get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Menu> list = session.createCriteria(Menu.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public String getTime(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("updateYN").as("updateYN"));
		Criteria crit = session.createCriteria(Menu.class);
		crit.setProjection(projectionList);
		crit.createAlias("store", "store");
		crit.add(Restrictions.eq("store.id", id));
		crit.addOrder(Order.desc("updateYN"));
		crit.setMaxResults(1);
		Date menu = (Date) crit.uniqueResult();
		String menuString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(menu);
		return menuString;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Menu> getByMenuList(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		
		return session.createCriteria(Menu.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", id)).list();
		
//		 (list.isEmpty() ? null : list.get(0));
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Menu> getByMenuCode(int id, String menuCode) {
		
		Session session = sessionFactory.getCurrentSession();
		
		
		return session.createCriteria(Menu.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", id))
				.add(Restrictions.eq("code", menuCode)).list();
		
//		 (list.isEmpty() ? null : list.get(0));
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OptionalMenuList> getByOptionCode(int id, String menuCode) {
		
		Session session = sessionFactory.getCurrentSession();
		
		
		return session.createCriteria(OptionalMenuList.class)
				.createAlias("optMenu", "optMenu")
				.add(Restrictions.eq("optMenu.id", id))
				.add(Restrictions.eq("code", menuCode)).list();
		
//		 (list.isEmpty() ? null : list.get(0));
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
	
	@Override
	public Menu getName(String name,  int groupId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Menu> list = session.createCriteria(Menu.class)
		.add(Restrictions.eq("name", name))
		.add(Restrictions.eq("groupId", groupId)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdateCode(Menu menu) {
		Session session = sessionFactory.getCurrentSession();
		
		List<Menu> children = session.createCriteria(Menu.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", menu.getStore().getId()))
				.add(Restrictions.eq("code", menu.getCode())).list();
		if(children.isEmpty()) {
			session.saveOrUpdate(menu);
		} else {
			for (Menu item : children) {
				if(item.getCode().equals(menu.getCode()) && item.getId()==(menu.getId())) {
					item.setCode(menu.getCode());
					item.setPrice(menu.getPrice());
					item.setName(menu.getName());
					item.setFlagType(menu.getFlagType());
					item.setPublished(menu.getPublished());
					item.setIntro(menu.getIntro());
					item.setSoldOut(menu.isSoldOut());
					item.setEvent(menu.getEvent());  
		    		
					session.saveOrUpdate(item);
				}else if(item.getCode().equals(menu.getCode())) {
					session.close();
				}
			} 
		}
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
