package kr.co.paycast.controllers.pay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.pay.CouponPolicy;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StorePolicy;
import kr.co.paycast.models.pay.UploadFile;
import kr.co.paycast.models.pay.service.CouponPointService;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.StoreAlimTalk;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderBasket;
import kr.co.paycast.models.store.StoreOrderCoupon;
import kr.co.paycast.models.store.StoreOrderPay;
import kr.co.paycast.models.store.StoreOrderPoint;
import kr.co.paycast.models.store.service.EasyPayService;
import kr.co.paycast.models.store.service.StoreAllimTalkService;
import kr.co.paycast.models.store.service.StoreBasketService;
import kr.co.paycast.models.store.service.StoreCookService;
import kr.co.paycast.models.store.service.StoreOrderService;
import kr.co.paycast.models.store.service.StorePayService;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * (KICC)easypay 결제
 */
@Controller("easypay-controller")
@RequestMapping(value="/easypay")
public class EasypayController {
	private static final Logger logger = LoggerFactory.getLogger(EasypayController.class);

	@Autowired
	private MessageManager msgMgr;
    
    @Autowired 
    private StoreService storeService;
	
    @Autowired
    private StoreOrderService storeOrderService;
    
    @Autowired
    private StorePayService storePayService;
    
	@Autowired 
    private StoreBasketService basketService;
	
	@Autowired 
	private EasyPayService easyPayService;
	
	@Autowired 
    private PayService payService;
	
    @Autowired
    private StoreCookService storeCookService;
    
    @Autowired
    private CouponPointService couponService;
    
    @Autowired 
    private StoreAllimTalkService alimTalkService;
	
	/**
	 * easypay 결제 시작
	 * 
	 * easypay 카드 결제를 제외한 나머지 테스트를 위해서 사용 되는 URL (2019.12.17 사용되지 않고 있음)
	 * 이후 해당 메소드는 테스트를 위해 사용될수 있으면 URL 및 데이터 변경이 필요
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (Exception e) {
			logger.error("/easypay >> CharacterEncoding >>> [{}]", e);
		}
	   
   		String StopURL = (String)request.getParameter("stopURL");
   		String storeName = (String)request.getParameter("storeName");
   		String goodsName = (String)request.getParameter("goodsName");
   		String amt = (String)request.getParameter("amt");
   		String moid = (String)request.getParameter("moid");
   		String payMethod = (String)request.getParameter("payMethod");
   		String reqUrl = (String)request.getParameter("reqUrl");
   		String telNum = (String)request.getParameter("telNum");
    	logger.info("/easypay >> StopURL >>> [{}]", StopURL);
    	logger.info("/easypay >> GoodsName >>> [{}]", goodsName);
    	logger.info("/easypay >> Amt >>> [{}]", amt);
    	logger.info("/easypay >> Moid >>> [{}]", moid);
    	logger.info("/easypay >> payMethod >>> [{}]", payMethod);
    	logger.info("/easypay >> reqUrl >>> [{}]", reqUrl);
    	logger.info("/easypay >> telNum >>> [{}]", telNum);
    	
    	msgMgr.addViewMessages(model, locale,
			new Message[] {
				new Message("pageTitle", "kicc.title"),
				new Message("mainAction", "kicc.mainAction"),
				new Message("mainScript", "kicc.mainScript")
			});
    	
    	/*--공통--*/
    	model.addAttribute("sp_mall_id", "T0001997");			//가맹점 ID
    	model.addAttribute("sp_mall_nm", storeName);			//가맹점명
    	model.addAttribute("sp_order_no", moid);				//가맹점 주문번호
    	model.addAttribute("sp_currency", "00");				//통화코드 : 00-원
    	model.addAttribute("sp_product_nm", goodsName);			//상품명
    	model.addAttribute("sp_product_amt", amt);				//상품금액
    	model.addAttribute("sp_lang_flag", "KOR");				//언어: KOR / ENG
    	model.addAttribute("sp_charset", "UTF-8");				//가맹점 Charset: EUC-KR(default) / UTF-8
    	model.addAttribute("sp_user_type", "1");				//사용자 구분(1:일반, 2:회원)
    	model.addAttribute("sp_user_id", "");					//가맹점 고객 ID
    	model.addAttribute("sp_memb_user_no", "");				//가맹점 고객 일련번호
    	model.addAttribute("sp_user_nm", "");					//가맹점 고객명
    	model.addAttribute("sp_user_mail", "");					//가맹점 고객 이메일
    	model.addAttribute("sp_user_phone1", telNum);			//가맹점 고객 번호1
    	model.addAttribute("sp_user_phone2", "");				//가맹점 고객 번호2
    	model.addAttribute("sp_user_addr", "");					//가맹점 고객 주소
    	model.addAttribute("sp_product_type", "0");				//상품정보구분 : 0-실물, 1-서비스
    	Date date = new Date ();
    	int day = Util.parseInt(msgMgr.message("kicc.productExpr", locale), 1);
    	Date addDay = Util.addDays(date, day);
    	String end_date = Util.toSimpleString(addDay, "yyyyMMdd");
    	
    	model.addAttribute("sp_product_expr", end_date);		//서비스기간 : YYYYMMDD
    	model.addAttribute("sp_app_scheme", "");				//가맹점 app scheme : 모바일app으로 서비스시 필수
    	
    	/*--신용카드--*/   
    	model.addAttribute("sp_usedcard_code", "");				//사용가능한 카드 LIST
    	model.addAttribute("sp_quota", "");						//할부개월
    	model.addAttribute("sp_os_cert_flag", "2");				//해외안심클릭 사용여부    
    	model.addAttribute("sp_noinst_term", "");				//무이자기간
    	model.addAttribute("sp_point_card", "");				//포인트카드 LIST
    	
    	/*--가상계좌--*/
    	model.addAttribute("sp_vacct_bank", "");				//가상계좌 사용가능한 은행 LIST 
    	model.addAttribute("sp_vacct_end_date", end_date);		//입금 만료 날짜
    	model.addAttribute("sp_vacct_end_time", Util.parseString(msgMgr.message("kicc.endTime", locale),"235959"));		//입금 만료 시간
    	
    	/*--return url--*/
    	model.addAttribute("sp_return_url", reqUrl+"/easypay/orderSubmit");
    	model.addAttribute("stopURL", StopURL);
    	
    	model.addAttribute("sp_cert_type", "");					//"":일반, 0:인증, 1:비인증
    	model.addAttribute("sp_window_type", "submit");			//윈도우타입
    	model.addAttribute("sp_kmotion_useyn", "Y");			//국민앱카드 사용유무
    	model.addAttribute("sp_disp_cash_yn", "N");				//현금영수증 입력 필드 노출(N:비노출)/계좌이체, 가상계좌 , 휴대폰에만 적용
    	
    	
    	/**
    	 * easypay 카드 결제를 제외한 나머지 테스트를 위해서 사용 되는 URL (2019.12.17 사용되지 않고 있음)
    	 * 이후 해당 메소드는 테스트를 위해 사용될수 있으면 URL 및 데이터 변경이 필요
    	 */
        return "kicc/order";
    }
    
	/**
	 * easypay 결제 
	 */
    @RequestMapping(value = "/orderSubmit", method = RequestMethod.POST)
    public String orderSubmit(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {

	   	msgMgr.addViewMessages(model, locale,
    			new Message[] {
    				new Message("html_lang", Util.htmlLang(locale)),	
					
					new Message("pay_orderNum", "smilepay.orderNum"),
					new Message("pay_stay", "smilepay.stay"),
					new Message("pay_firstPage", "smilepay.firstPage"),
					
					new Message("pay_msg_firstPage", "smilepay.msg.firstPage"),
					new Message("pay_msg_orderTable", "smilepay.msg.orderTable"),
					new Message("pay_msg_notSupportBrowser2", "smilepay.msg.notSupportBrowser2"),
    				new Message("pay_msg_paySuccess", "smilepay.msg.paySuccess"),
    				
    				new Message("confirm_ok", "confirm.ok"),
    				new Message("confirm_cancel", "confirm.cancel")
    			});
    	
   		String sp_mall_id = (String)request.getParameter("sp_mall_id");//가맹점 ID
    	String sp_res_cd = (String)request.getParameter("sp_res_cd");//응답 코드 
   		String sp_res_msg = (String)request.getParameter("sp_res_msg");//응답 메시지
   		String sp_tr_cd = (String)request.getParameter("sp_tr_cd");//거래요청 구분
   		String sp_ret_pay_type = (String)request.getParameter("sp_ret_pay_type");//결제 수단
   		String sp_trace_no = (String)request.getParameter("sp_trace_no");//전문추적번호
   		String sp_order_no = (String)request.getParameter("sp_order_no");//가맹점 주문번호
   		String sp_sessionkey = (String)request.getParameter("sp_sessionkey");//세션키
   		String sp_encrypt_data = (String)request.getParameter("sp_encrypt_data");//암호화 전문

   		String sp_mobilereserved1 = (String)request.getParameter("sp_mobilereserved1");
   		String sp_mobilereserved2 = (String)request.getParameter("sp_mobilereserved2");
   		String sp_reserved1 = (String)request.getParameter("sp_reserved1");
   		String sp_reserved2 = (String)request.getParameter("sp_reserved2");
   		String sp_reserved3 = (String)request.getParameter("sp_reserved3");
   		String sp_reserved4 = (String)request.getParameter("sp_reserved4");
   		String sp_card_code = (String)request.getParameter("sp_card_code");
   		String sp_eci_code = (String)request.getParameter("sp_eci_code");//MPI인 경우ECI코드
   		String sp_card_req_type = (String)request.getParameter("sp_card_req_type");
   		String sp_save_useyn = (String)request.getParameter("sp_save_useyn");
   		String sp_card_prefix = (String)request.getParameter("sp_card_prefix");
   		String sp_card_no_7 = (String)request.getParameter("sp_card_no_7");
   		String sp_spay_cp = (String)request.getParameter("sp_spay_cp");
   		String sp_prepaid_cp = (String)request.getParameter("sp_prepaid_cp");
   		
		try{
			sp_res_msg = URLDecoder.decode(sp_res_msg, "UTF-8");
			
		}catch(UnsupportedEncodingException e){
	    	logger.info("/easypay >> URLEncoder >>> sp_res_msg[{}]", e);
		}
   		
    	logger.info("/orderSubmit >> sp_res_cd >>> [{}]", sp_res_cd);						// 응답코드
    	logger.info("/orderSubmit >> sp_res_msg >>> [{}]", sp_res_msg);						// 응답메시지
    	logger.info("/orderSubmit >> sp_tr_cd >>> [{}]", sp_tr_cd);							// 결제창 요청 구분
    	logger.info("/orderSubmit >> sp_ret_pay_type >>> [{}]", sp_ret_pay_type);			// 결제 수단
    	logger.info("/orderSubmit >> sp_trace_no >>> [{}]", sp_trace_no);					// 추적번호
    	logger.info("/orderSubmit >> sp_order_no >>> [{}]", sp_order_no);					// 가맹점 주문번호
    	logger.info("/orderSubmit >> sp_sessionkey >>> [{}]", sp_sessionkey);				// 세션키 
    	logger.info("/orderSubmit >> sp_encrypt_data >>> [{}]", sp_encrypt_data);			// 암호화 전문
    	logger.info("/orderSubmit >> sp_mall_id >>> [{}]", sp_mall_id);						// 가맹점 ID
    	logger.info("/orderSubmit >> sp_mobilereserved1 >>> [{}]", sp_mobilereserved1);		// 여유필드 1(실패시 값 넘어오지 않음)
    	logger.info("/orderSubmit >> sp_mobilereserved2 >>> [{}]", sp_mobilereserved2);		// 여유필드 2(실패시 값 넘어오지 않음)
    	logger.info("/orderSubmit >> sp_reserved1 >>> [{}]", sp_reserved1);					// 여유필드 1(실패시 값 넘어오지 않음)
    	logger.info("/orderSubmit >> sp_reserved2 >>> [{}]", sp_reserved2);					// 여유필드 2(실패시 값 넘어오지 않음)
    	logger.info("/orderSubmit >> sp_reserved3 >>> [{}]", sp_reserved3);					// 여유필드 3(실패시 값 넘어오지 않음)
    	logger.info("/orderSubmit >> sp_reserved4 >>> [{}]", sp_reserved4);					// 여유필드 4(실패시 값 넘어오지 않음)
    	logger.info("/orderSubmit >> sp_card_code >>> [{}]", sp_card_code);					// 카드코드
    	logger.info("/orderSubmit >> sp_eci_code >>> [{}]", sp_eci_code);					// ECI 코드
    	logger.info("/orderSubmit >> sp_card_req_type >>> [{}]", sp_card_req_type);			// 거래구분(0:일반, 1:ISP, 2:MPI, 3:UPOP) 
    	logger.info("/orderSubmit >> sp_save_useyn >>> [{}]", sp_save_useyn);				// 카드사 세이브 여부(Y/N)
    	logger.info("/orderSubmit >> sp_card_prefix >>> [{}]", sp_card_prefix);				// 신용카드 prefix
    	logger.info("/orderSubmit >> sp_card_no_7 >>> [{}]", sp_card_no_7);					// 
    	logger.info("/orderSubmit >> sp_spay_cp >>> [{}]", sp_spay_cp);						// 간편결제 CP 코드
    	logger.info("/orderSubmit >> sp_prepaid_cp >>> [{}]", sp_prepaid_cp);				// 선불카드 CP

    	Store res = null;
		StoreOrderBasket basket = basketService.getOrderNum(sp_order_no);
		
		// basket 정보가 없다는건 이미 결제가 완료 되었다고 판단 되며
		// 해당 결제 내역을 조회 하여 주문번호 표출후 결제 완료 페이지로 이동시킨다
		if(basket == null){
			StoreOrder orderRes = storeOrderService.getOrderbyOrderNum(sp_order_no);
		logger.info("sp_order_no 없음 >> [{}]", sp_order_no);
		if(orderRes != null){
			StoreOrderPay orderPay = storePayService.getOrderPay(orderRes.getOrderPayId());
			res = storeService.getStore(orderRes.getStoreId());
			imgCommon(res, model);
			model.addAttribute("orderSequence", orderRes.getOrderSeq());
			model.addAttribute("authDate", orderPay.getPayAuthDate());
			model.addAttribute("stayMenuCnt",storeCookService.getStayCntMobile(orderRes.getStoreId()));
			model.addAttribute("orderTable", Util.parseString(orderRes.getOrderTable(), "0000") );
			logger.info("테스트_orderSequence : "+orderRes.getOrderSeq());
			logger.info("테스트_authDate : "+orderPay.getPayAuthDate());
			logger.info("테스트_stayMenuCnt : "+storeCookService.getStayCntMobile(orderRes.getStoreId()));
			logger.info("테스트_orderTable : "+Util.parseString(orderRes.getOrderTable(), "0000"));
			
				
				return "order/returnMobilePay";
			}else{
	    		model.addAttribute("code", sp_res_cd);
	    		model.addAttribute("gubun", "E");
	    		model.addAttribute("errorAdmin", sp_res_msg);
	    		
	    		// 결제가 실패했을 경우 에러 페이지
	    		return "order/stopMsg";
			}
		}else{
			res = storeService.getStore(basket.getStoreId());
		}
		
		logger.info("basket storeID key필요 >> [{}]", basket.getStoreId());
		logger.info("basket >> [{}]", basket.getBasketKey());
		logger.info("basket table >> [{}]", basket.getOrderTable());
		logger.info("basket time >> [{}]", basket.getOrderIntime());
		logger.info("basket order >> [{}]", basket.getOrderNumber());
		
		// 모바일 상점 로그 가져오기
		imgCommon(res, model);
		model.addAttribute("orderTable", Util.parseString(basket.getOrderTable(), "0000") );
		model.addAttribute("basket", basket.getBasketKey());
		model.addAttribute("order", basket.getOrderNumber());
		model.addAttribute("time", basket.getOrderIntime());
		
    	// 인증 등록이 실패 하였을 경우 
    	// order 페이지로 이동해야 함
    	// 인증 등록이 정상 적일 경우 결제 완료 페이지로 이동
		String successYn = "Y";
    	if("0000".equals(sp_res_cd)){
			successYn = easyPayService.easyPaySubmit(res.getId(), basket, sp_mall_id, sp_res_cd, sp_res_msg, sp_tr_cd, sp_ret_pay_type, sp_trace_no, 
					sp_order_no, sp_sessionkey, sp_encrypt_data, sp_mobilereserved1, sp_mobilereserved2, sp_reserved1, sp_reserved2, sp_reserved3, sp_reserved4, 
					sp_card_code, sp_eci_code, sp_card_req_type, sp_save_useyn, sp_card_prefix, sp_card_no_7, sp_spay_cp, sp_prepaid_cp, model, request, locale, session);
			// 최종 적으로 완료 되었을 경우 
			if("Y".equals(successYn)){
				// 1. 매장에서 쿠폰 / 포인트 / 사용안함 인지 판단
				
				// 2. 쿠폰
				// 2-1. 스탬프 정책을 가져온다
				// 2-2. 정책에 맞는 스탬프를 적립
				// 2-3. 쿠폰 정책을 가져온다. 
				// 2-4. 적립된 스탬프의 수가 쿠폰 발급 정책에 해당 될 경우 쿠폰 이 발급된다. 
				// 2-5. 만약 쿠폰이 발급 된 경우 발급정책의 스탬프 수를 제외한 나머지 스탬프를 적립한다.
				// 2-5-1. 쿠폰이 사용 되었을 경우 고객 전화번호로 검색하여 사용 표시 
				// 2-6. 누적스탬프 / 스탬프 적립 / 발급 쿠폰 ID / 사용적립여부 / 매장 ID / 고객전화번호 / 발급 기간 / 사용가능마지막 날짜(당일 11시59분59초 기준)LOG로 남긴다. 
				
				// 3. 포인트
				// 3-1. 포인트 정책 을 가져온다. 
				// 3-2. 정책에 맞는 포인트를 적립한다. 
				// 3-3. 누적 포인트 / 적립 포인트(사용 포인트) / 매장 ID / 고객 전화번호 / 적립 기간 LOG로 남긴다.
				
				// NO= 사용안함, CP= 쿠폰, PO= 포인트 
				String savingType = basket.getSavingType();
				
				model.addAttribute("savingType", savingType);
				
				if(!"NO".equals(savingType)){
					StoreOrder orderRes = storeOrderService.getOrderbyOrderNum(sp_order_no);
					String tel = orderRes.getTelNumber();
					int paymentAmt = orderRes.getPaymentAmt();
					
					if(Util.isValid(tel)){
						if("CP".equals(savingType)){
							
							stampCouponUpdate(res, orderRes, sp_order_no, session, model, locale);
							
						}else if("PO".equals(savingType)){
							// 포인트 적립시 사용
							List<StorePolicy> policyList = couponService.getPolicyList(res.getId(), "P");
							double percentage = 0;
							int point = 0;
							int usepoint = 0;
							if(policyList.size() > 0){
								for(StorePolicy one : policyList){
									percentage = one.getPercentage();	// 적립 포인트
									usepoint = one.getPoint();			// 1사용시 1원당 사용되는 포인트
								}
								
								BigDecimal bicTotalAmt = new BigDecimal(paymentAmt);
								BigDecimal bicPercentage = new BigDecimal(percentage);
								BigDecimal bicPoint = bicTotalAmt.multiply(bicPercentage);
								point = bicPoint.intValue();
								// 적립가능한 금액이 나왔을 때 1원당 사용 가능한 포인트로 계산하여 변환한다. 
								if(usepoint > 0){
									point = point / usepoint;
								}
							}
							
							StoreOrderPoint savePoint = null; 
							List<StoreOrderPoint> resList = couponService.getPointbyTel(res.getId(), tel);
							if(resList.size() > 0){
								for(StoreOrderPoint one : resList){
									one.setPointCnt(point);
									one.setPointTotal(one.getPointTotal() + point);
									
									savePoint = one;
								}
							}else{
								savePoint = new StoreOrderPoint(tel, point, point, res, session);
							}
							
							couponService.saveOrUpdate(savePoint);
							
							// 화면에 보여줘야 하는 내용
							
							DecimalFormat format = new DecimalFormat("###,###");
							String pointFormat = format.format(savePoint.getPointCnt());
							String pointTotalFormat = format.format(savePoint.getPointTotal());
							model.addAttribute("point", pointFormat);			//적립 포인트
							model.addAttribute("pointTotal", pointTotalFormat);	//총 포인트
							
							
							alimTalkSend(res, orderRes, "PO", tel, pointFormat, pointTotalFormat, locale);
						}
					}
				}
				
				// 주문번호도 같이 보내야 함
				// 결제가 성공했을 경우 성공 화면으로 이동 하여 "order/returnMobilePay"페이지 이동 
				if(basket != null){
					basketService.delete(basket);
				}
			}
    	}else{
    		// 사용자 취소 일 경우에만 
    		// 오더 정보로 조회 후 해당 내용으로 이동시킨다.
    		
    		// 고객결제에 대한 취소가 나왔을 경우에는 에러페이지로 이돟 후 그 화면에서 오더 정보 화면으로 이동하도록 만든다.
    		// storeKey="+storeKey+"&basket="+basket+"&table="+table+"&time="+time+"&order="+orderNumber
    		// basket 테이블에서 해당 내용을 조회 하여 전송 하도록 함
    		if("W002".equals(sp_res_cd)){
    			// [USER] 사용자 취소
    			// order 정보를 보내 삭제
        		// storeKey="+storeKey+"&basket="+basket+"&table="+table+"&time="+time+"&order="+orderNumber
    			
	    		model.addAttribute("menuMsg", sp_res_cd);
	    		model.addAttribute("gubun", "E");
	    		model.addAttribute("errorAdmin", "결제가 취소 되었습니다.");
	    		model.addAttribute("desc", sp_res_msg);
    			
    		}else{
    			// 그외 결제가 정상적이지 않을 경우
    			// order 정보를 보내지 않고 결제 불가에 대한 데이터 저장
        		// storeKey="+storeKey+"&basket="+basket+"&table="+table+"&time="+time+"&order="+orderNumber
	    		model.addAttribute("menuMsg", sp_res_cd);
	    		model.addAttribute("gubun", "E");
	    		model.addAttribute("errorAdmin", sp_res_msg);
    		}
    		
    		successYn = "N";
    	}
    	if(!"Y".equals(successYn)){
    		// 결제가 실패했을 경우 에러 페이지
    		return "order/stopMsg";
    	}
    	
		return "order/returnMobilePay";
    }
    
    
	public void imgCommon(Store store, Model model) {
		// 매장 화면 구성 데이터
		model.addAttribute("storeId", store.getId());
		model.addAttribute("storeName", store.getStoreName());
		
		model.addAttribute("url","/menu");
		model.addAttribute("oriUrl","/smartropay/order");
		model.addAttribute("oriUrlparam", store.getStoreKey());
		
		// 모바일 상점 로고 타입이 없을 경우 기본 "paycast" text 화면 출력
    	if(store.getStoreOpt() != null){
    		if(Util.isNotValid(store.getStoreOpt().getMobileLogoType())){
	    		// 로고가 없을 경우 
	    		model.addAttribute("mobileLogoType", "T");
	    		model.addAttribute("mobileLogoText","PayCast");
    		}else{
	    		String mLogoImageFilename = "";
	    		UploadFile mLogoImageFile = payService.getUploadFile(store.getStoreOpt().getMobileLogoImageId());
	    		if (mLogoImageFile != null) {
	    			mLogoImageFilename = mLogoImageFile.getFilename();
	    		}
	    		model.addAttribute("mobileLogoType", store.getStoreOpt().getMobileLogoType());
	    		model.addAttribute("mobileLogoText", store.getStoreOpt().getMobileLogoText());
	    		model.addAttribute("mLogoImageFilename", mLogoImageFilename);
				model.addAttribute("storeDownLocation", SolUtil.getUrlRoot("MobileTitle", store.getId()));
    		}
    	}else{
    		model.addAttribute("mobileLogoType", "T");
    		model.addAttribute("mobileLogoText","PayCast");
    	}
	}
	
	private void stampCouponUpdate(Store res, StoreOrder orderRes, String sp_order_no, HttpSession session, Model model, Locale locale){
		DecimalFormat format = new DecimalFormat("###,###");
		int dispStampCnt = 0;
		String dispCoupon = "N";
		
		// orderMenuCnt : 주문 총 갯수
    	int orderMenuCnt = Util.parseInt(orderRes.getGoodsTotal(), 0);
		String tel = orderRes.getTelNumber();
    	
		int inStamp = 0; // 최종 적립되는 스탬프 갯수
		// 스탬프 정책을 가져온다.
		// 정책에 따른 스탬프 총 갯수를 확인 => inStamp
		List<StorePolicy> policyList = couponService.getPolicyList(res.getId(), "S");
		if(policyList.size() > 0){
			int policyOrderStamp = 0, policyStamp = 0;;
			for(StorePolicy item : policyList){
				// 스팸프 적립 정책  : 주문수량{ }개 마다 스탬프 { }개 적립한다. 
				// 주문수량
				policyOrderStamp = item.getOrderAmt();
				// 스탬프 적립
				policyStamp = item.getStamp();
			}
			// 총 주문 갯수 와 주문수량(정책의 주문수량)을 나누어서 나오는 값  = 주문수량 {} 개 마다
			// 스탬프 적립 가능한 갯수와 스탬프 적립을 곱하여 나오는 값 = 스탬프 { }개 적립
			// 총 갯수 / 주문수량 = 스탬프 >>
			int cnt = orderMenuCnt / policyOrderStamp;
			// 스탬프 적립 가능 갯수 1개당 스탬프 적립 정책 갯수 를 곱해준다. 
			inStamp =  cnt * policyStamp;
		}
		
		if(inStamp > 0){
			// 최종적나온 스탬프를 저장 한다.
			// 저장 하기전 이전 스탬프를 조회 하여 최대값을 가져온다. 
			int maxStamp = 0;
			StoreOrderCoupon compStamp = null;
			List<StoreOrderCoupon> resStamp = couponService.getCouponStamp(res.getId(), tel, "S");
			if(resStamp.size() > 0){
				for(StoreOrderCoupon one : resStamp){
					maxStamp = one.getStampTotal() + inStamp;
					one.setStampCnt(inStamp);
					one.setStampTotal(maxStamp);
					compStamp = one;
				}
				
			}else{
				// 기존에 조회할 스탬프에 대한 값이 없을 경우 해당 내용을 저장한다. 
				compStamp = new StoreOrderCoupon(tel, inStamp, inStamp, res, session);
			}
			
			// 계산된 스탬프와 이전 총 갯수 스탬프를 더한 값을 저장 한다 .
			couponService.saveOrUpdate(compStamp);
			
			// 기존 스탬프를 가져와서 확인
			// inStamp 최종적으로 적립되는 스탬프 갯수
			int policyStamp = 0; 	// 쿠폰 발급  조건의 스탬프 갯수
			StoreCoupon coupon = null;	// 쿠폰 발급 조건이 맞을경우 발급 되는 쿠폰
			List<CouponPolicy> couponPolicyList = couponService.getCouponPolicyList(res.getId());
			if(couponPolicyList.size() > 0){
				for(CouponPolicy item : couponPolicyList){
					// 쿠폰 발급 정책  : 스탬프 { }개 모으면 쿠폰 { } 발급 
					StorePolicy policy = item.getPolicy();
					policyStamp = policy.getStamp();
					coupon = item.getCoupon();
				}
			}
			
			// 적립된 총 갯수가 쿠폰 발급 스탬프 갯수보다 클경우 쿠폰발급을 진행
			if(maxStamp >= policyStamp && coupon != null){
				SimpleDateFormat transToday = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat transEndDay = new SimpleDateFormat("MM-dd");
				String startDateSt = "", endDateSt = "";
				
				
				// 쿠폰 발급 하기 위한 갯수
				int issuable = maxStamp / policyStamp;
				// 쿠폰 발급 후에 남은 스탬프 
				int saveStamp = maxStamp % policyStamp;
				
				for(int i=0; i < issuable; i++){
					// 쿠폰 사용 가능한 날짜 계산 
					int validDate = coupon.getValidDate();
					
					// 오늘 날짜에 쿠폰 사용 마지막 날짜를 계산 
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.MONTH, validDate);
					Date endDate = Util.setMaxTimeOfDate(cal.getTime());
					
					StoreOrderCoupon issuableCoupon = new StoreOrderCoupon(tel, policyStamp, endDate, res, coupon, session);
					couponService.saveOrUpdate(issuableCoupon);
					
					startDateSt = transToday.format(new Date());
					endDateSt = transEndDay.format(endDate);
				}
				
				compStamp.setStampCnt(maxStamp - saveStamp);
				compStamp.setStampTotal(saveStamp);
				// 계산된 스탬프와 이전 총 갯수 스탬프를 더한 값을 저장 한다 .
				couponService.saveOrUpdate(compStamp);
				
				// 화면에 보여주는 쿠폰 과 스탬프
				dispStampCnt = inStamp;
				dispCoupon = coupon.getName();
				
				// 쿠폰이 발급이 되었을 경우 스탬프 알림톡과 쿠폰 알림톡을 전송
				try{
					// 스탬프 전송 / 적립된 스탬프와 쿠폰발급후 남은 스탬프를 보여준다.
					String inStampFormat = format.format(inStamp);
					String saveStampFormat = format.format(saveStamp);
					alimTalkSend(res, orderRes, "ST", tel, inStampFormat, saveStampFormat, locale);
					// 쿠폰 발급 전송
					alimTalkSend(res, orderRes, "CP", tel, coupon.getName(), startDateSt + " ~ " + endDateSt, locale);
				}catch (Exception e) {
		        	logger.error("stamp / coupon alarmUpdate ERROR", e);
				}
			}else{
				// 스탬프만 적립할 경우 사용, 쿠폰이 발급이 되지 않았을 경우 N이라는 값을 남긴다.
				dispStampCnt = inStamp;
				dispCoupon = "N";
				
				// 쿠폰 발급이 되지 않았을 경우 스탬프 알림톡만 전송
				String inStampFormat = format.format(inStamp);
				String maxStampFormat = format.format(maxStamp);
				alimTalkSend(res, orderRes, "ST", tel, inStampFormat, maxStampFormat, locale);
			}
			
		}
		
		// 화면에 보여줘야 하는 내용
		model.addAttribute("stamp", dispStampCnt);		//적립 스탬프
		model.addAttribute("coupon", dispCoupon);		//발급 쿠폰
	}
	
	// 포인트, 스탬프, 쿠폰 알림톡 전송
	@SuppressWarnings("unchecked")
	private void alimTalkSend(Store store, StoreOrder storeOrderOne, String type, String text0, String text1, String text2, Locale locale){
		
		if(store.isAlimTalkAllowed()){
			String senderKey = msgMgr.message("alimTalk.senderKey", locale);
			String tmplCd = "", subject = "", msg = "", smsmsg = "";
			if("PO".equals(type)){
				tmplCd = msgMgr.message("alimTalk.tmplPoint", locale);
				subject = msgMgr.message("alimTalk.subjectPoint", locale);
				msg = msgMgr.message("alimTalk.msgPoint", locale);
				smsmsg = msgMgr.message("alimTalk.smsmsgPoint", locale);
			}else if("ST".equals(type)){
				tmplCd = msgMgr.message("alimTalk.tmplStamp", locale);
				subject = msgMgr.message("alimTalk.subjectStamp", locale);
				msg = msgMgr.message("alimTalk.msgStamp", locale);
				smsmsg = msgMgr.message("alimTalk.smsmsgStamp", locale);
			}else if("CP".equals(type)){
				tmplCd = msgMgr.message("alimTalk.tmplCoupon", locale);
				subject = msgMgr.message("alimTalk.subjectCoupon", locale);
				msg = msgMgr.message("alimTalk.msgCoupon", locale);
				smsmsg = msgMgr.message("alimTalk.smsmsgCoupon", locale);
			}
			
			
			msg = msg.replace("{0}", phoneHyphenAdd(text0));
			msg = msg.replace("{1}", text1);
			msg = msg.replace("{2}", text2);
			msg = msg.replace("{3}", store.getPhone());
			smsmsg = smsmsg.replace("{0}", text0);
			smsmsg = smsmsg.replace("{1}", text1);
			
			logger.info("["+type+"]알림톡  >>> store.getBizName() [{}], store.getShortName() [{}]", store.getBizName(), store.getShortName());
			logger.info("["+type+"]알림톡  >>> store.getPhone() [{}], storeOrderOne.getOrderSeq() [{}]", store.getPhone(), storeOrderOne.getOrderSeq());
			logger.info("["+type+"]알림톡  >>> tel 전화번호. [{}], senderKey [{}]", storeOrderOne.getTelNumber(), senderKey);
			logger.info("["+type+"]알림톡  >>> tmplCd [{}], subject [{}]", tmplCd, subject);
			logger.info("["+type+"]알림톡  >>> msg [{}], smsmsg [{}]", msg, smsmsg);
			
			StoreAlimTalk alimTalk = new StoreAlimTalk(store.getShortName(), store.getStoreName(), store.getPhone(), storeOrderOne.getOrderSeq(), "", 
					"", storeOrderOne.getTelNumber(), senderKey, tmplCd, subject, msg, smsmsg);
			
//			alimTalkService.save(alimTalk);
			PayUtil.testServiceApi(alimTalk);

		}else{
			logger.info("["+type+"]알림톡 전송안됨  >>> store.getBizName() [{}], store.isAlimTalkAllowed() [{}]", store.getBizName(), store.isAlimTalkAllowed());
			logger.info("["+type+"]알림톡 전송안됨  >>> store.getBizName() [{}], store.getShortName() [{}]", store.getBizName(), store.getShortName());
		}
	}
	
	private static String phoneHyphenAdd(String num) {
	    String formatNum = "";
	    num = num.replaceAll("-","");
	    
	    if (num.length() == 11) {
            formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
	    }else if(num.length()==8){
	        formatNum = num.replaceAll("(\\d{4})(\\d{4})", "$1-$2");
	    }else{
	        if(num.indexOf("02")==0){
                formatNum = num.replaceAll("(\\d{2})(\\d{3,4})(\\d{4})", "$1-$2-$3");
	        }else{
	        	formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
	        }
	    }
	    return formatNum;
	}
}
