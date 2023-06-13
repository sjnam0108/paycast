package kr.co.paycast.models.store.dao;

import java.util.Date;
import java.util.List;

import kr.co.paycast.models.store.StoreDelivery;
import kr.co.paycast.models.store.StoreOrder;
import kr.co.paycast.models.store.StoreOrderList;
import kr.co.paycast.models.store.StoreOrderPay;


public interface StoreOrderDao {
	
	public void testlog();

	public void saveOrUpdate(StoreOrder condFile);
	
	public void saveOrder(StoreOrder orderDao);

	public void updateOrder(StoreOrder orderRes);
	
	public void saveOrderList(List<StoreOrderList> orderList);

	public StoreOrder getOrder(int oidStoreId, String orderNum);
	public StoreOrder getOrder(int oidStoreId);
	
	public StoreOrder getOrderNumberByStoreOrderNum(String orderNum);

	public StoreOrder getOrderOne(int id);

	public StoreOrder getOrderbyOrderNum(String orderNum);
	
	public void saveOrUpdate(StoreOrderPay orderPay);
	
	public int saveOrderPay(StoreOrderPay orderPay);

	public int getOrderCount(int storeId);

	public void delete(StoreOrder res);

	public void saveOrUpdate(StoreOrderList storeOrderList);
	
	public List<StoreOrderList> getOrderList(String order);
	
	public List<StoreOrderList> getOrderListByStatus(String order,String status);

	public void deleteOrderList(List<StoreOrderList> resList);

	// 매장의 주문 내역을 가져온다.
	public List<StoreOrder> getMenuOrderListStore(int storeId, String orderPrint);

	//프린트 출력시 취소내역 보내기
	public List<StoreOrder> getMenuCancelListStore(int storeId, String device, String cancelprintstatus);
	
	// 결제 정보를 가져온다.
	public StoreOrderPay getOrderPayAuthDate(int orderPayId);

	// 승인 번호를 가지고 결제 정보가 등록 되어 있는지 확인
	public boolean isRegisteredPayAuthCode(int storeId, String authCode);
	
	public List<StoreOrder> getOrderListDate(Date fromDate1, Date toDate2, int storeId);
//	public List<StoreOrder> getOrderListSid(int storeId);

	// StoreOrderList의 정보를 가져온다 
	public StoreOrderList getOrderListOne(int storeOrderListId);

	public List<StoreOrder> getMenuCancelListStorebyAllDevice(int storeId,	String cancelprintstatus);
	
	public List<StoreOrder> getMenuCancelListStorebyPadDevice(int storeId,	String canceldidstatus);

	// 특정 시간 동안 주문 메뉴를 가져온다.
	public List<StoreOrder> getMenuOrderListTime(int storeId, Date time);

	// 특정 시간 동안 취소된 주문 메뉴를 가져온다.
	public List<StoreOrder> getMenuCancelListTime(int storeId, Date time);

	// 배달 내용(주소 / 상세주소 / 매장요청메시지 / 배달 요청 메시지)
	public void saveOrUpdate(StoreDelivery deli);

	// 배달 정보 조회
	public StoreDelivery getDeliveryOne(int deliveryId);

	// 당일 주문 정보중 해당하는 전화 번호로 조회 
	public List<StoreOrder> getOrderListDatebyTelPhone(Date fromDate,
			Date toDate, int storeId, String viewTel);

	// 전화번호와 주소 삭제를 위해서 사용
	public List<StoreOrder> telNumberbyTime(Date startDate, Date endDate);
	
}
