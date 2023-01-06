<!--KICC와 전문통신페이지-->
<!--메뉴얼 '승인페이지 작성' 승인요청/승인응답 파라미터 포함.-->

<META NAME="ROBOTS" CONTENT="NOINDEX, NOFOLLOW">
<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="com.kicc.*" %>
<%!
    /*
     * 파라미터 체크 메소드
     */
    public String getNullToSpace(String param) 
    {
        return (param == null) ? "" : param.trim();
    }
%>
<%
    /* -------------------------------------------------------------------------- */
    /* ::: 처리구분 설정                                                          */
    /* -------------------------------------------------------------------------- */
    final String TRAN_CD_NOR_PAYMENT  = "00101000";   // 승인(일반, 에스크로)  
    final String TRAN_CD_NOR_MGR      = "00201000";   // 변경(일반, 에스크로)  
    final String TRAN_CD_NOR_MALL     = "00201030";   // 셀러(다중정산)

    /* -------------------------------------------------------------------------- */
    /* ::: 지불 정보 설정                                                         */
    /* -------------------------------------------------------------------------- */
    final String GW_URL               = "testgw.easypay.co.kr";  // Gateway URL ( test )
   //final String GW_URL              = "gw.easypay.co.kr";      // Gateway URL ( real )
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
    final String CERT_FILE            = "/service/pgweb/jspdoc/easypay_test/EP8.0Webpay_Mobile/easypay80_webpay_mobile_jsp/cert";
    final String LOG_DIR              = "/service/pgweb/jspdoc/easypay_test/EP8.0Webpay_Mobile/easypay80_webpay_mobile_jsp/log";
    final int LOG_LEVEL               = 0;
  
    /* -------------------------------------------------------------------------- */
    /* ::: 승인요청 정보 설정                                                     */
    /* -------------------------------------------------------------------------- */
    //[헤더]
    String tr_cd             = getNullToSpace(request.getParameter("sp_tr_cd"));           // [필수]결제창 요청구분
    String trace_no          = getNullToSpace(request.getParameter("sp_trace_no"));        // [필수]추적번호
    String order_no          = getNullToSpace(request.getParameter("sp_order_no"));        // [필수]가맹점 주문번호
    String mall_id           = getNullToSpace(request.getParameter("sp_mall_id"));         // [필수]가맹점 ID
    //[공통]
    String encrypt_data      = getNullToSpace(request.getParameter("sp_encrypt_data"));    // [필수]암호화전문
    String sessionkey        = getNullToSpace(request.getParameter("sp_sessionkey"));      // [필수]세션키   
    
    /* -------------------------------------------------------------------------- */
    /* ::: 변경관리 정보 설정                                                     */
    /* -------------------------------------------------------------------------- */
    String mgr_txtype       = getNullToSpace(request.getParameter("mgr_txtype"));         // [필수]거래구분         
    String mgr_subtype      = getNullToSpace(request.getParameter("mgr_subtype"));        // [선택]변경세부구분     
    String org_cno          = getNullToSpace(request.getParameter("org_cno"));            // [필수]원거래고유번호   
    String mgr_amt          = getNullToSpace(request.getParameter("mgr_amt"));            // [선택]금액             
    String mgr_rem_amt      = getNullToSpace(request.getParameter("mgr_rem_amt"));        // [선택]부분취소 잔액    
    String mgr_bank_cd      = getNullToSpace(request.getParameter("mgr_bank_cd"));        // [선택]환불계좌 은행코드
    String mgr_account      = getNullToSpace(request.getParameter("mgr_account"));        // [선택]환불계좌 번호    
    String mgr_depositor    = getNullToSpace(request.getParameter("mgr_depositor"));      // [선택]환불계좌 예금주명

    /* -------------------------------------------------------------------------- */
    /* ::: 결제 결과                                                              */
    /* -------------------------------------------------------------------------- */
        
    String r_res_cd             = "";     //응답코드
    String r_res_msg            = "";     //응답메시지
    String r_cno                = "";     //PG거래번호
    String r_amount             = "";     //총 결제금액
    String r_order_no           = "";     //주문번호
    String r_auth_no            = "";     //승인번호
    String r_tran_date          = "";     //승인일시
    String r_escrow_yn          = "";     //에스크로 사용유무
    String r_complex_yn         = "";     //복합결제 유무
    String r_stat_cd            = "";     //상태코드
    String r_stat_msg           = "";     //상태메시지
    String r_pay_type           = "";     //결제수단
    String r_card_no            = "";     //카드번호
    String r_issuer_cd          = "";     //발급사코드
    String r_issuer_nm          = "";     //발급사명
    String r_acquirer_cd        = "";     //매입사코드
    String r_acquirer_nm        = "";     //매입사명
    String r_install_period     = "";     //할부개월
    String r_noint              = "";     //무이자여부
    String r_part_cancel_yn     = "";     //부분취소 가능여부
    String r_card_gubun         = "";     //신용카드 종류
    String r_card_biz_gubun     = "";     //신용카드 구분
    String r_cpon_flag          = "";     //쿠폰사용유무
    String r_bank_cd            = "";     //은행코드
    String r_bank_nm            = "";     //은행명
    String r_account_no         = "";     //계좌번호
    String r_deposit_nm         = "";     //입금자명
    String r_expire_date        = "";     //계좌사용만료일
    String r_cash_res_cd        = "";     //현금영수증 결과코드
    String r_cash_res_msg       = "";     //현금영수증 결과메세지
    String r_cash_auth_no       = "";     //현금영수증 승인번호
    String r_cash_tran_date     = "";     //현금영수증 승인일시
    String r_cash_issue_type    = "";     //현금영수증 발행용도
    String r_cash_auth_type     = "";     //현금영수증 인증구분
    String r_cash_auth_value    = "";     //현금영수증 인증번호
    String r_auth_id            = "";     //휴대폰 PhoneID
    String r_billid             = "";     //휴대폰 인증번호
    String r_mobile_no          = "";     //휴대폰번호
    String r_mob_ansim_yn       = "";     //안심결제 사용유무
    String r_cp_cd              = "";     //포인트사/쿠폰사
    String r_rem_amt            = "";     //잔액
    String r_bk_pay_yn          = "";     //장바구니 결제여부
    String r_canc_acq_date      = "";     //매입취소일시
    String r_canc_date          = "";     //취소일시
    String r_refund_date        = "";     //환불예정일시
   
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
        
    /* -------------------------------------------------------------------------- */
    /* ::: 변경관리 요청                                                          */
    /* -------------------------------------------------------------------------- */
    }else if( TRAN_CD_NOR_MGR.equals( tr_cd ) ) {

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
        easyPayClient.easypay_deli_us( easypay_mgr_data_item, "req_ip"        , request.getRemoteAddr() ); // [필수]요청자 IP
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
    
    
    r_cno              = easyPayClient.easypay_get_res( "cno"             );     //PG거래번호
    r_amount           = easyPayClient.easypay_get_res( "amount"          );     //총 결제금액
    r_order_no         = easyPayClient.easypay_get_res( "order_no"        );     //주문번호
    r_auth_no          = easyPayClient.easypay_get_res( "auth_no"         );     //승인번호
    r_tran_date        = easyPayClient.easypay_get_res( "tran_date"       );     //승인일시
    r_escrow_yn        = easyPayClient.easypay_get_res( "escrow_yn"       );     //에스크로 사용유무
    r_complex_yn       = easyPayClient.easypay_get_res( "complex_yn"      );     //복합결제 유무
    r_stat_cd          = easyPayClient.easypay_get_res( "stat_cd"         );     //상태코드
    r_stat_msg         = easyPayClient.easypay_get_res( "stat_msg"        );     //상태메시지
    r_pay_type         = easyPayClient.easypay_get_res( "pay_type"        );     //결제수단
    r_card_no          = easyPayClient.easypay_get_res( "card_no"         );     //카드번호
    r_issuer_cd        = easyPayClient.easypay_get_res( "issuer_cd"       );     //발급사코드
    r_issuer_nm        = easyPayClient.easypay_get_res( "issuer_nm"       );     //발급사명
    r_acquirer_cd      = easyPayClient.easypay_get_res( "acquirer_cd"     );     //매입사코드
    r_acquirer_nm      = easyPayClient.easypay_get_res( "acquirer_nm"     );     //매입사명
    r_install_period   = easyPayClient.easypay_get_res( "install_period"  );     //할부개월
    r_noint            = easyPayClient.easypay_get_res( "noint"           );     //무이자여부
    r_part_cancel_yn   = easyPayClient.easypay_get_res( "part_cancel_yn"  );     //부분취소 가능여부
    r_card_gubun       = easyPayClient.easypay_get_res( "card_gubun"      );     //신용카드 종류
    r_card_biz_gubun   = easyPayClient.easypay_get_res( "card_biz_gubun"  );     //신용카드 구분
    r_cpon_flag        = easyPayClient.easypay_get_res( "cpon_flag"       );     //쿠폰사용 유무
    r_bank_cd          = easyPayClient.easypay_get_res( "bank_cd"         );     //은행코드
    r_bank_nm          = easyPayClient.easypay_get_res( "bank_nm"         );     //은행명
    r_account_no       = easyPayClient.easypay_get_res( "account_no"      );     //계좌번호
    r_deposit_nm       = easyPayClient.easypay_get_res( "deposit_nm"      );     //입금자명
    r_expire_date      = easyPayClient.easypay_get_res( "expire_date"     );     //계좌사용만료일
    r_cash_res_cd      = easyPayClient.easypay_get_res( "cash_res_cd"     );     //현금영수증 결과코드
    r_cash_res_msg     = easyPayClient.easypay_get_res( "cash_res_msg"    );     //현금영수증 결과메세지
    r_cash_auth_no     = easyPayClient.easypay_get_res( "cash_auth_no"    );     //현금영수증 승인번호
    r_cash_tran_date   = easyPayClient.easypay_get_res( "cash_tran_date"  );     //현금영수증 승인일시
    r_cash_issue_type  = easyPayClient.easypay_get_res( "cash_issue_type" );     //현금영수증 발행용도
    r_cash_auth_type   = easyPayClient.easypay_get_res( "cash_auth_type"  );     //현금영수증 인증구분
    r_cash_auth_value  = easyPayClient.easypay_get_res( "cash_auth_value" );     //현금영수증 인증번호
    r_auth_id          = easyPayClient.easypay_get_res( "auth_id"         );     //휴대폰 PhoneID
    r_billid           = easyPayClient.easypay_get_res( "billid"          );     //휴대폰 인증번호
    r_mobile_no        = easyPayClient.easypay_get_res( "mobile_no"       );     //휴대폰번호
    r_mob_ansim_yn     = easyPayClient.easypay_get_res( "mob_ansim_yn"    );     //안심결제 사용유무
    r_cp_cd            = easyPayClient.easypay_get_res( "cp_cd"           );     //포인트사/쿠폰사         
    r_rem_amt          = easyPayClient.easypay_get_res( "rem_amt"         );     //잔액
    r_bk_pay_yn        = easyPayClient.easypay_get_res( "bk_pay_yn"       );     //장바구니 결제여부 
    r_canc_acq_date    = easyPayClient.easypay_get_res( "canc_acq_date"   );     //매입취소일시
    r_canc_date        = easyPayClient.easypay_get_res( "canc_date"       );     //취소일시
    r_refund_date      = easyPayClient.easypay_get_res( "refund_date"     );     //환불예정일시
        
    /* -------------------------------------------------------------------------- */
    /* ::: 가맹점 DB 처리                                                         */
    /* -------------------------------------------------------------------------- */
    /* 응답코드(res_cd)가 "0000" 이면 정상승인 입니다.                            */
    /* r_amount가 주문DB의 금액과 다를 시 반드시 취소 요청을 하시기 바랍니다.     */
    /* DB 처리 실패 시 취소 처리를 해주시기 바랍니다.                             */
    /* -------------------------------------------------------------------------- */
    
    String bDBProc     = "";     //가맹점 DB처리 성공여부
    
    if ( r_res_cd.equals("0000") ) {
        bDBProc = "true";     // DB처리 성공 시 "true", 실패 시 "false"
        if ( bDBProc.equals("false") ) {
            // 승인요청이 실패 시 아래 실행
            if( TRAN_CD_NOR_PAYMENT.equals(tr_cd) ) {
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
                easyPayClient.easypay_deli_us( easypay_mgr_data_item, "req_ip",   request.getRemoteAddr() );
                easyPayClient.easypay_deli_us( easypay_mgr_data_item, "req_id",   "MALL_R_TRANS" );
                easyPayClient.easypay_deli_us( easypay_mgr_data_item, "mgr_msg",  "DB 처리 실패로 망취소"  );
              
                easyPayClient.easypay_run( mall_id, tr_cd, order_no );
              
                r_res_cd = easyPayClient.res_cd;
                r_res_msg = easyPayClient.res_msg;
                r_cno = easyPayClient.easypay_get_res( "cno"              );    //PG거래번호 
                r_canc_date = easyPayClient.easypay_get_res( "canc_date"  );    //취소일시
            }
        }
    }
%>
<html>
<meta name="robots" content="noindex, nofollow">
<script type="text/javascript">
    function f_submit(){
        document.frm.submit();
    }
</script>

<body onload="f_submit();">
<form name="frm" method="post" action="./result.jsp">
    <input type="hidden" id="res_cd"           name="res_cd"          value="<%=r_res_cd%>">            <!-- 결과코드 //-->
    <input type="hidden" id="res_msg"          name="res_msg"         value="<%=r_res_msg%>">           <!-- 결과메시지 //-->
    <input type="hidden" id="cno"              name="cno"             value="<%=r_cno%>">               <!-- PG거래번호 //-->
    <input type="hidden" id="amount"           name="amount"          value="<%=r_amount%>">            <!-- 총 결제금액 //-->
    <input type="hidden" id="order_no"         name="order_no"        value="<%=r_order_no%>">          <!-- 주문번호 //-->
    <input type="hidden" id="auth_no"          name="auth_no"         value="<%=r_auth_no%>">           <!-- 승인번호 //-->
    <input type="hidden" id="tran_date"        name="tran_date"       value="<%=r_tran_date%>">         <!-- 승인일시 //-->
    <input type="hidden" id="escrow_yn"        name="escrow_yn"       value="<%=r_escrow_yn%>">         <!-- 에스크로 사용유무 //-->
    <input type="hidden" id="complex_yn"       name="complex_yn"      value="<%=r_complex_yn%>">        <!-- 복합결제 유무 //-->
    <input type="hidden" id="stat_cd"          name="stat_cd"         value="<%=r_stat_cd%>">           <!-- 상태코드 //-->
    <input type="hidden" id="stat_msg"         name="stat_msg"        value="<%=r_stat_msg%>">          <!-- 상태메시지 //-->
    <input type="hidden" id="pay_type"         name="pay_type"        value="<%=r_pay_type%>">          <!-- 결제수단 //-->
    <input type="hidden" id="card_no"          name="card_no"         value="<%=r_card_no%>">           <!-- 카드번호 //-->
    <input type="hidden" id="issuer_cd"        name="issuer_cd"       value="<%=r_issuer_cd%>">         <!-- 발급사코드 //-->
    <input type="hidden" id="issuer_nm"        name="issuer_nm"       value="<%=r_issuer_nm%>">         <!-- 발급사명 //-->
    <input type="hidden" id="acquirer_cd"      name="acquirer_cd"     value="<%=r_acquirer_cd%>">       <!-- 매입사코드 //-->
    <input type="hidden" id="acquirer_nm"      name="acquirer_nm"     value="<%=r_acquirer_nm%>">       <!-- 매입사명 //-->
    <input type="hidden" id="install_period"   name="install_period"  value="<%=r_install_period%>">    <!-- 할부개월 //-->
    <input type="hidden" id="noint"            name="noint"           value="<%=r_noint%>">             <!-- 무이자여부 //-->
    <input type="hidden" id="part_cancel_yn"   name="part_cancel_yn"  value="<%=r_part_cancel_yn%>">    <!-- 부분취소 가능여부 //-->
    <input type="hidden" id="card_gubun"       name="card_gubun"      value="<%=r_card_gubun%>">        <!-- 신용카드 종류 //-->
    <input type="hidden" id="card_biz_gubun"   name="card_biz_gubun"  value="<%=r_card_biz_gubun%>">    <!-- 신용카드 구분 //-->
    <input type="hidden" id="cpon_flag"        name="cpon_flag"       value="<%=r_cpon_flag%>">         <!-- 쿠폰사용 유무 //-->
    <input type="hidden" id="bank_cd"          name="bank_cd"         value="<%=r_bank_cd%>">           <!-- 은행코드 //-->
    <input type="hidden" id="bank_nm"          name="bank_nm"         value="<%=r_bank_nm%>">           <!-- 은행명 //-->
    <input type="hidden" id="account_no"       name="account_no"      value="<%=r_account_no%>">        <!-- 계좌번호 //-->
    <input type="hidden" id="deposit_nm"       name="deposit_nm"      value="<%=r_deposit_nm%>">        <!-- 입금자명 //-->
    <input type="hidden" id="expire_date"      name="expire_date"     value="<%=r_expire_date%>">       <!-- 계좌사용만료일 //-->
    <input type="hidden" id="cash_res_cd"      name="cash_res_cd"     value="<%=r_cash_res_cd%>">       <!-- 현금영수증 결과코드 //-->
    <input type="hidden" id="cash_res_msg"     name="cash_res_msg"    value="<%=r_cash_res_msg%>">      <!-- 현금영수증 결과메세지 //-->
    <input type="hidden" id="cash_auth_no"     name="cash_auth_no"    value="<%=r_cash_auth_no%>">      <!-- 현금영수증 승인번호 //-->
    <input type="hidden" id="cash_tran_date"   name="cash_tran_date"  value="<%=r_cash_tran_date%>">    <!-- 현금영수증 승인일시 //-->
    <input type="hidden" id="cash_issue_type"  name="cash_issue_type" value="<%=r_cash_issue_type%>">   <!-- 현금영수증발행용도 //-->
    <input type="hidden" id="cash_auth_type"   name="cash_auth_type"  value="<%=r_cash_auth_type%>">    <!-- 인증구분 //-->
    <input type="hidden" id="cash_auth_value"  name="cash_auth_value" value="<%=r_cash_auth_value%>">   <!-- 인증번호 //-->
    <input type="hidden" id="auth_id"          name="auth_id"         value="<%=r_auth_id%>">           <!-- PhoneID //-->
    <input type="hidden" id="billid"           name="billid"          value="<%=r_billid%>">            <!-- 인증번호 //-->
    <input type="hidden" id="mobile_no"        name="mobile_no"       value="<%=r_mobile_no%>">         <!-- 휴대폰번호 //-->
    <input type="hidden" id="mob_ansim_yn"     name="mob_ansim_yn"    value="<%=r_mob_ansim_yn%>">      <!-- 안심결제 사용유무 //-->
    <input type="hidden" id="cp_cd"            name="cp_cd"           value="<%=r_cp_cd%>">             <!-- 포인트사/쿠폰사 //-->    
    <input type="hidden" id="rem_amt"          name="rem_amt"         value="<%=r_rem_amt%>">           <!-- 잔액 //-->
    <input type="hidden" id="bk_pay_yn"        name="bk_pay_yn"       value="<%=r_bk_pay_yn%>">         <!-- 장바구니 결제여부 //-->
    <input type="hidden" id="canc_acq_date"    name="canc_acq_date"   value="<%=r_canc_acq_date%>">     <!-- 매입취소일시 //-->
    <input type="hidden" id="canc_date"        name="canc_date"       value="<%=r_canc_date%>">         <!-- 취소일시 //-->
    <input type="hidden" id="refund_date"      name="refund_date"     value="<%=r_refund_date%>">       <!-- 환불예정일시 //-->
   

</form>
</body>
</html>
    