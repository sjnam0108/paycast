package kr.co.paycast.models.pay.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.DeviceTask;
import kr.co.paycast.models.pay.dao.DeviceDao;
import kr.co.paycast.models.pay.dao.DeviceTaskDao;

@Transactional
@Service("devService")
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private DeviceDao deviceDao;
    
    @Autowired
    private DeviceTaskDao deviceTaskDao;
    
    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	
	@Override
	public Device getDevice(int id) {
		
		return deviceDao.get(id);
	}

	@Override
	public void saveOrUpdate(Device device) {
		
		deviceDao.saveOrUpdate(device);
	}

	@Override
	public void saveAndReorder(Device device, HttpSession httpSession) {
		
		deviceDao.saveAndReorder(device, httpSession);
	}

	@Override
	public void deleteDevice(Device device) {
		
		deviceDao.delete(device);
	}

	@Override
	public void deleteDevices(List<Device> devices) {
		
		deviceDao.delete(devices);
	}

	@Override
	public DataSourceResult getDeviceList(DataSourceRequest request) {
		
		return deviceDao.getList(request);
	}

	@Override
	public List<Device> getDeviceListByStoreId(int storeId) {
		
		return deviceDao.getListByStoreId(storeId);
	}

	@Override
	public Device getLastDeviceByStoreIdDeviceType(int storeId, String deviceType) {
		
		return deviceDao.getLastByStoreIdDeviceType(storeId, deviceType);
	}

	@Override
	public void reorderDevices(int storeId, String deviceType, HttpSession httpSession) {
		
		deviceDao.reorder(storeId, deviceType, httpSession);
	}

	@Override
	public Device getDeviceByUkid(String ukid) {
		
		return deviceDao.getByUkid(ukid);
	}

	@Override
	public Device getEffDeviceByUkid(String ukid) {
		
		return deviceDao.getEffByUkid(ukid);
	}

	@Override
	public Device getEffDeviceByUkidStoreShortName(String ukid, String shortName) {
		
		return deviceDao.getEffByUkidStoreShortName(ukid, shortName);
	}


	@Override
	public DeviceTask getDeviceTask(int id) {
		
		return deviceTaskDao.get(id);
	}


	@Override
	public void saveOrUpdate(DeviceTask deviceTask) {
		
		deviceTaskDao.saveOrUpdate(deviceTask);
	}


	@Override
	public void deleteDeviceTask(DeviceTask deviceTask) {
		
		deviceTaskDao.delete(deviceTask);
	}


	@Override
	public void deleteDeviceTasks(List<DeviceTask> deviceTasks) {
		
		deviceTaskDao.delete(deviceTasks);
	}


	@Override
	public List<DeviceTask> getDeviceTaskListByStoreId(int storeId) {
		
		return deviceTaskDao.getListByStoreId(storeId);
	}


	@Override
	public List<DeviceTask> getDeviceTaskListByDeviceId(int deviceId) {
		
		return deviceTaskDao.getListByDeviceId(deviceId);
	}


	@Override
	public List<DeviceTask> getDeviceTaskListByDeviceIdStatus(int deviceId, String status) {
		
		return deviceTaskDao.getListByDeviceIdStatus(deviceId, status);
	}

	
	@Override
	public int getMaxDeviceSeqByStoreIdDeviceType(int storeId, String deviceType) {
		
		Device device = getLastDeviceByStoreIdDeviceType(storeId, deviceType);
		
		return (device == null) ? 0 : device.getDeviceSeq();
	}

}
