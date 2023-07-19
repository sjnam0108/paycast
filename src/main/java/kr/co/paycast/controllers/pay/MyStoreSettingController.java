package kr.co.paycast.controllers.pay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.DataSourceRequest;
import kr.co.paycast.models.DataSourceResult;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.pay.CouponPolicy;
import kr.co.paycast.models.pay.PayUploadTransModel;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StoreDeliveryPolicy;
import kr.co.paycast.models.pay.StoreOpt;
import kr.co.paycast.models.pay.StorePolicy;
import kr.co.paycast.models.pay.StoreDeliveryPay;
import kr.co.paycast.models.pay.UploadFile;
import kr.co.paycast.models.pay.service.CouponPointService;
import kr.co.paycast.models.pay.service.DeliveryPayService;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.self.service.SelfService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.PolicyDispItem;

//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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

/**
 * 내 매장 설정 컨트롤러
 */
@Controller("pay-my-store-setting-controller")
@RequestMapping(value="/pay/mystoresetting")
public class MyStoreSettingController {
	
	private static final Logger logger = LoggerFactory.getLogger(MyStoreSettingController.class);
	
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
    
    @Autowired
    private CouponPointService couponService;
    
    @Autowired
    private DeliveryPayService deliveryPayService;

	
	/**
	 * 내 매장 설정 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);
    	
    	solMsgMgr.checkStoreSelectionMessage(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "mystoresetting.title"),
					
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
					
					new Message("wizard_kioskTitle", "storesetting.kioskLogoWizardTitle"),
					new Message("wizard_mobileTitle", "storesetting.mobilekioskLogoWizardTitle"),
					
					new Message("label_tab0", "storesetting.upload"),
					new Message("label_tabDesc0", "storesetting.imageFile"),
					new Message("label_tab1", "storesetting.confirm"),
					new Message("label_tabDesc1", "storesetting.preview"),
					
					new Message("msg_uploadFirst", "storesetting.msg.uploadFirst"),
					new Message("msg_inputFirst", "storesetting.msg.inputFirst"),
					new Message("msg_updateComplete", "storesetting.msg.updateComplete"),
					new Message("msg_imgSize", "storesetting.msg.imgSize"),
					
					new Message("title_coupon", "storesetting.coupon"),									// 쿠폰
					new Message("title_stamp", "storesetting.stamp"),									// 스탬프
					new Message("title_stampReservePolicy", "storesetting.stampReservePolicy"),			// 스탬프 적립 정책
					new Message("title_couponIssuancePolicy", "storesetting.couponIssuancePolicy"),		// 발행 정책
					
					new Message("label_stampText1", "storesetting.stampText1"),							// 주문수량
					new Message("label_stampText2", "storesetting.stampText2"),							// 개 마다 
					new Message("label_stampText3", "storesetting.stampText3"),							// 개 적립한다.
					new Message("label_couponText1", "storesetting.couponText1"),						// 개 모으면
					new Message("label_couponText2", "storesetting.couponText2"),						// 발행한다.
					new Message("label_couponNm", "storesetting.couponNm"),								// 쿠폰명
					new Message("label_couponDiscount", "storesetting.couponDiscount"),					// 할인금액
					new Message("label_couponPrice1", "storesetting.couponPrice1"),						// (원)
					new Message("label_couponValidDate", "storesetting.couponValidDate"),				// 유효기간
					new Message("label_couponValidDate1", "storesetting.couponValidDate1"),				// (개월)
					new Message("label_chooseText", "storesetting.chooseText"),							// 선택하세요.
					new Message("label_couponPrice", "storesetting.couponPrice"),						// 쿠폰 가격
					
					new Message("title_point", "storesetting.point"),
					new Message("title_pointReservePolicy", "storesetting.pointReservePolicy"),
					new Message("title_pointUsePolicy", "storesetting.pointUsePolicy"),
					new Message("label_pointText1", "storesetting.pointText1"),
					new Message("label_pointText2", "storesetting.pointText2"),
					new Message("label_pointText3", "storesetting.pointText3"),
					
					new Message("label_immediate", "storesetting.immediate"),
					new Message("msg_immediateMsg", "storesetting.msg.immediate")
    			});
    	
    	// 상점 선택 스위치 표시(2개 이상일때)
    	model.addAttribute("isStoreSwitcherMode", true);
    	
    	// 업로드 모델 구성
    	PayUploadTransModel uploadModel = new PayUploadTransModel();

    	Store store = storeService.getStore(SolUtil.getSessionStoreId(session));

		uploadModel.setStoreId(store == null ? -1 : store.getId());
		uploadModel.setType("TITLE");
		uploadModel.setAllowedExtensions("[\".jpg\", \".jpeg\", \".png\", \".mp4\"]");
		
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

    	model.addAttribute("uploadModel", uploadModel);
    	//-
    	
    	// 상점 루트 디렉토리 정보
    	model.addAttribute("storeRoot", "S" + 
    			(store == null ? "0" : String.valueOf(store.getId())));

    	// 쿠폰/포인트 화면 TYPE 
    	String type = "NO";
    	if(store.getStoreEtc() != null){
    		type = store.getStoreEtc().getSavingType();
    	}
    	model.addAttribute("savingType", type);
    	
        return "pay/mystoresetting";
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
    	if (Util.isNotValid(uploadedFilename)) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
    	}

		String typeRootDir = SolUtil.getPhysicalRoot("UpTemp");
		File upFile = new File(typeRootDir + "/" + uploadedFilename);
		if (!upFile.exists()) {
    		throw new ServerOperationForbiddenException(
    				msgMgr.message("common.server.msg.wrongParamError", locale));
		}
		
		String newFilename = Util.uniqueIFilename(uploadedFilename);
		boolean success = false;
		
		try {
			success = upFile.renameTo(new File(typeRootDir + "/" + newFilename));
        } catch (Exception e) {
        	logger.error("upcmpl", e);
        	throw new ServerOperationForbiddenException("OperationError");
        }

		if (!success) {
        	throw new ServerOperationForbiddenException("OperationError");
		}
		
    	return newFilename;
    }
    
	
	/**
	 * 변경 액션
	 */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody String update(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
//    	System.out.println(model);
    	Store target = storeService.getStore((int)model.get("id"));
//    	System.out.println(target);
    	if (target != null) {
        	String kLogoType = Util.parseString((String)model.get("kLogoType"));
        	String kLogoImage = Util.parseString((String)model.get("kLogoImage"));
        	String kAd = Util.parseString((String)model.get("kAd"));
        	String kLogoUniqueName = Util.parseString((String)model.get("kLogoUniqueName"));
        	String vLogoUniqueName = Util.parseString((String)model.get("vLogoUniqueName"));
        	String kLogoText = Util.parseString((String)model.get("kLogoText"));
        	String kMatrix = Util.parseString((String)model.get("kMatrix"));
        	String mLogoType = Util.parseString((String)model.get("mLogoType"));
        	String mLogoImage = Util.parseString((String)model.get("mLogoImage"));
        	String mLogoUniqueName = Util.parseString((String)model.get("mLogoUniqueName"));
        	String mLogoText = Util.parseString((String)model.get("mLogoText"));
//        	logger.info("ad"+kAd);
        	// 즉시취소 ON/OFF
        	boolean immediateTF = (boolean)model.get("immediateTF");
        	logger.info("immediateTF [{}]", immediateTF);
        	
        	//
        	// 잘못된 전달인자:
        	//    1) 키오스크 메뉴 매트릭스 값이 없음
        	//    2) 로고가 이미지 유형으로 설정되었는데, 파일 정보가 전달 안됨
        	//
        	if (Util.isNotValid(kMatrix)) {
        		throw new ServerOperationForbiddenException(
        				msgMgr.message("common.server.msg.wrongParamError", locale));
        	}
        	
        	if ((kLogoType.equals("I") && Util.isNotValid(kLogoUniqueName)) || 
        			(mLogoType.equals("I") && Util.isNotValid(mLogoUniqueName))) {
        		throw new ServerOperationForbiddenException(
        				msgMgr.message("common.server.msg.wrongParamError", locale));
        	}
        	//-
        	
        	StoreOpt opt = target.getStoreOpt();
        	if (opt == null) {
        		opt = new StoreOpt(target, session);
        	}
        	
        	boolean refreshRequired = checkRefreshRequired(opt, kMatrix, kLogoType, kLogoUniqueName, kLogoText);
        	
    		String tempRootDir = SolUtil.getPhysicalRoot("UpTemp");
    		String dstKioskDir = SolUtil.getPhysicalRoot("KioskTitle", target.getId());
    		String dstKioskDirAd = SolUtil.getPhysicalRoot("Ad", target.getId());
    		String dstMobileDir = SolUtil.getPhysicalRoot("MobileTitle", target.getId());
    		
    		UploadFile kLogoImageFile = null, mLogoImageFile = null, kAdImageFile = null;
    		
        	try {
        		//
        		// 임시 폴더에 전달된 고유 파일이 존재하면, 새로 파일이 등록된 상태
        		//    미 존재면, 기존에 이미 등록된 상태
        		//
        		//    새로 등록 액션: 1) 대상 폴더에 불필요 파일 삭제
        		//                    2) 임시 폴더에서 대상 폴더로 파일 이동
        		//
        		File kLogoFile = new File(tempRootDir + "/" + kLogoUniqueName);
        		File kAdFile = new File(tempRootDir + "/" + vLogoUniqueName);
        		if (Util.isValid(kLogoUniqueName) && kLogoFile.exists()) {
        			long kLogoFileLen = cleanFolderAndmoveFile(tempRootDir, dstKioskDir, kLogoUniqueName);
        			long vLogoFileLen = cleanFolderAndmoveFile(tempRootDir, dstKioskDirAd, vLogoUniqueName);
        			
        			kLogoImageFile = new UploadFile(target, kLogoUniqueName, kLogoImage, kLogoFileLen, session);
        			if(Util.isValid(kLogoUniqueName) && kAdFile.exists()) {
        				kAdImageFile = new UploadFile(target, vLogoUniqueName, kAd, vLogoFileLen, session);
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
        	opt.setMenuMatrix(kMatrix);
        	opt.setImmediate(immediateTF);

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
        	
        	// 쿠폰 저장. 
        	if(target.getStoreEtc() != null && "CP".equals(target.getStoreEtc().getSavingType())){
        		//스탬프 정책 (policyId / orderAmt / reserveAmt)
        		int policyId = Util.parseInt((String)model.get("policyId"), 0);
        		int orderAmt = Util.parseInt((String)model.get("orderAmt"), 0);
        		int reserveAmt = Util.parseInt((String)model.get("reserveAmt"), 0);
        		// 쿠폰 정책(couponPolicyId / stampAmt / couponSelect)
        		int couponPolicyId = Util.parseInt((String)model.get("couponPolicyId"), 0);
        		int stampAmt = Util.parseInt((String)model.get("stampAmt"), 0);
        		int couponSelect = Util.parseInt((String)model.get("couponSelect"), 0);
        		
        		boolean couponTf = couponPolicyUpdate(target, policyId, orderAmt, reserveAmt, couponPolicyId, stampAmt, couponSelect, session);
        		if(couponTf){
                	logger.error("update - couponPolicyUpdate");
                	throw new ServerOperationForbiddenException("OperationError");
        		}
        	}
        	
        	// 포인트 저장 
        	if(target.getStoreEtc() != null && "PO".equals(target.getStoreEtc().getSavingType())){
        		int pointId = Util.parseInt((String)model.get("pointId"), 0);
        		int percentageNumber = Util.parseInt((String)model.get("percentageNumber"), 0);
        		int pointAmt = Util.parseInt((String)model.get("pointAmt"), 0);
        		
        		boolean couponTf = pointPolicyUpdate(target, pointId, percentageNumber, pointAmt, session);
        		if(couponTf){
                	logger.error("update - pointPolicyUpdate");
                	throw new ServerOperationForbiddenException("OperationError");
        		}
        	}
        	
        	//배달 요금 정보저장
        	if(target.getStoreEtc() != null){
	    		int deliveryId = Util.parseInt((String)model.get("deliveryId"), 0);
	    		int minOrderPrice = Util.parseInt((String)model.get("minOrderPrice"), 0);
	    		int deliveryPay = Util.parseInt((String)model.get("deliveryPay"), 0);
	    		boolean deliveryTf = deliveryUpdate(target,deliveryId, minOrderPrice,deliveryPay, session);
	    		if(deliveryTf){
                	logger.error("update - pointPolicyUpdate");
                	throw new ServerOperationForbiddenException("OperationError");
        		}
        	}

    	}
		
        return "OK";
    }
    
    /**
     * 대상 폴더를 정리하고 파일을 이동시킴
     */
    private long cleanFolderAndmoveFile(String srcFolderStr, String dstFolderStr, String filename)
    		throws Exception {
    	
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
			file.delete();
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
    private boolean checkRefreshRequired(StoreOpt opt, String kMatrix, String kLogoType,
    		String kLogoUniqueName, String kLogoText) {
    	
    	if (!kMatrix.equals(opt.getMenuMatrix()) || !kLogoType.equals(opt.getKioskLogoType())) {
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
	 * 쿠폰 정책을 업데이트
	 */
    private boolean couponPolicyUpdate(Store store, int policyId, int orderAmt, int reserveAmt, int couponPolicyId, int stampAmt, int couponSelect, HttpSession session) {
    	try{
        	// 1. 스탬프 정책을 저장
        	// 2. 쿠폰 정책을 저장 > 쿠폰 정책 ID 필요!
        	// 3. 생성된 쿠폰 정책과 쿠폰을 연결 하기 위한 couponPolicy 데이터를 넣는다.   
    		StorePolicy stampPolicy = null;
        	if(policyId > 0){
        		stampPolicy = couponService.getPolicy(policyId);
        		stampPolicy.setOrderAmt(orderAmt);
        		stampPolicy.setStamp(reserveAmt);
        		stampPolicy.touchWho(session);
        		
        		couponService.saveOrUpdate(stampPolicy);
        	}else{
            	// 기존 정책이 없을 경우
            	// type에 따라 저장을 다르게 저장 S: 스탬프 적립 정책, C: 쿠폰 발급 정책
            	// "S" 스탬프 일 경우   주문 수량, 스탬프 적립 수량, 매장 정보
        		stampPolicy = new StorePolicy(orderAmt, reserveAmt, "S", 10, store, session);
            	couponService.saveOrUpdate(stampPolicy);
        	}
        	
        	StoreCoupon coupon = couponService.get(couponSelect);
//        	// "C" 스탬프 일 경우   스탬프 적립 수량, 매장 정보, 매핑된 coupon 정보
//        	// 주문 수량 정보는 제외
        	if(coupon != null){
            	logger.info("couponPolicyUpdate >> coupon.getId() [{}], coupon.getName() [{}]", coupon.getId(), coupon.getName());
        		if(couponPolicyId > 0){
        			CouponPolicy resCouponPolicy = couponService.getCouponPolicy(couponPolicyId);
        			StorePolicy policy = resCouponPolicy.getPolicy();
        			policy.setStamp(stampAmt);
        			policy.touchWho(session);
        			couponService.saveOrUpdate(policy);
        			
        			resCouponPolicy.setCoupon(coupon);
        			resCouponPolicy.setPolicy(policy);
        			couponService.saveOrUpdate(resCouponPolicy);
        			
        		}else{
        			StorePolicy policy = new StorePolicy(0, stampAmt, "C", 10, store, session);
        			couponService.saveOrUpdate(policy);
        			
        			CouponPolicy cp = new CouponPolicy(coupon, policy, store, session);
        			couponService.saveOrUpdate(cp);
        		}
        	}
    	}catch(Exception e){
        	logger.error("couponPolicyUpdate >> (스탬프 저장)policyId [{}], (쿠폰 저장)couponPolicyId [{}]", policyId, couponPolicyId);
        	logger.error("couponPolicyUpdate >> orderAmt [{}], reserveAmt [{}]", orderAmt, reserveAmt);
        	logger.error("couponPolicyUpdate >> stampAmt [{}], couponSelect [{}]", stampAmt, couponSelect);
    		logger.error("couponPolicyUpdate >> save", e);
    		e.printStackTrace();
    		return true;
    	}
    	
    	return false;
    }
    
	/**
	 * 포인트 정책을 업데이트
	 */
    private boolean pointPolicyUpdate(Store store, int pointId, int percentageNumber, int pointAmt,  HttpSession session){
    	try{
    		
        	// 1. 스탬프 정책을 저장
        	// 2. 쿠폰 정책을 저장 > 쿠폰 정책 ID 필요!
        	// 3. 생성된 쿠폰 정책과 쿠폰을 연결 하기 위한 couponPolicy 데이터를 넣는다.   
    		StorePolicy pointPolicy = null;
    		
        	if(pointId > 0){
        		pointPolicy = couponService.getPolicy(pointId);
        		pointPolicy.setPercentage((double)percentageNumber/100);
        		pointPolicy.setPoint(pointAmt);
        		pointPolicy.touchWho(session);

        		couponService.saveOrUpdate(pointPolicy);
        	}else{
            	// 기존 정책이 없을 경우
            	// type에 따라 저장을 다르게 저장 S: 스탬프 적립 정책, C: 쿠폰 발급 정책
            	// "S" 스탬프 일 경우   주문 수량, 스탬프 적립 수량, 매장 정보
        		pointPolicy = new StorePolicy(percentageNumber, pointAmt, "P", 10, store, session);
            	couponService.saveOrUpdate(pointPolicy);
        	}
        	
    	}catch(Exception e){
        	logger.error("pointPolicyUpdate >> (포인트 저장), couponPolicyId [{}]", pointId);
        	logger.error("pointPolicyUpdate >> percentage [{}], pointAmt [{}]", percentageNumber, pointAmt);
    		logger.error("pointPolicyUpdate >> save", e);
    		return true;
    	}
    	return false;
    }
    
    private boolean deliveryUpdate(Store store, int deliveryId, int minOrderPrice, int deliverPay, HttpSession session){
    	try{
    		
        	// 1. 배달요금 저장
    		StoreDeliveryPolicy deliveryPolicy = null;
    		
    		List<StoreDeliveryPolicy> list = deliveryPayService.getDeliveryPolicyList(store.getId());
    		if(list.size()==1){
    			deliveryPolicy = list.get(0);
    			deliveryPolicy.setMinOderPeice(minOrderPrice);
    			deliveryPolicy.setDeliveryPrice(deliverPay);
    			deliveryPayService.saveOrUpdate(deliveryPolicy);
    		}else{
				deliveryPolicy = new StoreDeliveryPolicy(minOrderPrice, deliverPay, store, session);
	    		deliveryPayService.saveOrUpdate(deliveryPolicy);
    		}
    	}catch(Exception e){
        	logger.error("deliveryUpdate >> (배달요금 저장), deliveryId [{}]", deliveryId);
        	logger.error("deliveryUpdate >> minOrderPrice [{}], deliverPay [{}]", minOrderPrice, deliverPay);
    		logger.error("deliveryUpdate >> save", e);
    		return true;
    	}
    	return false;
    }
    
	/**
	 * 쿠폰 읽기 액션
	 */
	@RequestMapping(value = "/couponRead", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult couponRead(@RequestBody DataSourceRequest request, 
    		HttpSession session, HttpServletRequest req, HttpServletResponse res) {

    	try {
    		return couponService.getCouponRead(request);
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("readError");
    	}
    }
	
	/**
	 * 저장된 쿠폰 select로 조회
	 */
	@RequestMapping(value = "/couponList", method = RequestMethod.POST)
    public @ResponseBody List<StoreCoupon> couponReadList(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
		
		int storeId = (int)model.get("storeId");
		
    	try {
    		return couponService.getCouponList(storeId, 0);
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("readError");
    	}
    }
	
	
	/**
	 * 쿠폰 저장
	 */
	@RequestMapping(value = "/couponSave", method = RequestMethod.POST)
    public @ResponseBody String couponSave(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
		
		Store target = storeService.getStore((int)model.get("storeId"));
		if (target != null) {
			String name = Util.parseString((String)model.get("name"));
			int price = Util.parseInt((String)model.get("price"));
			int validDate = Util.parseInt((String)model.get("validDate"));
			
			try {
				StoreCoupon coupon = new StoreCoupon(name, price, validDate, target, session);
				
				couponService.saveOrUpdate(coupon);
			} catch (Exception e) {
				logger.error("couponSave SaveError", e);
				throw new ServerOperationForbiddenException("SaveError");
			}
		
		}
		
		return "OK";
	}
 
	/**
	 * 쿠폰 변경 액션
	 */
	@RequestMapping(value = "/couponUpdate", method = RequestMethod.POST)
    public @ResponseBody String couponUpdate(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
		
		StoreCoupon coupon = couponService.get(Util.parseInt((String)model.get("couponId")));
		if (coupon != null) {
			String name = Util.parseString((String)model.get("name"));
			int price = Util.parseInt((String)model.get("price"));
			
			try {
				coupon.setName(name);
				coupon.setPrice(price);
				
				couponService.saveOrUpdate(coupon);
			} catch (Exception e) { 
				logger.error("couponSave SaveError", e);
				throw new ServerOperationForbiddenException("SaveError");
			}
		
		}
		
		return "OK";
	}
	
	/**
	 * 삭제 액션
	 */
    @RequestMapping(value = "/couponDestroy", method = RequestMethod.POST)
    public @ResponseBody String couponDestroy(@RequestBody Map<String, Object> model, HttpSession session) {

    	@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
    	
    	for (Object id : objs) {
    		StoreCoupon coupon = couponService.get((int)id);
    		if (coupon != null) {
    	    	try {
    	    		coupon.setDeleteState(1);
    	    		
    	    		couponService.saveOrUpdate(coupon);
    	        	
    	    	} catch (Exception e) {
    	    		logger.error("destroy", e);
    	    		throw new ServerOperationForbiddenException("DeleteError");
    	    	}
    		}
    	}

        return "OK";
    }
    
    //배달료 관련 
    
	/**
	 * 쿠폰 읽기 액션
	 */
	@RequestMapping(value = "/deliveryPayRead", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult deliveryRead(@RequestBody DataSourceRequest request, 
    		HttpSession session, HttpServletRequest req, HttpServletResponse res) {

    	try {
    		return deliveryPayService.getDeliveryPayRead(request);
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("readError");
    	}
    }
	
	/**
	 * 저장된 쿠폰 select로 조회
	 */
	@RequestMapping(value = "/deliveryPayList", method = RequestMethod.POST)
    public @ResponseBody List<StoreCoupon> deliveryList(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
		
		int storeId = (int)model.get("storeId");
		
    	try {
    		return couponService.getCouponList(storeId, 0);
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("readError");
    	}
    }
	
	
    /**
	 * 구간별 배달료 저장
	 */
	@RequestMapping(value = "/deliveryPaySave", method = RequestMethod.POST)
    public @ResponseBody String deliverySave(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
		
		Store target = storeService.getStore((int)model.get("storeId"));
		if (target != null) {
			int fromOrderPrice = Util.parseInt((String)model.get("fromOrderPrice"));
			int toOrderPrice = Util.parseInt((String)model.get("toOrderPrice"));
			int deliveryPrice = Util.parseInt((String)model.get("deliveryPrice"));
						
			if(fromOrderPrice==-1){
				fromOrderPrice=0;
			}
			if(toOrderPrice==-1){
				toOrderPrice=999999999;
			}
			if(toOrderPrice<fromOrderPrice){
				return "IF1";
			}
			try {
				StoreDeliveryPay deliveryPay = new StoreDeliveryPay(fromOrderPrice, toOrderPrice, deliveryPrice,target, session);
				deliveryPayService.saveOrUpdate(deliveryPay);
			} catch (Exception e) {
				logger.error("couponSave SaveError", e);
				throw new ServerOperationForbiddenException("SaveError");
			}
		
		}
		
		return "OK";
	}
    
	/**
	 * 구간별 배달료 수정
	 */
	@RequestMapping(value = "/deliveryPayUpdate", method = RequestMethod.POST)
    public @ResponseBody String deliveryUpdate(@RequestBody Map<String, Object> model, Locale locale, 
    		HttpSession session) {
		
		StoreDeliveryPay deliveryPay = deliveryPayService.get(Util.parseInt((String)model.get("deliveryId")));
		if (deliveryPay != null) {
				int fromOrderPrice = Util.parseInt((String)model.get("fromOrderPrice"));
				int toOrderPrice = Util.parseInt((String)model.get("toOrderPrice"));
				int deliveryPrice = Util.parseInt((String)model.get("deliveryPrice"));
				if(fromOrderPrice==-1){
					fromOrderPrice=0;
				}
				if(toOrderPrice==-1){
					toOrderPrice=999999999;
				}
				if(toOrderPrice<fromOrderPrice){
					return "IF2";
				}
			try {
				deliveryPay.setFromOrderPrice(fromOrderPrice);
				deliveryPay.setToOrderPrice(toOrderPrice);
				deliveryPay.setDeliveryPrice(deliveryPrice);
				
				deliveryPayService.saveOrUpdate(deliveryPay);
			} catch (Exception e) { 
				logger.error("couponSave SaveError", e);
				throw new ServerOperationForbiddenException("SaveError");
			}
		
		}
		
		return "OK";
	}
	
	/**
	 * 삭제 액션
	 */
	@RequestMapping(value = "/deliveryPayDestroy", method = RequestMethod.POST)
    public @ResponseBody String deliveryDestroy(@RequestBody Map<String, Object> model, HttpSession session) {

    	@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
        	
    	for (Object id : objs) {
    		StoreDeliveryPay deliveryPay = deliveryPayService.get((int)id);
    		if (deliveryPay != null) {
    	    	try {  	    		
    	    		deliveryPayService.deliveryPayDelete(deliveryPay);
    	    	} catch (Exception e) {
    	    		logger.error("destroy", e);
    	    		throw new ServerOperationForbiddenException("DeleteError");
    	    	}
    		}
    	}

        return "OK";
    }
    
	/**
	 * 읽기 액션
	 */
    @RequestMapping(value = "/policyRead", method = RequestMethod.POST)
    public @ResponseBody List<PolicyDispItem> policyRead(@RequestBody Map<String, Object> model, HttpSession session) {
    	List<PolicyDispItem> policyDispItemList = new ArrayList<PolicyDispItem>();
    	
    	try {
    		int storeId = (int)model.get("storeId");
    		String readType = Util.parseString((String)model.get("readType"), "");
    		if("S".equals(readType)){
    			logger.info("프탬프/쿠폰 사용할경우");
    			List<StorePolicy> policyList = couponService.getPolicyList(storeId, "S");
    			StoreDeliveryPolicy deliveryPolicy = deliveryPayService.getDeliveryPolicy(storeId);
    			if(policyList.size() > 0){
    				for(StorePolicy item : policyList){
    					PolicyDispItem policyDispItem = new PolicyDispItem(item);
    					if(deliveryPolicy==null){
    						policyDispItem.setMinOrderPrice(0);
        					policyDispItem.setDeliveryPrice(0);
    					}else{
    						policyDispItem.setMinOrderPrice(deliveryPolicy.getMinOderPeice());
        					policyDispItem.setDeliveryPrice(deliveryPolicy.getDeliveryPrice());
    					}
    					policyDispItemList.add(policyDispItem);
    				}
    			}
    			
    			List<CouponPolicy> couponPolicyList = couponService.getCouponPolicyList(storeId);
    			if(couponPolicyList.size() > 0){
    				for(CouponPolicy item : couponPolicyList){
    					PolicyDispItem policyDispItem = new PolicyDispItem(item);
    					if(deliveryPolicy==null){
    						policyDispItem.setMinOrderPrice(0);
        					policyDispItem.setDeliveryPrice(0);
    					}else{
    						policyDispItem.setMinOrderPrice(deliveryPolicy.getMinOderPeice());
        					policyDispItem.setDeliveryPrice(deliveryPolicy.getDeliveryPrice());
    					}
    					policyDispItemList.add(policyDispItem);
    				}
    			}
    		}else if("P".equals(readType)){
    			logger.info("포인트 사용할 경우");
    			List<StorePolicy> policyList = couponService.getPolicyList(storeId, "P");
    			StoreDeliveryPolicy deliveryPolicy = deliveryPayService.getDeliveryPolicy(storeId);
    			if(policyList.size() > 0){
    				for(StorePolicy item : policyList){
    					PolicyDispItem policyDispItem = new PolicyDispItem(item);
    					if(deliveryPolicy==null){
    						policyDispItem.setMinOrderPrice(0);
        					policyDispItem.setDeliveryPrice(0);
    					}else{
    						policyDispItem.setMinOrderPrice(deliveryPolicy.getMinOderPeice());
        					policyDispItem.setDeliveryPrice(deliveryPolicy.getDeliveryPrice());
    					}
    					policyDispItemList.add(policyDispItem);
    				}
    			}
    		}else{
    			logger.info("포인트&쿠폰 사용안할경우");
    			StorePolicy policy = couponService.getPolicy(storeId);
    			StoreDeliveryPolicy deliveryPolicy = deliveryPayService.getDeliveryPolicy(storeId);
    			PolicyDispItem policyDispItem=null;
    			if(policy!=null){
    				policyDispItem = new PolicyDispItem(policy);
    			}else{
    				policyDispItem = new PolicyDispItem();
    			}
				if(deliveryPolicy==null){
					policyDispItem.setMinOrderPrice(0);
    				policyDispItem.setDeliveryPrice(0);
				}else{
					policyDispItem.setMinOrderPrice(deliveryPolicy.getMinOderPeice());
    				policyDispItem.setDeliveryPrice(deliveryPolicy.getDeliveryPrice());
				}
    			policyDispItemList.add(policyDispItem);
    			
    		}
    		
    	} catch (Exception e) {
    		logger.error("read", e);
    		throw new ServerOperationForbiddenException("ReadError");
    	}
    	//System.err.println(policyDispItemList);
		return policyDispItemList;
    }
   
}
