<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- Taglib -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ taglib prefix="func" tagdir="/WEB-INF/tags/func"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>


<!-- URL -->

<c:url value="/pay/store/create" var="createUrl" />
<c:url value="/pay/store/read" var="readUrl" />
<c:url value="/pay/store/update" var="updateUrl" />
<c:url value="/pay/store/destroy" var="destroyUrl" />

<c:url value="/pay/store/changeKey" var="changeKeyUrl" />


<!-- Opening tags -->

<common:pageOpening />


<!-- Page title -->

<div class="text-center">
	<h3 class="pt-1 pb-3 mb-3">
		${pageTitle}
	</h3>
</div>

<hr class="border-light container-m--x mt-0 mb-4">





<!-- Page body -->



<div class="mb-4 select-desc text-center">
	${desc_chooseStore}
</div>

<c:forEach var="item" items="${tempStoreList}">
	<c:choose>
		<c:when test="${sessionScope['currentStoreId'] eq item.storeId}">

<div class="mx-auto max-one-column" onclick="changeStore(${item.storeId})">
	<div class="card-hover selected mb-3">
		<div class="p-0">
			<div class="card p-3 sel-border">
				<div class="d-flex align-items-center media">
					<div class="pr-3 pl-2">
						<svg class="item-icon">
							<use xlink:href="/resources/shared/linear-icon.svg#store"></use>
						</svg>
					</div>
					<div class="store-item">
						<span>${item.storeName}</span>
						<span class="pl-3">${item.shortName}</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

		</c:when>
		<c:otherwise>

<div class="mx-auto max-one-column" onclick="changeStore(${item.storeId})">
	<div class="card-hover selected mb-3">
		<div class="p-0">
			<div class="card p-3 border">
				<div class="d-flex align-items-center media">
					<div class="pr-3 pl-2">
						<svg class="item-icon">
							<use xlink:href="/resources/shared/linear-icon.svg#store"></use>
						</svg>
					</div>
					<div class="store-item">
						<span>${item.storeName}</span>
						<span class="pl-3">${item.shortName}</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

		</c:otherwise>
	</c:choose>
</c:forEach>


<style>

.card-hover .border {
    border: 1px solid rgba(24,28,33,0.06);
}

.card-hover:hover .border {
	color: white;
	background-color: #4c84ff !important;
}

.card-hover .sel-border {
	border-color: #4c84ff !important;
	border-width: 2px;
}

.card-hover:hover .sel-border {
	color: white;
	background-color: #4c84ff !important;
}

.select-desc {
	font-size: 1.25rem;
	line-height: 1.2;
	font-weight: 300;
}

.store-item {
	font-size: 1.25rem;
	line-height: 1.1;
	font-weight: 300;
}

.item-icon {
	width: 32px; height: 32px; fill: currentColor;
}

</style>	

<style>

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

</style>



<!--  Scripts -->

<script>

$(document).ready(function() {

	$("#menu-StoreMgmt").addClass("open");
	
});	


function changeStore(id) {
	
	location.href = "/changestore?storeId=" + id + "&uri=${nextUrl}";
}

</script>

<!--  / Scripts -->


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
