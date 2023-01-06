package kr.co.paycast.models.pay.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.pay.SiteOpt;
import kr.co.paycast.models.pay.dao.SiteOptDao;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.OptionItem;
import kr.co.paycast.viewmodels.pay.OptionItem.OptType;

@Transactional
@Service("optService")
public class OptServiceImpl implements OptService {
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private SiteOptDao siteOptDao;
    
	@Autowired
	private MessageManager msgMgr;
    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	@Override
	public SiteOpt getSiteOpt(int id) {
		return siteOptDao.get(id);
	}

	@Override
	public List<SiteOpt> getSiteOptList() {
		return siteOptDao.getList();
	}

	@Override
	public void saveOrUpdate(SiteOpt siteOpt) {
		siteOptDao.saveOrUpdate(siteOpt);
	}

	@Override
	public void deleteSiteOpt(SiteOpt siteOpt) {
		siteOptDao.delete(siteOpt);
	}

	@Override
	public void deleteSiteOpts(List<SiteOpt> siteOpts) {
		siteOptDao.delete(siteOpts);
	}

	@Override
	public int getSiteOptCount() {
		return siteOptDao.getCount();
	}

	@Override
	public SiteOpt getSiteOptByOptNameSiteId(String optName, int siteId) {
		return siteOptDao.getByOptNameSiteId(optName, siteId);
	}

	@Override
	public List<SiteOpt> getSiteOptListBySiteId(int siteId) {
		return siteOptDao.getListBySiteId(siteId);
	}
	
	private OptionItem getGlobalSiteOption(String optName, Locale locale) {
		if (Util.isNotValid(optName)) {
			return null;
		}
		
		String value = msgMgr.message(optName, locale);
		value = Util.isValid(value) ? value : "";
		
		if (optName.equals(value)) {
			value = "";
		}
		
		return new OptionItem(optName, value, OptType.GlobalSite);
	}

	private ArrayList<OptionItem> registerOption(List<OptionItem> list, OptionItem optItem) {
		return registerOption(list, optItem, true);
	}

	private ArrayList<OptionItem> registerOption(List<OptionItem> list, 
			OptionItem optItem, boolean isAddedMode) {
		ArrayList<OptionItem> ret = new ArrayList<OptionItem>();
		
		if (list != null && list.size() > 0) {
			ret.addAll(list);
		}
		
		if (optItem != null) {
			boolean containsOptName = false;
			for(OptionItem item : list) {
				if (optItem.getName().equals(item.getName())) {
					item.setValue(optItem.getValue());
					item.setOptType(optItem.getOptType());
					
					containsOptName = true;
					
					break;
				}
			}
			
			if (isAddedMode && !containsOptName) {
				ret.add(optItem);
			}
		}
		
		return ret;
	}
	
	@Override
	public ArrayList<OptionItem> getGlobalSiteOptions(Locale locale) {
		ArrayList<OptionItem> ret = new ArrayList<OptionItem>();
		
		String[] optNames = new String[] { 
				"auto.siteUser",
				"quicklink.max.menu",
				"logo.title",
			};
		
		for(String optName : optNames) {
			ret = registerOption(ret, getGlobalSiteOption(optName, locale));
		}
		
		return ret;
	}
	
	@Override
	public ArrayList<OptionItem> getCurrentSiteOptions(int siteId, Locale locale) {
		ArrayList<OptionItem> items = getGlobalSiteOptions(locale);
		
		List<SiteOpt> siteOptions = getSiteOptListBySiteId(siteId);
		for(SiteOpt item : siteOptions) {
			items = registerOption(items, 
					new OptionItem(item.getOptName(), item.getOptValue(), OptType.Site), false);
		}
		
		return items;
	}
	
	@Override
	public String getSiteOption(int siteId, String name, Locale locale) {
		List<OptionItem> options = getCurrentSiteOptions(siteId, locale);
		for(OptionItem optItem : options) {
			if (optItem.getName().equals(name)) {
				return optItem.getValue();
			}
		}
		
		return "";
	}
	
	@Override
	public String getSiteOption(int siteId, String name) {
		return getSiteOption(siteId, name, null);
	}
	
	@Override
	public String getSiteOption(HttpSession session, String name) {
		return getSiteOption(Util.getSessionSiteId(session), name, null);
	}
	
	@Override
	public String getSiteOption(HttpSession session, String name, Locale locale) {
		return getSiteOption(Util.getSessionSiteId(session), name, locale);
	}
}
