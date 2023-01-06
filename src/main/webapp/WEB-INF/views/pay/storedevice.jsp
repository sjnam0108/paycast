<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/storedevice/create" var="createUrl" />
<c:url value="/pay/storedevice/read" var="readUrl" />
<c:url value="/pay/storedevice/update" var="updateUrl" />
<c:url value="/pay/storedevice/destroy" var="destroyUrl" />

<c:url value="/pay/storedevice/readDeviceTypes" var="readDeviceTypeUrl" />


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold pt-1 pb-3 mb-2">
	<div class="d-flex justify-content-between align-items-center">
		<div>
			<span class="mr-1 ${sessionScope['loginUser'].icon}"></span>
			${pageTitle}
		</div>
		<button id="sel-store-btn" type="button" class="btn btn-secondary btn-sm mb-1">${btn_selectOtherStore}</button>
	</div>
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<!-- Page message  -->

<c:if test="${not empty notifMsg}">

	<div class="alert alert-info alert-dismissible fade show">
		<button type="button" class="close" data-dismiss="alert">×</button>
		${notifMsg}
	</div>

</c:if>

<!-- / Page message  -->
						

<!-- Java(optional)  -->

<%
	String editTemplate = 
			"<button type='button' onclick='edit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>" + 
			"<span class='fas fa-pencil-alt'></span></button>";
	String typeTemplate =
			"# if (deviceType == 'K') { #" + "<span title='" + request.getAttribute("deviceType_kiosk") + "'><span class='fas fa-hamburger'></span>" +
			"# } else if (deviceType == 'D') { #" + "<span title='" + request.getAttribute("deviceType_kitchenPad") + "'><span class='fas fa-tablet-alt'></span>" +
			"# } else if (deviceType == 'N') { #" + "<span title='" + request.getAttribute("deviceType_notifier") + "'><span class='fas fa-solar-panel'></span>" +
			"# } else if (deviceType == 'P') { #" + "<span title='" + request.getAttribute("deviceType_printer") + "'><span class='fas fa-print'></span>" +
			"# } #";
%>


<!--  Overview -->

<div class="card mb-3">
	<h6 class="card-header with-elements py-2">
		<span>${ovwStoreName}</span>
		<span class="d-none d-sm-inline">
			<span class='px-2'>•</span>
			<span>${ovwShortName}</span>
		</span>
	</h6>
	<div class="row no-gutters row-bordered row-border-light">
		<div class="col-12 col-xl-2">
			<div class="d-flex align-items-center justify-content-center container-p-x text-muted h-100 py-3">
				<span title="${part_kioskOrder}" class="pr-3" name="kioskOrderIcon">
					<span class="fas fa-hamburger fa-2x"></span>
				</span>
				<span title="${part_mobileOrder}" class="pr-3" name="mobileOrderIcon">
					<span class="fas fa-wifi fa-2x"></span>
				</span>
				<span title="${part_kitchenPad}" name="kitchenPadIcon">
					<span class="fas fa-tablet-alt fa-2x"></span>
				</span>
			</div>
		</div>
		<div class="col-6 col-md-3 col-xl">
			<div class="d-flex align-items-center container-p-x py-4">
				<span class="fas fa-hamburger fa-2x text-gray"></span>
				<div class="ml-3">
					<div class="text-muted small">${deviceType_kiosk}</div>
					<div class="text-large">${kCnt}</div>
				</div>
			</div>
		</div>
		<div class="col-6 col-md-3 col-xl">
			<div class="d-flex align-items-center container-p-x py-4">
				<span class="fas fa-tablet-alt fa-2x text-gray"></span>
				<div class="ml-3">
					<div class="text-muted small">${deviceType_kitchenPad}</div>
					<div class="text-large">${dCnt}</div>
				</div>
			</div>
		</div>
		<div class="col-6 col-md-3 col-xl">
			<div class="d-flex align-items-center container-p-x py-4">
				<span class="fas fa-solar-panel fa-2x text-gray"></span>
				<div class="ml-3">
					<div class="text-muted small">${deviceType_notifier}</div>
					<div class="text-large">${nCnt}</div>
				</div>
			</div>
		</div>
		<div class="col-6 col-md-3 col-xl">
			<div class="d-flex align-items-center container-p-x py-4">
				<span class="fas fa-print fa-2x text-gray"></span>
				<div class="ml-3">
					<div class="text-muted small">${deviceType_printer}</div>
					<div class="text-large">${pCnt}</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!--  / Overview -->


<!--  Scripts -->

<script>

$(document).ready(function() {
	
	<c:if test="${koAllowed}">
		$("span[name='kioskOrderIcon']").addClass("text-blue");
	</c:if>

	<c:if test="${moAllowed}">
		$("span[name='mobileOrderIcon']").addClass("text-blue");
	</c:if>

	<c:if test="${kpAllowed}">
		$("span[name='kitchenPadIcon']").addClass("text-blue");
	</c:if>
	
});	
</script>

<!--  / Scripts -->


<!-- Kendo grid  -->

<div class="mb-4">
<kendo:grid name="grid" pageable="true" filterable="true" sortable="true" scrollable="false" reorderable="true" resizable="true" selectable="single" >
	<kendo:grid-excel fileName="${pageTitle}.xlsx" allPages="true" proxyURL="/proxySave"/>
	<kendo:grid-pageable refresh="true" previousNext="false" numeric="false" pageSize="10000" info="false" />
	<kendo:grid-toolbarTemplate>
    	<div class="clearfix">
    		<div class="float-left">
    			<div class="btn-group">
	    			<button type="button" class="btn btn-outline-success dropdown-toggle" data-toggle="dropdown">${cmd_add}</button>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="javascript:add('K')"><span class='fas fa-hamburger fa-fw mr-2'></span>${deviceType_kiosk}</a>
						<a class="dropdown-item" href="javascript:add('D')"><span class='fas fa-tablet-alt fa-fw mr-2'></span>${deviceType_kitchenPad}</a>
						<a class="dropdown-item" href="javascript:add('N')"><span class='fas fa-solar-panel fa-fw mr-2'></span>${deviceType_notifier}</a>
						<a class="dropdown-item" href="javascript:add('P')"><span class='fas fa-print fa-fw mr-2'></span>${deviceType_printer}</a>
					</div>
    			</div>
    			<button type="button" class="btn btn-default d-none d-sm-inline k-grid-excel">${cmd_excel}</button>
    		</div>
    		<div class="float-right">
    			<button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
    		</div>
    	</div>
	</kendo:grid-toolbarTemplate>
	<kendo:grid-columns>
		<kendo:grid-column title="${cmd_edit}" width="50" filterable="false" sortable="false" template="<%= editTemplate %>" />
		<kendo:grid-column title="${title_ukid}" field="ukid" filterable="false" />
		<kendo:grid-column title="${title_deviceType}" field="deviceType" template="<%= typeTemplate %>" filterable="false" >
			<kendo:grid-column-filterable multi="true" itemTemplate="kfcIconText">
				<kendo:dataSource>
					<kendo:dataSource-transport>
						<kendo:dataSource-transport-read url="${readDeviceTypeUrl}" dataType="json" type="POST" contentType="application/json" />
					</kendo:dataSource-transport>
				</kendo:dataSource>
			</kendo:grid-column-filterable>
		</kendo:grid-column>
		<kendo:grid-column title="${title_deviceSeq}" field="deviceSeq" filterable="false" minScreenWidth="500" />
		<kendo:grid-column title="${title_familiarName}" field="familiarName" minScreenWidth="600" filterable="false" />
	</kendo:grid-columns>
	<kendo:grid-filterable>
		<kendo:grid-filterable-messages selectedItemsFormat="${filter_selectedItems}"/>
	</kendo:grid-filterable>
	<kendo:dataSource serverPaging="true" serverSorting="true" serverFiltering="true" serverGrouping="true" error="kendoReadError">
		<kendo:dataSource-sort>
			<kendo:dataSource-sortItem field="deviceType" dir="asc"/>
			<kendo:dataSource-sortItem field="deviceSeq" dir="asc"/>
		</kendo:dataSource-sort>
		<kendo:dataSource-filter>
			<kendo:dataSource-filterItem field="store.site.id" operator="eq" logic="and" value="${sessionScope['currentSiteId']}">
				<kendo:dataSource-filterItem field="store.id" operator="eq" logic="and" value="${sessionScope['currentStoreId']}">
				</kendo:dataSource-filterItem>
			</kendo:dataSource-filterItem>
		</kendo:dataSource-filter>
		<kendo:dataSource-transport>
			<kendo:dataSource-transport-read url="${readUrl}" dataType="json" type="POST" contentType="application/json" />
			<kendo:dataSource-transport-parameterMap>
				<script>
					function parameterMap(options,type) {
						return JSON.stringify(options);	
					}
				</script>
			</kendo:dataSource-transport-parameterMap>
		</kendo:dataSource-transport>
		<kendo:dataSource-schema data="data" total="total" groups="data">
			<kendo:dataSource-schema-model id="id">
			</kendo:dataSource-schema-model>
		</kendo:dataSource-schema>
	</kendo:dataSource>
</kendo:grid>
</div>

<style>

/* 선택 체크박스를 포함하는 필터 패널을 보기 좋게 */
.k-filter-selected-items {
	font-weight: 500;
	margin: 0.5em 0;
}
.k-filter-menu .k-button {
	width: 47%;
	margin: 0.5em 1% 0.25em;
}


/* 그리드 자료 새로고침 버튼을 우측 정렬  */
div.k-pager-wrap.k-grid-pager.k-widget.k-floatwrap {
	display: flex!important;
	justify-content: flex-end!important;
}

</style>

<!-- / Kendo grid  -->


<!-- Grid button actions  -->

<script>
$(document).ready(function() {

	// Delete
	$("#delete-btn").click(function(e) {
		e.preventDefault();
			
		var grid = $("#grid").data("kendoGrid");
		var rows = grid.select();
	
		var delRows = [];
		rows.each(function(index, row) {
			var selectedItem = grid.dataItem(row);
			delRows.push(selectedItem.id);
		});
			
		if (delRows.length > 0) {
			showDelConfirmModal(function(result) {
				if (result) {
					$.ajax({
						type: "POST",
						contentType: "application/json",
						dataType: "json",
						url: "${destroyUrl}",
						data: JSON.stringify({ items: delRows }),
						success: function (form) {
        					showDeleteSuccessMsg();
							grid.dataSource.read();
						},
						error: ajaxDeleteError
					});
				}
			}, true, delRows.length);
		}
	});
	// / Delete
	
});	
</script>

<!-- / Grid button actions  -->


<!--  Root form container -->
<div id="formRoot"></div>


<!--  Forms -->

<script id="template-1" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog modal-sm">
		<form class="modal-content" id="form-1" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${label_device}
					<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-group col">
					<label class="form-label">
						${title_storeName}
						<span class="text-danger">*</span>
					</label>
					<div name="storeName-con">
						<input name="storeName" type="text" class="form-control min-height--2px">
					</div>
					<label name="storeName-feedback" for="storeName" class="small error invalid-feedback"></label>
				</div>
				<div class="form-group col">
					<label class="form-label">
						${title_deviceType}
					</label>
					<select name="deviceType" class="selectpicker bg-white" data-style="btn-default" data-tick-icon="fa-blank" data-none-selected-text="">
						<option value="K" data-icon="fas fa-hamburger fa-fw mr-2">${deviceType_kiosk}</option>
						<option value="D" data-icon="fas fa-tablet-alt fa-fw mr-2">${deviceType_kitchenPad}</option>
						<option value="N" data-icon="fas fa-solar-panel fa-fw mr-2">${deviceType_notifier}</option>
						<option value="P" data-icon="fas fa-print fa-fw mr-2">${deviceType_printer}</option>
					</select>
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveForm1()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>


<script id="template-2" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-2">
	<div class="modal-dialog">
		<form class="modal-content" id="form-2" rowid="-1" url="${editUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${label_device}
					<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-row">
					<div class="col-sm-5">
						<div class="form-group col">
							<label class="form-label">
								${title_storeName}
							</label>
							<input name="storeName" type="text" class="form-control" readonly>
						</div>
					</div>
					<div class="col-sm-5">
						<div class="form-group col">
							<label class="form-label">
								${title_deviceType}
							</label>
							<select name="deviceType" class="selectpicker bg-white" data-style="btn-default" data-tick-icon="fa-blank" data-none-selected-text="">
								<option value="K" data-icon="fas fa-hamburger fa-fw mr-2">${deviceType_kiosk}</option>
								<option value="D" data-icon="fas fa-tablet-alt fa-fw mr-2">${deviceType_kitchenPad}</option>
								<option value="N" data-icon="fas fa-solar-panel fa-fw mr-2">${deviceType_notifier}</option>
								<option value="P" data-icon="fas fa-print fa-fw mr-2">${deviceType_printer}</option>
							</select>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group col">
							<label class="form-label">
								${title_deviceSeq}
							</label>
							<input name="deviceSeq" type="text" class="form-control" readonly>
						</div>
					</div>
				</div>
				<div class="form-row">
					<div class="col-sm-6">
						<div class="form-group col">
							<label class="form-label">
								${title_ukid}
							</label>
							<input name="ukid" type="text" class="form-control" style="text-transform: uppercase;" maxlength="8">
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group col">
							<label class="form-label">
								${title_familiarName}
							</label>
							<input name="familiarName" type="text" class="form-control" maxlength="25">
						</div>
					</div>
				</div>
				<div class="form-row">
					<div class="col-sm-12">
						<div class="form-group col">
							<label class="form-label">
								${label_fcmToken}
							</label>
							<input name="fcmToken" type="text" class="form-control" readonly>
						</div>
					</div>
				</div>
				<div class="form-row">
					<div class="col-sm-12">
						<div class="form-group col">
							<label class="form-label">
								${label_memo}
							</label>
							<textarea name="memo" rows="2" class="form-control"></textarea>
						</div>
					</div>
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer d-flex">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveForm2()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>

<!--  / Forms -->


<!--  Scripts -->

<script>

//Form validator
var validator = null;

//Form error obj
var errors = {};


$(document).ready(function() {

	// Select store
	$("#sel-store-btn").click(function(e) {
		e.preventDefault();
		
		location.href = "/pay/selectstore/check?uri=" + window.location.pathname;
	});
	// / Select store
	
});

function initForm1(subtitle) {
	
	$("#formRoot").html(kendo.template($("#template-1").html()));

	$("#form-1 select[name='deviceType']").selectpicker('render');
	
	$("#form-1 input[name='storeName']").kendoAutoComplete({
        dataTextField: "name",
        dataValueField: "id",
        dataSource: {
		    serverFiltering: true,
			transport: {
				read: {
				    dataType: "json",
				    url: "${readStoreUrl}",
				    type: "POST",
				    contentType: "application/json",
					data: JSON.stringify({}),
				},
				parameterMap: function (options) {
                	return JSON.stringify(options);
				},
			},
			error: function(e) {
      			showReadErrorMsg();
			}
        },
        change: function(e) {
        	checkStoreName();
        },
        height: 400,
        delay: 500,
        minLength: 1,
        filter: "contains",
        noDataTemplate: "${control_noRows}",
    });
	
	$("#form-1 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");
	
	validator = $("#form-1").validate({
		rules: {
			storeName: {
				required: true,
			},
		}
	});
}


function isStoreNameValid() {
	
	var storeName = $.trim($("#form-1 input[name='storeName']").val());
	return (storeName && storeName.length > 0);
}


function checkStoreName() {
	
	var container = $("#form-1 div[name='storeName-con']");
	var feedback = $("#form-1 label[name='storeName-feedback']");
	
	if (isStoreNameValid()) {
		container.removeClass("is-invalid");
		feedback.removeClass("d-block").css("display", "none");
	} else {
		container.addClass("is-invalid");
		feedback.addClass("d-block").css("display", "block");
	}
}


function saveForm1() {

	if (isStoreNameValid()) {
		delete errors["storeName"];
	} else {
		errors["storeName"] = "${form_required}";
	}
	
	if (Object.keys(errors).length == 0) {
		var data = {
	   		storeName: $.trim($("#form-1 input[name='storeName']").val()),
	   		deviceType: $("#form-1 select[name='deviceType']").val(),
	   	};
		
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: $("#form-1").attr("url"),
			data: JSON.stringify(data),
			success: function (form) {
				showSaveSuccessMsg();
				$("#form-modal-1").modal("hide");
				$("#grid").data("kendoGrid").dataSource.read();
			},
			error: ajaxSaveError
		});
	} else {
		validator.showErrors(errors);
		
		checkStoreName();
	}
}


function initForm2(subtitle) {
	
	$("#formRoot").html(kendo.template($("#template-2").html()));

	$("#form-2 select[name='deviceType']").selectpicker('render');
	
	$("#form-2 textarea[name='memo']").keypress(function (e) {
		if (e.keyCode != 13) {
			return;
		}
		
		$(this).text().replace(/\n/g, "");
		
		return false;
	});

	bootstrapSelectDisabled($("#form-2 select[name='deviceType']"), true);

	
	$("#form-2 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");
	
	$("#form-2").validate({
		rules: {
			ukid: {
				minlength: 8, maxlength: 8,
				alphanumeric: true,
			},
		}
	});
}


function edit(id) {
	
	initForm2("${form_edit}");

	var dataItem = $("#grid").data("kendoGrid").dataSource.get(id);
	
	$("#form-2").attr("rowid", dataItem.id);
	$("#form-2").attr("url", "${updateUrl}");
	
	$("#form-2 input[name='storeName']").val(dataItem.store.storeName);
	$("#form-2 input[name='deviceSeq']").val(dataItem.deviceSeq);
	$("#form-2 input[name='familiarName']").val(dataItem.familiarName);
	$("#form-2 input[name='ukid']").val(dataItem.ukid);
	$("#form-2 input[name='fcmToken']").val(dataItem.fcmToken);
	
	$("#form-2 textarea[name='memo']").val(dataItem.memo);

	bootstrapSelectVal($("#form-2 select[name='deviceType']"), dataItem.deviceType);

	
	$('#form-modal-2 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-2").modal();
}


function saveForm2() {

	if ($("#form-2").valid()) {
    	var data = {
       		id: Number($("#form-2").attr("rowid")),
       		familiarName: $.trim($("#form-2 input[name='familiarName']").val()),
       		ukid: $.trim($("#form-2 input[name='ukid']").val()),
    		memo: $.trim($("#form-2 textarea[name='memo']").val()),
    	};
    	
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: $("#form-2").attr("url"),
			data: JSON.stringify(data),
			success: function (form) {
				showSaveSuccessMsg();
				$("#form-modal-2").modal("hide");
				$("#grid").data("kendoGrid").dataSource.read();
			},
			error: ajaxSaveError
		});
	}
}


function add(deviceType) {
	
	var data = {
   		deviceType: deviceType,
   	};
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${createUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			showSaveSuccessMsg();
			$("#form-modal-1").modal("hide");
			$("#grid").data("kendoGrid").dataSource.read();
		},
		error: ajaxSaveError
	});
}

</script>

<!--  / Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
