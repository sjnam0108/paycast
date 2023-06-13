package kr.co.paycast.models.store;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import kr.co.paycast.utils.Util;

@Entity
@Table(name="STORE_ORDER")
public class StoreOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "storeorder_seq_gen")
	@SequenceGenerator(name = "storeorder_seq_gen", sequenceName = "STORE_ORDER_SEQ")
	@Column(name = "STORE_ORDER_ID")
	private int id;

	@Column(name = "STORE_ID")
	private int storeId; //매장 번호
	
	@Column(name = "STORE_ORDER_NUMBER", nullable = false)
	private String orderNumber; //주문번호
	
	@Column(name = "STORE_GOODS_NAME", nullable = false)
	private String goodsName; //결제시 사용되는 주문명
	
	@Column(name = "STORE_GOODS_TOTAL", nullable = false)
	private String goodsTotal; //메뉴 주문 총 수량
	
	@Column(name = "STORE_GOODS_AMT", nullable = false)
	private int goodsAmt = 0; //메뉴 주문 총 금액
		
	@Column(name = "DISCOUNT_AMT", nullable = false)
	private int discountAmt = 0; // 쿠폰 / 포인트 사용시 사용된 할인 금액
	
	@Column(name = "PAYMENT_AMT", nullable = false)
	private int paymentAmt = 0; // 쿠폰 / 포인트 사용 후 pay에서 결제된 실 결제금액
	
	@Column(name = "SAVINGTYPE")
	private String savingType = ""; //쿠폰, 포인트에 대한 저장 type /CP= 쿠폰, PO= 포인트 (NO는 저장하지 않음)
	
	@Column(name = "USE_COUPONID")
	private int useCoupon = 0; //쿠폰 사용 매장일 경우 사용된 쿠폰 ID
	
	@Column(name = "STORE_ORDER_SEQ", nullable = true)
	private String orderSeq; //메뉴 주문 순서
	
	@Column(name = "STORE_ORDER_TID")
	private String orderTid; //메뉴 결제 거래번호
	
	@Column(name = "STORE_ORDERPAY_ID")
	private int orderPayId = 0; //결제 ID
	
	@Column(name = "STORE_ORDER_DEVICE", nullable = false, length = 2)
	private String orderDevice; //메뉴 결제 기기   키오스크 : K /모바일 (MS : 스마일페이 모바일  / ME : easypay) 
	
	@Column(name = "STORE_ORDER_PRINT", nullable = false, length = 2)
	private String orderPrint; //메뉴 프린팅 확인 여부  : 주문 생성 : N / 프린트에 내려줌 : 2 / 프린트에서 정상 받기 : Y / 결제 Error 일 경우 : E(프린트X) / 스마트로 결제금액과 저장된 결제금액이 맞지 않을경우 : H(프린트 X)  
	
	@Column(name = "STORE_ORDER_TABLE")
	private String orderTable; //매장에서 사용되는 테이블 번호를 넣어준다.
	
	@Column(name = "STORE_CANCEL_ID")
	private int orderCancelId; //취소 table ID
	
	@Column(name = "STORE_CANCEL_STATUS", nullable = false, length = 2)
	private String orderCancelStatus; //취소 상태 표시 (N : default, 1 : 취소승인번호 생성, 2 : 취소요청 (고객이 요청), Y : 취소가 완료된 상태 - 취소결과 받은상태 , F : 취소 실패)
	
	@Column(name = "STORE_CANCEL_PRINT", nullable = false, length = 2)
	private String orderCancelPrint; //취소 프린트 여부 (N : default, 1 : 취소완료 후 프린트 요청, Y : 취소 프린트 완료)
	
	@Column(name = "STORE_CANCEL_PAD", nullable = false, length = 2)
	private String orderCancelPad; //취소 패드 여부 (N : default, 1 : 취소완료 후 프린트 요청, Y : 취소 프린트 완료)

	@Column(name = "STORE_TEL", nullable = false)
	private String telNumber = ""; //알림톡을 사용 하기 위한 전화 번호
	
	@Column(name = "STORE_ORDER_TYPE", nullable = false, length = 2)
	private String orderType = ""; //주문 선택 (S : 매장이용, P : 포장, D : 배달)
	
	@Column(name = "STORE_PAYMENT", nullable = false, length = 2)
	private String payment = "AD"; //결제 정보 (선불 : AD, 후불 : DE)
	
	@Column(name = "STORE_DELIVERY_ID")
	private int deliveryId = 0; //주문 선택이 배달일 경우 사용 되는  ID
	
	@Column(name = "STORE_DELIVERY_PAY", nullable = true)
	private int deliveryPay = 0; //배달 요금
	
	@Column(name = "STORE_ORDER_PARENT")
	private int orderParent = 0;  // 리필일 경우 부모주문ID가 들어가며 정상 결제일 경우 -1 
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private String whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private String whoLastUpdatedBy;

	public StoreOrder() {}
	
	public StoreOrder(int storeId, String orderNumber, String goodsName,
			String totalindex, int goodsAmt, String device) {
		this.storeId = storeId;
		this.orderNumber = orderNumber;
		this.goodsName = goodsName;
		this.goodsTotal = totalindex;
		this.goodsAmt = goodsAmt;
		this.orderDevice = device;
		this.orderPrint = "N";
		this.orderCancelId = 0;
		this.orderCancelStatus = "N";
		this.orderCancelPrint = "N";
		this.orderCancelPad = "N";
		
		touchWhoC(orderNumber);
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
	
	public void touchWho(HttpSession session) {
		this.whoLastUpdatedBy = String.valueOf(Util.loginUserId(session));
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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsTotal() {
		return goodsTotal;
	}

	public void setGoodsTotal(String goodsTotal) {
		this.goodsTotal = goodsTotal;
	}

	public int getGoodsAmt() {
		return goodsAmt;
	}

	public void setGoodsAmt(int goodsAmt) {
		this.goodsAmt = goodsAmt;
	}

	public int getDiscountAmt() {
		return discountAmt;
	}

	public void setDiscountAmt(int discountAmt) {
		this.discountAmt = discountAmt;
	}

	public int getPaymentAmt() {
		return paymentAmt;
	}

	public void setPaymentAmt(int paymentAmt) {
		this.paymentAmt = paymentAmt;
	}

	public String getSavingType() {
		return savingType;
	}

	public void setSavingType(String savingType) {
		this.savingType = savingType;
	}

	public int getUseCoupon() {
		return useCoupon;
	}

	public void setUseCoupon(int useCoupon) {
		this.useCoupon = useCoupon;
	}

	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getOrderTid() {
		return orderTid;
	}

	public void setOrderTid(String orderTid) {
		this.orderTid = orderTid;
	}

	public String getOrderDevice() {
		return orderDevice;
	}

	public void setOrderDevice(String orderDevice) {
		this.orderDevice = orderDevice;
	}

	public String getOrderPrint() {
		return orderPrint;
	}

	public void setOrderPrint(String orderPrint) {
		this.orderPrint = orderPrint;
	}

	public int getOrderPayId() {
		return orderPayId;
	}

	public void setOrderPayId(int orderPayId) {
		this.orderPayId = orderPayId;
	}

	public String getOrderTable() {
		return orderTable;
	}

	public void setOrderTable(String orderTable) {
		this.orderTable = orderTable;
	}

	public int getOrderCancelId() {
		return orderCancelId;
	}

	public void setOrderCancelId(int orderCancelId) {
		this.orderCancelId = orderCancelId;
	}

	public String getOrderCancelStatus() {
		return orderCancelStatus;
	}

	public void setOrderCancelStatus(String orderCancelStatus) {
		this.orderCancelStatus = orderCancelStatus;
	}

	public String getOrderCancelPrint() {
		return orderCancelPrint;
	}

	public void setOrderCancelPrint(String orderCancelPrint) {
		this.orderCancelPrint = orderCancelPrint;
	}

	public String getOrderCancelPad() {
		return orderCancelPad;
	}

	public void setOrderCancelPad(String orderCancelPad) {
		this.orderCancelPad = orderCancelPad;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public int getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}
	
	public int getOrderParent() {
		return orderParent;
	}

	public void setOrderParent(int orderParent) {
		this.orderParent = orderParent;
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

	public int getDeliveryPay() {
		return deliveryPay;
	}

	public void setDeliveryPay(int deliveryPay) {
		this.deliveryPay = deliveryPay;
	}

	@Override
	public String toString() {
		return "StoreOrder [id=" + id + ", storeId=" + storeId + ", orderNumber=" + orderNumber + ", goodsName="
				+ goodsName + ", goodsTotal=" + goodsTotal + ", goodsAmt=" + goodsAmt + ", discountAmt=" + discountAmt
				+ ", paymentAmt=" + paymentAmt + ", savingType=" + savingType + ", useCoupon=" + useCoupon
				+ ", orderSeq=" + orderSeq + ", orderTid=" + orderTid + ", orderPayId=" + orderPayId + ", orderDevice="
				+ orderDevice + ", orderPrint=" + orderPrint + ", orderTable=" + orderTable + ", orderCancelId="
				+ orderCancelId + ", orderCancelStatus=" + orderCancelStatus + ", orderCancelPrint=" + orderCancelPrint
				+ ", orderCancelPad=" + orderCancelPad + ", telNumber=" + telNumber + ", orderType=" + orderType
				+ ", payment=" + payment + ", deliveryId=" + deliveryId + ", deliveryPay=" + deliveryPay
				+ ", orderParent=" + orderParent + ", whoCreationDate=" + whoCreationDate + ", whoLastUpdateDate="
				+ whoLastUpdateDate + ", whoCreatedBy=" + whoCreatedBy + ", whoLastUpdatedBy=" + whoLastUpdatedBy + "]";
	}
	

}
