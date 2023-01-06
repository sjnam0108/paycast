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


<div class="mb-4 select-desc text-center text-muted">
	<svg class="item-icon">
		<use xlink:href="/resources/shared/other-icon.svg#exclamation"></use>
	</svg>
</div>



<style>

.item-icon {
	width: 192px; height: 192px; fill: currentColor;
}

</style>	


<!-- / Page body -->





<!-- Functional tags -->

<func:cmmValidate />


<!-- Closing tags -->

<common:base />
<common:pageClosing />
