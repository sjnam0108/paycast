package kr.co.paycast.viewmodels.self;

import java.util.List;

public class MenuObject {

	private String productId;
	private String seq;
	private String name;
	private String price;
	private String imageFile;
	private String description;
	private String popular;
	private String newMenu;
	private boolean soldOut;
	private List<MenuOptionData> optMenusList;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
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
	public String getImageFile() {
		return imageFile;
	}
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPopular() {
		return popular;
	}
	public void setPopular(String popular) {
		this.popular = popular;
	}
	public String getNewMenu() {
		return newMenu;
	}
	public void setNewMenu(String newMenu) {
		this.newMenu = newMenu;
	}
	public boolean getSoldOut() {
		return soldOut;
	}
	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}
	public List<MenuOptionData> getOptMenusList() {
		return optMenusList;
	}
	public void setOptMenusList(List<MenuOptionData> optMenusList) {
		this.optMenusList = optMenusList;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	
	
	
}
