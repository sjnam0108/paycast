package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.OptionalMenuList;

public interface OptionalMenuListDao {
	
	// Common
//	public OptionalMenu get(int id);
	public void saveOrUpdate(OptionalMenuList optMenuList);
//	public void delete(OptionalMenu optionalMenu);
//	public void delete(List<OptionalMenu> optionalMenus);

	// for OptionalMenu specific
	public List<OptionalMenuList> getOptionalMenuListByOptionId(int id);
}
