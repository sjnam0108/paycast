package kr.co.paycast.models.pay.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.Content;

@Transactional
@Component
public class ContentDaoImpl implements ContentDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
	public Content get(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Content> list = session.createCriteria(Content.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(Content content) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(content);
	}

	@Override
	public void delete(Content content) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(Content.class, content.getId()));
	}

	@Override
	public void delete(List<Content> contents) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (Content content : contents) {
            session.delete(session.load(Content.class, content.getId()));
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Content> getListByStoreIdType(int storeId, String contentType) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(Content.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("contentType", contentType))
				.add(Restrictions.eq("store.id", storeId)).list();
	}

}
