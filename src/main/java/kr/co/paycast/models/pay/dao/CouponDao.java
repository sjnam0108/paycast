package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.StoreCoupon;

public interface CouponDao {

	
	public StoreCoupon get(int id);
	public List<StoreCoupon> getList(int storeId, int deleteState);
	public void saveOrUpdate(StoreCoupon coupon);
	
	// for Kendo Grid Remote Read
	public DataSourceResult getRead(DataSourceRequest request);



}
