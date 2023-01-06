package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.SiteRegion;

public interface SiteRegionDao {
	// Common
	public SiteRegion get(int id);
	public List<SiteRegion> getList();
	public void saveOrUpdate(SiteRegion siteRegion);
	public void delete(SiteRegion siteRegion);
	public void delete(List<SiteRegion> siteRegions);
	public int getCount();

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for SiteRegion specific
	public boolean isRegistered(int siteId, int regionId);
	public void saveOrUpdate(List<SiteRegion> siteRegions);
	public List<SiteRegion> getListBySiteId(int siteId);
	public List<SiteRegion> getListByRegionId(int regionId);
	public List<SiteRegion> getDefaultValueListBySiteId(int siteId);
	public SiteRegion get(int siteId, String regionCode);
}
