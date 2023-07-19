package kr.co.paycast.models.self.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.pay.Content;
import kr.co.paycast.models.pay.ContentFile;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.DeviceTask;
import kr.co.paycast.models.pay.Menu;
import kr.co.paycast.models.pay.MenuGroup;
import kr.co.paycast.models.pay.OptionalMenu;
import kr.co.paycast.models.pay.OptionalMenuList;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.UploadFile;
import kr.co.paycast.models.pay.service.ContentService;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.pay.service.MenuService;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.service.StoreOrderService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.store.MenuXmlItem;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("selfService")
public class SelfServiceImpl implements SelfService {
	private static final Logger logger = LoggerFactory.getLogger(SelfServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
    
	@Autowired
	private StoreOrderService storeOrderService;

    @Autowired 
    private DeviceService devService;

    @Autowired 
    private StoreService storeService;

    @Autowired 
    private ContentService ctntService;
	
	@Autowired 
    private PayService payService;
	
    @Autowired 
    private MenuService menuService;
	
	
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void makeMenuInfoXmlFile(String storeId, String deviceId, Document documentList) {
		List<MenuXmlItem> menuItemList = new ArrayList<MenuXmlItem>();
		
		// 매장에 대한 정보를 가져온다.
		Store store = storeService.getStore(Util.parseInt(storeId));
		if(store == null){
        	logger.error("makeStoreInfoXmlFile", "매장을 조회 할수 없습니다. ");
        	throw new ServerOperationForbiddenException("매장을 조회 할수 없습니다. ");
		}
		Device device = devService.getDeviceByUkid(deviceId);
		if(device == null){
        	logger.error("makeStoreInfoXmlFile", "기기를 조회 할수 없습니다. ");
        	throw new ServerOperationForbiddenException("기기를 조회 할수 없습니다. ");
		}
		// 상점 정보와 deviceID의 상점 정보가 같은 지 확인 하고 다를 경우 조회가 되지 않도록 한다.
		if(store.getId() != device.getStore().getId()){
        	logger.error("makeStoreInfoXmlFile [{}]", "상점 정보과 device 정보를 확인하여 주시기 바랍니다.");
        	throw new ServerOperationForbiddenException("기기를 조회 할수 없습니다. ");
		}
		

		Content lastCtnt = ctntService.getLastContentByStoreIdType(store.getId(), "M");
		if (lastCtnt != null) {
			List<ContentFile> list = ctntService.getContentFileListByContentIdDeviceId(lastCtnt.getId(), device.getId(), true);
			
			for(ContentFile contentFile : list){
				//------------------------------ 목록 만들어 주는 XML
				MenuXmlItem item = new MenuXmlItem(lastCtnt.getId() +"/" + contentFile.getFolderName(), contentFile.getFilename(), Long.toString(contentFile.getFileLength()));
				item.setStbfileid(String.valueOf(contentFile.getId()));
				menuItemList.add(item);
				//------------------------------ 목록 만들어 주는 XML
			}
		}
		
		Element rootList = documentList.addElement("Items");
		int itemSize = menuItemList.size();
		if(itemSize > 0){
			for(MenuXmlItem itemone : menuItemList){
				Element itemsElement = rootList.addElement("Item");
				itemsElement.addAttribute("foldername", itemone.getFoldername()); //foldername
				itemsElement.addAttribute("filename", itemone.getFilename()); //파일 명
				itemsElement.addAttribute("filelength", itemone.getFilelength()); //파일 크기
				itemsElement.addAttribute("stbfileid", itemone.getStbfileid()); //기기에서 파일 받았는지 확인하기 위해 사용되는 dplySchdCondFile ID 값
				itemsElement.addAttribute("playatonce", itemone.getPlayatonce());
				itemsElement.addAttribute("kfileid", itemone.getKfileid());
				itemsElement.addAttribute("kroot", itemone.getKroot());
			}
		}
	}
    
	@Override
	public void setMonTask(String page, String storeId, HttpSession session) {
		String pageCommand = "StoreInfoChg.bbmc";
		
		Store store = storeService.getStore(Util.parseInt(storeId));
		if(store == null){
        	logger.error("makeStoreInfoXmlFile", "매장을 조회 할수 없습니다. ");
        	throw new ServerOperationForbiddenException("매장을 조회 할수 없습니다. ");
		}
		
		if("M".equals(page.trim())){
			pageCommand = "MenuInfoChg.bbmc";
			// 내려줄 파일을 생성 한다. 
			makeMenuInfoFile(store, session);
		}
		// 키오스크 명령어 추가(주문 가능 여부 명령어 및 알림톡 사용 여부 )
		else if("KE".equals(page.trim())){
			pageCommand = "KioskEnabled.bbmc";
		}
		else if("A".equals(page.trim())){
			pageCommand = "BannerInfoChg.bbmc";
		}
		
		// if cmd == StoreInfoChg.bbmc -> K: 키오스크, P: 프린터  (2019.07.23 N: 알리미  제외 - kdk)
		// if cmd == MenuInfoChg.bbmc  -> K: 키오스크
		// if cmd == KioskEnabled.bbmc -> K: 키오스크
		// if cmd == BannerInfoChg.bbmc -> K: 키오스크
		
		List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
		for(Device device : deviceList) {
			boolean regRequired = false;
			if (pageCommand.equals("StoreInfoChg.bbmc")) {
				if (device.getDeviceType().equals("K") || device.getDeviceType().equals("P")) {
					regRequired = true;
				}
			} else if (pageCommand.equals("MenuInfoChg.bbmc") || pageCommand.equals("KioskEnabled.bbmc")|| pageCommand.equals("BannerInfoChg.bbmc")) {
				if (device.getDeviceType().equals("K")) {
					regRequired = true;
				}
			}

			if (regRequired) {
				devService.saveOrUpdate(new DeviceTask(store, device, pageCommand, session));
			}
		}
		
		//FCM 전송  : 키오스크 결제완료가 되어 DB에 저장 되었을 경우 fcm으로 stb 에 알려준다.
		storeOrderService.fcmTransmissionOne(store.getId());
	}
	
	private Content dplyScheduleSave(Store store, String selfKioskFilename, HttpSession session){
		
		Content content = new Content(store, "M", selfKioskFilename, session);
		
		ctntService.saveOrUpdate(content);
		
		return content;
	}
	
	private void dplySchdFileSave(Store store, Content content, List<MenuXmlItem> menuItemList, List<Device> deviceList, 
			HttpSession session){
		
		int itemSize = menuItemList.size();
		
		if (itemSize > 0 && deviceList.size() > 0) {
			for (Device device : deviceList) {
				// K: 키오스크, D: 주방용패드, N: 알리미, P: 프린터
				if (device.getDeviceType().equals("K") || device.getDeviceType().equals("P")) {
					for(MenuXmlItem itemone : menuItemList){
						
						ContentFile ctntFile = new ContentFile(content, device, itemone.getFoldername(), itemone.getFilename(),
								Util.parseLong(itemone.getFilelength()), session);
						
						ctntService.saveOrUpdate(ctntFile);
					}
				}
			}
		}
		
		// 작업이 완료된 후 "Y" 값으로 변경해야 한다.
		content.setStatusCode("Y");
		ctntService.saveOrUpdate(content);
	}
	
	private void makeMenuInfoFile(Store store, HttpSession session){
		
		List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
		if (deviceList.size() > 0) {
			String unit = "KRW";
			
	        //Kiosk 에 복사되는 파일은 따로 복사 
			String kioskTilteLoc = SolUtil.getPhysicalRoot("KioskTitle", store.getId());
	        String DepContent = SolUtil.getPhysicalRoot("DepContent", store.getId());
	        String MenuROOT = SolUtil.getPhysicalRoot("Menu", store.getId());
	        String contentImgUrl = "img/";
			
	        logger.info("kioskTilteLoc >> [{}]", kioskTilteLoc);
	        logger.info("DepContent >> [{}]", DepContent);
	        logger.info("MenuROOT >> [{}]", MenuROOT);
	        logger.info("contentImgUrl >> [{}]", contentImgUrl);
	        
			Document document = DocumentHelper.createDocument();
			
	        // 서버 저장 되는 이미지는 파일명을 변경
	        double randomValue = Math.random();
	        int randomNumber = (int) (randomValue * 9999) + 10000;
	        // .self 파일
	        String selfKioskFilename =  "S"+ store.getId() + randomNumber + ".self";
	        Content dplySch = dplyScheduleSave(store, selfKioskFilename, session);
	        
	        List<MenuXmlItem> menuItemList = new ArrayList<MenuXmlItem>();
			// 메뉴 그룹과 메뉴 목록을 가져온다.
	        List<MenuGroup> groupList = menuService.getMenuGroupListByStoreId(store.getId());
	        Collections.sort(groupList, MenuGroup.SiblingSeqComparator);
	        
			int groupSize = groupList.size();
			String delySchId = String.valueOf(dplySch.getId());
			String kioskImgLoc = File.separator + delySchId + File.separator + contentImgUrl;
			String kLogoImageFilename = "";
			String kLogoImageFileLen = "0";
			UploadFile kLogoImageFile = payService.getUploadFile(store.getStoreOpt().getKioskLogoImageId());
			
			if (kLogoImageFile != null) {
				kLogoImageFilename = kLogoImageFile.getFilename();
				kLogoImageFileLen = Long.toString(kLogoImageFile.getFileLength());
			}
			
			// 1. 파일 다운로드 될 XML을 생성한다.
			Element root = document.addElement("PayCast")
					.addAttribute("version", "1.0")
					.addAttribute("generated", new Date().toString());
			Element storeElement = root.addElement("Store").addAttribute("storeId", Integer.toString(store.getId()));
			
			if("I".equals(store.getStoreOpt().getKioskLogoType())){
				storeElement.addAttribute("storeimage", contentImgUrl + kLogoImageFilename); //매장 로고 이미지
			}else if("T".equals(store.getStoreOpt().getKioskLogoType())){
				storeElement.addAttribute("storename", store.getStoreOpt().getKioskLogoText()); //매장로고
			}else{
				storeElement.addAttribute("storeimage", ""); //매장 로고 이미지
			}
			storeElement.addAttribute("menutype", store.getStoreOpt().getMenuMatrix().replace("x", "_")); //매장 메뉴 타입(매트릭스)
			storeElement.addAttribute("background", ""); //배경이미지
			storeElement.addAttribute("catatorynum", Integer.toString(groupSize)); //메뉴 그룹 개수(카테고리로 구현)
			storeElement.addAttribute("unit", unit);
			
			// 기존 이미지를 kiosk에 맞게 파일을 복사
			String oriFile = "";
			String copyFileUrl = "";
			MenuXmlItem item = new MenuXmlItem();
			if(null != kLogoImageFilename&& !"".equals(kLogoImageFilename)){
				oriFile = SolUtil.getPhysicalRoot("KioskTitle", store.getId()) + File.separator + kLogoImageFilename;
				copyFileUrl = DepContent + kioskImgLoc ;
				Util.tempFileCopy(oriFile, copyFileUrl, kLogoImageFilename);
				
				//------------------------------ 목록 만들어 주는 XML
		        //로고 파일도 이미지 경로에
				item = new MenuXmlItem(contentImgUrl, kLogoImageFilename, kLogoImageFileLen);
				menuItemList.add(item);
				//------------------------------ 목록 만들어 주는 XML
			}
	        
			// XML 로 생성시 사용되는 seq는 DB데이터에 +1하여 기기에 보내준다. (웹은 seq가 0부터 시작하고 기기에 내려줄때는 1부터 시작)
			if(groupSize > 0){
				for(MenuGroup mg : groupList ){
					// 그룹에서 보여 주는 것만 키오스크에 내려준다. 
					if(!"N".equals(mg.getPublished())){
						Element groupElement = storeElement.addElement("Catagory");
						groupElement.addAttribute("seq", String.valueOf(mg.getSiblingSeq())); //그룹 순서
						groupElement.addAttribute("name", mg.getName()); //그룹 명
						
						List<Menu> list = menuService.getMenuListByStoreIdGroupId(mg.getStore().getId(), mg.getId());
						Collections.sort(list, Menu.SiblingSeqComparator);
						
						int menuListSize = list.size();
						groupElement.addAttribute("menunum", String.valueOf(menuListSize)); //메뉴 Size
						if(menuListSize > 0){
							for(Menu menu : list) {
								if(!"N".equals(menu.getPublished())){
								
									Element menuElement = groupElement.addElement("Menu");
									menuElement.addAttribute("id", String.valueOf(menu.getId())); //메뉴 ID
									menuElement.addAttribute("seq", String.valueOf(menu.getSiblingSeq())); //메뉴 순서
									menuElement.addAttribute("name", menu.getName()); //메뉴명
									
									float priceFl = menu.getPrice();
									if("KRW".equals(unit)){
										int priceint = (int)priceFl;
										menuElement.addAttribute("price", String.valueOf(priceint)); //메뉴 가격
									}else{
										menuElement.addAttribute("price", String.valueOf(priceFl)); //메뉴 가격
									}
									
									UploadFile menuImg = payService.getUploadFile(menu.getMenuImageId());
									if(menuImg != null){
										menuElement.addAttribute("file", contentImgUrl + menuImg.getFilename());
										oriFile = MenuROOT + File.separator + menuImg.getFilename();
										// 모바일에 생성된 경로에서 키오스크 다운로드 가능 경로로 변경
										copyFileUrl = DepContent + kioskImgLoc;
										Util.tempFileCopy(oriFile, copyFileUrl, menuImg.getFilename());
										//------------------------------ 목록 만들어 주는 XML
										// 이미지
										item = new MenuXmlItem(contentImgUrl, menuImg.getFilename(),Long.toString(menuImg.getFileLength()));
										menuItemList.add(item);
										//------------------------------ 목록 만들어 주는 XML	
									}
									
									menuElement.addAttribute("description", menu.getIntro());
									
									// (신메뉴 : N / 추천메뉴 : R / 리필가능메뉴 : I)
									String flagType = menu.getFlagType();
									
									String newGunbun = "";
									String popularGunbun = "";
									// 리필 : infinity(무제한) / limit(제한) / none(아님)
									// 2019.11.12 개발할 경우 제한에 대한 내요이 없으므로 무제한 아니면 none으로 처리
									String refillGunbun = "none";
									switch (flagType) {
										case "N":
											newGunbun = "true";
											break;
										case "R":
											popularGunbun = "true";
											break;
										case "I":
											refillGunbun = "infinity";
											break;
										default:
											break;
									}
									
									menuElement.addAttribute("newmenu", newGunbun); //신메뉴 여부
									menuElement.addAttribute("popular", popularGunbun); //인기 여부
									menuElement.addAttribute("refill ", refillGunbun); //리필 가능 여부
									
									//soldout
									if(menu.isSoldOut()){
										menuElement.addAttribute("soldout", "true"); //soldout
									}else{
										menuElement.addAttribute("soldout", ""); //soldout
									}
									
									if (menu != null) {
						        		List<OptionalMenu> optList = menuService.getOptionalMenuListByMenuId(menu.getId());
						        		if(optList.size() > 0){
							        		Collections.sort(optList,  OptionalMenu.SiblingSeqComparator);
							        		
											List<OptionalMenu> manMenus = new ArrayList<OptionalMenu>();
											List<OptionalMenu> optMenus = new ArrayList<OptionalMenu>();
											for(OptionalMenu optMenu : optList) {
												// M: 필수 메뉴, O: 추가 메뉴
												if (optMenu.getOptType().equals("M")) {
													manMenus.add(optMenu);
												} else if (optMenu.getOptType().equals("O")) {
													optMenus.add(optMenu);
												}
											}
											if(manMenus.size() > 0){
								        		for(OptionalMenu manMenu : manMenus) {
								        			Element selectElement = menuElement.addElement("optionMenu");
								        			selectElement.addAttribute("id", String.valueOf(manMenu.getId()));
													selectElement.addAttribute("name", manMenu.getName());
													selectElement.addAttribute("seq", String.valueOf(manMenu.getSiblingSeq()));
							        				selectElement.addAttribute("gubun", "0");// 0 : 필수 선택 메뉴 / 1 : 추가 선택 메뉴
							        				
													String[] menuList = manMenu.getMenuList().split(",");
													if(menuList.length > 0){
														List<OptionalMenuList> manMenuList = menuService.getOptionalMenuListByOptionId(manMenu.getId());
														for(int i=0; i < menuList.length; i++){
															logger.info("menuList[{}] >>> [{}]", i, menuList[i]);
															for(OptionalMenuList manMeList : manMenuList){
																String manMeListId = String.valueOf(manMeList.getId());
																
																if(menuList[i].equals(manMeListId)){
																	logger.info("optMeListId>>> [{}]", manMeListId);
																	String optMeListName = manMeList.getName();
																	String optMeListPrice = String.format("%.0f", manMeList.getPrice());
																	
																	Element optionElement = selectElement.addElement("option");
																	optionElement.addAttribute("id", manMeListId);
																	optionElement.addAttribute("name", optMeListName);
																	optionElement.addAttribute("price", optMeListPrice); 
																}
															}
														}
													}
								        		}
											}
											if(optMenus.size() > 0){
												for(OptionalMenu optMenu : optMenus) {
													Element selectElement = menuElement.addElement("optionMenu");
													selectElement.addAttribute("id", String.valueOf(optMenu.getId()));
													selectElement.addAttribute("name", optMenu.getName());
													selectElement.addAttribute("seq", String.valueOf(optMenu.getSiblingSeq()));
							        				selectElement.addAttribute("gubun", "1");// 0 : 필수 선택 메뉴 / 1 : 추가 선택 메뉴
							        				
													String[] menuList = optMenu.getMenuList().split(",");
													if(menuList.length > 0){
														List<OptionalMenuList> optMenuList = menuService.getOptionalMenuListByOptionId(optMenu.getId());
														for(int i=0; i < menuList.length; i++){
															logger.info("menuList[{}] >>> [{}]", i, menuList[i]);
															for(OptionalMenuList otpMeList : optMenuList){
																String optMeListId = String.valueOf(otpMeList.getId());
																
																if(menuList[i].equals(optMeListId)){
																	logger.info("optMeListId>>> [{}]", optMeListId);
																	String optMeListName = otpMeList.getName();
																	String optMeListPrice = String.format("%.0f", otpMeList.getPrice());
																	
																	Element optionElement = selectElement.addElement("option");
																	optionElement.addAttribute("id", optMeListId);
																	optionElement.addAttribute("name", optMeListName);
																	optionElement.addAttribute("price", optMeListPrice); 
																}
															}
														}
													}
												}
											}
						        		}
									}
								}
							}
						}
					}
				}
			}
			String prettyFormatedDoc = prettyFormat(document.asXML());
			
			stringToXmlFile(prettyFormatedDoc, DepContent + File.separator + delySchId + File.separator + selfKioskFilename);
			
			//------------------------------ 목록 만들어 주는 XML
	        //selforderKiosk.self 파일도 이미지 경로에
			File selforderKioskFile = new File(DepContent + File.separator + delySchId + File.separator + selfKioskFilename);
			item = new MenuXmlItem("Menu/", selfKioskFilename, Long.toString(selforderKioskFile.length()));
			item.setPlayatonce("Y");
			menuItemList.add(item);
			
			// Deploy를 하기 위해서 파일목록 및 키오스크 파일을 저장한다.
			dplySchdFileSave(store, dplySch, menuItemList, deviceList, session);
		}
	}
	
    private boolean stringToXmlFile(String xmlStr, String textFilename) {
        try {
        	File file = new File(textFilename);
        	File parentDir = file.getParentFile();
        	if (!parentDir.exists()) {
        		parentDir.mkdirs();
        	}
        	
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textFilename), StandardCharsets.UTF_8));

            out.write(xmlStr, 0, xmlStr.length());
            out.close();

            return true;
        } catch (IOException e) {
            logger.error("e ={}", e);
            throw new ServerOperationForbiddenException("WriteError [stringToXmlFile]");
        }
    }
    
    private String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }
    
    private String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            logger.error("e ={}", e);
            throw new ServerOperationForbiddenException("WriteError [prettyFormat]");
        }
    }
}
