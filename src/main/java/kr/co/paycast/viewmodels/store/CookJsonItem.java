package kr.co.paycast.viewmodels.store;

import java.util.List;


public class CookJsonItem  {
	
	private String cookId; // 주방주문내역 ID
	private String orderMenuComplete; //주문 대기,완료 여부
	private String storeId; // 매장 ID
	private String storeOrderId; //메뉴 주문 ID 
	private String orderNumber; // 메뉴주문 번호
	private String orderSeq; //주문번호 (고객에게 보여주는 번호)
	private String menuCancelYN; // 취소된 메뉴인지 구분(Y : 취소대상, N : 결제 완료된 메뉴)
	
	private List<CookJsonList> orderMenuList; // 메뉴 목록

	public String getCookId() {
		return cookId;
	}

	public void setCookId(String cookId) {
		this.cookId = cookId;
	}

	public String getOrderMenuComplete() {
		return orderMenuComplete;
	}

	public void setOrderMenuComplete(String orderMenuComplete) {
		this.orderMenuComplete = orderMenuComplete;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreOrderId() {
		return storeOrderId;
	}

	public void setStoreOrderId(String storeOrderId) {
		this.storeOrderId = storeOrderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public List<CookJsonList> getOrderMenuList() {
		return orderMenuList;
	}

	public void setOrderMenuList(List<CookJsonList> orderMenuList) {
		this.orderMenuList = orderMenuList;
	}

	public String getMenuCancelYN() {
		return menuCancelYN;
	}

	public void setMenuCancelYN(String menuCancelYN) {
		this.menuCancelYN = menuCancelYN;
	}

	@Override
	public String toString() {
		return "CookJsonItem [cookId=" + cookId + ", orderMenuComplete="
				+ orderMenuComplete + ", storeId=" + storeId
				+ ", storeOrderId=" + storeOrderId + ", orderNumber="
				+ orderNumber + ", orderSeq=" + orderSeq + ", menuCancelYN="
				+ menuCancelYN + ", orderMenuList=" + orderMenuList + "]";
	}

}
