package kr.co.paycast.viewmodels.calc;

import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderList;

public class CalcItem {
	
	private StoreOrder storeOrder;
	private StoreOrderList storeOrderList;
	private String orderNumber;
	private String compSelect; // 메뉴 ID + 옵션 ID로 구성된 값
	
	public CalcItem(StoreOrder storeOrder, StoreOrderList storeOrderList, String orderNumber, String compSelect) {
		this.storeOrder = storeOrder;
		this.storeOrderList = storeOrderList;
		this.orderNumber = orderNumber;
		this.compSelect = compSelect;
	}
	
	public StoreOrder getStoreOrder() {
		return storeOrder;
	}
	
	public void setStoreOrder(StoreOrder storeOrder) {
		this.storeOrder = storeOrder;
	}
	
	public StoreOrderList getStoreOrderList() {
		return storeOrderList;
	}
	
	public void setStoreOrderList(StoreOrderList storeOrderList) {
		this.storeOrderList = storeOrderList;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCompSelect() {
		return compSelect;
	}

	public void setCompSelect(String compSelect) {
		this.compSelect = compSelect;
	}

}
