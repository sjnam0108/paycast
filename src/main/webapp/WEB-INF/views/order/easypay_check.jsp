<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>${storeName}</title>

	<script src="<c:url value='/resources/js/jquery.min.js' />"></script>
</head>
<body>
	<div class="wrapper reset" >
		<div class="container order" style="margin-bottom: 0px;">
			
		<%-- 결제 진행 --%>
		<form id="kcForm" name="kcForm" method="post" >
			<input type="hidden" id="sp_pay_type"          name="sp_pay_type"          value="11" />               				
			<input type="hidden" id="sp_mall_id"           name="sp_mall_id"           value="${storeMid}" />               	
			<input type="hidden" id="sp_mall_nm"           name="sp_mall_nm"           value="${storeName}" />               	
			<input type="hidden" id="sp_order_no"          name="sp_order_no"          value="${orderNumber}" />                
			<input type="hidden" id="sp_currency"          name="sp_currency"          value="00" />              				
			<input type="hidden" id="sp_return_url"        name="sp_return_url"        value="${reqUrl}/easypay/orderSubmit" /> 
			<input type="hidden" id="sp_lang_flag"         name="sp_lang_flag"         value="KOR" />              				
			<input type="hidden" id="sp_charset"           name="sp_charset"           value="UTF-8" />               			
			<input type="hidden" id="sp_user_type"         name="sp_user_type"         value="1" />               				
			<input type="hidden" id="sp_user_id"           name="sp_user_id"           value="" />               				
			<input type="hidden" id="sp_memb_user_no"      name="sp_memb_user_no"      value="" />               				
			<input type="hidden" id="sp_user_nm"           name="sp_user_nm"           value="" />               				
			<input type="hidden" id="sp_user_mail"         name="sp_user_mail"         value="" />              				
			<input type="hidden" id="sp_user_phone1"       name="sp_user_phone1"       value="${sp_user_phone1}" />               				
			<input type="hidden" id="sp_user_phone2"       name="sp_user_phone2"       value="" />              
			<input type="hidden" id="sp_user_addr"         name="sp_user_addr"         value="${sp_user_addr}" />              
			<input type="hidden" id="sp_user_define1"      name="sp_user_define1"      value="" />              
			<input type="hidden" id="sp_user_define2"      name="sp_user_define2"      value="" />              
			<input type="hidden" id="sp_user_define3"      name="sp_user_define3"      value="" />              
			<input type="hidden" id="sp_user_define4"      name="sp_user_define4"      value="" />              
			<input type="hidden" id="sp_user_define5"      name="sp_user_define5"      value="" />              
			<input type="hidden" id="sp_user_define6"      name="sp_user_define6"      value="" />              
			<input type="hidden" id="sp_product_type"      name="sp_product_type"      value="0" />             
			<input type="hidden" id="sp_product_expr"      name="sp_product_expr"      value="${sp_product_expr}" />
			<input type="hidden" id="sp_app_scheme"        name="sp_app_scheme"        value="" />  
			
			<%-- 신용카드 --%>
			<input type="hidden" id="sp_usedcard_code"     name="sp_usedcard_code"     value="" />
			<input type="hidden" id="sp_quota"             name="sp_quota"             value="" />
			<input type="hidden" id="sp_os_cert_flag"      name="sp_os_cert_flag"      value="2" />
			<input type="hidden" id="sp_noinst_flag"       name="sp_noinst_flag"       value="" />
			<input type="hidden" id="sp_noinst_term"       name="sp_noinst_term"       value="" />
			<input type="hidden" id="sp_set_point_card_yn" name="sp_set_point_card_yn" value="" />
			<input type="hidden" id="sp_point_card"        name="sp_point_card"        value="" />
			<input type="hidden" id="sp_join_cd"           name="sp_join_cd"           value="" />
			
			<%-- 가상계좌 --%>
			<input type="hidden" id="sp_vacct_bank"       name="sp_vacct_bank"         value="" />
			<input type="hidden" id="sp_vacct_end_date"   name="sp_vacct_end_date"     value="${sp_product_expr}" />
			<input type="hidden" id="sp_vacct_end_time"   name="sp_vacct_end_time"     value="${sp_vacct_end_time}" />
			
			<%-- 선불카드 --%>
			<input type="hidden" id="sp_prepaid_cp" name="sp_prepaid_cp" value="" />
			<input type="hidden" name="sp_product_nm" id="sp_product_nm" value="${goodsName}">
			<input type="hidden" name="sp_product_amt"  id="sp_product_amt" value="${goodsAmt}">
			<input type="hidden" name="sp_window_type"  id="sp_window_type" value="submit">
			<input type="hidden" name="sp_disp_cash_yn"  id="sp_disp_cash_yn" value="N">
			<input type="hidden" name="sp_kmotion_useyn"  id="sp_kmotion_useyn" value="Y">
			
			<%-- 공통 --%>
			<input type="hidden" id="sp_res_cd"              name="sp_res_cd"                value="" /> 
			<input type="hidden" id="sp_res_msg"             name="sp_res_msg"               value="" /> 
			<input type="hidden" id="sp_tr_cd"               name="sp_tr_cd"                 value="" /> 
			<input type="hidden" id="sp_ret_pay_type"        name="sp_ret_pay_type"          value="" /> 
			<input type="hidden" id="sp_trace_no"            name="sp_trace_no"              value="" /> 
			<%-- 가맹점 주문번호 인증요청 필드에 존재.                                                           [필수]가맹점 주문번호 --%>
			<input type="hidden" id="sp_sessionkey"          name="sp_sessionkey"            value="" /> 
			<input type="hidden" id="sp_encrypt_data"        name="sp_encrypt_data"          value="" /> 
			<%-- 가맹점 ID  인증요청 필드에 존재.                                                            [필수]가맹점 ID --%>
			<input type="hidden" id="sp_mobilereserved1"     name="sp_mobilereserved1"       value="${storeKey}" /> 
			<input type="hidden" id="sp_mobilereserved2"     name="sp_mobilereserved2"       value="" /> 
			<input type="hidden" id="sp_reserved1"           name="sp_reserved1"             value="" /> 
			<input type="hidden" id="sp_reserved2"           name="sp_reserved2"             value="" /> 
			<input type="hidden" id="sp_reserved3"           name="sp_reserved3"             value="" /> 
			<input type="hidden" id="sp_reserved4"           name="sp_reserved4"             value="" /> 
			<%-- 신용카드--%>
			<input type="hidden" id="sp_card_code"            name="sp_card_code"            value="" />
			<input type="hidden" id="sp_eci_code"             name="sp_eci_code"             value="" />
			<input type="hidden" id="sp_card_req_type"        name="sp_card_req_type"        value="" />
			<input type="hidden" id="sp_save_useyn"           name="sp_save_useyn"           value="" />
			<input type="hidden" id="sp_card_prefix"          name="sp_card_prefix"          value="" />
			<input type="hidden" id="sp_card_no_7"            name="sp_card_no_7"            value="" />
			<%-- 간편결제 --%>
			<input type="hidden" id="sp_spay_cp"              name="sp_spay_cp"              value="" />
		</form>
	</div>
	
</body>
<script>

$(document).ready(function() {
	 var frm_pay = document.kcForm;
	    
	    /*  주문정보 확인 */
	    if( !frm_pay.sp_order_no.value ){
	        alert("${pay_msg_kcMsg1}");
	        frm_pay.sp_order_no.focus();
	        return;
	    }

	    if( !frm_pay.sp_product_amt.value ){
	        alert("${pay_msg_kcMsg2}");
	        frm_pay.sp_product_amt.focus();
	        return;
	    }
	    /* UTF-8 사용가맹점의 경우 EP_charset 값 셋팅 필수 */
	    if( frm_pay.sp_charset.value == "UTF-8" ){
	        // 한글이 들어가는 값은 모두 encoding 필수.
	        frm_pay.sp_mall_nm.value      = frm_pay.sp_mall_nm.value;
	        frm_pay.sp_product_nm.value   = frm_pay.sp_product_nm.value;
	        frm_pay.sp_user_nm.value      = frm_pay.sp_user_nm.value;
	        frm_pay.sp_user_addr.value    = frm_pay.sp_user_addr.value;
	    }
	    document.kcForm.action = "${mainAction}";
	    document.kcForm.submit();
	    
});




</script>

</html>