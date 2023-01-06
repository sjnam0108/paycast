package kr.co.paycast.models.pay.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.ContentFile;

@Transactional
@Component
public class ContentFileDaoImpl implements ContentFileDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
	public ContentFile get(int id) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<ContentFile> list = session.createCriteria(ContentFile.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(ContentFile contentFile) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(contentFile);
	}

	@Override
	public void delete(ContentFile contentFile) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(ContentFile.class, contentFile.getId()));
	}

	@Override
	public void delete(List<ContentFile> contentFiles) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (ContentFile contentFile : contentFiles) {
            session.delete(session.load(ContentFile.class, contentFile.getId()));
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentFile> getListByContentIdDeviceId(int contentId, int deviceId, 
			boolean isSortRequired) {
		
		Session session = sessionFactory.getCurrentSession();
		
		if (isSortRequired) {
			return session.createCriteria(ContentFile.class)
					.createAlias("content", "content")
					.createAlias("device", "device")
					.add(Restrictions.eq("content.id", contentId))
					.add(Restrictions.eq("device.id", deviceId))
					.addOrder(Order.desc("id")).list();
		} else {
			return session.createCriteria(ContentFile.class)
					.createAlias("content", "content")
					.createAlias("device", "device")
					.add(Restrictions.eq("content.id", contentId))
					.add(Restrictions.eq("device.id", deviceId)).list();
		}
	}

}
