package kr.co.paycast.models.pay.service;

import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.Content;
import kr.co.paycast.models.pay.ContentFile;
import kr.co.paycast.models.pay.PayComparator;
import kr.co.paycast.models.pay.dao.ContentDao;
import kr.co.paycast.models.pay.dao.ContentFileDao;
import kr.co.paycast.models.self.service.SelfServiceImpl;

@Transactional
@Service("ctntService")
public class ContentServiceImpl implements ContentService {

	private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
    
    
    @Autowired
    private ContentDao ctntDao;
    
    @Autowired
    private ContentFileDao ctntFileDao;

    
	@Override
	public void flush() {
		
		sessionFactory.getCurrentSession().flush();
	}

	
	
	@Override
	public Content getContent(int id) {
		
		return ctntDao.get(id);
	}

	@Override
	public void saveOrUpdate(Content content) {
		
		ctntDao.saveOrUpdate(content);
	}

	@Override
	public void deleteContent(Content content) {
		
		ctntDao.delete(content);
	}

	@Override
	public void deleteContents(List<Content> contents) {
		
		ctntDao.delete(contents);
	}

	@Override
	public List<Content> getContentListByStoreIdType(int storeId, 
			String contentType) {
		
		return ctntDao.getListByStoreIdType(storeId, contentType);
	}

	@Override
	public Content getLastContentByStoreIdType(int storeId, String contentType) {
		
		List<Content> list = getContentListByStoreIdType(storeId, contentType);
		
		if (list == null || list.size() == 0) {
			logger.info("getLastContentByStoreIdType >> list >> [{}]", "컨텐츠 사이즈가 없어요");
			
			
			return null;
		} else {
			
			logger.info("getLastContentByStoreIdType >> list.size() >> [{}]", list.size());
			
			Collections.sort(list, PayComparator.ContentIdReverseComparator);

			for(Content content: list) {
				logger.info("getLastContentByStoreIdType >> content.getStatusCode() >> [{}]", content.getStatusCode());
				if (content.getStatusCode().equals("Y")) {
					return content;
				}
			}
		}
		
		return null;
	}

	
	
	@Override
	public ContentFile getContentFile(int id) {
		
		return ctntFileDao.get(id);
	}

	@Override
	public void saveOrUpdate(ContentFile contentFile) {
		
		ctntFileDao.saveOrUpdate(contentFile);
	}

	@Override
	public void deleteContentFile(ContentFile contentFile) {
		
		ctntFileDao.delete(contentFile);
	}

	@Override
	public void deleteContentFiles(List<ContentFile> contentFiles) {
		
		ctntFileDao.delete(contentFiles);
	}

	@Override
	public List<ContentFile> getContentFileListByContentIdDeviceId(int contentId, int deviceId,
			boolean isSortRequired) {
		
		return ctntFileDao.getListByContentIdDeviceId(contentId, deviceId, isSortRequired);
	}

}
