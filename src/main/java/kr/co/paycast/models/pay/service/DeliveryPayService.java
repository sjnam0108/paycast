package kr.co.paycast.models.pay.service;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.CouponPolicy;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StoreDeliveryPay;
import kr.co.paycast.models.pay.StoreDeliveryPolicy;
import kr.co.paycast.models.pay.StorePolicy;
import kr.co.paycast.models.store.StoreOrderCoupon;
import kr.co.paycast.models.store.StoreOrderPoint;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DeliveryPayService {
	
	// Common
	public void flush();
	
	//
	// for coupon Service
	//
	public StoreDeliveryPay get(int id);
	public List<StoreDeliveryPay> getDeliveryPayList(int storeId);
	public void saveOrUpdate(StoreDeliveryPay deliveryPay);
	public void deliveryPayDelete(StoreDeliveryPay deliveryPay);
	
	// for Kendo Grid Remote Read
	public DataSourceResult getDeliveryPayRead(DataSourceRequest request);
	
	
	
	public StoreDeliveryPolicy getDeliveryPolicy(int id);
	public List<StoreDeliveryPolicy> getDeliveryPolicyList(int storeId);
	public void saveOrUpdate(StoreDeliveryPolicy deliveryPolicy);
	
	// for Kendo Grid Remote Read
	public DataSourceResult getgetMinOrderPayRead(DataSourceRequest request);

}
