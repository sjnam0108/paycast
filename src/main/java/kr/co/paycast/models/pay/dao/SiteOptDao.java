package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.SiteOpt;


public interface SiteOptDao {
	// Common
	public SiteOpt get(int id);
	public List<SiteOpt> getList();
	public void saveOrUpdate(SiteOpt siteOpt);
	public void delete(SiteOpt siteOpt);
	public void delete(List<SiteOpt> siteOpts);
	public int getCount();

	// for SiteOpt specific
	public SiteOpt getByOptNameSiteId(String optName, int siteId);
	public List<SiteOpt> getListBySiteId(int siteId);
}
