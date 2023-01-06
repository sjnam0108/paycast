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
	<div class="wrapper reset">
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
			<div class="inner">
				<div class="order_list">
					<div class="card mb-4" >
						<div class="card-body">
							<div style="text-align: center;">
							<c:choose>
								<c:when test="${gubun eq 'C'}">
									<h1>${menuMsg}</h1>
									<h3>(${pay_resultCode} ${resultCode})</h3>
									<h4 style="padding-top: 75px;">${errorAdmin}</h4>
									<div class="msg_box">
										<h3 style="padding-top: 15px;">${desc}</h3>
									</div>
								</c:when>
								<c:when test="${gubun eq 'B'}">
									<h1>${menuMsg}</h1>
									<h4 style="padding-top: 75px;">${errorAdmin}</h4>
									<div class="msg_box">
										<h3 style="padding-top: 15px;">${desc}</h3>
									</div>
								</c:when>
								<c:otherwise>
									<h1>${menuMsg}</h1>
									<h4 style="padding-top: 75px;">${errorAdmin}</h4>
									<div class="msg_box">
										<h3 style="padding-top: 15px;">${desc}</h3>
									</div>
								</c:otherwise>
							</c:choose>
							</div>
						</div>
					</div>	
				</div>
			</div>
		</div>
		
		<div class="order_btn">
			<c:if test="${not empty oriUrlparam}">
				<button type="button" class="button large red full circle" onclick="goInterface();">메뉴판으로 이동</button>
			</c:if>
		</div>
	</div>
	<div>
		<form id="returnMenu" name="returnMenu" action="${oriUrl}">
			<input type="hidden" name="storeKey" value="${oriUrlparam}">
			<input type="hidden" name="table" value="${orderTable}">
			<input type="hidden" name="basket" value="${basket}">
			<input type="hidden" name="order" value="${order}">
			<input type="hidden" name="time" value="${time}">
		</form>		
	</div>	
</body>
<script>
$(document).ready(function() {
	setTimeout(timeOutPage, 3600000);
	
	function timeOutPage() {
		alert("장시간 입력이 없으셨습니다. \n첫 화면으로 이동합니다.");
		document.location.href="/menu?store=${oriUrlparam}&table=${orderTable}";
	}
});

function goInterface(){
	bootbox.confirm({
		size: "small",
		title: "메뉴판으로 이동",
		message: "메뉴판으로 이동하시겠습니까?",
		backdrop: true,
		buttons: {
			cancel: {
				label: '취소',
				className: "btn-default",
			},
			confirm: {
				label: '확인',
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