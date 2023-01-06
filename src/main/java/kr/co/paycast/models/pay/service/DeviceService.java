package kr.co.paycast.models.pay.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.DeviceTask;

@Transactional
public interface DeviceService {
	
	// Common
	public void flush();
	
	
	//
	// for Device Dao
	//
	// Common
	public Device getDevice(int id);
	public void saveOrUpdate(Device device);
	public void saveAndReorder(Device device, HttpSession httpSession);
	public void deleteDevice(Device device);
	public void deleteDevices(List<Device> devices);

	// for Kendo Grid Remote Read
	public DataSourceResult getDeviceList(DataSourceRequest request);

	// for Device specific
	public List<Device> getDeviceListByStoreId(int storeId);
	public Device getLastDeviceByStoreIdDeviceType(int storeId, String deviceType);
	public void reorderDevices(int storeId, String deviceType, HttpSession httpSession);
	public Device getDeviceByUkid(String ukid);
	public Device getEffDeviceByUkid(String ukid);
	public Device getEffDeviceByUkidStoreShortName(String ukid, String shortName);
	
	
	//
	// for DeviceTask Dao
	//
	// Common
	public DeviceTask getDeviceTask(int id);
	public void saveOrUpdate(DeviceTask deviceTask);
	public void deleteDeviceTask(DeviceTask deviceTask);
	public void deleteDeviceTasks(List<DeviceTask> deviceTasks);

	// for DeviceTask Specific
	public List<DeviceTask> getDeviceTaskListByStoreId(int storeId);
	public List<DeviceTask> getDeviceTaskListByDeviceId(int deviceId);
	public List<DeviceTask> getDeviceTaskListByDeviceIdStatus(int deviceId, String status);
	
	
	//
	// for Common
	//
	public int getMaxDeviceSeqByStoreIdDeviceType(int storeId, String deviceType);
}
