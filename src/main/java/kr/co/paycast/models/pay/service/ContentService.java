package kr.co.paycast.models.pay.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.Content;
import kr.co.paycast.models.pay.ContentFile;

@Transactional
public interface ContentService {
	
	// Common
	public void flush();
	
	
	//
	// for Content Dao
	//
	// Common
	public Content getContent(int id);
	public void saveOrUpdate(Content content);
	public void deleteContent(Content content);
	public void deleteContents(List<Content> contents);

	// for Content specific
	public List<Content> getContentListByStoreIdType(int storeId, String contentType);
	public Content getLastContentByStoreIdType(int storeId, String contentType);
	
	
	//
	// for ContentFile Dao
	//
	// Common
	public ContentFile getContentFile(int id);
	public void saveOrUpdate(ContentFile contentFile);
	public void deleteContentFile(ContentFile contentFile);
	public void deleteContentFiles(List<ContentFile> contentFiles);

	// for ContentFile specific
	public List<ContentFile> getContentFileListByContentIdDeviceId(int contentId, int deviceId, 
			boolean isSortRequired);
}
