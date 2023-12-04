package kr.co.paycast.controllers.pay;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.pay.Menu;
import kr.co.paycast.models.pay.MenuGroup;
import kr.co.paycast.models.pay.OptionalMenu;
import kr.co.paycast.models.pay.OptionalMenuList;
import kr.co.paycast.models.pay.PayUploadTransModel;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StoreEvent;
import kr.co.paycast.models.pay.UploadFile;
import kr.co.paycast.models.pay.service.CouponPointService;
import kr.co.paycast.models.pay.service.MenuService;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.self.service.SelfService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.MenuEditItem;
import kr.co.paycast.viewmodels.pay.MenuGroupItem;
import kr.co.paycast.viewmodels.pay.MenuItem;
import kr.co.paycast.viewmodels.pay.OptionalMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 내 메뉴 컨트롤러
 */
@Controller("pay-my-store-menu-controller")
@RequestMapping(value="/pay/mystoremenu")
public class MyStoreMenuController {
	
	private static final Logger logger = LoggerFactory.getLogger(MyStoreMenuController.class);
	
	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;

    @Autowired 
    private StoreService storeService;

    @Autowired 
    private MenuService menuService;

    @Autowired 
    private PayService payService;

    @Autowired
    private SelfService selfService;
    
    @Autowired
    private CouponPointService couponService;
    
	
	/**
	 * 내 메뉴 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);
    	
    	solMsgMgr.checkStoreSelectionMessage(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "mystoremenu.title"),
					
					new Message("cmd_refresh", "storemenu.refresh"),
					
					new Message("label_group", "storemenu.group"),
					new Message("label_menu", "storemenu.menu"),
					new Message("label_name", "storemenu.name"),
					new Message("label_addMenu", "storemenu.addMenu"),
					new Message("label_groupFormat", "storemenu.groupFormat"),
					new Message("label_price", "storemenu.price"),
					new Message("label_intro", "storemenu.intro"),
					new Message("label_badge", "storemenu.badge"),
					new Message("label_visible", "storemenu.visible"),
					new Message("label_soldOut", "storemenu.soldOut"),
					new Message("label_image", "storemenu.image"),
					new Message("label_count", "storemenu.count"),
					new Message("label_optMenu", "storemenu.optMenu"),

					new Message("item_new", "storemenu.itemNew"),
					new Message("item_rec", "storemenu.itemRec"),
					new Message("item_refill", "storemenu.itemRefill"),
					new Message("item_visible", "storemenu.itemVisible"),
					new Message("item_hidden", "storemenu.itemHidden"),

					new Message("tip_newItem", "storemenu.tipNewItem"),
					new Message("tip_optMenu", "storemenu.tipOptMenu"),
					new Message("tip_checkPrice", "storemenu.tipCheckPrice"),
					new Message("tip_checkBlank", "storemenu.tipCheckBlank"),
    				
    				new Message("tab_basic", "storemenu.tabBasic"),
    				new Message("tab_man", "storemenu.tabMan"),
    				new Message("tab_opt", "storemenu.tabOpt"),
					
					new Message("title_menuImage", "storemenu.menuImage"),
					new Message("label_tab0", "storemenu.upload"),
					new Message("label_tabDesc0", "storemenu.imageFile"),
					new Message("label_tab1", "storemenu.confirm"),
					new Message("label_tabDesc1", "storemenu.preview"),
					
					new Message("msg_noRootMenu", "storemenu.msg.noRootMenu"),
					new Message("msg_noGroupInGroup", "storemenu.msg.noGroupInGroup"),
					new Message("msg_blankName", "storemenu.msg.blankName"),
					new Message("msg_blankMenu", "storemenu.msg.blankMenu"),
					new Message("msg_noQuot", "storemenu.msg.noQuot"),
					new Message("msg_confirmDelGroup", "storemenu.msg.confirmDelGroup"),
					new Message("msg_confirmDelMenu", "storemenu.msg.confirmDelMenu"),
					new Message("msg_refresh", "storemenu.msg.refresh"),
					new Message("msg_itemSize", "storemenu.msg.itemSize")
    			});

    	
    	// 상점 선택 스위치 표시(2개 이상일때)
    	model.addAttribute("isStoreSwitcherMode", true);

    	// 업로드 모델 구성
    	PayUploadTransModel uploadModel = new PayUploadTransModel();

    	Store store = storeService.getStore(SolUtil.getSessionStoreId(session));

		uploadModel.setStoreId(store == null ? -1 : store.getId());
		uploadModel.setType("MENU");
		uploadModel.setAllowedExtensions("[\".jpg\", \".jpeg\", \".png\"]");
		
    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("label_cancel", "upload.cancel"),
					new Message("label_dropFilesHere", "upload.dropFilesHere"),
					new Message("label_headerStatusUploaded", "upload.headerStatusUploaded"),
					new Message("label_headerStatusUploading", "upload.headerStatusUploading"),
					new Message("label_remove", "upload.remove"),
					new Message("label_retry", "upload.retry"),
					new Message("label_select", "upload.select"),
					new Message("label_uploadSelectedFiles", "upload.uploadSelectedFiles"),
					new Message("label_clearSelectedFiles", "upload.clearSelectedFiles"),
					new Message("label_invalidFileExtension", "upload.invalidFileExtension"),
    			});

    	model.addAttribute("uploadModel", uploadModel);
    	//-
    	
    	// 상점 루트 디렉토리 정보
    	model.addAttribute("storeRoot", "S" + 
    			(store == null ? "0" : String.valueOf(store.getId())));

    	
        return "pay/mystoremenu";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody List<MenuGroupItem> read(Locale locale, HttpSession session) {
    	
    	try {
        	List<MenuGroupItem> retList = new ArrayList<MenuGroupItem>();
        	
        	Store store = storeService.getStore(SolUtil.getSessionStoreId(session));
        	if (store != null) {
        		List<MenuGroup> groupList = menuService.getMenuGroupListByStoreId(store.getId());
        		Collections.sort(groupList, MenuGroup.SiblingSeqComparator);
        		
        		for (MenuGroup mg : groupList) {
            		MenuGroupItem group = new MenuGroupItem(mg);
            		
            		List<Menu> list = menuService.getMenuListByStoreIdGroupId(mg.getStore().getId(), mg.getId());
            		Collections.sort(list, Menu.SiblingSeqComparator);
            		List<MenuItem> menus = new ArrayList<MenuItem>();
            		for (Menu menu : list) {
            			menus.add(new MenuItem(menu));
            		}
            		group.setMenus(menus);
            		
            		retList.add(group);
        		}
        	}

        	return retList;
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    }
    
	/**
	 * 메뉴 읽기 액션
	 */
    @RequestMapping(value = "/readMenu", method = RequestMethod.POST)
    public @ResponseBody MenuEditItem readMenu(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	
    	try {
    		Menu menu = menuService.getMenu((int)model.get("id"));
    		MenuEditItem retMenu = new MenuEditItem(menu);
    		
    		retMenu.getMenu().setMenuImage(payService.getUploadFile(
    				retMenu.getMenu().getMenuImageId()));
    		
    		if (menu != null) {
        		List<OptionalMenu> optList = menuService.getOptionalMenuListByMenuId(menu.getId());
        		Collections.sort(optList,  OptionalMenu.SiblingSeqComparator);
    			
        		List<OptionalMenuItem> mandatoryMenus = new ArrayList<OptionalMenuItem>();
        		List<OptionalMenuItem> optionalMenus = new ArrayList<OptionalMenuItem>();
        		
        		for(OptionalMenu optMenu : optList) {
        			//2019.10.30 pos Menu 전송을 위해서 추가
        			List<OptionalMenuList> optMenuList = menuService.getOptionalMenuListByOptionId(optMenu.getId());
        			if (optMenu.getOptType().equals("M")) {
        				logger.info("optMenu.getOptType() [{}] / optMenuList >> size [{}]", optMenu.getOptType(), optMenuList.size());
        				mandatoryMenus.add(new OptionalMenuItem(optMenu, optMenuList));
        			} else if (optMenu.getOptType().equals("O")) {
        				logger.info("optMenu.getOptType() [{}] / optMenuList >> size [{}]", optMenu.getOptType(), optMenuList.size());
        				optionalMenus.add(new OptionalMenuItem(optMenu, optMenuList));
        			}
        		}
        		
        		retMenu.setMandatoryMenus(mandatoryMenus);
        		retMenu.setOptionalMenus(optionalMenus);
    		}
    		
    		return retMenu;
    	} catch (Exception e) {
    		logger.error("readMenu", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    }
    
	/**
	 * 메뉴 및 그룹 표시 상태 변경 액션
	 */
    @RequestMapping(value = "/updateVisible", method = RequestMethod.POST)
    public @ResponseBody String updateVisible(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {

    	String type = Util.parseString((String)model.get("type"));
    	String visible = Util.parseString((String)model.get("published"));
    	
		if (Util.isNotValid(type) || Util.isNotValid(visible) || !(type.equals("G") || type.equals("M"))) {
        	throw new ServerOperationForbiddenException(msgMgr.message(
        			"common.server.msg.wrongParamError", locale));
		}
    	
		if (type.equals("G")) {
			MenuGroup target = menuService.getMenuGroup((int)model.get("id"));
			if (target == null) {
	        	throw new ServerOperationForbiddenException(msgMgr.message(
	        			"common.server.msg.wrongParamError", locale));
			}
			
			target.setPublished(visible);
    		
    		target.touchWho(session);
    		
        	try {
        		menuService.saveOrUpdate(target);
        	} catch (Exception e) {
        		logger.error("updateVisible", e);
        		throw new ServerOperationForbiddenException("SaveError");
        	}
		} else {
			Menu target = menuService.getMenu((int)model.get("id"));
			if (target == null) {
	        	throw new ServerOperationForbiddenException(msgMgr.message(
	        			"common.server.msg.wrongParamError", locale));
			}
			
			target.setPublished(visible);
    		
    		target.touchWho(session);
    		
        	try {
        		menuService.saveOrUpdate(target);
        	} catch (Exception e) {
        		logger.error("updateVisible", e);
        		throw new ServerOperationForbiddenException("SaveError");
        	}
		}
    	
    	return "OK";
    }
    
	/**
	 * 그룹 변경 액션
	 */
    @RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
    public @ResponseBody MenuGroupItem updateGroup(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {

    	MenuGroup target = menuService.getMenuGroup((int)model.get("id"));
    	if (target != null) {
    		String name = Util.parseString((String)model.get("name"));
    		
    		if (Util.isNotValid(name)) {
            	throw new ServerOperationForbiddenException(msgMgr.message(
            			"common.server.msg.wrongParamError", locale));
    		}
    		
    		target.setName(name);
    		
    		target.touchWho(session);
    		
        	try {
        		menuService.saveOrUpdate(target);
        	} catch (Exception e) {
        		logger.error("updateGroup", e);
        		throw new ServerOperationForbiddenException("SaveError");
        	}
    	}
    	
    	return new MenuGroupItem(target);
    }
    
	/**
	 * 그룹 추가 액션
	 */
    @RequestMapping(value = "/createGroups", method = RequestMethod.POST)
    public @ResponseBody List<MenuGroupItem> createGroups(@RequestBody Map<String, Object> model, HttpSession session) {

    	List<MenuGroupItem> groups = new ArrayList<MenuGroupItem>();
    	
    	Store store = storeService.getStore(SolUtil.getSessionStoreId(session));
    	if (store != null) {
    		
        	@SuppressWarnings("unchecked")
    		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
        	
        	for (Object name : objs) {
        		MenuGroup group = new MenuGroup(store, (String)name, session);
        		
        		menuService.saveAndReorder(group, session);
        		
        		groups.add(new MenuGroupItem(group));
        	}
    	}
    	
    	return groups;
    }
    
	/**
	 * 그룹 삭제 액션
	 */
    @RequestMapping(value = "/destroyGroup", method = RequestMethod.POST)
    public @ResponseBody String destroyGroup(@RequestBody Map<String, Object> model, HttpSession session) {

    	MenuGroup menuGroup = menuService.getMenuGroup((int)model.get("id"));
    	if (menuGroup != null) {
        	try {
        		List<Menu> list = menuService.getMenuListByStoreIdGroupId(menuGroup.getStore().getId(), menuGroup.getId());
        		
        		// 삭제된 메뉴를 pos에 넣어주기 위해서 임시 저장 작업 - 2019.11.21 - kdk
        		menuService.saveMenuDelete(menuGroup, list, session);
        		
        		menuService.deleteMenus(list);
        		menuService.deleteMenuGroup(menuGroup);
        	} catch (Exception e) {
        		logger.error("destroyGroup", e);
        		throw new ServerOperationForbiddenException("DeleteError");
        	}
    	}
    	
    	return "OK";
    }
    
	/**
	 * 메뉴 추가 액션
	 */
    @RequestMapping(value = "/createMenus", method = RequestMethod.POST)
    public @ResponseBody List<MenuItem> createMenus(@RequestBody Map<String, Object> model, HttpSession session) {

    	Integer groupId = null;
    	MenuGroup menuGroup = menuService.getMenuGroup((int)model.get("id"));
    	if (menuGroup != null) {
    		groupId = menuGroup.getId();
    	}
    	
    	List<MenuItem> menus = new ArrayList<MenuItem>();
    	
    	Store store = storeService.getStore(SolUtil.getSessionStoreId(session));
    	if (store != null) {

        	@SuppressWarnings("unchecked")
    		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
        	
        	for (Object name : objs) {
        		Menu menu = new Menu(store, (String)name, session);
        		menu.setGroupId(groupId);
        		
        		menuService.saveAndReorder(menu, session);
        		
        		menus.add(new MenuItem(menu));
        	}
    	}
    	
    	return menus;
    }
    
	/**
	 * 메뉴 삭제 액션
	 */
    @RequestMapping(value = "/destroyMenu", method = RequestMethod.POST)
    public @ResponseBody String destroyMenu(@RequestBody Map<String, Object> model, HttpSession session) {

    	Menu menu = menuService.getMenu((int)model.get("id"));
    	if (menu != null) {
        	try {
        		// 삭제된 메뉴를 pos에 넣어주기 위해서 임시 저장 작업 - 2019.11.21 - kdk
        		List<Menu> list = new ArrayList<Menu>();
        		list.add(menu);
        		menuService.saveMenuDelete(null, list, session);
        		
        		menuService.deleteMenu(menu);
        	} catch (Exception e) {
        		logger.error("destroyMenu", e);
        		throw new ServerOperationForbiddenException("DeleteError");
        	}
    	}
    	
    	return "OK";
    }
    
	/**
	 * 항목(그룹, 메뉴) 위치 변경 액션
	 */
    @RequestMapping(value = "/dropItem", method = RequestMethod.POST)
    public @ResponseBody String dropItem(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {

    	int sourceId = (int)model.get("sourceId");
    	String sourceType = (String) model.get("sourceType");
    	Integer destId = (Integer)model.get("destId");
    	//int siblingCnt = (int)model.get("siblingCnt");
    	int index = (int)model.get("index");
    	
		if (Util.isNotValid(sourceType) || !(sourceType.equals("M") || sourceType.equals("G"))) {
        	throw new ServerOperationForbiddenException(msgMgr.message(
        			"common.server.msg.wrongParamError", locale));
		}
		
    	try {
    		if (sourceType.equals("G")) {
    			// 메뉴 그룹 액션으로 순서의 변경만 있음
    			MenuGroup group = menuService.getMenuGroup(sourceId);
    			if (group == null) {
    	        	throw new ServerOperationForbiddenException(msgMgr.message(
    	        			"common.server.msg.wrongParamError", locale));
    			}
    			
    			menuService.reorder(group, index, session);
    		} else {
    			// 메뉴 액션으로 1) 동일 부모 아래 순서의 변경 존재, 2) 다른 부모로 변경하여 순서도 변경
    			Menu menu = menuService.getMenu(sourceId);
    			if (menu == null) {
    	        	throw new ServerOperationForbiddenException(msgMgr.message(
    	        			"common.server.msg.wrongParamError", locale));
    			}
    			
    			if (menu.getGroupId() == destId) {
    				menuService.reorder(menu, index, session);
    			} else {
    				Integer oldGroupId = menu.getGroupId();
    				
    				menu.setGroupId(destId);
    				menuService.saveOrUpdate(menu);
    				
    				menuService.reorder(menu, index, session);
    				menuService.reorderMenu(menu.getStore().getId(), oldGroupId, session);
    			}
    			
    		}
    	} catch (Exception e) {
    		logger.error("dropItem", e);
    		throw new ServerOperationForbiddenException("OperationError");
    	}

    	return "OK";
    }
    
	/**
	 * 메뉴 변경 액션
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
    public @ResponseBody MenuItem updateMenu(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
    	Menu target = menuService.getMenu((int)model.get("id"));
    	
    	MenuItem menuItem = null;
    	if (target != null) {
    		String name = Util.parseString((String)model.get("name"));
    		float price = Util.parseFloat((String)model.get("price"));
    		String code = Util.parseString((String)model.get("code"));
    		String badgeType = Util.parseString((String)model.get("badgeType"));
    		String visibleType = Util.parseString((String)model.get("visibleType"));
        	String intro = Util.parseString((String)model.get("intro"));
        	String menuImage = Util.parseString((String)model.get("menuImage"));
        	String menuUniqueName = Util.parseString((String)model.get("menuUniqueName"));
        	boolean soldOut = Util.parseBoolean((String)model.get("soldOut"));
        	String eventSelect = Util.parseString((String)model.get("eventSelect"));
    		if (Util.isNotValid(name) || price < 0) {
            	throw new ServerOperationForbiddenException(msgMgr.message(
            			"common.server.msg.wrongParamError", locale));
    		}
    		
    		
    		
    		UploadFile menuImageFile = null;
    		String tempRootDir = SolUtil.getPhysicalRoot("UpTemp");
    		String dstMenuDir = SolUtil.getPhysicalRoot("Menu", target.getStore().getId());
    		
    		ArrayList<LinkedHashMap> manMenus = (ArrayList<LinkedHashMap>) model.get("manMenus");
    		ArrayList<LinkedHashMap> optMenus = (ArrayList<LinkedHashMap>) model.get("optMenus");
    		
        	try {
        		//
        		// 임시 폴더에 전달된 고유 파일이 존재하면, 새로 파일이 등록된 상태
        		//    미 존재면, 기존에 이미 등록된 상태
        		//
        		//    새로 등록 액션: 1) 대상 폴더로 파일 이동
        		//
        		File menuFile = new File(tempRootDir + "/" + menuUniqueName);
        		if (Util.isValid(menuUniqueName) && menuFile.exists()) {
        			long menuFileLen = moveFile(tempRootDir, dstMenuDir, menuUniqueName);
        			
        			menuImageFile = new UploadFile(target.getStore(), menuUniqueName, menuImage, menuFileLen, session);
        		}
        	} catch (Exception e) {
            	logger.error("update - move file", e);
            	throw new ServerOperationForbiddenException("OperationError");
        	}
        	
    		target.setName(name);
    		target.setPrice(price);
    		target.setFlagType(badgeType);
    		target.setPublished(visibleType);
    		target.setIntro(intro);
    		target.setSoldOut(soldOut);
    		target.setEvent(eventSelect);  			
    		if (menuImageFile != null) {
    			payService.saveOrUpdate(menuImageFile);
    			target.setMenuImageId(menuImageFile.getId());
    		}
    		if(code != null) {
    			target.setCode(code);
    			try {
            		menuService.saveOrUpdateCode(target);
            		
            		menuItem = new MenuItem(target);
            	} catch (Exception e) {
            		logger.error("updateMenu - save menu", e);
            		throw new ServerOperationForbiddenException("코드 중복");
            	}
    		}
    		target.touchWho(session);
    		
        	try {
        		menuService.saveOrUpdate(target);
        		
        		menuItem = new MenuItem(target);
        	} catch (Exception e) {
        		logger.error("updateMenu - save menu", e);
        		throw new ServerOperationForbiddenException("SaveError");
        	}
        	// 기존 저장된 선택 메뉴 저장
        	ArrayList<String> menuIds = new ArrayList<String>();
        	
        	List<OptionalMenu> userMenus = menuService.getOptionalMenuListByMenuId(target.getId());
        	for(OptionalMenu optMenu : userMenus) {
        		menuIds.add(String.valueOf(optMenu.getId()));
        	}
        	
        	for(LinkedHashMap map : manMenus) {
        		int id = (Integer) map.get("id");
        		if (id > 0) {
        			menuIds.remove(String.valueOf(id));
        		}
        	}
        	
        	for(LinkedHashMap map : optMenus) {
        		int id = (Integer) map.get("id");
        		if (id > 0) {
        			menuIds.remove(String.valueOf(id));
        		}
        	}
        	
        	List<OptionalMenu> delOptMenus = new ArrayList<OptionalMenu>();

        	for (String idStr : menuIds) {
        		OptionalMenu menu = new OptionalMenu();
        		
        		menu.setId(Integer.parseInt(idStr));
        		
        		delOptMenus.add(menu);
        	}
        	
        	try {
        		menuService.deleteOptionalMenus(delOptMenus);

            	for(LinkedHashMap map : manMenus) {
            		int menuId = (Integer) map.get("id");
            		int menuSeq = (Integer) map.get("seq");
            		String menuName = (String) map.get("name");
            		String menuMenus = (String) map.get("menus");

            		if (menuId > 0) {
            			OptionalMenu dstMenu = menuService.getOptionalMenu(menuId);
            			if (dstMenu != null) {
                			dstMenu.setName(menuName);
                			dstMenu.setSiblingSeq(menuSeq);
                			String opMenuListSt = "";
                			if(!Util.isNotValid(menuMenus)){
                				opMenuListSt = optMenuListSaveOrMake(dstMenu, menuMenus, session);
                			}
                			
                			logger.info("manMenus > dstMenu.getId() [{}], opMenuListSt > [{}]", dstMenu.getId(), opMenuListSt);
                			dstMenu.setMenuList(opMenuListSt);
                			dstMenu.touchWho(session);
                			
                			menuService.saveOrUpdate(dstMenu);
            			}
            			
            		} else {
            			OptionalMenu optionalMenu = new OptionalMenu(target, "M", menuName, menuSeq, session);
            			optionalMenu.setMenuList("");
            			menuService.saveOrUpdate(optionalMenu);
            			
            			String opMenuListSt = "";
            			if(!Util.isNotValid(menuMenus)){
            				opMenuListSt = optMenuListSaveOrMake(optionalMenu, menuMenus, session);
            			}
            			logger.info("manMenus > optionalMenu.getId() [{}], opMenuListSt > [{}]", optionalMenu.getId(), opMenuListSt);
            			// optionalMenu의 id값이 없으므로 생성 후 해당 내용으로 다시 업데이트 진행
            			optionalMenu.setMenuList(opMenuListSt);
            			optionalMenu.touchWho(session);
            			menuService.saveOrUpdate(optionalMenu);
            		}
            	}

            	for(LinkedHashMap map : optMenus) {
            		int menuId = (Integer) map.get("id");
            		int menuSeq = (Integer) map.get("seq");
            		String menuName = (String) map.get("name");
            		String menuMenus = (String) map.get("menus");
            		
            		if (menuId > 0) {
            			OptionalMenu dstMenu = menuService.getOptionalMenu(menuId);
            			if (dstMenu != null) {
                			dstMenu.setName(menuName);
                			dstMenu.setSiblingSeq(menuSeq);
                			
                			String opMenuListSt = "";
                			if(!Util.isNotValid(menuMenus)){
                				opMenuListSt = optMenuListSaveOrMake(dstMenu, menuMenus, session);
                			}
                			
                			logger.info("manMenus > dstMenu.getId() [{}], opMenuListSt > [{}]", dstMenu.getId(), opMenuListSt);
                			dstMenu.setMenuList(opMenuListSt);
                			dstMenu.touchWho(session);
                			
                			menuService.saveOrUpdate(dstMenu);
            			}
            			
            		} else {
            			OptionalMenu optionalMenu = new OptionalMenu(target, "O", menuName, menuSeq, session);
            			optionalMenu.setMenuList("");
            			menuService.saveOrUpdate(optionalMenu);
            			
            			String opMenuListSt = "";
            			if(!Util.isNotValid(menuMenus)){
            				opMenuListSt = optMenuListSaveOrMake(optionalMenu, menuMenus, session);
            			}
            			logger.info("manMenus > optionalMenu.getId() [{}], opMenuListSt > [{}]", optionalMenu.getId(), opMenuListSt);
            			
            			// optionalMenu의 id값이 없으므로 생성 후 해당 내용으로 다시 업데이트 진행
            			optionalMenu.setMenuList(opMenuListSt);
            			optionalMenu.touchWho(session);
            			menuService.saveOrUpdate(optionalMenu);
            		}
            	}
        	} catch (Exception e) {
        		logger.error("updateMenu - save optional menus", e);
        		throw new ServerOperationForbiddenException("SaveError");
        	}
        	//-
    	}
    	
    	return menuItem;
    }
    
    /**
     * 옵션 메뉴 List를 등록
     * 2019.10.29 pos Menu 전송을 위해서 추가
     */
    private String optMenuListSaveOrMake(OptionalMenu optMenu, String menuMenus, HttpSession session)throws Exception {
    	// optMenu의 id 값으로 OptionalMenuList 값을 조회
    	// 1. OptionalMenuList 값이 0일 경우 menuMenus 값을 모두 등록 - ok
    	// 2. OptionalMenuList 값이 있을 경우 
    	// 2-1. menuMenus의 name과 price 분리 
    	// 2-2. 분리한 menuMenus의 name과 price 값과 OptionalMenuList의 name과 price 값을 비교 한다. 
    	// 2-3. 동일값이 있을 경우 opMenuListSt에  OptionalMenuList의 ID 값을 포함   
    	// 2-4. 동일값이 없을 경우 해당 정보를 DB에 등록 
    	// 2-4-1. 등록된 값으로 opMenuListSt에 포함
    	// opMenuListSt 값은 ","으로 구문
    	String opMenuListSt = "";
    	// 화면에서 넘어온 List 목록
    	List<String> menuMenusList = Util.tokenizeValidStr(menuMenus, ",");
    	
    	//삭제 할 목록을 넣는다. 
    	List<String> menuListRemove = new ArrayList<String>();
    	// 옵션에뉴에 포함되어 있는 선택옵션 메뉴 LIST 목록
    	List<OptionalMenuList> optMenuList = menuService.getOptionalMenuListByOptionId(optMenu.getId());
    	int optMenuListInt = optMenuList.size();
    	if(optMenuListInt > 0){
    		// 있는것과 없는 것을 구분하여 가지고 있는다. 
    		// 있는것
    		HashMap<String, OptionalMenuList> beforeHave = new HashMap<String, OptionalMenuList>();
    		for(String menuMenusOne : menuMenusList){
    			String[] optionSplit1 = menuMenusOne.split(" ");
				if(optionSplit1.length > 0){
					int optionSplit1Int = optionSplit1.length;
	    			String optMenuPrice = "";
					String optMenuName = "";
					for(int ttt=0; ttt < optionSplit1Int; ttt++){
						if((optionSplit1Int-1) == ttt){
							optMenuPrice = optionSplit1[optionSplit1Int-1];
						}else{
							optMenuName += (" " + optionSplit1[ttt]);
						}
					}
					optMenuName = optMenuName.trim();
					float optMenuPriceFl = Float.parseFloat(optMenuPrice.trim());
	    			for(OptionalMenuList optMeList : optMenuList){
	    				String name = optMeList.getName();
	    				float price = optMeList.getPrice();
	    				if (Float.compare(price, optMenuPriceFl) == 0 && name.equals(optMenuName)) {
	    					beforeHave.put(menuMenusOne, optMeList);
	    					menuListRemove.add(menuMenusOne);
	    				}
	    			}
				}
    		}
    		for (String name : menuListRemove){
    			menuMenusList.remove(name);
    		}
    		
    		// 기존의 것에서 없는 데이터를 저장 한다. 
			if(menuMenusList.size() > 0){
	    		for(String one : menuMenusList){
	    			String[] optionSplit1 = one.split(" ");
					if(optionSplit1.length > 0){
						int optionSplit1Int = optionSplit1.length;
		    			String optMenuPrice = "";
						String optMenuName = "";
						for(int ttt=0; ttt < optionSplit1Int; ttt++){
							if((optionSplit1Int-1) == ttt){
								optMenuPrice = optionSplit1[optionSplit1Int-1];
							}else{
								optMenuName += (" " + optionSplit1[ttt]);
							}
						}
						optMenuName = optMenuName.trim();
						float optMenuPriceFl = Float.parseFloat(optMenuPrice.trim());
						OptionalMenuList opMenuList = new OptionalMenuList(optMenu, optMenuName, optMenuPriceFl, session);
						
						menuService.saveOrUpdate(opMenuList);
						
						beforeHave.put(one, opMenuList);
					}
	    		}
			}
			
			List<String> menuMenusListNew = Util.tokenizeValidStr(menuMenus, ",");
			// key 값을 비교 하여 순서대로 id를 가져온다. 
			for(String menuMenusOne : menuMenusListNew){
				if(beforeHave.containsKey(menuMenusOne)){
					OptionalMenuList list = (OptionalMenuList) beforeHave.get(menuMenusOne);
					if(!Util.isNotValid(opMenuListSt)){
						opMenuListSt += ",";
					}
					opMenuListSt += list.getId() ;
				}
			}
    		
    	}else{
    		String[] optionOrigin = menuMenus.split(",");
    		int loopInt = optionOrigin.length;
    		for(int i = 0; i < loopInt; i++) {
    			if(!Util.isNotValid(optionOrigin[i])){
    				String[] optionSplit1 = optionOrigin[i].split(" ");
    				if(optionSplit1.length > 0){
    					int optionSplit1Int = optionSplit1.length;
    					String optMenuPrice = "";
    					String optMenuName = "";
    					for(int ttt=0; ttt < optionSplit1Int; ttt++){
    						if((optionSplit1Int-1) == ttt){
    							optMenuPrice = optionSplit1[optionSplit1Int-1];
    						}else{
    							optMenuName += (" " + optionSplit1[ttt]);
    						}
    					}
    					OptionalMenuList opMenuList = new OptionalMenuList(optMenu, optMenuName.trim(), Util.parseFloat(optMenuPrice), session);
    					menuService.saveOrUpdate(opMenuList);
    					
    					logger.info("manMenus > optMenuName [{}], opMenuList.getId() > [{}]", optMenuName.trim(), opMenuList.getId());
    					if(!Util.isNotValid(opMenuListSt)){
    						opMenuListSt += ",";
    					}
    					opMenuListSt += opMenuList.getId() ;
    				}
    			}
    		}
    	}
    	
		return opMenuListSt;
    }
    
	/**
	 * 키오스크 컨텐츠 새로고침 액션
	 */
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public @ResponseBody String refreshKiosk(Locale locale, HttpSession session) {
    	
    	try {
    		selfService.setMonTask("M", String.valueOf(SolUtil.getSessionStoreId(session)), session);
    		
    	} catch (Exception e) {
    		logger.error("refreshKiosk", e);
    		throw new ServerOperationForbiddenException("OperationError");
    	}
    	
    	return "OK";
    }
    
    /**
     * 업로드된 파일을 임의 파일명으로 변경 액션
     */
    @RequestMapping(value = "/upcmpl", method = RequestMethod.POST)
    public @ResponseBody String renameUploadedFile(@RequestBody Map<String, Object> model, 
    		HttpSession session, Locale locale) {
		
    	String uploadedFilename = (String)model.get("uploadedFilename");
    	if (Util.isNotValid(uploadedFilename)) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
    	}

		String typeRootDir = SolUtil.getPhysicalRoot("UpTemp");
		File upFile = new File(typeRootDir + "/" + uploadedFilename);
		if (!upFile.exists()) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
		}
		
		String newFilename = Util.uniqueIFilename(uploadedFilename);
		boolean success = false;
		
		try {
			success = upFile.renameTo(new File(typeRootDir + "/" + newFilename));
        } catch (Exception e) {
        	logger.error("upcmpl", e);
        	throw new ServerOperationForbiddenException("OperationError");
        }

		if (!success) {
        	throw new ServerOperationForbiddenException("OperationError");
		}
		
    	return newFilename;
    }
    
    /**
     * 대상 폴더로 파일을 이동시킴
     */
    private long moveFile(String srcFolderStr, String dstFolderStr, String filename)
    		throws Exception {
    	
    	Util.checkDirectory(dstFolderStr);
    	
		File srcFile = new File(srcFolderStr + "/" + filename);
		File dstFile = new File(dstFolderStr + "/" + filename);
		
    	FileCopyUtils.copy(srcFile, new File(dstFolderStr + "/" + filename));
		
    	srcFile.delete();
    	
    	return dstFile.length();
    }
    
	/**
	 * 저장된 쿠폰 select로 조회
	 */
	@RequestMapping(value = "/eventList", method = RequestMethod.POST)
    public @ResponseBody List<StoreEvent> eventReadList(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
		
		int storeId = (int)model.get("storeId");
		
    	try {
    		return couponService.getEventList(storeId);
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("readError");
    	}
    }
    
}
