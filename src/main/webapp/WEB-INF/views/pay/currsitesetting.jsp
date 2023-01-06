<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/currsitesetting/read" var="readUrl" />
<c:url value="/pay/currsitesetting/update" var="updateUrl" />


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold pt-1 pb-3 mb-3">
	<span class="mr-1 ${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
</h4>





<!-- Page body -->


<!-- Java(optional)  -->

<%
	String allSiteValue = "<span class='fas fa-times text-light'></span>";
	String currSiteValue = "<span class='fas fa-times text-light'></span>";
	
	Boolean globalCustLogoDisplayed = (Boolean) request.getAttribute("globalCustLogoDisplayed");
	Boolean currCustLogoDisplayed = (Boolean) request.getAttribute("currCustLogoDisplayed");

	if (globalCustLogoDisplayed != null && globalCustLogoDisplayed.booleanValue()) {
		allSiteValue = "<span class='fas fa-check text-primary'></span>";
	}
	if (currCustLogoDisplayed != null && currCustLogoDisplayed.booleanValue()) {
		currSiteValue = "<span class='fas fa-check text-primary'></span>";
	}
%>


<!--  Forms -->

<ul class="nav nav-tabs tabs-alt container-p-x container-m--x mb-4">
	<li class="nav-item">
		<a class="nav-link active" data-toggle="tab" href="#basic-info">
			<i class="mr-1 fas fa-check"></i>
			${tab_general}
		</a>
	</li>
	<li class="nav-item">
		<a class="nav-link" data-toggle="tab" href="#logo-loc">
			<i class="mr-1 fas fa-location-arrow"></i>
			${tab_location}
		</a>
	</li>
</ul>

<div class="tab-content">
	<div class="tab-pane active" id="basic-info">
		<div class="card">
			<div class="card-body">
				<div class="pb-2">
					${title_autoSiteUser}
					<span class="small text-muted pl-3">${desc_autoSiteUser}</span>
				</div>
				<label class="switcher switcher-lg">
					<input type="checkbox" class="switcher-input" name="auto-site-user-switch" checked="checked">
					<span class="switcher-indicator"></span>
				</label>
			</div>
			<hr class="m-0" />
			<div class="card-body">
				<div class="pb-2">
					${title_maxQuickLinkMenu}
					<span class="small text-muted pl-3">${desc_maxQuickLinkMenu}</span>
				</div>
				<select name="maxMenuCount" class="selectpicker col-sm-3 px-0" data-style="btn-default" data-none-selected-text="">
					<option value="0">0</option>
					<option value="5">5</option>
					<option value="7">7</option>
					<option value="10">10</option>
					<option value="15">15</option>
					<option value="20">20</option>
				</select>
			</div>
		</div>
	</div>
	<div class="tab-pane" id="logo-loc">
		<div class="card">
			<div class="card-body">
				<div class="pb-2">
					${title_siteLogo}
					<span class="small text-muted pl-3">${desc_siteLogo}</span>
				</div>
				<hr class="border-light m-0 mt-2">
				<div class="table-responsive">
					<table class="table card-table m-0">
						<tbody>
							<tr>
								<th class="small"></th>
								<th class="small">${label_allSite}</th>
								<th class="small">${label_currSite}</th>
							</tr>
							<tr>
								<td class="small">${label_currSetting}</td>
								<td><%= allSiteValue %></td>
								<td><%= currSiteValue %></td>
							</tr>
							
<c:if test="${requestScope.currCustLogoDisplayed}">

							<tr>
								<td class="small">${label_serviceURL}</td>
								<td></td>
								<td class="small"><samp>${currSiteLogoServiceURL}</samp></td>
							</tr>

</c:if>
							
<c:if test="${requestScope.currCustLogoDisplayed or requestScope.globalCustLogoDisplayed}">
							
							<tr>
								<td class="small">${label_logoFile}</td>
								<td class="small">

<c:if test="${requestScope.globalCustLogoDisplayed}">

									<samp>
										logo_login.png<br>
										logo_top.png
									</samp>

</c:if>
							
								</td>
								<td class="small">

<c:if test="${requestScope.currCustLogoDisplayed}">

									<samp>
										logo_login.${siteShortName}.png<br>
										logo_top.${siteShortName}.png
									</samp>

</c:if>
							
								</td>
							</tr>

</c:if>
							
						</tbody>
					</table>
				</div>
				<hr class="border-light m-0">
				<div class="mt-3">
					<div class="d-flex flex-wrap">
						<div class="mb-3 mr-3" style="width: 302px;">
							<div class="mb-1 ml-1">
								<span class="fas fa-map-marker-alt mr-1"></span>${label_login}
							</div>
							<div style="background: #f8f8f8;">
								<img src="${logoFileLogin}" style="border: solid 1px #e5e5e5;" class="my-5">
							</div>
							<div class="d-flex justify-content-center text-muted">
								<span>300px</span>
								<span class="text-body mx-2">x</span>
								<span>[${tip_changeable}]</span>
							</div>
						</div>

						<div class="mb-3 mr-3" style="width: 15.625rem;">
							<div class="mb-1">
								<span class="fas fa-map-marker-alt mr-1"></span>${label_slide}
							</div>
							<div style="background: #3f4853; height: 60px; display: flex; align-items: center;">
								<span class="app-brand-logo logo-frame ml-4">
									<img src="${logoFileTop}" alt>
								</span>
								<span class="app-brand-text logo-text sidenav-text font-weight-normal ml-2 text-white">
									${logoTitleText}
								</span>
							</div>
							<div class="d-flex justify-content-center text-muted">
								<span>30px</span>
								<span class="text-body mx-2">x</span>
								<span>30px</span>
							</div>
						</div>
					
						<div class="mb-3 mr-3" style="width: 250px;">
							<div class="mb-1">
								<span class="fas fa-map-marker-alt mr-1"></span>${label_top}
							</div>
							<div style="background: #4170d9; height: 60px; display: flex; align-items: center;">
								<span class="app-brand-logo ml-4">
									<img src="${logoTopPathFile}" alt>
								</span>
								<span class="app-brand-text logo-text sidenav-text font-weight-normal ml-2 text-white">
									${logoTitleText}
								</span>
								<div class="ml-4">
									<i class="fas fa-bars fa-lg text-white"></i>
								</div>
							</div>
							<div class="d-flex justify-content-center text-muted">
								<span>30px</span>
								<span class="text-body mx-2">x</span>
								<span>30px</span>
							</div>
						</div>
					
						<div style="width: 250px;">
							<div class="mb-1">
								<span class="fas fa-map-marker-alt mr-1"></span>${label_logoText}
							</div>
							<input name="logoTitle" type="text" maxlength="15" class="form-control">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<div class="text-right mt-3">
	<button id="save-btn" type="button" class="btn btn-primary">${form_save}</button>
</div>

<!--  / Forms -->


<!--  Scripts -->

<script>

$(document).ready(function() {
	
	$("select[name='maxMenuCount']").selectpicker('render');
	
	bootstrapSelectVal($("select[name='maxMenuCount']"), "5");
	
	
	$("#save-btn").click(function(e) {
    	var data = {
			autoSiteUser: $("input[name='auto-site-user-switch']").is(":checked") ? "Y" : "N",
			maxMenuCount: $("select[name='maxMenuCount']").val(),
			logoTitle: $.trim($("input[name='logoTitle']").val()),
		};

    	$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${updateUrl}",
			data: JSON.stringify(data),
			success: function (form) {
				showAlertModal("success", "${msg_updateComplete}");
			},
			error: ajaxSaveError
		});
	});

	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readUrl}",
		data: JSON.stringify({ }),
		success: function (data, status) {
			var locLat = null;
			var locLng = null;
			
			for(var i in data) {
				if (data[i].name == "auto.siteUser") {
					$("input[name='auto-site-user-switch']").prop("checked", data[i].value == "Y");
				} else if (data[i].name == "quicklink.max.menu") {
					bootstrapSelectVal($("select[name='maxMenuCount']"), data[i].value);
				} else if (data[i].name == "logo.title") {
					$("input[name='logoTitle']").val(data[i].value);
				}
			}
		},
		error: ajaxReadError
	});
});

</script>

<!--  / Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
