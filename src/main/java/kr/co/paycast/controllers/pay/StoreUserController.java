package kr.co.paycast.controllers.pay;

import java.util.ArrayList;
import java.util.Date;
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
import kr.co.paycast.models.DataSourceRequest.SortDescriptor;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.fnd.LoginLog;
import kr.co.paycast.models.fnd.Role;
import kr.co.paycast.models.fnd.RolePrivilege;
import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.SiteUser;
import kr.co.paycast.models.fnd.User;
import kr.co.paycast.models.fnd.UserPrivilege;
import kr.co.paycast.models.fnd.service.PrivilegeService;
import kr.co.paycast.models.fnd.service.SiteService;
import kr.co.paycast.models.fnd.service.UserService;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreUser;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.UserItem;

/**
 * 상점 관리 - 사용자 컨트롤러
 */
@Controller("pay-store-user-controller")
@RequestMapping(value="/pay/storeuser")
public class StoreUserController {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreUserController.class);

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;

    @Autowired 
    private StoreService storeService;

    @Autowired 
    private UserService userService;
    
	@Autowired 
    private SiteService siteService;
    
	@Autowired 
    private PrivilegeService privService;

    
	/**
	 * 상점 관리 - 사용자 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);
    	
    	solMsgMgr.checkStoreSelectionMessage(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "storeuser.title"),

					new Message("title_username", "storeuser.username"),
					new Message("title_familiarName", "storeuser.familiarName"),
					new Message("title_effective", "storeuser.effective"),
					new Message("title_lastLogin", "storeuser.lastLogin"),
					new Message("title_elapsedTime", "storeuser.elapsedTime"),
					
					new Message("label_password", "storeuser.password"),
					new Message("label_effectiveStartDate", "storeuser.effectiveStartDate"),
					new Message("label_effectiveEndDate", "storeuser.effectiveEndDate"),
					new Message("label_addUser", "storeuser.addUser"),
					new Message("label_regNew", "storeuser.regNew"),
					new Message("label_addPrevUser", "storeuser.addPrevUser"),

					new Message("cmd_exclude", "storeuser.exclude"),
					new Message("cmd_setDefaultPassword", "storeuser.setDefaultPassword"),
    			});

    	// 상점 개요 정보
    	SolUtil.addStoreOverviewInfo(model, locale, session, request);

    	// Device가 PC일 경우에만, 다중 행 선택 설정
    	Util.setMultiSelectableIfFromComputer(model, request);

    	
        return "pay/storeuser";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody List<UserItem> read(@RequestBody DataSourceRequest request, HttpSession session) {
    	
    	ArrayList<UserItem> retList = new ArrayList<UserItem>();
    	Store store = storeService.getStore(getStoreId(session));
    	
    	try {
    		if (store != null) {
    			List<StoreUser> storeUserList = storeService.getStoreUserListByStoreId(store.getId());
    			
    			for(StoreUser storeUser : storeUserList) {
        			User user = storeUser.getUser();
        			LoginLog loginLog = userService.getLastLoginLogByUserId(user.getId());

        			UserItem item = new UserItem(user, userService.isEffectiveUser(user));
        			item.setStoreUserId(storeUser.getId());
        			if (loginLog != null) {
        				item.setLastLoginDate(loginLog.getWhoCreationDate());
        			}
        			
        			retList.add(item);
    			}
    		}
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    	
    	return retList;
    }
    
	/**
	 * 추가 액션
	 */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody String create(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
    	
		String destType = (String)model.get("destType");
		String username = (String)model.get("username");
		String familiarName = (String)model.get("familiarName");
    	ArrayList<Object> userIds = (ArrayList<Object>) model.get("userIds");
    	
    	Site site = siteService.getSite(Util.getSessionSiteId(session));
    	Store store = storeService.getStore(getStoreId(session));
    	
    	// 파라미터 검증
    	if (Util.isNotValid(destType) || site == null || store == null) {
        	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
    	}
    	
    	if (destType.equals("N")) {
    		// 신규 등록(N)
    		if (Util.isNotValid(username) || Util.isNotValid(familiarName)) {
            	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
    		}
        	
        	User target = new User(username, familiarName, "welcome", Util.removeTimeOfDate(new Date()), null, session);
    		
            try {
                userService.saveOrUpdate(target);
                
				siteService.saveOrUpdate(new SiteUser(site, target, session));
				
				storeService.saveOrUpdate(new StoreUser(store, target, session));
				
				grantStoreManagerRoleTo(target, session);
            } catch (DataIntegrityViolationException dive) {
        		logger.error("create", dive);
            	throw new ServerOperationForbiddenException(msgMgr.message("storeuser.server.msg.sameUsername", locale));
            } catch (ConstraintViolationException cve) {
        		logger.error("create", cve);
            	throw new ServerOperationForbiddenException(msgMgr.message("storeuser.server.msg.sameUsername", locale));
            } catch (Exception e) {
        		logger.error("create", e);
            	throw new ServerOperationForbiddenException("SaveError");
            }
    	} else if (destType.equals("U")) {
    		// 기존 사용자 추가(U)
    		
    		int cnt = 0;
			for(Object userObj : userIds) {
				User user = userService.getUser((int) userObj);
				
				if (user != null && !storeService.isRegisteredStoreUser(store.getId(), user.getId())) {
					storeService.saveOrUpdate(new StoreUser(store, user, session));
					
					grantStoreManagerRoleTo(user, session);
					
					cnt ++;
				}
			}

			if (cnt == 0) {
				return msgMgr.message("common.server.msg.operationNotRequired", locale);
			} else {
		    	return msgMgr.message("common.server.msg.saveSuccessWithCount", new Object[] {cnt}, locale);
			}
    	} else {
        	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
    	}
    	
    	return "OK";
    }
    
	/**
	 * 변경 액션
	 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody User update(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {

    	User target = userService.getUser((int)model.get("id"));
    	if (target != null) {
            target.setUsername((String)model.get("username"));
            target.setFamiliarName((String)model.get("familiarName"));
            target.setEffectiveStartDate(Util.removeTimeOfDate(Util.parseZuluTime((String)model.get("effectiveStartDate"))));
            target.setEffectiveEndDate(Util.setMaxTimeOfDate(Util.parseZuluTime((String)model.get("effectiveEndDate"))));
            
            String newPassword = Util.encrypt((String)model.get("newPassword"), target.getSalt());
            if (newPassword != null && ! newPassword.isEmpty()) {
            	target.setPassword(newPassword);
            	target.setNewPassword("");
            }
            
            target.touchWho(session);
            
        	// 비즈니스 로직 검증
            if (target.getEffectiveStartDate() != null && target.getEffectiveEndDate() != null
            		&& target.getEffectiveStartDate().after(target.getEffectiveEndDate())) {
            	throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.effectivedates", locale));
            }
            
            // DB 작업 수행 결과 검증
            try {
                userService.saveOrUpdate(target);
            } catch (DataIntegrityViolationException dive) {
        		logger.error("saveOrUpdate", dive);
            	throw new ServerOperationForbiddenException(msgMgr.message("storeuser.server.msg.sameUsername", locale));
            } catch (ConstraintViolationException cve) {
        		logger.error("saveOrUpdate", cve);
            	throw new ServerOperationForbiddenException(msgMgr.message("storeuser.server.msg.sameUsername", locale));
            } catch (Exception e) {
        		logger.error("saveOrUpdate", e);
            	throw new ServerOperationForbiddenException("SaveError");
            }
            
    	}
    	
        return target;
    }
    
    /**
	 * 제외 액션
	 */
    @RequestMapping(value = "/exclude", method = RequestMethod.POST)
    public @ResponseBody String exclude(@RequestBody Map<String, Object> model, 
    		HttpSession session) {
    	
    	@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
    	
    	List<StoreUser> storeUsers = new ArrayList<StoreUser>();

    	for (Object id : objs) {
    		StoreUser storeUser = storeService.getStoreUser((int)id);
    		if (storeUser != null) {
    			storeUsers.add(storeUser);
    		}
    	}
    	
    	try {
    		storeService.deleteStoreUsers(storeUsers);
    	} catch (Exception e) {
    		logger.error("exclude", e);
    		throw new ServerOperationForbiddenException("OperationError");
    	}

        return "OK";
    }
    
	/**
	 * 삭제 액션
	 */
    @RequestMapping(value = "/destroy", method = RequestMethod.POST)
    public @ResponseBody List<User> destroy(@RequestBody Map<String, Object> model) {
    	
    	@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
    	
    	List<User> users = new ArrayList<User>();

    	for (Object id : objs) {
    		User user = new User();
    		
    		user.setId((int)id);
    		
    		users.add(user);
    	}
    	
    	try {
        	userService.deleteUsers(users);
    	} catch (Exception e) {
    		logger.error("destroy", e);
    		throw new ServerOperationForbiddenException("DeleteError");
    	}

        return users;
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

	/**
	 * 상점 관리자 권한 부여
	 */
    private void grantStoreManagerRoleTo(User user, HttpSession session) {
    
    	Role storeMgrRole = privService.getRole("internal.StoreManager");
    	if (storeMgrRole != null && user != null) {
        	List<RolePrivilege> list = privService.getRolePrivilegeListByRoleId(storeMgrRole.getId());
    		for(RolePrivilege rolePriv : list) {
    			if (!privService.isRegisteredUserPrivilege(user.getId(), rolePriv.getPrivilege().getId())) {
    				privService.saveOrUpdate(new UserPrivilege(user, rolePriv.getPrivilege(), session));
    			}
    		}
    	}
    }
    
	/**
	 * 읽기 액션 - 사용자 정보
	 */
    @RequestMapping(value = "/readUsers", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult readUsers(@RequestBody DataSourceRequest request) {
    	try {
    		SortDescriptor sort = new SortDescriptor();
    		sort.setDir("asc");
    		sort.setField("familiarName");
    		
    		ArrayList<SortDescriptor> list = new ArrayList<SortDescriptor>();
    		list.add(sort);
    		
    		request.setSort(list);
    		
            return userService.getUserList(request);
    	} catch (Exception e) {
    		logger.error("readUsers", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    }
}
