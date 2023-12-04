<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/mystoresetting/create" var="createUrl" />
<c:url value="/pay/mystoresetting/read" var="readUrl" />
<c:url value="/pay/mystoresetting/update" var="updateUrl" />

<c:url value="/pay/mystoresetting/upcmpl" var="uploadCmplUrl" />


<c:url value="/pay/mystoresetting/couponRead" var="couponReadUrl" />
<c:url value="/pay/mystoresetting/eventRead" var="eventReadUrl" />
<c:url value="/pay/mystoresetting/couponList" var="couponListUrl" />
<c:url value="/pay/mystoresetting/couponSave" var="couponSaveUrl" />
<c:url value="/pay/mystoresetting/eventSave" var="eventSaveUrl" />
<c:url value="/pay/mystoresetting/couponUpdate" var="couponUpdateUrl" />
<c:url value="/pay/mystoresetting/eventUpdate" var="eventUpdateUrl" />
<c:url value="/pay/mystoresetting/couponDestroy" var="destroyUrl" />
<c:url value="/pay/mystoresetting/eventDestroy" var="eventdestroyUrl" />

<c:url value="/pay/mystoresetting/deliveryPayRead" var="deliveryPayReadUrl" />
<c:url value="/pay/mystoresetting/deliveryPayList" var="deliveryPayListUrl" />
<c:url value="/pay/mystoresetting/deliveryPaySave" var="deliveryPaySaveUrl" />
<c:url value="/pay/mystoresetting/deliveryPayUpdate" var="deliveryPayUpdateUrl" />
<c:url value="/pay/mystoresetting/deliveryPayDestroy" var="deliveryPayDestroyUrl" />

<c:url value="/pay/mystoresetting/policyRead" var="policyRead" />

<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<h4 class="font-weight-bold pt-1 pb-3 mb-3">
	<span class="mr-1 ${sessionScope['loginUser'].icon}"></span>
	${pageTitle}
</h4>





<!-- Page body -->


<!-- Page scripts  -->

<link rel="stylesheet" href="/resources/vendor/lib/smartwizard/smartwizard.css">

<script src="/resources/vendor/lib/smartwizard/smartwizard.js"></script>

<!--  Forms -->

<ul class="nav nav-tabs tabs-alt container-p-x container-m--x mb-4">
	<li class="nav-item">
		<a class="nav-link active" data-toggle="tab" href="#kiosk-order">
			<i class="mr-1 fas fa-hamburger"></i>
			${tab_kiosk}
		</a>
	</li>
	<li class="nav-item">
		<a class="nav-link" data-toggle="tab" href="#mobile-order">
			<i class="mr-1 fas fa-wifi"></i>
			${tab_mobileOrder}
		</a>
	</li>
	<c:choose>
		<c:when test="${'CP' eq savingType}">
	<li class="nav-item">
		</c:when>
		<c:otherwise>
	<li class="nav-item" style="display: none;">
		</c:otherwise>
	</c:choose>
		<a class="nav-link" data-toggle="tab" href="#saving-order">
			<i class="mr-1 fab fa-patreon"></i>
			${title_stamp}/${title_coupon}
		</a>
	</li>
	<c:choose>
		<c:when test="${'PO' eq savingType}">
	<li class="nav-item">
		</c:when>
		<c:otherwise>
	<li class="nav-item" style="display: none;">
		</c:otherwise>
	</c:choose>
		<a class="nav-link" data-toggle="tab" href="#point-order">
			<i class="mr-1 fab fa-patreon"></i>
			${title_point}
		</a>
	</li>
 	<li class="nav-item">
		<a class="nav-link" data-toggle="tab" href="#delivery-order">
			<i class="mr-1 fas fa-hamburger"></i>
			배달
		</a>
	</li>
 	<li class="nav-item">
		<a class="nav-link" data-toggle="tab" href="#discount-order">
			<i class="mr-1 fas fa-percent"></i>
			할인
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
			<hr class="m-0" />
			<div class="card-body">
				<div class="pb-2">
					${title_menuMatrix}
					<span class="small text-muted pl-3">${desc_menuMatrix}</span>
				</div>
				<select name="kioskMenuMatrix" class="selectpicker col-sm-3 px-0" data-style="btn-default" data-none-selected-text="">
					<option value="2x2">2 x 2</option>
					<option value="3x3">3 x 3</option>
					<option value="3x4">3 x 4</option>
					<option value="4x4">4 x 4</option>
				</select>
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
				<label class="switcher switcher-success">
                   	<input id="immediate" name="immediate" type="checkbox" class="switcher-input">
                   	<span class="switcher-indicator">
                     		<span class="switcher-no">
                       		<span class="ion ion-md-close"></span>
                     		</span>
                     		<span class="switcher-yes">
                       		<span class="ion ion-md-checkmark"></span>
                     		</span>
                   	</span>
                   	<span class="switcher-label">${label_immediate}</span>
						<span class="small text-muted pl-3">${msg_immediateMsg}</span>
				</label>
			</div>
		</div>
	</div>
	<div class="tab-pane" id="saving-order">
		<div class="card">
			<div class="card-body">
				<div class="pb-2 mb-2">
					${title_stampReservePolicy}
				</div>
				<div id="stampDiv">
					<div class="form-row pb-2">
						<div class="form-group col-sm-auto mb-auto mt-auto">${label_stampText1}</div>
						<div class="form-group col-sm-1 mb-auto mt-auto">
							<input type="hidden" name="policyId" value="">
							<input type="text" class="form-control numberClass" name="orderAmt" maxlength="3"/>
						</div>
						<div class="form-group col-sm-auto mb-auto mt-auto">${label_stampText2} ${title_stamp}</div>
						<div class="form-group col-sm-1 mb-auto mt-auto">
							<input type="text" class="form-control numberClass" name="reserveAmt" maxlength="3">
						</div>
						<div class="form-group col-sm-auto mb-auto mt-auto">${label_stampText3}</div>
					</div>
				</div>
				<hr class="m-0" />
				
				<div class="pt-2 pb-2 mt-2 mb-2">
					${title_coupon} ${title_couponIssuancePolicy}
				</div>
				<div id="couponDiv">
					<div class="form-row pb-2">
						<div class="form-group col-sm-auto mb-auto mt-auto">${title_stamp}</div>
						<div class="form-group col-sm-1 mb-auto mt-auto">
							<input type="hidden" name="couponPolicyId" value="">
							<input type="text" class="form-control numberClass" name="stampAmt" maxlength="3"/>
						</div>
						<div class="form-group col-sm-auto mb-auto mt-auto">${label_couponText1} ${title_coupon}</div>
						<div class="form-group col-sm-3 mb-auto mt-auto">
							<select name="couponSelect" style="width: 100%;" class="form-control"></select>
						</div>
						<div class="form-group col-sm-auto mb-auto mt-auto">${label_couponText2}</div>
					</div>
				</div>
			</div>
		</div>
		<div class="card">
			<div class="mb-4">
				<%
					String editTemplate = 
					"<button type='button' onclick='edit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>" + 
					"<span class='fas fa-pencil-alt'></span></button>";
				%>
			
				<kendo:grid name="grid" pageable="true" filterable="false" sortable="false" scrollable="false" reorderable="false" resizable="true" selectable="single" >
					<kendo:grid-pageable refresh="false" previousNext="false" numeric="false" pageSize="10000" info="false" />
					<kendo:grid-toolbarTemplate>
				    	<div class="clearfix">
				    		<div class="float-left">
				    			<button id="add-btn" type="button" class="btn btn-outline-success">${cmd_add}</button>
				    		</div>
				    		<div class="float-right">
				    			<button id="delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
				    		</div>
				    	</div>
					</kendo:grid-toolbarTemplate>
					<kendo:grid-columns>
						<kendo:grid-column title="${cmd_edit}" width="50" filterable="false" sortable="false" template="<%= editTemplate %>" />
						<kendo:grid-column title="${label_couponNm}" field="name" filterable="false" />
						<kendo:grid-column title="${label_couponDiscount}${label_couponPrice1}" field="price" filterable="false" minScreenWidth="500" />
						<kendo:grid-column title="${label_couponValidDate}${label_couponValidDate1}" field="validDate" minScreenWidth="600" filterable="false" />
					</kendo:grid-columns>
					<kendo:grid-filterable>
						<kendo:grid-filterable-messages selectedItemsFormat="${filter_selectedItems}"/>
					</kendo:grid-filterable>
					<kendo:dataSource serverPaging="true" serverSorting="true" serverFiltering="true" serverGrouping="true" error="kendoReadError">
						<kendo:dataSource-sort>
							<kendo:dataSource-sortItem field="whoLastUpdateDate" dir="asc"/>
						</kendo:dataSource-sort>
						<kendo:dataSource-filter>
							<kendo:dataSource-filterItem field="store.site.id" operator="eq" logic="and" value="${sessionScope['currentSiteId']}">
								<kendo:dataSource-filterItem field="store.id" operator="eq" logic="and" value="${sessionScope['currentStoreId']}">
								</kendo:dataSource-filterItem>
							</kendo:dataSource-filterItem>
							<kendo:dataSource-filterItem field="deleteState" operator="eq" logic="and" value="0">
							</kendo:dataSource-filterItem>
						</kendo:dataSource-filter>
						<kendo:dataSource-transport>
							<kendo:dataSource-transport-read url="${couponReadUrl}" dataType="json" type="POST" contentType="application/json" />
							<kendo:dataSource-transport-parameterMap>
								<script>
									function parameterMap(options,type) {
										return JSON.stringify(options);	
									}
								</script>
							</kendo:dataSource-transport-parameterMap>
						</kendo:dataSource-transport>
						<kendo:dataSource-schema data="data" total="total" groups="data">
							<kendo:dataSource-schema-model id="id">
							</kendo:dataSource-schema-model>
						</kendo:dataSource-schema>
					</kendo:dataSource>
				</kendo:grid>
			</div>
		</div>
	</div>
	<div class="tab-pane" id="point-order">
		<div class="card">
			<div class="card-body">
				<div class="pb-2 mb-2">
					${title_point} ${title_pointReservePolicy}
				</div>
				<div class="form-row pb-2">
					<div class="form-group col-sm-auto mb-auto mt-auto">${label_pointText1}</div>
					<div class="form-group col-sm-1 mb-auto mt-auto">
						<input type="hidden" name="pointId" value="">
						<input type="text" class="form-control numberClass" name="percentageNumber" maxlength="3"/>
					</div>
					<div class="form-group col-sm-auto mb-auto mt-auto">${label_pointText2}</div>
				</div>
				<hr class="m-0" />
				
				<div class="pt-2 pb-2 mt-2 mb-2">
					${title_point} ${title_pointUsePolicy}
				</div>
				<div class="form-row pb-2">
					<input type="hidden" class="form-control numberClass" name="pointAmt" maxlength="3" value="1"/>
					<div class="form-group col-sm-auto mb-auto mt-auto">${label_pointText3}</div>
				</div>
			</div>
		</div>
	</div>	
	
	<div class="tab-pane" id="delivery-order">
		<div class="card">
			<div class="card-body">
				<div class="pb-2 mb-2">
					최소주문 금액 정책
				</div>
				<div id="minOrderDiv">
					<div class="form-row pb-2">
						<div class="form-group col-sm-auto mb-auto mt-auto">최소 주문금액은  </div>
						<div class="form-group col-sm-1 mb-auto mt-auto">
							<input type="hidden" name="deliveryId" value="">
							<input type="text" class="form-control numberClass" name="minOrderPrice" maxlength="10" value="0"/>
						</div>
						<div class="form-group col-sm-auto mb-auto mt-auto">원 입니다. </div>
					</div>
				</div>
			</div>
		</div>		
		<div class="card">
			<div class="mb-4">
				<div class="pt-2 pb-2 mt-2 mb-2">
					<p style="margin:0 0 0 20px">기본 배달료 정책</p>
				</div>
				<div id="deliveryDiv" style="margin:0 0 20px 20px">
					<div class="form-row pb-2">
						<div class="form-group col-sm-auto mb-auto mt-auto">기본 배달료 </div>
						<div class="form-group col-sm-1 mb-auto mt-auto">
							<input type="hidden" name="deliveryId" value="">
							<input type="text" class="form-control numberClass" name="deliveryPay" maxlength="10" value="0"/>
						</div>
						<div class="form-group col-sm-auto mb-auto mt-auto">원 입니다. </div>
					</div>
				</div>
				
				<%
					String editPayTemplate = 
					"<button type='button' onclick='deliveryEdit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>" + 
					"<span class='fas fa-pencil-alt'></span></button>";
				%>
			
				<kendo:grid name="delivery-grid" pageable="true" filterable="false" sortable="false" scrollable="false" reorderable="false" resizable="true" selectable="single" >
					<kendo:grid-pageable refresh="false" previousNext="false" numeric="false" pageSize="10000" info="false" />
					<kendo:grid-toolbarTemplate>
				    	<div class="clearfix">
				    		<div class="float-left">
				    			<button id="delivery-add-btn" type="button" class="btn btn-outline-success">${cmd_add}</button>
				    		</div>
				    		<div class="float-right">
				    			<button id="delivery-delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
				    		</div>
				    	</div>
					</kendo:grid-toolbarTemplate>
					<kendo:grid-columns>
						<kendo:grid-column title="${cmd_edit}" width="50" filterable="false" sortable="false" template="<%= editPayTemplate %>" />
						<kendo:grid-column title="시작 주문금액(원)" field="fromOrderPrice" filterable="false" />
						<kendo:grid-column title="종료 주문금액(원)" field="toOrderPrice" filterable="false" minScreenWidth="500" />
						<kendo:grid-column title="배달료(원)" field="deliveryPrice" minScreenWidth="600" filterable="false" />
					</kendo:grid-columns>
					<kendo:grid-filterable>
						<kendo:grid-filterable-messages selectedItemsFormat="${filter_selectedItems}"/>
					</kendo:grid-filterable>
					<kendo:dataSource serverPaging="true" serverSorting="true" serverFiltering="true" serverGrouping="true" error="kendoReadError">
						<kendo:dataSource-sort>
							<kendo:dataSource-sortItem field="whoLastUpdateDate" dir="asc"/>
						</kendo:dataSource-sort>
						<kendo:dataSource-filter>
							<kendo:dataSource-filterItem field="store.site.id" operator="eq" logic="and" value="${sessionScope['currentSiteId']}">
								<kendo:dataSource-filterItem field="store.id" operator="eq" logic="and" value="${sessionScope['currentStoreId']}">
								</kendo:dataSource-filterItem>
							</kendo:dataSource-filterItem>
						</kendo:dataSource-filter>
						<kendo:dataSource-transport>
							<kendo:dataSource-transport-read url="${deliveryPayReadUrl}" dataType="json" type="POST" contentType="application/json" />
							<kendo:dataSource-transport-parameterMap>
								<script>
									function parameterMap(options,type) {
										return JSON.stringify(options);	
									}
								</script>
							</kendo:dataSource-transport-parameterMap>
						</kendo:dataSource-transport>
						<kendo:dataSource-schema data="data" total="total" groups="data">
							<kendo:dataSource-schema-model id="id">
							</kendo:dataSource-schema-model>
						</kendo:dataSource-schema>
					</kendo:dataSource>
				</kendo:grid>
			</div>
		</div>
	</div>
	<div class="tab-pane" id="discount-order">
		<div class="card">
			<div class="card-body">
				<div class="pb-2 mb-2">
					할인 정책
				</div>
				<div class="form-row pb-2">
					<div class="form-group col-sm-auto mb-auto mt-auto">제품 금액의</div>
					<div class="form-group col-sm-1 mb-auto mt-auto">
						<input type="hidden" name="discountId" value="">
						<input type="text" class="form-control numberClass" name="dispercentageNumber" maxlength="3"/>
					</div>
					<div class="form-group col-sm-auto mb-auto mt-auto">%를 할인한다.</div>
				</div>
			</div>
		</div>
		<div class="card">
			<div class="card-body">
				<div class="pb-2 mb-2">
					행사 상품
				</div>

			
			<div class="mb-4">
				<%
					String editEventTemplate = 
					"<button type='button' onclick='eventEdit(#= id #)' class='btn icon-btn btn-sm btn-outline-success borderless'>" + 
					"<span class='fas fa-pencil-alt'></span></button>";
				%>

				<kendo:grid name="event-grid" pageable="true" filterable="false" sortable="false" scrollable="false" reorderable="false" resizable="true" selectable="single" >
					<kendo:grid-pageable refresh="false" previousNext="false" numeric="false" pageSize="10000" info="false" />
					<kendo:grid-toolbarTemplate>
				    	<div class="clearfix">
				    		<div class="float-left">
				    			<button id="event-add-btn" type="button" class="btn btn-outline-success">${cmd_add}</button>
				    		</div>
				    		<div class="float-right">
				    			<button id="event-delete-btn" type="button" class="btn btn-danger">${cmd_delete}</button>
				    		</div>
				    	</div>
					</kendo:grid-toolbarTemplate>
					<kendo:grid-columns>
						<kendo:grid-column title="${cmd_edit}" width="50" filterable="false" sortable="false" template="<%= editEventTemplate %>" />
						<kendo:grid-column title="이벤트 명" field="eventName" filterable="false" />
						<kendo:grid-column title="유효기간" field="effectiveStartDate" format="{0:yyyy-MM-dd}" minScreenWidth="700"/>
						<kendo:grid-column title="만료기간" field="effectiveEndDate" format="{0:yyyy-MM-dd}" minScreenWidth="700"/>
					</kendo:grid-columns>
					<kendo:grid-filterable>
						<kendo:grid-filterable-messages selectedItemsFormat="${filter_selectedItems}"/>
					</kendo:grid-filterable>
					<kendo:dataSource serverPaging="true" serverSorting="true" serverFiltering="true" serverGrouping="true" error="kendoReadError">
						<kendo:dataSource-sort>
							<kendo:dataSource-sortItem field="whoLastUpdateDate" dir="asc"/>
						</kendo:dataSource-sort>
						<kendo:dataSource-filter>
							<kendo:dataSource-filterItem field="store.site.id" operator="eq" logic="and" value="${sessionScope['currentSiteId']}">
								<kendo:dataSource-filterItem field="store.id" operator="eq" logic="and" value="${sessionScope['currentStoreId']}">
								</kendo:dataSource-filterItem>
							</kendo:dataSource-filterItem>
						</kendo:dataSource-filter>
						<kendo:dataSource-transport>
							<kendo:dataSource-transport-read url="${eventReadUrl}" dataType="json" type="POST" contentType="application/json" />
							<kendo:dataSource-transport-parameterMap>
								<script>
									function parameterMap(options,type) {
										return JSON.stringify(options);	
									}
								</script>
							</kendo:dataSource-transport-parameterMap>
						</kendo:dataSource-transport>
						<kendo:dataSource-schema data="data" total="total" groups="data">
							<kendo:dataSource-schema-model id="id">
								<kendo:dataSource-schema-model-fields>
									<kendo:dataSource-schema-model-field name="effectiveStartDate" type="date" />
									<kendo:dataSource-schema-model-field name="effectiveEndDate" type="date" />
								</kendo:dataSource-schema-model-fields>
							</kendo:dataSource-schema-model>
						</kendo:dataSource-schema>
					</kendo:dataSource>
				</kendo:grid>
			</div>
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
									<input name="files" type="file" />
									<br>
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

<div class="modal fade" data-backdrop="static" id="couponAdd">
	<div class="modal-dialog">
		<form class="modal-content" id="couponForm" method="post" enctype="application/json">
			<div class="modal-header move-cursor">
				<h5 class="modal-title">${title_coupon}</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">${label_couponNm}</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="name" maxlength="50">
					</div>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">${label_couponDiscount}</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="price" maxlength="10"> 
					</div>
					<label class="col-form-label col-sm-2 text-sm-left">${label_couponPrice1}</label>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">${label_couponValidDate}</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="validDate" maxlength="3"> 
					</div>
					<label class="col-form-label col-sm-2 text-sm-left">${label_couponValidDate1}</label>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" onclick="couponSave();">${confirm_ok}</button>
			</div>
		</form>
	</div>
</div>

<div class="modal fade" data-backdrop="static" id="couponUpdate">
	<div class="modal-dialog">
		<form class="modal-content" id="couponUp" method="post" enctype="application/json">
			<input type="hidden" name="couponId" />
			<div class="modal-header move-cursor">
				<h5 class="modal-title">${title_coupon}</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">${label_couponNm}</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="name" maxlength="50">
					</div>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">${label_couponDiscount}</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="price" maxlength="10"> 
					</div>
					<label class="col-form-label col-sm-2 text-sm-left">${label_couponPrice1}</label>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">${label_couponValidDate}</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="validDate" maxlength="3"> 
					</div>
					<label class="col-form-label col-sm-2 text-sm-left">${label_couponValidDate1}</label>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" onclick="couponUpdate();">${confirm_ok}</button>
			</div>
		</form>
	</div>
</div>

<div class="modal fade" data-backdrop="static" id="eventAdd">
	<div class="modal-dialog">
		<form class="modal-content" id="eventForm" method="post" enctype="application/json">
			<div class="modal-header move-cursor">
				<h5 class="modal-title">이벤트</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">이벤트 명<span class="text-danger">*</span></label>
					<div class="col-sm-8">
						<input type="text" class="form-control required" name="name" maxlength="50">
					</div>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">유효기간<span class="text-danger">*</span></label>
					<div class="col-sm-8">
						<input name="effectiveStartDate" type="text" class="form-control required">
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" onclick="eventSave();">${confirm_ok}</button>
			</div>
		</form>
	</div>
</div>

<div class="modal fade" data-backdrop="static" id="eventUpdate">
	<div class="modal-dialog">
		<form class="modal-content" id="eventUp" method="post" enctype="application/json">
			<input type="hidden" name="eventId" />
			<div class="modal-header move-cursor">
				<h5 class="modal-title">이벤트</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">이벤트 명</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="eventName" maxlength="50">
					</div>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">유효기간</label>
					<div class="col-sm-8">
						<input name="effectiveStartDate" type="text" class="form-control required">
					</div>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">만료기간</label>
					<div class="col-sm-8">
						<input name="effectiveEndDate" type="text" class="form-control">
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" onclick="eventUpdate();">${confirm_ok}</button>
			</div>
		</form>
	</div>
</div>

<div class="modal fade" data-backdrop="static" id="deliveryAdd">
	<div class="modal-dialog">
		<form class="modal-content" id="deliveryForm" method="post" enctype="application/json">
			<div class="modal-header move-cursor">
				<h5 class="modal-title">배달료</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">시작금액</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="fromOrderPrice" maxlength="9">
					</div>
					<label class="col-form-label col-sm-2 text-sm-left">${label_couponPrice1}</label>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">종료금액</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="toOrderPrice" maxlength="9"> 
					</div>
					<label class="col-form-label col-sm-2 text-sm-left">${label_couponPrice1}</label>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">배달요금</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="deliveryPrice" maxlength="9"> 
					</div>
					<label class="col-form-label col-sm-2 text-sm-left">${label_couponPrice1}</label>
				</div>
				<label id="addError" class="col-form-label text-sm-right" style="color: red; display:none;">종료금액은 시작금액 보다 높아야됩니다.</label>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" onclick="deliverySave();">${confirm_ok}</button>
			</div>
		</form>
	</div>
</div>

<div class="modal fade" data-backdrop="static" id="deliveryUpdate">
	<div class="modal-dialog">
		<form class="modal-content" id="deliveryUp" method="post" enctype="application/json">
			<input type="hidden" name="deliveryId" />
			<div class="modal-header move-cursor">
				<h5 class="modal-title">배달료</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">×</button>
			</div>
			<div class="modal-body">
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">시작 금액</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="fromOrderPrice" maxlength="9">
					</div>
					<label class="col-form-label col-sm-2 text-sm-left">${label_couponPrice1}</label>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">종료 금액</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="toOrderPrice" maxlength="9"> 
					</div>
					<label class="col-form-label col-sm-2 text-sm-left">${label_couponPrice1}</label>
				</div>
				<div class="form-group row">
					<label class="col-form-label col-sm-2 text-sm-right">배달료</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="deliveryPrice" maxlength="9"> 
					</div>
					<label class="col-form-label col-sm-2 text-sm-left">${label_couponPrice1}</label>
				</div>
				<label id="updateError" class="col-form-label text-sm-right" style="color: red; display:none;">종료금액은 시작금액 보다 높아야됩니다.</label>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" onclick="deliveryUpdate();">${confirm_ok}</button>
			</div>
		</form>
	</div>
</div>

<style>

.upload-root-div {
	height: 200px;
}

.k-upload-files {
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

</style>

<!-- / Smart wizard -->


<!--  Scripts -->

<script>

var storeId = -1;

var uploadType = "";

var kLogoType = "";
var mLogoType = "";

var kPreviewFile = "/titles/${storeRoot}/";
var mPreviewFile = "/m/titles/${storeRoot}/";

var kFilename = "";
var mFilename = "";

var objImgSel = null;
var objImgUnsel = null;
var objTxtSel = null;
var objTxtUnsel = null;
var objImgBtn = null;
var objTxtBtn = null;
00
var couponData = null;

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
		multiple: false,
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
			$("#wizard-form input[name='uploadImgFilename']").val(e.files[0].name);
			
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
    				}
    			},
    			error: ajaxSaveError
    		});
		}
	}
}


$(document).ready(function() {

<c:if test="${not empty sessionScope['currentStoreId']}">
	storeId = ${sessionScope['currentStoreId']};
</c:if>

	$("#kiosk-order select[name='kioskMenuMatrix']").selectpicker('render');

	bootstrapSelectVal($("#kiosk-order select[name='kioskMenuMatrix']"), "3x3");
	
	
	$("#save-btn").click(function(e) {
    	var data = {
    		id: storeId,
    		kLogoType: kLogoType,
    		kLogoImage: $.trim($("#kiosk-order input[name='imgFilename']").val()),
    		kLogoUniqueName: kFilename,
			kLogoText: $.trim($("#kiosk-order input[name='txtLogo']").val()),
   			kMatrix: $("#kiosk-order select[name='kioskMenuMatrix']").val(),
    		mLogoType: mLogoType,
    		mLogoImage: $.trim($("#mobile-order input[name='imgFilename']").val()),
    		mLogoUniqueName: mFilename,
			mLogoText: $.trim($("#mobile-order input[name='txtLogo']").val()),
			immediateTF : $("#mobile-order input[name='immediate']").is(":checked"),
			
			policyId: $.trim($("#saving-order input[name='policyId']").val()),
			orderAmt: $.trim($("#saving-order input[name='orderAmt']").val()),
			reserveAmt: $.trim($("#saving-order input[name='reserveAmt']").val()),
			couponPolicyId: $.trim($("#saving-order input[name='couponPolicyId']").val()),
			stampAmt: $.trim($("#saving-order input[name='stampAmt']").val()),
			couponSelect: $.trim($("#saving-order select[name='couponSelect']").val()),
			
			//포인트 관련 정보
			pointId: $.trim($("#point-order input[name='pointId']").val()),
			percentageNumber: $.trim($("#point-order input[name='percentageNumber']").val()),
			pointAmt: $.trim($("#point-order input[name='pointAmt']").val()),
			
// 			discountId: $.trim($("#discount-order input[name='discountId']").val()),
			dispercentageNumber: $.trim($("#discount-order input[name='dispercentageNumber']").val()),
			
			//배달료 관련 정보
  			deliveryId: $.trim($("#delivery-order input[name='deliveryId']").val()),
  			minOrderPrice: $.trim($("#delivery-order input[name='minOrderPrice']").val()),
  			deliveryPay: $.trim($("#delivery-order input[name='deliveryPay']").val()),
   		}
    	
    	$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${updateUrl}",
			data: JSON.stringify(data),
			success: function (form) {
				showAlertModal("success", "${msg_updateComplete}");
				kPreviewFile = "/titles/${storeRoot}/" + kFilename;
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
			$("#discount-order input[name='dispercentageNumber']").val(data.discount);
			
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
			
			bootstrapSelectVal($("select[name='kioskMenuMatrix']"), data.menuMatrix);
			
			$("#mobile-order input[name='immediate']").prop("checked", data.immediate);
			
	// 쿠폰 정책 가져오기  >> 이후 포인트도 사용될수 있음
	<c:choose>
		<c:when test="${'CP' eq savingType}">
			policyRead("S");
		</c:when>
		<c:when test="${'PO' eq savingType}">
			policyRead("P");
		</c:when>
        <c:otherwise>
        	policyRead("N");
    	 </c:otherwise>
	</c:choose>
			
		},
		error: ajaxReadError
	});
	
	$(document).on("click","#add-btn",function(){
		$("#couponAdd").modal('show');
		$("#couponAdd").draggable({handle: ".modal-header"});
		$("#couponForm input[name='name']").val("");
		$("#couponForm input[name='price']").val("");
		$("#couponForm input[name='validDate']").val("");
		
		$("#couponForm").validate({
			rules: {
				name: {
					required: true, maxlength: 20
				},
				price: {
					required: true, maxlength: 10, number:true, numberCheck: true
				},
				validDate: {
					required: true, maxlength: 3, number:true, numberCheck: true
				}
			}
		});
	});
	
	$(document).on("click","#event-add-btn",function(){
		
		$("#eventAdd").modal('show');
		$("#eventAdd").draggable({handle: ".modal-header"});
		
		$("#eventForm input[name='effectiveStartDate']").kendoDatePicker({
			format: "yyyy-MM-dd",
			parseFormats: [
				"yyyy-MM-dd",
			],
			value: new Date(),
		});
		
		$("#eventForm input[name='name']").val("");
		
		$("#evnetForm").validate({
			rules: {
				name: {
					required: true, maxlength: 20
				},
				effectiveStartDate: { date: true },
			}
		});
		

	});
	
	$(document).on("click","#delivery-add-btn",function(){
		$("#deliveryAdd").modal('show');
		$("#deliveryAdd").draggable({handle: ".modal-header"});
		$("#deliveryForm input[name='fromOrderprice']").val("");
		$("#deliveryForm input[name='toOrderprice']").val("");
		$("#deliveryForm input[name='deliveryPrice']").val("");
		
		$("#deliveryForm").validate({
			rules: {
				fromOrderprice: {
					required: true, maxlength: 20
				},
				toOrderprice: {
					required: true, maxlength: 10, number:true, numberCheck: true
				},
				deliveryPrice: {
					required: true, maxlength: 10, number:true, numberCheck: true
				}
			}
		});
	});
	
	$.validator.addMethod('numberCheck',
		function(value, element) {
			var regexp = /^[0-9]*$/;
			return regexp.test(value);
		},
		'올바른 숫자를 입력하십시오.'
	);
	
	// Delete
	$("#delete-btn").click(function(e) {
		e.preventDefault();
			
		var grid = $("#grid").data("kendoGrid");
		var rows = grid.select();
	
		var delRows = [];
		rows.each(function(index, row) {
			var selectedItem = grid.dataItem(row);
			delRows.push(selectedItem.id);
		});
			
		if (delRows.length > 0) {
			showDelConfirmModal(function(result) {
				if (result) {
					$.ajax({
						type: "POST",
						contentType: "application/json",
						dataType: "json",
						url: "${destroyUrl}",
						data: JSON.stringify({ items: delRows }),
						success: function (form) {
        					showDeleteSuccessMsg();
							grid.dataSource.read();
							couponSelect();
						},
						error: ajaxDeleteError
					});
				}
			}, true, delRows.length);
		}
	});
	// Delete
	$("#event-delete-btn").click(function(e) {
		e.preventDefault();
			
		var grid = $("#event-grid").data("kendoGrid");
		var rows = grid.select();
	
		var delRows = [];
		rows.each(function(index, row) {
			var selectedItem = grid.dataItem(row);
			delRows.push(selectedItem.id);
		});
			
		if (delRows.length > 0) { 
			showDelConfirmModal(function(result) {
				if (result) {
					$.ajax({
						type: "POST",
						contentType: "application/json",
						dataType: "json",
						url: "${eventdestroyUrl}",
						data: JSON.stringify({ items: delRows }),
						success: function (form) {
        					showDeleteSuccessMsg();
							grid.dataSource.read();
							couponSelect();x
						},
						error: ajaxDeleteError
					});
				}
			}, true, delRows.length);
		}
	});
	
	// / Delete
		$("#delivery-delete-btn").click(function(e) {
		e.preventDefault();
			
		var grid = $("#delivery-grid").data("kendoGrid");
		var rows = grid.select();
	
		var delRows = [];
		rows.each(function(index, row) {
			var selectedItem = grid.dataItem(row);
			delRows.push(selectedItem.id);
		});
			
		if (delRows.length > 0) {
			showDelConfirmModal(function(result) {
				if (result) {
					$.ajax({
						type: "POST",
						contentType: "application/json",
						dataType: "json",
						url: "${deliveryPayDestroyUrl}",
						data: JSON.stringify({ items: delRows }),
						success: function (form) {
        					showDeleteSuccessMsg();
							grid.dataSource.read();
						},
						error: ajaxDeleteError
					});
				}
			}, true, delRows.length);
		}
	});

});

$(".numberClass").on("keyup", function() {
    $(this).val($(this).val().replace(/[^0-9]/g,""));
});

function couponSave(){
	if ($("#couponForm").valid()) {
    	var data = {
   			storeId : storeId, 
   			name: $("#couponForm input[name='name']").val(),
   			price: $("#couponForm input[name='price']").val(),
   			validDate: $("#couponForm input[name='validDate']").val()
       	};
		
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${couponSaveUrl}",
			data: JSON.stringify(data),
			success: function (data) {
				showSaveSuccessMsg();
				$("#couponAdd").modal("hide");
				$("#grid").data("kendoGrid").dataSource.read();
				couponSelect();
			},
			error: ajaxSaveError
		});
	};
}

function eventSave(){
	validateKendoDateValue($("#eventForm input[name='effectiveStartDate']"));
	
	if ($("#eventForm").valid()) {
    	var data = {
   			storeId : storeId, 
   			name: $("#eventForm input[name='name']").val(),
   			effectiveStartDate: $("#eventForm input[name='effectiveStartDate']").data("kendoDatePicker").value(),
       	};
		
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${eventSaveUrl}",
			data: JSON.stringify(data),
			success: function (data) {
				showSaveSuccessMsg();
				$("#eventAdd").modal("hide");
				$("#event-grid").data("kendoGrid").dataSource.read();
			},
			error: ajaxSaveError
		});
	};
}


function edit(id) {
	var dataItem = $("#grid").data("kendoGrid").dataSource.get(id);
	
	$("#couponUp input[name='couponId']").val(dataItem.id);
	$("#couponUp input[name='name']").val(dataItem.name);
	$("#couponUp input[name='price']").val(dataItem.price);
	$("#couponUp input[name='validDate']").val(dataItem.validDate);
	
	$("#couponUp").validate({
		rules: {
			name: {
				required: true, maxlength: 20
			},
			price: {
				required: true, maxlength: 10, number:true, numberCheck: true
			},
			validDate: {
				required: true, maxlength: 3, number:true, numberCheck: true
			}
		}
	});
	
	$("#couponUpdate").draggable({handle: ".modal-header"});
	$("#couponUpdate").modal('show');
}
function eventEdit(id) {
	
	$("#eventUp input[name='effectiveStartDate']").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd",
		],
		value: new Date(),
	});
	$("#eventUp input[name='effectiveEndDate']").kendoDatePicker({
		format: "yyyy-MM-dd",
		parseFormats: [
			"yyyy-MM-dd",
		],
		value: new Date(),
	});
	
	var dataItem = $("#event-grid").data("kendoGrid").dataSource.get(id);
	
	$("#eventUp input[name='eventId']").val(dataItem.id);
	$("#eventUp input[name='eventName']").val(dataItem.eventName);
	$("#eventUp input[name='effectiveStartDate']").data("kendoDatePicker").value(dataItem.effectiveStartDate);
	$("#eventUp input[name='effectiveEndDate']").data("kendoDatePicker").value(dataItem.effectiveEndDate);
	
	$("#eventUp").validate({
		rules: {
			eventName: {
				required: true, maxlength: 20
			},
			effectiveStartDate: { date: true },
			effectiveEndDate: { date: true },
		}
	});
	
	$("#eventUpdate").draggable({handle: ".modal-header"});
	$("#eventUpdate").modal('show');
}


function couponUpdate(){
	if ($("#couponUp").valid()) {
    	var data = {
    			couponId : $("#couponUp input[name='couponId']").val(), 
    			name: $("#couponUp input[name='name']").val(),
    			price: $("#couponUp input[name='price']").val(),
    			validDate: $("#couponUp input[name='validDate']").val()
       	};
		
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${couponUpdateUrl}",
			data: JSON.stringify(data),
			success: function (data) {
				showOperationSuccessMsg();
				$("#couponUp input[name='couponId']").val("");
				$("#couponUpdate").modal("hide");
				$("#grid").data("kendoGrid").dataSource.read();
				couponSelect();
			},
			error: ajaxSaveError
		});
	};
}

function eventUpdate(){
	validateKendoDateValue($("#eventUp input[name='effectiveStartDate']"));
	validateKendoDateValue($("#eventUp input[name='effectiveEndDate']"));
	
	if ($("#eventUp").valid()) {
    	var data = {
    			id : $("#eventUp input[name='eventId']").val(), 
    			eventName: $("#eventUp input[name='eventName']").val(),
       			effectiveStartDate: $("#eventUp input[name='effectiveStartDate']").data("kendoDatePicker").value(),
       			effectiveEndDate: $("#eventUp input[name='effectiveEndDate']").data("kendoDatePicker").value(),
       	};
		
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${eventUpdateUrl}",
			data: JSON.stringify(data),
			success: function (data) {
				showOperationSuccessMsg();
				$("#eventUp input[name='eventId']").val("");
				$("#eventUpdate").modal("hide");
				$("#event-grid").data("kendoGrid").dataSource.read();
				
			},
			error: ajaxSaveError
		});
	};
}

function couponSelect(){
	var data = {
		storeId : storeId
   	};

	$("#saving-order select[name='couponSelect']").kendoDropDownList({
		dataTextField: "name",
		dataValueField: "id",
        dataSource: {
			transport: {
				read: {
					type: "POST",
					contentType: "application/json",
					dataType: "json",
					url: "${couponListUrl}",
					data: data
				},
				parameterMap: function (data) {
					console.log(data)
                	return JSON.stringify(data);
				},
			},
			error: function(e) {
      			showReadErrorMsg();
			}
        },
        optionLabel: {
        	name: "${label_chooseText}", value: "",
		},
        delay: 500
    });
	
	couponPolicySelect();
}


function deliveryEdit(id) {
	var dataItem = $("#delivery-grid").data("kendoGrid").dataSource.get(id);
	console.log(dataItem)
 	$("#deliveryUp input[name='deliveryId']").val(dataItem.id);
	$("#deliveryUp input[name='fromOrderPrice']").val(dataItem.fromOrderPrice);
	$("#deliveryUp input[name='toOrderPrice']").val(dataItem.toOrderPrice);
	$("#deliveryUp input[name='deliveryPrice']").val(dataItem.deliveryPrice);
	
	$("#deliveryUp").validate({
		rules: {
			fromPrice: {
				required: true, maxlength: 20
			},
			toPrice: {
				required: true, maxlength: 10, number:true, numberCheck: true
			},
			deliveryPrice: {
				required: true, maxlength: 10, number:true, numberCheck: true
			}
		}
	});
	
	$("#deliveryUpdate").draggable({handle: ".modal-header"});
	$("#deliveryUpdate").modal('show');
}

function deliverySave(){
	if ($("#deliveryForm").valid()) {
    	var data = {
   			storeId : storeId, 
   			fromOrderPrice: $("#deliveryForm input[name='fromOrderPrice']").val(),
   			toOrderPrice: $("#deliveryForm input[name='toOrderPrice']").val(),
   			deliveryPrice: $("#deliveryForm input[name='deliveryPrice']").val()
       	};
		
  		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${deliveryPaySaveUrl}",
			data: JSON.stringify(data),
			success: function (data) {
				if(data=="IF1"){
					$("#addError").show();
				}else{
					showSaveSuccessMsg();
					$("#deliveryAdd").modal("hide");
					$("#addError").hide();
					$("#delivery-grid").data("kendoGrid").dataSource.read();
				}
			},
			error: ajaxSaveError
		});
	};
}


function deliveryUpdate(){
	if ($("#deliveryUp").valid()) {
    	var data = {
    			deliveryId : $("#deliveryUp input[name='deliveryId']").val(), 
    			fromOrderPrice: $("#deliveryUp input[name='fromOrderPrice']").val(),
    			toOrderPrice: $("#deliveryUp input[name='toOrderPrice']").val(),
    			deliveryPrice: $("#deliveryUp input[name='deliveryPrice']").val()
       	};
		
 		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${deliveryPayUpdateUrl}",
			data: JSON.stringify(data),
			success: function (data) {
				if(data=="IF1"){
					$("#updateError").show();
				}else{
					showOperationSuccessMsg();
					$("#deliveryUp input[name='deliveryId']").val("");
					$("#deliveryUpdate").modal("hide");
					$("#updateError").hide();
					$("#delivery-grid").data("kendoGrid").dataSource.read();
				}
			},
			error: ajaxSaveError
		}); 
	};
}

function policyRead(readType){
	var data = {
		storeId : storeId,
		readType : readType
   	};
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${policyRead}",
		data: JSON.stringify(data),
		success: function (data) {
			if(data.length > 0){
				var minOrderDiv = $("#minOrderDiv");
				var deliveryDiv = $("#deliveryDiv");
				if("S" == readType){
					var stampDiv = $("#stampDiv");
					var couponDiv = $("#couponDiv");

					
					for(var i=0; i < data.length; i++){
						console.log(data[i]);
						
						var type = data[i].type;
						if("S" == type){
							stampDiv.html("");
							
							var stampHtml = '<div class="form-row pb-2">';
							stampHtml += '<div class="form-group col-sm-auto mb-auto mt-auto">${label_stampText1}</div>';
							stampHtml += '<div class="form-group col-sm-1 mb-auto mt-auto">';
							stampHtml += '<input type="hidden" name="policyId" value="'+data[i].policyId+'">';
							stampHtml += '<input type="text" class="form-control numberClass" name="orderAmt" value="'+data[i].orderAmt+'" maxlength="3"/>';
							stampHtml += '</div>';
							stampHtml += '<div class="form-group col-sm-auto mb-auto mt-auto">${label_stampText2} ${title_stamp}</div>';
							stampHtml += '<div class="form-group col-sm-1 mb-auto mt-auto">';
							stampHtml += '<input type="text" class="form-control numberClass" name="reserveAmt" value="'+data[i].stamp+'" maxlength="3">';
							stampHtml += '</div>';
							stampHtml += '<div class="form-group col-sm-auto mb-auto mt-auto">${label_stampText3}</div>';
							stampHtml += '</div>';
								
							stampDiv.append(stampHtml);
							
						}else if("C" == type){
							couponDiv.html("");
							
							var couponHtml = '<div class="form-row pb-2">';
							couponHtml += '<div class="form-group col-sm-auto mb-auto mt-auto">${title_stamp}</div>';
							couponHtml += '<div class="form-group col-sm-1 mb-auto mt-auto">';
							couponHtml += '<input type="hidden" name="couponPolicyId" value="'+data[i].couponPolicyId+'">';
							couponHtml += '<input type="text" class="form-control numberClass" name="stampAmt" value="'+data[i].stamp+'" maxlength="3"/>';
							couponHtml += '</div>';
							couponHtml += '<div class="form-group col-sm-auto mb-auto mt-auto">${label_couponText1} ${title_coupon}</div>';
							couponHtml += '<div class="form-group col-sm-3 mb-auto mt-auto">';
							couponHtml += '<select name="couponSelect" style="width: 100%;" class="form-control"></select>';
							couponHtml += '</div>';
							couponHtml += '<div class="form-group col-sm-auto mb-auto mt-auto">${label_couponText2}</div>';
							couponHtml += '</div>';
							
							couponDiv.append(couponHtml);
						
						}
 						minOrderDiv.html("");										
						minOrderDiv.append(minOrdrHtmlSetting(data[i]));
							
						deliveryDiv.html("");
						deliveryDiv.append(deliveryHtmlSetting(data[i]));
// 						$("#discount-order input[name='dispercentageNumber']").val(data[i].dispercentageNumber);
					}
				}else if("P" == readType){
					for(var i=0; i < data.length; i++){
						$("#point-order input[name='pointId']").val(data[i].pointId);
						$("#point-order input[name='percentageNumber']").val(data[i].percentageNumber);
						$("#point-order input[name='pointAmt']").val(data[i].pointAmt);
// 						$("#discount-order input[name='dispercentageNumber']").val(data[i].dispercentageNumber);
						
 						minOrderDiv.html("");										
						minOrderDiv.append(minOrdrHtmlSetting(data[i]));
							
						deliveryDiv.html("");
						deliveryDiv.append(deliveryHtmlSetting(data[i]));
					}
				}else{
					for(var i=0; i < data.length; i++){
 						minOrderDiv.html("");										
						minOrderDiv.append(minOrdrHtmlSetting(data[i]));
							
						deliveryDiv.html("");
						deliveryDiv.append(deliveryHtmlSetting(data[i]));
					}
				}
			}
			
			if("S" == readType){
				couponData = data;
				// 쿠폰 List 추가
				couponSelect();
			}
		},
		error: ajaxSaveError
	});
}

function couponPolicySelect(){
	$("#couponDiv input[name=couponPolicyId]").each(function(index){
		var cpDivId = $(this).val();
		console.log(cpDivId);
		for(var i=0; i < couponData.length; i++){
			if("C" == couponData[i].type){
				if(cpDivId == couponData[i].couponPolicyId){
					$(this).parent().parent().find("select[name='couponSelect']").data("kendoDropDownList").value(couponData[i].couponId);
				} 
			}
		}
	});
}

function minOrdrHtmlSetting(data){
	var minOrdrHtml = '<div class="form-row pb-2">'
		minOrdrHtml+='<div class="form-group col-sm-auto mb-auto mt-auto">최소 주문금액은  </div>'
		minOrdrHtml+='<div class="form-group col-sm-1 mb-auto mt-auto">'
		minOrdrHtml+='<input type="hidden" name="deliveryId" value="'+data.couponPolicyId+'">'
		minOrdrHtml+='<input type="text" class="form-control numberClass" name="minOrderPrice" maxlength="10" value="'+data.minOrderPrice+'"/>'
		minOrdrHtml+='</div><div class="form-group col-sm-auto mb-auto mt-auto">원 입니다. </div></div>'
	return minOrdrHtml;
}

function deliveryHtmlSetting(data){
	var deliveryHtml = '<div class="form-row pb-2">'
		deliveryHtml+='<div class="form-group col-sm-auto mb-auto mt-auto">기본 배달료   </div>'
		deliveryHtml+='<div class="form-group col-sm-1 mb-auto mt-auto">'
		deliveryHtml+='<input type="hidden" name="deliveryId" value="'+data.couponPolicyId+'">'
		deliveryHtml+='<input type="text" class="form-control numberClass" name="deliveryPay" maxlength="10" value="'+data.deliveryPrice+'"/>'
		deliveryHtml+='</div><div class="form-group col-sm-auto mb-auto mt-auto">원 입니다. </div></div>'
	return deliveryHtml;
}
</script>

<!--  / Scripts -->

<!-- / Page body -->

<!-- Functional tags -->

<func:cmmImgLightBox /> 
<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
