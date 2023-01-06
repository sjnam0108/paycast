package kr.co.paycast.controllers.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreDeliveryPay;
import kr.co.paycast.models.pay.StoreEtc;
import kr.co.paycast.models.pay.StoreDeliveryPolicy;
import kr.co.paycast.models.pay.StoreOpt;
import kr.co.paycast.models.pay.StorePolicy;
import kr.co.paycast.models.pay.UploadFile;
import kr.co.paycast.models.pay.service.CouponPointService;
import kr.co.paycast.models.pay.service.DeliveryPayService;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.StoreDelivery;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderBasket;
import kr.co.paycast.models.store.StoreOrderBasketList;
import kr.co.paycast.models.store.StoreOrderCook;
import kr.co.paycast.models.store.StoreOrderCoupon;
import kr.co.paycast.models.store.StoreOrderList;
import kr.co.paycast.models.store.StoreOrderPay;
import kr.co.paycast.models.store.StoreOrderPoint;
import kr.co.paycast.models.store.service.StoreBasketService;
import kr.co.paycast.models.store.service.StoreCookService;
import kr.co.paycast.models.store.service.StoreOrderService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.CouponDispItem;
import kr.co.paycast.viewmodels.pay.PointDispItem;
import kr.co.paycast.viewmodels.pay.StoreTime;
import kr.co.paycast.viewmodels.self.MenuPayItem;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 모바일 주문 Controller
 * 2019.12.05 스마일 페이 사용 안함으로 인한여 분리 작업 시작
 */
@Controller("mobileOrder-controller")
@RequestMapping(value="/mobileOrder")
public class MobileOrderController {
	private static final Logger logger = LoggerFactory.getLogger(MobileOrderController.class);
	
	@Autowired
	private MessageManager msgMgr;
	
    @Autowired 
    private StoreService storeService;
	
    @Autowired
    private StoreOrderService storeOrderService;
    
	@Autowired 
    private PayService payService;
    
	@Autowired 
    private StoreBasketService basketService;
	
    @Autowired
    private CouponPointService couponService;
	
    @Autowired
    private StoreCookService storeCookService;
    
    @Autowired
    private DeliveryPayService deliveryPayService; 
    
	/**
	 * 모바일 결제 마지막 단계 
	 * 매장 / 포장 / 배달 선택 가능 화면
	 */
	@RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public String mobileLast(Model model, HttpServletRequest request, Locale locale,
    		HttpServletResponse response, HttpSession session) {
			
			// kicc.mainAction / kicc.mainScript easypay 에서 사용
	   		msgMgr.addViewMessages(model, locale,
    			new Message[] {
    				new Message("html_lang", Util.htmlLang(locale)),	
					new Message("url_login", "common.server.msg.loginUrl"),
					new Message("url_login2", "common.server.msg.loginForcedLogoutUrl"),
					
					new Message("pay_won", "smilepay.won"),
					new Message("pay_total", "smilepay.total"),
					new Message("pay_payment", "smilepay.payment"),
					new Message("pay_email", "smilepay.email"),
					new Message("pay_phone", "smilepay.phone"),
					new Message("pay_name", "smilepay.name"),
					new Message("pay_totalPrice", "smilepay.totalPrice"),
					new Message("pay_pdCnt", "smilepay.pdCnt"),
					new Message("pay_pdNm", "smilepay.pdNm"),
					new Message("pay_orderChooseTitle", "smilepay.orderChooseTitle"),
					new Message("pay_orderTypeS", "smilepay.orderTypeS"),
					new Message("pay_orderTypeP", "smilepay.orderTypeP"),
					new Message("pay_orderTypeD", "smilepay.orderTypeD"),
					new Message("pay_reservationTime", "smilepay.reservationTime"),
					new Message("pay_deliveryInfo", "smilepay.deliveryInfo"),
					
					new Message("pay_addr1", "smilepay.addr1"),
					new Message("pay_addr2", "smilepay.addr2"),
					new Message("pay_storeMsg", "smilepay.storeMsg"),
					new Message("pay_storeMsgEx", "smilepay.storeMsgEx"),
					new Message("pay_deliMsg", "smilepay.deliMsg"),
					new Message("pay_deliMsgEx", "smilepay.deliMsgEx"),
					new Message("pay_agreeAll", "smilepay.agreeAll"),
					new Message("pay_14year", "smilepay.14year"),
					new Message("pay_consent1", "smilepay.consent1"),
					new Message("pay_agreeTitle", "smilepay.agreeTitle"),
					new Message("pay_timeAM", "smilepay.timeAM"),
					new Message("pay_timePM", "smilepay.timePM"),
					new Message("pay_timeHr", "smilepay.timeHr"),
					new Message("pay_timeHrs", "smilepay.timeHrs"),
					new Message("pay_timeMins", "smilepay.timeMins"),
					new Message("pay_msg_readyYouWant", "smilepay.msg.readyYouWant"),
					new Message("pay_msg_chooseYouWant", "smilepay.msg.chooseYouWant"),
					new Message("pay_msg_phoneCheck", "smilepay.msg.phoneCheck"),
					new Message("pay_msg_phoneMsg", "smilepay.msg.phoneMsg"),
    				new Message("pay_msg_phone", "smilepay.msg.phone"),
    				new Message("pay_msg_phoneKeyin", "smilepay.msg.phoneKeyin"),
    				new Message("pay_msg_addrKeyin", "smilepay.msg.addrKeyin"),
    				new Message("pay_msg_agree1", "smilepay.msg.agree1"),
    				new Message("pay_msg_agree2", "smilepay.msg.agree2"),
    				new Message("pay_msg_alertMsg1", "smilepay.msg.alertMsg1"),
    				new Message("pay_msg_alertMsg2", "smilepay.msg.alertMsg2"),
    				new Message("pay_msg_alertMsg3", "smilepay.msg.alertMsg3"),
    				new Message("pay_msg_timeOutMsg", "smilepay.msg.timeOutMsg"),

    				new Message("mainAction", "kicc.mainAction"),
    				new Message("mainScript", "kicc.mainScript")
    			});
	   		String storeKey = (String)request.getParameter("storeKey");
	   		String basket = (String)request.getParameter("basket");
	   		String table = (String)request.getParameter("table");
	   		String time = (String)request.getParameter("time");

    		// id대신 사용 되는 외부용 ID(상정 조회 사용되는 KEY)			
			model.addAttribute("storeKey", storeKey);
			// 장바구니 키
			model.addAttribute("basket", basket);
			model.addAttribute("table",table);
			model.addAttribute("menuInTime", time);
	   		
	   		try {
	   			Store res = storeService.getStoreByStoreKey(storeKey);
	   			if(res == null){
	   				logger.error("mobileLast > payError > storeKey : [{}], table :[{}]", storeKey, table);
	   				logger.error("mobileLast > payError > basket : [{}], time : [{}]", basket, time);
	   				logger.error("Menu Store NOT FOUND [{}]", storeKey);
	   				payError("STORE", "NOTFOUND", "STEP3", model, locale);
	   				return "order/error"; 
	   			}
	   			// 모바일 상점 로그 가져오기
	   			imgCommon(res, model, locale);

				if(!res.isMobileOrderEnabled()){
					return "order/storeOff";
	    		}
	   			// 모바일 결제 시 주문 선택에 대한 서비스 유형 정의 값
				// 모바일에서 사용되는 TYPE(type1 / type2 / type3)
				if(res.getStoreOpt() != null){
					model.addAttribute("mOrderType", res.getStoreOpt().getOrderType());
				}else{
					model.addAttribute("mOrderType", "type2");
				}
				// 쿠폰 및 포인트 에 대한 정의값 
				// NO= 사용안함, CP= 쿠폰, PO= 포인트 
				if(res.getStoreEtc() != null){
					model.addAttribute("savingType", res.getStoreEtc().getSavingType());
				}else{
					model.addAttribute("savingType", "NO");
				}
				
				StoreOrderBasket basketOne = basketService.getBasketKey(basket);
				if(basketOne == null){
					logger.error("mobileLast > basket > storeKey [{}], table [{}]", storeKey, table);
					logger.error("mobileLast > basket > basket : [{}], time : [{}]", basket, time);
					logger.error("Menu Basket NOT FOUND [{}]", basket);
					payError("BASKET", "NOTFOUND", "STEP3", model, locale);
					return "order/error";
				}
				
				int goodsAmt = 0; //총 금액
				int totalindex = 0;
				String goodsName = "";
				List<MenuPayItem> payitemList = new ArrayList<MenuPayItem>();
				List<StoreOrderBasketList> list = basketService.getBasketList(basketOne.getId());
				if(list.size() > 0){
					int i=0;
					for(StoreOrderBasketList basketList : list){
    					MenuPayItem payitem = new MenuPayItem();
    	    			
    	    			payitem.setId(String.valueOf(basketList.getId()));
    	    			payitem.setMenuId(basketList.getMenuId());
    	    			payitem.setCompSelect(basketList.getCompSelect());
    	    			payitem.setName(basketList.getMenuName());
    	    			payitem.setOrderCount(String.valueOf(basketList.getMenuAmount()));
    	    			payitem.setPrice(String.valueOf(basketList.getMenuAmt()));
    	    			payitem.setToPrice(String.valueOf(basketList.getMenuTotalAmt()));
    	    			payitem.setPacking(basketList.getPacking());
    	    			payitem.setEssVal(basketList.getEssVal());
    	    			payitem.setEssName(basketList.getEssName());
    	    			payitem.setAddVal(basketList.getAddVal());
    	    			payitem.setAddName(basketList.getAddName());
    	    			payitem.setSubMenu(Util.parseString(basketList.getSubmenu()));
    	    			payitem.setImgSrc(basketList.getSrc());
    	    			
    	    			goodsAmt += (basketList.getMenuAmount() * basketList.getMenuTotalAmt());
    	    			totalindex += basketList.getMenuAmount();
    	    			
    	    			if(i == 0){
    	    				goodsName = basketList.getMenuName();
						}
    	    			payitemList.add(payitem);
					}
				}
				//1,000형식으로 변경
				SimpleDateFormat yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				String nowTime = yyyyMMddHHmmssSSS.format(new Date());
				DecimalFormat format = new DecimalFormat("###,###");
				
		    	StoreDeliveryPolicy deliveryPolicy = deliveryPayService.getDeliveryPolicy(res.getId());
		    	int minOrderPrice=0;
		    	int deliveryPrice=0;
		    	if(deliveryPolicy!=null){
		    		minOrderPrice=deliveryPolicy.getMinOderPeice();
		    		deliveryPrice=deliveryPolicy.getDeliveryPrice();
		    	}
		    	//최소 주문금액
		    	model.addAttribute("minOrderPrice", minOrderPrice);
		    	model.addAttribute("minOrderPriceCom", format.format(minOrderPrice));
		    	//배달료
		    	model.addAttribute("baseDeliveryPrice", deliveryPrice);
		    	model.addAttribute("baseDeliveryPriceCom", format.format(minOrderPrice));	    	
				
				
				//최소 주문 금액 및 배달료 측정
		    	List<StoreDeliveryPay> deliveryPayList = deliveryPayService.getDeliveryPayList(res.getId());
		    	int deliveryPayAmt = deliveryPrice;
		    	if(deliveryPayList.size()>0){
			    	for(StoreDeliveryPay deliveryPay : deliveryPayList){
			    		if(deliveryPay.getFromOrderPrice()<=goodsAmt&&goodsAmt<=deliveryPay.getToOrderPrice()){
			    			deliveryPayAmt = deliveryPay.getDeliveryPrice();
			    		}
			    	}
	    			model.addAttribute("deliveryPayAmt", deliveryPayAmt);
					// 화면에 보여주는 총 배달료
					String deliveryPayCom = format.format(deliveryPayAmt);
					model.addAttribute("deliveryPayCom", deliveryPayCom);
		    	}else{
	    			model.addAttribute("deliveryPayAmt", deliveryPayAmt);
					// 화면에 보여주는 총 배달료
					String deliveryPayCom = format.format(deliveryPayAmt);
					model.addAttribute("deliveryPayCom", deliveryPayCom);
		    	}
		    	
		    	
				// device 정보를 확인하여 개발이 필요하다. 
				// M 일 경우 : 모바일로 표시_2019.10.02
				// 2019.10.02 이후  MS : 스마일페이 모바일  / ME : easypay 로 나누어 저장
				String device = res.getStoreEtc().getStorePayGubun();
				if(Util.isNotValid(device)){
					//2020.03.25 가맹점ID가 없어도 결제 화면은 들어오고 결제 할수 있는 버튼만 없애야 한다고 하여 지나가는 "NoDevice"로 강제 삽입
					device = "NoDevice";
				}
				
				//MS일 경우 spStoreKey 사용
				//ME일 경우 epStoreKey 사용
				String storeMid = res.getStoreEtc().getEpStoreKey();
				// 해당 키가 없을 경우 결제를 진행하지 않고 에러로 이동
				if(Util.isNotValid(storeMid)){
					//2020.03.25 가맹점ID가 없어도 결제 화면은 들어오고 결제 할수 있는 버튼만 없애야 한다고 하여 지나가는 "NoDevice"로 강제 삽입
					storeMid = "NoStoreMid";
				}
				
				// 주문키 생성  > 모바일 : ME(MS) + 장바구니 코드 + paycast상점 KEY + 스마일페이상점ID + 날짜시간
				// 이후 결제 승인이 정상적으로 났을 경우 해당 주문 번호에 주문순서번호를 붙여준다.
				// 2019.07.07 주문키 생성  : ME(MS) + 보안키 + 상점 KEY(???) + 날짜시간
				String orderNumber = device + res.getStoreKey() + storeMid + nowTime;
				
				// returnURL, retryURL, 결제중지 URL 에 대한 도메인을 가져와 URL을 생성해 준다.
				String reqUrl = request.getRequestURL().toString().replace(request.getRequestURI(),"");
				
				// 결제결과를 수신할 가맹점 returnURL 설정
				model.addAttribute("reqUrl", reqUrl);
				
				// 총 수량
				model.addAttribute("totalindex", totalindex);
				// 총 주문 금액
				model.addAttribute("goodsAmt", goodsAmt);
				// 화면에 보여주는 총 주문 금액
				String goodsAmtCom = format.format(goodsAmt);
				model.addAttribute("goodsAmtCom", goodsAmtCom);
				//총 결제 금액
				model.addAttribute("billingAmt", goodsAmt+deliveryPayAmt);
				// 화면에 보여주는 결제 금액
				String billingAmtCom = format.format(goodsAmt+deliveryPayAmt);
				model.addAttribute("billingAmtCom", billingAmtCom);
				// 거래 상품명
				model.addAttribute("goodsName", goodsName);
				// 주문 번호
				model.addAttribute("orderNumber", orderNumber);
				// 상점ID
				model.addAttribute("storeMid", storeMid);
				// 매장 화면 구성 데이터
				model.addAttribute("storeName", res.getStoreName());
				model.addAttribute("storeIntroduction", "");
				model.addAttribute("storeId", res.getId());
				// 알림톡 여부
				model.addAttribute("alimTalkAllowed", res.isAlimTalkAllowed());
				// 매장 전화 번호
				model.addAttribute("phone", res.getPhone());
				
				StoreOrder orderDao = new StoreOrder(res.getId(), orderNumber, goodsName, String.valueOf(totalindex), goodsAmt, device);
				orderDao.setOrderTable(table);
				storeOrderService.saveOrder(orderDao, payitemList);
				
				// 장바구니 정보에 만들어진 주문 정보 저장 
				basketOne.setOrderNumber(orderNumber);
				basketService.saveOrUpdate(basketOne);
				model.addAttribute("refiTel", basketOne.getRefiTel());
				
				// 서비스 기간 
		    	Date date = new Date ();
		    	int day = Util.parseInt(msgMgr.message("kicc.productExpr", locale), 1);
		    	Date addDay = Util.addDays(date, day);
		    	String end_date = Util.toSimpleString(addDay, "yyyyMMdd");
		    	model.addAttribute("sp_product_expr", end_date);		//서비스기간 : YYYYMMDD
		    	model.addAttribute("sp_vacct_end_time", Util.parseString(msgMgr.message("kicc.endTime", locale),"235959"));		//입금 만료 시간
				
		    	// 주문 가능 시간 설정이 P로 설정이 되어 있을 경우 
		    	// 특정 시간 까지 예약이 불가능 하며 이후 시간에는 예약이 가능
		    	// endDate 는 24시간 영업일 경우에는 사용하지 않는다. 
		    	Date endDate = null;
		    	if(!res.isOpenHour_24()){
		   			endDate = new Date(res.getEndTime().getTime());
		   			GregorianCalendar calendar = new GregorianCalendar();
		   			calendar.setTime(endDate);
		   			int endampm  = calendar.get(Calendar.AM_PM);
		   			int endhour  = calendar.get(Calendar.HOUR);
		   			int endmin   = calendar.get(Calendar.MINUTE);
		   			calendar.setTime(new Date());
		   			StoreOpt storeOpt = res.getStoreOpt();
		   			// 다음날 인지 확인하여 더해준다.
		   			if(storeOpt.isNextDayClose()){
		   				calendar.add(Calendar.DATE, 1);	
		   			}
		   			calendar.set(Calendar.AM_PM, endampm);
		   			calendar.set(Calendar.HOUR, endhour);
		   			calendar.set(Calendar.MINUTE, endmin);
		   			calendar.set(Calendar.SECOND, 00);
		   			calendar.set(Calendar.MILLISECOND, 0);
		   			endDate = calendar.getTime();
		   		}
		    	Date possiblDate = null;
		    	if(res.getStoreEtc() != null){
			   		StoreEtc storeEtc = res.getStoreEtc();
			   		if("P".equals(storeEtc.getOderPossiblCheck())){
			   			Date possibleDate = new Date(storeEtc.getOder_possible_Time().getTime());
			   			GregorianCalendar calendarPu = new GregorianCalendar();
			   			calendarPu.setTime(possibleDate);
			   			calendarPu.add(Calendar.MINUTE, storeEtc.getOder_setting_Time());
			   			possiblDate = calendarPu.getTime();
			   		}
		    	}
		    	
				// String 형식으로 저장되어 있는 예약시간을 List 형식으로 변경하여 VIEW로 전송
		    	List<StoreTime> timeList = new ArrayList<StoreTime>();
		    	if(res.getStoreOpt() != null && !"".equals(res.getStoreOpt().getRsvpTime())){
		    		timeList = timeList(res.getStoreOpt().getRsvpTime(), possiblDate, endDate);
		    	}
		    	model.addAttribute("timeList", timeList);
		    	// 포장시 픽업 시간은 있지만 선택할수 있는 시간이 없을 경우 
		    	// 화면 문구를 보여주지 않기 위해서 사용
		    	Boolean charYN = false;
		    	if(timeList.size() > 0){
		    		for(StoreTime one : timeList){
		    			if(one.isClickTF()){
		    				charYN = true;
		    			}
		    		}
		    	}
		    	
		    	model.addAttribute("charYN", charYN);
		    	
		    	if(possiblDate != null){
		    		model.addAttribute("possiblDate", possiblDate.getTime());		    		
		    	}else{
		    		model.addAttribute("possiblDate", 0);
		
		    	}
		    	model.addAttribute("possiblText", calendarTime(possiblDate));
		    	
		    	if(endDate != null){
		    		model.addAttribute("endDate", endDate.getTime());
		    	}else{
		    		model.addAttribute("endDate", 0);
		    	}
		    	model.addAttribute("endDateText", calendarTime(endDate));
		    	
		    	model.addAttribute("date", Util.toSimpleString(date, "yyyyMMdd"));
		    	

			} catch (Exception e) {
				logger.error("smilepay > Exception > STEP3  storeKey [{}], table [{}]", storeKey, table);
				logger.error("smilepay > Exception > STEP3  basket : [{}], time : [{}]", basket, time);
				logger.error("smilepay > Exception > STEP3  storeKey : [{}], code : [{}]",storeKey, "M999");
				logger.error("Menu STEP3 error[{}]", e);
				
	    		model.addAttribute("code", "M999");
	    		model.addAttribute("gubun", "E");
				
				return "order/stopMsg";
			}
			
		return "order/easypay";
    }
	
	@RequestMapping(value ="/easyPayCheck", method = RequestMethod.POST)
    public String easyPayCheck(Model model, HttpServletRequest request, Locale locale,
    		HttpServletResponse response, HttpSession session) {
		
		msgMgr.addViewMessages(model, locale, 
    			new Message[] {
					new Message("html_lang", Util.htmlLang(locale)),	
					
					new Message("pay_orderNum", "smilepay.orderNum"),
					new Message("pay_stay", "smilepay.stay"),
					new Message("pay_firstPage", "smilepay.firstPage"),
					
					new Message("pay_msg_firstPage", "smilepay.msg.firstPage"),
					new Message("pay_msg_orderTable", "smilepay.msg.orderTable"),
					new Message("pay_msg_notSupportBrowser2", "smilepay.msg.notSupportBrowser2"),
					new Message("pay_msg_paySuccess", "smilepay.msg.paySuccess"),
					
					new Message("confirm_ok", "confirm.ok"),
					new Message("confirm_cancel", "confirm.cancel"),
				
    				new Message("pay_msg_kcMsg1", "smilepay.msg.kcMsg1"),
    				new Message("pay_msg_kcMsg2", "smilepay.msg.kcMsg2")
    			});
		String storeIdSt = Util.parseString((String)request.getParameter("storeId"));
		int storeId = Util.parseInt(storeIdSt);
		String storeKey = (String)request.getParameter("storeKey");
		String storeName = (String)request.getParameter("storeName");
   		String basket = (String)request.getParameter("basket");
   		String storeMid = (String)request.getParameter("storeMid");
   		String reqUrl = (String)request.getParameter("reqUrl");
   		String orderNumber = (String)request.getParameter("orderNumber");
   		String sp_product_expr = (String)request.getParameter("sp_product_expr");
   		String sp_vacct_end_time = (String)request.getParameter("sp_vacct_end_time");
   		String sp_user_phone1 = (String)request.getParameter("sp_user_phone1");
   		String sp_user_addr = (String)request.getParameter("sp_user_addr");
   		String sp_product_nm = (String)request.getParameter("sp_product_nm");
   		String sp_product_amt = (String)request.getParameter("sp_product_amt");
   		String mainAction = (String)request.getParameter("mainAction");
   		String deliveryPay = (String)request.getParameter("deliveryPay");   		

   		// 쿠폰, 포인트 해당내용 체크 필요 
   		String coupon = (String)request.getParameter("coupon");
   		String couponSelect = (String)request.getParameter("couponSelect");
   		String pointTotal = (String)request.getParameter("pointTotal");
   		String usePoint = (String)request.getParameter("usePoint");
   		int discount = Util.parseInt((String)request.getParameter("discount"), 0);
   		String payment = (String)request.getParameter("payment");
   		
   		//최종 결제가 될 금액이 0원 일 경우 easypay를 사용 하지 않는다. 2020-04-27
   		int paymentInt = Util.parseInt(payment, 0);	
   		int deliveryPayInt =Util.parseInt(deliveryPay, 0);	 
   		   		
		logger.info("/easyPayCheck coupon [{}] couponAmt [{}]", coupon, couponSelect);
		logger.info("/easyPayCheck pointTotal [{}] usePoint [{}]", pointTotal, usePoint);
		logger.info("/easyPayCheck discount [{}] payment [{}]", discount, payment);
   		
   		if(paymentInt <= 0){
   	        try {
   	        	sp_product_nm = URLDecoder.decode(sp_product_nm, "UTF-8");
   	        	storeName = URLDecoder.decode(storeName, "UTF-8");
   	        } catch (UnsupportedEncodingException e) {
   	        	logger.info("/easyPayCheck URLEncoder >>> sp_product_nm[{}][{}]", sp_product_nm, e);
   	        }
   			
   			Store res = storeService.getStoreByStoreKey(storeKey);
   			
   			StoreOrderBasket basketOne = basketService.getOrderNum(orderNumber);
   			String savingType = basketOne.getSavingType();
   			
			StoreOrder order = storeOrderService.getOrder(storeId, orderNumber);
			// 결제된 내용을 저장
			String orderSequence = "";
			SimpleDateFormat AuthDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String AuthDate = AuthDateFormat.format(new Date());
			if(order.getOrderPayId() > 0){
				
				orderSequence = order.getOrderSeq();
			}else{
				
				order.setOrderParent(-1);
				order.setDiscountAmt(discount);
				order.setPaymentAmt(paymentInt);
				order.setSavingType(savingType);
				order.setUseCoupon(basketOne.getCouponId());
				storeOrderService.saveOrUpdate(order);
				
				StoreOrderPay orderPay = new StoreOrderPay();
				orderPay.setStoreId(storeId);
				orderPay.setOrderNumber(orderNumber);
				orderPay.setOrderTid(savingType);
				orderPay.setPayMethod(savingType);
				orderPay.setPayMid(storeMid);
				orderPay.setPayAmt(payment);
				orderPay.setDeliveryPay(deliveryPay);
				orderPay.setGoodsname(sp_product_nm);
				orderPay.setPayOid(orderNumber);
				orderPay.setPayAuthDate(AuthDate);
				orderPay.setPayAuthCode(savingType);
				orderPay.setPayResultCode("0000");
				orderPay.setPayResultMsg(savingType);
				orderPay.setPaySignValue(savingType);
				orderPay.setPayFnCd("999999");
				orderPay.setPayFnName("999999");
				orderPay.setPayCardQuota("999999");
				orderPay.setPayAcquCardcode("999999");
				orderPay.setPayDivideInfo("");
				orderPay.touchWhoC(orderNumber);
				
				orderSequence = storeOrderService.saveOrderPay(order, orderPay, locale, session);
				
				StoreOrderCook storeOrderCook = new StoreOrderCook(storeId, order.getId(), order.getOrderNumber());
				//FCM 전송  : 모바일 결제완료가 되어 DB에 저장 되었을 경우 fcm으로 stb 에 알려준다.
				storeOrderService.fcmTransmission(storeId, storeOrderCook);
				
				if("CP".equals(basketOne.getSavingType())){
					StoreOrderCoupon couponOne = couponService.getCoupon(basketOne.getCouponId());
					// 쿠폰 사용을 하지 않을 경우 null 체크
					if(couponOne != null){
						couponOne.setUseState(1);
						couponService.saveOrUpdate(couponOne);
					}
				}else if("PO".equals(basketOne.getSavingType())){
					StoreOrderPoint savePoint = null; 
					List<StoreOrderPoint> resList = couponService.getPointbyTel(storeId, order.getTelNumber());
					logger.info("resList.size() [{}]", resList.size());
					
					if(resList.size() > 0){
						for(StoreOrderPoint one : resList){
							one.setPointCnt(-basketOne.getBasketDiscount());
							one.setPointTotal(one.getPointTotal() - basketOne.getBasketDiscount());
							
							savePoint = one;
						}
					}
					
					couponService.saveOrUpdate(savePoint);
				}
			}
			
			model.addAttribute("stayMenuCnt", storeCookService.getStayCntMobile(storeId));
			
			imgCommon(res, model, locale);
			// 매장 화면 구성 데이터
			model.addAttribute("storeId", storeId);
			model.addAttribute("storeName", storeName);
			model.addAttribute("storeIntroduction","");
			
			model.addAttribute("url","/menu");
			model.addAttribute("oriUrlparam", storeKey);
			model.addAttribute("orderTable", Util.parseString(basketOne.getOrderTable(), "0000") );
			model.addAttribute("orderSequence", orderSequence);
			Calendar calendar = new GregorianCalendar(Locale.KOREA);
			int nYear = calendar.get(Calendar.YEAR);
			String nYearSt = String.valueOf(nYear);
			model.addAttribute("authDate", nYearSt.substring(0, 2) + AuthDate);
			model.addAttribute("desc", "");
			
			// 쿠폰 및 포인트로 결제가 완료되었을 경우 해당 basket 정보를 삭제 한다. 
			if(basket != null){
				basketService.delete(basketOne);
			} 
			
   			return "order/refillOrder";
   		}
   		
   		model.addAttribute("storeKey", storeKey);
   		model.addAttribute("storeMid", storeMid);
   		model.addAttribute("storeName", storeName);
   		model.addAttribute("reqUrl", reqUrl);
   		model.addAttribute("orderNumber", orderNumber);
   		model.addAttribute("sp_product_expr", sp_product_expr);
   		model.addAttribute("sp_vacct_end_time", sp_vacct_end_time);
   		model.addAttribute("sp_user_phone1", sp_user_phone1);
   		model.addAttribute("sp_user_addr", sp_user_addr);
   		model.addAttribute("goodsName", sp_product_nm);
   		model.addAttribute("goodsAmt", payment);
   		model.addAttribute("deliveryPay", deliveryPay);
   		model.addAttribute("mainAction", mainAction);
   		
   		//easyPay 웹 결제를 하기 위한  이동
		return "order/easypay_check";
    }
	

	
    /**
	 * 결제 하기전에 해당 주문건에 대해서 결제된 내역이 있는지 체크
	 * 2019.12.09 smilepayController(기존 로직)에서 변경
	 */
	@RequestMapping(value = "/payCheck", method = {RequestMethod.POST})
	public @ResponseBody String payCheck(@RequestBody Map<String, Object> model, HttpSession session, HttpServletRequest request) {
		logger.info("페이체크 시작*************");
		boolean consent2 = (boolean)model.get("consent2");
		logger.info("페이체크 시작*************" + consent2);
		boolean consent3 = (boolean)model.get("consent3");
		logger.info("페이체크 시작*************" + consent3);
		String orderNumber = Util.parseString((String)model.get("Moid"));
		logger.info("페이체크 시작*************" + orderNumber);
		String orderType = Util.parseString((String)model.get("orderType"));
		logger.info("페이체크 시작*************" + orderType);
		String basket = Util.parseString((String)model.get("basket"));
		logger.info("페이체크 시작*************" + basket);
		String bookingTime = Util.parseString((String)model.get("bookingTime"));
		logger.info("페이체크 시작*************" + bookingTime);
		String tel = Util.parseString((String)model.get("tel"));
		logger.info("페이체크 시작*************"+ tel);
		String storeSPMsg = Util.parseString((String)model.get("storeSPMsg"));
		logger.info("페이체크 시작*************"+ storeSPMsg);
		String roadAddr = Util.parseString((String)model.get("roadAddr"));
		logger.info("페이체크 시작*************"+ roadAddr);
		String addrDetail = Util.parseString((String)model.get("addrDetail"));
		logger.info("페이체크 시작*************"+ addrDetail);
		String deliTel = Util.parseString((String)model.get("deliTel"));
		logger.info("페이체크 시작*************"+ deliTel);
		String deliMsg = Util.parseString((String)model.get("deliMsg"));
		logger.info("페이체크 시작*************"+ deliMsg);
		int couponId = Util.parseInt((String)model.get("coupon"), 0);		
		// 쿠폰 ID
		logger.info("페이체크 시작*************"+ couponId);
		int couponAmt = Util.parseInt((String)model.get("couponSelect"), 0);	
		// 쿠폰 사용 금액
		logger.info("페이체크 시작*************"+ couponAmt);
		int pointTotal = Util.parseInt((String)model.get("pointTotal"), 0);		
		// 사용가능한 총 포인트
		logger.info("페이체크 시작*************"+ pointTotal);
		int usePoint = Util.parseInt((String)model.get("usePoint"), 0);		
		// 사용하려는 포인트
		logger.info("페이체크 시작*************"+ usePoint);
		int goodsAmtPrice = Util.parseInt((String)model.get("goodsAmtPrice"), 0);
		// 결제 하려는 총 금액
		logger.info("페이체크 시작*************"+ goodsAmtPrice);
		int discount = Util.parseInt((String)model.get("discount"), 0);		
		// 할인금액  > usePoint / couponAmt 와 같은 금액이여 한다
		logger.info("페이체크 시작*************"+ discount);
		int payment = Util.parseInt((String)model.get("payment"), 0);	
		// 실 결제 금액 
		logger.info("페이체크 시작*************"+ payment);
		int deliveryPay = Util.parseInt((String)model.get("deliveryPay"),0);		//배달요금
		logger.info("페이체크 시작*************"+ deliveryPay);
		String storeIdSt = Util.parseString((String)model.get("storeId"));
		logger.info("페이체크 시작*************"+ storeIdSt);
		int storeId = Util.parseInt(storeIdSt);
		


		try {
			// 전화번호의 "-" 삭제
			tel = tel.replace("-", "");
			deliTel = deliTel.replace("-", "");
			logger.info(tel);
			logger.info(deliTel);
	          if (Util.isNotValid(tel))
	              tel = deliTel;
			Store store = storeService.getStore(storeId);
			if(store == null){
				logger.error("orderNumber [{}] / storeId [{}] / error [매장정보를 찾을수 없습니다. ]", orderNumber, storeId);
				return "N";
			}
			
			StoreOrder order = storeOrderService.getOrder(storeId, orderNumber);
			logger.info(orderNumber);
			if(order == null){
				logger.error("orderNumber [{}] / storeId [{}] / error [주문정보를 찾을수 없습니다.]", orderNumber, storeId);
				StoreOrderBasket basketOne = basketService.getBasketKey(basket);
				if(basketOne != null){
					basketService.delete(basketOne);
				}
				return "N";
			}
	
			if(!store.isMobileOrderEnabled()){
				return "OFF";
			}
			
			// 매장 / 배달 일 경우 
			if(Util.isNotValid(bookingTime)){
				bookingTime = calendarTime(new Date());
			}
			String result = bookingTimeCheck(store, bookingTime);
			if(Util.isValid(result)){
				return result;
			}
			
			String mOrderType = store.getStoreOpt().getOrderType();
			// [type1 : 매장 / 포장 만 되며 배달은 매장 문의 문구 추가]
			// [type2 : 매장 / 포장 / 배달 모두 가능]
			// [type3 : 배달만 되며 모두 숨김처리]
			// 해당 내용으로  체크!!
			if("type1".equals(mOrderType)){
				// 배달이 허용 되는 매장이 아닙니다. 
				if("D".equals(orderType)){
					logger.error("StoreName [{}] / storeId [{}] / error [배달이 허용되는 매장이 아닙니다. ]", store.getStoreName(), storeId);
					logger.error("mOrderType [{}] / orderType [{}] / error [배달이 허용되는 매장이 아닙니다. ]", mOrderType, orderType);
					return "T1";
				}
			}else if("type3".equals(mOrderType)){
				if("S".equals(orderType) || "P".equals(orderType)){
					logger.error("StoreName [{}] / storeId [{}] / error [매장 및 포장이 허용된 매장이 아닙니다.]", store.getStoreName(), storeId);
					logger.error("mOrderType [{}] / orderType [{}] / error [매장 및 포장이 허용된 매장이 아닙니다.]", mOrderType, orderType);
					return "T3";
				}
			}
			//  S(매장이용), P(포장)일 경우  체크
			if("S".equals(orderType) || "P".equals(orderType)){
				// 전화번호가 비여 있을 경우 
				if(Util.isNotValid(tel)){
					logger.info("Util.isNotValid(tel) [{}]", Util.isNotValid(tel));
					// 전화번호는 없지만 개인정보 제3자 제공동의 / 개인정보 수집 및 이용동의 를 체크 하나라도 체크가 되어 있을 경우
					if(consent2 || consent3){
						logger.error("orderNumber [{}] / error [{}]", orderNumber, "R2");
						return "R2";
					}
				}else{
					// 전화번호가 비어 있지 않을 경우 
					// 개인정보 제3자 제공동의 / 개인정보 수집 및 이용동의 를 체크
					if(!consent2){
						logger.error("orderNumber [{}] / error [{}]", orderNumber, "D2");
						return "D2";
					}
					if(!consent3){
						logger.error("orderNumber [{}] / error [{}]", orderNumber, "D3");
						return "D3";
					}
				}
				
				if("P".equals(orderType)){
					// 예약시간을 체크
					if(Util.isValid(bookingTime)){
						result = bookingTimeCheck(store, bookingTime);
						if(Util.isValid(result)){
							return result;
						}
					}
					
					// 주문 정보 list를 가져와서 포장으로 변경한다. 
					List<StoreOrderList> resList = storeOrderService.getOrderListbyNumber(order.getOrderNumber());
					for(StoreOrderList orderlist : resList){
						orderlist.setOrderMenuPacking(1);
						orderlist.touchWho(session);
						storeOrderService.saveOrUpdate(orderlist);
					}
				}
				
				// 매장 메시지가 있을 경우 해당 내용 저장
				if(!Util.isNotValid(storeSPMsg) || !Util.isNotValid(bookingTime)){
					StoreDelivery deli = new StoreDelivery(storeId, orderNumber, storeSPMsg,
							deliMsg, roadAddr, addrDetail, bookingTime, session);
					storeOrderService.saveOrUpdate(deli);
					
					order.setDeliveryId(deli.getId());
				}
				order.setTelNumber(tel);
				order.setOrderType(orderType);
				order.setPayment("AD"); // 선불 
				order.touchWho(session);
				storeOrderService.saveOrUpdate(order);
				
			}else if("D".equals(orderType)){
				// 주소 / 상세주소가 없을 경우 확인하라는 메시지 출력 - A				
				if(Util.isNotValid(roadAddr) || Util.isNotValid(addrDetail)){
					logger.error("orderNumber [{}] / error [{}]", orderNumber, "A");
					return "A";
				}
				
				// 전화 번호가 없을 경우 확인하라는 메시지 출력 - R3					
				if(Util.isNotValid(deliTel)){
					logger.error("orderNumber [{}] / error [{}]", orderNumber, "R3");
					return "R3";
				}
				
				if(consent2 && consent3){
					
					logger.info("orderNumber [{}] / orderType [{}]", orderNumber, orderType);
					
					// 주문 정보 list를 가져와서 포장으로 변경한다. 
					List<StoreOrderList> resList = storeOrderService.getOrderListbyNumber(order.getOrderNumber());
					for(StoreOrderList orderlist : resList){
						orderlist.setOrderMenuPacking(1);
						orderlist.touchWho(session);
						storeOrderService.saveOrUpdate(orderlist);
					}
					
					StoreDelivery deli = new StoreDelivery(storeId, orderNumber, storeSPMsg,
							deliMsg, roadAddr, addrDetail, bookingTime, session);
					storeOrderService.saveOrUpdate(deli);
					
					// 주소 / 매장 요청 메시지, 배달 요청 메시지  저장 후 
					order.setTelNumber(deliTel);
					order.setOrderType(orderType);
					order.setDeliveryId(deli.getId());
					order.setPayment("AD");  // 선불 
					order.touchWho(session);
					storeOrderService.saveOrUpdate(order);
					
				}else{
					// 동의하지 않았을 경우 전화번호가 있으면 동의하라고 다시 확인
					if(!consent2){
						logger.error("orderNumber [{}] / error [{}]", orderNumber, "D2");
						return "D2";
					}
					if(!consent3){
						logger.error("orderNumber [{}] / error [{}]", orderNumber, "D3");
						return "D3";
					}
				}
			}else{
				// payType이 S, P, D 가 아닐 경우 페이지 확인 메시지 출력
				logger.error("orderNumber [{}] / error [{}]", orderNumber, "NN");
				return "NN";
			}
			
			if(order.getOrderPayId() < 1){
				
				// NO= 사용안함, CP= 쿠폰, PO= 포인트
				// ETC를 확인 하여 쿠폰 및 포인트일 경우 해당 내용을 저장하며 결제금액을 확인한다.
				String savingType = "NO";
				StoreEtc etc = store.getStoreEtc();
				if(etc != null){
					savingType = etc.getSavingType();
					logger.info(savingType);
				}
				// 쿠폰 , 포인트 일경우 할인 금액 실 결제 금액을 DB에 저장
				if(!"NO".equals(savingType)){
					StoreOrderBasket basketOne = basketService.getBasketKey(basket);
					if(basketOne != null){
						if("CP".equals(savingType)){
							// 쿠폰 사용 금액과 할인 금액이 같은지 확인후 넘어간다 
							// 다를 경우 실패
							if(discount != couponAmt){
								logger.error("discount [{}] / couponAmt [{}] 할인금액과 쿠폰 가격이 다릅니다. ", discount, couponAmt);
								return "DIS";
							}
							
							//0 일 경우 사용하는 쿠폰이 없음
							if(couponId > 0){
								// 가지고 있는 쿠폰과 선택한 쿠폰의 가격과 ID가 동일한지 확인
								boolean couponChk = true;
								List<StoreOrderCoupon> res = null;
								if("D".equals(orderType)){								
									res = couponService.getIssueCouponRead(storeIdSt, deliTel);
								}else {
									res = couponService.getIssueCouponRead(storeIdSt, tel);	
								}
								if(res.size() > 0){
									for(StoreOrderCoupon one : res){
										CouponDispItem disp = new CouponDispItem(one);
										// 선택한 쿠폰의 ID와 쿠폰 가격이 모두 동일하면 패스
										if((couponId == disp.getId()) && (couponAmt == disp.getPrice())){
											couponChk = false;
										}
									}
								}
								if(couponChk){
									logger.error("orderNumber [{}] / error [{}] 쿠폰 ID 및 쿠폰 가격 확인 필요합니다. ", orderNumber, "IDF");
									logger.error("couponId [{}] / couponAmt [{}] 넘어온값 ", couponId, couponAmt);
									
									return "IDF";
								}
							}
							
							basketOne.setCouponId(couponId);			// 사용 쿠폰 ID
							
						}else if("PO".equals(savingType)){
							// 포인트 사용 금액과 할인 금액이 같은지 확인후 넘어간다 
							// 다를 경우 실패
							if(discount != usePoint){
								logger.error("discount [{}] / usePoint [{}] 할인금액과 사용포인트가 다릅니다. ", discount, usePoint);
								return "DIS";
							}
							
							PointDispItem pointOne = pointCalc(storeIdSt, tel);
							boolean pointChk = true;
							int oriPoint = pointOne.getPoint();
							if((pointTotal == oriPoint) && (usePoint <= oriPoint)){
								pointChk = false;
							}
							if(pointChk){
								logger.error("orderNumber [{}] / error [{}] 포인트 확인 필요합니다. ", orderNumber, "POF");
								logger.error("pointTotal [{}] / oriPoint [{}]  ", pointTotal, oriPoint);
								logger.error("usePoint [{}] / oriPoint [{}]  ", usePoint, oriPoint);
								
								return "POF";
							}
							
							basketOne.setCouponId(0);					// 포인트는 사용되는 쿠폰ID는 없음							
						}else{
							logger.error("basket [{}] / storeId [{}] / error [store 정보중 savingType 확인이 필요 합니다. ]", basket, storeId);
						}
						
						basketOne.setBasketDiscount(discount);		// 쿠폰 사용금액, 포인트 사용
						basketOne.setBasketPayment(payment);		// 쿠폰 사용후 실 결제 금액, 포인트 사용후 실 결제 금액
						basketOne.setSavingType(savingType);
						basketService.saveOrUpdate(basketOne);
					}else{
						logger.error("basket [{}] / storeId [{}] / error [Basket 주문정보를 찾을수 없습니다.]", basket, storeId);
						
						return "N";
					}
				}
				
				order.setDiscountAmt(discount);
				order.setPaymentAmt(payment);
				order.setSavingType(savingType);
				order.setDeliveryPay(deliveryPay);
				order.touchWho(session);
				storeOrderService.saveOrUpdate(order);
				logger.info(savingType);
				return "Y";
			}else{
				// 결제가 완료 되어 있을 경우  P로 넘긴다. 
				return "P";
			}
		} catch (Exception e) {
			logger.error("Exception [{}]", e);
			logger.error("/payCheck storeId [{}]", storeId);
			logger.error("/payCheck orderNumber [{}]", orderNumber);
			return "N";	
		}

	}
	
	public String bookingTimeCheck(Store store, String bookingTime){
		StoreOpt storeOpt = store.getStoreOpt();
		// 영업 종료 시간 (24시간 영업일 경우에는 제외)
    	Date endDate = null;
    	if(!store.isOpenHour_24()){
   			endDate = new Date(store.getEndTime().getTime());
   			GregorianCalendar calendar = new GregorianCalendar();
   			calendar.setTime(endDate);
   			int endampm  = calendar.get(Calendar.AM_PM);
   			int endhour  = calendar.get(Calendar.HOUR);
   			int endmin   = calendar.get(Calendar.MINUTE);
   			calendar.setTime(new Date());
   			// 다음날 인지 확인하여 더해준다.
   			if(storeOpt.isNextDayClose()){
   				calendar.add(Calendar.DATE, 1);	
   			}
   			calendar.set(Calendar.AM_PM, endampm);
   			calendar.set(Calendar.HOUR, endhour);
   			calendar.set(Calendar.MINUTE, endmin);
   			calendar.set(Calendar.SECOND, 00);
   			calendar.set(Calendar.MILLISECOND, 0);
   			endDate = calendar.getTime();
   		}
    	// 주문 가능 시간 체크 
    	Date possiblDate = null;
    	if(store.getStoreEtc() != null){
	   		StoreEtc storeEtc = store.getStoreEtc();
	   		if("P".equals(storeEtc.getOderPossiblCheck())){
	   			Date possibleDate = new Date(storeEtc.getOder_possible_Time().getTime());
	   			GregorianCalendar calendarPu = new GregorianCalendar();
	   			calendarPu.setTime(possibleDate);
	   			calendarPu.add(Calendar.MINUTE, storeEtc.getOder_setting_Time());
	   			possiblDate = calendarPu.getTime();
	   		}
    	}
    	
    	// 고객이 선택한 예약시간 
    	int hour = 0, min = 0, ampm = 0;
    	String[] bookingArr = bookingTime.split(" ");
    	if(bookingArr.length > 0){
    		if(bookingArr[1].equals("PM")){
    			ampm = 1;
			}
    		String[] timeArr = bookingArr[0].split(":");
    		hour = Util.parseInt(timeArr[0], 0) ;
			min = Util.parseInt(timeArr[1], 0);
    	}
    	
    	GregorianCalendar newTimeChk = new GregorianCalendar();
    	newTimeChk.setTime(new Date());
    	newTimeChk.set(Calendar.AM_PM, ampm);
    	newTimeChk.set(Calendar.HOUR, hour);
    	newTimeChk.set(Calendar.MINUTE, min);
    	newTimeChk.set(Calendar.SECOND, 00);
    	newTimeChk.set(Calendar.MILLISECOND, 0);
		Date nowTimeChk = newTimeChk.getTime();
		
		if(possiblDate != null){
			logger.info("주문 가능 시간 체크  nowTimeChk.compareTo(possiblDate) [{}]", nowTimeChk.compareTo(possiblDate));
			if(nowTimeChk.compareTo(possiblDate) <= 0){
				return "PT";
			}
		}
		
		// 버튼이 영업 종료 시간보다 클 경우 
		if(endDate != null){
			logger.info(" 영업 종료 시간 체크 nowTimeChk.compareTo(endDate) [{}]", nowTimeChk.compareTo(endDate));
			if(nowTimeChk.compareTo(endDate) > 0){
				
				// 매장 재오픈 했을 경우 CustomOperation:true 이기 때문에 영업 종료 시간을 생각하지 않는다. 
				// 포장일 경우에도 종료시간 이후 재 오픈이기 때문에 
				if(!storeOpt.isCustomOperation()){
					return "ET";	
				}
			}
		}
		
		return "";
	}
	
	/**
	 * 쿠폰 조회일 경우 사용
	 */
	@RequestMapping(value = "/readcoupon", method = RequestMethod.POST)
    public @ResponseBody List<CouponDispItem> readcoupon(@RequestBody Map<String, Object> model, HttpServletRequest request, Locale locale, 
	    		HttpSession session) {
		List<CouponDispItem> couponDisp = new ArrayList<CouponDispItem>();
		String storeId = "", tel = "", deliTel = "";
		try {
			
			storeId = Util.parseString((String)model.get("storeId"));
			tel = Util.parseString((String)model.get("tel"));
			deliTel = Util.parseString((String)model.get("deliTel"));
			
	    	if(Util.isNotValid(tel)){
	    		tel = deliTel;
	    	}
	    	List<StoreOrderCoupon> res = couponService.getIssueCouponRead(storeId, tel);
	    	if(res.size() > 0){
	    		for(StoreOrderCoupon one : res){
	    			CouponDispItem disp = new CouponDispItem(one);
	    			
	    			couponDisp.add(disp);
	    		}
	    	}
		} catch (Exception e) {
			logger.error("/readcoupon > payError > storeId : [{}], tel : [{}]", storeId, tel);
			logger.error("/readcoupon > payError > storeId : [{}], deliTel : [{}]", storeId, deliTel);
		}
		
		return couponDisp;
	}
	
	/**
	 * 포인트 조회일 경우 사용
	 */
	@RequestMapping(value = "/readpoint", method = RequestMethod.POST)
	public @ResponseBody PointDispItem readpoint(@RequestBody Map<String, Object> model, HttpServletRequest request, Locale locale, 
			HttpSession session) {
		PointDispItem pointOne = new PointDispItem();
		String storeId = "", tel = "", deliTel = "";
		try {
			
			storeId = Util.parseString((String)model.get("storeId"));
			tel = Util.parseString((String)model.get("tel"));
			deliTel = Util.parseString((String)model.get("deliTel"));
			
			if(Util.isNotValid(tel)){
				tel = deliTel;
			}
			
			pointOne = pointCalc(storeId, tel);
			
		} catch (Exception e) {
			logger.error("/readpoint > payError > storeId : [{}], tel : [{}]", storeId, tel);
			logger.error("/readpoint > payError > storeId : [{}], deliTel : [{}]", storeId, deliTel);
		}
		
		return pointOne;
	}
	
	public PointDispItem pointCalc(String storeId, String tel) {
		PointDispItem pointOne = new PointDispItem();
		List<StorePolicy> policyList = couponService.getPolicyList(Util.parseInt(storeId), "P");
		int usepoint = 0;
		if(policyList.size() > 0){
			for(StorePolicy one : policyList){
				usepoint = one.getPoint();
			}
		}

		List<StoreOrderPoint> res = couponService.getIssuePointRead(storeId, tel);
		if(res.size() > 0){
			for(StoreOrderPoint one : res){
				pointOne.setId(one.getId());
				pointOne.setPoint(one.getPointTotal());
			}
		}
		
		return pointOne;
	}
	
	
	/**
	 * 최종 결제전 결제 하기 버튼 클릭시 매장 주문 불가일 경우 진입경로
	 */
	@RequestMapping(value = "/storeOff", method = RequestMethod.GET)
    public String menuCancelVerifi(Model model, HttpServletRequest request, Locale locale, 
	    		HttpSession session) {
		
		String store = "", table = "";
		try {
			request.setCharacterEncoding("UTF-8");
			
	    	store = Util.parseString((String)request.getParameter("store"));
	    	table = Util.parseString((String)request.getParameter("table"), "0000");
	    	
	    	Store res = storeService.getStoreByStoreKey(store);
   			// 모바일 상점 로그 가져오기
   			imgCommon(res, model, locale);
   			
   			model.addAttribute("table",table);
		} catch (Exception e) {
			logger.error("/storeOff > payError > store : [{}], table : [{}]", store, table);
			model.addAttribute("code", "M9999");
    		model.addAttribute("title", msgMgr.message("smilepay.msg.errorNotStore", locale));
    		model.addAttribute("desc", msgMgr.message("smilepay.msg.errorAdmin", locale));
    		return "order/error";
		}
		
		return "order/storeOff";
	}
	
	public void imgCommon(Store store, Model model, Locale locale) {
		model.addAttribute("storeName", store.getStoreName());

		// 모바일 상점 로고 타입이 없을 경우 기본 "paycast" text 화면 출력
    	if(store.getStoreOpt() != null){
    		if(Util.isNotValid(store.getStoreOpt().getMobileLogoType())){
	    		// 로고가 없을 경우 
	    		model.addAttribute("mobileLogoType", "T");
	    		model.addAttribute("mobileLogoText","PayCast");
    		}else{
	    		String mLogoImageFilename = "";
	    		UploadFile mLogoImageFile = payService.getUploadFile(store.getStoreOpt().getMobileLogoImageId());
	    		if (mLogoImageFile != null) {
	    			mLogoImageFilename = mLogoImageFile.getFilename();
	    		}
	    		model.addAttribute("mobileLogoType", store.getStoreOpt().getMobileLogoType());
	    		model.addAttribute("mobileLogoText", store.getStoreOpt().getMobileLogoText());
	    		model.addAttribute("mLogoImageFilename", mLogoImageFilename);
				model.addAttribute("storeDownLocation", SolUtil.getUrlRoot("MobileTitle", store.getId()));
    		}
    	}else{
    		model.addAttribute("mobileLogoType", "T");
    		model.addAttribute("mobileLogoText","PayCast");
    	}
    	
		model.addAttribute("msg_closeStore", msgMgr.message("smilepay.off.closeStore", locale));
		model.addAttribute("msg_MOEnabled", msgMgr.message("smilepay.off.mobileOrderEnabled", locale));
		model.addAttribute("msg_MOAllowed", msgMgr.message("smilepay.off.mobileOrderAllowed", locale));
		model.addAttribute("msg_openHours", msgMgr.message("smilepay.off.openHours", locale));
		
		model.addAttribute("openType", store.getOpenType());
		model.addAttribute("mobileOrderEnabled", store.isMobileOrderEnabled());
		model.addAttribute("mobileOrderAllowed", store.isMobileOrderAllowed());
		String openTime = "";
		if(store.isOpenHour_24()){
			openTime = "24시간 영업";
		}else{
			String nextSt = "";
			if(store.getStoreOpt().isNextDayClose()){
				nextSt = "<br/>다음날";
			}
			openTime = calendarTime(new Date(store.getStartTime().getTime())) +" ~ " + nextSt +" " + calendarTime(new Date(store.getEndTime().getTime()));
		}
		model.addAttribute("desc", openTime);
	}
	
	public void payError(String mainError, String gubun, String code, Model model, Locale locale) {
		//payError("STORE", "NOTFOUND", "STEP3", model, locale); 매장정보를 찾지 못했을 경우 
		//payError("BASKET", "NOTFOUND", "STEP3", model, locale); 주문정보를 찾지 못했을 경우 
		//payError("DEVICE", "NOTCHOOSE", "M005", model, locale); 결제 정보가 등록이 되어 있지 않을경우 KEY
	
		if("STORE".equals(mainError)){
			model.addAttribute("title", msgMgr.message("smilepay.msg.errorNotStore", locale));
		}else if("BASKET".equals(mainError)){
			model.addAttribute("title", msgMgr.message("smilepay.msg.errorNotBasket", locale));
		}else if("DEVICE".equals(mainError)){
			model.addAttribute("title", msgMgr.message("smilepay.msg.errorNotDevice", locale));
		}
		
		if("NOTFOUND".equals(gubun)){
			model.addAttribute("NOTFOUND", mainError);
		}else if("NOTCHOOSE".equals(gubun)){
			model.addAttribute("NOTCHOOSE", mainError);
		}

		model.addAttribute("code", code);
		model.addAttribute("desc", msgMgr.message("smilepay.msg.errorAdmin", locale));
	}
	
	public String calendarTime(Date setTime){
		if(setTime == null){
			return "";
		}
    	GregorianCalendar calendarBooking = new GregorianCalendar();
    	calendarBooking.setTime(setTime);
		int endampm  = calendarBooking.get(Calendar.AM_PM);
		String endampmText = endampm > 0? " PM" : " AM";
		int endhour  = calendarBooking.get(Calendar.HOUR);
		String endhourText = String.valueOf(endhour);
		if(endhour < 10){
			endhourText = "0" + endhour;
		}
		int endmin   = calendarBooking.get(Calendar.MINUTE);
		String endminText = String.valueOf(endmin);
		if(endmin < 10){
			endminText = "0" + endmin;
		}		
		return endhourText + ":" + endminText + endampmText;
	}
	
	public List<StoreTime> timeList(String rsvpTimeDB, Date possiblDate, Date endDate){
		List<StoreTime> timeList = new ArrayList<StoreTime>();
		String[] rsvpTimeArray = rsvpTimeDB.split(",");
		List<String> rsvpTimeList = new ArrayList<String>(Arrays.asList(rsvpTimeArray));
		if(rsvpTimeList.size() > 0){
			for(String rsvpTime : rsvpTimeList){
				boolean clickTF = true;
				int rsvpTimeInt = Util.parseInt(rsvpTime, 0);
				
				// 화면에 표시할 경우 예약이 불가능한 시간을 표시하기 위하여 사용
	   			Date rsvpTimeDate = new Date();
	   			GregorianCalendar calendarRsvp = new GregorianCalendar();
	   			calendarRsvp.setTime(rsvpTimeDate);
	   			calendarRsvp.add(Calendar.MINUTE, rsvpTimeInt);
	   			rsvpTimeDate = calendarRsvp.getTime();
	   			if(possiblDate != null){
	   				if(rsvpTimeDate.compareTo(possiblDate) <= 0){
	   					clickTF = false;
	   				}
	   			}
	   			
	   			// 버튼이 영업 종료 시간보다 클 경우 
	   			if(endDate != null){
	   				if(rsvpTimeDate.compareTo(endDate) > 0){
	   					clickTF = false;
	   				}
	   			}
				// 화면에 표기 하기 위해 시 / 분을 나누는 작업
	   			String total =  "+" + rsvpTimeInt + "분";
				StoreTime timeOne = new StoreTime(rsvpTimeInt, total, clickTF);
				timeList.add(timeOne);
			}
		}
		
		return timeList;
	}
	
	public static final String encodeSHA256Base64(String pw) {
		String passACL = null;
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (Exception e) {
			e.printStackTrace();
		}

		md.update(pw.getBytes());
		byte[] raw = md.digest();
		byte[] encodedBytes = Base64.encodeBase64(raw);
		passACL = new String(encodedBytes);

		return passACL;
	}
}
