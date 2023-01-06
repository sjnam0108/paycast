package kr.co.paycast.viewmodels.store;

public class CookJsonList  {
	
	private String orderListID; //주문 목록 list ID
	private String productID; //상품 ID
	private String productName; //상품명
	private String orderCount; //해당 상품의 주문 수량
	private String orderMenuNotice; //해당 상품 알림 여부
	private String orderMenuPacking; //해당 상품 포장 여부
	
	public String getOrderListID() {
		return orderListID;
	}
	public void setOrderListID(String orderListID) {
		this.orderListID = orderListID;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}
	public String getOrderMenuNotice() {
		return orderMenuNotice;
	}
	public void setOrderMenuNotice(String orderMenuNotice) {
		this.orderMenuNotice = orderMenuNotice;
	}
	public String getOrderMenuPacking() {
		return orderMenuPacking;
	}
	public void setOrderMenuPacking(String orderMenuPacking) {
		this.orderMenuPacking = orderMenuPacking;
	}
	@Override
	public String toString() {
		return "CookJsonList [orderListID=" + orderListID + ", productID="
				+ productID + ", productName=" + productName + ", orderCount="
				+ orderCount + ", orderMenuNotice=" + orderMenuNotice
				+ ", orderMenuPacking=" + orderMenuPacking + "]";
	}
	
}
