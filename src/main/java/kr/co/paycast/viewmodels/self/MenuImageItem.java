package kr.co.paycast.viewmodels.self;

import java.util.ArrayList;
import java.util.List;

public class MenuImageItem  {
	private int id;

	private String menuGroupName; //메뉴 그룹 명
	
	private String menuGroupSeq; //메뉴 그룹 순서	
	
	private List<MenuItemOption> selfMenu = new ArrayList<MenuItemOption>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMenuGroupName() {
		return menuGroupName;
	}

	public void setMenuGroupName(String menuGroupName) {
		this.menuGroupName = menuGroupName;
	}

	public String getMenuGroupSeq() {
		return menuGroupSeq;
	}

	public void setMenuGroupSeq(String menuGroupSeq) {
		this.menuGroupSeq = menuGroupSeq;
	}

	public List<MenuItemOption> getSelfMenu() {
		return selfMenu;
	}

	public void setSelfMenu(List<MenuItemOption> selfMenu) {
		this.selfMenu = selfMenu;
	}

}
