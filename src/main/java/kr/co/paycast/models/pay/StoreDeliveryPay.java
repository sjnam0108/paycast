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
@Table(name="PAY_STORE_DELIVERY_PAY")
public class StoreDeliveryPay {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_delivery_pay_seq_gen")
	@SequenceGenerator(name = "store_delivery_pay_seq_gen", sequenceName = "PAY_STORE_DELIVERY_PAY_SEQ")
	@Column(name = "STORE_DELIVERY_PAY_ID")
	private int id;
	
	@Column(name = "FromOderPrice", nullable = false)
	private int fromOrderPrice; 				// 최소 주문금액
	
	@Column(name = "ToOderPrice", nullable = false)
	private int toOrderPrice; 
	
	@Column(name = "DeliveryPrice", nullable = false)
	private int deliveryPrice ; 				// 배달료
	
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
	
	public StoreDeliveryPay() {}
	
	
	public StoreDeliveryPay(int fromOderPrice, int toOderPrice, int deliveryPrice, Store store, HttpSession session) {
		this.fromOrderPrice =fromOderPrice;
		this.toOrderPrice = toOderPrice;
		this.deliveryPrice = deliveryPrice;
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

	public int getFromOrderPrice() {
		return fromOrderPrice;
	}

	public void setFromOrderPrice(int fromOderPrice) {
		this.fromOrderPrice = fromOderPrice;
	}

	public int getToOrderPrice() {
		return toOrderPrice;
	}

	public void setToOrderPrice(int toOderPrice) {
		this.toOrderPrice = toOderPrice;
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
				.format("StoreDeliveryPay [id=%s, fromOderPrice=%s, toOderPrice=%s, deliveryPrice=%s, whoCreationDate=%s, whoLastUpdateDate=%s, whoCreatedBy=%s, whoLastUpdatedBy=%s]",
						id, fromOrderPrice, toOrderPrice, deliveryPrice,
						whoCreationDate, whoLastUpdateDate, whoCreatedBy,
						whoLastUpdatedBy);
	}

	
}
