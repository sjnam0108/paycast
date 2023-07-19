package kr.co.paycast.viewmodels.self;

import java.util.List;

public class MenuOptionData {
	
	private String menuOptName;
	private String menuGubun;
	private String menuOptSeq;
	private List<SubOption> optionList;
	
	public String getMenuOptName() {
		return menuOptName;
	}
	public void setMenuOptName(String menuOptName) {
		this.menuOptName = menuOptName;
	}
	public String getMenuGubun() {
		return menuGubun;
	}
	public void setMenuGubun(String menuGubun) {
		this.menuGubun = menuGubun;
	}
	public String getMenuOptSeq() {
		return menuOptSeq;
	}
	public void setMenuOptSeq(String menuOptSeq) {
		this.menuOptSeq = menuOptSeq;
	}
	public List<SubOption> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<SubOption> optionList) {
		this.optionList = optionList;
	}

	
}
