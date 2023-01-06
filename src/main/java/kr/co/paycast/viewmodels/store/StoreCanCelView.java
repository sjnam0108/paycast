package kr.co.paycast.viewmodels.store;

public class StoreCanCelView  {
	
	private String storeOrderId; // storeOrderId
	private String verificationId; // VerificationId
	
	private String orderSeq; //주문번호
	private String payAuthDate; //승인일자
	private String payAuthCode; //승인번호
	private String payAmt; //결제 금액
	private String orderDevice; //모바일 결제 프로그램 확인 (ME:easypay / MS:smilepay)
	
	private String payAuthYN; //해당 인증키 사용 가능 여부
	
	public String getStoreOrderId() {
		return storeOrderId;
	}

	public void setStoreOrderId(String storeOrderId) {
		this.storeOrderId = storeOrderId;
	}

	public String getVerificationId() {
		return verificationId;
	}

	public void setVerificationId(String verificationId) {
		this.verificationId = verificationId;
	}

	public String getOrderSeq() {
		return orderSeq;
	}
	
	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}
	
	public String getPayAuthDate() {
		return payAuthDate;
	}
	
	public void setPayAuthDate(String payAuthDate) {
		this.payAuthDate = payAuthDate;
	}
	
	public String getPayAuthCode() {
		return payAuthCode;
	}
	
	public void setPayAuthCode(String payAuthCode) {
		this.payAuthCode = payAuthCode;
	}
	
	public String getPayAmt() {
		return payAmt;
	}
	
	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}
	
	public String getOrderDevice() {
		return orderDevice;
	}

	public void setOrderDevice(String orderDevice) {
		this.orderDevice = orderDevice;
	}

	public String getPayAuthYN() {
		return payAuthYN;
	}
	
	public void setPayAuthYN(String payAuthYN) {
		this.payAuthYN = payAuthYN;
	}
}
