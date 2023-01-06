<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="${html_lang}">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>${storeName}</title>
	<link rel="stylesheet" href="/resources/vendor/css/appwork.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/reset.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/ui.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/jquery-ui.min.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/style.css">
	<link rel="stylesheet" href="/resources/vendor/lib/swiper/swiper.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="/resources/selfmenu/menuOrderList/js/jquery-ui.min.js"></script>
	<script src="/resources/selfmenu/menuList/js/appMenu.js"></script>
	<script src="/resources/vendor/lib/swiper/swiper.min.js"></script>
</head>
<body>
	<div class="wrapper reset" id="orderPageCom">
		<input type="hidden" id="storeKey" value="${storeKey}" />
		<input type="hidden" id="basket" value="${basket}" />
		<input type="hidden" id="table" name="table" value="${table}" />
		<input type="hidden" id="time" name="time" value="${time}" />
		
		<div style="position:absolute; font-size: 15px; margin-top: 22px; margin-left: 82%;">
			<span  onclick="popup1Open();" >${pay_order}${pay_refer}</span>
		</div>
		<div class="header taC">
			<c:choose>
				<c:when test="${mobileLogoType eq 'I'}">
					<div class="logo"><img src="${storeDownLocation}/${mLogoImageFilename}" alt=""></div>	
				</c:when>
				<c:otherwise>
					<div class="logo">${mobileLogoText}</div>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="container order">
			<div class="inner">
				<div class="order_list">
					<c:if test="${not empty payitemList}">
						<c:forEach var="item" items="${payitemList}" varStatus="st" >
							<div class="order_menu">
								<a href="#" class="del" onclick="menuDelete(${item.id}, '${item.compSelect}')"><span class="txtX">×</span></a>
								<div class="img_wrap">
									<img src="${item.imgSrc}" alt="${item.name}" style="margin-bottom: 45%;">
								</div>
								<div class="infoWrapTotal">
									<h2 class="name">${item.name}</h2>
									<div class="row" style="font-size: 12px;">${item.essNameText}${item.addNameText}</div>
									<div class="row">
										<div class="info_wrap">
											<span class="price">${item.toPrice}</span>
										</div>
										<div class="count_wrap">
											<a href="#none" class="down_count">-</a>
											<span class="count" compSelect="${item.compSelect}">${item.orderCount}</span>
											<a href="#none" class="up_count">+</a>
										</div>
									</div>
									<div class="total">
										<span class="price"><span class="label">${pay_total}</span> ${item.toPrice * item.orderCount}${pay_won}</span>
									</div>
									<input type="hidden" name="compSelect" value="${item.compSelect}" />
									<input type="hidden" name="basketLiId" value="${item.id}" />
									<input type="hidden" name="menuId" value="${item.menuId}" />
									<input type="hidden" name="name" value="${item.name}" />
									<input type="hidden" name="price" value="${item.price}" />
									<input type="hidden" name="toPrice" value="${item.toPrice}" />
									<input type="hidden" name="orderCount" value="${item.orderCount}" />
									<input type="hidden" name="packing" value="${item.packing}" />
									<input type="hidden" name="essVal" value="${item.essVal}" />
									<input type="hidden" name="essName" value="${item.essName}" />
									<input type="hidden" name="addVal" value="${item.addVal}" />
									<input type="hidden" name="addName" value="${item.addName}" />
								</div>
							</div>
						</c:forEach>
					</c:if>
				</div>
				<div class="menu_add">
					<a href="#" class="button medium yell full circle" onclick="menuPageGO();">${pay_menu_Add}</a>
				</div>
				<div class="order_total">
					<span class="label">${pay_payPrice}</span> <span class="price"> ${goodsAmt}${pay_won}</span>
				</div>
			</div>
		</div>
		
		<div class="order_btn">
			<a href="#none" class="button large red full circle" onclick="compltePage();">${pay_payment}</a>
		</div>
		<div class="popup_area1">
			<div class="close">
				<span class="txtX">×</span>
				${pay_shut}</div>
			<div class="swiper-container">
			  <div class="swiper-wrapper">
			  </div>
			    <!-- Add Pagination -->
			    <div class="swiper-pagination" style="font-size: 16px; padding-left: 36%;"></div>
			    <!-- Add Arrows -->
			    <div class="swiper-button-next" ></div>
			    <div class="swiper-button-prev" ></div>
			</div>
		</div>
	</div>
	<c:choose>
		<c:when test="${storePay eq 'MS'}">
			<form id="orderForm" name="orderForm" method="post" Content-Type="application/json" action="/smilepay"></form>
		</c:when>
		<c:otherwise>
			<form id="orderForm" name="orderForm" method="post" Content-Type="application/json" action="/mobileOrder"></form>
		</c:otherwise>		
	</c:choose>
	
</body>
<style type="text/css">

.popup_area1 {display: none; width: 80%; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: #fff; z-index: 20; padding: 15px 20px 10px; box-sizing: border-box}
.popup_area1 .close {display: block; position: absolute; right: 0; top: 0; margin-top: -30px; color: #fff;  font-size: 22px; line-height: 20px; vertical-align: top;}
.popup_area1 .close .txtX {position: relative; top:4px ; margin-right: 3px;}


.swiper-container {
    width:100%;
    height:230px;
    border:5px solid silver;
    border-radius:7px;
    box-shadow:0 0 20px #ccc inset;
}
.swiper-slide {
    text-align:center;
  	-webkit-box-align: center;
  	-ms-flex-align: center;
  	-webkit-align-items: center;
  	align-items: center;
	-webkit-box-pack: center;
  	-ms-flex-pack: center;
  	-webkit-justify-content: center;
  	justify-content: center;
    font-size: 24px;
}

</style>

<script>
$(function() {
	setTimeout(timeOutPage, 3600000);
	
	function timeOutPage() {
		alert("장시간 입력이 없으셨습니다. \n첫 화면으로 이동합니다.");
		document.location.href="/menu?store=${storeKey}&table=${table}";
	}
	
	$(document).on("click",".logo",function(){
		document.location.href="/menu?store=${storeKey}&table=${table}";
	} );
	
	var swiper = new Swiper('.swiper-container', {
		slidesPerView: 1,
		centeredSlides: true,
		paginationClickable: true,
		observer: true,
		observeParents: true,
		pagination: {
			el: '.swiper-pagination',
			type: 'fraction',
			},
		navigation: {
			nextEl: '.swiper-button-next',
			prevEl: '.swiper-button-prev',
			}
	});
	
	var cookieEnabled = navigator.cookieEnabled;
	var cookieName = "";
	if (cookieEnabled){
		cookieName = 'bbmcorder_${storeId}';
		var cookieTF = unescape(getCookie(cookieName));
		var menuList = "";
		if("" == cookieTF){
			$(".swiper-pagination").remove();
			$(".swiper-button-next").remove();
			$(".swiper-button-prev").remove();
			
			menuList = '<div class="swiper-slide" style="margin-top:37%;"><h4>${msg_noOrder}</h4></div>';
			swiper.appendSlide(menuList);
		}else{
			var tmp1 = cookieTF.split(",");
			if(tmp1.length < 2){
				$(".swiper-button-next").remove();
				$(".swiper-button-prev").remove();
			}
			for( var i in tmp1 ){
				var tmp2 = tmp1[i].split("_");
				if(tmp2.length > 1){
					if(tmp2[1] > getDate()){
						menuList += '<div class="swiper-slide" style="margin-top:10%;">';
						menuList += stringDate(tmp2[1]);
						menuList += '<br /><br />${pay_ordernum} ['+tmp2[0]+']';
						menuList += '</div>';
					}else{
						if(tmp1.length < 2){
							$(".swiper-pagination").remove();
							$(".swiper-button-next").remove();
							$(".swiper-button-prev").remove();
							
							menuList = '<div class="swiper-slide" style="margin-top:100px;"><h4>${msg_noOrder}</h4></div>';
						}
					}
				}
			}
		}
		swiper.appendSlide(menuList);
	}else{
		$(".swiper-pagination").remove();
		$(".swiper-button-next").remove();
		$(".swiper-button-prev").remove();
		
		menuList = '<div class="swiper-slide" style="margin-top:30%;"><h4>${msg_noOrder}</h4></div>';
		swiper.appendSlide(menuList);
	}
	
	priceChangeFn();
	onFn();
});
  
Date.prototype.YYYYMMDDHHMMSS = function () {
    var yyyy = this.getFullYear().toString();
    var MM = pad(this.getMonth() + 1,2);
    var dd = pad(this.getDate()-1, 2);
    var hh = pad(this.getHours(), 2);
    var mm = pad(this.getMinutes(), 2);
    var ss = pad(this.getSeconds(), 2);

    return yyyy + MM + dd+ hh + mm + ss;
};

function getDate() {
    d = new Date();
    return d.YYYYMMDDHHMMSS();
}

function pad(number, length) {
    var str = '' + number;
    while (str.length < length) {
        str = '0' + str;
    }

    return str;
}

$('.popup_area1 .close').on('click', function(e){
	e.preventDefault();
	
	$('.dim').remove();
	$(this).parent().hide();
	$('body').removeClass('fix');
});

function onFn() {
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "/smilepay/onFn",
		data: JSON.stringify({ menuInTime: "${time}" }),
		success: function (data, status) {
			if (data == "N") {
				document.location.href="/menu?store=${storeKey}&table=${table}";
			}
		},
		error: function (data, status) {
			alert("TIME ERROR");
		}
	});
}


function menuPageGO() {
	document.location.href="/menu?store=${storeKey}&table=${table}";
}

</script>
<script>

function priceChangeFn(){
	var $orderMenuList = $("#orderPageCom .order_list").find(".order_menu");
	$orderMenuList.each(function(){
		var price = $(this).find(".info_wrap>.price").html();
		$(this).find(".info_wrap>.price").html(numberCom(price));
		var toPrice = $(this).find("input[name=toPrice]").val();
		var orderCount = $(this).find("input[name=orderCount]").val();
		$(this).find(".total .price").html('<span class="label">${pay_total}</span>  '+numberCom((toPrice*orderCount))+'${pay_won}');
	})
}

function popup1Open(){
	var popup = $('.popup_area1');
	popup.show();
	$('body').addClass('fix').append('<div class="dim"></div>');
}

function stringDate(value){
	var year = value.substring(0, 4);
	var month = value.substring(4, 6);
	var day = value.substring(6, 8);
	var hour = value.substring(8, 10);
	var minute = value.substring(10, 12);
	var second = value.substring(12, 14);
	
	return year+"."+month+"."+day+" "+hour+":"+minute+":"+second;
}

function numberCom(str){
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

$(document).ready(function () {
	$('.menu_type button').on('click', function(){
		$(this).parent().toggleClass('on');
		$(this).next().slideToggle();
	});
	
	$('.order_menu .del').on('click', function(e){
		e.preventDefault();
		$(this).parents('.order_menu').remove();
	});
	
	$(document).on("click",".down_count",function(){
		var $home = $(this).parent().find(".count");
		var stat = $home.text();
		var num = parseInt(stat,10);
		num--;
		
		if(num<=0){
			num =1;
		}
		$home.text(num);
		countChg($(this), num);
	} );
	$(document).on("click",".up_count",function(){
		var $home = $( this ).parent().find(".count");
		var stat = $home.text();
		var num = parseInt(stat,10);
		num++;
		if(num<=0){
			num =1;
		}
		$home.text(num);
		countChg($(this), num);
	} );
});

function popupClose(){
	$('.dim').remove();
	$('.popup_area').hide();
	$('body').removeClass('fix');
}

function menuDelete(basketLiId, compSelect){
	var data = {
		compSelect: compSelect,
		basketLiId: basketLiId,
		basket: $("#basket").val()
	};
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "/smilepay/basketDel",
		data: JSON.stringify(data),
		success: function (resData) {
			if(resData.length > 0){
				var totalPriceView = 0;
				var $orderMenuList = $("#orderPageCom .order_list").find(".order_menu");
				
				if( $orderMenuList.length > 0){
					$orderMenuList.each(function(){
						var toPrice = $(this).find("input[name=toPrice]").val();
						var orderCount = $(this).find("input[name=orderCount]").val();
						totalPriceView += (parseInt(orderCount)*parseInt(toPrice));
					});
				}
				
				if($orderMenuList.length > 0){
					var $selTotal = $("#orderPageCom .order_total");
					$selTotal.find(".price").text(numberCom(totalPriceView)+"${pay_won}");
				}else{
					menuPageGO();
				}
			}else{
				menuPageGO();
			}
		},
		error: function (e) {
			alert(e.responseText);
		}
	});
}

function menuPacking(firstChk){
	var packing = "0";
	var $orderMenuList = $("#orderPageCom .order_list").find(".order_menu");
	var packinYN = $("#menuPackId input:checkbox[name=menuPackChk]").is(":checked");
	if(firstChk != 'Y'){
		if(packinYN){
			$("#menuPackId input:checkbox[name=menuPackChk]").prop("checked", false);
			packing = "0";
		}else{
			$("#menuPackId input:checkbox[name=menuPackChk]").prop("checked", true);
			packing = "1";
		}
		
		var data = {
			packing: packing,
			basket: $("#basket").val()
		};
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "/smilepay/packingChg",
			data: JSON.stringify(data),
			success: function (data) {
				if(data != "OK"){
					console.log("No packing Change");
				}
			},
			error: function (e) {
				console.log(e);
			}
		});
		
	}else{
		if(packinYN){
			packing = "1";
			$("#menuPackId input:checkbox[name=menuPackChk]").prop("checked", true);
		}
	}
	
	if($orderMenuList.length > 0){
		$orderMenuList.each(function(){
			$(this).find("input[name=packing]").val(packing);
		});
	}
}


function countChg($home, num){
	var basketLiId = $home.parent().parent().parent().find("input[name=basketLiId]").val();
	var data = {
		basketLiId: basketLiId,
		count: num
	};
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "/smilepay/countChg",
		data: JSON.stringify(data),
		success: function (data) {
			if(data != "OK"){
				console.log("No count Change");
			}else{
				orderComChange($home.parent().find(".count").attr("compSelect"), num);
			}
		},
		error: function (e) {
			console.log(e);
		}
	});
}

function orderComChange(home, num){
	var totalPriceView = 0;
	var $orderMenuList = $("#orderPageCom .order_list").find(".order_menu");
	
	if( $orderMenuList.length > 0){
		$orderMenuList.each(function(){
			var compSelect = $(this).find("input[name=compSelect]").val();
			var toPrice = $(this).find("input[name=toPrice]").val();
			if(compSelect == home){
				$(this).find("input[name=orderCount]").val(num);
				var priceCal=(parseInt(num)*parseInt(toPrice));
				$(this).find(".total .price").html('<span class="label">${pay_total}</span>  '+numberCom(priceCal)+'${pay_won}');
				totalPriceView += priceCal;		
			}else{
				var orderCount = $(this).find("input[name=orderCount]").val();
				totalPriceView += (parseInt(orderCount)*parseInt(toPrice));
			}
		});
	}
	
	if($orderMenuList.length > 0){
		var $selTotal = $("#orderPageCom.order_total");
		$selTotal.find(".price").text(numberCom(totalPriceView)+"원");
	}
}

function compltePage(){
	var $orderForm = $("#orderForm");
	$orderForm.html("");

	var menOrderSelList = '<input type="hidden" name="storeKey" value="'+$("#storeKey").val()+'" />';
	menOrderSelList += '<input type="hidden" name="basket" value="'+$("#basket").val()+'" />';
	menOrderSelList += '<input type="hidden" name="table" value="'+$("#table").val()+'" />';
	menOrderSelList += '<input type="hidden" name="time" value="'+$("#time").val()+'" />';

	$orderForm.append(menOrderSelList);
	
	orderPay();
}
function orderPay(){
	var orderForm = document.orderForm;
	orderForm.submit();
}

$(window).load(function(){
	menuPacking('Y');
});

</script>
</html>