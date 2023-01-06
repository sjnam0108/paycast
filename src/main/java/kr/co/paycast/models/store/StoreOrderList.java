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
@Table(name="STORE_ORDER_LIST")
public class StoreOrderList {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "storeorderlist_seq_gen")
	@SequenceGenerator(name = "storeorderlist_seq_gen", sequenceName = "STORE_ORDER_LIST_SEQ")
	@Column(name = "STORE_ORDERLIST_ID")
	private int id;

	@Column(name = "STORE_ORDER_NUMBER", nullable = false)
	private String orderNumber; //주문번호
	
	@Column(name = "ORDER_MENU_ID", nullable = false)
	private int orderMenuId; //주문메뉴ID
	
	@Column(name = "ORDER_MENU_AMOUNT", nullable = false)
	private int orderMenuAmount; //주문메뉴수량
	
	@Column(name = "ORDER_MENU_NAME", nullable = false)
	private String orderMenuName; //주문메뉴명
	
	@Column(name = "ORDER_MENU_AMT", nullable = false)
	private int orderMenuAmt; //주문메뉴금액
	
	@Column(name = "ORDER_MENU_TOTALAMT", nullable = false)
	private int orderMenuTotalAmt; //주문메뉴총금액
	
	@Column(name = "ORDER_MENU_PACKING", length = 2)
	private int orderMenuPacking; //메뉴 포장여부  ( 1:포장일 경우 , 0:포장이 아닐 경우 ) 
	
	@Column(name = "ORDER_MENU_NOTICE", length = 2)
	private String orderMenuNotice; //메뉴 알림 여부 ( Y:알림을 1번초과 했을 경우 , O:처음 알람을 울렸을 경우, N:알림이 울리지 않았을 경우  )
	
	@Column(name = "ORDER_SELECT_ESS", length = 2000)
	private String orderSelectEss; //필수 선택
	
	@Column(name = "ORDER_SELECT_Add", length = 2000)
	private String orderSelectAdd; //추가 선택
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private String whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private String whoLastUpdatedBy;

	public StoreOrderList() {}
	
	public StoreOrderList(String orderNum, int orderProductID, int orderCount,
			String productName, int orderPrice, int orderMenuTotalAmt, int orderMenuPacking) {
		this.orderNumber = orderNum;
		this.orderMenuId = orderProductID;
		this.orderMenuAmount = orderCount;
		this.orderMenuName = productName;
		this.orderMenuAmt = orderPrice;
		this.orderMenuTotalAmt = orderMenuTotalAmt;
		this.orderMenuNotice = "N";
		this.orderMenuPacking = orderMenuPacking;
		
		touchWhoC(orderNum);
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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getOrderMenuId() {
		return orderMenuId;
	}

	public void setOrderMenuId(int orderMenuId) {
		this.orderMenuId = orderMenuId;
	}

	public int getOrderMenuAmount() {
		return orderMenuAmount;
	}

	public void setOrderMenuAmount(int orderMenuAmount) {
		this.orderMenuAmount = orderMenuAmount;
	}

	public String getOrderMenuName() {
		return orderMenuName;
	}

	public void setOrderMenuName(String orderMenuName) {
		this.orderMenuName = orderMenuName;
	}

	public int getOrderMenuAmt() {
		return orderMenuAmt;
	}

	public void setOrderMenuAmt(int orderMenuAmt) {
		this.orderMenuAmt = orderMenuAmt;
	}

	public int getOrderMenuTotalAmt() {
		return orderMenuTotalAmt;
	}

	public void setOrderMenuTotalAmt(int orderMenuTotalAmt) {
		this.orderMenuTotalAmt = orderMenuTotalAmt;
	}

	public int getOrderMenuPacking() {
		return orderMenuPacking;
	}

	public void setOrderMenuPacking(int orderMenuPacking) {
		this.orderMenuPacking = orderMenuPacking;
	}

	public String getOrderMenuNotice() {
		return orderMenuNotice;
	}

	public void setOrderMenuNotice(String orderMenuNotice) {
		this.orderMenuNotice = orderMenuNotice;
	}

	public String getOrderSelectEss() {
		return orderSelectEss;
	}

	public void setOrderSelectEss(String orderSelectEss) {
		this.orderSelectEss = orderSelectEss;
	}

	public String getOrderSelectAdd() {
		return orderSelectAdd;
	}

	public void setOrderSelectAdd(String orderSelectAdd) {
		this.orderSelectAdd = orderSelectAdd;
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
