<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/storeinfo/read" var="readUrl" />
<c:url value="/pay/storeinfo/update" var="updateUrl" />
<c:url value="/pay/storeinfo/updateStatus" var="updateStatusUrl" />
<c:url value="/pay/storeinfo/refresh" var="refreshUrl" />


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


<!-- Page scripts  -->
<link rel="stylesheet" type="text/css" href="/resources/css/timepicki.css">
<link rel="stylesheet" href="/resources/vendor/lib/bootstrap-toggle/bootstrap-toggle.css">
<link rel="stylesheet" href="/resources/vendor/lib/bootstrap-select2/select2.css">

<script src="/resources/js/timepicki.js"></script>
<script src="/resources/vendor/lib/bootstrap-toggle/bootstrap-toggle.js"></script>
<script src="/resources/vendor/lib/bootstrap-select2/select2.min.js"></script>


<c:if test="${not empty notifMsg}">

	<div class="alert alert-info alert-dismissible fade show">
		<button type="button" class="close" data-dismiss="alert">×</button>
		${notifMsg}
	</div>

</c:if>


<!--  Overview -->

<div class="card mb-3" style="display: none;" id="actControlDiv">
	<h6 class="card-header with-elements">
		<span>${ovwStoreName}</span>
		<span class="d-none d-sm-inline">
			<span class='px-2'>•</span>
			<span>${ovwShortName}</span>
		</span>
		<div class="card-header-elements ml-auto">
			<button id="refresh-btn" type="button" class="btn btn-round btn-outline-secondary btn-sm">
				<span class="fas fa-sync"></span>
				<span class="pl-1">${cmd_refresh}</span>
			</button>
		</div>
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
	<hr class="m-0">
	<div class="card-body py-3">
		<div style="line-height: 60px;">
		<span class="mb-4 pr-4">
			<input name="openTypeSwitch" type="checkbox" data-width="220"
<c:if test="${openType}">
					checked="checked"
</c:if>
					data-toggle="toggle" data-on="${switch_open}" data-off="${switch_closed}" 
					data-onstyle="success" data-offstyle="danger" data-size="large">
		</span>
		<span class="pr-2 pb-2">
			<input name="koSwitch" type="checkbox" data-width="220" disabled
<c:if test="${kioskOrderEnabled}">
					checked="checked"
</c:if>
					data-toggle="toggle" data-on="${switch_kioskEnabled}" data-off="${switch_kioskDisabled}" 
					data-onstyle="success" data-offstyle="danger" data-size="large">
		</span>
		<span class="pb-2">
			<input name="moSwitch" type="checkbox" data-width="220" disabled
<c:if test="${mobileOrderEnabled}">
					checked="checked"
</c:if>
					data-toggle="toggle" data-on="${switch_mobileEnabled}" data-off="${switch_mobileDisabled}" 
					data-onstyle="success" data-offstyle="danger" data-size="large">
		</span>
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


<!--  Forms -->

<style>

/* 스위치 색상 변경 */
.custom-btn-default {
    color: #333;
    background-color: #fff;
    border-color: #ccc;
}
.btn-danger.disabled, .btn-danger[disabled], fieldset[disabled] .btn-danger, .btn-danger.disabled:hover, .btn-danger[disabled]:hover, fieldset[disabled] .btn-danger:hover, .btn-danger.disabled:focus, .btn-default[disabled]:focus, fieldset[disabled] .btn-danger:focus, .btn-default.disabled:active, .btn-danger[disabled]:active, fieldset[disabled] .btn-danger:active, .btn-danger.disabled.active, .btn-danger[disabled].active, fieldset[disabled] .btn-danger.active {
    background-color: #fff;
    border-color: #ccc;
}
.btn-danger.disabled, .btn-danger[disabled], fieldset[disabled] .btn-danger {
    pointer-events: none;
    cursor: not-allowed;
    filter: alpha(opacity=65);
    -webkit-box-shadow: none;
    box-shadow: none;
    opacity: .65;
}
.btn-success.disabled, .btn-success[disabled], fieldset[disabled] .btn-success, .btn-success.disabled:hover, .btn-success[disabled]:hover, fieldset[disabled] .btn-success:hover, .btn-success.disabled:focus, .btn-default[disabled]:focus, fieldset[disabled] .btn-success:focus, .btn-default.disabled:active, .btn-success[disabled]:active, fieldset[disabled] .btn-success:active, .btn-success.disabled.active, .btn-success[disabled].active, fieldset[disabled] .btn-success.active {
    background-color: #fff;
    border-color: #ccc;
}
.btn-success.disabled, .btn-success[disabled], fieldset[disabled] .btn-success {
    pointer-events: none;
    cursor: not-allowed;
    filter: alpha(opacity=65);
    -webkit-box-shadow: none;
    box-shadow: none;
    opacity: .65;
}

</style>


<div class="card mb-3">
	<div class="card-body pb-3">
		<div class="pb-3">
			${tip_cardSlip}
			<span class="small text-muted pl-3">${desc_cardSlip}</span>
		</div>
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="form-label">${label_bizName}</label>
					<input type="text" class="form-control" name="bizName"  maxlength="50" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group col">
					<label class="form-label">${label_bizRep}</label>
					<input type="text" class="form-control" name="bizRep" maxlength="50" />
				</div>
			</div>
		</div>
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="form-label">${label_bizNum}</label>
					<input type="text" class="form-control" name="bizNum"  maxlength="50" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group col">
					<label class="form-label">${label_phone}</label>
					<input type="text" class="form-control" name="phone" maxlength="50" />
				</div>
			</div>
		</div>
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="form-label">${label_local}</label>
					<select name="localName" class="selectpicker bg-white" data-style="btn-default" data-none-selected-text="">
<c:forEach var="item" items="${Regions}">
					<option value="${item.value}">${item.text}</option>
</c:forEach>
					</select>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group col">
					<label class="form-label">${label_address}</label>
					<input type="text" class="form-control" name="address" maxlength="50" />
				</div>
			</div>
		</div>
	</div>
	<hr class="m-0" />
	<div class="card-body pb-3">
		<div class="pb-3">
			${tip_cardMobileOrder}
			<span class="small text-muted pl-3">${desc_cardMobileOrder}</span>
		</div>
		<hr class="mt-0" />
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="form-label">${labal_startTime}</label>
					<input type="text" id="startTime" name="timepicker-one" class="form-control time_element" value="${startTime}"/>
				</div>
			</div>
			<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="form-label">${labal_endTime}</label>
					<input type="text" id="endTime" name="timepicker-one" class="form-control time_element" value="${endTime}"/>
				</div>
			</div>
		</div>
	    <div class="form-row">
			<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="switcher switcher-success">
                    	<input id="24Hour" name="24Hour" type="checkbox" class="switcher-input">
                    	<span class="switcher-indicator">
                      		<span class="switcher-no">
                        		<span class="ion ion-md-close"></span>
                      		</span>
                      		<span class="switcher-yes">
                        		<span class="ion ion-md-checkmark"></span>
                      		</span>
                    	</span>
                    	<span class="switcher-label">${labal_open24Houres}</span>
                  	</label>
				</div>
			</div>
			<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="switcher switcher-success">
                    	<input id="nextDayClose" name="nextDayClose" type="checkbox" class="switcher-input">
                    	<span class="switcher-indicator">
                      		<span class="switcher-no">
                        		<span class="ion ion-md-close"></span>
                      		</span>
                      		<span class="switcher-yes">
                        		<span class="ion ion-md-checkmark"></span>
                      		</span>
                    	</span>
                    	<span class="switcher-label">${labal_nextDayClose}</span>
                  	</label>
				</div>
			</div>
		</div>
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="form-label">${msg_storeOpenHoures}</label>
				</div>
			</div>
	    </div>
		
		<hr class="mt-0" />
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
			    	<h7>${labal_orderable} ${labal_set}</h7>
				</div>
			</div>
		</div>
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="custom-control custom-radio">
	                	<input name="oder_setting_Time" type="radio" class="custom-control-input" value="on" checked="checked">
	                    <span class="custom-control-label"><b>${labal_always}</b></span>
	                </label>
				</div>
			</div>
			<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="custom-control custom-radio">
						<input name="oder_setting_Time" type="radio" class="custom-control-input" value="${labal_firstSetMin}">
	                    <span id="fursMin" class="custom-control-label" >${labal_firstMin}</span>
					</label>
				</div>
			</div>
		</div>
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="custom-control custom-radio">
						<input name="oder_setting_Time" type="radio" class="custom-control-input" value="${labal_secondSetMin}">
	                    <span id="seconMin" class="custom-control-label" >${labal_secondMin}</span>
					</label>
				</div>
			</div>
			<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="custom-control custom-radio">
						<input name="oder_setting_Time" type="radio" class="custom-control-input" value="${labal_thirdSetMin}">
	                    <span id="thirdMin" class="custom-control-label" >${labal_thirdMin}</span>
					</label>
				</div>
			</div>
		</div>
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="custom-control custom-radio">
						<input name="oder_setting_Time" type="radio" class="custom-control-input" value="direct">
	                    <span class="custom-control-label" >${labal_directTime}</span>
					</label>
					<label id="direct" class="text-muted mb-0"  style="display:none; width: 100%;">
						<p>
							<input type="text" id="oder_setting_Time" class="form-control oder_Time" value="00:00"/>
						</p>
					</label>
				</div>
			</div>
			<div class="col-sm-6">
			    <div class="form-group col" >
					<label class="custom-control custom-radio">
						<input name="oder_setting_Time" type="radio" class="custom-control-input" value="off">
	                    <span class="custom-control-label" >${labal_noOrder}</span>
					</label>
				</div>
			</div>
		</div>
		<hr class="mt-0" />
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
			    	<h7>${labal_appointment}</h7>
				</div>
			</div>
		</div>
	    <div class="form-row">
	    	<div class="col-sm-6">
			    <div class="form-group col" >
					<select class="rsvpTime form-control" multiple style="width: 100%">
						<c:forEach var="item" items="${timeList}">
							<option value="${item.timeNum}">${item.timeName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
	    	<div class="col-sm-6">
			    <div class="form-group col" style="text-align: right;">
					<label class="form-label">${msg_appointment}</label>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="text-right mb-3">
	<button id="save-btn" type="button" class="btn btn-primary">${form_save}</button>
</div>


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

	$("#kCnt").html("${kCnt}");
	$("#dCnt").html("${dCnt}");
	$("#nCnt").html("${nCnt}");
	$("#pCnt").html("${pCnt}");
});	


var storeId = -1;

var storeOpened = false;
var processing = false;

var koAllowed = ${koAllowed};
var moAllowed = ${moAllowed};

var oderSettingTime = null;
var hour24 = false;

var custom = true; 

$(document).ready(function() {

	$('.rsvpTime').each(function() {
		$(this).wrap('<div class="position-relative"></div>')
		.select2({
			placeholder: '${labal_appointment}',
			dropdownParent: $(this).parent()
		});
	})
	
	var afterSelectTime;
	$('.rsvpTime').on('select2:select', function(e) {
		var value = $(this).val();
		if(value.length > 20){
			$(this).val(afterSelectTime).trigger('change');
		}else{
			afterSelectTime = value;
		}
	});
	
	$("select[name='localName']").selectpicker('render');
	
	bootstrapSelectVal($("select[name='localName']"), "${initRegionCode}");

	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readUrl}",
		data: JSON.stringify({ }),
		success: function (data, status) {
			$("#actControlDiv").show();
			
			storeId = data.id;
			storeOpened = data.openType == "O";
			
			$("input[name='bizName']").val(data.bizName);
			$("input[name='bizRep']").val(data.bizRep);
			$("input[name='bizNum']").val(data.bizNum);
			$("input[name='phone']").val(data.phone);
			$("input[name='address']").val(data.addr2);

			bootstrapSelectVal($("select[name='localName']"), data.localCode);
			
			$("#24Hour").prop("checked", data.openHour24);
			hour24 = data.openHour24;
			var open24Text = "";
			if(hour24){
				$("#startTime").attr("disabled", true);
				$("#endTime").attr("disabled", true);	
				$("#nextDayClose").attr("disabled", true);
				
				open24Text = "${labal_open24Houres}";
			}else{
				$("#startTime").attr("disabled", false);
				$("#endTime").attr("disabled", false);	
				$("#nextDayClose").attr("disabled", false);
				open24Text = data.startTime+" ~ "+data.endTime;
			}
			
			$("#moOpenHours").html(open24Text);
			
			
			$("#nextDayClose").prop("checked", data.nextDayClose);
			
			oderSettingTime = data.oderPossibleCheck;
			if(oderSettingTime === "O"){
				// 언제나 가능
				orderSetting("on", "");
			}else if(oderSettingTime === "P"){
				if(data.settingTime > 0){
					// 시간지정	
					if(data.settingTime == 60){
						orderSetting("${labal_thirdSetMin}", data.oderPosibleTimeFomat);
					}else if(data.settingTime == 30){
						orderSetting("${labal_secondSetMin}", data.oderPosibleTimeFomat);
					}else if(data.settingTime == 10){
						orderSetting("${labal_firstSetMin}", data.oderPosibleTimeFomat);
					}
				}else{
					// direct
					orderSetting("direct", data.oderPosibleTimeFomat);
				}
			}else if(oderSettingTime === "C"){
				// 주문 불가능
				orderSetting("off", "");
			}
			if (koAllowed && storeOpened) {
				$("input[name='koSwitch']").bootstrapToggle('enable');
			}
			if (moAllowed && storeOpened) {
				$("input[name='moSwitch']").bootstrapToggle('enable');
			}else{
				orderSetting("off", "");
			}
			
			$("#oder_posible_Time").html(data.oderPosibleTimeFomat);
			
			$('.rsvpTime').val(data.rsvpTimeList).trigger('change');
			clcuRsvpTime(data.rsvpTimeList);
			
			custom = true;
		},
		error: ajaxReadError
	});
	
	$("input[name='openTypeSwitch']").change(function() {
		processing = true;
		
		storeOpened = $(this).prop('checked');
		
		if (storeOpened) {
			if (koAllowed) { $("input[name='koSwitch']").bootstrapToggle('enable'); }
			if (moAllowed) { $("input[name='moSwitch']").bootstrapToggle('enable'); }
			
			$("input[name='koSwitch']").bootstrapToggle("on");
			$("input[name='moSwitch']").bootstrapToggle("on");
		} else {
			$("input[name='koSwitch']").bootstrapToggle("off");
			$("input[name='moSwitch']").bootstrapToggle("off");
			
			if (koAllowed) { $("input[name='koSwitch']").bootstrapToggle('disable'); }
			if (moAllowed) { $("input[name='moSwitch']").bootstrapToggle('disable'); }
			$("#moOpenHours").html(startTime+" ~ "+endTime);
		}
		
		processing = false;
		saveStatus();
	});
	// / Open & Closed switch
	
	
	// Kiosk order switch
	$("input[name='koSwitch']").change(function() {
		if (processing == false) {
			saveStatus();
		}
	});
	
	// / Mobile order switch
	$("input[name='moSwitch']").change(function() {
		
		if (processing == false) {
			saveStatus();
		}
	});

	//OpenTime setting
	$('.time_element').timepicki({
		overflow_minutes:true,
		increase_direction:'up'});
	
	valChg($("#startTime"));
	valChg($("#endTime"));
	
	$('.oder_Time').timepicki({
		overflow_minutes:true,
		increase_direction:'up'});
	
	//시간 설정 직접입력 선택 체크 확인
	$('input[name="oder_setting_Time"]').change(function(){
		if($(this).val() == "direct") {
			$("#direct").show();
			var toDate = new Date();
			var ampm = toDate.getHours() < 12 ? "AM" : "PM";
			var hours = (h = toDate.getHours() % 12) ? h : 12;
			if(hours < 10){
				hours = "0"+hours;
			}
			var min = toDate.getMinutes();
			if(min < 10){
				min = "0"+min;
			}
			$("#oder_setting_Time").val(hours+":"+min+" "+ampm);
		}else{
			$("#direct").hide();
			$("#oder_setting_Time").val("00:00");
		}
	});
	
	/* 24시간 영업 체크 */
	$("input[name='24Hour'").click(function(){
		if($("#24Hour").is(":checked")){
			hour24=true;
			$("#startTime").attr("disabled", true);
			$("#endTime").attr("disabled", true);
			$("#nextDayClose").attr("disabled", true);
        }else{
        	hour24=false;
        	$("#startTime").attr("disabled", false);
			$("#endTime").attr("disabled", false);
			$("#nextDayClose").attr("disabled", false);
        }
	});
	
	// Save
	$("#save-btn").click(function(e) {
		var startTime = $.trim($("#startTime").val());
		var endTime = $.trim($("#endTime").val());
		var nextDayClose = $("#nextDayClose").is(":checked");
		var rsvpTime = $(".rsvpTime").val();
		var oderSettingTime = $('input[name="oder_setting_Time"]:checked').val();
		var timeCheck = 0;
		if(oderSettingTime === "direct"){
			var setTime = $.trim($("#oder_setting_Time").val());
			var changTime = timeChange(setTime);
			timeCheck = (changTime.split(":")[0]*60) + (changTime.split(":")[1]*1);
			oderSettingTime = "D:"+setTime;
		}
		
		if(startTime == ""){
			showAlertModal("danger", "${empty_startTime}");
			return false;
		}else if(endTime == ""){
			showAlertModal("danger", "${empty_endTime}");
			return false;
		}
		
		if(!hour24){
			var stch = timeChange( startTime );
	    	var edch = timeChange( endTime );
	    	
	    	var stchSp = ((stch.split(":")[0]*60) + (stch.split(":")[1]*1));
	    	var edchSp = ((edch.split(":")[0]*60) + (edch.split(":")[1]*1));
	    	
	    	if(nextDayClose){
	    		if(stchSp <= edchSp){
	    			showAlertModal("danger", "${empty_dayTime}");
	    			return false;
	    		}
	    	}else{
	    		if(stchSp >= edchSp){
	    			showAlertModal("danger", "${empty_dayTime}");
	    			return false;
	    		}
	    	}
		}
		
		var now = new Date();
		var nowTime  = now.getHours()*60+now.getMinutes()*1;
		
    	var data = {
        		id: storeId,
        		bizName: $.trim($("input[name='bizName']").val()),
        		bizRep: $.trim($("input[name='bizRep']").val()),
        		bizNum: $.trim($("input[name='bizNum']").val()),
        		phone: $.trim($("input[name='phone']").val()),
        		localCode: $("select[name='localName']").val(),
        		localName: $("select[name='localName']>option:selected").html(),
        		address: $.trim($("input[name='address']").val()),
    			start: startTime,
    			end: endTime,
    			oderSetting: oderSettingTime,
    			rsvpTime: rsvpTime,
    			hour24:hour24,
    			nextDayClose:nextDayClose
       		};
		    	
		if((timeCheck - nowTime) <= 0 && oderSettingTime.split(":")[0] == "D"){
	 		showAlertModal("danger", "${msg_afterSelect}");
		}else{
	    	$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: "${updateUrl}",
				data: JSON.stringify(data),
				success: function (data) {
					var onoffChg = "", moOnoffChg = ""; 
					
					$('input[name="24Hour"]').prop('checked', data.openHour24);
					hour24 = data.openHour24;
					if(data.openHour24){
						$("#moOpenHours").text("24시간영업");
						var compareOpne = $("input[name='openTypeSwitch']").prop('checked');
						if(!compareOpne)
							custom = false;
							$("input[name='openTypeSwitch']").bootstrapToggle('on');
						
					}else{
						if( nextDayClose ){
							if((nowTime-compareTime(endTime) < 0) && (nowTime-compareTime(startTime) < 0)){
								custom = false;
								storeOpened = true;
								onoffChg = "on";
							}else if((nowTime-compareTime(endTime) > 0) && (nowTime-compareTime(startTime) > 0)){
								custom = false;
								storeOpened = true;
								onoffChg = "on";
							}else{
								custom = false;
								storeOpened = false;
								onoffChg = "off";
							}
						}else{
							if((nowTime-compareTime(startTime) > 0) && (nowTime-compareTime(endTime) < 0)){
								custom = false;
								storeOpened = true;
								onoffChg = "on";
							}else{
								custom = false;
								storeOpened = false;
								onoffChg = "off";
							}
						}
					}
					
					$("#nextDayClose").prop("checked", data.nextDayClose);
					
					var oderPossibleCheck = data.oderPossibleCheck;
					switch (oderPossibleCheck) {
						case "O":
							orderSetting("on","")
							setRadio();
							moOnoffChg = "on";
							
							break;
						case "C":
							orderSetting("off","");
							setRadio();
							moOnoffChg = "off";

							break;
						case "P":
							setRadio();
							if(data.settingTime > 0){
								// 시간지정	
								if(data.settingTime == 60){
									orderSetting("${labal_thirdSetMin}", data.oderPosibleTimeFomat);
								}else if(data.settingTime == 30){
									orderSetting("${labal_secondSetMin}", data.oderPosibleTimeFomat);
								}else if(data.settingTime == 10){
									orderSetting("${labal_firstSetMin}", data.oderPosibleTimeFomat);
								}
							}else{
								// direct
								orderSetting("direct", data.oderPosibleTimeFomat);
							}
							moOnoffChg = "on";
							
							break;
						default:
							break;
					}
					
					if(hour24){
						$("#startTime").attr("disabled", true);
						$("#endTime").attr("disabled", true);	
						$("#nextDayClose").attr("disabled", true);
					}else{
						$("#startTime").attr("disabled", false);
						$("#endTime").attr("disabled", false);	
						$("#nextDayClose").attr("disabled", false);
					}
					
					if("" != onoffChg){
						$("input[name='openTypeSwitch']").bootstrapToggle(onoffChg);
					}
					if("" != moOnoffChg){
						$("input[name='moSwitch']").bootstrapToggle(moOnoffChg);
					}
					showAlertModal("success", "${msg_updateComplete}");
				},
				error: ajaxSaveError
			});
		}
	});
	// / Save
	
	// Select store
	$("#sel-store-btn").click(function(e) {
		e.preventDefault();
		
		location.href = "/pay/selectstore/check?uri=" + window.location.pathname;
	});
	// / Select store
	
	
	// Refresh
	$("#refresh-btn").click(function(e) {
		e.preventDefault();
		
		showConfirmModal("${msg_refresh}", function(result) {
			if (result) {
				$.ajax({
					type: "POST",
					contentType: "application/json",
					dataType: "json",
					url: "${refreshUrl}",
					success: function (data) {
						showOperationSuccessMsg();
					},
					error: ajaxOperationError
				});
			}
		});
		console.log("기기 새로고침!!");
	});
	// / Refresh
});
	
	
function saveStatus(){
	var kioskOrderEnabled = $("input[name='koSwitch']").prop('checked') ? "Y" : "N";
	var mobileOrderEnabled = $("input[name='moSwitch']").prop('checked') ? "Y" : "N";
	var oderSettingTime = $('input[name="oder_setting_Time"]:checked').val();
	var settingGroup = oderSettingGroup(oderSettingTime);
	var data = {
		id: storeId,
		openType: storeOpened ? "O" : "C",
		kioskOrderEnabled: kioskOrderEnabled,
		mobileOrderEnabled: mobileOrderEnabled,
		settingGroup: settingGroup,
		hour24: hour24,
		custom : custom
	};
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${updateStatusUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			showOperationSuccessMsg();
			
			if(mobileOrderEnabled == "Y"){
				if(settingGroup == "O"){
					setRadio();
					orderSetting("on","");
				}else  if(settingGroup == "C"){
					setRadio();
					orderSetting("on","");
				}
			}else{
				setRadio();
				orderSetting("off","");
			}
			custom = true;
		},
		error: ajaxSaveError
	});
}

function clcuRsvpTime(rsvpTimeList){
	var rsvpTimeViewVal = "";
	if(rsvpTimeList != ""){
		for(var i=0; i < rsvpTimeList.length; i++){
			var time = rsvpTimeList[i];
			if(time != ""){
			    var min = time % 60;
			    var hour = parseInt(time / 60); 
			    var total =  "+" + min + "${labal_minute}";
			    if(hour >= 1){
		        	if(min > 0){
		        		total = "+" + hour + "${labal_time}" + min + "${labal_minute}";
		        	}else{
		        		total = "+" + hour + "${labal_time} ";
		        	}
			    }
			    if(rsvpTimeViewVal === ""){
			    	rsvpTimeViewVal += total;
			    }else{
			    	rsvpTimeViewVal += (" , " + total);
			    }
			}
		}
		if(time != ""){
			$("span[name=rsvpTimeView]").html(rsvpTimeViewVal);
			$("#rsvpTimeDiv").show();
		}else{
			$("span[name=rsvpTimeView]").html("");
			$("#rsvpTimeDiv").hide();
		}
	}else{
		$("#rsvpTimeDiv").hide();
	}
}

function orderSetting(value, oderPosibleTime){	
	$('input[name="oder_setting_Time"]').each(function(){
		var oderSettingChk = $(this).val();
		if(value == oderSettingChk){
			switch (value) {
				case "direct":
					$(this).prop('checked', true);
					$("#direct").show();
					$("#oder_setting_Time").val(oderPosibleTime);				
					break;
				case "on":	
					$("input:radio[name='oder_setting_Time']:radio[value='on']").prop('checked', true);
					break;
				case "off":
					$("input:radio[name='oder_setting_Time']:radio[value='off']").prop('checked', true);				
					break;
				default:
					$(this).prop('checked', true);
						if("" != oderPosibleTime){
						var thisSpan = $(this).parent().find("span");
						var thisSpanHTML = thisSpan.html();
						thisSpan.html(thisSpanHTML + "("+oderPosibleTime+" 가능)");	
					}
					break;

			}
		}
	});
}

function timeChange(time){
	
	var arrayTime = new Array();
	arrayTime = time.split(" ");
	var hour, minute = null;
	
	if(arrayTime[1]=="AM"){
		hour = arrayTime[0].split(":")[0];
		minute = arrayTime[0].split(":")[1];
	}else{
		hour = arrayTime[0].split(":")[0]*1+12;
		minute = arrayTime[0].split(":")[1];
	}
	return hour+":"+minute;
}

function setRadio(){
	$("#fursMin").html("${labal_firstMin}");
	$("#seconMin").html("${labal_secondMin}");
	$("#thirdMin").html("${labal_thirdMin}");
	$("#oder_setting_Time").val("");
}

function oderSettingGroup(setting){
	switch (setting) {
	case "on":
		return "O";
	case "off":
		return "C";
	case "direct":
		return "D";	
	default:
		return "P";
	}
	return "";
}

function compareTime(time){
	var arrayTime = new Array();
	arrayTime = time.split(" ");
	var compare = null;
	
	if(arrayTime[1]=="AM"){
		compare = arrayTime[0].split(":")[0]*60 +arrayTime[0].split(":")[1]*1;
	}else{
		compare = (arrayTime[0].split(":")[0]*1+12)*60+arrayTime[0].split(":")[1]*1;
	}
	return compare;
}

function valChg($chgTime){
    var arrayTime = new Array();
    var arrayAMPM = new Array();
	var time = $chgTime.val();
	arrayTime = time.split(":");
	
	if(arrayTime.length > 1){
		arrayAMPM = arrayTime[1].split(" ");
		if(arrayAMPM.length > 1){
			$chgTime.attr({
				  "data-timepicki-tim": arrayTime[0],
				  "data-timepicki-mini": arrayAMPM[0],
				  "data-timepicki-meri": arrayAMPM[1]
				});
		}
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
