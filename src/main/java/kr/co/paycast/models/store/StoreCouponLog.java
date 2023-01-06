package kr.co.paycast.models.store;

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

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.utils.Util;

@Entity
@Table(name="STORE_COUPON_LOG")
public class StoreCouponLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "coupon_log_seq_gen")
	@SequenceGenerator(name = "coupon_log_seq_gen", sequenceName = "STORE_COUPON_LOG_SEQ")
	@Column(name = "ORDER_COUPONLOG_ID")
	private int id;
	
	@Column(name = "TEL", length = 20)
	private String tel = "";		// 전화 번호 - 스탬프 및 쿠폰을 조회 하는 전체 값
	
	@Column(name = "TYPE", nullable = false, length = 1)
	private String type = "S";		// S: 스탬프, C: 쿠폰

	@Column(name = "STAMP_COUNT", nullable = false)
	private int stampCnt = 0; 		// 적립 스탬프  / 쿠폰일 경우 사용된 스탬프 수
	
	@Column(name = "STAMP_TOTAL", nullable = false)
	private int stampTotal = 0; 	// 적립 스탬프  총 갯수 (적립 스탬프 포함)
	
	@Column(name = "DELETE_STATE", nullable = false, length = 1)
	private int useState = 0; 		// 쿠폰 등록 / 사용여부  (0 : 쿠폰 등록  / 1 : 쿠폰 사용 완료  )
	
	@Column(name = "END_DATE")
	private Date endDate = new Date();			// 쿠폰 사용 마지막날
	
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_COUPON_ID")
	private StoreCoupon coupon;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORDER_COUPON_ID")
	private StoreOrderCoupon orderCoupon;
	
	public StoreCouponLog() {}
	
	public StoreCouponLog(String tel, String type, int stampCnt, int stampTotal, int useState, Date endDate, 
			Store store, StoreCoupon coupon, StoreOrderCoupon orderCoupon) {
		
		this(tel, type, stampCnt, stampTotal, useState, endDate, store, coupon, orderCoupon, null);
	}
	
	public StoreCouponLog(String tel, String type, int stampCnt, int stampTotal, int useState, Date endDate, 
			Store store, StoreCoupon coupon, StoreOrderCoupon orderCoupon, HttpSession session) {
		this.tel = tel;
		this.type = type;
		this.stampCnt = stampCnt;
		this.stampTotal = stampTotal;
		this.useState = useState;
		this.endDate = endDate;
		this.store = store;
		this.coupon = coupon;
		this.orderCoupon = orderCoupon;
		
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStampCnt() {
		return stampCnt;
	}

	public void setStampCnt(int stampCnt) {
		this.stampCnt = stampCnt;
	}

	public int getStampTotal() {
		return stampTotal;
	}

	public void setStampTotal(int stampTotal) {
		this.stampTotal = stampTotal;
	}

	public int getUseState() {
		return useState;
	}

	public void setUseState(int useState) {
		this.useState = useState;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	public StoreCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(StoreCoupon coupon) {
		this.coupon = coupon;
	}

	@JsonIgnore
	public StoreOrderCoupon getOrderCoupon() {
		return orderCoupon;
	}

	public void setOrderCoupon(StoreOrderCoupon orderCoupon) {
		this.orderCoupon = orderCoupon;
	}
	
}
