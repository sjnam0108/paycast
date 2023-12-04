package kr.co.paycast.models.pay.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.pay.Menu;
import kr.co.paycast.models.pay.MenuGroup;
import kr.co.paycast.models.pay.MenuGroupDelete;
import kr.co.paycast.models.pay.OptionalMenu;
import kr.co.paycast.models.pay.OptionalMenuDelete;
import kr.co.paycast.models.pay.OptionalMenuList;
import kr.co.paycast.models.pay.OptionalMenuListDelete;
import kr.co.paycast.viewmodels.pay.MenuDispItem;
import kr.co.paycast.viewmodels.pay.MenuGroupItem;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MenuService {
	
	// Common
	public void flush();
	
	
	//
	// for Menu Dao
	//
	// Common
	public Menu getMenu(int id);
	public void saveOrUpdate(Menu menu);
	public void saveOrUpdateCode(Menu menu);
	public void deleteMenu(Menu menu);
	public void deleteMenus(List<Menu> menus);

	// for Menu specific
	public void saveAndReorder(Menu menu, HttpSession httpSession);
	public List<Menu> getMenuListByStoreIdGroupId(int storeId, Integer groupId);
	public List<Menu> getMenuListByStoreIdGroupIdPublished(int storeId, Integer groupId,String published);
	public void reorderMenu(int storeId, Integer groupId, HttpSession httpSession);
	public void reorder(Menu menu, int index, HttpSession httpSession);
	
	
	//
	// for MenuGroup Dao
	//
	// Common
	public MenuGroup getMenuGroup(int id);
	public void saveOrUpdate(MenuGroup menuGroup);
	public void deleteMenuGroup(MenuGroup menuGroup);
	public void deleteMenuGroups(List<MenuGroup> menuGroups);

	// for MenuGroup specific
	public void saveAndReorder(MenuGroup menuGroup, HttpSession httpSession);
	public List<MenuGroup> getMenuGroupListByStoreId(int storeId);
	public List<MenuGroup> getMenuGroupListByStoreIdByPublished(int storeId, String published);
	public void reorder(MenuGroup group, int index, HttpSession httpSession);
	
	
	//
	// for OptionalMenu Dao
	//
	// Common
	public OptionalMenu getOptionalMenu(int id);
	public void saveOrUpdate(OptionalMenu optionalMenu);
	public void deleteOptionalMenu(OptionalMenu optionalMenu);
	public void deleteOptionalMenus(List<OptionalMenu> optionalMenus);

	// for OptionalMenu specific
	public List<OptionalMenu> getOptionalMenuListByMenuId(int menuId);
	
	
	//
	// for Common
	//
	public List<MenuGroupItem> getAllMenus(int storeId, boolean onlyPublishedMode);

	// for OptionalMenuList specific
	public void saveOrUpdate(OptionalMenuList opMenuList);
	public List<OptionalMenuList> getOptionalMenuListByOptionId(int id);

	//
	// for MenuDelete Dao
	//
	public void saveMenuDelete(MenuGroup menuGroup, List<Menu> menuList, HttpSession session);
	public List<MenuGroupItem> getAllMenusPos(int storeId);

	List<MenuDispItem> getMenuDeleteListByStoreIdGroupId(int storeId, Integer groupId);
	List<MenuGroupDelete> getMenuGroupDeleteListByStoreId(int storeId);

	List<OptionalMenuDelete> getOptionalMenuListDeleteByMenuDeleteId(int menuDeleteId);
	List<OptionalMenuListDelete> getOptionalMenuListDeleteByOptDeleteMenuId(int optMenuDeleteId);
}
