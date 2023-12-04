package kr.co.paycast.models.pay.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.fnd.dao.MenuDeleteDao;
import kr.co.paycast.models.pay.Menu;
import kr.co.paycast.models.pay.MenuDelete;
import kr.co.paycast.models.pay.MenuGroup;
import kr.co.paycast.models.pay.MenuGroupDelete;
import kr.co.paycast.models.pay.OptionalMenu;
import kr.co.paycast.models.pay.OptionalMenuDelete;
import kr.co.paycast.models.pay.OptionalMenuList;
import kr.co.paycast.models.pay.OptionalMenuListDelete;
import kr.co.paycast.models.pay.dao.MenuGroupDao;
import kr.co.paycast.models.pay.dao.OptionalMenuDao;
import kr.co.paycast.models.pay.dao.OptionalMenuListDao;
import kr.co.paycast.models.pay.dao.StoreMenuDao;
import kr.co.paycast.viewmodels.pay.MenuDispItem;
import kr.co.paycast.viewmodels.pay.MenuGroupItem;
import kr.co.paycast.viewmodels.pay.OptionalMenuItem;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("menuService")
public class MenuServiceImpl implements MenuService {
	private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
	
	@Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private StoreMenuDao menuDao;
    
    @Autowired
    private MenuGroupDao menuGroupDao;
    
    @Autowired
    private OptionalMenuDao optionalMenuDao;

    @Autowired
    private OptionalMenuListDao optionalMenuListDao;
    
    @Autowired
    private MenuDeleteDao menuDeleteDao;
    
	@Override
	public void flush() {
		
		sessionFactory.getCurrentSession().flush();
	}
	

	@Override
	public Menu getMenu(int id) {
		
		return menuDao.get(id);
	}

	@Override
	public void saveOrUpdate(Menu menu) {
		
		menuDao.saveOrUpdate(menu);
	}
	
	@Override
	public void saveOrUpdateCode(Menu menu) {
		
		menuDao.saveOrUpdateCode(menu);
	}

	@Override
	public void deleteMenu(Menu menu) {
		
		menuDao.delete(menu);
	}

	@Override
	public void deleteMenus(List<Menu> menus) {
		
		menuDao.delete(menus);
	}

	@Override
	public void saveAndReorder(Menu menu, HttpSession httpSession) {
		
		menuDao.saveAndReorder(menu, httpSession);
	}

	@Override
	public List<Menu> getMenuListByStoreIdGroupId(int storeId, Integer groupId) {
		
		return menuDao.getListByStoreIdGroupId(storeId, groupId);
	}
	
	@Override
	public List<Menu> getMenuListByStoreIdGroupIdPublished(int storeId, Integer groupId, String published) {
		
		return menuDao.getListByStoreIdGroupIdPublished(storeId, groupId, published);
	}

	@Override
	public void reorderMenu(int storeId, Integer groupId, HttpSession httpSession) {
		
		menuDao.reorder(storeId, groupId, httpSession);
	}

	@Override
	public void reorder(Menu menu, int index, HttpSession httpSession) {
		
		menuDao.reorder(menu, index, httpSession);
	}


	@Override
	public MenuGroup getMenuGroup(int id) {
		
		return menuGroupDao.get(id);
	}

	@Override
	public void saveOrUpdate(MenuGroup menuGroup) {
		
		menuGroupDao.saveOrUpdate(menuGroup);
	}

	@Override
	public void deleteMenuGroup(MenuGroup menuGroup) {
		
		menuGroupDao.delete(menuGroup);
	}

	@Override
	public void deleteMenuGroups(List<MenuGroup> menuGroups) {
		
		menuGroupDao.delete(menuGroups);
	}

	@Override
	public void saveAndReorder(MenuGroup menuGroup, HttpSession httpSession) {
		
		menuGroupDao.saveAndReorder(menuGroup, httpSession);
	}

	@Override
	public List<MenuGroup> getMenuGroupListByStoreId(int storeId) {
		
		return menuGroupDao.getListByStoreId(storeId);
	}
	
	@Override
	public List<MenuGroup> getMenuGroupListByStoreIdByPublished(int storeId, String published) {
		
		return menuGroupDao.getListByStoreIdByPublished(storeId,published);
	}

	@Override
	public void reorder(MenuGroup group, int index, HttpSession httpSession) {
		
		menuGroupDao.reorder(group, index, httpSession);
	}


	@Override
	public OptionalMenu getOptionalMenu(int id) {
		
		return optionalMenuDao.get(id);
	}

	@Override
	public void saveOrUpdate(OptionalMenu optionalMenu) {
		
		optionalMenuDao.saveOrUpdate(optionalMenu);
	}

	@Override
	public void deleteOptionalMenu(OptionalMenu optionalMenu) {
		
		optionalMenuDao.delete(optionalMenu);
	}

	@Override
	public void deleteOptionalMenus(List<OptionalMenu> optionalMenus) {
		
		optionalMenuDao.delete(optionalMenus);
	}

	@Override
	public List<OptionalMenu> getOptionalMenuListByMenuId(int menuId) {
		
		return optionalMenuDao.getListByMenuId(menuId);
	}


	@Override
	public List<MenuGroupItem> getAllMenus(int storeId, boolean onlyPublishedMode) {
		
		List<MenuGroupItem> retList = new ArrayList<MenuGroupItem>();
		
		List<MenuGroup> gList = getMenuGroupListByStoreId(storeId);
		Collections.sort(gList, MenuGroup.SiblingSeqComparator);
		
		for(MenuGroup group : gList) {
			if (onlyPublishedMode && !group.getPublished().equals("Y")) {
				continue;
			}
			
			MenuGroupItem g = new MenuGroupItem(group);
			
			List<Menu> mList = getMenuListByStoreIdGroupId(storeId, g.getId());
			Collections.sort(mList, Menu.SiblingSeqComparator);
			
			List<MenuDispItem> dMenus = new ArrayList<MenuDispItem>();
			
			for(Menu menu : mList) {
				if (onlyPublishedMode && !menu.getPublished().equals("Y")) {
					continue;
				}
				
				MenuDispItem m = new MenuDispItem(menu);
				
				List<OptionalMenu> oMenus = getOptionalMenuListByMenuId(menu.getId());
				Collections.sort(oMenus, OptionalMenu.SiblingSeqComparator);
				
				List<OptionalMenuItem> manMenus = new ArrayList<OptionalMenuItem>();
				List<OptionalMenuItem> optMenus = new ArrayList<OptionalMenuItem>();
				
				for(OptionalMenu optMenu : oMenus) {
					// M: 필수 메뉴, O: 추가 메뉴
//					if (optMenu.getOptType().equals("M")) {
//						manMenus.add(new OptionalMenuItem(optMenu));
//					} else if (optMenu.getOptType().equals("O")) {
//						optMenus.add(new OptionalMenuItem(optMenu));
//					}
					List<OptionalMenuList> optMenuList = getOptionalMenuListByOptionId(optMenu.getId());
        			if (optMenu.getOptType().equals("M")) {
        				manMenus.add(new OptionalMenuItem(optMenu, optMenuList));
        			} else if (optMenu.getOptType().equals("O")) {
        				optMenus.add(new OptionalMenuItem(optMenu, optMenuList));
        			}
				}
				m.setManMenus(manMenus);
				m.setOptMenus(optMenus);
				
				dMenus.add(m);
			}
			
			g.setDMenus(dMenus);
			
			retList.add(g);
		}

		return retList;
	}

	@Override
	public void saveOrUpdate(OptionalMenuList optMenuList) {
		
		optionalMenuListDao.saveOrUpdate(optMenuList);
	}


	@Override
	public List<OptionalMenuList> getOptionalMenuListByOptionId(int id) {
		return optionalMenuListDao.getOptionalMenuListByOptionId(id);
	}
	
	
	@Override
	public void saveMenuDelete(MenuGroup menuGroup, List<Menu> menuList, HttpSession session) {

		MenuGroupDelete menuGroupDelete = null;
		if(menuGroup != null){
			// 메뉴 그룹이 삭제 되었을 경우
			menuGroupDelete = new MenuGroupDelete(menuGroup.getId(), menuGroup.getStore(), menuGroup.getName(), menuGroup.getSiblingSeq(),
					 menuGroup.getPublished(), session);
			
			menuDeleteDao.saveOrUpdate(menuGroupDelete);
		}
		
		if(menuList.size() > 0){
			for(Menu menu : menuList){
				MenuDelete menuDelete = new MenuDelete(menu.getId(), menu.getStore(), menu.getName(), menu.getSiblingSeq(), menu.getPrice(),
						menu.getMenuImageId(), menu.getFlagType(), menu.getIntro(), menu.isSoldOut(), menu.getPublished(), session);
				if(menuGroupDelete != null){
					//group 삭제가 동시에 진행될 경우 저장된 menuGroupDelete id로 변경 하여 저장
					menuDelete.setGroupId(menuGroupDelete.getId());
				}else{
					menuDelete.setGroupId(menu.getGroupId());
				}
				
				menuDeleteDao.saveOrUpdate(menuDelete);
				
				// 해당 메뉴의 옵션 메뉴도 복사 진행
				List<OptionalMenu> optionalMenuList = getOptionalMenuListByMenuId(menu.getId()); 
				if(optionalMenuList.size() > 0){
					for(OptionalMenu optionalMenu : optionalMenuList){
						OptionalMenuDelete optionalMenuDelete = new OptionalMenuDelete(optionalMenu, menuDelete, session);
						menuDeleteDao.saveOrUpdate(optionalMenuDelete);
						
						List<OptionalMenuList> optMenuList = getOptionalMenuListByOptionId(optionalMenu.getId());
						if(optionalMenuList.size() > 0){
							for(OptionalMenuList optionalMenuListOne : optMenuList){
								OptionalMenuListDelete optionalMenuListDelete = new OptionalMenuListDelete(optionalMenuListOne, optionalMenuDelete, session);
								
								menuDeleteDao.saveOrUpdate(optionalMenuListDelete);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public List<MenuGroupItem> getAllMenusPos(int storeId) {
		
		List<MenuGroupItem> retList = new ArrayList<MenuGroupItem>();
		
		List<MenuGroup> gList = getMenuGroupListByStoreId(storeId);
		Collections.sort(gList, MenuGroup.SiblingSeqComparator);
		
		for(MenuGroup group : gList) {
			
			MenuGroupItem g = new MenuGroupItem(group);
			
			List<Menu> mList = getMenuListByStoreIdGroupId(storeId, g.getId());
			Collections.sort(mList, Menu.SiblingSeqComparator);
			
			List<MenuDispItem> dMenus = new ArrayList<MenuDispItem>();
			
			for(Menu menu : mList) {
				MenuDispItem m = new MenuDispItem(menu);
				List<OptionalMenu> oMenus = getOptionalMenuListByMenuId(menu.getId());
				Collections.sort(oMenus, OptionalMenu.SiblingSeqComparator);
				
				List<OptionalMenuItem> manMenus = new ArrayList<OptionalMenuItem>();
				List<OptionalMenuItem> optMenus = new ArrayList<OptionalMenuItem>();
				
				for(OptionalMenu optMenu : oMenus) {
					List<OptionalMenuList> optMenuList = getOptionalMenuListByOptionId(optMenu.getId());
        			if (optMenu.getOptType().equals("M")) {
        				manMenus.add(new OptionalMenuItem(optMenu, optMenuList, true));
        			} else if (optMenu.getOptType().equals("O")) {
        				optMenus.add(new OptionalMenuItem(optMenu, optMenuList, true));
        			}
				}
				m.setManMenus(manMenus);
				m.setOptMenus(optMenus);
				
				dMenus.add(m);
			}
			// 2019.11.21 삭제된 메뉴를 추가
			List<MenuDispItem> dList = getMenuDeleteListByStoreIdGroupId(storeId, g.getId());
			for(MenuDispItem mdi: dList){
				dMenus.add(mdi);
			}
			
			g.setDMenus(dMenus);
			
			retList.add(g);
		}
		
		// 삭제된 그룹에 대해서 추가 진행
		List<MenuGroupDelete> dGroupList = getMenuGroupDeleteListByStoreId(storeId);
		if(dGroupList.size() > 0){
			for(MenuGroupDelete dao : dGroupList){
				MenuGroup menuGroup = new MenuGroup();	
				menuGroup.setId(dao.getGroupId());
				menuGroup.setName(dao.getName());
				menuGroup.setPublished("D");
				menuGroup.setSiblingSeq(9999);
				menuGroup.setStore(dao.getStore());
				menuGroup.setWhoCreatedBy(dao.getWhoCreatedBy());
				menuGroup.setWhoCreationDate(dao.getWhoCreationDate());
				menuGroup.setWhoLastUpdateDate(dao.getWhoLastUpdateDate());
				menuGroup.setWhoLastUpdatedBy(dao.getWhoLastUpdatedBy());
				menuGroup.setWhoLastUpdateLogin(dao.getWhoLastUpdateLogin());
				
				MenuGroupItem g = new MenuGroupItem(menuGroup);
				// 2019.11.21 삭제된 메뉴를 추가
				List<MenuDispItem> dList = getMenuDeleteListByStoreIdGroupId(storeId, dao.getId());
				
				g.setDMenus(dList);
				
				retList.add(g);
			}
		}

		return retList;
	}
	
	@Override
	public List<MenuGroupDelete> getMenuGroupDeleteListByStoreId(int storeId) {
		
		return menuDeleteDao.getListByStoreId(storeId);
	}
	
	@Override
	public List<MenuDispItem> getMenuDeleteListByStoreIdGroupId(int storeId, Integer groupId) {
		List<MenuDispItem> dMenus = new ArrayList<MenuDispItem>();
		
		
		List<Menu> menuList = new ArrayList<Menu>();
		List<MenuDelete> menuDeleteList =  menuDeleteDao.getMenuDeleteListByStoreIdGroupId(storeId, groupId);
		logger.info("menuDeleteList.size() [{}]", menuDeleteList.size());
		
		if(menuDeleteList.size() > 0){
			for(MenuDelete dao : menuDeleteList){
				Menu menu = new Menu();
				menu.setId(dao.getMenuId());
				menu.setStore(dao.getStore());
				menu.setName(dao.getName());
				menu.setSiblingSeq(9999);
				menu.setPrice(dao.getPrice());
				menu.setMenuImageId(dao.getMenuImageId());
				menu.setFlagType(dao.getFlagType());
				menu.setIntro(dao.getIntro());
				menu.setSoldOut(dao.isSoldOut());
				menu.setPublished("D");
				menu.setWhoCreatedBy(dao.getWhoCreatedBy());
				menu.setWhoCreationDate(dao.getWhoCreationDate());
				menu.setWhoLastUpdateDate(dao.getWhoLastUpdateDate());
				menu.setWhoLastUpdatedBy(dao.getWhoLastUpdatedBy() );
				menu.setWhoLastUpdateLogin(dao.getWhoLastUpdateLogin());
				
				menuList.add(menu);
				
				logger.info("MenuDelete.getId() [{}]", dao.getId());
				
				//삭제된 옵션을 가져온다.
				MenuDispItem m = new MenuDispItem(menu);
				List<OptionalMenuDelete> oMenus = getOptionalMenuListDeleteByMenuDeleteId(dao.getId());

				logger.info("oMenus.size() [{}]", oMenus.size());
				List<OptionalMenuItem> manMenus = new ArrayList<OptionalMenuItem>();
				List<OptionalMenuItem> optMenus = new ArrayList<OptionalMenuItem>();
				if(oMenus.size()>0){
					for(OptionalMenuDelete optDeleteMenu : oMenus) {
						logger.info("optDeleteMenu.getId() [{}]", optDeleteMenu.getId());
						List<OptionalMenuListDelete> optMenuListDelete = getOptionalMenuListDeleteByOptDeleteMenuId(optDeleteMenu.getId());
						logger.info("optMenuListDelete.size() [{}]", optMenuListDelete.size());
						
						if (optDeleteMenu.getOptType().equals("M")) {
							manMenus.add(new OptionalMenuItem(optDeleteMenu, optMenuListDelete));
						} else if (optDeleteMenu.getOptType().equals("O")) {
							optMenus.add(new OptionalMenuItem(optDeleteMenu, optMenuListDelete));
						}
					}
				}
				m.setManMenus(manMenus);
				m.setOptMenus(optMenus);
				
				dMenus.add(m);
			}
		}

		return dMenus;
	}
	
	@Override
	public List<OptionalMenuDelete> getOptionalMenuListDeleteByMenuDeleteId(int menuDeleteId) {
		
		return menuDeleteDao.getOptionalMenuListDeleteByMenuDeleteId(menuDeleteId);
	}
	
	@Override
	public List<OptionalMenuListDelete> getOptionalMenuListDeleteByOptDeleteMenuId(int optMenuDeleteId) {
		
		return menuDeleteDao.getOptionalMenuListDeleteByOptDeleteMenuId(optMenuDeleteId);
	}
}
