<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>

<!DOCTYPE html>
<html lang="${html_lang}">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>${storeName}</title>
	
	<link rel="stylesheet" href="/resources/vendor/css/bootstrap.css">
    <link rel="stylesheet" href="/resources/vendor/css/appwork.css">
    
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/reset.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/ui.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/jquery-ui.min.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuList/css/style.css">
	<link rel="stylesheet" href="/resources/vendor/lib/toastr/toastr.css">
	<link rel="stylesheet" href="/resources/vendor/lib/swiper/swiper.min.css">
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="/resources/selfmenu/menuList/js/jquery-ui.min.js"></script>
	<script src="/resources/selfmenu/menuList/js/appMenu.js"></script>
	<script src="/resources/vendor/lib/swiper/swiper.min.js"></script>
	<script src="/resources/vendor/lib/validate/validate.${html_lang}.js"></script>
	<script src="/resources/vendor/lib/toastr/toastr.js"></script>
	<script src="/resources/shared/js/jquery.form.js"></script>
</head>
<body>
	<div class="wrapper reset">
		<input type="hidden" id="storeKey" value="${storeKey}" />
		<input type="hidden" id="basket" value="${basket}" />
		<input type="hidden" id="table" name="table" value="${table}" />
		<input type="hidden" id="time" name="time" value="${time}" />
		<input type="hidden" id="refiTel" name="refiTel" value="${refiTel}"/>
		
		<span style="position:absolute; margin-left: 3%; font-size: 15px; margin-top: 22px;" onclick="popupCancelOpen(); return false;">${pay_order}${pay_cancel}</span>
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
		<div class="container marBot">
			<div class="inner">
				<c:choose>
					<c:when test="${!empty menuGroup}">
						<c:forEach var="item" items="${menuGroup}" varStatus="status">
							<c:choose>
								<c:when test="${status.first}">
									<div class="menu_type on">
								</c:when>
								<c:otherwise>
									<div class="menu_type">
								</c:otherwise>
							</c:choose>
								<button type="button">${item.name}<span></span></button>
								<c:choose>
									<c:when test="${status.first}">
										<ul class="row menu_list" style="display: block;">
									</c:when>
									<c:otherwise>
										<ul class="row menu_list" style="display: none;">	
									</c:otherwise>
								</c:choose>
									<c:forEach var="menuItem" items="${item.DMenus}" varStatus="menuSt">
										<li>
											<c:choose>
												<c:when test="${menuItem.soldOut}">
													<a href="#" class="menu no_order" meli="${menuItem.id}">
												</c:when> 
												<c:otherwise>
													<a href="#" class="menu" meli="${menuItem.id}">
												</c:otherwise>
											</c:choose>
												<span class="img_wrap"><img src="${menuItem.imgPathFilename}" alt="" style="width: auto; height: auto; max-width: 100%; max-height: 100%;" /></span>
												<span class="info_wrap taC">
													<input type="hidden" name="productId" value="${menuItem.id}" />
													<input type="hidden" name="intro" value="${menuItem.intro}" />
													<input type="hidden" name="price" value="<fmt:formatNumber value='${menuItem.price}' pattern='####' />" />
													<input type="hidden" name="flagType" value="${menuItem.flagType}" />
													<span class="optionList">
														<c:forEach var="optionItemMan" items="${menuItem.manMenus}">
															<input type="hidden" name="id" value="${optionItemMan.id}" />
															<input type="hidden" name="name_${optionItemMan.id}" value="${optionItemMan.name}" />
															<input type="hidden" name="gubun_${optionItemMan.id}" value="0" />
															<input type="hidden" name="menuList_${optionItemMan.id}" value="${optionItemMan.menus}" />
														</c:forEach>
														<c:forEach var="optionItemOpt" items="${menuItem.optMenus}">
															<input type="hidden" name="id" value="${optionItemOpt.id}" />
															<input type="hidden" name="name_${optionItemOpt.id}" value="${optionItemOpt.name}" />
															<input type="hidden" name="gubun_${optionItemOpt.id}" value="1" />
															<input type="hidden" name="menuList_${optionItemOpt.id}" value="${optionItemOpt.menus}" />
														</c:forEach>
													</span>
													<span class="name">${menuItem.name}</span>
													<span class="price"><fmt:formatNumber value="${menuItem.price}" type="number" /></span>
												</span>
												<c:choose>
													<c:when test="${menuItem.flagType eq 'N'}">
														<span class="patch NEW"></span>
													</c:when>
													<c:when test="${menuItem.flagType eq 'R'}">
														<span class="patch BEST"></span>
													</c:when>
													<c:when test="${menuItem.flagType eq 'I'}">
														<span class="patch1 INFIRF"></span>
													</c:when>
													<c:otherwise></c:otherwise>
												</c:choose>
												<c:if test="${menuItem.soldOut}">
													<span class="no_inventory"><span class="icon"></span>${msg_notOrder}</span>
												</c:if>
											</a>
										</li>
									</c:forEach>
								</ul>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div class="menu_type">
							<ul class="row menu_list" style="display: block;">
								${msg_noAvaMenu}
							</ul>
						</div>
					</c:otherwise>
				</c:choose>
				<div class="menu_type"">
					<ul class="row" style="display: block; font-size: 12px; background-color: #e4e4e4;">
						<li>
							<span style="float:inherit;">
								<c:if test="${not empty bizName}">
									<p><b style="margin:1%">상호명</b> ${bizName}</p>
								</c:if>
								<c:if test="${not empty bizNum}">
									<p><b style="margin:1%">사업자번호</b> ${bizNum}</p>
								</c:if>
								<c:if test="${not empty bizRep}">
									<p><b style="margin:1%">대표자</b> ${bizRep}</p>
								</c:if>
								<c:if test="${not empty phone}">
									<p><b style="margin:1%">대표전화</b> ${phone}</p>
								</c:if>
								<c:if test="${not empty openHours}">
									<p><b style="margin:1%">영업시간</b> ${openHours}</p>
								</c:if>
								<c:if test="${not empty addr2}">
									<p><b style="margin:1%">주소</b> ${addr2}</p>
								</c:if>
							</span>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<c:choose>
			<c:when test="${not empty payitemList}">
				<div class="sel_menu">
					<ul class="sel_list">
						<c:forEach var="payitem" items="${payitemList}" varStatus="status">
							<c:if test="${not empty payitem.compSelect}">
								<c:choose>
									<c:when test="${not empty payitem.subMenu}">
										<li class="sel row" style="height: 21px;">
									</c:when>
									<c:otherwise>
										<li class="sel row">
									</c:otherwise>
								</c:choose>
									<div class="name menuChange">${payitem.name}</div>
									<div class="count">${payitem.orderCount}</div>
									<div class="price"><fmt:formatNumber value="${payitem.toPrice*payitem.orderCount}" type="number" />${pay_won}</div>
									<a href="#" class="del" onclick="menuDelete(${payitem.id}, '${payitem.compSelect}')">×</a>
									<input type="hidden" name="compSelect" value="${payitem.compSelect}" />
									<input type="hidden" name="menuId" value="${payitem.menuId}" />
									<input type="hidden" name="basketLiId" value="${payitem.id}" />
									<input type="hidden" name="toCount" value="${payitem.orderCount}" />
									<input type="hidden" name="price" value="${payitem.price}" />
									<input type="hidden" name="toPrice" value="${payitem.toPrice}" />
									<input type="hidden" name="packing" value="${payitem.packing}" />
									<input type="hidden" name="essVal" value="${payitem.essVal}" />
									<input type="hidden" name="essName" value="${payitem.essName}" />
									<input type="hidden" name="addVal" value="${payitem.addVal}" />
									<input type="hidden" name="addName" value="${payitem.addName}" />
								</li>
								<c:if test="${not empty payitem.subMenu}">
									<li class="sel row sub_${payitem.compSelect}" style="height: 17px;">
										<div style="width: 90%; font-size: 12px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">${payitem.subMenu}</div>
									</li>
								</c:if>
							</c:if>
						</c:forEach>
					</ul>
					<ul><li class="total row">
						<div class="name">${pay_total}</div>
						<div class="count">${totalcount}</div>
						<div class="price">${goodsAmt}${pay_won}</div>
					</li></ul>
					<ul class="btn_area row"><li class="full"><div class="button medium red full circle" onclick="compltePage();">${pay_step1}</div></li></ul>
				</div>
			</c:when>
			<c:otherwise>
				<div class="sel_menu no_sel">
					<span>${msg_noChooseMenu}</span>
				</div>
			</c:otherwise>
		</c:choose>
		<div class="popup_area">
			<div class="close">
				<span class="txtX">×</span>
				${pay_shut}</div>
			<div class="title"></div>
			<div class="img_wrap"><img src="" alt="" style="width: auto; height: auto; max-width: 100%; max-height: 100%;"></div>
			<div class="info_wrap">
				<span class="additional"></span>
				<span class="price"></span>
				<input type="hidden" name="befCompSe"/>
				<input type="hidden" name="price" />
				<input type="hidden" name="id" />
				<input type="hidden" name="basketLiId" />
				<input type="hidden" name="count" />
				<input type="hidden" name="name" />
				<input type="hidden" name="price" />
				<input type="hidden" name="src" />
				<input type="hidden" name="packing" />
				<input type="hidden" name="intro" />
				<input type="hidden" name="popChoose" />
			</div>
			<div class="emptyDiv"></div>
			<div class="count_wrap">
				<a href="#" class="down_count">-</a>
				<span class="count">1</span>
				<a href="#" class="up_count">+</a>
			</div>
			<ul class="btn_area">
				<li class="half">
					<div class="button medium red full circle" onclick="popupClose();">${pay_cancel}</div>
				</li>
				<li class="half">
					<div class="button medium yell full circle" onclick="order();">${pay_order} ${pay_embody}</div>
				</li>
			</ul>
		</div>
		
		<div class="popup_area1">
			<div class="close">
				<span class="txtX">×</span>
				${pay_shut}</div>
			<div class="swiper-container">
			  <div class="swiper-wrapper">
			  </div>
			    <div class="swiper-pagination" style="font-size: 16px; padding-left: 36%;"></div>
			    <div class="swiper-button-next" ></div>
			    <div class="swiper-button-prev" ></div>
			</div>
		</div>
		
		<div class="popupCancel_area">
			<div class="close"><span class="txtX">×</span>${pay_shut}</div>
			<form id="cancelForm" onsubmit="return false;">
				<h5 class="card-header">${pay_order} ${pay_cancel}</h5>
				<div class="card-body" id="cardCancelVerifiCode">
					<input type="hidden" name="cancelStoreId"  value="${storeId}" />
					<input type="hidden" name="cancelTable" value="${table}" />
					<input type="hidden" name="cancelPaygubun" value="${paygubun}" />
					<div class="form-group" id="verifiCodeDiv" >
						<input type="text" class="form-control underIn" style="width: 100%; text-align:center; font-size: 100px;" maxlength="${approvalDigit}" name="verifiCode" placeholder="" />
					</div>
					<div class="form-group" style="width: 100%; font-size: 12px;">
						※ ${pay_apprCancel} ${approvalDigit}${msg_numEnter}
					</div>
              	</div>
				<div class="card-body" id="cardCancelGo" style="display: none;">
           		</div>
				<div id="cardCancelVerifiCodeBtn" class="button medium yell full circle" onclick="menuCancelCheck();">${pay_order} ${pay_cancel}</div>
				<ul class="btn_area" id="cardCancelGoBtn" style="display: none;">
					<li class="half">
						<div class="button medium red full circle" onclick="popupCancelClose();">${pay_nope}</div>
					</li>
					<li class="half">
						<div id="yesDiv" class="button medium yell full circle" onclick="menuCancel();">${pay_yes}</div>
						<div id="yesHideDiv" class="button medium full circle" style="display: none;">${pay_yes}</div>
					</li>
				</ul>
            </form>
		</div>
		<div class="popup_area4">
			<div class="close">
				<span class="txtX">×</span>
				${pay_shut}</div>
			<div class="card-body" style="text-align: center;">
				<h6 id="popupArea4H6">${msg_orderCancel}<br/>${msg_thx}</h6> 
			</div>
			<div class="button medium yell full circle" onclick="popup4Close();">${alert_ok}</div>
		</div>
		<div class="popup_area5">
			<div class="close">
				<span class="txtX">×</span>
				${pay_shut}</div>
			<div class="card-body" style="text-align: center;">
				<form id="menuForm2" name="menuForm2" onsubmit="return false;">
				<div style="width: 100%; height: 46px;">
					<span class="patch2 INFIRFBALL"></span>
				</div>
				<div class="form-group" style="font-size: 20px;">
					<b>아래에 전화번호를 입력하시면 리필 가능하세요.</b> 
				</div> 
				<div class="form-group errorMsg" style="margin-top: 2%; font-size: 69%;" >
					<input type="text" class="form-control underIn" style="width: 100%; text-align:center; font-size: 30px;" maxlength="13" name="viewTel" placeholder="전화번호 입력" onKeyup="inputPhoneNumber(this);" />
					<input type="hidden" name="rfMenuId" value="" />
				</div>
				
				<div class="form-group row" style="margin-bottom: 10px;">
					<label class="col-form-label col-sm-2 text-sm-right errorMsg" style="font-size: 18px; font-weight: 600;">
						<div class="btn-group" style="width: 100%;">
							<button type="button" class="btn btn-default" style="width: 80%; padding-left: 2%;" >
								<label class="custom-control custom-checkbox m-0">
									<input type="checkbox" class="custom-control-input option" name="consent2" />
									<span class="custom-control-label">개인정보 제 3자 제공 동의</span>
								</label>
							<button type="button" class="btn btn-default dropdown-toggle dropdown-toggle-split textAreaDiv2" style="width: 21%;" data-toggle="dropdown"></button>
							<div class="dropdown-menu"></div>
						</div>
					</label>
					<div class="col-sm-2 textAreaDivTogle2" id="consentdiv2">
						<div class="card-body" style="padding: 0px;" >
							<textarea class="form-control" rows="10" readonly="readonly" disabled>회원님으로부터 수집한 개인정보는 "개인정보처리방침"에서 고지한 범위 내에서만 사용하며, 회원님의 동의 없이는 해당 범위를 초과하여 이용하거나 제3자에게 제공하지 않습니다.
													다만, 양질의 서비스제공을 위하여 회원님의 개인정보를 협력업체에게 제공하거나 공유할 필요가 있는 경우에는 제공 또는 공유할 정보의 항목 및 제공받는 자, 제공목적, 이용 및 보유기간 등을 호원님께 고지하여 동의를 구합니다.
													제1조(개인정보의 제3자 제공 및 공유)
													회사는 회원님의 개인정보를 아래에서 명시한 개인정보의 처리목적 범위 내에서만 처리하며, 회원님의 동의내지 법이 허용한 경우에만 개인정보를 제3자에게 제공합니다.
													회사는 다음과 같이 회원님들의 개인정보를 제3자에게 제공합니다.
													①개인정보를 제공받는 메시지 서비스 업체
													②주문한 서비스 또는 재화의 배송 수신자 성명, 휴대폰번호, 배송주소
													③개인정보 보유 및 이용기간은 서비스 제공 완료 후 6개월
													아래의 경우에는 관련법령의 규정에 의하여 회원님의 동의 없이 개인정보를 제공할 수도 있습니다.
													①이용자가 사전에 공개하거나 또는 제3자 제공에 동의한 경우
													②관계법령의 규정에 의거하거나, 수사목적으로 법령에 정해진 절차와 방법에 따라 수사기관의 요구가 있는 경우</textarea>
						</div>
					</div>
				</div>
				</form>
			</div>
			<ul class="btn_area">
				<li class="half">
					<div class="button medium red full circle" onclick="popup5Close();">건너뛰기</div>
				</li>
				<li class="half">
					<div class="button medium yell full circle" onclick="rfCheck();">주문</div>
				</li>
			</ul>
		</div>
		<div class="popup_refill">
			<div class="close">
				<span class="txtX">×</span>
				${pay_shut}</div>
			<div class="title"></div>
			<div class="img_wrap"><img src="" alt="" style="width: auto; height: auto; max-width: 100%; max-height: 100%;"></div>
			<div class="info_wrap">
				<span class="additional"></span>
				<span class="price"></span>
				<input type="hidden" name="befCompSe"/>
				<input type="hidden" name="price" />
				<input type="hidden" name="id" />
				<input type="hidden" name="basketLiId" />
				<input type="hidden" name="count" />
				<input type="hidden" name="name" />
				<input type="hidden" name="price" />
				<input type="hidden" name="src" />
				<input type="hidden" name="packing" />
				<input type="hidden" name="intro" />
				<input type="hidden" name="popChoose" />
				<input type="hidden" name="paOrderId" />
			</div>
			<div class="emptyDiv"></div>
			<div class="count_wrap">
				<a href="#" class="down_count">-</a>
				<span class="count">1</span>
				<a href="#" class="up_count">+</a>
			</div>
			<ul class="btn_area">
				<li class="half">
					<div class="button medium red full circle" onclick="popupRefillClose();">${pay_cancel}</div>
				</li>
				<li class="half">
					<div class="button medium yell full circle" onclick="orderReFill();">${pay_order} 하기</div>
				</li>
			</ul>
		</div>
		
	</div>
	<form id="orderSmilepay" name="orderSmilepay" method="post" Content-Type="application/json" action="/smilepay/order"></form>
	<form id="orderRefill" name="orderRefill" method="post" Content-Type="application/json" action="/smilepay/rfOrder"></form>
	
</body>
<style type="text/css">

.underIn {border: 0; outline: 0; background: transparent; border-bottom: 2px solid black; font-size: small; margin-bottom: 3%;}

.popup_area1 {display: none; width: 80%; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: #fff; z-index: 20; padding: 15px 20px 10px; box-sizing: border-box}
.popup_area1 .close {display: block; position: absolute; right: 0; top: 0; margin-top: -30px; color: #fff;  font-size: 22px; line-height: 20px; vertical-align: top;}
.popup_area1 .close .txtX {position: relative; top:4px ; margin-right: 3px;}

.popupCancel_area {display: none; width:90%; max-width: 300px; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: #fff; z-index: 20; padding: 15px 20px 10px; box-sizing: border-box}
.popupCancel_area .close {display: block; position: absolute; right: 0; top: 0; margin-top: -30px; color: #fff;  font-size: 22px; line-height: 20px; vertical-align: top;}
.popupCancel_area .close .txtX {position: relative; top:4px ; margin-right: 3px;}
.popupCancel_area .title {text-align: center; font-size: 27px; margin-bottom: 10px;}

.popup_area4 {display: none; width: 80%; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: #fff; z-index: 20; padding: 15px 20px 10px; box-sizing: border-box}
.popup_area4 .close {display: block; position: absolute; right: 0; top: 0; margin-top: -30px; color: #fff;  font-size: 22px; line-height: 20px; vertical-align: top;}
.popup_area4 .close .txtX {position: relative; top:4px ; margin-right: 3px;}

.popup_area5 {display: none; width: 80%; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: #fff; z-index: 20; padding: 15px 20px 10px; box-sizing: border-box}
.popup_area5 .close {display: block; position: absolute; right: 0; top: 0; margin-top: -30px; color: #fff;  font-size: 22px; line-height: 20px; vertical-align: top;}
.popup_area5 .close .txtX {position: relative; top:4px ; margin-right: 3px;}

.popup_refill {display: none; width: 80%; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: #fff; z-index: 20; padding: 15px 20px 10px; box-sizing: border-box}
.popup_refill .close {display: block; position: absolute; right: 0; top: 0; margin-top: -30px; color: #fff;  font-size: 22px; line-height: 20px; vertical-align: top;}
.popup_refill .close .txtX {position: relative; top:4px ; margin-right: 3px;}

.swiper-container {width:100%; height:230px; border:5px solid silver; border-radius:7px; box-shadow:0 0 20px #ccc inset;}
.swiper-slide {text-align:center; -webkit-box-align: center; -ms-flex-align: center; -webkit-align-items: center; align-items: center; -webkit-box-pack: center;
  	-ms-flex-pack: center; -webkit-justify-content: center; justify-content: center; font-size: 24px;}

.checks {position: relative; font-size:16px; margin-bottom: 8px;}
.checks input[type="radio"] { position: absolute; width: 1px; height: 1px; padding: 0; margin: -1px; overflow: hidden; clip:rect(0,0,0,0); border: 0; }
.checks input[type="radio"] + label { display: inline-block; position: relative; padding-left: 30px; cursor: pointer; -webkit-user-select: none; -moz-user-select: none; -ms-user-select: none; }
.checks input[type="radio"] + label:before { content: ''; position: absolute; left: 0; top: -1px; width: 18px; height: 18px; text-align: center; background: #fafafa; border: 1px solid #cacece; border-radius: 100%; box-shadow: 0px 1px 2px rgba(0,0,0,0.05), inset 0px -15px 10px -12px rgba(0,0,0,0.05); }
.checks input[type="radio"] + label:active:before, .checks input[type="radio"]:checked + label:active:before { box-shadow: 0 1px 2px rgba(0,0,0,0.05), inset 0px 1px 3px rgba(0,0,0,0.1); }
.checks input[type="radio"]:checked + label:before { background: #E9ECEE; border-color: #adb8c0; }
.checks input[type="radio"]:checked + label:after { content: ''; position: absolute; top: 2px; left: 3px; width: 13px; height: 13px; background: #99a1a7; border-radius: 100%; box-shadow: inset 0px 0px 10px rgba(0,0,0,0.3); }

.checks input[type="checkbox"] {  /* 실제 체크박스는 화면에서 숨김 */
  position: absolute;  width: 1px;  height: 1px;  padding: 0;  margin: -1px;  overflow: hidden;  clip:rect(0,0,0,0);  border: 0}
.checks input[type="checkbox"] + label {  display: inline-block;  position: relative;  cursor: pointer;  -webkit-user-select: none;  -moz-user-select: none;  -ms-user-select: none;}
.checks input[type="checkbox"] + label:before {  /* 가짜 체크박스 */
  content: ' ';  display: inline-block;  width: 21px;  /* 체크박스의 너비를 지정 */  height: 21px;  /* 체크박스의 높이를 지정 */  line-height: 21px; /* 세로정렬을 위해 높이값과 일치 */  margin: -2px 8px 0 0;  text-align: center; 
  vertical-align: middle;  background: #fafafa;  border: 1px solid #cacece;  border-radius : 3px;  box-shadow: 0px 1px 2px rgba(0,0,0,0.05), inset 0px -15px 10px -12px rgba(0,0,0,0.05);}
.checks input[type="checkbox"] + label:active:before,
.checks input[type="checkbox"]:checked + label:active:before {  box-shadow: 0 1px 2px rgba(0,0,0,0.05), inset 0px 1px 3px rgba(0,0,0,0.1);}
.checks input[type="checkbox"]:checked + label:before {  /* 체크박스를 체크했을때 */ 
  content: '\2714'; color: #99a1a7;  text-shadow: 1px 1px #fff;  background: #e9ecee;  border-color: #adb8c0;
  box-shadow: 0px 1px 2px rgba(0,0,0,0.05), inset 0px -15px 10px -12px rgba(0,0,0,0.05), inset 15px 10px -12px rgba(255,255,255,0.1); font-size:14px; color:red;}
 
.textAreaDivTogle2 { display: none; }
</style>

<script>
var swiper;
var checkCencelID;

function reloadPage(){
	var data = {
		basket: $("#basket").val()
	};
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "/smilepay/basketReload",
		data: JSON.stringify(data),
		success: function (resData) {
			var $selMenu = $(".sel_menu"); 
			$selMenu.html("");
			if(resData.length > 0){
				var tCnt = 0, tPrice = 0;
				var selList = '<ul class="sel_list">';
				for(var i = 0; i < resData.length; i++) {
					var resDataNe = resData[i];
				 	tCnt += parseInt(resDataNe.orderCount);
				 	tPrice += (resDataNe.orderCount * resDataNe.toPrice);
				 	selList = basketAdd(selList, resDataNe.menuId, resDataNe.id, resDataNe.name, resDataNe.orderCount, resDataNe.price, resDataNe.toPrice, 
				 			resDataNe.compSelect, resDataNe.packing, resDataNe.essVal, resDataNe.essName, resDataNe.addVal, resDataNe.addName, resDataNe.subMenu.trim());
				}
				
				selList += '</ul>';
				selList += '<ul><li class="total row">';
				selList += '<div class="name">${pay_total}</div><div class="count">'+tCnt+'</div>';
				selList += '<div class="price">'+numberCom(tPrice)+'${pay_won}</div>';
				selList += '</li></ul>';
				selList += '<ul class="btn_area row"><li class="full"><div class="button medium red full circle" onclick="compltePage();">${pay_step1}</div></li></ul>';
				
				$selMenu.append(selList);
				$(".marBot").attr('style','margin-bottom: 210px');
			}else{
				$selMenu.addClass("no_sel");
				$selMenu.html("<span>선택된 메뉴가 없습니다.</span>");
				
				$(".marBot").attr('style','margin-bottom: 30px');
			}
		},
		error: function (e) {
			console.log(e);
		}
	});
}

$(function() {
	reloadPage();

	$(document).on("click",".textAreaDiv2",function(){
		$("#consentdiv2").toggleClass( 'textAreaDivTogle2' );
	} );
	
	setTimeout(timeOutPage, 3600000);

	function timeOutPage() {
		alert("${msg_timeOutMsg}");
		document.location.href="/menu?store=${storeKey}&table=${table}";
	}

	$(document).on("click",".logo",function(){
		document.location.href="/menu?store=${storeKey}&table=${table}";
	} );
	
	swiper = new Swiper('.swiper-container', {
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
	
	cookiechange();
	
	<c:if test="${not empty payitemList}">
		$(".marBot").attr('style','margin-bottom: 210px');
	</c:if>
	
	onFn();
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

$('.popup_area1 .close').on('click', function(e){
	e.preventDefault();
	
	$('.dim').remove();
	$(this).parent().hide();
	$('body').removeClass('fix');
});

$('.popupCancel_area .close').on('click', function(e){
	e.preventDefault();
	
	var htmlText = '<input type="text" class="form-control underIn" style="width: 100%; text-align:center; font-size: 100px;" maxlength="${approvalDigit}" name="verifiCode" placeholder="" />';
	$('#verifiCodeDiv').html(htmlText);
	$('.dim').remove();
	$(this).parent().hide();
	$('body').removeClass('fix');
});

$('.popup_area4 .close').on('click', function(e){
	e.preventDefault();
	
	$('.dim').remove();
	$(this).parent().hide();
	$('body').removeClass('fix');
});
$('.popup_area5 .close').on('click', function(e){
	e.preventDefault();
	
	$('.dim').remove();
	$(this).parent().hide();
	$('body').removeClass('fix');
});

$('.popup_refill .close').on('click', function(e){
	e.preventDefault();
	
	$('.dim').remove();
	$(this).parent().hide();
	$('body').removeClass('fix');
});

function popupRefillClose(){
	$('.dim').remove();
	$('.popup_refill').hide();
	$('body').removeClass('fix');
}

function popupCancelClose(){
	$('.dim').remove();
	$('.popupCancel_area').hide();
	$('body').removeClass('fix');
}

function popup4Close(){
	$('.dim').remove();
	$('.popup_area4').hide();
	$('body').removeClass('fix');
}

function priceChangeFn(){
	var $orderMenuList = $(".sel_menu .sel_list").find("li");
	$orderMenuList.each(function(){
		var price = $(this).find(".price").html();
		$(this).find(".price").html(numberCom(price));
	})
}

</script>
<script>
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

function popupCancelOpen(){
	var popupCancel = $('.popupCancel_area');
	$('#cancelForm').find("input[name=verifiCode]").val("");
	$("#cardCancelVerifiCode").show();
	$("#cardCancelVerifiCodeBtn").show();
	$("#cardCancelGo").hide();
	$("#cardCancelGoBtn").hide();
	
	$("#cancelForm").validate({
		errorPlacement: function errorPlacement(error, element) {
			$(element).parents('.form-group').append(error.addClass('invalid-feedback small d-block')) },
		highlight: function(element) { $(element).addClass('is-invalid'); },
		unhighlight: function(element) { $(element).removeClass('is-invalid'); },
		rules: {
			verifiCode: {
				required: true, minlength: ${approvalDigit}, maxlength: ${approvalDigit}, number:true
			}
		}
	});
	
	popupCancel.show();
	$('body').addClass('fix').append('<div class="dim"></div>');
}

function menuCancelCheck(){
	var $verifiCode = $('#verifiCodeDiv');
	$verifiCode.find("label").remove();
	$("#cardCancelVerifiCodeBtn").show();
	$("#yesDiv").show();
	$("#yesHideDiv").hide();
	if ($("#cancelForm").valid()) {
		var data = {
			cancelStoreId: $('#cancelForm').find("input[name=cancelStoreId]").val(),
			cancelTable: $('#cancelForm').find("input[name=cancelTable]").val(),
			verifiCode: $('#cancelForm').find("input[name=verifiCode]").val()
		};
		$("#cardCancelVerifiCodeBtn").hide();
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "/smilepay/menuCancelVerifi",
			data: JSON.stringify(data),
			success: function (form) {
				if(form.payAuthYN != "Y"){
					$("#cardCancelVerifiCodeBtn").show();
					$verifiCode.append('<label id="verifiCode-error" class="error invalid-feedback small d-block" for="verifiCode">${msg_manager}<br>${msg_contact}</label>');
					$verifiCode.find("input[name=verifiCode]").addClass('is-invalid');
				}else{
					$("#cardCancelVerifiCode").hide();
					$("#cardCancelVerifiCodeBtn").hide();
					$("#cardCancelGo").show();
					$("#cardCancelGoBtn").show();
					var check = '';
					check += '<input type="hidden" name="storeOrderId" value="'+form.storeOrderId+'" />';
					check += '<div class="form-group"><h6>${pay_ordernum} : '+form.orderSeq+'</h6></div>';
					check += '<div class="form-group"><h6>${pay_paydate} : <br/>'+form.payAuthDate+'</h6></div>';
					check += '<div class="form-group"><h6>${pay_totalamt} : '+form.payAmt+'</h6></div>';
					check += '<div class="form-group"><h6>${pay_approval} : '+form.payAuthCode+'</h6></div>';
					check += '<div class="form-group" style="text-align: center;"><label class="form-label">${msg_auCancelOrder}</label></div>';
					$("#cardCancelGo").html(check);
					
					checkCencelID = form.orderSeq;
				}
			}
		});
	}
}

function menuCancel(){
	$("#yesDiv").hide();
	$("#yesHideDiv").show();
	
	if ($("#cancelForm").valid()) {
		var data = {
			cancelStoreId: $('#cancelForm').find("input[name=cancelStoreId]").val(),
			cancelTable: $('#cancelForm').find("input[name=cancelTable]").val(),
			verifiCode: $('#cancelForm').find("input[name=verifiCode]").val(),
			storeOrderId : $('#cancelForm').find("input[name=storeOrderId]").val()
		};
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "/smilepay/menuCancel",
			data: JSON.stringify(data),
			success: function (form) {
				popupCancelClose();
				$('.popup_area4').show();
				$('body').addClass('fix').append('<div class="dim"></div>');
				
				if(form != 'true'){
					$("#popupArea4H6").html(form);
				}	
				
				menuCancelCookieDel();					
			}
		});
	}
}

function menuCancelCookieDel(){
	
	var cookieReset = "";
	var cookieName = 'bbmcorder_${storeId}';
	var cookieTF = unescape(getCookie(cookieName));
	if("" != cookieTF){
		//쿠키 초기화
		setCookie(cookieName, '', '-1');
		var tmp1 = cookieTF.split(",");
		
		for( var i in tmp1 ){
			var tmp2 = tmp1[i].split("_");
			if(tmp2.length > 1){
				if(checkCencelID != tmp2[0]){
					if(cookieReset != ""){
						cookieReset += (","+tmp2[0]+"_"+tmp2[1]);
					}else{
						cookieReset += (tmp2[0]+"_"+tmp2[1]);
					}
				}
			}
		}
		
		setCookie(cookieName, cookieReset, 1);
		cookiechange();
	}
}

function cookiechange(){
	var cookieEnabled = navigator.cookieEnabled;
	var cookieName = "";
	
	swiper.removeAllSlides();
	if (cookieEnabled){
		
		cookieName = 'bbmcorder_${storeId}';
		
		var cookieTF = unescape(getCookie(cookieName));
		var menuList = "";
		if(null == cookieTF || "" == cookieTF){
			$(".swiper-pagination").remove();
			$(".swiper-button-next").remove();
			$(".swiper-button-prev").remove();
			
			menuList = '<div class="swiper-slide" style="margin-top:100px;"><h4>${msg_noOrder}</h4></div>';
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
		
		menuList = '<div class="swiper-slide" style="margin-top:30%;"><h4>${msg_notSupportBrowser}</h4></div>';
		swiper.appendSlide(menuList);
	}
}

function numberCom(str){
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

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

function popupClose(){
	$('.dim').remove();
	$('.popup_area').hide();
	$('body').removeClass('fix');
}

function menuOpenPop(prod_img, nameView, priceView, orderCount, intro, prod_Id, prod_price, $opList){
	var popup = $('.popup_area');
	
	$('.popup_refill').find(".emptyDiv").html("");
	optionListAdd(popup, $opList, "", "");
	
	popup.show().scrollTop(0);
	popup.find('.img_wrap img').attr('src', prod_img);
	popup.find('.title').text(nameView);
	popup.find('.price').text(priceView);
	popup.find('.count_wrap .count').text(orderCount);
	popup.find('.info_wrap .additional').text(intro);
	popup.find('input[name="befCompSe"]').val("M");
	popup.find('input[name="id"]').val(prod_Id);
	popup.find('input[name="basketLiId"]').val("");
	popup.find('input[name="name"]').val(nameView);
	popup.find('input[name="price"]').val(prod_price);
	popup.find('input[name="count"]').val(orderCount);
	popup.find('input[name="src"]').val(prod_img);
	popup.find('input[name="intro"]').val(intro);
	popup.find('input[name="packing"]').val("0");
	popup.find('input[name="popChoose"]').val("menuClick"); // 메뉴 클릭 인지 아닌지 확인.
	
	$('body').addClass('fix').append('<div class="dim"></div>');
}

<% // 리필할 경우 사용되는 변수 %>
var $rfOpList;
var rfProd_img, rfNameView, rfPriceView, rfOrderCount, rfIntro, rfProd_Id, rfProd_price;

function menuRFOpenPop(prod_img, nameView, priceView, orderCount, intro, prod_Id, prod_price, $opList, flagType){
	$("input[name=consent2]").prop('checked', false);
	$('.popup_area5').find("input[name=viewTel]").val("");
	rfProd_img = prod_img; 
	rfNameView = nameView;
	rfPriceView = priceView;
	rfOrderCount = orderCount;
	rfIntro = intro;
	rfProd_Id = prod_Id;
	rfProd_price = prod_price;
	$rfOpList = $opList;
	$("input[name=rfMenuId]").val(rfProd_Id);
	$(".error").each(function(){
 		$(this).parent().find("input").removeClass("is-invalid");
		$(this).remove();
	})
	
	var popup = $('.popup_area5');
	popup.show().scrollTop(0);
	$('body').addClass('fix').append('<div class="dim"></div>');
}

function popup5Close(){
	$('.dim').remove();
	$('.popup_area5').hide();
	$('body').removeClass('fix');
	
	menuOpenPop(rfProd_img, rfNameView, rfPriceView, rfOrderCount, rfIntro, rfProd_Id, rfProd_price, $rfOpList);
}

function rfCheck(){
	var checkYn = true;
	var consent2 =  $('.popup_area5').find("input[name=consent2]").is(":checked");
	var viewTel = $('.popup_area5').find("input[name=viewTel]").val();
	var rfMenuId = $('.popup_area5').find("input[name=rfMenuId]").val();
	if ($("#menuForm2").valid()) {
		var data = {
				consent2: consent2,
				viewTel: viewTel,
				rfMenuId: rfMenuId,
				storeKey: $('#storeKey').val()
		};
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "/smilepay/rfChk",
			data: JSON.stringify(data),
			success: function (form) {
				if(viewTel != "" && viewTel != null){
					$("#refiTel").val(viewTel);
				}
				
				if("Y" === form.rfyn){
					menuOpenPopReFill(rfProd_img, rfNameView, rfPriceView, rfOrderCount, rfIntro, rfProd_Id, rfProd_price, $rfOpList, form.paOrderId);
				}else if("N" === form.rfyn && "-1" === form.rfcnt){
					popup5Close();
				}else{
					alert("다시 한번 시도해 주시기 바랍니다. ");
				}
			}
		});
	}
}
function menuOpenPopReFill(prod_img, nameView, priceView, orderCount, intro, prod_Id, prod_price, $opList, paOrderId){
	$('.dim').remove();
	$('.popup_area5').hide();
	$('body').removeClass('fix');
	
	var popupRef = $('.popup_refill');
	$('.popup_area').find(".emptyDiv").html("");
	optionListAddReFill(popupRef, $opList, "", "");
	
	popupRef.show().scrollTop(0);
	popupRef.find('.img_wrap img').attr('src', prod_img);
	popupRef.find('.title').text(nameView);
	popupRef.find('.price').text("리필 주문 입니다.");
	popupRef.find('.count_wrap .count').text(orderCount);
	popupRef.find('.info_wrap .additional').text(intro);
	popupRef.find('input[name="befCompSe"]').val("M");
	popupRef.find('input[name="id"]').val(prod_Id);
	popupRef.find('input[name="basketLiId"]').val("");
	popupRef.find('input[name="name"]').val(nameView);
	popupRef.find('input[name="price"]').val(prod_price);
	popupRef.find('input[name="count"]').val(orderCount);
	popupRef.find('input[name="src"]').val(prod_img);
	popupRef.find('input[name="intro"]').val(intro);
	popupRef.find('input[name="packing"]').val("0");
	popupRef.find('input[name="paOrderId"]').val(paOrderId);
	popupRef.find('input[name="popChoose"]').val("menuClick"); // 메뉴 클릭 인지 아닌지 확인.
	
	$('body').addClass('fix').append('<div class="dim"></div>');
}
function optionListAddReFill(popupRef, $opList, essVal, addVal){
	popupRef.find(".emptyDiv").html("");
	if($opList.find("input[name=id]").length > 0){
		var optionListDiv ="";
		$opList.find("input[name=id]").each(function (){
			var menuArray = new Array();
			
			var id = $(this).val();
			var menuGubun = $opList.find("input[name=gubun_"+id+"]").val();
			var list = $opList.find("input[name=menuList_"+id+"]").val();
			if(list != ""){
				menuArray = list.split(",");
			}
			if(menuGubun == 0){
				optionListDiv += '<div>';
				optionListDiv += '<div class="divider"></div>';
				optionListDiv += ('<h6 style="margin-bottom: 6px; font-size: x-large;">'+$opList.find("input[name=name_"+id+"]").val()+'</h6>');
				if(menuArray.length > 0){
					for( var i in menuArray ){
						var nameTmp = ""; 
						var priceTmp = ""; 
						var tmp = menuArray[i].split(" ");
						var temLen = tmp.length;
						if(temLen > 0){
							for(var tt in tmp){
								if(tt == (temLen-1)){
									priceTmp = tmp[tt];
								}else{
									nameTmp += (" "+tmp[tt]);
								}
							}
		
							if(menuGubun == 0){
								var checkedOp = "";
								var essArr = new Array();
								if(essVal != ""){
									essArr = essVal.split("^");
								}
								
								if(i == 0){
									checkedOp = 'checked="checked"';
								}
								if(essArr.length > 0){
									var checkId = id+'_'+i;
									for(var essOne in essArr){
										if(checkId == essArr[essOne] ){
											checkedOp = 'checked="checked"';
										}
									}
								}
								
								optionListDiv += '<div class="checks">';
								optionListDiv += '<input type="radio" class="option" id="option_'+id+'_'+i+'" name="option_'+id+'" groupId="'+id+'" price="'+priceTmp+'" tell="'+nameTmp.trim()+'" gubun="'+menuGubun+'" '+checkedOp+' value="'+i+'"> ';
								optionListDiv += '<label for="option_'+id+'_'+i+'">'+nameTmp.trim()+'</label>';
								optionListDiv += '<label for="option_'+id+'_'+i+'" style="float: right; padding-right: 10px;">'+numberCom(priceTmp)+'${pay_won}</label>';
								optionListDiv += '</div>';
							}
						}
					}
				}
			}
			optionListDiv += '</div>';
		})
		optionListDiv += '<div class="divider"></div>';
		popupRef.find(".emptyDiv").append(optionListDiv);
	}
}

function orderReFill(){
	var $popup = $('.popup_refill');
	
	var befCompSe = $popup.find('input[name="befCompSe"]').val();
	var id = $popup.find('input[name="id"]').val();
	var basketLiId = $popup.find('input[name="basketLiId"]').val();
	var name = $popup.find('input[name="name"]').val();
	var price = $popup.find('input[name="price"]').val();
	var count = $popup.find('.count_wrap .count').text();
	var src = $popup.find('input[name="src"]').val();
	var packing = $popup.find('input[name="packing"]').val();
	var paOrderId = $popup.find('input[name="paOrderId"]').val();
	var popChoose = $popup.find('input[name="popChoose"]').val();
	var $otptGoupList = $popup.find('.emptyDiv .checks');
	
	var submenu = ""; // 보여주기 위해서 사용하는 
	var menuPrice= parseInt(price);
	var essVal = "", essName = "";
	var addVal = "", addName = "";
	var compSelect = id+",";
	if($otptGoupList.length > 0){
		$otptGoupList.each(function (){
			var $option = $(this).find('.option');
			var groupid = $option.attr("groupid");
			var tell = $option.attr("tell");
			var price = $option.attr("price");
			var gubun = $option.attr("gubun");
			var selectNm = $option.val();
			var checked = $option.is(":checked") ;
			
			// 필수 선택 사항 중 선택 된 옵션
			if(checked && (gubun == 0)){
				essVal += groupid+"_"+selectNm+"^";
				essName += tell+"("+price+")"+"^";
				menuPrice += parseInt(price);
				submenu += "("+tell+")";
			}
			// 추가 선택 사항 중 선택 된 옵션
			if(checked && (gubun == 1)){
				addVal += groupid+"_"+selectNm+"^";
				addName += tell+"("+price+")"+"^";
				menuPrice += parseInt(price);
				submenu += "("+tell+")";
			}
		});
	}
	
	compSelect += (essVal + addVal);
	
	
	var $orderRefill = $("#orderRefill");
	$orderRefill.html("");

	var menOrderSelList = '<input type="hidden" name="choose" value="'+befCompSe+'" />';
	menOrderSelList += '<input type="hidden" name="id" value="'+id+'" />';
	menOrderSelList += '<input type="hidden" name="basketLiId" value="'+basketLiId+'" />';
	menOrderSelList += '<input type="hidden" name="name" value="'+name+'" />';
	menOrderSelList += '<input type="hidden" name="price" value="'+price+'" />';
	menOrderSelList += '<input type="hidden" name="count" value="'+count+'" />';
	menOrderSelList += '<input type="hidden" name="src" value="'+src+'" />';
	menOrderSelList += '<input type="hidden" name="packing" value="'+packing+'" />';
	menOrderSelList += '<input type="hidden" name="essVal" value="'+essVal+'" />';
	menOrderSelList += '<input type="hidden" name="essName" value="'+essName+'" />';
	menOrderSelList += '<input type="hidden" name="addVal" value="'+addVal+'" />';
	menOrderSelList += '<input type="hidden" name="addName" value="'+addName+'" />';
	menOrderSelList += '<input type="hidden" name="compSelect" value="'+compSelect+'" />';
	menOrderSelList += '<input type="hidden" name="menuPrice" value="'+menuPrice+'" />';
	menOrderSelList += '<input type="hidden" name="submenu" value="'+submenu+'" />';
	menOrderSelList += '<input type="hidden" name="storeKey" value="'+$("#storeKey").val()+'" />';
	menOrderSelList += '<input type="hidden" name="basket" value="'+$("#basket").val()+'" />';
	menOrderSelList += '<input type="hidden" name="table" value="'+$("#table").val()+'" />';
	menOrderSelList += '<input type="hidden" name="time" value="'+$("#time").val()+'" />';
	menOrderSelList += '<input type="hidden" name="tel" value="'+$("#refiTel").val()+'" />';
	menOrderSelList += '<input type="hidden" name="paOrderId" value="'+paOrderId+'" />';
	
	$orderRefill.append(menOrderSelList);
	orderReFillsubmit();
	
}

function orderReFillsubmit(){
	var orderRefillpay = document.orderRefill;
	orderRefillpay.submit();
}

var max_h = 0, max_h3 = 0;
function changeMenuList($onList, index){
	var orientation = window.orientation;
	
	if(orientation != undefined){
	    if(orientation == 0 || orientation == 180){
			if((index%2) > 0){
				var h = parseInt($onList.css("height"));
				if(max_h > h){
					$onList.css({height:max_h});
				}
			}else{
				var h = parseInt($onList.css("height"));
				max_h = h+1; 
			}
	    }else if(orientation == -90 || orientation == 90){
			if((index%3) > 0){
				var h = parseInt($onList.css("height"));
				if(max_h3 > h){
					$onList.css({height:max_h3});
				}
			}else{
				var h = parseInt($onList.css("height"));
				if(max_h3 < h){
					max_h3 = h+1;
				}
			}
	    }
	}else{
		if((index%3) > 0){
			var h = parseInt($onList.css("height"));
			if(max_h > h){
				$onList.css({height:max_h});
			}
		}else{
			var h = parseInt($onList.css("height"));
			max_h = h+1; 
		}
	}
}

$(window).on("orientationchange", function(event){
	setTimeout(function() {
		$(".menu_type.on").find("li").each(function(index){
			$(this).removeAttr( 'style' );
		}); 
		
		$(".menu_type.on").find("ul").each(function(){
			max_h = 0;
			max_h3 = 0;
			$(this).find("li").each(function(index){
				changeMenuList($(this), index);
			});
		});
	}, 500);
});

$(document).ready(function () {
	setTimeout(function() {
		$(".menu_type.on").find("li").each(function(index){
			changeMenuList($(this), index);
		});
		max_h = 0;
		max_h3 = 0;
	}, 500);
	
	$('.menu_type button').on('click', function(){
		$(this).parent().toggleClass('on');
		$(this).next().slideToggle();
		
		if($(this).parent().hasClass('on')){
			$(this).parent().find('li').each(function(index){
				changeMenuList($(this), index);
			});
			max_h = 0;
			max_h3 = 0;
		}
	});
	
	$('.down_count').click(function(e){
		e.preventDefault();
		var stat = $(this).next().text();
		var num = parseInt(stat,10);
		num--;
		
		if(num<=0){
			num =1;
		}
		$(this).next().text(num);
		$("#orderCountPop").val(num);
	});
	$('.up_count').on('click', function(e){
		e.preventDefault();
		var stat = $(this).prev().text();
		var num = parseInt(stat,10);
		num++;
		
		$(this).prev().text(num);
		$("#orderCountPop").val(num);
	});
	
	$('.menu').each(function(){
		var priceClass = $(this).find('.price');
		var priceText = numberCom(priceClass.text()) + "${pay_won}";
		priceClass.text(priceText);
	});
	
	$('.menu').on('click', function(e){
		e.preventDefault();
		if(!$(this).hasClass('no_order')) {
			var orderCount = 1;
			var prod_img = $(this).find('.img_wrap img').attr('src');
			var nameView = $(this).find('.name').text();
			var priceView = $(this).find('.price').text();
			var flagType = $(this).find('input[name=flagType]').val();
			var prod_price = $(this).find('input[name=price]').val();
			var prod_Id = $(this).find('input[name=productId]').val();
			var intro = $(this).find('input[name=intro]').val();
			var $opList = $(this).find(".optionList");
			
			if(flagType === "I"){
				menuRFOpenPop(prod_img, nameView, priceView, orderCount, intro, prod_Id, prod_price, $opList, flagType);
			}else{
				menuOpenPop(prod_img, nameView, priceView, orderCount, intro, prod_Id, prod_price, $opList);
			}
		}
	});
	
	$('.order_menu .del').on('click', function(e){
		e.preventDefault();
		$(this).parents('.order_menu').remove();
	});
	
	$('.popup_area .close').on('click', function(e){
		e.preventDefault();
		
		$('.dim').remove();
		$(this).parent().hide();
		$('body').removeClass('fix');
	});
	
	$(document).on("click",".menuChange",function(){
		var $choose = $(this).parent();
		var compSelect = $choose.find("input[name=compSelect]").val(); 
		var menuId = $choose.find("input[name=menuId]").val(); 
		var basketLiId = $choose.find("input[name=basketLiId]").val(); 
		var toCount = $choose.find("input[name=toCount]").val(); 
		var price = $choose.find("input[name=price]").val(); 
		var toPrice = $choose.find("input[name=toPrice]").val(); 
		var packing = $choose.find("input[name=packing]").val(); 
		var essVal = $choose.find("input[name=essVal]").val(); 
		var addVal = $choose.find("input[name=addVal]").val(); 
		
		changePop(compSelect, menuId, basketLiId, toCount, price, toPrice, packing, essVal, addVal);
	} );
	
	
	$.validator.setDefaults({
		errorPlacement: function errorPlacement(error, element) {
			$(element).parents('.errorMsg').append(error.addClass('invalid-feedback small d-block')) },
		highlight: function(element) { $(element).addClass('is-invalid'); },
		unhighlight: function(element) { $(element).removeClass('is-invalid'); },
    });	
	$.validator.addMethod('phone_format',
			function(value, element) {
				return this.optional(element) || /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/.test(value);
			},
			'올바른 전화번호를 입력하십시오.'
		);
	$.validator.addMethod('phone1_format',
		function(value, element) {
			if(value == "" || value == null){
				return false;
			}else{
				return true;
			}
		},
		'전화번호를 입력해주세요.'
	);

	$.validator.addMethod('consent2_format',
		function(value, element) {
			var consent2 = $("input[name=consent2]").is(":checked");
			if(consent2){
				return true;
			}else{
				return false;
			}
		},
		'개인정보 제3자 제공동의가 필요 합니다.'
	);
	$("#menuForm2").validate({
		rules: {
			viewTel: {
				phone1_format: true, phone_format: true
			},
			consent2: {
				consent2_format: true
			}
		}
	});
	
});

function optionListAdd(popup, $opList, essVal, addVal){
	popup.find(".emptyDiv").html("");
	if($opList.find("input[name=id]").length > 0){
		var optionListDiv ="";
		$opList.find("input[name=id]").each(function (){
			var menuArray = new Array();
			
			var id = $(this).val();
			var menuGubun = $opList.find("input[name=gubun_"+id+"]").val();
			var list = $opList.find("input[name=menuList_"+id+"]").val();
			if(list != ""){
				menuArray = list.split(",");
			}
			optionListDiv += '<div>';
			optionListDiv += '<div class="divider"></div>';
			optionListDiv += ('<h6 style="margin-bottom: 6px; font-size: x-large;">'+$opList.find("input[name=name_"+id+"]").val()+'</h6>');
			if(menuArray.length > 0){
				for( var i in menuArray ){
					var nameTmp = ""; 
					var priceTmp = ""; 
					var tmp = menuArray[i].split(" ");
					var temLen = tmp.length;
					if(temLen > 0){
						for(var tt in tmp){
							if(tt == (temLen-1)){
								priceTmp = tmp[tt];
							}else{
								nameTmp += (" "+tmp[tt]);
							}
						}
	
						if(menuGubun == 0){
							var checkedOp = "";
							var essArr = new Array();
							if(essVal != ""){
								essArr = essVal.split("^");
							}
							
							if(i == 0){
								checkedOp = 'checked="checked"';
							}
							if(essArr.length > 0){
								var checkId = id+'_'+i;
								for(var essOne in essArr){
									if(checkId == essArr[essOne] ){
										checkedOp = 'checked="checked"';
									}
								}
							}
							
							optionListDiv += '<div class="checks">';
							optionListDiv += '<input type="radio" class="option" id="option_'+id+'_'+i+'" name="option_'+id+'" groupId="'+id+'" price="'+priceTmp+'" tell="'+nameTmp.trim()+'" gubun="'+menuGubun+'" '+checkedOp+' value="'+i+'"> ';
							optionListDiv += '<label for="option_'+id+'_'+i+'">'+nameTmp.trim()+'</label>';
							optionListDiv += '<label for="option_'+id+'_'+i+'" style="float: right; padding-right: 10px;">'+numberCom(priceTmp)+'${pay_won}</label>';
							optionListDiv += '</div>';
						}
						if(menuGubun == 1){
							var checkedOp = "";
							var addArr = new Array();
							if(addVal != ""){
								addArr = addVal.split("^");
							}
							if(addArr.length > 0){
								var checkId = id+'_'+i;
								for(var addOne in addArr){
									if(checkId == addArr[addOne] ){
										checkedOp = 'checked="checked"';
									}
								}
							}
														
							optionListDiv += '<div class="checks">';
							optionListDiv += '<input type="checkbox" class="option" id="option_'+id+'_'+i+'" name="option_'+id+'" groupId="'+id+'" price="'+priceTmp+'" tell="'+nameTmp.trim()+'" gubun="'+menuGubun+'" '+checkedOp+' value="'+i+'"> ';
							optionListDiv += '<label for="option_'+id+'_'+i+'">'+nameTmp.trim()+'</label>';
							optionListDiv += '<label for="option_'+id+'_'+i+'" style="float: right; padding-right: 10px;">'+numberCom(priceTmp)+'${pay_won}</label>';
							optionListDiv += '</div>';	
						}
					}
				}
			}
			optionListDiv += '</div>';
		})
		optionListDiv += '<div class="divider"></div>';
		popup.find(".emptyDiv").append(optionListDiv);
	}
}

function order(){
	var $popup = $('.popup_area');
	
	var befCompSe = $popup.find('input[name="befCompSe"]').val();
	var id = $popup.find('input[name="id"]').val();
	var basketLiId = $popup.find('input[name="basketLiId"]').val();
	var name = $popup.find('input[name="name"]').val();
	var price = $popup.find('input[name="price"]').val();
	var count = $popup.find('.count_wrap .count').text();
	var src = $popup.find('input[name="src"]').val();
	var packing = $popup.find('input[name="packing"]').val();
	var popChoose = $popup.find('input[name="popChoose"]').val();
	var $otptGoupList = $popup.find('.emptyDiv .checks');
	
	var submenu = ""; // 보여주기 위해서 사용하는 
	var menuPrice= parseInt(price);
	var essVal = "", essName = "";
	var addVal = "", addName = "";
	var compSelect = id+",";
	if($otptGoupList.length > 0){
		$otptGoupList.each(function (){
			var $option = $(this).find('.option');
			var groupid = $option.attr("groupid");
			var tell = $option.attr("tell");
			var price = $option.attr("price");
			var gubun = $option.attr("gubun");
			var selectNm = $option.val();
			var checked = $option.is(":checked") ;
			
			// 필수 선택 사항 중 선택 된 옵션
			if(checked && (gubun == 0)){
				essVal += groupid+"_"+selectNm+"^";
				essName += tell+"("+price+")"+"^";
				menuPrice += parseInt(price);
				submenu += "("+tell+")";
			}
			// 추가 선택 사항 중 선택 된 옵션
			if(checked && (gubun == 1)){
				addVal += groupid+"_"+selectNm+"^";
				addName += tell+"("+price+")"+"^";
				menuPrice += parseInt(price);
				submenu += "("+tell+")";
			}
		});
	}
	
	compSelect += (essVal + addVal);
	var data = {
			choose: befCompSe,
			id: id,
			basketLiId: basketLiId,
			name: name,
			price: price,
			count: count,
			src: src,
			packing: packing,
			essVal: essVal,
			essName: essName,
			addVal: addVal,
			addName: addName,
			compSelect: compSelect,
			menuPrice: menuPrice,
			submenu : submenu,
			storeKey: $("#storeKey").val(),
			basket: $("#basket").val(),
			table: $("#table").val(),
			time: $("#time").val(),
			refiTel: $("#refiTel").val()
	};
	$.ajax({
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		url: "/smilepay/basket",
		data: JSON.stringify(data),
		success: function (resData) {
			var $selMenu = $(".sel_menu"); 
			$selMenu.removeClass('no_sel');
			$selMenu.html("");
			
			var tCnt = 0, tPrice = 0;
			var selList = '<ul class="sel_list">';
			if(resData.length > 0){
				for(var i = 0; i < resData.length; i++) {
					var resDataNe = resData[i];
				 	tCnt += parseInt(resDataNe.orderCount);
				 	tPrice += (resDataNe.orderCount * resDataNe.toPrice);
				 	selList = basketAdd(selList, resDataNe.menuId, resDataNe.id, resDataNe.name, resDataNe.orderCount, resDataNe.price, resDataNe.toPrice, 
				 			resDataNe.compSelect, resDataNe.packing, resDataNe.essVal, resDataNe.essName, resDataNe.addVal, resDataNe.addName, resDataNe.subMenu.trim());
				}
			}
			selList += '</ul>';
			selList += '<ul><li class="total row">';
			selList += '<div class="name">${pay_total}</div><div class="count">'+tCnt+'</div>';
			selList += '<div class="price">'+numberCom(tPrice)+'${pay_won}</div>';
			selList += '</li></ul>';
			selList += '<ul class="btn_area row"><li class="full"><div class="button medium red full circle" onclick="compltePage();">${pay_step1}</div></li></ul>';
			
			$selMenu.append(selList);
			
			$('.dim').remove();
			$('.popup_area').hide();
			$('body').removeClass('fix');
			
			$(".marBot").attr('style','margin-bottom: 210px');
		},
		error: function (e) {
			console.log(e);
		}
	});
}
function basketAdd(selList, menuId, basketLiId, name, orderCnt, price, totalprice, compSelect, packing, essVal, essName, addVal, addName, submenu){
	
	if(submenu != ""){
		selList += '<li class="sel row" style="height: 21px;">';
	}else{
		selList += '<li class="sel row">';	
	}
	selList += '<div class="name menuChange">'+name+'</div>';
	selList += '<div class="count">'+orderCnt+'</div>';
	selList += '<div class="price">'+numberCom((orderCnt * totalprice))+'${pay_won}</div>';
	selList += '<a href="#" class="del" onclick="menuDelete('+basketLiId+', \''+compSelect+'\')">×</a>';
	selList += '<input type="hidden" name="compSelect" value="'+compSelect+'" />';
	selList += '<input type="hidden" name="menuId" value="'+menuId+'" />';
	selList += '<input type="hidden" name="basketLiId" value="'+basketLiId+'" />';
	selList += '<input type="hidden" name="toCount" value="'+orderCnt+'" />';
	selList += '<input type="hidden" name="price" value="'+price+'" />';
	selList += '<input type="hidden" name="toPrice" value="'+totalprice+'" />';
	selList += '<input type="hidden" name="packing" value="'+packing+'" />';
	selList += '<input type="hidden" name="essVal" value="'+essVal+'" />';
	selList += '<input type="hidden" name="essName" value="'+essName+'" />';
	selList += '<input type="hidden" name="addVal" value="'+addVal+'" />';
	selList += '<input type="hidden" name="addName" value="'+addName+'" />';
	selList += '</li>';
	
	if(submenu != ""){
		selList += '<li class="sel row sub_'+compSelect+'" style="height: 17px;"><div style="width: 90%; font-size: 12px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">';
		selList += (submenu.trim()+'</div></li>');	
	}
	
	return selList;
}

function changePop(compSelect, menuId, basketLiId, toCount, price, toPrice, packing, essVal, addVal){
	
	var $home = null;
	$(".marBot .menu_list .menu").each(function(){
		if(menuId == $(this).attr("meli")){
			$home = $(this);
		}
	})
	
	if($home == null || $home.hasClass('no_order')) {
		return false;
	}
	
	var prod_img = $home.find('.img_wrap img').attr('src');
	var nameView = $home.find('.name').text();
	var priceView = $home.find('.price').text();
	var prod_price = $home.find('input[name=price]').val();
	var prod_Id = $home.find('input[name=productId]').val();
	var intro = $home.find('input[name=intro]').val();
	var $opList = $home.find(".optionList");
	
	var popup = $('.popup_area');
	popup.find('.img_wrap img').attr('src', prod_img);
	popup.find('.title').text(nameView);
	popup.find('.price').text(priceView);
	popup.find('.count_wrap .count').text(toCount);
	popup.find('.info_wrap .additional').text(intro);
	
	optionListAdd(popup, $opList, essVal, addVal);
	
	popup.find('input[name="befCompSe"]').val("U");
	popup.find('input[name="id"]').val(prod_Id);
	popup.find('input[name="basketLiId"]').val(basketLiId);
	popup.find('input[name="name"]').val(nameView);
	popup.find('input[name="price"]').val(price);
	popup.find('input[name="count"]').val(toCount);
	popup.find('input[name="src"]').val(prod_img);
	popup.find('input[name="intro"]').val(intro);
	popup.find('input[name="packing"]').val(packing);
	popup.find('input[name="popChoose"]').val("change"); // 메뉴 클릭 인지 아닌지 확인.
	
	popup.show().scrollTop(0);
	
	$('body').addClass('fix').append('<div class="dim"></div>');
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
			var $selMenu = $(".sel_menu"); 
			$selMenu.html("");
			if(resData.length > 0){
				var tCnt = 0, tPrice = 0;
				var selList = '<ul class="sel_list">';
				for(var i = 0; i < resData.length; i++) {
					var resDataNe = resData[i];
				 	tCnt += parseInt(resDataNe.orderCount);
				 	tPrice += (resDataNe.orderCount * resDataNe.toPrice);
				 	selList = basketAdd(selList, resDataNe.menuId, resDataNe.id, resDataNe.name, resDataNe.orderCount, resDataNe.price, resDataNe.toPrice, 
				 			resDataNe.compSelect, resDataNe.packing, resDataNe.essVal, resDataNe.essName, resDataNe.addVal, resDataNe.addName, resDataNe.subMenu.trim());
				}
				
				selList += '</ul>';
				selList += '<ul><li class="total row">';
				selList += '<div class="name">${pay_total}</div><div class="count">'+tCnt+'</div>';
				selList += '<div class="price">'+numberCom(tPrice)+'${pay_won}</div>';
				selList += '</li></ul>';
				selList += '<ul class="btn_area row"><li class="full"><div class="button medium red full circle" onclick="compltePage();">${pay_step1}</div></li></ul>';
				
				$selMenu.append(selList);
			}else{
				$selMenu.addClass("no_sel");
				$selMenu.html("<span>선택된 메뉴가 없습니다.</span>");
				
				$(".marBot").attr('style','margin-bottom: 30px');
			}
		},
		error: function (e) {
			console.log(e);
		}
	});
}

function compltePage(){
	var $orderSmilepay = $("#orderSmilepay");
	$orderSmilepay.html("");

	var menOrderSelList = '<input type="hidden" name="storeKey" value="'+$("#storeKey").val()+'" />';
	menOrderSelList += '<input type="hidden" name="basket" value="'+$("#basket").val()+'" />';
	menOrderSelList += '<input type="hidden" name="table" value="'+$("#table").val()+'" />';
	menOrderSelList += '<input type="hidden" name="time" value="'+$("#time").val()+'" />';

	$orderSmilepay.append(menOrderSelList);
	smilepay();
}p

function smilepay(){
	var orderSmilepay = document.orderSmilepay;
	orderSmilepay.submit();
}

</script>

<script src="/resources/vendor/lib/validate/validate.ko.js"></script>
</html>