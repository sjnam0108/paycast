<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/store/storesite/read" var="readUrl" />
<c:url value="/store/storesite/create" var="createUrl" />
<c:url value="/store/storesite/update" var="updateUrl" />
<c:url value="/store/storesite/destroy" var="destroyUrl" />
<c:url value="/store/storesite/readSites" var="readSiteUrl" />
<c:url value="/store/storesite/readStbGroups" var="readGroupUrl" />

<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold py-3 mb-3">
	<span class="mr-1 fas fa-${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">



<!-- Java(optional)  -->

<%
	String editTemplate = 
			"<button type='button' onclick='edit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>" + 
			"<span class='fas fa-pencil-alt'></span></button>";
	String copyTemplate = 
			"<button type='button' onclick='return false;' class='btn icon-btn btn-sm btn-outline-secondary borderless'>" + 
			"<span class='far fa-copy'></span></button>";
%>

<!-- Page body -->


<!-- Kendo grid  -->

<div class="mb-4">
<kendo:grid name="grid" filterable="true" groupable="false" pageable="true" sortable="true" scrollable="false" reorderable="true" resizable="true" selectable="${value_gridSelectable}">
	<kendo:grid-pageable refresh="true" buttonCount="5" pageSize="10" pageSizes="${pageSizesNormal}" />
	<kendo:grid-toolbarTemplate>
    	<div class="clearfix">
    		<div class="float-left">
    			<button id="add-btn" type="button" class="btn btn-outline-success k-grid-add">${cmd_add}</button>
    		</div>
    		<div class="float-right">
    			<button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
    		</div>
    	</div>
	</kendo:grid-toolbarTemplate>
	<kendo:grid-columns>
		<kendo:grid-column title="${cmd_customEdit}" width="50" filterable="false" sortable="false" template="<%= editTemplate %>" />
		<kendo:grid-column title="${title_name}" field="selfView.storeName" />
		<kendo:grid-column title="${title_stbGroupName}" field="stbGroup.stbGroupName" />
		<kendo:grid-column title="${title_instruction}" field="selfView.storeExplain" filterable="false" sortable="false" minScreenWidth="500" />
	</kendo:grid-columns>
	<kendo:dataSource serverPaging="true" serverSorting="true" serverFiltering="true" serverGrouping="true" serverAggregates="true" error="kendoReadError">
		<kendo:dataSource-sort>
			<kendo:dataSource-sortItem field="id" dir="desc"/>
		</kendo:dataSource-sort>
		<kendo:dataSource-filter>
			<kendo:dataSource-filterItem field="site.id" operator="eq" logic="and" value="${sessionScope['currentSiteId']}">
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
		<kendo:dataSource-schema data="data" total="total" groups="data" aggregates="aggregates">
			<kendo:dataSource-schema-model id="id" />
		</kendo:dataSource-schema>
	</kendo:dataSource>
</kendo:grid>
</div>

<!-- / Kendo grid  -->


<!-- Grid button actions  -->

<script>
$(document).ready(function() {
	$(document).keypress(function(e) { if (e.keyCode == 13) e.preventDefault(); });
	
	
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
			bootbox.confirm({
				size: "small",
				title: "${confirm_title}",
				message: "${msg_delConfirm}".replace("{0}", "<strong>" + delRows.length + "</strong>"),
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
				}
			});
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
	<div class="modal-dialog">
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
			<div class="modal-body">
				<div class="card mb-4" style="border:0px;">
					<div class="card-body" style="padding:0px;">
						<div class="form-row">
							<div class="form-group col-md-6">
								<label class="form-label">
									${title_name}
									<span class="text-danger">*</span>
								</label>
								<input name="storeName" type="text" class="form-control required" />
							</div>
							<div class="form-group col-md-6">
								<label class="form-label">
									${label_group}<span class="text-danger">*</span>
								</label>
								<div name="groups-con">
									<select name="groups" style="width: 100%;" class="form-control"></select>
								</div>
								<label name="groups-feedback" for="groups" class="error invalid-feedback"></label>
							</div>
						</div>
						<div class="form-row">
							<div class="form-group col-md-6">
								<label class="form-label">
									${title_storeMID}<span class="text-danger">*</span>
								</label>
								<input name="storeMID" type="text" class="form-control required" />
							</div>
							<div class="form-group col-md-6">
								<label class="form-label">
									${title_merchantKey}<span class="text-danger">*</span>
								</label>
								<input name="merchantKey" type="text" class="form-control required" />
							</div>
						</div>
						<div class="form-row">
							<div class="form-group col-md-6">
								<div class="form-label">
									${title_instruction}
								</div>
								<input name="storeExplain" type="text" class="form-control required" maxlength="150" />
							</div>
							<div class="form-group col-md-6">
								<label class="form-label">
									${title_qrcode}
								</label>
								<button type='button' onclick='copyUrl();' class='btn icon-btn btn-sm btn-outline-secondary borderless'><span class='far fa-copy'></span></button>
								<input id="qrcopy" type='text' class='form-control required' readonly />
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

<!--  / Forms -->



<!--  Scripts -->

<script>

// Form validator
var validator = null;

// Form error obj
var errors = {};


function validateMSValue(name) {
	var selector = null;
	
	if (name == "groups") {
		selector = $("#form-1 select[name='groups']");
	} else {
		return;
	}
	
	var ids = selector.data("kendoDropDownList").value();
	
	if (ids.length == 0) {
		errors[name] = "${form_selectReq}";
	} else {
		delete errors[name];
	}
}

function checkMSMessage(name) {
	
	var selector = null;
	var container = null;
	var feedback = null;
	
	if (name == "groups") {
		selector = $("#form-1 select[name='groups']");
		container = $("#form-1 div[name='groups-con']");
		feedback = $("#form-1 label[name='groups-feedback']");
	} else {
		return;
	}
	
	var ids = selector.data("kendoDropDownList").value();

	if (ids.length == 0) {
		container.addClass("is-invalid");
		feedback.addClass("small d-block").css("display", "block");
		feedback.html(errors[name]);
	} else {
		container.removeClass("is-invalid");
		feedback.removeClass("small d-block").css("display", "none");
		feedback.html("");
	}
}

function initForm1(subtitle) {
	
	$("#formRoot").html(kendo.template($("#template-1").html()));
	
	    $("#form-1 select[name='groups']").kendoDropDownList({
	        dataTextField: "stbGroupName",
	        dataValueField: "id",
	        tagTemplate: "<span class='fas fa-folder'></span>" + 
	        			 "<span class='pl-2' title='#:data.stbGroupName#'>#:data.stbGroupName#</span>",
	        itemTemplate: "<span class='fas fa-folder'></span>" +
	        		      "<span class='pl-2' title='#:data.stbGroupName#'>#:data.stbGroupName#</span>",
	        dataSource: {
	            transport: {
	                read: {
	                    dataType: "json",
	                    url: "${readGroupUrl}",
	                    type: "POST",
	                    contentType: "application/json"
	                },
	                parameterMap: function (options) {
	            		return JSON.stringify(additional_data());	
	                }
	            },
	            schema: {
	            	data: "data",
	            	total: "total"
	            },
				error:ajaxReadError
	        },
		    change: function(e) {
		    	checkMSMessage("groups");
		    }
	    });

    var multiSelectGroup = $("#form-1 select[name='groups']").data("kendoDropDownList");
	multiSelectGroup.enable(true);
    
	$("#form-1 span[name='subtitle']").text(subtitle ? subtitle : "${form_add}");
	
	$("#form-1").validate({
		rules: {
			storeName: {
				required: true, maxlength: 50
			},
			storeMID: {
				required: true
			},
			merchantKey: {
				required: true
			}
		}
	});
}

function additional_data() {
	var siteId = -1;
	
	<c:if test="${not empty sessionScope['currentSiteId']}">
		siteId = ${sessionScope['currentSiteId']};
	</c:if>
	
	return {
		adminSiteId: siteId,
	};
}

function changeMSMessage(name) {
	
	var selector = null;
	var container = null;
	var feedback = null;
	
	if (name == "groups") {
		selector = $("#form-1 select[name='groups']");
		container = $("#form-1 div[name='groups-con']");
		feedback = $("#form-1 label[name='groups-feedback']");
	} else {
		return;
	}
	
	container.removeClass("is-invalid");
	feedback.removeClass("small d-block").css("display", "none");
}

function saveForm1() {

	errors = {};
	
	var groupIds = "";
	validateMSValue("groups");
	if (Object.keys(errors).length == 0) {
		var storeName = $("#form-1 input[name='storeName']").val();
		if("" == storeName){
			showAlertModal("danger", '${label_notName}');
			return false;
		}
		
		var storeMID = $("#form-1 input[name='storeMID']").val();
		if("" == storeMID){
			showAlertModal("danger", '${label_notStoreId}');
			return false;
		}
		var merchantKey = $("#form-1 input[name='merchantKey']").val();
		if("" == merchantKey){
			showAlertModal("danger", '${label_notStoreKey}');
			return false;
		}
		var storeExplain = $("#form-1 input[name='storeExplain']").val();
		groupIds = $("#form-1 select[name='groups']").data("kendoDropDownList").value();
		if (groupIds.length > 0) {
			var data = {
				id: Number($("#form-1").attr("rowid")),
				storeName : storeName,
				groupIds : groupIds,
				storeMID : storeMID,
				merchantKey : merchantKey,
				storeExplain : storeExplain
			};
        	
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: $("#form-1").attr("url"),
				data: JSON.stringify(data),
				success: function (data, status, xhr) {
					showSaveSuccessMsg();
					$("#form-modal-1").modal("hide");
					$("#grid").data("kendoGrid").dataSource.read();
				},
				error: ajaxSaveError
			});
		}
	} else {
		checkMSMessage("groups");
	}
}

function edit(id) {
	initForm1("${form_edit}");

	var dataItem = $("#grid").data("kendoGrid").dataSource.get(id);
	
	$("#form-1").attr("rowid", dataItem.id);
	$("#form-1").attr("url", "${updateUrl}");
	
	$("#form-1 input[name='storeName']").val(dataItem.selfView.storeName);
	$("#form-1 input[name='storeExplain']").val(dataItem.selfView.storeExplain);
	$("#form-1 select[name='groups']").data("kendoDropDownList").value(dataItem.stbGroup.id);
	$("#form-1 input[name='storeMID']").val(dataItem.selfView.storeMID);
	$("#form-1 input[name='merchantKey']").val(dataItem.selfView.merchantKey);
	
	qrcreateSelf(dataItem.selfView.storeKey);
	
	$('#form-modal-1 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-1").modal();
}

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
		bootbox.confirm({
			size: "small",
			title: "${confirm_title}",
			message: "${msg_delConfirm}".replace("{0}", "<strong>" + delRows.length + "</strong>"),
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
			}
		});
	}
});
// / Delete

function qrcreateSelf(storeKey) {
	var domain = "${domainQr}";
	var idEncoding = encodeURI(storeKey);
	var encodingURL =(domain+"/menu?store="+idEncoding+"&table=");
	$("#qrcopy").val(encodingURL);
}

function copyUrl(){
    var urlbox = document.getElementById("qrcopy");
    if("" == urlbox.value){
    	return false;	
    }
    urlbox.select();
    try {
    	var successful = document.execCommand('copy');
    	if(successful){
    		showAlertModal("success", '${label_copyUrlKey}');
    	}else{
    		showAlertModal("danger", '${label_notCopyUrlKey}');
    	}
    } catch (err) { 
    	showAlertModal("danger", '${label_notSupportBrowser}');
    }
}

</script>

<!--  / Scripts -->
<style>
    .store-fieldlist {
        margin: 0 0 -1em;
        padding: 0;
    }

    .store-fieldlist li {
        list-style: none;
        padding-bottom: 1em;
    }
</style>

<!-- / Page body -->

<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
