<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/basregion/create" var="createUrl" />
<c:url value="/pay/basregion/read" var="readUrl" />
<c:url value="/pay/basregion/destroy" var="destroyUrl" />
<c:url value="/pay/basregion/defaultvalue" var="defaultValueUrl" />

<c:url value="/pay/common/readRegions" var="readCmnRegionUrl" />
<c:url value="/pay/common/readCountryCodes" var="readCmnCountryCodeUrl" />


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold pt-1 pb-3 mb-3">
	<span class="mr-1 ${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->


<!-- Kendo grid  -->

<div class="mb-4">
<kendo:grid name="grid" pageable="true" filterable="true" sortable="true" scrollable="false" reorderable="true" resizable="true" selectable="${value_gridSelectable}" >
	<kendo:grid-excel fileName="${pageTitle}.xlsx" allPages="true" proxyURL="/proxySave"/>
	<kendo:grid-pageable refresh="true" buttonCount="5" pageSize="10" pageSizes="${pageSizesNormal}" />
    <kendo:grid-toolbarTemplate>
    	<div class="clearfix">
    		<div class="float-left">
    			<button id="add-btn" type="button" class="btn btn-outline-success">${cmd_add}</button>
    			<button type="button" class="btn btn-default d-none d-sm-inline k-grid-excel">${cmd_excel}</button>
    		</div>
    		<div class="float-right">
    			<button id="default-btn" type="button" class="btn btn-default d-none d-sm-inline">${cmd_setDefaultValue}</button>
    			<button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
    		</div>
    	</div>
   	</kendo:grid-toolbarTemplate>
	<kendo:grid-filterable extra="false" />
	<kendo:grid-columns>
		<kendo:grid-column title="${title_regionName}" field="region.regionName" />
		<kendo:grid-column title="${title_regionCode}" field="region.regionCode" />
		<kendo:grid-column title="${title_countryCode}" field="region.countryCode" minScreenWidth="700" />
		<kendo:grid-column title="${title_defaultValue}" field="defaultValue"
				template="#=defaultValue == 'Y' ? \"<span class='fas fa-check'>\" : \"\"#"/>
	</kendo:grid-columns>
	<kendo:grid-filterMenuInit>
		<script>
			function grid_filterMenuInit(e) {
				if (e.field == "region.countryCode") {
					e.container.find("div.k-filter-help-text").text("${grid_customFilterInfoSelector}");
					e.container.find("span.k-dropdown:first").css("display", "none");
	    				
					e.container.find(".k-textbox:first")
						.removeClass("k-textbox")
						.kendoDropDownList({
							dataSource: {
								transport: {
									read: {
										dataType: "json",
										url: "${readCmnCountryCodeUrl}",
										type: "POST",
										contentType: "application/json"
									},
									parameterMap: function (options) {
										return JSON.stringify({ });
									},
								},
								error: function(e) {
									showReadErrorMsg();
								}
							},
							optionLabel: {
								text: "${grid_selectValue}", value: "",
							},
							dataTextField: "text",
							dataValueField: "value",
						});
				}
			}
		</script>
	</kendo:grid-filterMenuInit>
	<kendo:dataSource serverPaging="true" serverSorting="true" serverFiltering="true" serverGrouping="true" error="kendoReadError">
		<kendo:dataSource-sort>
			<kendo:dataSource-sortItem field="region.countryCode" dir="asc"/>
			<kendo:dataSource-sortItem field="region.regionName" dir="asc"/>
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
		<kendo:dataSource-schema data="data" total="total" groups="data">
			<kendo:dataSource-schema-model id="id">
			</kendo:dataSource-schema-model>
		</kendo:dataSource-schema>
	</kendo:dataSource>
</kendo:grid>
</div>

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
	
	// Default value
	$("#default-btn").click(function(e) {
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
				url: "${defaultValueUrl}",
				data: JSON.stringify({ items: opRows }),
				success: function (data, status, xhr) {
					showOperationSuccessMsg();
					grid.dataSource.read();
				},
				error: ajaxOperationError
			});
		}
	});
	// / Default value
	
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
			<div class="modal-body modal-bg-color">
				<div id="accordion">
					<div class="card mb-2">
						<div class="card-header">
							<a id="acc-tab-1" class="d-flex justify-content-between text-dark" data-toggle="collapse" href="\\\#country">
								${label_destCountry}
								<div class="collapse-icon"></div>
							</a>
						</div>
						<div id="country" class="collapse" data-parent="\\\#accordion">
							<div class="card-body">
								<select name="countryCode" class="selectpicker" data-style="btn-default" data-none-selected-text="">
<c:forEach var="item" items="${CountryCodes}">
									<option value="${item.value}">${item.text}</option>
</c:forEach>
								</select>
							</div>
						</div>
					</div>

					<div class="card mb-2">
						<div class="card-header">
							<a id="acc-tab-2" class="collapsed d-flex justify-content-between text-dark" data-toggle="collapse" href="\\\#region">
								${label_destRegion}
								<div class="collapse-icon"></div>
							</a>
						</div>
						<div id="region" class="collapse" data-parent="\\\#accordion">
							<div class="card-body">
								<select name="regions" class="form-control border-none"></select>
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

function initForm1(subtitle) {
	
	$("#formRoot").html(kendo.template($("#template-1").html()));
	
	$("#form-1 select[name='countryCode']").selectpicker('render');

	bootstrapSelectVal($("#form-1 select[name='countryCode']"), "${value_defaultCountryCode}");

    $("#form-1 select[name='regions']").kendoMultiSelect({
        dataTextField: "regionName",
        dataValueField: "id",
        tagTemplate: "<span class='fas fa-map-marker-alt text-gray'></span>" + 
        			 "<span class='pl-2'>#:data.regionName#</span>",
        itemTemplate: "<span class='fas fa-map-marker-alt text-gray'></span>" +
        		      "<span class='pl-2'>#:data.regionName#</span>",
        dataSource: {
        	serverFiltering: true,
            transport: {
                read: {
                    dataType: "json",
                    url: "${readCmnRegionUrl}",
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify({}),
                },
                parameterMap: function (options) {
            		return JSON.stringify(options);	
                }
            },
			error: kendoReadError
        },
        height: 300,
        delay: 500,
        minLength: 1,
        filter: "contains",
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
	
	var countryCode = $("#form-1 select[name='countryCode']").val();
	var regionIds = $("#form-1 select[name='regions']").data("kendoMultiSelect").value();

	var destType = "";
	var goAhead = false;

	if (tab1 == "true" && countryCode) {
		goAhead = true;
		destType = "C";
	} else if (tab2 == "true" && regionIds.length > 0) {
		goAhead = true;
		destType = "R";
	}
	
	if (goAhead) {
		showWaitModal();
		
    	var data = {
			destType: destType,
			countryCode: countryCode,
			regionIds: regionIds,
    	};
    	
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: $("#form-1").attr("url"),
			data: JSON.stringify(data),
			success: function (data, status, xhr) {
				hideWaitModal();
				showAlertModal("success", JSON.parse(xhr.responseText));
				$("#form-modal-1").modal("hide");
				$("#grid").data("kendoGrid").dataSource.read();
			},
			error: ajaxSaveError
		});
	}
}

</script>

<!--  / Scripts -->


<!--  Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
