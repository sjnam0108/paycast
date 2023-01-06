<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="${html_lang}">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>${storeName}</title>
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/reset.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/ui.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/jquery-ui.min.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/style.css">
	
	<link rel="stylesheet" href="/resources/vendor/css/bootstrap.css">
    <link rel="stylesheet" href="/resources/vendor/css/appwork.css">
    <link rel="stylesheet" href="/resources/vendor/css/theme-bbmc-twitlight-blue.css">
    <link rel="stylesheet" href="/resources/vendor/css/colors.css">
    
	<link rel="stylesheet" href="/resources/vendor/lib/toastr/toastr.css">
	<link rel="stylesheet" href="/resources/vendor/lib/bootstrap-select/bootstrap-select.css">
	<link rel="stylesheet" href="/resources/vendor/lib/spinkit/spinkit.css">
	
	<script src="<c:url value='/resources/js/jquery.min.js' />"></script>
	<script src="/resources/selfmenu/menuList/js/jquery-ui.min.js"></script>
	<script src="/resources/selfmenu/menuList/js/appMenu.js"></script>
	
	<script src="/resources/vendor/lib/popper/popper.js"></script>
	<script src="/resources/vendor/js/bootstrap.js"></script>
	<script src="/resources/vendor/js/sidenav.js"></script>
	
	<script src="/resources/vendor/lib/bootbox/bootbox.js"></script>
	<script src="/resources/vendor/lib/bootstrap-select/bootstrap-select.js"></script>
	
</head>
<script type="text/javascript">
	window.addEventListener('load', function(){
		setTimeout(scrollTo, 0, 0, 1);
	}, false);
</script>
<body>
	<div class="wrapper reset" id="orderPageCom" >
		<div class="header taC">
			<c:choose>
				<c:when test="${mobileLogoType eq 'I'}">
					<div class="logo"><img src="${storeDownLocation}/${mLogoImageFilename}" alt=""></div>	
				</c:when>
				<c:otherwise>
					<div class="logo" style="font-size: 27px;">${mobileLogoText}</div>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="container order">
			<div class="innerDiv">
				<div class="order_list">
					<div class="card mb-4" >
						<div class="card-body">
							<div id="error_area" style="text-align: center;">
								<h1>${pay_orderNum}</h1><br/>
								<h1 style="font-size: 80px;">${orderSequence}</h1><br/>
								<c:choose>
									<c:when test="${orderTable != '' and orderTable ne null}">
										<c:choose>
											<c:when test="${orderTable != '0000'}">
												<h4 style="padding-top: 20px;">${orderTable}${pay_msg_orderTable}</h4>
											</c:when>
											<c:otherwise>
												<h4 style="padding-top: 20px;">${pay_msg_paySuccess}</h4>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<h4 style="padding-top: 20px;">${pay_msg_paySuccess}</h4>									
									</c:otherwise>
								</c:choose>
								<c:if test="${stayMenuCnt != 9999}">
									<h4 style="padding-top: 20px;">${pay_stay} :  ${stayMenuCnt}</h4>
								</c:if>
								<div class="msg_box">
									<h3 style="padding-top: 15px;">${desc}</h3>
									<h3 id="msgcookie" style="padding-top: 15px;"></h3>
								</div>
							</div>
						</div>
					</div>	
				</div>
			</div>
		</div>
		
		<div class="order_btn">
			<button type="button" class="button large red full circle" onclick="goInterface();">${pay_firstPage}</button>
		</div>
	</div>
	<div>
		<form id="returnMenu" name="returnMenu" action="${url}">
			<input type="hidden" name="store" value="${oriUrlparam}">
			<input type="hidden" name="table" value="${orderTable}">
		</form>
	</div>	
</body>

<style type="text/css">
.innerDiv { padding: 10px 10px 0px; }
</style>

<script>
$(function() {
	setTimeout(timeOutPage, 3600000);
	
	function timeOutPage() {
		alert("첫 화면으로 이동합니다.");
		document.location.href="/menu?store=${oriUrlparam}&table=${orderTable}";
	}
	
	 var cookieEnabled = navigator.cookieEnabled;
	 var cookieName = "";
	 var orderSequence = "${orderSequence}";
	 var authDate = "${authDate}";
	 if (cookieEnabled && orderSequence != ""){
			cookieName = 'bbmcorder_${storeId}';
			var cookieTF = unescape(getCookie(cookieName));
			if("" == cookieTF){
				setCookie(cookieName, orderSequence+"_"+authDate, 1);
			}else{
				var tmp1 = cookieTF.split(",");
				var orderCheck = true;
				for( var i in tmp1 ){
					var tmp2 = tmp1[i].split("_");
					if(orderSequence == tmp2[0] && authDate == tmp2[1]){
						orderCheck = false;
					}
				}
				if(orderCheck){
					cookieTF =orderSequence+"_"+authDate+","+cookieTF;
					setCookie(cookieName, cookieTF, 1);
				}
			}
	 }else{
			var msgcookie = "${pay_msg_notSupportBrowser2}";
			$("#msgcookie").html(msgcookie);
	 }
});

function goInterface(){
	bootbox.confirm({
		size: "small",
		title: "${pay_firstPage}",
		message: "${pay_msg_firstPage}",
		backdrop: true,
		buttons: {
			cancel: {
				label: '${confirm_cancel}',
				className: "btn-default",
			},
			confirm: {
				label: '${confirm_ok}',
				className: "btn-danger",
			}
		},
		callback: function(result) {
			if (result) {
				var form = document.returnMenu;
				
				form.submit();
				return false;	
			}
		}
	});
}

</script>

<script src="/resources/vendor/lib/validate/validate.ko.js"></script>

</html>