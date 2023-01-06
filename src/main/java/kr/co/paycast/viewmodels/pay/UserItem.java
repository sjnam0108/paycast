package kr.co.paycast.viewmodels.pay;

import java.util.Date;

import kr.co.paycast.models.fnd.User;
import kr.co.paycast.utils.Util;


public class UserItem {
	
	private int id;
	private int storeUserId;
	
	private String username;
	private String familiarName;
	
	private boolean effectiveUser;

	private Date effectiveStartDate;
	private Date effectiveEndDate;
	private Date lastLoginDate;
	
	
	public UserItem(User user, boolean effectiveUser) {
		
		this.id = user.getId();
		this.username = user.getUsername();
		this.familiarName = user.getFamiliarName();
		
		this.effectiveStartDate = user.getEffectiveStartDate();
		this.effectiveEndDate = user.getEffectiveEndDate();
		
		this.effectiveUser = effectiveUser;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFamiliarName() {
		return familiarName;
	}

	public void setFamiliarName(String familiarName) {
		this.familiarName = familiarName;
	}

	public boolean isEffectiveUser() {
		return effectiveUser;
	}

	public void setEffectiveUser(boolean effectiveUser) {
		this.effectiveUser = effectiveUser;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	
	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public int getStoreUserId() {
		return storeUserId;
	}

	public void setStoreUserId(int storeUserId) {
		this.storeUserId = storeUserId;
	}


	public String getDiffTime() {
		if (lastLoginDate != null) {
			return Util.getEngTimespan(Util.getDiffTimespanArr(new Date(), lastLoginDate));
		}
		
		return "";
	}
}
