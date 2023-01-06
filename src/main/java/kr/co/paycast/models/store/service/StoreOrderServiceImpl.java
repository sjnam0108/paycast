package kr.co.paycast.models.store.service;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import kr.co.paycast.exceptions.ServerOperationForbiddenException;
import kr.co.paycast.models.MessageManager;
import kr.co.paycast.models.pay.Device;
import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.service.DeviceService;
import kr.co.paycast.models.pay.service.MenuService;
import kr.co.paycast.models.pay.service.StoreService;
import kr.co.paycast.models.store.StoreCookTask;
import kr.co.paycast.models.store.StoreDelivery;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderCancel;
import kr.co.paycast.models.store.StoreOrderCook;
import kr.co.paycast.models.store.StoreOrderList;
import kr.co.paycast.models.store.StoreOrderPay;
import kr.co.paycast.models.store.StoreOrderVerification;
import kr.co.paycast.models.store.dao.StoreCookTaskDao;
import kr.co.paycast.models.store.dao.StoreOrderDao;
import kr.co.paycast.utils.FireMessage;
import kr.co.paycast.utils.PayUtil;
import kr.co.paycast.utils.Util;
import kr.co.paycast.viewmodels.self.MenuPayItem;
import kr.co.paycast.viewmodels.store.MenuJsonPrintItem;
import kr.co.paycast.viewmodels.store.MenuListJsonPrintItem;
import kr.co.paycast.viewmodels.store.RefillView;

import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("StoreOrderService")
public class StoreOrderServiceImpl implements StoreOrderService {
	private static final Logger logger = LoggerFactory.getLogger(StoreOrderServiceImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private StoreOrderDao storeOrderDao;
    
    @Autowired
    private StoreCookService storeCookService;
    
	@Autowired
	private StoreCookTaskDao storeCookTaskDao;
    
	@Autowired
	private MessageManager msgMgr;

    @Autowired 
    private DeviceService devService;

    @Autowired 
    private StoreService storeService;

    @Autowired 
    private StoreNumberService storeNumberService;
    
    @Autowired 
    private MenuService menuService;
    
    @Autowired 
    private StoreCancelService storeCancelService;

	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public StoreOrder getDplyStoreOrderPrint(int id) {
		return storeOrderDao.getOrderOne(id);
	}
	
	@Override
	public void saveOrUpdate(StoreOrder condFile) {
		storeOrderDao.saveOrUpdate(condFile);
	}
	
	@Override
	public void saveOrder(StoreOrder orderDao, List<MenuPayItem> payitemList) {
		storeOrderDao.saveOrder(orderDao);
		try{
			String orderNum = orderDao.getOrderNumber();//주문 키
			if(payitemList.size() > 0){
				List<StoreOrderList> orderList = new ArrayList<StoreOrderList>();
				for(MenuPayItem payItem : payitemList){
					int orderProductID = payItem.getMenuId();
					String productName = payItem.getName();
					int orderPrice = Integer.parseInt(payItem.getToPrice());
					int orderCount = Integer.parseInt(payItem.getOrderCount());
					int productPacking = Integer.parseInt(payItem.getPacking());
					
					String essDB = "";
					String addDB = "";
					if(!Util.isNotValid(payItem.getEssVal())){	
						String essVal = payItem.getEssVal();
						String[] essValArr = essVal.split("\\^");
						String essName = payItem.getEssName();
						String[] essNameArr = essName.split("\\^");
						if(essValArr.length > 0){
							for(int a=0; a < essValArr.length; a++ ){
								String[] essValArr2 = essValArr[a].split("_");
								if(Util.isNotValid(essDB)){
									essDB = essValArr2[0] +"_"+ essNameArr[a];
								}else{
									essDB += ","+essValArr2[0] +"_"+ essNameArr[a];
								}
							}
						}
					}
   					if(!Util.isNotValid(payItem.getAddVal())){
   						String addVal = payItem.getAddVal();
   						String[] addValArr = addVal.split("\\^");
						String addName = payItem.getAddName();
						String[] addNameArr = addName.split("\\^");
						if(addValArr.length > 0){
							String comp = "";
							for(int a=0; a < addValArr.length; a++ ){
								String[] addValArr2 = addValArr[a].split("_");
								if(Util.isNotValid(addDB)){
									addDB = addValArr2[0] +"_"+ addNameArr[a];
									comp = addValArr2[0];
								}else{
									if(comp.equals(addValArr2[0])){
										addDB += "||"+ addNameArr[a];	
									}else{
										addDB += ","+addValArr2[0] +"_"+ addNameArr[a];
										comp = addValArr2[0];
									}
								}
							}
						}
					}
					
					StoreOrderList storeOrderListOne = new StoreOrderList(orderNum, orderProductID, orderCount, productName, orderPrice, (orderPrice*orderCount), productPacking);
					storeOrderListOne.setOrderSelectEss(essDB);
					storeOrderListOne.setOrderSelectAdd(addDB);
					orderList.add(storeOrderListOne);
				}
			
				storeOrderDao.saveOrderList(orderList);
			}
		}catch(Exception e){
        	logger.error("save", e);
        	throw new ServerOperationForbiddenException("SaveError");
		}
	}

	@Override
	public StoreOrder getOrder(int storeId, String orderNum) {
		return storeOrderDao.getOrder(storeId, orderNum);
	}
	
	@Override
	public StoreOrder getOrderbyOrderNum(String sp_order_no) {
		return storeOrderDao.getOrderbyOrderNum(sp_order_no);
	}

	@Override
	public void saveOrUpdate(StoreOrderList storeOrderList) {
		storeOrderDao.saveOrUpdate(storeOrderList);
	}
	
	@Override
	public List<StoreOrderList> getOrderListbyNumber(String orderNumber) {
		return storeOrderDao.getOrderList(orderNumber);
	}
	
	@Override
	public String saveOrderPay(StoreOrder orderRes, StoreOrderPay orderPay, Locale locale, HttpSession session) {
		// 조회된 주문 내역(orderRes) 거래고유번호 업데이트와  결제 내역(orderPay)를  저장
		
		int orderSeq = 0;
		String orderNumberView = "";
		try{
			logger.info("saveOrderPay >>> 결제 내역 저장 시작 >> orderRes.getId(): [{}]", orderRes.getId());
			
			// 1.결제 내역을 저장한다.
			int orderPayId = storeOrderDao.saveOrderPay(orderPay);
			
			logger.info("saveOrderPay >>> 결제 내역 완료 후 ID 값 >> orderPayId : [{}]", orderPayId);
			
			// 2.주문 순서를 조회 하기 위해서 주문을 매장 번호로 조회 
			Store store = storeService.getStore(orderRes.getStoreId());
	    	orderSeq = storeNumberService.getOrderNumMobile(store.getId(), session, locale);
	    	logger.info("saveOrderPay >>> 주문번호 >> orderSeq : [{}], orderRes.getOrderNumber() [{}]", orderSeq, orderRes.getOrderNumber());
	    	
			orderNumberView = String.valueOf(orderSeq);
			
			StoreOrder storeOrderOne = storeOrderDao.getOrderOne(orderRes.getId());
			// 3.결제 내역의 거래 고유 번호, 결제 ID와 주문 순서를 저장
			storeOrderOne.setOrderTid(orderPay.getOrderTid());
			storeOrderOne.setOrderPayId(orderPayId);
			storeOrderOne.setOrderSeq(orderNumberView);
			storeOrderOne.touchWho(orderRes.getOrderNumber());
			storeOrderDao.updateOrder(storeOrderOne);
			
			store.getStoreOpt().setStoreOrderSeq(orderSeq);
			storeService.saveOrUpdate(store.getStoreOpt());
			
		}catch(Exception e){
        	logger.error("save", e);
        	throw new ServerOperationForbiddenException("SaveError");
		}
		
		// 4. 주문 순서를  return 해준다.
		return orderNumberView;
	}

	@Override
	public void deleteOrder(int storeId, String order) {
		StoreOrder res = storeOrderDao.getOrder(storeId, order);
		if( res != null){
			//결제가 완료된 주문 건에 대해서는 삭제 하지 않는다.
			if(res.getOrderPayId() > 0){
				logger.info("deleteOrder >> order [{}][{}]", order, "결제가 완료된 건으로 삭제불가");
				logger.info("deleteOrder >> orderSeq [{}]", res.getOrderSeq());				
			}else{
				logger.info("deleteOrder >> order [{}][{}]", order, "결제가 미완료된 건으로 삭제 진행");
				storeOrderDao.delete(res);
				
				List<StoreOrderList> resList = storeOrderDao.getOrderList(order);
				storeOrderDao.deleteOrderList(resList);
			}
		}else{
			logger.info("deleteOrder >> order [{}][{}]", order, "조회된 주문정보가 없습니다. ");
		}
		
		logger.info("deleteOrder >> order [{}][{}]", order, "삭제 진행 끝");
	}

	@Override
	public List<MenuJsonPrintItem> makeMenuListPrint(String storeId, String deviceId) {
		Store store = storeService.getStore(Util.parseInt(storeId));
		if(store == null){
        	logger.error("makeMenuListPrint [{}]", "매장을 조회 할수 없습니다. ");
        	throw new ServerOperationForbiddenException("매장을 조회 할수 없습니다. ");
		}
		Device device = devService.getDeviceByUkid(deviceId);
		if(device == null){
        	logger.error("makeStoreInfoXmlFile [{}]", "기기를 조회 할수 없습니다. ");
        	throw new ServerOperationForbiddenException("기기를 조회 할수 없습니다. ");
		}
		// 상점 정보와 deviceID의 상점 정보가 같은 지 확인 하고 다를 경우 조회가 되지 않도록 한다.
		if(store.getId() != device.getStore().getId()){
        	logger.error("makeStoreInfoXmlFile [{}]", "상점 정보과 device 정보를 확인하여 주시기 바랍니다.");
        	throw new ServerOperationForbiddenException("기기를 조회 할수 없습니다. ");
		}
		
		List<MenuJsonPrintItem> menuListMake = new ArrayList<MenuJsonPrintItem>(); 
		
		// TODO StoreOrder에서 메뉴 내역을 가져온다. 
		// 주문한 메뉴가 프린트 여부가 N 일 경우를 가져온다.
		// 하루전까지 메뉴만 조회 한다. 
		List<StoreOrder> storeOrderOneList = storeOrderDao.getMenuOrderListStore(Integer.parseInt(storeId), "N");
		
		if(storeOrderOneList.size() > 0){
			for(StoreOrder order : storeOrderOneList){
				if(order.getOrderPayId() > 0){
					StoreOrderPay orderPay = storeOrderDao.getOrderPayAuthDate(order.getOrderPayId());
					if(!"".equals(orderPay.getPayAuthDate())){
						MenuJsonPrintItem menuitme = new MenuJsonPrintItem();
						menuitme.setRecommand(String.valueOf(order.getId()));
						menuitme.setStoreName(store.getBizName());
						menuitme.setOrderTable(order.getOrderTable());
						menuitme.setMenuCancelYN("N"); //결제 취소에 대한 여부 - Y:결제취소완료 / N:결제완료 메뉴 주문
						
						// 전화 번호 추가 - 2019.10.15
    	        		String phone = PayUtil.phoneChange(Util.parseString(order.getTelNumber(), ""));
						menuitme.setTel(phone);
						menuitme.setPayment(order.getPayment());
						menuitme.setOrderType(order.getOrderType());
						
						StoreDelivery deli = storeOrderDao.getDeliveryOne(order.getDeliveryId());
						String bTime = "";
						if(deli != null){
							// orderType 주문 선택 (S : 매장이용, P : 포장, D : 배달)
							if("D".equals(order.getOrderType())){
								menuitme.setRoadAddr(deli.getRoadAddr());
								menuitme.setAddrDetail(deli.getAddrDetail());
							}else{
								menuitme.setRoadAddr("");
								menuitme.setAddrDetail("");
							}
							menuitme.setStoreMsg(deli.getStoreMsg());
							menuitme.setDeliMsg(deli.getDeliMsg());
							
							if("P".equals(order.getOrderType())){
								bTime = deli.getBookingTime();
							}
							menuitme.setBookingTime(bTime);
							
						}else{
							menuitme.setRoadAddr("");
							menuitme.setAddrDetail("");
							menuitme.setStoreMsg("");
							menuitme.setDeliMsg("");
							menuitme.setBookingTime(bTime);
						}
						
						// MS : 스마일페이 모바일  / ME : easypay
						if("MS".equals(order.getOrderDevice())){
							// 모바일 결제 승인 일자가 181126144622 으로 넘어옴
							Calendar calendar = new GregorianCalendar(Locale.KOREA);
							int nYear = calendar.get(Calendar.YEAR);
							String nYearSt = String.valueOf(nYear);
							menuitme.setOrderDate(nYearSt.substring(0, 2) + orderPay.getPayAuthDate());	//결제 승인 일자
						}else{
							menuitme.setOrderDate(orderPay.getPayAuthDate());	//결제 승인 일자
						}
						menuitme.setOrderNumber(order.getOrderSeq());
						menuitme.setGoodsAmt(String.valueOf(order.getGoodsAmt()));
/*						menuitme.setDeliPrice(String.valueOf(order.getDeliveryPay()));*/
						
						List<MenuListJsonPrintItem> menuListItem = new ArrayList<MenuListJsonPrintItem>();
						// DB에 저장되어 있는 메뉴 List
						List<StoreOrderList> resList = storeOrderDao.getOrderList(order.getOrderNumber());
						for(StoreOrderList storeOrderListOne : resList){
							MenuListJsonPrintItem printItem = new MenuListJsonPrintItem();
							printItem.setProductID(String.valueOf(storeOrderListOne.getOrderMenuId()));
							printItem.setProductName(storeOrderListOne.getOrderMenuName());
							printItem.setOrderCount(String.valueOf(storeOrderListOne.getOrderMenuAmount()));
							printItem.setOrderPrice(String.valueOf(storeOrderListOne.getOrderMenuAmt()));
							printItem.setOrderMenuPacking(String.valueOf(storeOrderListOne.getOrderMenuPacking()));
							printItem.setEss(storeOrderListOne.getOrderSelectEss());
							printItem.setAdd(storeOrderListOne.getOrderSelectAdd());
							
							menuListItem.add(printItem);
						}
						menuitme.setOrderMenu(menuListItem);
						
						menuListMake.add(menuitme);
					}
				}
			}
		}
		
		// 2019.04.17 주문이 취소된 내역을 가져온다. 
		// 모든 Device 에 대해서 가져온다. 
		// 하루전까지 메뉴만 조회 한다. 
		List<StoreOrder> storeOrderCancelList = storeOrderDao.getMenuCancelListStorebyAllDevice(Integer.parseInt(storeId), "1");
		if(storeOrderCancelList.size() > 0){
			for(StoreOrder order : storeOrderCancelList){
				if(order.getOrderPayId() > 0){
					StoreOrderPay orderPay = storeOrderDao.getOrderPayAuthDate(order.getOrderPayId());
					if(!"".equals(orderPay.getPayAuthDate())){
						MenuJsonPrintItem menuitme = new MenuJsonPrintItem();
						menuitme.setRecommand(String.valueOf(order.getId()));
						menuitme.setStoreName(store.getBizName());
						menuitme.setOrderTable(order.getOrderTable());
						menuitme.setMenuCancelYN("Y"); //결제 취소에 대한 여부 - Y:결제취소완료 / N:결제완료 메뉴 주문
						
						// 전화 번호 추가 - 2019.10.15
    	        		String phone = PayUtil.phoneChange(Util.parseString(order.getTelNumber(), ""));
						menuitme.setTel(phone);
						menuitme.setPayment(order.getPayment());
						menuitme.setOrderType(order.getOrderType());
						
						StoreDelivery deli = storeOrderDao.getDeliveryOne(order.getDeliveryId());
						String bTime = "";
						if(deli != null){
							// orderType 주문 선택 (S : 매장이용, P : 포장, D : 배달)
							if("D".equals(order.getOrderType())){
								menuitme.setRoadAddr(deli.getRoadAddr());
								menuitme.setAddrDetail(deli.getAddrDetail());
							}else{
								menuitme.setRoadAddr("");
								menuitme.setAddrDetail("");
							}
							menuitme.setStoreMsg(deli.getStoreMsg());
							menuitme.setDeliMsg(deli.getDeliMsg());
							if("P".equals(order.getOrderType())){
								bTime = deli.getBookingTime();
							}
							menuitme.setBookingTime(bTime);
						}else{
							menuitme.setRoadAddr("");
							menuitme.setAddrDetail("");
							menuitme.setStoreMsg("");
							menuitme.setDeliMsg("");
							menuitme.setBookingTime(bTime);
						}
						
						// MS : 스마일페이 모바일  / ME : easypay
						if("MS".equals(order.getOrderDevice())){
							// 모바일 결제 승인 일자가 181126144622 으로 넘어옴
							Calendar calendar = new GregorianCalendar(Locale.KOREA);
							int nYear = calendar.get(Calendar.YEAR);
							String nYearSt = String.valueOf(nYear);
							menuitme.setOrderDate(nYearSt.substring(0, 2) + orderPay.getPayAuthDate());	//결제 승인 일자
						}else{
							menuitme.setOrderDate(orderPay.getPayAuthDate());	//결제 승인 일자
						}
						menuitme.setOrderNumber(order.getOrderSeq());
						menuitme.setGoodsAmt("-"+String.valueOf(order.getGoodsAmt()));
/*						menuitme.setDeliPrice(String.valueOf(order.getDeliveryPay()));
						*/
						List<MenuListJsonPrintItem> menuListItem = new ArrayList<MenuListJsonPrintItem>();
						// DB에 저장되어 있는 메뉴 List
						List<StoreOrderList> resList = storeOrderDao.getOrderList(order.getOrderNumber());
						for(StoreOrderList storeOrderListOne : resList){
							MenuListJsonPrintItem printItem = new MenuListJsonPrintItem();
							printItem.setProductID(String.valueOf(storeOrderListOne.getOrderMenuId()));
							printItem.setProductName(storeOrderListOne.getOrderMenuName());
							printItem.setOrderCount("-"+String.valueOf(storeOrderListOne.getOrderMenuAmount()));
							printItem.setOrderPrice("-"+String.valueOf(storeOrderListOne.getOrderMenuAmt()));
							printItem.setOrderMenuPacking(String.valueOf(storeOrderListOne.getOrderMenuPacking()));
							printItem.setEss(storeOrderListOne.getOrderSelectEss());
							printItem.setAdd(storeOrderListOne.getOrderSelectAdd());
							
							menuListItem.add(printItem);
						}
						menuitme.setOrderMenu(menuListItem);
						
						menuListMake.add(menuitme);
					}
				}
			}
		}		
		
		return menuListMake;
	}

	@Override
	public boolean kioskPayMent(MenuJsonPrintItem jsonMenu) {
		boolean success = false;
		
		logger.info("/kioskpaymentinfo >> 저장 시작 하기 ");
		//테스트
		logger.info("/kioskpaymentinfo >> 테스트  [{}]",jsonMenu);
		
		try{
			SimpleDateFormat yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String nowTime = yyyyMMddHHmmssSSS.format(new Date());
			
			logger.info("/kioskpaymentinfo >> nowTime > [{}]", nowTime);
			
			String orderNum = "K"+jsonMenu.getMid()+nowTime;//K + 가맹점 번호 + 날짜시간
			
			logger.info("/kioskpaymentinfo >> orderNum 생성  > [{}]", orderNum);
			
			int storeId = Util.parseInt(jsonMenu.getRecommand());
			logger.info("/kioskpaymentinfo >> 매장ID  > [{}]", storeId);
			String authCode = "";
			if("RF".equals(jsonMenu.getPayment())){
				authCode = "RF"+nowTime;		
			}else{
				authCode = jsonMenu.getAuthCode();
			}
			logger.info("/kioskpaymentinfo >> authCode  > [{}]", authCode);
			
			// 하루 전까지 결제 내역에서 authCode(승인번호)가 겹치는 지 확인하고
			// 겹치치 않았을 경우 저장
			// 겹쳤을 경우 저장 하지 않고 success = true
			if(storeOrderDao.isRegisteredPayAuthCode(storeId, authCode)){
				logger.info("/kioskpaymentinfo >> 매장 ID[{}], 승인번호[{}] 이미 등록된 결제 내역입니다. (현재 시간 하루전까지 체크)", storeId, authCode);
				success = true;
			}else{
	
				logger.info("/kioskpaymentinfo >> storeOrder  > [{}]", "결제된 내역 storeOrder에 저장 시작");
				StoreOrder orderDao = new StoreOrder(storeId, orderNum, "", jsonMenu.getTotalindex(), Util.parseInt(jsonMenu.getGoodsAmt()), "K");
				
				logger.info("/kioskpaymentinfo >> jsonMenu.getPayment() > [{}]", jsonMenu.getPayment());
				logger.info("/kioskpaymentinfo >> jsonMenu.getPaOrderId() > [{}]", jsonMenu.getPaOrderId());
				
				if("RF".equals(jsonMenu.getPayment())){
					orderDao.setOrderTid("RF");					
				}else{
					orderDao.setOrderTid(jsonMenu.getTid());
				}
				orderDao.setTelNumber(jsonMenu.getTel());
				orderDao.setPayment(jsonMenu.getPayment());
				orderDao.setOrderParent(Util.parseInt(jsonMenu.getPaOrderId()));
				
				//테스트
				logger.info("/kioskpaymentinfo 데이터 테스트");
				
				logger.info(orderDao.getId()+ " - "+ orderDao.getStoreId()+ " - " + orderDao.getOrderPayId());
				
				storeOrderDao.saveOrder(orderDao);
	
				logger.info("/kioskpaymentinfo >> storeOrder end > [{}]", "결제된 내역 storeOrder에 저장 완료");
				
				List<MenuListJsonPrintItem> menuJsonList = jsonMenu.getOrderMenu();
				
				String orderTypeKiosk = "S";
				String nameTotal = "";
				List<StoreOrderList> orderList = new ArrayList<StoreOrderList>();
				int t0 = 0;
				logger.info("/kioskpaymentinfo >> StoreOrderList> [{}]", "결제된  메뉴내역 StoreOrderList에 저장 시작");
				for(MenuListJsonPrintItem payItem : menuJsonList){
					int orderProductID = Integer.parseInt(payItem.getProductID());
					String productName = payItem.getProductName();
					
					float price = Util.parseFloat(payItem.getOrderPrice());
					int orderPrice = (int)price;
					int orderCount = Util.parseInt(payItem.getOrderCount());
					int orderMenuPacking = Util.parseInt(payItem.getOrderMenuPacking());
					String ess = payItem.getEss();
					String add = payItem.getAdd();
					
					StoreOrderList orderListOne = new StoreOrderList(orderNum, orderProductID, orderCount, productName, orderPrice, (orderPrice*orderCount), orderMenuPacking);
					orderListOne.setOrderSelectEss(ess);
					orderListOne.setOrderSelectAdd(add);
					orderList.add(orderListOne);
					if(t0 != 0){
						nameTotal = (","+productName);
					}else{
						nameTotal = productName;
						t0 = t0+1;
					}
					
					if("1".equals(orderMenuPacking)){
						orderTypeKiosk = "P";
					}
				}
				nameTotal += (" 외" + (menuJsonList.size()-1));
				storeOrderDao.saveOrderList(orderList);
	
				logger.info("/kioskpaymentinfo >> StoreOrderList end > [{}]", "결제된  메뉴내역 StoreOrderList에 저장 완료");
				
				logger.info("/kioskpaymentinfo >> StoreOrderPay > [{}]", "결제내역 저장 하기 StoreOrderPay 시작");
				StoreOrderPay storeOrderPay = new StoreOrderPay();
				storeOrderPay.setStoreId(storeId);
				storeOrderPay.setOrderNumber(jsonMenu.getOrderNumber());
				storeOrderPay.setOrderTid(jsonMenu.getTid());
				storeOrderPay.setPayMethod(jsonMenu.getPayMethod());
				storeOrderPay.setPayMid(jsonMenu.getMid());
				storeOrderPay.setPayAmt(jsonMenu.getGoodsAmt());
				storeOrderPay.setGoodsname(nameTotal);
				storeOrderPay.setPayOid(orderNum);
				storeOrderPay.setPayAuthDate(jsonMenu.getOrderDate());
				storeOrderPay.setPayAuthCode(authCode);
				storeOrderPay.setPayFnCd(jsonMenu.getFnCd());
				storeOrderPay.setPayFnName(jsonMenu.getFnName());
				storeOrderPay.setPayFnCd1(jsonMenu.getFnCd1());
				storeOrderPay.setPayFnName1(jsonMenu.getFnName1());
				storeOrderPay.touchWhoC(orderNum);
				
				int orderPayId = storeOrderDao.saveOrderPay(storeOrderPay);
				
				logger.info("/kioskpaymentinfo >> StoreOrderPay end > [{}]", "결제내역 저장 하기 StoreOrderPay 완료");
				
				StoreOrder storeOrderOne = storeOrderDao.getOrderOne(orderDao.getId());
				storeOrderOne.setGoodsName(nameTotal);
				storeOrderOne.setOrderPayId(orderPayId);
				storeOrderOne.setOrderSeq(jsonMenu.getOrderNumber());
				
				// 리필일 경우에는 포장 /매장 에 대한 type을 무조건 리필로 변경 한다.  
				if("RF".equals(jsonMenu.getPayment())){
					orderTypeKiosk = "I";			
				}
				storeOrderOne.setOrderType(orderTypeKiosk);
				storeOrderOne.touchWho(orderNum);
				storeOrderDao.updateOrder(storeOrderOne);
				
				success = true;
				
				// 4. 저장된 결제 내역에 대한 주방용 메뉴 리스트 작성 : STORE_ORDER_COOK TABLE 저장
				// 4-1. 3001(카드 결제 성공)이 아닐 경우 주방용 메뉴 리스트에 넣지 않는다. - 위에서 에러
				// 4-2. "실제 스마트로 서버의 승인 값을 검증 하기 위해서 값 " 끝나면 결제된 내용의 결제 금액과 저장되어 있는 총 결제 금액이 같은지 비교(스마트로 권장사항)
				//      금액이 같지 않을 경우 주방용 메뉴 리스트에 넣지 않는다. - 위에서 에러
				// FCM 을 보낼때 주방용 패드를 체크하여 없을 경우 해당 StoreOrderCook에 데이터를 넣지 않는다.
				StoreOrderCook storeOrderCook = new StoreOrderCook(orderDao.getStoreId(), orderDao.getId(), orderDao.getOrderNumber());
				//FCM 전송  : 키오스크 결제완료가 되어 DB에 저장 되었을 경우 fcm으로 stb 에 알려준다.
				fcmTransmission(storeId, storeOrderCook);
			}
		}catch(Exception e){
        	logger.error("save", e);
        	throw new ServerOperationForbiddenException("SaveError");
		}
		return success ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean fcmTransmission(int storeId, StoreOrderCook storeOrderCook) {
		boolean cookPadCheck = false;
		boolean didCheck = false;
		boolean success = false;
		//1. 매장 ID를 가지고 등록된 매장의 stbGroupId 값을 가져온다.
		//2. stbGroup에 매핑 되어 있는 stb 목록을 조회한다.
		//3. 조회된 stb들의 fcmToken들을 조회 하여 FCM을 전송한다.
		//2019.04.06 FCM 전송시  D(DID)관련 FCM 은 제외 한다.
		//4. C(주방용 패드)fcm 을 보낼때 주방용 패드 목록에 추가 하기 위해서 FCM 을 보낸다. 
		//5. 주방용 패드가 없을 경우 StoreOrderCook 에 저장 하지 않기 위해서 체크 한다. 
		
		logger.info("[결제 완료]FCM 보내기 자료 만들기 >>>");
		
		Store store = storeService.getStore(storeId);
		if (store != null) {
			FireMessage fcm = new FireMessage();
			JSONArray mobileTokens = new JSONArray();
			JSONArray mobileTokensDid = new JSONArray();
			JSONArray mobileTokensCook = new JSONArray();
			
			logger.info("[결제 완료]FCM 에 들어가는 TOKEN 조회 및 모델 구분으로 task 생성 >>> ");
			
			List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
			
			try{
				List<StoreCookTask> storeCookTaskList = new ArrayList<StoreCookTask>();
				
				if(deviceList.size() > 0){
					for(Device device : deviceList){
						String fcmToken = device.getFcmToken();
						if(fcmToken != null){
							fcmToken = URLDecoder.decode(fcmToken, "UTF-8");
							if (device.getDeviceType().equals("D")) {			// D: 주방용패드
								logger.info("[결제 완료]주방용 패드 TOKEN [{}]", fcmToken);
								
								mobileTokensCook.add(fcmToken);	
								cookPadCheck = true;
							} else if (device.getDeviceType().equals("N")) {	// N: 알리미
								// 2019.04.22 DID에 FCM 보내기
								// StoreStay.bbmc : 대기 목록을 가져가도록 한다.( 알리미에서 사용 ) 
								// StoreComplete.bbmc : 완료 목록을 가져가도록 한다.
								// StoreWaitPeople.bbmc : DID에서 대기번호를 조회 한다. 
								String cookMenuTask = "StoreWaitPeople.bbmc";
								
								logger.info("[결제 완료]알리미 명령어 추가 [{}]", cookMenuTask);
								
								StoreCookTask storeCookTask = new StoreCookTask();
								Date dateNew = new Date();
								storeCookTask.setSiteId(device.getStore().getSite().getId());
								storeCookTask.setStoreId(storeId);
								storeCookTask.setStbId(device.getId());
								storeCookTask.setDeviceID(device.getUkid());
								storeCookTask.setCommand(cookMenuTask);
								storeCookTask.setStatus("N");
								storeCookTask.setDestDate(dateNew);
								storeCookTask.setCancelDate(Util.setMaxTimeOfDate(dateNew));
								storeCookTask.setWhoCreatedBy(-1);
								storeCookTask.setWhoCreationDate(dateNew);
								storeCookTask.setWhoLastUpdatedBy(-1);
								storeCookTask.setWhoLastUpdateDate(dateNew);
								storeCookTask.setWhoLastUpdateLogin(-1);
	
								storeCookTaskList.add(storeCookTask);
								
								logger.info("[결제 완료]알리미 명령어 추가 완료 [{}]", cookMenuTask);
								
								mobileTokensDid.add(fcmToken);
								didCheck = true;
							} else {
								logger.info("[결제 완료]프린트 FCM 추가 [{}]", fcmToken);
								mobileTokens.add(fcmToken);	
							}
						}
					}
						
					success = true;
				}

				logger.info("[결제 완료]FCM 전송 시작");
				
				logger.info("[결제 완료]패드 있는지 확인 [{}]", cookPadCheck);
				
				logger.info("[결제 완료]mobileTokens.size() [{}]", mobileTokens.size());
				if(mobileTokens.size() > 0){
					
					fcm.sendToGroup(mobileTokens);
				}
				
				if(cookPadCheck){
					storeCookService.saveOrUpdate(storeOrderCook);
					
					fcm.sendToGroupCook(mobileTokensCook);
				}
				
				
				logger.info("[결제 완료]알리미 있는지 확인 [{}]", didCheck);
				//did 대기 번호 조회를 위해서 FCM을 전송
				if(didCheck){
					// 2019.04.22 DID에 FCM 보내기 (프린트 및 DID 가 전부 들어 갈수 있음)
					// StoreStay.bbmc : 대기 목록을 가져가도록 한다.
					// StoreComplete.bbmc : 완료 목록을 가져가도록 한다.
					// StoreWaitPeople.bbmc : DID에서 대기번호를 조회 한다. 
					if(storeCookTaskList.size() > 0){
						for(StoreCookTask storeCookTask : storeCookTaskList){
							storeCookTaskDao.saveOrUpdate(storeCookTask);
						}
					}
					
					logger.info("[결제 완료]mobileTokensDid.size() [{}]", mobileTokensDid.size());
					if(mobileTokensDid.size() > 0){
						fcm.sendToGroupDid(mobileTokensDid);
					}
				}
				
				logger.info("[모바일 결제 완료]FCM 전송 종료");
				
			}catch(Exception e){
	        	logger.error("FCM ERROR", e);
	        	throw new ServerOperationForbiddenException("FCM을 전송할수 없습니다.");
			}
		}
		
		return success;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean fcmTransmissionOne(int storeId) {
		boolean success = false;
		//1. 매장 ID를 가지고 등록된 매장의 stbGroupId 값을 가져온다.
		//2. stbGroup에 매핑 되어 있는 stb 목록을 조회한다.
		//3. 조회된 stb들의 fcmToken들을 조회 하여 FCM을 전송한다.
		//2019.04.10 
		//MoTask에서 사용 하는 FCM 전송은   D(DID), C(cookPad)관련 FCM은 제외 한다.
		
		Store store = storeService.getStore(storeId);
		if (store != null) {
			List<Device> deviceList = devService.getDeviceListByStoreId(store.getId());
			
			try{
				if(deviceList.size() > 0){
					FireMessage fcm = new FireMessage();
					if(deviceList.size() > 1){
						JSONArray mobileTokens = new JSONArray();
						for (Device device : deviceList) {
							if (!device.getDeviceType().equals("N") && !device.getDeviceType().equals("D")) { // D: 주방용패드, N: 알리미
								String fcmToken = device.getFcmToken();
								if(!Util.isNotValid(fcmToken)){
									fcmToken = URLDecoder.decode(fcmToken, "UTF-8");
									mobileTokens.add(fcmToken);	
								}
							}
						}
						
						fcm.sendToGroup(mobileTokens);
					}else{
						for (Device device : deviceList) {
							if (!device.getDeviceType().equals("N") && !device.getDeviceType().equals("D")) { // D: 주방용패드, N: 알리미
								String fcmToken = device.getFcmToken();
								if(!Util.isNotValid(fcmToken)){
									fcmToken = URLDecoder.decode(fcmToken, "UTF-8");
									fcm.sendToToken(fcmToken);
								}
							}
						}
					}
					
					success = true;
				}
			}catch(Exception e){
	        	logger.error("FCM ERROR", e);
	        	throw new ServerOperationForbiddenException("FCM을 전송할수 없습니다.");
			}
		}
		
		return success;
	}
	
	@Override
	public List<MenuJsonPrintItem> makePOSMenuList(String storeKey, Date time) {
		logger.info("makePOSMenuList >> storeKey [{}], time [{}]", storeKey, time);
		Store res = storeService.getStoreByStoreKey(storeKey);
		if(res == null){
        	logger.error("makePOSMenuList >> makeMenuListPrint [{}] storeKey >>> [{}]", "매장을 조회 할수 없습니다. ", storeKey);
        	throw new ServerOperationForbiddenException("매장을 조회 할수 없습니다. ");
		}
		
		List<MenuJsonPrintItem> menuListMake = new ArrayList<MenuJsonPrintItem>(); 
		
		// TODO StoreOrder에서 메뉴 내역을 가져온다. 
		// 특정시간동안에 주문된 메뉴를 가져온다.  
		List<StoreOrder> storeOrderOneList = storeOrderDao.getMenuOrderListTime(res.getId(), time);
		logger.info("storeOrderOneList.size() >>> [{}]", storeOrderOneList.size());
		if(storeOrderOneList.size() > 0){
			for(StoreOrder order : storeOrderOneList){
				// 2019.11.21 정상결제건이 취소가 되어도 해당 내역을 Pos에 전송
				if(order.getOrderPayId() > 0){
					StoreOrderPay orderPay = storeOrderDao.getOrderPayAuthDate(order.getOrderPayId());
					if(!"".equals(orderPay.getPayAuthDate())){
						MenuJsonPrintItem menuitme = new MenuJsonPrintItem();
						menuitme.setRecommand(String.valueOf(order.getId()));
						menuitme.setStoreName(res.getStoreName());
						menuitme.setMenuCancelYN("N"); //결제 취소에 대한 여부 - Y:결제취소완료 / N:결제완료 메뉴 주문
						// MS : 스마일페이 모바일  / ME : easypay 
						if("MS".equals(order.getOrderDevice())){
							// 모바일 결제 승인 일자가 181126144622 으로 넘어옴
							Calendar calendar = new GregorianCalendar(Locale.KOREA);
							int nYear = calendar.get(Calendar.YEAR);
							String nYearSt = String.valueOf(nYear);
							menuitme.setOrderDate(nYearSt.substring(0, 2) + orderPay.getPayAuthDate());	//결제 승인 일자
						}else{
							menuitme.setOrderDate(orderPay.getPayAuthDate());	//결제 승인 일자
						}
						menuitme.setOrderNumber(order.getOrderSeq());
						menuitme.setGoodsAmt(String.valueOf(order.getGoodsAmt()));
						menuitme.setOrderTable(order.getOrderTable());
						
						// 2019.11.07 pos - 배달 내용 추가
						menuitme.setDevice(order.getOrderDevice());             //결제 기기(K:키오스크/M:모바일)     
						if("MS".equals(order.getOrderDevice()) || "ME".equals(order.getOrderDevice())){
							menuitme.setDevice("M");
						}
						menuitme.setOrderType(order.getOrderType());       //주문선택(S:매장/P:포장/D:배달/I:리필)        
						menuitme.setPayment(order.getPayment());           //결제정보(AD:선불/DE:후불/RF:리필)   
						menuitme.setDeliPrice(String.valueOf(order.getDeliveryPay())); //배달요금
						menuitme.setTel(order.getTelNumber());                   //전화번호(배달일 경우 제공)
						menuitme.setPaOrderId(String.valueOf(order.getOrderParent()));
						
						StoreDelivery deli = storeOrderDao.getDeliveryOne(order.getDeliveryId());
						String bTime = "";
						if(deli != null){
							// orderType 주문 선택 (S : 매장이용, P : 포장, D : 배달)
							if("D".equals(order.getOrderType())){
								menuitme.setRoadAddr(deli.getRoadAddr());
								menuitme.setAddrDetail(deli.getAddrDetail());
							}else{
								menuitme.setRoadAddr("");
								menuitme.setAddrDetail("");
							}
							menuitme.setStoreMsg(deli.getStoreMsg());
							menuitme.setDeliMsg(deli.getDeliMsg());
							if("P".equals(order.getOrderType())){
								bTime = deli.getBookingTime();
							}
							menuitme.setBookingTime(bTime);
						}else{
							menuitme.setRoadAddr("");
							menuitme.setAddrDetail("");
							menuitme.setStoreMsg("");
							menuitme.setDeliMsg("");
							menuitme.setBookingTime(bTime);
						}
						
						List<MenuListJsonPrintItem> menuListItem = new ArrayList<MenuListJsonPrintItem>();
						// DB에 저장되어 있는 메뉴 List
						List<StoreOrderList> resList = storeOrderDao.getOrderList(order.getOrderNumber());
						for(StoreOrderList storeOrderListOne : resList){
							MenuListJsonPrintItem printItem = new MenuListJsonPrintItem();
							printItem.setProductID(String.valueOf(storeOrderListOne.getOrderMenuId()));
							printItem.setProductName(storeOrderListOne.getOrderMenuName());
							printItem.setOrderCount(String.valueOf(storeOrderListOne.getOrderMenuAmount()));
							printItem.setOrderPrice(String.valueOf(storeOrderListOne.getOrderMenuAmt()));
							printItem.setOrderMenuPacking(String.valueOf(storeOrderListOne.getOrderMenuPacking()));
							printItem.setEss(storeOrderListOne.getOrderSelectEss());
							printItem.setAdd(storeOrderListOne.getOrderSelectAdd());
							
							menuListItem.add(printItem);
						}
						menuitme.setOrderMenu(menuListItem);
						
						menuListMake.add(menuitme);
					}
				}
			}
		}
		
		// 2019.04.17 주문이 취소된 내역을 가져온다. 
		// 모든 Device 에 대해서 가져온다. 
		List<StoreOrder> storeOrderCancelList = storeOrderDao.getMenuCancelListTime(res.getId(), time);
		if(storeOrderCancelList.size() > 0){
			for(StoreOrder order : storeOrderCancelList){
				if(order.getOrderPayId() > 0){
					StoreOrderPay orderPay = storeOrderDao.getOrderPayAuthDate(order.getOrderPayId());
					if(!"".equals(orderPay.getPayAuthDate())){
						MenuJsonPrintItem menuitme = new MenuJsonPrintItem();
						menuitme.setRecommand(String.valueOf(order.getId()));
						menuitme.setStoreName(res.getStoreName());
						menuitme.setMenuCancelYN("Y"); //결제 취소에 대한 여부 - Y:결제취소완료 / N:결제완료 메뉴 주문
						// MS : 스마일페이 모바일  / ME : easypay
						if("MS".equals(order.getOrderDevice())){
							// 모바일 결제 승인 일자가 181126144622 으로 넘어옴
							Calendar calendar = new GregorianCalendar(Locale.KOREA);
							int nYear = calendar.get(Calendar.YEAR);
							String nYearSt = String.valueOf(nYear);
							menuitme.setOrderDate(nYearSt.substring(0, 2) + orderPay.getPayAuthDate());	//결제 승인 일자
							
							logger.info("order.getOrderPayment() >>> [{}]", menuitme.getOrderDate());
						}else{
							
							logger.info("order.getOrderCancelId() [{}]", order.getOrderCancelId());
							StoreOrderVerification verifi = storeCancelService.getStoreCancelOne(order.getOrderCancelId());
							
							logger.info("verifi.getStoreCancelId() [{}]", verifi.getStoreCancelId());
							StoreOrderCancel orderCancel = storeCancelService.get(verifi.getStoreCancelId());
							String cancelDate = orderCancel.getCancelDate();
							
							logger.info("cancelDate [{}]", cancelDate);
							if(Util.isNotValid(cancelDate)){
					    		SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
								Date creationDate = orderCancel.getWhoCreationDate();
								cancelDate = yyyyMMddHHmmss.format(creationDate);
								
								logger.info("WhoCreationDate >> cancelDate [{}]", cancelDate);
							}
							
							menuitme.setOrderDate(orderCancel.getCancelDate());	//취소 승일 일자
						}
						menuitme.setOrderNumber(order.getOrderSeq());
						menuitme.setGoodsAmt("-"+String.valueOf(order.getGoodsAmt()));
						menuitme.setOrderTable(order.getOrderTable());
						
						// 2019.11.07 pos - 배달 내용 추가
						menuitme.setDevice(order.getOrderDevice());             //결제 기기(K:키오스크/M:모바일)
						menuitme.setPaOrderId(String.valueOf(order.getOrderParent()));
						
						if("MS".equals(order.getOrderDevice()) || "ME".equals(order.getOrderDevice())){
							menuitme.setDevice("M");
						}
						menuitme.setOrderType(order.getOrderType());       //주문선택(S:매장/P:포장/D:배달)        
						menuitme.setPayment(order.getPayment());           //결제정보(AD:선불/DE:후불)       
						menuitme.setTel(order.getTelNumber());                   //전화번호(배달일 경우 제공)
						
						StoreDelivery deli = storeOrderDao.getDeliveryOne(order.getDeliveryId());
						String bTime = "";
						if(deli != null){
							// orderType 주문 선택 (S : 매장이용, P : 포장, D : 배달)
							if("D".equals(order.getOrderType())){
								menuitme.setRoadAddr(deli.getRoadAddr());
								menuitme.setAddrDetail(deli.getAddrDetail());
							}else{
								menuitme.setRoadAddr("");
								menuitme.setAddrDetail("");
							}
							menuitme.setStoreMsg(deli.getStoreMsg());
							menuitme.setDeliMsg(deli.getDeliMsg());
							if("P".equals(order.getOrderType())){
								bTime = deli.getBookingTime();
							}
							menuitme.setBookingTime(bTime);
						}else{
							menuitme.setRoadAddr("");
							menuitme.setAddrDetail("");
							menuitme.setStoreMsg("");
							menuitme.setDeliMsg("");
							menuitme.setBookingTime(bTime);
						}
						
						List<MenuListJsonPrintItem> menuListItem = new ArrayList<MenuListJsonPrintItem>();
						// DB에 저장되어 있는 메뉴 List
						List<StoreOrderList> resList = storeOrderDao.getOrderList(order.getOrderNumber());
						for(StoreOrderList storeOrderListOne : resList){
							MenuListJsonPrintItem printItem = new MenuListJsonPrintItem();
							printItem.setProductID(String.valueOf(storeOrderListOne.getOrderMenuId()));
							printItem.setProductName(storeOrderListOne.getOrderMenuName());
							printItem.setOrderCount("-"+String.valueOf(storeOrderListOne.getOrderMenuAmount()));
							printItem.setOrderPrice("-"+String.valueOf(storeOrderListOne.getOrderMenuAmt()));
							printItem.setOrderMenuPacking(String.valueOf(storeOrderListOne.getOrderMenuPacking()));
							printItem.setEss(storeOrderListOne.getOrderSelectEss());
							printItem.setAdd(storeOrderListOne.getOrderSelectAdd());
							
							menuListItem.add(printItem);
						}
						menuitme.setOrderMenu(menuListItem);
						
						menuListMake.add(menuitme);
					}
				}
			}
		}
		
		return menuListMake;
	}

	@Override
	public void saveOrUpdate(StoreDelivery deli) {
		storeOrderDao.saveOrUpdate(deli);
	}
	
	@Override
	public RefillView refillbyOrderList(String storeKey, String viewTel, String rfMenuId) {
		RefillView refillView = new RefillView();
		Store res = storeService.getStoreByStoreKey(storeKey);
		if(res == null){
			logger.error("refillbyOrderList [{}] storeKey [{}]", "매장을 조회 할수 없습니다.", storeKey);
			
			refillView.setPaOrderId("-9999");
			refillView.setRFyn("N");
			refillView.setRFCnt("-9999");
			return refillView;
		}
		
		refillView = refillViewFind(res, viewTel, rfMenuId, refillView);
		
		return refillView;
	}

	public RefillView refillViewFind(Store res, String viewTel, String rfMenuId, RefillView refillView) {

		// 당일 00시 00분 부터 지금 까지 결제된 내역을 조회 
		Calendar afterTime = Calendar.getInstance();
		afterTime.set(Calendar.HOUR_OF_DAY , 00);
		afterTime.set(Calendar.MINUTE, 00);
		afterTime.set(Calendar.SECOND, 00);
		afterTime.set(Calendar.MILLISECOND, 0);
		Date fromDate  = afterTime.getTime();
		Date toDate = new Date(); // 현재 시간 
		
		List<StoreOrder> orderList = storeOrderDao.getOrderListDatebyTelPhone(fromDate, toDate, res.getId(), viewTel);
		boolean refill = false;
		if(orderList.size()> 0){
			for(StoreOrder order : orderList){
				if(!"Y".equals(order.getOrderCancelStatus()) && !"RF".equals(order.getOrderTid())){
					List<StoreOrderList> resOrderList = storeOrderDao.getOrderList(order.getOrderNumber());
					if(resOrderList.size() > 0){
						for(StoreOrderList list : resOrderList){
							String orderMenuId = String.valueOf(list.getOrderMenuId());
							if(rfMenuId.equals(orderMenuId)){
								refill = true;
								refillView.setPaOrderId(String.valueOf(order.getId()));
								refillView.setRFyn("Y");
								// 2019.11.11 리필 가능 횟수에 대한 정의가 없으므로 -1(무한리필) 고정
								refillView.setRFCnt("-1");
							}
							if(refill){
								break;
							}
						}
					}
				}
				
				if(refill){
					break;
				}
			}
		}else{
			refillView.setPaOrderId("0");
			refillView.setRFyn("N");
			refillView.setRFCnt("-1");
			return refillView;
		}
		
		return refillView;
	}

	@Override
	public RefillView refillbyOrderListKiosk(int storeId, String deviceId, String viewTel, String rfMenuId) {
		RefillView refillView = new RefillView();
		Store res = storeService.getStore(storeId);
		if(res == null){
			logger.error("refillbyOrderListKiosk [{}] storeId [{}]", "매장을 조회 할수 없습니다.", storeId);
			
			refillView.setPaOrderId("-9999");
			refillView.setRFyn("N");
			refillView.setRFCnt("-9999");
			return refillView;
		}
		Device device = devService.getDeviceByUkid(deviceId);
		if(device == null){
        	logger.error("refillbyOrderListKiosk [{}] deviceId [{}]", "기기를 조회 할수 없습니다. ", deviceId);
        	
			refillView.setPaOrderId("-9999");
			refillView.setRFyn("N");
			refillView.setRFCnt("-9999");
			return refillView;
		}
		// 상점 정보와 deviceID의 상점 정보가 같은 지 확인 하고 다를 경우 조회가 되지 않도록 한다.
		if(res.getId() != device.getStore().getId()){
			logger.error("refillbyOrderListKiosk [{}] storeId [{}]", "상점 정보과 device 정보를 확인하여 주시기 바랍니다.", storeId);
        	logger.error("refillbyOrderListKiosk [{}] deviceId [{}]", "상점 정보과 device 정보를 확인하여 주시기 바랍니다.", deviceId);
			refillView.setPaOrderId("-9999");
			refillView.setRFyn("N");
			refillView.setRFCnt("-9999");
			return refillView;
		}
		
		refillView = refillViewFind(res, viewTel, rfMenuId, refillView);
		
		return refillView;
	}

	@Override
	public void telNumberAddrbyTime(Date startDate, Date endDate) {
		logger.info("==============================================================");
		//  
		// 1. 주문정보중 전화 번호가 있는지 확인 (매장 / 포장 / 배달  모두 전화번호가 있을수 있으므로 전부 찾는다.)
		// 2. 찾아온 전화번호 목록 중 배달 일 경우 (orderType D 일 경우 조회 )
		// 3. deliveryId로 조회 후 해당 주소내용 삭제
		// 
		List<StoreOrder> orderlist = storeOrderDao.telNumberbyTime(startDate, endDate);
		if(orderlist.size() > 0){
			for(StoreOrder order :  orderlist){
				logger.info("order.getOrderType() [{}], order.getDeliveryId()[{}]", order.getOrderType(), order.getDeliveryId());
				if("D".equals(order.getOrderType()) && order.getDeliveryId() > 0 ){
					StoreDelivery deli = storeOrderDao.getDeliveryOne(order.getDeliveryId());
					deli.setRoadAddr("");
					deli.setAddrDetail("");
					deli.touchWho(-1);
					logger.info("Delete RoadAddr / AddrDetail > deli.getId() [{}]", deli.getId());
					storeOrderDao.saveOrUpdate(deli);
				}
				order.setTelNumber("");
				order.touchWho("-1");
				logger.info("Delete TelNumber > order.getId() [{}]", order.getId());
				logger.info("");
				storeOrderDao.saveOrUpdate(order);
			}
		}
		
		logger.info("");
		logger.info("orderlist.size() [{}]", orderlist.size());
		logger.info("==============================================================");
		
	}
}
