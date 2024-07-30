package kr.co.paycast.viewmodels.self;

public class SubOption {

	private String menuName;
	private String price;
	private String image;
	private String menuCode;
	private boolean soldOut;
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public boolean isSoldOut() {
		return soldOut;
	}
	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}
	
	
	
}
