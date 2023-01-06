package kr.co.paycast.controllers.pay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.models.pay.SiteOpt;
import kr.co.paycast.models.pay.service.OptService;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.OptionItem;

/**
 * 현재 사이트 설정 컨트롤러
 */
@Controller("pay-currsite-setting-controller")
@RequestMapping(value="/pay/currsitesetting")
public class CurrSiteSettingController {
	
	private static final Logger logger = LoggerFactory.getLogger(CurrSiteSettingController.class);

    @Autowired 
    private SiteService siteService;

    @Autowired 
    private OptService optService;

    @Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;
	
	/**
	 * 현재 사이트 설정 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "currsitesetting.title"),
    				new Message("tab_general", "currsitesetting.general"),
    				new Message("tab_location", "currsitesetting.location"),
					new Message("title_autoSiteUser", "currsitesetting.autositeuser.title"),
					new Message("desc_autoSiteUser", "currsitesetting.autositeuser.desc"),
					new Message("title_siteLogo", "currsitesetting.siteLogo.title"),
					new Message("desc_siteLogo", "currsitesetting.siteLogo.desc"),
					new Message("label_allSite", "currsitesetting.siteLogo.allSite"),
					new Message("label_currSite", "currsitesetting.siteLogo.currSite"),
					new Message("label_currSetting", "currsitesetting.siteLogo.currSetting"),
					new Message("label_serviceURL", "currsitesetting.siteLogo.serviceURL"),
					new Message("label_logoFile", "currsitesetting.siteLogo.logoFile"),
					new Message("label_login", "currsitesetting.siteLogo.login"),
					new Message("label_slide", "currsitesetting.siteLogo.slide"),
					new Message("label_top", "currsitesetting.siteLogo.top"),
					new Message("label_logoText", "currsitesetting.siteLogo.text"),
					new Message("tip_changeable", "currsitesetting.siteLogo.changeable"),
					new Message("title_maxQuickLinkMenu", "currsitesetting.maxQuickLinkMenu.title"),
					new Message("desc_maxQuickLinkMenu", "currsitesetting.maxQuickLinkMenu.desc"),
					
					new Message("msg_updateComplete", "currsitesetting.msg.updateComplete"),
    			});
    	
    	// 사이트 로고 관련 모델값 설정
    	String logoDomainName = Util.getFileProperty("logo.domain.name");
    	String currentSiteUrl = "-";
    	String siteShortName = "[siteID]";
    	
    	Site site = siteService.getSite(Util.getSessionSiteId(session));
    	if (site != null) {
    		siteShortName = site.getShortName();

    		if (Util.isValid(logoDomainName)) {
    			currentSiteUrl = site.getShortName() + "." + logoDomainName;
        	}
    	}
    	
    	model.addAttribute("globalCustLogoDisplayed", Util.getBooleanFileProperty("custom.logo.displayed", false));
    	model.addAttribute("currCustLogoDisplayed", Util.isValid(logoDomainName));
    	
    	model.addAttribute("currSiteLogoServiceURL", currentSiteUrl);
    	model.addAttribute("siteShortName", siteShortName);
    	
    	model.addAttribute("logoFileLogin", Util.getLogoPathFile("login", request.getServerName()));
    	model.addAttribute("logoFileTop", Util.getLogoPathFile("top", request.getServerName()));
    	//-
    	
        return "pay/currsitesetting";
    }

	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody List<OptionItem> read(Locale locale, HttpSession session) {
		return optService.getCurrentSiteOptions(Util.getSessionSiteId(session), locale);
    }
    
	/**
	 * 개별 옵션 항목의 값 변경
	 */
	private List<OptionItem> updateSiteOption(List<OptionItem> items, String name, String value) {
		if (items == null || items.size() == 0) {
			return items;
		}
		
		for(OptionItem item : items) {
			if (item.getName().equals(name)) {
				item.setValue(Util.isNotValid(value) ? "" : value);
			}
		}
		
		return items;
	}
	
	/**
	 * 변경 액션
	 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
		Site site = siteService.getSite(Util.getSessionSiteId(session));
		if (site == null) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
		}
		
		String routineSchedPrefix = (String)model.get("routineSchedPrefix");
		routineSchedPrefix = Util.isNotValid(routineSchedPrefix) ? "" : routineSchedPrefix;
		
		List<OptionItem> siteOptions = optService.getCurrentSiteOptions(site.getId(), locale);

		// 전달된 옵션 정리(추가 혹은 변경 대상)
		
		//
		// [Key]: [paramName]
		//
		// auto.siteUser: autoSiteUser
		// quicklink.max.menu: maxMenuCount
		// logo.title: logoTitle
    	siteOptions = updateSiteOption(siteOptions, "auto.siteUser", (String)model.get("autoSiteUser"));
    	siteOptions = updateSiteOption(siteOptions, "quicklink.max.menu", (String)model.get("maxMenuCount"));
    	siteOptions = updateSiteOption(siteOptions, "logo.title", 
    			Util.parseString((String)model.get("logoTitle"), Util.getFileProperty("logo.title", null, "PayCast")) );

		ArrayList<SiteOpt> items = new ArrayList<SiteOpt>();
		for(OptionItem item : siteOptions) {
			SiteOpt siteOpt = optService.getSiteOptByOptNameSiteId(item.getName(), site.getId());
			
			if (siteOpt == null) {
				siteOpt = new SiteOpt(site, item.getName(), item.getValue(), session);
			} else {
				siteOpt.setOptValue(item.getValue());
				siteOpt.touchWho(session);
			}
			
			items.add(siteOpt);
		}
    	
		// 저장된 옵션 검토(삭제 대상 파악)
		List<SiteOpt> dbOptions = optService.getSiteOptListBySiteId(site.getId());
		ArrayList<SiteOpt> delOptions = new ArrayList<SiteOpt>();
		
		for(SiteOpt dbSiteOpt : dbOptions) {
			boolean exists = false;
			for(SiteOpt siteOpt : items) {
				if (siteOpt.getOptName().equals(dbSiteOpt.getOptName())) {
					exists = true;
					break;
				}
			}
			
			if (!exists) {
				delOptions.add(dbSiteOpt);
			}
		}
		
		try {
			// 삭제 대상 자료 삭제
			optService.deleteSiteOpts(delOptions);
			
			// 추가, 변경 대상 자료 저장
			for (SiteOpt siteOpt : items) {
				optService.saveOrUpdate(siteOpt);
			}
		} catch (Exception e) {
    		logger.error("update", e);
    		throw new ServerOperationForbiddenException("SaveError");
    	}

        return "OK";
    }
}
