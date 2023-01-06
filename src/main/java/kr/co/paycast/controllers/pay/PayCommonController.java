package kr.co.paycast.controllers.pay;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceRequest.FilterDescriptor;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.fnd.Region;
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.models.pay.PayComparator;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.DropDownListItem;
import kr.co.paycast.viewmodels.pay.BasicItem;
import kr.co.paycast.viewmodels.pay.RegionItem;

/**
 * PAY 공통 컨트롤러
 */
@Controller("pay-common-controller")
@RequestMapping(value="/pay/common")
public class PayCommonController {
    
	private static final Logger logger = LoggerFactory.getLogger(PayCommonController.class);

    @Autowired 
    private SiteService siteService;

    @Autowired 
    private StoreService storeService;
	
    
	/**
	 * 공통 읽기 액션 - 전체 지역 정보
	 */
    @RequestMapping(value = "/readRegions", method = RequestMethod.POST)
    public @ResponseBody List<RegionItem> readRegions(@RequestBody DataSourceRequest request, 
    		HttpSession session) {
		ArrayList<RegionItem> retList = new ArrayList<RegionItem>();
    	
		try {
        	DataSourceResult result = siteService.getRegionList(request);
        	if (result.getData().size() <= 500) {
        		for(Object obj : result.getData()) {
        			Region region = (Region) obj;
        			retList.add(new RegionItem(region.getId(), region.getRegionName()));
        		}
        	}
		} catch (Exception e) {
    		logger.error("readRegions", e);
    		throw new ServerOperationForbiddenException("ReadError");
		}
    	
    	return retList;
    }
    
	/**
	 * 공통 읽기 액션 - 국가코드 DropDownList
	 */
    @RequestMapping(value = "/readCountryCodes", method = RequestMethod.POST)
    public @ResponseBody List<DropDownListItem> readCountryCodes(Locale locale, 
    		HttpSession session) {
		ArrayList<DropDownListItem> retList = new ArrayList<DropDownListItem>();
    	
    	List<String> codes = Util.tokenizeValidStr(Util.getFileProperty("country.code.list", null, "KR"));
    	
    	for(String code : codes) {
    		retList.add(new DropDownListItem(code, code));
    	}
    	
    	return retList;
    }
	
	/**
	 * 공통 읽기 액션 - Kendo AutoComplete 용 상점 정보
	 */
    @RequestMapping(value = "/readACStores", method = RequestMethod.POST)
    public @ResponseBody List<BasicItem> readAutoComplStbs(@RequestBody DataSourceRequest request, 
    		HttpSession session) {
    	
    	int siteId = Util.getSessionSiteId(session);
    	
		ArrayList<BasicItem> list = new ArrayList<BasicItem>();

		FilterDescriptor filter = request.getFilter();
		List<FilterDescriptor> filters = filter.getFilters();
		String userInput = "";
		if (filters.size() > 0) {
			userInput = Util.parseString((String) filters.get(0).getValue());
		}

		List<Store> storeList = storeService.getStoreListBySiteIdStoreName(siteId, userInput);
		
		if (storeList.size() <= 50) {
    		for(Store store : storeList) {
    			list.add(new BasicItem(store.getId(), store.getStoreName()));
    		}
    		
    		Collections.sort(list, PayComparator.BasicItemNameComparator);
		}

    	return list;
    }
    
    /**
     * 모듈 전용 파일 업로드 저장 액션
     */
    @RequestMapping(value = "/uploadsave", method = RequestMethod.POST)
    public @ResponseBody String save(@RequestParam List<MultipartFile> files,
    		@RequestParam int storeId, @RequestParam String type, HttpSession session) {
    	try {
    		if (Util.isValid(type)) {
    			Store store = storeService.getStore(storeId);

        		if (store != null) {
            		if (type.equals("TITLE") || type.equals("MENU")) {			// 설정, 메뉴: wizard
            			String typeRootDir = SolUtil.getPhysicalRoot("UpTemp");
            			
            	        for (MultipartFile file : files) {
            	        	if (!file.isEmpty()) {
            	        		File uploadedFile = new File(typeRootDir + "/" + file.getOriginalFilename());
            	        		Util.checkParentDirectory(uploadedFile.getAbsolutePath());
            	        		
            	        		FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadedFile));
            	        	}
            	        }
        			}
        		}
    		}
    	} catch (Exception e) {
    		logger.error("uploadsave", e);
    	}
        
        // Return an empty string to signify success
        return "";
    }

}
