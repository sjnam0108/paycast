package kr.co.paycast.viewmodels.self;

import java.util.List;

public class CategoryObject {

	private String seq;
	private String name;
	private String image;
	private String menuNum;
	
	private List<MenuObject> menuObjectList;
	
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getMenuNum() {
		return menuNum;
	}
	public void setMenuNum(String menuNum) {
		this.menuNum = menuNum;
	}
	public List<MenuObject> getMenuObjectList() {
		return menuObjectList;
	}
	public void setMenuObjectList(List<MenuObject> menuObjectList) {
		this.menuObjectList = menuObjectList;
	}


	
	
	
}
