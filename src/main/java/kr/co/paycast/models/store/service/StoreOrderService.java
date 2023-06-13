package kr.co.paycast.models.store.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.store.StoreDelivery;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderCook;
import kr.co.paycast.models.store.StoreOrderList;
import kr.co.paycast.models.store.StoreOrderPay;
import kr.co.paycast.viewmodels.self.MenuPayItem;
import kr.co.paycast.viewmodels.store.MenuJsonPrintItem;
import kr.co.paycast.viewmodels.store.RefillView;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StoreOrderService {
	// Common
	public void flush();

	public StoreOrder getDplyStoreOrderPrint(int id);

	public void saveOrUpdate(StoreOrder condFile);
	
	public void saveOrder(StoreOrder orderDao, List<MenuPayItem> payitemList);

	public StoreOrder getOrder(int storeId, String oID);
	public StoreOrder getOrder(int storeId);

	public StoreOrder getOrderbyOrderNum(String sp_order_no);
	
	public void saveOrUpdate(StoreOrderList storeOrderList);
	
	// 주문 목록 조회 
	public List<StoreOrderList> getOrderListbyNumber(String orderNumber);
	
	public String saveOrderPay(StoreOrder orderRes, StoreOrderPay orderPay, Locale locale, HttpSession session);

	public void deleteOrder(int storeId, String order);

	// 주문 메뉴 List 프린트
	public List<MenuJsonPrintItem> makeMenuListPrint(String storeId, String catId);

	//kiosk 주문 정보 저장
	public boolean kioskPayMent(MenuJsonPrintItem jsonMenu);

	//fcm 전송
	public boolean fcmTransmission(int storeId, StoreOrderCook storeOrderCook);

	//fcm 전송(storeCook 체크가 필요 없는 부분)
	public boolean fcmTransmissionOne(int storeId);
	
	// 주문 메뉴 List 포스 연동 XML
	public List<MenuJsonPrintItem> makePOSMenuList(String storeKey, Date time);

	// 배달 정보 저장 
	public void saveOrUpdate(StoreDelivery deli);

	// 리필 정보 체크 하기
	public RefillView refillbyOrderList(String storeKey, String viewTel, String rfMenuId);

	// 리필 정보 체크 하기 - kiosk
	public RefillView refillbyOrderListKiosk(int storeId, String deviceId, String viewTel, String rfMenuId);

	// 주문정보 전화번호 및 주소 삭제 하기 위해 사용
	public void telNumberAddrbyTime(Date startDate, Date endDate);
	
}
