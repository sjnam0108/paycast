package kr.co.paycast.viewmodels.pay;

import java.util.ArrayList;
import java.util.List;

import kr.co.paycast.models.pay.Menu;

public class MenuEditItem {

	private Menu menu;

	private List<OptionalMenuItem> mandatoryMenus;
	private List<OptionalMenuItem> optionalMenus;
	
	
	public MenuEditItem() {}
	
	public MenuEditItem(Menu menu) {
		
		this.menu = menu;
		
		mandatoryMenus = new ArrayList<OptionalMenuItem>();
		optionalMenus = new ArrayList<OptionalMenuItem>();
	}

	
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public List<OptionalMenuItem> getMandatoryMenus() {
		return mandatoryMenus;
	}

	public void setMandatoryMenus(List<OptionalMenuItem> mandatoryMenus) {
		this.mandatoryMenus = mandatoryMenus;
	}

	public List<OptionalMenuItem> getOptionalMenus() {
		return optionalMenus;
	}

	public void setOptionalMenus(List<OptionalMenuItem> optionalMenus) {
		this.optionalMenus = optionalMenus;
	}
}
