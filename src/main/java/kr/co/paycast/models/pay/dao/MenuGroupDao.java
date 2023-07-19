package kr.co.paycast.models.pay.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.pay.MenuGroup;

public interface MenuGroupDao {
	
	// Common
	public MenuGroup get(int id);
	public void saveOrUpdate(MenuGroup menuGroup);
	public void delete(MenuGroup menuGroup);
	public void delete(List<MenuGroup> menuGroups);

	// for MenuGroup specific
	public void saveAndReorder(MenuGroup menuGroup, HttpSession httpSession);
	public List<MenuGroup> getListByStoreId(int storeId);
	public List<MenuGroup> getListByStoreIdByPublished(int storeId, String published);
	public void reorder(MenuGroup group, int index, HttpSession httpSession);
}
