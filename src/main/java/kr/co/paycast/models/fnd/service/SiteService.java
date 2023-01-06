package kr.co.paycast.models.fnd.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Region;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.SiteSite;
import kr.co.paycast.models.fnd.SiteUser;

@Transactional
public interface SiteService {
	// Common
	public void flush();

	//
	// for Site Dao
	//
	// Common
	public Site getSite(int id);
	public List<Site> getSiteList();
	public void saveOrUpdate(Site site);
	public void deleteSite(Site site);
	public void deleteSites(List<Site> sites);
	public int getSiteCount();
	
	// for Kendo Grid Remote Read
	public DataSourceResult getSiteList(DataSourceRequest request);

	// for Site specific
	public Site getSite(String shortName);
	public List<Site> getEffectiveSiteList();
	public List<Site> getEffectiveSiteList(Date time);
	public boolean isEffectiveSite(Site site);

	//
	// for SiteUser Dao
	//
	// Common
	public SiteUser getSiteUser(int id);
	public List<SiteUser> getSiteUserList();
	public void saveOrUpdate(SiteUser siteUser);
	public void deleteSiteUser(SiteUser siteUser);
	public void deleteSiteUsers(List<SiteUser> siteUsers);
	public int getSiteUserCount();
	
	// for Kendo Grid Remote Read
	public DataSourceResult getSiteUserList(DataSourceRequest request);

	// for SiteUser specific
	public boolean isRegisteredSiteUser(int siteId, int userId);
	
	//
	// for SiteSite Dao
	//
	// Common
	public SiteSite getSiteSite(int id);
	public List<SiteSite> getSiteSiteList();
	public void saveOrUpdate(SiteSite siteSite);
	public void deleteSiteSite(SiteSite siteSite);
	public void deleteSiteSites(List<SiteSite> siteSites);
	public int getSiteSiteCount();
	
	// for Kendo Grid Remote Read
	public DataSourceResult getSiteSiteList(DataSourceRequest request);

	// for SiteSite specific
	public boolean isRegisteredSiteSite(int parentSiteId, int childSiteId);
	public List<SiteSite> getSiteSiteListByParentSiteId(int siteId);
	
	//
	// for Region Dao
	//
	// Common
	public Region getRegion(int id);
	public List<Region> getRegionList();
	public void saveOrUpdate(Region region);
	public void deleteRegion(Region region);
	public void deleteRegions(List<Region> regions);
	public int getRegionCount();

	// for Kendo Grid Remote Read
	public DataSourceResult getRegionList(DataSourceRequest request);

	// for Region specific
	public Region getRegion(String regionCode);
	public Region getRegionByRegionName(String regionName);
	public List<Region> getRegionListByCountryCode(String countryCode);
	
}
