<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="${html_lang}">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta name="description" content="" />
    <meta name="author" content="Smartro" />
	<title>${storeName}</title>
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/reset.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/ui.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/jquery-ui.min.css">
	<link rel="stylesheet" href="/resources/selfmenu/menuOrderList/css/style.css">
	
	<link rel="stylesheet" href="/resources/vendor/css/bootstrap.css">
    <link rel="stylesheet" href="/resources/vendor/css/appwork.css">
    <link rel="stylesheet" href="/resources/vendor/css/theme-bbmc-twitlight-blue.css">
    <link rel="stylesheet" href="/resources/vendor/css/colors.css">
    
	<link rel="stylesheet" href="/resources/vendor/lib/toastr/toastr.css">
	<link rel="stylesheet" href="/resources/vendor/lib/bootstrap-select/bootstrap-select.css">
	<link rel="stylesheet" href="/resources/vendor/lib/spinkit/spinkit.css">
	
	<script src="<c:url value='/resources/js/jquery.min.js' />"></script>
	<script src="/resources/selfmenu/menuOrderList/js/jquery-ui.min.js"></script>
	
	<script src="https://tmpay.smartropay.co.kr/asset/js/SmartroPAY-1.0.min.js?version=${toDate}"></script>
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

					<form id="approvalForm" name="approvalForm" method="post">
					    <input type="hidden" id="Tid" name="Tid" />
                        <input type="hidden" name="TrAuthKey" />
                    </form>
				




					<div class="card mb-4" >
						<div class="card-body">
							<div class="form-group row" style="margin-bottom: 0px; font-size: 18px;">
								<label class="col-form-label col-sm-2 text-sm-right" style="margin-bottom: 0px; font-size: 20px; font-weight: 600; position: absolute;">${pay_pdCnt}</label>
								<div class="col-sm-10">
                     					<div class="form-control-plaintext" style="text-align: right;">${totalindex}</div>
                   				</div>
							</div>
							<div class="form-group row" style="margin-bottom: 0px; font-size: 18px;">
								<label class="col-form-label col-sm-2 text-sm-right" style="margin-bottom: 0px; font-size: 20px; font-weight: 600; position: absolute;">${pay_pdNm}</label>
								<div class="col-sm-10">
                     					<div class="form-control-plaintext  amtText" style="text-align: right; width: 80%; float: right;">${goodsName}</div>
                   				</div>
							</div>
							<div class="form-group row" style="margin-bottom: 0px; font-size: 18px;">
								<label class="col-form-label col-sm-2 text-sm-right" style="margin-bottom: 0px; font-size: 20px; font-weight: 600; position: absolute;">${pay_totalPrice}</label>
								<div class="col-sm-10">
                     					<div class="form-control-plaintext" style="text-align: right;">${goodsAmtCom} ${pay_won}</div>
                   				</div>
							</div>
							<hr class="m-1">
							
							
							<div class="form-group row" style="margin-bottom: 15%; font-size: 18px;">
								<label class="col-form-label col-sm-2 text-sm-right" style="margin-bottom: 0px; font-size: 20px; font-weight: 600; position: absolute;">주문선택</label>
							</div>
							
							<div class="checks">
								<input type="radio" class="option" id="orderType_S" name="orderType" checked="checked" value="S">
								<label for="orderType_S">매장에서 이용할게요.</label>
							</div>
							<div class="checks">
								<input type="radio" class="option" id="orderType_P" name="orderType" value="P">
								<label for="orderType_P">포장해 주세요.</label>
								<input type="hidden" name="packing" value="0"/>
							</div>
							<div class="checks">
								<input type="radio" class="option" id="orderType_D" name="orderType" value="D">
								<label for="orderType_D">배달해 주세요.</label>
							</div>
							<div name="infoDiv1">
								<c:choose>
									<c:when test="${alimTalkAllowed}">
										<form id="menuForm" name="menuForm" method="post" action="returnMobilePay" >
											<input type="hidden" name="PayMethod" value="${payMethod}"/>
											<input type="hidden" name="GoodsCnt" value="${totalindex}"/>
											<input type="hidden" name="GoodsName" value="${goodsName}"/>
											<input type="hidden" name="Amt" value="${goodsAmt}"/>
											<input type="hidden" name="Moid" value="${orderNumber}"/>
											<input type="hidden" name="Mid" value="${storeMid}"/>
											<input type="hidden" name="ReturnUrl" value="${returnUrl}"/>
											<input type="hidden" name="RetryUrl" value="${retryUrl}"/>
											<input type="hidden" name="StopUrl" value="${stopUrl}"/>
											<input type="hidden" name="BuyerAddr" value=""/>
											<input type="hidden" name="EncryptData" value="${encryptData}"/>
											<input type="hidden" name="MallResultFWD" value="${mallResultFWD}"/>
											<input type="hidden" name="TransType" value="${transType}"/>
											<input type="hidden" name="SocketYN" value="${socketYN}"/>
											<input type="hidden" name="Language" value="${language}"/>
											<input type="hidden" name="EncodingType" value="${encodingType}"/>
											<input type="hidden" name="clientType" value="${clientType}"/>
											<input type="hidden" name="OfferPeriod" value=""/>
											<input type="hidden" name="EdiDate" value="${EdiDate}"/>
											<input type="hidden" name="VatAmt" value=""/>
											<input type="hidden" name="TaxAmt" value=""/>
											<input type="hidden" name="TaxFreeAmt" value=""/>
											<input type="hidden" name="SvcAmt" value=""/>
											<input type="hidden" name="CardQuota" value="${cardQuota}"/>
											<input type="hidden" name="CardInterest" value=""/>
											<input type="hidden" name="CardPoint" value="${cardPoint}"/>
											<input type="hidden" name="IspWapUrl" value="${ispWapUrl}"/>
											<input type="hidden" name="DivideInfo" value="${divideInfo}"/>
											<input type="hidden" name="UserIp" value="${userIp}"/>
											<input type="hidden" name="MallIp" value="${serverIp}"/>
											<input type="hidden" name="FnCd" value=""/>
											<input type="hidden" name="menuInTime" value="${menuInTime}"/>
											<input type="hidden" name="GoodsCl" value="1"/>
											<input type="hidden" name="VbankExpDate" value="99991225"/>

											<hr class="m-1">
			                   				<div class="form-group row" style="margin-bottom: 15%; font-size: 18px;">
												<label class="col-form-label col-sm-2 text-sm-right" style="margin-bottom: 0px; font-size: 20px; font-weight: 600; position: absolute;">매장 요청 메시지</label>
											</div>
											<div class="form-group row">
												<label class="col-form-label col-sm-2 text-sm-right" style="font-size: 14px; font-weight: 600;">
												 	${pay_msg_phoneMsg}
												</label>
												<div class="col-sm-10 errorMsg">
			                      					<input type="text" name="BuyerName" id="BuyerName" class="form-control" placeholder="구매자명"/>
			                    				</div>
												<div class="col-sm-10 errorMsg">
			                      					<input type="email" name="BuyerEmail" id="BuyerEmail" class="form-control" placeholder="구매자 메일주소"/>
			                    				</div>
												<div class="col-sm-10 errorMsg">
			                      					<input type="number" name="BuyerTel" id="BuyerTel" class="form-control" placeholder="${pay_msg_phone}"/>
			                    				</div>
												<label class="col-form-label col-sm-2 text-sm-right errorMsg" style="font-size: 18px; font-weight: 600;">
													<div class="btn-group" style="width: 100%;">
														<button type="button" class="btn btn-default" style="width: 80%; padding-left: 2%;" >
														<label class="custom-control custom-checkbox m-0">
									                      <input type="checkbox" class="custom-control-input option" name="consent">
									                      <span class="custom-control-label">${pay_consent}</span>
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
		                   				</form>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="BuyerTel" class="form-control" value="" />
									</c:otherwise>
								</c:choose>
                  				</div>

								<%--	스마일 페이 사용 값	--%>
					<form id="tranMgr" name="tranMgr" method="post"></form>
					
                  				<%-- 여기서 부터 배달에 관련된 내용 --%>
                  				<div name="infoDiv2" class="divHidden">
                  				<form id="menuForm2" name="menuForm2" method="post" >

									<hr class="m-1">
	                   				<div class="form-group row" style="margin-bottom: 15%; font-size: 18px;">
										<label class="col-form-label col-sm-2 text-sm-right" style="margin-bottom: 0px; font-size: 20px; font-weight: 600; position: absolute;">배달정보</label>
									</div>
									<div class="form-group row" style="margin-bottom: 0px;">
										<div class="col-sm-10 errorMsg" style="margin-bottom: 1%;">
	                      					<input type="text" name="roadAddr" class="form-control" readonly="readonly" placeholder="주소(필수)"/>
	                    				</div>
										<div class="col-sm-10 errorMsg" style="margin-bottom: 1%;">
	                      					<input type="text" name="addrDetail" class="form-control" readonly="readonly" placeholder="상세주소"/>
	                    				</div>


									</div>
	                   				<div class="form-group row" style="margin-bottom: 15%; font-size: 18px;">
										<label class="col-form-label col-sm-2 text-sm-right" style="margin-bottom: 0px; font-size: 20px; font-weight: 600; position: absolute;">매장 요청 메시지</label>
									</div>
									<div class="form-group row" style="margin-bottom: 0px;">
										<div class="col-sm-10">
	                      					<input type="text" name="storeMsg" class="form-control" placeholder="매장 요청 메시지" maxlength="100"/>
	                    				</div>
									</div>
									<div class="form-group row" style="margin-bottom: 15%; font-size: 18px;">
										<label class="col-form-label col-sm-2 text-sm-right" style="margin-bottom: 0px; font-size: 20px; font-weight: 600; position: absolute;">배달 요청 메시지</label>
									</div>
									<div class="form-group row">
										<div class="col-sm-10">
	                      					<input type="text" name="deliMsg" class="form-control" placeholder="배달 요청 메시지" maxlength="100"/>
	                    				</div>
									</div>
									<hr class="m-1">
									
	           						<div class="form-group row" style="margin-bottom: 0px;">
	           							<label class="col-form-label col-sm-2 text-sm-right" style="font-size: 14px; font-weight: 600;">
	           								<label class="custom-control custom-checkbox m-0">
							                    <input type="checkbox" class="custom-control-input option" name="consentALL" />
												<span class="custom-control-label">전체 동의</span>
											</label>
						            	</label>
										<label class="col-form-label col-sm-2 text-sm-right" style="font-size: 14px; font-weight: 600;">
										 	본인은 만 14세 이상 이며, 아래 내용에 동의 합니다. 
										</label>
									</div>
									<div class="form-group row" style="margin-bottom: 0px;">
										<label class="col-form-label col-sm-2 text-sm-right errorMsg" style="font-size: 18px; font-weight: 600;">
											<div class="btn-group" style="width: 100%;">
												<button type="button" class="btn btn-default" style="width: 80%; padding-left: 2%;" >
												<label class="custom-control custom-checkbox m-0">
							                      <input type="checkbox" class="custom-control-input option" name="consent2">
							                      <span class="custom-control-label">${pay_consent}</span>
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
										<label class="col-form-label col-sm-2 text-sm-right errorMsg" style="font-size: 18px; font-weight: 600;">
											<div class="btn-group" style="width: 100%;">
												<button type="button" class="btn btn-default" style="width: 80%; padding-left: 2%;" >
												<label class="custom-control custom-checkbox m-0">
							                      <input type="checkbox" class="custom-control-input option" name="consent3">
							                      <span class="custom-control-label">개인정보 수집 및 이용동의</span>
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

								</form>
							</div>
						</div>
					</div>
				</div>	
			</div>
		</div>

		<form id="kcForm1" name="kcForm1" method="post" >
			<input type="hidden" name="stopUrl" value="${stopUrl}"/>
			<input type="hidden" name="storeName" value="${storeName}"/>
			<input type="hidden" name="payMethod" value="${payMethod}"/>
			<input type="hidden" name="goodsName" value="${goodsName}"/>
			<input type="hidden" name="amt" value="${goodsAmt}"/>
			<input type="hidden" name="moid" value="${orderNumber}"/>
			<input type="hidden" name="reqUrl" value="${reqUrl}"/>
			<input type="hidden" name="telNum" />
		</form>

		<!-- 결제 진행 -->
		<form id="kcForm" name="kcForm" method="post" >
			<input type="hidden" id="sp_pay_type"          name="sp_pay_type"          value="" />               				
			<input type="hidden" id="sp_mall_id"           name="sp_mall_id"           value="${storeMid}" />               	
			<input type="hidden" id="sp_mall_nm"           name="sp_mall_nm"           value="${storeName}" />               	
			<input type="hidden" id="sp_order_no"          name="sp_order_no"          value="${orderNumber}" />                
			<input type="hidden" id="sp_currency"          name="sp_currency"          value="00" />              				
			<input type="hidden" id="sp_return_url"        name="sp_return_url"        value="${smilePayURL}" /> 
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
			
			<!--신용카드-->
			<input type="hidden" id="sp_usedcard_code"     name="sp_usedcard_code"     value="" />
			<input type="hidden" id="sp_quota"             name="sp_quota"             value="" />
			<input type="hidden" id="sp_os_cert_flag"      name="sp_os_cert_flag"      value="2" />
			<input type="hidden" id="sp_noinst_flag"       name="sp_noinst_flag"       value="" />
			<input type="hidden" id="sp_noinst_term"       name="sp_noinst_term"       value="" />
			<input type="hidden" id="sp_set_point_card_yn" name="sp_set_point_card_yn" value="" />
			<input type="hidden" id="sp_point_card"        name="sp_point_card"        value="" />
			<input type="hidden" id="sp_join_cd"           name="sp_join_cd"           value="" />
			
			<!--가상계좌-->
			<input type="hidden" id="sp_vacct_bank"       name="sp_vacct_bank"         value="" />
			<input type="hidden" id="sp_vacct_end_date"   name="sp_vacct_end_date"     value="${sp_product_expr}" />
			<input type="hidden" id="sp_vacct_end_time"   name="sp_vacct_end_time"     value="${sp_vacct_end_time}" />
			
			<!--선불카드-->
			<input type="hidden" id="sp_prepaid_cp"       name="sp_prepaid_cp"         value="" />
			<input type='hidden' name="sp_product_nm" id="sp_product_nm" value="${goodsName}">
			<input type='hidden' name="sp_product_amt"  id="sp_product_amt" value="${goodsAmt}">
			<input type='hidden' name="sp_window_type"  id="sp_window_type" value="submit">
			<input type='hidden' name="sp_disp_cash_yn"  id="sp_disp_cash_yn" value="N">
			<input type='hidden' name="sp_kmotion_useyn"  id="sp_kmotion_useyn" value="Y">
			<!-- [END] 인증요청 필드  --> 
			
			
			<!-- [START] 인증응답 필드 -->
			<!--공통-->
			<input type="hidden" id="sp_res_cd"              name="sp_res_cd"                value="" /> 
			<input type="hidden" id="sp_res_msg"             name="sp_res_msg"               value="" /> 
			<input type="hidden" id="sp_tr_cd"               name="sp_tr_cd"                 value="" /> 
			<input type="hidden" id="sp_ret_pay_type"        name="sp_ret_pay_type"          value="" /> 
			<input type="hidden" id="sp_trace_no"            name="sp_trace_no"              value="" /> 
			
			<!-- 가맹점 주문번호 인증요청 필드에 존재.                                                                [필수]가맹점 주문번호 --> 
			<input type="hidden" id="sp_sessionkey"          name="sp_sessionkey"            value="" /> 
			<input type="hidden" id="sp_encrypt_data"        name="sp_encrypt_data"          value="" /> 
			
			<!-- 가맹점 ID  인증요청 필드에 존재.                                                                     [필수]가맹점 ID       -->
			<input type="hidden" id="sp_mobilereserved1"     name="sp_mobilereserved1"       value="${stopURL}" /> 
			<input type="hidden" id="sp_mobilereserved2"     name="sp_mobilereserved2"       value="${storeKey}" /> 
			<input type="hidden" id="sp_reserved1"           name="sp_reserved1"             value="" /> 
			<input type="hidden" id="sp_reserved2"           name="sp_reserved2"             value="" /> 
			<input type="hidden" id="sp_reserved3"           name="sp_reserved3"             value="" /> 
			<input type="hidden" id="sp_reserved4"           name="sp_reserved4"             value="" /> 
			
			<!--신용카드-->
			<input type="hidden" id="sp_card_code"            name="sp_card_code"            value="" />
			<input type="hidden" id="sp_eci_code"             name="sp_eci_code"             value="" />
			<input type="hidden" id="sp_card_req_type"        name="sp_card_req_type"        value="" />
			<input type="hidden" id="sp_save_useyn"           name="sp_save_useyn"           value="" />
			<input type="hidden" id="sp_card_prefix"          name="sp_card_prefix"          value="" />
			<input type="hidden" id="sp_card_no_7"            name="sp_card_no_7"            value="" />
			
			<!--간편결제-->
			<input type="hidden" id="sp_spay_cp"              name="sp_spay_cp"              value="" />
			
		</form>
		
		<div class="order_btn">
			<button type="button" class="button large red full circle" onclick="goPay();">${pay_payment}</button>
		</div>
<!-- 		<div class="order_btn"> -->
<!-- 			<button type="button" class="button large red full circle" onclick="payCheck('V');">KICC 화면 확인</button> -->
<!-- 		</div> -->
<%--	스마일 페이 결제 버튼	
		<div class="order_btn">
 			<button type="button" class="button large red full circle" onclick="payCheck('S');">${pay_payment}</button> 
		</div>
--%>		

<%--
		<div class="order_btn">
			<button type="button" class="button large red full circle" onclick="goPopup();">주소 테스트</button>
		</div>
--%>		
		
	</div>
	
</body>
<style type="text/css">

.amtText { font-size: 20px; display: block; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;}

.divHidden { display: none; }

.errorMsg .small{ font-size: 72%; font-weight: 400; }

.textAreaDivTogle { display: none; }
.textAreaDivTogle2 { display: none; }
.textAreaDivTogle3 { display: none; }

.checks {position: relative; font-size:18px; margin-bottom: 8px;}
.checks input[type="radio"] { position: absolute; width: 1px; height: 1px; padding: 0; margin: -1px; overflow: hidden; clip:rect(0,0,0,0); border: 0; }
.checks input[type="radio"] + label { display: inline-block; position: relative; padding-left: 30px; cursor: pointer; -webkit-user-select: none; -moz-user-select: none; -ms-user-select: none; }
.checks input[type="radio"] + label:before { content: ''; position: absolute; left: 0; top: 3px; width: 19px; height: 18px; text-align: center; background: #fafafa; border: 1px solid #cacece; border-radius: 100%; box-shadow: 0px 1px 2px rgba(0,0,0,0.05), inset 0px -15px 10px -12px rgba(0,0,0,0.05); }
.checks input[type="radio"] + label:active:before, .checks input[type="radio"]:checked + label:active:before { box-shadow: 0 1px 2px rgba(0,0,0,0.05), inset 0px 1px 3px rgba(0,0,0,0.1); }
.checks input[type="radio"]:checked + label:before { background: #E9ECEE; border-color: #adb8c0; }
.checks input[type="radio"]:checked + label:after { content: ''; position: absolute; top: 5px; left: 3px; width: 13px; height: 13px; background: #99a1a7; border-radius: 100%; box-shadow: inset 0px 0px 10px rgba(0,0,0,0.3); }

.radioText { font-size: 22px; font-weight: bold; color: red;}

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

</style>


<script>

function orderTypeCheck(){
	orderTypeChange($("input[name=orderType]:checked"));
}

function orderTypeChange($orderInput){
	$("input[name=orderType]").each(function(){
		$(this).parent().find("label").removeClass('radioText');
	})
	
	var $packing = $("input[name=packing]");
	var orderType = $orderInput.val();
	if("D" == orderType){
		$("div[name=infoDiv1]").removeClass('divHidden');
		$("div[name=infoDiv2]").removeClass('divHidden');
		$packing.val(0);
	}else{
		if("P" == orderType){
			$packing.val(1);
		}else{
			$packing.val(0);
		}
		$("div[name=infoDiv1]").removeClass('divHidden');
		$("div[name=infoDiv2]").addClass('divHidden');
	}
	$orderInput.parent().find("label").addClass('radioText');
};

$(document).ready(function() {
	setTimeout(timeOutPage, 3600000);
	orderTypeCheck();
	
	function timeOutPage() {
		alert("장시간 입력이 없으셨습니다. \n첫 화면으로 이동합니다.");
		document.location.href="/menu?store=${storeKey}&table=${table}";
	}
	
	$(document).on("click",".logo",function(){
		document.location.href="/menu?store=${storeKey}&table=${table}";
	} );
	$(document).on("click","input[name=orderType]",function(){
		orderTypeChange($(this));
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
	
	$.validator.setDefaults({
		errorPlacement: function errorPlacement(error, element) {
			$(element).parents('.errorMsg').append(error.addClass('invalid-feedback small d-block')) },
		highlight: function(element) { $(element).addClass('is-invalid'); },
		unhighlight: function(element) { $(element).removeClass('is-invalid'); },
    });	
	
	$.validator.addMethod('phone_format',
		function(value, element) {
			return this.optional(element) || /^\d{3}\d{4}\d{4}$/.test(value);
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
		'전화번호를 입력해주세요.'
	);
	$.validator.addMethod('addr_format',
		function(value, element) {
			if(value == "" || value == null){
				return false;
			}else{
				return true;
			}
		},
		'주소를 입력해 주세요.'
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
		'개인정보 제3자 제공동의가 필요 합니다.'
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
	$.validator.addMethod('consent3_format',
		function(value, element) {
			var consent3 = $("input[name=consent3]").is(":checked");
			if(consent3){
				return true;
			}else{
				return false;
			}
		},
		'개인정보 수집 및 이용동의가 필요합니다.'
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


function payCheck(payGuBun){
	var checkYn = true;
	var orderType = $("input[name=orderType]:checked").val();
	var consent = $("input[name=consent]").is(":checked");
	var consent2 = $("input[name=consent2]").is(":checked");
	var consent3 = $("input[name=consent3]").is(":checked");
	var buyerTel = $("#menuForm").find("input[name=BuyerTel]").val();
	var roadAddr = $("#menuForm").find("input[name=roadAddr]").val();
	var addrDetail = $("#menuForm").find("input[name=addrDetail]").val();
	var deliTel = $("#menuForm").find("input[name=deliTel]").val();
	var storeMsg = $("#menuForm").find("input[name=storeMsg]").val();
	var deliMsg = $("#menuForm").find("input[name=deliMsg]").val();
 	var BuyerName = $("#menuForm").find("input[name=BuyerName]").val();
	var data = {
			orderType: orderType,
			storeId: '${storeId}',
			basket: '${basket}',
			consent: consent,
			consent2: consent2,
			consent3: consent3,
			tel: buyerTel,
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
	}
	if (checkYn) {
		$.ajax({
			type: "POST",
			contentType: "application/json",
			dataType: "json",
			url: "${returnUrl}",
			data: JSON.stringify(data),
			success: function (res) {
				switch (res){
			    	case "Y" :
	 					if(payGuBun === 'S'){

						}else if(payGuBun === 'K'){
							if(orderType != "D"){
								deliTel = buyerTel;
							}
	 						$("#kcForm").find("input[name=sp_user_phone1]").val(deliTel);
	 						$("#kcForm").find("input[name=sp_user_addr]").val(roadAddr + " " + addrDetail);
							
							goInterfaceK();
						}else if(payGuBun === 'V'){
							$("#kcForm1").find("input[name=telNum]").val(tel);
							goInterfaceKW();
						}
			        	break;
				    case "R2" :
				    	alert("전화번호를 입력해주세요.");
				        break;
				    case "R" :
				    	alert("개인정보 제3자 제공동의가 필요 합니다.");
				        break;
				    case "D2" :
				    	alert("개인정보 제3자 제공동의가 필요 합니다.");
				        break;
				    case "D3" :
				    	alert("개인정보 수집 및 이용동의가 필요합니다. ");
				        break;
				    case "A" :
				    	alert("주소를 입력해 주세요.");
				        break;
				    case "R3" :
				    	alert("전화번호를 입력해주세요.");
				        break;
				    case "P" :
				    	alert("이미 결제 하신 내역이 있습니다. \n주문 내역을 확인하여 주시기 바랍니다.");
				    	document.location.href="/menu?store=${storeKey}&table=${table}";
				        break;
				    case "NN" :
				    	alert("정보가 변경되었습니다. \n다시한번 주문해주시기 바랍니다.");
				    	document.location.href="/menu?store=${storeKey}&table=${table}";
				        break;
				    default :
				    	alert("주문정보를 찾을수 없습니다. \n다시한번 주문해주시기 바랍니다.");
	 					document.location.href="/menu?store=${storeKey}&table=${table}";
				}
			}
		});
	}
}



function goInterfaceKW() 
{	
	var form = document.kcForm1;
	
	form.action = '/easypay';
	form.submit();
	return false;	
}

</script>
<script type="text/javascript">
/* 인증창 호출, 인증 요청 */
function goInterfaceK() 
{
    var frm_pay = document.kcForm;
    
    /*  주문정보 확인 */
    if( !frm_pay.sp_order_no.value ) 
    {
        alert("가맹점주문번호를 입력하세요!!");
        frm_pay.sp_order_no.focus();
        return;
    }

    if( !frm_pay.sp_product_amt.value ) 
    {
        alert("상품금액을 입력하세요!!");
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

function goPopup(){
    var pop = window.open("/popup/juso","pop","scrollbars=yes, resizable=yes");
}

function jusoCallBack(roadFullAddr,roadAddrPart1,addrDetail,roadAddrPart2,engAddr, jibunAddr, zipNo, admCd, rnMgtSn, bdMgtSn,detBdNmList,bdNm,bdKdcd,siNm,sggNm,emdNm,
		liNm,rn,udrtYn,buldMnnm,buldSlno,mtYn,lnbrMnnm,lnbrSlno,emdNo, entX, entY){
	$("input[name=roadAddr]").val(roadAddrPart1+" "+roadAddrPart2);
	$("input[name=addrDetail]").val(addrDetail);
}

</script>


<script>

	var PayMethod = $("#menuForm").find("input[name=PayMethod]").val();
		var GoodsCnt = $("#menuForm").find("input[name=GoodsCnt]").val();
		var GoodsCnt = $("#menuForm").find("input[name=PayMethod]").val();
				var GoodsName = $("#menuForm").find("input[name=GoodsName]").val();
				var Amt = $("#menuForm").find("input[name=Amt]").val();
				var Moid = $("#menuForm").find("input[name=Moid]").val();
				var ReturnUrl = $("#menuForm").find("input[name=ReturnUrl]").val();
				var RetryUrl = $("#menuForm").find("input[name=RetryUrl]").val();
				var StopUrl = $("#menuForm").find("input[name=StopUrl]").val();
				var BuyerAddr = $("#menuForm").find("input[name=BuyerAddr]").val();
				var EncryptData = $("#menuForm").find("input[name=EncryptData]").val();
				var MallResultFWD = $("#menuForm").find("input[name=MallResultFWD]").val();
				var TransType = $("#menuForm").find("input[name=TransType]").val();
				var SocketYN = $("#menuForm").find("input[name=SocketYN]").val();
				var Language = $("#menuForm").find("input[name=Language]").val();
				var EncodingType = $("#menuForm").find("input[name=EncodingType]").val();
				var OfferPeriod = $("#menuForm").find("input[name=OfferPeriod]").val();
				var EdiDate = $("#menuForm").find("input[name=EdiDate]").val();
				var VatAmt = $("#menuForm").find("input[name=VatAmt]").val();
				var TaxAmt = $("#menuForm").find("input[name=TaxAmt]").val();
				var TaxFreeAmt = $("#menuForm").find("input[name=TaxFreeAmt]").val();
				var SvcAmt = $("#menuForm").find("input[name=SvcAmt]").val();
				var CardQuota = $("#menuForm").find("input[name=CardQuota]").val();
				var CardInterest = $("#menuForm").find("input[name=CardInterest]").val();
				var IspWapUrl = $("#menuForm").find("input[name=IspWapUrl]").val();
				var DivideInfo = $("#menuForm").find("input[name=DivideInfo]").val();
				var UserIp = $("#menuForm").find("input[name=UserIp]").val();
				var MallIp = $("#menuForm").find("input[name=MallIp]").val();
				var FnCd = $("#menuForm").find("input[name=FnCd]").val();
				var menuInTime = $("#menuForm").find("input[name=menuInTime]").val();
				var GoodsCl = $("#menuForm").find("input[name=GoodsCl]").val();
				var VbankExpDate = $("#menuForm").find("input[name=VbankExpDate]").val();
				var BuyerName = $("#menuForm").find("input[name=BuyerName]").val();
				var BuyerEmail = $("#menuForm").find("input[name=BuyerEmail]").val();
				var BuyerTel = $("#menuForm").find("input[name=BuyerTel]").val();

function goPay() {

	  $.ajax({
    url: "smartropay/returnMobilePay",
    data: "PayMethod=" + PayMethod,
    data: "GoodsCnt=" + GoodsCnt,
    data: "GoodsName=" + GoodsName,
    data: "Amt=" + Amt,
    data: "Moid=" + Moid,
    data: "ReturnUrl=" + ReturnUrl,
    data: "RetryUrl=" + RetryUrl,
    data: "StopUrl=" + StopUrl,
    data: "BuyerAddr=" + BuyerAddr,
    data: "EncryptData=" + EncryptData,
    data: "MallResultFWD=" + MallResultFWD,
    data: "TransType=" + TransType,
    data: "SocketYN=" + SocketYN,
    data: "Language=" + Language,
    data: "EncodingType=" + EncodingType,
    data: "OfferPeriod=" + OfferPeriod,
    data: "EdiDate=" + EdiDate,
    data: "VatAmt=" + VatAmt,
    data: "TaxAmt=" + TaxAmt,
    data: "TaxFreeAmt=" + TaxFreeAmt,
    data: "SvcAmt=" + SvcAmt,
    data: "CardQuota=" + CardQuota,
    data: "CardInterest=" + CardInterest,
    data: "IspWapUrl=" + IspWapUrl,
    data: "DivideInfo=" + DivideInfo,
    data: "UserIp=" + UserIp,
    data: "MallIp=" + MallIp,
    data: "FnCd=" + FnCd,
    data: "menuInTime=" + menuInTime,
    data: "GoodsCl=" + GoodsCl,
    data: "VbankExpDate=" + VbankExpDate,
    data: "BuyerName=" + BuyerName,
    data: "BuyerEmail=" + BuyerEmail,
    data: "BuyerTel=" + BuyerTel,
    type: "POST",
    success : function(data){
      alert("성공")
    },
    error : function(){
      alert("에러")		
    }
  });

    // 스마트로페이 초기화
    smartropay.init({
        mode: "STG"		// STG: 테스트, REAL: 운영
    });

    // 스마트로페이 결제요청
    // PC 연동시 아래와 같이 smartropay.payment 함수를 구현합니다.
	
	smartropay.payment({
        FormId : 'menuForm',				// 폼ID
        Callback : function(res) {
            var approvalForm = document.approvalForm;
            approvalForm.Tid.value = res.Tid;
            approvalForm.TrAuthKey.value = res.TrAuthKey;
            approvalForm.action = '${returnUrl}';
            approvalForm.submit();
        }
    });
	
    // Mobile 연동시 아래와 같이 smartropay.payment 함수를 구현합니다.
    smartropay.payment({
		FormId : 'menuForm'				// 폼ID
    });
};

</script>



<script src="/resources/vendor/lib/validate/validate.ko.js"></script>

</html>