package kr.co.paycast.viewmodels.self;

import java.util.List;

public class MenuOptionData {
	
	private String menuName;
	private String optType;
	private String menuOptSeq;
	private List<SubOption> optionalList;
	
	public String getMenuOptSeq() {
		return menuOptSeq;
	}
	public void setMenuOptSeq(String menuOptSeq) {
		this.menuOptSeq = menuOptSeq;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public List<SubOption> getOptionalList() {
		return optionalList;
	}
	public void setOptionalList(List<SubOption> optionalList) {
		this.optionalList = optionalList;
	}

}
