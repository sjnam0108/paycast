package kr.co.paycast.viewmodels.pay;

import kr.co.paycast.models.pay.Store;
import kr.co.paycast.utils.Util;

public class StoreItem {

	private String shortName;
	private String storeName;
	private String creationDate;
	
	private int storeId;
	
	
	public StoreItem(int storeId, String shortName, String storeName, String creationDate) {
		
		this.storeId = storeId;
		this.shortName = shortName;
		this.storeName = storeName;
		this.creationDate = creationDate;
	}
	
	public StoreItem(Store store) {
		
		this.storeId = store.getId();
		this.shortName = store.getShortName();
		this.storeName = store.getStoreName();
		this.creationDate = Util.toSimpleString(store.getWhoCreationDate(), "yyyy-MM-dd");
	}

	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
}
