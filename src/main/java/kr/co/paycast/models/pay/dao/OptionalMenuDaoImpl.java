package kr.co.paycast.models.pay.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.OptionalMenu;

@Transactional
@Component
public class OptionalMenuDaoImpl implements OptionalMenuDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public OptionalMenu get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<OptionalMenu> list = session.createCriteria(OptionalMenu.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(OptionalMenu optionalMenu) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(optionalMenu);
	}

	@Override
	public void delete(OptionalMenu optionalMenu) {

		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(OptionalMenu.class, optionalMenu.getId()));
	}

	@Override
	public void delete(List<OptionalMenu> optionalMenus) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (OptionalMenu optionalMenu : optionalMenus) {
            session.delete(session.load(OptionalMenu.class, optionalMenu.getId()));
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OptionalMenu> getListByMenuId(int menuId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(OptionalMenu.class)
				.createAlias("menu", "menu")
				.add(Restrictions.eq("menu.id", menuId)).list();
	}
}
