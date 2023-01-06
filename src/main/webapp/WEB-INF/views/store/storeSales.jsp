<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/store/sales/read" var="readUrl" />
<c:url value="/store/sales/cancel" var="cancelUrl" />
<c:url value="/store/sales/cancelKiosk" var="cancelKioskUrl" />
<c:url value="/store/sales/immediateCanCel" var="immediateCanCel" />

<c:url value="/store/storeInfo/readGroup" var="readGroupUrl" />


<!-- Opening tags -->

<common:pageOpening />

<script src="/resources/shared/js/jquery.form.js"></script>
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

<div class="card " >
	<table class="table mb-0">
		<tbody>
			<tr>
				<c:if test="${Mobile eq 'N'}">
					<th style="width: 49px;"><button type="button" class="btn btn-default d-none d-sm-inline" onclick="execlClick();">엑셀</button></th>
				</c:if>
				<th style="width: 49px; padding-top: 19px;">${title_termdate}</th>
				<td><input id="from" name="from" type="text" class="form-control required" /></td>
				<td><button id="filter" type="button" class="btn btn-outline-default k-grid-add" style="color: #4c84ff;">조회</button></td>
			</tr>
		</tbody>
	</table>

</div>
<div class="mb-4">
	<div id="grid"></div>
</div>

<!-- / Kendo grid  -->

<!-- / Modal  -->
<div class="modal fade" data-backdrop="static" id="cancelSales">
	<div class="modal-dialog">
		<form class="modal-content" id="cancelSalesForm" method="post" enctype="multipart/form-data" onsubmit="return false;">
			<input type="hidden" name="checkId" />
			<input type="hidden" name="storeId" />
			<div class="modal-header move-cursor">
				<h5 class="modal-title">${title_cancel}</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-row">
			    	<div class="col-sm">
					    <div class="form-group col" >
							<label class="form-label">${title_cancelPw}</label>
							<input type="password" class="form-control required" id="cancelPw" name="cancelPw" placeholder="${title_cancelPw}" maxlength="20" />
							<input type="hidden" class="custom-control-input" name="partialCode"  value="0"/>
							<input type="hidden" class="form-control required" id="cancelAmt" name="cancelAmt" maxlength="12" />
						</div>
					</div>
				</div>
				<div class="form-row">
					<div class="col-sm">
						<div id="amtDiv" class="form-group col" style="display: none;">
							<h3 id="cancelAuth"></h3>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer" id="footer1">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" id="save-btn">${form_save}</button>
			</div>
			<div class="modal-footer" style="display: none;" id="footer2">
				<button type="button" class="btn btn-default" data-dismiss="modal" onclick="fnClickRe()">확인</button>
			</div>
		</form>
	</div>
</div>


<div class="modal fade" data-backdrop="static" id="salesKioskCancel">
	<div class="modal-dialog">
		<form class="modal-content" id="cancelKioskSalesForm" method="post" enctype="multipart/form-data" onsubmit="return false;">
			<input type="hidden" name="checkId" />
			<input type="hidden" name="storeId" />
			<div class="modal-header move-cursor">
				<h5 class="modal-title">${title_cancel}</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-row">
					<div class="col-sm">
						<div class="form-group col">
							<h3 id="cancelKioskAuth"></h3>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal" onclick="fnClickRe()">확인</button>
			</div>
		</form>
	</div>
</div>
<div class="modal fade" data-backdrop="static" id="telAndAddress">
	<div class="modal-dialog">
		<form class="modal-content" id="telAndAddressForm" method="post" enctype="multipart/form-data" onsubmit="return false;">
			<div class="modal-header move-cursor">
				<h5 class="modal-title">전화번호 및 주소</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-row">
					<div class="col-sm">
						<div class="form-group col">
							<h3 name="telView"></h3>
							<h3 name="addr"></h3>
							<h3 name="storeMsg"></h3>
							<h3 name="deliMsg"></h3>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<!-- / Modal  -->

<!-- Grid button actions  -->

<script>
var $homeListThis = null;
$(document).ready(function() {

	var start = $("#from").kendoDatePicker({
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
				var storeId = $("#store").val();
				return JSON.stringify({skip: 0, page: 1, sort: [{field: "created", dir: "desc"}], from: from, reqStrValue1: storeId});
			}
		},
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
				title: "${title_date}",
				field: "date",
				width: 85,
				template: function(dataItem){
					var date = dataItem.date;
					var boardYn = dataItem.boardYn;
					if(boardYn == "Y"){
						date = "<div class='gridTdChg'></div>";
					}
					return date;
				}
			},{
				title: "${title_packing}",
				width: 55,
				field: "packing",
				filterable: false,
				template: function(dataItem){
					var fnname = "";
					var packing = dataItem.packing;
					if("packing" == packing){
						fnname = '<i class="fas fa-box d-block" style="float: left; margin-top: 5%;"></i>';
					}else if("delivery" == packing){
						fnname = '<i class="fas fa-shipping-fast d-block" style="float: left; margin-top: 5%;"></i>';
					}else{
						fnname = '';
					}
					if("" != dataItem.telNumber || "" != dataItem.storeMsg){
						fnname += '&nbsp;&nbsp;<button type="button" class="btn icon-btn btn-xs btn-outline-info addressInfo"><span class="far fa-paper-plane"></span>';
						fnname += '<input type="hidden" name="addrYn" value="Y" />';
						fnname += '<input type="hidden" name="telNumber" value="'+dataItem.telNumber+'" />';
						fnname += '<input type="hidden" name="roadAddr" value="'+dataItem.roadAddr+'" />';
						fnname += '<input type="hidden" name="addrDetail" value="'+dataItem.addrDetail+'" />';
						fnname += '<input type="hidden" name="storeMsg" value="'+dataItem.storeMsg+'" />';
						fnname += '<input type="hidden" name="deliMsg" value="'+dataItem.deliMsg+'" />';
						fnname += '</button>';
					}
					return fnname;
				}
			},{
				title: "${title_menu}",
				width: 150,
				field: "menu",
				template: function(dataItem){
					var menu = dataItem.menu;
					return menu;
				}
			}, {
				title: "${title_amount}",
				width: 60,
				field: "amount",
				filterable: false
			}, {
				title: "${title_amt}",
				width: 100,
				field: "amt",
				filterable: false
			}, {
				title: "${title_totalamt}",
				width: 100,
				field: "totalamt",
				filterable: false
			}, {
				title: "${title_fnname}",
				width: 100,
				field: "fnname",
				filterable: false,
				template: function(dataItem){
					var fnname = dataItem.fnname;
					return fnname;
				}
			}, {
				title: "${title_orderDevice}",
				width: 100,
				field: "orderDevice",
				filterable: false
			}, {
				title: "${title_ordernum}",
				width: 100,
				field: "ordernum",
				filterable: false,
				template: function(dataItem){
					return dataItem.orderNumButton;
				}
			}
		],
		error: ajaxReadError
	});
	
	$("#filter").on("click", function () {
		alert("테스트")
		var grid = $("#grid").data("kendoGrid");
		grid.dataSource.read();
	});
	
	$("#save-btn").click(function(e) {
		if ($("#cancelSalesForm").valid()) {
			$('#cancelSalesForm').ajaxForm({
				type: "POST",
		    	url : "${cancelUrl}",
		        success: function(data) {
		        	showOperationSuccessMsg();
		        	$("#cancelAuth").html("취소 코드 : [" + data + "]");
		        	$("#amtDiv").show();	
		        	$("#footer1").hide();	
		        	$("#footer2").show();	
		        	var checkId = $homeListThis.attr( 'checkId' );
		        	$homeListThis.parent().html('<button id="delete-btn" checkid="'+checkId+'" type="button" class="btn btn-sm btn-danger salesCancel">주문취소</button><br>'+data);
		        },
		        error: ajaxOperationError
		    }).submit();
		};
	});
	
	$(document).on("click",".salesCancel",function(){
		salesKioskCancel($( this ));
	} );
	
	$(document).on("click",".salesKioskCancel",function(){
		salesKioskCancel($( this ));
	} );
	
	$(document).on("click",".addressInfo",function(){
		telAndAddress($( this ));
	} );
	
	$('.popup_area4 .close').on('click', function(e){
		e.preventDefault();
		
		$('.dim').remove();
		$(this).parent().hide();
		$('body').removeClass('fix');
	});
	
	
	$(document).on("click",".immediateCanCel",function(){
		var checkId = $(this).attr( 'checkId' );
		
		bootbox.confirm({
			size: "small",
			title: "${title_immediateCanCel}",
			message: "${mag_immediateCanCelMsg}",
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
							checkId : checkId
						};
					$.ajax({
						type: "POST",
						contentType: "application/json",
						dataType: "json",
						url: "${immediateCanCel}",
						data: JSON.stringify(data),
						success: function (data, status, xhr) {
							showSaveSuccessMsg();
							fnClickRe();
						},
						error: ajaxSaveError
					});
				}
			}
		});
	} );
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

function salesCancel($home){
	$homeListThis = null;
	var checkId = $home.attr( 'checkId' );
	$homeListThis = $home;
	
	$("#cancelSales").modal('show');
	$("#cancelSales").draggable({handle: ".modal-header"});
	$("#amtDiv").hide();
	$("#footer1").show();
	$("#footer2").hide();	
	$("#cancelSalesForm input[name=checkId]").val(checkId);
	$("#cancelSalesForm input[name=storeId]").val($("#store").val());
	$("#cancelSalesForm input[name=cancelPw]").val("");
	$("#cancelSalesForm").validate({
		rules: {
			cancelPw: {
				required: true, maxlength: 20
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

function salesKioskCancel($home){
	$homeListThis = null;
	var checkId = $home.attr( 'checkId' );
	$homeListThis = $home;
	
	$("#salesKioskCancel").modal('show');
	$("#salesKioskCancel").draggable({handle: ".modal-header"});
	
	$("#cancelKioskSalesForm input[name=checkId]").val(checkId);
	$("#cancelKioskSalesForm input[name=storeId]").val($("#store").val());
	
	$('#cancelKioskSalesForm').ajaxForm({
		type: "POST",
    	url : "${cancelKioskUrl}",
        success: function(data) {
        	$("#cancelKioskAuth").html("취소 코드 : [" + data + "]");
        	var checkId = $homeListThis.attr( 'checkId' );
        	$homeListThis.parent().html('<button id="delete-btn" checkid="'+checkId+'" type="button" class="btn btn-sm btn-danger salesCancel">주문취소</button><br>'+data);
        	showOperationSuccessMsg();
        },
        error: ajaxOperationError
    }).submit();
}

function telAndAddress($home){
	$("#telAndAddress").modal('show');
	$("#telAndAddress").draggable({handle: ".modal-header"});
	var telNumber = $home.find('input[name=telNumber]').val();
	var addrYn = $home.find('input[name=addrYn]').val();
	$("#telAndAddress h3[name=telView]").html("전화번호 : "+telNumber);
	if("Y" === addrYn){
		var roadAddr = $home.find('input[name=roadAddr]').val();
		var addrDetail = $home.find('input[name=addrDetail]').val();
		var storeMsg = $home.find('input[name=storeMsg]').val();
		var deliMsg = $home.find('input[name=deliMsg]').val();
		
		$("#telAndAddress h3[name=addr]").html("주소 : "+roadAddr + addrDetail);
		$("#telAndAddress h3[name=storeMsg]").html("매장요구사항 : "+storeMsg);
		$("#telAndAddress h3[name=deliMsg]").html("배송요구사항 : "+deliMsg);
	}else{
		$("#telAndAddress h3[name=addr]").html("주소 : ");
		$("#telAndAddress h3[name=storeMsg]").html("매장요구사항 : ");
		$("#telAndAddress h3[name=deliMsg]").html("배송요구사항 : ");
	}
}

function fnClickRe(){
	var grid = $("#grid").data("kendoGrid");
	grid.dataSource.read();	
}
</script>


<func:cmmValidate />

<common:base />
<common:pageClosing />
