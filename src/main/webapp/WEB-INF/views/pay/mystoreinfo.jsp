<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/mystoreinfo/read" var="readUrl" />
<c:url value="/pay/mystoreinfo/updateStatus" var="updateStatusUrl" />
<c:url value="/pay/mystoreinfo/updateOpenTimes" var="updateOpenTimeUrl" />

<!-- Opening tags -->
<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold pt-1 pb-3 mb-3">
	<span class="mr-1 ${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
</h4>

<hr class="border-light container-m--x mt-0 mb-4">

<!-- Page body -->

<!-- Page scripts  -->
<link rel="stylesheet" href="/resources/vendor/lib/bootstrap-toggle/bootstrap-toggle.css">
<link rel="stylesheet" href="/resources/vendor/lib/bootstrap-select2/select2.css">
<link href="/resources/css/timepicki.css" rel="stylesheet" type="text/css">

<script src="/resources/js/timepicki.js" type="text/javascript" ></script> 
<script src="/resources/vendor/lib/bootstrap-toggle/bootstrap-toggle.js"></script>
<script src="/resources/vendor/lib/bootstrap-select2/select2.min.js"></script>  
  

<!--  Overview -->

<div class="card mb-3" style="display: none;" id="actControlDiv">
	<h6 class="card-header with-elements py-3">
		<span>${ovwStoreName}</span>
		<span class="d-none d-sm-inline">
			<span class='px-2'>•</span>
			<span>${ovwShortName}</span>
		</span>
	</h6>
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
<div class="row">
	<div class="col-sm-6">
		<div class="card mb-3">
			<div class="card-header"><h5 class="mb-0 py-1">${tip_cardSlip}</h5></div>
			<div class="card-body">
				<p><span class="text-muted pr-3">${label_bizName}:</span><span id="cardBizName"></span></p>
				<p><span class="text-muted pr-3">${label_bizRep}:</span><span id="cardBizRep"></span></p>
				<p><span class="text-muted pr-3">${label_bizNum}:</span><span id="cardBizNum"></span></p>
				<p><span class="text-muted pr-3">${label_phone}:</span><span id="cardPhone"></span></p>
				<p class="mb-0"><span class="text-muted pr-3">${label_address}:</span><span id="cardAddress"></span></p>
			</div>
		</div>	
	</div>
	<div class="col-sm-6">
		<div class="card mb-3">
			<div class="card-header">
				<h5 class="mb-0">${tip_cardMobileOrder} 
					<button style="float: right;" type="button" class="btn btn-sm btn-outline-success ml-auto" onclick="updateTime();" >${labal_uptateOpenTime}</button>
				</h5>
			</div>
			<div class="card-body">
				<p id="nowOpenTime" class="mb-3">
					<span class="text-muted pr-3" >${label_openHoures} :</span>
					<span id="moOpenHours"></span>
				</p>
				<div id="settingTime" style="display: none">
					<h5>${labal_storeOpenHoures}</h5>
					<p>${msg_storeOpenHoures}</p>
					
					<label class="text-muted mb-0" style="width: 100%; ">${labal_startTime}
						<input id='startTime' class="form-control timePicker" type='text'name='timepicker' value="${startTime}"/><br>
					</label>
					<label class="text-muted mb-0" style="width: 100%; ">${labal_endTime}
						<input id='endTime' class="form-control timePicker" type='text'name='timepicker' value="${endTime}"/><br>
					</label>
					<div style="line-height: 60px;">
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
					<hr>
					
					<h5>${labal_orderable} ${labal_set}</h5>
					<p>${msg_orderable}</p>
					<label class="custom-control custom-radio">
	                	<input name="oder_setting_Time" type="radio" class="custom-control-input" value="on" checked="checked">
	                    <span class="custom-control-label"><b>${labal_always}</b></span>
	                </label>
					<label class="custom-control custom-radio">
						<input name="oder_setting_Time" type="radio" class="custom-control-input" value="${labal_firstSetMin}">
	                    <span id="fursMin" class="custom-control-label" >${labal_firstMin}</span>
					</label>

					<label class="custom-control custom-radio">
						<input name="oder_setting_Time" type="radio" class="custom-control-input" value="${labal_secondSetMin}">
	                    <span id="seconMin" class="custom-control-label" >${labal_secondMin}</span>
					</label>

					<label class="custom-control custom-radio">
						<input name="oder_setting_Time" type="radio" class="custom-control-input" value="${labal_thirdSetMin}">
	                    <span id="thirdMin" class="custom-control-label" >${labal_thirdMin}</span>
					</label>
					
					<label class="custom-control custom-radio">
						<input name="oder_setting_Time" type="radio" class="custom-control-input" value="direct">
	                    <span class="custom-control-label" >${labal_directTime}</span>
					</label>
					
					<!-- 직접입력 선택시 활성화 -->
				    <label id="direct" class="text-muted mb-0"  style="display:none; width: 100%;">
				   		<input id='oder_setting_Time' class="form-control timePicker" type='text'name='timepicker' value="${endTime}"/><br>
				    </label>
				    
					<label class="custom-control custom-radio">
						<input name="oder_setting_Time" type="radio" class="custom-control-input" value="off">
	                    <span class="custom-control-label" >${labal_noOrder}</span>
					</label>
					
					<hr>
					<div class="form-row mb-3">
						<div class="col">
							<p class="text-muted mb-0">${labal_appointment}</p>
							<select class="rsvpTime form-control" multiple style="width: 100%">
								<c:forEach var="item" items="${timeList}">
									<option value="${item.timeNum}">${item.timeName}</option>
								</c:forEach>
							</select>
							<p>${msg_appointment}</p>
						</div>
					</div>
					<button type="button" class="btn btn-default" onclick="cancel();">${form_cancel}</button>
					<button type="button" class="btn btn-primary" onclick="updateOpenTime();">${form_save}</button>
				</div>
				<div id="oder_time_div">
					<p class="mb-3">
						<span class="text-muted pr-3">${labal_orderable} : </span>
						<span  id="oder_posible_Time"></span>
					</p>
				</div>
				<div id="rsvpTimeDiv">
					<p class="mb-0">
						<span class="text-muted pr-3">${labal_appointment} : </span>
						<span name="rsvpTimeView"></span>
					</p>
				</div>
			</div>
		</div>	
	</div>

</div>

<!--  Scripts -->

<script>

var storeId = -1;

var storeOpened = false;
var processing = false;

var koAllowed = ${koAllowed};
var moAllowed = ${moAllowed};

var oderSettingTime = null;
var hour24=false;

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
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readUrl}",
		data: JSON.stringify({ }),
		success: function (data, status) {
			
			storeId = data.id;
			storeOpened = data.openType == "O";
			
			if (koAllowed && storeOpened) {
				$("input[name='koSwitch']").bootstrapToggle('enable');
			}
			if (moAllowed && storeOpened) {
				$("input[name='moSwitch']").bootstrapToggle('enable');
			}
			
			$("#actControlDiv").show();
			
			$("#cardBizName").html(data.bizName);
			$("#cardBizRep").html(data.bizRep);
			$("#cardBizNum").html(data.bizNum);
			$("#cardPhone").html(data.phone);
			$("#cardAddress").html(data.address);
			oderSettingTime = data.oderSettingCheck;
			
			$("#nextDayClose").prop("checked", data.nextDayClose);
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
			
			$("#oder_posible_Time").html(data.oderPosibleTimeFomat);
			
			$('.rsvpTime').val(data.rsvpTimeList).trigger('change');
			clcuRsvpTime(data.rsvpTimeList);
			
			custom = true;
		},
		error: ajaxReadError
	});
	
	
	// Open & Closed switch
	$("input[name='openTypeSwitch']").change(function() {
		
		processing = true;
		
		storeOpened = $(this).prop('checked');
		var startTime = $.trim($("#startTime").val());
		var endTime = $.trim($("#endTime").val());
		
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
	// / Kiosk order switch
	
	
	// Mobile order switch
	$("input[name='moSwitch']").change(function() {
		if (processing == false) {
			saveStatus();
		}
	});

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
	
	$('.timePicker').timepicki({
		overflow_minutes:true,
		increase_direction:'up',});
	
	valChg($("#startTime"));
	valChg($("#endTime"));
});	

function saveStatus() {
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
			
			if(mobileOrderEnabled=="Y"){
				if(settingGroup=="O"){
					setRadio();
					orderSetting("on","");
				}else  if(settingGroup=="C"){
					orderSetting("on","");
					setRadio();
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

function updateOpenTime() {
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
	var nowTime = now.getHours()*60+now.getMinutes()*1;
	
		var data = {
				id: storeId,
				start: startTime,
				end: endTime,
				oderSetting: oderSettingTime,
				rsvpTime: rsvpTime,
				hour24: hour24,
				nextDayClose : nextDayClose
			};
		
		if(timeCheck-nowTime<=0 && oderSettingTime.split(":")[0] == "D"){
	 		showAlertModal("danger", "${msg_afterSelect}");
		}else{
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: "${updateOpenTimeUrl}",
				data: JSON.stringify(data),
				success: function (data) {

					 $("#moOpenHours").html(data.startTime+" ~ "+data.endTime);
					 $("#oder_posible_Time").html(data.oderPosibleTimeFomat);
					 $("#settingTime").hide();
					 $("#nowOpenTime").show();		 
					 
					 $("#oder_time_div").show();
					 $("#rsvpTimeDiv").show();
					 $("#oder_setting_Time").val("00:00");
					 
					$('input[name="24Hour"]').prop('checked', data.openHour24);
					hour24 = data.openHour24;
					 if(hour24){
						$("#moOpenHours").text("24시간영업");
						var compareOpne = $("input[name='openTypeSwitch']").prop('checked');
						if(!compareOpne)
							$("input[name='openTypeSwitch']").bootstrapToggle('on');
					 }else{
						if( nextDayClose ){
							if((nowTime-compareTime(endTime) < 0) && (nowTime-compareTime(startTime) < 0)){
								custom = false;
								storeOpened = true;
								$("input[name='openTypeSwitch']").bootstrapToggle("on");
							}else if((nowTime-compareTime(endTime) > 0) && (nowTime-compareTime(startTime) > 0)){
								custom = false;
								storeOpened = true;
								$("input[name='openTypeSwitch']").bootstrapToggle("on");
							}else{
								custom = false;
								storeOpened = false;
								$("input[name='openTypeSwitch']").bootstrapToggle("off");
							}
						}else{
							if((nowTime-compareTime(startTime) > 0) && (nowTime-compareTime(endTime) < 0)){
								custom = false;
								storeOpened = true;
								$("input[name='openTypeSwitch']").bootstrapToggle("on");
							}else{
								custom = false;
								storeOpened = false;
								$("input[name='openTypeSwitch']").bootstrapToggle("off");
							}
						}
					 }
	
					 $("#nextDayClose").prop("checked", data.nextDayClose);
					 
					 oderPossibleCheck = data.oderPossibleCheck;
					 switch (oderPossibleCheck) {
						case "O":
							orderSetting("on", "");
							setRadio();
							$("input[name='moSwitch']").bootstrapToggle('on');
							break;
						case "C":
							orderSetting("off", "");
							setRadio();
							$("input[name='moSwitch']").bootstrapToggle('off');
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
							$("input[name='moSwitch']").bootstrapToggle('on');
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
					 
				 showAlertModal("success", "${msg_updateComplete}");
				 
				 clcuRsvpTime(rsvpTime);
			},
			error: ajaxSaveError
		});
	}
}


function hasAllowedString(str) {

	if (str == "") {
		showAlertModal("danger", "${msg_blankName}");
		return false;
	} else if (str.indexOf("'") > -1 || str.indexOf("\"") > -1) {
		showAlertModal("danger", "${msg_noQuot}");
		return false;
	}
	
	return true;
}

function updateTime() {
	 $("#settingTime").show();
	 $("#nowOpenTime").hide();
	 $("#oder_time_div").hide();
	 $("#rsvpTimeDiv").hide();
}
	  
function cancel() {
	$("#settingTime").hide();
	$("#nowOpenTime").show();
	$("#oder_time_div").show();
	$("#rsvpTimeDiv").show();
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
					$("#oder_posible_Time").html("언제나 가능");	
					$("input:radio[name='oder_setting_Time']:radio[value='on']").prop('checked', true);
										break;
				case "off":
					$("#oder_posible_Time").html("주문 불가능");	
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



function clcuRsvpTime(rsvpTimeList){
	var rsvpTimeViewVal = "";
	if(rsvpTimeList != null && rsvpTimeList != ""){
		var time;
		if(rsvpTimeList.length > 0){
			for(var i=0; i < rsvpTimeList.length; i++){
				var time = rsvpTimeList[i];
				if(time != ""){
				    var total =  "+" + time + "${labal_minute}";
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
			$("span[name=rsvpTimeView]").html("");
			$("#rsvpTimeDiv").hide();
		}
	}else{
		$("#rsvpTimeDiv").hide();
	}
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

