package kr.co.paycast.models.pay.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.pay.SiteOpt;
import kr.co.paycast.viewmodels.pay.OptionItem;

@Transactional
public interface OptService {
	// Common
	public void flush();
	
	//
	// for SiteOpt Dao
	//
	// Common
	public SiteOpt getSiteOpt(int id);
	public List<SiteOpt> getSiteOptList();
	public void saveOrUpdate(SiteOpt siteOpt);
	public void deleteSiteOpt(SiteOpt siteOpt);
	public void deleteSiteOpts(List<SiteOpt> siteOpts);
	public int getSiteOptCount();

	// for SiteOpt specific
	public SiteOpt getSiteOptByOptNameSiteId(String optName, int siteId);
	public List<SiteOpt> getSiteOptListBySiteId(int siteId);
	
	// for General purposes
	public ArrayList<OptionItem> getGlobalSiteOptions(Locale locale);
	public ArrayList<OptionItem> getCurrentSiteOptions(int siteId, Locale locale);
	public String getSiteOption(int siteId, String name);
	public String getSiteOption(int siteId, String name, Locale locale);
	public String getSiteOption(HttpSession session, String name);
	public String getSiteOption(HttpSession session, String name, Locale locale);
	
}
