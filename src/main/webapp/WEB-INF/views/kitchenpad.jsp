<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/storecook/read" var="readUrl" />
<c:url value="/storecook/alarmUpdate" var="alarmUrl" />
<c:url value="/storecook/stayCntRead" var="stayCntRead" />
<c:url value="/storecook/readCom" var="readComUrl" />
<c:url value="/storecook/comCancelUpdate" var="comCancelUrl" />
<c:url value="/storecook/renuwRead" var="renuwReadUrl" />
<c:url value="/storecook/renuwCancelRead" var="renuwCancelReadUrl" />


<!-- Opening tags -->

<!DOCTYPE html>
<html lang="${html_lang}" class="default-style layout-navbar-fixed">
	<common:head />
	<body>
		<div class="page-loader">
			<div class="bg-primary"></div>
		</div>
        

		<!-- Layout wrapper -->
		<div class="layout-wrapper layout-1 layout-without-sidenav">
		
			<div class="layout-inner">

				<!-- Layout navbar -->
				<nav class="layout-navbar navbar navbar-expand align-items-center container-p-x bg-navbar-black">

					<div><span class="title-display">${pageTitle}</span></div>

					<div class="navbar-nav align-items-md-center ml-auto">
						<img src="/resources/shared/images/logo/logo_app.png" alt/>
					</div>
				</nav>
				<!-- / Layout navbar -->

				<!-- Layout container -->
				<div class="layout-container">

					<!-- Layout content -->
					<div class="layout-content">

						<!-- Content -->
						<div class="container-fluid flex-grow-1 py-3">
						

<style>
.bg-navbar-black {
	height: 50px;
	background-color: #3e4752;
	color: #fefefe;
}
.title-display {
	font-size: 1.25rem;
	font-weight: 300;
	line-height: 1.1;
}
</style>


<!-- Page body -->
<script src="/resources/shared/js/jquery.form.js"></script>

<style>
	.tableIn {
		width: 100%;
		max-width: 100%;
		border : 0px;
		padding: 0;
	}

	.tableIn tr td {
		border : 0px;
	}
	
	.jbBox {
		border: 2px solid #000000;
	}
	
	.jbBoxComp {
		border: 2px solid #000000;
	}
</style>


<c:choose>
	<c:when test="${padEnabled}">
	
	
<c:set var="storeIdValue" value="${storeIdCo}"></c:set>
<c:set var="storeNameValue" value="${storeNameCo}"></c:set>		

<div class="row pt-3">
	<div class="col-md">
		<div class="card text-center mb-3">
			<div class="card-header">
				<ul class="nav nav-pills card-header-pills nav-responsive-md">
					<li class="nav-item">
						<a id="standby" class="nav-link active" onclick="tabActive('standby'); return false;">${title_standby}</a>
					</li>
					<li class="nav-item">
						<a id="complete" class="nav-link" onclick="tabActive('complete'); return false;">${title_complete}</a>
					</li>
				</ul>
				<div id="stayCntDiv" style="float: right; margin-top:-22px;">
					${title_standby} : <span id="stayCnt">0</span>
				</div>
				<div id="completeCntDiv" style="float: right; margin-top:-22px; display: none;">
					${title_complete} : <span id="completeCnt">0</span>
				</div>
			</div>
			<div class="card-body" id="stayTableDIV">
	            <table class="table">
	              <thead>
	                <tr>
	                  <th width="10%">${title_inorder}</th>
	                  <th width="10%">${title_ordernum}</th>
	                  <th width="10%">${title_package}배달</th>
	                  <th width="30%" style="text-align: left;">${title_menu}</th>
	                  <th width="20%">${title_amount}</th>
	                  <th width="10%">${title_notice}</th>
	                  <th width="10%">${title_completeorder}</th>
	                </tr>
	              </thead>
	              <tbody id="sortableGroup">
	              </tbody>
	            </table>
	            <input type="hidden" id="chooseMenu" />
			</div>
			<div class="card-body" id="complteTableDIV" style="display: none;">
	            <table class="table">
	              <thead>
	                <tr>
<%-- 	                  <th width="10%">${title_inorder}</th> --%>
	                  <th width="10%">${title_ordernum}</th>
	                  <th width="10%">${title_package}배달</th>
	                  <th width="30%" style="text-align: left;">${title_menu}</th>
	                  <th width="20%">${title_amount}</th>
	                  <th width="20%">${title_completecancel}</th>
	                </tr>
	              </thead>
	              <tbody id="comList">
	              </tbody>
	            </table>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

$(function() {
	storeRead('${storeIdValue}', '${storeNameValue}');
	
	$(document).on("click",".menuClickTR",function(){
		var $chooseMenu = $("#chooseMenu");
		var checkOrderNum = $(this).attr( 'chooseMenu' );
		if( checkOrderNum != $chooseMenu.val() ){
			$(".tableIn tr" ).each(function(){
				$( this ).removeClass( 'jbBox' );
			});
		}

		$( this ).toggleClass( 'jbBox' );
		$chooseMenu.val(checkOrderNum);
	} );
	
	$(document).on("click",".bellTD",function(){
		alarmDID($( this ));
	} );
	
	$(document).on("click",".complteTD",function(){
		menuComplte($( this ));
	} );
	
	$(document).on("click",".comCancelTD",function(){
		comCancelFn($( this ));
	} );
	
	$(document).on("click",".orderSeqTd",function(){
		menuALLChooseFn($( this ));
	} );
	
	setInterval(defaltTr, 5000);
	
});
function defaltTr(){
	var cnt = $('#stayCnt').text();
	if(cnt == 0){
		if($("#sortableGroup > tr").size() > 0){
			$("#sortableGroup").html("");
		}
	}
}

function menuALLChooseFn($home){
	var checkLoop = false;
	var chooseMenuVal = $("#chooseMenu").val();
	var checkOrderNum = "";
	
	$home.parent().find(".tableIn tr").each(function(){
		checkOrderNum = $( this ).attr( 'chooseMenu' );
	})
	
 	var chgClass = 0;
	$(".tableIn tr" ).each(function(){
		if($( this ).attr( 'chooseMenu' ) == checkOrderNum){
			if(chgClass == 0){
				if($(this).hasClass("jbBox")){
					$( this ).removeClass( 'jbBox' );
					chgClass = 1;
				}else{
					$( this ).addClass( 'jbBox' );
					chgClass = 2;
				}
			}
			if(chgClass == 1){
				$(this).removeClass( 'jbBox' );
			}else if(chgClass == 2){
				$(this).addClass( 'jbBox' );
			}

		}else{
			$( this ).removeClass( 'jbBox' );
		}
	});
	
	$("#chooseMenu").val(checkOrderNum);
}

function storeRead(storeId, storeNm){
	if("" == storeId){
		showAlertModal("danger", "조회 가능한 매장이 없습니다.");
		return;
	}
	
	var data = {
		storeId: storeId
	};
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			$('#sortableGroup').html("");
			
			var formLen = form.length;
			$('#stayCnt').text(formLen);
			if(formLen > 0){
				for(var i = 0; i < form.length; i++) { 
					var fromcookId = form[i].cookId;
					var orderSeq = form[i].orderSeq;
					var orderNumber = form[i].orderNumber;
					var orderMenuListLen = form[i].orderMenuList.length;

					var text = '';
					if((i%2) != 0){
						text += '<tr class="table-primary">';						
					}else{
						text += '<tr>';
					}
					text += '<th scope="row" name="cooknumber" width="10%" style="vertical-align : middle;">'+ (i+1) +'</th>'
					text += '<td width="10%" class="orderSeqTd" style="vertical-align : middle;">'+orderSeq+'</td>'
					text += '<td width="10%" colspan="3">'
					text += '<table class="tableIn" >'
					text += '<tbody>'
					text += '<input type="hidden" name="cookId" value="'+fromcookId+'" />'
					text += '<input type="hidden" name="storeId" value="'+form[i].storeId+'" />'
					text += '<input type="hidden" name="storeOrderId" value="'+form[i].storeOrderId+'" />'
					text += '<input type="hidden" name="orderSeq" value="'+orderSeq+'" />'
					
					if(orderMenuListLen > 0){
						var orderMenuList = form[i].orderMenuList;
						for(var t = 0; t < orderMenuListLen; t++) {
							if(orderMenuList[t].orderMenuNotice != "N"){
								text += '<tr class="menuClickTR jbBoxComp" chooseMenu="'+fromcookId+'_'+orderNumber+'">'
							}else{
								text += '<tr class="menuClickTR" chooseMenu="'+fromcookId+'_'+orderNumber+'">'
							}
							
							text += '<input type="hidden" name="orderListID" value="'+orderMenuList[t].orderListID+'" />'
							if(orderMenuList[t].orderMenuPacking != 0){
								text += '<td width="10%" style="vertical-align : middle; padding: 0 0 0 6%;"><i class="fas fa-check d-block"></i></td>'
							}else{
								text += '<td width="10%"></td>'
							}
							
							if(orderMenuList[t].orderMenuNotice != "N"){
								text += '<td width="30%" style="text-align: left;"><b><del style="color: red;">'+orderMenuList[t].productName+'</del></b></td>'
							}else{
								text += '<td width="30%" style="text-align: left;"><b>'+orderMenuList[t].productName+'</b></td>'
							}
							text += '<td width="20%">'+orderMenuList[t].orderCount+'</td>'
							text += '</tr>'
						}
					}

					text += '</tbody>'
					text += '</table>'
					text += '</td>'
					text += '<td width="10%" style="cursor: pointer; vertical-align : middle; padding: 0 0 0 5%;" class="bellTD"><i class="far fa-bell d-block"></i></td>'
					text += '<td width="10%" style="cursor: pointer; vertical-align : middle; padding: 0 0 0 5%;" class="complteTD"><i class="fas fa-times-circle d-block"></i></td>'
					text += '</tr>'
			    	$(text).appendTo('#sortableGroup');
				}
			}
		},
		error: ajaxReadError
	});
}

function alarmDID(home){
	var $chooseMenuTable = home.parent().find("table.tableIn"); 
	var $chooseMenuTableTr = $chooseMenuTable.find("tr"); 
	var menuListLen = $chooseMenuTableTr.length;
	
	var cookId = $chooseMenuTable.find("input[name=cookId]").val();
	var storeId = $chooseMenuTable.find("input[name=storeId]").val();
	var storeOrderId = $chooseMenuTable.find("input[name=storeOrderId]").val();
	var orderSeq = $chooseMenuTable.find("input[name=orderSeq]").val();

	var orderIDList = new Array(); 
	var messagealarm = orderSeq+"번 주문을 알리시겠습니까?";
	var complteYn = "N";
	if(menuListLen > 1){
		var $menuList = home.parent().find("table.tableIn tr");
		
		var checkCnt = 0;
		$menuList.each(function(){
			
			if(!$(this).hasClass("jbBox") || $(this).hasClass("jbBoxComp")){
				complteYn = "N";
				checkCnt++;
			}else{
				var orderListID = $(this).find("input[name=orderListID]").val();
				orderIDList.push(orderListID);
			}
			
			if($(this).hasClass("jbBoxComp")){
				checkCnt--;
			}
		});
		
		if($menuList.length == checkCnt){
			showAlertModal("danger", "메뉴를 선택해 주십시오.");
			return false;
		}
		
	}else{
		var count = 0;
		$chooseMenuTableTr.each(function(){
			if($(this).hasClass("jbBox") || $(this).hasClass("jbBoxComp")){
				count++;
			}
			
			var orderListID = $(this).find("input[name=orderListID]").val();
			orderIDList.push(orderListID);
		});
		
		if(count == 0){
			showAlertModal("danger", "메뉴를 선택해 주십시오.");
			return false;
		}
	}
	
	alarmDIDSend(complteYn, cookId, storeId, storeOrderId, orderSeq, orderIDList, messagealarm, $chooseMenuTable);
}

function alarmDIDSend(complteYn, cookId, storeId, storeOrderId, orderSeq, orderIDList, messagealarm, $chooseMenuTable){
	
	bootbox.confirm({
		size: "small",
		title: "${confirm_title}",
		message: messagealarm,
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
					siteId : -1,
					cookId : cookId,
					storeId : storeId,
					storeOrderId : storeOrderId,
					orderIDList : orderIDList,
					complteYn : complteYn
				};
				$.ajax({
					type: "POST",
					contentType: "application/json",
					dataType: "json",
					url: "${alarmUrl}",
					data: JSON.stringify(data),
					success: function (resData, status, xhr) {
						if("Y" == complteYn){
							showToastNotification("success", "메뉴가 완료되었습니다.");
							$chooseMenuTable.parent().parent().remove();		
						}else{
							showToastNotification("success", "알림을 울렸습니다.");
							$chooseMenuTable.find("tr").each(function(){
								if($(this).hasClass("jbBox")){
									$(this).addClass( 'jbBoxComp' );
									var $btag = $(this).find("b");
									var bText = $btag.html();
									$btag.html("<del style='color: red;'>"+bText+"</del>")
								}
							});	
						}
						
						fnNumberReset();
					},
					error: ajaxSaveError
				});
			}
		}
	});
	
	storeCookCnt(storeId);
	fnNumberReset();
}

function storeCookCnt(storeId){
	var data = {
			storeId: storeId
		};
		
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${stayCntRead}",
		data: JSON.stringify(data),
		success: function (form) {
			$('#stayCnt').text(form);
		},
		error: ajaxReadError
	});
}

function menuComplte(home){
	var $chooseMenuTable = home.parent().find("table.tableIn"); 
	var $chooseMenuTableTr = $chooseMenuTable.find("tr"); 
	
	var cookId = $chooseMenuTable.find("input[name=cookId]").val();
	var storeId = $chooseMenuTable.find("input[name=storeId]").val();
	var storeOrderId = $chooseMenuTable.find("input[name=storeOrderId]").val();
	var orderSeq = $chooseMenuTable.find("input[name=orderSeq]").val();
	
	var orderIDList = new Array(); 
	var messagealarm = orderSeq+"번 주문을 완료 처리 하겠습니까?";
	
	$chooseMenuTableTr.each(function(){
		if($(this).hasClass("jbBox")){
			var orderListID = $(this).find("input[name=orderListID]").val();
			orderIDList.push(orderListID);
		}
	});
	
	alarmDIDSend("Y", cookId, storeId, storeOrderId, orderSeq, orderIDList, messagealarm, $chooseMenuTable);
}

function tabActive(tabgubun){
	if("standby" == tabgubun){
		$("#standby").addClass("active");
		$("#complete").removeClass("active");
		$("#stayTableDIV").show();
		$("#complteTableDIV").hide();
		$('#comList').html("");
		$("#stayCntDiv").show();
		$("#completeCntDiv").hide();
	}else if("complete" == tabgubun){
		$("#standby").removeClass("active");
		$("#complete").addClass("active");
		$("#stayTableDIV").hide();
		$("#complteTableDIV").show();
		$("#stayCntDiv").hide();
		$("#completeCntDiv").show();
		
		menuCompltePage('${storeIdValue}');
	}
}

function menuCompltePage(storeId){

	if("" == storeId){
		showAlertModal("danger", "조회 가능한 매장이 없습니다.");
		return;
	}
	
	var data = {
		storeId: storeId
	};
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readComUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			$('#comList').html("");
			
			var formLen = form.length;
			$('#completeCnt').text(formLen);
			
			if(formLen > 0){
				for(var i = formLen; 1 <= i; i--) {
					var newi = i-1;
					var fromcookId = form[newi].cookId;
					var orderSeq = form[newi].orderSeq;
					var orderNumber = form[newi].orderNumber;
					var orderMenuListLen = form[newi].orderMenuList.length;

					var text = '';
					if((i%2) != 0){
						text += '<tr class="table-primary">';						
					}else{
						text += '<tr>';
					}
					text += '<th width="10%" class="orderSeqTd" style="vertical-align : middle;">'+orderSeq+'</th>'
					text += '<td width="10%" colspan="3">'
					text += '<table class="tableIn" >'
					text += '<tbody>'
					text += '<input type="hidden" name="cookId" value="'+fromcookId+'" />'
					text += '<input type="hidden" name="storeId" value="'+form[newi].storeId+'" />'
					text += '<input type="hidden" name="storeOrderId" value="'+form[newi].storeOrderId+'" />'
					text += '<input type="hidden" name="orderSeq" value="'+orderSeq+'" />'
					
					if(orderMenuListLen > 0){
						var orderMenuList = form[newi].orderMenuList;
						for(var t = 0; t < orderMenuListLen; t++) {
							if(orderMenuList[t].orderMenuNotice != "N"){
								text += '<tr class="menuClickTR jbBoxComp" chooseMenu="'+fromcookId+'_'+orderNumber+'">'
							}else{
								text += '<tr class="menuClickTR" chooseMenu="'+fromcookId+'_'+orderNumber+'">'
							}
							
							text += '<input type="hidden" name="orderListID" value="'+orderMenuList[t].orderListID+'" />'
							if(orderMenuList[t].orderMenuPacking != 0){
								text += '<td width="10%" style="vertical-align : middle; padding: 0 0 0 6%;"><i class="fas fa-check d-block"></i></td>'
							}else{
								text += '<td width="10%"></td>'
							}
							
							if(orderMenuList[t].orderMenuNotice != "N"){
								text += '<td width="30%" style="text-align: left;"><b><del style="color: red;">'+orderMenuList[t].productName+'</del></b></td>'
							}else{
								text += '<td width="30%" style="text-align: left;"><b>'+orderMenuList[t].productName+'</b></td>'
							}
							text += '<td width="20%">'+orderMenuList[t].orderCount+'</td>'
							text += '</tr>'
						}
					}

					text += '</tbody>'
					text += '</table>'
					text += '</td>'
					text += '<td width="20%" style="cursor: pointer; vertical-align : middle; " class="comCancelTD"><button type="button" class="btn btn-secondary">취소</button></td>'
					text += '</tr>'
			    	$(text).appendTo('#comList');
				}
			}
		},
		error: ajaxReadError
	});
}

function comCancelFn(comHome){
	var $chooseMenuTable = comHome.parent().find("table.tableIn"); 
	var cookId = $chooseMenuTable.find("input[name=cookId]").val();
	
	var data = {
			siteId : -1,
			cookId : cookId,
			storeId : '${storeIdValue}'
		};
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${comCancelUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			showSaveSuccessMsg();
			storeRead('${storeIdValue}', '${storeNameValue}');
			menuCompltePage('${storeIdValue}');
			storeCookCnt('${storeIdValue}');
		},
		error: ajaxReadError
	});
}

function reNewListApp(){
	var data = {
		storeId: '${storeIdValue}'
	};
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${renuwReadUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			var formLen = form.length;
			if(formLen > 0){
				var $cookDiv = $("#sortableGroup .tableIn").find("input[name=cookId]");
				$('#stayCnt').text(formLen);
				for(var i = 0; i < formLen; i++) {
					var $homeListTr;
					var addMenuTR = true;
		 			$cookDiv.each(function(){
		 				if($(this).val() == form[i].cookId){
		 					addMenuTR = false;
		 					
		 					$homeListTr = $(this);
		 				}
	 				});
					
 					var fromcookId = form[i].cookId;
 					var orderSeq = form[i].orderSeq;
 					var orderNumber = form[i].orderNumber;
 					var orderMenuListLen = form[i].orderMenuList.length;
		 			if(addMenuTR){
	 					var text = '';
	 					if((i%2) != 0){
	 						text += '<tr class="table-primary">';						
	 					}else{
	 						text += '<tr>';
	 					}
	 					text += '<th scope="row" name="cooknumber" width="10%" style="vertical-align : middle;">'+ (i+1) +'</th>'
	 					text += '<td width="10%" class="orderSeqTd" style="vertical-align : middle;">'+orderSeq+'</td>'
	 					text += '<td width="10%" colspan="3">'
	 					text += '<table class="tableIn" >'
	 					text += '<tbody>'
	 					text += '<input type="hidden" name="cookId" value="'+fromcookId+'" />'
	 					text += '<input type="hidden" name="storeId" value="'+form[i].storeId+'" />'
	 					text += '<input type="hidden" name="storeOrderId" value="'+form[i].storeOrderId+'" />'
	 					text += '<input type="hidden" name="orderSeq" value="'+orderSeq+'" />'
						
	 					if(orderMenuListLen > 0){
	 						var orderMenuList = form[i].orderMenuList;
	 						for(var t = 0; t < orderMenuListLen; t++) {
	 							if(orderMenuList[t].orderMenuNotice != "N"){
	 								text += '<tr class="menuClickTR jbBoxComp" chooseMenu="'+fromcookId+'_'+orderNumber+'">'
	 							}else{
	 								text += '<tr class="menuClickTR" chooseMenu="'+fromcookId+'_'+orderNumber+'">'
	 							}
								
	 							text += '<input type="hidden" name="orderListID" value="'+orderMenuList[t].orderListID+'" />'
	 							if(orderMenuList[t].orderMenuPacking != 0){
	 								text += '<td width="10%" style="vertical-align : middle; padding: 0 0 0 6%;"><i class="fas fa-check d-block"></i></td>'
	 							}else{
	 								text += '<td width="10%"></td>'
	 							}
								
	 							if(orderMenuList[t].orderMenuNotice != "N"){
	 								text += '<td width="30%" style="text-align: left;"><b><del style="color: red;">'+orderMenuList[t].productName+'</del></b></td>'
	 							}else{
	 								text += '<td width="30%" style="text-align: left;"><b>'+orderMenuList[t].productName+'</b></td>'
	 							}
	 							text += '<td width="20%">'+orderMenuList[t].orderCount+'</td>'
	 							text += '</tr>'
	 						}
	 					}

	 					text += '</tbody>'
	 					text += '</table>'
	 					text += '</td>'
	 					text += '<td width="10%" style="cursor: pointer; vertical-align : middle; padding: 0 0 0 5%;" class="bellTD"><i class="far fa-bell d-block"></i></td>'
	 					text += '<td width="10%" style="cursor: pointer; vertical-align : middle; padding: 0 0 0 5%;" class="complteTD"><i class="fas fa-times-circle d-block"></i></td>'
	 					text += '</tr>'
	 			    	$(text).appendTo('#sortableGroup');
		 			}else{
		 				if(orderMenuListLen > 0){
	 						var orderMenuList = form[i].orderMenuList;
	 						for(var t = 0; t < orderMenuListLen; t++) {
	 							$homeListTr.parent().find("tr").each(function(){
		 							if($(this).find("input[name=orderListID]").val() == orderMenuList[t].orderListID){
		 								if("N" != orderMenuList[t].orderMenuNotice){
		 									if(!$(this).hasClass("jbBoxComp")){
		 										$(this).addClass( "jbBoxComp" );
		 										var $btag = $(this).find("b");
		 										var bText = $btag.html();
		 										$btag.html("<del style='color: red;'>"+bText+"</del>")
		 									}
		 								}
		 							}
				 				});
	 						}
		 				}
		 			}
				}
				
				
				$cookDiv.each(function(){
					var removeDiv = true;
					for(var i = 0; i < formLen; i++) {
						if($(this).val() == form[i].cookId){
		 					removeDiv = false;
		 				}	
					}
		 			if(removeDiv){
		 				$(this).parent().parent().parent().parent().remove();
		 			}
 				});
				fnNumberReset();
			}
		},
		error: ajaxReadError
	});
	
	
	renuwCancelList();
}

function renuwCancelList(){
	var data = {
		storeId: '${storeIdValue}'
	};
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${renuwCancelReadUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			var formLen = form.length;
			if(formLen > 0){
 				var $cookDivCancel = $("#sortableGroup .tableIn").find("input[name=cookId]");
				//취소된 목록을 삭제
				for(var i = 0; i < formLen; i++) {
					$cookDivCancel.each(function(){
		 				if($(this).val() == form[i].cookId){
		 					if(form[i].menuCancelYN == "Y"){
		 						$(this).parent().parent().parent().parent().remove();
		 					}
		 				}
	 				});
				}
			}
			fnNumberReset();
		},
		error: ajaxReadError
	});
	
	fnNumberReset();
	storeCookCnt('${storeIdValue}');
	padEnabledFn();
}

function fnNumberReset(){
	var $sortableGroupChg = $("#sortableGroup");
	var $cooknumberChg = $sortableGroupChg.find(" > tr");
	$cooknumberChg.each(function(index){ 
		$(this).find("th[name=cooknumber]").html(index+1);
		if((index%2) != 0){
			if(!$(this).hasClass("table-primary")){
				$(this).addClass( 'table-primary' );
			}
		}else{
			if($(this).hasClass("table-primary")){
				$(this).removeClass( 'table-primary' );
			}
		}
	});
}

</script>


<!-- / Page body -->
	</c:when>
	<c:otherwise>

<div class="row pt-3">
	<div class="col-md">
		<div class="card text-center mb-3">
			<div class="mb-4 mt-5 select-desc text-center text-muted">
				<svg class="item-icon">
					<use xlink:href="/resources/shared/other-icon.svg#exclamation"></use>
				</svg>
				<br>
				<h3>${cook_noorder}</h3>
				<h4>${mag_errorAdmin}</h4>
			</div>
		</div>
	</div>
</div>

<style>

.item-icon {
	width: 192px; height: 192px; fill: currentColor;
}

</style>	

<script type="text/javascript">

$(function() {
	setInterval(padEnabledFn, 5000);
});

</script>


	</c:otherwise>
</c:choose>

<script type="text/javascript">

function padEnabledFn(){
	var data = {
			padChk: 'K',
			deviceId: '${deviceId}'
		};
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "/storecook/inactiveChk",
		data: JSON.stringify(data),
		success: function (form) {
			if('NO' != form){
				document.location.href=form;
			}
		},
		error: ajaxReadError
	});
}

</script>



<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />


						</div>
						<!-- / Content -->

					</div>
					<!-- Layout content -->

				</div>
				<!-- / Layout container -->

			</div>
			<!-- Overlay -->
			
		</div>
		<!-- / Layout wrapper -->

		<!-- Core scripts -->
		<script src="/resources/vendor/lib/popper/popper.js"></script>
		<script src="/resources/vendor/js/bootstrap.js"></script>
		<script src="/resources/vendor/js/sidenav.js"></script>

		<!-- Libs -->
		<script src="/resources/vendor/lib/perfect-scrollbar/perfect-scrollbar.js"></script>
		<script src="/resources/vendor/lib/toastr/toastr.js"></script>
		<script src="/resources/vendor/lib/bootbox/bootbox.js"></script>
		<script src="/resources/vendor/lib/bootstrap-select/bootstrap-select.js"></script>
	</body>
</html>