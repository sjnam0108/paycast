package kr.co.paycast.models.store.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.calc.StoreCalcDay;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.DeviceTask;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.utils.Util;

@Transactional
@Service("StoreSiteService")
public class StoreSiteServiceImpl implements StoreSiteService {
	private static final Logger logger = LoggerFactory.getLogger(StoreSiteServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired 
    private StoreService storeService;

    @Autowired 
    private DeviceService devService;

    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	// 매장 ID로 키오스크에 정보 업데이트 하는 XML 생성
	// 2019.05.27
	// 매장 정보 변경에 대한 내용은 XML 형식으로 내려주던 방식에서 json으로 내려주는 방식으로 변경되었으며 해당 URL은 사용하지 않는다.  - [kdk]
	// 2019.05.28
	// 해당 내용으로 다시 사용 / 명령어에 넣어 Json 방식으로 내려주는건 PayCast v1.3 적용
	@Override
	public void makeStoreInfoXmlFile(String storeId, Document document) {
		
		Store store = storeService.getStore(Util.parseInt(storeId));
		if(store == null){
        	logger.error("makeStoreInfoXmlFile", "매장을 조회 할수 없습니다. ");
        	throw new ServerOperationForbiddenException("매장을 조회 할수 없습니다. ");
		}
		
		Element root = document.addElement("Signcast")
						.addAttribute("version", "1")
						.addAttribute("generated", new Date().toString());
          
		Element storeElement = root.addElement("Server");
		storeElement.addAttribute("storeId", Integer.toString(store.getId()));//매장번호
		storeElement.addAttribute("storeName", Util.returnValueOrBlankIfNull(store.getBizName())); //상호명
		storeElement.addAttribute("storeAddr", Util.returnValueOrBlankIfNull(store.getAddress()));//매장주소
		storeElement.addAttribute("businessNum", Util.returnValueOrBlankIfNull(store.getBizNum()));//사업자번호
		storeElement.addAttribute("storeTel", Util.returnValueOrBlankIfNull(store.getPhone()));//매장전화번호
		storeElement.addAttribute("merchantNum", ""); //가맹점 번호
		storeElement.addAttribute("represent", Util.returnValueOrBlankIfNull(store.getBizRep())); //대표자명
		storeElement.addAttribute("operatingTime", Util.returnValueOrBlankIfNull(store.getOpenHours())); //운영시간
		storeElement.addAttribute("storeIntroduction", "");//매장소개
	}

	@Override
	public Document getMonListByStoreId(String storeId, String deviceId, Document document, HttpSession session) {
		
		Store store = storeService.getStore(Util.parseInt(storeId));
		if(store == null){
        	logger.error("getMonListByStoreId [{}]", "매장을 조회 할수 없습니다. ");
        	throw new ServerOperationForbiddenException("매장을 조회 할수 없습니다. ");
		}
		
		Device device = devService.getDeviceByUkid(Util.parseString(deviceId));
		if (device == null) {
        	logger.error("getMonListByStoreId [{}]", "기기를 조회 할수 없습니다. ");
        	throw new ServerOperationForbiddenException("기기를 조회 할수 없습니다. ");
		}
		
		// 상점 정보와 deviceID의 상점 정보가 같은 지 확인 하고 다를 경우 조회가 되지 않도록 한다.
		if(store.getId() != device.getStore().getId()){
        	logger.error("getMonListByStoreId [{}]", "상점 정보과 device 정보를 확인하여 주시기 바랍니다.");
        	throw new ServerOperationForbiddenException("기기를 조회 할수 없습니다. ");
		}
		
		List<DeviceTask> list = devService.getDeviceTaskListByDeviceIdStatus(device.getId(), "R");
		
		Date now = new Date();
		
		Element root = document.addElement("data");
		Element commandsElement = root.addElement("commands");
		if(list.size() > 0){
			 HashMap<String, Object> deviceTaskMap = new HashMap<String, Object>();
			 for(DeviceTask deviceTask : list){
				 if (now.after(deviceTask.getDestDate()) && now.before(deviceTask.getCancelDate())){
					 logger.info("겹치는 명령어 바꾸기  deviceTask.getCommand() >>> [{}] , rcCommandId[{}]", deviceTask.getCommand(), deviceTask.getId());
					 if(deviceTaskMap.containsKey(deviceTask.getCommand())){
						 DeviceTask one = (DeviceTask)deviceTaskMap.get(deviceTask.getCommand());
						 one.setStatus("C");
						 one.touchWho(session);
						 logger.info("contains device Command Cancel device.getId() >>> [{}] , device.getCommand()[{}]", one.getId(), one.getCommand());
						 devService.saveOrUpdate(device);
						 // 같은 것이 있을 경우 업데이트
						 deviceTaskMap.put(deviceTask.getCommand(), deviceTask);
					 }else{
						 deviceTaskMap.put(deviceTask.getCommand(), deviceTask);
					 }
				 }
			 }
        	Iterator<String> keys = deviceTaskMap.keySet().iterator(); 
        	while( keys.hasNext() ){
        		String key = keys.next(); 
        		DeviceTask deviceTask = (DeviceTask)deviceTaskMap.get(key);
				Element commandElement = commandsElement.addElement("command");
				commandElement.addAttribute("rcCommandId", Integer.toString(deviceTask.getId()) );
				commandElement.addAttribute("command", deviceTask.getCommand() );
				commandElement.addAttribute("execTime", "");
				logger.info("-----------------------------------------------------------------------");
				logger.info("deviceId >>> [{}] , rcCommandId[{}]", deviceId, deviceTask.getId());
				logger.info("rcCommandId >>> [{}] , deviceTask.getCommand()[{}]", deviceTask.getId(), deviceTask.getCommand());
				if("KioskEnabled.bbmc".equals(deviceTask.getCommand())){
					logger.info("deviceId >>> [{}] , KioskEnabled.bbmc >>> getOpenType[{}]", deviceId, store.getOpenType());
					logger.info("deviceId >>> [{}] , KioskEnabled.bbmc >>> isKioskOrderEnabled[{}]", deviceId, store.isKioskOrderEnabled());
					logger.info("deviceId >>> [{}] , KioskEnabled.bbmc >>> isAlimTalkAllowed[{}]", deviceId, store.isAlimTalkAllowed());
					commandElement.addAttribute("openType", store.getOpenType());
					commandElement.addAttribute("koEnabled", String.valueOf(store.isKioskOrderEnabled()));
					commandElement.addAttribute("atEnabled", String.valueOf(store.isAlimTalkAllowed()));
				}
				String dataJson = "";
				commandElement.addCDATA(dataJson);
				logger.info("-----------------------------------------------------------------------");
        	}
		}
		
		return document;
	}
	
	@Override
	public boolean setMonTaskCommandUpdate(String rcCmdId, String result, HttpSession session) {
		boolean success = false;
		
		logger.info("setMonTaskCommandUpdate >> rcCmdId  >>> [{}], result  >>> [{}]", rcCmdId, result);
		
		try {
			DeviceTask deviceTask = devService.getDeviceTask(Util.parseInt(rcCmdId));
			logger.info("deviceTask.getDevice().getDeviceSeq() [{}]", deviceTask.getDevice().getDeviceSeq());
			
			deviceTask.setStatus(result);
			deviceTask.touchWho(session);
			devService.saveOrUpdate(deviceTask);
			
			success = true;
		} catch (Exception e) {
			logger.error("setMonTaskCommandUpdate 결과 저장 실패 >> rcCmdId  >>> [{}], result  >>> [{}]", rcCmdId, result);
		}
		
		return success;
	}
}
