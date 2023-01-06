package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.Content;

public interface ContentDao {
	
	// Common
	public Content get(int id);
	public void saveOrUpdate(Content content);
	public void delete(Content content);
	public void delete(List<Content> contents);

	// for Content specific
	public List<Content> getListByStoreIdType(int storeId, String contentType);
}
