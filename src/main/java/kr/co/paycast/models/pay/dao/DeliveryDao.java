package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StoreDeliveryPay;
import kr.co.paycast.models.pay.StoreDeliveryPolicy;
public interface DeliveryDao {

	
	public StoreDeliveryPay getDeliveryPay(int id);
	public List<StoreDeliveryPay> getDeliveryPayList(int storeId);
	public void saveOrUpdate(StoreDeliveryPay deliveryPay);
	
	public void deliveryPayDelete(StoreDeliveryPay deliveryPay);
	
	// for Kendo Grid Remote Read
	public DataSourceResult getDeliveryPayRead(DataSourceRequest request);
	
	//MinOrderPay
	
	public StoreDeliveryPolicy getDeliveryPolicy(int id);
	public List<StoreDeliveryPolicy> getDeliveryPolicyList(int storeId);
	public void saveOrUpdate(StoreDeliveryPolicy deliveryPolicy);
	
	// for Kendo Grid Remote Read
	public DataSourceResult getgetMinOrderPayRead(DataSourceRequest request);
	
}
