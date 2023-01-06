package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.ContentFile;

public interface ContentFileDao {

	// Common
	public ContentFile get(int id);
	public void saveOrUpdate(ContentFile contentFile);
	public void delete(ContentFile contentFile);
	public void delete(List<ContentFile> contentFiles);

	// for ContentFile specific
	public List<ContentFile> getListByContentIdDeviceId(int contentId, int deviceId, 
			boolean isSortRequired);
}
