package kr.co.paycast.models.fnd.dao;

import java.util.Date;
import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Site;

public interface SiteDao {
	// Common
	public Site get(int id);
	public List<Site> getList();
	public void saveOrUpdate(Site site);
	public void delete(Site site);
	public void delete(List<Site> sites);
	public int getCount();

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for Site specific
	public Site get(String shortName);
	public List<Site> getEffectiveList();
	public List<Site> getEffectiveList(Date time);
}
