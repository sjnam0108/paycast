package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.AppUser;

public interface AppUserDao {
	
	// Common
	public AppUser get(int id);
	public List<AppUser> getList();
	public void saveOrUpdate(AppUser appUser);
	public void delete(AppUser appUser);
	public void delete(List<AppUser> appUsers);

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for AppUser specific
	public List<AppUser> getListBySiteIdUserId(int siteId, int userId);
	public List<AppUser> getActiveListBySiteId(int siteId);
	public List<AppUser> getListByToken(String token);
}
