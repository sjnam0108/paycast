<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/storecook/read" var="readUrl" />
<c:url value="/storecook/alarmUpdate" var="alarmUrl" />
<c:url value="/storecook/stayCntRead" var="stayCntRead" />
<c:url value="/storecook/readCom" var="readComUrl" />
<c:url value="/storecook/comCancelUpdate" var="comCancelUrl" />
<c:url value="/storecook/renuwRead" var="renuwReadUrl" />
<c:url value="/storecook/renuwCancelRead" var="renuwCancelReadUrl" />


<!-- Opening tags -->

<!DOCTYPE html>
<html lang="${html_lang}" class="default-style layout-navbar-fixed">
	<common:head />
	<body>
		<div class="page-loader">
			<div class="bg-primary"></div>
		</div>
        

		<!-- Layout wrapper -->
		<div class="layout-wrapper layout-1 layout-without-sidenav">
		
			<div class="layout-inner">

				<!-- Layout navbar -->
				<nav class="layout-navbar navbar navbar-expand align-items-center container-p-x bg-navbar-black">

					<div><span class="title-display">${pageTitle}</span></div>

					<div class="navbar-nav align-items-md-center ml-auto">
						<img src="/resources/shared/images/logo/logo_app.png" alt/>
					</div>
				</nav>
				<!-- / Layout navbar -->

				<!-- Layout container -->
				<div class="layout-container">

					<!-- Layout content -->
					<div class="layout-content">

						<!-- Content -->
						<div class="container-fluid flex-grow-1 py-3">
						

<style>
.bg-navbar-black {
	height: 50px;
	background-color: #3e4752;
	color: #fefefe;
}
.title-display {
	font-size: 1.25rem;
	font-weight: 300;
	line-height: 1.1;
}
</style>


<!-- Page body -->
<script src="/resources/shared/js/jquery.form.js"></script>

<style>
	.tableIn {
		width: 100%;
		max-width: 100%;
		border : 0px;
		padding: 0;
	}

	.tableIn tr td {
		border : 0px;
	}
	
	.jbBox {
		border: 2px solid #000000;
	}
	
	.jbBoxComp {
		border: 2px solid #000000;
	}
</style>

<div class="row pt-3">
	<div class="col-md">
		<div class="card text-center mb-3">
			<div class="mb-4 mt-5 select-desc text-center text-muted">
				<svg class="item-icon">
					<use xlink:href="/resources/shared/other-icon.svg#exclamation"></use>
				</svg>
				<br>
				<h3>${cook_noorder}</h3>
				<h4>${mag_errorAdmin}</h4>
			</div>
		</div>
	</div>
</div>

<style>

.item-icon {
	width: 192px; height: 192px; fill: currentColor;
}

</style>	
<script type="text/javascript">

$(function() {
	setInterval(padEnabledFn, 5000);
});

function padEnabledFn(){
	var data = {
			padChk: 'I',
			deviceId: '${deviceId}'
		};
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "/storecook/inactiveChk",
		data: JSON.stringify(data),
		success: function (form) {
			if('NO' != form){
				document.location.href=form;
			}
		},
		error: ajaxReadError
	});
}

</script>

<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />


						</div>
						<!-- / Content -->

					</div>
					<!-- Layout content -->

				</div>
				<!-- / Layout container -->

			</div>
			<!-- Overlay -->
			
		</div>
		<!-- / Layout wrapper -->

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