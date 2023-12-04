package kr.co.paycast.viewmodels.store;

import java.util.Date;

public class MenuListJsonPrintItem  {
	
	private String productID; //상품 ID
	private String productCode; //상품 ID
	private String productName; //상품명
	private String orderCount; //해당 상품의 주문 수량
	private String orderPrice; //해당 상품의 금액
	private String orderMenuPacking; //해당 상품의 포장 여부
	private String ess; //필수 선택 
	private String add; //추가 선택
	
	public MenuListJsonPrintItem() {

	}
	
	public MenuListJsonPrintItem(String productID,String productCode, String productName,String orderCount,String orderPrice, String orderMenuPacking) {
        this.productID = productID;
        this.productName = productName;
        this.orderCount = orderCount;
        this.orderPrice = orderPrice;
        this.orderMenuPacking = orderMenuPacking;
    }
	
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
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
	
	public String getOrderPrice() {
		return orderPrice;
	}
	
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	public String getOrderMenuPacking() {
		return orderMenuPacking;
	}
	
	public void setOrderMenuPacking(String orderMenuPacking) {
		this.orderMenuPacking = orderMenuPacking;
	}

	public String getEss() {
		return ess;
	}

	public void setEss(String ess) {
		this.ess = ess;
	}

	public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	@Override
	public String toString() {
		return String
				.format("MenuListJsonPrintItem [productID=%s, productName=%s, orderCount=%s, orderPrice=%s, orderMenuPacking=%s, ess=%s, add=%s]",
						productID, productName, orderCount, orderPrice,
						orderMenuPacking, ess, add);
	}
	
	
	
}
