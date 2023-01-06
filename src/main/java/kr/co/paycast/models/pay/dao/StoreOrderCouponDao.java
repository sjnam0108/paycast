package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.store.StoreOrderCoupon;

public interface StoreOrderCouponDao {

	public void saveOrUpdate(StoreOrderCoupon compStamp);
	
	public List<StoreOrderCoupon> getCouponStamp(int storeId, String tel, String type);

	public List<StoreOrderCoupon> getIssueCouponRead(int storeId, String tel, String type);

	public StoreOrderCoupon getCoupon(int couponId);

}
