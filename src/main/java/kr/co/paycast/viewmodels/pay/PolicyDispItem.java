package kr.co.paycast.viewmodels.pay;

import kr.co.paycast.models.pay.CouponPolicy;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StorePolicy;

public class PolicyDispItem {

	private int policyId = 0;			// 정책 ID
	private int couponPolicyId = 0;		// 쿠폰 정책 연결 ID
	private int couponId = 0;			// 쿠폰 ID
	private int orderAmt = 0;			// 스탬프정책의 주문 수량
	private int stamp = 0;				// "S" 스탬프정책의 스탬프 갯수 / "C" 쿠폰의 스탬프 갯수 
	private int siblingSeq = 0;			// 순서 
	private int pointId = 0;			// 포인트ID
	private int percentageNumber = 0;	// 포인트적립 정책 퍼센트 
	private int pointAmt = 0;			// 사용정책 포인트
	private String type;				// S: 스탬프 적립 정책, C: 쿠폰 발급 정책
	private int minOrderPrice = 0;		// 주문 최소금액
	private int deliveryPrice = 0;
	private int dispercentageNumber = 0;
	
	public PolicyDispItem() {}
	
	public PolicyDispItem(StorePolicy policy) {
		
		if (policy != null && "S".equals(policy.getType())) {
			this.policyId = policy.getId();
			this.couponPolicyId = 0;
			this.couponId = 0;
			this.orderAmt = policy.getOrderAmt();
			this.stamp = policy.getStamp();
			this.siblingSeq = policy.getSiblingSeq();
			this.type = policy.getType();
			this.pointId = 0;
			this.percentageNumber = 0;
			this.pointAmt = 0;
		}else if (policy != null && "P".equals(policy.getType())) {
			this.policyId = 0;
			this.couponPolicyId = 0;
			this.couponId = 0;
			this.orderAmt = 0;
			this.stamp = 0;
			this.siblingSeq = policy.getSiblingSeq();
			this.type = policy.getType();
			this.pointId = policy.getId();
			int per = (int)(policy.getPercentage()*100);
			this.percentageNumber = per;
			this.pointAmt = policy.getPoint();
		}
	}
	
	public PolicyDispItem(CouponPolicy couponPolicy) {
		if (couponPolicy != null) {
			StorePolicy policy = couponPolicy.getPolicy();
			StoreCoupon coupon = couponPolicy.getCoupon();
			
			if(policy != null){
				this.policyId = policy.getId();
				this.orderAmt = policy.getOrderAmt();
				this.stamp = policy.getStamp();
				this.siblingSeq = policy.getSiblingSeq();
				this.type = policy.getType();	
			}
			if(coupon != null){
				this.couponPolicyId = couponPolicy.getId();
				this.couponId = coupon.getId();	
			}
		}
	}

	public int getPolicyId() {
		return policyId;
	}

	public void setPolicyId(int policyId) {
		this.policyId = policyId;
	}

	public int getCouponPolicyId() {
		return couponPolicyId;
	}

	public void setCouponPolicyId(int couponPolicyId) {
		this.couponPolicyId = couponPolicyId;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
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

	public int getSiblingSeq() {
		return siblingSeq;
	}

	public void setSiblingSeq(int siblingSeq) {
		this.siblingSeq = siblingSeq;
	}

	public int getPointId() {
		return pointId;
	}

	public void setPointId(int pointId) {
		this.pointId = pointId;
	}

	public int getPercentageNumber() {
		return percentageNumber;
	}

	public void setPercentageNumber(int percentageNumber) {
		this.percentageNumber = percentageNumber;
	}

	public int getPointAmt() {
		return pointAmt;
	}

	public void setPointAmt(int pointAmt) {
		this.pointAmt = pointAmt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMinOrderPrice() {
		return minOrderPrice;
	}

	public void setMinOrderPrice(int minOrderPrice) {
		this.minOrderPrice = minOrderPrice;
	}

	public int getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(int deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}
	
	

	public int getDispercentageNumber() {
		return dispercentageNumber;
	}

	public void setDispercentageNumber(int dispercentageNumber) {
		this.dispercentageNumber = dispercentageNumber;
	}

	@Override
	public String toString() {
		return String
				.format("PolicyDispItem [policyId=%s, couponPolicyId=%s, couponId=%s, orderAmt=%s, stamp=%s, siblingSeq=%s, pointId=%s, percentageNumber=%s, pointAmt=%s, type=%s, minOrderPrice=%s, deliveryPrice=%s]",
						policyId, couponPolicyId, couponId, orderAmt, stamp,
						siblingSeq, pointId, percentageNumber, pointAmt, type,
						minOrderPrice, deliveryPrice);
	}
	
	
	
	
}
