<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/currappuser/read" var="readUrl" />
<c:url value="/pay/currappuser/update" var="updateUrl" />
<c:url value="/pay/currappuser/destroy" var="destroyUrl" />

<c:url value="/pay/currappuser/activate" var="activateUrl" />
<c:url value="/pay/currappuser/deactivate" var="deactivateUrl" />

<c:url value="/pay/currappuser/readDeviceOSTypes" var="readDeviceOSTypeUrl" />
<c:url value="/pay/currappuser/readStatusTypes" var="readStatusTypeUrl" />


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold pt-1 pb-3 mb-3">
	<span class="mr-1 ${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<!-- Java(optional)  -->

<%
	String editTemplate = 
	"<button type='button' onclick='edit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>" + 
	"<span class='fas fa-pencil-alt'></span></button>";
	String osTemplate =
	"<span class='#: getOsIconDisp(osType) # fa-lg fa-fw'></span>" +
	"<span class='pl-1'>#: getOsTypeDisp(osType) # #: getOsVerDisp(osType, osVer) #</span>";
	String statusTemplate =
	"<span title='#: getStatusTip(status) #'><span class='#: getStatusIconDisp(status) # fa-fw'></span></span>";
	String dateTemplate = kr.co.paycast.utils.Util.getSmartDate();
%>


<!-- Kendo grid  -->

<div class="mb-4">
<kendo:grid name="grid" pageable="true" filterable="true" sortable="true" scrollable="false" reorderable="true" resizable="true" selectable="${value_gridSelectable}" >
	<kendo:grid-excel fileName="${pageTitle}.xlsx" allPages="true" proxyURL="/proxySave"/>
	<kendo:grid-pageable refresh="true" buttonCount="5" pageSize="10" pageSizes="${pageSizesNormal}" />
	<kendo:grid-toolbarTemplate>
    	<div class="clearfix">
    		<div class="float-left">
    			<button type="button" class="btn btn-default d-none d-sm-inline k-grid-excel">${cmd_excel}</button>
    		</div>
    		<div class="float-right">
    			<button id="activate-btn" type="button" class="btn btn-default">${cmd_activate}</button>
    			<button id="deactivate-btn" type="button" class="btn btn-default">${cmd_deactivate}</button>
    			<button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
    		</div>
    	</div>
	</kendo:grid-toolbarTemplate>
	<kendo:grid-columns>
		<kendo:grid-column title="${cmd_edit}" width="50" filterable="false" sortable="false" template="<%= editTemplate %>" />
		<kendo:grid-column title="${title_username}" field="user.username" />
		<kendo:grid-column title="${title_familiarName}" field="user.familiarName" minScreenWidth="800" />
		<kendo:grid-column title="${title_deviceName}" field="deviceName" filterable="false" />
		<kendo:grid-column title="${title_os}" field="osType" template="<%= osTemplate %>" minScreenWidth="1100" >
			<kendo:grid-column-filterable multi="true" itemTemplate="kfcIconText">
				<kendo:dataSource>
					<kendo:dataSource-transport>
						<kendo:dataSource-transport-read url="${readDeviceOSTypeUrl}" dataType="json" type="POST" contentType="application/json" />
					</kendo:dataSource-transport>
				</kendo:dataSource>
			</kendo:grid-column-filterable>
		</kendo:grid-column>
		<kendo:grid-column title="${title_status}" field="status" template="<%= statusTemplate %>" >
			<kendo:grid-column-filterable multi="true" itemTemplate="kfcIconText">
				<kendo:dataSource>
					<kendo:dataSource-transport>
						<kendo:dataSource-transport-read url="${readStatusTypeUrl}" dataType="json" type="POST" contentType="application/json" />
					</kendo:dataSource-transport>
				</kendo:dataSource>
			</kendo:grid-column-filterable>
		</kendo:grid-column>
		<kendo:grid-column title="${title_lang}" field="lang" minScreenWidth="550" />
		<kendo:grid-column title="${title_regDate}" field="whoCreationDate" template="<%= dateTemplate %>" minScreenWidth="650" />
	</kendo:grid-columns>
	<kendo:grid-filterable>
		<kendo:grid-filterable-messages selectedItemsFormat="${filter_selectedItems}"/>
	</kendo:grid-filterable>
	<kendo:dataSource serverPaging="true" serverSorting="true" serverFiltering="true" serverGrouping="true" error="kendoReadError">
		<kendo:dataSource-sort>
			<kendo:dataSource-sortItem field="user.username" dir="asc"/>
			<kendo:dataSource-sortItem field="deviceName" dir="asc"/>
		</kendo:dataSource-sort>
		<kendo:dataSource-filter>
			<kendo:dataSource-filterItem field="site.id" operator="eq" logic="and" value="${sessionScope['currentSiteId']}">
			</kendo:dataSource-filterItem>
		</kendo:dataSource-filter>
		<kendo:dataSource-transport>
			<kendo:dataSource-transport-read url="${readUrl}" dataType="json" type="POST" contentType="application/json"/>
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
				<kendo:dataSource-schema-model-fields>
					<kendo:dataSource-schema-model-field name="whoCreationDate" type="date"/>
				</kendo:dataSource-schema-model-fields>
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

</style>

<!-- / Kendo grid  -->


<!-- Grid button actions  -->

<script>
$(document).ready(function() {

	// Activate
	$("#activate-btn").click(function(e) {
		e.preventDefault();
			
		var grid = $("#grid").data("kendoGrid");
		var rows = grid.select();
	
		var opRows = [];
		rows.each(function(index, row) {
			var selectedItem = grid.dataItem(row);
			opRows.push(selectedItem.id);
		});
			
		if (opRows.length > 0) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: "${activateUrl}",
				data: JSON.stringify({ items: opRows }),
				success: function (form) {
      					showOperationSuccessMsg();
					grid.dataSource.read();
				},
				error: ajaxOperationError
			});
		}
	});
	// / Activate

	// Deactivate
	$("#deactivate-btn").click(function(e) {
		e.preventDefault();
			
		var grid = $("#grid").data("kendoGrid");
		var rows = grid.select();
	
		var opRows = [];
		rows.each(function(index, row) {
			var selectedItem = grid.dataItem(row);
			opRows.push(selectedItem.id);
		});
			
		if (opRows.length > 0) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: "${deactivateUrl}",
				data: JSON.stringify({ items: opRows }),
				success: function (form) {
      					showOperationSuccessMsg();
					grid.dataSource.read();
				},
				error: ajaxOperationError
			});
		}
	});
	// / Deactivate
	
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
					${pageTitle}
					<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body py-3">
				<div class="row mb-2 px-3">
					<div class="col-5 text-muted">${title_username}:</div>
					<div class="col-7"><span name="username"></span></div>
				</div>
				<div class="row mb-2 px-3">
					<div class="col-5 text-muted">${title_familiarName}:</div>
					<div class="col-7"><span name="familiarName"></span></div>
				</div>
				<div class="row mb-2 px-3">
					<div class="col-5 text-muted">${title_deviceName}:</div>
					<div class="col-7"><span name="deviceName"></span></div>
				</div>
				<div class="row mb-2 px-3">
					<div class="col-5 text-muted">${title_os}:</div>
					<div class="col-7"><span name="os"></span></div>
				</div>
				<hr>
				<div class="row px-2">
					<div class="form-group col">
						<label class="form-label">
							${label_fcmToken}
						</label>
						<input name="fcmToken" type="text" class="form-control" readonly="readonly">
					</div>
				</div>
				<div class="row px-2">
					<div class="form-group col">
						<label class="form-label">
							${title_status}
						</label>
						<select name="status" class="selectpicker bg-white" data-style="btn-default" data-icon-base="fas" data-tick-icon="fa-blank" data-none-selected-text="">
							<option value="R" data-icon="fa-hourglass-half fa-fw mr-2">${tip_statusR}</option>
							<option value="U" data-icon="fa-user text-secondary fa-fw mr-2">${tip_statusU}</option>
							<option value="S" data-icon="fa-robot text-secondary fa-fw mr-2">${tip_statusS}</option>
							<option value="D" data-icon="fa-user-tie text-secondary fa-fw mr-2">${tip_statusD}</option>
							<option value="A" data-icon="fa-bell text-blue fa-fw mr-2">${tip_statusA}</option>
						</select>
					</div>
				</div>
				<div class="row px-2">
					<div class="form-group col">
						<label class="form-label">
							${title_lang}
						</label>
						<select name="lang" class="selectpicker bg-white" data-style="btn-default" data-none-selected-text="">
							<option value="en">en</option>
							<option value="ko">ko</option>
						</select>
					</div>
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

<!--  / Forms -->


<!--  Scripts -->

<script>

function initForm1(subtitle) {
	
	$("#formRoot").html(kendo.template($("#template-1").html()));
	
	$("#form-1 select[name='status']").selectpicker('render');
	$("#form-1 select[name='lang']").selectpicker('render');
	
	$("#form-1 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");
}


function saveForm1() {

   	var data = {
   		id: Number($("#form-1").attr("rowid")),
   		status: $("#form-1 select[name='status']").val(),
   		lang: $("#form-1 select[name='lang']").val(),
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
}


function edit(id) {
	
	initForm1("${form_edit}");

	var dataItem = $("#grid").data("kendoGrid").dataSource.get(id);
	
	$("#form-1").attr("rowid", dataItem.id);
	$("#form-1").attr("url", "${updateUrl}");
	
	$("#form-1 span[name='username']").html(dataItem.user.username);
	$("#form-1 span[name='familiarName']").html(dataItem.user.familiarName);
	$("#form-1 span[name='deviceName']").html(dataItem.deviceName);
	$("#form-1 span[name='os']").html(kendo.template(
			"<span class='#: getOsIconDisp(osType) # fa-fw'></span>" +
			"<span class='pl-1'>#: getOsTypeDisp(osType) # #: getOsVerDisp(osType, osVer) #</span>")(dataItem));
	$("#form-1 input[name='fcmToken']").val(dataItem.fcmToken);
	
	bootstrapSelectVal($("#form-1 select[name='status']"), dataItem.status);
	bootstrapSelectVal($("#form-1 select[name='lang']"), dataItem.lang);

	
	$('#form-modal-1 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-1").modal();
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


function getStatusIconDisp(status) {
	
	if (status) {
		if (status == "R") {
			return "fas fa-hourglass-half";
		} else if (status == "U") {
			return "fas fa-user text-secondary";
		} else if (status == "S") {
			return "fas fa-robot text-secondary";
		} else if (status == "D") {
			return "fas fa-user-tie text-secondary";
		} else if (status == "A") {
			return "fas fa-bell text-blue";
		}
	}
	
	return "";
}


function getStatusTip(status) {
	
	if (status) {
		if (status == "R") {
			return "${tip_statusR}";
		} else if (status == "U") {
			return "${tip_statusU}";
		} else if (status == "S") {
			return "${tip_statusS}";
		} else if (status == "D") {
			return "${tip_statusD}";
		} else if (status == "A") {
			return "${tip_statusA}";
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
