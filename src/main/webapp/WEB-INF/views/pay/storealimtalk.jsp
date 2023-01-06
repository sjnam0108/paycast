<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/storealimtalk/read" var="readUrl" />


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold py-3 mb-3">
	<span class="mr-1 fas fa-${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">

<div class="card mb-0">
	<div class="card-body p-2">
    	<div class="clearfix">
   			<button type="button" class="btn btn-default d-none d-sm-inline ml-2" onclick="execlClick();">${title_execl}</button>
   			<span class="d-none d-sm-inline">
				<label class="custom-control custom-radio switcher ml-2">
					<input name="dayMonth" type="radio" class="custom-control-input" value="D" checked="checked">
					<span class="custom-control-label"><b>${title_dayRadio}</b></span>
				</label>
				<label class="custom-control custom-radio switcher ml-2">
					<input name="dayMonth" type="radio" class="custom-control-input" value="M">
					<span class="custom-control-label"><b>${title_monthRadio}</b></span>
				</label>
				<label class="switcher dateInput">
					<input name="fromDay" type="text" class="form-control required" />	
				</label>
				<label class="switcher dateInput">
					<input name="toDay" type="text" class="form-control required" />
				</label>
				<label class="switcher dateInput divHidden">
					<input name="fromMonth" type="text" class="form-control required" />	
				</label>
				<label class="switcher dateInput divHidden">
					<input name="toMonth" type="text" class="form-control required" />
				</label>
			</span>
			<button id="filter" type="button" class="btn btn-outline-default k-grid-add" style="color: #4c84ff;">${title_refer}</button>	
    	</div>
	</div>
</div>

<div class="mb-4">
	<div id="grid"></div>
</div>

<!-- / Kendo grid  -->
<style>
.divHidden { display: none; }
</style>

<script>

function dayMonthControl($dayMonthThis){
	$dayMonthThis.parent().parent().find(".dateInput").each(function(){
		if($(this).hasClass('divHidden')){
			$(this).removeClass('divHidden');
		}else{
			$(this).addClass('divHidden');
		}
	})
}

function initDate() {
	$('input[name=dayMonth]').change(function(){
		dayMonthControl($(this));
	});
	
	$("input[name=fromDay]").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd", "yyyyMMdd"
		],
		max: new Date(),
		value: "${fromDay}"
	});

	$("input[name=toDay]").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd", "yyyyMMdd"
		],
		max: new Date(),
		value: "${toDay}"
	});
	
	$("input[name=fromMonth]").kendoDatePicker({
		start: "year",
		depth: "year",
		format: "yyyy-MM",
		parseFormats: [
			"yyyy-MM", "yyyyMM"
		],
		max: new Date(),
		value: "${fromDay}"
	});

	$("input[name=toMonth]").kendoDatePicker({
		start: "year",
		depth: "year",
		format: "yyyy-MM",
		parseFormats: [
			"yyyy-MM", "yyyyMM"
		],
		max: new Date(),
		value: "${toDay}"
	});
}

$(document).ready(function() {
	initDate();
	
	var dataSour = new kendo.data.DataSource({
		transport: {
			read: {
				type:"POST",
				contentType:"application/json",
				dataType: "json",
				url: "${readUrl}"
			},
			parameterMap: function (options) {
				
				var dayMonth = $('input[name=dayMonth]:checked').val();
				var fromDay = $("input[name=fromDay]").val();
				var toDay = $("input[name=toDay]").val();
				var fromMonth = $("input[name=fromMonth]").val();
				var toMonth = $("input[name=toMonth]").val();
				
				if(dayMonth == "D"){
					if(fromDay == ""){
						fromDay = '${fromDay}';
					}
					if(toDay == ""){
						toDay = '${toDay}';
					}
				}else if(dayMonth == "M"){
					if(fromMonth == ""){
						fromMonth = '${fromDay}';
					}
					if(toMonth == ""){
						toMonth = '${toDay}';
					}
					
					fromDay = fromMonth;
					toDay = toMonth;
				}else{
					fromDay = '${fromDay}';
					toDay = '${toDay}';
				}
				
				return JSON.stringify({skip: 0, page: 1, sort: [{field: "created", dir: "desc"}], reqStrValue1: dayMonth, reqStrValue2: fromDay, reqStrValue3 : toDay});
			}
		},
        schema: {
        	data: "data",
        	total: "total"
        },
		error: ajaxReadError,
		pageSize: 100000
	});
	
	var listDate = "";
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
					$(this).parent().parent().find("td").attr("style","border-top: 1px solid black;");
				})
			}
		},
		height: 550,
		scrollable: true,
		filterable: { extra: false },
		sortable: false,
		pageable: false,
		resizable: true,
		columns: [{
				title: "${title_day}",
				field: "date",
				width: 70,
				filterable: false,
				template: function(dataItem){
					var date = dataItem.date;
					if(listDate === date){
						date = "";
					}else if(date == null){
						date = "<div class='gridTdChg'></div>";
					}else{
						listDate = date;
						date += "<div class='gridTdChg'></div>";
					}
					return date;
				}
			},{
				title: "${title_storeid}",
				width: 100,
				field: "shortName",
	            filterable: {
	                multi: true,
	                search: true
	            }
			}, {
				title: "${title_storenm}",
				field: "name",
				width: 150,
	            filterable: {
	                multi: true,
	                search: true
	            }
			}, {
				title: "${title_alim}${title_addtext}",
				field: "alimCnt",
				width: 100,
				filterable: false
			}, {
				title: "${title_sms}${title_addtext}",
				field: "smsCnt",
				width: 100,
				filterable: false
			}, {
				title: "${title_total}${title_addtext}",
				field: "totalCnt",
				width: 100,
				filterable: false
			}
		]
	});
	
	$("#filter").on("click", function () {
		listDate = "";
		var grid = $("#grid").data("kendoGrid");
		grid.dataSource.read();
	});
	
});	

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
