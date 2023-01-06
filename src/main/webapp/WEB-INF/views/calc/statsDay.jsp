<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/calc/statsDay/read" var="readUrl" />
<c:url value="/store/storeInfo/readGroup" var="readGroupUrl" />


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold py-3 mb-3">
	<span class="mr-1 fas fa-${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
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

<!-- Kendo grid  -->
<div class="card mb-0">
	<table class="table mb-0">
		<tbody>
			<tr>
				<c:if test="${Mobile eq 'N'}">
					<th style="width: 49px;"><button type="button" class="btn btn-default d-none d-sm-inline" onclick="execlClick();">엑셀</button></th>
				</c:if>
				<th style="width: 49px; padding-top: 19px;">${title_termdate}</th>
				<td><input id="from" name="from" type="text" class="form-control required" /></td>
				<td><input id="to" name="to" type="text" class="form-control" /></td>
				<td><button id="filter" type="button" class="btn btn-outline-default k-grid-add" style="color: #4c84ff;">조회</button></td>
			</tr>
		</tbody>
	</table>
</div>

<div class="mb-4">
	<div id="grid"></div>
</div>

<!-- / Kendo grid  -->


<!-- Grid button actions  -->

<script>

$(document).ready(function() {

	var start = $("#from").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd", "yyyyMMdd"
		],
		max: new Date(),
		value: "${fromDay}"
	});

	var end = $("#to").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd", "yyyyMMdd"
		],
		max: new Date(),
		value: "${toDay}"
	});
	
	var dataSour = new kendo.data.DataSource({
		transport: {
			read: {
				type:"POST",
				contentType:"application/json",
				dataType: "json",
				url: "${readUrl}"
			},
			parameterMap: function (options) {
				var from = $("#from").val();
				if(from==""){
					from = '${fromDay}';
				}
				var to = $("#to").val();
				if(to==""){
					to = '${toDay}';
				}
				
				var storeId = $("#store").val();
				return JSON.stringify({skip: 0, page: 1, sort: [{field: "created", dir: "desc"}], from: from, to : to, reqStrValue1: storeId});
			}
		},
        schema: {
			errors: function(response) {
				return response.error;
			}
		},
		error: ajaxReadError,
		pageSize: 100000
	});
	
	$("#grid").kendoGrid({
		excelExport: function(e) {
			  e.workbook.fileName = "${pageTitle}.xlsx";
		 },
		dataSource: dataSour,
		dataBound: function(e) {
			var grid = $("#grid").data("kendoGrid");
			var gridData = grid.dataSource.view();
			for(var i = 0; i < gridData.length; i++) {
				grid.tbody.find("tr td div.gridTdChg").each(function(){
					$(this).parent().parent().find("td").attr("style","border-bottom: 1px solid black;");
				})
			}
		},
		height: 550,
		scrollable: true,
		filterable: false,
		sortable: false,
		pageable: false,
		columns: [{
				title: "${title_day}",
				field: "statsDay",
				width: 120,
				template: function(dataItem){
					var date = dataItem.statsDay;
					var boardYn = dataItem.boardYn;
					if(boardYn == "Y"){
						date = "<div class='gridTdChg'></div>";
					}
					return date;
				}
			},{
				title: "${title_menu}",
				width: 180,
				field: "statsMenu",
				template: function(dataItem){
					var statsMenu = dataItem.statsMenu;
					return statsMenu;
				}
			}, {
				title: "${title_amount}",
				field: "statsAmount",
				width: 70,
				filterable: false
			}, {
				title: "${title_amt}",
				field: "statsAmt",
				width: 100,
				filterable: false
			}, {
				title: "${title_salesamt}",
				field: "statsSalesamt",
				width: 100,
				filterable: false
			}
		]
	});
	
	$("#filter").on("click", function () {

		var grid = $("#grid").data("kendoGrid");
		grid.dataSource.read();
	});
	
});	

function storeRead(storeId, storeNm){
	if("" == storeId){
		showAlertModal("danger", "조회 가능한 매장이 없습니다.");
		return;
	}
	
	$("#store").val(storeId);
	$("#storeNm").val(storeNm);
	
	var grid = $("#grid").data("kendoGrid");
	grid.dataSource.read();
}

function execlClick(){
	var grid = $("#grid").data("kendoGrid");
	grid.saveAsExcel();
}

</script>


<!-- / Grid button actions  -->


<!-- Functional tags -->

<func:cmmValidate />

<!-- Closing tags -->

<common:base />
<common:pageClosing />
