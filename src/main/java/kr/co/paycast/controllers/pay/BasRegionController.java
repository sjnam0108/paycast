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
import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.fnd.Region;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.models.pay.SiteRegion;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.DropDownListItem;

/**
 * 지역 컨트롤러
 */
@Controller("pay-bas-region-controller")
@RequestMapping(value="/pay/basregion")
public class BasRegionController {
	private static final Logger logger = LoggerFactory.getLogger(BasRegionController.class);

    @Autowired 
    private StoreService storeService;

    @Autowired 
    private SiteService siteService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;
	
	/**
	 * 지역 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "siteregion.title"),
    				new Message("title_regionName", "siteregion.regionName"),
    				new Message("title_regionCode", "siteregion.regionCode"),
    				new Message("title_countryCode", "siteregion.countryCode"),
    				new Message("title_defaultValue", "siteregion.defaultValue"),
    				new Message("label_destCountry", "siteregion.destCountry"),
    				new Message("label_destRegion", "siteregion.destRegion"),
    				new Message("cmd_setDefaultValue", "siteregion.setDefaultValue"),
    			});

    	model.addAttribute("value_defaultCountryCode", Util.getFileProperty("country.code.default", null, "KR"));
    	model.addAttribute("CountryCodes", readCountryCodes());

    	// Device가 PC일 경우에만, 다중 행 선택 설정
    	Util.setMultiSelectableIfFromComputer(model, request);
    	
        return "pay/basregion";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request, 
    		Locale locale) {
    	try {
    		return storeService.getSiteRegionList(request);
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("readError");
    	}
    }
    
	/**
	 * 추가 액션(자료 저장 포함)
	 */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
		String destType = (String)model.get("destType");
		String countryCode = (String)model.get("countryCode");
    	ArrayList<Object> regionIds = (ArrayList<Object>) model.get("regionIds");

    	Site site = siteService.getSite(Util.getSessionSiteId(session));
    	
		int cnt = 0;

		if (site != null && Util.isValid(destType)) {
			if (destType.equals("C") && Util.isValid(countryCode)) {
				List<Region> regList = siteService.getRegionListByCountryCode(countryCode);
				if (regList.size() == 0) {
					return msgMgr.message("siteregion.server.msg.operationNotRequired", locale);
				}
				for(Region region : regList) {
					if (!storeService.isRegisteredSiteRegion(site.getId(), region.getId())) {
						storeService.saveOrUpdate(new SiteRegion(site, region, session));
						cnt ++;
					}
				}
			} else if (destType.equals("R") && regionIds.size() > 0) {
				for(Object regionObj : regionIds) {
					Region region = siteService.getRegion((int) regionObj);
					
					if (region != null && !storeService.isRegisteredSiteRegion(site.getId(), region.getId())) {
						storeService.saveOrUpdate(new SiteRegion(site, region, session));
						cnt ++;
					}
				}
			}
		}

		if (cnt == 0) {
			return msgMgr.message("common.server.msg.operationNotRequired", locale);
		}
		
    	return msgMgr.message("common.server.msg.saveSuccessWithCount", new Object[] {cnt}, locale);
    }
    
	/**
	 * 삭제 액션
	 */
    @RequestMapping(value = "/destroy", method = RequestMethod.POST)
    public @ResponseBody String destroy(@RequestBody Map<String, Object> model, 
    		HttpSession session) {
    	@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
    	
    	List<SiteRegion> siteRegions = new ArrayList<SiteRegion>();

    	for (Object id : objs) {
    		SiteRegion siteRegion = storeService.getSiteRegion((int)id);
    		if (siteRegion != null) {
        		siteRegions.add(siteRegion);
    		}
    	}
    	
    	try {
        	
            // 지역이 삭제될 때 그 지역을 참고로 하는 기기의 자료도 함께 변경
            // 기기 모델 완료 후 적용
    		/*
    		for(SiteRegion siteRegion : siteRegions) {
                List<Stb> regionCodeStbs = stbService.getStbListByLocalCode(
                		siteRegion.getRegion().getRegionCode());
                
                for(Stb stb : regionCodeStbs) {
                	stb.setLocalCode(null);
                	stb.setLocalName(null);
                	
                	stb.touchWho(session);
                	
                	stbService.saveOrUpdate(stb);
                }
    		}
    		*/

    		storeService.deleteSiteRegions(siteRegions);
    	} catch (Exception e) {
    		logger.error("destroy", e);
    		throw new ServerOperationForbiddenException("DeleteError");
    	}

        return "OK";
    }
    
	/**
	 * 기본값으로 설정 액션
	 */
    @RequestMapping(value = "/defaultvalue", method = RequestMethod.POST)
    public @ResponseBody String defaultValue(@RequestBody Map<String, Object> model, 
    		Locale locale, HttpSession session) {
		Site site = siteService.getSite(Util.getSessionSiteId(session));

		@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
    	
    	if (site == null) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
    	}
    	
    	try {
        	for (Object id : objs) {
        		SiteRegion target = storeService.getSiteRegion((int)id);
        		
        		if (target != null) {
        			List<SiteRegion> oldList = storeService.getSiteRegionDefaultValueListBySiteId(site.getId());
        			for(SiteRegion oldVal : oldList) {
        				oldVal.setDefaultValue("N");
        				oldVal.touchWho(session);
        				
        				storeService.saveOrUpdate(oldVal);
        			}
        			
        			target.setDefaultValue("Y");
        			target.touchWho(session);
        			
        			storeService.saveOrUpdate(target);
        			
        			break;
        		}
        	}
    	} catch (Exception e) {
    		logger.error("defaultValue", e);
    		throw new ServerOperationForbiddenException("OperationError");
    	}

        return "OK";
    }
    
    private List<DropDownListItem> readCountryCodes() {
		ArrayList<DropDownListItem> retList = new ArrayList<DropDownListItem>();
    	
    	List<String> codes = Util.tokenizeValidStr(Util.getFileProperty("country.code.list", null, "KR"));
    	
    	for(String code : codes) {
    		retList.add(new DropDownListItem(code, code));
    	}
    	
    	return retList;
    }
}
