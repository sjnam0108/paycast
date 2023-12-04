package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StoreEvent;

public interface CouponDao {

	
	public StoreCoupon get(int id);
	public StoreEvent getId(int id);
	public List<StoreCoupon> getList(int storeId, int deleteState);
	public List<StoreEvent> getEventList(int storeId);
	public void saveOrUpdate(StoreCoupon coupon);
	public void saveOrUpdate(StoreEvent event);
	public void delete(StoreEvent event);
	
	// for Kendo Grid Remote Read
	public DataSourceResult getRead(DataSourceRequest request);
	public DataSourceResult getEventRead(DataSourceRequest request);



}
