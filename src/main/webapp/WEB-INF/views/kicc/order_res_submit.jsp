<!--[submit]인증응답 페이지-->
<!--메뉴얼 '인증페이지 작성' 인증응답 파라미터 포함.-->

<!DOCTYPE html>
<%@ page pageEncoding="EUC-KR"%>
<%
/* -------------------------------------------------------------------------- */
/* 캐쉬 사용안함                                                              */
/* -------------------------------------------------------------------------- */
response.setHeader("cache-control","no-cache");
response.setHeader("expires","-1");
response.setHeader("pragma","no-cache");
request.setCharacterEncoding("euc-kr");
%>
<%!
    /*
     * 파라미터 체크 메소드
     */
    public String getNullToSpace(String param) 
    {
        return (param == null) ? "" : param.trim();
    }
%>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=10" />
<meta name="robots" content="noindex, nofollow" />
<meta http-equiv="Content-Type" content="hidden/html; charset=euc-kr" />
<script>
    /*--KICC 서버로부터 인증응답 파라미터 수신--*/
    window.onload = function() 
    {
        // <!--공통-->
        window.document.getElementById("sp_res_cd").value             = "<%=getNullToSpace(request.getParameter("sp_res_cd"))           %>";  // [필수]응답코드
        window.document.getElementById("sp_res_msg").value            = "<%=getNullToSpace(request.getParameter("sp_res_msg"))          %>";  // [필수]응답메세지
        window.document.getElementById("sp_tr_cd").value              = "<%=getNullToSpace(request.getParameter("sp_tr_cd"))            %>";  // [필수]결제창 요청구분
        window.document.getElementById("sp_ret_pay_type").value       = "<%=getNullToSpace(request.getParameter("sp_ret_pay_type"))     %>";  // [필수]결제수단
        window.document.getElementById("sp_trace_no").value           = "<%=getNullToSpace(request.getParameter("sp_trace_no"))         %>";  // [선택]추적번호     
        window.document.getElementById("sp_order_no").value           = "<%=getNullToSpace(request.getParameter("sp_order_no"))         %>";  // [필수]가맹점 주문번호
        window.document.getElementById("sp_sessionkey").value         = "<%=getNullToSpace(request.getParameter("sp_sessionkey"))       %>";  // [필수]세션키
        window.document.getElementById("sp_encrypt_data").value       = "<%=getNullToSpace(request.getParameter("sp_encrypt_data"))     %>";  // [필수]암호화전문
        window.document.getElementById("sp_mall_id").value            = "<%=getNullToSpace(request.getParameter("sp_mall_id"))          %>";  // [필수]가맹점 ID
        window.document.getElementById("sp_mobilereserved1").value    = "<%=getNullToSpace(request.getParameter("sp_mobilereserved1"))  %>";  // [선택]여유필드
        window.document.getElementById("sp_mobilereserved2").value    = "<%=getNullToSpace(request.getParameter("sp_mobilereserved2"))  %>";  // [선택]여유필드
        window.document.getElementById("sp_reserved1").value          = "<%=getNullToSpace(request.getParameter("sp_reserved1"))        %>";  // [선택]여유필드 
        window.document.getElementById("sp_reserved2").value          = "<%=getNullToSpace(request.getParameter("sp_reserved2"))        %>";  // [선택]여유필드
        window.document.getElementById("sp_reserved3").value          = "<%=getNullToSpace(request.getParameter("sp_reserved3"))        %>";  // [선택]여유필드
        window.document.getElementById("sp_reserved4").value          = "<%=getNullToSpace(request.getParameter("sp_reserved4"))        %>";  // [선택]여유필드

        // <!--신용카드-->
        window.document.getElementById("sp_card_code").value          = "<%=getNullToSpace(request.getParameter("sp_card_code"))        %>";  // [필수]카드코드 
        window.document.getElementById("sp_eci_code").value           = "<%=getNullToSpace(request.getParameter("sp_eci_code"))         %>";  // [선택]ECI코드(MPI인 경우)
        window.document.getElementById("sp_card_req_type").value      = "<%=getNullToSpace(request.getParameter("sp_card_req_type"))    %>";  // [필수]거래구분
        window.document.getElementById("sp_save_useyn").value         = "<%=getNullToSpace(request.getParameter("sp_save_useyn"))       %>";  // [선택]카드사 세이브 여부
        window.document.getElementById("sp_card_prefix").value        = "<%=getNullToSpace(request.getParameter("sp_card_prefix"))      %>";  // [선택]신용카드 Prefix 
        window.document.getElementById("sp_card_no_7").value          = "<%=getNullToSpace(request.getParameter("sp_card_no_7"))        %>";  // [선택]신용카드번호 앞7자리   
        
        // <!--간편결제-->
        window.document.getElementById("sp_spay_cp").value            = "<%=getNullToSpace(request.getParameter("sp_spay_cp"))          %>";  // [선택]간편결제 CP코드
        
        // <!--선불카드-->
        window.document.getElementById("sp_prepaid_cp").value         = "<%=getNullToSpace(request.getParameter("sp_prepaid_cp"))       %>";  // [선택]선불카드 CP코드
                          
       if( "<%=request.getParameter("sp_res_cd") %>" == "0000" )
        {
            frm_pay.target = "_self";
            frm_pay.action = "../easypay_request.jsp";
            frm_pay.submit();
        }
        else
        {
        	
        	// 실패일 경우 
            alert( "<%=request.getParameter("sp_res_cd") %> : <%=request.getParameter("sp_res_msg") %>");
            location.href="./order.jsp";
        }
    }
</script>
<title>EasyPay 8.0 webpay mobile</title>
</head>
<body>
 <form name="frm_pay" method="post" >  
    
    <!-- [START] 인증응답 필드 -->     
    
    <!--공통-->
    <input type="hidden" id="sp_res_cd"              name="sp_res_cd"                value="" />         <!-- [필수]응답코드        --> 
    <input type="hidden" id="sp_res_msg"             name="sp_res_msg"               value="" />         <!-- [필수]응답메시지      --> 
    <input type="hidden" id="sp_tr_cd"               name="sp_tr_cd"                 value="" />         <!-- [필수]결제창 요청구분 --> 
    <input type="hidden" id="sp_ret_pay_type"        name="sp_ret_pay_type"          value="" />         <!-- [필수]결제수단        --> 
    <input type="hidden" id="sp_trace_no"            name="sp_trace_no"              value="" />         <!-- [선택]추적번호        --> 
    <input type="hidden" id="sp_order_no"            name="sp_order_no"              value="" />         <!-- [필수]가맹점 주문번호 --> 
    <input type="hidden" id="sp_sessionkey"          name="sp_sessionkey"            value="" />         <!-- [필수]세션키          --> 
    <input type="hidden" id="sp_encrypt_data"        name="sp_encrypt_data"          value="" />         <!-- [필수]암호화전문      --> 
    <input type="hidden" id="sp_mall_id"             name="sp_mall_id"               value="" />         <!-- [필수]가맹점 ID       -->
    <input type="hidden" id="sp_mobilereserved1"     name="sp_mobilereserved1"       value="" />         <!-- [선택]여유필드        --> 
    <input type="hidden" id="sp_mobilereserved2"     name="sp_mobilereserved2"       value="" />         <!-- [선택]여유필드        --> 
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
                                                                                                        
    <!--선불카드-->                                                                                     
    <input type="hidden" id="sp_prepaid_cp"           name="sp_prepaid_cp"           value="" />          <!-- [선택]선불카드 CP코드 -->
    
    <!-- [END] 인증응답 필드  --> 


</body>
</html>
