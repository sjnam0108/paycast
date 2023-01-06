package kr.co.paycast.models.pay;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import kr.co.paycast.utils.Util;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="PAY_STORE_DELIVERY_POLICY")
public class StoreDeliveryPolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_store_delivery_poliy_gen")
	@SequenceGenerator(name = "store_store_delivery_poliy_gen", sequenceName = "PAY_STORE_DELIVERY_POLICY_SEQ")
	@Column(name = "STORE_DELIVERY_POLICY_ID")
	private int id;
	
	@Column(name = "MIN_ORDER_PRICE", nullable = false)
	private int minOderPeice ; 		// 최소 주문 가격 
	
	@Column(name = "DELIVERY_PRICE", nullable = false)
	private int deliveryPrice ; 
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private int whoLastUpdatedBy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;
	
	public StoreDeliveryPolicy() {}
	
	
	public StoreDeliveryPolicy(int minOderPeice, int deliveryPrice, Store store, HttpSession session) {
		this.minOderPeice =minOderPeice;
		this.deliveryPrice =deliveryPrice;
		this.store = store;
		
		touchWhoC(session);
	}
	
	private void touchWhoC(HttpSession session) {
		this.whoCreatedBy = Util.loginUserId(session);
		this.whoCreationDate = new Date();
		touchWho(session);
	}
	
	public void touchWho(HttpSession session) {
		this.whoLastUpdatedBy = Util.loginUserId(session);
		this.whoLastUpdateDate = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinOderPeice() {
		return minOderPeice;
	}


	public void setMinOderPeice(int minOderPeice) {
		this.minOderPeice = minOderPeice;
	}


	public int getDeliveryPrice() {
		return deliveryPrice;
	}


	public void setDeliveryPrice(int deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
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

	public int getWhoCreatedBy() {
		return whoCreatedBy;
	}

	public void setWhoCreatedBy(int whoCreatedBy) {
		this.whoCreatedBy = whoCreatedBy;
	}

	public int getWhoLastUpdatedBy() {
		return whoLastUpdatedBy;
	}

	public void setWhoLastUpdatedBy(int whoLastUpdatedBy) {
		this.whoLastUpdatedBy = whoLastUpdatedBy;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}


	@Override
	public String toString() {
		return String
				.format("StoreDeliveryPolicy [id=%s, minOderPeice=%s, deliveryPrice=%s, whoCreationDate=%s, whoLastUpdateDate=%s, whoCreatedBy=%s, whoLastUpdatedBy=%s]",
						id, minOderPeice, deliveryPrice, whoCreationDate,
						whoLastUpdateDate, whoCreatedBy, whoLastUpdatedBy);
	}
		
	
}
