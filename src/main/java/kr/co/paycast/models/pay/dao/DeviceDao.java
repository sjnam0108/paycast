package kr.co.paycast.models.pay.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.Device;

public interface DeviceDao {
	
	// Common
	public Device get(int id);
	public void saveOrUpdate(Device device);
	public void saveAndReorder(Device device, HttpSession httpSession);
	public void delete(Device device);
	public void delete(List<Device> devices);

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for Device specific
	public List<Device> getListByStoreId(int storeId);
	public List<Device> getListByStoreIdDeviceType(int storeId, String deviceType);
	public Device getLastByStoreIdDeviceType(int storeId, String deviceType);
	public void reorder(int storeId, String deviceType, HttpSession httpSession);
	public Device getByUkid(String ukid);
	public Device getEffByUkid(String ukid);
	public Device getEffByUkidStoreShortName(String ukid, String shortName);
}
