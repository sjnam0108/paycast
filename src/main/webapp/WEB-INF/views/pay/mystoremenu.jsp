<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/mystoremenu/read" var="readUrl" />
<c:url value="/pay/mystoremenu/readMenu" var="readMenuUrl" />
<c:url value="/pay/mystoremenu/eventList" var="eventListUrl" />

<c:url value="/pay/mystoremenu/createGroups" var="createGroupUrl" />

<c:url value="/pay/mystoremenu/dropItem" var="dropItemUrl" />

<c:url value="/pay/mystoremenu/updateVisible" var="updateVisibleUrl" />
<c:url value="/pay/mystoremenu/updateGroup" var="updateGroupUrl" />
<c:url value="/pay/mystoremenu/destroyGroup" var="destroyGroupUrl" />

<c:url value="/pay/mystoremenu/createMenus" var="createMenuUrl" />
<c:url value="/pay/mystoremenu/destroyMenu" var="destroyMenuUrl" />
<c:url value="/pay/mystoremenu/updateMenu" var="updateMenuUrl" />

<c:url value="/pay/mystoremenu/upcmpl" var="uploadCmplUrl" />

<c:url value="/pay/mystoremenu/refresh" var="refreshUrl" />


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

<link rel="stylesheet" href="/resources/vendor/lib/smartwizard/smartwizard.css">
<link rel="stylesheet" href="/resources/vendor/lib/nestable/nestable2.css">
<link rel="stylesheet" href="/resources/vendor/css/fa581.all.css">

<script src="/resources/vendor/lib/smartwizard/smartwizard.js"></script>
<script src="/resources/vendor/lib/nestable/nestable2.js"></script>
<script src="/resources/vendor/lib/sortablejs/sortable.js"></script>


<!--  Main HTML -->

<div class="card mx-auto max-one-column mb-4 p-3 pt-2">
	<!-- Toolbar -->
	<div class="d-flex align-items-center pb-1">
		<label class="switcher switcher-secondary switcher-lg mr-1">
			<input type="checkbox" class="switcher-input" checked="checked" id="exp-col-switcher" disabled="true">
			<span class="switcher-indicator">
				<span class="switcher-yes">
					<span class="fas fa-angle-down fa-lg"></span>
				</span>
				<span class="switcher-no">
					<span class="fas fa-angle-up fa-lg"></span>
				</span>
			</span>
		</label>
		<label class="switcher switcher-danger switcher-lg">
			<input type="checkbox" class="switcher-input" checked="checked" id="move-lock-switcher" disabled="true">
			<span class="switcher-indicator">
				<span class="switcher-yes">
					<span class="fas fa-lock fa-lg"></span>
				</span>
				<span class="switcher-no">
					<span class="fas fa-lock-open fa-lg"></span>
				</span>
			</span>
		</label>
		<button type="button" class="btn btn-sm btn-outline-success ml-auto" onclick="addGroups();" >
			<span class="fas fa-plus"></span><span class="pl-2">${cmd_add}</span>
		</button>
		<div class="btn-group ml-1">
			<button type="button" class="btn btn-default btn-sm btn-outline-success dropdown-toggle hide-arrow disabled" id="action-btn" data-toggle="dropdown">
				<span class="fas fa-ellipsis-v"></span>
			</button>
			<div class="dropdown-menu">
				<a class="dropdown-item" href="javascript:refreshContent()"><span class='fas fa-sync'></span><span class="ml-2">${cmd_refresh}</span></a>
			</div>
		</div>
	</div>
   	<hr class="mt-1 mb-2"/>
   	
   	<!-- Menu root container -->
   	<div class="dd" id="menu-nestable">
   		<ol class="dd-list" id="menu-nestable-root">
			<div class='d-flex align-items-center justify-content-center py-4 text-center w-100'>${wait_pleaseWait}</div>
   		</ol>
   	</div>
</div>

<style>

/* 가로 가운데 적당한 크기의 컬럼 만들기 */
.max-one-column {
	max-width: 350px;
}
@media (min-width: 575px) {
	.max-one-column {
		max-width: 425px;
	}
}
@media (min-width: 767px) {
	.max-one-column {
		max-width: 500px;
	}
}
@media (min-width: 991px) {
	.max-one-column {
		max-width: 600px;
	}
}


/* nestable 항목 예쁘게 */
.dd-handle {
	font-weight: 400;
	font-size: 1.0rem;
	border: 1px solid #d0d0d0;
}
.menu-group {
	background: linear-gradient( 90deg, #4c84ff, #4c84ff 7px, transparent 8px, transparent 100px);
}
.dd a {
	color: inherit;
	pointer: inherit;
}
.hidden {
	display: none;
}
.visible {
	display: inline;
}


/* 팝오버의 메뉴 보기 좋게 */
.popover-custom {
	font-size:.75rem; font-weight: 300; margin: .1rem;
}


/* 선택 메뉴 순서 변경의 메뉴 보기 좋게 */
.reorder-custom {
	font-size:.9rem; font-weight: 300; margin: .2rem;
}

</style>


<!--  Root form container -->
<div id="formRoot"></div>
<div id="formRootLevel2"></div>


<!--  Forms -->

<script id="template-group-item" type="text/x-kendo-template">

<li class="dd-item" data-id="#: id #" data-name="#: name #" data-type="G" id="li-group-root-#: id #">
	<div class="dd-handle menu-group">
		<a href="javascript:void(0);" class="dd-nodrag" name="chg-group-vis-btn">
# if (published) { #
			<span id="g-vis-#: id #" class="visible"><span class="far fa-eye fa-fw"></span></span>
			<span id="g-invis-#: id #" class="hidden"><span class="far fa-eye-slash fa-fw"></span></span>
# } else { #
			<span id="g-vis-#: id #" class="hidden"><span class="far fa-eye fa-fw"></span></span>
			<span id="g-invis-#: id #" class="visible"><span class="far fa-eye-slash fa-fw"></span></span>
# } #
		</a>
		<span class="ml-3"></span>
		<a class="dd-nodrag" href="javascript:void(0);" name="edit-group-btn"><span id="G-name-#: id #">#: name #</span></a>
		<div class="float-right">
			<button type="button" class="btn icon-btn btn-sm btn-outline-success borderless dd-nodrag" name="add-menu-btn">
				<span class="fas fa-plus"></span>
			</button>
			<button type="button" class="btn icon-btn btn-sm btn-outline-danger borderless dd-nodrag" name="del-group-btn">
				<span class="fas fa-times"></span>
			</button>
		</div>
	</div>
# if (menus.length > 0) { #
	<ol class="dd-list" id="group-root-#: id #"></ol>
# } #
</li>

</script>

<script id="template-menu-item" type="text/x-kendo-template">

<li class="dd-item" data-id="#: id #" data-name="#: name #" data-type="M" id="li-menu-root-#: id #">
	<div class="dd-handle">
		<a href="javascript:void(0);" class="dd-nodrag" name="chg-menu-vis-btn">
# if (published) { #
			<span id="m-vis-#: id #" class="visible"><span class="far fa-eye fa-fw"></span></span>
			<span id="m-invis-#: id #" class="hidden"><span class="far fa-eye-slash fa-fw"></span></span>
# } else { #
			<span id="m-vis-#: id #" class="hidden"><span class="far fa-eye fa-fw"></span></span>
			<span id="m-invis-#: id #" class="visible"><span class="far fa-eye-slash fa-fw"></span></span>
# } #
		</a>
		<span class="ml-3"></span>
		<a class="dd-nodrag" href="javascript:void(0);" name="edit-menu-btn"><span id="M-name-#: id #">#: name #</span></a>
		<div class="float-right">
			<button type="button" class="btn icon-btn btn-sm btn-outline-danger borderless dd-nodrag" name="del-menu-btn">
				<span class="fas fa-times"></span>
			</button>
		</div>
	</div>
</li>

</script>

<script id="template-opt-menu-item" type="text/x-kendo-template">

<tr data-id="#: id #" data-name="#: name #" data-menus="#: menus #">
	<td class="align-middle">#: name #</td>
	<td class="align-middle">
		#: count #
# if (count > 0) { #
		<span class="ml-1"></span>
		<a href="javascript:void(0)" data-toggle="popover" data-trigger="focus" data-content="content" data-placement="top" tabindex="0" class="menuhint">
			<span class="fas fa-info-circle text-secondary"></span>
		</a>
# } #
	</td>
	<td class="text-right">
		<button type="button" class="btn icon-btn btn-sm btn-outline-success borderless edit">
			<span class="fas fa-pencil-alt"></span>
		</button>
		<button type="button" class="btn icon-btn btn-sm btn-outline-secondary borderless up" >
			<span class="fas fa-arrow-up"></span>
		</button>
		<button type="button" class="btn icon-btn btn-sm btn-outline-secondary borderless down">
			<span class="fas fa-arrow-down"></span>
		</button>
		<button type="button" class="btn icon-btn btn-sm btn-outline-secondary borderless arrange">
			<span class="fas fa-arrows-alt-h"></span>
		</button>
		<button type="button" class="btn icon-btn btn-sm btn-outline-danger borderless del">
			<span class="fas fa-times"></span>
		</button>
	</td>
</tr>

</script>

<script id="template-opt-menu-item-items" type="text/x-kendo-template">

<span class="badge badge-pill badge-secondary popover-custom">#: data #</span>

</script>

<script id="template-opt-menu-item-reorder-item" type="text/x-kendo-template">

<span class="badge badge-pill badge-secondary reorder-custom" data-menu="#: data #">#: data #</span>

</script>

<script id="template-group-edit" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-1">
	<div class="modal-dialog modal-sm">
		<form class="modal-content" id="form-1" rowid="-1" url="">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${label_group}
					<span class="font-weight-light pl-1"><span>${form_edit}</span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-group col">
					<label class="form-label">
						${label_name}
						<span class="text-danger">*</span>
					</label>
					<input name="name" type="text" maxlength="14" class="form-control required">
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveMenuGroup()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>

<script id="template-group-add" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-2">
	<div class="modal-dialog">
		<form class="modal-content" id="form-2" rowid="-1" url="">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${label_group}
					<span class="font-weight-light pl-1"><span>${form_add}</span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-group col">
					<label class="form-label">
						${label_name}
						<span class="text-danger">*</span>
						<span class="small text-muted pl-3">${tip_newItem}</span>
					</label>
					<input name="name" type="text" class="form-control">
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveMenuGroups()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>

<script id="template-menu-add" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-3">
	<div class="modal-dialog">
		<form class="modal-content" id="form-3" rowid="-1" url="">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					<span name="title"></span>
					<span class="font-weight-light pl-1"><span>${label_addMenu}</span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-group col">
					<label class="form-label">
						${label_name}
						<span class="text-danger">*</span>
						<span class="small text-muted pl-3">${tip_newItem}</span>
					</label>
					<input name="name" type="text" class="form-control">
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveMenus()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>

<script id="template-menu-edit" type="text/x-kendo-template">

<div class="modal fade" data-backdrop="static" id="form-modal-4">
	<div class="modal-dialog">
		<form class="modal-content" id="form-4" rowid="-1" url="${editUrl}">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${label_menu}
					<span class="font-weight-light pl-1"><span name="subtitle">${form_edit}</span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color pt-3">
				<div class="form-row pr-2">
					<div class="d-flex justify-content-end w-100">
						<label class="custom-control custom-checkbox">
							<input type="checkbox" class="custom-control-input" name="sold-out">
							<span class="custom-control-label">
								<span class="badge badge-outline-dark font-weight-normal small">${label_soldOut}</span>
							</span>
						</label>
					</div>
				</div>
				<div class="form-row px-1">
					<div class="form-group col">
						<label class="form-label">
							${label_name}
							<span class="text-danger">*</span>
						</label>
						<input name="name" type="text" maxlength="20" class="form-control required">
					</div>
					<div class="form-group col">
						<label class="form-label">
							${label_price}
							<span class="text-danger">*</span>
						</label>
						<input name="price" type="text" class="form-control required">
					</div>
				</div>

				<div class="nav-tabs-top mt-1">
					<ul class="nav nav-tabs px-1">
						<li class="nav-item">
							<a class="nav-link active" data-toggle="tab" id="menu-basic" href="\\\#menu-basic-ctnt">
								<span>${tab_basic}</span>
							</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" data-toggle="tab" id="menu-man" href="\\\#menu-man-ctnt">
								<span>${tab_man}</span>
							</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" data-toggle="tab" id="menu-opt" href="\\\#menu-opt-ctnt">
								<span>${tab_opt}</span>
							</a>
						</li>
					</ul>
					<div class="tab-content mx-1">
						<div class="tab-pane p-2 active" id="menu-basic-ctnt">
							<div class="form-row mt-3">
								<div class="col-sm-12">
									<div class="form-group col">
										<label class="form-label">
											${label_image}
										</label>
										<div class="input-group">
											<span class="input-group-prepend">
												<button class="btn btn-secondary" type="button" onclick="uploadImage()">
													<span class="fas fa-cloud-upload-alt"></span>
												</button>
											</span>
											<input name="imgFilename" type="text" class="form-control" readonly="readonly">
											<span class="input-group-append">
												<button class="btn btn-secondary" type="button" onclick="viewImg()">
													<span class="fas fa-search"></span>
												</button>
											</span>
										</div>
									</div>
								</div>
							</div>
							<div class="form-row">
								<div class="col-sm-6">
									<div class="form-group col">
										<label class="form-label">
											상품코드
										</label>
										<input name="code" type="text" maxlength="20" class="form-control">
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group col">
										<label class="form-label">
											행사상품
										</label>
											<input type="hidden" name="eventId" value="">
											<select name="eventSelect" style="width: 100%;" class="form-control">

											</select>
									</div>
								</div>
							</div>
							<div class="form-row">
								<div class="col-sm-6">
									<div class="form-group col">
										<label class="form-label">
											${label_badge}
										</label>
										<select name="badgeType" class="selectpicker bg-white" data-style="btn-default" data-icon-base="fas" data-tick-icon="fa-blank" data-none-selected-text="">
											<option value="" data-icon="fa-blank"></option>
											<option value="N" data-icon="fa-asterisk fa-fw mr-2">${item_new}</option>
											<option value="R" data-icon="fa-award fa-fw mr-2">${item_rec}</option>
											<option value="I" data-icon="fa-infinity mr-2">${item_refill}</option>
											<option value="D" data-icon="fa-percent fa-fw mr-2"> 할인 메뉴</option>
										</select>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group col">
										<label class="form-label">
											${label_visible}
										</label>
										<select name="visibleType" class="selectpicker bg-white" data-style="btn-default" data-icon-base="far" data-tick-icon="fa-blank" data-none-selected-text="">
											<option value="Y" data-icon="fa-eye fa-fw mr-2">${item_visible}</option>
											<option value="N" data-icon="fa-eye-slash fa-fw mr-2">${item_hidden}</option>
										</select>
									</div>
								</div>
							</div>
							<div class="form-row">
								<div class="col-sm-12">
									<div class="form-group col">
										<label class="form-label">
											${label_intro}
										</label>
										<textarea name="intro" rows="2" class="form-control" maxlength="150"></textarea>
									</div>
								</div>
							</div>

						</div>
						<div class="tab-pane p-2 fade" id="menu-man-ctnt">
							<div class="form-row mt-0 pt-1 pb-2 px-3">
								<table class="table card-table">
									<thead>
										<tr class="pt-0">
											<th>${label_name}</th>
											<th>${label_count}</th>
											<th class="text-right">
												<button type="button" class="btn btn-sm btn-outline-success ml-auto" onclick="addOptMenu();" >
													<span class="fas fa-plus"></span><span class="pl-2">${cmd_add}</span>
												</button>
											</th>
										</tr>
									</thead>
									<tbody id="man-sel-menu-root"></tbody>
								</table>
							</div>
						</div>
						<div class="tab-pane p-2 fade" id="menu-opt-ctnt">
							<div class="form-row mt-0 pt-1 pb-2 px-3">
								<table class="table card-table">
									<thead>
										<tr class="pt-0">
											<th>${label_name}</th>
											<th>${label_count}</th>
											<th class="text-right">
												<button type="button" class="btn btn-sm btn-outline-success ml-auto" onclick="addOptMenu();" >
													<span class="fas fa-plus"></span><span class="pl-2">${cmd_add}</span>
												</button>
											</th>
										</tr>
									</thead>
									<tbody id="opt-sel-menu-root"></tbody>
								</table>
							</div>
						</div>
					</div>
				</div>


			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer d-flex">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='saveMenu()'>${form_save}</button>
			</div>
			
		</form>
	</div>
</div>

</script>

<!-- 옵션 추가 팝업 -->
<script id="template-opt-menu-add" type="text/x-kendo-template">

<div class="modal fade modal-level-plus-1" data-backdrop="static" id="form-modal-5">
	<div class="modal-dialog">
		<form class="modal-content" id="form-5" rowid="-1" url="">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${label_optMenu}
					<span class="font-weight-light pl-1"><span>${form_add}</span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-row px-3">
					<div class="form-group col">
						<label class="form-label">
							${label_name}
							<span class="text-danger">*</span>
						</label>
						<input name="name" type="text" class="form-control required">
					</div>
				</div>
				<div class="form-row px-3">
					<div class="form-group col">
						<label class="form-label">
							${label_optMenu}
							<span class="text-danger">*</span>
							<span class="small text-muted pl-3">${tip_optMenu}</span>
						</label>
						<input name="menus" type="text" class="form-control">
					</div>
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='closeOptMenuAdd()'>${form_close}</button>
			</div>
			
		</form>
	</div>
</div>
</script>

<script id="template-opt-menu-edit" type="text/x-kendo-template">

<div class="modal fade modal-level-plus-1" data-backdrop="static" id="form-modal-6">
	<div class="modal-dialog">
		<form class="modal-content" id="form-6" rowid="-1" url="">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${label_optMenu}
					<span class="font-weight-light pl-1"><span>${form_add}</span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">
				<div class="form-row px-3">
					<div class="form-group col">
						<label class="form-label">
							${label_name}
							<span class="text-danger">*</span>
						</label>
						<input name="name" type="text" class="form-control required">
					</div>
				</div>
				<div class="form-row px-3">
					<div class="form-group col">
						<label class="form-label">
							${label_optMenu}
							<span class="text-danger">*</span>
							<span class="small text-muted pl-3">${tip_optMenu}</span>
						</label>
						<input name="menus" type="text" class="form-control">
					</div>
				</div>
			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='closeOptMenuEdit()'>${form_close}</button>
			</div>
			
		</form>
	</div>
</div>

</script>

<script id="template-opt-menu-reorder" type="text/x-kendo-template">

<div class="modal fade modal-level-plus-1" data-backdrop="static" id="form-modal-7">
	<div class="modal-dialog">
		<form class="modal-content" id="form-7" rowid="-1" url="">
      
			<!-- Modal Header -->
			<div class="modal-header move-cursor">
				<h5 class="modal-title">
					${label_optMenu}
					<span class="font-weight-light pl-1"><span>${form_add}</span>
				</h5>
				<button type="button" class="close" data-dismiss="modal">×</button>
			</div>
        
			<!-- Modal body -->
			<div class="modal-body modal-bg-color">

				<div id="reorder-root-div"></div>

			</div>
        
			<!-- Modal footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${form_cancel}</button>
				<button type="button" class="btn btn-primary" onclick='closeOptMenuReorder()'>${form_close}</button>
			</div>
			
		</form>
	</div>
</div>

</script>

<!--  / Forms -->


<!-- Smart wizard -->

<script id="sw-template" type="text/x-kendo-template">

<div class="modal fade modal-level-plus-1" data-backdrop="static" id="wizard-modal">
    <div class="modal-dialog">
        <form class="modal-content" id="wizard-form">
            <div class="modal-header move-cursor"">
				<h5 class="modal-title">
					<span name="form-title">${title_menuImage}</span>
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
									<br />
									<p style="color: red;"> ${msg_itemSize}</p>
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
var couponData = null;
var initNestableRequired = true;

var previewFile = "/m/menus/${storeRoot}/";
var imgFilename = "";

var callerType = "";
var replaceId  = /[()]/gi;

$(document).ready(function() {

	$("#exp-col-switcher").click(function(){
		if($(this).is(':checked')){
			$('.dd').nestable('expandAll');
		} else {
			$('.dd').nestable('collapseAll');
		}
	});

	$("#move-lock-switcher").click(function(){
		if($(this).is(':checked')){
		    $('#menu-nestable').nestable("disabled");
		} else {
		    $('#menu-nestable').nestable("enabled");
		}
	});
	
	readStoreMenus();

});


//------------------------------------------------------------------- Toolbar functions: 1 function


function addGroups() {
	
	$("#formRoot").html(kendo.template($("#template-group-add").html()));

	$("#form-2 input[name='name']").tagsinput({
		trimValue: true,
		tagClass: 'badge badge-outline-primary text-primary',
		maxChars: 14,
		itemText: function(item) {
			return item.substr(0, 14);
		}
	});
	
	$('#form-modal-2 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-2").modal();
	
	$("#form-modal-2").on("shown.bs.modal", function() {
		$("#form-2 input[name='name']").tagsinput("focus");
	})
	$("#form-2 input[name='name']").on('beforeItemAdd', function(e) {
	    var element = $(this);
	    if(e.item.length > 14){
	    	e.cancel = true;
	    	element.tagsinput('add', e.item.substr(0, 14));
	    }
	});
}


//------------------------------------------------------------------- Group item functions: 4 functions


function changeGroupVisible(e) {		// 1 / 4
	
	e.preventDefault();
	
	var id = $(this).closest("li").attr("data-id");

	var hidden = $("#g-vis-" + id).css('display') == 'none';
	
	var data = {
       	id: Number(id),
       	type: "G",
      	published: hidden ? "Y" : "N",		// 현재값 토글
    };

	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${updateVisibleUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			showOperationSuccessMsg();
			
			if (hidden){
			    $("#g-vis-" + id).show();
			    $("#g-invis-" + id).hide();
			} else {
			    $("#g-vis-" + id).hide();
			    $("#g-invis-" + id).show();
			}
		},
		error: ajaxOperationError
	});
}


function deleteGroup(e) {		// 2 / 4
	
	e.preventDefault();
	
	var id = $(this).closest("li").attr("data-id");
	var name = $(this).closest("li").attr("data-name");
	
	var msg = "${msg_confirmDelGroup}".replace("{0}", "<strong class='text-red'>" + name + "</strong>");
	
	var target = $(this).closest("li");

	showConfirmModal(msg, function(result) {
		if (result) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: "${destroyGroupUrl}",
				data: JSON.stringify({ id: Number(id) }),
				success: function (data) {
					showDeleteSuccessMsg();
					
					target.remove();
				},
				error: ajaxDeleteError
			});
		}
	});
}


function editGroup(e) {		// 3 / 4
	
	e.preventDefault();
	
	var id = $(this).closest("li").attr("data-id");
	var name = $(this).closest("li").attr("data-name");
	
	
	$("#formRoot").html(kendo.template($("#template-group-edit").html()));
	
	$("#form-1").attr("rowid", id);
	$("#form-1 input[name='name']").val(name);

	
	$("#form-modal-1 .modal-dialog").draggable({ handle: ".modal-header" });
	$("#form-modal-1").modal();
	
	$("#form-modal-1").on("shown.bs.modal", function() {
		$("#form-1 input[name='name']").focus().select();
	})
}


function addMenus(e) {		// 4 / 4

	e.preventDefault();
	
	var id = $(this).closest("li").attr("data-id");
	var name = $(this).closest("li").attr("data-name");

	var title = "${label_groupFormat}".replace("{0}", "<strong class='text-primary'>" + name + "</strong>");
	
	$("#formRoot").html(kendo.template($("#template-menu-add").html()));

	$("#form-3 input[name='name']").tagsinput({
		trimValue: true,
		tagClass: 'badge badge-outline-secondary text-secondary',
		maxChars: 20,
		itemText: function(item) {
			return item.substr(0, 20);
		}
	});
	
	$("#form-3").attr("rowid", id);
	$("#form-3 span[name='title']").html(title);
	
	
	$('#form-modal-3 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-3").modal();
	
	$("#form-modal-3").on("shown.bs.modal", function() {
		$("#form-3 input[name='name']").tagsinput("focus");
	})
	
	$("#form-3 input[name='name']").on('beforeItemAdd', function(e) {
	    var element = $(this);
	    if(e.item.length > 20){
	    	e.cancel = true;
	    	element.tagsinput('add', e.item.substr(0, 20));
	    }
	});

}


//------------------------------------------------------------------- Menu item functions: 3 functions


function changeMenuVisible(e) {		// 1 / 3
	
	e.preventDefault();
	
	var id = $(this).closest("li").attr("data-id");

	var hidden = $("#m-vis-" + id).css('display') == 'none';
	
	var data = {
       	id: Number(id),
       	type: "M",
       	published: hidden ? "Y" : "N",		// 현재값 토글
    };

	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${updateVisibleUrl}",
		data: JSON.stringify(data),
		success: function (form) {
			showOperationSuccessMsg();
			
			if (hidden){
			    $("#m-vis-" + id).show();
			    $("#m-invis-" + id).hide();
			} else {
			    $("#m-vis-" + id).hide();
			    $("#m-invis-" + id).show();
			}
		},
		error: ajaxOperationError
	});
}

//등록된 메뉴 정보 가져져기
function editMenu(e) {		// 2 / 3
	
	e.preventDefault();
	
	var id = Number($(this).closest("li").attr("data-id"));

	if (id != null) {
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			data: JSON.stringify({ id: id }),
			url: "${readMenuUrl}",
			success: function (data) {
				
				var itemTemplate = kendo.template($("#template-opt-menu-item").html());
				
				// init
				$("#formRoot").html(kendo.template($("#template-menu-edit").html()));
				
				$("#form-4 select[name='badgeType']").selectpicker('render');
				$("#form-4 select[name='visibleType']").selectpicker('render');

				
				bootstrapSelectVal($("#form-4 select[name='badgeType']"), "");
				bootstrapSelectVal($("#form-4 select[name='visibleType']"), "Y");

				$("#form-4 textarea[name='intro']").keypress(function (e) {
					if (e.keyCode != 13) {
						return;
					}
					
					$(this).text().replace(/\n/g, "");
					
					return false;
				});
				
				$("#form-4").validate({
					rules: {
						price: {
							maxlength: 10, number: true,
						},
					}
				});
				
				// edit
				$("#form-4").attr("rowid", id);
				
				$("#form-4 input[name='name']").val(data.menu.name);
				$("#form-4 input[name='price']").val(data.menu.price);
				$("#form-4 input[name='code']").val(data.menu.code);
				
				bootstrapSelectVal($("#form-4 select[name='badgeType']"), data.menu.flagType);
				bootstrapSelectVal($("#form-4 select[name='visibleType']"), data.menu.published);
// 				bootstrapSelectVal($("#form-4 select[name='eventSelect']"), data.menu.event);
				$("#form-4 select[name='eventSelect']").val( data.menu.event);
// 				$("#form-4 select[name='eventId']").val( data.menu.event.id);

				$("#form-4 textarea[name='intro']").val(data.menu.intro);
				
				$("#form-4 input[name='sold-out']").prop("checked", data.menu.soldOut);

				previewFile = "/m/menus/${storeRoot}/";
				imgFilename = "";
				
				if (data.menu.menuImage) {
					$("#form-4 input[name='imgFilename']").val(data.menu.menuImage.orgFilename);
					
					imgFilename = data.menu.menuImage.filename;
					if (data.menu.menuImage.orgFilename) {
						previewFile = "/m/menus/${storeRoot}/" + imgFilename;
					}
				}
				
				if (data.mandatoryMenus.length > 0) {
					for (var i = 0; i < data.mandatoryMenus.length; i++) {
						$("#man-sel-menu-root").append(itemTemplate(data.mandatoryMenus[i]));
						addOptMenuEventActions("M");
					}
				} else {
					$('#man-sel-menu-root').html("<tr data-id='noData'><td colspan='3'><div class='d-flex align-items-center justify-content-center py-4 text-center w-100'>${control_noRows}</div></td></tr>");
				}
				
				if (data.optionalMenus.length > 0) {
					for (var i = 0; i < data.optionalMenus.length; i++) {
						$("#opt-sel-menu-root").append(itemTemplate(data.optionalMenus[i]));
						addOptMenuEventActions("O");
					}
				} else {
					$('#opt-sel-menu-root').html("<tr data-id='noData'><td colspan='3'><div class='d-flex align-items-center justify-content-center py-4 text-center w-100'>${control_noRows}</div></td></tr>");
				}
				
				$('#form-4 .nav-tabs a').on('shown.bs.tab', function(e){
					var id = $(e.target).attr("id");
					
					if (id == "menu-man") {
						callerType = "M";
					} else if (id == "menu-opt") {
						callerType = "O";
					}
				});
				
				
				$('#form-modal-4 .modal-dialog').draggable({ handle: '.modal-header' });
				$("#form-modal-4").modal();
				couponData = data.menu.event;
				console.log(couponData);
				eventSelect(data.menu.store.id);
			},
			error: ajaxReadError
		});
	}
}


function deleteMenu(e) {		// 3 / 3
	
	e.preventDefault();
	
	var id = $(this).closest("li").attr("data-id");
	var name = $(this).closest("li").attr("data-name");
	
	var msg = "${msg_confirmDelMenu}".replace("{0}", "<strong class='text-red'>" + name + "</strong>");
	
	var target = $(this).closest("li");
	var targetParent = $(target).closest("ol");
	var siblingCount = $(targetParent).find("li").length;
	
	showConfirmModal(msg, function(result) {
		if (result) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: "${destroyMenuUrl}",
				data: JSON.stringify({ id: Number(id) }),
				success: function (data) {
					showDeleteSuccessMsg();
					
					target.remove();
					
					if (siblingCount == 1) {
						$(targetParent).closest("li").find("button[data-action='expand']").remove();
						$(targetParent).closest("li").find("button[data-action='collapse']").remove();
						
						targetParent.remove();
					}
				},
				error: ajaxDeleteError
			});
		}
	});
}


//------------------------------------------------------------------- functions for optional menus 

//옵션 추가
function addOptMenu() {

	$("#formRootLevel2").html(kendo.template($("#template-opt-menu-add").html()));

	$("#form-5 input[name='menus']").tagsinput({
		trimValue: true,
		tagClass: 'badge badge-outline-secondary text-secondary',
		maxChars: 50,
	});
	
	$("#form-5 input[name='menus']").on('beforeItemAdd', function(e) {
		
		e.preventDefault();
		
	    e.cancel = !validateTagInputValue(e.item);
	});
 
	$("#form-5").validate({
		rules: {
			name: { required: true },
		}
	});
	
	
	$('#form-modal-5 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-5").modal();
	
	$("#form-modal-5").on("shown.bs.modal", function() {
		setTimeout(function(){
			$('.modal-backdrop:last-child').addClass('modal-level-plus-1');
		});
		
		$("#form-5 input[name='name']").focus();
	})
}

//옵션 메뉴 추가
function closeOptMenuAdd() {

	var name = $.trim($("#form-5 input[name='name']").val());
	var menus = $.trim($("#form-5 input[name='menus']").val());
	if(name == ""){
		showAlertModal("danger", "공백 문자만 있는 이름은 허용되지 않습니다. ");
		return;
	}

 	if ($("#form-5").valid() && hasAllowedString2(menus)) {
		var rootTag = "";
		if (callerType == "M") {
			rootTag = "#man-sel-menu-root";
		} else if (callerType == "O") {
			rootTag = "#opt-sel-menu-root";
		}
		
		var row = $(rootTag + " tr").last();
		
		var data = {id: Math.floor(Math.random() * 100000) - 200000, name: name, count: menus.split(",").length, menus: menus}

		if ($(row).attr("data-id") == "noData") {
			$(rootTag).html("");
		}
		
		var itemTemplate = kendo.template($("#template-opt-menu-item").html());
		
		$(rootTag).append(itemTemplate(data));
		addOptMenuEventActions(callerType);
		
		$("#form-modal-5").modal("hide");
	} 
}


// function validateTagInputValue(str){
	
// 	var menus = str.split(" ");
	
// 	if(menus.length > 1) {
// 		var moneyInput = menus[menus.length - 1];
		
// 		if(!$.isNumeric(moneyInput)){
// 			showToastNotification("error", "${tip_checkPrice}");
// 			return false;
// 		}
		
// 		 return true;
// 	} else {
// 		showToastNotification("error", "${tip_checkBlank}");
// 		return false;
// 	}
// }

function validateTagInputValue(str){
	
	var menus = str.split(" ");
	var row = $(this).closest("tr");
	console.log(menus);
	var names = $("#form-6 input[name='menus']").val();
	console.log(names);


	if(menus.length > 1) {
		var moneyInput = menus[menus.length - 1];
		var menuType = menus[0];
		console.log(menuType);
		console.log(menus[0]);

		if(names !=null){
			var stnames = names.replace(/[0-9]/g,""); // 숫자제거
			var onenames = stnames.split(",");
			console.log(stnames);
			console.log(onenames);
			if(onenames.find(element => element == menuType + " ")){
				showToastNotification("error", "${tip_checkMenu}");
				return false
			}
		}
			

		if(!$.isNumeric(moneyInput)){
			showToastNotification("error", "${tip_checkPrice}");
			return false;
		}

		if(menuType == names) {
			showToastNotification("error", "${tip_checkBlank}");
			return false;
		}
		
		 return true;
	} else {
		showToastNotification("error", "${tip_checkBlank}");
		return false;
	}
}




function addOptMenuEventActions(type) {

	var rootObj = "";
	if (type == "M") {
		rootObj = $("#man-sel-menu-root tr").last();
	} else if (type == "O") {
		rootObj = $("#opt-sel-menu-root tr").last();
	}
		
	$(rootObj).find(".edit").on("click", optMenuEdit);
	$(rootObj).find(".up").on("click", optMenuGoUp);
	$(rootObj).find(".down").on("click", optMenuGoDown);
	$(rootObj).find(".arrange").on("click", optMenuReorder);
	$(rootObj).find(".del").on("click", optMenuDelete);
		
	$(rootObj).find(".menuhint").attr("data-content", beautifyMenus($(rootObj).attr("data-menus")));
	$(rootObj).find(".menuhint").popover({html:true, container: ".tab-content"});
}


function beautifyMenus(str) {

	var itemTemplate = kendo.template($("#template-opt-menu-item-items").html());
	
	var ret = "";
	
	if (str) {
		var strSplit = str.split(',');
		for (var i in strSplit) {
			ret = ret + itemTemplate(strSplit[i]);
		}
	}
		
	return ret;
}


function optMenuEdit(e) {
	
	e.preventDefault();
	
	$("#formRootLevel2").html(kendo.template($("#template-opt-menu-edit").html()));
	
    var row = $(this).closest("tr");
    
	$("#form-6").attr("rowid", row.attr("data-id"));
	
	$("#form-6 input[name='name']").val(row.attr("data-name"));
	$("#form-6 input[name='menus']").val(row.attr("data-menus"));

	$("#form-6 input[name='menus']").tagsinput({
		trimValue: true,
		tagClass: 'badge badge-outline-secondary text-secondary',
		maxChars: 50,
	});
	
	$("#form-6 input[name='menus']").on('beforeItemAdd', function(e) {
		
		e.preventDefault();
		
	    e.cancel = !validateTagInputValue(e.item);
	});
	 
	$("#form-6").validate({
		rules: {
			name: { required: true },
		}
	});
	
	
	$('#form-modal-6 .modal-dialog').draggable({ handle: '.modal-header' });
	$("#form-modal-6").modal();
	
	$("#form-modal-6").on("shown.bs.modal", function() {
		setTimeout(function(){
			$('.modal-backdrop:last-child').addClass('modal-level-plus-1');
		});
	})
}


function closeOptMenuEdit() {
	
	var name = $.trim($("#form-6 input[name='name']").val());
	var menus = $.trim($("#form-6 input[name='menus']").val());
	var id = $("#form-6").attr("rowid");
	if(name == ""){
		showAlertModal("danger", "공백 문자만 있는 이름은 허용되지 않습니다. ");
		return;
	}
	
 	if ($("#form-6").valid() && hasAllowedString2(menus)) {
		var rootTrObj = null;
		if (callerType == "M") {
			rootTrObj = "#man-sel-menu-root tr";
		} else if (callerType == "O") {
			rootTrObj = "#opt-sel-menu-root tr";
		}
		
		if (rootTrObj) {
			$(rootTrObj).each(function(i, obj) {
				if (id == $(obj).attr("data-id")) {
					var data = {id: id, name: name, count: menus.split(",").length, menus: menus}
					var itemTemplate = kendo.template($("#template-opt-menu-item").html());

					$(obj).after(itemTemplate(data));
					obj.remove();
					
					return false;
				}
			})
			
			$(rootTrObj).each(function(i, obj) {
				if (id == $(obj).attr("data-id")) {
					$(obj).find(".edit").on("click", optMenuEdit);
					$(obj).find(".up").on("click", optMenuGoUp);
					$(obj).find(".down").on("click", optMenuGoDown);
					$(obj).find(".arrange").on("click", optMenuReorder);
					$(obj).find(".del").on("click", optMenuDelete);
					
					$(obj).find(".menuhint").attr("data-content", beautifyMenus($(obj).attr("data-menus")));
					$(obj).find(".menuhint").popover({html:true, container: ".tab-content"});
					
					return false;
				}
			})
			
			$("#form-modal-6").modal("hide");
		}
	}
}


function optMenuGoUp(e) {
	
	e.preventDefault();
	
    var row = $(this).closest("tr");
    if (row.prev("tr").length) {
		row.insertBefore(row.prev("tr"));
    }
}


function optMenuGoDown(e) {
	
	e.preventDefault();
	
    var row = $(this).closest("tr");
    if (row.next("tr").length) {
        row.insertAfter(row.next("tr"));
    }
}


function optMenuReorder(e) {
	
	e.preventDefault();
	
    var row = $(this).closest("tr");

    var menus = $(row).attr("data-menus");
    if (menus && menus.split(",").length > 0) {
    	$("#formRootLevel2").html(kendo.template($("#template-opt-menu-reorder").html()));
        
    	$("#form-7").attr("rowid", row.attr("data-id"));
    	
    	var itemTemplate = kendo.template($("#template-opt-menu-item-reorder-item").html());
    	
    	var menuArray = menus.split(",");
    	for(var i = 0; i < menuArray.length; i++) {
        	$("#reorder-root-div").append(itemTemplate(menuArray[i]));
    	}
    	Sortable.create(document.getElementById("reorder-root-div"), { animation: 150 });
    	
    	
    	$('#form-modal-7 .modal-dialog').draggable({ handle: '.modal-header' });
    	$("#form-modal-7").modal();
    	
    	$("#form-modal-7").on("shown.bs.modal", function() {
    		setTimeout(function(){
    			$('.modal-backdrop:last-child').addClass('modal-level-plus-1');
    		});
    	})
    }
}


function closeOptMenuReorder() {

	var menus = "";
	$("#reorder-root-div span").each(function(i, obj) {
		if (menus) {
			menus = menus + ",";
		}
		menus = menus + $(obj).attr("data-menu");
	})

	if (menus) {
		var id = $("#form-7").attr("rowid");
		
		var rootTrObj = null;
		if (callerType == "M") {
			rootTrObj = "#man-sel-menu-root tr";
		} else if (callerType == "O") {
			rootTrObj = "#opt-sel-menu-root tr";
		}
		
		if (rootTrObj) {
			$(rootTrObj).each(function(i, obj) {
				if (id == $(obj).attr("data-id")) {
					var name = $(obj).attr("data-name");
					
					var data = {id: id, name: name, count: menus.split(",").length, menus: menus}
					var itemTemplate = kendo.template($("#template-opt-menu-item").html());

					$(obj).after(itemTemplate(data));
					obj.remove();
					
					return false;
				}
			})
			
			$(rootTrObj).each(function(i, obj) {
				if (id == $(obj).attr("data-id")) {
					$(obj).find(".edit").on("click", optMenuEdit);
					$(obj).find(".up").on("click", optMenuGoUp);
					$(obj).find(".down").on("click", optMenuGoDown);
					$(obj).find(".arrange").on("click", optMenuReorder);
					$(obj).find(".del").on("click", optMenuDelete);
					
					$(obj).find(".menuhint").attr("data-content", beautifyMenus($(obj).attr("data-menus")));
					$(obj).find(".menuhint").popover({html:true, container: ".tab-content"});
					
					return false;
				}
			})
			
			$("#form-modal-7").modal("hide");
		}
	}
}


function optMenuDelete(e) {
	
	e.preventDefault();
	
	var root = $(this).closest("tbody");
    var row = $(this).closest("tr");
    
    row.remove();
    
    if (root.find("tr").last().length == 0) {
    	root.html("<tr data-id='noData'><td colspan='3'><div class='d-flex align-items-center justify-content-center py-4 text-center w-100'>${control_noRows}</div></td></tr>");
    }
}

//-------------------------------------------------------------------


function initState(value) {

	if (value) {
		$("#exp-col-switcher").prop('disabled', false);
		$("#move-lock-switcher").prop('disabled', false);
		$("#action-btn").removeClass("disabled");
	} else {
		$("#exp-col-switcher").prop('disabled', true);
		$("#move-lock-switcher").prop('disabled', true);
		$("#action-btn").addClass("disabled");
		
		$('#menu-nestable-root').html("<div class='d-flex align-items-center justify-content-center py-4 text-center w-100'>${wait_pleaseWait}</div>");
	}
}


function readStoreMenus() {
	
	initState(false);
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "${readUrl}",
		success: function (data, status) {

			var groupItemTemplate = kendo.template($("#template-group-item").html());
			var menuItemTemplate = kendo.template($("#template-menu-item").html());
			
			$("#menu-nestable-root").html("");
			if(data.length > 0){
				for (var i = 0; i < data.length; i++) {
					$("#menu-nestable-root").append(groupItemTemplate(data[i]));
					
					for (var j = 0; j < data[i].menus.length; j++) {
						$("#group-root-" + data[i].id).append(menuItemTemplate(data[i].menus[j]));
					}
				}

				initializeNestable();
			} else {
				$('#menu-nestable-root').html("<div class='d-flex align-items-center justify-content-center py-4 text-center w-100'>${control_noRows}</div>");
			}
		},
		error: ajaxReadError
	});
}


function initializeNestable() {
	
	initState(true);
	initNestableRequired = false;
	
    $('#menu-nestable').nestable({
    	maxDepth:2,
    	dropCallback: dropCallback,
        reject: [{
            rule: function (draggedElement) {
            	var dataType = $(draggedElement).attr("data-type");
                var hierachy = $(draggedElement).parentsUntil('.dd', 'ol');

                if (hierachy.length == 1) {		// depth 0에 drop
                	caseCode = "M";
                	return !(dataType && dataType == "G");
                } else if (hierachy.length == 2) {		// depth 1에 drop
                	caseCode = "G";
                	return dataType && dataType == "G";
                }
                
                return false;
            },
            action: function (nestable) {
            	if (caseCode) {
            		if (caseCode == "M") {
            			showAlertModal("danger", "${msg_noRootMenu}");
            		} else if (caseCode == "G") {
            			showAlertModal("danger", "${msg_noGroupInGroup}");
            		}
            		caseCode = "";
            	}
            }
        }],
	});
    
    $('#menu-nestable').nestable("disabled");

    
	$("#menu-nestable a[name='chg-group-vis-btn']").click(changeGroupVisible);
	$("#menu-nestable a[name='edit-group-btn']").click(editGroup);
	$("#menu-nestable button[name='add-menu-btn']").click(addMenus);
	$("#menu-nestable button[name='del-group-btn']").click(deleteGroup);
    
	$("#menu-nestable a[name='chg-menu-vis-btn']").click(changeMenuVisible);
	$("#menu-nestable a[name='edit-menu-btn']").click(editMenu);
	$("#menu-nestable button[name='del-menu-btn']").click(deleteMenu);
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


function hasAllowedString2(str) {

	if (str == "") {
		showAlertModal("danger", "${msg_blankMenu}");
		return false;
	} else if (str.indexOf("'") > -1 || str.indexOf("\"") > -1) {
		showAlertModal("danger", "${msg_noQuot}");
		return false;
	}
	
	return true;
}


function saveMenuGroup() {
	
	var name = $.trim($("#form-1 input[name='name']").val());
	
	if ($("#form-1").valid() && hasAllowedString(name)) {
    	var data = {
           	id: Number($("#form-1").attr("rowid")),
        	name: name,
       	};
    	
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${updateGroupUrl}",
			data: JSON.stringify(data),
			success: function (form) {
				showSaveSuccessMsg();
				$("#form-modal-1").modal("hide");
				
				$("#li-group-root-" + form.id).attr("data-name", form.name);
				$("#G-name-" + form.id).html(form.name);
			},
			error: ajaxSaveError
		});
	}
}


function saveMenuGroups() {
	
	var name = $.trim($("#form-2 input[name='name']").val());
	
	if (hasAllowedString(name)) {
		
		var items = $("#form-2 input[name='name']").tagsinput("items");
		if (items.length > 0) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: "${createGroupUrl}",
				data: JSON.stringify({ items: items }),
				success: function (data) {
					showSaveSuccessMsg();
					$("#form-modal-2").modal("hide");
					
					var groupItemTemplate = kendo.template($("#template-group-item").html());
					
					if (initNestableRequired) {
						$("#menu-nestable-root").html("");
					}
					
					if(data.length > 0){
						for (var i = 0; i < data.length; i++) {
							$("#menu-nestable-root").append(groupItemTemplate(data[i]));
							
							if (!initNestableRequired) {
								$("#li-group-root-" + data[i].id + " a[name='chg-group-vis-btn']").click(changeGroupVisible);
								$("#li-group-root-" + data[i].id + " a[name='edit-group-btn']").click(editGroup);
								$("#li-group-root-" + data[i].id + " button[name='add-menu-btn']").click(addMenus);
								$("#li-group-root-" + data[i].id + " button[name='del-group-btn']").click(deleteGroup);
							}
						}

						if (initNestableRequired) {
							initializeNestable();
						}
					}
				},
				error: ajaxSaveError
			});
		}
	}
}


function dropCallback(details) {
	
	if (details != null && details.updateRequired) {
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${dropItemUrl}",
			data: JSON.stringify(details),
			success: function (form) {
				showOperationSuccessMsg();
			},
			error: ajaxOperationError
		});
	}
}


function saveMenus() {
	
	var name = $.trim($("#form-3 input[name='name']").val());
	var groupId = Number($("#form-3").attr("rowid"));
	
	if (hasAllowedString(name)) {
		var items = $("#form-3 input[name='name']").tagsinput("items");
		if (items.length > 0) {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: "${createMenuUrl}",
				data: JSON.stringify({ id: groupId, items: items }),
				success: function (data) {
					showSaveSuccessMsg();
					$("#form-modal-3").modal("hide");

					var menuItemTemplate = kendo.template($("#template-menu-item").html());
					if(data.length > 0){
						if (!$("#group-root-" + groupId).length) {
							$("#li-group-root-" + groupId).prepend("<button data-action='expand' type='button' style='display: none;'>Expand</button>");
							$("#li-group-root-" + groupId).prepend("<button data-action='collapse' type='button' style='display: block;'>Collapse</button>");
							$("#li-group-root-" + groupId).append("<ol class='dd-list' id='group-root-" + groupId + "'></ol>");
						}
						
						for (var i = 0; i < data.length; i++) {
							$("#group-root-" + groupId).append(menuItemTemplate(data[i]));
						    
							$("#li-menu-root-" + data[i].id + " a[name='chg-menu-vis-btn']").click(changeMenuVisible);
							$("#li-menu-root-" + data[i].id + " a[name='edit-menu-btn']").click(editMenu);
							$("#li-menu-root-" + data[i].id + " button[name='del-menu-btn']").click(deleteMenu);
						}
					}
					
				},
				error: ajaxSaveError
			});
		}
	}
}


function getRowData(root) {
	
	var data = [];

	if ($(root).length == 0) {
		return data;
	} else if ($(root).length == 1 && $(root).first().attr("data-id") == "noData") {
		return data;
	}

	$(root).each(function(i, obj) {
		data[i] = {id: Number($(obj).attr("data-id")), name: $(obj).attr("data-name"), seq: i * 10 + 10, menus: $(obj).attr("data-menus") };
	})
	
	return data;
}

//메뉴 저장
function saveMenu() {
	
	if ($("#form-4").valid()) {
    	var data = {
       		id: Number($("#form-4").attr("rowid")),
       		name: $.trim($("#form-4 input[name='name']").val()),
       		price: $("#form-4 input[name='price']").val().replace(/,/g, ""),
       		code: $.trim($("#form-4 input[name='code']").val()),
       		soldOut: $("#form-4 input[name='sold-out']").is(':checked') ? "Y" : "N",
       		
			menuImage: $.trim($("#form-4 input[name='imgFilename']").val()),
			menuUniqueName: imgFilename,
			eventSelect: $.trim($("#form-4 select[name='eventSelect']").val()),
       		badgeType: $("#form-4 select[name='badgeType']").val(),
       		visibleType: $("#form-4 select[name='visibleType']").val(),
       		
    		intro: $.trim($("#form-4 textarea[name='intro']").val()),
    		
    		manMenus: getRowData("#man-sel-menu-root tr"),
    		optMenus: getRowData("#opt-sel-menu-root tr"),
       	};
    	
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${updateMenuUrl}",
			data: JSON.stringify(data),
			success: function (form) {
				showSaveSuccessMsg();
				$("#form-modal-4").modal("hide");
				
				if (form != null) {
					$("#li-menu-root-" + form.id).attr("data-name", form.name);
					$("#M-name-" + form.id).html(form.name);
					
					if (form.published){
					    $("#m-vis-" + form.id).show();
					    $("#m-invis-" + form.id).hide();
					} else {
					    $("#m-vis-" + form.id).hide();
					    $("#m-invis-" + form.id).show();
					}
				}
			},
			error: ajaxSaveError
		});
	}
}


function viewImg() {
	
	var filename = $("#form-4 input[name='imgFilename']").val();
	if (filename) {
		viewUnivImage(previewFile);
	}
}


function viewTempImg() {
	
	var filename = $("#wizard-form input[name='uploadImgFilename']").val();
	var url = "/uptemp/" + filename;
	
	viewUnivImage(url);
}


function uploadImage() {
	
	$("#formRootLevel2").html(kendo.template($("#sw-template").html()));
	
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
			$("#wizard-form input[name='uploadImgFilename']").val(e.files[0].name);
			
			$("#wizard-form div[name='smartWizard']").smartWizard("next");
		},
		validation: {
			allowedExtensions: ${uploadModel.allowedExtensions}
		},
	});
	
	$("#wizard-modal").on('show.bs.modal', function (e) {
		setTimeout(function(){
			$('.modal-backdrop:last-child').addClass('modal-level-plus-1');
		});
	});
	
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
    				
    				$("#form-4 input[name='imgFilename']").val(uploadedFilename);
    				previewFile = "/uptemp/" + form;
    				imgFilename = form;
    			},
    			error: ajaxSaveError
    		});
		}
	}
}


function refreshContent() {
	
	showConfirmModal("${msg_refresh}", function(result) {
		if (result) {
			showWaitModal();
			$.ajax({
				type: "POST",
				contentType: "application/json",
				dataType: "json",
				url: "${refreshUrl}",
				success: function (data) {
					hideWaitModal();
					showOperationSuccessMsg();
				},
				error: function(e) {
					hideWaitModal();
					ajaxOperationError(e);
				}
			});
		}
	});
}

function eventSelect(id){
	var data = {
		storeId : id
   	};
	$("#form-4 select[name='eventSelect']").kendoDropDownList({
		dataTextField: "eventName",
		dataValueField: "eventName",
        dataSource: {
			transport: {
				read: {
					type: "POST",
					contentType: "application/json",
					dataType: "json",
					url: "${eventListUrl}",
					data: data
				},
				parameterMap: function (data) {

                	return JSON.stringify(data);
				},
			},
			error: function(e) {
      			showReadErrorMsg();
			}
        },
        optionLabel: {
        	eventName: "", value: "",
		},
        delay: 500,
    });
	couponPolicySelect()
}

function couponPolicySelect(){
	$("#form-4 input[name=eventId]").each(function(index){
		console.log(couponData);
					$(this).parent().parent().find("select[name='eventSelect']").data("kendoDropDownList").value(couponData);
	});
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
