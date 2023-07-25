package kr.co.paycast.models.pay.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.Ad;
import kr.co.paycast.models.pay.AppUser;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreUser;
import kr.co.paycast.models.pay.UploadFile;

@Transactional
public interface PayService {
	
	// Common
	public void flush();
	
	//
	// for AppUser Dao
	//
	// Common
	public AppUser getAppUser(int id);
	public List<AppUser> getAppUserList();
	public void saveOrUpdate(AppUser appUser);
	public void deleteAppUser(AppUser appUser);
	public void deleteAppUsers(List<AppUser> appUsers);
	public void deleteAd(Ad ad);
	public void deleteAds(List<Ad> ad);

	// for Kendo Grid Remote Read
	public DataSourceResult getAppUserList(DataSourceRequest request);

	// for AppUser specific
	public List<AppUser> getAppUserListBySiteIdUserId(int siteId, int userId);
	public List<AppUser> getActiveAppUserListBySiteId(int siteId);
	public List<AppUser> getAppUserListByToken(String token);
	
	
	//
	// for UploadFile Dao
	//
	// Common
	public UploadFile getUploadFile(Integer id);
	public Ad getAd(Integer id);
	public Ad getAdByIndex(int index);
	public Ad getAdByFileName(String fileName);
	public Ad getAdByStoreId(Integer id);
	public List<Ad> getAdList(int id);
	public List<UploadFile> getUploadFilebySize(int listSize);
	public List<Ad> getAdbySize(int listSize,int storeId, String enabled);
	public void saveOrUpdate(UploadFile uploadFile);
	public void saveOrUpdate(Ad ad);
	public void deleteUploadFile(UploadFile uploadFile);
	public void deleteUploadFiles(List<UploadFile> uploadFiles);
	
	
	//
	// for Common
	//
	public void deactivateAppUserBySystem(String token);
}
