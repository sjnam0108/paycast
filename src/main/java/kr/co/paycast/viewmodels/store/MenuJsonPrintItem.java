package kr.co.paycast.viewmodels.store;

import java.util.List;


public class MenuJsonPrintItem  {
	
	private String tid; //거래고유번호
	private String mid; //가맹점 번호
	private String recommand; //orderID = storeId로도 사용
	private String storeName; //매장명
	private String menuCancelYN; //결제 취소 여부
	private String orderDate; //주문 날짜 시간(결제완료시간), yyyyMMddHHmmss
	private String orderNumber; //주문번호
	private String goodsAmt; //메뉴 주문 총 금액
	private String payMethod; //"CARD / 현금" 인지 확인
	private String orderTable; //테이블 번호
	private String tel; // 알림톡 사용 가능 전화번호 
	private String payment; // 결제 선불 여부(결제 정보 - AD :선불 / DE:후불)
							// kiosk 결제시 사용 타입(AD : 선불, CA : 취소, RF : 리필, DE : 후불 )
	
	private String orderType; // orderType 주문 선택 (S : 매장이용, P : 포장, D : 배달)
	private String roadAddr; // 주소
	private String addrDetail;// 상세주소
	private String storeMsg; // 매장 요청 메시지
	private String deliPrice; //배달 요금
	private String deliMsg; // 배달 요청 메시지
	private String bookingTime = ""; // 예약시간
	private String device; // 결제 기기(K:키오스크/M:모바일)
	private String paOrderId = "0"; // 부모 주문 id
	
	private List<MenuListJsonPrintItem> orderMenu; // 메뉴 목록

	private String totalindex; 
	private String goodsTotal; //메뉴 주문 총 금액
	private String authCode;  //승인 번호
	private String catId; //catId
	private String fnCd; //발급사
	private String fnName; //발급사명
	private String fnCd1; //매입사
	private String fnName1; //매입사명
	
	public String getRecommand() {
		return recommand;
	}

	public void setRecommand(String recommand) {
		this.recommand = recommand;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getMenuCancelYN() {
		return menuCancelYN;
	}

	public void setMenuCancelYN(String menuCancelYN) {
		this.menuCancelYN = menuCancelYN;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getGoodsAmt() {
		return goodsAmt;
	}

	public void setGoodsAmt(String goodsAmt) {
		this.goodsAmt = goodsAmt;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
	public String getOrderTable() {
		return orderTable;
	}

	public void setOrderTable(String orderTable) {
		this.orderTable = orderTable;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getRoadAddr() {
		return roadAddr;
	}

	public void setRoadAddr(String roadAddr) {
		this.roadAddr = roadAddr;
	}

	public String getAddrDetail() {
		return addrDetail;
	}

	public void setAddrDetail(String addrDetail) {
		this.addrDetail = addrDetail;
	}

	public String getStoreMsg() {
		return storeMsg;
	}

	public void setStoreMsg(String storeMsg) {
		this.storeMsg = storeMsg;
	}

	public String getDeliMsg() {
		return deliMsg;
	}

	public void setDeliMsg(String deliMsg) {
		this.deliMsg = deliMsg;
	}

	public String getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getPaOrderId() {
		return paOrderId;
	}

	public void setPaOrderId(String paOrderId) {
		this.paOrderId = paOrderId;
	}

	public List<MenuListJsonPrintItem> getOrderMenu() {
		return orderMenu;
	}

	public void setOrderMenu(List<MenuListJsonPrintItem> orderMenu) {
		this.orderMenu = orderMenu;
	}

	public String getTotalindex() {
		return totalindex;
	}

	public void setTotalindex(String totalindex) {
		this.totalindex = totalindex;
	}

	public String getGoodsTotal() {
		return goodsTotal;
	}

	public void setGoodsTotal(String goodsTotal) {
		this.goodsTotal = goodsTotal;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getFnName() {
		return fnName;
	}

	public void setFnName(String fnName) {
		this.fnName = fnName;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getFnCd() {
		return fnCd;
	}

	public void setFnCd(String fnCd) {
		this.fnCd = fnCd;
	}

	public String getFnCd1() {
		return fnCd1;
	}

	public void setFnCd1(String fnCd1) {
		this.fnCd1 = fnCd1;
	}

	public String getFnName1() {
		return fnName1;
	}

	public void setFnName1(String fnName1) {
		this.fnName1 = fnName1;
	}

	public String getDeliPrice() {
		return deliPrice;
	}

	public void setDeliPrice(String deliPrice) {
		this.deliPrice = deliPrice;
	}
}
