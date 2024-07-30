package kr.co.paycast.viewmodels.self;

import java.util.List;

public class CategoryObject {

	private String siblingSeq;
	private String groupName;
	private String published;
	
	private List<MenuObject> menus;
	
	public List<MenuObject> getMenus() {
		return menus;
	}
	public void setMenus(List<MenuObject> menus) {
		this.menus = menus;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getSiblingSeq() {
		return siblingSeq;
	}
	public void setSiblingSeq(String siblingSeq) {
		this.siblingSeq = siblingSeq;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	


	
	
	
}
