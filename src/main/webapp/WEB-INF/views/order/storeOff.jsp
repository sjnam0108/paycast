<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="${html_lang}">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>${storeName}</title>
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/reset.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/ui.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/jquery-ui.min.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/style.css">
	
	<link rel="stylesheet" href="/resources/vendor/css/bootstrap.css">
    <link rel="stylesheet" href="/resources/vendor/css/appwork.css">
    <link rel="stylesheet" href="/resources/vendor/css/theme-bbmc-twitlight-blue.css">
    <link rel="stylesheet" href="/resources/vendor/css/colors.css">
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="/resources/selfmenu/menuList/js/jquery-ui.min.js"></script>
</head>
<body>
	<div class="wrapper reset">
		<div class="header taC">
			<c:choose>
				<c:when test="${mobileLogoType eq 'I'}">
					<div class="logo"><img src="${storeDownLocation}/${mLogoImageFilename}" alt=""></div>	
				</c:when>
				<c:otherwise>
					<div class="logo"><h1>${mobileLogoText}</h1></div>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="container order">
			<div class="inner">
				<div class="order_list">
					<div class="card mb-4" >
						<div class="card-body">
							<div style="text-align: left;">
								<c:choose>
									<c:when test="${openType eq 'O'}">
										<c:choose>
											<c:when test="${mobileOrderAllowed}">
												<h2>${msg_MOEnabled}</h1>
											</c:when>
											<c:otherwise>
												<h2>${msg_MOAllowed}</h1>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<h2>${msg_closeStore}</h2>
									</c:otherwise>
								</c:choose>
							</div>			
							<div class="msg_box">
								<h3 style="padding-top: 60px;">${msg_openHours}<br />${desc}</h3>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>