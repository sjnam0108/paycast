package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.StoreUser;

public interface StoreUserDao {

	// Common
	public StoreUser get(int id);
	public void saveOrUpdate(StoreUser storeUser);
	public void delete(StoreUser storeUser);
	public void delete(List<StoreUser> storeUsers);

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for StoreUser specific
	public boolean isRegistered(int storeId, int userId);
	public List<StoreUser> getListByStoreId(int storeId);
	public List<StoreUser> getListByUserId(int userId);
}
