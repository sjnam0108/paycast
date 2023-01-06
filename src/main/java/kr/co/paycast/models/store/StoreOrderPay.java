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
@Table(name="STORE_ORDER_PAY")
public class StoreOrderPay {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "storeorderpay_seq_gen")
	@SequenceGenerator(name = "storeorderpay_seq_gen", sequenceName = "STORE_ORDER_PAY_SEQ")
	@Column(name = "STORE_ORDERPAY_ID")
	private int id;

	@Column(name = "STORE_ID")
	private int storeId = 0; //매장 번호
	
	@Column(name = "STORE_ORDER_NUMBER", nullable = false)
	private String orderNumber; //주문번호(OID)
	
	@Column(name = "STORE_ORDER_TID", nullable = false)
	private String orderTid; //메뉴 결제 거래번호 / 키오스크에서는 거래고유번호 / RF(리필) / CP(쿠폰으로 0원 결제완료) / PO(포인트로 0원 결제 완료)
	
	// easypay기준 결제 수단(payMethod > 11:신용카드, 21:계좌이체, 22:가상계좌, 31:휴대폰, 50:선불결제, 60:간편결제)
	@Column(name = "ORDER_PAY_METHOD")
	private String payMethod; //지불수단 / RF(리필) / CP(쿠폰으로 0원 결제완료) / PO(포인트로 0원 결제 완료)
	
	@Column(name = "ORDER_PAY_MID", nullable = false)
	private String payMid; //상점 ID
	
	@Column(name = "ORDER_PAY_AMT", nullable = false)
	private String payAmt; //결제 금액
	
	@Column(name = "ORDER_DELIVERY_PAY", nullable = true)
	private String deliveryPay; //배달 요금
	
	@Column(name = "ORDER_PAY_GOODSNAME", nullable = false)
	private String goodsname; //상품명

	@Column(name = "ORDER_PAY_OID", nullable = false)
	private String payOid; //주문번호
	
	// 주의 ! easypay 경우 :20191002100403
	// 주의 ! smilepay 경우 :191002100403
	@Column(name = "ORDER_PAY_AUTHDATE", nullable = false)
	private String payAuthDate; //승인일자
	
	@Column(name = "ORDER_PAY_AUTHCODE", nullable = false)
	private String payAuthCode; //승인번호
	
	@Column(name = "ORDER_PAY_RESULTCODE")
	private String payResultCode; //결과코드
	
	@Column(name = "ORDER_PAY_RESULTMSG")
	private String payResultMsg; //결과메시지
	
	@Column(name = "ORDER_PAY_SIGNVALUE")
	private String paySignValue; //위변조 사인값  / easypay (추적 번호)
	
	@Column(name = "ORDER_PAY_FN_CD")
	private String payFnCd; //결제카드사 코드 / 발급사(키오스크 사용)
	
	@Column(name = "ORDER_PAY_FN_NAME")
	private String payFnName; //결제카드사명 / 발급사명(키오스크 사용)
	
	@Column(name = "ORDER_PAY_FN_CD1")
	private String payFnCd1; //매입사(키오스크 사용)
	
	@Column(name = "ORDER_PAY_FN_NAME1")
	private String payFnName1; //매입사명(키오스크 사용)
	
	@Column(name = "ORDER_PAY_CARDQUOTA")
	private String payCardQuota; //할부개월수
	
	@Column(name = "ORDER_PAY_ACQUCARDCODE")
	private String payAcquCardcode; //매입사코드
	
	@Column(name = "ORDER_PAY_DIVIDEINFO", length=2000)
	private String payDivideInfo; //종합정보
	
	@Column(name = "STAT_CD")
	private String statCd = ""; //easyPay 상태코드
	
	@Column(name = "STAT_MSG")
	private String statMsg = ""; //easyPay 상태메시지
	
	@Column(name = "PAY_TYPE")
	private String payType = ""; //easyPay 결제수단
	
	@Column(name = "CARD_NO")
	private String cardNo = ""; //카드번호
	
	@Column(name = "NOINT")
	private String noint = ""; //easyPay 무이자여부
	
	@Column(name = "PART_CANCEL_YN")
	private String partCancelYn = ""; //easyPay 부분취소 가능여부
	
	@Column(name = "CARD_GUBUN")
	private String cardGubun = ""; //easyPay 신용카드 종류
	
	@Column(name = "CARD_BIZ_GUBUN")
	private String cardBizGubun = ""; //easyPay 신용카드 구분
	
	@Column(name = "CPON_FLAG")
	private String cponFlag = ""; //easyPay 쿠폰사용 유무
	
	@Column(name = "BANK_CD")
	private String bankCd = ""; //easyPay 은행코드
	
	@Column(name = "BANK_NM")
	private String bankNm = ""; //easyPay 은행명
	
	@Column(name = "ACCOUNT_NO")
	private String accountNo = ""; //easyPay 계좌번호
	
	@Column(name = "DEPOSIT_NM")
	private String depositNm = ""; //easyPay 입금자명
	
	@Column(name = "EXPIRE_DATE")
	private String expireDate = ""; //easyPay 계좌사용만료일
	
	@Column(name = "CASH_RES_CD")
	private String cashResCd = ""; //easyPay 현금영수증 결과코드
	
	@Column(name = "CASH_RES_MSG")
	private String cashResMsg = ""; //easyPay 현금영수증 결과메세지
	
	@Column(name = "CASH_AUTH_NO")
	private String cashAuthNo = ""; //easyPay 현금영수증 승인번호
	
	@Column(name = "CASH_TRAN_DATE")
	private String cashTranDate = ""; //easyPay 현금영수증 승인일시
	
	@Column(name = "CASH_ISSUE_TYPE")
	private String cashIssueType = ""; //easyPay 현금영수증 발행용도
	
	@Column(name = "CASH_AUTH_TYPE")
	private String cashAuthType = ""; //easyPay 현금영수증 인증구분
	
	@Column(name = "CASH_AUTH_VALUE")
	private String cashAuthValue = ""; //easyPay 현금영수증 인증번호
	
	@Column(name = "AUTH_ID")
	private String authId = ""; //easyPay 휴대폰 PhoneID(인증성공시 소액결제사에서 부여된 값)
	
	@Column(name = "BILLID")
	private String billid = ""; //easyPay 휴대폰 인증번호
	
	@Column(name = "MOBILE_NO")
	private String mobileNo = ""; //easyPay 휴대폰번호
	
	@Column(name = "MOB_ANSIM_YN")
	private String mobAnsimYn = ""; //easyPay 안심결제 사용유무
	
	@Column(name = "CP_CD")
	private String cpCd = ""; //easyPay 포인트사/쿠폰사(간편결제사 및 선불 결제사 코드)
	
	@Column(name = "REM_AMT")
	private String remAmt = ""; //easyPay 선불 잔액잔액
	
	@Column(name = "BK_PAY_YN")
	private String bkPayYn = ""; //easyPay 장바구니 결제여부
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private String whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private String whoLastUpdatedBy;

	public StoreOrderPay() {}
	
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

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderTid() {
		return orderTid;
	}

	public void setOrderTid(String orderTid) {
		this.orderTid = orderTid;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayMid() {
		return payMid;
	}

	public void setPayMid(String payMid) {
		this.payMid = payMid;
	}

	public String getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getPayOid() {
		return payOid;
	}

	public void setPayOid(String payOid) {
		this.payOid = payOid;
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

	public String getPayResultCode() {
		return payResultCode;
	}

	public void setPayResultCode(String payResultCode) {
		this.payResultCode = payResultCode;
	}

	public String getPayResultMsg() {
		return payResultMsg;
	}

	public void setPayResultMsg(String payResultMsg) {
		this.payResultMsg = payResultMsg;
	}

	public String getPaySignValue() {
		return paySignValue;
	}

	public void setPaySignValue(String paySignValue) {
		this.paySignValue = paySignValue;
	}

	public String getPayFnCd() {
		return payFnCd;
	}

	public void setPayFnCd(String payFnCd) {
		this.payFnCd = payFnCd;
	}

	public String getPayFnName() {
		return payFnName;
	}

	public void setPayFnName(String payFnName) {
		this.payFnName = payFnName;
	}

	public String getPayFnCd1() {
		return payFnCd1;
	}

	public void setPayFnCd1(String payFnCd1) {
		this.payFnCd1 = payFnCd1;
	}

	public String getPayFnName1() {
		return payFnName1;
	}

	public void setPayFnName1(String payFnName1) {
		this.payFnName1 = payFnName1;
	}

	public String getPayCardQuota() {
		return payCardQuota;
	}

	public void setPayCardQuota(String payCardQuota) {
		this.payCardQuota = payCardQuota;
	}

	public String getPayAcquCardcode() {
		return payAcquCardcode;
	}

	public void setPayAcquCardcode(String payAcquCardcode) {
		this.payAcquCardcode = payAcquCardcode;
	}

	public String getPayDivideInfo() {
		return payDivideInfo;
	}

	public void setPayDivideInfo(String payDivideInfo) {
		this.payDivideInfo = payDivideInfo;
	}

	public String getStatCd() {
		return statCd;
	}

	public void setStatCd(String statCd) {
		this.statCd = statCd;
	}

	public String getStatMsg() {
		return statMsg;
	}

	public void setStatMsg(String statMsg) {
		this.statMsg = statMsg;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getNoint() {
		return noint;
	}

	public void setNoint(String noint) {
		this.noint = noint;
	}

	public String getPartCancelYn() {
		return partCancelYn;
	}

	public void setPartCancelYn(String partCancelYn) {
		this.partCancelYn = partCancelYn;
	}

	public String getCardGubun() {
		return cardGubun;
	}

	public void setCardGubun(String cardGubun) {
		this.cardGubun = cardGubun;
	}

	public String getCardBizGubun() {
		return cardBizGubun;
	}

	public void setCardBizGubun(String cardBizGubun) {
		this.cardBizGubun = cardBizGubun;
	}

	public String getCponFlag() {
		return cponFlag;
	}

	public void setCponFlag(String cponFlag) {
		this.cponFlag = cponFlag;
	}

	public String getBankCd() {
		return bankCd;
	}

	public void setBankCd(String bankCd) {
		this.bankCd = bankCd;
	}

	public String getBankNm() {
		return bankNm;
	}

	public void setBankNm(String bankNm) {
		this.bankNm = bankNm;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getDepositNm() {
		return depositNm;
	}

	public void setDepositNm(String depositNm) {
		this.depositNm = depositNm;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getCashResCd() {
		return cashResCd;
	}

	public void setCashResCd(String cashResCd) {
		this.cashResCd = cashResCd;
	}

	public String getCashResMsg() {
		return cashResMsg;
	}

	public void setCashResMsg(String cashResMsg) {
		this.cashResMsg = cashResMsg;
	}

	public String getCashAuthNo() {
		return cashAuthNo;
	}

	public void setCashAuthNo(String cashAuthNo) {
		this.cashAuthNo = cashAuthNo;
	}

	public String getCashTranDate() {
		return cashTranDate;
	}

	public void setCashTranDate(String cashTranDate) {
		this.cashTranDate = cashTranDate;
	}

	public String getCashIssueType() {
		return cashIssueType;
	}

	public void setCashIssueType(String cashIssueType) {
		this.cashIssueType = cashIssueType;
	}

	public String getCashAuthType() {
		return cashAuthType;
	}

	public void setCashAuthType(String cashAuthType) {
		this.cashAuthType = cashAuthType;
	}

	public String getCashAuthValue() {
		return cashAuthValue;
	}

	public void setCashAuthValue(String cashAuthValue) {
		this.cashAuthValue = cashAuthValue;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getBillid() {
		return billid;
	}

	public void setBillid(String billid) {
		this.billid = billid;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getMobAnsimYn() {
		return mobAnsimYn;
	}

	public void setMobAnsimYn(String mobAnsimYn) {
		this.mobAnsimYn = mobAnsimYn;
	}

	public String getCpCd() {
		return cpCd;
	}

	public void setCpCd(String cpCd) {
		this.cpCd = cpCd;
	}

	public String getRemAmt() {
		return remAmt;
	}

	public void setRemAmt(String remAmt) {
		this.remAmt = remAmt;
	}

	public String getBkPayYn() {
		return bkPayYn;
	}

	public void setBkPayYn(String bkPayYn) {
		this.bkPayYn = bkPayYn;
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

	public String getDeliveryPay() {
		return deliveryPay;
	}

	public void setDeliveryPay(String deliveryPay) {
		this.deliveryPay = deliveryPay;
	}
	
}
