package kr.co.paycast.controllers.store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.calc.service.CalculateService;
import kr.co.paycast.models.pay.ContentFile;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.DeviceTask;
import kr.co.paycast.models.pay.OptionalMenuList;
import kr.co.paycast.models.pay.OptionalMenuListDelete;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.ContentService;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.pay.service.MenuService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.self.service.SelfService;
import kr.co.paycast.models.store.StoreCookAlarm;
import kr.co.paycast.models.store.StoreCookTask;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.service.StoreCancelService;
import kr.co.paycast.models.store.service.StoreCookService;
import kr.co.paycast.models.store.service.StoreNumberService;
import kr.co.paycast.models.store.service.StoreOrderService;
import kr.co.paycast.models.store.service.StoreSiteService;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.pay.MenuDispItem;
import kr.co.paycast.viewmodels.pay.MenuGroupItem;
import kr.co.paycast.viewmodels.pay.OptionalMenuItem;
import kr.co.paycast.viewmodels.store.MenuJsonPrintItem;
import kr.co.paycast.viewmodels.store.MenuListJsonPrintItem;
import kr.co.paycast.viewmodels.store.RefillView;
import net.sf.json.JSONException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ���� �ֹ� ���� �ý��� ���� ���� API
 */
@Controller("store-api-controller")
@RequestMapping(value = "/store/api")
public class StoreAPIController {
	private static final Logger logger = LoggerFactory.getLogger(StoreAPIController.class);

	@Autowired
	private SelfService selfService;

	@Autowired
	private StoreSiteService storeSiteService;

	@Autowired
	private StoreOrderService storeOrderService;

	@Autowired
	private StoreCookService storeCookService;

	@Autowired
	private StoreCancelService storeCancelService;

	@Autowired
	private CalculateService calculateService;

	@Autowired
	private ContentService ctntService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreNumberService storeNumberService;

	@Autowired
	private MenuService menuService;
	
	@Autowired
	private MessageManager msgMgr;
	
	@Autowired
	private DeviceService devService;

	/**
	 * 매장 및 메뉴 중 변경된 정보에 대한 변경 수행 명령 내려주는 곳
	 */
	@RequestMapping(value = "/stbStoreInfo", method = RequestMethod.GET)
	public void stbStoreInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {

		String storeId = (String) request.getParameter("storeId");
		String deviceId = (String) request.getParameter("deviceId");

		Document document = DocumentHelper.createDocument();

		document = storeSiteService.getMonListByStoreId(storeId, deviceId, document, session);

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter pw = response.getWriter();

		document.write(pw);
	}
	
	/**
	 * 매장 및 메뉴 중 변경된 정보에 대한 변경 수행 명령 내려주는 곳
	 */
	@RequestMapping(value = "/storeStateInfo", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> stbStoreState(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		boolean success = true;
		String message = "OK";

		String storeId = (String) request.getParameter("storeId");
		
		String deviceId = (String) request.getParameter("deviceId");
		Map<String, Object> info2 = new HashMap<String, Object>();
		info2.put("openType", null);
		info2.put("kioskEnable", null);
		Store store = storeService.getStore(Util.parseInt(storeId));
		if(store == null) {
			success = false;
			
			info.put("data", info2);
			info.put("success", success);
			info.put("message", "요청하신 상점을 찾을 수 없습니다.");
			
			return info;
		}
		
		Device device = devService.getDeviceByUkid(Util.parseString(deviceId));
		if (device == null) {
			
			success = false;
			
			info.put("data", info2);
			info.put("success", success);
			info.put("message", "요청하신 기기를 찾을 수 없습니다.");
			
			return info;
		}
		
		if(store.getId() != device.getStore().getId()){
			
			success = false;
			info.put("data", info2);
			info.put("success", success);
			info.put("message", "상점 정보과 device 정보를 확인하여 주시기 바랍니다.");
			
			return info;
		}
		
		info2.put("openType", store.getOpenType());
		info2.put("kioskEnable",String.valueOf(store.isKioskOrderEnabled()));
		
		info.put("data", info2);
		info.put("success", success);
		info.put("message", message);
	
		return info;
	}

	/**
	 * 매장 및 메뉴 중 변경된 정보에 대한 내용 명령 실행 확인
	 */
	@RequestMapping(value = "/storecomplete", method = RequestMethod.GET)
	public void storeComplete(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		boolean success = false;

		String rcCmdId = (String) request.getParameter("rcCmdId");
		String result = (String) request.getParameter("result");

		logger.info("storecomplete >> rcCmdId  >>> [{}], result  >>> [{}]", rcCmdId, result);
		// �������� N�� ��쿡�� ����
		if (!"N".equals(result)) {
			success = storeSiteService.setMonTaskCommandUpdate(rcCmdId, result, session);
		} else {
			success = true;
		}

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		out.print(success ? "Y" : "N");
		out.close();
	}

	/**
	 * 기기에서 매장 정보 요청 2019.05.26 해당 URL은 사용하지 않는다. - [kdk] 매장 정보 변경에 대한 내용은 XML 형식으로
	 * 내려주던 방식에서 json으로 내려주는 방식으로 변경 2019.05.28 해당 메소드 다시 사용(v1.2에서는 상태 유지)
	 */
	@RequestMapping(value = "/updateStoreInfo", method = RequestMethod.GET)
	public void stbInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String storeId = (String) request.getParameter("storeId");

		Document document = DocumentHelper.createDocument();

		storeSiteService.makeStoreInfoXmlFile(storeId, document);

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter pw = response.getWriter();

		document.write(pw);
	}

	/**
	 * 기기에서 메뉴 정보 요청 2019.05.27 해당 URL은 사용하지 않는다. - [kdk] 매장 정보 변경에 대한 내용은 XML 형식으로
	 * 내려주던 방식에서 json으로 내려주는 방식으로 변경 2019.05.28 해당 메소드 다시 사용(v1.2에서는 상태 유지) 아래 사용은
	 * v1.3 으로 사용 됨
	 */
	@RequestMapping(value = "/updateMenuInfo", method = RequestMethod.GET)
	public void updateMenuInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String deviceId = (String) request.getParameter("deviceId");
		String storeId = (String) request.getParameter("storeId");

		logger.info("updateMenuInfo >> deviceId [{}], storeId [{}]", deviceId, storeId);

		Document documentList = DocumentHelper.createDocument();

		selfService.makeMenuInfoXmlFile(storeId, deviceId, documentList);

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter pw = response.getWriter();

		documentList.write(pw);
	}

	/**
	 * 컨텐츠 동기화 시 STB 존재 컨텐츠 파일 보고
	 */
	@RequestMapping(value = "/dctntreport", method = { RequestMethod.GET, RequestMethod.POST })
	public void dctntReport(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		String storeId = request.getParameter("storeId");
		String completeY = request.getParameter("completeY");
		String completeN = request.getParameter("completeN");

		logger.info("컨텐츠 파일 보고 dctntReport, storeId >> [{}] / completeY >> [{}]", storeId, completeY);
		boolean success = true;
		if (Util.isValid(storeId)) {
			Store store = storeService.getStore(Util.parseInt(storeId));
			if (store != null) {
				List<String> items = Util.tokenizeValidStr(completeY);

				for (String idStr : items) {
					logger.info("fileDownload Check store.getStoreName() [{}] / Y >> [{}]", store.getStoreName(),
							idStr);
					if (Util.isIntNumber(idStr)) {
						ContentFile ctntFile = ctntService.getContentFile(Util.parseInt(idStr));
						if (ctntFile != null && success) {
							ctntFile.setTransferred("Y");

							ctntService.saveOrUpdate(ctntFile);
						} else {
							success = false;
						}
					}
				}

				if (success) {
					items = Util.tokenizeValidStr(completeN);

					for (String idStr : items) {
						logger.info("fileDownload Check store.getStoreName() [{}] / N >> [{}]", store.getStoreName(),
								idStr);
						if (Util.isIntNumber(idStr)) {
							ContentFile ctntFile = ctntService.getContentFile(Util.parseInt(idStr));
							if (ctntFile != null && success) {
								ctntFile.setTransferred("N");

								ctntService.saveOrUpdate(ctntFile);
							} else {
								success = false;
							}
						}
					}
				}
			}
		}

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		out.print(success ? "Y" : "N");
		out.close();
	}

	/**
	 * 주문 내역에 대한 메뉴 내용을 프린트로 Json 형태로 만들어 내려주기
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/printmenu", method = { RequestMethod.GET, RequestMethod.POST })
	public void printmenu(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String deviceId = (String) request.getParameter("deviceId");
		String storeId = (String) request.getParameter("storeId");

//		logger.info("/printmenu deviceId >>> [{}] / storeId >>> [{}]", deviceId, storeId);

		List<MenuJsonPrintItem> list = storeOrderService.makeMenuListPrint(storeId, deviceId);
		try {
			JSONArray jArray = new JSONArray();//배열이 필요할때
			if (list.size() > 0) {
				for (MenuJsonPrintItem item : list) {
					List<String> addList = new ArrayList<String>();
					JSONObject menuObject = new JSONObject();//배열 내에 들어갈 json
					// ���� �Է�
					menuObject.put("recommand", item.getRecommand());
					menuObject.put("name", item.getStoreName());
					menuObject.put("date", item.getOrderDate());
					menuObject.put("number", item.getOrderNumber());
					if ("RF".equals(item.getPayment())) {
						menuObject.put("amt", "0");
					} else {
						menuObject.put("amt", item.getGoodsAmt());
					}
					menuObject.put("payment", item.getPayment());
					menuObject.put("cancel", item.getMenuCancelYN());

					menuObject.put("orderTable", item.getOrderTable());
					menuObject.put("tel", item.getTel());
					menuObject.put("orderType", item.getOrderType());
					menuObject.put("roadAddr", item.getRoadAddr());
					menuObject.put("addrDetail", item.getAddrDetail());
					menuObject.put("storeMsg", item.getStoreMsg());
					menuObject.put("deliPrice", item.getDeliPrice());
					menuObject.put("deliMsg", item.getDeliMsg());
					menuObject.put("bookingTime", item.getBookingTime());

					JSONArray jMenuListArray = new JSONArray();//배열이 필요할때
					List<MenuListJsonPrintItem> orderMenu = item.getOrderMenu();
					if(orderMenu.size() > 0){
						for(MenuListJsonPrintItem itemmenu : orderMenu){
							JSONObject menuListObject = new JSONObject();//배열 내에 들어갈 json
							
							menuListObject.put("id", itemmenu.getProductID());
							menuListObject.put("name", itemmenu.getProductName());
							menuListObject.put("cnt", itemmenu.getOrderCount());
							menuListObject.put("price", itemmenu.getOrderPrice());
							menuListObject.put("packing", itemmenu.getOrderMenuPacking());
							// 1. ","으로 split 
							// 2. "_"으로 split
							// 3. "\\("으로 split
							// 4. 추가 선택에 대한 메뉴 명만
							String essText = "";
							if (!Util.isNotValid(itemmenu.getEss())) {
								String[] menu1 = itemmenu.getEss().split(",");
								for (int t = 0; t < menu1.length; t++) {
									if (!Util.isNotValid(menu1[t])) {
										String[] menu2 = menu1[t].split("_");
										if (menu2.length > 0) {
											String[] menu3 = menu2[1].split("\\(");
											if (!"".equals(essText) && menu3.length > 1) {
												essText += ",";
											}
											essText += PayUtil.separationistName(menu3);
										}
									}
								}
							}
							// 1. ","으로 split 
							// 2. "_"으로 split
							// 3. "\\|\\|"으로 split
							// 4. "\\("으로 split
							// 5. 추가 선택에 대한 메뉴 명만
							String addText = "";
							if (!Util.isNotValid(itemmenu.getAdd())) {
								String[] menu1 = itemmenu.getAdd().split(",");
								for (int t = 0; t < menu1.length; t++) {
									if (!Util.isNotValid(menu1[t])) {
										String[] menu2 = menu1[t].split("_");
										if (menu2.length > 0) {
											String[] menu3 = menu2[1].split("\\|\\|");
											if (menu3.length > 0) {
												for (int ttt = 0; ttt < menu3.length; ttt++) {
													String[] menu4 = menu3[ttt].split("\\(");
													if (!"".equals(addText) && menu4.length > 1) {
														addText += "||";
													}
													addText += PayUtil.separationistName(menu4);
												}
											}
										}
									}
								}
							}

							menuListObject.put("ess", essText);
							menuListObject.put("add", addText);
							if (!Util.isNotValid(itemmenu.getAdd())) {
								addList.add(itemmenu.getAdd());
							}
							jMenuListArray.add(menuListObject);
						}
					}
					menuObject.put("orderMenu", jMenuListArray);

					// Array�� �Է�
					jArray.add(menuObject);
				}
			} else {
				JSONObject menuObject = new JSONObject();//배열 내에 들어갈 json
				menuObject.put("recommand", 0);
				jArray.add(menuObject);
			}

			PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));

			response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
			response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");

			out.print(jArray.toString());
			out.close();

		} catch (JSONException e) {
        	logger.error("JSONException > printmenu", "json 생성시 오류");
		}
	}

	/**
	 * 프린트 된 내용 완료 보고
	 */
	@RequestMapping(value = "/printcomplete", method = { RequestMethod.GET, RequestMethod.POST })
	public void printcomplete(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		String storeId = request.getParameter("storeId");
		String completeY = request.getParameter("completeY");
		String cancelYN = request.getParameter("cancelYN");

		completeY = URLDecoder.decode(completeY, "UTF-8");
		cancelYN = URLDecoder.decode(cancelYN, "UTF-8");

		boolean success = true;

		if (Util.isValid(storeId)) {
			Store store = storeService.getStore(Util.parseInt(storeId));
			if (store != null) {
				List<String> items = Util.tokenizeValidStr(completeY);
				List<String> cancelYNitem = Util.tokenizeValidStr(cancelYN);
				int i = 0;
				for (String idStr : items) {

					if (Util.isIntNumber(idStr)) {
						StoreOrder condFile = storeOrderService.getDplyStoreOrderPrint(Util.parseInt(idStr));

						logger.info("idStr >>> [{}]", idStr);
						logger.info("cancelYN >>> [{}]", (String) cancelYNitem.get(i));

						if (condFile != null && success) {
							condFile.setOrderPrint("Y");
							if ("Y".equals((String) cancelYNitem.get(i))) {
								condFile.setOrderCancelPrint("Y");
							}
							condFile.touchWho(session);

							storeOrderService.saveOrUpdate(condFile);
						} else {
							success = false;
						}
					}
					i++;
				}
			}
		} else {
			success = false;
		}

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		out.print(success ? "Y" : "N");
		out.close();
	}

	/**
	 * kiosk에서 던져준 데이터를 가지고 (결제 데이터)
	 */
	@RequestMapping(value = "/kioskpaymentinfo", method = { RequestMethod.GET, RequestMethod.POST })
	public void kioskpaymentinfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		logger.info("/kioskpaymentinfo >> 키오스크에서 결제 완료 데이터 저장 시작");

		BufferedReader reader = null;
		String line = null;
		boolean success = false;
		try {
			reader = request.getReader();
			String ctnt = "";
			logger.info(" ----------------------------");
			while ((line = reader.readLine()) != null) {
				logger.info(line);
				ctnt += line;
			}
			logger.info("--------------------------------------- end");

			JSONParser jsonParser = new JSONParser();
			JSONArray objectList = (JSONArray) jsonParser.parse(ctnt);

			// 2019.11.13 리필 추가로 인하영 키오스크 결제 시 변수(trtype) 추가
			// trtype : AD : 선불, CA : 취소, RF : 리필, DE : 후불

			MenuJsonPrintItem jsonMenu = new MenuJsonPrintItem();
			if (objectList.size() > 0) {
				for (int t = 0; t < objectList.size(); t++) {
					JSONObject object = (JSONObject) objectList.get(t);
					if (object != null) {
						String storeIdpay = (String) object.get("storeId"); // 매장ID
						String tid = (String) object.get("tid"); // 거래고유번호
						String mid = (String) object.get("mid"); // 가맹점 번호
						String totalindex = (String) object.get("cnt"); // 주문 상품 개수
						String goodsAmt = (String) object.get("amt"); // 메뉴 주문 금액
						String orderNumber = (String) object.get("num"); // 주문 번호
						String orderDate = (String) object.get("date"); // 주문 날짜 시간
						String authCode = (String) object.get("authCode"); // 승인 번호
						String fnCd = (String) object.get("fnCd"); // 발급사
						String fnName = (String) object.get("fnName"); // 발급사명
						String fnCd1 = (String) object.get("fnCd1"); // 매입사
						String fnName1 = (String) object.get("fnName1"); // 매입사명
						String paOrderId = (String) object.get("paOrderId"); // 리필 시 사용되는 부모 메뉴 ID
						String deviceId = (String) object.get("deviceId"); // 기기고유 번호
						String payMethod = (String) object.get("payMethod"); // 결제 내용(CARD)

						// 전화번호는 알림톡과 리필일 경우에 사용 되는 전화 번호
						String tel = (String) object.get("tel"); // 결제 전화 번호
						String trtype = (String) object.get("trtype"); // trtype kiosk 사용 타입(AD : 선불, CA : 취소, RF : 리필,
																		// DE : 후불)

						float goodsAmtFl = Util.parseFloat(goodsAmt);
						int goodsAmtint = (int) goodsAmtFl;

						jsonMenu.setRecommand(storeIdpay);
						jsonMenu.setTid(tid);
						jsonMenu.setMid(mid);
						jsonMenu.setTotalindex(totalindex);
						jsonMenu.setGoodsAmt(String.valueOf(goodsAmtint));
						jsonMenu.setOrderNumber(orderNumber);
						jsonMenu.setOrderDate(orderDate);
						jsonMenu.setAuthCode(authCode);
						jsonMenu.setFnCd(fnCd);
						jsonMenu.setFnName(fnName);
						jsonMenu.setFnCd1(fnCd1);
						jsonMenu.setFnName1(fnName1);
						jsonMenu.setCatId(deviceId);
						jsonMenu.setPayMethod(payMethod);
						jsonMenu.setTel(Util.parseString(tel));
						jsonMenu.setPaOrderId(paOrderId);

						jsonMenu.setPayment(trtype);

						List<MenuListJsonPrintItem> menuJson = new ArrayList<MenuListJsonPrintItem>();
						JSONArray orderMenuList = (JSONArray) object.get("menu");
						if (orderMenuList.size() > 0) {
							for (int i = 0; i < orderMenuList.size(); i++) {
								MenuListJsonPrintItem menuList = new MenuListJsonPrintItem();
								JSONObject orderMenu = (JSONObject) orderMenuList.get(i);
								String productID = (String) orderMenu.get("id");
								String productName = (String) orderMenu.get("name");
								String orderCount = (String) orderMenu.get("count");
								String orderPrice = (String) orderMenu.get("price");
								String[] price = orderPrice.split("\\.");
								if (price.length > 1) {
									orderPrice = price[0];
								}
								String orderMenuPacking = (String) orderMenu.get("pack");
								String ess = (String) orderMenu.get("ess");
								String add = (String) orderMenu.get("add");

								menuList.setProductID(productID);
								menuList.setProductName(productName);
								menuList.setOrderCount(orderCount);
								menuList.setOrderPrice(orderPrice);
								menuList.setOrderMenuPacking(orderMenuPacking);
								menuList.setEss(ess);
								menuList.setAdd(add);

								menuJson.add(menuList);
							}
						}
						jsonMenu.setOrderMenu(menuJson);
					}
				}
			}
			logger.info("/kioskpaymentinfo >> 데이터 생성 완료 후 저장 시작 ");
			
			success = storeOrderService.kioskPayMent(jsonMenu);
			
			logger.info("/kioskpaymentinfo >> 데이터 생성 완료 후 저장 완료 ");
			

		} catch (Exception e) {
			logger.error("updatePick", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
					logger.error("updatePick", ex);
				}
			}
		}

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		 logger.info("/kioskpaymentinfo >> 키오스크에서 결제 완료 데이터 저장 종료");
	        
	        out.print(success ? "Y" : "N");
	        out.close();
	    }
		
		/**
		 * 대기자 목록을 조회 한다. (주문 목록의 대기 수를 조회 하여 보여준다.)
		 */
	@RequestMapping(value = {"/cookStayCntRead"}, method = {RequestMethod.GET})
	public @ResponseBody Map<String, Object> cookStayCntRead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		request.setCharacterEncoding("utf-8");
		String storeId = request.getParameter("storeId");
		String deviceId = request.getParameter("deviceId");
		boolean success = true;
		String localMessage = "Ok";
		int stayCnt = this.storeCookService.getStayCntAPI(Util.parseInt(storeId, 0), deviceId);
		if (stayCnt == 9999) {
			success = false;
			info.put("data", stayCnt);
			info.put("success", success);
			info.put("message", "요청하신 상점 및 기기를 찾을 수 없습니다.");
			return info;
		} else {
			info.put("data", stayCnt);
			info.put("success", success);
			info.put("message", localMessage);
			return info;
		}
	}

	/**
	 * DID 중 변경된 정보에 대한 변경 수행 명령 내려주는 곳 
	 */
	@RequestMapping(value = "/storedidinfo", method = RequestMethod.GET)
	public void storeDIDInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String storeId = (String) request.getParameter("storeId");
		String deviceId = (String) request.getParameter("deviceId");

		Document document = DocumentHelper.createDocument();
		document = storeCookService.getTaskByStoreIdDeviceId(Util.parseInt(storeId), deviceId, document);

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter pw = response.getWriter();

		document.write(pw);
	}

	/**
	 * DID 명령어 정상 확인
	 */
	@RequestMapping(value = "/storedidreport", method = RequestMethod.GET)
	public void storedidreport(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		String storeId = request.getParameter("storeId");
		String completeY = request.getParameter("completeY");

		completeY = URLDecoder.decode(completeY, "UTF-8");

		logger.info("storedidreport >>> storeId [{}]", storeId);
		logger.info("storedidreport >>> completeY [{}]", completeY);

		boolean success = true;

		if (Util.isValid(storeId)) {
			Store store = storeService.getStore(Util.parseInt(storeId));
			if (store != null) {
				List<String> items = Util.tokenizeValidStr(completeY);

				for (String idStr : items) {
					if (Util.isIntNumber(idStr)) {
						StoreCookTask condFile = storeCookService.getStoreCookTask(Util.parseInt(idStr));

						if (condFile != null && success) {
							logger.info("condFile >>> condFile [{}]", condFile.getId());

							condFile.setStatus("Y");
							condFile.touchWho(session);

							storeCookService.saveOrUpdate(condFile);
						} else {
							success = false;
							logger.info("storedidreport >> condFile >>> success [{}]", success);
						}
					}
				}
			}
		} else {
			success = false;
		}

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		out.print(success ? "Y" : "N");
		out.close();
	}

	/**
	 * 주문 알림 된 내용 List를 보내준다(DID 목록 내려주기 . XML 형식)
	 */
	@RequestMapping(value = "/cookAlarmListRead", method = RequestMethod.GET)
    public void cookAlramListRead(HttpServletRequest request, 
    		HttpServletResponse response) throws ServletException, IOException {
    	
		String storeId = (String) request.getParameter("storeId");
		String deviceId = (String) request.getParameter("deviceId");
		
		Document document = DocumentHelper.createDocument();
		document = storeCookService.getCookAlramListByStoreIdDeviceId(Util.parseInt(storeId), deviceId, document);
		
        response.setHeader("Cache-Control", "no-store");              // HTTP 1.1
        response.setHeader("Pragma", "no-cache, must-revalidate");    // HTTP 1.0
        response.setContentType("text/xml;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter pw = response.getWriter();
        document.write(pw);
    }
	
	/**
	 * 주문 알림 된 내용 List를 보내준다(DID 목록 내려주기 . XML 형식)
	 */
	@RequestMapping(value = "/cookComListRead", method = RequestMethod.GET)
	public void cookComListRead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String storeId = (String) request.getParameter("storeId");
		String deviceId = (String) request.getParameter("deviceId");
		Document document = DocumentHelper.createDocument();

		document = storeCookService.getCookComListByStoreIdDeviceId(Util.parseInt(storeId), deviceId, document);

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter pw = response.getWriter();

		document.write(pw);
	}

	   /**
		 * DID 정상확인 내용 완료 보고
		 */
	@RequestMapping(value = "/cookdispcom", method = RequestMethod.GET)
	public void cookdispcom(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		String storeId = request.getParameter("storeId");
		String completeY = request.getParameter("completeY");

		completeY = URLDecoder.decode(completeY, "UTF-8");

		boolean success = true;

		if (Util.isValid(storeId)) {
			Store store = storeService.getStore(Util.parseInt(storeId));
			if (store != null) {
				List<String> items = Util.tokenizeValidStr(completeY);

				for (String idStr : items) {
					if (Util.isIntNumber(idStr)) {
						StoreCookAlarm condFile = storeCookService.getDplyStoreCookAlarm(Util.parseInt(idStr));

						if (condFile != null && success) {
							condFile.setOrderDIDComplete("Y");
							condFile.touchWho(session);

							storeCookService.saveOrUpdate(condFile);
						} else {
							success = false;
						}
					}
				}
			}
		} else {
			success = false;
		}

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		out.print(success ? "Y" : "N");
		out.close();
	}

	  // ------------------------------ 취소 로직 추가 ------------------------------
    
    
		/**
		 * 주문 한 메뉴를 취소 하기위해서 승인번호가 맞는지 체크 하는 로직
		 */
	@RequestMapping(value = "/cancelVerifiCheck", method = RequestMethod.GET)
	public void cancelVerifiCheck(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String storeId = (String) request.getParameter("storeId");
		String deviceId = (String) request.getParameter("deviceId");
		String cancelCode = (String) request.getParameter("cancelCode");
		Document document = DocumentHelper.createDocument();

		document = storeCancelService.checkVerifiCodebyStoreIdVerifiCodeDeviceID(Util.parseInt(storeId), deviceId,
				cancelCode, document, "K");

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter pw = response.getWriter();

		document.write(pw);
	}

	/**
	 * 키오스크 주문 취소 완료시 사용
	 */
	@RequestMapping(value = "/cancelSuccess", method = { RequestMethod.GET, RequestMethod.POST })
	public void cancelSuccess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		BufferedReader reader = null;
		String line = null;
		boolean success = false;
		try {
			reader = request.getReader();
			String ctnt = "";
			logger.info(" cancelSuccess --------------------------- start");
			while ((line = reader.readLine()) != null) {
				logger.info(line);
				ctnt += line;
			}
			logger.info(" cancelSuccess --------------------------- end");

			JSONParser jsonParser = new JSONParser();
			JSONArray objectList = (JSONArray) jsonParser.parse(ctnt);

			success = storeCancelService.cancelSuccessbyStoreIdOrderIdDeviceID(objectList);
		} catch (Exception e) {
			logger.error("updatePick", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
					logger.error("updatePick", ex);
				}
			}
		}

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		out.print(success ? "Y" : "N");
		out.close();
	}


	// ------------------------------ 키오스크 마감 정산 START ------------------------------
	
	@RequestMapping(value = "/operEnd", method = { RequestMethod.GET, RequestMethod.POST })
    public void operEnd(HttpServletRequest request, 
    		HttpServletResponse response) throws ServletException, IOException {
    	
		String storeId = (String) request.getParameter("storeId");
		
		boolean success = calculateService.operEnd(storeId, null);
		
        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
        
        response.setHeader("Cache-Control", "no-store");              // HTTP 1.1
        response.setHeader("Pragma", "no-cache, must-revalidate");    // HTTP 1.0
        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        out.print(success ? "Y" : "N");
        out.close();
    }
	// ------------------------------ 키오스크 마감 정산 END ------------------------------ 
	
	
	/**
	 * 주문 번호 중앙 관리를 위한 키오스크용 API 
	 */
	@RequestMapping(value = {"/ordernum"}, method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String, Object> ordernum(HttpServletRequest request, Locale locale, HttpSession session)
			throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		request.setCharacterEncoding("utf-8");
		String storeId = request.getParameter("storeId");
		String deviceId = request.getParameter("deviceId");
		boolean success = true;
		int ordernum = this.storeNumberService.getOrderNum(Util.parseInt(storeId, 0), deviceId, session, locale);
		String localMessage = "Ok";
		Store store = this.storeService.getStore(Util.parseInt(storeId, 0));
		if (store == null) {
			success = false;
			info.put("success", success);
			info.put("data", ordernum);
			info.put("message", this.msgMgr.message("error.store", locale));
			return info;
		} else {
			Device device = this.devService.getDeviceByUkid(Util.parseString(deviceId));
			if (device == null) {
				success = false;
				info.put("success", success);
				info.put("data", ordernum);
				info.put("message", this.msgMgr.message("error.deiviceId", locale));
				return info;
			} else if (store.getId() != device.getStore().getId()) {
				success = false;
				info.put("success", success);
				info.put("data", ordernum);
				info.put("message", this.msgMgr.message("error.storeDeiviceId", locale));
				return info;
			} else if (ordernum != 9999) {
				info.put("success", success);
				info.put("data", ordernum);
				info.put("message", localMessage);
				return info;
			} else {
				return info;
			}
		}
	}
	
	/**
	 * 리필 가능 체크 키오스크용 API 
	 */
	@RequestMapping(value = "/refillCheck", method = { RequestMethod.GET, RequestMethod.POST })
	public void refillCheck(HttpServletRequest request, HttpServletResponse response, Locale locale,
			HttpSession session) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		String storeId = (String) request.getParameter("storeId");
		String deviceId = (String) request.getParameter("deviceId");
		String viewTel = (String) request.getParameter("tel");
		String rfMenuId = (String) request.getParameter("menuId");

		viewTel = viewTel.replace("-", "");
		logger.info("/refillCheck > storeId >>> [{}], tel >>> [{}]", storeId, viewTel);
		logger.info("/refillCheck > storeId >>> [{}], deviceId >>> [{}]", storeId, deviceId);
		logger.info("/refillCheck > storeId >>> [{}], menuId >>> [{}]", storeId, rfMenuId);

		RefillView refillView = storeOrderService.refillbyOrderListKiosk(Util.parseInt(storeId, 0), deviceId, viewTel,
				rfMenuId);

    	JSONObject menuObject = new JSONObject();//배열 내에 들어갈 json
		menuObject.put("RFyn", refillView.getRFyn());
		menuObject.put("paOrderId", refillView.getPaOrderId());
		menuObject.put("RFCnt", refillView.getRFCnt());
		logger.info("/refillCheck > storeId >>> [{}], refillView.getRFyn() >>> [{}]", storeId, refillView.getRFyn());
		logger.info("/refillCheck > storeId >>> [{}], refillView.getPaOrderId() >>> [{}]", storeId,
				refillView.getPaOrderId());
		logger.info("/refillCheck > storeId >>> [{}], refillView.getRFCnt() >>> [{}]", storeId, refillView.getRFCnt());

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		out.print(menuObject.toString());
		out.close();
	}


	// ------------------------------ 바나나 포스 ------------------------------ 시작
	
	
	/**
	 * 바나나 포스 연동 - 주문 결제 내역 상세 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pospayment", method = { RequestMethod.GET, RequestMethod.POST })
	public void pospayment(HttpServletRequest request, Locale locale, HttpServletResponse response)
			throws ServletException, IOException {

		String storeKey = (String) request.getParameter("store");
		int mins = Util.parseInt((String) request.getParameter("min"), 10);
		if (mins > 1440) {
			mins = 1440;
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -1 * mins);

		Store store = storeService.getStoreByStoreKey(storeKey);
		logger.info("storeKey [{}]", storeKey);
		logger.info("mins >>> [{}] cal.getTime() >>> [{}]", mins, cal.getTime());

		Document document = DocumentHelper.createDocument();
		List<MenuJsonPrintItem> list = storeOrderService.makePOSMenuList(storeKey, cal.getTime());
		// 1. 파일 다운로드 될 XML을 생성한다.
		Element root = document.addElement("PayCast").addAttribute("version", "1.0").addAttribute("generated",
				new Date().toString());
		try {
			if (list.size() > 0) {
				for (MenuJsonPrintItem item : list) {
					Element catagoryElement = root.addElement("Catagory");
					catagoryElement.addElement("storeName").addText(store.getBizName());  //결제 매장명
					if(store.getPhone()!=null) { //결제 매장 번호
						catagoryElement.addElement("storeTel").addText(store.getPhone()); 
					}else {
						catagoryElement.addElement("storeTe").addText("");  
					}
					
					catagoryElement.addElement("appNum").addText(item.getRecommand());  //결제내역의 고유번호 (orderID)
					
					// 결제정보(AD:선불/DE:후불/RF:리필) && 주문선택(S:매장/P:포장/D:배달/I:리필)
					if ("RF".equals(item.getPayment()) && "I".equals(item.getOrderType())) {
						catagoryElement.addElement("price").addText("0"); //결제금액(총금액)
					} else {
						catagoryElement.addElement("price").addText(item.getGoodsAmt()); //결제금액(총금액)
					}
					
					if ("D".equals(item.getOrderType())) {
						if (item.getDeliPrice() != null)
							catagoryElement.addElement("deliveryPrice").addText(item.getDeliPrice());
						else
							catagoryElement.addElement("deliveryPrice").addText("0");
					} else {
						catagoryElement.addElement("deliveryPrice").addText("0");
					}
					catagoryElement.addElement("date").addText(item.getOrderDate()); //결제시간
					catagoryElement.addElement("ordNum").addText(item.getOrderNumber()); //주문번호(고객 주문번호)
					String info = "S";
					if ("Y".equals(item.getMenuCancelYN())) {
						info = "C";
					}
					catagoryElement.addElement("appInfo").addText(info); //결제 정보(S:결제완료 / C:결제취소)
					if(item.getOrderTable()!= null){
						catagoryElement.addElement("table").addText(item.getOrderTable()); //테이블 번호						
					}else{
						catagoryElement.addElement("table").addText("0000"); //테이블 번호	
					}
					catagoryElement.addElement("device").addText(item.getDevice());
					catagoryElement.addElement("type").addText(item.getOrderType());
					catagoryElement.addElement("parentAppNum").addText(item.getPaOrderId());
					catagoryElement.addElement("payment").addText(item.getPayment());
					catagoryElement.addElement("tel").addText(item.getTel());
					catagoryElement.addElement("roadAddr").addText(item.getRoadAddr());
					catagoryElement.addElement("addrDetail").addText(item.getAddrDetail());
					catagoryElement.addElement("storeMsg").addText(item.getStoreMsg());
					catagoryElement.addElement("deliMsg").addText(item.getDeliMsg());
					catagoryElement.addElement("bookingTime").addText(item.getBookingTime());

					List<MenuListJsonPrintItem> orderMenu = item.getOrderMenu();
					if (orderMenu.size() > 0) {
						for (MenuListJsonPrintItem itemmenu : orderMenu) {
							Element menuElement = catagoryElement.addElement("Menu");
							menuElement.addElement("code").addText(itemmenu.getProductID());
							menuElement.addElement("count").addText(itemmenu.getOrderCount());
							menuElement.addElement("name").addText(itemmenu.getProductName());
							menuElement.addElement("price").addText(itemmenu.getOrderPrice());
							String packing = "N";
							if ("1".equals(itemmenu.getOrderMenuPacking())) {
								packing = "Y";
							}
							menuElement.addElement("packing").addText(packing);
							// 1. ","으로 split 
							// 2. "_"으로 split
							// 3. "\\("으로 split
							// 4. 추가 선택에 대한 메뉴 명만
							if (!Util.isNotValid(itemmenu.getEss())) {
								String[] menu1 = itemmenu.getEss().split(",");
								for (int t = 0; t < menu1.length; t++) {
									if (!Util.isNotValid(menu1[t])) {
										String[] menu2 = menu1[t].split("_");
										if (menu2.length > 0) {
											List<OptionalMenuList> optMenuList = menuService
													.getOptionalMenuListByOptionId(Integer.parseInt(menu2[0]));
											String[] menu3 = menu2[1].split("\\(");
											if (menu3.length > 0) {
												String viewName = PayUtil.separationistName(menu3);
												String viewPrice = PayUtil.separationistPrice(menu3);
												for (OptionalMenuList optMeList : optMenuList) {
													String optMeListId = String.valueOf(optMeList.getId());
													String optMeListName = optMeList.getName();
													String optMeListPrice = String.format("%.0f", optMeList.getPrice());
													if (viewName.equals(optMeListName)
															&& viewPrice.equals(optMeListPrice)) {
														Element menuOptionElement = menuElement.addElement("Option");
														menuOptionElement.addElement("code").addText(optMeListId);
														menuOptionElement.addElement("count").addText("1");
														menuOptionElement.addElement("name").addText(optMeListName);
														menuOptionElement.addElement("price").addText(optMeListPrice);
														menuOptionElement.addElement("gubun").addText("0");
													}
												}
											}
										}
									}
								}
							}
							// 1. ","으로 split 
							// 2. "_"으로 split
							// 3. "\\|\\|"으로 split
							// 4. "\\("으로 split
							// 5. 추가 선택에 대한 메뉴 명만
							if (!Util.isNotValid(itemmenu.getAdd())) {
								String[] menu1 = itemmenu.getAdd().split(",");
								for (int t = 0; t < menu1.length; t++) {
									if (!Util.isNotValid(menu1[t])) {
										String[] menu2 = menu1[t].split("_");
										if (menu2.length > 0) {
											List<OptionalMenuList> optMenuList = menuService
													.getOptionalMenuListByOptionId(Integer.parseInt(menu2[0]));

											String[] menu3 = menu2[1].split("\\|\\|");
											if (menu3.length > 0) {
												for (int ttt = 0; ttt < menu3.length; ttt++) {
													String[] menu4 = menu3[ttt].split("\\(");
													String viewName = PayUtil.separationistName(menu4);
													String viewPrice = PayUtil.separationistPrice(menu4);
													for (OptionalMenuList optMeList : optMenuList) {
														String optMeListId = String.valueOf(optMeList.getId());
														String optMeListName = optMeList.getName();
														String optMeListPrice = String.format("%.0f",
																optMeList.getPrice());
														if (viewName.equals(optMeListName)
																&& viewPrice.equals(optMeListPrice)) {
															Element menuOptionElement = menuElement
																	.addElement("Option");
															menuOptionElement.addElement("code").addText(optMeListId);
															menuOptionElement.addElement("count").addText("1");
															menuOptionElement.addElement("name").addText(optMeListName);
															menuOptionElement.addElement("price")
																	.addText(optMeListPrice);
															menuOptionElement.addElement("gubun").addText("1");
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				root.addAttribute("response", "0000");
				//조회된 값이 없습니다. 
				root.addAttribute("errcode", "0001");
			}

		} catch (Exception e) {
			logger.error("error {}", e);
			logger.error("XML Exception > pospayment", "XML ������ ����");

			root.addAttribute("response", "9999");
			root.addAttribute("errcode", "9999");
		}

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter pw = response.getWriter();

        logger.info("POS XML 생성 >> 종료  storeKey[{}]", storeKey);

		document.write(pw);
	}


	/**
	 * 바나나 포스 연동 - 메뉴 내려주기 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/posmenu", method = { RequestMethod.GET, RequestMethod.POST })
	public void posmenu(HttpServletRequest request, Locale locale,
			HttpServletResponse response) throws ServletException, IOException {
		
		String storeKey = Util.parseString((String)request.getParameter("store"));
		logger.info("storeKey >>> [{}]", storeKey);
		
		Document document = DocumentHelper.createDocument();
		// 1. 파일 다운로드 될 XML을 생성한다.
		Element root = document.addElement("PayCast")
				.addAttribute("version", "1.0")
				.addAttribute("generated", new Date().toString());
		
		Store res = storeService.getStoreByStoreKey(storeKey);
    	if (res != null) {
			List<MenuGroupItem> menuGroup = menuService.getAllMenusPos(res.getId());
			if(menuGroup.size() > 0){
				for(MenuGroupItem menuGroupOne : menuGroup){
					Element catagoryElement = root.addElement("Catagory");
					catagoryElement.addElement("seq").addText(String.valueOf(menuGroupOne.getSiblingSeq())); //그룹 순서
					catagoryElement.addElement("code").addText(String.valueOf(menuGroupOne.getId())); //그룹 ID
					catagoryElement.addElement("name").addText(menuGroupOne.getName()); //그룹 명
					catagoryElement.addElement("status").addText("D".equals(menuGroupOne.getStatus())? menuGroupOne.getStatus(): "U"); //그룹 상태

					List<MenuDispItem> menuList = menuGroupOne.getDMenus();
					if (menuList.size() > 0) {
						for (MenuDispItem menuOne : menuList) {
							Element menuElement = catagoryElement.addElement("Menu");
							menuElement.addElement("seq").addText(String.valueOf(menuOne.getSiblingSeq()));
							menuElement.addElement("code").addText(String.valueOf(menuOne.getId()));
							menuElement.addElement("name").addText(String.valueOf(menuOne.getName()));
							menuElement.addElement("price").addText(String.valueOf((int) menuOne.getPrice()));
							menuElement.addElement("File").addText(menuOne.getImgPathFilename());
							menuElement.addElement("des").addText(Util.parseString(menuOne.getIntro()));
							String newGunbun = "";
							String popularGunbun = "";
							// 리필 : infinity(무제한) / limit(제한) / none(아님)
							// 2019.11.12 개발할 경우 제한에 대한 내요이 없으므로 무제한 아니면 none으로 처리
							String refillGunbun = "none";
							switch (menuOne.getFlagType()) {
							case "N":
								newGunbun = "true";
								break;
							case "R":
								popularGunbun = "true";
								break;
							case "I":
								refillGunbun = "infinity";
								break;
							default:
								break;
							}

							menuElement.addElement("new").addText(newGunbun);
							menuElement.addElement("popular").addText(popularGunbun);
							menuElement.addElement("refill").addText(refillGunbun);
							menuElement.addElement("soldout")
									.addText((menuOne.isSoldOut()) ? String.valueOf(menuOne.isSoldOut()) : "");
							menuElement.addElement("status")
									.addText("D".equals(menuOne.getStatus()) ? menuOne.getStatus() : "U"); // �޴� ����

							List<OptionalMenuItem> optManMenu = menuOne.getManMenus();
							if (optManMenu.size() > 0) {
								int ii = 1;
								for (OptionalMenuItem optManMenuOne : optManMenu) {
									Element optionElement = menuElement.addElement("Option");
									optionElement.addElement("code").addText(String.valueOf(optManMenuOne.getId()));
									optionElement.addElement("seq").addText(String.valueOf(ii));
									optionElement.addElement("name").addText(optManMenuOne.getName());
									optionElement.addElement("gubun").addText("0");
									String useMenuSt = "U";
									if ("D".equals(menuOne.getStatus())) {
										useMenuSt = "D";
									}
									optionElement.addElement("status").addText(useMenuSt);

									HashMap<String, String> menuUseMap = new HashMap<String, String>();
									//선택 옵션인 필수 옵션이 사용하는지 사용하지 않는지 확인하고 위해서 사용
									List<String> menuStrList = Util.tokenizeValidStr(optManMenuOne.getMenus(), ",");
									if (menuStrList.size() > 0) {
										for (String menuStrOne : menuStrList) {
											logger.info("menuStrOne [{}]", menuStrOne);

											String[] menuUse = menuStrOne.split(" ");
											if (menuUse.length > 3) {
												logger.info(
														"menuUse[menuUse.length-2] [{}], menuUse[menuUse.length-1] [{}]",
														menuUse[menuUse.length - 2], menuUse[menuUse.length - 1]);

												menuUseMap.put(menuUse[menuUse.length - 2],
														menuUse[menuUse.length - 1]);
											}
										}
									}

									LinkedHashMap<String, OptionalMenuList> lhm = optManMenuOne.getOptMenuChoose();
									Iterator<String> keyData = lhm.keySet().iterator();
									while (keyData.hasNext()) {
										String k = (String) keyData.next();
										OptionalMenuList v = (OptionalMenuList) lhm.get(k);

										Element opListElement = optionElement.addElement("OpList");
										opListElement.addElement("code").addText(String.valueOf(v.getId()));
										opListElement.addElement("name").addText(v.getName());
										opListElement.addElement("price").addText(String.format("%.0f", v.getPrice()));

										String useStatus = (String) menuUseMap.get(String.valueOf(v.getId()));
						    			if(!Util.isNotValid(useStatus)){
						    				opListElement.addElement("status").addText(useStatus); //메뉴 상태가 삭제일 경우 옵션도 모두 삭제
						    			}else{
						    				opListElement.addElement("status").addText("Z"); //메뉴 상태가 삭제일 경우 옵션도 모두 삭제
						    			}
					                }
									 
					                LinkedHashMap<String, OptionalMenuListDelete> hm2 = optManMenuOne.getOptMenuDeleteChoose();
					                Iterator<String> keyData2 = hm2.keySet().iterator();
					                while (keyData2.hasNext()) {
					                	String k = (String) keyData2.next();
					                	OptionalMenuListDelete v = (OptionalMenuListDelete) hm2.get(k);
					                	
					                	Element opListElement = optionElement.addElement("OpList");
					                	opListElement.addElement("code").addText(String.valueOf(v.getOptListId()));
					                	opListElement.addElement("name").addText(v.getName());
					                	opListElement.addElement("price").addText(String.format("%.0f", v.getPrice()));
					                	opListElement.addElement("status").addText(menuOne.getStatus()); //메뉴 상태가 삭제일 경우 옵션도 모두 삭제
									}
								}
							}
							List<OptionalMenuItem> optOtpMenu = menuOne.getOptMenus();
							if (optOtpMenu.size() > 0) {
								int ii = 1;
								for (OptionalMenuItem optManMenuOne : optOtpMenu) {
									Element optionElement = menuElement.addElement("Option");
									optionElement.addElement("code").addText(String.valueOf(optManMenuOne.getId()));
									optionElement.addElement("seq").addText(String.valueOf(ii));
									optionElement.addElement("name").addText(optManMenuOne.getName());
									optionElement.addElement("gubun").addText("1");
									String useMenuSt = "U";
									if ("D".equals(menuOne.getStatus())) {
										useMenuSt = "D";
									}
									optionElement.addElement("status").addText(useMenuSt);

									HashMap<String, String> menuUseMap = new HashMap<String, String>();
									//선택 옵션인 필수 옵션이 사용하는지 사용하지 않는지 확인하고 위해서 사용
									List<String> menuStrList = Util.tokenizeValidStr(optManMenuOne.getMenus(), ",");
									if (menuStrList.size() > 0) {
										for (String menuStrOne : menuStrList) {
											String[] menuUse = menuStrOne.split(" ");
											if (menuUse.length > 3) {
												logger.info(
														"menuUse[menuUse.length-2] [{}], menuUse[menuUse.length-1] [{}]",
														menuUse[menuUse.length - 2], menuUse[menuUse.length - 1]);

												menuUseMap.put(menuUse[menuUse.length - 2],
														menuUse[menuUse.length - 1]);
											}
										}
									}

									LinkedHashMap<String, OptionalMenuList> lhm = optManMenuOne.getOptMenuChoose();
									Iterator<String> keyData = lhm.keySet().iterator();
									while (keyData.hasNext()) {
										String k = (String) keyData.next();
										OptionalMenuList v = (OptionalMenuList) lhm.get(k);

										Element opListElement = optionElement.addElement("OpList");
										opListElement.addElement("code").addText(String.valueOf(v.getId()));
										opListElement.addElement("name").addText(v.getName());
										opListElement.addElement("price").addText(String.format("%.0f", v.getPrice()));

										String useStatus = (String) menuUseMap.get(String.valueOf(v.getId()));
										if(!Util.isNotValid(useStatus)){
						    				opListElement.addElement("status").addText(useStatus); //메뉴 상태가 삭제일 경우 옵션도 모두 삭제
						    			}else{
						    				opListElement.addElement("status").addText("Z"); //메뉴 상태가 삭제일 경우 옵션도 모두 삭제
										}
									}
									LinkedHashMap<String, OptionalMenuListDelete> hm2 = optManMenuOne
											.getOptMenuDeleteChoose();
									Iterator<String> keyData2 = hm2.keySet().iterator();
									while (keyData2.hasNext()) {
										String k = (String) keyData2.next();
										OptionalMenuListDelete v = (OptionalMenuListDelete) hm2.get(k);

										Element opListElement = optionElement.addElement("OpList");
										opListElement.addElement("code").addText(String.valueOf(v.getOptListId()));
										opListElement.addElement("name").addText(v.getName());
										opListElement.addElement("price").addText(String.format("%.0f", v.getPrice()));
										opListElement.addElement("status").addText(menuOne.getStatus()); //메뉴 상태가 삭제일 경우 옵션도 모두 삭제
									}
								}
							}
						}
					}
				}
			} else {
				root.addAttribute("response", "0000");
				root.addAttribute("errcode", "0001");
			}
		} else {
			root.addAttribute("response", "0000");
			root.addAttribute("errcode", "0002");
		}

		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache, must-revalidate"); // HTTP 1.0
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter pw = response.getWriter();

		logger.info("POS XML 생성 >> 종료  storeKey[{}]", storeKey);

		document.write(pw);
	}

	// ------------------------------ 바나나 포스 종료  ------------------------------ 

}
