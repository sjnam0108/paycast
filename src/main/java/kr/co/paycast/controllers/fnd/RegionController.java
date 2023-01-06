package kr.co.paycast.controllers.fnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import kr.co.paycast.models.fnd.Region;
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.DropDownListItem;

/**
 * 전체 지역 컨트롤러
 */
@Controller("fnd-region-controller")
@RequestMapping(value="/fnd/region")
public class RegionController {
	private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired 
    private SiteService siteService;

    /*
    @Autowired 
    private DeviceService devService;
    */
    
	@Autowired
	private MessageManager msgMgr;
    
	@Autowired
	private ModelManager modelMgr;
	
	/**
	 * 전체 지역 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	msgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "region.title"),
    				new Message("title_regionName", "region.regionName"),
    				new Message("title_regionCode", "region.regionCode"),
    				new Message("title_countryCode", "region.countryCode"),
    				new Message("title_coordX", "region.coordX"),
    				new Message("title_coordY", "region.coordY"),
    				new Message("label_region", "region.region"),
    			});

    	model.addAttribute("value_defaultCountryCode", Util.getFileProperty("country.code.default", null, "KR"));
    	model.addAttribute("CountryCodes", readCountryCodes());

    	// Device가 PC일 경우에만, 다중 행 선택 설정
    	Util.setMultiSelectableIfFromComputer(model, request);
        
    	
        return "fnd/region";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request, 
    		Locale locale) {
    	try {
    		return siteService.getRegionList(request);
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("readError");
    	}
    }
    
	/**
	 * 추가 액션
	 */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	String regionName = (String)model.get("regionName");
    	String regionCode = (String)model.get("regionCode");
    	String x = (String)model.get("x");
    	String y = (String)model.get("y");
    	String countryCode = (String)model.get("countryCode");
		
		if (Util.isNotValid(regionName) || Util.isNotValid(regionCode) || Util.isNotValid(countryCode)) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
		}

		Region target = new Region(regionCode, regionName, x, y, countryCode, session);
    	
        // DB 작업 수행 결과 검증
        try {
        	siteService.saveOrUpdate(target);
        } catch (DataIntegrityViolationException dive) {
    		logger.error("saveOrUpdate", dive);
        	throw new ServerOperationForbiddenException(msgMgr.message("region.server.msg.sameValue", locale));
        } catch (ConstraintViolationException cve) {
    		logger.error("saveOrUpdate", cve);
        	throw new ServerOperationForbiddenException(msgMgr.message("region.server.msg.sameValue", locale));
        } catch (Exception e) {
    		logger.error("saveOrUpdate", e);
        	throw new ServerOperationForbiddenException("SaveError");
        }

        return "OK";
    }
    
	/**
	 * 변경 액션
	 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	String regionName = (String)model.get("regionName");
    	String regionCode = (String)model.get("regionCode");
    	String x = (String)model.get("x");
    	String y = (String)model.get("y");
    	String countryCode = (String)model.get("countryCode");
		
    	Region target = siteService.getRegion((int)model.get("id"));
		if (target == null || Util.isNotValid(regionName) || Util.isNotValid(regionCode) || 
				Util.isNotValid(countryCode)) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
		}

		//String oldLocalCode = target.getRegionCode();
		//String oldLocalName = target.getRegionName();

		target.setRegionCode(regionCode);
		target.setRegionName(regionName);
		target.setX(x);
		target.setY(y);
		target.setCountryCode(countryCode);
        
        target.touchWho(session);
        
        // DB 작업 수행 결과 검증
        try {
        	siteService.saveOrUpdate(target);
        } catch (DataIntegrityViolationException dive) {
    		logger.error("saveOrUpdate", dive);
        	throw new ServerOperationForbiddenException(msgMgr.message("region.server.msg.sameValue", locale));
        } catch (ConstraintViolationException cve) {
    		logger.error("saveOrUpdate", cve);
        	throw new ServerOperationForbiddenException(msgMgr.message("region.server.msg.sameValue", locale));
        } catch (Exception e) {
    		logger.error("saveOrUpdate", e);
        	throw new ServerOperationForbiddenException("SaveError");
        }
    	
        // 지역이 변경될 때 그 지역을 참고로 하는 기기의 자료도 함께 변경
        // 기기 모델 완료 후 적용
        /*
        try {
            if (!oldLocalCode.equals(regionCode) || !oldLocalName.equals(regionName)) {
                List<Stb> localCodeStbs = stbService.getStbListByLocalCode(oldLocalCode);
                for(Stb stb : localCodeStbs) {
                	stb.setLocalCode(regionCode);
                	stb.setLocalName(regionName);
                	
                	stb.touchWho(session);
                	
                	stbService.saveOrUpdate(stb);
                }
            }
        } catch (Exception e) {
    		logger.error("saveOrUpdate", e);
        	throw new ServerOperationForbiddenException("SaveError");
        }
        */
        
        return "OK";
    }
    
	/**
	 * 삭제 액션
	 */
    @RequestMapping(value = "/destroy", method = RequestMethod.POST)
    public @ResponseBody String destroy(@RequestBody Map<String, Object> model, 
    		HttpSession session) {
    	@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
    	
    	List<Region> regions = new ArrayList<Region>();
    	
    	for (Object id : objs) {
    		Region region = siteService.getRegion((int)id);
    		if (region != null) {
    			regions.add(region);
    		}
    	}
    	
    	try {
        	
            // 지역이 삭제될 때 그 지역을 참고로 하는 기기의 자료도 함께 변경
            // 기기 모델 완료 후 적용
    		/*
    		for(Region region : regions) {
                List<Stb> regionCodeStbs = stbService.getStbListByLocalCode(region.getRegionCode());
                
                for(Stb stb : regionCodeStbs) {
                	stb.setLocalCode(null);
                	stb.setLocalName(null);
                	
                	stb.touchWho(session);
                	
                	stbService.saveOrUpdate(stb);
                }
    		}
    		*/

        	siteService.deleteRegions(regions);
    	} catch (Exception e) {
    		logger.error("destroy", e);
    		throw new ServerOperationForbiddenException("DeleteError");
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
