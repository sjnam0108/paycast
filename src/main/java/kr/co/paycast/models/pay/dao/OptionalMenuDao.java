package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.OptionalMenu;

public interface OptionalMenuDao {
	
	// Common
	public OptionalMenu get(int id);
	public OptionalMenu getName(String name, int id);
	public void saveOrUpdate(OptionalMenu optionalMenu);
	public void delete(OptionalMenu optionalMenu);
	public void delete(List<OptionalMenu> optionalMenus);

	// for OptionalMenu specific
	public List<OptionalMenu> getListByMenuId(int menuId);
}
