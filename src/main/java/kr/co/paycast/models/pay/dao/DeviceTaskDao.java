package kr.co.paycast.models.pay.dao;

import java.util.List;

import kr.co.paycast.models.pay.DeviceTask;

public interface DeviceTaskDao {

	// Common
	public DeviceTask get(int id);
	public void saveOrUpdate(DeviceTask deviceTask);
	public void delete(DeviceTask deviceTask);
	public void delete(List<DeviceTask> deviceTasks);

	// for DeviceTask Specific
	public List<DeviceTask> getListByStoreId(int storeId);
	public List<DeviceTask> getListByDeviceId(int deviceId);
	public List<DeviceTask> getListByDeviceIdStatus(int deviceId, String status);
}
