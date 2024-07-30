package kr.co.paycast.models.pay.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.User;
import kr.co.paycast.models.pay.SiteRegion;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreEtc;
import kr.co.paycast.models.pay.StoreOpt;
import kr.co.paycast.models.pay.StoreUser;
import kr.co.paycast.viewmodels.pay.StoreInfoItem;
import kr.co.paycast.viewmodels.pay.StoreItem;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StoreService {
	
	// Common
	public void flush();
	
	//
	// for Store Dao
	//
	// Common
	public List<Store> get();
	public Store getStore(int id);
	public Store getStoreByStoreName(String name);
	public Store getStoreByCode(int code);
	public Store getAllStore();
	public Store getStoreKey(String storeKey);
	public void saveOrUpdate(Store store);
	public void deleteStore(Store store);
	public void deleteStores(List<Store> stores);
	

	// for Kendo Grid Remote Read
	public DataSourceResult getStoreList(DataSourceRequest request);
	public DataSourceResult getStoreList(DataSourceRequest request, boolean isEffectiveMode);

	// for Store specific
	public Store getStore(Site site, String shortName);
	public Store getStoreBySiteIdStoreName(int siteId, String storeName);
	public Store getStoreByStoreKey(String storeKey);
	public List<Store> getStoreListBySiteId(int siteId);
	public List<Store> getEffectiveStoreList();
	public List<Store> getEffectiveStoreListBySiteId(int siteId);
	public List<Store> getStoreListBySiteIdStoreName(int siteId, String storeName);
	public int getStoreCountBySiteId(int siteId);

	
	//
	// for StoreEtc Dao
	//
	// Common
	public void saveOrUpdate(StoreEtc storeEtc);

	
	//
	// for StoreOpt Dao
	//
	// Common
	public void saveOrUpdate(StoreOpt storeOpt);

	
	//
	// for SiteRegion Dao
	//
	// Common
	public SiteRegion getSiteRegion(int id);
	public List<SiteRegion> getSiteRegionList();
	public void saveOrUpdate(SiteRegion siteRegion);
	public void deleteSiteRegion(SiteRegion siteRegion);
	public void deleteSiteRegions(List<SiteRegion> siteRegions);
	public int getSiteRegionCount();

	// for Kendo Grid Remote Read
	public DataSourceResult getSiteRegionList(DataSourceRequest request);

	// for SiteRegion specific
	public boolean isRegisteredSiteRegion(int siteId, int regionId);
	public List<SiteRegion> getSiteRegionListBySiteId(int siteId);
	public List<SiteRegion> getSiteRegionListByRegionId(int regionId);
	public List<SiteRegion> getSiteRegionDefaultValueListBySiteId(int siteId);
	public SiteRegion getSiteRegion(int siteId, String regionCode);
	
	
	//
	// for StoreUser Dao
	//
	// Common
	public StoreUser getStoreUser(int id);
	public void saveOrUpdate(StoreUser storeUser);
	public void deleteStoreUser(StoreUser storeUser);
	public void deleteStoreUsers(List<StoreUser> storeUsers);

	// for Kendo Grid Remote Read
	public DataSourceResult getStoreUserList(DataSourceRequest request);

	// for StoreUser specific
	public boolean isRegisteredStoreUser(int storeId, int userId);
	public List<StoreUser> getStoreUserListByStoreId(int storeId);
	public List<StoreUser> getStoreUserListByUserId(int userId);

	
	//
	// for Common
	//
	public List<StoreItem> getStoreSwitcherListBySiteIdUserId(int siteId, int userId);
	public List<User> getUserListByStoreId(int storeId);
	public List<Store> getStoreListBySiteIdUserId(int siteId, int userId);

	
	//
	// for StoreInfo Service
	//
	// Common
	public StoreInfoItem storeInfoUpdate(Map<String, Object> model, Locale locale, HttpSession session, String admYn);
	
}
