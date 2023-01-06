package kr.co.paycast.models.store.service;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderPay;
import kr.co.paycast.utils.SmilePayUtil;
import kr.co.paycast.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.kicc.EasyPayClient;

@SuppressWarnings("unchecked")
public class EasyPayUtil {
	private static final Logger logger = LoggerFactory.getLogger(EasyPayUtil.class);

	/* -------------------------------------------------------------------------- */
	/* ::: 처리구분 설정                                                          */
	/* -------------------------------------------------------------------------- */
	private static final String TRAN_CD_NOR_PAYMENT  = "00101000";   // 승인(일반, 에스크로)  
	private static final String TRAN_CD_NOR_MGR      = "00201000";   // 변경(일반, 에스크로)  
//	private static final String TRAN_CD_NOR_MALL     = "00201030";   // 셀러(다중정산)
	
	@SuppressWarnings("unused")
	@Autowired
	private MessageManager msgMgr;
    
	@Autowired
	public void setStaticMsgMgr(MessageManager msgMgr) {
		EasyPayUtil.sMsgMgr = msgMgr;
	}
	@Autowired
	public void setStaticStoreService(StoreService storeService) {
		EasyPayUtil.sStoreService = storeService;
	}
	@Autowired
	public void setStaticStoreOrderService(StoreOrderService storeOrderService) {
		EasyPayUtil.sStoreOrderService = storeOrderService;
	}
	
	static MessageManager sMsgMgr;
	static StoreService sStoreService;
    static StoreOrderService sStoreOrderService;
	
	public static String easyPaySubmit(int storeId, String GW_URL, String CERT_FILE, String LOG_DIR, String sp_mall_id, String sp_res_cd, String sp_res_msg, String sp_tr_cd, String sp_ret_pay_type, String sp_trace_no, String sp_order_no, 
				String sp_sessionkey, String sp_encrypt_data, String sp_mobilereserved1, String sp_mobilereserved2, String sp_reserved1, String sp_reserved2, String sp_reserved3,
				String sp_reserved4, String sp_card_code, String sp_eci_code, String sp_card_req_type, String sp_save_useyn, String sp_card_prefix, String sp_card_no_7,
				String sp_spay_cp, String sp_prepaid_cp, Model model, HttpServletRequest request, Locale locale, HttpSession session){
		
	    /* -------------------------------------------------------------------------- */
	    /* ::: 지불 정보 설정                                                         */
	    /* -------------------------------------------------------------------------- */
//	    String GW_URL = sMsgMgr.message("kicc.gwUrl", locale);
		
		
		logger.info("kicc.gwUrl [{}]", GW_URL);
		logger.info("kicc.certFile [{}]", CERT_FILE);
		logger.info("kicc.logDir [{}]", LOG_DIR);
//	    String GW_URL               = "testgw.easypay.co.kr";  // Gateway URL ( test )
//	    String GW_URL              = "gw.easypay.co.kr";      // Gateway URL ( real )
	    final String GW_PORT              = "80";                    // 포트번호(변경불가) 
		
	    /* -------------------------------------------------------------------------- */ 
	    /* ::: 지불 데이터 셋업 (업체에 맞게 수정)                                    */ 
	    /* -------------------------------------------------------------------------- */ 
	    /*     ※ 주의 ※                                                             */ 
	    /*     #cert_file 변수 설정                                                   */
	    /*       - pg_cert.pem 파일이 있는 디렉토리의 절대 경로 설정                  */ 
	    /*     #log_dir 변수 설정                                                     */
	    /*       - log 디렉토리 설정                                                  */                              
	    /*     #log_level 변수 설정                                                   */
	    /*       - log 레벨 설정                                                      */
	    /* -------------------------------------------------------------------------- */
//	    String CERT_FILE            = sMsgMgr.message("kicc.certFile", locale);
//	    String LOG_DIR              = sMsgMgr.message("kicc.logDir", locale);
	    int LOG_LEVEL               = 0;
	    
	    /* -------------------------------------------------------------------------- */
	    /* ::: 승인요청 정보 설정                                                     */
	    /* -------------------------------------------------------------------------- */
	    //[헤더]
	    String tr_cd             = Util.parseString(sp_tr_cd);           // [필수]결제창 요청구분
	    String trace_no          = Util.parseString(sp_trace_no);        // [필수]추적번호
	    String order_no          = Util.parseString(sp_order_no);        // [필수]가맹점 주문번호
	    String mall_id           = Util.parseString(sp_mall_id);         // [필수]가맹점 ID
	    //[공통]
	    String encrypt_data      = Util.parseString(sp_encrypt_data);    // [필수]암호화전문
	    String sessionkey        = Util.parseString(sp_sessionkey);      // [필수]세션키   
	    
	    /* -------------------------------------------------------------------------- */
	    /* ::: 결제 결과                                                              */
	    /* -------------------------------------------------------------------------- */
	        
	    String r_res_cd             = "";     //응답코드
	    String r_res_msg            = "";     //응답메시지
	    
	    /* -------------------------------------------------------------------------- */
	    /* ::: EasyPayClient 인스턴스 생성 [변경불가 !!].                             */
	    /* -------------------------------------------------------------------------- */
	    EasyPayClient easyPayClient = new EasyPayClient();
	    easyPayClient.easypay_setenv_init( GW_URL, GW_PORT, CERT_FILE, LOG_DIR, LOG_LEVEL );
	    easyPayClient.easypay_reqdata_init();
	    
	    /* -------------------------------------------------------------------------- */
	    /* ::: 승인요청                                                               */
	    /* -------------------------------------------------------------------------- */
	    if( TRAN_CD_NOR_PAYMENT.equals(tr_cd) ){
	        // 승인요청 전문 설정
	        easyPayClient.easypay_set_trace_no(trace_no);
	        easyPayClient.easypay_encdata_set(encrypt_data, sessionkey);
	    }
	    
	    /* -------------------------------------------------------------------------- */
	    /* ::: 실행                                                                   */
	    /* -------------------------------------------------------------------------- */         
	    if ( tr_cd.length() > 0 ) {
	        easyPayClient.easypay_run( mall_id, tr_cd, order_no );
	        
	        r_res_cd = easyPayClient.res_cd;
	        r_res_msg = easyPayClient.res_msg;
	    } 
	    else {
	        r_res_cd  = "M114";
	        r_res_msg = "연동 오류|tr_cd값이 설정되지 않았습니다.";
	    } 
	    
	    /* -------------------------------------------------------------------------- */
	    /* ::: 결과 처리                                                              */
	    /* -------------------------------------------------------------------------- */
	    String r_cno              = easyPayClient.easypay_get_res( "cno"             );     //PG거래번호
	    String r_amount           = easyPayClient.easypay_get_res( "amount"          );     //총 결제금액
	    String r_order_no         = easyPayClient.easypay_get_res( "order_no"        );     //주문번호
	    String r_auth_no          = easyPayClient.easypay_get_res( "auth_no"         );     //승인번호
	    String r_tran_date        = easyPayClient.easypay_get_res( "tran_date"       );     //승인일시
	    String r_escrow_yn        = easyPayClient.easypay_get_res( "escrow_yn"       );     //에스크로 사용유무
	    String r_complex_yn       = easyPayClient.easypay_get_res( "complex_yn"      );     //복합결제 유무
	    String r_stat_cd          = easyPayClient.easypay_get_res( "stat_cd"         );     //상태코드
	    String r_stat_msg         = easyPayClient.easypay_get_res( "stat_msg"        );     //상태메시지
	    String r_pay_type         = easyPayClient.easypay_get_res( "pay_type"        );     //결제수단
	    String r_card_no          = easyPayClient.easypay_get_res( "card_no"         );     //카드번호
	    String r_issuer_cd        = easyPayClient.easypay_get_res( "issuer_cd"       );     //발급사코드
	    String r_issuer_nm        = easyPayClient.easypay_get_res( "issuer_nm"       );     //발급사명
	    String r_acquirer_cd      = easyPayClient.easypay_get_res( "acquirer_cd"     );     //매입사코드
	    String r_acquirer_nm      = easyPayClient.easypay_get_res( "acquirer_nm"     );     //매입사명
	    String r_install_period   = easyPayClient.easypay_get_res( "install_period"  );     //할부개월
	    String r_noint            = easyPayClient.easypay_get_res( "noint"           );     //무이자여부
	    String r_part_cancel_yn   = easyPayClient.easypay_get_res( "part_cancel_yn"  );     //부분취소 가능여부
	    String r_card_gubun       = easyPayClient.easypay_get_res( "card_gubun"      );     //신용카드 종류
	    String r_card_biz_gubun   = easyPayClient.easypay_get_res( "card_biz_gubun"  );     //신용카드 구분
	    String r_cpon_flag        = easyPayClient.easypay_get_res( "cpon_flag"       );     //쿠폰사용 유무
	    String r_bank_cd          = easyPayClient.easypay_get_res( "bank_cd"         );     //은행코드
	    String r_bank_nm          = easyPayClient.easypay_get_res( "bank_nm"         );     //은행명
	    String r_account_no       = easyPayClient.easypay_get_res( "account_no"      );     //계좌번호
	    String r_deposit_nm       = easyPayClient.easypay_get_res( "deposit_nm"      );     //입금자명
	    String r_expire_date      = easyPayClient.easypay_get_res( "expire_date"     );     //계좌사용만료일
	    String r_cash_res_cd      = easyPayClient.easypay_get_res( "cash_res_cd"     );     //현금영수증 결과코드
	    String r_cash_res_msg     = easyPayClient.easypay_get_res( "cash_res_msg"    );     //현금영수증 결과메세지
	    String r_cash_auth_no     = easyPayClient.easypay_get_res( "cash_auth_no"    );     //현금영수증 승인번호
	    String r_cash_tran_date   = easyPayClient.easypay_get_res( "cash_tran_date"  );     //현금영수증 승인일시
	    String r_cash_issue_type  = easyPayClient.easypay_get_res( "cash_issue_type" );     //현금영수증 발행용도
	    String r_cash_auth_type   = easyPayClient.easypay_get_res( "cash_auth_type"  );     //현금영수증 인증구분
	    String r_cash_auth_value  = easyPayClient.easypay_get_res( "cash_auth_value" );     //현금영수증 인증번호
	    String r_auth_id          = easyPayClient.easypay_get_res( "auth_id"         );     //휴대폰 PhoneID
	    String r_billid           = easyPayClient.easypay_get_res( "billid"          );     //휴대폰 인증번호
	    String r_mobile_no        = easyPayClient.easypay_get_res( "mobile_no"       );     //휴대폰번호
	    String r_mob_ansim_yn     = easyPayClient.easypay_get_res( "mob_ansim_yn"    );     //안심결제 사용유무
	    String r_cp_cd            = easyPayClient.easypay_get_res( "cp_cd"           );     //포인트사/쿠폰사         
	    String r_rem_amt          = easyPayClient.easypay_get_res( "rem_amt"         );     //잔액
	    String r_bk_pay_yn        = easyPayClient.easypay_get_res( "bk_pay_yn"       );     //장바구니 결제여부 
	    String r_canc_acq_date    = easyPayClient.easypay_get_res( "canc_acq_date"   );     //매입취소일시
	    String r_canc_date        = easyPayClient.easypay_get_res( "canc_date"       );     //취소일시
	    String r_refund_date      = easyPayClient.easypay_get_res( "refund_date"     );     //환불예정일시
	    
	    logger.info("응답 코드 >>  r_res_cd [{}]",r_res_cd); 
	    logger.info("응답 메시지  >>  r_res_msg [{}]",r_res_msg); 
	    
	    logger.info("PG거래번호 >>  r_cno [{}]",r_cno); 
	    logger.info("총 결제금액 >>  r_amount [{}]",r_amount);   
	    logger.info("주문번호 >>    r_order_no [{}]",r_order_no);   
	    logger.info("승인번호 >>    r_auth_no [{}]",r_auth_no);
	    logger.info("승인일시 >>    r_tran_date [{}]",r_tran_date);  
	    logger.info("에스크로 사용유무          r_escrow_yn [{}]",r_escrow_yn); 
	    logger.info("복합결제 유무 >> r_complex_yn [{}]",r_complex_yn);
	    logger.info("상태코드 >>    r_stat_cd [{}]",r_stat_cd); 
	    logger.info("상태메시지 >>   r_stat_msg [{}]",r_stat_msg); 
	    logger.info("결제수단 >>    r_pay_type [{}]",r_pay_type); 
	    logger.info("카드번호 >>    r_card_no [{}]",r_card_no);
	    logger.info("발급사코드 >>   r_issuer_cd [{}]",r_issuer_cd); 
	    logger.info("발급사명 >>    r_issuer_nm [{}]",r_issuer_nm); 
	    logger.info("매입사코드 >>   r_acquirer_cd [{}]",r_acquirer_cd); 
	    logger.info("매입사명 >>    r_acquirer_nm [{}]",r_acquirer_nm);
	    logger.info("할부개월 >>    r_install_period [{}]",r_install_period); 
	    logger.info("무이자여부 >>   r_noint [{}]",r_noint); 
	    logger.info("부분취소 가능여부          r_part_cancel_yn [{}]",r_part_cancel_yn); 
	    logger.info("신용카드 종류 >> r_card_gubun [{}]",r_card_gubun);
	    logger.info("신용카드 구분 >> r_card_biz_gubun [{}]",r_card_biz_gubun); 
	    logger.info("쿠폰사용 유무 >> r_cpon_flag [{}]",r_cpon_flag); 
	    logger.info("은행코드 >>    r_bank_cd [{}]",r_bank_cd); 
	    logger.info("은행명 >>     r_bank_nm [{}]",r_bank_nm);
	    logger.info("계좌번호 >>    r_account_no [{}]",r_account_no); 
	    logger.info("입금자명 >>    r_deposit_nm [{}]",r_deposit_nm); 
	    logger.info("계좌사용만료일 >> r_expire_date [{}]",r_expire_date); 
	    logger.info("현금영수증 결과코드         r_cash_res_cd [{}]",r_cash_res_cd);
	    logger.info("현금영수증 결과메세지        r_cash_res_msg [{}]",r_cash_res_msg); 
	    logger.info("현금영수증 승인번호         r_cash_auth_no [{}]",r_cash_auth_no); 
	    logger.info("현금영수증 승인일시         r_cash_tran_date [{}]",r_cash_tran_date);
	    logger.info("현금영수증 발행용도         r_cash_issue_type [{}]",r_cash_issue_type); 
	    logger.info("현금영수증 인증구분         r_cash_auth_type [{}]",r_cash_auth_type); 
	    logger.info("현금영수증 인증번호         r_cash_auth_value [{}]",r_cash_auth_value);
	    logger.info("휴대폰 PhoneID        r_auth_id [{}]",r_auth_id); 
	    logger.info("휴대폰 인증번호           r_billid [{}]",r_billid); 
	    logger.info("휴대폰번호 >>   r_mobile_no [{}]",r_mobile_no); 
	    logger.info("안심결제 사용유무          r_mob_ansim_yn [{}]",r_mob_ansim_yn);
	    logger.info("포인트사/쿠폰사           r_cp_cd [{}]",r_cp_cd); 
	    logger.info("잔액 >>      r_rem_amt [{}]",r_rem_amt); 
	    logger.info("장바구니 결제여부          r_bk_pay_yn [{}]",r_bk_pay_yn); 
	    logger.info("매입취소일시 >>  r_canc_acq_date [{}]",r_canc_acq_date);
	    logger.info("취소일시 >>    r_canc_date [{}]",r_canc_date); 
	    logger.info("환불예정일시 >>  r_refund_date [{}]",r_refund_date);
	    /* -------------------------------------------------------------------------- */
	    /* ::: 가맹점 DB 처리                                                         */
	    /* -------------------------------------------------------------------------- */
	    /* 응답코드(res_cd)가 "0000" 이면 정상승인 입니다.                            */
	    /* r_amount가 주문DB의 금액과 다를 시 반드시 취소 요청을 하시기 바랍니다.     */
	    /* DB 처리 실패 시 취소 처리를 해주시기 바랍니다.                             */
	    /* -------------------------------------------------------------------------- */
	    
	    String bDBProc     = "";     //가맹점 DB처리 성공여부
	    if ("0000".equals(r_res_cd)) {
	    	// 결제가 성공했을 경우 
	    	try {
				String orderSequence = "";
				StoreOrder orderRes = sStoreOrderService.getOrder(storeId, r_order_no);
				
				if(orderRes != null){
					logger.info("[모바일 결제 완료]매장 번호와 주문 번호로 조회");
					logger.info("[모바일 결제 완료]r_order_no > [{}]", r_order_no);
					logger.info("[모바일 결제 완료]returnMobilePay orderRes.getOrderPayId() >>> [{}]", orderRes.getOrderPayId());
					
					if(orderRes.getOrderPayId() > 0){
						orderSequence = orderRes.getOrderSeq();
					}else{
						// 결제된 내용을 저장
						StoreOrderPay orderPay = new StoreOrderPay();
						orderPay.setStoreId(storeId);
						orderPay.setOrderNumber(r_order_no);
						orderPay.setOrderTid(r_cno);
						orderPay.setPayMethod(r_pay_type);
						orderPay.setPayMid(r_auth_no);
						orderPay.setPayAmt(r_amount);
						orderPay.setGoodsname(orderRes.getGoodsName());
						orderPay.setPayOid(r_order_no);
						orderPay.setPayAuthDate(r_tran_date);
//						orderPay.setPayAuthCode(AuthCode);
//						orderPay.setPayResultCode(ResultCode);
//						orderPay.setPayResultMsg(ResultMsg);
//						orderPay.setPaySignValue(SignValue);
//						orderPay.setPayFnCd(fn_cd);
//						orderPay.setPayFnName(fn_name);
//						orderPay.setPayCardQuota(CardQuota);
//						orderPay.setPayAcquCardcode(AcquCardCode);
//						orderPay.setPayDivideInfo(DivideInfo);
//						orderPay.touchWhoC(OID);
//						
//						// 웹 링크 버전일 경우에 실제 스마트로 서버의 승인 값을 검증 하기 위해서 값을 비교 합니다.
//						if (SignValue.equals(VerifySignValue)) {
//							logger.info("SignValue.equals(VerifySignValue) >>> [{}]", "검증 성공");
//						}else{
//							logger.error("error PayMethod >>> [{}]", PayMethod);
//							logger.error("error MID >>> [{}]", MID);
//							logger.error("error Amt >>> [{}]", Amt);
//							logger.error("error BuyerName >>> [{}]", BuyerName);
//							logger.error("error GoodsName >>> [{}]", GoodsName);
//							logger.error("error mallUserID 고객사 회원 ID >>> [{}]", mallUserID);
//							logger.error("error TID 거래번호 >>> [{}]", TID);
//							logger.error("error OID 주문번호 >>> [{}]", OID);
//							logger.error("error AuthDate 승인일자 >>> [{}]", AuthDate);
//							logger.error("error AuthCode 승인번호 >>> [{}]", AuthCode);
//							logger.error("error ResultCode 결과코드 >>> [{}]", ResultCode);
//							logger.error("error ResultMsg 결과메시지 >>> [{}]", ResultMsg);
//							logger.error("error BuyerTel >>> [{}]", BuyerTel);
//							logger.error("error BuyerEmail >>> [{}]", BuyerEmail);
//							logger.error("error SignValue >>> [{}]", SignValue);
//							logger.error("error fn_cd >>> [{}]", fn_cd);
//							logger.error("error fn_name 결제카드사명 >>> [{}]", fn_name);
//							logger.error("error CardQuota >>> [{}]", CardQuota);
//							logger.error("error AcquCardCode 매입사코드  >>> [{}]", AcquCardCode);
//							logger.error("error ReceiptType 현금영수증유형  >>> [{}]", ReceiptType);
//							logger.error("error VbankNum 가상계좌번호  >>> [{}]", VbankNum);
//							logger.error("error VbankName 가상계좌은행명  >>> [{}]", VbankName);
//							logger.error("error DivideInfo >>> [{}]", DivideInfo);
//							logger.error("error merchantKey >>> [{}]", merchantKey);
//							logger.error("error VerifySignValue >>> [{}]", VerifySignValue);
//
//							logger.error("returnMobilePay > STEP4  storeKey [{}], table(orderRes.getOrderTable()) [{}]", res.getStoreKey(), orderRes.getOrderTable());
//							logger.error("returnMobilePay > STEP4  basket : [{}]", basket);
//							logger.error("returnMobilePay > STEP4  OID [{}], TID [{}]", OID, TID);
//							request.setAttribute("storeKey", res.getStoreKey());
//							request.setAttribute("table", orderRes.getOrderTable());
//							request.setAttribute("code", "M005");
//							request.setAttribute("gubun", "B");
//							request.setAttribute("info", OID);
//							
//							return "forward:/stopViewUrl";
//						}
//						
//						orderSequence = sStoreOrderService.saveOrderPay(orderRes, orderPay, locale, session);
//						
//						logger.info("[모바일 결제 완료]returnMobilePay orderSequence >>> [{}]", orderSequence);
//						logger.info("[모바일 결제 완료]returnMobilePay ResultCode >>> [{}]", ResultCode);
//						
//						//3001(카드 결제 성공)이 아닐 경우 에러 메시지 출력
//						if(!"3001".equals(ResultCode)){
//							// 에러에 대한 코드 값을 저장한 후이기 때문에 프린트에 내려가지 않도록 StoreOrder 정보의 프린트 Y / N을 E로 변경 한다. 
//	        				StoreOrder condFile = storeOrderService.getDplyStoreOrderPrint(orderRes.getId());
//	    					condFile.setOrderPrint("E");
//	    					condFile.touchWho(session);
//	    					storeOrderService.saveOrUpdate(condFile);
//							logger.error("returnMobilePay > STEP4 FAIL : [{}]", "//3001(카드 결제 성공)이 아닐 경우 에러 메시지 출력");
//	    					logger.error("[모바일 결제 완료]error 거래번호 TID >>> [{}] 결제 주문 번호  OID [{}]", TID, OID);
//							logger.error("[모바일 결제 완료]error 결제 에러 코드 ResultCode  >>> [{}], 결제 에러 메시지 ResultMsg >>> [{}]", ResultCode, ResultMsg);
//				    		logger.error("returnMobilePay > STEP4 FAIL : [{}]", "M006");
//							request.setAttribute("code", "M006");
//							request.setAttribute("gubun", "C");
//							request.setAttribute("resultCode", ResultCode);
//							request.setAttribute("resultMsg", ResultMsg);
//							request.setAttribute("info", OID);
//							
//							return "forward:/stopViewUrl";
//						}
//						
//						// "실제 스마트로 서버의 승인 값을 검증 하기 위해서 값 " 끝나면 결제된 내용의 결제 금액과 저장되어 있는 총 결제 금액이 같은지 비교(스마트로 권장사항)
//						int replateAmt = Integer.parseInt(Amt.replace("원", ""));
//						logger.info("[모바일 결제 완료]OID > [{}] returnMobilePay Amt >>> [{}]", OID,  Amt);
//						logger.info("[모바일 결제 완료]OID > [{}] returnMobilePay replateAmt >>> [{}]", OID,  replateAmt);
//						logger.info("[모바일 결제 완료]OID > [{}] returnMobilePay orderRes.getGoodsAmt() >>> [{}]", OID,  orderRes.getGoodsAmt());
//						if(orderRes.getGoodsAmt() != replateAmt){
//							// 에러에 대한 코드 값을 저장한 후이기 때문에 프린트에 내려가지 않도록 StoreOrder 정보의 프린트 Y / N을 E로 변경 한다. 
//	        				StoreOrder condFile = storeOrderService.getDplyStoreOrderPrint(orderRes.getId());
//	    					condFile.setOrderPrint("H");
//	    					condFile.touchWho(session);
//	    					storeOrderService.saveOrUpdate(condFile);
//	    					
//	    					logger.info("결제된 내용의 결제 금액과 저장되어 있는 총 결제 금액이 같은지 비교");
//							logger.info("error 거래번호 TID >>> [{}]", TID);
//							logger.info("error 결제 주문 번호  OID >>> [{}]", OID);
//							logger.info("error 결제 하기 전 총 금액 orderRes.getGoodsAmt() >>> [{}]", orderRes.getGoodsAmt());
//							logger.info("error 결제 받은 총 금액 Amt >>> [{}]", replateAmt);
//							
//				    		logger.info("returnMobilePay > payError > M007 ");
//							request.setAttribute("code", "M007");
//							request.setAttribute("gubun", "B");
//							request.setAttribute("info", OID);
//							
//							return "forward:/stopViewUrl";
//						}
//						
//						// 4. 저장된 결제 내역에 대한 주방용 메뉴 리스트 작성 : STORE_ORDER_COOK TABLE 저장
//						// 4-1. 3001(카드 결제 성공)이 아닐 경우 주방용 메뉴 리스트에 넣지 않는다. - 위에서 에러
//						// 4-2. "실제 스마트로 서버의 승인 값을 검증 하기 위해서 값 " 끝나면 결제된 내용의 결제 금액과 저장되어 있는 총 결제 금액이 같은지 비교(스마트로 권장사항)
//						//      금액이 같지 않을 경우 주방용 메뉴 리스트에 넣지 않는다. - 위에서 에러
//						// FCM 을 보낼때 주방용 패드를 체크하여 없을 경우 해당 StoreOrderCook에 데이터를 넣지 않는다.
//						StoreOrderCook storeOrderCook = new StoreOrderCook(orderRes.getStoreId(), orderRes.getId(), orderRes.getOrderNumber());
//						
//						logger.info("[모바일 결제 완료]FCM 보내기 전>>> [{}]", OID);
//						
//						//FCM 전송  : 모바일 결제완료가 되어 DB에 저장 되었을 경우 fcm으로 stb 에 알려준다.
//						storeOrderService.fcmTransmission(res.getId(), storeOrderCook);
//						
//						logger.info("[모바일 결제 완료]FCM 보내기 후>>> [{}]", OID);
					}
				}
			} catch (Exception e) {
				logger.info("easyPayReady >> Exception >>> e[{}]", e);
				bDBProc = "false";     // DB처리 성공 시 "true", 실패 시 "false"
			}
	        if ( bDBProc.equals("false") ) {
	            // DB 저장 실패시 취소  처리 하기!
	        	// 에러 페이지 이동 필요
	            if( TRAN_CD_NOR_PAYMENT.equals(tr_cd) ) {
	            	easyPayCancel(easyPayClient, r_cno, order_no, mall_id, tr_cd, r_escrow_yn, request);
	            }
	            
	            return "fail";
	        }
	    }else{
	    	// 결제가 실패 했을 경우에도 실패에 대한 DB를 남기며 에러페이지로 이동  
	    	logger.info("결제 실패"); 
//		    logger.info("PG거래번호 >>  r_cno [{}]",r_cno); 
//		    logger.info("총 결제금액 >>  r_amount [{}]",r_amount);   
//		    logger.info("주문번호 >>    r_order_no [{}]",r_order_no);   
//		    logger.info("승인번호 >>    r_auth_no [{}]",r_auth_no);
//		    logger.info("승인일시 >>    r_tran_date [{}]",r_tran_date);  
//		    logger.info("에스크로 사용유무          r_escrow_yn [{}]",r_escrow_yn); 
//		    logger.info("복합결제 유무 >> r_complex_yn [{}]",r_complex_yn);
//		    logger.info("상태코드 >>    r_stat_cd [{}]",r_stat_cd); 
//		    logger.info("상태메시지 >>   r_stat_msg [{}]",r_stat_msg); 
//		    logger.info("결제수단 >>    r_pay_type [{}]",r_pay_type); 
//		    logger.info("카드번호 >>    r_card_no [{}]",r_card_no);
//		    logger.info("발급사코드 >>   r_issuer_cd [{}]",r_issuer_cd); 
//		    logger.info("발급사명 >>    r_issuer_nm [{}]",r_issuer_nm); 
//		    logger.info("매입사코드 >>   r_acquirer_cd [{}]",r_acquirer_cd); 
//		    logger.info("매입사명 >>    r_acquirer_nm [{}]",r_acquirer_nm);
//		    logger.info("할부개월 >>    r_install_period [{}]",r_install_period); 
//		    logger.info("무이자여부 >>   r_noint [{}]",r_noint); 
//		    logger.info("부분취소 가능여부          r_part_cancel_yn [{}]",r_part_cancel_yn); 
//		    logger.info("신용카드 종류 >> r_card_gubun [{}]",r_card_gubun);
//		    logger.info("신용카드 구분 >> r_card_biz_gubun [{}]",r_card_biz_gubun); 
//		    logger.info("쿠폰사용 유무 >> r_cpon_flag [{}]",r_cpon_flag); 
//		    logger.info("은행코드 >>    r_bank_cd [{}]",r_bank_cd); 
//		    logger.info("은행명 >>     r_bank_nm [{}]",r_bank_nm);
//		    logger.info("계좌번호 >>    r_account_no [{}]",r_account_no); 
//		    logger.info("입금자명 >>    r_deposit_nm [{}]",r_deposit_nm); 
//		    logger.info("계좌사용만료일 >> r_expire_date [{}]",r_expire_date); 
//		    logger.info("현금영수증 결과코드         r_cash_res_cd [{}]",r_cash_res_cd);
//		    logger.info("현금영수증 결과메세지        r_cash_res_msg [{}]",r_cash_res_msg); 
//		    logger.info("현금영수증 승인번호         r_cash_auth_no [{}]",r_cash_auth_no); 
//		    logger.info("현금영수증 승인일시         r_cash_tran_date [{}]",r_cash_tran_date);
//		    logger.info("현금영수증 발행용도         r_cash_issue_type [{}]",r_cash_issue_type); 
//		    logger.info("현금영수증 인증구분         r_cash_auth_type [{}]",r_cash_auth_type); 
//		    logger.info("현금영수증 인증번호         r_cash_auth_value [{}]",r_cash_auth_value);
//		    logger.info("휴대폰 PhoneID        r_auth_id [{}]",r_auth_id); 
//		    logger.info("휴대폰 인증번호           r_billid [{}]",r_billid); 
//		    logger.info("휴대폰번호 >>   r_mobile_no [{}]",r_mobile_no); 
//		    logger.info("안심결제 사용유무          r_mob_ansim_yn [{}]",r_mob_ansim_yn);
//		    logger.info("포인트사/쿠폰사           r_cp_cd [{}]",r_cp_cd); 
//		    logger.info("잔액 >>      r_rem_amt [{}]",r_rem_amt); 
//		    logger.info("장바구니 결제여부          r_bk_pay_yn [{}]",r_bk_pay_yn); 
//		    logger.info("매입취소일시 >>  r_canc_acq_date [{}]",r_canc_acq_date);
//		    logger.info("취소일시 >>    r_canc_date [{}]",r_canc_date); 
//		    logger.info("환불예정일시 >>  r_refund_date [{}]",r_refund_date);
	    }
	    model.addAttribute("r_cno", r_cno);
	    model.addAttribute("r_amount", r_amount);
	    model.addAttribute("r_order_no", r_order_no);
	    model.addAttribute("r_auth_no", r_auth_no);
	    model.addAttribute("r_tran_date", r_tran_date);
	    model.addAttribute("r_escrow_yn", r_escrow_yn);
	    model.addAttribute("r_complex_yn", r_complex_yn);
	    model.addAttribute("r_stat_cd", r_stat_cd);
	    model.addAttribute("r_stat_msg", r_stat_msg);
	    model.addAttribute("r_pay_type", r_pay_type);
	    model.addAttribute("r_card_no", r_card_no);
	    model.addAttribute("r_issuer_cd", r_issuer_cd);
	    model.addAttribute("r_issuer_nm", r_issuer_nm);
	    model.addAttribute("r_acquirer_cd", r_acquirer_cd);
	    model.addAttribute("r_acquirer_nm", r_acquirer_nm);
	    model.addAttribute("r_install_period", r_install_period);
	    model.addAttribute("r_noint", r_noint);
	    model.addAttribute("r_part_cancel_yn", r_part_cancel_yn);
	    model.addAttribute("r_card_gubun", r_card_gubun);
	    model.addAttribute("r_card_biz_gubun", r_card_biz_gubun);
	    model.addAttribute("r_cpon_flag", r_cpon_flag);
	    model.addAttribute("r_bank_cd", r_bank_cd);
	    model.addAttribute("r_bank_nm", r_bank_nm);
	    model.addAttribute("r_account_no", r_account_no);
	    model.addAttribute("r_deposit_nm", r_deposit_nm);
	    model.addAttribute("r_expire_date", r_expire_date);
	    model.addAttribute("r_cash_res_cd", r_cash_res_cd);
	    model.addAttribute("r_cash_res_msg", r_cash_res_msg);
	    model.addAttribute("r_cash_auth_no", r_cash_auth_no);
	    model.addAttribute("r_cash_tran_date", r_cash_tran_date);
	    model.addAttribute("r_cash_issue_type", r_cash_issue_type);
	    model.addAttribute("r_cash_auth_type", r_cash_auth_type);
	    model.addAttribute("r_cash_auth_value", r_cash_auth_value);
	    model.addAttribute("r_auth_id", r_auth_id);
	    model.addAttribute("r_billid", r_billid);
	    model.addAttribute("r_mobile_no", r_mobile_no);
	    model.addAttribute("r_mob_ansim_yn", r_mob_ansim_yn);
	    model.addAttribute("r_cp_cd", r_cp_cd);
	    model.addAttribute("r_rem_amt", r_rem_amt);
	    model.addAttribute("r_bk_pay_yn", r_bk_pay_yn);
	    model.addAttribute("r_canc_acq_date", r_canc_acq_date);
	    model.addAttribute("r_canc_date", r_canc_date);
	    model.addAttribute("r_refund_date", r_refund_date);
	    
	    return "success";
	}
	
	public static void easyPayCancel(EasyPayClient easyPayClient, String r_cno, String order_no, String mall_id, String tr_cd, String r_escrow_yn, HttpServletRequest request) {
		 
        int easypay_mgr_data_item;
        
        easyPayClient.easypay_reqdata_init();
      
        tr_cd = TRAN_CD_NOR_MGR; 
        easypay_mgr_data_item = easyPayClient.easypay_item( "mgr_data" );
        if ( !r_escrow_yn.equals( "Y" ) ) {
            easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_txtype", "40"   );
        }
        else {
            easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_txtype",  "61"   );
            easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_subtype", "ES02" );
        }
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "org_cno",  r_cno     );
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "order_no", order_no  );
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "req_ip",   SmilePayUtil.getRemoteAddr(request) );
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "req_id",   "MALL_R_TRANS" );
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_msg",  "DB 처리 실패로 망취소"  );
      
        easyPayClient.easypay_run( mall_id, tr_cd, order_no );
      
        String r_res_cd = easyPayClient.res_cd;
        String r_res_msg = easyPayClient.res_msg;
        String r_canc_date = easyPayClient.easypay_get_res("canc_date");    //취소일시
        r_cno = easyPayClient.easypay_get_res("cno");    //PG거래번호 
        
        logger.info("취소 후  >>> r_res_cd [{}]",r_res_cd);
        logger.info("취소 후  >>> r_res_msg [{}]",r_res_msg);
        logger.info("취소 후  >>> r_canc_date [{}] (취소일시)",r_canc_date);
        logger.info("취소 후  >>> r_cno [{}] (PG거래번호 )",r_cno);
        
    }
	
	
}
