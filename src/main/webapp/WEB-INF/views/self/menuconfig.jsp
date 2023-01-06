<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- URL -->
<c:url value="/self/menuconfig/read" var="readUrl" />

<c:url value="/self/menuconfig/menuSave" var="menuSaveUrl" />
<c:url value="/self/menuconfig/groupSave" var="saveGroupUrl" />
<c:url value="/self/menuconfig/menuUpdate" var="menuUpdateUrl" />
<c:url value="/self/menuconfig/groupUpdate" var="groupUpdateUrl" />
<c:url value="/self/menuconfig/menuDestroy" var="menuDestroyUrl" />
<c:url value="/self/menuconfig/groupDestroy" var="groupDestroyUrl" />
<c:url value="/self/menuconfig/menuSeqChange" var="menuSeqChange" />
<c:url value="/self/menuconfig/groupsSeqChange" var="groupsSeqChange" />
<c:url value="/self/menuconfig/selectMenuRead" var="selectMenuRead" />

<c:url value="/store/storeInfo/updateKiosk" var="storeMonTask" />

<c:url value="/store/storesite/readStores" var="readStoreUrl" />
<c:url value="/store/storeInfo/readGroup" var="readGroupUrl" />

<common:pageOpening />

<script src="/resources/shared/js/jquery.form.js"></script>

<link rel="stylesheet" href="/resources/vendor/css/ionicons.css">
<link rel="stylesheet" href="/resources/vendor/lib/dragula/dragula.css">
<link rel="stylesheet" href="/resources/vendor/lib/bootstrap-tagsinput/bootstrap-tagsinput.css">
  
<style>
	.sortableGroup {margin: 0;padding: 0;}
	.sortableGroup li {background: #ffffff;padding: 6px 10px;margin-bottom: 8px;list-style: none;border: 2px solid #A3A4A6;}
	
	.divCard{margin-top: 5px;padding-left: 10px;}
	.divCardBody{padding-top: 5px;padding-bottom: 5px;}
	
	.group-invalid{border-color: #d9534f !important;}

	.menudelDiv{text-align: right;}
	.menudelDiv i{padding-right: 3%;}
	.menudelDiv button{margin-top: 0.5%;margin-right: 1%;}
	
	.underIn{border: 0; outline: 0; background: transparent; border-bottom: 1px solid black; font-size: small; }
	
	.menuPop{margin-top: 1%;}
</style>

<h4 class="font-weight-bold py-3 mb-3">
	<span class="mr-1 fas fa-${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
	<div style="float: right;">
		<button type="button" class="btn btn-outline-secondary" onclick="fnPageUpload();" >
			<span >${title_stbupload}</span>
		</button>
	</div>
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
	<div class="form-row" >
		<input type="hidden" id="site" name="sites" value="${currentSiteID}" />
		<input type="hidden" id="store" name="stores" value="${storeIdValue}" />
		<input type="hidden" id="storeNm" name="storesNm" value="${storeNameValue}" />
	</div>
</div>

<div class="card mb-4">
	<h6 class="card-header">
		<button id="groupAdd" type="button" class="btn btn-outline-success" onclick="groupAdd();" ><span class="fas fa-plus"></span>&nbsp;&nbsp;${menuGroupAdd}</button>
		<button type="button" data-toggle="modal" data-target="#groupChgSeq" class="btn btn-outline-secondary" onclick="groupChgSeqFn();" ><span class="fas fa-sort"></span>&nbsp;&nbsp;${menuGroupSeqChg}</button>
	</h6>
	<div class="container-fluid flex-grow-1 container-p-y" style="margin-left: 12px;">
		<input type="hidden" id="menuStoreIdLast" name="menuStoreIdLast"/>
		<ul class="sortableGroup" id="sortableGroup"></ul>
	</div>
</div>


<div class="modal fade" data-backdrop="static" id="modalMenu">
	<div class="modal-dialog">
		<form class="modal-content" id="menuAddForm" enctype="multipart/form-data">
			<input type="hidden" id="menuSiteId" name="menuSiteId" />
			<input type="hidden" id="menuStoreId" name="menuStoreId" />
			<input type="hidden" id="groupSetMenu" name="groupSetMenu" />
			<input type="hidden" id="groupSetDragulaDiv" />
			<input type="hidden" id="menuAddSeq" name="menuAddSeq" />
			<div class="modal-header move-cursor">
				<h5 class="modal-title">${menuMenuAdd}</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-row">
					<h6 class="underIn">${title_mainMenu}</h6>
				</div>
				<div class="form-row" style="margin-left: 0px;">
					<div class="form-group col" style="padding: 0; margin:0;">
						<input type="text" id="menuAddName" name="menuAddName" class="form-control" placeholder="${menuAdd}" maxlength="10" />
					</div>
				</div>
				<div class="form-row menuPop" style="margin-left: 0px;">
					<div class="form-group col" style="padding: 0; margin:0;">
						<input type="text" id="menuAddPrice" name="menuAddPrice" class="form-control" placeholder="${menuPrice}" maxlength="10" />
					</div>
				</div>
				<div class="form-row menuPop" style="margin-left: 0px;">
					<div class="form-group col" style="padding: 0; margin:0;">
						<input type="file" id="menuImage" name="menuImage" class="custom-file-input" onchange="$('#upload-file-info').html(this.files[0].name);" />
						<span class="custom-file-label" id="upload-file-info">${menuIn}</span>
						<small>${menuInDec}</small>
					</div>
				</div>
				<div class="form-row" style="margin-left: 0px;">
					<select id="menuAddOption" name="menuAddOption" class="selectpicker" style="width: 100%;" data-style="btn-default" data-none-selected-text="${menuOption}">
						<option value=""></option>
						<option value="0">${menuOpNew}</option>
						<option value="1">${menuOpNom}</option>
						<option value="2">${menuOpEmp}</option>
					</select>
				</div>
				<div class="form-row menuPop" style="margin-left: 0px;">
                     <textarea class="form-control" id="menuDescription" maxlength="200" name="menuDescription" placeholder="${menuDescription}"></textarea>
				</div>
			</div>
			<hr class="m-0">
			<div class="modal-body">
				<div class="form-row">
					<h6 class="underIn">${title_selectMenu}</h6>
				</div>
				<div class="form-row">
					<div class="col-sm-3">
						<input type="text" name="essSelectMenu" class="form-control" placeholder="${essSelectMenu}" maxlength="10" />
					</div>
					<div class="col-sm-9">
						<input type="text" name="essSelectMenuTag" value="" data-role="tagsinput" class="form-control" />
					</div>
				</div>
				<div class="form-row menuPop">
					<div class="col-sm-3">
						<input type="text" name="essSelectMenu" class="form-control" placeholder="${essSelectMenu}" maxlength="10" />
					</div>
					<div class="col-sm-9">
						<input type="text" name="essSelectMenuTag" value="" data-role="tagsinput" class="form-control" />
					</div>
				</div>
				<div class="form-row menuPop">
					<div class="col-sm-3">
						<input type="text" name="essSelectMenu" class="form-control" placeholder="${essSelectMenu}" maxlength="10" />
					</div>
					<div class="col-sm-9">
						<input type="text" name="essSelectMenuTag" value="" data-role="tagsinput" class="form-control" />
					</div>
				</div>
				<div class="form-row menuPop">
					<div class="col-sm-3">
						<input type="text" name="addSelectMenu" class="form-control" placeholder="${addSelectMenu}" maxlength="10" />
					</div>
					<div class="col-sm-9">
						<input type="text" name="addSelectMenuTag" value="" data-role="tagsinput" class="form-control" />
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick="menuSaveAdd();">${form_save}</button>
			</div>
		</form>
	</div>
</div>

<div class="modal fade" data-backdrop="static" id="modalupdate">
	<div class="modal-dialog">
		<form class="modal-content" id="menuUpdateForm" enctype="multipart/form-data">
			<input type="hidden" id="menuUpId" name="menuUpId" />
			<div class="modal-header move-cursor">
				<h5 class="modal-title">${menuMenuUpd}</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-row">
					<h6 class="underIn">${title_mainMenu}</h6>
				</div>
				<div class="form-row" style="margin-left: 0px;">
					<div class="form-group col" style="padding: 0; margin:0;">
						<input type="text" id="menuUpName" name="menuUpName" class="form-control" placeholder="${menuAdd}" maxlength="10" />
					</div>
				</div>
				<div class="form-row menuPop" style="margin-left: 0px;">
					<div class="form-group col" style="padding: 0; margin:0;">
						<input type="text" id="menuUpPrice" name="menuUpPrice" class="form-control" placeholder="${menuPrice}" maxlength="10" />
					</div>
				</div>
				<div class="form-row menuPop" style="margin-left: 0px;">
					<div class="form-group col" style="padding: 0; margin:0;">
						<input type="file" id="menuUpImage" name="menuUpImage" class="custom-file-input" onchange="$('#update-upload-file-info').html(this.files[0].name);" />
						<span class="custom-file-label" id="update-upload-file-info">${menuIn}</span>
						<small>${menuInDec}</small>
					</div>
				</div>
				<div class="form-row" style="margin-left: 0px;">
					<select id="menuUpOption" name="menuUpOption" class="selectpicker" style="width: 100%;" data-style="btn-default" data-none-selected-text="${menuOption}">
						<option value=""></option>
						<option value="0">${menuOpNew}</option>
						<option value="1">${menuOpNom}</option>
						<option value="2">${menuOpEmp}</option>
					</select>
				</div>
				<div class="form-row menuPop" style="margin-left: 0px;">
                     <textarea class="form-control" id="menuUpDescription" maxlength="200" name="menuUpDescription" placeholder="${menuDescription}"></textarea>
				</div>
			</div>
			<hr class="m-0">
			<div class="modal-body">
				<div class="form-row">
					<h6 class="underIn">${title_selectMenu}</h6>
				</div>
				<div class="form-row">
					<div class="col-sm-3">
						<input type="text" name="essSelectMenu" class="form-control" placeholder="${essSelectMenu}" maxlength="10" />
					</div>
					<div class="col-sm-9">
						<input type="text" name="essSelectMenuTag" value="" data-role="tagsinput" class="form-control" />
					</div>
				</div>
				<div class="form-row menuPop">
					<div class="col-sm-3">
						<input type="text" name="essSelectMenu" class="form-control" placeholder="${essSelectMenu}" maxlength="10" />
					</div>
					<div class="col-sm-9">
						<input type="text" name="essSelectMenuTag" value="" data-role="tagsinput" class="form-control" />
					</div>
				</div>
				<div class="form-row menuPop">
					<div class="col-sm-3">
						<input type="text" name="essSelectMenu" class="form-control" placeholder="${essSelectMenu}" maxlength="10" />
					</div>
					<div class="col-sm-9">
						<input type="text" name="essSelectMenuTag" value="" data-role="tagsinput" class="form-control" />
					</div>
				</div>
				<div class="form-row menuPop">
					<div class="col-sm-3">
						<input type="text" name="addSelectMenu" class="form-control" placeholder="${addSelectMenu}" maxlength="10" />
					</div>
					<div class="col-sm-9">
						<input type="text" name="addSelectMenuTag" value="" data-role="tagsinput" class="form-control" />
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick="menuSaveUpdate();">${form_save}</button>
			</div>
		</form>
	</div>
</div>

<div class="modal fade" data-backdrop="static" id="groupChgSeq">
	<div class="modal-dialog">
		<form class="modal-content" >
			<div class="modal-header move-cursor">
				<h5 class="modal-title">${menuGroupSeqChg}</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div id="groupChgDiv"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" style="display:none;" id="groupSaveBtn" onclick="groupdrop();">${form_save}</button>
			</div>
		</form>
	</div>
</div>
<div class="modal fade" data-backdrop="static" id="menuChgSeq">
	<div class="modal-dialog">
		<form class="modal-content" >
			<div class="modal-header move-cursor">
				<h5 class="modal-title">${menuMenuSeqChg}</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div id="menuChgDiv"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" style="display:none;" id="menuSaveBtn" onclick="menudrop();">${form_save}</button>
			</div>
		</form>
	</div>
</div>


<!--  Root form container -->
<div id="formRoot"></div>

<!--  Forms -->

<script id="template-1" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog">
		<form id="groupAddForm1" onsubmit="return false;">
		<div class="modal-content">
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">${menuGroupAdd}<span class="font-weight-light pl-1"><span name="subtitle"></span></h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body">
				<div class="form-group">
					<input type="text" id="groupNameAdd" name="groupNameAdd" class="form-control" placeholder="${menuGroupAddText}" maxlength="12" />
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='groupSaveForm1()'>${form_save}</button>
			</div>
		</div>
		</form>
	</div>
</div>

</script>

<script id="template-2" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-2">
	<div class="modal-dialog">
		<form id="groupUpForm1" onsubmit="return false;">
		<div class="modal-content">
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">${menuGroupUpd}<span class="font-weight-light pl-1"><span name="subtitle"></span></h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body">
				<div class="form-group">
					<input type="hidden" id="groupIdUp" name="groupIdUp" />
					<input type="text" id="groupNameUp" name="groupNameUp" class="form-control" placeholder="${menuGroupAddText}" maxlength="12" />
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='groupUpForm1()'>${form_save}</button>
			</div>
		</div>
		</form>
	</div>
</div>

</script>

<script type="text/javascript">
var menuImgTagArray = [];
var menuImgUpdateArray = [];

function groupAdd() {
	// 사이트와 그룹 매장이 선택 되어 있어야만 팝업을 띄운다.
	if("" == $("#store").val()){
		showAlertModal("danger", "매장을 선택하여 주시기 바랍니다.");
		return false;
	}
	
	$("#formRoot").html(kendo.template($("#template-1").html()));
	
	$("#groupAddForm1").validate({
		rules: {
			groupNameAdd: {
				required: true, minlength: 2, maxlength: 9
			}
		}
	});
	
	$('#form-modal-1 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-1").modal();
}

function groupSaveForm1() {
	var groupAddName = $("#groupNameAdd").val();
	
	if ($("#groupAddForm1").valid()) {
		var data = {
			groupAddName: groupAddName
			, storeId: $("#store").val()
			, groupAddSeq : $("#sortableGroup li").length
		};
			
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${saveGroupUrl}",
			data: JSON.stringify(data),
			success: function (resData) {
			    var fromId = resData.id;
			    var menuGroupName = resData.menuGroupName;
			    var menuGroupSeq = resData.menuGroupSeq;
			    groupAddfn(fromId,menuGroupName,menuGroupSeq);
			},
			error: ajaxSaveError
		});
	    
	    $("#form-modal-1").modal("hide");
	}
}


var groupChgSeqDrag;
function groupChgSeqFn() {
	if(nvlCheck(groupChgSeqDrag,false)){
		groupChgSeqDrag.destroy();
	}
 	$( "#groupChgSeq" ).draggable({handle: ".modal-header"});
	// 그룹의 목록을 가져온다
	var data = {
		storeId: $("#store").val()
	};
	$('#groupChgDiv').html("");
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			var formLen = form.length;
			if(formLen > 0){
				for(var i = 0; i < form.length; i++) { 
					var fromId = form[i].id;
					groupReadFn(fromId,form[i].menuGroupName,form[i].menuGroupSeq,"#groupChgDiv");
				}
				$("#groupSaveBtn").show();
			}else{
				chgReadFn("#groupChgDiv");
				$("#groupSaveBtn").hide();
			}
			
			groupChgSeqDrag = dragula([$('#groupChgDiv')[0]], {revertOnSpill: true});
		},
		error: ajaxReadError
	});
}

function groupReadFn(fromId,menuGroupName,menuGroupSeq,div){
    $('<div class="card card-condenced groupDiv" style="margin-bottom: 6px;"><div class="card-body" style="padding-top: 5px; padding-bottom: 5px;">'
	    	+'&nbsp; <span name="groupSpan">'+menuGroupName +'</span></div>'
	    	+'<input type="hidden" id="groupId_'+fromId+'" name="groupId" value="'+fromId+'" />'
	    	+'<input type="hidden" id="groupName_'+fromId+'" name="groupName" value="'+menuGroupName+'" />'
	    	+'<input type="hidden" id="groupSeq_'+fromId+'" name="groupSeq" value="'+menuGroupSeq+'" />'
	    	+'</div>').appendTo(div);
}

function groupdrop(){
	var groups = [];
	$('#groupChgDiv').find(".groupDiv").each(function(index){
		$(this).find("input[name=groupSeq]").val(index);
		groups.push($(this).find("input[name=groupId]").val());
	});
	
	if (groups.length > 0) {
		groupChangeFn(groups);
	}
}

function groupChangeFn(groups){
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${groupsSeqChange}",
		data: JSON.stringify({ items: groups }),
		success: function (form) {
			showSaveSuccessMsg();
			$('#groupChgSeq').modal('hide');
			storeRead($("#store").val(), $("#storeNm").val());
		},
		error: ajaxSaveError
	});
}

// ------------------ 그룹 순서 변경 완료 -----------------------------------------
// 3. 메뉴 순서 변경
var menuChgSeqDrag;
function menuChgSeqFn(groupId) {
	if(nvlCheck(menuChgSeqDrag,false)){
		menuChgSeqDrag.destroy();
	}
 	$( "#menuChgSeq" ).draggable({handle: ".modal-header"});
	// 그룹의 목록을 가져온다
	var data = {
		storeId: $("#store").val()
	};
	$('#menuChgDiv').html("");
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			
			var formLen = form.length;
			if(formLen > 0){
				for(var i = 0; i < form.length; i++) { 
					var fromId = form[i].id;
				    if(fromId == groupId){
				    	var selfMenuLen = form[i].selfMenu.length;
				    	if(selfMenuLen > 0){
				    		var fromSelfMenu = form[i].selfMenu;
				    		for(var t = 0; t < selfMenuLen; t++) { 
				    			var menuAddId = fromSelfMenu[t].id;
				    			var menuAddName = fromSelfMenu[t].name;
				    			var menuAddSeq = fromSelfMenu[t].seq;
					    
				    			menuReadFn(menuAddId,menuAddName,menuAddSeq,"#menuChgDiv");
	    					}
				    		$("#menuSaveBtn").show();
				    	}else{
				    		$("#menuSaveBtn").hide();
				    		chgReadFn("#menuChgDiv");
				    	}
				    }
				}
			}
			menuChgSeqDrag = dragula([$('#menuChgDiv')[0]], {revertOnSpill: true});
		},
		error: ajaxReadError
	});
}

function menuReadFn(menuAddId,menuAddName,menuAddSeq,div){
	$('<div class="card card-condenced menuDiv" style="margin-top: 5px; padding-left: 10px;">'
	     + '<div class="card-body" style="padding-top: 5px; padding-bottom: 5px;">'
       	 + '<input type="hidden" id="id_'+menuAddId+'" name="id" value="'+menuAddId+'" />'
       	 + '<input type="hidden" id="seq_'+menuAddId+'" name="seq" value="'+menuAddSeq+'" />'
       	 + '&nbsp;<span>'+menuAddName+'</span>' 
       	 + '</div>').appendTo(div);
}

function menudrop() {
	var menus = [];
	$('#menuChgDiv').find(".menuDiv").each(function(index){
		$(this).find("input[name=seq]").val(index);
		menus.push($(this).find("input[name=id]").val());
	});
	
	if (menus.length > 0) {
		menuChangeFn(menus);
	}
}

function chgReadFn(div){
	$('<div class="card card-condenced menuDiv" style="margin-top: 5px; padding-left: 10px;">'
	     + '<div class="card-body" style="padding-top: 5px; padding-bottom: 5px;">'
       	 + '&nbsp;<span>변경 가능 목록이 없습니다.</span>' 
       	 + '</div>').appendTo(div);
}

//------------------ 메뉴 순서 변경 완료 -----------------------------------------

function groupNmChange(groupId){
	$("#formRoot").html(kendo.template($("#template-2").html()));
	$('#form-modal-2 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-2").modal();
	
	
	$("#groupUpForm1").validate({
		rules: {
			groupNameUp: {
				required: true, minlength: 2, maxlength: 9
			}
		}
	});
	
	$("#groupIdUp").val(groupId);
	$("#groupNameUp").val($("#groupName_"+groupId).val());
}

function groupUpForm1() {
	var groupIdUp = $("#groupIdUp").val();
	var groupNameUp = $("#groupNameUp").val();
	if ($("#groupUpForm1").valid()) {
		var data = {
				groupIdUp: groupIdUp,
				groupNameUp: groupNameUp
			};
		
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${groupUpdateUrl}",
			data: JSON.stringify(data),
			success: function (resData) {
				var $groupName = $("#groupName_"+groupIdUp);
				$groupName.val(groupNameUp);
				$groupName.parent().find(".card-body span[name=groupSpan]").text(groupNameUp);
				
				showSaveSuccessMsg();
			},
			error: ajaxSaveError
		});
	    
	    $("#form-modal-2").modal("hide");
	}
}


function menuAdd(dragulaDiv, groupId){
	// 사이트와 그룹 매장이 선택 되어 있어야만 팝업을 띄운다.
	if("" == $("#store").val()){
		showAlertModal("danger", "사이트/STB그룹과 매장을 선택하여 주시기 바랍니다.");
		return false;
	}
	
 	$( "#modalMenu" ).draggable({handle: ".modal-header"});
	
	// 이미지 초기화 작업 필요 
	menuImgTagArray[1] = menuImgTagArray[0].clone(true);
    $('#menuAddForm').replaceWith(menuImgTagArray[1]);

    var div = eval("\'#"+dragulaDiv+"\'");
	$("#menuAddSeq").val($(div).find("div[name=groupmenunum]").length);
	
	$("#menuSiteId").val($("#site").val());
	$("#menuStoreId").val($("#store").val());
	$("#groupSetMenu").val(groupId);
	
	$("#groupSetDragulaDiv").val(dragulaDiv);
	
	$('#menuAddForm').find("input[name=essSelectMenuTag]").tagsinput();
	$('#menuAddForm').find("input[name=addSelectMenuTag]").tagsinput();

    bootstrapSelectVal($("#menuAddOption"), "");
    
	$("#menuAddForm").validate({
		rules: {
			menuAddName: {
				required: true, minlength: 2, maxlength: 10
			},
			menuAddPrice: {
				required: true, minlength: 3, number:true
			},
			menuImage: {
				required: true
			},
			menuDescription: {
				rangelength:[2,200]
			}
		}
	});
	
	$('#menuAddForm').find("input[name=essSelectMenuTag]").on('beforeItemAdd', function(event) {
	    event.cancel = !tagsInputChk(event.item);
	});
	$('#menuAddForm').find("input[name=addSelectMenuTag]").on('beforeItemAdd', function(event) {
	    event.cancel = !tagsInputChk(event.item);
	});
}

function menuSaveAdd(){
	var menuCheckEss = false;
	var menuCheckAdd = false;
	menuCheckEss = selectMenuCheck($('#menuAddForm').find("input[name=essSelectMenuTag]"), "essSelectMenu");
	menuCheckAdd = selectMenuCheck($('#menuAddForm').find("input[name=addSelectMenuTag]"), "addSelectMenu");
	
	if( menuCheckEss && menuCheckAdd){
		$('#menuAddForm').ajaxForm({
			type: "POST",
	    	url : "${menuSaveUrl}",
	        success: function(data) {
	        	// 메뉴가 만들어질 위치
	        	var dragulaDiv = $("#groupSetDragulaDiv").val();
	        	var div = eval("\'#"+dragulaDiv+"\'");
	        	
	        	var groupSetMenu = $("#groupSetMenu").val();
	        	
	        	// 이미지 명
	        	var menuAddId = data.id;
	        	var menuAddName = data.name;
	        	var menuAddPrice = data.price;
	        	var menuAddOption = data.option;
	        	var menuAddImgOri = data.image;
	        	var menuAddImgOriName = data.imageOriName;
	        	var menuAddDescription = data.description;
	        	var menuAddSeq = data.seq;
	        	var menuAddGroup = data.menuGroupID;
	        	
	        	menuAddfn(menuAddId,menuAddName,menuAddPrice,menuAddOption,menuAddSeq,menuAddDescription,menuAddImgOri,menuAddGroup,menuAddImgOriName,div);
	        	
	        	showSaveSuccessMsg();
	        	
	        	$('#modalMenu').modal('hide');
	        },
	        error: ajaxSaveError
	    }).submit();
	}
}


function tagsInputChk(inputName){
	var checkMenu = inputName.split(" ");
	 if(checkMenu.length > 1){
		 // 1. 금액 체크 - 숫자인지 체크
		 // 2. 금액 체크 - 두자 이상인지 체크 (100원 이상)  
		 // 3. 메뉴 명 체크  - 글자 20자 제한(화면에서 확인 필요)
		 var checkMenulast = checkMenu[checkMenu.length-1];
		 if(!$.isNumeric(checkMenulast)){
			showAlertModal("danger", '가격을 정상입력하였는지 "{0}"을 다시 확인하여 주시기 바랍니다.'.replace("{0}", inputName));
			return false;
		 }
		 
// 		 if(checkMenulast.length < 2){
// 			showAlertModal("danger", '가격을 정상입력하였는지 "{0}"을 다시 확인하여 주시기 바랍니다.'.replace("{0}", inputName));
// 			return false;
// 		 }
		 
		 var testText = "";
		 for(var test=0; test < checkMenu.length-1; test++) {
			 testText += (" " + checkMenu[test]);
		 }
		 
		 var reTF = fncChkByte(testText.trim(), 18, "");
		 return reTF;
	 }else{
		showAlertModal("danger", '[{0}]을 다시 확인하여 주시기 바랍니다.'.replace("{0}", inputName));
		return false;
	 }
}

function selectMenuCheck($home, inputName){
	var reTF = true;
	//필수 선택명이 있을 경우 
	// 1. 필수 선택 목록을 조회 하여 데이터를 확인한다. 
	// 1-1. 조회한 데이터중 메뉴명 영역과 금액 부분을 체크
	// 1-2. 금액 부분은 마지막 부분으로 숫자인지 체크
	// 1-3. 메뉴명 영역에서는 글자수를 체크 한다. 제한 : 20자
	 $home.each(function(){
		 var menuSelName = $(this).parent().parent().find("input[name="+inputName+"]").val();
		 if(menuSelName != null && menuSelName != ""){
			 var essSMT = $(this).val();
			 if(essSMT != null && essSMT != ""){
				 var essSelectMenuTagArray = essSMT.split(",");
				 for(var essCnt=0; essCnt < essSelectMenuTagArray.length; essCnt++) {
					 // 스페이스 기준으로 split 한 뒤 마지막 값이 숫자인지 확인 
					 // 그리고 나머지 값을 합 하여 20자 이상인지 확인 
					 // 정상적이면 넘어가고 아니라면 해당 값에 대한 에러 메시지 보여주기
					 var checkMenu = essSelectMenuTagArray[essCnt].split(" ");
					 if(checkMenu.length > 1){
						 // 1. 금액 체크 - 숫자인지 체크
						 // 2. 금액 체크 - 두자 이상인지 체크 (100원 이상)  
						 // 3. 메뉴 명 체크  - 글자 20자 제한(화면에서 확인 필요)
						 var checkMenulast = checkMenu[checkMenu.length-1];
						 if(!$.isNumeric(checkMenulast)){
							showAlertModal("danger", '선택 메뉴 중 "{0}"을 다시 확인하여 주시기 바랍니다.'.replace("{0}", essSelectMenuTagArray[essCnt]));
							reTF = false;
							return false;
						 }
						 
// 						 if(checkMenulast.length < 2){
// 							showAlertModal("danger", '선택 메뉴 중 "{0}"을 다시 확인하여 주시기 바랍니다.'.replace("{0}", essSelectMenuTagArray[essCnt]));
// 							reTF = false;
// 							return false;
// 						 }
						 
						 var testText = "";
						 for(var test=0; test < checkMenu.length-1; test++) {
							 testText += (" " + checkMenu[test]);
						 }
						 
						 reTF = fncChkByte(testText.trim(), 20, essSelectMenuTagArray[essCnt]);
						 
					 }else{
						showAlertModal("danger", '선택 메뉴 중 [{0}]을 다시 확인하여 주시기 바랍니다.'.replace("{0}", essSelectMenuTagArray[essCnt]));
						reTF = false;
						return false;
					 }
				 }
			 }else{
					showAlertModal("danger", '선택 메뉴 중 [{0}]의 메뉴를 확인할 수 없습니다.'.replace("{0}", menuSelName));
					reTF = false;
					return false;
			 }
		 }
		 
		 if(!reTF){
			 return false;
		 }
	 });
	
	return reTF;
}

function fncChkByte(ls_str, maxByte, oriText) {
	var li_str_len = ls_str.length; 
	var li_byte = 0;
	var ls_one_char = "";
 
	for(var i=0; i< li_str_len; i++) {
		ls_one_char = ls_str.charAt(i);
		if(escape(ls_one_char).length > 4){
			li_byte += 2;
		}else{
			li_byte++;
		}
		if(li_byte <= maxByte) {
			li_len = i + 1;
		}
	}
	if(li_byte > maxByte) {
		if(oriText != ""){
			showAlertModal("danger", "한글 "+(maxByte/2)+"자 / 영문 "+maxByte+"자를 초과 입력 할 수 없습니다. <br />선택 메뉴 중 [{0}]을 다시 확인하여 주시기 바랍니다.".replace("{0}", oriText));
		}else{
			showAlertModal("danger", "한글 "+(maxByte/2)+"자 / 영문 "+maxByte+"자를 초과 입력 할 수 없습니다.");
		}
		
		return false;
	}else{
		return true;
	}
}

$(function() {
	menuImgTagArray = [$('#menuAddForm').clone()];
	
	menuImgUpdateArray = [$('#menuUpdateForm').clone()];
	
	storeRead('${storeIdValue}', '${storeNameValue}');
});

function storeRead(storeId, storeNm){
	if("" == storeId){
		showAlertModal("danger", "조회 가능한 매장이 없습니다.");
		return;
	}
	
	$("#store").val(storeId);
	$("#storeNm").val(storeNm);
	
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
			if(formLen > 0){
				for(var i = 0; i < form.length; i++) { 
					var fromId = form[i].id;
					groupAddfn(fromId,form[i].menuGroupName,form[i].menuGroupSeq);
				    
			    	var selfMenuLen = form[i].selfMenu.length;
			    	if(selfMenuLen > 0){
			    		var div = eval("\'#dragulaDiv_"+fromId+"\'");
			    		var fromSelfMenu = form[i].selfMenu;
			    		for(var t = 0; t < selfMenuLen; t++) { 
			    			var menuAddId = fromSelfMenu[t].id;
			    			var menuAddName = fromSelfMenu[t].name;
			    			var menuAddPrice = fromSelfMenu[t].price;
			    			var menuAddOption = fromSelfMenu[t].option;
			    			var menuAddSeq = fromSelfMenu[t].seq;
			    			var menuAddDescription = fromSelfMenu[t].description;
			    			var menuAddImgOri = fromSelfMenu[t].image;
			    			var menuAddImgOriName = fromSelfMenu[t].imageOriName;
			    			var menuAddGroup = fromSelfMenu[t].menuGroupID;
				    
				    		menuAddfn(menuAddId,menuAddName,menuAddPrice,menuAddOption,menuAddSeq,menuAddDescription,menuAddImgOri,menuAddGroup,menuAddImgOriName,div);
    					}
			    	}
				}
			}
		},
		error: ajaxReadError
	});
}

function groupAddfn(fromId,menuGroupName,menuGroupSeq){
	
    $('<li style="font-size: larger;">'
	    	+'<div class="card card-condenced" style="margin-top: 5px; padding-left: 10px; padding-bottom: 5px;">'
	    	+'<div class="card-body" style="padding-top: 5px; padding-bottom: 5px; position: absolute; width: 62%;" onclick="groupNmChange(\''+fromId+'\');">'
	    	+'&nbsp; <span name="groupSpan">'+menuGroupName +'</span>'
	    	+'</div>'
	    	+'<div class="menudelDiv">'
	    	+'<button type="button" class="btn btn-sm btn-outline-success" data-toggle="modal" data-target="#modalMenu" onclick="menuAdd(\'dragulaDiv_'+fromId+'\',\''+fromId+'\');" >'
			<c:choose>
				<c:when test="${Mobile eq 'N'}">
					+'<span class="fas fa-plus"></span>&nbsp;&nbsp;${menuMenuAdd}</button>'
					+'<button type="button" data-toggle="modal" data-target="#menuChgSeq" class="btn btn-sm btn-outline-secondary" onclick="menuChgSeqFn(\''+fromId+'\');" >'
					+'<span class="fas fa-sort"></span>&nbsp;&nbsp;${menuMenuSeqChg}</button>'
				</c:when>
				<c:otherwise>
					+'<span class="fas fa-plus"></span></button>'
					+'<button type="button" data-toggle="modal" data-target="#menuChgSeq" class="btn btn-sm btn-outline-secondary" onclick="menuChgSeqFn(\''+fromId+'\');" >'
					+'<span class="fas fa-sort"></span></button>'
				</c:otherwise>
			</c:choose>
			+'<button type="button" data-toggle="modal" class="btn btn-sm btn-danger" style="float: right; cursor: pointer;" onclick="groupDel(\''+fromId+'\');" >'
	    	+'<i class="ion ion-md-close"></i></button></div></div>'
	    	+'<input type="hidden" id="groupId_'+fromId+'" name="groupId" value="'+fromId+'" />'
	    	+'<input type="hidden" id="groupName_'+fromId+'" name="groupName" value="'+menuGroupName+'" />'
	    	+'<input type="hidden" id="groupSeq_'+fromId+'" name="groupSeq" value="'+menuGroupSeq+'" />'
	    	+'<div id="dragulaDiv_'+fromId+'" name="dragulaDiv" style="padding-left: 24px;">'
	    	+'</div>').appendTo('#sortableGroup');
}

function menuAddfn(menuAddId,menuAddName,menuAddPrice,menuAddOption,menuAddSeq,menuAddDescription,menuAddImgOri,menuAddGroup,menuAddImgOriName,div){
	
	$('<div name="groupmenunum" class="card card-condenced" style="margin-top: 5px; padding-left: 10px; padding-bottom: 5px;">'
	     + '<div class="card-body" style="padding-top: 5px; padding-bottom: 5px; position: absolute; width: 90%;" data-toggle="modal" data-target=\"#modalupdate\" onclick="menuUpdateModal('+menuAddId+');">'
       	 + '<input type="hidden" id="id_'+menuAddId+'" name="id" value="'+menuAddId+'" />'
       	 + '<input type="hidden" id="name_'+menuAddId+'" name="name" value="'+menuAddName+'" />'
       	 + '<input type="hidden" id="price_'+menuAddId+'" name="price" value="'+menuAddPrice+'" />'
       	 + '<input type="hidden" id="option_'+menuAddId+'" name="option" value="'+menuAddOption+'" />'
       	 + '<input type="hidden" id="seq_'+menuAddId+'" name="seq" value="'+menuAddSeq+'" />'
       	 + '<input type="hidden" id="description_'+menuAddId+'" name="description" value="'+menuAddDescription+'" />'
       	 + '<input type="hidden" id="imgOriginal_'+menuAddId+'" name="imgOriginal" value="'+menuAddImgOri+'" />'
       	 + '<input type="hidden" id="menuGroupID_'+menuAddId+'" name="menuGroupID" value="'+menuAddGroup+'" />'
       	 + '<input type="hidden" id="imgOriName_'+menuAddId+'" name="imgOriName" value="'+menuAddImgOriName+'" />'
       	 + '&nbsp;<span>'+menuAddName+'</span>' 
       	 + '</div><div class="menudelDiv">'
       	 + '<button type="button" class="btn btn-sm btn-danger" onclick="menuOneDel(\''+menuAddId+'\',\''+menuAddGroup+'\');"><i class="ion ion-md-close"></i></button></div></div>').appendTo(div);

}

function menuUpdateModal(menuAddId){
	$( "#modalupdate" ).draggable({handle: ".modal-header"});
	// 이미지 초기화 작업 필요 
	menuImgUpdateArray[1] = menuImgUpdateArray[0].clone(true);
    $('#menuUpdateForm').replaceWith(menuImgUpdateArray[1]);
    
	var data = {
			menuId: menuAddId
	};
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${selectMenuRead}",
		data: JSON.stringify(data),
		success: function (form) {
			var $updateEssTag = $('#menuUpdateForm').find("input[name=essSelectMenuTag]");
			var $updateAddTag = $('#menuUpdateForm').find("input[name=addSelectMenuTag]");
			if(form.length > 0){
				$updateEssTag.each(function(ess){
					for(var i=0; i<form.length; i++){
						if(ess == form[i].seq && form[i].menuGubun == 0){
							$(this).val(form[i].menuList);
							var $parentFind = $(this).parent().parent(); 
							$parentFind.find("input[name=essSelectMenu]").val(form[i].name);
							var appendInput = '<input type="hidden" name="selectId" value="'+form[i].id+'" />';
							appendInput += '<input type="hidden" name="selectMenuGubun" value="'+form[i].menuGubun+'" />';
							$parentFind.find("div.col-sm-3").append(appendInput);
						}
					}
				});
				$updateAddTag.each(function(add){
					for(var i=0; i<form.length; i++){
						if(add == form[i].seq && form[i].menuGubun == 1){
							$(this).val(form[i].menuList);
							var $parentFind = $(this).parent().parent(); 
							$parentFind.find("input[name=addSelectMenu]").val(form[i].name);
							var appendInput = '<input type="hidden" name="selectId" value="'+form[i].id+'" />';
							appendInput += '<input type="hidden" name="selectMenuGubun" value="'+form[i].menuGubun+'" />';
							$parentFind.find("div.col-sm-3").append(appendInput);
						}
					}
				});
			}
			
			$updateEssTag.tagsinput();
			$updateAddTag.tagsinput();
			
			$updateEssTag.on('beforeItemAdd', function(event) {
			    event.cancel = !tagsInputChk(event.item);
			});
			$updateAddTag.on('beforeItemAdd', function(event) {
			    event.cancel = !tagsInputChk(event.item);
			});
		},
		error: ajaxReadError
	});
	
	var id = $("#id_"+menuAddId).val();
	var name = $("#name_"+menuAddId).val();
	var price = $("#price_"+menuAddId).val();
	var option = $("#option_"+menuAddId).val();
	var description = $("#description_"+menuAddId).val();
	var imgOriginal = $("#imgOriginal_"+menuAddId).val();
	var imgOriName = $("#imgOriName_"+menuAddId).val();
	
	$("#menuUpId").val(id);
	$("#menuUpName").val(name);
	$("#menuUpPrice").val(price);
	$("#update-upload-file-info").html(imgOriName);
	
	bootstrapSelectVal($("#menuUpOption"), option);
	$("#menuUpDescription").val(description);
	
	$("#menuUpdateForm").validate({
		rules: {
			menuUpName: {
				required: true, minlength: 2, maxlength: 10
			},
			menuUpPrice: {
				required: true, minlength: 3, number:true
			},
			menuUpDescription: {
				rangelength:[2,200]
			}
		}
	});
}

function menuSaveUpdate(){
	var menuCheckEss = false;
	var menuCheckAdd = false;
	menuCheckEss = selectMenuCheck($('#menuUpdateForm').find("input[name=essSelectMenuTag]"), "essSelectMenu");
	menuCheckAdd = selectMenuCheck($('#menuUpdateForm').find("input[name=addSelectMenuTag]"), "addSelectMenu");
	
	if( menuCheckEss && menuCheckAdd){
		$('#menuUpdateForm').ajaxForm({
			type: "POST",
	    	url : "${menuUpdateUrl}",
	        success: function(data) {
	        	
	        	var menuUpId = data.id;
	        	$("#id_"+menuUpId).val(menuUpId);
	        	$("#name_"+menuUpId).val(data.name);
	        	$("#price_"+menuUpId).val(data.price);
	        	$("#option_"+menuUpId).val(data.option);
	        	$("#description_"+menuUpId).val(data.description);
	        	$("#imgOriginal_"+menuUpId).val(data.image);
	        	$("#imgOriName_"+menuUpId).val(data.imageOriName);
	        	
	        	$("#id_"+menuUpId).parent().find("span").html(data.name);
	        	
	        	showSaveSuccessMsg();
	        	
	        	$('#modalupdate').modal('hide');
	        },
	        error: ajaxSaveError
	    }).submit();
	}
}

function groupDel(groupId){
	var data = {
			groupId: groupId
		};
	bootbox.confirm({
		size: "small",
		title: "${confirm_title}",
		message: "해당 그룹을 지우실 경우 포함된 메뉴들이 삭제 됩니다. 삭제 하시겠습니까?",
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
					url: "${groupDestroyUrl}",
					data: JSON.stringify(data),
					success: function (form) {
						$("#groupId_"+groupId).parent().remove();
						showDeleteSuccessMsg();
    					
						var groups = [];
						$("#sortableGroup").find("li").each(function(index){
							$(this).find("input[name=groupSeq]").val(index);
							groups.push($(this).find("input[name=groupId]").val());
						});
						if (groups.length > 0) {
							groupChangeFn(groups);
						}
					},
					error: ajaxDeleteError
				});
			}
		}
	});
}

function menuOneDel(menuId,menuGroupId){
	
	var data = {
			menuId: menuId
		};
	bootbox.confirm({
		size: "small",
		title: "${confirm_title}",
		message: "${msg_delConfirm}".replace("{0}", "<strong>1</strong>"),
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
					url: "${menuDestroyUrl}",
					data: JSON.stringify(data),
					success: function (form) {
						$("#id_"+menuId).parent().parent().remove();
						
						showDeleteSuccessMsg();
    					
    					var menus = [];
    					var groupTotal = $("#groupId_"+menuGroupId).parent();
    					groupTotal.find("div[name=groupmenunum]").each(function(index){
    						$(this).find("input[name=seq]").val(index);
    						menus.push($(this).find("input[name=id]").val());
    					});
    					
    					if (menus.length > 0) {
    						menuChangeFn(menus);
    					}
					},
					error: ajaxDeleteError
				});
			}
		}
	});
}

function menuChangeFn(menus){
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${menuSeqChange}",
		data: JSON.stringify({ items: menus }),
		success: function (form) {
			showSaveSuccessMsg();
			$('#menuChgSeq').modal('hide');
			storeRead($("#store").val(), $("#storeNm").val());
		},
		error: ajaxSaveError
	});
}

function fnPageUpload(){
	var storeName = $("#storeNm").val(); 
	if(""== storeName){
		showAlertModal("danger", "새로고침 가능한 매장이 없습니다.");
		return;
	}
	var text = "해당 "+storeName+" 매장에 대해서 정보를 새로고침 하시겠습니까?";
	bootbox.confirm({
		size: "small",
		title: "${confirm_title}",
		message: text,
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
				showWaitModal();
				var data = {
						page : "M",
						siteId : $("#site").val(),
						storeId : $("#store").val()
					};
				$.ajax({
					type: "POST",
					contentType: "application/json",
					dataType: "json",
					url: "${storeMonTask}",
					data: JSON.stringify(data),
					success: function (data, status, xhr) {
						showSaveSuccessMsg();
						hideWaitModal();
					},
					error: function (data) {
						hideWaitModal();
						ajaxSaveError(data);
					}
				});
			}
		}
	});
}

function nvlCheck(str, defaultVal) {
    var defaultValue = "";
     
    if (typeof defaultVal != 'undefined') {
        defaultValue = defaultVal;
    }
     
    if (typeof str == "undefined" || str == null || str == '' || str == "undefined") {
        return defaultValue;
    }
     
    return str;
}
</script>



<func:cmmValidate />

<common:base />
<common:pageClosing />