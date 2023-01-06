<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/appuser/create" var="createUrl" />
<c:url value="/pay/appuser/read" var="readUrl" />
<c:url value="/pay/appuser/update" var="updateUrl" />
<c:url value="/pay/appuser/destroy" var="destroyUrl" />

<c:url value="/pay/appuser/changeStatus" var="changeStatusUrl" />

<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold pt-1 pb-3 mb-3">
	<span class="mr-1 ${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<div style="display:none;" id="curr-app-div">
	<h6 class="text-muted small font-weight-bold py-3 mt-4 mb-3">${msg_currApp}</h6>
	<p class="mb-3">${msg_currAppDesc}</p>

	<div id="form-container" class="mb-3 pb-1"></div>

	<hr class="border-light container-m--x my-4">
</div>

<h6 class="text-muted small font-weight-bold py-3 mt-4 mb-3">${msg_regApps}</h6>
<p class="mb-3" id="reg-apps-desc">${msg_regAppsDesc}</p>

<div id="item-container" class="row mb-3"></div>


<!--  Forms -->

<script id="template-1" type="text/x-kendo-template">

<div class="card mb-4">
	<div class="card-header with-elements">
		<h5 class="m-0 mr-2">
			<span class="m-0 mr-2">#: deviceName #</span>
		</h5>
		<div class="card-header-elements">
# if (status == 'R') { #
			<span class="badge badge-outline-info font-weight-normal small">${tip_statusR}</span>
# } else if (status == 'A') { #
			<span class="badge badge-outline-success font-weight-normal small">${tip_statusA}</span>
# } else if (status == 'D' || status == 'S' || status == 'U') { #
			<span class="badge badge-outline-secondary font-weight-normal small">${tip_statusDSU}</span>
# } #
		</div>
		<div class="card-header-elements ml-auto">
			<button type="button" class="btn btn-sm btn-default icon-btn borderless btn-round md-btn-flat dropdown-toggle hide-arrow" data-toggle="dropdown">
				<span class="fas fa-ellipsis-v"></span>
			</button>
			<div class="dropdown-menu dropdown-menu-right">
# if (status == 'A') { #
				<a class="dropdown-item" href="javascript:changeStatusApp(#: id #, 'U')"><span class="far fa-bell-slash fa-fw text-secondary"></span><span class="pl-2">${cmd_requestD}</span></a>
				<div class='dropdown-divider'></div>
# } else if (status == 'D' || status == 'S' || status == 'U') { #
				<a class="dropdown-item" href="javascript:changeStatusApp(#: id #, 'R')"><span class="far fa-bell fa-fw"></span><span class="pl-2">${cmd_requestA}</span></a>
				<div class='dropdown-divider'></div>
# } #
				<a class="dropdown-item" href="javascript:deleteApp(#: id #)"><span class="far fa-trash-alt fa-fw text-danger"></span><span class="pl-2">${cmd_delete}</span></a>
			</div>
		</div>
	</div>
	<div class="card-body">
		<form id="form-curr" rowid="-1" osver="">
			<div class="form-group col">
				<span class="#: osIconDisp # fa-lg fa-fw"></span><span class='pl-1'>#: osTypeDisp #</span>
				<span class="px-2">•</span><span>#: osVerDisp #</span>
			</div>
			<div class="form-group col">
				<label class="form-label">
					${title_deviceName}
					<span class="text-danger">*</span>
				</label>
				<input name="deviceName" type="text" maxlength="50" class="form-control required">
			</div>
			<div class="form-group col">
				<label class="form-label">
					${label_fcmToken}
				</label>
				<input name="fcmToken" type="text" class="form-control" readonly="readonly">
			</div>
		</form>
	</div>
	<div class="card-footer d-flex flex-wrap justify-content-end">
		<button type="button" class="btn btn-default" onclick='readData()'>${form_cancel}</button>
		<button type="button" class="btn btn-primary ml-2" onclick='saveForm1()'>${form_save}</button>
	</div>
</div>

</script>


<script id="template-2" type="text/x-kendo-template">

<div class="card mb-4">
	<div class="card-header with-elements">
		<h5 class="m-0 mr-2 py-2">
			<span class="m-0 mr-2">${label_newApp}</span>
		</h5>
	</div>
	<div class="card-body">
		<form id="form-new" rowid="-1" osver="">
			<div class="form-group col">
				<span class="#: osIconDisp # fa-lg fa-fw"></span><span class='pl-1'>#: osTypeDisp #</span>
				<span class="px-2">•</span><span>#: osVerDisp #</span>
			</div>
			<div class="form-group col">
				<label class="form-label">
					${title_deviceName}
					<span class="text-danger">*</span>
				</label>
				<input name="deviceName" type="text" maxlength="50" class="form-control required" placeholder="${tip_deviceNameTip}" value="${tip_device}">
			</div>
			<div class="form-group col">
				<label class="form-label">
					${label_fcmToken}
				</label>
				<input name="fcmToken" type="text" class="form-control" readonly="readonly">
			</div>
		</form>
	</div>
	<div class="card-footer d-flex flex-wrap justify-content-end">
		<button type="button" class="btn btn-default" onclick='readData()'>${form_cancel}</button>
		<button type="button" class="btn btn-primary ml-2" onclick='saveForm2()'>${cmd_requestR}</button>
	</div>
</div>

</script>


<script id="template-item" type="text/x-kendo-template">

<div class="col-md-6">
	<div class="card mb-3">
		<div class="card-body">
			<div class="card-title with-elements">
				<h5 class="m-0 mr-2">#: deviceName #</h5>
				<div class="card-title-elements">
# if (status == 'R') { #
					<span class="badge badge-outline-info font-weight-normal small">${tip_statusR}</span>
# } else if (status == 'A') { #
					<span class="badge badge-outline-success font-weight-normal small">${tip_statusA}</span>
# } else if (status == 'D' || status == 'S' || status == 'U') { #
					<span class="badge badge-outline-secondary font-weight-normal small">${tip_statusDSU}</span>
# } #
				</div>
				<div class="card-title-elements ml-auto">
					<button type="button" class="btn btn-sm btn-default icon-btn borderless btn-round md-btn-flat dropdown-toggle hide-arrow" data-toggle="dropdown">
						<span class="fas fa-ellipsis-v"></span>
					</button>
					<div class="dropdown-menu dropdown-menu-right">
# if (status == 'A') { #
						<a class="dropdown-item" href="javascript:changeStatusApp(#: id #, 'U')"><span class="far fa-bell-slash fa-fw text-secondary"></span><span class="pl-2">${cmd_requestD}</span></a>
						<div class='dropdown-divider'></div>
# } else if (status == 'D' || status == 'S' || status == 'U') { #
						<a class="dropdown-item" href="javascript:changeStatusApp(#: id #, 'R')"><span class="far fa-bell fa-fw"></span><span class="pl-2">${cmd_requestA}</span></a>
						<div class='dropdown-divider'></div>
# } #
						<a class="dropdown-item" href="javascript:deleteApp(#: id #)"><span class="far fa-trash-alt fa-fw text-danger"></span><span class="pl-2">${cmd_delete}</span></a>
					</div>
				</div>
			</div>
			<p class="card-text">
				<span class="#: osIconDisp # fa-lg fa-fw"></span><span class='pl-1'>#: osTypeDisp #</span>
				<span class="px-2">•</span><span>#: osVerDisp #</span>
			</p>
		</div>
	</div>
</div>

</script>

<!--  / Forms -->


<!--  Scripts -->

<script>

var appTokenID = "";
var appOsType = "";
var appOsVer = "";

$(document).ready(function() {
	  
	if ("A" == "${appMode}") {
		location.href="app://token";
	} else if ("I" == "${appMode}") {
		window.webkit.messageHandlers.app.postMessage("token");
	} else {
		readData();
	}
	
	//setTokenInfo("ZeOru19YMKg:APA91bGT13qRpAjMUICNxUgBlCQxhCehnS-ORk44OXqKJxJh2p1vRE0kG-JKIR7QYVZaEouABQIhQlxz5jFjxcEpKWc9XceOHlZfy3UPmSRMePAb81W6bNDOtlUapIpZB4TPMX4YusFY",
	//		"A", "25");
    
});


function readData() {
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readUrl}",
		data: JSON.stringify({ }),
		success: function (data, status) {
			
			$('#item-container').html("");
			
			var currTemplate = kendo.template($("#template-1").html());
			var newTemplate = kendo.template($("#template-2").html());
			
			var itemTemplate = kendo.template($("#template-item").html());
			
			var listCnt = 0;
			var hasOwnData = false;
			for(var i in data) {
				if (data[i].fcmToken == appTokenID) {
					$('#form-container').html(currTemplate(data[i]));
					
					$("#form-curr").attr("rowid", data[i].id);
					
					$("#form-curr input[name='deviceName']").val(data[i].deviceName);
					$("#form-curr input[name='fcmToken']").val(data[i].fcmToken);
					
					$("#form-curr").validate({
						rules: {
							deviceName: { minlength: 2 },
						}
					});
					
					hasOwnData = true;
				} else {
					$('#item-container').append(itemTemplate(data[i]));
					listCnt ++;
				}
			}
			
			if (!hasOwnData && appTokenID && appOsType && appOsVer) {
				$('#form-container').html(newTemplate({
					osIconDisp: getOsIconDisp(appOsType),
					osTypeDisp: getOsTypeDisp(appOsType),
					osVerDisp: getOsVerDisp(appOsType, appOsVer),
				}));
				
				$("#form-new input[name='fcmToken']").val(appTokenID);
				
				$("#form-new").validate({
					rules: {
						deviceName: { minlength: 2 },
					}
				});
			}
			
			if (listCnt == 0) {
				$("#reg-apps-desc").text("${msg_regAppsNoData}");
			}
		},
		error: ajaxReadError
	});
}


function saveForm1() {

	if ($("#form-curr").valid()) {
    	var data = {
    		id: Number($("#form-curr").attr("rowid")),
    		deviceName: $.trim($("#form-curr input[name='deviceName']").val()),
    		osVer: appOsVer,
    	};
    	
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${updateUrl}",
			data: JSON.stringify(data),
			success: function (form) {
				showSaveSuccessMsg();
				
				readData();
			},
			error: ajaxSaveError
		});
	}
}


function saveForm2() {

	if ($("#form-new").valid()) {
    	var data = {
    		deviceName: $.trim($("#form-new input[name='deviceName']").val()),
    		osType: appOsType,
    		osVer: appOsVer,
    		fcmToken: appTokenID,
    	};
    	
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${createUrl}",
			data: JSON.stringify(data),
			success: function (form) {
				showSaveSuccessMsg();
				
				readData();
			},
			error: ajaxSaveError
		});
	}
}


function setTokenInfo(tokenID, osType, osVer) {
	
	if (tokenID && osType && osVer) {
		appTokenID = tokenID;
		appOsType = osType;
		appOsVer = osVer;
		
		readData();
		
		$("#curr-app-div").show();
	}
}


function deleteApp(id) {

	showDelConfirmModal(function(result) {
		if (result) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: "${destroyUrl}",
				data: JSON.stringify({ items: [ id ] }),
				success: function (form) {
					showDeleteSuccessMsg();
					
					readData();
				},
				error: ajaxDeleteError
			});
		}
	});
}


function changeStatusApp(id, status) {

	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${changeStatusUrl}",
		data: JSON.stringify({ id: id, status: status }),
		success: function (form) {
			showOperationSuccessMsg();
			
			readData();
		},
		error: ajaxOperationError
	});
}


function getOsVerDisp(osType, osVer) {
	
	if (osType) {
		if (osType == "A") {
			if (osVer == "19") { return "v19 KitKat"; }
			else if (osVer == "20") { return "v20 Kitkat"; }
			else if (osVer == "21") { return "v21 Lollipop"; }
			else if (osVer == "22") { return "v22 Lollipop"; }
			else if (osVer == "23") { return "v23 Marshmallow"; }
			else if (osVer == "24") { return "v24 Nougat"; }
			else if (osVer == "25") { return "v25 Nougat"; }
			else if (osVer == "26") { return "v26 Oreo"; }
			else if (osVer == "27") { return "v27 Oreo"; }
			else if (osVer == "28") { return "v28 Pie"; }
		}
	}
	
	return "v" + osVer;
}


function getOsIconDisp(osType) {
	
	if (osType) {
		if (osType == "A") {
			return "fab fa-android text-green";
		} else if (osType == "I") {
			return "fab fa-apple text-dark";
		}
	}
	
	return "";
}


function getOsTypeDisp(osType) {
	
	if (osType) {
		if (osType == "A") {
			return "Android";
		} else if (osType == "I") {
			return "iOS";
		}
	}
	
	return "";
}

</script>

<!--  / Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
