package kr.co.paycast.models.pay.dao;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.pay.Menu;
import kr.co.paycast.models.pay.OptionalMenuList;

public interface StoreMenuDao {
	
	// Common
	public Menu get(int id);
	public String getTime(int id);
	public List<Menu> getByMenuList(int id);
	public List<Menu> getByMenuCode(int id,String menuCode);
	public List<OptionalMenuList> getByOptionCode(int id,String menuCode);
	public Menu getName(String name, int groupId);
	public void saveOrUpdate(Menu menu);
	public void saveOrUpdateCode(Menu menu);
	public void delete(Menu menu);
	public void delete(List<Menu> menus);

	// for Menu specific
	public void saveAndReorder(Menu menu, HttpSession httpSession);
	public List<Menu> getListByStoreIdGroupId(int storeId, Integer groupId);
	public List<Menu> getListByStoreIdGroupIdPublished(int storeId, Integer groupId,String published);
	public void reorder(int storeId, Integer groupId, HttpSession httpSession);
	public void reorder(Menu menu, int index, HttpSession httpSession);
}
