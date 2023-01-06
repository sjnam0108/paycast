package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.StorePolicy;


public interface PolicyDao {

	public void saveOrUpdate(StorePolicy policy);
	
	public StorePolicy getPolicy(int policyId);
	public List<StorePolicy> getPolicyList(int storeId);
	public List<StorePolicy> getPolicyList(int storeId, String type);

	

}
