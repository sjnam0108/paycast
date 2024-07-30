package kr.co.paycast.viewmodels.self;

import java.util.Date;
import java.util.List;

import kr.co.paycast.viewmodels.pay.OptionalMenuItem;

public class MenuObject {

	private String menuCode;
	private String eventName;
	private String siblingSeq;
	private String menuName;
	private String price;
	private String image;
	private String intro;
	private String flagType;
	private String discountPrice;
	private String discountName;
	private String discount;
	private Date updateYN;
	private boolean soldOut;
	private List<MenuOptionData> optionalMenus;
	
	public boolean getSoldOut() {
		return soldOut;
	}
	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}
	public List<MenuOptionData> getOptionalMenus() {
		return optionalMenus;
	}
	public void setOptionalMenus(List<MenuOptionData> optionalMenus) {
		this.optionalMenus = optionalMenus;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
//	public String getMenuCode() {
//		return menuCode;
//	}
//	public void setMenuCode(String menuCode) {
//		this.menuCode = menuCode;
//	}
	public String getSiblingSeq() {
		return siblingSeq;
	}
	public void setSiblingSeq(String siblingSeq) {
		this.siblingSeq = siblingSeq;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getFlagType() {
		return flagType;
	}
	public void setFlagType(String flagType) {
		this.flagType = flagType;
	}
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getDiscountName() {
		return discountName;
	}
	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public Date getUpdateYN() {
		return updateYN;
	}
	public void setUpdateYN(Date updateYN) {
		this.updateYN = updateYN;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

}
