<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<script src="<c:url value='/resources/js/jquery.min.js' />"></script>

<script src="/resources/shared/js/jquery.form.js"></script>


<!DOCTYPE html>
<html lang="${html_lang}" class="default-style">
	<common:head />
	<body>
		<div class="page-loader">
			<div class="bg-primary"></div>
		</div>
        

<!-- Content -->

<div class="authentication-wrapper authentication-1 px-4">
	<div class="authentication-inner py-5">

			<button type="button" class="btn btn-primary" onclick="testSMS()">test</button>

	</div>
</div>

<script type="text/javascript">

$(function() {
// 	var data = {
// 			apiKey: "a9ace015c90c0da2132075da6fdf3492a2fca176",
// 			message: "안녕하세요~ 테스트 입니다. ",
// 			phoneNumber: "01090004751",
// 			callbackNumber: "024681112",
// 			agentId: "87",
// 			brandId: "4",
// 			isAdvertisement: false,
// 			serivceTelNumber: "",
// 			brandName: ""
// 		};
	
// 	$.ajax({
// 		type: "POST",
// 		contentType: "application/json",
// 		dataType: "json",
// 		url: "http://talk-dev.thecloudgate.io/api/external/sendMobileMessage",
// 		data: JSON.stringify(data),
// 		success: function (form) {
// 			console.log(form);
// 		},
// 		error: function(e) {
// 			console.log(e);
// 		}
// 	});
});

function testSMS() {
	var data = {
			apiKey: "a9ace015c90c0da2132075da6fdf3492a2fca176",
			message: "ajax에서 테스트 하였습니다.",
			phoneNumber: "01090004751",
			callbackNumber: "024681112",
			agentId: "87",
			brandId: "4",
			isAdvertisement: false,
			serivceTelNumber: "",
			brandName: ""
		};
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "https://talk-dev.thecloudgate.io/api/external/sendMobileMessage",
		data: JSON.stringify(data),
		success: function (form) {
			console.log(form);
		},
		error: function(e) {
			console.log(e);
		}
	});
}

</script>

<!-- Base modules -->
<common:base />


		<!-- Core scripts -->
		<script src="/resources/vendor/lib/popper/popper.js"></script>
		<script src="/resources/vendor/js/bootstrap.js"></script>
		<script src="/resources/vendor/js/sidenav.js"></script>

		<!-- Libs -->
		<script src="/resources/vendor/lib/perfect-scrollbar/perfect-scrollbar.js"></script>
		<script src="/resources/vendor/lib/toastr/toastr.js"></script>
		<script src="/resources/vendor/lib/bootbox/bootbox.js"></script>
		<script src="/resources/vendor/lib/bootstrap-select/bootstrap-select.js"></script>
	</body>
</html>