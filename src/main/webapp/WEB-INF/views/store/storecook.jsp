<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- URL -->
<c:url value="/storecook/read" var="readUrl" />
<c:url value="/storecook/alarmUpdate" var="alarmUrl" />
<c:url value="/storecook/stayCntRead" var="stayCntRead" />
<c:url value="/storecook/readCom" var="readComUrl" />
<c:url value="/storecook/comCancelUpdate" var="comCancelUrl" />
<c:url value="/storecook/renuwRead" var="renuwReadUrl" />
<c:url value="/storecook/renuwCancelRead" var="renuwCancelReadUrl" />

<common:pageOpening />

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

<c:set var="storeIdValue" value="${storeIdCo}"></c:set>
<c:set var="storeNameValue" value="${storeNameCo}"></c:set>			

<c:choose>
	<c:when test="${padEnabled}">

<div class="row">
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
	                  <th width="10%">${title_package}</th>
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
	                  <th width="10%">${title_ordernum}</th>
	                  <th width="10%">${title_package}</th>
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
});

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
				console.log("$(this).hasClass(jbBox) >>> ["+$(this).hasClass('jbBox')+"]");
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
	
	fnNumberReset();
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
					siteId : ${sessionScope['currentSiteId']},
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
					text += '<th width="10%" class="orderSeqTd" style="vertical-align : middle;">'+orderSeq+'</th>';
					text += '<td width="10%" colspan="3">';
					text += '<table class="tableIn" >';
					text += '<tbody>';
					text += '<input type="hidden" name="cookId" value="'+fromcookId+'" />';
					text += '<input type="hidden" name="storeId" value="'+form[newi].storeId+'" />';
					text += '<input type="hidden" name="storeOrderId" value="'+form[newi].storeOrderId+'" />';
					text += '<input type="hidden" name="orderSeq" value="'+orderSeq+'" />';
					
					if(orderMenuListLen > 0){
						var orderMenuList = form[newi].orderMenuList;
						for(var t = 0; t < orderMenuListLen; t++) {
							if(orderMenuList[t].orderMenuNotice != "N"){
								text += '<tr class="menuClickTR jbBoxComp" chooseMenu="'+fromcookId+'_'+orderNumber+'">';
							}else{
								text += '<tr class="menuClickTR" chooseMenu="'+fromcookId+'_'+orderNumber+'">';
							}
							
							text += '<input type="hidden" name="orderListID" value="'+orderMenuList[t].orderListID+'" />';
							if(orderMenuList[t].orderMenuPacking != 0){
								text += '<td width="10%" style="vertical-align : middle; padding: 0 0 0 6%;"><i class="fas fa-check d-block"></i></td>';
							}else{
								text += '<td width="10%"></td>';
							}
							
							if(orderMenuList[t].orderMenuNotice != "N"){
								text += '<td width="30%" style="text-align: left;"><b><del style="color: red;">'+orderMenuList[t].productName+'</del></b></td>';
							}else{
								text += '<td width="30%" style="text-align: left;"><b>'+orderMenuList[t].productName+'</b></td>';
							}
							text += '<td width="20%">'+orderMenuList[t].orderCount+'</td>';
							text += '</tr>';
						}
					}

					text += '</tbody>';
					text += '</table>';
					text += '</td>';
					text += '<td width="20%" style="cursor: pointer; vertical-align : middle; " class="comCancelTD"><button type="button" class="btn btn-secondary">취소</button></td>';
					text += '</tr>';
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
			siteId : ${sessionScope['currentSiteId']},
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
	 					text += '<th scope="row" name="cooknumber" width="10%" style="vertical-align : middle;">'+ (i+1) +'</th>';
	 					text += '<td width="10%" class="orderSeqTd" style="vertical-align : middle;">'+orderSeq+'</td>';
	 					text += '<td width="10%" colspan="3">';
	 					text += '<table class="tableIn" >';
	 					text += '<tbody>';
	 					text += '<input type="hidden" name="cookId" value="'+fromcookId+'" />';
	 					text += '<input type="hidden" name="storeId" value="'+form[i].storeId+'" />';
	 					text += '<input type="hidden" name="storeOrderId" value="'+form[i].storeOrderId+'" />';
	 					text += '<input type="hidden" name="orderSeq" value="'+orderSeq+'" />';
						
	 					if(orderMenuListLen > 0){
	 						var orderMenuList = form[i].orderMenuList;
	 						for(var t = 0; t < orderMenuListLen; t++) {
	 							if(orderMenuList[t].orderMenuNotice != "N"){
	 								text += '<tr class="menuClickTR jbBoxComp" chooseMenu="'+fromcookId+'_'+orderNumber+'">';
	 							}else{
	 								text += '<tr class="menuClickTR" chooseMenu="'+fromcookId+'_'+orderNumber+'">';
	 							}
								
	 							text += '<input type="hidden" name="orderListID" value="'+orderMenuList[t].orderListID+'" />';
	 							if(orderMenuList[t].orderMenuPacking != 0){
	 								text += '<td width="10%" style="vertical-align : middle; padding: 0 0 0 6%;"><i class="fas fa-check d-block"></i></td>';
	 							}else{
	 								text += '<td width="10%"></td>';
	 							}
								
	 							if(orderMenuList[t].orderMenuNotice != "N"){
	 								text += '<td width="30%" style="text-align: left;"><b><del style="color: red;">'+orderMenuList[t].productName+'</del></b></td>';
	 							}else{
	 								text += '<td width="30%" style="text-align: left;"><b>'+orderMenuList[t].productName+'</b></td>';
	 							}
	 							text += '<td width="20%">'+orderMenuList[t].orderCount+'</td>';
	 							text += '</tr>'
	 						}
	 					}

	 					text += '</tbody>';
	 					text += '</table>';
	 					text += '</td>';
	 					text += '<td width="10%" style="cursor: pointer; vertical-align : middle; padding: 0 0 0 5%;" class="bellTD"><i class="far fa-bell d-block"></i></td>';
	 					text += '<td width="10%" style="cursor: pointer; vertical-align : middle; padding: 0 0 0 5%;" class="complteTD"><i class="fas fa-times-circle d-block"></i></td>';
	 					text += '</tr>';
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
		 										$btag.html("<del style='color: red;'>"+bText+"</del>");
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
			}else{
				//목록이 없을 경우 
				$("#sortableGroup").html("");
			}
		},
		error: ajaxReadError
	});
	
	fnNumberReset();
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


	</c:when>
	<c:otherwise>
	
	없어요..
	</c:otherwise>
</c:choose>

<func:cmmValidate />

<common:base />
<common:pageClosing />
