<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height: 100%;">
<head>
<meta name="robots" content="noindex, nofollow">
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, target-densitydpi=medium-dpi" />
<title>${pageTitle}</title>
<link rel="stylesheet" type="text/css" href="/resources/easypay/css/easypay.css" />
<link rel="stylesheet" type="text/css" href="/resources/easypay/css/board.css" />

<!-- Test -->
<script language="javascript" src="${mainScript}"></script>

<script type="text/javascript">
    /* 인증창 호출, 인증 요청 */
    function f_cert() 
    {
        var frm_pay = document.frm_pay;
        
        /*  주문정보 확인 */
        if( !frm_pay.sp_order_no.value ) 
        {
            alert("가맹점주문번호를 입력하세요!!");
            frm_pay.sp_order_no.focus();
            return;
        }

        if( !frm_pay.sp_product_amt.value ) 
        {
            alert("상품금액을 입력하세요!!");
            frm_pay.sp_product_amt.focus();
            return;
        }
        /* UTF-8 사용가맹점의 경우 EP_charset 값 셋팅 필수 */
        if( frm_pay.sp_charset.value == "UTF-8" )
        {
            // 한글이 들어가는 값은 모두 encoding 필수.
            frm_pay.sp_mall_nm.value      = encodeURIComponent( frm_pay.sp_mall_nm.value );
            frm_pay.sp_product_nm.value   = encodeURIComponent( frm_pay.sp_product_nm.value );
            frm_pay.sp_user_nm.value      = encodeURIComponent( frm_pay.sp_user_nm.value );
            frm_pay.sp_user_addr.value    = encodeURIComponent( frm_pay.sp_user_addr.value );
        }
        document.frm_pay.action = "${mainAction}";
		document.frm_pay.submit();
    }
    
</script>
</head>
<body id="container_skyblue">
<form name="frm_pay" method="post">  

<!--공통-->
<input type="hidden" id="sp_mall_id"           name="sp_mall_id"           value="${sp_mall_id}" />               <!--[필수]가맹점 ID(*) -->
<input type="hidden" id="sp_mall_nm"           name="sp_mall_nm"           value="${sp_mall_nm}" />               <!--[선택]가맹점명 -->
<input type="hidden" id="sp_order_no"          name="sp_order_no"          value="${sp_order_no}" />               <!--[필수]가맹점 주문번호(인증응답) -->  
<input type="hidden" id="sp_currency"          name="sp_currency"          value="${sp_currency}" />               <!--[필수]통화코드(수정불가) -->        
<input type="hidden" id="sp_return_url"        name="sp_return_url"        value="${sp_return_url}" />               <!--[필수]가맹점 return URL -->
<input type="hidden" id="sp_lang_flag"         name="sp_lang_flag"         value="${sp_lang_flag}" />               <!--[선택]언어 -->
<input type="hidden" id="sp_charset"           name="sp_charset"           value="${sp_charset}" />               <!--[선택]가맹점 charset -->
<input type="hidden" id="sp_user_type"         name="sp_user_type"         value="${sp_user_type}" />               <!--[선택]사용자 구분 -->  
<input type="hidden" id="sp_user_id"           name="sp_user_id"           value="${sp_user_id}" />               <!--[선택]가맹점 고객ID -->
<input type="hidden" id="sp_memb_user_no"      name="sp_memb_user_no"      value="${sp_memb_user_no}" />               <!--[선택]가맹점 고객일련번호 -->
<input type="hidden" id="sp_user_nm"           name="sp_user_nm"           value="${sp_user_nm}" />               <!--[선택]가맹점 고객명 -->
<input type="hidden" id="sp_user_mail"         name="sp_user_mail"         value="${sp_user_mail}" />               <!--[선택]가맹점 고객 E-mail -->
<input type="hidden" id="sp_user_phone1"       name="sp_user_phone1"       value="${sp_user_phone1}" />               <!--[선택]가맹점 고객 연락처1 -->
<input type="hidden" id="sp_user_phone2"       name="sp_user_phone2"       value="${sp_user_phone2}" />               <!--[선택]가맹점 고객 연락처2 -->
<input type="hidden" id="sp_user_addr"         name="sp_user_addr"         value="${sp_user_addr}" />               <!--[선택]가맹점 고객 주소 -->
<input type="hidden" id="sp_user_define1"      name="sp_user_define1"      value="" />               <!--[선택]가맹점 필드1 -->
<input type="hidden" id="sp_user_define2"      name="sp_user_define2"      value="" />               <!--[선택]가맹점 필드2 -->
<input type="hidden" id="sp_user_define3"      name="sp_user_define3"      value="" />               <!--[선택]가맹점 필드3 -->
<input type="hidden" id="sp_user_define4"      name="sp_user_define4"      value="" />               <!--[선택]가맹점 필드4 -->
<input type="hidden" id="sp_user_define5"      name="sp_user_define5"      value="" />               <!--[선택]가맹점 필드5 -->
<input type="hidden" id="sp_user_define6"      name="sp_user_define6"      value="" />               <!--[선택]가맹점 필드6 -->
<input type="hidden" id="sp_product_type"      name="sp_product_type"      value="${sp_product_type}" />               <!--[선택]상품정보구분 -->
<input type="hidden" id="sp_product_expr"      name="sp_product_expr"      value="${sp_product_expr}" />               <!--[선택]서비스 기간 -->
<input type="hidden" id="sp_app_scheme"        name="sp_app_scheme"        value="${sp_app_scheme}" />               <!--[선택]가맹점 APP scheme -->  

<!--신용카드-->
<input type="hidden" id="sp_usedcard_code"     name="sp_usedcard_code"     value="" />               <!--[선택]사용가능카드 LIST -->
<input type="hidden" id="sp_quota"             name="sp_quota"             value="" />               <!--[선택]할부개월 -->
<input type="hidden" id="sp_os_cert_flag"      name="sp_os_cert_flag"      value="${sp_os_cert_flag}" />               <!--[선택]해외안심클릭 사용여부-->
<input type="hidden" id="sp_noinst_flag"       name="sp_noinst_flag"       value="" />               <!--[선택]무이자 여부(Y/N)-->
<input type="hidden" id="sp_noinst_term"       name="sp_noinst_term"       value="${sp_noinst_term}" />               <!--[선택]무이자 기간 -->
<input type="hidden" id="sp_set_point_card_yn" name="sp_set_point_card_yn" value="" />									<!--[선택]카드사포인트 사용여부(Y/N)-->
<input type="hidden" id="sp_point_card"        name="sp_point_card"        value="${sp_point_card}" />					<!--[선택]포인트카드 LIST(카드코드-더할 할부개월) -->
<input type="hidden" id="sp_join_cd"           name="sp_join_cd"           value="" />              				 <!--[선택]조인코드 -->

<!--가상계좌-->
<input type="hidden" id="sp_vacct_bank"       name="sp_vacct_bank"         value="${sp_vacct_bank}" />               <!--[선택]가상계좌 사용가능한 은행 LIST -->
<input type="hidden" id="sp_vacct_end_date"   name="sp_vacct_end_date"     value="${sp_vacct_end_date}" />               <!--[선택]입금 만료 날짜 -->
<input type="hidden" id="sp_vacct_end_time"   name="sp_vacct_end_time"     value="${sp_vacct_end_time}" />               <!--[선택]입금 만료 시간 -->

<!--선불카드-->
<input type="hidden" id="sp_prepaid_cp"       name="sp_prepaid_cp"         value="" />               <!--[선택]선불카드 CP -->

<input type='hidden' name="sp_product_nm" id="sp_product_nm" value="${sp_product_nm}">
<input type='hidden' name="sp_product_amt"  id="sp_product_amt" value="${sp_product_amt}">
<input type='hidden' name="sp_window_type"  id="sp_window_type" value="submit">
<input type='hidden' name="sp_disp_cash_yn"  id="sp_disp_cash_yn" value="">
<input type='hidden' name="sp_kmotion_useyn"  id="sp_kmotion_useyn" value="Y">
<!-- [END] 인증요청 필드  --> 


<!-- [START] 인증응답 필드 -->
<!--공통-->
<input type="hidden" id="sp_res_cd"              name="sp_res_cd"                value="" />         <!-- [필수]응답코드        --> 
<input type="hidden" id="sp_res_msg"             name="sp_res_msg"               value="" />         <!-- [필수]응답메시지      --> 
<input type="hidden" id="sp_tr_cd"               name="sp_tr_cd"                 value="" />         <!-- [필수]결제창 요청구분 --> 
<input type="hidden" id="sp_ret_pay_type"        name="sp_ret_pay_type"          value="" />         <!-- [필수]결제수단        --> 
<input type="hidden" id="sp_trace_no"            name="sp_trace_no"              value="" />         <!-- [선택]추적번호        --> 
<!-- 가맹점 주문번호 인증요청 필드에 존재.                                                                [필수]가맹점 주문번호 --> 
<input type="hidden" id="sp_sessionkey"          name="sp_sessionkey"            value="" />         <!-- [필수]세션키          --> 
<input type="hidden" id="sp_encrypt_data"        name="sp_encrypt_data"          value="" />         <!-- [필수]암호화전문      --> 
<!-- 가맹점 ID  인증요청 필드에 존재.                                                                     [필수]가맹점 ID       -->
<input type="hidden" id="sp_mobilereserved1"     name="sp_mobilereserved1"       value="${stopURL}" />         <!-- [선택]여유필드        --> 
<input type="hidden" id="sp_mobilereserved2"     name="sp_mobilereserved2"       value="${sp_order_no}" />         <!-- [선택]여유필드        --> 
<input type="hidden" id="sp_reserved1"           name="sp_reserved1"             value="" />         <!-- [선택]여유필드        --> 
<input type="hidden" id="sp_reserved2"           name="sp_reserved2"             value="" />         <!-- [선택]여유필드        --> 
<input type="hidden" id="sp_reserved3"           name="sp_reserved3"             value="" />         <!-- [선택]여유필드        --> 
<input type="hidden" id="sp_reserved4"           name="sp_reserved4"             value="" />         <!-- [선택]여유필드        --> 

<!--신용카드-->
<input type="hidden" id="sp_card_code"            name="sp_card_code"            value="" />         <!-- [필수]카드코드               -->
<input type="hidden" id="sp_eci_code"             name="sp_eci_code"             value="" />         <!-- [선택]ECI코드(MPI인 경우)    -->
<input type="hidden" id="sp_card_req_type"        name="sp_card_req_type"        value="" />         <!-- [필수]거래구분               -->
<input type="hidden" id="sp_save_useyn"           name="sp_save_useyn"           value="" />         <!-- [선택]카드사 세이브 여부     -->
<input type="hidden" id="sp_card_prefix"          name="sp_card_prefix"          value="" />         <!-- [선택]신용카드 Prefix        -->
<input type="hidden" id="sp_card_no_7"            name="sp_card_no_7"            value="" />         <!-- [선택]신용카드번호 앞7자리   -->

<!--간편결제-->
<input type="hidden" id="sp_spay_cp"              name="sp_spay_cp"              value="" />          <!-- [선택]간편결제 CP코드 -->
   
<!-- [END] 인증응답 필드  --> 

<div id="div_mall">
   <div class="contents1">
		<div class="con1">
			<p>
				<img src='/resources/easypay/img/common/logo.png' height="19" alt="Easypay">
			</p>
		</div>
		<div class="con1t1">
			<p>${sp_mall_nm}</p>
		</div>
    </div>
    <div class="contents">
        <section class="section00 bg_skyblue">
            <fieldset>
                <legend>주문</legend>
                <br>
                <div class="roundTable">
                    <table width="100%" class="table_roundList" cellpadding="5">          
                        <!-- [START] 인증요청 필드 -->  
                        <tbody>
                            <tr>
                                <td>결제수단(*)</td>
                                <td>     
                                    <select name="sp_pay_type" id="sp_pay_type">
	                                    <option value="11" selected>신용카드</option>
	                                    <option value="21">계좌이체</option>
	                                    <option value="22">가상계좌</option>
	                                    <option value="31">휴대폰</option>
	                                    <option value="60">간편결제</option>
                                    </select>
                                </td>  
                            </tr>
							<tr>
	                            <td>인증타입</td>
	                            <td>     
	                                <select name="sp_cert_type" id="sp_cert_type">
	                                    <option value="" selected>일반</option>
	                                    <option value="0">인증</option>
	                                    <option value="1">비인증</option>                                     
	                                </select>
	                            </td>  
	                        </tr>
                   	 </tbody>
                    </table>
                </div><br>
            </fieldset>
           <div class="btnMidNext" align="center"><!-- //button guide에서 button 참고하여 작성 -->
              <a href="javascript:f_cert();" class="btnBox_blue"><span class="btnWhiteVlines">다음</span></a>
              <a href="${stopURL}" class="btnBox_blue"><span class="btnWhiteVlines">돌아가기</span></a>
          </div>
        </section>
    </div>
</div><br>
<!-- <footer class="center margin_b12"> -->
<!--   <p> -->
<!--       <img src='/resources/easypay/img/common/k-logo.gif' width="50" height="9" alt="kicc"> <span class="cop1">Copyrightⓒ 2015 KICC All right reserved</span> -->
<!--   </p> -->
<!-- </footer> -->
</form>
</body>
</html>