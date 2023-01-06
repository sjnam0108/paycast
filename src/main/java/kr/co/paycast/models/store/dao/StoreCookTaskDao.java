package kr.co.paycast.models.store.dao;

import java.util.List;

import kr.co.paycast.models.store.StoreCookTask;

public interface StoreCookTaskDao {

	public void saveOrUpdate(StoreCookTask storeCookTask);
	
	public List<StoreCookTask> getTaskListByStoreIdDeviceId(int storeId, String deviceId, String status);

	public StoreCookTask getStoreCookTask(int taskId);

}
