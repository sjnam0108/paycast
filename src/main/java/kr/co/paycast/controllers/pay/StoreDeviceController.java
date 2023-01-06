package kr.co.paycast.controllers.pay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.DropDownListItem;

/**
 * 상점 관리 - 기기 컨트롤러
 */
@Controller("pay-store-device-controller")
@RequestMapping(value="/pay/storedevice")
public class StoreDeviceController {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreDeviceController.class);

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;

    @Autowired 
    private DeviceService devService;

    @Autowired 
    private StoreService storeService;

    
	/**
	 * 상점 관리 - 기기 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);
    	
    	solMsgMgr.checkStoreSelectionMessage(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "storedevice.title"),
					new Message("title_deviceType", "storedevice.deviceType"),
					new Message("title_deviceSeq", "storedevice.deviceSeq"),
					new Message("title_familiarName", "storedevice.familiarName"),
					new Message("title_ukid", "storedevice.ukid"),
					new Message("label_fcmToken", "storedevice.fcmToken"),
					new Message("label_memo", "storedevice.memo"),
    			});

    	// 상점 개요 정보
    	SolUtil.addStoreOverviewInfo(model, locale, session, request);

    	
        return "pay/storedevice";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request, 
    		HttpSession session, HttpServletRequest req, HttpServletResponse res) {

    	try {
    		return devService.getDeviceList(request);
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
    	
    	Store store = storeService.getStore(getStoreId(session));
    	String deviceType = (String)model.get("deviceType");
    	
    	// 파라미터 검증
    	if (store == null || Util.isNotValid(deviceType)) {
        	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
        }

    	Device target = new Device(store, deviceType, session);
    	
        try {
            devService.saveAndReorder(target, session);
        } catch (DataIntegrityViolationException dive) {
    		logger.error("create", dive);
        	throw new ServerOperationForbiddenException(msgMgr.message("storedevice.server.msg.sameDeviceTypeSeq", locale));
        } catch (ConstraintViolationException cve) {
    		logger.error("create", cve);
        	throw new ServerOperationForbiddenException(msgMgr.message("storedevice.server.msg.sameDeviceTypeSeq", locale));
        } catch (Exception e) {
    		logger.error("create", e);
        	throw new ServerOperationForbiddenException("SaveError");
        }

        return "OK";
    }
    
	/**
	 * 변경 액션
	 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {

    	Device target = devService.getDevice((int)model.get("id"));
    	if (target != null) {
    		String ukid = Util.parseString((String)model.get("ukid"));
    		
        	target.setUkid(Util.isValid(ukid) ? ukid.toUpperCase() : null);
        	target.setFamiliarName(Util.parseString((String)model.get("familiarName")));
        	target.setMemo(Util.parseString((String)model.get("memo")));
        	
            target.touchWho(session);
        	
            try {
            	devService.saveOrUpdate(target);
            } catch (DataIntegrityViolationException dive) {
        		logger.error("update", dive);
            	throw new ServerOperationForbiddenException(msgMgr.message("storedevice.server.msg.sameUkid", locale));
            } catch (ConstraintViolationException cve) {
        		logger.error("update", cve);
            	throw new ServerOperationForbiddenException(msgMgr.message("storedevice.server.msg.sameUkid", locale));
            } catch (Exception e) {
        		logger.error("update", e);
            	throw new ServerOperationForbiddenException("SaveError");
            }
    	}
    	
        return "OK";
    }
    
	/**
	 * 삭제 액션
	 */
    @RequestMapping(value = "/destroy", method = RequestMethod.POST)
    public @ResponseBody String destroy(@RequestBody Map<String, Object> model, HttpSession session) {

    	@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
    	
    	String deviceType = "";
    	int storeId = -1;
    	
    	for (Object id : objs) {
    		Device device = devService.getDevice((int)id);
    		if (device != null) {
    			storeId = device.getStore().getId();
    			deviceType = device.getDeviceType();
    			
    	    	try {
    	        	devService.deleteDevice(device);
    	        	
    	        	if (storeId > 0 && Util.isValid(deviceType)) {
    	        		devService.reorderDevices(storeId, deviceType, session);
    	        	}
    	    	} catch (Exception e) {
    	    		logger.error("destroy", e);
    	    		throw new ServerOperationForbiddenException("DeleteError");
    	    	}
    		}
    	}

        return "OK";
    }
    
	/**
	 * 읽기 액션 - 기기 유형 DropDownList
	 */
    @RequestMapping(value = "/readDeviceTypes", method = RequestMethod.POST)
    public @ResponseBody List<DropDownListItem> readStatusTypes(Locale locale, 
    		HttpSession session) {
		ArrayList<DropDownListItem> list = new ArrayList<DropDownListItem>();

		list.add(new DropDownListItem("fas fa-hamburger fa-fw", msgMgr.message("deviceType.kiosk", locale), "K"));
		list.add(new DropDownListItem("fas fa-tablet-alt fa-fw", msgMgr.message("deviceType.kitchenPad", locale), "D"));
		list.add(new DropDownListItem("fas fa-solar-panel fa-fw", msgMgr.message("deviceType.notifier", locale), "N"));
		list.add(new DropDownListItem("fas fa-print fa-fw", msgMgr.message("deviceType.printer", locale), "P"));
		
		return list;
    }

	/**
	 * 상점 번호 획득
	 */
    private int getStoreId(HttpSession session) {
    	
    	int storeId = -1;
    	LoginUser loginUser = null;
    	if (session != null) {
    		loginUser = (LoginUser) session.getAttribute("loginUser");
    		if (loginUser != null) {
    			storeId = loginUser.getStoreId();
    		}
    	}
		
    	return storeId;
    }
}