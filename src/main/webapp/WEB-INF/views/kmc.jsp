<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<script src="<c:url value='/resources/js/jquery.min.js' />"></script>

<script src="/resources/shared/js/jquery.form.js"></script>

<div class="row">
	<form name="reqForm" method="post" action="https://www.kmcert.com/kmcis/web/kmcisReq.jsp">
	    <input type="hidden" name="tr_cert"     value = "${tr_cert}">
	    <input type="hidden" name="tr_url"      value = "${tr_url}">
	    <input type="hidden" name="tr_add"      value = "${tr_add}">	
	    <input type="hidden" name="target"      value = "">	
	</form>

	<div class="col-md">
		<div class="card text-center mb-3">
			<div class="card-header">
				본인인증서비스 테스트 결과
			</div>
			<div class="card-body">
			    <div class="form-row">
			    	<div class="col-sm-6">
					    <div class="form-group col" >
							서비스 요청 일시 : ${date}
						</div>
					</div>
					<div class="col-sm-6">
					    <div class="form-group col" >
							Unique 한 식별정보(CI) : ${CI}
						</div>
					</div>
				</div>
			    <div class="form-row">
			    	<div class="col-sm-6">
					    <div class="form-group col" >
							휴대폰 번호 : ${phoneNo}
						</div>
					</div>
					<div class="col-sm-6">
					    <div class="form-group col" >
							이동통신사 정보 : ${phoneCorp}
							<br /> SKT : SKT 텔레콤 / KTF : KT / LGT : LG U+
							<br /> SKM : SKT mvno / KTM : KT mvno / LGM : LG U+ mvno
						</div>
					</div>
				</div>
			    <div class="form-row">
			    	<div class="col-sm-6">
					    <div class="form-group col" >
							생년월일 : ${birthDay}
						</div>
					</div>
					<div class="col-sm-6">
					    <div class="form-group col" >
							성별 정보 (0:남자 / 1:여자) : ${gender}
						</div>
					</div>
				</div>
			    <div class="form-row">
			    	<div class="col-sm-6">
					    <div class="form-group col" >
							내 .외국인 정보(0:내국인/ 1:외국인) : ${nation}
						</div>
					</div>
					<div class="col-sm-6">
					    <div class="form-group col" >
							성명 : ${name}
						</div>
					</div>
				</div>
			    <div class="form-row">
			    	<div class="col-sm-6">
					    <div class="form-group col" >
							본인인증 결과 값(Y:성공 / N:실패 / F:오류) : ${result}
						</div>
					</div>
					<div class="col-sm-6">
					    <div class="form-group col" >
							서비스 방법(M:휴대폰 본인확인): ${certMet}
						</div>
					</div>
				</div>
			    <div class="form-row">
			    	<div class="col-sm-6">
					    <div class="form-group col" >
							이용자 IP주소 정보 : ${ip}
						</div>
					</div>
					<div class="col-sm-6">
					    <div class="form-group col" >
							14세 미만 성명 정보: ${M_name}
						</div>
					</div>
				</div>
			    <div class="form-row">
			    	<div class="col-sm-6">
					    <div class="form-group col" >
							14세 미만 생년월일 정보 : ${M_birthDay}
						</div>
					</div>
					<div class="col-sm-6">
					    <div class="form-group col" >
							14세 미만 성별 정보: ${M_Gender}
						</div>
					</div>
				</div>
			    <div class="form-row">
			    	<div class="col-sm-6">
					    <div class="form-group col" >
							14세 미만 내,외국인 정보 : ${M_nation}
						</div>
					</div>
					<div class="col-sm-6">
					    <div class="form-group col" >
							요청정보 지원 : ${plusInfo}
						</div>
					</div>
				</div>
			    <div class="form-row">
			    	<div class="col-sm-6">
					    <div class="form-group col" >
							DI - Unique 시별 정보 (웹사이트 기준) : ${DI}
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</div>

<script type="text/javascript">

$(function() {
	<c:choose>
		<c:when test="${returnYn eq 'N'}">
			f_submit();
		</c:when>
		<c:otherwise>
// 			try {
// 				var varUA = navigator.userAgent.toLowerCase();
// 				if (varUA.match('android') != null) {
// //					window.CashGoApp.setKMCModule("${date}", "${CI}", "${phoneNo}", "${phoneCorp}", "${birthDay}", "${gender}", "${nation}", "${name}", "${result}", "${certMet}", "${ip}"
// //							, "${M_name}", "${M_birthDay}", "${M_Gender}", "${M_nation}", "${plusInfo}", "${DI}");
// 					window.CashGoApp.setKMCModule("${result}");
// 				} else if (varUA.indexOf("iphone")>-1||varUA.indexOf("ipad")>-1||varUA.indexOf("ipod")>-1) {
// //					var jsCall = "jscall://callKMCInput?date='${date}'&CI='${CI}'&phoneNo='${phoneNo}'&phoneCorp='${phoneCorp}'&birthDay='${birthDay}'&gender='${gender}'";
// //						jsCall += "&nation='${nation}'&name='${name}'&result='${result}'&certMet='${certMet}'&ip='${ip}'";
// //						jsCall += "&M_name='${M_name}'&M_birthDay='${M_birthDay}'&M_Gender='${M_Gender}'&M_nation='${M_nation}'&DI='${DI}'";
// //					window.location = jsCall;
// 					window.location = "jscall://callKMCModule?result='${result}'";
// 				}
// 			} catch (e) { }
// 			window.close();
		</c:otherwise>
	</c:choose>
});

function f_submit(){
    document.reqForm.submit();
}

</script>

