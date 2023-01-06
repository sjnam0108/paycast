<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<!DOCTYPE html>
<html lang="${html_lang}">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>${storeName}</title>
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/reset.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/ui.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/jquery-ui.min.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/style.css">
	
	<link rel="stylesheet" href="/resources/vendor/css/bootstrap.css">
    <link rel="stylesheet" href="/resources/vendor/css/appwork.css">
    <link rel="stylesheet" href="/resources/vendor/css/theme-bbmc-twitlight-blue.css">
    <link rel="stylesheet" href="/resources/vendor/css/colors.css">
    <link rel="stylesheet" href="/resources/vendor/css/uikit.css">
    	
	<link rel="stylesheet" href="/resources/vendor/lib/toastr/toastr.css">
	<link rel="stylesheet" href="/resources/vendor/lib/bootstrap-select/bootstrap-select.css">
	<link rel="stylesheet" type="text/css" href="/resources/css/timepicki.css">
	
	<script src="<c:url value='/resources/js/jquery.min.js' />"></script>
	<script src="/resources/selfmenu/menuOrderList/js/jquery-ui.min.js"></script>
	<script src="/resources/selfmenu/menuList/js/appMenu.js?ver=${date}" charset="UTF-8"></script>
	<script src="/resources/selfmenu/menuList/js/lastMenu.js?ver=${date}" charset="UTF-8"></script>
	<script src="/resources/js/timepicki.js"></script>
	
	<script language="javascript" src="${mainScript}"></script>
</head>
<script type="text/javascript">
	window.addEventListener('load', function(){
		setTimeout(scrollTo, 0, 0, 1);
	}, false);
</script>
<body>
	<div class="wrapper reset" >
		<div class="header taC">
			<c:choose>
				<c:when test="${mobileLogoType eq 'I'}">
					<div class="logo"><img src="${storeDownLocation}/${mLogoImageFilename}" alt=""></div>	
				</c:when>
				<c:otherwise>
					<div class="logo" style="font-size: 27px;">${mobileLogoText}</div>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="container order mb-0">
			<div class="innerDiv">
				<div class="order_list">
					<div class="card mb-4" >
						<div class="card-body">
							<div class="row">
								<label class="col-form-label col-sm-12 font-type1 textUnder">1. 주문정보</label>
							</div>
							<c:if test="${mOrderType ne 'type3'}">
								<div class="row">
									<label class="col-form-label col-sm-12 font-type5">주문유형</label>
								</div>
							</c:if>							
							<c:choose>
								<c:when test="${mOrderType eq 'type1'}">
									<%-- [type1 : 매장 / 포장 만 되며 배달은 매장 문의 문구 추가]--%>
									<div class="btn-group btn-group-lg" style="width: 100%;">
										<button type="button" class="btn btn-lg btn-outline-dark" id="orderType_S" name="orderType" value="S" style="width: 33.333%;"><b>매장</b></button>
										<button type="button" class="btn btn-lg btn-outline-dark" id="orderType_P" name="orderType" value="P" style="width: 33.333%;"><b>포장</b></button>
										<button type="button" class="btn btn-lg btn-outline-dark" id="orderType_D" name="orderType" value="D" style="width: 33.333%;"><b>배달</b></button>
				                    </div>
									<input type="hidden" name="packing" value="0"/>
									<input type="hidden" name="orderTypeClick" value="S"/>
								</c:when>
								<c:when test="${mOrderType eq 'type3'}">
									<%-- [type3 : 배달만 되며 모두 숨김처리]--%>
									<div class="btn-group btn-group-lg" style="width: 100%;">
										<button type="button" class="btn btn-lg btn-outline-dark" style="display: none;" id="orderType_S" name="orderType" value="S" style="width: 33.333%;"><b>매장</b></button>
										<button type="button" class="btn btn-lg btn-outline-dark" style="display: none;" id="orderType_P" name="orderType" value="P" style="width: 33.333%;"><b>포장</b></button>
										<button type="button" class="btn btn-lg btn-outline-dark" style="display: none;" id="orderType_D" name="orderType" value="D" style="width: 33.333%;"><b>배달</b></button>
				                    </div>
									<input type="hidden" name="packing" value="0"/>
									<input type="hidden" name="orderTypeClick" value="D"/>
								</c:when>
								<c:otherwise>
									<%-- [type2 : 매장 / 포장 / 배달 모두 가능]--%>
									<div class="btn-group btn-group-lg" style="width: 100%;">
										<button type="button" class="btn btn-lg btn-outline-dark" id="orderType_S" name="orderType" value="S" style="width: 33.333%;"><b>매장</b></button>
										<button type="button" class="btn btn-lg btn-outline-dark" id="orderType_P" name="orderType" value="P" style="width: 33.333%;"><b>포장</b></button>
										<button type="button" class="btn btn-lg btn-outline-dark" id="orderType_D" name="orderType" value="D" style="width: 33.333%;"><b>배달</b></button>
				                    </div>
           							<input type="hidden" name="packing" value="0"/>
									<input type="hidden" name="orderTypeClick" value="S"/>
								</c:otherwise>
							</c:choose>
							
							<c:choose>
								<c:when test="${fn:length(timeList) <= 0}">
									<div name="timeView" style="display: none;">
                      					<input type="hidden" name="bookingTime" class="form-control" value=""/>
									</div>
								</c:when>
								<c:otherwise>
									<div name="timeView" style="display: none;">
										<c:if test="${charYN}">
			                   				<div class="row">
			                   					<label class="col-form-label col-sm-12 font-type5">${pay_reservationTime}</label>
											</div>
			                   				<div class="row">
												<label class="col-form-label col-sm-12 font-type5">${pay_msg_readyYouWant}</label>
											</div>
											<div class="mb-1 row" >
												<div class="col-sm-auto">
													<c:forEach var="item" items="${timeList}" varStatus="st" >
														<c:choose>
															<c:when test="${not item.clickTF}">
																<%-- 2019.12.24 사용하지 않는 에약시간 버튼 HIDE 처리 --%>
																<%-- <button type="button" name="darkTime" class="btn btn-lg btn-dark mt-1" disabled value="${item.timeNum}">${item.timeName}</button> --%>
															</c:when>
															<c:otherwise>
																<button type="button" name="darkTime" class="btn btn-lg btn-outline-dark mt-1" value="${item.timeNum}">${item.timeName}</button>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</div>
											</div>
											<div class="row">
												<label class="col-form-label col-sm-12 font-type5">${pay_msg_chooseYouWant}</label>
												<div class="col-sm-12">
			                      					<input type="text" name="bookingTime" class="form-control" value="" />
			                    				</div>
											</div>
										</c:if>
									</div>
								</c:otherwise>
							</c:choose>
							
							<form id="addrForm" name="addrForm" onSubmit="return false;">	
								<div name="infoDiv2" class="divHidden">
									<c:choose>
										<c:when test="${mOrderType eq 'type1'}">
											<input type="hidden" name="roadAddr" readonly="readonly" value="" />
											<input type="hidden" name="addrDetail" readonly="readonly" value="" />
											<input type="hidden" name="deliTel" readonly="readonly" value="" />
											<div class="form-group">
												<h1>배달 주문은 매장에 문의해 주세요.</h1>
											</div>
											<div class="form-group">
												<h1><span style="text-decoration:underline;">${phone}</span></h1>
											</div>
											<button  type="button" class="button large yell full circle" onclick="location.href='tel:${phone}'" >전화걸기</button>
										</c:when>
										<c:otherwise>
			                   				<div class="row">
												<label class="col-form-label col-sm-6 font-type5">${pay_deliveryInfo}<span style="color: red; font-size: 4vw;"> &nbsp; (필수 *)</span></label>
											</div>
											<div class="form-group row" style="margin-bottom: 0px;">
												<div class="col-sm-10 errorMsg" style="margin-bottom: 1%;">
			                      					<input type="text" name="roadAddr" class="form-control redInput" readonly="readonly" placeholder="${pay_addr1}"/>
			                      					<label class="error colorChg" for="roadAddr" generated="true" style="display:none;"></label>
			                    				</div>
												<div class="col-sm-10 errorMsg" style="margin-bottom: 1%;">
			                      					<input type="text" name="addrDetail" class="form-control redInput" readonly="readonly" placeholder="${pay_addr2}"/>
			                      					<label class="error colorChg" for="addrDetail" generated="true" style="display:none;"></label>
			                    				</div>
												<div class="col-sm-10 errorMsg" style="margin-bottom: 1%;">
													<c:choose>
														<c:when test="${not empty refiTel}">
<%-- 			                      							<input type="text" name="deliTel" class="form-control" readonly="readonly" value="${refiTel}"/> --%>
			                      							<input type="number" name="deliTel" class="form-control" readonly="readonly" value="${refiTel}"/>
			                      						</c:when>
														<c:otherwise>
<%-- 															<input type="text" name="deliTel" class="form-control redInput" placeholder="${pay_msg_phone}" onKeyup="inputPhoneNumber(this);" maxlength="15"/> --%>
															<input type="number" name="deliTel" class="form-control redInput" placeholder="${pay_msg_phone}" onKeyup="inputPhoneNumber(this);" maxlength="15"/>
														</c:otherwise>
													</c:choose>
													<label class="error colorChg" for="deliTel" generated="true" style="display:none;"></label>
			                    				</div>
											</div>
											<p style="font-size:1rem;">※ 택배 신청의 경우에는 성함을 꼭 기입해 주세요</p>
										
										</c:otherwise>
									</c:choose>
								</div>
							</form>
							
							<div id="type1out">
							
							<form id="telForm" name="telForm" onSubmit="return false;" >
								<div class="row" name="telDiv">
									<label class="col-form-label col-sm-12 font-type5">휴대폰 전화</label>
									<div class="col-sm-10 errorMsg">
										<c:choose>
											<c:when test="${'' eq refiTel}">
												<input type="number" name="BuyerTel" class="form-control" placeholder="${pay_msg_phone}" value="${refiTel}" onKeyup="inputPhoneNumber(this);" maxlength="15"/>
<%-- 												<input type="text" name="BuyerTel" class="form-control" placeholder="${pay_msg_phone}" value="${refiTel}" onKeyup="inputPhoneNumber(this);" maxlength="15"/> --%>
											</c:when>
											<c:otherwise>
												<input type="text" name="BuyerTel" class="form-control" readonly="readonly" value="${refiTel}" />										
											</c:otherwise>
										</c:choose>
										<label class="error colorChg" for="BuyerTel" generated="true" style="display:none;"></label>
									</div>
								</div>
							</form>
							
							<hr class="m-2 addhr3">
							
							<div class="row">
								<label class="col-form-label col-sm-12 font-type1 textUnder">2. 요청 메시지</label>
							</div>
							
	                   		<div class="row">
								<label class="col-form-label col-sm-12 font-type5">매장으로</label>
							</div>
							<div class="row" style="margin-bottom: 0px;">
								<div class="col-sm-12">
                     				<input type="text" name="storeSPMsg" class="form-control" placeholder="${pay_storeMsgEx}" maxlength="100"/>
                   				</div>
							</div>
							<div name="riderDiv">
		                   		<div class="row divHidden">
									<label class="col-form-label col-sm-12 font-type5">라이더에게</label>
								</div>
								<div class="row divHidden" style="margin-bottom: 0px;">
									<div class="col-sm-12">
										<input type="text" name="deliMsg" class="form-control" placeholder="${pay_deliMsgEx}" maxlength="100"/>
	                   				</div>
								</div>
							</div>
							
							<hr class="m-2 addhr3">
							
							<div class="row">
								<label class="col-form-label col-sm-12 font-type1 textUnder">3. 결제</label>
								<div id="minOrderDiv" class="divHidden">
									<label class="col-form-label col-sm-12 font-type5">※ 최소 주문 금액 : ${minOrderPriceCom} 원</label>
									<input type="hidden" name="minOrderPriceCom" value="${minOrderPriceCom}">
									<input type="hidden" name="minOrderPrice" value="${minOrderPrice}">
								</div>
							</div>
							
							<c:choose>
								<c:when test="${savingType eq 'PO'}">
									<div class="row">
										<label class="col-form-label col-sm-12 font-type5">포인트 조회/사용</label>
										<input type="hidden" name="coupon" value="0" >
										<input type="hidden" name="couponSelect" value="0" >
									</div>
									<div class="row">
										<div class="col-sm-10 errorMsg" style="width: 70%; margin-right: 6%;" >
											<input type="text" name="pointText" class="form-control underIn font-type4 pr-2" style="width: 85%; float: left" readonly="readonly" value="0"/>
											<input type="hidden" name="pointTotal" value="0">
											<label class="col-form-label font-type5" style="width: 5%; text-align: center;">p</label>
										</div>
										<button type="button" class="btn btn-sm btn-default" style="margin-bottom: 2%;" onclick="pointJoin();">조회</button>
									</div>
									<div class="row mt-2">
										<div class="col-sm-10 errorMsg" style="width: 70%; margin-right: 6%;" >
											<input type="number" maxlength="7" name="usePoint" class="form-control pr-2" style="text-align: right; width: 85%; float: left" onKeyup="usePointfn(this);" onfocus="pointChg(this);" value="0" />
											<label class="col-form-label font-type5" style="width: 5%; text-align: center;">p</label>
										</div>
										<button type="button" class="btn btn-sm btn-default" onclick="pointCalc();">사용</button>
									</div>
								</c:when>
								<c:when test="${savingType eq 'CP'}">
									<div class="row">
										<label class="col-form-label col-sm-12 font-type5">쿠폰 조회/사용</label>
										<input type="hidden" name="pointTotal" value="0" >
										<input type="hidden" name="usePoint" value="0" >
									</div>
									<div class="row">
										<div class="col-sm-10 errorMsg" style="margin-right: 6%;" >
											<input type="hidden" name="coupon" value="0" >
											<!-- <select class="form-control couponSelect" name="couponSelect" style="width: 100%;" onclick="couponFindFn();" onchange="chageCoupon();" > -->
											<select class="form-control" name="couponSelect" style="width: 100%;" onchange="chageCoupon();" >
												<option value="0" id="0">쿠폰없음</option>
											</select>
										</div>
									</div>
								</c:when>
								<c:otherwise>
									<input type="hidden" name="coupon" value="0" >
									<input type="hidden" name="couponSelect" value="0" >
									<input type="hidden" name="pointTotal" value="0" >
									<input type="hidden" name="usePoint" value="0" >
								</c:otherwise>								
							</c:choose>
							
							<c:choose>
								<c:when test="${(savingType eq 'PO') or (savingType eq 'CP')}">
									<div class="row mt-2">
										<label class="col-form-label col-sm-12 font-type5" style="width: 35%;">주문금액</label>
										<input type="text" class="form-control underIn font-type4 pr-2" style="width: 50%;" readonly="readonly" value="${goodsAmtCom}" />
										<input type="hidden" name="goodsAmtPrice" value="${goodsAmt}">
										<label class="col-form-label col-sm-12 font-type5" style="width: 10%;">원</label>
									</div>
									<div class="row">
										<label class="col-form-label col-sm-12 font-type5" style="width: 35%;">할인금액</label>
										<input type="text" id="discountCom" class="form-control underIn font-type4 pr-2" style="width: 50%;" readonly="readonly" value="0"/>
										<input type="hidden" name="discount" value="0">
										<label class="col-form-label col-sm-12 font-type5" style="width: 10%;">원</label>
									</div>
								
								</c:when>
								<c:otherwise>
									<div class="row mt-2">
										<label class="col-form-label col-sm-12 font-type5" style="width: 35%;">주문금액</label>
										<input type="text" class="form-control underIn font-type4 pr-2" style="width: 50%;" readonly="readonly" value="${goodsAmtCom}" />
										<input type="hidden" name="goodsAmtPrice" value="${goodsAmt}">
										<label class="col-form-label col-sm-12 font-type5" style="width: 10%;">원</label>
									</div>
									<input type="hidden" name="discount" value="0">
								</c:otherwise>
							</c:choose>
							
							<div class="row" id="deliveryPayDiv" class="divHidden">
										<label class="col-form-label col-sm-12 font-type5" style="width: 35%;">배달요금</label>
										<input type="text" id="deliveryPay" class="form-control underIn font-type4 pr-2" style="width: 50%;" readonly="readonly" value="${deliveryPayCom}"/>
										<input type="hidden" name="deliveryPay" value="${deliveryPayAmt}">
										<label class="col-form-label col-sm-12 font-type5" style="width: 10%;">원</label>
							</div>
							<div class="row">
								<label class="col-form-label col-sm-12 font-type5" style="width: 35%;">결제금액</label>
								<input type="text" id="paymentCom" class="form-control underIn font-type4 pr-2" style="width: 50%;" readonly="readonly" value="${goodsAmtCom}"/>
								<input type="hidden" name="payment" value="${goodsAmt}">
								<input type="hidden" name="goodsAmtCom" value="${goodsAmtCom}">
								<input type="hidden" name="goodsAmt" value="${goodsAmt}">
								<input type="hidden" name="billingAmtCom" value="${billingAmtCom}">
								<input type="hidden" name="billingAmt" value="${billingAmt}">
								<label class="col-form-label col-sm-12 font-type5" style="width: 10%;">원</label>
							</div>
							
							<form id="consentForm" name="consentForm" onSubmit="return false;" >								
								<div class="row">
									<label class="col-form-label col-sm-6 font-type1 ">
	          							<label class="custom-control custom-checkbox">
						                    <input type="checkbox" class="custom-control-input option" name="consentALL"/>
											<span class="custom-control-label all">${pay_agreeAll}</span>
										</label>
					            	</label>
									<label class="col-form-label col-sm-12 font-type5">${pay_14year}</label>
								</div>
								<div class="row">
									<label class="col-form-label col-sm-12 font-type5 errorMsg">
										<div id="consent2Div" class="btn-group" style="width: 100%;">
											<button type="button" class="btn btn-default" style="width: 80%; padding-left: 2%;" >
											<label class="custom-control custom-checkbox m-0">
												<c:choose>
													<c:when test="${not empty refiTel}">
														<input type="checkbox" class="custom-control-input option" checked="checked" name="consent2">
		                      						</c:when>
													<c:otherwise>
														<input type="checkbox" class="custom-control-input option" name="consent2">
													</c:otherwise>
												</c:choose>
						                      <span class="custom-control-label sizeChg colorChg">${pay_consent1}</span>
						                    </label>
											<button type="button" class="btn btn-default dropdown-toggle dropdown-toggle-split" style="width: 21%;" data-toggle="dropdown" onclick="textAreaToggle(2);"></button>
											<div class="divHidden"><div class="dropdown-menu"></div></div>
					                    </div>
					                    <label class="error colorChg small" for="consent2" generated="true" style="display:none;"></label>
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
	              				<div class="row">
									<label class="col-form-label col-sm-12 font-type5 errorMsg">
										<div id="consent3Div" class="btn-group" style="width: 100%;">
											<button type="button" class="btn btn-default" style="width: 80%; padding-left: 2%;" >
											<label class="custom-control custom-checkbox m-0">
						                      <input type="checkbox" class="custom-control-input option" name="consent3">
						                      <span class="custom-control-label sizeChg colorChg">${pay_agreeTitle}</span>
						                    </label>
											<button type="button" class="btn btn-default dropdown-toggle dropdown-toggle-split" style="width: 21%;" data-toggle="dropdown" onclick="textAreaToggle(3);"></button>
											<div class="divHidden"><div class="dropdown-menu"></div></div>
					                    </div>
					                    <label class="error colorChg small" for="consent3" generated="true" style="display:none;"></label>
									</label>
									<div class="col-sm-2 textAreaDivTogle3" id="consentdiv3">
										<div class="card-body" style="padding: 0px;" >
											<textarea class="form-control" rows="10" readonly="readonly" disabled>1.  개인정보의 수집.이용 목적
													회사는 다음과 같은 목적을 위하여 개인정보를 수집하고 있으며 목적이 변경될 경우에는 사전에 이용자의 동의를 구하도록 하겠습니다.
													① 휴대폰번호, 배달주소
													- 고객님의 주문에 따른 배달 진행, 배달대행 서비스 이용을 위한 정확한 배송지의 확보 및 본인 식별
	
												2.  수집하려는 개인정보의 항목
													① 휴대폰번호, 배달주소
	
												3.  개인정보의 보유 및 이용기간
													- 고객님의 개인정보는 서비스를 제공하는 기간 동안 보유 및 이용하며, 전자상거래 등에서의 소비자보호에 관한 법률에 따라 5년간 보관합니다.
	
													동의를 거부할 수 있으며, 동의 거부 시 서비스가 제공되지 않습니다</textarea>
	           							</div>
	                   				</div>
								</div>
							</form>
							
							
							</div>
						</div>
					</div>
				</div>	
			</div>
		</div>
			
		<c:if test="${storeMid ne 'NoStoreMid'}">
			<div class="order_btn">
				<button type="button" class="button large red full circle" onclick="payCheck('K');">${pay_payment}</button>
			</div>
		</c:if>
		<form id="payCheckForm" name="payCheckForm" method="post" accept-charset="utf-8" Content-Type="application/json" action="/mobileOrder/easyPayCheck"></form>
	</div>
	
</body>
<style type="text/css">
.innerDiv { padding: 10px 10px 0px; }
.divHidden { display: none; }

.redInput{ border-color: red; }

.errorMsg .small{ font-size: 72%; font-weight: 400; }
.errorMsg .colorChg{ color: red; }
.errorMsg .error{ border-color: red; }
.errorMsg .checkColor{ border: 1px ridge red; }

.textAreaDivTogle { display: none; }
.textAreaDivTogle2 { display: none; }
.textAreaDivTogle3 { display: none; }


.radioText { font-size: 22px; font-weight: bold; color: red;}


@media screen and (max-width: 512px){
.amtText { font-size: 6vw; display: block; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;}
.font-type1{ margin-bottom: 0px; font-size:6.5vw; font-weight: 600;}
.font-type2{font-size: 8vw;}
.font-type3{font-size: 6vw; font-weight: 400;}
.font-type4{font-size: 7vw !important; font-weight: 400; margin-bottom: 0px !important;}
.font-type5{ margin-bottom: 0px; font-size:4.5vw; font-weight: 600;}
}

@media screen and (min-width: 513px){
.amtText { font-size: 5vw; display: block; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;}
.font-type1{ margin-bottom: 0px; font-size:6.5vw; font-weight: 600;}
.font-type2{font-size:7vw;}
.font-type3{font-size: 5vw; font-weight: 400;}
.font-type4{font-size: 6vw !important; font-weight: 400; margin-bottom: 0px !important;}
.font-type5{ margin-bottom: 0px; font-size:4.5vw; font-weight: 400;}

.checks input[type="radio"] + label:before { top: 15px;}
.checks input[type="radio"]:checked + label:after { top: 17px;}
}

@media screen and (min-width: 700px){
.amtText { font-size: 4.5vw; display: block; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;}
.font-type1{ margin-bottom: 0px; font-size:5vw; font-weight: 600;}
.font-type2{font-size:5.5vw;}
.font-type3{font-size: 4.5vw; font-weight: 600;}
.font-type4{font-size: 5vw !important; font-weight: 600; margin-bottom: 0px !important;}
.font-type5{ margin-bottom: 0px; font-size:4.5vw; font-weight: 600;}

.checks input[type="radio"] + label:before { top: 12px;}
.checks input[type="radio"]:checked + label:after { top: 15px;}
}

.custom-checkbox .all::before{ top: 23%; left: 0; border: 1px solid rgba(24,28,33,1); background-color: #fff; background-position: center center; background-repeat: no-repeat; -webkit-transition: all .2s; transition: all .2s; pointer-events: auto;}
.custom-checkbox .sizeChg{ font-size: 3vw; }
.custom-checkbox .colorChg::before { border-color: red; }


.textUnder{ text-decoration: underline; text-decoration-color: #ffc000; }
.underIn {border: 0; outline: 0; background: transparent; border-bottom: 2px solid black; text-align:right; padding: 0; background-color: #fff !important;}

.addhr3 { border: 0; height: 3px; background: #ccc; }
</style>

<script>

function timeOutPage() {
	showAlertModalCall("warning", "${pay_msg_timeOutMsg}");
}

function orderBtnOut(orderType){
	if("D" == orderType){
		<c:if test="${mOrderType eq 'type1'}">
			$('.order_btn').hide();
			$("#type1out").addClass('divHidden');
		</c:if>
	}else{
		<c:if test="${mOrderType eq 'type1'}">
			$('.order_btn').show();
			$("#type1out").removeClass('divHidden');
		</c:if>
	}
}

function firstPage(){
	document.location.href="/menu?store=${storeKey}&table=${table}";	
}

function couponFindFn(){
	/* $("#discountCom").val(0); */
	var checkYn = true;
	var orderType = $("input[name=orderTypeClick]").val();
	var buyerTel = $("input[name=BuyerTel]").val();
	var deliTel = $("input[name=deliTel]").val();
	if(buyerTel){
		buyerTel = buyerTel.replace(/-/gi, "");;
	}
	if(deliTel){
		deliTel = deliTel.replace(/-/gi, "");
	}
	
	if(orderType == "D"){
		if(!deliTel){
			var addrForm = $("#addrForm").valid();
			if(!addrForm){
				checkYn = false;
			}
		}
	}else{
		if(!buyerTel){
			var telFormVa = $("#telForm").valid();
			if(!telFormVa){
				checkYn = false;
			}
		}
	}
	
	if(!buyerTel && !deliTel){
		showAlertModal("warning", "휴대폰번호를 입력해 주세요.");
		return;
	}
	
	if(checkYn){
		var data = {
 			storeId: '${storeId}',
 			tel: buyerTel,
 			deliTel: deliTel
	 	};
		
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "/mobileOrder/readcoupon",
			data: JSON.stringify(data),
			success: function (data) {
				$("select[name=couponSelect]").html("");
				if(data.length > 0){
					$("select[name=couponSelect]").append($('<option>', { text: "쿠폰선택", value: "0", id: "0" }));
					for (var i = 0; i < data.length; i++) {
						$("select[name=couponSelect]").append($('<option>', { text: data[i].name, value: data[i].price, id: data[i].id }));
					}
				}else{
					$("select[name=couponSelect]").append($('<option>', { text: "쿠폰없음", value: "0", id: "0" }));
				}
			},
			error: function () {
				showAlertModal("warning", "쿠폰 조회가 정상적이지 않습니다. ");
			}
		});
	}
}

function pointJoin(){
	var checkYn = true;
	var orderType = $("input[name=orderTypeClick]").val();
	var buyerTel = $("input[name=BuyerTel]").val();
	var deliTel = $("input[name=deliTel]").val();
	if(buyerTel){
		buyerTel = buyerTel.replace(/-/gi, "");;
	}
	if(deliTel){
		deliTel = deliTel.replace(/-/gi, "");
	}
	
	if(orderType == "D"){
		if(!deliTel){
			var addrForm = $("#addrForm").valid();
			if(!addrForm){
				checkYn = false;
			}
		}
	}else{
		if(!buyerTel){
			var telFormVa = $("#telForm").valid();
			if(!telFormVa){
				checkYn = false;
			}
		}
	}
	
	if(!buyerTel && !deliTel){
		showAlertModal("warning", "휴대폰번호를 입력해 주세요.");
		return;
	}
	
	if(checkYn){
		var data = {
 			storeId: '${storeId}',
 			tel: buyerTel,
 			deliTel: deliTel
	 	};
		
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "/mobileOrder/readpoint",
			data: JSON.stringify(data),
			success: function (data) {
				$("input[name=pointTotal]").val(data.point);
				$("input[name=pointText]").val(numberCom(data.point));
			},
			error: function () {
				showAlertModal("warning", "쿠폰 조회가 정상적이지 않습니다. ");
			}
		});
	}
}

$(document).ready(function() {
	setTimeout(timeOutPage, 3600000);
	
	$(document).on("click",".logo",function(){
		firstPage();
	});

	$(document).on("click","button[name=orderType]",function(){
		orderTypeChange($(this).val());
		resetSetting();
	});
	
	orderTypeCheck($("input[name=orderTypeClick]").val());
	
	$("input[name=bookingTime]").timepicki({reset: true}); 
	
	$(document).on("click","input[name=consentALL]",function(){
        var chk = $(this).is(":checked");
        if(chk) {
        	$("input[name=consent2]").prop('checked', true);
        	$("input[name=consent3]").prop('checked', true);
        }else {
        	$("input[name=consent2]").prop('checked', false);
        	$("input[name=consent3]").prop('checked', false);
        }
	});
	
	$(document).on("click","input[name=consent2]",function(){
	   	$("input[name=consentALL]").prop('checked', false);
	   	var chk = $(this).is(":checked");
	   	if(chk){
	   		$("#consent2Div").removeClass('checkColor');
	   	}else{
	   		$("#consent2Div").addClass('checkColor');
	   	}
	});

	$(document).on("click","input[name=consent3]",function(){
	   	$("input[name=consentALL]").prop('checked', false);
	   	var chk = $(this).is(":checked");
	   	if(chk){
	   		$("#consent3Div").removeClass('checkColor');
	   	}else{
	   		$("#consent3Div").addClass('checkColor');
	   	}
	});

	$(document).on("click","input[name=roadAddr]",function(){
		goPopup();
	} );

	$(document).on("click","input[name=addrDetail]",function(){
		goPopup();
	} );

	$(document).on("click","button[name=darkTime]",function(){
		var toDate = new Date();
		toDate.setMinutes(toDate.getMinutes()+ parseInt($(this).val()));
		var ampm = toDate.getHours() < 12 ? "AM" : "PM";
		var hours = (h = toDate.getHours() % 12) ? h : 12;
		if(hours < 10){
			hours = "0"+hours;
		}
		var min = toDate.getMinutes();
		if(min < 10){
			min = "0"+min;
		}		
		
		$("input[name=bookingTime]").val(hours+":"+min+" "+ampm);
	} );
	
	$("#addrForm").validate({
		rules: {
			roadAddr: {
				addr_format:true
			},
			addrDetail: {
				addr_format:true
			},
			deliTel: {
				phone1_format: true, phone_format: true
			}
		}
	});
	
	$("#telForm").validate({
		rules: {
			BuyerTel: {
				phone1_format: true, phone_format: true
			}
		}
	});

	$("#consentForm").validate({
		rules: {
			consent2: {
				consent2_format: true
			},
			consent3: {
				consent3_format: true
			}
		}
	});
	$('input[name=BuyerTel]').blur(function(){
		couponFindFn();
	});
	
	$('input[name=deliTel]').blur(function(){
		couponFindFn();
	});

});



<c:if test="${storeMid ne 'NoStoreMid'}">

function payCheck(payGuBun){
	
	var checkYn = true;
	var orderType = $("input[name=orderTypeClick]").val();
	var consent2 = $("input[name=consent2]").is(":checked");
	var consent3 = $("input[name=consent3]").is(":checked");
	var bookingTime = $("input[name=bookingTime]").val();
	var buyerTel = $("input[name=BuyerTel]").val();
	var storeSPMsg = $("input[name=storeSPMsg]").val();
	var roadAddr = $("input[name=roadAddr]").val();
	var addrDetail = $("input[name=addrDetail]").val();
	var deliTel = $("input[name=deliTel]").val();
	var deliMsg = $("input[name=deliMsg]").val();
	var coupon = $("input[name=coupon]").val();
	var couponSelect = $("select[name=couponSelect]").val();
	var pointTotal = $("input[name=pointTotal]").val();
	var usePoint = $("input[name=usePoint]").val();
	
	var goodsAmtPrice = $("input[name=goodsAmtPrice]").val();
	var minOrderPrice = $("input[name=minOrderPrice]").val();
	var deliveryPay = $("input[name=deliveryPay]").val();
	var discount = $("input[name=discount]").val();
	var payment = $("input[name=payment]").val();
			
	if(buyerTel){
		buyerTel = buyerTel.replace(/-/gi, "");
	}
	if(deliTel){
		deliTel = deliTel.replace(/-/gi, "");
	}

	if(orderType == "D"){
		var addrForm = $("#addrForm").valid();
		var consentFormVa = $("#consentForm").valid();

		if(goodsAmtPrice*1<minOrderPrice*1){
			checkYn = false;
			showAlertModal("warning", "배달 주문은 <br>${minOrderPriceCom}원 이상 주문해주세요.");
		}else if(!addrForm){
			checkYn = false;
 			showAlertModal("warning", "배달정보를 입력해주세요."); 
		}else if(!consentFormVa){
			checkYn = false;
			$("#consent2Div").addClass('checkColor');
			$("#consent3Div").addClass('checkColor');
 			showAlertModal("warning", "개인정보동의를 해주세요.");
		}
	}else{
		if(buyerTel){
			var telFormVa = $("#telForm").valid();
			var consentFormVa = $("#consentForm").valid();
			if(!(telFormVa && consentFormVa)){
				checkYn = false
			}
			if(!consentFormVa){
				$("#consent2Div").addClass('checkColor');
				$("#consent3Div").addClass('checkColor');
			}
		}
		if(orderType == "P"){
			if(!timeChk(bookingTime)){
				checkYn = false;
			}
		}
	}
	
	usePoint = usePoint.replace(/,/gi, "");
	
	var data = {
			orderType: orderType,
			storeId: '${storeId}',
			basket: '${basket}',
			Moid: '${orderNumber}',
			consent2: consent2,
			consent3: consent3,
			bookingTime: bookingTime,
			tel: buyerTel,
			storeSPMsg: storeSPMsg,
			roadAddr: roadAddr,
			addrDetail: addrDetail,
			deliTel: deliTel,
			deliMsg: deliMsg,
			coupon: coupon,
			couponSelect: couponSelect,
			pointTotal: pointTotal,
			usePoint: usePoint,
			goodsAmtPrice: goodsAmtPrice,
			minOrderPrice: minOrderPrice,
			deliveryPay: deliveryPay,
			discount: discount,
			payment: payment
	};
	

	if (checkYn) {
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "/mobileOrder/payCheck",
			data: JSON.stringify(data),
			success: function (res) {
				switch (res){
			    	case "Y" :
	 					if(payGuBun === 'K'){
							if(orderType != "D"){
								deliTel = buyerTel;
							}
	 						 
							var $payCheckForm = $("#payCheckForm");
							$payCheckForm.html("");

							var checkOrder = '<input type="hidden" name="storeKey" value="'+'${storeKey}'+'" />';
							checkOrder += '<input type="hidden" name="storeId" value="'+'${storeId}'+'" />';
							checkOrder += '<input type="hidden" name="storeMid" value="'+'${storeMid}'+'" />';
							checkOrder += '<input type="hidden" name="storeName" value="'+encodeURIComponent('${storeName}')+'" />';
							checkOrder += '<input type="hidden" name="reqUrl" value="'+'${reqUrl}'+'" />';
							checkOrder += '<input type="hidden" name="basket" value="'+'${basket}'+'" />';
							checkOrder += '<input type="hidden" name="orderNumber" value="'+'${orderNumber}'+'" />';
							checkOrder += '<input type="hidden" name="sp_product_expr" value="'+'${sp_product_expr}'+'" />';
							checkOrder += '<input type="hidden" name="sp_vacct_end_time" value="'+'${sp_vacct_end_time}'+'" />';
							checkOrder += '<input type="hidden" name="sp_user_phone1" value="'+deliTel+'" />';
							checkOrder += '<input type="hidden" name="sp_user_addr" value="'+encodeURIComponent(roadAddr + " " + addrDetail)+'" />';
							checkOrder += '<input type="hidden" name="sp_product_nm" value="'+encodeURIComponent('${goodsName}')+'" />';
							checkOrder += '<input type="hidden" name="sp_product_amt" value="'+'${goodsAmt}'+'" />';
							checkOrder += '<input type="hidden" name="coupon" value="'+coupon+'" />';
							checkOrder += '<input type="hidden" name="couponSelect" value="'+couponSelect+'" />';
							checkOrder += '<input type="hidden" name="pointTotal" value="'+pointTotal+'" />';
							checkOrder += '<input type="hidden" name="usePoint" value="'+usePoint+'" />';
							checkOrder += '<input type="hidden" name="discount" value="'+discount+'" />';
							checkOrder += '<input type="hidden" name="payment" value="'+payment+'" />';
							checkOrder += '<input type="hidden" name="deliveryPay" value="'+deliveryPay+'" />';
							checkOrder += '<input type="hidden" name="mainAction" value="'+'${mainAction}'+'" />';
							$payCheckForm.append(checkOrder);
							$payCheckForm.submit();

						}
			        	break;
				    case "R2" :
				    	showAlertModal("warning", "${pay_msg_phoneKeyin}");
				        break;
				    case "R" :
				    	showAlertModal("warning", "${pay_msg_agree1}");
				        break;
				    case "D2" :
				    	showAlertModal("warning", "${pay_msg_agree1}");
				        break;
				    case "D3" :
				    	showAlertModal("warning", "${pay_msg_agree2}");
				        break;
				    case "A" :
				    	showAlertModal("warning", "${pay_msg_addrKeyin}");
				        break;
				    case "R3" :
				    	showAlertModal("warning", "${pay_msg_phoneKeyin}");
				        break;
				    case "DIS" :
				    	showAlertModal("warning", "할인금액이 확인이 필요 합니다.");
				        break;
				    case "IDF" :
				    	showAlertModal("warning", "선택된 쿠폰 확인이 필요합니다.");
				        break;
				    case "POF" :
				    	showAlertModal("warning", "포인트 확인이 필요합니다.");
				        break;
				    case "T1" :
				    	showAlertModal("warning", "배달이 허용된 매장이 아닙니다.");
				        break;
				    case "T3" :
				    	showAlertModal("warning", "매장 사용 및 포장이 허용된 매장이 아닙니다.");
				        break;
				    case "PT" :
				    	document.location.href="/mobileOrder/storeOff?store=${storeKey}&table=${table}";
				        break;
				    case "ET" :
				    	showAlertModal("warning", "영업종료 시간 이후의 예약 시간입니다. <br>영업 종료 시간 : ${endDateText}");
				        break;
				    case "P" :
				    	showAlertModal("warning", "${pay_msg_alertMsg1}");
				    	setTimeout(function() {
				    		document.location.href="/menu?store=${storeKey}&table=${table}";
						}, 5000);
				        break;
				    case "NN" :
				    	showAlertModal("warning", "${pay_msg_alertMsg2}");
				    	setTimeout(function() {
				    		document.location.href="/menu?store=${storeKey}&table=${table}";
						}, 5000);
				        break;
				    case "OFF" :
				    	document.location.href="/mobileOrder/storeOff?store=${storeKey}&table=${table}";
				        break;
				    default :
				    	showAlertModal("warning", "${pay_msg_alertMsg3}");
						setTimeout(function() {
		 					document.location.href="/menu?store=${storeKey}&table=${table}";
						}, 5000);
	 					break;
				}
			}
		});
	}
}

</c:if>

function timeChk(bookingTime){
	if(bookingTime == ""){
		return true;
	}
	
	var hour = 0, min = 0;
	var timeArr = bookingTime.split(" ");
	var timeArr1 = timeArr[0].split(":");
	if(timeArr[1] === "AM"){
		hour = timeArr1[0];
		min = timeArr1[1];
	}else{
		hour = parseInt(timeArr1[0])+12;
		min = timeArr1[1];
	}
	var toDate = new Date();
	toDate.setHours(hour, min, 0, 0);
	var possiblDate = ${possiblDate};
	var endDate = ${endDate};
	if(possiblDate > 0){
		if(possiblDate > toDate.getTime()){
			showAlertModal("warning", "예약 주문은  ${possiblText} 이후 가능하십니다.");
			return false;
		}
	}
	if(endDate > 0){
		if(endDate < toDate.getTime()){
			showAlertModal("warning", "영업종료 시간 이후의 예약 시간입니다. <br>영업 종료 시간 : ${endDateText}");
			return false;
		}
	}
	
	return true;
}

function alertMsg(gubun, text){
	if(gubun === "1"){
		showAlertModal("warning", "사용가능한 포인트는 " + text + "입니다.");
	}else if(gubun === "2"){
		showAlertModal("warning", "쿠폰 사용이 불가능 하십니다.");
		$("select[name=couponSelect]").val(0);
	}
}

function showAlertModal(notifType, msg) {
	var className = "";
	if (notifType) {
		className += "bg-" + notifType + " text-black";
	}
	
	var box = bootbox.alert({
		size: "small",
		title: "알려드립니다.",
		message: msg,
		backdrop: true,
		buttons: {
			ok: {
				label: '확인',
				className: 'btn-'+ notifType
			}
		},
		animate: false,
		show: true,
		className: "modal-level-top"
	}).init(function() {
		setTimeout(function(){
			$('.modal-backdrop:last-child').addClass('modal-level-top');
		});
	});
	
	box.find('.modal-level-top .modal-header').addClass(className);
	box.find('.modal-level-top .close').addClass("text-black");
	box.find('.modal-level-top .modal-dialog').addClass("modal-dialog-vertical-center");
	box.find('.modal-level-top .modal-content').addClass("modal-content-border-1");
	box.find('.modal-level-top .bootbox-body').addClass("font-type3");
}


function resetSetting(){
	$("input[name=BuyerTel]").val("");
	$("input[name=deliTel]").val("");
	
	$("#discountCom").val(0);
	$("#discount").val(0);
	
	$("select[name=couponSelect]").html("");
	$("select[name=couponSelect]").append($('<option>', { text: "쿠폰없음", value: "0", id: "0" }));
	
	
}

function showAlertModalCall(notifType, msg) {
	var className = "";
	if (notifType) {
		className += "bg-" + notifType + " text-black";
	}
	
	var box = bootbox.alert({
		size: "small",
		title: "알려드립니다.",
		message: msg,
		backdrop: true,
		buttons: {
			ok: {
				label: '확인',
				className: 'btn-'+ notifType
			}
		},
		animate: false,
		show: true,
		className: "modal-level-top",
		callback: function (result) {
			firstPage();
	    }
	}).init(function() {
		setTimeout(function(){
			$('.modal-backdrop:last-child').addClass('modal-level-top');
		});
	});
	
	box.find('.modal-level-top .modal-header').addClass(className);
	box.find('.modal-level-top .close').addClass("text-black");
	box.find('.modal-level-top .modal-dialog').addClass("modal-dialog-vertical-center");
	box.find('.modal-level-top .modal-content').addClass("modal-content-border-1");
	box.find('.modal-level-top .bootbox-body').addClass("font-type3");
}

$(document).ready(function() {
	
	$.validator.addMethod('phone_format',
		function(value, element) {
			return this.optional(element) || /^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/.test(value);
		},
		'${pay_msg_phoneCheck}'
	);
	$.validator.addMethod('phone1_format',
		function(value, element) {
			if(!value){
				return false;
			}else{
				return true;
			}
		},
		'${pay_msg_phoneKeyin}'
	);
	$.validator.addMethod('addr_format',
		function(value, element) {
			if(!value){
				return false;
			}else{
				return true;
			}
		},
		'${pay_msg_addrKeyin}'
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
		'${pay_msg_agree1}'
	);
	$.validator.addMethod('consent3_format',
		function(value, element) {
			var consent3 = $("input[name=consent3]").is(":checked");
			if(consent3){
				return true;
			}else{
				return false;
			}
		},
		'${pay_msg_agree2}'
	);
});

</script>

<script src="/resources/vendor/lib/popper/popper.js"></script>
<script src="/resources/vendor/js/bootstrap.js"></script>
<script src="/resources/vendor/lib/validate/validate.ko.js"></script>
<script src="/resources/vendor/lib/bootbox/bootbox.js"></script>

</html>


