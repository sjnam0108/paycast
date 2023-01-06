package kr.co.paycast.models.fnd.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Region;

public interface RegionDao {
	// Common
	public Region get(int id);
	public List<Region> getList();
	public void saveOrUpdate(Region region);
	public void delete(Region region);
	public void delete(List<Region> regions);
	public int getCount();

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for Region specific
	public Region get(String regionCode);
	public Region getByRegionName(String regionName);
	public List<Region> getListByCountryCode(String countryCode);
}
