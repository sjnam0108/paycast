package kr.co.paycast.models.fnd.dao;

import java.util.List;

import kr.co.paycast.models.pay.MenuDelete;
import kr.co.paycast.models.pay.MenuGroupDelete;
import kr.co.paycast.models.pay.OptionalMenuDelete;
import kr.co.paycast.models.pay.OptionalMenuListDelete;

public interface MenuDeleteDao {
	// Common
	public void saveOrUpdate(MenuDelete menuDelete);
	
	public List<MenuDelete> getMenuDeleteListByStoreIdGroupId(int storeId, Integer groupId);
	
	// Common
	public void saveOrUpdate(MenuGroupDelete menuGroupDelete);

	public List<MenuGroupDelete> getListByStoreId(int storeId);

	// Common
	public void saveOrUpdate(OptionalMenuDelete optionalMenuDelete);

	public List<OptionalMenuDelete> getOptionalMenuListDeleteByMenuDeleteId(int menuDeleteId);
	
	// Common
	public void saveOrUpdate(OptionalMenuListDelete optionalMenuListDelete);

	public List<OptionalMenuListDelete> getOptionalMenuListDeleteByOptDeleteMenuId(int optMenuDeleteId);

	
}
