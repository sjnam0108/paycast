package kr.co.paycast.models.pay.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	public void saveOrUpdate(UploadFile uploadFile) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(uploadFile);
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

}
