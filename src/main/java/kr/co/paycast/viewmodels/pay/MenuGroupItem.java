package kr.co.paycast.viewmodels.pay;

import java.util.ArrayList;
import java.util.List;

import kr.co.paycast.models.pay.MenuGroup;
import kr.co.paycast.utils.Util;

public class MenuGroupItem {

	private int id;
	private int siblingSeq;
	
	private String name;
	private String status;
	
	private boolean published;
	
	private List<MenuItem> menus = new ArrayList<MenuItem>();
	private List<MenuDispItem> dMenus = new ArrayList<MenuDispItem>();
	
	
	public MenuGroupItem() {}
	
	public MenuGroupItem(MenuGroup menuGroup) {
		
		if (menuGroup != null) {
			this.id = menuGroup.getId();
			this.siblingSeq = menuGroup.getSiblingSeq();
			this.name = menuGroup.getName();
			this.published = Util.isValid(menuGroup.getPublished()) && menuGroup.getPublished().equals("Y");
			this.status = "D".equals(menuGroup.getPublished())? "D":"";
		}
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSiblingSeq() {
		return siblingSeq;
	}

	public void setSiblingSeq(int siblingSeq) {
		this.siblingSeq = siblingSeq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public List<MenuItem> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuItem> menus) {
		this.menus = menus;
	}

	public List<MenuDispItem> getDMenus() {
		return dMenus;
	}

	public void setDMenus(List<MenuDispItem> dMenus) {
		this.dMenus = dMenus;
	}
	
}
