package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.pay.Store;

public interface StoreDao {

	public Store getStoreByStoreKey(String paramString);
	
	// Common
	public List<Store> get();
	public Store get(int id);
	public void saveOrUpdate(Store store);
	public void delete(Store store);
	public void delete(List<Store> stores);

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);
	public DataSourceResult getList(DataSourceRequest request, boolean isEffectiveMode);

	// for Store specific
	public Store get(Site site, String shortName);
	public Store getBySiteIdStoreName(int siteId, String storeName);
	public Store getByStoreKey(String storeKey);
	public List<Store> getListBySiteId(int siteId);
	public List<Store> getEffectiveList();
	public List<Store> getEffectiveListBySiteId(int siteId);
	public List<Store> getListBySiteIdStoreName(int siteId, String storeName);
	public int getCountBySiteId(int siteId);

}
