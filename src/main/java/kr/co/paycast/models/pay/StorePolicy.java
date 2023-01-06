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
@Table(name="PAY_STORE_POLICY")
public class StorePolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_policy_seq_gen")
	@SequenceGenerator(name = "store_policy_seq_gen", sequenceName = "PAY_STORE_POLICY_SEQ")
	@Column(name = "STORE_POLICY_ID")
	private int id;
	
	@Column(name = "ORDERAMT", length = 3)
	private int orderAmt = 0; 				// 주문수량
	
	@Column(name = "STAMP", nullable = false, length = 3)
	private int stamp = 0; 					// 스탬프

	@Column(name = "TYPE", nullable = false, length = 1)
	private String type = "S";		// S: 스탬프 적립 정책, C: 쿠폰 발급 정책, P: 포인트 적립 및 사용 정책
	
	@Column(name = "PERCENTAGE", nullable = false)
	private double percentage = 0; 			// 포인트 적립 퍼센트
	
	@Column(name = "POINT", nullable = false, length = 3)
	private int point = 0; 			// 포인트 사용 정책
	
	@Column(name = "SIBLING_SEQ", nullable = false)
	private Integer siblingSeq;
	
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
	
	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<CouponPolicy> couponPolicys = new HashSet<CouponPolicy>(0);
	
	public StorePolicy() {}
	
	public StorePolicy(int orderAmt, int stamp, String type, int siblingSeq, Store store, HttpSession session) {
		if("P".equals(type)){
			this.orderAmt = 0;
			this.stamp = 0;
			if(orderAmt > 0){
				this.percentage = (double)orderAmt/100;
			}else{
				this.percentage = 0;
			}
			this.point = stamp;
		}else{
			this.orderAmt = orderAmt;
			this.stamp = stamp;
			this.percentage = 0;
			this.point = 0;
		}
		
		this.type = type;
		this.siblingSeq = siblingSeq;
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

	public int getOrderAmt() {
		return orderAmt;
	}

	public void setOrderAmt(int orderAmt) {
		this.orderAmt = orderAmt;
	}
	

	public int getStamp() {
		return stamp;
	}

	public void setStamp(int stamp) {
		this.stamp = stamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public Integer getSiblingSeq() {
		return siblingSeq;
	}

	public void setSiblingSeq(Integer siblingSeq) {
		this.siblingSeq = siblingSeq;
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
	
}
