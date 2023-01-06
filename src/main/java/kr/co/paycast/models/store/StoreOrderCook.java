package kr.co.paycast.models.store;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import kr.co.paycast.utils.Util;

@Entity
@Table(name="STORE_ORDER_COOK")
public class StoreOrderCook {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "storeordercook_seq_gen")
	@SequenceGenerator(name = "storeordercook_seq_gen", sequenceName = "STORE_ORDER_COOK_SEQ")
	@Column(name = "ORDER_COOK_ID")
	private int id;

	@Column(name = "STORE_ID")
	private int storeId; //매장 번호
	
	@Column(name = "STORE_ORDER_ID")
	private int storeOrderId; //주문에 대한 ID (TABLE:STORE_ORDER 의 ID값)
	
	@Column(name = "ORDER_MENU_COMPLETE", length = 2)
	private String orderMenuComplete; //모든 메뉴 완료 여부 ( Y:메뉴가 완료된 상태 , N:메뉴가 대기/제조중 상태 )
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private String whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private String whoLastUpdatedBy;

	public StoreOrderCook() {}
	
	public StoreOrderCook(int storeId, int storeOrderId, String orderNumber) {
		this.storeId = storeId;
		this.storeOrderId = storeOrderId;
		this.orderMenuComplete = "N";
		
		touchWhoC(orderNumber);
	}

	public void touchWhoC(String orderNumber) {
		this.whoCreatedBy = orderNumber;
		this.whoCreationDate = new Date();
		touchWho(orderNumber);
	}
	
	public void touchWho(String orderNumber) {
		this.whoLastUpdatedBy = orderNumber;
		this.whoLastUpdateDate = new Date();
	}
	
	public void touchWho(HttpSession session) {
		this.whoLastUpdatedBy = String.valueOf(Util.loginUserId(session));
		this.whoLastUpdateDate = new Date();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getStoreOrderId() {
		return storeOrderId;
	}

	public void setStoreOrderId(int storeOrderId) {
		this.storeOrderId = storeOrderId;
	}

	public String getOrderMenuComplete() {
		return orderMenuComplete;
	}

	public void setOrderMenuComplete(String orderMenuComplete) {
		this.orderMenuComplete = orderMenuComplete;
	}

	public Date getWhoCreationDate() {
		return whoCreationDate;
	}

	public void setWhoCreationDate(Date whoCreationDate) {
		this.whoCreationDate = whoCreationDate;
	}

	public Date getWhoLastUpdateDate() {
		return whoLastUpdateDate;
	}

	public void setWhoLastUpdateDate(Date whoLastUpdateDate) {
		this.whoLastUpdateDate = whoLastUpdateDate;
	}

	public String getWhoCreatedBy() {
		return whoCreatedBy;
	}

	public void setWhoCreatedBy(String whoCreatedBy) {
		this.whoCreatedBy = whoCreatedBy;
	}

	public String getWhoLastUpdatedBy() {
		return whoLastUpdatedBy;
	}

	public void setWhoLastUpdatedBy(String whoLastUpdatedBy) {
		this.whoLastUpdatedBy = whoLastUpdatedBy;
	}

}
