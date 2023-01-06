<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/storeuser/create" var="createUrl" />
<c:url value="/pay/storeuser/read" var="readUrl" />
<c:url value="/pay/storeuser/update" var="updateUrl" />

<c:url value="/pay/storeuser/destroy" var="destroyUrl" />
<c:url value="/pay/storeuser/exclude" var="excludeUrl" />

<c:url value="/pay/storeuser/readUsers" var="readUserUrl" />


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
	String loginDateTemplate = kr.co.paycast.utils.Util.getSmartDate("lastLoginDate");
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
<kendo:grid name="grid" pageable="false" filterable="false" sortable="false" scrollable="false" reorderable="false" resizable="true" selectable="${value_gridSelectable}" >
	<kendo:grid-excel fileName="${pageTitle}.xlsx" allPages="true" proxyURL="/proxySave"/>
	<kendo:grid-pageable refresh="true" previousNext="false" numeric="false" pageSize="10000" info="false" />
	<kendo:grid-toolbarTemplate>
    	<div class="clearfix">
    		<div class="float-left">
    			<button id="add-btn" type="button" class="btn btn-outline-success">${cmd_add}</button>
    			<button type="button" class="btn btn-default d-none d-sm-inline k-grid-excel">${cmd_excel}</button>
    		</div>
    		<div class="float-right">
    			<div class="btn-group">
	    			<button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown">${cmd_delete}</button>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="javascript:excludeRows()"><span class='fas fa-unlink fa-fw mr-2'></span>${cmd_exclude}</a>
						<a class="dropdown-item" href="javascript:deleteRows()"><span class='far fa-trash-alt fa-fw mr-2 text-danger'></span>${cmd_delete}</a>
					</div>
    			</div>
    		</div>
    	</div>
	</kendo:grid-toolbarTemplate>
	<kendo:grid-columns>
		<kendo:grid-column title="${cmd_edit}" width="50" filterable="false" sortable="false" template="<%= editTemplate %>" />
		<kendo:grid-column title="${title_username}" field="username" />
		<kendo:grid-column title="${title_familiarName}" field="familiarName" />
		<kendo:grid-column title="${title_effective}" field="effectiveUser" template="#= effectiveUser ? \"<span class='fas fa-check'>\" : \"\"#" />
		<kendo:grid-column title="${title_lastLogin}" field="lastLoginDate" template="<%= loginDateTemplate %>" />
		<kendo:grid-column title="${title_elapsedTime}" template="#= diffTime #" />
	</kendo:grid-columns>
	<kendo:dataSource serverPaging="false" serverSorting="false" serverFiltering="false" serverGrouping="false" error="kendoReadError">
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
		<kendo:dataSource-schema>
			<kendo:dataSource-schema-model id="id">
				<kendo:dataSource-schema-model-fields>
					<kendo:dataSource-schema-model-field name="lastLoginDate" type="date"/>
				</kendo:dataSource-schema-model-fields>
			</kendo:dataSource-schema-model>
		</kendo:dataSource-schema>
	</kendo:dataSource>
</kendo:grid>
</div>

<style>

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

	// Add
	$("#add-btn").click(function(e) {
		e.preventDefault();
		
		initForm1();

		
		$('#form-modal-1 .modal-dialog').draggable({ handle: '.modal-header' });
		$("#form-modal-1").modal();
	});
	// / Add

});	
</script>

<!-- / Grid button actions  -->


<!--  Root form container -->
<div id="formRoot"></div>


<!--  Forms -->

<script id="template-1" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog">
		<form class="modal-content" id="form-1" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${label_addUser}
					<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div id="accordion">
					<div class="card mb-2">
						<div class="card-header">
							<a id="acc-tab-1" class="d-flex justify-content-between text-dark" data-toggle="collapse" href="\\\#country">
								${label_regNew}
								<div class="collapse-icon"></div>
							</a>
						</div>
						<div id="country" class="collapse" data-parent="\\\#accordion">
							<div class="card-body pb-3">
								<div class="form-row">
									<div class="col-sm-6">
										<div class="form-group col">
											<label class="form-label">
												${title_username}
											</label>
											<input name="username" type="text" maxlength="50" class="form-control" required>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group col">
											<label class="form-label">
												${title_familiarName}
											</label>
											<input name="familiarName" type="text" maxlength="25" class="form-control" required>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="card mb-2">
						<div class="card-header">
							<a id="acc-tab-2" class="collapsed d-flex justify-content-between text-dark" data-toggle="collapse" href="\\\#region">
								${label_addPrevUser}
								<div class="collapse-icon"></div>
							</a>
						</div>
						<div id="region" class="collapse" data-parent="\\\#accordion">
							<div class="card-body">
								<select name="users" class="form-control border-none"></select>
							</div>
						</div>
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


<script id="template-2" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-2">
	<div class="modal-dialog modal-sm">
		<form class="modal-content" id="form-2" rowid="-1" url="${createUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${pageTitle}
					<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-group col">
					<label class="form-label">
						${title_username}
						<span class="text-danger">*</span>
					</label>
					<input name="username" type="text" class="form-control required">
				</div>
				<div class="form-group col">
					<label class="form-label">
						${title_familiarName}
						<span class="text-danger">*</span>
					</label>
					<input name="familiarName" type="text" class="form-control required">
				</div>
				<div class="form-group col">
					<label class="form-label">
						${label_password}
					</label>
					<input name="password" type="password" class="form-control">
				</div>
				<div class="form-group col">
					<label class="form-label">
						${label_effectiveStartDate}
						<span class="text-danger">*</span>
					</label>
					<input name="effectiveStartDate" type="text" class="form-control required">
				</div>
				<div class="form-group col">
					<label class="form-label">
						${label_effectiveEndDate}
					</label>
					<input name="effectiveEndDate" type="text" class="form-control">
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
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

	$("#form-1 input[name='username']").val("${ovwShortName}");
	$("#form-1 input[name='familiarName']").val("${ovwStoreName}");

	$("#form-1 select[name='users']").kendoMultiSelect({
        dataTextField: "familiarName",
        dataValueField: "id",
        tagTemplate: "<span class='far fa-user text-gray'></span>" + 
        			 "<span class='pl-2' title='#:data.username#'>#:data.familiarName#</span>",
        itemTemplate: "<span class='far fa-user text-gray'></span>" +
        		      "<span class='pl-2' title='#:data.username#'>#:data.familiarName#</span>",
        dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: "${readUserUrl}",
                    type: "POST",
                    contentType: "application/json"
                },
                parameterMap: function (options) {
            		return JSON.stringify(options);	
                }
            },
            schema: {
            	data: "data",
            	total: "total"
            },
			error: kendoReadError
        },
        noDataTemplate: "${control_noRows}",
    });

	
	$("#form-1 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");

	$('[data-toggle="collapse"]').on('click',function(e){
		if ( $(this).parents('.accordion').find('.collapse.show') ){
			var idx = $(this).index('[data-toggle="collapse"]');
			if (idx == $('.collapse.show').index('.collapse')) {
				e.stopPropagation();
			}
		}
	});
	
	$("#form-1").validate({
		rules: {
			username: {
				minlength: 2, alphanumeric: true,
			},
			familiarName: {
				minlength: 2,
			},
		}
	});
}


function saveForm1() {

	var tab1 = $("#acc-tab-1").attr("aria-expanded");
	var tab2 = $("#acc-tab-2").attr("aria-expanded");
	
	if (tab1 == undefined) {
		tab1 = false;
	}
	
	if (tab2 == undefined) {
		tab2 = false;
	}
	
	var username = $.trim($("#form-1 input[name='username']").val());
	var familiarName = $.trim($("#form-1 input[name='familiarName']").val());
	var userIds = $("#form-1 select[name='users']").data("kendoMultiSelect").value();

	var destType = "";
	var goAhead = false;

	if (tab1 == "true" && $("#form-1").valid()) {
		goAhead = true;
		destType = "N";
	} else if (tab2 == "true" && userIds.length > 0) {
		goAhead = true;
		destType = "U";
	}

	if (goAhead) {
		showWaitModal();
		
    	var data = {
			destType: destType,
			username: username,
			familiarName: familiarName,
			userIds: userIds,
    	};
    	
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: $("#form-1").attr("url"),
			data: JSON.stringify(data),
			success: function (data, status, xhr) {
				hideWaitModal();
				
				if (JSON.parse(xhr.responseText) == "OK") {
					showSaveSuccessMsg();
				} else {
					showAlertModal("success", JSON.parse(xhr.responseText));
				}
				
				$("#form-modal-1").modal("hide");
				$("#grid").data("kendoGrid").dataSource.read();
			},
			error: function(e) {
				hideWaitModal();
				
				if (JSON.parse(e.responseText).error == "SaveError") {
					showSaveErrorMsg();
				} else {
					showAlertModal("danger", JSON.parse(e.responseText).error);
				}
			}
		});
	}
}


function initForm2(subtitle) {
	
	$("#formRoot").html(kendo.template($("#template-2").html()));
	
	$("#form-2 input[name='effectiveStartDate']").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd",
		],
		value: new Date(),
	});
	
	$("#form-2 input[name='effectiveEndDate']").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd",
		],
	});
	
	$("#form-2 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");
	
	$("#form-2").validate({
		rules: {
			username: {
				minlength: 2, maxlength: 50, alphanumeric: true,
			},
			familiarName: {
				minlength: 2, maxlength: 25,
			},
			effectiveStartDate: { date: true },
			effectiveEndDate: { date: true },
		}
	});
}


function edit(id) {
	
	initForm2("${form_edit}");

	var dataItem = $("#grid").data("kendoGrid").dataSource.get(id);
	
	$("#form-2").attr("rowid", dataItem.id);
	$("#form-2").attr("url", "${updateUrl}");
	
	$("#form-2 input[name='username']").val(dataItem.username);
	$("#form-2 input[name='familiarName']").val(dataItem.familiarName);
	
	$("#form-2 input[name='effectiveStartDate']").data("kendoDatePicker").value(dataItem.effectiveStartDate);
	$("#form-2 input[name='effectiveEndDate']").data("kendoDatePicker").value(dataItem.effectiveEndDate);

	
	$('#form-modal-2 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-2").modal();
}


function saveForm2() {

	// kendo datepicker validation
	validateKendoDateValue($("#form-2 input[name='effectiveStartDate']"));
	validateKendoDateValue($("#form-2 input[name='effectiveEndDate']"));
	
	if ($("#form-2").valid()) {
    	var data = {
    		id: Number($("#form-2").attr("rowid")),
    		username: $.trim($("#form-2 input[name='username']").val()),
    		familiarName: $.trim($("#form-2 input[name='familiarName']").val()),
    		newPassword: $.trim($("#form-2 input[name='password']").val()),
    		effectiveStartDate: $("#form-2 input[name='effectiveStartDate']").data("kendoDatePicker").value(),
    		effectiveEndDate: $("#form-2 input[name='effectiveEndDate']").data("kendoDatePicker").value(),
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


function excludeRows() {

	var grid = $("#grid").data("kendoGrid");
	var rows = grid.select();

	var opRows = [];
	rows.each(function(index, row) {
		var selectedItem = grid.dataItem(row);
		opRows.push(selectedItem.storeUserId);
	});
		
	if (opRows.length > 0) {
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${excludeUrl}",
			data: JSON.stringify({ items: opRows }),
			success: function (form) {
				showOperationSuccessMsg();
				grid.dataSource.read();
			},
			error: ajaxOperationError
		});
	}
}


function deleteRows() {

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
}

</script>

<!--  / Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
