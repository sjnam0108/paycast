<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/store/create" var="createUrl" />
<c:url value="/pay/store/read" var="readUrl" />
<c:url value="/pay/store/update" var="updateUrl" />
<c:url value="/pay/store/destroy" var="destroyUrl" />

<c:url value="/pay/store/changeKey" var="changeKeyUrl" />


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold pt-1 pb-3 mb-3">
	<span class="mr-1 ${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<c:if test="${not empty notifMsg}">

	<div class="alert alert-info alert-dismissible fade show">
		<button type="button" class="close" data-dismiss="alert">×</button>
		${notifMsg}
	</div>

</c:if>
							

<!-- Java(optional)  -->

<%
	String editTemplate = 
			"<button type='button' onclick='edit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>" + 
			"<span class='fas fa-pencil-alt'></span></button>";
	String permissionTemplate =
			"# if (kioskOrderAllowed == true) { #" +
			"<span title='" + request.getAttribute("part_kioskOrder") + "' class='pr-2'><span class='fas fa-hamburger fa-fw'></span></span>" + "# } #" +
			"# if (mobileOrderAllowed == true) { #" +
			"<span title='" + request.getAttribute("part_mobileOrder") + "' class='pr-2'><span class='fas fa-wifi fa-fw'></span></span>" + "# } #" +
			"# if (kitchenPadAllowed == true) { #" +
			"<span title='" + request.getAttribute("part_kitchenPad") + "' class='pr-2'><span class='fas fa-tablet-alt fa-fw'></span></span>" + "# } #"+
			"# if (alimTalkAllowed == true) { #" +
			"<span title='" + request.getAttribute("label_alimTalk") + "'><span class='far fa-comment-alt fa-fw'></span></span>" + "# } #";
	String actionTemplate =
			"<div class='btn-group'>" +
				"<button type='button' class='btn btn-default btn-sm btn-outline-success borderless dropdown-toggle hide-arrow' data-toggle='dropdown'><span class='fas fa-ellipsis-v'></span></button>" +
				"<div class='dropdown-menu'>" +
					"<a class='dropdown-item' href='javascript:visitStore(#= id #, \"info\")'><span class='fas fa-store fa-fw mr-1'></span>" + request.getAttribute("storeMenu_StoreInfo") + "</a>" +
					"<a class='dropdown-item' href='javascript:visitStore(#= id #, \"device\")'><span class='far fa-hdd fa-fw mr-1'></span>" + request.getAttribute("storeMenu_StoreDevice") + "</a>" +
					"<a class='dropdown-item' href='javascript:visitStore(#= id #, \"user\")'><span class='far fa-user fa-fw mr-1'></span>" + request.getAttribute("storeMenu_StoreUser") + "</a>" +
					"<a class='dropdown-item' href='javascript:visitStore(#= id #, \"setting\")'><span class='fas fa-wrench fa-fw mr-1'></span>" + request.getAttribute("storeMenu_StoreSetting") + "</a>" +
				"</div>" +
			"</div>";
%>


<!-- Kendo grid  -->

<div class="mb-4">
<kendo:grid name="grid" pageable="true" filterable="true" sortable="true" scrollable="false" reorderable="true" resizable="true" selectable="single" >
	<kendo:grid-excel fileName="${pageTitle}.xlsx" allPages="true" proxyURL="/proxySave"/>
	<kendo:grid-pageable refresh="true" buttonCount="5" pageSize="10" pageSizes="${pageSizesNormal}" />
	<kendo:grid-toolbarTemplate>
    	<div class="clearfix">
    		<div class="float-left">
    			<button id="add-btn" type="button" class="btn btn-outline-success">${cmd_add}</button>
    			<button type="button" class="btn btn-default d-none d-sm-inline k-grid-excel">${cmd_excel}</button>
    		</div>
    		<div class="float-right">
    			<span class="d-none d-sm-inline">
					${label_onlyEffective}
					<label class="switcher switcher-lg ml-2">
						<input type="checkbox" class="switcher-input" name="view-mode-switch">
						<span class="switcher-indicator"></span>
					</label>
					<span class="mr-3">|</span>
    			</span>
    			<button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
    		</div>
    	</div>
	</kendo:grid-toolbarTemplate>
	<kendo:grid-columns>
		<kendo:grid-column title="${cmd_edit}" width="50" filterable="false" sortable="false" template="<%= editTemplate %>" />
		<kendo:grid-column title="${title_shortName}" field="shortName" />
		<kendo:grid-column title="${title_storeName}" field="storeName" />
		<kendo:grid-column title="${title_permission}" filterable="false" sortable="false" template="<%= permissionTemplate %>" minScreenWidth="550"/>
		<kendo:grid-column title="${title_effectiveStartDate}" field="effectiveStartDate" format="{0:yyyy-MM-dd}" minScreenWidth="700"/>
		<kendo:grid-column width="50" filterable="false" sortable="false" template="<%= actionTemplate %>" />
	</kendo:grid-columns>
	<kendo:dataSource serverPaging="true" serverSorting="true" serverFiltering="true" serverGrouping="true" error="kendoReadError">
		<kendo:dataSource-sort>
			<kendo:dataSource-sortItem field="shortName" dir="asc"/>
		</kendo:dataSource-sort>
		<kendo:dataSource-filter>
			<kendo:dataSource-filterItem field="site.id" operator="eq" logic="and" value="${sessionScope['currentSiteId']}">
			</kendo:dataSource-filterItem>
		</kendo:dataSource-filter>
		<kendo:dataSource-transport>
			<kendo:dataSource-transport-read url="${readUrl}" dataType="json" type="POST" contentType="application/json">
				<kendo:dataSource-transport-read-data>
					<script>
						function additionalData(e) {
							var viewMode = $("input[name='view-mode-switch']").is(":checked") ? "E" : "A";
							if (isFirstRead) {
								viewMode = "${sessionScope['userCookie'].viewCodeStore}";
								isFirstRead = false;
							}
						
							return { reqStrValue1:  viewMode };
						}
					</script>
				</kendo:dataSource-transport-read-data>
			</kendo:dataSource-transport-read>
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
					<kendo:dataSource-schema-model-field name="effectiveStartDate" type="date" />
					<kendo:dataSource-schema-model-field name="effectiveEndDate" type="date" />
				</kendo:dataSource-schema-model-fields>
			</kendo:dataSource-schema-model>
		</kendo:dataSource-schema>
	</kendo:dataSource>
</kendo:grid>
</div>

<style>

/* Kendo 그리드에서 bootstrap dropdownmenu 모두 보이게 */
.k-grid td {
	overflow: visible;
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
					${label_store}
					<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-group col">
					<label class="form-label">
						${title_shortName}
						<span class="text-danger">*</span>
					</label>
					<input name="shortName" type="text" maxlength="25" class="form-control required">
				</div>
				<div class="form-group col">
					<label class="form-label">
						${title_storeName}
						<span class="text-danger">*</span>
					</label>
					<input name="storeName" type="text" maxlength="25" class="form-control required">
				</div>
				<div class="form-group col">
					<label class="form-label">
						${title_effectiveStartDate}
						<span class="text-danger">*</span>
					</label>
					<input name="effectiveStartDate" type="text" class="form-control required">
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
					${label_store}
					<span class="font-weight-light pl-1"><span name="subtitle"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-row px-1">
					<div class="form-group col">
						<label class="form-label">
							${title_shortName}
							<span class="text-danger">*</span>
						</label>
						<input name="shortName" type="text" maxlength="25" class="form-control required">
					</div>
					<div class="form-group col">
						<label class="form-label">
							${title_storeName}
							<span class="text-danger">*</span>
						</label>
						<input name="storeName" type="text" maxlength="25" class="form-control required">
					</div>
				</div>

				<div class="nav-tabs-top mt-1">
					<ul class="nav nav-tabs px-1">
						<li class="nav-item">
							<a class="nav-link active" data-toggle="tab" id="stb-basic" href="\\\#store-basic-ctnt">
								<span>${tab_basic}</span>
							</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" data-toggle="tab" id="stb-option" href="\\\#store-pay-ctnt">
								<span>${tab_pay}</span>
							</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" data-toggle="tab" id="stb-saving" href="\\\#store-saving-ctnt">
								<span>${tab_saving}</span>
							</a>
						</li>
					</ul>
					<div class="tab-content mx-1">
						<div class="tab-pane p-2 active" id="store-basic-ctnt">
							<div class="form-row mt-3">
								<div class="col-sm-6">
									<div class="form-group col">
										<label class="form-label">
											${title_effectiveStartDate}
											<span class="text-danger">*</span>
										</label>
										<input name="effectiveStartDate" type="text" class="form-control required">
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group col">
										<label class="form-label">
											${title_effectiveEndDate}
										</label>
										<input name="effectiveEndDate" type="text" class="form-control">
									</div>
								</div>
							</div>
							<div class="form-row">
								<div class="col-sm-6">
									<div class="form-group col">
										<label class="form-label">
											${label_storeKey}
										</label>
										<div class="form-group">
											<div class="input-group">
												<input name="storeKey" type="text" class="form-control" readonly="readonly">
												<span class="input-group-append">
													<button class="btn btn-default" type="button" onclick='changeKey()'>
														<span class="fas fa-sync fa-sm text-body"></span>
													</button>
												</span>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group col">
										<label class="form-label">
											${label_mobileOrderUrl}
										</label>
										<button type='button' onclick='copyUrl()' class='btn icon-btn btn-sm btn-outline-secondary borderless'>
											<span class='far fa-copy'></span>
										</button>
										<input name="orderUrl" type="text" class="form-control" readonly="readonly">
									</div>
								</div>
							</div>
							<div class="form-row">
								<div class="col-sm-12">
									<div class="form-group col mb-2">
										<label class="form-label d-block">
											${title_permission}
										</label>
										<label class="switcher mb-2">
											<input type="checkbox" class="switcher-input" name="kioskOrderAllowed" checked="checked">
											<span class="switcher-indicator"></span>
											<span class="switcher-label">${part_kioskOrder}</span>
										</label>
										<span class="pl-2"></span>
										<label class="switcher mb-2">
											<input type="checkbox" class="switcher-input" name="mobileOrderAllowed" checked="checked">
											<span class="switcher-indicator"></span>
											<span class="switcher-label">${part_mobileOrder}</span>
										</label>
										<span class="pl-2"></span>
										<label class="switcher mb-2">
											<input type="checkbox" class="switcher-input" name="kitchenPadAllowed" checked="checked">
											<span class="switcher-indicator"></span>
											<span class="switcher-label">${part_kitchenPad}</span>
										</label>
										<label class="switcher mb-2">
											<input type="checkbox" class="switcher-input" name="alimTalkAllowed" checked="checked">
											<span class="switcher-indicator"></span>
											<span class="switcher-label">${label_alimTalk}</span>
										</label>
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
						<div class="tab-pane p-2 fade" id="store-permission-ctnt">
							<div class="form-row mt-3">
							</div>
						</div>
						<div class="tab-pane p-2 fade" id="store-pay-ctnt">
							<input name="mobilePayment" type="hidden" />	
							<div class="form-row mt-3 mb-2">
								<div class="col-12">
									<div class="form-group col mb-1">
										<label class="form-label d-block">결제 방식</label>
										<label class="switcher mb-2">
											<input type="radio" class="switcher-input" name="paymentType" value="AD">
											<span class="switcher-indicator"></span>
											<span class="switcher-label">선불</span>
										</label>
										<span class="pl-2"></span>
										<label class="switcher mb-2">
											<input type="radio" class="switcher-input" name="paymentType" value="DE">
											<span class="switcher-indicator"></span>
											<span class="switcher-label">후불</span>
										</label>
									</div>
									<div class="form-group col mb-1">
										<span class="pr-1">
											<img src="/resources/shared/images/cus/logo_smilepay.png" height="20" />
										</span>
										<button type="button" class="btn btn-xs btn-outline-secondary collapsed" data-toggle="collapse" data-target="\\\#smilepay">
											<span name="smilepay-show"><span class="fas fa-caret-up"></span></span>
											<span name="smilepay-hide" class="collapse"><span class="fas fa-caret-down"></span></span>
											<span class="pl-1">${label_smilepay}</span>
										</button>
										<a name="msBtn" href="javascript:selectRadioBtn('MS')" class="btn btn-outline-primary btn-sm rounded-pill" style="padding-top: 0px; padding-bottom: 0px;">
											<span name="msSelected" style="display: none;">
												<span class="fas fa-check"></span>
												<span class="ml-2">${tip_selected}</span>
											</span>
											<span name="msUnselected">${tip_select}</span>
										</a>							
									</div>
								</div>
							</div>
							<div class="collapse hide" id="smilepay">
								<div class="form-row">
									<div class="col-sm-6">
										<div class="form-group col">
											<label class="form-label">
												${label_smilepayStoreID}
											</label>
											<input name="smilepayStoreID" type="text" class="form-control" readonly />
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group col">
											<label class="form-label">
												${label_smilepayAuthKey}
											</label>
											<input name="smilepayAuthKey" type="text" class="form-control" readonly />
										</div>
									</div>
								</div>
								<div class="form-row">
									<div class="col-sm-6">
										<div class="form-group col">
											<label class="form-label">
												${label_smilepayCancelCode}
											</label>
											<input name="smilepayCancelCode" type="text" class="form-control" readonly />
										</div>
									</div>
								</div>
							</div>
							<div class="form-row mt-3 mb-2">
								<div class="col-12">
									<div class="form-group col mb-1">
										<span class="pr-1">
											<img src="/resources/shared/images/cus/logo_easypay.png" height="20" />
										</span>
										<button type="button" class="btn btn-xs btn-outline-secondary collapsed" data-toggle="collapse" data-target="\\\#easypay">
											<span name="easypay-show" class="collapse"><span class="fas fa-caret-up"></span></span>
											<span name="easypay-hide"><span class="fas fa-caret-down"></span></span>
											<span class="pl-1">${label_easypay}</span>
										</button>
										<a name="meBtn" href="javascript:selectRadioBtn('ME')" class="btn btn-outline-primary btn-sm rounded-pill" style="padding-top: 0px; padding-bottom: 0px;">
											<span name="meSelected" style="display: none;">
												<span class="fas fa-check"></span>
												<span class="ml-2">${tip_selected}</span>
											</span>
											<span name="meUnselected">${tip_select}</span>
										</a>									
									</div>
								</div>
							</div>
							<div class="collapse show" id="easypay">
								<div class="form-row">
									<div class="col-sm-6">
										<div class="form-group col">
											<label class="form-label">
												${label_easypayStoreID}
											</label>
											<input name="easypayStoreID" type="text" class="form-control">
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="tab-pane p-2 fade" id="store-saving-ctnt">
							<div class="form-row mt-3">
								<div class="col-sm-12">
									<div class="form-group col mb-2">
										<label class="form-label d-block">${title_savepermission}</label>
										<label class="switcher mb-2">
											<input type="radio" class="switcher-input" name="savingType" value="PO">
											<span class="switcher-indicator"></span>
											<span class="switcher-label">${label_savepoint}</span>
										</label>
										<span class="pl-2"></span>
										<label class="switcher mb-2">
											<input type="radio" class="switcher-input" name="savingType" value="CP">
											<span class="switcher-indicator"></span>
											<span class="switcher-label">${label_savecoupon}</span>
										</label>
										<span class="pl-2"></span>
										<label class="switcher mb-2">
											<input type="radio" class="switcher-input" name="savingType" value="NO" checked="checked">
											<span class="switcher-indicator"></span>
											<span class="switcher-label">${label_saveunused}</span>
										</label>
									</div>
								</div>
							</div>
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

var objImgSel = null;
var objImgUnsel = null;
var objTxtSel = null;
var objTxtUnsel = null;

var isFirstRead = true;

$(document).ready(function() {

	$("input[name='view-mode-switch']").prop("checked", "${sessionScope['userCookie'].viewCodeStore}" == "E");
	
	$("input[name='view-mode-switch']").change(function() {
		$("#grid").data("kendoGrid").dataSource.read();
	});
});

function initForm1(subtitle) {
	
	$("#formRoot").html(kendo.template($("#template-1").html()));
	
	$("#form-1 input[name='effectiveStartDate']").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd",
		],
		change: onDatePickerChange,
		value: new Date(),
	});
	
	$("#form-1 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");
	
	$("#form-1").validate({
		rules: {
			shortName: {
				minlength: 2, alphanumeric: true,
			},
			storeName: {
				minlength: 2,
			},
			effectiveStartDate: { date: true },
		}
	});
}


function saveForm1() {

	// kendo datepicker validation
	validateKendoDateValue($("#form-1 input[name='effectiveStartDate']"));

	if ($("#form-1").valid()) {
    	var data = {
    		shortName: $.trim($("#form-1 input[name='shortName']").val()),
    		storeName: $.trim($("#form-1 input[name='storeName']").val()),
    		effectiveStartDate: $("#form-1 input[name='effectiveStartDate']").data("kendoDatePicker").value(),
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
}


function initForm2(subtitle) {
	
	$("#formRoot").html(kendo.template($("#template-2").html()));
	
	$("#form-2 input[name='effectiveStartDate']").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd",
		],
		change: onDatePickerChange,
		value: new Date(),
	});
	
	$("#form-2 input[name='effectiveEndDate']").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd",
		],
		change: onKendoPickerChange,
	});

	$("#form-2 textarea[name='memo']").keypress(function (e) {
		if (e.keyCode != 13) {
			return;
		}
		
		$(this).text().replace(/\n/g, "");
		
		return false;
	});

	
	$("#smilepay").on('show.bs.collapse', function(){
		$("#form-2 span[name='smilepay-show']").hide();
		$("#form-2 span[name='smilepay-hide']").show();
	});
	
	$("#smilepay").on('hide.bs.collapse', function(){
		$("#form-2 span[name='smilepay-show']").show();
		$("#form-2 span[name='smilepay-hide']").hide();
	});
	
	$("#easypay").on('show.bs.collapse', function(){
		$("#form-2 span[name='easypay-show']").hide();
		$("#form-2 span[name='easypay-hide']").show();
	});
	
	$("#easypay").on('hide.bs.collapse', function(){
		$("#form-2 span[name='easypay-show']").show();
		$("#form-2 span[name='easypay-hide']").hide();
	});
	
	$("#form-2 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");
	
	$("#form-2").validate({
		rules: {
			shortName: {
				minlength: 2, alphanumeric: true,
			},
			storeName: {
				minlength: 2,
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
	
	$("#form-2 input[name='shortName']").val(dataItem.shortName);
	$("#form-2 input[name='storeName']").val(dataItem.storeName);
	
	// tab: basic
	$("#form-2 input[name='effectiveStartDate']").data("kendoDatePicker").value(dataItem.effectiveStartDate);
	$("#form-2 input[name='effectiveEndDate']").data("kendoDatePicker").value(dataItem.effectiveEndDate);

	$("#form-2 input[name='storeKey']").val(dataItem.storeKey);
	$("#form-2 input[name='orderUrl']").val(getOrderUrl(dataItem.storeKey));
	
	$("#form-2 input[name='kioskOrderAllowed']").prop("checked", dataItem.kioskOrderAllowed);
	$("#form-2 input[name='mobileOrderAllowed']").prop("checked", dataItem.mobileOrderAllowed);
	$("#form-2 input[name='kitchenPadAllowed']").prop("checked", dataItem.kitchenPadAllowed);
	$("#form-2 input[name='alimTalkAllowed']").prop("checked", dataItem.alimTalkAllowed);
	
	$("#form-2 textarea[name='memo']").val(dataItem.memo);
	
	// tab: pay
	$("#form-2 input[name='smilepayStoreID']").val(dataItem.storeEtc.spStoreKey);
	$("#form-2 input[name='smilepayAuthKey']").val(dataItem.storeEtc.spAuthKey);
	$("#form-2 input[name='smilepayCancelCode']").val(dataItem.storeEtc.spCancelCode);
	
	$("#form-2 input[name='easypayStoreID']").val(dataItem.storeEtc.epStoreKey);
	
	$("#form-2 input[name='mobilePayment']").val(dataItem.storeEtc.storePayGubun);
	selectRadioBtn(dataItem.storeEtc.storePayGubun);
	
	$("#form-2 input[name='mobilePayment']").val(dataItem.storeEtc.storePayGubun);
	
	$("#form-2 input[name='savingType']").each(function(){
		if($(this).val() == dataItem.storeEtc.savingType){
			$(this).prop("checked", true);
		}
	})
	
	$("#form-2 input[name='paymentType']").each(function(){
		if($(this).val() == dataItem.storeEtc.paymentType){
			$(this).prop("checked", true);
		}
	})
	
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
    		shortName: $.trim($("#form-2 input[name='shortName']").val()),
    		storeName: $.trim($("#form-2 input[name='storeName']").val()),
    		effectiveStartDate: $("#form-2 input[name='effectiveStartDate']").data("kendoDatePicker").value(),
    		effectiveEndDate: $("#form-2 input[name='effectiveEndDate']").data("kendoDatePicker").value(),
			storeKey: $.trim($("#form-2 input[name='storeKey']").val()),
    		memo: $.trim($("#form-2 textarea[name='memo']").val()),
    		
    		kioskOrderAllowed: $("#form-2 input[name='kioskOrderAllowed']").is(":checked") ? "Y" : "N",
    		mobileOrderAllowed: $("#form-2 input[name='mobileOrderAllowed']").is(":checked") ? "Y" : "N",
    		kitchenPadAllowed: $("#form-2 input[name='kitchenPadAllowed']").is(":checked") ? "Y" : "N",
    		alimTalkAllowed: $("#form-2 input[name='alimTalkAllowed']").is(":checked") ? "Y" : "N",

			spStoreKey: $.trim($("#form-2 input[name='smilepayStoreID']").val()),
			spAuthKey: $.trim($("#form-2 input[name='smilepayAuthKey']").val()),
			spCancelCode: $.trim($("#form-2 input[name='smilepayCancelCode']").val()),
			epStoreKey: $.trim($("#form-2 input[name='easypayStoreID']").val()),
			storePayGubun: $.trim($("#form-2 input[name='mobilePayment']").val()),
			savingType: $.trim($("#form-2 input[name='savingType']:checked").val()),
			paymentType: $.trim($("#form-2 input[name='paymentType']:checked").val())
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


function changeKey() {
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${changeKeyUrl}",
		success: function (data, status) {
			$("#form-2 input[name='storeKey']").val(data);
			$("#form-2 input[name='orderUrl']").val(getOrderUrl(data));
		},
		error: function(e) {
			showReadErrorMsg();
		}
	});
}


function onDatePickerChange(e) {
	
	var value = e.sender.value();
	
	if (value == null) {
		e.sender.value(kendo.toString(new Date(), 'yyyy-MM-dd'));
	}
}


function getOrderUrl(key) {

	return "${orderURL}".replace("{0}", key);
}


function copyUrl() {
	
	var urlInput = $("#form-2 input[name='orderUrl']");
	
	if ($.trim(urlInput.val()) == "") {
		return false;
	}
	
	urlInput.select();

	try {
		if(document.execCommand('copy')){
			showAlertModal("success", '${msg_urlcopied}');
		} else {
			showAlertModal("danger", '${msg_notSupportedBrowser}');
		}
	} catch (err) {
		showAlertModal("danger", '${msg_notSupportedBrowser}');
	}
}


function visitStore(id, code) {

	var uri = "";
	if (code == "info") {
		uri = "/pay/storeinfo";
	} else if (code == "device") {
		uri = "/pay/storedevice";
	} else if (code == "user") {
		uri = "/pay/storeuser";
	} else if (code == "setting") {
		uri = "/pay/storesetting";
	}
	
	location.href = "/changestore?storeId=" + id + "&uri=" + uri;
}

function selectRadioBtn(type) {
	var $objMsSel = $("#store-pay-ctnt span[name='msSelected']");
	var $objMsUnSel = $("#store-pay-ctnt span[name='msUnselected']");
	var $objMeSel = $("#store-pay-ctnt span[name='meSelected']");
	var $objMeUnSel = $("#store-pay-ctnt span[name='meUnselected']");
	
	// 스마일페이 선택
	if(type == "MS"){
		$objMsSel.show();
		$objMsUnSel.hide();
		$objMeSel.hide();
		$objMeUnSel.show();
		
		$("#store-pay-ctnt a[name='msBtn']").removeClass("btn-outline-primary").addClass("btn-primary");
		$("#store-pay-ctnt a[name='meBtn']").removeClass("btn-primary").addClass("btn-outline-primary");
		
	// easypay 선택
	}else if(type == "ME"){
		$objMsSel.hide();
		$objMsUnSel.show();
		$objMeSel.show();
		$objMeUnSel.hide();
		
		$("#store-pay-ctnt a[name='msBtn']").removeClass("btn-primary").addClass("btn-outline-primary");
		$("#store-pay-ctnt a[name='meBtn']").removeClass("btn-outline-primary").addClass("btn-primary");
	}
	
	$("#store-pay-ctnt input[name='mobilePayment']").val(type);
}

</script>

<!--  / Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
