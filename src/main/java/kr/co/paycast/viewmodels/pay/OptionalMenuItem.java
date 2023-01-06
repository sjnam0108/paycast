package kr.co.paycast.viewmodels.pay;

import java.util.LinkedHashMap;
import java.util.List;

import kr.co.paycast.controllers.store.StoreAPIController;
import kr.co.paycast.models.pay.OptionalMenu;
import kr.co.paycast.models.pay.OptionalMenuDelete;
import kr.co.paycast.models.pay.OptionalMenuList;
import kr.co.paycast.models.pay.OptionalMenuListDelete;
import kr.co.paycast.utils.Util;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionalMenuItem {
	private static final Logger logger = LoggerFactory.getLogger(OptionalMenuItem.class);
	
	
	private int id;
	private int count;
	
	private String name;
	private String menus;
	
	private LinkedHashMap<String, OptionalMenuList> optMenuChoose  = new LinkedHashMap<String, OptionalMenuList>();
	private LinkedHashMap<String, OptionalMenuListDelete> optMenuDeleteChoose  = new LinkedHashMap<String, OptionalMenuListDelete>();
	
	
	public OptionalMenuItem() {}
	public OptionalMenuItem(OptionalMenu optMenu) {
		
		if (optMenu != null) {
			this.id = optMenu.getId();
			this.name = optMenu.getName();
			
			String menuStr = optMenu.getMenuList();
			
			this.menus = menuStr;
			this.count = Util.tokenizeValidStr(menuStr, ",").size();
		}
	}
	public OptionalMenuItem(OptionalMenu optMenu, List<OptionalMenuList> optMenuList) {
		
		if (optMenu != null) {
			this.id = optMenu.getId();
			this.name = optMenu.getName();
			
			String menuStr = optMenu.getMenuList();
			String chooseMenu = "";
			List<String> menuStrList = Util.tokenizeValidStr(menuStr, ",");
			int listsize = menuStrList.size();
			if(listsize > 0 && optMenuList.size() > 0){
				for(String menuStrOne : menuStrList){
					if(!Util.isNotValid(menuStrOne)){
						for(OptionalMenuList optMeList : optMenuList){
							String optMeListId = String.valueOf(optMeList.getId());
							if(menuStrOne.equals(optMeListId)){
								if(!Util.isNotValid(chooseMenu)){
									chooseMenu += ",";
								}
								chooseMenu += optMeList.getName() + " " + String.format("%.0f", optMeList.getPrice()) ;
								
								//선택된 메뉴만 보여준다. 
								this.optMenuChoose.put(optMeListId, optMeList);
							}
						}
					}
				}
			}else{
				chooseMenu = menuStr;
			}
			
			this.menus = chooseMenu;
			this.count = listsize;
		}
	}
	
	public OptionalMenuItem(OptionalMenu optMenu, List<OptionalMenuList> optMenuList, boolean posUse) {
		
		if (optMenu != null) {
			this.id = optMenu.getId();
			this.name = optMenu.getName();
			
			String menuStr = optMenu.getMenuList();
			String chooseMenu = "";
			List<String> menuStrList = Util.tokenizeValidStr(menuStr, ",");
			int listsize = menuStrList.size();
			if(listsize > 0 && optMenuList.size() > 0){
				for(String menuStrOne : menuStrList){
					logger.info( "OptionalMenuItem >>> menuStrOne [{}]", menuStrOne );
					
					if(!Util.isNotValid(menuStrOne)){
						for(OptionalMenuList optMeList : optMenuList){
							String optMeListId = String.valueOf(optMeList.getId());
							if(!Util.isNotValid(chooseMenu)){
								chooseMenu += ",";
							}
							
							// 사용하지 않는 옵션과 사용하고 있는 옵션을 표현
							if(menuStrOne.equals(optMeListId)){
								chooseMenu += optMeList.getName() + " " + String.format("%.0f", optMeList.getPrice())+ " " + optMeListId + " U" ;
							}else{
								chooseMenu += optMeList.getName() + " " + String.format("%.0f", optMeList.getPrice())+ " " + optMeListId + " N" ;
							}
							
							//선택된 메뉴만 보여준다. 
							this.optMenuChoose.put(optMeListId, optMeList);
						}
					}
				}
			}else{
				logger.info( "OptionalMenuItem >>> menuStr [{}]", menuStr );
				
				chooseMenu = menuStr;
			}
			
			this.menus = chooseMenu;
			this.count = listsize;
		}
	}
	
	public OptionalMenuItem(OptionalMenuDelete optDeleteMenu, List<OptionalMenuListDelete> optMenuListDelete) {
		
		if (optDeleteMenu != null) {
			this.id = optDeleteMenu.getOptId();
			this.name = optDeleteMenu.getName();
			
			String menuStr = optDeleteMenu.getMenuList();
			String chooseMenu = "";
			List<String> menuStrList = Util.tokenizeValidStr(menuStr, ",");
			int listsize = menuStrList.size();
			if(listsize > 0 && optMenuListDelete.size() > 0){
				for(String menuStrOne : menuStrList){
					if(!Util.isNotValid(menuStrOne)){
						for(OptionalMenuListDelete optMeList : optMenuListDelete){
							String optMeListId = String.valueOf(optMeList.getOptListId());
							if(!Util.isNotValid(chooseMenu)){
								chooseMenu += ",";
							}
							chooseMenu += optMeList.getName() + " " + String.format("%.0f", optMeList.getPrice()) ;
							
							//선택된 메뉴만 보여준다. 
							this.optMenuDeleteChoose.put(optMeListId, optMeList);
						}
					}
				}
			}else{
				chooseMenu = menuStr;
			}
			
			this.menus = chooseMenu;
			this.count = listsize;
		}
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMenus() {
		return menus;
	}
	
	public void setMenus(String menus) {
		this.menus = menus;
	}
	
	@JsonIgnore
	public LinkedHashMap<String, OptionalMenuList> getOptMenuChoose() {
		return optMenuChoose;
	}
	
	public void setOptMenuChoose(
			LinkedHashMap<String, OptionalMenuList> optMenuChoose) {
		this.optMenuChoose = optMenuChoose;
	}
	
	@JsonIgnore
	public LinkedHashMap<String, OptionalMenuListDelete> getOptMenuDeleteChoose() {
		return optMenuDeleteChoose;
	}
	
	public void setOptMenuDeleteChoose(
			LinkedHashMap<String, OptionalMenuListDelete> optMenuDeleteChoose) {
		this.optMenuDeleteChoose = optMenuDeleteChoose;
	}
	
}
