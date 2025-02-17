package kr.co.paycast.models.pay.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.Ad;
import kr.co.paycast.models.pay.StoreUser;
import kr.co.paycast.models.pay.UploadFile;

@Transactional
@Component
public class UploadFileDaoImpl implements UploadFileDao {

    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public UploadFile get(Integer id) {
		
		if (id == null) {
			return null;
		}
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<UploadFile> list = session.createCriteria(UploadFile.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public Ad getAd(Integer id) {
		
		if (id == null) {
			return null;
		}
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Ad> list = session.createCriteria(Ad.class)
		.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public Ad getAdByIndex(Integer index) {
		
		if (index == null) {
			return null;
		}
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Ad> list = session.createCriteria(Ad.class)
		.add(Restrictions.eq("fileIndex", index)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public Ad getAdByFileName(String fileName) {
		
		if (fileName == null) {
			return null;
		}
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Ad> list = session.createCriteria(Ad.class)
		.add(Restrictions.eq("fileName", fileName)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	@Override
	public Ad getAdByStoreId(Integer id) {
		
		if (id == null) {
			return null;
		}
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Ad> list = session.createCriteria(Ad.class)
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	@Override
	public Ad getLastAdByStoreId(Integer id) {
		
		if (id == null) {
			return null;
		}
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Ad> list = session.createCriteria(Ad.class)
		.setFirstResult(0)
		.setMaxResults(1)
		.addOrder(Order.desc("fileIndex"))
		.createAlias("store", "store")
		.add(Restrictions.eq("store.id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Ad> getAdList(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(Ad.class)
				.addOrder(Order.asc("fileIndex"))
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", id)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UploadFile> getList(int listSize) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(UploadFile.class)
				.setFirstResult(0)
				.setMaxResults(listSize)
				.addOrder(Order.desc("whoCreationDate"))
				.add(Restrictions.like("filename", ".mp4", MatchMode.END)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Ad> getList(int listSize, int storeId, String enabled) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(Ad.class)
//				.setFirstResult(0)
//				.setMaxResults(5)
				.addOrder(Order.asc("fileIndex"))
				.createAlias("store", "store")
				.add(Restrictions.eq("store.id", storeId))
				.add(Restrictions.eq("enabled", enabled)).list();
	}

	@Override
	public void saveOrUpdate(UploadFile uploadFile) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(uploadFile);
	}
	
	@Override
	public void saveOrUpdate(Ad ad) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(ad);
	}

	@Override
	public void delete(Ad ad) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(Ad.class, ad.getId()));
	}
	
	@Override
	public void delete(UploadFile uploadFile) {

		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(UploadFile.class, uploadFile.getId()));
	}

	@Override
	public void delete(List<UploadFile> uploadFiles) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (UploadFile uploadFile : uploadFiles) {
            session.delete(session.load(UploadFile.class, uploadFile.getId()));
        }
	}
	
	@Override
	public void deleteAds(List<Ad> Ads) {
		
		Session session = sessionFactory.getCurrentSession();
		
		for (Ad ad : Ads) {
			session.delete(session.load(Ad.class, ad.getIndex()));
		}
	}

}
