package kr.co.paycast.models.store;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="STORE_ORDER_CANCEL")
public class StoreOrderCancel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_order_cancel_seq_gen")
	@SequenceGenerator(name = "store_order_cancel_seq_gen", sequenceName = "STORE_ORDER_CANCEL_SEQ")
	@Column(name = "STORE_ORDER_CANCEL_ID")
	private int id;

	@Column(name = "PAY_METHOD")
	private String payMethod; //지불수단
	
	@Column(name = "PAY_NAME", nullable = false)
	private String payName; //지불수단명
	
	@Column(name = "MID", nullable = false)
	private String mid; //상점ID
	
	@Column(name = "TID", nullable = false)
	private String tid; //거래번호
	
	@Column(name = "CANCEL_AMT")
	private String cancelAmt; //취소금액
	
	@Column(name = "CANCEL_MSG")
	private String cancelMSG; //취소메세지
	
	@Column(name = "RESULT_CODE")
	private String resultCode; //결과코드
	
	@Column(name = "RESULT_MSG")
	private String resultMsg; //결과메시지
	
	@Column(name = "CANCEL_DATE")
	private String cancelDate; //취소일자
	
	@Column(name = "CANCEL_TIME")
	private String cancelTime; //취소시간
	
	@Column(name = "CANCEL_NUM")
	private String cancelNum; //취소번호
	
	@Column(name = "MOID")
	private String moid; //주문번호
	
	@Column(name = "CARD_NUMBER")
	private String cardNumber; //카드번호
	
	@Column(name = "CARD_NAME")
	private String cardName; //카드회사
	
	@Column(name = "FN_CD")
	private String fnCd; //매입사
	
	@Column(name = "FN_NAME")
	private String fnName; //매입사명

	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private String whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private String whoLastUpdatedBy;

	public StoreOrderCancel() {}
	
	public StoreOrderCancel(String payMethod, String payName, String mid, String tid, String cancelAmt, String cancelMSG,
			String resultCode, String resultMsg, String cancelDate, String cancelTime, String cancelNum, String moid) {
		this.payMethod = payMethod;
		this.payName = payName;
		this.mid = mid;
		this.tid = tid;
		this.cancelAmt = cancelAmt;
		this.cancelMSG = cancelMSG;
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.cancelDate = cancelDate;
		this.cancelTime = cancelTime;
		this.cancelNum = cancelNum;
		this.moid = moid;
		
		touchWhoC(moid);
	}

	public void touchWhoC(String orderNumber) {
		this.whoCreatedBy = orderNumber;
		this.whoCreationDate = new Date();
		touchWho(orderNumber);
	}
	
	public void touchWho(String orderNumber) {
		this.whoLastUpdatedBy = orderNumber;
		this.whoLastUpdateDate = new Date();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getCancelAmt() {
		return cancelAmt;
	}

	public void setCancelAmt(String cancelAmt) {
		this.cancelAmt = cancelAmt;
	}

	public String getCancelMSG() {
		return cancelMSG;
	}

	public void setCancelMSG(String cancelMSG) {
		this.cancelMSG = cancelMSG;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getCancelNum() {
		return cancelNum;
	}

	public void setCancelNum(String cancelNum) {
		this.cancelNum = cancelNum;
	}

	public String getMoid() {
		return moid;
	}

	public void setMoid(String moid) {
		this.moid = moid;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getFnCd() {
		return fnCd;
	}

	public void setFnCd(String fnCd) {
		this.fnCd = fnCd;
	}

	public String getFnName() {
		return fnName;
	}

	public void setFnName(String fnName) {
		this.fnName = fnName;
	}

	public Date getWhoCreationDate() {
		return whoCreationDate;
	}

	public void setWhoCreationDate(Date whoCreationDate) {
		this.whoCreationDate = whoCreationDate;
	}

	public Date getWhoLastUpdateDate() {
		return whoLastUpdateDate;
	}

	public void setWhoLastUpdateDate(Date whoLastUpdateDate) {
		this.whoLastUpdateDate = whoLastUpdateDate;
	}

	public String getWhoCreatedBy() {
		return whoCreatedBy;
	}

	public void setWhoCreatedBy(String whoCreatedBy) {
		this.whoCreatedBy = whoCreatedBy;
	}

	public String getWhoLastUpdatedBy() {
		return whoLastUpdatedBy;
	}

	public void setWhoLastUpdatedBy(String whoLastUpdatedBy) {
		this.whoLastUpdatedBy = whoLastUpdatedBy;
	}

}
