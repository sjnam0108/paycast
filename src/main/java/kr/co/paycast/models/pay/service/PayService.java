package kr.co.paycast.models.pay.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.AppUser;
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
	public void saveOrUpdate(UploadFile uploadFile);
	public void deleteUploadFile(UploadFile uploadFile);
	public void deleteUploadFiles(List<UploadFile> uploadFiles);
	
	
	//
	// for Common
	//
	public void deactivateAppUserBySystem(String token);
}
