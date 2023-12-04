package kr.co.paycast.viewmodels.pay;

import kr.co.paycast.models.pay.Menu;
import kr.co.paycast.models.pay.StoreEvent;
import kr.co.paycast.utils.Util;

public class MenuItem {

	private int id;
	private String code;
	private String event;
	private int siblingSeq;
	
	private String name;
	
	private boolean published;
	
	
	public MenuItem() {}
	
	public MenuItem(Menu menu) {
		
		if (menu != null) {
			this.id = menu.getId();
			this.code = menu.getCode();
			this.event = menu.getEvent();
			this.siblingSeq = menu.getSiblingSeq();
			this.name = menu.getName();
			this.published = Util.isValid(menu.getPublished()) && menu.getPublished().equals("Y");
		}
	}

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
}
