package kr.co.paycast.models.calc;

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
@Table(name="STORE_CALC_MENU")
public class StoreCalcMenu {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "storecalcmenu_seq_gen")
	@SequenceGenerator(name = "storecalcmenu_seq_gen", sequenceName = "STORE_CALC_MENU_SEQ")
	@Column(name = "STORE_CALC_MENU_ID")
	private int id;
	
	@Column(name = "STORE_ID")
	private int storeId; //매장 번호
	
	@Column(name = "ORDER_DAY_CREATION")
	private Date orderCreation; // 주문된 날

	@Column(name = "ORDER_MENU_ID", nullable = false)
	private int orderMenuId; //주문메뉴ID
	
	@Column(name = "ORDER_MENU_NAME", nullable = false)
	private String orderMenuName; //주문메뉴명
	
	@Column(name = "ORDER_MONTHDAY")
	private String orderMonthDay; //년월일  ex)2018-12-02
	
	@Column(name = "ORDER_MENU_AMOUNT", nullable = false)
	private int orderMenuAmount; //주문메뉴수량
	
	@Column(name = "ORDER_MENU_AMT", nullable = false)
	private int orderMenuAmt; //주문메뉴금액
	
	@Column(name = "ORDER_SELECT_ESS")
	private String selectEss; //필수 선택 
	
	@Column(name = "ORDER_SELECT_Add")
	private String selectAdd; //추가 선택

	@Column(name = "ORDER_SELECT_COMP")
	private String compSelect;
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private int whoLastUpdatedBy;

	public StoreCalcMenu() {}
	
	public StoreCalcMenu(int storeId, int calcYear, int calcMonth, 
			HttpSession session) {
		this.storeId = storeId;
		
		touchWhoC(session);
	}
	
	public StoreCalcMenu(int storeId, Date orderCreation, int orderMenuId, String orderMenuName, String orderMonthDay, int orderMenuAmount, int orderMenuAmt,
			String selectEss, String selectAdd, String compSelect, HttpSession session) {
		this.storeId = storeId;
		this.orderCreation = orderCreation;
		this.orderMenuId = orderMenuId;
		this.orderMenuName = orderMenuName;
		this.orderMonthDay = orderMonthDay;
		this.orderMenuAmount = orderMenuAmount;
		this.orderMenuAmt = orderMenuAmt;
		this.selectEss = selectEss;
		this.selectAdd = selectAdd;
		this.compSelect = compSelect;
		
		touchWhoC(session);
	}
	
	public void touchWhoC(HttpSession session) {
		this.whoCreatedBy = Util.loginUserId(session);
		this.whoCreationDate = new Date();
		touchWho(session);
	}
	
	public void touchWho(HttpSession session) {
		this.whoLastUpdatedBy = Util.loginUserId(session);
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

	public Date getOrderCreation() {
		return orderCreation;
	}

	public void setOrderCreation(Date orderCreation) {
		this.orderCreation = orderCreation;
	}

	public int getOrderMenuId() {
		return orderMenuId;
	}

	public void setOrderMenuId(int orderMenuId) {
		this.orderMenuId = orderMenuId;
	}

	public String getOrderMenuName() {
		return orderMenuName;
	}

	public void setOrderMenuName(String orderMenuName) {
		this.orderMenuName = orderMenuName;
	}

	public String getOrderMonthDay() {
		return orderMonthDay;
	}

	public void setOrderMonthDay(String orderMonthDay) {
		this.orderMonthDay = orderMonthDay;
	}

	public int getOrderMenuAmount() {
		return orderMenuAmount;
	}

	public void setOrderMenuAmount(int orderMenuAmount) {
		this.orderMenuAmount = orderMenuAmount;
	}

	public int getOrderMenuAmt() {
		return orderMenuAmt;
	}

	public void setOrderMenuAmt(int orderMenuAmt) {
		this.orderMenuAmt = orderMenuAmt;
	}

	public String getSelectEss() {
		return selectEss;
	}

	public void setSelectEss(String selectEss) {
		this.selectEss = selectEss;
	}

	public String getSelectAdd() {
		return selectAdd;
	}

	public void setSelectAdd(String selectAdd) {
		this.selectAdd = selectAdd;
	}

	public String getCompSelect() {
		return compSelect;
	}

	public void setCompSelect(String compSelect) {
		this.compSelect = compSelect;
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

	public int getWhoCreatedBy() {
		return whoCreatedBy;
	}

	public void setWhoCreatedBy(int whoCreatedBy) {
		this.whoCreatedBy = whoCreatedBy;
	}

	public int getWhoLastUpdatedBy() {
		return whoLastUpdatedBy;
	}

	public void setWhoLastUpdatedBy(int whoLastUpdatedBy) {
		this.whoLastUpdatedBy = whoLastUpdatedBy;
	}
	
}
