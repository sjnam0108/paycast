package kr.co.paycast.controllers.store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.LoginUser;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.ModelManager;
import kr.co.paycast.models.PayMessageManager;
import kr.co.paycast.models.self.service.SelfService;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderVerification;
import kr.co.paycast.models.store.service.EasyPayService;
import kr.co.paycast.models.store.service.StoreCancelService;
import kr.co.paycast.models.store.service.StoreOrderService;
import kr.co.paycast.models.store.service.StorePayService;
import kr.co.paycast.models.store.service.StoreSiteService;
import kr.co.paycast.utils.GenerateCert;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.store.StoreCanCelView;
import kr.co.paycast.viewmodels.store.StoreSalesView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 매출 내역 조회
 */
@Controller("store-pay-controller")
@RequestMapping(value="/store/sales")
public class StoreSalesController {
	private static final Logger logger = LoggerFactory.getLogger(StoreSalesController.class);

	@Autowired
	private MessageManager msgMgr;
    
	@Autowired
	private ModelManager modelMgr;
	
    @Autowired
    private StorePayService storePayService;
    
    @Autowired
    private StoreSiteService storeSiteService;
    
    @Autowired
    private SelfService selfService;
    
    @Autowired
    private StoreOrderService storeOrderService;
    
    @Autowired
    private StoreCancelService storeCancelService;

	@Autowired
	private PayMessageManager solMsgMgr;
    
	@Autowired 
	private EasyPayService easyPayService;
	
	/**
	 * 스마일 페이 결재 내역
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	solMsgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "sales.title"),
    				new Message("title_termdate", "sales.termdate"),
    				new Message("title_date", "sales.date"),
    				new Message("title_packing", "sales.packing"),
    				new Message("title_menu", "sales.menu"),
    				new Message("title_amount", "sales.amount"),
    				new Message("title_amt", "sales.amt"),
    				new Message("title_totalamt", "sales.totalamt"),
    				new Message("title_fnname", "sales.fnname"),
    				new Message("title_orderDevice", "sales.orderDevice"),
    				new Message("title_ordernum", "sales.ordernum"),
    				
    				new Message("title_cancel", "sales.cancel"),
    				new Message("title_cancelPw", "sales.cancelPw"),
    				new Message("title_cancelAmt", "sales.cancelAmt"),
    				new Message("title_cancelMSG", "sales.cancelMSG"),
    				new Message("title_cancelPartialCode", "sales.cancelPartialCode"),
					new Message("title_cancelAll", "sales.cancelAll"),
					new Message("title_cancelPart", "sales.cancelPart"),
					
					new Message("title_immediateCanCel", "sales.immediateCanCel"),
					new Message("mag_immediateCanCelMsg", "sales.immediateCanCelMsg")
    			});
    	
    	PayUtil.searchDay(model, locale);
    	
    	// Device가 PC일 경우에만, 다중 행 선택 설정
    	Util.setMultiSelectableIfFromComputer(model, request);
    	
    	// 모바일 여부체크
    	PayUtil.getMobileCheck(model, request);
    	
        return "store/storeSales";
    }
    
	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public @ResponseBody List<StoreSalesView> read(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {
		List<StoreSalesView> resultList = new ArrayList<StoreSalesView>();

		try {
			String fromDate = (String)model.get("from");
			
			System.err.println(fromDate+ "아이디  : "+getStoreId(session));

			resultList = storePayService.getRead(fromDate, getStoreId(session), locale);

		} catch (RuntimeException re) {
			logger.error("read", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
		return resultList;
	}
	
	/**
	 * 결제 취소 코드 생성 
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public @ResponseBody String cancel(Model model, HttpServletRequest request, HttpSession session, Locale locale) {
		
		// 1. 매출 취소를 할경우 6자리의 다른 값을 뽑아 낸다. (key 값 생성)
        MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
        String storeOrderId = mrequest.getParameter("checkId");
        String storeId = mrequest.getParameter("storeId");
        String cancelPw = mrequest.getParameter("cancelPw");
        String partialCode = mrequest.getParameter("partialCode");
		
		String certNumber = "";
        StoreOrder storeOrder = storeOrderService.getDplyStoreOrderPrint(Util.parseInt(storeOrderId, 0));

        try {
        	if(storeOrder != null){
		        // 1. 해당 결제 내역에서 취소여부가 Y 가 아닐 경우 인증키 생성 및 재생성을 해줘야 한다. 
		        // Y : 결제 취소가 완료된 경우
		        if( !"Y".equals(storeOrder.getOrderCancelStatus()) ){
		        	if(!"N".equals(storeOrder.getOrderCancelStatus()) && !"".equals(storeOrder.getOrderCancelStatus())){
		        		//이전에 생성했던 승인번호는 사용하지 못하게 변경
		        		StoreOrderVerification verification = storeCancelService.getStoreCancelOne(storeOrder.getOrderCancelId());
	        			verification.setStatus("N");
	        			storeCancelService.saveOrUpdate(verification);
		        	}
		        	
		        	//취소 코드 생성
		        	certNumber = orderCancelCode(locale);
		        	
		        	StoreOrderVerification orderVerification = new StoreOrderVerification(storeOrder.getStoreId(), storeOrder.getId(), storeOrder.getOrderNumber(), 
		        			certNumber, "Y", new Date(), PayUtil.getMaxTaskHOUR(new Date()), session);
		        	orderVerification.setCancelStoreAuth(cancelPw);
		        	int idVerifi = storeCancelService.save(orderVerification);
		        	
		        	storeOrder.setOrderCancelId(idVerifi);
		        	storeOrder.setOrderCancelStatus("1"); // 취소 승인번호 생성일 경우 상태  : 1
		        	
		        	storeOrderService.saveOrUpdate(storeOrder);
		        }else{
					logger.error("Cancel_Status", storeOrder.getOrderCancelStatus());
					throw new ServerOperationForbiddenException("이미 취소가 완료 되었습니다. ");
		        }
        	}
		} catch (Exception e) {
			logger.error("cancel > update", e);
			throw new ServerOperationForbiddenException("SaveError");
		}
        
        return certNumber;
	}
	
	/**
	 * 결제 취소 번호 생성
	 */
	@RequestMapping(value = "/cancelKiosk", method = RequestMethod.POST)
    public @ResponseBody String cancelKiosk(Model model, HttpServletRequest request, HttpSession session, Locale locale) {
		
		logger.info("윤영 결제 취소 테스트 cancelKiosk");
		
		// 1. 매출 취소를 할경우 6자리의 다른 값을 뽑아 낸다. (key 값 생성)
        MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
        String storeOrderId = mrequest.getParameter("checkId");
        String storeId = mrequest.getParameter("storeId");
		
		String certNumber = "";
        StoreOrder storeOrder = storeOrderService.getDplyStoreOrderPrint(Util.parseInt(storeOrderId, 0));

        try {
        	if(storeOrder != null){
		        // 1. 해당 결제 내역에서 취소여부가 Y 가 아닐 경우 인증키 생성 및 재생성을 해줘야 한다. 
		        // Y : 결제 취소가 완료된 경우
		        if( !"Y".equals(storeOrder.getOrderCancelStatus()) ){
		        	if(!"N".equals(storeOrder.getOrderCancelStatus()) && !"".equals(storeOrder.getOrderCancelStatus())){
		        		//이전에 생성했던 승인번호는 사용하지 못하게 변경
		        		StoreOrderVerification verification = storeCancelService.getStoreCancelOne(storeOrder.getOrderCancelId());
	        			verification.setStatus("N");
	        			storeCancelService.saveOrUpdate(verification);
		        	}
		        	
		    		//취소 코드 생성
		            certNumber = orderCancelCode(locale);
		
		        	StoreOrderVerification orderVerification = new StoreOrderVerification(storeOrder.getStoreId(), storeOrder.getId(), storeOrder.getOrderNumber(), 
		        			certNumber, "Y", new Date(), PayUtil.getMaxTaskHOUR(new Date()), session);
		        	int idVerifi = storeCancelService.save(orderVerification);
		        	
		        	storeOrder.setOrderCancelId(idVerifi);
		        	storeOrder.setOrderCancelStatus("1"); // 취소 승인번호 생성일 경우 상태  : 1
		        	storeOrderService.saveOrUpdate(storeOrder);
		        }else{
					logger.error("Cancel_Status", storeOrder.getOrderCancelStatus());
					throw new ServerOperationForbiddenException("이미 취소가 완료 되었습니다. ");
		        }
        	}
		} catch (Exception e) {
			logger.error("cancel > update", e);
			throw new ServerOperationForbiddenException("SaveError");
		}
        
        return certNumber;
	}
	
	/**
	 * 즉시 결제 취소
	 *  2020.07.02 코드 생성이 아닌 즉시 취소를 위해서 생성
	 *   - 해당 내용은 모바일만 즉시 취소가 가능
	 */
	@RequestMapping(value = "/immediateCanCel", method = RequestMethod.POST)
    public @ResponseBody String immediateCanCel(@RequestBody Map<String, Object> model, HttpServletRequest request, Locale locale, 
    		HttpSession session) {
		
		int storeId = getStoreId(session);
    	int storeOrderId = Util.parseInt((String)model.get("checkId"), 0);
    	logger.info("storeOrderId [{}] StoreId [{}]", storeOrderId, storeId);
    	
    	String success = "true";
    	StoreOrder storeOrder = storeOrderService.getDplyStoreOrderPrint(storeOrderId);
    	if(storeOrder != null){
    		//취소 코드 생성
            String certNumber = orderCancelCode(locale);

        	StoreOrderVerification orderVerification = new StoreOrderVerification(storeOrder.getStoreId(), storeOrder.getId(), storeOrder.getOrderNumber(), 
        			certNumber, "Y", new Date(), PayUtil.getMaxTaskHOUR(new Date()), session);
        	int idVerifi = storeCancelService.save(orderVerification);
        	
        	storeOrder.setOrderCancelId(idVerifi);
        	storeOrder.setOrderCancelStatus("1"); // 취소 승인번호 생성일 경우 상태  : 1
        	storeOrderService.saveOrUpdate(storeOrder);
        	
        	StoreCanCelView storeCanCelView = storeCancelService.immediateCanCel(orderVerification, certNumber, "M");
        	
        	if("Y".equals(storeCanCelView.getPayAuthYN()) && "ME".equals(storeCanCelView.getOrderDevice())){
				success = easyPayService.easyPayCancel(storeId, storeOrderId, certNumber, request, locale, session);
        	}
    	}
		return success;
	}
	
	/**
	 * 상점 번호 획득
	 */
    private int getStoreId(HttpSession session) {
    	
    	int storeId = -1;
    	LoginUser loginUser = null;
    	if (session != null) {
    		loginUser = (LoginUser) session.getAttribute("loginUser");
    		if (loginUser != null) {
    			storeId = loginUser.getStoreId();
    		}
    	}
		
    	return storeId;
    }
    
	/**
	 * 취소 코드 생성
	 *  - 상태  : 4자리 생성
	 */
    private String orderCancelCode(Locale locale) {
		String approvalDigit = msgMgr.message("approval.digit", locale);
		GenerateCert ge = new GenerateCert();
        ge.setCertNumLength(Util.parseInt(approvalDigit));
        
        String certNumber = ge.excuteGenerateNumber();
		
    	return certNumber;
    }
}
