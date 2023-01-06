<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
	<script src="/resources/selfmenu/menuList/js/appMenu.js"></script>
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
		<div class="container order" style=" margin-bottom: 0px;">
			<div class="inner">
				<div class="order_list">
					<div class="card mb-4" >
						<div class="card-body">
							<div class="form-group row" style="margin-bottom: 0px; font-size: 18px;">
								<label class="col-form-label col-sm-4 text-sm-left font-type1" style=" position: absolute;">${pay_pdCnt}</label>
								<div class="col-sm-12">
                     					<div class="form-control-plaintext amtText" style="text-align: right;">${totalindex}</div>
                   				</div>
							</div>
							<div class="form-group row" style="margin-bottom: 0px; font-size: 18px;">
								<label class="col-form-label col-sm-4 text-sm-left font-type1" style=" position: absolute;">${pay_pdNm}</label>
								<div class="col-sm-12">
                     					<div class="form-control-plaintext amtText" style="text-align: right; width: 80%; float: right;">${goodsName}</div>
                   				</div>
							</div>
							<div class="form-group row" style="margin-bottom: 0px; font-size: 18px;">
								<label class="col-form-label col-sm-4 text-sm-left font-type1" style=" position: absolute;">${pay_totalPrice}</label>
								<div class="col-sm-12">
                     					<div class="form-control-plaintext amtText" style="text-align: right;">${goodsAmtCom} ${pay_won}</div>
                   				</div>
							</div>
							
							<c:if test="${mOrderType ne 'type3'}">
								<hr class="m-1">
								<div class="form-group row" style="margin-bottom: 5%;">
									<label class="col-form-label col-sm-12 text-sm-left font-type1">${pay_orderChooseTitle}</label>
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
							
							<div name="infoDiv1">
								<hr class="m-1">
								<form id="menuForm" name="menuForm" method="post" >
									<c:choose>
										<c:when test="${empty timeList}">
											<div name="timeView" style="display: none;">
		                      					<input type="hidden" name="bookingTime" class="form-control" value=""/>
											</div>
										</c:when>
										<c:otherwise>
											<div name="timeView" style="display: none;">
				                   				<div class="form-group row" style="margin-bottom: 8%; font-size: 18px;">
													<label class="col-form-label col-sm-12 text-sm-left font-type1" style="position: absolute;">${pay_reservationTime}</label>
												</div>
				                   				<div class="row" style="font-size: 18px; margin-top: 33px;">
													<label class="col-form-label col-sm-12 text-sm-left font-type3">${pay_msg_readyYouWant}</label>
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
												<div class="form-group row" style="margin-bottom: 0px;">
													<label class="col-form-label col-sm-12 text-sm-left font-type3">${pay_msg_chooseYouWant}</label>
													<div class="col-sm-12">
				                      					<input type="text" name="bookingTime" class="form-control" value="" />
				                    				</div>
												</div>
												<hr class="mt-3">
											</div>
										</c:otherwise>
									</c:choose>
									
	                   				<div class="form-group row">
										<label class="col-form-label col-sm-12 text-sm-left font-type1" >${pay_storeMsg}</label>
									</div>
									<div class="form-group row" style="margin-bottom: 0px;">
										<div class="col-sm-12">
	                      					<input type="text" name="storeSPMsg" class="form-control" placeholder="${pay_storeMsgEx}" maxlength="100"/>
	                    				</div>
									</div>
								<c:choose>
									<c:when test="${alimTalkAllowed}">
										<hr class="mt-3">
										<div class="row">
											<label class="col-form-label col-sm-12 text-sm-left font-type3 ">
											 	${pay_msg_phoneMsg}
											</label>
											<div class="col-sm-10 errorMsg">
												<c:choose>
													<c:when test="${not empty refiTel}">
		                      							<input type="text" name="BuyerTel" class="form-control" readonly="readonly" value="${refiTel}"/>
		                      						</c:when>
													<c:otherwise>
														<input type="text" name="BuyerTel" class="form-control" placeholder="${pay_msg_phone}" onKeyup="inputPhoneNumber(this); chgColor(this);"/>
													</c:otherwise>
												</c:choose>
		                    				</div>
											<label class="col-form-label col-sm-12 text-sm-left font-type4 errorMsg">
												<div class="btn-group" style="width: 100%;">
													<button type="button" class="btn btn-default" style="width: 80%; padding-left: 2%;" >
													<label class="custom-control custom-checkbox m-0">
														<c:choose>
															<c:when test="${not empty refiTel}">
																<input type="checkbox" class="custom-control-input option" checked="checked" name="consent">
				                      						</c:when>
															<c:otherwise>
																<input type="checkbox" class="custom-control-input option" name="consent">
															</c:otherwise>
														</c:choose>
								                      <span class="custom-control-label sizeChg">${pay_consent1}</span>
								                    </label>
													<button type="button" class="btn btn-default dropdown-toggle dropdown-toggle-split textAreaDiv" style="width: 21%;" data-toggle="dropdown"></button>
													<div class="dropdown-menu"></div>
							                    </div>
											</label>
										</div>
										<div class="col-sm-2 textAreaDivTogle" id="consentdiv">
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
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${not empty refiTel}">
												<hr class="m-1">
												<div class="form-group row">
													<label class="col-form-label col-sm-2 text-sm-right font-type1" style="font-size: 14px; font-weight: 600;">
													 	${pay_msg_phoneMsg}
													</label>
													<div class="col-sm-10 errorMsg">
															<input type="text" class="form-control" readonly="readonly" value="${refiTel}"/>
															<input type="hidden" name="BuyerTel" class="form-control" value="${refiTel}" />
				                    				</div>
													<label class="col-form-label col-sm-2 text-sm-right font-type1 errorMsg" style="font-size: 18px; font-weight: 600;">
														<div class="btn-group" style="width: 100%;">
															<button type="button" class="btn btn-default" style="width: 80%; padding-left: 2%;" >
															<label class="custom-control custom-checkbox m-0">
															<input type="checkbox" class="custom-control-input option" checked="checked" name="consent">
										                      <span class="custom-control-label colorChg">${pay_consent1}</span>
										                    </label>
															<button type="button" class="btn btn-default dropdown-toggle dropdown-toggle-split textAreaDiv" style="width: 21%;" data-toggle="dropdown"></button>
															<div class="dropdown-menu"></div>
									                    </div>
													</label>
												</div>
											</c:when>
											<c:otherwise>
												<hr class="m-1">
												<div class="form-group row" style="display: none;">
													<label class="col-form-label col-sm-2 text-sm-right font-type1" style="font-size: 14px; font-weight: 600;">
													 	${pay_msg_phoneMsg}
													</label>
													<div class="col-sm-10 errorMsg">
														<input type="text" name="BuyerTel" class="form-control" placeholder="${pay_msg_phone}" onKeyup="inputPhoneNumber(this);"/>													
				                    				</div>
													<label class="col-form-label col-sm-2 text-sm-right font-type1 errorMsg" style="font-size: 18px; font-weight: 600;">
														<div class="btn-group" style="width: 100%;">
															<button type="button" class="btn btn-default" style="width: 80%; padding-left: 2%;" >
															<label class="custom-control custom-checkbox m-0">
															<input type="checkbox" class="custom-control-input option" name="consent">
										                      <span class="custom-control-label sizeChg">${pay_consent1}</span>
										                    </label>
															<button type="button" class="btn btn-default dropdown-toggle dropdown-toggle-split textAreaDiv" style="width: 21%;" data-toggle="dropdown"></button>
															<div class="dropdown-menu"></div>
									                    </div>
													</label>
												</div>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
	                   				</form>
                  				</div>
                  				
                  				<%-- 여기서 부터 배달에 관련된 내용 --%>
                  				<div name="infoDiv2" class="divHidden">
                  				<form id="menuForm2" name="menuForm2" method="post" >
									<hr class="m-1">
									<c:choose>
										<c:when test="${mOrderType eq 'type1'}">
											<input type="hidden" name="roadAddr" readonly="readonly" value="" />
											<input type="hidden" name="addrDetail" readonly="readonly" value="" />
											<input type="hidden" name="deliTel" readonly="readonly" value="" />
											<input type="hidden" name="storeMsg" maxlength="10" value="" />
											<input type="hidden" name="deliMsg" maxlength="10" value="" />
											<input type="checkbox" checked="" name="consent2" style="display: none" />
											<input type="checkbox" checked="" name="consent3" style="display: none" />
											<div class="form-group">
												<h1>배달 주문은 매장에 문의해 주세요.</h1>
											</div>
											<div class="form-group">
												<h1><span style="text-decoration:underline;">${phone}</span></h1>
											</div>
											<button  type="button" class="button large yell full circle" onclick="location.href='tel:${phone}'" >전화걸기</button>
										</c:when>
										<c:otherwise>
			                   				<div class="form-group row" style="margin-bottom: 15%; font-size: 18px;">
												<label class="col-form-label col-sm-6 text-sm-left font-type1" style="position: absolute;">${pay_deliveryInfo}<span style="color: red; font-size: 4vw;"> &nbsp; (필수 *)</span></label>
											</div>
											<div class="form-group row" style="margin-bottom: 0px;">
												<div class="col-sm-10 errorMsg" style="margin-bottom: 1%;">
			                      					<input type="text" name="roadAddr" class="form-control redInput" readonly="readonly" placeholder="${pay_addr1}"/>
			                    				</div>
												<div class="col-sm-10 errorMsg" style="margin-bottom: 1%;">
			                      					<input type="text" name="addrDetail" class="form-control redInput" readonly="readonly" placeholder="${pay_addr2}"/>
			                    				</div>
												<div class="col-sm-10 errorMsg" style="margin-bottom: 1%;">
													<c:choose>
														<c:when test="${not empty refiTel}">
			                      							<input type="text" name="deliTel" class="form-control" readonly="readonly" value="${refiTel}"/>
			                      						</c:when>
														<c:otherwise>
															<input type="text" name="deliTel" class="form-control redInput" placeholder="${pay_msg_phone}" onKeyup="inputPhoneNumber(this);"/>
														</c:otherwise>
													</c:choose>
			                    				</div>
											</div>
			                   				<div class="form-group row" style="margin-bottom: 15%; font-size: 18px;">
												<label class="col-form-label col-sm-8 text-sm-left font-type1" style="position: absolute;">${pay_storeMsg}</label>
											</div>
											<div class="form-group row" style="margin-bottom: 0px;">
												<div class="col-sm-10">
			                      					<input type="text" name="storeMsg" class="form-control" placeholder="${pay_storeMsgEx}" maxlength="100"/>
			                    				</div>
											</div>
											<div class="form-group row" style="margin-bottom: 15%; font-size: 18px;">
												<label class="col-form-label col-sm-8 text-sm-left font-type1" style="position: absolute;">${pay_deliMsg}</label>
											</div>
											<div class="form-group row">
												<div class="col-sm-10">
			                      					<input type="text" name="deliMsg" class="form-control" placeholder="${pay_deliMsgEx}" maxlength="100"/>
			                    				</div>
											</div>
											<hr class="m-1">
											
			           						<div class="form-group row" style="margin-bottom: 0px;">
			           							<label class="col-form-label col-sm-6 text-sm-left font-type1">
			           								<label class="custom-control custom-checkbox">
									                    <input type="checkbox" class="custom-control-input option" name="consentALL" />
														<span class="custom-control-label all">${pay_agreeAll}</span>
													</label>
								            	</label>
												<label class="col-form-label col-sm-12 text-sm-right font-type3">${pay_14year}</label>
											</div>
											<div class="form-group row" style="margin-bottom: 0px;">
												<label class="col-form-label col-sm-12 text-sm-left font-type3 errorMsg">
													<div class="btn-group" style="width: 100%;">
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
									                      <span class="custom-control-label colorChg">${pay_consent1}</span>
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
			               					<div class="form-group row">
												<label class="col-form-label col-sm-12 text-sm-right font-type3 errorMsg">
													<div class="btn-group" style="width: 100%;">
														<button type="button" class="btn btn-default" style="width: 80%; padding-left: 2%;" >
														<label class="custom-control custom-checkbox m-0">
									                      <input type="checkbox" class="custom-control-input option" name="consent3">
									                      <span class="custom-control-label colorChg">${pay_agreeTitle}</span>
									                    </label>
														<button type="button" class="btn btn-default dropdown-toggle dropdown-toggle-split textAreaDiv3" style="width: 21%;" data-toggle="dropdown"></button>
														<div class="dropdown-menu"></div>
								                    </div>
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
										</c:otherwise>
									</c:choose>
								</form>
							</div>
						</div>
					</div>
				</div>	
			</div>
		</div>
			
		<%-- 결제 진행 --%>
		<form id="kcForm" name="kcForm" method="post" >
			<input type="hidden" id="sp_pay_type"          name="sp_pay_type"          value="11" />               				
			<input type="hidden" id="sp_mall_id"           name="sp_mall_id"           value="${storeMid}" />               	
			<input type="hidden" id="sp_mall_nm"           name="sp_mall_nm"           value="${storeName}" />               	
			<input type="hidden" id="sp_order_no"          name="sp_order_no"          value="${orderNumber}" />                
			<input type="hidden" id="sp_currency"          name="sp_currency"          value="00" />              				
			<input type="hidden" id="sp_return_url"        name="sp_return_url"        value="${reqUrl}/easypay/orderSubmit" /> 
			<input type="hidden" id="sp_lang_flag"         name="sp_lang_flag"         value="KOR" />              				
			<input type="hidden" id="sp_charset"           name="sp_charset"           value="UTF-8" />               			
			<input type="hidden" id="sp_user_type"         name="sp_user_type"         value="1" />               				
			<input type="hidden" id="sp_user_id"           name="sp_user_id"           value="" />               				
			<input type="hidden" id="sp_memb_user_no"      name="sp_memb_user_no"      value="" />               				
			<input type="hidden" id="sp_user_nm"           name="sp_user_nm"           value="" />               				
			<input type="hidden" id="sp_user_mail"         name="sp_user_mail"         value="" />              				
			<input type="hidden" id="sp_user_phone1"       name="sp_user_phone1"       value="" />               				
			<input type="hidden" id="sp_user_phone2"       name="sp_user_phone2"       value="" />              
			<input type="hidden" id="sp_user_addr"         name="sp_user_addr"         value="" />              
			<input type="hidden" id="sp_user_define1"      name="sp_user_define1"      value="" />              
			<input type="hidden" id="sp_user_define2"      name="sp_user_define2"      value="" />              
			<input type="hidden" id="sp_user_define3"      name="sp_user_define3"      value="" />              
			<input type="hidden" id="sp_user_define4"      name="sp_user_define4"      value="" />              
			<input type="hidden" id="sp_user_define5"      name="sp_user_define5"      value="" />              
			<input type="hidden" id="sp_user_define6"      name="sp_user_define6"      value="" />              
			<input type="hidden" id="sp_product_type"      name="sp_product_type"      value="0" />             
			<input type="hidden" id="sp_product_expr"      name="sp_product_expr"      value="${sp_product_expr}" />
			<input type="hidden" id="sp_app_scheme"        name="sp_app_scheme"        value="" />  
			
			<%-- 신용카드 --%>
			<input type="hidden" id="sp_usedcard_code"     name="sp_usedcard_code"     value="" />
			<input type="hidden" id="sp_quota"             name="sp_quota"             value="" />
			<input type="hidden" id="sp_os_cert_flag"      name="sp_os_cert_flag"      value="2" />
			<input type="hidden" id="sp_noinst_flag"       name="sp_noinst_flag"       value="" />
			<input type="hidden" id="sp_noinst_term"       name="sp_noinst_term"       value="" />
			<input type="hidden" id="sp_set_point_card_yn" name="sp_set_point_card_yn" value="" />
			<input type="hidden" id="sp_point_card"        name="sp_point_card"        value="" />
			<input type="hidden" id="sp_join_cd"           name="sp_join_cd"           value="" />
			
			<%-- 가상계좌 --%>
			<input type="hidden" id="sp_vacct_bank"       name="sp_vacct_bank"         value="" />
			<input type="hidden" id="sp_vacct_end_date"   name="sp_vacct_end_date"     value="${sp_product_expr}" />
			<input type="hidden" id="sp_vacct_end_time"   name="sp_vacct_end_time"     value="${sp_vacct_end_time}" />
			
			<%-- 선불카드 --%>
			<input type="hidden" id="sp_prepaid_cp" name="sp_prepaid_cp" value="" />
			<input type='hidden' name="sp_product_nm" id="sp_product_nm" value="${goodsName}">
			<input type='hidden' name="sp_product_amt"  id="sp_product_amt" value="${goodsAmt}">
			<input type='hidden' name="sp_window_type"  id="sp_window_type" value="submit">
			<input type='hidden' name="sp_disp_cash_yn"  id="sp_disp_cash_yn" value="N">
			<input type='hidden' name="sp_kmotion_useyn"  id="sp_kmotion_useyn" value="Y">
			
			<%-- 공통 --%>
			<input type="hidden" id="sp_res_cd"              name="sp_res_cd"                value="" /> 
			<input type="hidden" id="sp_res_msg"             name="sp_res_msg"               value="" /> 
			<input type="hidden" id="sp_tr_cd"               name="sp_tr_cd"                 value="" /> 
			<input type="hidden" id="sp_ret_pay_type"        name="sp_ret_pay_type"          value="" /> 
			<input type="hidden" id="sp_trace_no"            name="sp_trace_no"              value="" /> 
			<%-- 가맹점 주문번호 인증요청 필드에 존재.                                                           [필수]가맹점 주문번호 --%>
			<input type="hidden" id="sp_sessionkey"          name="sp_sessionkey"            value="" /> 
			<input type="hidden" id="sp_encrypt_data"        name="sp_encrypt_data"          value="" /> 
			<%-- 가맹점 ID  인증요청 필드에 존재.                                                            [필수]가맹점 ID --%>
			<input type="hidden" id="sp_mobilereserved1"     name="sp_mobilereserved1"       value="${storeKey}" /> 
			<input type="hidden" id="sp_mobilereserved2"     name="sp_mobilereserved2"       value="" /> 
			<input type="hidden" id="sp_reserved1"           name="sp_reserved1"             value="" /> 
			<input type="hidden" id="sp_reserved2"           name="sp_reserved2"             value="" /> 
			<input type="hidden" id="sp_reserved3"           name="sp_reserved3"             value="" /> 
			<input type="hidden" id="sp_reserved4"           name="sp_reserved4"             value="" /> 
			<%-- 신용카드--%>
			<input type="hidden" id="sp_card_code"            name="sp_card_code"            value="" />
			<input type="hidden" id="sp_eci_code"             name="sp_eci_code"             value="" />
			<input type="hidden" id="sp_card_req_type"        name="sp_card_req_type"        value="" />
			<input type="hidden" id="sp_save_useyn"           name="sp_save_useyn"           value="" />
			<input type="hidden" id="sp_card_prefix"          name="sp_card_prefix"          value="" />
			<input type="hidden" id="sp_card_no_7"            name="sp_card_no_7"            value="" />
			<%-- 간편결제 --%>
			<input type="hidden" id="sp_spay_cp"              name="sp_spay_cp"              value="" />
		</form>
		
		<c:if test="${storeMid ne 'NoStoreMid'}">
			<div class="order_btn">
				<button type="button" class="button large red full circle" onclick="payCheck('K');">${pay_payment}</button>
			</div>
		</c:if>
	</div>
	
</body>
<style type="text/css">

.divHidden { display: none; }

.redInput{ border-color: red; }

.errorMsg .small{ font-size: 72%; font-weight: 400; }

.textAreaDivTogle { display: none; }
.textAreaDivTogle2 { display: none; }
.textAreaDivTogle3 { display: none; }

.checks {position: relative; font-size:5vw; margin-bottom: 8px;}
.checks input[type="radio"] { position: absolute; width: 1px; height: 1px; padding: 0; margin: -1px; overflow: hidden; clip:rect(0,0,0,0); border: 0; }
.checks input[type="radio"] + label { display: inline-block; position: relative; padding-left: 30px; cursor: pointer; -webkit-user-select: none; -moz-user-select: none; -ms-user-select: none; }
.checks input[type="radio"] + label:before { content: ''; position: absolute;  left: 0; top: 7px; width: 19px; height: 18px; text-align: center; background: #fafafa; border: 1px solid #cacece; border-radius: 100%; box-shadow: 0px 1px 2px rgba(0,0,0,0.05), inset 0px -15px 10px -12px rgba(0,0,0,0.05); }
.checks input[type="radio"] + label:active:before, .checks input[type="radio"]:checked + label:active:before { box-shadow: 0 1px 2px rgba(0,0,0,0.05), inset 0px 1px 3px rgba(0,0,0,0.1); }
.checks input[type="radio"]:checked + label:before { background: #E9ECEE; border-color: #adb8c0; }
.checks input[type="radio"]:checked + label:after { content: ''; position: absolute; top: 10px; left: 3px; width: 13px; height: 13px; background: #99a1a7; border-radius: 100%; box-shadow: inset 0px 0px 10px rgba(0,0,0,0.3); }

.radioText { font-size: 22px; font-weight: bold; color: red;}

.checks input[type="checkbox"] {  /* 실제 체크박스는 화면에서 숨김 */
  position: absolute;  width: 1px;  height: 1px;  padding: 0;  margin: -1px;  overflow: hidden;  clip:rect(0,0,0,0);  border: 0}
.checks input[type="checkbox"] + label {  display: inline-block;  position: relative;  cursor: pointer;  -webkit-user-select: none;  -moz-user-select: none;  -ms-user-select: none;}
.checks input[type="checkbox"] + label:before {  /* 가짜 체크박스 */
  content: ' ';  display: inline-block;  width: 21px;  /* 체크박스의 너비를 지정 */  height: 0px;  /* 체크박스의 높이를 지정 */  line-height: 0x; /* 세로정렬을 위해 높이값과 일치 */  margin: -2px 8px 0 0;  text-align: center; 
  vertical-align: middle;  background: #fafafa;  border: 1px solid #cacece;  border-radius : 3px;  box-shadow: 0px 1px 2px rgba(0,0,0,0.05), inset 0px -15px 10px -12px rgba(0,0,0,0.05);}
.checks input[type="checkbox"] + label:active:before,
.checks input[type="checkbox"]:checked + label:active:before {  box-shadow: 0 1px 2px rgba(0,0,0,0.05), inset 0px 1px 3px rgba(0,0,0,0.1);}
.checks input[type="checkbox"]:checked + label:before {  /* 체크박스를 체크했을때 */ 
  content: '\2714'; color: #99a1a7;  text-shadow: 1px 1px #fff;  background: #e9ecee;  border-color: #adb8c0;
  box-shadow: 0px 1px 2px rgba(0,0,0,0.05), inset 0px -15px 10px -12px rgba(0,0,0,0.05), inset 15px 10px -12px rgba(255,255,255,0.1); font-size:14px; color:red;}

@media screen and (max-width: 512px){
.amtText { font-size: 6vw; display: block; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;}
.font-type1{ margin-bottom: 0px; font-size:6.5vw; font-weight: 600;}
.font-type2{font-size: 8vw;}
.font-type3{font-size: 6vw; font-weight: 400;}
.font-type4{font-size: 7vw; font-weight: 400;}

}

@media screen and (min-width: 513px){
.amtText { font-size: 5vw; display: block; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;}
.font-type1{ margin-bottom: 0px; font-size:6.5vw; font-weight: 600;}
.font-type2{font-size:7vw;}
.font-type3{font-size: 5vw; font-weight: 400;}
.font-type4{font-size: 6vw; font-weight: 400;}
.checks input[type="radio"] + label:before { top: 15px;}
.checks input[type="radio"]:checked + label:after { top: 17px;}
}

@media screen and (min-width: 700px){
.amtText { font-size: 4.5vw; display: block; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;}
.font-type1{ margin-bottom: 0px; font-size:5vw; font-weight: 600;}
.font-type2{font-size:5.5vw;}
.font-type3{font-size: 4.5vw; font-weight: 600;}
.font-type4{font-size: 5vw; font-weight: 600;}
.checks input[type="radio"] + label:before { top: 12px;}
.checks input[type="radio"]:checked + label:after { top: 15px;}
}

.custom-checkbox .all::before{ top: 23%; left: 0; border: 1px solid rgba(24,28,33,0.1); background-color: #fff; background-position: center center; background-repeat: no-repeat; -webkit-transition: all .2s; transition: all .2s; pointer-events: auto;}
.custom-checkbox .colorChg{ font-size: 3vw; color: red; }
.custom-checkbox .sizeChg{ font-size: 3vw; }
.custom-checkbox .colorChg::before { border-color: red; }
</style>

<script>

function orderTypeCheck(orderTypeChoose){
	orderTypeChange(orderTypeChoose);
}

function orderTypeChange(orderType){
	$("button[name=orderType]").each(function(){
		$(this).removeClass('btn-dark').removeClass('btn-outline-dark');
		if($(this).val() == orderType){
			$(this).addClass('btn-dark');
		}else{
			$(this).addClass('btn-outline-dark');	
		}
	});

	var $packing = $("input[name=packing]");
	$("div[name=timeView]").hide();
	if("D" == orderType){
		$("div[name=infoDiv1]").addClass('divHidden');
		$("div[name=infoDiv2]").removeClass('divHidden');
		$packing.val(0);
		
		<c:if test="${mOrderType eq 'type1'}">
			$('.order_btn').hide();
		</c:if>
	}else{
		if("P" == orderType){
			$packing.val(1);
			$("div[name=timeView]").show();
		}else{
			$packing.val(0);
		}
		$("div[name=infoDiv1]").removeClass('divHidden');
		$("div[name=infoDiv2]").addClass('divHidden');
		<c:if test="${mOrderType eq 'type1'}">
			$('.order_btn').show();
		</c:if>
	}
	$("input[name=orderTypeClick]").val(orderType);
};

$(document).ready(function() {
	setTimeout(timeOutPage, 3600000);
	
	orderTypeCheck($("input[name=orderTypeClick]").val());
	
	function timeOutPage() {
		alert("${pay_msg_timeOutMsg}"); 
		document.location.href="/menu?store=${storeKey}&table=${table}";
	}
	
	$(document).on("click",".logo",function(){
		document.location.href="/menu?store=${storeKey}&table=${table}";
	} );
	$(document).on("click","button[name=orderType]",function(){
		orderTypeChange($(this).val());
	} );
	
	$(document).on("click","input[name=consentALL]",function(){
        var chk = $(this).is(":checked");
        if(chk) {
        	$("input[name=consent2]").prop('checked', true);
        	$("input[name=consent3]").prop('checked', true);
        }else {
        	$("input[name=consent2]").prop('checked', false);
        	$("input[name=consent3]").prop('checked', false);
        }
        $("#menuForm2").valid();
	} );
	$(document).on("click","input[name=consent2]",function(){
       	$("input[name=consentALL]").prop('checked', false);
	} );
	$(document).on("click","input[name=consent3]",function(){
       	$("input[name=consentALL]").prop('checked', false);
	} );
	$(document).on("click",".textAreaDiv",function(){
		$("#consentdiv").toggleClass( 'textAreaDivTogle' );
	} );
	$(document).on("click",".textAreaDiv2",function(){
		$("#consentdiv2").toggleClass( 'textAreaDivTogle2' );
	} );
	$(document).on("click",".textAreaDiv3",function(){
		$("#consentdiv3").toggleClass( 'textAreaDivTogle3' );
	} );
	
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
		
		$("#menuForm input[name=bookingTime]").val(hours+":"+min+" "+ampm);
	} );
	
	$("#menuForm input[name=bookingTime]").timepicki({reset: true}); 
	
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
		'${pay_msg_phoneCheck}'
	);
	$.validator.addMethod('phone1_format',
		function(value, element) {
			if(value == "" || value == null){
				return false;
			}else{
				return true;
			}
		},
		'${pay_msg_phoneKeyin}'
	);
	$.validator.addMethod('addr_format',
		function(value, element) {
			if(value == "" || value == null){
				return false;
			}else{
				return true;
			}
		},
		'${pay_msg_addrKeyin}'
	);
	$.validator.addMethod('consent_format',
		function(value, element) {
			var consent2 = $("input[name=consent]").is(":checked");
			if(consent2){
				return true;
			}else{
				return false;
			}
		},
		'${pay_msg_agree1}'
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
	
	<c:if test="${alimTalkAllowed}">
		$("#menuForm").validate({
			rules: {
				BuyerTel: {
					phone1_format: true, phone_format: true
				},
				consent: {
					consent_format: true
				},
			}
		});
	</c:if>
	
	$("#menuForm2").validate({
		rules: {
			roadAddr: {
				addr_format:true
			},
			addrDetail: {
				addr_format:true
			},
			deliTel: {
				phone1_format: true, phone_format: true
			},
			consent2: {
				consent2_format: true
			},
			consent3: {
				consent3_format: true
			}
		}
	});

});

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
	toDate.setHours(hour, min, 0, 0)
	var possiblDate = ${possiblDate};
	var endDate = ${endDate};
	if(possiblDate > 0){
		if(possiblDate > toDate.getTime()){
			alert("예약 주문은  ${possiblText} 이후 가능하십니다.");
			return false;
		}
	}
	if(endDate > 0){
		if(endDate < toDate.getTime()){
			alert("영업종료 시간 이후의 예약 시간입니다. \n영업 종료 시간 : ${endDateText}");
			return false;
		}
	}
	
	return true;
}

<c:if test="${storeMid ne 'NoStoreMid'}">

function payCheck(payGuBun){
	var checkYn = true;
	var orderType = $("input[name=orderTypeClick]").val();
	var consent = $("input[name=consent]").is(":checked");
	var consent2 = $("input[name=consent2]").is(":checked");
	var consent3 = $("input[name=consent3]").is(":checked");
	var bookingTime = $("#menuForm input[name=bookingTime]").val();
	var buyerTel = $("#menuForm input[name=BuyerTel]").val();
	var storeSPMsg = $("#menuForm input[name=storeSPMsg]").val();
	var roadAddr = $("#menuForm2 input[name=roadAddr]").val();
	var addrDetail = $("#menuForm2 input[name=addrDetail]").val();
	var deliTel = $("#menuForm2 input[name=deliTel]").val();
	var storeMsg = $("#menuForm2 input[name=storeMsg]").val();
	var deliMsg = $("#menuForm2 input[name=deliMsg]").val();
	if(buyerTel != ""){
		buyerTel = buyerTel.replace("-", "");
	}
	if(deliTel != ""){
		deliTel = deliTel.replace("-", "");
	}
	
	var data = {
			orderType: orderType,
			storeId: '${storeId}',
			basket: '${basket}',
			consent: consent,
			consent2: consent2,
			consent3: consent3,
			bookingTime: bookingTime,
			tel: buyerTel,
			storeSPMsg: storeSPMsg,
			roadAddr: roadAddr,
			addrDetail: addrDetail,
			deliTel: deliTel,
			storeMsg: storeMsg,
			deliMsg: deliMsg,
			Moid: '${orderNumber}'
	};
	
	if(orderType == "D"){
		checkYn = $("#menuForm2").valid(); 
	}else{
		<c:if test="${alimTalkAllowed}">
			if(consent){
				checkYn = $("#menuForm").valid();
			}else{
				if(buyerTel != ""){
					checkYn = $("#menuForm").valid();
				}
			}
		</c:if>
		if(orderType == "P"){
			if(!timeChk(bookingTime)){
				return false;
			}
		}
	}
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
	 						$("#kcForm").find("input[name=sp_user_phone1]").val(deliTel);
	 						$("#kcForm").find("input[name=sp_user_addr]").val(roadAddr + " " + addrDetail);
							
							goInterfaceK();
						}
			        	break;
				    case "R2" :
				    	alert("${pay_msg_phoneKeyin}");
				        break;
				    case "R" :
				    	alert("${pay_msg_agree1}");
				        break;
				    case "D2" :
				    	alert("${pay_msg_agree1}");
				        break;
				    case "D3" :
				    	alert("${pay_msg_agree2}");
				        break;
				    case "A" :
				    	alert("${pay_msg_addrKeyin}");
				        break;
				    case "R3" :
				    	alert("${pay_msg_phoneKeyin}");
				        break;
				    case "T1" :
				    	alert("배달이 허용된 매장이 아닙니다.");
				        break;
				    case "T3" :
				    	alert("매장 사용 및 포장이 허용된 매장이 아닙니다.");
				        break;
				    case "PT" :
				    	document.location.href="/mobileOrder/storeOff?store=${storeKey}&table=${table}";
				        break;
				    case "ET" :
				    	alert("영업종료 시간 이후의 예약 시간입니다. \n영업 종료 시간 : ${endDateText}");
				        break;
				    case "P" :
				    	var alertMsg1 = "${pay_msg_alertMsg1}".replace("<br>", "\n");
				    	alert(alertMsg1);
				    	document.location.href="/menu?store=${storeKey}&table=${table}";
				        break;
				    case "NN" :
				    	var alertMsg2 = "${pay_msg_alertMsg2}".replace("<br>", "\n");
				    	alert(alertMsg2);
				    	document.location.href="/menu?store=${storeKey}&table=${table}";
				        break;
				    case "OFF" :
				    	document.location.href="/mobileOrder/storeOff?store=${storeKey}&table=${table}";
				        break;
				    default :
				    	var alertMsg3 = "${pay_msg_alertMsg3}".replace("<br>", "\n");
				    	alert(alertMsg3);
	 					document.location.href="/menu?store=${storeKey}&table=${table}";
	 					break;
				}
			}
		});
	}
}

/* 인증창 호출, 인증 요청 */
function goInterfaceK() 
{
    var frm_pay = document.kcForm;
    
    /*  주문정보 확인 */
    if( !frm_pay.sp_order_no.value ) 
    {
        alert("${pay_msg_kcMsg1}");
        frm_pay.sp_order_no.focus();
        return;
    }

    if( !frm_pay.sp_product_amt.value ) 
    {
        alert("${pay_msg_kcMsg2}");
        frm_pay.sp_product_amt.focus();
        return;
    }
    /* UTF-8 사용가맹점의 경우 EP_charset 값 셋팅 필수 */
    if( frm_pay.sp_charset.value == "UTF-8" )
    {
        // 한글이 들어가는 값은 모두 encoding 필수.
        frm_pay.sp_mall_nm.value      = encodeURIComponent( frm_pay.sp_mall_nm.value );
        frm_pay.sp_product_nm.value   = encodeURIComponent( frm_pay.sp_product_nm.value );
        frm_pay.sp_user_nm.value      = encodeURIComponent( frm_pay.sp_user_nm.value );
        frm_pay.sp_user_addr.value    = encodeURIComponent( frm_pay.sp_user_addr.value );
    }
    document.kcForm.action = "${mainAction}";
	document.kcForm.submit();
}

</c:if>

function goPopup(){
    var pop = window.open("/popup/juso","pop","scrollbars=yes, resizable=yes");
}

function jusoCallBack(roadFullAddr,roadAddrPart1,addrDetail,roadAddrPart2,engAddr, jibunAddr, zipNo, admCd, rnMgtSn, bdMgtSn,detBdNmList,bdNm,bdKdcd,siNm,sggNm,emdNm,
		liNm,rn,udrtYn,buldMnnm,buldSlno,mtYn,lnbrMnnm,lnbrSlno,emdNo, entX, entY){
	$("input[name=roadAddr]").val(roadAddrPart1+" "+roadAddrPart2);
	$("input[name=addrDetail]").val(addrDetail);
}

function chgColor(obj) {
    var telPhone = obj.value;
    if(telPhone == ""){
    	$("input[name=BuyerTel]").parents().find("label .custom-control-label").removeClass("colorChg");
    }else{
    	$("input[name=BuyerTel]").parents().find("label .custom-control-label").addClass("colorChg");
    }
}

</script>

<script src="/resources/vendor/lib/validate/validate.ko.js"></script>

</html>