

function goPopup(){
    window.open("/popup/juso","pop","scrollbars=yes, resizable=yes");
}

function jusoCallBack(roadFullAddr,roadAddrPart1,addrDetail,roadAddrPart2,engAddr, jibunAddr, zipNo, admCd, rnMgtSn, bdMgtSn,detBdNmList,bdNm,bdKdcd,siNm,sggNm,emdNm,
		liNm,rn,udrtYn,buldMnnm,buldSlno,mtYn,lnbrMnnm,lnbrSlno,emdNo, entX, entY){
	$("input[name=roadAddr]").val(roadAddrPart1+" "+roadAddrPart2);
	$("input[name=addrDetail]").val(addrDetail);
}

function orderTypeCheck(orderTypeChoose){
	orderTypeChange(orderTypeChoose);
}

function textAreaToggle(num){
	$("#consentdiv"+num).toggleClass( 'textAreaDivTogle'+num );
}

function orderTypeChange(orderType){
	var goodsAmtCom = $("input[name=goodsAmtCom]").val();
	var goodsAmt = $("input[name=goodsAmt]").val()*1;
	var billingAmtCom = $("input[name=billingAmtCom]").val();
	var billingAmt = $("input[name=billingAmt]").val();

	$("button[name=orderType]").each(function(){
		$(this).removeClass('btn-warning').removeClass('btn-outline-dark');
		if($(this).val() == orderType){
			$(this).addClass('btn-warning');
		}else{
			$(this).addClass('btn-outline-dark');	
		}
	});
	
	var $packing = $("input[name=packing]");
	var minOrderPriceCom = $("input[name=minOrderPriceCom]").val();
	var minOrderPrice = $("input[name=minOrderPrice]").val()*1;
	
	$("div[name=timeView]").hide();
	if("D" == orderType){
		if(goodsAmt<minOrderPrice){
			showAlertModal("warning", "배달 주문은 <br>"+minOrderPriceCom+"원 이상 주문해주세요.");
		}
		$("div[name=telDiv]").addClass('divHidden');
		$("div[name=infoDiv2]").removeClass('divHidden');
		$("div[name=riderDiv] > div").removeClass('divHidden');
		$packing.val(0);

		$("#deliveryPayDiv").removeClass('divHidden');
		$("#minOrderDiv").removeClass('divHidden');
		$("#paymentCom").val(billingAmtCom);
		$("input[name=Amt]").val(billingAmt);
		
		orderBtnOut(orderType);
	}else{
		if("P" == orderType){
			$packing.val(1);
			$("div[name=timeView]").show();
		}else{
			$packing.val(0);
		}
		$("div[name=telDiv]").removeClass('divHidden');
		$("div[name=infoDiv2]").addClass('divHidden');
		$("div[name=riderDiv] > div").addClass('divHidden');
		
		$("#deliveryPayDiv").addClass('divHidden');
		$("#minOrderDiv").addClass('divHidden');
		$("#paymentCom").val(goodsAmtCom);
		$("input[name=Amt]").val(goodsAmt);
		
		orderBtnOut(orderType);
	}
	$("input[name=orderTypeClick]").val(orderType);
};

function chageCoupon(){
	var orderType = $("input[name=orderTypeClick]").val();
	var goodsAmtPrice = $("input[name=goodsAmtPrice]").val();
	var deliveryPayAmt = $("input[name=deliveryPay]").val();
	var couponSelectVal = $("select[name=couponSelect] option:selected").val();
	
	var payPrice=0;
	if(orderType=="D")
		payPrice = goodsAmtPrice*1 + deliveryPayAmt*1 - couponSelectVal*1;
	else
		payPrice = goodsAmtPrice - couponSelectVal;
	
	if(payPrice < 0){
		alertMsg("2", "");
		return false;
	}
	
	$("#discountCom").val("-"+numberCom(couponSelectVal));
	$("#paymentCom").val(numberCom(payPrice));
	$("input[name=discount]").val(couponSelectVal);
	$("input[name=deliveryPay]").val(deliveryPayAmt);
	$("input[name=Amt]").val(payPrice);
	$("input[name=coupon]").val($("select[name=couponSelect] option:selected").attr("id"));
};

function numberCom(str){
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

function usePointfn(obj) {
	var regexp = /^[0-9]*$/;
	var number = "0";
	var pointTotal = $("input[name=pointTotal]").val();
	var check = obj.value.replace(/,/g, "");
	if( !regexp.test(check) || check == "" ) {
		check = "0";
	}
	
	if(parseInt(check) > parseInt(pointTotal)){
		/*number = numberCom(pointTotal);*/
		number = pointTotal;
	}else{
		/*number = numberCom(parseInt(check));*/
		number = parseInt(check);
	}
	
    obj.value = number;
}

function pointChg(obj) {
    obj.value = "";
}

function pointCalc(){
	var orderType = $("input[name=orderTypeClick]").val();
	var pointTotal = $("input[name=pointTotal]").val();
	var goodsAmtPrice = $("input[name=goodsAmt]").val();
	var usePoint = $("input[name=usePoint]").val().replace(/,/g, "");
	var deliveryPayAmt = $("input[name=deliveryPay]").val();
	
	if(!usePoint){
		usePoint = 0;
	}
	
	var payPrice =0;
	
	if(orderType=="D")
		payPrice = parseInt(goodsAmtPrice) +  parseInt(deliveryPayAmt) - parseInt(usePoint, 0);
	else
		payPrice = parseInt(goodsAmtPrice) - parseInt(usePoint, 0);
	

	if(payPrice < 0){
		usePoint = goodsAmtPrice;
		$("input[name=usePoint]").val(numberCom(usePoint));
		payPrice = 0;
	}
	if(usePoint!=0)
		$("#discountCom").val("-"+numberCom(usePoint));
	else
		$("#discountCom").val(numberCom(usePoint));
	
	$("#paymentCom").val(numberCom(payPrice));
	$("input[name=discount]").val(usePoint);
	$("input[name=Amt]").val(payPrice);
}
