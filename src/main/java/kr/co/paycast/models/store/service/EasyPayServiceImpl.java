package kr.co.paycast.models.store.service;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.CouponPointService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderBasket;
import kr.co.paycast.models.store.StoreOrderCancel;
import kr.co.paycast.models.store.StoreOrderCook;
import kr.co.paycast.models.store.StoreOrderCoupon;
import kr.co.paycast.models.store.StoreOrderPay;
import kr.co.paycast.models.store.StoreOrderPoint;
import kr.co.paycast.utils.SmilePayUtil;
import kr.co.paycast.utils.Util;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.kicc.EasyPayClient;

@Transactional
@Service("EasyPayService")
public class EasyPayServiceImpl implements EasyPayService {
	private static final Logger logger = LoggerFactory.getLogger(EasyPayServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
	private MessageManager msgMgr;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private StoreOrderService storeOrderService;
	
    @Autowired
    private StoreCookService storeCookService;
    
    @Autowired
    private StorePayService storePayService;
    
    @Autowired
    private StoreCancelService storeCancelService;
    
    @Autowired
    private CouponPointService couponService;
    
	/* -------------------------------------------------------------------------- */
	/* ::: 처리구분 설정                                                          */
	/* -------------------------------------------------------------------------- */
	private static final String TRAN_CD_NOR_PAYMENT  = "00101000";   // 승인(일반, 에스크로)  
	private static final String TRAN_CD_NOR_MGR      = "00201000";   // 변경(일반, 에스크로)  
//	private static final String TRAN_CD_NOR_MALL     = "00201030";   // 셀러(다중정산)
    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	//결제가 완료 되었을 때 저장을 진행  모바일 결제와 키오스크 결제 에서 확인 가능
	@Override
	public String easyPaySubmit(int storeId, StoreOrderBasket basket, String sp_mall_id, String sp_res_cd, String sp_res_msg, String sp_tr_cd, String sp_ret_pay_type, String sp_trace_no, String sp_order_no, 
			String sp_sessionkey, String sp_encrypt_data, String sp_mobilereserved1, String sp_mobilereserved2, String sp_reserved1, String sp_reserved2, String sp_reserved3,
			String sp_reserved4, String sp_card_code, String sp_eci_code, String sp_card_req_type, String sp_save_useyn, String sp_card_prefix, String sp_card_no_7,
			String sp_spay_cp, String sp_prepaid_cp, Model model, HttpServletRequest request, Locale locale, HttpSession session) {
		
	    logger.info("매장 ID >>  storeId [{}]", storeId);
		
	    /* -------------------------------------------------------------------------- */
	    /* ::: 지불 정보 설정                                                         */
	    /* -------------------------------------------------------------------------- */
	    String GW_URL = msgMgr.message("kicc.gwUrl", locale);
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
	    String CERT_FILE            = msgMgr.message("kicc.certFile", locale);
	    String LOG_DIR              = msgMgr.message("kicc.logDir", locale);
	    int LOG_LEVEL               = 0;
	    
	    logger.info("kicc.gwUrl [{}]", GW_URL);
	    logger.info("kicc.certFile [{}]", CERT_FILE);
	    logger.info("kicc.logDir [{}]", LOG_DIR);
	    
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
	    if(TRAN_CD_NOR_PAYMENT.equals(tr_cd)){
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
	    
	    String bDBProc     = "Y";     //가맹점 DB처리 성공여부
		String orderSequence = "";
		StoreOrder orderRes = storeOrderService.getOrder(storeId, r_order_no);
		
		if(orderRes != null){
			// 결제된 내용을 저장
			StoreOrderPay orderPay = new StoreOrderPay();
			orderPay.setStoreId(storeId);
			orderPay.setOrderNumber(r_order_no);
			orderPay.setOrderTid(r_cno);
			orderPay.setPayMethod(r_pay_type);
			orderPay.setPayMid(sp_mall_id);
			orderPay.setPayAmt(r_amount);
			orderPay.setGoodsname(orderRes.getGoodsName());
			orderPay.setPayOid(r_order_no);
			orderPay.setPayAuthDate(r_tran_date);
			orderPay.setPayAuthCode(r_auth_no);
			orderPay.setPayResultCode(r_res_cd);
			orderPay.setPayResultMsg(r_res_msg);
			orderPay.setPaySignValue(sp_trace_no);	// 추적 번호
			orderPay.setPayFnCd(r_issuer_cd);
			orderPay.setPayFnName(r_issuer_nm);
			orderPay.setPayFnCd1(r_acquirer_cd);
			orderPay.setPayFnName1(r_acquirer_nm);
			orderPay.setPayCardQuota(r_install_period);
			orderPay.setPayDivideInfo(sp_encrypt_data);
			orderPay.setStatCd(r_stat_cd);
			orderPay.setStatMsg(r_stat_msg);
			orderPay.setPayType(r_pay_type);
			orderPay.setCardNo(r_card_no);
			orderPay.setNoint(r_noint);
			orderPay.setPartCancelYn(r_part_cancel_yn);
			orderPay.setCardGubun(r_card_gubun);
			orderPay.setCardBizGubun(r_card_biz_gubun);
			orderPay.setCponFlag(r_cpon_flag);
			orderPay.setBankCd(r_bank_cd);
			orderPay.setBankNm(r_bank_nm);
			orderPay.setAccountNo(r_account_no);
			orderPay.setDepositNm(r_deposit_nm);
			orderPay.setExpireDate(r_expire_date);
			orderPay.setCashResCd(r_cash_res_cd);
			orderPay.setCashResMsg(r_cash_res_msg);
			orderPay.setCashAuthNo(r_cash_auth_no);
			orderPay.setCashTranDate(r_cash_tran_date);
			orderPay.setCashIssueType(r_cash_issue_type);
			orderPay.setCashAuthType(r_cash_auth_type);
			orderPay.setCashAuthValue(r_cash_auth_value);
			orderPay.setAuthId(r_auth_id);
			orderPay.setBillid(r_billid);
			orderPay.setMobileNo(r_mobile_no);
			orderPay.setMobAnsimYn(r_mob_ansim_yn);
			orderPay.setCpCd(r_cp_cd);
			orderPay.setRemAmt(r_rem_amt);
			orderPay.setBkPayYn(r_bk_pay_yn);
			orderPay.touchWhoC(r_order_no);
			
			String replateAmt = r_amount.replace(",", "");
			String paymentAmtDb = String.valueOf(orderRes.getPaymentAmt());
			logger.info("[모바일 결제 완료]매장 번호와 주문 번호로 조회");
			logger.info("[모바일 결제 완료]r_order_no > [{}]", r_order_no);
			logger.info("[모바일 결제 완료]easyPaySubmit orderRes.getOrderPayId() >>> [{}]", orderRes.getOrderPayId());
			
		    if ("0000".equals(r_res_cd)) {
		    	// 결제가 성공했을 경우 
		    	try {
						logger.info("[easypay 결제 완료]r_order_no > [{}] easyPaySubmit Amt >>> [{}]", r_order_no,  r_amount);
						logger.info("[easypay 결제 완료]r_order_no > [{}] easyPaySubmit replateAmt >>> [{}]", r_order_no,  replateAmt);
						logger.info("[easypay 결제 완료]r_order_no > [{}] easyPaySubmit orderRes.getGoodsAmt() >>> [{}]", r_order_no,  paymentAmtDb);
						
						/* r_amount가 주문DB의 금액과 다를 시 반드시 취소 요청을 하시기 바랍니다.     */
						if(!paymentAmtDb.equals(r_amount)){
							// 결제 금액이 다르기 때문에 취소 요청 후 에러 처리를 해야 한다. 
							// 에러에 대한 코드 값을 저장한 후이기 때문에 프린트에 내려가지 않도록 StoreOrder 정보의 프린트 Y / N을 E로 변경 한다. 
	        				StoreOrder condFile = storeOrderService.getDplyStoreOrderPrint(orderRes.getId());
	    					condFile.setOrderPrint("H");
	    					condFile.touchWho(session);
	    					storeOrderService.saveOrUpdate(condFile);
	    					
	    					logger.error("결제된 내용의 결제 금액과 저장되어 있는 총 결제 금액이 같은지 비교");
							logger.error("error 거래번호 TID(r_cno)>>> [{}] / 결제 주문 번호  OID >>> [{}]", r_cno, r_order_no);
							logger.error("error 결제 하기 전 총 금액 orderRes.getGoodsAmt() >>> [{}]", orderRes.getGoodsAmt());
							logger.error("error 결제 받은 총 금액 Amt >>> [{}]", replateAmt);
				    		logger.error("easyPaySubmit > payError > M007 ");
				    		model.addAttribute("menuMsg", "M007");
				    		model.addAttribute("gubun", "B");
				    		model.addAttribute("errorAdmin", "관리자에게 문의 바랍니다.");
				    		model.addAttribute("desc", r_order_no);
							
							// 금액이 다르므로 취소처리 신청
							bDBProc = "N";     
						}else{
							if(orderRes.getOrderPayId() > 0){
								orderSequence = orderRes.getOrderSeq();
							}else{
								orderSequence = storeOrderService.saveOrderPay(orderRes, orderPay, locale, session);
								
								StoreOrder storeOrderOne = storeOrderService.getDplyStoreOrderPrint(orderRes.getId());
								storeOrderOne.setOrderParent(-1);
								storeOrderOne.setDiscountAmt(basket.getBasketDiscount());
								storeOrderOne.setPaymentAmt(basket.getBasketPayment());
								storeOrderOne.setSavingType(basket.getSavingType());
								storeOrderOne.setUseCoupon(basket.getCouponId());
								
								storeOrderService.saveOrUpdate(storeOrderOne);
								logger.info("[모바일 결제 완료]easyPaySubmit orderSequence >>> [{}]", orderSequence);
								logger.info("[모바일 결제 완료]easyPaySubmit ResultCode >>> [{}]", r_res_cd);
								
								// 4. 저장된 결제 내역에 대한 주방용 메뉴 리스트 작성 : STORE_ORDER_COOK TABLE 저장
								// 4-1. 3001(카드 결제 성공)이 아닐 경우 주방용 메뉴 리스트에 넣지 않는다. - 위에서 에러
								// 4-2. "실제 스마트로 서버의 승인 값을 검증 하기 위해서 값 " 끝나면 결제된 내용의 결제 금액과 저장되어 있는 총 결제 금액이 같은지 비교(스마트로 권장사항)
								//      금액이 같지 않을 경우 주방용 메뉴 리스트에 넣지 않는다. - 위에서 에러
								// FCM 을 보낼때 주방용 패드를 체크하여 없을 경우 해당 StoreOrderCook에 데이터를 넣지 않는다.
								StoreOrderCook storeOrderCook = new StoreOrderCook(orderRes.getStoreId(), orderRes.getId(), orderRes.getOrderNumber());
								logger.info("[모바일 결제 완료]FCM 보내기 전>>>r_order_no  [{}]", r_order_no);
								//FCM 전송  : 모바일 결제완료가 되어 DB에 저장 되었을 경우 fcm으로 stb 에 알려준다.
								storeOrderService.fcmTransmission(storeId, storeOrderCook);
								
								
								// 5. 쿠폰 및 포인트를 사용하였을 경우  
								// 5-1. 쿠폰은 사용된 쿠폰을 조회되지 않도록 한다.
								// 5-2. 포인트일 경우 사용된 포인트를 제외 한다. 
								if("CP".equals(basket.getSavingType())){
									StoreOrderCoupon coupon = couponService.getCoupon(basket.getCouponId());
									// 쿠폰 사용을 하지 않을 경우 null 체크
									if(coupon != null){
										coupon.setUseState(1);
										couponService.saveOrUpdate(coupon);
									}
								}else if("PO".equals(basket.getSavingType())){
									StoreOrderPoint savePoint = null; 
									List<StoreOrderPoint> resList = couponService.getPointbyTel(storeId, storeOrderOne.getTelNumber());
									logger.info("resList.size() [{}]", resList.size());
									
									if(resList.size() > 0){
										for(StoreOrderPoint one : resList){
											if(basket.getBasketDiscount() > 0){
												one.setPointCnt(-basket.getBasketDiscount());
												one.setPointTotal(one.getPointTotal() - basket.getBasketDiscount());

											}else{
												one.setPointCnt(basket.getBasketDiscount());
												one.setPointTotal(one.getPointTotal());
											}

											savePoint = one;
										}
										
										couponService.saveOrUpdate(savePoint);
									}
								}
									
								// 주방용 패드가 있을 경우에 대한 대기번호 순수
								// 없을 경우 9999 반환
								model.addAttribute("stayMenuCnt",storeCookService.getStayCntMobile(orderRes.getStoreId()));
							}
							
							model.addAttribute("orderSequence", orderSequence);
							model.addAttribute("authDate", r_tran_date);
						}
				} catch (Exception e) {
					
					// 에러에 대한 코드 값을 저장한 후이기 때문에 프린트에 내려가지 않도록 StoreOrder 정보의 프린트 Y / N을 E로 변경 한다. 
					StoreOrder condFile = storeOrderService.getDplyStoreOrderPrint(orderRes.getId());
					condFile.setOrderPrint("E");
					condFile.touchWho(session);
					storeOrderService.saveOrUpdate(condFile);
					
					logger.error("error (DB 저장 도중 에러)  거래번호 TID(r_cno)>>> [{}] / 결제 주문 번호  OID >>> [{}]", r_cno, r_order_no);
					logger.error("error 결제 하기 전 총 금액 orderRes.getGoodsAmt() >>> [{}]", orderRes.getGoodsAmt());
					logger.error("error 결제 받은 총 금액 Amt >>> [{}]", replateAmt);
		    		logger.error("easyPaySubmit > payError > M999 [{}]", e);
		    		model.addAttribute("menuMsg", "M999");
		    		model.addAttribute("gubun", "E");
		    		model.addAttribute("errorAdmin", "관리자에게 문의 바랍니다.");
		    		model.addAttribute("desc", r_order_no);
					
					// DB처리 성공 시 "", 실패 시 "N"
					logger.error("easyPaySubmit >> Exception (DB 저장 실패)>>> e.getMessage()[{}] / e[{}]", e.getMessage(), e);
					bDBProc = "N";     
				}
		    	
		    	// DB 저장 실패 및 금액이 상이할 경우  취소  처리 하기!(필수)
		        if("N".equals(bDBProc)){
		        	easyPaySubmitCancel(easyPayClient, r_cno, order_no, mall_id, tr_cd, r_escrow_yn, request);
		            return bDBProc;
		        }
		    }else{
				logger.info("[모바일 결제 실패]easyPaySubmit orderSequence >>> [{}]", orderSequence);
				logger.info("[모바일 결제 실패]easyPaySubmit ResultCode >>> [{}]", r_res_cd);
		    	// 결제가 실패 했을 경우에도 실패에 대한 DB를 남기며 에러페이지로 이동  
				orderSequence = storeOrderService.saveOrderPay(orderRes, orderPay, locale, session);
				
				// 에러에 대한 코드 값을 저장한 후이기 때문에 프린트에 내려가지 않도록 StoreOrder 정보의 프린트 Y / N을 E로 변경 한다. 
				StoreOrder condFile = storeOrderService.getDplyStoreOrderPrint(orderRes.getId());
				condFile.setOrderPrint("E");
				condFile.touchWho(session);
				storeOrderService.saveOrUpdate(condFile);
				logger.error("easyPaySubmit > FAIL : [{}]", "//0000(결제 성공)이 아닐 경우 에러 메시지 출력");
				logger.error("[모바일 결제 완료]error 거래번호 TID(r_cno)>>> [{}] / 결제 주문 번호  OID >>> [{}]", r_cno, r_order_no);
				logger.error("[모바일 결제 완료]error 결제 에러 코드 r_res_cd  >>> [{}], 결제 에러 메시지 ResultMsg >>> [{}]", r_res_cd, r_res_msg);
	    		logger.error("returnMobilePay > STEP4 FAIL : [{}]", "M006");
	    		
	    		model.addAttribute("menuMsg", r_res_cd);
	    		model.addAttribute("gubun", "E");
	    		model.addAttribute("errorAdmin", r_res_msg);
	    		model.addAttribute("desc", r_order_no);
	    		
		    	return "N";
		    }
		}else{
			// StoreOrder 정보 조회시 해당 값을 찾을 수 없을 경우 에러 발생 및 결제 취소
			logger.error("easyPaySubmit > FAIL : [StoreOrder orderRes 를 찾을수가 없습니다. ]");
			logger.error("[모바일 결제 완료]error 거래번호 TID(r_cno)>>> [{}] / 결제 주문 번호  OID >>> [{}]", r_cno, r_order_no);
			logger.error("[모바일 결제 완료]error 결제 에러 코드 r_res_cd  >>> [{}], 결제 에러 메시지 ResultMsg >>> [{}]", r_res_cd, r_res_msg);
    		logger.error("returnMobilePay > STEP4 FAIL : [{}]", "M006");
    		model.addAttribute("code", "M006");
    		model.addAttribute("gubun", "C");
    		model.addAttribute("resultCode", r_res_cd);
    		model.addAttribute("resultMsg", r_res_msg);
    		model.addAttribute("errorAdmin", r_res_msg);
    		model.addAttribute("info", r_order_no);
			
			//정상 결제가 되었지만 주문정보를 찾을수 없으므로 해당 결제 취소 처리 
			if ("0000".equals(r_res_cd)) {
				easyPaySubmitCancel(easyPayClient, r_cno, order_no, mall_id, tr_cd, r_escrow_yn, request);
			}
			
	    	return "N";
		}
		
	    return bDBProc;
	}
	
	// 승인요청 후 DB 저장 실패 및 금액이 다른 경우 해당 요청내용은 취소처리 
	public static void easyPaySubmitCancel(EasyPayClient easyPayClient, String r_cno, String order_no, String mall_id, String tr_cd, String r_escrow_yn, HttpServletRequest request) {
		 
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
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_msg",  "DB 처리 실패 및 금액 상이로 인한 망취소"  );
      
        easyPayClient.easypay_run( mall_id, tr_cd, order_no );
      
        String r_res_cd = easyPayClient.res_cd;
        String r_res_msg = easyPayClient.res_msg;
        String r_canc_date = easyPayClient.easypay_get_res("canc_date");    //취소일시
        r_cno = easyPayClient.easypay_get_res("cno");    //PG거래번호 
        
        logger.info("취소 후 easyPayCancel >>> r_res_cd [{}] / r_res_msg [{}]", r_res_cd, r_res_msg);
        logger.info("취소 후 easyPayCancel >>> r_canc_date [{}] (취소일시) / r_cno [{}] (PG거래번호 )", r_canc_date, r_cno);
    }

	@Override
	public String easyPayCancel(int storeId, int storeOrderId, String verifiCode, HttpServletRequest request, Locale locale, HttpSession session) {
		Store res = storeService.getStore(storeId);
		String mall_id = res.getStoreEtc().getEpStoreKey();
		StoreOrder storeOrder = storeOrderService.getDplyStoreOrderPrint(storeOrderId);
		if(storeOrder == null){
			logger.error("menuCancel storeOrder null > payError > M008 ");
			return msgMgr.message("smilepay.M008", locale);
		}
		StoreOrderPay storeOrderPay = storePayService.getOrderPay(storeOrder.getOrderPayId());
		if(storeOrderPay == null){
			logger.error("menuCancel storeOrderPay null > payError > M008 ");
			return msgMgr.message("smilepay.M008", locale);
		}
		
	    String mgr_txtype       = "40";         // [필수]거래구분         
	    String mgr_subtype      = ""; // Util.parseString(mgr_subtype);        // [선택]변경세부구분     
	    String org_cno          = storeOrderPay.getOrderTid();            // [필수]원거래고유번호   
	    String mgr_amt          = ""; // Util.parseString(mgr_amt);            // [선택]금액             
	    String mgr_rem_amt      = ""; //  Util.parseString(mgr_rem_amt);        // [선택]부분취소 잔액    
	    String mgr_bank_cd      = ""; // Util.parseString(mgr_bank_cd);        // [선택]환불계좌 은행코드
	    String mgr_account      = ""; // Util.parseString(mgr_account);        // [선택]환불계좌 번호    
	    String mgr_depositor    = ""; // Util.parseString(mgr_depositor);      // [선택]환불계좌 예금주명
		
	    String GW_URL = msgMgr.message("kicc.gwUrl", locale);
	    final String GW_PORT              = "80";                    // 포트번호(변경불가)
	    String CERT_FILE            = msgMgr.message("kicc.certFile", locale);
	    String LOG_DIR              = msgMgr.message("kicc.logDir", locale);
	    int LOG_LEVEL               = 0;
	    
	    String r_res_cd             = "";     //응답코드
	    String r_res_msg            = "";     //응답메시지
	    EasyPayClient easyPayClient = new EasyPayClient();
	    easyPayClient.easypay_setenv_init( GW_URL, GW_PORT, CERT_FILE, LOG_DIR, LOG_LEVEL );
	    easyPayClient.easypay_reqdata_init();
    	logger.info("<<<<<<<<<<<<<<<<< 변경관리 요청 >>>>>>>>>>>>>>>>>");
        int easypay_mgr_data_item;
        easypay_mgr_data_item = easyPayClient.easypay_item( "mgr_data" );

        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_txtype"    , mgr_txtype    );           // [필수]거래구분                  
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_subtype"   , mgr_subtype   );           // [선택]변경세부구분   
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "org_cno"       , org_cno       );           // [필수]원거래고유번호  
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_amt"       , mgr_amt       );           // [선택]금액       
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_rem_amt"   , mgr_rem_amt   );           // [선택]부분취소 잔액  
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_bank_cd"   , mgr_bank_cd   );           // [선택]환불계좌 은행코드
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_account"   , mgr_account   );           // [선택]환불계좌 번호  
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_depositor" , mgr_depositor );           // [선택]환불계좌 예금주명
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "req_ip"        , SmilePayUtil.getRemoteAddr(request) ); // [필수]요청자 IP
        easyPayClient.easypay_run( mall_id, TRAN_CD_NOR_MGR, storeOrder.getOrderNumber() );
        
        r_res_cd = easyPayClient.res_cd;
        r_res_msg = easyPayClient.res_msg;
        
	    String r_cno              = easyPayClient.easypay_get_res( "cno"             );     //PG거래번호
	    String r_mgr_amt          = easyPayClient.easypay_get_res( "mgr_amt"         );     //부분 취소 / 환율 금액
	    String r_amount           = easyPayClient.easypay_get_res( "amount"          );     //총 결제금액
	    String r_order_no         = easyPayClient.easypay_get_res( "order_no"        );     //주문번호
	    String r_tran_date        = easyPayClient.easypay_get_res( "tran_date"       );     //승인일시
	    String r_escrow_yn        = easyPayClient.easypay_get_res( "escrow_yn"       );     //에스크로 사용유무
	    String r_stat_cd          = easyPayClient.easypay_get_res( "stat_cd"         );     //상태코드
	    String r_stat_msg         = easyPayClient.easypay_get_res( "stat_msg"        );     //상태메시지
	    String r_pay_type         = easyPayClient.easypay_get_res( "pay_type"        );     //결제수단
	    String r_cash_res_cd      = easyPayClient.easypay_get_res( "cash_res_cd"     );     //현금영수증 결과코드
	    String r_cash_res_msg     = easyPayClient.easypay_get_res( "cash_res_msg"    );     //현금영수증 결과메세지
	    String r_cash_auth_no     = easyPayClient.easypay_get_res( "cash_auth_no"    );     //현금영수증 승인번호
	    String r_cash_tran_date   = easyPayClient.easypay_get_res( "cash_tran_date"  );     //현금영수증 승인일시
	    String r_canc_acq_date    = easyPayClient.easypay_get_res( "canc_acq_date"   );     //매입취소일시
	    String r_canc_date        = easyPayClient.easypay_get_res( "canc_date"       );     //취소일시
	    String r_refund_date      = easyPayClient.easypay_get_res( "refund_date"     );     //환불예정일시
	    
	    logger.info("응답 코드 >>  r_res_cd [{}]",r_res_cd); 
	    logger.info("응답 메시지  >>  r_res_msg [{}]",r_res_msg);
	    logger.info("PG거래번호 >>  r_cno [{}]",r_cno); 
	    logger.info("부분 취소 / 환율 금액 >>  r_mgr_amt [{}]",r_mgr_amt); 
	    logger.info("총 결제금액 >>  r_amount [{}]",r_amount);   
	    logger.info("주문번호 >>    r_order_no [{}]",r_order_no);   
	    logger.info("승인일시 >>    r_tran_date [{}]",r_tran_date);  
	    logger.info("에스크로 사용유무          r_escrow_yn [{}]",r_escrow_yn); 
	    logger.info("상태코드 >>    r_stat_cd [{}]",r_stat_cd); 
	    logger.info("상태메시지 >>   r_stat_msg [{}]",r_stat_msg); 
	    logger.info("결제수단 >>    r_pay_type [{}]",r_pay_type); 
	    logger.info("현금영수증 결과코드         r_cash_res_cd [{}]",r_cash_res_cd);
	    logger.info("현금영수증 결과메세지        r_cash_res_msg [{}]",r_cash_res_msg); 
	    logger.info("현금영수증 승인번호         r_cash_auth_no [{}]",r_cash_auth_no); 
	    logger.info("현금영수증 승인일시         r_cash_tran_date [{}]",r_cash_tran_date);
	    logger.info("매입취소일시 >>  r_canc_acq_date [{}]",r_canc_acq_date);
	    logger.info("취소일시 >>    r_canc_date [{}]",r_canc_date); 
	    logger.info("환불예정일시 >>  r_refund_date [{}]",r_refund_date);
	    
	 
        StoreOrderCancel storeOrderCancel = new StoreOrderCancel();
        storeOrderCancel.setPayMethod(r_pay_type);
        storeOrderCancel.setPayName(r_pay_type);
        storeOrderCancel.setMid(mall_id);
        storeOrderCancel.setTid(r_cno);
        storeOrderCancel.setCancelAmt(r_amount);
        storeOrderCancel.setCancelMSG(r_res_cd +"_"+ r_res_msg);
        storeOrderCancel.setResultCode(r_stat_cd);
        storeOrderCancel.setResultMsg(r_stat_msg);
        storeOrderCancel.setCancelDate(r_canc_date);
        storeOrderCancel.setCancelTime("");
        storeOrderCancel.setCancelNum(r_cno);
        storeOrderCancel.setMoid(r_order_no);
        storeOrderCancel.touchWhoC(r_order_no);
        String success="true";
        if("0000".equals(r_res_cd)) {
        	// 취소 및 환불 성공에 따른 가맹점 비지니스 로직 구현 필요
	        storeCancelService.cancelSuccessSave(storeOrderCancel, storeId, verifiCode);
        } else {
        	// 취소 및 환불 실패에 따른 가맹점 비지니스 로직 구현 필요
	        storeCancelService.cancelFailSave(storeOrderCancel, storeId, verifiCode);
	        success = r_res_msg;
        }
	    
	    return success;
	}
	
}
