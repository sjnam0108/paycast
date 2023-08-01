package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.Ad;
import kr.co.paycast.models.pay.StoreUser;
import kr.co.paycast.models.pay.UploadFile;

public interface UploadFileDao {
	
	// Common
	public UploadFile get(Integer id);
	public Ad getAd(Integer id);
	public Ad getAdByIndex(Integer index);
	public Ad getAdByFileName(String fileName);
	public Ad getAdByStoreId(Integer id);
	public Ad getLastAdByStoreId(Integer id);
	public List<Ad> getAdList(int id);
	public List<UploadFile> getList(int listSize);
	public List<Ad> getList(int listSize, int storeId, String enabled);
	public void saveOrUpdate(UploadFile uploadFile);
	public void saveOrUpdate(Ad ad);
	public void deleteAds(List<Ad> Ads);
	public void delete(Ad ad);
	public void delete(UploadFile uploadFile);
	public void delete(List<UploadFile> uploadFiles);
}
