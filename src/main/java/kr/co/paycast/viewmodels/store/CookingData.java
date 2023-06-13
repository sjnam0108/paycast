package kr.co.paycast.viewmodels.store;

import java.util.Comparator;
import java.util.Date;

public class CookingData implements Comparable<CookingData> {
	
	private int orderNumber; //주문 목록 list ID
	private String menuName; //상품 ID
	private int count; //상품명
	private String cookingState; //해당 상품의 주문 수량
	private Date lastUpdateDate;
	
	public CookingData() {

	}
	
	public CookingData(int orderNumber, String menuName,int count,String cookingState, Date date) {
        this.orderNumber = orderNumber;
        this.menuName = menuName;
        this.count = count;
        this.cookingState = cookingState;
        this.lastUpdateDate = date;
    }


	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCookingState() {
		return cookingState;
	}
	public void setCookingState(String cookingState) {
		this.cookingState = cookingState;
	}

	
	
	@Override
	public String toString() {
		return "CookingData [orderNumber=" + orderNumber + ", menuName=" + menuName + ", count=" + count
				+ ", cookingState=" + cookingState + ", lastUpdateDate=" + lastUpdateDate + "]";
	}


//	public int compareTo(CookingData data) {
//		return this.lastUpdateDate.compareTo(data.lastUpdateDate);
//	}

	@Override
	public int compareTo(CookingData o) {
		// TODO Auto-generated method stub
		return this.lastUpdateDate.compareTo(o.lastUpdateDate);
	}

	
	
	
}
