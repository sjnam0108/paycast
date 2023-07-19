package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.Ad;
import kr.co.paycast.models.pay.StoreUser;
import kr.co.paycast.models.pay.UploadFile;

public interface UploadFileDao {
	
	// Common
	public UploadFile get(Integer id);
	public List<UploadFile> getList(int listSize);
	public List<Ad> getList(int listSize, int storeId, String enabled);
	public void saveOrUpdate(UploadFile uploadFile);
	public void saveOrUpdate(Ad ad);
	public void delete(UploadFile uploadFile);
	public void delete(List<UploadFile> uploadFiles);
}
