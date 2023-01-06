<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>${storeName}</title>
	<script src="${pay}${toDate}"></script>
	<script src="${mpay}${toDate}"></script>
	<script src="<c:url value='/resources/js/jquery.min.js' />"></script>
</head>
<body>
	<div class="wrapper reset" >
		<div class="container order" style="margin-bottom: 0px;">
			
		<%-- 결제 진행 --%>
		<form id="kcForm" name="kcForm" method="post" >
			 <input type="hidden" name="PayMethod" value="${payMethod}"/>
			 <input type="hidden" name="orderType" value="${orderType}"/>
			 <input type="hidden" name="GoodsCnt" value="${GoodsCnt}"/>
			 <input type="hidden" name="GoodsName" value="${GoodsName}"/>
			 <input type="hidden" name="Amt" value="${goodsAmt}"/>
			 <input type="hidden" name="Moid" value="${Moid}"/>
			 <input type="hidden" name="Mid" value="${Mid}"/>
			 <input type="hidden" name="ReturnUrl" value="${ReturnUrl}"/>
			 <input type="hidden" name="RetryUrl" value="${RetryUrl}"/>
			 <input type="hidden" name="StopUrl" value="${StopUrl}"/>
			 <input type="hidden" name="BuyerAddr" value="${BuyerAddr}"/>
			 <input type="hidden" name="EncryptData" value="${EncryptData}"/>
			 <input type="hidden" name="MallResultFWD" value="${MallResultFWD}"/>
			 <input type="hidden" name="TransType" value="${TransType}"/>
			 <input type="hidden" name="SocketYN" value="${SocketYN}"/>
			 <input type="hidden" name="Language" value="${Language}"/>
			 <input type="hidden" name="EncodingType" value="${EncodingType}"/>
			 <input type="hidden" name="clientType" value="${clientType}"/>
			 <input type="hidden" name="OfferPeriod" value="${OfferPeriod}"/>
			 <input type="hidden" name="EdiDate" value="${EdiDate}"/>
			 <input type="hidden" name="VatAmt" value="${VatAmt}"/>
			 <input type="hidden" name="TaxAmt" value="${TaxAmt}"/>
			 <input type="hidden" name="TaxFreeAmt" value="${TaxFreeAmt}"/>
			 <input type="hidden" name="SvcAmt" value="${SvcAmt}"/>
			 <input type="hidden" name="CardQuota" value="${CardQuota}"/>
			 <input type="hidden" name="CardInterest" value="${CardInterest}"/>
			 <input type="hidden" name="CardPoint" value="${CardPoint}"/>
			 <input type="hidden" name="IspWapUrl" value="${IspWapUrl}"/>
			 <input type="hidden" name="DivideInfo" value="${DivideInfo}"/>
			 <input type="hidden" name="UserIp" value="${UserIp}"/>
			 <input type="hidden" name="MallIp" value="${MallIp}"/>
			 <input type="hidden" name="FnCd" value="${FnCd}"/>
			 <input type="hidden" name="menuInTime" value="${menuInTime}"/>
			 <input type="hidden" name="GoodsCl" value="${GoodsCl}"/>
			 <input type="hidden" name="VbankExpDate" value="${VbankExpDate}"/>
			 <input type="hidden" name="roadAddr" value="${roadAddr}"/>
			 <input type="hidden" name="addrDetail" value="${addrDetail}"/>
			 <!--<input type="hidden" name="BuyerName" value="스마트로페이 고객님"/>-->
			 <input type="hidden" name="BuyerName" value="${BuyerName}"/>
			 <!--<input type="hidden" name="BuyerEmail" value="noname@smartro.co.kr"/>-->
			 <input type="hidden" name="BuyerEmail" value="${BuyerEmail}"/>
			 <input type="hidden" name="BuyerTel" value="${BuyerTel}"/>
		</form>
	</div>
	
</body>
<script>


$(document).ready(function() {
    // 스마트로페이 초기화
    smartropay.init({
        //mode: "STG" 
		mode: ${INIT}
		// STG: 테스트, REAL: 운영
    });

    // 스마트로페이 결제요청
    // PC 연동시 아래와 같이 smartropay.payment 함수를 구현합니다.
	    smartropay.payment({
        FormId : 'kcForm',				// 폼ID
        Callback : function(res) {
            var approvalForm = document.approvalForm;
            approvalForm.Tid.value = res.Tid;
            approvalForm.TrAuthKey.value = res.TrAuthKey;
            approvalForm.action = '${ReturnUrl}';
            approvalForm.submit();
        }
    });
  
   
    // Mobile 연동시 아래와 같이 smartropay.payment 함수를 구현합니다.
    smartropay.payment({
      FormId : 'kcForm'            // 폼ID
    });
});



</script>

</html>