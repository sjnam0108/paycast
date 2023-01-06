<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- URL -->
<c:url value="/store/storeInfo/read" var="readUrl" />
<c:url value="/store/storeInfo/update" var="updateUrl" />

<c:url value="/store/storeInfo/updateKiosk" var="storeMonTask" />
<c:url value="/store/storeInfo/operEnd" var="storeOperEnd" />

<c:url value="/store/storesite/readStores" var="readStoreUrl" />

<common:pageOpening />

<script src="/resources/shared/js/jquery.form.js"></script>

<h4 class="font-weight-bold py-3 mb-3">
	<span class="mr-1 fas fa-${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
	
	<div style="float: right;">
    	<button type="button" class="btn btn-primary mr-1" id="save-btn">${form_save}</button>
		<button type="button" class="btn btn-outline-secondary" onclick="fnPageUpload();" >
			<span>${title_stbupload}</span>
		</button>
	</div>
</h4>

<hr class="border-light container-m--x mt-0 mb-4">
<c:forEach var="item" items="${storeList}" varStatus="st" >
	<c:choose>
		<c:when test="${not empty sessionScope['currentStoreId']}">
			<c:choose>
				<c:when test="${sessionScope['currentStoreId'] eq item.id}">
					<c:set var="storeIdValue" value="${item.id}"></c:set>
					<c:set var="storeNameValue" value="${item.storeName}"></c:set>				
				</c:when>
				<c:otherwise>
					<c:if test="${st.first}">
						<c:set var="storeIdValue" value="${item.id}"></c:set>
						<c:set var="storeNameValue" value="${item.storeName}"></c:set>
					</c:if>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<c:if test="${st.first}">
				<c:set var="storeIdValue" value="${item.id}"></c:set>
				<c:set var="storeNameValue" value="${item.storeName}"></c:set>
			</c:if>		
		</c:otherwise>
	</c:choose>
</c:forEach>

<div>
	<input type="hidden" id="site" name="sites" value="${currentSiteID}" />
	<input type="hidden" id="store" name="stores" value="${storeIdValue}" />
	<input type="hidden" id="storeNm" name="storesNm" value="${storeNameValue}" />
</div>

<form id="menuForm" name="menuForm" method="post" enctype="multipart/form-data" >
<input type="hidden" name="id" value="-1"/>
	<div class="card mb-4" >
		<div class="card-body">
			<div class="form-group" >
				<label class="switcher switcher-success switcher-lg">
					<input type="checkbox" class="switcher-input" id="storeOnoff" name="storeOnoff"/>
					<span class="switcher-indicator">
						<span class="switcher-yes">
							<span class="ion ion-md-checkmark"></span>
						</span>
						<span class="switcher-no">
							<span class="ion ion-md-close"></span>
						</span>
					</span>
					<span class="switcher-label">${title_startOn}</span>
				</label>
		    </div>
		    <div class="form-row">
		    	<div class="col-sm-6">
				    <div class="form-group col" >
						<label class="form-label">${title_businessname}</label>
						<input type="text" class="form-control required" id="businessname" name="businessname" placeholder="${Label_businessname}" maxlength="45" />
					</div>
				</div>
				<div class="col-sm-6">
					<div class="form-group col">
						<label class="form-label">${title_representative}</label>
						<input type="text" class="form-control required" id="representative" name="representative" placeholder="${Label_representative}" maxlength="45" />
					</div>
				</div>
			</div>
		    <div class="form-row">
		    	<div class="col-sm-6">
					<div class="form-group col">
						<label class="form-label">${title_registrationNumber}</label>
						<input type="text" class="form-control required" id="registrationNumber" name="registrationNumber" placeholder="${Label_registrationNumber}" maxlength="30"></input>
					</div>
				</div>
				<div class="col-sm-6">
					<div class="form-group col">
						<label class="form-label">${title_merchantNum}</label>
						<input type="text" class="form-control" id="merchantNum" name="merchantNum" placeholder="${Label_merchantNum}" maxlength="30"></input>
					</div>
				</div>
			</div>
		    <div class="form-row">
		    	<div class="col-sm-6">
					<div class="form-group col">
						<label class="form-label">${title_phone}</label>
						<input type="text" class="form-control required" id="phone" name="phone" placeholder="${Label_phone}" maxlength="200"/>
					</div>
				</div>
				<div class="col-sm-6">
					<div class="form-group col">
						<label class="form-label">${title_smilecancelpw}</label>
						<input type="password" class="form-control required" id="smileCancelPW" name="smileCancelPW" placeholder="${Label_smilecancelpw}" maxlength="6"/>
					</div>
				</div>
			</div>
		    <div class="form-row">
				<div class="col-sm-6">
					<div class="form-group col">
						<label class="form-label">${title_address}</label>
						<input type="text" class="form-control required" id="address" name="address" placeholder="${Label_address}" maxlength="200"/>
					</div>
				</div>
		    	<div class="col-sm-6">
					<div class="form-group col">
						<label class="form-label">${title_introduction}</label>
		                <textarea class="form-control required" id="introduction" name="introduction" placeholder="${Label_introduction}"  maxlength="200"></textarea>
					</div>
				</div>
			</div>
		    <div class="form-row">
				<div class="col-sm-6">
					<div class="form-group col">
						<label class="form-label">${title_operatingHours}</label>
		                <textarea class="form-control required" id="operatingHours" name="operatingHours" placeholder="${Label_operatingHours}" maxlength="200"></textarea>
					</div>
				</div>
			</div>
		</div>
	</div>	
</form>    


<script type="text/javascript">
$("#storeOnoff").change(function(e) {
	if(!$(this).prop("checked")){
		$(this).parent().find(".switcher-label").html("${title_startOff}");
		fnOperEnd();
	}else{
		$(this).parent().find(".switcher-label").html("${title_startOn}");
		fnSave();
	}
});
function fnOperEnd(){
	var storeName = $("#storeNm").val(); 
	if(""== storeName){
		showAlertModal("danger", "${store_notOperEnd}");
		return;
	}
	var text = storeName+" ${store_operEnd}";
	bootbox.confirm({
		size: "small",
		title: "${confirm_title}",
		message: text,
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
				showWaitModal();
				var data = {
						page : "S",
						siteId : $("#site").val(),
						storeId : $("#store").val()
					};
				$.ajax({
					type: "POST",
					contentType: "application/json",
					dataType: "json",
					url: "${storeOperEnd}",
					data: JSON.stringify(data),
					success: function (data, status, xhr) {
						hideWaitModal();
						showSaveSuccessMsg();
					},
					error: function (data) {
						hideWaitModal();
						ajaxSaveError(data);
					}
				});
			}else{
				$("#storeOnoff").prop("checked", true);
				$("#storeOnoff").parent().find(".switcher-label").html("${title_startOn}");
			}
		}
	});
}

function defaultStore(){
	$('#menuForm input[name=id]').val('-1');
	$("#storeOnoff").prop("checked", false);
	$('#menuForm input[name=businessname]').val("");
	$('#menuForm textarea[name=introduction]').val("");
	$('#menuForm input[name=representative]').val("");
	$('#menuForm input[name=registrationNumber]').val("");
	$('#menuForm textarea[name=operatingHours]').val("");
	$('#menuForm input[name=merchantNum]').val("");
	$('#menuForm input[name=phone]').val("");
	$('#menuForm input[name=address]').val("");
	$('#menuForm input[name=smileCancelPW]').val("");
	
	
	$("#menuForm").validate({
		rules: {
			businessname: {
				required: true
			},
			introduction: {
				required: true
			},
			representative: {
				required: true, maxlength: 45
			},
			registrationNumber: {
				required: true, maxlength: 30
			},
			operatingHours: {
				required: true, maxlength: 200
			},
			phone: {
				required: true
			},
			address:{
		        required: true
			},
			smileCancelPW:{
				maxlength: 6, number:true
			}
		},
	    errorPlacement: function errorPlacement(error, element) {
			var $parent = $(element).parents('.form-group');
			if ($parent.find('.jquery-validation-error').length) { return; }
			$parent.append(
				error.addClass('jquery-validation-error small form-text invalid-feedback')
			);
		},
		highlight: function(element) {
			var $el = $(element);
			$el.addClass('is-invalid');
		},
		unhighlight: function(element) {
			$(element).parents('.form-group').find('.is-invalid').removeClass('is-invalid');
		}
	});
}

function storeRead(storeId, storeNm){
	if("" == storeId){
		showAlertModal("danger", "${store_notStore}");
		return;
	}
	
	defaultStore();
	
	var data = {
		storeId: storeId
	};
	
	$("#store").val(storeId);
	$("#storeNm").val(storeNm);
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			
			$('#menuForm input[name=id]').val(form.id);
			if(form.mobileOrderAllowed){
				$("#storeOnoff").prop("checked", true);
				$("#storeOnoff").parent().find(".switcher-label").html("${title_startOn}");
			}else{
				$("#storeOnoff").prop("checked", false);
				$("#storeOnoff").parent().find(".switcher-label").html("${title_startOff}");
			}
			
			$('#menuForm input[name=businessname]').val(form.bizName);
			$('#menuForm textarea[name=introduction]').val(form.bizRep);
			$('#menuForm input[name=representative]').val(form.bizRep);
			$('#menuForm input[name=registrationNumber]').val(form.bizNum);
			$('#menuForm textarea[name=operatingHours]').val(form.openHours);
			$('#menuForm input[name=merchantNum]').val(form.storeMerchantNum);
			$('#menuForm input[name=phone]').val(form.phone);
			$('#menuForm input[name=address]').val(form.address);
			$('#menuForm input[name=smileCancelPW]').val(form.smileCancelPW);
		},
		error: ajaxReadError
	});
}

// Save
$("#save-btn").click(function(e) {
	fnSave();
});

function fnSave(){
	var id = $('#menuForm input[name=id]').val();
	if(id == '-1'){
		showAlertModal("danger", "${store_chooseStore}");
		return false;
	}
	if ($("#menuForm").valid()) {
		
		$('#menuForm').ajaxForm({
			type: "POST",
	    	url : "${updateUrl}",
	        success: function(e) {
	        	showSaveSuccessMsg();
	        },
	        error: ajaxSaveError
	    }).submit();
	}
}

// / Save

$(function() {
	storeRead('${storeIdValue}', '${storeNameValue}');
	
	$(document).keypress(function(e) { if (e.keyCode == 13) e.preventDefault(); });
});

function fnPageUpload(){
	var storeName = $("#storeNm").val(); 
	if(""== storeName){
		showAlertModal("danger", "${store_reflash}");
		return;
	}
	var text = storeName+" ${store_reflashMsg}";
	bootbox.confirm({
		size: "small",
		title: "${confirm_title}",
		message: text,
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
				var data = {
						page : "S",
						siteId : $("#site").val(),
						storeId : $("#store").val()
					};
				$.ajax({
					type: "POST",
					contentType: "application/json",
					dataType: "json",
					url: "${storeMonTask}",
					data: JSON.stringify(data),
					success: function (data, status, xhr) {
						showSaveSuccessMsg();
					},
					error: ajaxSaveError
				});
			}
		}
	});
}

</script>

<func:cmmValidate />

<common:base />
<common:pageClosing />
