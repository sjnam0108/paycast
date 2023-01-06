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
@Table(name="PAY_STORE_COUPON")
public class StoreCoupon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_coupon_seq_gen")
	@SequenceGenerator(name = "store_coupon_seq_gen", sequenceName = "PAY_STORE_COUPON_SEQ")
	@Column(name = "STORE_COUPON_ID")
	private int id;
	
	@Column(name = "NAME", nullable = false, length = 100)
	private String name = "";		// 쿠폰명

	@Column(name = "PRICE", nullable = false)
	private int price; 				// 쿠폰 가격

	@Column(name = "VALID_DATE", nullable = false, length = 3)
	private int validDate = 0; 			// 쿠폰 유효 기간 (개월)

	@Column(name = "DELETE_STATE", nullable = false, length = 2)
	private int deleteState = 0; 			// 쿠폰 삭제 여부   (0 : 미삭제  / 1 : 삭제(목록에 나오지 않음) )
	
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
	
	@OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<CouponPolicy> couponPolicys = new HashSet<CouponPolicy>(0);
	
	public StoreCoupon() {}
	
	public StoreCoupon(String name, int price, int validDate, Store store, HttpSession session) {
		this.name = name;
		this.price = price;
		this.validDate = validDate;
		this.store = store;
		this.deleteState = 0;
		
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getValidDate() {
		return validDate;
	}

	public void setValidDate(int validDate) {
		this.validDate = validDate;
	}
	
	public int getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(int deleteState) {
		this.deleteState = deleteState;
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

	@JsonIgnore
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
	@JsonIgnore
	public Set<CouponPolicy> getCouponPolicys() {
		return couponPolicys;
	}

	public void setCouponPolicys(Set<CouponPolicy> couponPolicys) {
		this.couponPolicys = couponPolicys;
	}

	@Override
	public String toString() {
		return String
				.format("StoreCoupon [id=%s, name=%s, price=%s, validDate=%s, deleteState=%s, whoCreationDate=%s, whoLastUpdateDate=%s, whoCreatedBy=%s, whoLastUpdatedBy=%s]",
						id, name, price, validDate, deleteState,
						whoCreationDate, whoLastUpdateDate, whoCreatedBy,
						whoLastUpdatedBy);
	}
	
}
