package kr.co.paycast.viewmodels.store;

public class StoreSalesView  {
	
	private String boardYn = "N";//화면에서의 board 처리
	private String date; //일시
	private String menu; //메뉴
	private String amount; //수량
	private String amt; //단가
	private String totalamt; //결제금액
	private String fnname; //결제유형
	private String orderDevice; //주문유형
	private String ordernum; //주문 번호
	private String orderNumButton = ""; //주문 번호 button html
	private String packing; //포장 및 배달 
	
	private String telNumber = ""; //알림톡을 사용 하기 위한 전화 번호
	private String storeMsg = ""; //매장 요청 메시지
	private String deliMsg = ""; //결제시 사용되는 주문명
	private String roadAddr = "";
	private String addrDetail = "";
	
	public String getBoardYn() {
		return boardYn;
	}
	
	public void setBoardYn(String boardYn) {
		this.boardYn = boardYn;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getMenu() {
		return menu;
	}
	
	public void setMenu(String menu) {
		this.menu = menu;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getAmt() {
		return amt;
	}
	
	public void setAmt(String amt) {
		this.amt = amt;
	}
	
	public String getTotalamt() {
		return totalamt;
	}
	
	public void setTotalamt(String totalamt) {
		this.totalamt = totalamt;
	}
	
	public String getFnname() {
		return fnname;
	}
	
	public void setFnname(String fnname) {
		this.fnname = fnname;
	}
	
	public String getOrdernum() {
		return ordernum;
	}
	
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	
	public String getOrderNumButton() {
		return orderNumButton;
	}

	public void setOrderNumButton(String orderNumButton) {
		this.orderNumButton = orderNumButton;
	}

	public String getOrderDevice() {
		return orderDevice;
	}
	
	public void setOrderDevice(String orderDevice) {
		this.orderDevice = orderDevice;
	}
	
	public String getPacking() {
		return packing;
	}
	
	public void setPacking(String packing) {
		this.packing = packing;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
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
	
}
