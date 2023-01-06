package kr.co.paycast.viewmodels.store;

public class RefillView  {
	
	private String RFyn = "N"; // 리필 가능 여부 (Y : 가능 / N : 불가능)
	private String paOrderId = "0"; //리필 하려는 주문정보의  seq 번호(부모 seq)
	private String RFCnt = "-1"; //리필 주문 가능 count (-1 일 경우 무한 리필 및 불가능)
	
	public String getRFyn() {
		return RFyn;
	}
	public void setRFyn(String rFyn) {
		RFyn = rFyn;
	}
	public String getPaOrderId() {
		return paOrderId;
	}
	public void setPaOrderId(String paOrderId) {
		this.paOrderId = paOrderId;
	}
	public String getRFCnt() {
		return RFCnt;
	}
	public void setRFCnt(String rFCnt) {
		RFCnt = rFCnt;
	}
}
