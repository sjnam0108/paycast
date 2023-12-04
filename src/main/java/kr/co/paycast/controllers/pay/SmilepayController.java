package kr.co.paycast.controllers.pay;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.Message;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.pay.CouponPolicy;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.pay.StoreDeliveryPay;
import kr.co.paycast.models.pay.StoreDeliveryPolicy;
import kr.co.paycast.models.pay.StoreEtc;
import kr.co.paycast.models.pay.StoreOpt;
import kr.co.paycast.models.pay.StorePolicy;
import kr.co.paycast.models.pay.UploadFile;
import kr.co.paycast.models.pay.service.CouponPointService;
import kr.co.paycast.models.pay.service.DeliveryPayService;
import kr.co.paycast.models.pay.service.MenuService;
import kr.co.paycast.models.pay.service.PayService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.self.service.SelfService;
import kr.co.paycast.models.store.StoreAlimTalk;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderBasket;
import kr.co.paycast.models.store.StoreOrderBasketList;
import kr.co.paycast.models.store.StoreOrderCancel;
import kr.co.paycast.models.store.StoreOrderCook;
import kr.co.paycast.models.store.StoreOrderCoupon;
import kr.co.paycast.models.store.StoreOrderPay;
import kr.co.paycast.models.store.StoreOrderPoint;
import kr.co.paycast.models.store.service.EasyPayService;
import kr.co.paycast.models.store.service.StoreAllimTalkService;
import kr.co.paycast.models.store.service.StoreBasketService;
import kr.co.paycast.models.store.service.StoreCancelService;
import kr.co.paycast.models.store.service.StoreCookService;
import kr.co.paycast.models.store.service.StoreOrderService;
import kr.co.paycast.models.store.service.StorePayService;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.SmilePayUtil;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.CouponDispItem;
import kr.co.paycast.viewmodels.pay.MenuGroupItem;
import kr.co.paycast.viewmodels.pay.PointDispItem;
import kr.co.paycast.viewmodels.pay.StoreTime;
import kr.co.paycast.viewmodels.self.MenuPayItem;
import kr.co.paycast.viewmodels.store.RefillView;
import kr.co.paycast.viewmodels.store.StoreCanCelView;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 스마일 페이 컨트롤러
 * 
 */
@Controller("smilepay-controller")
@RequestMapping(value = "/smartropay")
public class SmilepayController {
	private static final Logger logger = LoggerFactory.getLogger(SmilepayController.class);

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private SelfService selfService;

	@Autowired
	private StoreOrderService storeOrderService;

	@Autowired
	private StoreCookService storeCookService;

	@Autowired
	private StoreCancelService storeCancelService;

	@Autowired
	private StorePayService storePayService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private PayService payService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private StoreBasketService basketService;

	@Autowired
	private EasyPayService easyPayService;
	
	  @Autowired
	  private DeliveryPayService deliveryPayService;
	  
	  @Autowired
	  private CouponPointService couponService;
	  
	  @Autowired
	  private StoreAllimTalkService alimTalkService;



	/**
	 * 3시간 시간 체크 후 첫페이지로 이동
	 */
	@RequestMapping(value = "/onFn", method = { RequestMethod.POST })
	public @ResponseBody String checkLoginKey(@RequestBody Map<String, Object> model) {

		String menuInTime = Util.parseString((String) model.get("menuInTime"));

		return (checkTime(menuInTime) ? "Y" : "N");
	}

	private boolean checkTime(String inTime) {
		long time = 99;
		try {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Date startTime = dateFormat.parse(inTime);
			long diff = now.getTime() - startTime.getTime();
			time = diff / 3600000;
		} catch (Exception e) {
			logger.error("TIME Error [{}]", e);
			logger.error("TIME Error [{}]", inTime);
			return false;
		}

		if (time > 3) {
			return false;
		}

		return true;
	}

	public void imgCommon(Store store, Model model) {
		// 모바일 상점 로고 타입이 없을 경우 기본 "paycast" text 화면 출력
		if (store.getStoreOpt() != null) {
			if (Util.isNotValid(store.getStoreOpt().getMobileLogoType())) {
				// 로고가 없을 경우
				model.addAttribute("mobileLogoType", "T");
				model.addAttribute("mobileLogoText", "PayCast");
			} else {
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
		} else {
			model.addAttribute("mobileLogoType", "T");
			model.addAttribute("mobileLogoText", "PayCast");
		}
	}

	public String calendarTime(Date setTime) {
		if (setTime == null) {
			return "";
		}
		GregorianCalendar calendarBooking = new GregorianCalendar();
		calendarBooking.setTime(setTime);
		int endampm = calendarBooking.get(Calendar.AM_PM);
		String endampmText = endampm > 0 ? " PM" : " AM";
		int endhour = calendarBooking.get(Calendar.HOUR);
		String endhourText = String.valueOf(endhour);
		if (endhour < 10) {
			endhourText = "0" + endhour;
		}
		int endmin = calendarBooking.get(Calendar.MINUTE);
		String endminText = String.valueOf(endmin);
		if (endmin < 10) {
			endminText = "0" + endmin;
		}
		return endhourText + ":" + endminText + endampmText;
	}

	/**
	 * 메뉴 선택 화면
	 */
	@RequestMapping(value = "/menu", method = { RequestMethod.GET, RequestMethod.POST })
	public String menuChoose(Model model, Locale locale, HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		DecimalFormat format = new DecimalFormat("###,###");

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("html_lang", Util.htmlLang(locale)),

				new Message("pay_won", "smilepay.won"), new Message("pay_total", "smilepay.total"),
				new Message("pay_step1", "smilepay.step1"), new Message("pay_order", "smilepay.order"),
				new Message("pay_refer", "smilepay.refer"), new Message("pay_cancel", "smilepay.cancel"),
				new Message("pay_shut", "smilepay.shut"), new Message("pay_embody", "smilepay.embody"),
				new Message("pay_nope", "smilepay.nope"), new Message("pay_yes", "smilepay.yes"),
				new Message("pay_paydate", "smilepay.paydate"), new Message("pay_approval", "smilepay.approval"),
				new Message("pay_apprCancel", "smilepay.apprCancel"),

				new Message("pay_bizRep", "smilepay.bizRep"), new Message("pay_bizPhone", "smilepay.bizPhone"),
				new Message("pay_addr2", "smilepay.addr2"),

				new Message("msg_numEnter", "smilepay.msg.numEnter"),
				new Message("msg_noOrder", "smilepay.msg.noOrder"),
				new Message("msg_notSupportBrowser", "smilepay.msg.notSupportBrowser"),
				new Message("msg_notOrder", "smilepay.msg.notOrder"),
				new Message("msg_noAvaMenu", "smilepay.msg.noAvaMenu"),
				new Message("msg_noChooseMenu", "smilepay.msg.noChooseMenu"),
				new Message("msg_orderCancel", "smilepay.msg.orderCancel"), new Message("msg_thx", "smilepay.msg.thx"),
				new Message("msg_manager", "smilepay.msg.manager"), new Message("msg_contact", "smilepay.msg.contact"),
				new Message("msg_auCancelOrder", "smilepay.msg.auCancelOrder"),
				new Message("msg_checkOrder", "smilepay.msg.checkOrder"),
				new Message("msg_timeOutMsg", "smilepay.msg.timeOutMsg"),

				new Message("alert_ok", "alert.ok"), new Message("pay_totalamt", "sales.totalamt"),
				new Message("pay_ordernum", "sales.ordernum"),

				new Message("approvalDigit", "approval.digit") });

		// 1. 상점 키를 체크
		// 2. 영업 중인지 체크
		// 3. 모바일 주문 사용을 하는 매장인지 체크
		String menuUrl = "order/menu";

		String store = Util.parseString((String) request.getParameter("store"));
		String table = Util.parseString((String) request.getParameter("table"), "0000");
		try {
			Store res = storeService.getStoreByStoreKey(store);
			if (res == null) {
				logger.error("/menu > payError > store : [{}]", store);
				model.addAttribute("code", "M9999");
				model.addAttribute("title", msgMgr.message("smilepay.msg.errorNotStore", locale));
				model.addAttribute("desc", msgMgr.message("smilepay.msg.errorAdmin", locale));
				return "order/error";
			}

			// 상점 정보가 있을 경우 모든 모바일 페이지 에서 사용되는 정보를 model set
			// 페이지 title : storename
			model.addAttribute("storeName", res.getStoreName());
			// id대신 사용 되는 외부용 ID(상정 조회 사용되는 KEY)
			model.addAttribute("storeKey", res.getStoreKey());

			// 스마일 페이 상점 ID를 체크 하여
			// 2020.03.25 팀장님의 요청으로 메뉴화면은 보이도록 변경 (이전에는 결제 Key가 없으면 모바일 화면 사용 못함)
//    		if(res.getStoreEtc() != null){
//    			if((Util.isNotValid(res.getStoreEtc().getSpStoreKey()) || Util.isNotValid(res.getStoreEtc().getSpAuthKey())) && Util.isNotValid(res.getStoreEtc().getEpStoreKey())){
//					logger.info("smilepay > SmilePayKey > NOT FOUND > STEP1 M001 StoreName [{}]", res.getStoreName());
//					logger.info("smilepay > SmilePayKey > NOT FOUND > STEP1 M001 storeKey [{}] code [{}]", "isNotValid", "M003");
//					request.setAttribute("storeKey", store);
//					request.setAttribute("table", table);
//					request.setAttribute("code", "M003");
//					request.setAttribute("gubun", "NF");
//					
//					return "forward:/stopViewUrl";
//    			}
//    		}

			// 모바일 상점 로그 가져오기
			imgCommon(res, model);

			// Store.getOpenType() : O(영업중), C(영업마감)
			// isMobileOrderEnabled : 영업중에도 모바일 결제 사용가능 여부 (true:사용, false:미사용)
			if (res.isMobileOrderEnabled()) {
				List<MenuGroupItem> menuGroup = menuService.getAllMenus(res.getId(), true);
				model.addAttribute("menuGroup", menuGroup);
			} else {
				model.addAttribute("msg_closeStore", msgMgr.message("smilepay.off.closeStore", locale));
				model.addAttribute("msg_MOEnabled", msgMgr.message("smilepay.off.mobileOrderEnabled", locale));
				model.addAttribute("msg_MOAllowed", msgMgr.message("smilepay.off.mobileOrderAllowed", locale));
				model.addAttribute("msg_openHours", msgMgr.message("smilepay.off.openHours", locale));

				model.addAttribute("openType", res.getOpenType());
				model.addAttribute("mobileOrderEnabled", res.isMobileOrderEnabled());
				model.addAttribute("mobileOrderAllowed", res.isMobileOrderAllowed());
				String openTime = "";
				if (res.isOpenHour_24()) {
					openTime = "24시간 영업";
				} else {
					String nextSt = "";
					if (res.getStoreOpt().isNextDayClose()) {
						nextSt = "<br/>다음날";
					}
					openTime = calendarTime(new Date(res.getStartTime().getTime())) + " ~ " + nextSt + " "
							+ calendarTime(new Date(res.getEndTime().getTime()));
				}
				model.addAttribute("desc", openTime);
				menuUrl = "order/storeOff";
			}

			// 해당 상점 메뉴에 접속한 시간이 총 3시간이상 되었을 경우 처음 화면으로 이동(장시간 이용하지 않아 첫페이지 이동)
			SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmss");
			String toDate = dt.format(new Date());
			model.addAttribute("time", toDate);

			// 장바구니 Key 사용
			// 1. 선택했던 메뉴 정보를 다시 조회
			// 2. 결제 완료되었을 경우 해당 Key를 주문고유번호 생성에 사용
			// 3. 주문결제 진행중 취소 되었을 경우 해당 메뉴 정보를 다시 조회 하여 결제전 화면으로 이동
			// 쿠키에 장바구니 key 포함
			String basket = "";
			String cookieKey = "Store_" + res.getId(); // 상점 별로 쿠키 생성
			String cookieValue = Util.cookieValue(request, cookieKey);
			int goodsAmt = 0; // 총 금액
			int totalMenuCount = 0;

			boolean cookieCreate = true;
			if (cookieValue != null) {
				// 쿠키 값에서 장바구니 ID를 가져와서 장바구니 체크를 진행
				// 3시간 이상 장바구니가 생성되어 있을 경우 해당 장바구니 정보를 삭제 하고 새로 생성
				// 3시간이 지나지 않았을 경우 해당 메뉴를 조회하여 화면에 표시
				// 단! 기존 메뉴를 보여줄 경우 고객에서 문의 후 Yes를 눌렀을 경우에만 보여준다.
				String[] cookieArray = cookieValue.split("_");
				basket = cookieArray[2];

				// 기존의 장바구니를 가져온다.
				StoreOrderBasket basketOne = basketService.getBasketKey(basket);
				if (basketOne != null) {
					// 등록된 시간이 3시간이 넘었을 경우 해당 내용을 삭제 하고 새로 발급한다.
					if (checkTime(basketOne.getOrderIntime())) {
						cookieCreate = false;

						List<MenuPayItem> payitemList = new ArrayList<MenuPayItem>();
						List<StoreOrderBasketList> list = basketService.getBasketList(basketOne.getId());
						if (list.size() > 0) {
							for (StoreOrderBasketList basketList : list) {
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

								goodsAmt += (basketList.getMenuAmount() * basketList.getMenuTotalAmt());
								totalMenuCount += basketList.getMenuAmount();

								payitemList.add(payitem);
							}
						}
						model.addAttribute("payitemList", payitemList);
						model.addAttribute("goodsAmt", format.format(goodsAmt));
						model.addAttribute("totalcount", totalMenuCount);

						// 리필 메뉴에 대해서 주문 할경우 장바구니 저장된 전화번호를 가져온다.
						model.addAttribute("refiTel", basketOne.getRefiTel());

					} else {
						logger.info("[NEW] 3시간이 지나서 새로 생성해야 합니다. 이전 KEY [{}]", basketOne.getBasketKey());
						// 삭제 필요
						basketService.delete(basketOne);
					}
				} else {
					logger.info("basketKey 체크 >>> [{}] 존재 하지 않습니다.", basket);
				}
			} else {
				logger.info("생성되어 있는 쿠키값이 없습니다.");
			}

			if (cookieCreate) {
				// 쿠키 값이 없을 경우 새로 생성 + 장바구니 Key 새로 생성
				basket = Util.getRandomKey(8);

				cookieValue = res.getStoreKey() + "_" + table + "_" + basket;
				if (cookieValue != null) {
					Cookie cookie = new Cookie(cookieKey, cookieValue);
					cookie.setMaxAge(60 * 60 * 24); // 하루로 변경
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}

			model.addAttribute("basket", basket);
			model.addAttribute("storeId", res.getId());
			model.addAttribute("table", table);
			model.addAttribute("paygubun", res.getStoreEtc().getStorePayGubun());

			// 화면에서 하단에 상점 정보를 보여주기 위해서 포함
			model.addAttribute("bizName", res.getBizName());
			model.addAttribute("bizNum", res.getBizNum());
			model.addAttribute("phone", res.getPhone());
			model.addAttribute("bizRep", res.getBizRep());
			model.addAttribute("addr2", res.getAddress());
			if (res.isOpenHour_24()) {
				model.addAttribute("openHours", "24시간 영업");
			} else {
				model.addAttribute("openHours", calendarTime(new Date(res.getStartTime().getTime())) + " ~ "
						+ calendarTime(new Date(res.getEndTime().getTime())));
			}
			model.addAttribute("memo", res.getMemo());

		} catch (Exception e) {
			logger.error("smilepay > Exception > STEP1  store [{}], table [{}]", store, table);
			logger.error("smilepay > Exception > STEP1  store [{}], code [{}]", store, "M999");
			logger.error("Menu STEP1 error[{}]", e);
			request.setAttribute("storeKey", store);
			request.setAttribute("table", table);
			request.setAttribute("code", "M999");
			request.setAttribute("gubun", "E");

			return "forward:/stopViewUrl";
		}
		System.err.println("???????????????????????" + menuUrl);
		return menuUrl;
	}

	/**
	 * 첫페이지에서 order 페이지로 이동
	 */
	@RequestMapping(value = "/order", method = { RequestMethod.GET, RequestMethod.POST })
	public String order(Model model, HttpServletRequest request, Locale locale, HttpSession session) {

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("html_lang", Util.htmlLang(locale)),
				new Message("url_login", "common.server.msg.loginUrl"),
				new Message("url_login2", "common.server.msg.loginForcedLogoutUrl"),

				new Message("pay_won", "smilepay.won"), new Message("pay_shut", "smilepay.shut"),
				new Message("pay_total", "smilepay.total"), new Message("pay_order", "smilepay.order"),
				new Message("pay_refer", "smilepay.refer"), new Message("pay_payPrice", "smilepay.payPrice"),
				new Message("pay_payment", "smilepay.payment"), new Message("pay_menu_Add", "smilepay.menu.Add"),
				new Message("pay_menu_packing", "smilepay.menu.packing"),
				new Message("msg_noOrder", "smilepay.msg.noOrder"),
				new Message("msg_notSupportBrowser", "smilepay.msg.notSupportBrowser"),

				new Message("alert_ok", "alert.ok"), new Message("pay_totalamt", "sales.totalamt"),
				new Message("pay_ordernum", "sales.ordernum"),

				new Message("approvalDigit", "approval.digit") });

		DecimalFormat format = new DecimalFormat("###,###");

		String storeKey = (String) request.getParameter("storeKey");
		String basket = (String) request.getParameter("basket");
		String table = (String) request.getParameter("table");
		String time = (String) request.getParameter("time");
		String order = (String) request.getParameter("order");

		// id대신 사용 되는 외부용 ID(상정 조회 사용되는 KEY)
		model.addAttribute("storeKey", storeKey);
		// 장바구니 키
		model.addAttribute("basket", basket);

		try {
			Store res = storeService.getStoreByStoreKey(storeKey);
			if (res == null) {
				logger.error("/order > payError > storeKey : [{}], table :[{}]", storeKey, table);
				logger.error("/order > payError > basket : [{}], time : [{}]", basket, time);
				logger.error("/order > payError > basket : [{}], order : [{}]", basket, order);
				model.addAttribute("code", "STEP2");
				model.addAttribute("title", msgMgr.message("smilepay.msg.errorNotStore", locale));
				model.addAttribute("desc", msgMgr.message("smilepay.msg.errorAdmin", locale));
				return "order/error";
			}

			if (!Util.isNotValid(order)) {
				logger.info("order 주문 내역 삭제 >>> [{}]", order);
				// 기존에 있던 주문내역 정보를 삭제 한다.
				// order = orderNumber
				storeOrderService.deleteOrder(res.getId(), order);
			}

			// 모바일 상점 로그 가져오기
			imgCommon(res, model);

			// 상점 정보가 있을 경우 모든 모바일 페이지 에서 사용되는 정보를 model set
			// 페이지 title : storename
			model.addAttribute("storeName", res.getStoreName());

			if (!res.isMobileOrderEnabled()) {
				model.addAttribute("msg_closeStore", msgMgr.message("smilepay.off.closeStore", locale));
				model.addAttribute("msg_MOEnabled", msgMgr.message("smilepay.off.mobileOrderEnabled", locale));
				model.addAttribute("msg_MOAllowed", msgMgr.message("smilepay.off.mobileOrderAllowed", locale));
				model.addAttribute("msg_openHours", msgMgr.message("smilepay.off.openHours", locale));

				model.addAttribute("openType", res.getOpenType());
				model.addAttribute("mobileOrderEnabled", res.isMobileOrderEnabled());
				model.addAttribute("mobileOrderAllowed", res.isMobileOrderAllowed());

				String openTime = "";
				if (res.isOpenHour_24()) {
					openTime = "24시간 영업";
				} else {
					String nextSt = "";
					if (res.getStoreOpt().isNextDayClose()) {
						nextSt = "<br/>다음날";
					}
					openTime = calendarTime(new Date(res.getStartTime().getTime())) + " ~ " + nextSt + " "
							+ calendarTime(new Date(res.getEndTime().getTime()));
				}
				model.addAttribute("desc", openTime);

				return "order/storeOff";
			}

			StoreOrderBasket basketOne = basketService.getBasketKey(basket);

			// 메뉴중 하나라도 포장일 경우 포장으로 체크(전체 포장으로 - 2019.08.06)
			String packingYn = "N";
			int goodsAmt = 0; // 총 금액
			int totalMenuCount = 0;
			if (basketOne != null) {
				List<MenuPayItem> payitemList = new ArrayList<MenuPayItem>();
				List<StoreOrderBasketList> list = basketService.getBasketList(basketOne.getId());
				if (list.size() > 0) {
					for (StoreOrderBasketList basketList : list) {
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
						if (!Util.isNotValid(basketList.getEssName())) {
							String essName = basketList.getEssName();
							String essOption = "";
							String[] ess = essName.split("\\^");
							if (ess.length > 0) {
								for (int essInt = 0; essInt < ess.length; essInt++) {
									if (!"".equals(ess[essInt])) {
										essOption += "- " + ess[essInt] + "<br>";
									}
								}
								payitem.setEssNameText(essOption);
							} else {
								payitem.setEssNameText("");
							}
						}
						payitem.setAddVal(basketList.getAddVal());
						payitem.setAddName(basketList.getAddName());
						if (!Util.isNotValid(basketList.getAddName())) {
							String addName = basketList.getAddName();
							String addOption = "";
							String[] add = addName.split("\\^");
							if (add.length > 0) {
								for (int addInt = 0; addInt < add.length; addInt++) {
									if (!"".equals(add[addInt])) {
										addOption += "- " + add[addInt] + "<br>";
									}
								}
								payitem.setAddNameText(addOption);
							} else {
								payitem.setAddNameText("");
							}
						}

						payitem.setSubMenu(Util.parseString(basketList.getSubmenu()));
						payitem.setImgSrc(basketList.getSrc());

						if ("1".equals(basketList.getPacking())) {
							packingYn = "Y";
						}

						goodsAmt += (basketList.getMenuAmount() * basketList.getMenuTotalAmt());
						totalMenuCount += basketList.getMenuAmount();

						payitemList.add(payitem);
					}
				}
				model.addAttribute("payitemList", payitemList);
				model.addAttribute("goodsAmt", format.format(goodsAmt));
				model.addAttribute("totalcount", totalMenuCount);
			} else {
				logger.info("BasketKey 체크 >>> [{}] 존재 하지 않습니다.", basket);
			}

			model.addAttribute("packingYn", packingYn);
			model.addAttribute("table", table);
			model.addAttribute("time", time);
			model.addAttribute("storeId", res.getId());

			model.addAttribute("storePay", res.getStoreEtc().getStorePayGubun());
			
			

		} catch (Exception e) {
			logger.error("smilepay > Exception > STEP2  storeKey [{}], table [{}]", storeKey, table);
			logger.error("smilepay > Exception > STEP2  storeKey [{}], code [{}]", storeKey, "M999");
			logger.error("Menu STEP2 error[{}]", e);
			request.setAttribute("storeKey", storeKey);
			request.setAttribute("table", table);
			request.setAttribute("code", "M999");
			request.setAttribute("gubun", "E");

			return "forward:/stopViewUrl";
		}

		return "order/menuOrder";
	}

	/**
	 * 스마일 페이 2019.12.05 모바일 결제전 화면은 MobileOrderController 으로 이전 됨. (easypay 이후 스마일
	 * 페이가 사용될수 있어서 그대로 놓아둠)
	 * 
	 */
	@RequestMapping(value = { "", "/" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String smilepayIndex(Model model, HttpServletRequest request, Locale locale, HttpServletResponse response,
			HttpSession session) {
		msgMgr.addViewMessages(model, locale, new Message[] { new Message("html_lang", Util.htmlLang(locale)),
				new Message("url_login", "common.server.msg.loginUrl"),
				new Message("url_login2", "common.server.msg.loginForcedLogoutUrl"),

				new Message("pay_won", "smilepay.won"), new Message("pay_total", "smilepay.total"),
				new Message("pay_payment", "smilepay.payment"), new Message("pay_email", "smilepay.email"),
				new Message("pay_phone", "smilepay.phone"), new Message("pay_name", "smilepay.name"),
				new Message("pay_totalPrice", "smilepay.totalPrice"), new Message("pay_pdCnt", "smilepay.pdCnt"),
				new Message("pay_pdNm", "smilepay.pdNm"), new Message("pay_consent", "smilepay.consent"),

				new Message("pay_msg_phoneCheck", "smilepay.msg.phoneCheck"),
				new Message("pay_msg_phoneMsg", "smilepay.msg.phoneMsg"),
				new Message("pay_msg_phone", "smilepay.msg.phone"),
		        new Message("pay_msg_timeOutMsg", "smilepay.msg.timeOutMsg"),
				new Message("pay_msg_readyYouWant", "smilepay.msg.readyYouWant"),
				new Message("pay_msg_chooseYouWant", "smilepay.msg.chooseYouWant")
		});
		String storeKey = (String) request.getParameter("storeKey");
		String basket = (String) request.getParameter("basket");
		String table = (String) request.getParameter("table");
		String time = (String) request.getParameter("time");

		String jspURL = "order/smartropay";

		try {
			// id대신 사용 되는 외부용 ID(상정 조회 사용되는 KEY)
			model.addAttribute("storeKey", storeKey);
			// 장바구니 키
			model.addAttribute("basket", basket);

			// 화면 구성을 위해서 매장 정보 조회
			Store res = storeService.getStoreByStoreKey(storeKey);
			if (res == null) {
				logger.error("/order > payError > storeKey : [{}], table :[{}]", storeKey, table);
				logger.error("/order > payError > basket : [{}], time : [{}]", basket, time);
				model.addAttribute("code", "STEP3");
				model.addAttribute("title", msgMgr.message("smilepay.msg.errorNotStore", locale));
				model.addAttribute("desc", msgMgr.message("smilepay.msg.errorAdmin", locale));
				return "order/error";
			}
			model.addAttribute("storeName", res.getStoreName());

			// 모바일 결제 시 주문 선택에 대한 서비스 유형 정의 값
			model.addAttribute("mOrderType", res.getStoreOpt().getOrderType());
			
			// 쿠폰 및 포인트 에 대한 정의값 
			// NO= 사용안함, CP= 쿠폰, PO= 포인트 
			if(res.getStoreEtc() != null){
				model.addAttribute("savingType", res.getStoreEtc().getSavingType());
			}else{
				model.addAttribute("savingType", "NO");
			}

			// 모바일 상점 로그 가져오기
			imgCommon(res, model);

			if (!res.isMobileOrderEnabled()) {
				model.addAttribute("msg_closeStore", msgMgr.message("smilepay.off.closeStore", locale));
				model.addAttribute("msg_MOEnabled", msgMgr.message("smilepay.off.mobileOrderEnabled", locale));
				model.addAttribute("msg_MOAllowed", msgMgr.message("smilepay.off.mobileOrderAllowed", locale));
				model.addAttribute("msg_openHours", msgMgr.message("smilepay.off.openHours", locale));

				model.addAttribute("openType", res.getOpenType());
				model.addAttribute("mobileOrderEnabled", res.isMobileOrderEnabled());
				model.addAttribute("mobileOrderAllowed", res.isMobileOrderAllowed());

				String openTime = "";
				if (res.isOpenHour_24()) {
					openTime = "24시간 영업";
				} else {
					String nextSt = "";
					if (res.getStoreOpt().isNextDayClose()) {
						nextSt = "<br/>다음날";
					}
					openTime = calendarTime(new Date(res.getStartTime().getTime())) + " ~ " + nextSt + " "
							+ calendarTime(new Date(res.getEndTime().getTime()));
				}
				model.addAttribute("desc", openTime);

				return "order/storeOff";
			}

			StoreOrderBasket basketOne = basketService.getBasketKey(basket);
			if (basketOne == null) {
				logger.error("smilepay > Exception > STEP3  storeKey [{}], table [{}]", storeKey, table);
				logger.error("smilepay > Exception > STEP3  basket : [{}], time : [{}]", basket, time);
				logger.error("Menu basketOne NOT FOUND [{}]", basket);
				request.setAttribute("storeKey", storeKey);
				request.setAttribute("table", table);
				request.setAttribute("code", "M003");
				request.setAttribute("gubun", "NF");

				return "forward:/stopViewUrl";
			}

			int goodsAmt = 0; // 총 금액
			int totalindex = 0;
			String goodsName = "";
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
			List<MenuPayItem> payitemList = new ArrayList<MenuPayItem>();
			List<StoreOrderBasketList> list = basketService.getBasketList(basketOne.getId());
			if (list.size() > 0) {
				totalindex = list.size();
				int i = 0;
				for (StoreOrderBasketList basketList : list) {
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

					if (i == 0) {
						goodsName = basketList.getMenuName();
					}
					payitemList.add(payitem);
				}

				if (totalindex > 1) {
					goodsName += ("외" + (totalindex - 1));
				}
			}

			SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
			nowTime = yyyyMMddHHmmss.format(new Date());
			// String nowTime = yyyyMMddHHmmssSS.format(new Date());

			// device 정보를 확인하여 개발이 필요하다.
			// M 일 경우 : 모바일로 표시_2019.10.02
			// 2019.10.02 이후 MS : 스마일페이 모바일 / ME : easypay 로 나누어 저장
			String device = res.getStoreEtc().getStorePayGubun();
			if (Util.isNotValid(device)) {
				logger.error("smilepay > SmilePayKey > STEP3  storeKey [{}], table [{}]", storeKey, table);
				logger.error("smilepay > SmilePayKey > STEP3  basket : [{}], time : [{}]", basket, time);
				logger.error("smilepay > SmilePayKey > NOT FOUND > M005 StoreName [{}]", res.getStoreName());
				logger.error("Menu device NOT SELECT [{}]", basket);
				request.setAttribute("storeKey", storeKey);
				request.setAttribute("table", table);
				request.setAttribute("code", "M005");
				request.setAttribute("gubun", "SK");

				return "forward:/stopViewUrl";
			} else {

				// 2019.11.07 MS : 스마일페이 모바일 / ME : easypay 로 나누어 저장
				// 2019.11.07 MS : 스마일페이 모바일 사용하지 않는다.!(소스 및 상점 > 결제 화면에서만 확인할수 있음)
				if ("ME".equals(device)) {
					jspURL = "order/easypay";
				}
			}

			// MS일 경우 spStoreKey 사용
			// ME일 경우 epStoreKey 사용
			String storeMid = res.getStoreEtc().getSpStoreKey();
			if ("ME".equals(device)) {
				storeMid = res.getStoreEtc().getEpStoreKey();
			}
			// 해당 키가 없을 경우 결제를 진행하지 않고 에러로 이동
			if (Util.isNotValid(storeMid)) {
				logger.error("smilepay > SmilePayKey > STEP3  storeKey [{}], table [{}]", storeKey, table);
				logger.error("smilepay > SmilePayKey > STEP3  basket : [{}], time : [{}]", basket, time);
				logger.error("smilepay > SmilePayKey > NOT FOUND > M005 StoreName [{}]", res.getStoreName());
				logger.error("Menu SmilePayKey NOT FOUND [{}]", basket);
				request.setAttribute("storeKey", storeKey);
				request.setAttribute("table", table);
				request.setAttribute("code", "M005");
				request.setAttribute("gubun", "SK");

				return "forward:/stopViewUrl";
			}

			// 주문키 생성 > 모바일 : ME(MS) + 장바구니 코드 + paycast상점 KEY + 스마일페이상점ID + 날짜시간
			// 이후 결제 승인이 정상적으로 났을 경우 해당 주문 번호에 주문순서번호를 붙여준다.
			// 2019.07.07 주문키 생성 : ME(MS) + 상점 ID + 상점 KEY(???) + 날짜시간
			String orderNumber = device + res.getStoreKey() + storeMid + nowTime;

			// returnURL, retryURL, 결제중지 URL 에 대한 도메인을 가져와 URL을 생성해 준다.
			String reqUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");

			// 결제결과를 수신할 가맹점 returnURL 설정
			model.addAttribute("reqUrl", reqUrl);

			model.addAttribute("returnUrl",
					reqUrl + "/smartropay/returnMobilePay?store=" + res.getStoreKey() + "&basket=" + basket);
			// 가맹점 retryURL 설정
			model.addAttribute("retryUrl", reqUrl + "/smartropay/informMobile?store=" + res.getStoreKey());
			// 결제중지 URL
			model.addAttribute("stopUrl", reqUrl + "/smartropay/order?storeKey=" + storeKey + "&basket=" + basket
					+ "&table=" + table + "&time=" + time + "&order=" + orderNumber);

			String encryptData = encodeSHA256Base64(nowTime + storeMid + goodsAmt + res.getStoreEtc().getSpAuthKey());
//			model.addAttribute("encryptData", encryptData);

			// 총 수량
			model.addAttribute("totalindex", totalindex);
			// 총 금액
			model.addAttribute("goodsAmt", goodsAmt);
			// 화면에 보여주는 총 금액
			format = new DecimalFormat("###,###");
			String goodsAmtCom = format.format(goodsAmt);
			model.addAttribute("goodsAmtCom", goodsAmtCom);
			// 거래 상품명
			model.addAttribute("goodsName", goodsName);
			// 주문 번호
			model.addAttribute("orderNumber", orderNumber);
			// 상점ID
			model.addAttribute("storeMid", storeMid);
			// 지불수단 - 카드 (고정)
			model.addAttribute("payMethod", "CARD");
			// 상점결제결과처리방식 - Y-[권장]상점결과URL즉시전송(고정) / N-PG결제결과창 호출
			model.addAttribute("mallResultFWD", "Y");
			// 결제타입 - 0-일반
			model.addAttribute("transType", "0");
			// 소켓사용여부 - N으로 고정
			model.addAttribute("socketYN", "N");
			// 응답메시지 타입 - Language ->> KR 기본값
			model.addAttribute("language", "KR");
			// 결제결과인코딩 - EncodingType
			model.addAttribute("encodingType", "utf8");
			// 결제 연동방식 - WEB 고정 / APP(앱) / HYB(하이브리드 앱)
			model.addAttribute("clientType", "WEB");
			// 전문생성일시 - 필수
			model.addAttribute("EdiDate", nowTime);
			// ISP 인증 후 결과 페이지 기존창 처리 여부 - Y-[권장]기존결제창 / N-새창
			model.addAttribute("ispWapUrl", "Y");
			// 할부개월 - 카드사 선택 호출시 사용. 00:일시, 02,…,12
			model.addAttribute("cardQuota", "00");
			// 카드사포인트사용 - 0-미사용 /1-사용 /2-사용, UI안보임
			model.addAttribute("cardPoint", "0");
			//총 결제 금액
			model.addAttribute("billingAmt", goodsAmt+deliveryPayAmt);
			// 화면에 보여주는 결제 금액
			String billingAmtCom = format.format(goodsAmt+deliveryPayAmt);
			model.addAttribute("billingAmtCom", billingAmtCom);

			String divideInfoFirst = "{'DivideInfo':[";
			String divideInfoLast = "]}";
			for (MenuPayItem payItem : payitemList) {
				String orderPrice = payItem.getToPrice();
				String orderCount = payItem.getOrderCount();

				String makePay = "{'Amt':'" + (Integer.parseInt(orderPrice) * Integer.parseInt(orderCount))
						+ "','Mid':'" + storeMid + "','GoodsName':'" + payItem.getName() + "'}";

				divideInfoFirst = divideInfoFirst + makePay;
			}
			String divideInfo = divideInfoFirst + divideInfoLast;
			String temp = divideInfo.replaceAll("&#39;", "\"");
			temp = divideInfo.replaceAll("\'", "\"");
			// DivideInfo = temp;

			Charset euckrCharset = Charset.forName("utf-8");
			ByteBuffer byteBuffer = euckrCharset.encode(temp);
			byte[] euckrStringBuffer = new byte[byteBuffer.remaining()];
			byteBuffer.get(euckrStringBuffer);

			String b64Enc = new String(Base64.encodeBase64(euckrStringBuffer));

			divideInfo = b64Enc;
			// 상점 모드 - 서브몰승인데이터
			model.addAttribute("divideInfo", divideInfo);

			// 매장 화면 구성 데이터
			model.addAttribute("storeName", res.getStoreName());
			model.addAttribute("storeIntroduction", "");
			model.addAttribute("storeId", res.getId());
			model.addAttribute("table", table);
			// 매장 화면 구성 데이터
			model.addAttribute("alimTalkAllowed", res.isAlimTalkAllowed());

			// 서버IP
			InetAddress local = InetAddress.getLocalHost();
			String serverIp = local.getHostAddress();
			model.addAttribute("serverIp", serverIp);

			// 접속 IP
			String UserIp = SmilePayUtil.getRemoteAddr(request);
			model.addAttribute("UserIp", UserIp);

			// 결제 페이지 URL
			model.addAttribute("smilePayURL", msgMgr.message("smartropay.URL", locale));

			model.addAttribute("menuInTime", time);

			StoreOrder orderDao = new StoreOrder(res.getId(), orderNumber, goodsName, String.valueOf(totalindex),
					goodsAmt, device);
			orderDao.setOrderTable(table);
			storeOrderService.saveOrder(orderDao, payitemList);

			// 장바구니 정보에 만들어진 주문 정보 저장
			basketOne.setOrderNumber(orderNumber);
			basketOne.setSavingType(res.getStoreEtc().getSavingType());
			basketService.saveOrUpdate(basketOne);
			model.addAttribute("refiTel", basketOne.getRefiTel());

			msgMgr.addViewMessages(model, locale, new Message[] { new Message("mainAction", "kicc.mainAction"),
					new Message("mainScript", "kicc.mainScript") });

			// 서비스 기간 
	    	Date date = new Date ();
	    	int day = Util.parseInt(msgMgr.message("kicc.productExpr", locale), 1);
	    	Date addDay = Util.addDays(date, day);
	    	String end_date = Util.toSimpleString(addDay, "yyyyMMdd");
	    	model.addAttribute("sp_product_expr", end_date);		//서비스기간 : YYYYMMDD
	    	model.addAttribute("sp_vacct_end_time", Util.parseString(msgMgr.message("kicc.endTime", locale),"235959"));		//입금 만료 시간
	        Date endDate = null;
	        if (!res.isOpenHour_24()) {
	            endDate = new Date(res.getEndTime().getTime());
	            GregorianCalendar calendar = new GregorianCalendar();
	            calendar.setTime(endDate);
	            int endampm = calendar.get(9);
	            int endhour = calendar.get(10);
	            int endmin = calendar.get(12);
	            calendar.setTime(new Date());
	            StoreOpt storeOpt = res.getStoreOpt();
	            if (storeOpt.isNextDayClose())
	              calendar.add(5, 1); 
	            calendar.set(9, endampm);
	            calendar.set(10, endhour);
	            calendar.set(12, endmin);
	            calendar.set(13, 0);
	            calendar.set(14, 0);
	            endDate = calendar.getTime();
	          } 
	        Date possiblDate = null;
	        if (res.getStoreEtc() != null) {
	          StoreEtc storeEtc = res.getStoreEtc();
	          if ("P".equals(storeEtc.getOderPossiblCheck())) {
	            Date possibleDate = new Date(storeEtc.getOder_possible_Time().getTime());
	            GregorianCalendar calendarPu = new GregorianCalendar();
	            calendarPu.setTime(possibleDate);
	            calendarPu.add(12, storeEtc.getOder_setting_Time());
	            possiblDate = calendarPu.getTime();
	          } 
	        } 
	        List<StoreTime> timeList = new ArrayList<>();
	        if (res.getStoreOpt() != null && !"".equals(res.getStoreOpt().getRsvpTime()))
	          timeList = timeList(res.getStoreOpt().getRsvpTime(), possiblDate, endDate); 
	        model.addAttribute("timeList", timeList);
	        Boolean charYN = Boolean.valueOf(false);
	        if (timeList.size() > 0)
	          for (StoreTime one : timeList) {
	            if (one.isClickTF())
	              charYN = Boolean.valueOf(true); 
	          }  
	        model.addAttribute("charYN", charYN);
	        if (possiblDate != null) {
	          model.addAttribute("possiblDate", Long.valueOf(possiblDate.getTime()));
	        } else {
	          model.addAttribute("possiblDate", Integer.valueOf(0));
	        } 
	        model.addAttribute("possiblText", calendarTime(possiblDate));
	        if (endDate != null) {
	          model.addAttribute("endDate", Long.valueOf(endDate.getTime()));
	        } else {
	          model.addAttribute("endDate", Integer.valueOf(0));
	        } 
	        model.addAttribute("endDateText", calendarTime(endDate));
	        model.addAttribute("date", Util.toSimpleString(date, "yyyyMMdd"));		
	        


		} catch (Exception e) {
			logger.error("smilepay > Exception > STEP3  storeKey [{}], table [{}]", storeKey, table);
			logger.error("smilepay > Exception > STEP3  basket : [{}], time : [{}]", basket, time);
			logger.error("smilepay > Exception > STEP3  storeKey : [{}], code : [{}]", storeKey, "M999");
			logger.error("Menu STEP3 error[{}]", e);
			request.setAttribute("storeKey", storeKey);
			request.setAttribute("table", table);
			request.setAttribute("code", "M999");
			request.setAttribute("gubun", "E");

			return "forward:/stopViewUrl";
		}
		System.err.println(jspURL);
		return jspURL;
	}


	/**
	 * 결제결과전송URL	
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/returnMobilePay", method = { RequestMethod.GET, RequestMethod.POST })
	public String returnMobilePay(Model model, HttpServletRequest request, Locale locale, HttpSession session,
			String TrAuthKey, String Tid, String callUrl) {

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("html_lang", Util.htmlLang(locale)),

				new Message("pay_orderNum", "smilepay.orderNum"), new Message("pay_stay", "smilepay.stay"),
				new Message("pay_firstPage", "smilepay.firstPage"),

				new Message("pay_msg_firstPage", "smilepay.msg.firstPage"),
				new Message("pay_msg_orderTable", "smilepay.msg.orderTable"),
				new Message("pay_msg_notSupportBrowser2", "smilepay.msg.notSupportBrowser2"),
				new Message("pay_msg_paySuccess", "smilepay.msg.paySuccess"),

				new Message("confirm_ok", "confirm.ok"), new Message("confirm_cancel", "confirm.cancel") });

		try {
			request.setCharacterEncoding("utf-8");

			String storeKey = (String) request.getParameter("store");
			String basket = (String) request.getParameter("basket");

			logger.info("/returnMobilePay >>> storeKey >>> [{}]", storeKey);
			logger.info("/returnMobilePay >>> basket >>> [{}]", basket);
			
			// String PayMethod = (String)request.getParameter("PayMethod"); //지불수단
			// String Mid = (String)request.getParameter("Mid");//상점 아이디
			// String Amt = (String)request.getParameter("Amt");
			// String BuyerName = (String)request.getParameter("BuyerName");
			// String GoodsCnt = (String)request.getParameter("GoodsCnt");
			// String GoodsName = (String)request.getParameter("GoodsName");
			// String MallUserId = (String)request.getParameter("MallUserId"); //고객사 회원 ID

			String OID = (String) request.getParameter("OID"); // 주문키
			// String Moid = (String)request.getParameter("Moid"); // 주문키
			// String AuthDate = (String)request.getParameter("AuthDate"); // 승인일자
			// String AuthCode = (String)request.getParameter("AuthCode"); // 승인번호
			// String ResultCode = (String)request.getParameter("ResultCode"); // 결과코드
			// String ResultMsg = (String)request.getParameter("ResultMsg"); // 결과메시지

			// String BuyerTel = (String)request.getParameter("BuyerTel");
			// String BuyerEmail = (String)request.getParameter("BuyerEmail");
			// String SignValue = (String)request.getParameter("SignValue"); // 위변조 사인값

			// 결제 요청 결과 파라미터[신용카드]
			String FnCd = (String) request.getParameter("FnCd");
			String FnName = (String) request.getParameter("FnName");// 결제카드사명
			String CardQuota = (String) request.getParameter("CardQuota");
			String AcquCardCode = (String) request.getParameter("AcquCardCode"); // 매입사코드
			String VbankExpDate = (String) request.getParameter("VbankExpDate"); // 매입사코드
			String GoodsCl = (String) request.getParameter("GoodsCl"); // 매입사코드

			// 결제 요청 결과 파라미터[계좌이체, 가상계좌]
			String ReceiptType = (String) request.getParameter("ReceiptType");// 현금영수증유형

			// 결제 요청 결과 파라미터[가상계좌]
			String VbankNum = (String) request.getParameter("VbankNum");// 가상계좌번호
			String VbankName = (String) request.getParameter("VbankName"); // 가상계좌은행명

			Tid = (String) request.getParameter("Tid"); // 거래번호
			TrAuthKey = request.getParameter("TrAuthKey") == null ? "" : request.getParameter("TrAuthKey");
			String DivideInfo = (String) request.getParameter("DivideInfo"); // 서브몰 정보

			// StringBuilder responseBody = null;
			// HashMap<String, Object> result = new HashMap<>();

			// http urlCall 승인 요청 및 TrAuthKey 유효성 검증
			int connectTimeout = 1000;
			int readTimeout = 5000; // 가맹점에 맞게 TimeOut 조절

			URL url = null;
			HttpsURLConnection connection = null;

			try {
				SSLContext sslCtx = SSLContext.getInstance("TLSv1.2");
				sslCtx.init(null, null, new SecureRandom());

				url = new URL(msgMgr.message("smartropay.RE", locale));
				logger.info(" url " + url.toString());
				connection = (HttpsURLConnection) url.openConnection();
				connection.setSSLSocketFactory(sslCtx.getSocketFactory());

				connection.addRequestProperty("Content-Type", "application/json");
				connection.addRequestProperty("Accept", "application/json");
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setConnectTimeout(connectTimeout);
				connection.setReadTimeout(readTimeout);

				OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(connection.getOutputStream()),
						"utf-8");

				JSONObject body = new JSONObject();
				body.put("Tid", Tid);
				body.put("TrAuthKey", TrAuthKey);

				char[] bytes = body.toString().toCharArray();
				osw.write(bytes, 0, bytes.length);
				osw.flush();
				osw.close();

				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
				String line = null;
				String contents = "";
				// responseBody = new StringBuilder();

				line = br.readLine();
				contents += line;
				// logger.info(" 테스트라인 " + line);

				if (Util.isValid(line)) {
					String CoNm = "", PayType = "", ResultMsg = "", ResultCode = "", AuthDate = "", AuthCode = "",
							GoodsName = "", GoodsCnt = "", Amt = "", Moid = "", Mid = "", MallUserId = "",
							BuyerName = "", PayMethod = "", BuyerEmail = "", ParentEmail = "", SubId = "",
							MallReserved = "", EdiDate = "", SignValue = "", BuyerAuthNum = "", BuyerTel = "";
					logger.info(line);
					try {
						JSONParser parser = new JSONParser();
						JSONObject object = (JSONObject) parser.parse(line);
						CoNm = (String) object.get("CoNm");
						PayType = (String) object.get("PayType");
						ResultCode = (String) object.get("ResultCode");
						AuthCode = (String) object.get("AuthCode");
						AuthDate = (String) object.get("AuthDate");
						GoodsName = (String) object.get("GoodsName");
						GoodsCnt = (String) object.get("GoodsCnt");
						Amt = (String) object.get("Amt");
						Moid = (String) object.get("Moid");
						Mid = (String) object.get("Mid");
						MallUserId = (String) object.get("MallUserId");
						BuyerName = (String) object.get("BuyerName");
						PayMethod = (String) object.get("PayMethod");
						BuyerEmail = (String) object.get("BuyerEmail");
						ParentEmail = (String) object.get("ParentEmail");
						SubId = (String) object.get("SubId");
						MallReserved = (String) object.get("MallReserved");
						EdiDate = (String) object.get("EdiDate");
						SignValue = (String) object.get("SignValue");
						BuyerAuthNum = (String) object.get("BuyerAuthNum");
						BuyerTel = (String) object.get("BuyerTel");
						ResultMsg = (String) object.get("ResultMsg");

						logger.info("승진 파싱CoNm=" + CoNm);
						logger.info("승진 파싱PayType=" + PayType);
						logger.info("승진 파싱ResultCode=" + ResultCode);
						logger.info("승진 파싱AuthCode=" + AuthCode);
						logger.info("승진 파싱AuthDate=" + AuthDate);
						logger.info("승진 파싱GoodsName=" + GoodsName);
						logger.info("승진 파싱GoodsCnt=" + GoodsCnt);
						logger.info("승진 파싱Amt=" + Amt);
						logger.info("승진 파싱Moid=" + Moid);
						logger.info("승진 파싱Mid=" + Mid);
						logger.info("승진 파싱MallUserId=" + MallUserId);
						logger.info("승진 파싱BuyerName=" + BuyerName);
						logger.info("승진 파싱PayMethod=" + PayMethod);
						logger.info("승진 파싱BuyerEmail=" + BuyerEmail);
						logger.info("승진 파싱ParentEmail=" + ParentEmail);
						logger.info("승진 파싱SubId=" + SubId);
						logger.info("승진 파싱MallReserved=" + MallReserved);
						logger.info("승진 파싱EdiDate=" + EdiDate);
						logger.info("승진 파싱SignValue=" + SignValue);
						logger.info("승진 파싱BuyerAuthNum=" + BuyerAuthNum);
						logger.info("승진 파싱BuyerTel=" + BuyerTel);
						logger.info("승진 파싱ResultMsg=" + ResultMsg);

						// 결제 결과
						// result = new ObjectMapper().readValue(responseBody.toString(),
						// HashMap.class);
						// logger.info(result.toString());

						DivideInfo = "{'Amt':" + Amt + "," + "'Mid':" + Mid + "," + "'GoodsName':" + GoodsName + "}";

						logger.info("DivideInfo 값 : " + DivideInfo);

						if (!StringUtils.isEmpty(DivideInfo)) {
							if ("%".contains(DivideInfo)) {
								DivideInfo = URLDecoder.decode(DivideInfo, "utf-8");
							}

							// logger.info("인코딩 테스트");
							byte[] byteDivideInfo = Base64.decodeBase64(DivideInfo.getBytes());
							DivideInfo = new String(byteDivideInfo, "utf-8");

							// logger.info("인코딩 테스트2");
							logger.info("Moid 값? " + Moid);
							// String[] oidStore = OID.split(Mid);

							// String[] oidStore = Moid.split(Mid);
							logger.info("Mid 값" + Mid);

							// String Moid2 = Moid_b[1].replace(Mid_b[1],"*");
							String[] oidStore = Moid.split(Mid);

							// String[] oidStore = Moid_b[1].split("*");

							logger.info("0번방" + oidStore);
							logger.info("1번방" + oidStore);

							String oidStoreId = oidStore[1];
							// String stroidStore = oidStore[0];

							logger.info("[모바일 결제 완료]returnMobilePay oidStore >>> [{}]", oidStore);
							logger.info("[모바일 결제 완료]returnMobilePay oidStoreId >>> [{}]", oidStoreId);

							// 여기까지됨

							Store res = storeService.getStoreByStoreKey(storeKey);
							String savingtype = res.getStoreEtc().getSavingType();
							logger.info(savingtype);
							String merchantKey = res.getStoreEtc().getSpAuthKey();
							String VerifySignValue = encodeSHA256Base64(Tid.substring(0, 10) + ResultCode
									+ Tid.substring(10, 15) + merchantKey + Tid.substring(15, Tid.length()));

							logger.info("[모바일 결제 완료]returnMobilePay merchantKey >>> [{}]", merchantKey);
							logger.info("[모바일 결제 완료]returnMobilePay VerifySignValue >>> [{}]", VerifySignValue);

							String orderSequence = "";
							// StoreOrder orderRes = storeOrderService.getOrder(res.getId(),OID);
							StoreOrder orderRes = storeOrderService.getOrder(res.getId(), Moid);

							if (orderRes != null) {

								logger.info("===오더알이에스 " + orderRes);

								logger.info("===오더시퀀스  ", orderSequence);
								logger.info("===겟아이디()  ", res.getId());

								logger.info("[모바일 결제 완료]매장 번호와 주문 번호로 조회");
								logger.info("[모바일 결제 완료]OID > [{}]", Moid);
								logger.info("[모바일 결제 완료]returnMobilePay orderRes.getOrderPayId() >>> [{}]",
										orderRes.getOrderPayId());

								if (orderRes.getOrderPayId() > 0) {
									orderSequence = orderRes.getOrderSeq();
									logger.info("시퀀스", orderSequence);
									logger.info("하이", orderRes.getOrderPayId());

								} else {
									StoreOrderPay orderPay = new StoreOrderPay();
									// orderSequence = storeOrderService.saveOrderPay(orderRes, orderPay, locale,
									// session);
									logger.info("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");

									orderPay.setStoreId(res.getId());
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setOrderNumber(Moid);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setOrderTid(Tid);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setPayMethod(PayMethod);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setPayMid(Mid);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setPayAmt(Amt);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setGoodsname(GoodsName);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setPayOid(Moid);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setPayAuthDate(AuthDate);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setPayAuthCode(AuthCode);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setPayResultCode(ResultCode);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setPayResultMsg(ResultMsg);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setPaySignValue(SignValue);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									orderPay.setPayDivideInfo(DivideInfo);
									orderPay.touchWhoC(Moid);
									logger.info("윤영 ) orderSequence " + orderSequence);
									logger.info("윤영 ) VerifySignValue " + VerifySignValue);
									String a = orderPay.getGoodsname();
									logger.info(a);
									StoreOrderBasket basketOne = basketService.getBasketKey(basket);
									orderSequence = storeOrderService.saveOrderPay(orderRes, orderPay, locale, session);
									StoreOrder storeOrderOne = storeOrderService.getDplyStoreOrderPrint(orderRes.getId());
									storeOrderOne.setOrderParent(-1);
									storeOrderOne.setDiscountAmt(basketOne.getBasketDiscount());
									storeOrderOne.setPaymentAmt(basketOne.getBasketPayment());
									storeOrderOne.setSavingType(basketOne.getSavingType());
									storeOrderOne.setUseCoupon(basketOne.getCouponId());
									storeOrderService.saveOrUpdate(storeOrderOne);
									String b = basketOne.getSavingType();
									logger.info(b);
						              if ("CP".equals(basketOne.getSavingType())) {
						                  StoreOrderCoupon coupon = couponService.getCoupon(basketOne.getCouponId());
						                  if (coupon != null) {
						                    coupon.setUseState(1);
						                    this.couponService.saveOrUpdate(coupon);
						                  } 
						                } else if ("PO".equals(basketOne.getSavingType())) {
						                  StoreOrderPoint savePoint = null;
						                  List<StoreOrderPoint> resList = couponService.getPointbyTel(res.getId(), storeOrderOne.getTelNumber());
						                  logger.info("resList.size() [{}]", Integer.valueOf(resList.size()));
						                  if (resList.size() > 0) {
						                    for (StoreOrderPoint one : resList) {
						                      if (basketOne.getBasketDiscount() > 0) {
						                        one.setPointCnt(-basketOne.getBasketDiscount());
						                        one.setPointTotal(one.getPointTotal() - basketOne.getBasketDiscount());
						                      } else {
						                        one.setPointCnt(basketOne.getBasketDiscount());
						                        one.setPointTotal(one.getPointTotal());
						                      } 
						                      savePoint = one;
						                    } 
						                    this.couponService.saveOrUpdate(savePoint);
						                  } 
						                } 
						              
						              
										// 1. 매장에서 쿠폰 / 포인트 / 사용안함 인지 판단
										
										// 2. 쿠폰
										// 2-1. 스탬프 정책을 가져온다
										// 2-2. 정책에 맞는 스탬프를 적립
										// 2-3. 쿠폰 정책을 가져온다. 
										// 2-4. 적립된 스탬프의 수가 쿠폰 발급 정책에 해당 될 경우 쿠폰 이 발급된다. 
										// 2-5. 만약 쿠폰이 발급 된 경우 발급정책의 스탬프 수를 제외한 나머지 스탬프를 적립한다.
										// 2-5-1. 쿠폰이 사용 되었을 경우 고객 전화번호로 검색하여 사용 표시 
										// 2-6. 누적스탬프 / 스탬프 적립 / 발급 쿠폰 ID / 사용적립여부 / 매장 ID / 고객전화번호 / 발급 기간 / 사용가능마지막 날짜(당일 11시59분59초 기준)LOG로 남긴다. 
										
										// 3. 포인트
										// 3-1. 포인트 정책 을 가져온다. 
										// 3-2. 정책에 맞는 포인트를 적립한다. 
										// 3-3. 누적 포인트 / 적립 포인트(사용 포인트) / 매장 ID / 고객 전화번호 / 적립 기간 LOG로 남긴다.
										
										// NO= 사용안함, CP= 쿠폰, PO= 포인트 
										String savingType = basketOne.getSavingType();
										
										model.addAttribute("savingType", savingType);
										
										if(!"NO".equals(savingType)){
											orderRes = storeOrderService.getOrderbyOrderNum(Moid);
											String tel = orderRes.getTelNumber();
											int paymentAmt = orderRes.getPaymentAmt();
											
											if(Util.isValid(tel)){
												if("CP".equals(savingType)){
													
													stampCouponUpdate(res, orderRes, Moid, session, model, locale);
													
												}else if("PO".equals(savingType)){
													// 포인트 적립시 사용
													List<StorePolicy> policyList = couponService.getPolicyList(res.getId(), "P");
													double percentage = 0;
													int point = 0;
													int usepoint = 0;
													if(policyList.size() > 0){
														for(StorePolicy one : policyList){
															percentage = one.getPercentage();	// 적립 포인트
															usepoint = one.getPoint();			// 1사용시 1원당 사용되는 포인트
														}
														
														BigDecimal bicTotalAmt = new BigDecimal(paymentAmt);
														BigDecimal bicPercentage = new BigDecimal(percentage);
														BigDecimal bicPoint = bicTotalAmt.multiply(bicPercentage);
														point = bicPoint.intValue();
														// 적립가능한 금액이 나왔을 때 1원당 사용 가능한 포인트로 계산하여 변환한다. 
														if(usepoint > 0){
															point = point / usepoint;
														}
													}
													
													StoreOrderPoint savePoint = null; 
													List<StoreOrderPoint> resList = couponService.getPointbyTel(res.getId(), tel);
													if(resList.size() > 0){
														for(StoreOrderPoint one : resList){
															one.setPointCnt(point);
															one.setPointTotal(one.getPointTotal() + point);
															
															savePoint = one;
														}
													}else{
														savePoint = new StoreOrderPoint(tel, point, point, res, session);
													}
													
													couponService.saveOrUpdate(savePoint);
													
													// 화면에 보여줘야 하는 내용
													
													DecimalFormat format = new DecimalFormat("###,###");
													String pointFormat = format.format(savePoint.getPointCnt());
													String pointTotalFormat = format.format(savePoint.getPointTotal());
													model.addAttribute("point", pointFormat);			//적립 포인트
													model.addAttribute("pointTotal", pointTotalFormat);	//총 포인트
													
													
													alimTalkSend(res, orderRes, "PO", tel, pointFormat, pointTotalFormat, locale);
												}
											}
										}
										
										// 주문번호도 같이 보내야 함
										// 결제가 성공했을 경우 성공 화면으로 이동 하여 "order/returnMobilePay"페이지 이동 
										if(basket != null){
											basketService.delete(basketOne);
										}

									if (SignValue.equals(VerifySignValue)) {
										logger.info("SignValue.equals(VerifySignValue) >>> [{}]", "검증 성공");
									} else {
										logger.error("error PayMethod >>> [{}]", PayMethod);
										logger.error("error Mid >>> [{}]", Mid);
										logger.error("error Amt >>> [{}]", Amt);
										logger.error("error BuyerName >>> [{}]", BuyerName);
										logger.error("error GoodsName >>> [{}]", GoodsName);
										logger.error("error MallUserId 고객사 회원 ID >>> [{}]", MallUserId);
										logger.error("error Tid 거래번호 >>> [{}]", Tid);
										logger.error("error OID 주문번호 >>> [{}]", Moid);
										logger.error("error AuthDate 승인일자 >>> [{}]", AuthDate);
										logger.error("error AuthCode 승인번호 >>> [{}]", AuthCode);
										logger.error("error ResultCode 결과코드 >>> [{}]", ResultCode);
										logger.error("error ResultMsg 결과메시지 >>> [{}]", ResultMsg);
										logger.error("error BuyerTel >>> [{}]", BuyerTel);
										logger.error("error BuyerEmail >>> [{}]", BuyerEmail);
										logger.error("error SignValue >>> [{}]", SignValue);
										logger.error("error FnCd >>> [{}]", FnCd);
										logger.error("error FnName 결제카드사명 >>> [{}]", FnName);
										logger.error("error CardQuota >>> [{}]", CardQuota);
										logger.error("error AcquCardCode 매입사코드  >>> [{}]", AcquCardCode);
										logger.error("error ReceiptType 현금영수증유형  >>> [{}]", ReceiptType);
										logger.error("error VbankNum 가상계좌번호  >>> [{}]", VbankNum);
										logger.error("error VbankName 가상계좌은행명  >>> [{}]", VbankName);
										logger.error("error DivideInfo >>> [{}]", DivideInfo);
										logger.error("error merchantKey >>> [{}]", merchantKey);
										logger.error("error VerifySignValue >>> [{}]", VerifySignValue);

										logger.error(
												"returnMobilePay > STEP4  storeKey [{}], table(orderRes.getOrderTable()) [{}]",
												res.getStoreKey(), orderRes.getOrderTable());
										logger.error("returnMobilePay > STEP4  basket : [{}]", basket);
										logger.error("returnMobilePay > STEP4  OID [{}], Tid [{}]", Moid, Tid);
										request.setAttribute("storeKey", res.getStoreKey());
										request.setAttribute("table", orderRes.getOrderTable());
										request.setAttribute("code", "M005");
										request.setAttribute("gubun", "B");
										request.setAttribute("info", Moid);

										return "forward:/stopViewUrl";
									}
									// orderSequence = storeOrderService.saveOrderPay(orderRes, orderPay, locale,
									// session);

									logger.info("[모바일 결제 완료]returnMobilePay orderSequence >>> [{}]", orderSequence);
									logger.info("[모바일 결제 완료]returnMobilePay ResultCode >>> [{}]", ResultCode);

									if (!"3001".equals(ResultCode)) {
										// 에러에 대한 코드 값을 저장한 후이기 때문에 프린트에 내려가지 않도록 StoreOrder 정보의 프린트 Y / N을 E로 변경 한다.
										StoreOrder condFile = storeOrderService
												.getDplyStoreOrderPrint(orderRes.getId());
										condFile.setOrderPrint("E");
										condFile.touchWho(session);
										storeOrderService.saveOrUpdate(condFile);
										logger.error("returnMobilePay > STEP4 FAIL : [{}]",
												"//3001(카드 결제 성공)이 아닐 경우 에러 메시지 출력");
										logger.error("[모바일 결제 완료]error 거래번호 Tid >>> [{}] 결제 주문 번호  OID [{}]", Tid,
												Moid);
										logger.error(
												"[모바일 결제 완료]error 결제 에러 코드 ResultCode  >>> [{}], 결제 에러 메시지 ResultMsg >>> [{}]",
												ResultCode, ResultMsg);
										logger.error("returnMobilePay > STEP4 FAIL : [{}]", "M006");
										request.setAttribute("code", "M006");
										request.setAttribute("gubun", "C");
										request.setAttribute("resultCode", ResultCode);
										request.setAttribute("resultMsg", ResultMsg);
										request.setAttribute("info", Moid);
										logger.info("[모바일 결제 완료]OID > [{}] returnMobilePay Amt >>> [{}]", Moid, Amt);
										logger.info("[모바일 결제 완료]OID > [{}] returnMobilePay orderRes.getGoodsAmt() >>> [{}]",
												Moid, orderRes.getGoodsAmt());
										
										return "forward:/stopViewUrl";
									}
									// "실제 스마트로 서버의 승인 값을 검증 하기 위해서 값 " 끝나면 결제된 내용의 결제 금액과 저장되어 있는 총 결제 금액이 같은지
									// 비교(스마트로 권장사항)
									int replateAmt = Integer.parseInt(Amt.replace("원", ""));
									logger.info("[모바일 결제 완료]OID > [{}] returnMobilePay Amt >>> [{}]", Moid, Amt);
									logger.info("[모바일 결제 완료]OID > [{}] returnMobilePay replateAmt >>> [{}]", Moid,
											replateAmt);
									logger.info("[모바일 결제 완료]OID > [{}] returnMobilePay orderRes.getGoodsAmt() >>> [{}]",
											Moid, orderRes.getGoodsAmt());

									if (orderRes.getPaymentAmt() != replateAmt) {
										// 에러에 대한 코드 값을 저장한 후이기 때문에 프린트에 내려가지 않도록 StoreOrder 정보의 프린트 Y / N을 E로 변경 한다.
										StoreOrder condFile = storeOrderService
												.getDplyStoreOrderPrint(orderRes.getId());
										condFile.setOrderPrint("H");
										condFile.touchWho(session);
										storeOrderService.saveOrUpdate(condFile);

										logger.info("결제된 내용의 결제 금액과 저장되어 있는 총 결제 금액이 같은지 비교");
										logger.info("error 거래번호 Tid >>> [{}]", Tid);
										logger.info("error 결제 주문 번호  OID >>> [{}]", Moid);
										logger.info("error 결제 하기 전 총 금액 orderRes.getGoodsAmt() >>> [{}]",
												orderRes.getGoodsAmt());
										logger.info("error 결제 받은 총 금액 Amt >>> [{}]", replateAmt);

										logger.info("returnMobilePay > payError > M007 ");
										request.setAttribute("code", "M007");
										request.setAttribute("gubun", "B");
										request.setAttribute("info", Moid);

										return "forward:/stopViewUrl";
									}
									// 4. 저장된 결제 내역에 대한 주방용 메뉴 리스트 작성 : STORE_ORDER_COOK TABLE 저장
									// 4-1. 3001(카드 결제 성공)이 아닐 경우 주방용 메뉴 리스트에 넣지 않는다. - 위에서 에러
									// 4-2. "실제 스마트로 서버의 승인 값을 검증 하기 위해서 값 " 끝나면 결제된 내용의 결제 금액과 저장되어 있는 총 결제 금액이 같은지
									// 비교(스마트로 권장사항)
									// 금액이 같지 않을 경우 주방용 메뉴 리스트에 넣지 않는다. - 위에서 에러
									// FCM 을 보낼때 주방용 패드를 체크하여 없을 경우 해당 StoreOrderCook에 데이터를 넣지 않는다.

									StoreOrderCook storeOrderCook = new StoreOrderCook(orderRes.getStoreId(),
											orderRes.getId(), orderRes.getOrderNumber());
									logger.info("[모바일 결제 완료]FCM 보내기 전>>> [{}]", OID);
									// FCM 전송 : 모바일 결제완료가 되어 DB에 저장 되었을 경우 fcm으로 stb 에 알려준다.
									storeOrderService.fcmTransmission(res.getId(), storeOrderCook);
									logger.info("[모바일 결제 완료]FCM 보내기 후>>> [{}]", OID);
									
																		
								}
							}

							// 주방용 패드가 있을 경우에 대한 대기번호 순수
							// 없을 경우 9999 반환

							model.addAttribute("stayMenuCnt", storeCookService.getStayCntMobile(res.getId()));

							// 매장 화면 구성 데이터
							model.addAttribute("storeId", res.getId());
							;

							model.addAttribute("storeName", res.getStoreName());

							// 모바일 상점 로그 가져오기
							imgCommon(res, model);

							model.addAttribute("storeIntroduction", "");

							model.addAttribute("url", "/menu");
							model.addAttribute("oriUrlparam", res.getStoreKey());
							model.addAttribute("oriUrlparam", storeKey);
							model.addAttribute("orderTable", Util.parseString(orderRes.getOrderTable(), "0000"));
							model.addAttribute("orderSequence", orderSequence);

							// 모바일 결제 승인 일자가 181126144622 으로 넘어옴
							Calendar calendar = new GregorianCalendar(Locale.KOREA);
							int nYear = calendar.get(Calendar.YEAR);
							String nYearSt = String.valueOf(nYear);
							model.addAttribute("authDate", nYearSt.substring(0, 2) + AuthDate);
							model.addAttribute("GoodsName", GoodsName);

							StoreOrderBasket basketOne = basketService.getBasketKey(basket);
							if (basketOne != null) {
								basketService.delete(basketOne);
							}
						}
						


						return "order/returnMobilePay";

					} catch (Exception ex) {
						logger.error("mediacast-rpt - json", ex);
					}

				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (UnsupportedEncodingException e) {
			logger.info("returnMobilePay Exception > [{}]", e);
			logger.info("returnMobilePay UnsupportedEncodingException > payError > M006 ");
			request.setAttribute("code", "M006");
			request.setAttribute("gubun", "A");

			return "forward:/stopViewUrl";
		} catch (Exception e) {
			logger.info("returnMobilePay Exception > [{}]", e);
			logger.info("returnMobilePay Exception > payError > M006 ");
			request.setAttribute("code", "M006");
			request.setAttribute("gubun", "A");

			return "forward:/stopViewUrl";
		}
		return "order/returnMobilePay";
	}

	/**
	 * 결제결과전송URL
	 */
	@RequestMapping(value = "/informMobile", method = { RequestMethod.GET, RequestMethod.POST })
	public void informMobile(Model model, HttpServletRequest request, Locale locale, HttpSession session) {

		try {
			request.setCharacterEncoding("utf-8");

			String storeKey = (String) request.getParameter("store");
			if (Util.isNotValid(storeKey)) {
				logger.error("stopUrl > payError > M003 ");
				logger.error("payError", "/informMobile >>> store정보를 찾을수 없습니다. ");
				throw new ServerOperationForbiddenException("SaveError");
			}

			String PayMethod = (String) request.getParameter("PayMethod");
			String Mid = (String) request.getParameter("Mid");
			String MallUserId = (String) request.getParameter("MallUserId"); // 고객사 회원 ID
			String Amt = (String) request.getParameter("Amt");
			String name = (String) request.getParameter("name"); //
			String GoodsName = (String) request.getParameter("GoodsName");
			String Tid = (String) request.getParameter("Tid"); // 거래번호
			String OID = (String) request.getParameter("OID"); // 주문번호
			String AuthDate = (String) request.getParameter("AuthDate"); // 승인일자
			String AuthCode = (String) request.getParameter("AuthCode"); // 승인번호
			String ResultCode = (String) request.getParameter("ResultCode"); // 결과코드
			String ResultMsg = (String) request.getParameter("ResultMsg"); // 결과메시지
			String state_cd = (String) request.getParameter("state_cd"); // 결과메시지
			String FnCd = (String) request.getParameter("FnCd");
			String FnName = (String) request.getParameter("FnName");// 결제카드사명
			String CardQuota = (String) request.getParameter("CardQuota");
			String BuyerEmail = (String) request.getParameter("BuyerEmail");
			String BuyerAuthNum = (String) request.getParameter("BuyerAuthNum");
			String VbankNum = (String) request.getParameter("VbankNum");// 가상계좌번호
			String VbankName = (String) request.getParameter("VbankName"); // 가상계좌은행명
			String ReceiptType = (String) request.getParameter("ReceiptType");// 현금영수증유형
			String RcptAppNo = (String) request.getParameter("RcptAppNo");
			String RcptCcNo = (String) request.getParameter("RcptCcNo");

//			logger.info("informMobile > PayMethod >>> [{}]", PayMethod);
//			logger.info("informMobile > Mid >>> [{}]", Mid);
//			logger.info("informMobile > Amt >>> [{}]", Amt);
//			logger.info("informMobile > GoodsName >>> [{}]", GoodsName);
//			logger.info("informMobile > MallUserId 고객사 회원 ID >>> [{}]", MallUserId);
//			logger.info("informMobile > Tid 거래번호 >>> [{}]", Tid);
//			logger.info("informMobile > OID 주문번호 >>> [{}]", OID);
//			logger.info("informMobile > AuthDate 승인일자 >>> [{}]", AuthDate);
//			logger.info("informMobile > AuthCode 승인번호 >>> [{}]", AuthCode);
//			logger.info("informMobile > ResultCode 결과코드 >>> [{}]", ResultCode);
//			logger.info("informMobile > ResultMsg 결과메시지 >>> [{}]", ResultMsg);
//			logger.info("informMobile > BuyerEmail >>> [{}]", BuyerEmail);
//			logger.info("informMobile > FnCd >>> [{}]", FnCd);
//			logger.info("informMobile > FnName 결제카드사명 >>> [{}]", FnName);
//			logger.info("informMobile > CardQuota >>> [{}]", CardQuota);
//			logger.info("informMobile > ReceiptType 현금영수증유형  >>> [{}]", ReceiptType);
//			logger.info("informMobile > VbankNum 가상계좌번호  >>> [{}]", VbankNum);
//			logger.info("informMobile > VbankName 가상계좌은행명  >>> [{}]", VbankName);
//			logger.info("informMobile > name >>> [{}]", name);
//			logger.info("informMobile > state_cd >>> [{}]", state_cd);
//			logger.info("informMobile > BuyerAuthNum >>> [{}]", BuyerAuthNum);

			Store res = storeService.getStoreByStoreKey(storeKey);

			// 정상 결제 된 내역이 있는 지 확인을 한다.
			// 주문 정보가 있을 경우 해당 내용을 업데이트를 진행한다.
			String msgRes = "";
			if (ResultCode != null) {
				StoreOrder orderRes = storeOrderService.getOrder(res.getId(), OID);
				if ("3001".equals(ResultCode)) { // CARD
					// 결제 성공시 DB처리 하세요.
					// Tid 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT
					msgRes = "[3001]CARD > Tid 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT";

					logger.info("/informMobile >>> orderRes >>> [{}]", orderRes);
					if (orderRes != null && orderRes.getOrderPayId() > 0) {
						logger.info("/informMobile >>> orderRes.getOrderPayId() >>> [{}]", orderRes.getOrderPayId());
						StoreOrderPay orderPay = storePayService.getOrderPay(orderRes.getOrderPayId());
						logger.info("/informMobile >>> orderPay.getOrderNumber() >>> [{}] / OID >>> [{}]",
								orderPay.getOrderNumber(), OID);
						if (orderPay != null && orderPay.getOrderNumber().equals(OID)) {
							orderPay.setStoreId(res.getId());
							orderPay.setOrderNumber(OID);
							orderPay.setOrderTid(Tid);
							orderPay.setPayMethod(PayMethod);
							orderPay.setPayMid(Mid);
							orderPay.setPayAmt(Amt);
							orderPay.setGoodsname(GoodsName);
							orderPay.setPayOid(OID);
							orderPay.setPayAuthDate(AuthDate);
							orderPay.setPayAuthCode(AuthCode);
							orderPay.setPayResultCode(ResultCode);
							orderPay.setPayResultMsg(ResultMsg);
							orderPay.setPayFnCd(FnCd);
							orderPay.setPayFnName(FnName);
							orderPay.setPayCardQuota(CardQuota);
							orderPay.touchWhoC(OID);

							storePayService.saveOrUpdate(orderPay);
						}
					} else {
						logger.info("/informMobile >>> OID >>> [{}]주문 번호로 결제된 내역이 없습니다.", OID);

						StoreOrderPay orderPay = new StoreOrderPay();

						orderPay.setStoreId(res.getId());
						orderPay.setOrderNumber(OID);
						orderPay.setOrderTid(Tid);
						orderPay.setPayMethod(PayMethod);
						orderPay.setPayMid(Mid);
						orderPay.setPayAmt(Amt);
						orderPay.setGoodsname(GoodsName);
						orderPay.setPayOid(OID);
						orderPay.setPayAuthDate(AuthDate);
						orderPay.setPayAuthCode(AuthCode);
						orderPay.setPayResultCode(ResultCode);
						orderPay.setPayResultMsg(ResultMsg);
						orderPay.setPayFnCd(FnCd);
						orderPay.setPayFnName(FnName);
						orderPay.setPayCardQuota(CardQuota);
						orderPay.touchWhoC(OID);

						storeOrderService.saveOrderPay(orderRes, orderPay, locale, session);
					}
				}
				/*
				 * if("4000".equals(ResultCode)){ //BANK // 결제 성공시 DB처리 하세요. // TID 결제 취소한 데이터
				 * 존재시 UPDATE, 존재하지 않을 경우 INSERT msgRes =
				 * "[4000]BANK > TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT"; }
				 * if("4100".equals(ResultCode)){ //VBANK 체번완료 // 결제 성공시 DB처리 하세요. // TID 결제 취소한
				 * 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT msgRes =
				 * "[4100]VBANK 체번완료 > TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT"; }
				 * if("4110".equals(ResultCode)){ //VBANK 입금완료 // 결제 성공시 DB처리 하세요. // TID 결제 취소한
				 * 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT msgRes =
				 * "[4110]VBANK 입금완료 > TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT"; }
				 * if("A000".equals(ResultCode)){ //cellphone // 결제 성공시 DB처리 하세요. // TID 결제 취소한
				 * 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT msgRes =
				 * "[A000]cellphone > TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT"; }
				 * if("7001".equals(ResultCode)){ //현금영수증 // 결제 성공시 DB처리 하세요. // TID 결제 취소한 데이터
				 * 존재시 UPDATE, 존재하지 않을 경우 INSERT msgRes =
				 * "[7001]현금영수증 > TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT"; }
				 * if("H000".equals(ResultCode)){ //해피머니승인 // 결제 성공시 DB처리 하세요. // TID 결제 취소한 데이터
				 * 존재시 UPDATE, 존재하지 않을 경우 INSERT msgRes =
				 * "[H000]해피머니승인 > TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT"; }
				 * if("T000".equals(ResultCode)){ //T-money 결제/성공 // 결제 성공시 DB처리 하세요. // TID 결제
				 * 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT msgRes =
				 * "[T000]T-money 결제/성공 > TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT"; }
				 * if("T001".equals(ResultCode)){ //T-money 등록/ 성공 // 결제 성공시 DB처리 하세요. // TID 결제
				 * 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT msgRes =
				 * "[T001]T-money 등록/ 성공 > TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT"; }
				 * if("BL00".equals(ResultCode)){ //도서문화상품권(도서문화보급) 승인 // 결제 성공시 DB처리 하세요. //
				 * TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT msgRes =
				 * "[BL00]도서문화상품권(도서문화보급) 승인 > TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT"; }
				 */
				// 결제 취소
				/*
				 * if("2001".equals(ResultCode)){ // 취소 성공시 DB처리 하세요. //TID 결제 취소한 데이터 존재시
				 * UPDATE, 존재하지 않을 경우 INSERT msgRes =
				 * "[2001]도서문화상품권(도서문화보급) 승인 > TID 결제 취소한 데이터 존재시 UPDATE, 존재하지 않을 경우 INSERT"; }
				 * if("2211".equals(ResultCode)){ // 환불 msgRes = "[2211] 환불"; }
				 */

				if ("2013".equals(ResultCode)) {
					// 취소 완료 거래임(기취소)
					msgRes = "[2013] 취소 완료 거래임(기취소)";
				}
			}

		} catch (Exception e) {
			logger.error("payError", e);
			throw new ServerOperationForbiddenException("SaveError");
		}
	}

	/*
	 * public static final String encodeSHA256Base64(String pw) { return new
	 * String(Base64.encodeBase64(DigestUtils.md5Hex(pw).getBytes())); }
	 */
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

	/**
	 * --------------------------- 주문 취소 영역-----------------------------------------------
	 */

	/**
	 * 주문 내역 취소 - 승인 번호 체크
	 */
	@RequestMapping(value = "/menuCancelVerifi", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody StoreCanCelView menuCancelVerifi(@RequestBody Map<String, Object> model,
			HttpServletRequest request, Locale locale, HttpSession session) {
		StoreCanCelView storeCanCelView = null;
		try {
			request.setCharacterEncoding("UTF-8");

			// 1. storeId를 정상 적인지 확인
			// 2. storeId와 verifiCode(승인번호)를 체크 하여 정상적인지 확인한다.
			String table = (String) model.get("cancelTable");
			String storeId = (String) model.get("cancelStoreId");
			String verifiCode = (String) model.get("verifiCode");
			String deviceGubun = (String) model.get("cancelPaygubun");
			
			logger.info("/menuCancelVerifi > storeId >>> [{}], verifiCode >>> [{}]", storeId, verifiCode);
			logger.info("/menuCancelVerifi > table >>> [{}]", table);
			storeCanCelView = storeCancelService.checkVerifiCodebyStoreIdVerifiCode(Integer.parseInt(storeId),
					verifiCode, deviceGubun);
		} catch (Exception e) {
			logger.info("menuCancelVerifi Exception > payError > M006 ");
			request.setAttribute("code", "M008");
			request.setAttribute("gubun", "A");
		}

		return storeCanCelView;
	}

	/**
	 * 주문 내역 취소 - 승인 번호 체크
	 */
	@RequestMapping(value = "/menuCancel", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String menuCancel(@RequestBody Map<String, Object> model, HttpServletRequest request,
			Locale locale, HttpSession session) {
		String SUCCESS_CANCEL = "2001";// 취소 성공 코드
		String success = "true";
		StoreCanCelView storeCanCelView = null;
		try {
			request.setCharacterEncoding("UTF-8");

			// 1. storeId를 정상 적인지 확인
			// 2. storeId와 verifiCode(승인번호)를 체크 하여 정상적인지 확인한다.
			// String table = (String)model.get("cancelTable");
			String storeId = (String) model.get("cancelStoreId");
			String verifiCode = (String) model.get("verifiCode");
			String storeOrderId = (String) model.get("storeOrderId");
			String deviceGubun = (String) model.get("cancelPaygubun");
			// 이전 값과 승인 번호 등을 다시 한번 확인 한다.
			storeCanCelView = storeCancelService.checkVerifiCodebyStoreIdVerifiCode(Integer.parseInt(storeId),
					verifiCode,deviceGubun);
			if (storeCanCelView != null) {
				if (storeOrderId.equals(storeCanCelView.getStoreOrderId())) {
					// easypay 취소일 경우
					
					if ("ME".equals(storeCanCelView.getOrderDevice())) {
						success = easyPayService.easyPayCancel(Util.parseInt(storeId, 0),
								Util.parseInt(storeOrderId, 0), verifiCode, request, locale, session);
					} else {
						Store res = storeService.getStore(Util.parseInt(storeId, 0));

						String partialCode = "0"; // 전체취소 0, 부분취소 1 [전체취소 Default]
						String cancelAmt = "";
						// 취소 비밀번호를 위해서 조회한다.
						// 2019.04.29 비밀번호 저장 로직 변경으로 인하여 조회 되는곳 변경 - 김영종 팀장님 확인해주심
						// 매장 정보에 저장되어 있는 취소 패스워드 가져오기
						// StoreOrderVerification storeOrderVerification1 =
						// storeCancelService.getStoreCancelOne(Util.parseInt(storeCanCelView.getVerificationId()));
						// String cancelPw = storeOrderVerification1.getCancelStoreAuth() ;
						String cancelPw = res.getStoreEtc().getSpCancelCode();
						String cancelMSG = "주문 취소";
						if (Util.isNotValid(cancelPw)) {
							logger.error("menuCancel cancelPw > payError > M008 ");
							return msgMgr.message("smilepay.M008", locale);
						}

						StoreOrder storeOrder = storeOrderService
								.getDplyStoreOrderPrint(Util.parseInt(storeOrderId, 0));
						if (storeOrder == null) {
							logger.error("menuCancel storeOrder null > payError > M008 ");
							return msgMgr.message("smilepay.M008", locale);
						}
						StoreOrderPay storeOrderPay = storePayService.getOrderPay(storeOrder.getOrderPayId());
						if (storeOrderPay == null) {
							logger.error("menuCancel storeOrderPay null > payError > M008 ");
							return msgMgr.message("smilepay.M008", locale);
						}
						// 0일경우에는 전체 취소일 경우
						// 부분 취소는 현재 사용 하지 않는다. 2019.04.16 - 김영종 팀장이 확인해주심

						if ("0".equals(partialCode)) {
							cancelAmt = storeOrderPay.getPayAmt();
						} else {
							// 부분 취소
							int cancelAmtInt = Util.parseInt(cancelAmt, 0);
							if (cancelAmtInt > 0) {
								int payAmtInt = Util.parseInt(storeOrderPay.getPayAmt());

								if (cancelAmtInt > payAmtInt) {
									logger.info("취소금액이 결제금액보다 클수 없지!! cancelAmt[{}]", cancelAmt);
									logger.info("취소금액이 결제금액보다 클수 없지!! payAmtInt[{}]", payAmtInt);
									logger.error("menuCancel (cancelAmtInt > payAmtInt)> payError > M008 ");
									return msgMgr.message("smilepay.M008", locale);
								}
							} else {
								logger.info("부분 취소??? 여기 아닌데... storeId[{}]", storeId);
								logger.info("부분 취소??? 여기 아닌데... verifiCode[{}]", verifiCode);
								logger.info("부분 취소??? 여기 아닌데... cancelAmt[{}]", cancelAmt);
								logger.error("menuCancel (cancelAmtInt > payAmtInt)> payError > M008 ");
								return msgMgr.message("smilepay.M008", locale);
							}
						}

						Hashtable<String, String> result;
						Hashtable<String, String> cancelRequest = new Hashtable<String, String>();

						cancelRequest.put("Tid", storeOrderPay.getOrderTid()); // 1.취소할 거래 Tid [필수]
						cancelRequest.put("CancelAmt", cancelAmt); // 2.취소 금액 [필수]
						cancelRequest.put("Cancelpw", cancelPw); // 3.취소 패스워드 [필수]
						cancelRequest.put("CancelMSG", SmilePayUtil.urlEncodeEuckr(cancelMSG)); // 4.취소 사유 메세지 (euc-kr
																								// urlencoding)
						cancelRequest.put("PartialCancelCode", partialCode); // 5.전체취소 0, 부분취소 1 [전체취소 Default]
						cancelRequest.put("MerchantMode", ""); // 6.상점모드 - 서브몰정산 가맹점 전용 (MerchantMode : "T")

						// Tid + 상점키 + 취소 금액 + 취소타입
						String hashData = storeOrderPay.getOrderTid() + res.getStoreEtc().getSpAuthKey() + cancelAmt
								+ partialCode;
						cancelRequest.put("hashData", encodeSHA256Base64(hashData));// 6.HASH 설정 [필수]

						String rTemp = "";
						try {
							// httpClient 통신
							rTemp = SmilePayUtil.sendByPost(cancelRequest, msgMgr.message("smilepay.cancelURL", locale))
									.trim();
						} catch (Exception e) {
							logger.error("menuCancel Exception > ", e);
							logger.error("menuCancel Exception > payError > M008 ");
							logger.error("SmilePayUtil.sendByPost");
							return msgMgr.message("smilepay.M008", locale);
						}

						result = SmilePayUtil.parseMessage(rTemp, "&", "=");
						
						logger.info("윤영 result 취소 테스트 "+result);

						StoreOrderCancel storeOrderCancel = new StoreOrderCancel();
						storeOrderCancel.setPayMethod(result.get("PayMethod"));
						storeOrderCancel.setPayName(SmilePayUtil.urlDecodeEuckr(result.get("PayName")));
						storeOrderCancel.setMid(result.get("MID"));
						storeOrderCancel.setTid(result.get("TID"));
						storeOrderCancel.setCancelAmt(result.get("CancelAmt"));
						storeOrderCancel.setCancelMSG(SmilePayUtil.urlDecodeEuckr(result.get("CancelMSG")));
						storeOrderCancel.setResultCode(result.get("ResultCode"));
						storeOrderCancel.setResultMsg(SmilePayUtil.urlDecodeEuckr(result.get("ResultMsg")));
						storeOrderCancel.setCancelDate(result.get("CancelDate"));
						storeOrderCancel.setCancelTime(result.get("CancelTime"));
						storeOrderCancel.setCancelNum(result.get("CancelNum"));
						storeOrderCancel.setMoid(result.get("Moid"));
						storeOrderCancel.setResultCode(result.get("ResultCode"));
						storeOrderCancel.touchWhoC(result.get("MID"));

						if (SUCCESS_CANCEL.equals(result.get("ResultCode"))) {
							// 취소 및 환불 성공에 따른 가맹점 비지니스 로직 구현 필요
							storeCancelService.cancelSuccessSave(storeOrderCancel, Integer.parseInt(storeId),
									verifiCode);
						} else {
							// 취소 및 환불 실패에 따른 가맹점 비지니스 로직 구현 필요
							storeCancelService.cancelFailSave(storeOrderCancel, Integer.parseInt(storeId), verifiCode);
							success = storeOrderCancel.getResultMsg();
						}

					}
				} else {
					logger.info("이전에 보낸값과 다릅니다. key 값 확인 필요");
					success = "이전에 보낸값과 다릅니다.<br/>매장관리자에게 문의 하세요.";
				}
			}

		} catch (Exception e) {
			logger.error("menuCancel Exception > ", e);
			logger.error("menuCancel Exception > payError > M008 ");
			return msgMgr.message("smilepay.M008", locale);
		}

		logger.info("success >>> [{}]", success);

		return success;
	}

	/**
	 * basket INSERT / UPDATE
	 */
	@RequestMapping(value = "/basket", method = RequestMethod.POST)
	public @ResponseBody List<MenuPayItem> basketInsert(@RequestBody Map<String, Object> model, Locale locale,
			HttpSession session) {

		String choose = (String) model.get("choose");
		int id = Util.parseInt((String) model.get("id"));
		String code = (String) model.get("code");
		// basketLiId 가 비어있을 경우 -1 return
		int basketLiId = Util.parseInt((String) model.get("basketLiId"));
		String name = (String) model.get("name");
		int price = Util.parseInt((String) model.get("price"), 0);
		int count = Util.parseInt((String) model.get("count"), 0);
		String src = (String) model.get("src");
		String packing = (String) model.get("packing");
		String essVal = (String) model.get("essVal");
		String essName = (String) model.get("essName");
		String addVal = (String) model.get("addVal");
		String addName = (String) model.get("addName");
		String submenu = (String) model.get("submenu");
		String compSelect = (String) model.get("compSelect");
		int menuPrice = (Integer) model.get("menuPrice");
		String storeKey = (String) model.get("storeKey");
		String basket = (String) model.get("basket");
		String table = (String) model.get("table");
		String time = (String) model.get("time");
		String refiTel = (String) model.get("refiTel");

		List<StoreOrderBasketList> listRes = null;
		List<MenuPayItem> basketItemList = new ArrayList<MenuPayItem>();
		try {
			boolean listIns = true;
			StoreOrderBasket basketOne = basketService.getBasketKey(basket);

			if (basketOne != null) {
				if (!Util.isNotValid(refiTel)) {
					basketOne.setRefiTel(refiTel);
					basketService.saveOrUpdate(basketOne);
				}

				List<StoreOrderBasketList> list = basketService.getBasketList(basketOne.getId());
				StoreOrderBasketList listone = new StoreOrderBasketList();
				StoreOrderBasketList deleteOne = new StoreOrderBasketList();
				if (list != null) {
					boolean comp = false;
					for (StoreOrderBasketList basketList : list) {
						logger.info("compSelect >>> [{}]", compSelect);
						logger.info("basketList.getCompSelect() >>> [{}]", basketList.getCompSelect());

						if (compSelect.equals(basketList.getCompSelect())) {
							listone = basketList;
							comp = true;
						}

						if (basketLiId != -1 && basketLiId == basketList.getId()) {
							deleteOne = basketList;
						}
					}

					logger.info("basketLiId >>> [{}]", basketLiId);
					logger.info("listone.getId() >>> [{}]", listone.getId());
					// 중요!!! 주문 목록에서 수정 이후 업데이트 진행시 이미 동일한 메뉴가 장바구니에 존재 할 경우
					// 해당 내용을 update 하고 업데이트 진행하려는 내용은 삭제 한다.!!!
					// 넘겨온 basketLiId 와 listone.getId()가 다를 경우 basketLiId 는 삭제 한다.
					if (basketLiId != -1 && basketLiId != listone.getId()) {
						logger.info("basketLiId 삭제 한다. >>> 삭제 ID [{}]", basketLiId);
						basketService.delete(deleteOne);
						if ("U".equals(choose)) {
							int amo = listone.getMenuAmount();
							count += amo;
						}
					}

					if (comp) {
						// [kdk] 중요!!! 2019.07.31
						// 주의! 메뉴가 장바구니 클릭인지 메뉴 클릭인지 확인하여 아래 더하는 내용을 빼고 넣고 하는 것을 확인한다.
						if ("M".equals(choose)) {
							int amo = listone.getMenuAmount();
							count = count + amo;
						}

						listone.setMenuAmount(count);
						listone.setMenuAmt(price);
						listone.setMenuTotalAmt(menuPrice);
						listone.setPacking(packing);
						listone.setEssVal(essVal);
						listone.setEssName(essName);
						listone.setAddVal(addVal);
						listone.setAddName(addName);
						listone.setCompSelect(compSelect);
						listone.setSubmenu(submenu);
						listone.setWhoLastUpdateDate(new Date());

						basketService.saveOrUpdate(listone);

						listIns = false;
					}
				}
			} else {
				Store store = storeService.getStoreByStoreKey(storeKey);
				if (store != null) {
					basketOne = new StoreOrderBasket(store.getId(), basket, 0, 0, table, time);
					basketOne.setRefiTel(refiTel);

					basketService.saveOrUpdate(basketOne);
				} else {
					listIns = false;
				}
			}
			if (listIns) {
				StoreOrderBasketList basketList = new StoreOrderBasketList(basketOne, id, count, name, price, menuPrice,
						PayUtil.seqChgCipher(1), packing, essVal, essName, addVal, addName, basket, compSelect, submenu,
						src);

				basketService.saveOrUpdate(basketList);
			}

			logger.info("전체 조회 basketOne.getId() >?? [{}]", basketOne.getId());
			listRes = basketService.getBasketList(basketOne.getId());
			if (listRes.size() > 0) {
				for (StoreOrderBasketList one : listRes) {
					MenuPayItem basketItem = new MenuPayItem();

					basketItem.setId(String.valueOf(one.getId()));
					basketItem.setMenuId(one.getMenuId());
					basketItem.setCompSelect(one.getCompSelect());
					basketItem.setName(one.getMenuName());
					basketItem.setOrderCount(String.valueOf(one.getMenuAmount()));
					basketItem.setPrice(String.valueOf(one.getMenuAmt()));
					basketItem.setToPrice(String.valueOf(one.getMenuTotalAmt()));
					basketItem.setPacking(one.getPacking());
					basketItem.setEssVal(one.getEssVal());
					basketItem.setEssName(one.getEssName());
					basketItem.setAddVal(one.getAddVal());
					basketItem.setAddName(one.getAddName());
					basketItem.setSubMenu(Util.parseString(one.getSubmenu()));
					basketItem.setImgSrc(one.getSrc());

					basketItemList.add(basketItem);
				}
			}

		} catch (Exception e) {
			logger.error("menu > basketInsert > e : [{}]", e);
			logger.error("menu > basketInsert > storeKey : [{}]", storeKey);
			logger.error("menu > basketInsert > basket : [{}]", basket);
			throw new ServerOperationForbiddenException("SaveError");
		}

		return basketItemList;
	}

	/**
	 * basket Delete
	 */
	@RequestMapping(value = "/basketDel", method = RequestMethod.POST)
	public @ResponseBody List<MenuPayItem> basketDelete(@RequestBody Map<String, Object> model, Locale locale,
			HttpSession session) {

		int basketLiId = (int) model.get("basketLiId");
		String compSelect = (String) model.get("compSelect");
		String basket = (String) model.get("basket");

		List<StoreOrderBasketList> listRes = null;
		List<MenuPayItem> basketItemList = new ArrayList<MenuPayItem>();
		try {
			StoreOrderBasket basketOne = basketService.getBasketKey(basket);
			if (basketOne != null) {
				basketService.deleteBasketList(basketLiId);

				listRes = basketService.getBasketList(basketOne.getId());
				for (StoreOrderBasketList one : listRes) {
					MenuPayItem basketItem = new MenuPayItem();

					basketItem.setId(String.valueOf(one.getId()));
					basketItem.setMenuId(one.getMenuId());
					basketItem.setCompSelect(one.getCompSelect());
					basketItem.setName(one.getMenuName());
					basketItem.setOrderCount(String.valueOf(one.getMenuAmount()));
					basketItem.setPrice(String.valueOf(one.getMenuAmt()));
					basketItem.setToPrice(String.valueOf(one.getMenuTotalAmt()));
					basketItem.setPacking(one.getPacking());
					basketItem.setEssVal(one.getEssVal());
					basketItem.setEssName(one.getEssName());
					basketItem.setAddVal(one.getAddVal());
					basketItem.setAddName(one.getAddName());
					basketItem.setSubMenu(Util.parseString(one.getSubmenu()));
					basketItem.setImgSrc(one.getSrc());

					basketItemList.add(basketItem);
				}
			}
		} catch (Exception e) {
			logger.error("menu > basketDelete > e : [{}]", e);
			logger.error("menu > basketDelete > basketLiId : [{}]", basketLiId);
			logger.error("menu > basketDelete > basket : [{}]", basket);
			throw new ServerOperationForbiddenException("DeleteError");
		}

		return basketItemList;
	}

	/**
	 * basket ReLoad
	 */
	@RequestMapping(value = "/basketReload", method = RequestMethod.POST)
	public @ResponseBody List<MenuPayItem> basketReload(@RequestBody Map<String, Object> model, Locale locale,
			HttpSession session) {

		String basket = (String) model.get("basket");

		List<StoreOrderBasketList> listRes = null;
		List<MenuPayItem> basketItemList = new ArrayList<MenuPayItem>();
		try {
			StoreOrderBasket basketOne = basketService.getBasketKey(basket);
			if (basketOne != null) {

				listRes = basketService.getBasketList(basketOne.getId());
				for (StoreOrderBasketList one : listRes) {
					MenuPayItem basketItem = new MenuPayItem();

					basketItem.setId(String.valueOf(one.getId()));
					basketItem.setMenuId(one.getMenuId());
					basketItem.setCompSelect(one.getCompSelect());
					basketItem.setName(one.getMenuName());
					basketItem.setOrderCount(String.valueOf(one.getMenuAmount()));
					basketItem.setPrice(String.valueOf(one.getMenuAmt()));
					basketItem.setToPrice(String.valueOf(one.getMenuTotalAmt()));
					basketItem.setPacking(one.getPacking());
					basketItem.setEssVal(one.getEssVal());
					basketItem.setEssName(one.getEssName());
					basketItem.setAddVal(one.getAddVal());
					basketItem.setAddName(one.getAddName());
					basketItem.setSubMenu(Util.parseString(one.getSubmenu()));
					basketItem.setImgSrc(one.getSrc());

					basketItemList.add(basketItem);
				}
			}
		} catch (Exception e) {
			logger.error("menu > basketReload > e : [{}]", e);
			logger.error("menu > basketReload > basket : [{}]", basket);
			throw new ServerOperationForbiddenException("DeleteError");
		}

		return basketItemList;
	}

	/**
	 * basket packingChg
	 */
	@RequestMapping(value = "/packingChg", method = RequestMethod.POST)
	public @ResponseBody String packingChg(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {

		String packing = (String) model.get("packing");
		String basket = (String) model.get("basket");

		List<StoreOrderBasketList> listRes = null;
		try {
			StoreOrderBasket basketOne = basketService.getBasketKey(basket);
			if (basketOne != null) {
				listRes = basketService.getBasketList(basketOne.getId());
				for (StoreOrderBasketList one : listRes) {
					one.setPacking(packing);
					one.touchWho(basket);
					basketService.saveOrUpdate(one);
				}
			}
		} catch (Exception e) {
			logger.error("menu > packingChg > e : [{}]", e);
			logger.error("menu > packingChg > basket : [{}]", basket);
			throw new ServerOperationForbiddenException("OperationError");
		}

		return "OK";
	}

	/**
	 * basket countChg
	 */
	@RequestMapping(value = "/countChg", method = RequestMethod.POST)
	public @ResponseBody String countChg(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {

		String basketLiId = (String) model.get("basketLiId");
		int count = (int) model.get("count");

		try {
			StoreOrderBasketList one = basketService.get(Util.parseInt(basketLiId, 0));
			one.setMenuAmount(count);
			basketService.saveOrUpdate(one);
		} catch (Exception e) {
			logger.error("menu > countChg > e : [{}]", e);
			logger.error("menu > countChg > basket : [{}]", basketLiId);
			throw new ServerOperationForbiddenException("OperationError");
		}

		return "OK";
	}

	/**
	 * 리필 가능 여부 체크 하기
	 */
	@RequestMapping(value = "/rfChk", method = RequestMethod.POST)
	public @ResponseBody RefillView rfChk(@RequestBody Map<String, Object> model, HttpServletRequest request,
			Locale locale, HttpSession session) {
		RefillView refillView = new RefillView();
		try {
			// 1. storeId를 정상 적인지 확인
			// 2. storeId와 verifiCode(승인번호)를 체크 하여 정상적인지 확인한다.
			boolean consent2 = (boolean) model.get("consent2");
			String viewTel = (String) model.get("viewTel");
			String storeKey = (String) model.get("storeKey");
			String rfMenuId = (String) model.get("rfMenuId");

			logger.info("/rfChk > storeKey >>> [{}], 변경전 viewTel >>> [{}]", storeKey, viewTel);
			viewTel = viewTel.replace("-", "");
			logger.info("/rfChk > storeKey >>> [{}], 변경 후 viewTel >>> [{}]", storeKey, viewTel);
			logger.info("/rfChk > storeKey >>> [{}], consent2 >>> [{}]", storeKey, consent2);
			logger.info("/rfChk > storeKey >>> [{}], rfMenuId >>> [{}]", storeKey, rfMenuId);
			refillView = storeOrderService.refillbyOrderList(storeKey, viewTel, rfMenuId);
		} catch (Exception e) {
			logger.error("rfChk Exception > refillView Error");
			refillView.setPaOrderId("-9999");
			refillView.setRFyn("N");
			refillView.setRFCnt("-9999");
		}

		return refillView;
	}

	/**
	 * submit으로 리필을 주문 한다.
	 */
	@RequestMapping(value = "/rfOrder", method = RequestMethod.POST)
	public String rfOrder(Model model, HttpServletRequest request, Locale locale, HttpSession session) {

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("html_lang", Util.htmlLang(locale)),

				new Message("pay_orderNum", "smilepay.orderNum"), new Message("pay_stay", "smilepay.stay"),
				new Message("pay_firstPage", "smilepay.firstPage"),

				new Message("pay_msg_firstPage", "smilepay.msg.firstPage"),
				new Message("pay_msg_orderTable", "smilepay.msg.orderTable"),
				new Message("pay_msg_notSupportBrowser2", "smilepay.msg.notSupportBrowser2"),
				new Message("pay_msg_paySuccess", "smilepay.msg.paySuccess"),

				new Message("confirm_ok", "confirm.ok"), new Message("confirm_cancel", "confirm.cancel") });
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {

		}

		String choose = (String) request.getParameter("choose");
		int id = Util.parseInt((String) request.getParameter("id"));
		// basketLiId 가 비어있을 경우 -1 return
		int basketLiId = Util.parseInt((String) request.getParameter("basketLiId"));
		String name = (String) request.getParameter("name");
		int price = Util.parseInt((String) request.getParameter("price"), 0);
		int count = Util.parseInt((String) request.getParameter("count"), 0);
		String src = (String) request.getParameter("src");
		String packing = (String) request.getParameter("packing");
		String essVal = (String) request.getParameter("essVal");
		String essName = (String) request.getParameter("essName");
		String addVal = (String) request.getParameter("addVal");
		String addName = (String) request.getParameter("addName");
		String submenu = (String) request.getParameter("submenu");
		String compSelect = (String) request.getParameter("compSelect");
		int menuPrice = Util.parseInt((String) request.getParameter("menuPrice"));
		String storeKey = (String) request.getParameter("storeKey");
		String basket = (String) request.getParameter("basket");
		String table = (String) request.getParameter("table");
		String time = (String) request.getParameter("time");
		String tel = (String) request.getParameter("tel");
		String paOrderId = (String) request.getParameter("paOrderId");

		logger.info("rfOrder choose [{}], id [{}]", choose, id);
		logger.info("rfOrder basketLiId [{}], name [{}]", basketLiId, name);
		logger.info("rfOrder price [{}], count [{}]", price, count);
		logger.info("rfOrder src [{}], packing [{}]", src, packing);
		logger.info("rfOrder essVal [{}], essName [{}]", essVal, essName);
		logger.info("rfOrder addVal [{}], essName [{}]", addVal, essName);
		logger.info("rfOrder submenu [{}], compSelect [{}]", submenu, compSelect);
		logger.info("rfOrder menuPrice [{}], menuPrice [{}]", menuPrice, menuPrice);
		logger.info("rfOrder storeKey [{}], basket [{}]", storeKey, basket);
		logger.info("rfOrder time [{}], tel [{}]", time, tel);
		logger.info("rfOrder paOrderId [{}]", paOrderId);

		Store res = storeService.getStoreByStoreKey(storeKey);

		// 리필일 경우 총 금액은 0원으로 처리
		int goodsAmt = 0;
		int totalindex = 1;
		String goodsName = name;
		List<MenuPayItem> payitemList = new ArrayList<MenuPayItem>();
		MenuPayItem payitem = new MenuPayItem();

		payitem.setId("");
		payitem.setMenuId(id);
		payitem.setCompSelect(compSelect);
		payitem.setName(name);
		payitem.setOrderCount(String.valueOf(count));
		payitem.setPrice(String.valueOf(goodsAmt));
		payitem.setToPrice(String.valueOf(goodsAmt));
		payitem.setPacking(packing);
		payitem.setEssVal(essVal);
		payitem.setEssName(essName);
		payitem.setAddVal(addVal);
		payitem.setAddName(addName);
		payitem.setSubMenu(submenu);
		payitem.setImgSrc(src);

		payitemList.add(payitem);

		SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowTime = yyyyMMddHHmmss.format(new Date());
		// device 정보를 확인하여 개발이 필요하다.
		// M 일 경우 : 모바일로 표시_2019.10.02
		// 2019.10.02 이후 MS : 스마일페이 모바일 / ME : easypay 로 나누어 저장
		// 2022.08.26 이후 MS : 스마트로페이 모바일 / ME : easypay 로 나누어 저장
		String device = res.getStoreEtc().getStorePayGubun();
		// MS일 경우 spStoreKey 사용
		// ME일 경우 epStoreKey 사용
		String storeMid = res.getStoreEtc().getSpStoreKey();
		if ("ME".equals(device)) {
			storeMid = res.getStoreEtc().getEpStoreKey();
		}
		// 주문키 생성 > 모바일 : ME(MS) + 장바구니 코드 + paycast상점 KEY + 스마일페이상점ID + 날짜시간
		// 이후 결제 승인이 정상적으로 났을 경우 해당 주문 번호에 주문순서번호를 붙여준다.
		// 2019.07.07 주문키 생성 : ME(MS) + 상점 ID + 상점 KEY(???) + 날짜시간
		String orderNumber = device + res.getStoreKey() + storeMid + nowTime;

		StoreOrder orderDao = new StoreOrder(res.getId(), orderNumber, goodsName, String.valueOf(totalindex), goodsAmt,
				device);
		orderDao.setOrderTable(table);
		orderDao.setTelNumber(tel.replace("-", ""));
		orderDao.setOrderType("I");
		orderDao.setOrderParent(Util.parseInt(paOrderId, -1));
		orderDao.setPayment("RF");
		storeOrderService.saveOrder(orderDao, payitemList);

		String orderSequence = "";
		SimpleDateFormat AuthDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String AuthDate = AuthDateFormat.format(new Date());
		// 결제된 내용을 저장
		StoreOrderPay orderPay = new StoreOrderPay();
		orderPay.setStoreId(res.getId());
		orderPay.setOrderNumber(orderNumber);
		orderPay.setOrderTid("RF");
		orderPay.setPayMethod("RF");
		orderPay.setPayMid(storeMid);
		orderPay.setPayAmt(String.valueOf(goodsAmt));
		orderPay.setGoodsname(goodsName);
		orderPay.setPayOid(orderNumber);
		orderPay.setPayAuthDate(AuthDate);
		orderPay.setPayAuthCode("RF");
		orderPay.setPayResultCode("RF");
		orderPay.setPayResultMsg("ReFill");
		orderPay.setPaySignValue("RF");
		orderPay.setPayFnCd("999999");
		orderPay.setPayFnName("999999");
		orderPay.setPayCardQuota("999999");
		orderPay.setPayAcquCardcode("999999");
		orderPay.setPayDivideInfo("");
		orderPay.touchWhoC(orderNumber);

		orderSequence = storeOrderService.saveOrderPay(orderDao, orderPay, locale, session);
		StoreOrderCook storeOrderCook = new StoreOrderCook(orderDao.getStoreId(), orderDao.getId(),
				orderDao.getOrderNumber());
		// FCM 전송 : 모바일 결제완료가 되어 DB에 저장 되었을 경우 fcm으로 stb 에 알려준다.
		storeOrderService.fcmTransmission(res.getId(), storeOrderCook);

		// 모바일 상점 로그 가져오기
		imgCommon(res, model);
		// 주방용 패드가 있을 경우에 대한 대기번호 순수
		// 없을 경우 9999 반환
		model.addAttribute("stayMenuCnt", storeCookService.getStayCntMobile(res.getId()));

		// 매장 화면 구성 데이터
		model.addAttribute("storeId", res.getId());
		model.addAttribute("storeName", res.getStoreName());
		model.addAttribute("storeIntroduction", "");

		model.addAttribute("url", "/menu");
		model.addAttribute("oriUrlparam", res.getStoreKey());
		model.addAttribute("orderTable", Util.parseString(table, "0000"));
		model.addAttribute("orderSequence", orderSequence);

		// 모바일 결제 승인 일자가 181126144622 으로 넘어옴
		Calendar calendar = new GregorianCalendar(Locale.KOREA);
		int nYear = calendar.get(Calendar.YEAR);
		String nYearSt = String.valueOf(nYear);
		model.addAttribute("authDate", nYearSt.substring(0, 2) + AuthDate);
		model.addAttribute("GoodsName", goodsName);

		return "order/refillOrder";
	}
	
    public List<StoreTime> timeList(String rsvpTimeDB, Date possiblDate, Date endDate) {
        List<StoreTime> timeList = new ArrayList<>();
        String[] rsvpTimeArray = rsvpTimeDB.split(",");
        List<String> rsvpTimeList = new ArrayList<>(Arrays.asList(rsvpTimeArray));
        if (rsvpTimeList.size() > 0)
          for (String rsvpTime : rsvpTimeList) {
            boolean clickTF = true;
            int rsvpTimeInt = Util.parseInt(rsvpTime, 0);
            Date rsvpTimeDate = new Date();
            GregorianCalendar calendarRsvp = new GregorianCalendar();
            calendarRsvp.setTime(rsvpTimeDate);
            calendarRsvp.add(12, rsvpTimeInt);
            rsvpTimeDate = calendarRsvp.getTime();
            if (possiblDate != null && 
              rsvpTimeDate.compareTo(possiblDate) <= 0)
              clickTF = false; 
            if (endDate != null && 
              rsvpTimeDate.compareTo(endDate) > 0)
              clickTF = false; 
            String total = "+" + rsvpTimeInt + "분";
            StoreTime timeOne = new StoreTime(rsvpTimeInt, total, clickTF);
            timeList.add(timeOne);
          }  
        return timeList;
      }
    
    private void stampCouponUpdate(Store res, StoreOrder orderRes, String orderNumber, HttpSession session, Model model, Locale locale) {
        DecimalFormat format = new DecimalFormat("###,###");
        int dispStampCnt = 0;
        String dispCoupon = "N";
        int orderMenuCnt = Util.parseInt(orderRes.getGoodsTotal(), 0);
        String tel = orderRes.getTelNumber();
        int inStamp = 0;
        List<StorePolicy> policyList = this.couponService.getPolicyList(res.getId(), "S");
        if (policyList.size() > 0) {
          int policyOrderStamp = 0, policyStamp = 0;
          for (StorePolicy item : policyList) {
            policyOrderStamp = item.getOrderAmt();
            policyStamp = item.getStamp();
          } 
          int cnt = orderMenuCnt / policyOrderStamp;
          inStamp = cnt * policyStamp;
        } 
        if (inStamp > 0) {
          int maxStamp = 0;
          StoreOrderCoupon compStamp = null;
          List<StoreOrderCoupon> resStamp = this.couponService.getCouponStamp(res.getId(), tel, "S");
          if (resStamp.size() > 0) {
            for (StoreOrderCoupon one : resStamp) {
              maxStamp = one.getStampTotal() + inStamp;
              one.setStampCnt(inStamp);
              one.setStampTotal(maxStamp);
              compStamp = one;
            } 
          } else {
            compStamp = new StoreOrderCoupon(tel, inStamp, inStamp, res, session);
          } 
          this.couponService.saveOrUpdate(compStamp);
          int policyStamp = 0;
          StoreCoupon coupon = null;
          List<CouponPolicy> couponPolicyList = this.couponService.getCouponPolicyList(res.getId());
          if (couponPolicyList.size() > 0)
            for (CouponPolicy item : couponPolicyList) {
              StorePolicy policy = item.getPolicy();
              policyStamp = policy.getStamp();
              coupon = item.getCoupon();
            }  
          if (maxStamp >= policyStamp && coupon != null) {
            SimpleDateFormat transToday = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat transEndDay = new SimpleDateFormat("MM-dd");
            String startDateSt = "", endDateSt = "";
            int issuable = maxStamp / policyStamp;
            int saveStamp = maxStamp % policyStamp;
            for (int i = 0; i < issuable; i++) {
              int validDate = coupon.getValidDate();
              Calendar cal = Calendar.getInstance();
              cal.setTime(new Date());
              cal.add(2, validDate);
              Date endDate = Util.setMaxTimeOfDate(cal.getTime());
              StoreOrderCoupon issuableCoupon = new StoreOrderCoupon(tel, policyStamp, endDate, res, coupon, session);
              this.couponService.saveOrUpdate(issuableCoupon);
              startDateSt = transToday.format(new Date());
              endDateSt = transEndDay.format(endDate);
            } 
            compStamp.setStampCnt(maxStamp - saveStamp);
            compStamp.setStampTotal(saveStamp);
            this.couponService.saveOrUpdate(compStamp);
            dispStampCnt = inStamp;
            dispCoupon = coupon.getName();
            try {
              String inStampFormat = format.format(inStamp);
              String saveStampFormat = format.format(saveStamp);
              alimTalkSend(res, orderRes, "ST", tel, inStampFormat, saveStampFormat, locale);
              alimTalkSend(res, orderRes, "CP", tel, coupon.getName(), String.valueOf(startDateSt) + " ~ " + endDateSt, locale);
            } catch (Exception e) {
              logger.error("stamp / coupon alarmUpdate ERROR", e);
            } 
          } else {
            dispStampCnt = inStamp;
            dispCoupon = "N";
            String inStampFormat = format.format(inStamp);
            String maxStampFormat = format.format(maxStamp);
            alimTalkSend(res, orderRes, "ST", tel, inStampFormat, maxStampFormat, locale);
          } 
        } 
        model.addAttribute("stamp", Integer.valueOf(dispStampCnt));
        model.addAttribute("coupon", dispCoupon);
      }
      
      private void alimTalkSend(Store store, StoreOrder storeOrderOne, String type, String text0, String text1, String text2, Locale locale) {
        if (store.isAlimTalkAllowed()) {
          String senderKey = this.msgMgr.message("alimTalk.senderKey", locale);
          String tmplCd = "", subject = "", msg = "", smsmsg = "";
          if ("PO".equals(type)) {
            tmplCd = this.msgMgr.message("alimTalk.tmplPoint", locale);
            subject = this.msgMgr.message("alimTalk.subjectPoint", locale);
            msg = this.msgMgr.message("alimTalk.msgPoint", locale);
            smsmsg = this.msgMgr.message("alimTalk.smsmsgPoint", locale);
          } else if ("ST".equals(type)) {
            tmplCd = this.msgMgr.message("alimTalk.tmplStamp", locale);
            subject = this.msgMgr.message("alimTalk.subjectStamp", locale);
            msg = this.msgMgr.message("alimTalk.msgStamp", locale);
            smsmsg = this.msgMgr.message("alimTalk.smsmsgStamp", locale);
          } else if ("CP".equals(type)) {
            tmplCd = this.msgMgr.message("alimTalk.tmplCoupon", locale);
            subject = this.msgMgr.message("alimTalk.subjectCoupon", locale);
            msg = this.msgMgr.message("alimTalk.msgCoupon", locale);
            smsmsg = this.msgMgr.message("alimTalk.smsmsgCoupon", locale);
          } 
          msg = msg.replace("{0}", phoneHyphenAdd(text0));
          msg = msg.replace("{1}", text1);
          msg = msg.replace("{2}", text2);
          msg = msg.replace("{3}", store.getPhone());
          smsmsg = smsmsg.replace("{0}", text0);
          smsmsg = smsmsg.replace("{1}", text1);
          logger.info("[" + type + "] >>> store.getBizName() [{}], store.getShortName() [{}]", store.getBizName(), store.getShortName());
          logger.info("[" + type + "] >>> store.getPhone() [{}], storeOrderOne.getOrderSeq() [{}]", store.getPhone(), storeOrderOne.getOrderSeq());
          logger.info("[" + type + "] >>> tel [{}], senderKey [{}]", storeOrderOne.getTelNumber(), senderKey);
          logger.info("[" + type + "] >>> tmplCd [{}], subject [{}]", tmplCd, subject);
          logger.info("[" + type + "] >>> msg [{}], smsmsg [{}]", msg, smsmsg);
          StoreAlimTalk alimTalk = new StoreAlimTalk(store.getShortName(), store.getStoreName(), store.getPhone(), storeOrderOne.getOrderSeq(), "", 
              "", storeOrderOne.getTelNumber(), senderKey, tmplCd, subject, msg, smsmsg);
          this.alimTalkService.save(alimTalk);
			try {
				PayUtil.testServiceApi(alimTalk);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
          logger.info("[" + type + "] >>> store.getBizName() [{}], store.isAlimTalkAllowed() [{}]", store.getBizName(), Boolean.valueOf(store.isAlimTalkAllowed()));
          logger.info("[" + type + "] >>> store.getBizName() [{}], store.getShortName() [{}]", store.getBizName(), store.getShortName());
        } 
      }
      
      private static String phoneHyphenAdd(String num) {
    	    String formatNum = "";
    	    num = num.replaceAll("-", "");
    	    if (num.length() == 11) {
    	      formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
    	    } else if (num.length() == 8) {
    	      formatNum = num.replaceAll("(\\d{4})(\\d{4})", "$1-$2");
    	    } else if (num.indexOf("02") == 0) {
    	      formatNum = num.replaceAll("(\\d{2})(\\d{3,4})(\\d{4})", "$1-$2-$3");
    	    } else {
    	      formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
    	    } 
    	    return formatNum;
    	  }
      
      @RequestMapping(value = {"/readcoupon"}, method = {RequestMethod.POST})
      @ResponseBody
      public List<CouponDispItem> readcoupon(@RequestBody Map<String, Object> model, HttpServletRequest request, Locale locale, HttpSession session) {
        List<CouponDispItem> couponDisp = new ArrayList<>();
        String storeId = "", tel = "", deliTel = "";
        try {
          storeId = Util.parseString((String)model.get("storeId"));
          tel = Util.parseString((String)model.get("tel"));
          deliTel = Util.parseString((String)model.get("deliTel"));
          if (Util.isNotValid(tel))
            tel = deliTel; 
          List<StoreOrderCoupon> res = this.couponService.getIssueCouponRead(storeId, tel);
          if (res.size() > 0)
            for (StoreOrderCoupon one : res) {
              CouponDispItem disp = new CouponDispItem(one);
              couponDisp.add(disp);
            }  
        } catch (Exception e) {
          logger.error("/readcoupon > payError > storeId : [{}], tel : [{}]", storeId, tel);
          logger.error("/readcoupon > payError > storeId : [{}], deliTel : [{}]", storeId, deliTel);
        } 
        return couponDisp;
      }
      
      @RequestMapping(value = {"/readpoint"}, method = {RequestMethod.POST})
      @ResponseBody
      public PointDispItem readpoint(@RequestBody Map<String, Object> model, HttpServletRequest request, Locale locale, HttpSession session) {
        PointDispItem pointOne = new PointDispItem();
        String storeId = "", tel = "", deliTel = "";
        try {
          storeId = Util.parseString((String)model.get("storeId"));
          tel = Util.parseString((String)model.get("tel"));
          deliTel = Util.parseString((String)model.get("deliTel"));
          if (Util.isNotValid(tel))
            tel = deliTel;
          pointOne = pointCalc(storeId, tel);
          logger.info(deliTel);
          logger.info(tel);
        } catch (Exception e) {
          logger.error("/readpoint > payError > storeId : [{}], tel : [{}]", storeId, tel);
          logger.error("/readpoint > payError > storeId : [{}], deliTel : [{}]", storeId, deliTel);
        } 
        return pointOne;
      }
      

	

//   		


  	@RequestMapping(value ="/smartroPayCheck", method = RequestMethod.POST)
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
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String storeIdSt = Util.parseString((String)request.getParameter("storeId"));
		logger.info(storeIdSt);
		int storeId = Util.parseInt(storeIdSt);
		String storeKey = (String)request.getParameter("storeKey");
		logger.info(storeKey);
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
   		
		String PayMethod = Util.parseString((String)request.getParameter("PayMethod"));
		logger.info("PayMethod" + PayMethod);
		String orderType = Util.parseString((String)request.getParameter("orderType"));
		logger.info("orderType" + orderType);
		String GoodsCnt = Util.parseString((String)request.getParameter("GoodsCnt"));
		logger.info("GoodsCnt" + GoodsCnt);
		String GoodsName = Util.parseString((String)request.getParameter("GoodsName"));
		logger.info("GoodsName" + GoodsName);
		String Mid = (String)request.getParameter("Mid");
		logger.info("Mid" + Mid);
		Store res = storeService.getStoreByStoreKey(storeKey);
		String EdiDate = (String)request.getParameter("EdiDate");
		logger.info("EdiDate" + EdiDate);
		String Amt = (String)request.getParameter("Amt");
		logger.info("Amt" + Amt);
		String Moid = (String)request.getParameter("Moid");
		logger.info("Moid" + Moid);
		String ReturnUrl = (String)request.getParameter("ReturnUrl");
		logger.info("ReturnUrl" + ReturnUrl);
		String RetryUrl = (String)request.getParameter("RetryUrl");
		logger.info("RetryUrl" + RetryUrl);
		String StopUrl = (String)request.getParameter("StopUrl");
		logger.info("StopUrl" + StopUrl);
		String BuyerAddr = (String)request.getParameter("BuyerAddr");
		logger.info("BuyerAddr" + BuyerAddr);
		String MallResultFWD = (String)request.getParameter("MallResultFWD");
		logger.info("MallResultFWD" + MallResultFWD);
		String TransType = (String)request.getParameter("TransType");
		logger.info("TransType" + TransType);
		String SocketYN = (String)request.getParameter("SocketYN");
		logger.info("SocketYN" + SocketYN);
		String Language = (String)request.getParameter("Language");
		logger.info("Language" + Language);
		String EncodingType = (String)request.getParameter("EncodingType");
		logger.info("EncodingType" + EncodingType);
		String clientType = (String)request.getParameter("clientType");
		logger.info("clientType" + clientType);
		String OfferPeriod = (String)request.getParameter("OfferPeriod");
		logger.info("OfferPeriod" + OfferPeriod);
		String VatAmt = (String)request.getParameter("VatAmt");
		logger.info("VatAmt" + VatAmt);
		String TaxAmt = (String)request.getParameter("TaxAmt");
		logger.info("TaxAmt" + TaxAmt);
		String TaxFreeAmt = (String)request.getParameter("TaxFreeAmt");
		logger.info("TaxFreeAmt" + TaxFreeAmt);
		String SvcAmt = (String)request.getParameter("SvcAmt");
		logger.info("SvcAmt" + SvcAmt);
		String CardQuota = (String)request.getParameter("CardQuota");
		logger.info("CardQuota" + CardQuota);
		String CardInterest = (String)request.getParameter("CardInterest");
		logger.info("CardInterest" + CardInterest);
		String CardPoint = (String)request.getParameter("CardPoint");
		logger.info("CardPoint" + CardPoint);
		String IspWapUrl = (String)request.getParameter("IspWapUrl");
		logger.info("IspWapUrl" + IspWapUrl);
		String DivideInfo = (String)request.getParameter("DivideInfo");
		logger.info("DivideInfo" + DivideInfo);
		String UserIp = (String)request.getParameter("UserIp");
		logger.info("UserIp" + UserIp);
		String MallIp = (String)request.getParameter("MallIp");
		logger.info("MallIp" + MallIp);
		String FnCd = (String)request.getParameter("FnCd");
		logger.info("FnCd" + FnCd);
		String menuInTime = (String)request.getParameter("menuInTime");
		logger.info("menuInTime" + menuInTime);
		String GoodsCl = (String)request.getParameter("GoodsCl");
		logger.info("GoodsCl" + GoodsCl);
		String VbankExpDate = (String)request.getParameter("VbankExpDate");
		logger.info("VbankExpDate" + VbankExpDate);
		String roadAddr = (String)request.getParameter("roadAddr");
		logger.info("roadAddr" + roadAddr);
		String addrDetail = (String)request.getParameter("addrDetail");
		logger.info("addrDetail" + addrDetail);
		String BuyerName = (String)request.getParameter("BuyerName");
		logger.info("BuyerName" + BuyerName);
		String BuyerEmail = (String)request.getParameter("BuyerEmail");
		logger.info("BuyerEmail" + BuyerEmail);
		String BuyerTel = (String)request.getParameter("BuyerTel");
		logger.info("BuyerTel" + BuyerTel);
   		String payment = (String)request.getParameter("payment");
   		logger.info(payment);
		String EncryptData = encodeSHA256Base64(EdiDate + Mid + payment + res.getStoreEtc().getSpAuthKey());
		SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
		String toDate = yyyyMMddHHmmss.format(new Date());		
   		logger.info("EncryptData" + EncryptData);

   		// 쿠폰, 포인트 해당내용 체크 필요 
   		String coupon = (String)request.getParameter("coupon");
   		String couponSelect = (String)request.getParameter("couponSelect");
   		String pointTotal = (String)request.getParameter("pointTotal");
   		String usePoint = (String)request.getParameter("usePoint");
   		int discount = Util.parseInt((String)request.getParameter("discount"), 0);

   		
   		//최종 결제가 될 금액이 0원 일 경우 easypay를 사용 하지 않는다. 2020-04-27
   		int paymentInt = Util.parseInt(payment, 0);	
   		int deliveryPayInt =Util.parseInt(deliveryPay, 0);	 
   		   		
		logger.info("/easyPayCheck coupon [{}] couponAmt [{}]", coupon, couponSelect);
		logger.info("/easyPayCheck pointTotal [{}] usePoint [{}]", pointTotal, usePoint);
		logger.info("/easyPayCheck discount [{}] payment [{}]", discount, payment);
   		
   		if(paymentInt <= 0){
   			logger.info("페이가 0일 경우");
   	        try {
   	        	GoodsName = URLDecoder.decode(GoodsName, "UTF-8");
   	        	logger.info(GoodsName);
   	        	storeName = URLDecoder.decode(storeName, "UTF-8");
   	        	logger.info(storeName);
   	        	logger.info("여기");
   	        } catch (UnsupportedEncodingException e) {
   	        	logger.info("/easyPayCheck URLEncoder >>> sp_product_nm[{}][{}]", sp_product_nm, e);
   	        }
   			
   			res = storeService.getStoreByStoreKey(storeKey);

   			StoreOrderBasket basketOne = basketService.getOrderNum(Moid);
   			String savingType = basketOne.getSavingType();
   			logger.info(savingType);
			StoreOrder order = storeOrderService.getOrder(storeId, Moid);

			logger.info(Moid);
			// 결제된 내용을 저장
			String orderSequence = "";
			SimpleDateFormat AuthDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String AuthDate = AuthDateFormat.format(new Date());
			logger.info(AuthDate);
			if(order.getOrderPayId() > 0){
				logger.info("페이아이디 잘 가져왔음");
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
				orderPay.setOrderNumber(Moid);
				orderPay.setOrderTid(savingType);
				orderPay.setPayMethod(savingType);
				orderPay.setPayMid(Mid);
				orderPay.setPayAmt(payment);
				orderPay.setDeliveryPay(deliveryPay);
				orderPay.setGoodsname(GoodsName);
				orderPay.setPayOid(Moid);
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
				orderPay.touchWhoC(Moid);
				
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
			logger.info(AuthDate);
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
       	logger.info("저기");
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
		model.addAttribute("PayMethod", PayMethod);
		model.addAttribute("orderType", orderType);
		model.addAttribute("GoodsCnt", GoodsCnt);
		model.addAttribute("GoodsName", GoodsName);
		model.addAttribute("Mid", Mid);
		model.addAttribute("EdiDate", EdiDate);
		model.addAttribute("Amt", Amt);
		model.addAttribute("Moid", Moid);
		model.addAttribute("ReturnUrl", ReturnUrl);
		model.addAttribute("RetryUrl", RetryUrl);
		model.addAttribute("StopUrl", StopUrl);
		model.addAttribute("BuyerAddr", BuyerAddr);
		model.addAttribute("MallResultFWD", MallResultFWD);
		model.addAttribute("TransType", TransType);
		model.addAttribute("SocketYN", SocketYN);
		model.addAttribute("Language", Language);
		model.addAttribute("EncodingType", EncodingType);
		model.addAttribute("clientType", clientType);
		model.addAttribute("OfferPeriod", OfferPeriod);
		model.addAttribute("VatAmt", VatAmt);
		model.addAttribute("TaxAmt", TaxAmt);
		model.addAttribute("TaxFreeAmt", TaxFreeAmt);
		model.addAttribute("SvcAmt", SvcAmt);
		model.addAttribute("CardQuota", CardQuota);
		model.addAttribute("CardInterest", CardInterest);
		model.addAttribute("CardPoint", CardPoint);
		model.addAttribute("IspWapUrl", IspWapUrl);
		model.addAttribute("DivideInfo", DivideInfo);
		model.addAttribute("UserIp", UserIp);
		model.addAttribute("MallIp", MallIp);
		model.addAttribute("FnCd", FnCd);
		model.addAttribute("menuInTime", menuInTime);
		model.addAttribute("GoodsCl", GoodsCl);
		model.addAttribute("VbankExpDate", VbankExpDate);
		model.addAttribute("roadAddr", roadAddr);
		model.addAttribute("addrDetail", addrDetail);
		model.addAttribute("BuyerName", BuyerName);
		model.addAttribute("BuyerEmail", BuyerEmail);
		model.addAttribute("BuyerTel", BuyerTel);
		model.addAttribute("EncryptData", EncryptData);
		model.addAttribute("toDate", toDate);
		model.addAttribute("INIT", msgMgr.message("smartropay.INIT", locale));
		model.addAttribute("pay", msgMgr.message("smartropay.pay", locale));
		model.addAttribute("mpay", msgMgr.message("smartropay.mpay", locale));
   		//easyPay 웹 결제를 하기 위한  이동
		return "order/smartropay_check";
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
      
      public PointDispItem pointCalc(String storeId, String tel) {
        PointDispItem pointOne = new PointDispItem();
        List<StorePolicy> policyList = this.couponService.getPolicyList(Util.parseInt(storeId), "P");
        int usepoint = 0;
        if (policyList.size() > 0)
          for (StorePolicy one : policyList)
            usepoint = one.getPoint();  
        List<StoreOrderPoint> res = this.couponService.getIssuePointRead(storeId, tel);
        if (res.size() > 0)
          for (StoreOrderPoint one : res) {
            pointOne.setId(one.getId());
            pointOne.setPoint(one.getPointTotal());
          }  
        return pointOne;
      }

}
