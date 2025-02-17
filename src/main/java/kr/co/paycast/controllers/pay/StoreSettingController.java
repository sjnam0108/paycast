package kr.co.paycast.controllers.pay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.pay.Ad;
import kr.co.paycast.models.pay.PayUploadTransModel;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreOpt;
import kr.co.paycast.models.pay.UploadFile;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.self.service.SelfService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;

/**
 * 상점 관리 - 설정 컨트롤러
 */
@Controller("pay-store-setting-controller")
@RequestMapping(value="/pay/storesetting")
public class StoreSettingController {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreSettingController.class);
	
	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired
	private ModelManager modelMgr;

    @Autowired 
    private StoreService storeService;

    @Autowired 
    private PayService payService;
    
    @Autowired
    private SelfService selfService;

	
	/**
	 * 상점 관리 - 설정 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);
    	
    	solMsgMgr.checkStoreSelectionMessage(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "storesetting.title"),
					
					new Message("tab_kiosk", "storesetting.kiosk"),
					new Message("tab_mobileOrder", "storesetting.mobileOrder"),
					
					new Message("label_image", "storesetting.image"),
					new Message("label_text", "storesetting.text"),
					new Message("tip_select", "storesetting.select"),
					new Message("tip_selected", "storesetting.selected"),
					
					new Message("title_kioskLogo", "storesetting.kioskLogo.title"),
					new Message("desc_kioskLogo", "storesetting.kioskLogo.desc"),
					
					new Message("title_menuMatrix", "storesetting.menuMatrix.title"),
					new Message("desc_menuMatrix", "storesetting.menuMatrix.desc"),
					
					new Message("title_mobileLogo", "storesetting.mobileLogo.title"),
					new Message("desc_mobileLogo", "storesetting.mobileLogo.desc"),

					new Message("title_mobileOrder", "storesetting.mobileOrderMatrix.title"),
					new Message("desc_mobileOrder", "storesetting.mobileOrderMatrix.desc"),
					new Message("type1_mobileOrder", "storesetting.mobileOrderMatrix.type1"),
					new Message("type2_mobileOrder", "storesetting.mobileOrderMatrix.type2"),
					new Message("type3_mobileOrder", "storesetting.mobileOrderMatrix.type3"),
					
					new Message("wizard_kioskTitle", "storesetting.kioskLogoWizardTitle"),
					new Message("wizard_mobileTitle", "storesetting.mobilekioskLogoWizardTitle"),
					
					new Message("label_tab0", "storesetting.upload"),
					new Message("label_tabDesc0", "storesetting.imageFile"),
					new Message("label_tab1", "storesetting.confirm"),
					new Message("label_tabDesc1", "storesetting.preview"),
					
					new Message("msg_uploadFirst", "storesetting.msg.uploadFirst"),
					new Message("msg_inputFirst", "storesetting.msg.inputFirst"),
					new Message("msg_updateComplete", "storesetting.msg.updateComplete"),
					new Message("msg_imgSize", "storesetting.msg.imgSize")
    			});

    	
    	// 상점 개요 정보
    	SolUtil.addStoreOverviewInfo(model, locale, session, request);
    	

    	// 업로드 모델 구성
    	PayUploadTransModel uploadModel = new PayUploadTransModel();

    	Store store = storeService.getStore(SolUtil.getSessionStoreId(session));

		uploadModel.setStoreId(store == null ? -1 : store.getId());
		uploadModel.setType("TITLE");
		uploadModel.setAllowedExtensions("[\".jpg\", \".jpeg\", \".png\", \".mp4\"]");
		List<Ad> adList = payService.getAdList(store.getId());
    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("label_cancel", "upload.cancel"),
					new Message("label_dropFilesHere", "upload.dropFilesHere"),
					new Message("label_headerStatusUploaded", "upload.headerStatusUploaded"),
					new Message("label_headerStatusUploading", "upload.headerStatusUploading"),
					new Message("label_remove", "upload.remove"),
					new Message("label_retry", "upload.retry"),
					new Message("label_select", "upload.select"),
					new Message("label_uploadSelectedFiles", "upload.uploadSelectedFiles"),
					new Message("label_clearSelectedFiles", "upload.clearSelectedFiles"),
					new Message("label_invalidFileExtension", "upload.invalidFileExtension"),
    			});
    	model.addAttribute("adList",adList);
    	model.addAttribute("uploadModel", uploadModel);
    	//-
    	
    	// 상점 루트 디렉토리 정보
    	model.addAttribute("storeRoot", "S" + 
    			(store == null ? "0" : String.valueOf(store.getId())));

    	
        return "pay/storesetting";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public @ResponseBody StoreOpt read(Locale locale, HttpSession session) {
    	
    	try {
        	Store store = storeService.getStore(SolUtil.getSessionStoreId(session));
        	
        	if (store == null) {
        		return null;
        	} else {
        		StoreOpt opt = store.getStoreOpt();
        		
        		opt.setKioskLogoImage(payService.getUploadFile(opt.getKioskLogoImageId()));
        		opt.setMobileLogoImage(payService.getUploadFile(opt.getMobileLogoImageId()));
        		

        		return opt;
        	}
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    }
    
    
    /**
     * 업로드된 파일을 임의 파일명으로 변경 액션
     */
    @RequestMapping(value = "/upcmpl", method = RequestMethod.POST)
    public @ResponseBody String renameUploadedFile(@RequestBody Map<String, Object> model, 
    		HttpSession session, Locale locale) {
    	String uploadedFilename = (String)model.get("uploadedFilename");
    	String uploadType = (String)model.get("uploadType");
    	if (Util.isNotValid(uploadedFilename)) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
    	}
    	if(uploadType.equals("V")) {
    		System.out.println(uploadedFilename);
    		System.out.println(uploadType);
    	String newFilename = "";
    	String [] list = uploadedFilename.split(",");
    	System.out.println(list);
    	ArrayList<Object> name = new ArrayList<>();
    	for(int i=0; i<list.length; i++) {
    	
		String typeRootDir = SolUtil.getPhysicalRoot("UpTemp");
		File upFile = new File(typeRootDir + "/" + list[i]);
		
		System.out.println(upFile);
		if (!upFile.exists()) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
		}
		
		newFilename = Util.uniqueIFilename(list[i]);
		boolean success = false;
		name.add(newFilename);
		try {
			success = upFile.renameTo(new File(typeRootDir + "/" + newFilename));
        } catch (Exception e) {
        	logger.error("upcmpl", e);
        	throw new ServerOperationForbiddenException("OperationError");
        }
		
		if (!success) {
        	throw new ServerOperationForbiddenException("OperationError");
		}
		
    	}
    	return name.toString();
    	} else {
    	String typeRootDir = SolUtil.getPhysicalRoot("UpTemp");
		File upFile = new File(typeRootDir + "/" +uploadedFilename);
		if (!upFile.exists()) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
		}
		
		String newFilename = Util.uniqueIFilename(uploadedFilename);
		boolean success = false;
		try {
			success = upFile.renameTo(new File(typeRootDir + "/" + newFilename));
		}catch (Exception e) {
	        	logger.error("upcmpl", e);
	        	throw new ServerOperationForbiddenException("OperationError");
	        }
		return newFilename;
    	}
    }
    
	
	/**
	 * 변경 액션
	 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {

    	Store target = storeService.getStore((int)model.get("id"));
    	if (target != null) {
        	String kLogoType = Util.parseString((String)model.get("kLogoType"));
        	String kLogoImage = Util.parseString((String)model.get("kLogoImage"));
        	String kAd = Util.parseString((String)model.get("kAd"));
        	String kLogoUniqueName = Util.parseString((String)model.get("kLogoUniqueName"));
        	String vLogoUniqueName = Util.parseString((String)model.get("vLogoUniqueName"));
        	String kLogoText = Util.parseString((String)model.get("kLogoText"));
        	String mLogoType = Util.parseString((String)model.get("mLogoType"));
        	String mLogoImage = Util.parseString((String)model.get("mLogoImage"));
        	String mLogoUniqueName = Util.parseString((String)model.get("mLogoUniqueName"));
        	String mLogoText = Util.parseString((String)model.get("mLogoText"));
        	String mType = Util.parseString((String)model.get("mType"));
        	String debugStr = Util.parseString((String)model.get("debugStr"));
        	logger.info("ad"+kAd);
        	logger.info("debugStr"+debugStr);
        	
        	int storeId= SolUtil.getSessionStoreId(session);
        	
        	String vLogoUniqueName1 = vLogoUniqueName.replace("[", "");
        	String vLogoUniqueName2 = vLogoUniqueName1.replace("]", "");
        	String vLogoUniqueName3 = vLogoUniqueName2.replace(" ", "");
        	
        	System.out.println(vLogoUniqueName3);
        	//
        	// 잘못된 전달인자:
        	//    1) 키오스크 메뉴 매트릭스 값이 없음
        	//    2) 로고가 이미지 유형으로 설정되었는데, 파일 정보가 전달 안됨
        	//
        	
        	if ((kLogoType.equals("I") && Util.isNotValid(kLogoUniqueName)) || 
        			(mLogoType.equals("I") && Util.isNotValid(mLogoUniqueName))) {
        		throw new ServerOperationForbiddenException(
        				msgMgr.message("common.server.msg.wrongParamError", locale));
        	}
        	//-
        	String [] list = vLogoUniqueName3.split(",");
        	String [] list2 = kAd.split(",");
        	String [] indexList = debugStr.split(",");
        	List<Ad> adList = payService.getAdList(storeId);
        	
        	// 인덱스 처리
        	Index(indexList,adList,target, session);
        	
        	StoreOpt opt = target.getStoreOpt();
        	if (opt == null) {
        		opt = new StoreOpt(target, session);
        	}
        	
        	boolean refreshRequired = checkRefreshRequired(opt, kLogoType, kLogoUniqueName, kLogoText);
        	
    		String tempRootDir = SolUtil.getPhysicalRoot("UpTemp");
    		String dstKioskDir = SolUtil.getPhysicalRoot("KioskTitle", target.getId());
    		String dstKioskDirAd = SolUtil.getPhysicalRoot("Ad", target.getId());
    		String dstMobileDir = SolUtil.getPhysicalRoot("MobileTitle", target.getId());
    		
    		UploadFile kLogoImageFile = null, mLogoImageFile = null;
    		Ad adFile = null;
//    		List<UploadFile> = kAdImageFile2 = null;
        	try {
        		//
        		// 임시 폴더에 전달된 고유 파일이 존재하면, 새로 파일이 등록된 상태
        		//    미 존재면, 기존에 이미 등록된 상태
        		//
        		//    새로 등록 액션: 1) 대상 폴더에 불필요 파일 삭제
        		//                    2) 임시 폴더에서 대상 폴더로 파일 이동
        		//
        		
        		File kLogoFile = new File(tempRootDir + "/" + kLogoUniqueName);
        			
        		
        		if (Util.isValid(kLogoUniqueName) && kLogoFile.exists()) {
        			long kLogoFileLen = cleanFolderAndmoveFile(tempRootDir, dstKioskDir, kLogoUniqueName);

        			kLogoImageFile = new UploadFile(target, kLogoUniqueName, kLogoImage, kLogoFileLen, session);

        		}
        		
        		int adSize = payService.getAdList(storeId).size();
        		for (int i = 0; i < list.length;i++) {

        		File kAdFile = new File(tempRootDir + "/" + list[i]);
        			System.out.println(kAdFile);
    				if(Util.isValid(list[i]) && kAdFile.exists()) {
    					long vLogoFileLen = cleanFolderAndmoveFile(tempRootDir, dstKioskDirAd, list[i]);
    					adFile = new Ad(target, list[i], list2[i], vLogoFileLen, adSize + i,"A","Y", "/store/api/video/",session);
    					
    		        	if (adFile != null) {
    		        		payService.saveOrUpdate(adFile);
    		        		selfService.setMonTask("A", String.valueOf(target.getId()), session);
    		        	}
    					
    				}
        		}
        		File mLogoFile = new File(tempRootDir + "/" + mLogoUniqueName);
        		if (Util.isValid(mLogoUniqueName) && mLogoFile.exists()) {
        			long mLogoFileLen = cleanFolderAndmoveFile(tempRootDir, dstMobileDir, mLogoUniqueName);
        			
        			mLogoImageFile = new UploadFile(target, mLogoUniqueName, mLogoImage, mLogoFileLen, session);
        		}
        	} catch (Exception e) {
            	logger.error("update - move file", e);
            	throw new ServerOperationForbiddenException("OperationError");
        	}
        	
        	opt.setKioskLogoType(kLogoType);
        	opt.setKioskLogoText(kLogoText);
        	opt.setMobileLogoType(mLogoType);
        	opt.setMobileLogoText(mLogoText);
        	opt.setOrderType(mType);

        	if (kLogoImageFile != null) {
        		payService.saveOrUpdate(kLogoImageFile);
        		opt.setKioskLogoImageId(kLogoImageFile.getId());
        	}
        	


        	if (mLogoImageFile != null) {
        		payService.saveOrUpdate(mLogoImageFile);
        		opt.setMobileLogoImageId(mLogoImageFile.getId());
        	}
        	
        	opt.touchWho(session);
        	
        	storeService.saveOrUpdate(opt);
        	
        	if (refreshRequired) {
        		selfService.setMonTask("M", String.valueOf(target.getId()), session);
        	}
    	}
		
        return "OK";
    }
    /**
     * 변경 액션
     */
    @RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
    public @ResponseBody String updateMenu(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
    	
    	Store target = storeService.getStore((int)model.get("id"));
    	if (target != null) {
    		String menu = Util.parseString((String)model.get("menu"));
    		String type = Util.parseString((String)model.get("type"));

    		
    		int storeId= SolUtil.getSessionStoreId(session);
    		
    		StoreOpt opt = target.getStoreOpt();
    		if (opt == null) {
    			opt = new StoreOpt(target, session);
    		}

    		Ad adFile = payService.getLastAd(storeId);
    		Ad ad = null;
    		System.out.println(adFile.getType());
    		int adSize = payService.getAdList(storeId).size();
    		try {
    				if(!adFile.getType().equals("M")) {

    					ad = new Ad(target, Util.uniqueMenu(menu), menu,0, adSize,type,"Y", "Menu",session);
    					
    					if (ad != null) {
    						payService.saveOrUpdate(ad);
    						selfService.setMonTask("A", String.valueOf(target.getId()), session);
    					}
    					
    				}
    			

    		} catch (Exception e) {
    			logger.error("update - move file", e);
    			throw new ServerOperationForbiddenException("OperationError");
    		}
    		
    	}
    	
    	return "OK";
    }
    
    /**
     * 대상 폴더를 정리하고 파일을 이동시킴
     */
    private long cleanFolderAndmoveFile(String srcFolderStr, String dstFolderStr, String filename)
    		throws Exception {
    	System.out.println("F"+filename);
    	System.out.println("d"+dstFolderStr);
    	Util.checkDirectory(dstFolderStr);
    	
    	ArrayList<File> list = new ArrayList<File>();
    	
		File dir = new File(dstFolderStr);
		if (dir.exists()) {
			if (dir.isDirectory()) {
				File[] listFiles = dir.listFiles();
				if (listFiles != null && listFiles.length > 0) {
					for (File file : listFiles) {
						list.add(file);
					}
				}
			}
		}
    	
		for(File file : list) {
//			file.delete();
		}

		File srcFile = new File(srcFolderStr + "/" + filename);
		File dstFile = new File(dstFolderStr + "/" + filename);
		
    	FileCopyUtils.copy(srcFile, new File(dstFolderStr + "/" + filename));
		
    	srcFile.delete();
    	
    	return dstFile.length();
    }
    
    /**
     * 상점 기기의 새로고침 필요 여부를 판단함
     */
    private boolean checkRefreshRequired(StoreOpt opt, String kLogoType,
    		String kLogoUniqueName, String kLogoText) {
    	
    	if (!kLogoType.equals(opt.getKioskLogoType())) {
    		return true;
    	}
    	
    	if (Util.isValid(kLogoText)) {
    		if (Util.isNotValid(opt.getKioskLogoText()) || !kLogoText.equals(opt.getKioskLogoText())) {
    			return true;
    		}
    	} else {
    		if (Util.isValid(opt.getKioskLogoText())) {
    			return true;
    		}
    	}
    	
		UploadFile kioskLogoFile = payService.getUploadFile(opt.getKioskLogoImageId());
    	if (Util.isValid(kLogoUniqueName)) {
    		if (kioskLogoFile == null || (kioskLogoFile != null && !kLogoUniqueName.equals(kioskLogoFile.getFilename()))) {
    			return true;
    		}
    	} else {
    		if (kioskLogoFile != null) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * 상점 기기의 새로고침 필요 여부를 판단함
     */
    private boolean Index(String[] indexList,List<Ad> adList,Store target, HttpSession session ) {
    	
    	if(indexList.length >0) {
        	for(int i = 0; i < adList.size(); i++) {
        		Ad ad = payService.getAdByFileName(indexList[i]);
            	System.out.println(indexList[i]);
        		ad.setIndex(i);
        		payService.saveOrUpdate(ad);
        	}
        	selfService.setMonTask("A", String.valueOf(target.getId()), session);
        	return true;
    	}
    	
    	return false;
    }
    
	/**
	 * 삭제 액션
	 */
    @RequestMapping(value = "/destroy", method = RequestMethod.POST)
    public @ResponseBody String destroy(@RequestBody Map<String, Object> model, HttpSession session) {

    	String UUID = Util.parseString((String)model.get("fileName"));
    	
    	Ad ad = payService.getAdByFileName(UUID);
    	int storeId = SolUtil.getSessionStoreId(session);
    	Store store = storeService.getStore(storeId);
    	List<Ad> adList = payService.getAdList(storeId);

    	
    	try {
        	payService.deleteAd(ad);
			selfService.setMonTask("A", String.valueOf(storeId), session);

    	} catch (Exception e) {
    		logger.error("destroy", e);
    		throw new ServerOperationForbiddenException("DeleteError");
    	}

        return "OK";
    }
}
