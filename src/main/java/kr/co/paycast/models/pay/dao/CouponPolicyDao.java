package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.CouponPolicy;

public interface CouponPolicyDao {

	public void saveOrUpdate(CouponPolicy cp);
	
	public CouponPolicy getCouponPolicy(int couponPolicyId);

	public List<CouponPolicy> getCouponPolicyList(int storeId);


}
