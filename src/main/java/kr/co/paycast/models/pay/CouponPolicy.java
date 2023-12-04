package kr.co.paycast.models.pay;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import kr.co.paycast.utils.Util;


@Entity
@Table(name="PAY_COUPON_POLICY", uniqueConstraints = 
	@javax.persistence.UniqueConstraint(columnNames = {"STORE_COUPON_ID", "STORE_POLICY_ID"}))
public class CouponPolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "coupon_policy_seq_gen")
	@SequenceGenerator(name = "coupon_policy_seq_gen", sequenceName = "PAY_COUPON_POLICY_SEQ")
	@Column(name = "COUPON_POLICY_ID")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_COUPON_ID", nullable = false)
	private StoreCoupon coupon;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_POLICY_ID", nullable = false)
	private StorePolicy policy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATE_LOGIN", nullable = false)
	private int whoLastUpdateLogin;
	
	public CouponPolicy() {}
	
	
	public CouponPolicy(StoreCoupon coupon, StorePolicy policy, Store store) {
		this(coupon, policy, store, null);
	}
	
	public CouponPolicy(StoreCoupon coupon, StorePolicy policy, Store store, HttpSession session) {
		this.coupon = coupon;
		this.policy = policy;
		this.store = store;
		
		touchWhoC(session);
	}

	private void touchWhoC(HttpSession session) {
		this.whoCreatedBy = Util.loginUserId(session);
		this.whoCreationDate = new Date();
		this.whoLastUpdateLogin = Util.loginId(session);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public StoreCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(StoreCoupon coupon) {
		this.coupon = coupon;
	}

	public StorePolicy getPolicy() {
		return policy;
	}

	public void setPolicy(StorePolicy policy) {
		this.policy = policy;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Date getWhoCreationDate() {
		return whoCreationDate;
	}

	public void setWhoCreationDate(Date whoCreationDate) {
		this.whoCreationDate = whoCreationDate;
	}

	public int getWhoCreatedBy() {
		return whoCreatedBy;
	}

	public void setWhoCreatedBy(int whoCreatedBy) {
		this.whoCreatedBy = whoCreatedBy;
	}

	public int getWhoLastUpdateLogin() {
		return whoLastUpdateLogin;
	}

	public void setWhoLastUpdateLogin(int whoLastUpdateLogin) {
		this.whoLastUpdateLogin = whoLastUpdateLogin;
	}
	
}
