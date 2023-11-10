<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/storesetting/create" var="createUrl" />
<c:url value="/pay/storesetting/read" var="readUrl" />
<c:url value="/pay/storesetting/readAd" var="readAdUrl" />
<c:url value="/pay/storesetting/update" var="updateUrl" />
<c:url value="/pay/storesetting/updateMenu" var="updateMenuUrl" />
<c:url value="/pay/storesetting/destroy" var="destroyUrl" />

<c:url value="/pay/storesetting/upcmpl" var="uploadCmplUrl" />


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

<!--  Scripts -->

<script>

$(document).ready(function() {

	// Select store
	$("#sel-store-btn").click(function(e) {
		e.preventDefault();
		
		location.href = "/pay/selectstore/check?uri=" + window.location.pathname;
	});
	// / Select store
	
});

</script>

<!--  / Scripts -->





<!-- Page body -->


<!-- Page scripts  -->

<link rel="stylesheet" href="/resources/vendor/lib/smartwizard/smartwizard.css">

<script src="/resources/vendor/lib/smartwizard/smartwizard.js"></script>


<!-- Page message  -->

<c:if test="${not empty notifMsg}">

	<div class="alert alert-info alert-dismissible fade show">
		<button type="button" class="close" data-dismiss="alert">×</button>
		${notifMsg}
	</div>

</c:if>

<!-- / Page message  -->


<!--  Overview -->

<div class="card mb-3">
	<h6 class="card-header with-elements py-2">
		<span>${ovwStoreName}</span>
		<span class="d-none d-sm-inline">
			<span class='px-2'>•</span>
			<span>${ovwShortName}</span>
		</span>
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

<ul class="nav nav-tabs tabs-alt mb-4">
	<li class="nav-item">
		<a class="nav-link active" data-toggle="tab" href="#kiosk-order" id="K">
			<i class="mr-1 fas fa-hamburger"></i>
			${tab_kiosk}
		</a>
	</li>
	<li class="nav-item">
		<a class="nav-link" data-toggle="tab" href="#mobile-order" id="M">
			<i class="mr-1 fas fa-wifi"></i>
			${tab_mobileOrder}
		</a>
	</li>
	<li class="nav-item">
		<a class="nav-link" data-toggle="tab" href="#ad-order" id="A">
			<i class="mr-1 fas fa-video"></i>
			광고 설정
		</a>
	</li>
</ul>

<div class="tab-content">
	<div class="tab-pane active" id="kiosk-order">
		<div class="card">
			<div class="card-body">
				<div class="pb-2 mb-2">
					${title_kioskLogo}
					<span class="small text-muted pl-3">${desc_kioskLogo}</span>
				</div>
				
				<div class="row no-gutters row-bordered text-center">
					<div class="d-flex col-md flex-column pt-3 pb-4">

						<div class="d-flex align-items-center justify-content-center h5 mb-0">
							<span class="text-muted">
								<svg class="item-icon">
									<use xlink:href="/resources/shared/other-icon.svg#image"></use>
								</svg>
							</span>
							<span class="ml-2 font-weight-light">${label_image}</span>
						</div>
                  
						<div class="form-group px-4 mt-3">
							<div class="input-group">
								<span class="input-group-prepend">
									<button class="btn btn-secondary" type="button" onclick="uploadImage('K')">
										<span class="fas fa-cloud-upload-alt"></span>
									</button>
								</span>
								<input name="imgFilename" type="text" class="form-control" readonly="readonly">
								<span class="input-group-append">
									<button class="btn btn-secondary" type="button" onclick="viewImg('K')">
										<span class="fas fa-search"></span>
									</button>
								</span>
							</div>
						</div>

						<div class="px-md-3 px-lg-5">
							<a name="imgBtn" href="javascript:selectRadioBtn('K', 'I')" class="btn btn-outline-primary btn-lg rounded-pill">
								<span name="imgSelected" style="display: none;">
									<span class="fas fa-check"></span>
									<span class="ml-2">${tip_selected}</span>
								</span>
								<span name="imgUnselected">${tip_select}</span>
							</a>
						</div>
					</div>

					<div class="d-flex col-md flex-column pt-3 pb-4">

						<div class="d-flex align-items-center justify-content-center h5 mb-0">
							<span class="text-muted">
								<svg class="item-icon">
									<use xlink:href="/resources/shared/other-icon.svg#text"></use>
								</svg>
							</span>
							<span class="ml-2 font-weight-light">${label_text}</span>
						</div>

						<div class="px-4 mb-3 mt-3">
							<input name="txtLogo" type="text" maxlength="25" class="form-control">
						</div>
                  
						<div class="px-md-3 px-lg-5">
							<a name="txtBtn" href="javascript:selectRadioBtn('K', 'T')" class="btn btn-outline-primary btn-lg rounded-pill">
								<span name="txtSelected" style="display: none;">
									<span class="fas fa-check"></span>
									<span class="ml-2">${tip_selected}</span>
								</span>
								<span name="txtUnselected">${tip_select}</span>
							</a>
						</div>
					</div>
				</div>				
			</div>
		</div>
	</div>
	<div class="tab-pane" id="ad-order">
		<div class="card">
			<div class="card-body">
				<div class="pb-2 mb-2">
					광고 업로드
				</div>
				<div class="row no-gutters row-bordered text-center">
					<div class="d-flex col-md flex-column pt-3 pb-4">
				<div class="clearfix">
						<div class="float-left">
	         	         <button type="button" class="btn btn-primary" onclick="addRow()" >메뉴 추가</button>
	                  </div>
						<div class="float-right">
	               		   <button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
	                  </div>
                  </div>
						<div class="form-group px-4 mt-3">
							<div class="input-group">
								<span class="input-group-prepend">
									<button class="btn btn-secondary" type="button" onclick="uploadImage('V')">
										<span class="fas fa-cloud-upload-alt"></span>
									</button>
								</span>
								<input name="adFilename" type="text" class="form-control" readonly="readonly">
								<span class="input-group-append">
								</span>
							</div>
						</div>	


					<table class="table" id ="table_tb">
						<thead>
							<tr>
								<th scope="col">번호</th>
								<th scope="col" style="display:none;">파일명</th>
								<th scope="col">원본 파일명</th>
								<th scope="col">생성 날짜</th>
							</tr>
						</thead>
							<tbody>
						<c:forEach items="${adList }" var="ad">

							<tr id="${ad.fileName }" class="clickableRow">
								<td id="index"><c:out value="${ad.index }"></c:out></td>
								<td id="fileName" style="display:none;"><c:out value="${ad.fileName }"></c:out></td>
								<td id="orgFilename"><c:out value="${ad.orgFilename }"></c:out></td>
								<td id="createDate"><c:out value="${ad.createDate }"></c:out></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
					
					</div>
				</div>				
			</div>
		</div>
	</div>
	<div class="tab-pane" id="mobile-order">
		<div class="card">
			<div class="card-body">
				<div class="pb-2 mb-2">
					${title_mobileLogo}
					<span class="small text-muted pl-3">${desc_mobileLogo}</span>
				</div>
				
				<div class="row no-gutters row-bordered text-center">
					<div class="d-flex col-md flex-column pt-3 pb-4">

						<div class="d-flex align-items-center justify-content-center h5 mb-0">
							<span class="text-muted">
								<svg class="item-icon">
									<use xlink:href="/resources/shared/other-icon.svg#image"></use>
								</svg>
							</span>
							<span class="ml-2 font-weight-light">${label_image}</span>
						</div>
                  
						<div class="form-group px-4 mt-3">
							<div class="input-group">
								<span class="input-group-prepend">
									<button class="btn btn-secondary" type="button" onclick="uploadImage('M')">
										<span class="fas fa-cloud-upload-alt"></span>
									</button>
								</span>
								<input name="imgFilename" type="text" class="form-control" readonly="readonly">
								<span class="input-group-append">
									<button class="btn btn-secondary" type="button" onclick="viewImg('M')">
										<span class="fas fa-search"></span>
									</button>
								</span>
							</div>
						</div>

						<div class="px-md-3 px-lg-5">
							<a name="imgBtn" href="javascript:selectRadioBtn('M', 'I')" class="btn btn-outline-primary btn-lg rounded-pill">
								<span name="imgSelected" style="display: none;">
									<span class="fas fa-check"></span>
									<span class="ml-2">${tip_selected}</span>
								</span>
								<span name="imgUnselected">${tip_select}</span>
							</a>
						</div>
					</div>

					<div class="d-flex col-md flex-column pt-3 pb-4">

						<div class="d-flex align-items-center justify-content-center h5 mb-0">
							<span class="text-muted">
								<svg class="item-icon">
									<use xlink:href="/resources/shared/other-icon.svg#text"></use>
								</svg>
							</span>
							<span class="ml-2 font-weight-light">${label_text}</span>
						</div>

						<div class="px-4 mb-3 mt-3">
							<input name="txtLogo" type="text" maxlength="25" class="form-control">
						</div>
                  
						<div class="px-md-3 px-lg-5">
							<a name="txtBtn" href="javascript:selectRadioBtn('M', 'T')" class="btn btn-outline-primary btn-lg rounded-pill">
								<span name="txtSelected" style="display: none;">
									<span class="fas fa-check"></span>
									<span class="ml-2">${tip_selected}</span>
								</span>
								<span name="txtUnselected">${tip_select}</span>
							</a>
						</div>
					</div>
				</div>				
				
			</div>
			<hr class="m-0" />
			<div class="card-body">
				<div class="pb-2">
					${title_mobileOrder}
					<span class="small text-muted pl-3">${desc_mobileOrder}</span>
				</div>
				<select name="mobileOrderType" class="selectpicker col-sm-3 px-0" data-style="btn-default" data-none-selected-text="">
					<option value="type4">매장</option>
					<option value="type1">${type1_mobileOrder}</option>
					<option value="type2">${type2_mobileOrder}</option>
					<option value="type3">${type3_mobileOrder}</option>
				</select>
			</div>
		</div>
	</div>
</div>


<div class="text-right my-3">
	<button id="save-btn" type="button" class="btn btn-primary">${form_save}</button>
</div>


<!--  Root form container -->
<div id="formRoot"></div>


<style>

.item-icon {
	width: 64px; height: 64px; fill: currentColor;
}

</style>	

<!--  / Forms -->


<!-- Smart wizard -->

<script id="sw-template" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="wizard-modal">
    <div class="modal-dialog">
        <form class="modal-content" id="wizard-form">
            <div class="modal-header move-cursor"">
				<h5 class="modal-title">
					<span name="form-title"></span>
				</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">

				<div name="smartWizard">
					<ul>
						<li name="tab-0">
							<a href="\\\#sw-step-1" class="mb-3">
								<span class="sw-done-icon"><span class="fas fa-check"></span></span>
								<span class="sw-icon"><span class="fas fa-cloud-upload-alt"></span></span>
								${label_tab0}
								<div class="text-muted small">${label_tabDesc0}</div>
							</a>
						</li>
						<li name="tab-1">
							<a href="\\\#sw-step-2" class="mb-3">
								<span class="sw-done-icon"><span class="fas fa-check"></span></span>
								<span class="sw-icon"><span class="fas fa-pencil-alt"></span></span>
								${label_tab1}
								<div class="text-muted small">${label_tabDesc1}</div>
							</a>
						</li>
					</ul>

					<div class="mb-3">
						<div id="sw-step-1" class="card animated fadeIn">
							<div class="card-body">
								<div class="drop-zone upload-root-div">
									<input name="files" type="file" /><br>
									<p style="color: red;"> ${msg_imgSize}</p>
								</div>
							</div>
						</div>
						<div id="sw-step-2" class="card animated fadeIn">
							<div class="card-body">
								<div class="form-group col">
									<label class="form-label">
										${label_image}
									</label>
									<div class="form-group">
										<div class="input-group">
											<input name="uploadImgFilename" type="text" class="form-control" readonly="readonly">
											<span class="input-group-append">
												<button class="btn btn-secondary" type="button" onclick="viewTempImg()">
													<span class="fas fa-search"></span>
												</button>
											</span>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button name="confirm-btn" type="button" class="btn btn-primary disabled" onclick='confirmWizardForm()'>${confirm_ok}</button>
			</div>
        </form>
    </div>
</div>

</script>

<style>

.upload-root-div {
	height: 200px;
}

.k-upload-` {
	height: 100px;
	overflow-x: hidden;
	overflow-y: hidden;
}

strong.k-upload-status.k-upload-status-total { font-weight: 500; color: #2e2e2e; }

div.k-dropzone.k-dropzone-hovered em, div.k-dropzone em { color: #2e2e2e; }

.k-upload .k-upload-files ~ .k-button {
	width: 48%;
	margin: 3px 3px 0.25em 1%;
}

.k-upload .k-button {
	height: 38px;
	border-radius: .25rem;
}

.k-upload .k-upload-button {
	border-color: transparent;
	background: #8897AA;
	color: #fff;
}

.k-upload .k-upload-button:hover {
	background: #818fa2;
}

.k-upload .k-upload-files ~ .k-upload-selected {
	border-color: transparent;
	background: #4c84ff;
	color: #fff;
}

.k-upload .k-upload-files ~ .k-upload-selected:hover {
	background: #487df2;
}

.k-upload .k-upload-files ~ .k-clear-selected {
	background: transparent;
	color: #4E5155;
	border: 1px solid rgba(24,28,33,0.1);
}

.k-upload .k-upload-files ~ .k-clear-selected:hover {
	background: rgba(24,28,33,0.06);
}
.highlight{
background-color:#D2E0FF;

}
</style>

<!-- / Smart wizard -->


<!--  Scripts -->

<script>
var adSize = [];
var adType = [];
<c:forEach var="ad" items="${adList }" >
adSize.push("${ad.index }");
adType.push("${ad.type }");
</c:forEach>
var tdArr = new Array();	// 배열 선언
var storeId = -1;

var uploadType = "";

var rows = "";
var debugStr = "";
var kLogoType = "";
var mLogoType = "";

var vPreviewFile = "/ad/${storeRoot}/";
var kPreviewFile = "/titles/${storeRoot}/";
var mPreviewFile = "/m/titles/${storeRoot}/";

var kFilename = "";
var vFilename = "";
var vFilenameList = [];
var List = [];

var mFilename = "";

var objImgSel = null;
var objImgUnsel = null;
var objTxtSel = null;
var objTxtUnsel = null;
var objImgBtn = null;
var objTxtBtn = null;

function changeRadioButtons(type, val) {
	
	if (type == "S") {
		if (val == "I") {
			objImgSel.show();
			objImgUnsel.hide();
			objTxtSel.hide();
			objTxtUnsel.show();
			
			objImgBtn.removeClass("btn-outline-primary").addClass("btn-primary");
			objTxtBtn.removeClass("btn-primary").addClass("btn-outline-primary");
		} else if (val == "T") {
			objImgSel.hide();
			objImgUnsel.show();
			objTxtSel.show();
			objTxtUnsel.hide();
			
			objImgBtn.removeClass("btn-primary").addClass("btn-outline-primary");
			objTxtBtn.removeClass("btn-outline-primary").addClass("btn-primary");
		}
	} else if (type == "U") {
		if (val == "I") {
			objImgSel.hide();
			objImgUnsel.show();
			
			objImgBtn.removeClass("btn-primary").addClass("btn-outline-primary");
		} else if (val == "T") {
			objTxtSel.hide();
			objTxtUnsel.show();
			
			objTxtBtn.removeClass("btn-primary").addClass("btn-outline-primary");
		}
	}
}


function selectRadioBtn(type, val) {
	
	var selType = "";
	
	var kImgFilename = $.trim($("#kiosk-order input[name='imgFilename']").val());
	var kLogoText = $.trim($("#kiosk-order input[name='txtLogo']").val());
	var kadFilename = $.trim($("#ad-order input[name='adFilename']").val());
	var mImgFilename = $.trim($("#mobile-order input[name='imgFilename']").val());
	var mLogoText = $.trim($("#mobile-order input[name='txtLogo']").val());
	
	var goAhead = false;
	if (type == "K") {
		objImgSel = $("#kiosk-order span[name='imgSelected']");
		objImgUnsel = $("#kiosk-order span[name='imgUnselected']");
		objTxtSel = $("#kiosk-order span[name='txtSelected']");
		objTxtUnsel = $("#kiosk-order span[name='txtUnselected']");
		objImgBtn = $("#kiosk-order a[name='imgBtn']");
		objTxtBtn = $("#kiosk-order a[name='txtBtn']");
		
		if (val == "I") {
			if (kImgFilename) {
				goAhead = true;
				
				if (kLogoType == "I") {
					kLogoType = "";
					selType = "U";
				} else {
					kLogoType = "I";
					selType = "S";
				}
			} else {
				if (kLogoType == "I") {
					kLogoType = "";
				}
				changeRadioButtons("U", val);
				showAlertModal("danger", "${msg_uploadFirst}");
			}
		} else if (val == "T") {
			if (kLogoText) {
				goAhead = true;
				
				if (kLogoType == "T") {
					kLogoType = "";
					selType = "U";
				} else {
					kLogoType = "T";
					selType = "S";
				}
			} else {
				if (kLogoType == "T") {
					kLogoType = "";
				}
				changeRadioButtons("U", val);
				showAlertModal("danger", "${msg_inputFirst}");
			}
		}
		
		if (goAhead) {
			changeRadioButtons(selType, val);
		}
	} else if (type == "M") {
		objImgSel = $("#mobile-order span[name='imgSelected']");
		objImgUnsel = $("#mobile-order span[name='imgUnselected']");
		objTxtSel = $("#mobile-order span[name='txtSelected']");
		objTxtUnsel = $("#mobile-order span[name='txtUnselected']");
		objImgBtn = $("#mobile-order a[name='imgBtn']");
		objTxtBtn = $("#mobile-order a[name='txtBtn']");
		
		if (val == "I") {
			if (mImgFilename) {
				goAhead = true;
				
				if (mLogoType == "I") {
					mLogoType = "";
					selType = "U";
				} else {
					mLogoType = "I";
					selType = "S";
				}
			} else {
				if (mLogoType == "I") {
					mLogoType = "";
				}
				changeRadioButtons("U", val);
				showAlertModal("danger", "${msg_uploadFirst}");
			}
		} else if (val == "T") {
			if (mLogoText) {
				goAhead = true;
				
				if (mLogoType == "T") {
					mLogoType = "";
					selType = "U";
				} else {
					mLogoType = "T";
					selType = "S";
				}
			} else {
				if (mLogoType == "T") {
					mLogoType = "";
				}
				changeRadioButtons("U", val);
				showAlertModal("danger", "${msg_inputFirst}");
			}
		}
		
		if (goAhead) {
			changeRadioButtons(selType, val);
		}
	}
}


function initSmartWizard() {
	
	$("#formRoot").html(kendo.template($("#sw-template").html()));
	
	$("#wizard-form div[name='smartWizard']").smartWizard({
		showStepURLhash: false,
		toolbarSettings: {
			showNextButton: false,
			showPreviousButton: false,
		}
	});

	$("#wizard-form input[name='files']").kendoUpload({
		multiple: true,
		async: {
			saveUrl: "${uploadModel.saveUrl}",
			autoUpload: false
		},
		localization: {
			cancel: "${label_cancel}",
			dropFilesHere: "${label_dropFilesHere}",
			headerStatusUploaded: "${label_headerStatusUploaded}",
			headerStatusUploading: "${label_headerStatusUploading}",
			remove: "${label_remove}",
			retry: "${label_retry}",
			select: "${label_select}",
			uploadSelectedFiles: "${label_uploadSelectedFiles}",
			clearSelectedFiles: "${label_clearSelectedFiles}",
			invalidFileExtension: "${label_invalidFileExtension}",
		},
		dropZone: ".drop-zone",
		upload: function(e) {
			e.data = {
				storeId: ${uploadModel.storeId},
				type: "${uploadModel.type}",
			};
		},
		success: function(e) {
			$("#wizard-form button[name='confirm-btn']").removeClass("disabled");
			for (var i = 0; i < e.files.length; i++) {
				var iname = e.files[i].name
				vFilenameList.push(iname);
				$("#wizard-form input[name='uploadImgFilename']").val(vFilenameList);
			}
			
			
			$("#wizard-form div[name='smartWizard']").smartWizard("next");
		},
		validation: {
			allowedExtensions: ${uploadModel.allowedExtensions}
		},
	});
}


function viewImg(type) {
	
	if (type == "K") {
		var filename = $("#kiosk-order input[name='imgFilename']").val();
		if (filename) {
			viewUnivImage(kPreviewFile);
		}
	} else if (type == "M") {
		var filename = $("#mobile-order input[name='imgFilename']").val();
		if (filename) {
			viewUnivImage(mPreviewFile);
		}
	}else if (type == "V") {
		var filename = $("#ad-order input[name='adFilename']").val();
		if (filename) {
			viewUnivImage(vPreviewFile);
		}
	}
}


function viewTempImg() {
	
	var filename = $("#wizard-form input[name='uploadImgFilename']").val();
	var url = "/uptemp/" + filename;
	
	viewUnivImage(url);
}


function uploadImage(type) {

	initSmartWizard();
	uploadType = type;
	
	if (type == "K") {
		$("#wizard-form span[name='form-title']").html("${wizard_kioskTitle}");
	} else if (type == "M") {
		$("#wizard-form span[name='form-title']").html("${wizard_mobileTitle}");
	} else if (type == "V") {
		$("#wizard-form span[name='form-title']").html("광고파일 업로드");
	}
	
	$('#wizard-modal .modal-dialog').draggable({ handle: '.modal-header' });
	$("#wizard-modal").modal();
}


function confirmWizardForm() {

	var uploadedFilename = $("#wizard-form input[name='uploadImgFilename']").val();

	if (uploadedFilename) {
		if ($("#wizard-form li[name='tab-0']").hasClass("active")) {
			$("#wizard-form div[name='smartWizard']").smartWizard("next");
		}
		
		if ($("#wizard-form").valid()) {
	    	var data = {
	    		uploadedFilename: uploadedFilename,
	    		uploadType: uploadType,
	       	};
	        
			$.ajax({
    			type: "POST",
    			contentType: "application/json",
    			dataType: "json",
    			url: "${uploadCmplUrl}",
    			data: JSON.stringify(data),
    			success: function (form) {
    				$("#wizard-modal").modal("hide");
    				
    				if (uploadType == "K") {
        				$("#kiosk-order input[name='imgFilename']").val(uploadedFilename);
        				kPreviewFile = "/uptemp/" + form;
    					kFilename = form;
    				} else if (uploadType == "M") {
        				$("#mobile-order input[name='imgFilename']").val(uploadedFilename);
        				mPreviewFile = "/uptemp/" + form;
    					mFilename = form;
    				} else if (uploadType == "V") {
        				$("#ad-order input[name='adFilename']").val(uploadedFilename);
        				vPreviewFile = "/uptemp/" + form;
        				vFilename = form;
    				}
    			},
    			error: ajaxSaveError
    		});
		}
	}
}

function checkType() {
	var num = adSize.length-1;
	var type = adType[num]
	console.log(type);
	console.log(adType[num]);
	
	return type == "M";
	

}

function addRow() {
		
		if(checkType()) {
			alert("메뉴 다음에 메뉴가 올 수 없습니다.");
			return;
		}
	
	  // table element 찾기
	  var table = document.getElementById('table_tb');
	  var curDate = new Date();

	  var curTime = curDate.getFullYear() + "-" + (curDate.getMonth() + 1) + "-" + curDate.getDate() + " " + curDate.getHours() + ":" + curDate.getMinutes() + ":" + curDate.getSeconds();
	  // 새 행(Row) 추가
	  var newRow = table.insertRow();
	  // 새 행(Row)에 Cell 추가
	  var newCell1 = newRow.insertCell(0);
	  var newCell2 = newRow.insertCell(1);
	  var newCell3 = newRow.insertCell(2);
	  var type = "M";
	  
	  // Cell에 텍스트 추가
	  newCell1.innerText = adSize.length+1;
	  newCell2.innerText = '메뉴판';
	  newCell3.innerText = curTime;
	  
  	var data = {
    		id: storeId,
    		menu: '메뉴판' + adSize.length,
    		type: type,
    		createDate: curTime,

   		};
    	
    	$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${updateMenuUrl}",
			data: JSON.stringify(data),
			success: function (form) {
				window.location.reload();
			},
			error: ajaxSaveError
		});
		
}

$(document).ready(function() {
	
	$("#table_tb").tableDnD({
		onDragClass: "myDragClass",
	    onDrop: function(table, row) {
            rows = table.tBodies[0].rows;
            debugStr = "";
            for (var i=0; i<rows.length; i++) {
                debugStr += rows[i].id+",";
            }
            console.log(rows);
            console.log(debugStr);
	    },

	});

	 
	$("#table_tb tr").click(function(){ 
		
// 	      $(".clickableRow").on("click",function(){
		       $(".highlight").removeClass("highlight");
		      $(this).addClass("highlight");
		      
// 		      });

		var str = ""
			tdArr = new Array();
		
		// 현재 클릭된 Row(<tr>)
		var tr = $(this);
		var td = tr.children();
		
		// tr.text()는 클릭된 Row 즉 tr에 있는 모든 값을 가져온다.
		console.log("클릭한 Row의 모든 데이터 : "+tr.text());
		
		// 반복문을 이용해서 배열에 값을 담아 사용할 수 도 있다.
		td.each(function(i){
			
			tdArr.push(td.eq(i).text());
		});
		
		console.log("배열에 담긴 값 : "+tdArr);
		
		// td.eq(index)를 통해 값을 가져올 수도 있다.
		var no = td.eq(0).text();
		var userid = td.eq(1).text();
		var name = td.eq(2).text();
		var email = td.eq(3).text();
		
		
		str +=	" * 클릭된 Row의 td값 = No. : <font color='red'>" + no + "</font>" +
				", 아이디 : <font color='red'>" + userid + "</font>" +
				", 이름 : <font color='red'>" + name + "</font>" +
				", 이메일 : <font color='red'>" + email + "</font>";
	});


<c:if test="${not empty sessionScope['currentStoreId']}">
	storeId = ${sessionScope['currentStoreId']};
</c:if>
	
	$("#mobile-order select[name='mobileOrderType']").selectpicker('render');
	bootstrapSelectVal($("#mobile-order select[name='mobileOrderType']"), "type2");
	
	$("#save-btn").click(function(e) {
		
    	var data = {
    		id: storeId,
    		kLogoType: kLogoType,
    		rows: rows,
    		debugStr: debugStr,
    		kLogoImage: $.trim($("#kiosk-order input[name='imgFilename']").val()),
    		kAd: $.trim($("#ad-order input[name='adFilename']").val()),
    		kLogoUniqueName: kFilename,
    		vLogoUniqueName: $.trim(vFilename),
			kLogoText: $.trim($("#kiosk-order input[name='txtLogo']").val()),
    		mLogoType: mLogoType,
    		mLogoImage: $.trim($("#mobile-order input[name='imgFilename']").val()),
    		mLogoUniqueName: mFilename,
			mLogoText: $.trim($("#mobile-order input[name='txtLogo']").val()),
			mType: $("#mobile-order select[name='mobileOrderType']").val()
   		};
    	
    	$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${updateUrl}",
			data: JSON.stringify(data),
			success: function (form) {
// 				bootbox.alert('${msg_updateComplete}', function() {

//                     });
				showSaveSuccessMsg();
				window.location.reload();
				kPreviewFile = "/titles/${storeRoot}/" + kFilename;
				vPreviewFile = "/ad/${storeRoot}/" + vFilename;
				mPreviewFile = "/m/titles/${storeRoot}/" + mFilename;
			},
			error: ajaxSaveError
		});
	});


	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readUrl}",
		success: function (data, status) {

			$("#kiosk-order input[name='txtLogo']").val(data.kioskLogoText);
			if (data.kioskLogoImage) {
				$("#kiosk-order input[name='imgFilename']").val(data.kioskLogoImage.orgFilename);
				
				kFilename = data.kioskLogoImage.filename;
				if (data.kioskLogoImage.orgFilename) {
					kPreviewFile = "/titles/${storeRoot}/" + kFilename;
				}
			}
			if (data.kioskLogoType) {
				selectRadioBtn('K', data.kioskLogoType);
			}

			
			$("#mobile-order input[name='txtLogo']").val(data.mobileLogoText);
			if (data.mobileLogoImage) {
				$("#mobile-order input[name='imgFilename']").val(data.mobileLogoImage.orgFilename);
				
				mFilename = data.mobileLogoImage.filename;
				if (data.mobileLogoImage.orgFilename) {
					mPreviewFile = "/m/titles/${storeRoot}/" + mFilename;
				}
			}
			if (data.mobileLogoType) {
				selectRadioBtn('M', data.mobileLogoType);
			}
			
			bootstrapSelectVal($("select[name='mobileOrderType']"), data.orderType);
		},
		error: ajaxReadError
	});
	
	$("#delete-btn").click(function(e) {
		e.preventDefault();
			var fileName = tdArr[1];
			showDelConfirmModal(function(result) {
				if (result) {
					$.ajax({
						type: "POST",
						contentType: "application/json",
						dataType: "json",
						url: "${destroyUrl}",
						data: JSON.stringify({fileName}),
						success: function (form) {
        					showDeleteSuccessMsg();
							window.location.reload();
						},
						error: ajaxDeleteError
					});
				}
			}, true);
		
	});
	
	
});


</script>

<!--  / Scripts -->

<!-- / Page body -->





<!-- Functional tags -->

<func:cmmImgLightBox />
<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
