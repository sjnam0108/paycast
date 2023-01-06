package kr.co.paycast.models.pay.service;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.CouponPolicy;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StorePolicy;
import kr.co.paycast.models.store.StoreOrderCoupon;
import kr.co.paycast.models.store.StoreOrderPoint;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CouponPointService {
	
	// Common
	public void flush();
	
	//
	// for coupon Service
	//
	public StoreCoupon get(int id);
	public List<StoreCoupon> getCouponList(int storeId, int deleteState);
	public void saveOrUpdate(StoreCoupon coupon);
	
	// for Kendo Grid Remote Read
	public DataSourceResult getCouponRead(DataSourceRequest request);

	
	//
	// for policy Service
	//
	public StorePolicy getPolicy(int policyId);
	public List<StorePolicy> getPolicyList(int storeId);
	public List<StorePolicy> getPolicyList(int storeId, String type);

	public void saveOrUpdate(StorePolicy policy);

	
	//
	// for CouponPolicy Service
	//
	public CouponPolicy getCouponPolicy(int couponPolicyId);
	public List<CouponPolicy> getCouponPolicyList(int storeId);

	public void saveOrUpdate(CouponPolicy cp);

	//
	// for OrderCoupon Service
	//
	
	public void saveOrUpdate(StoreOrderCoupon compStamp);
	public List<StoreOrderCoupon> getCouponStamp(int storeId, String tel, String type);
	public List<StoreOrderCoupon> getIssueCouponRead(String storeId, String tel);
	public StoreOrderCoupon getCoupon(int couponId);
	
	//
	// for OrderPoint Service
	//
	public void saveOrUpdate(StoreOrderPoint savePoint);
	public List<StoreOrderPoint> getPointbyTel(int storeId, String tel);
	public List<StoreOrderPoint> getIssuePointRead(String storeId, String tel);




}
