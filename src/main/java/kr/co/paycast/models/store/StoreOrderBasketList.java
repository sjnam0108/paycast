package kr.co.paycast.models.store;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="STORE_BASKET_LIST")
public class StoreOrderBasketList {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_basket_list_seq_gen")
	@SequenceGenerator(name = "store_basket_list_seq_gen", sequenceName = "STORE_BASKET_LIST_SEQ")
	@Column(name = "STORE_BASKETLIST_ID")
	private int id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_BASKET_ID", nullable = false)
	private StoreOrderBasket basket;
	
	@Column(name = "BASKET_MENU_ID", nullable = false)
	private int menuId = 0; //주문메뉴ID
	
	@Column(name = "BASKET_MENU_AMOUNT", nullable = false)
	private int menuAmount = 0; //주문메뉴수량
	
	@Column(name = "COMP_SELECT", nullable = false)
	private String compSelect; //화면 및 옵션에 대한 select 정보모음
	
	@Column(name = "BASKET_MENU_NAME", nullable = false)
	private String menuName; //주문메뉴명
	
	@Column(name = "BASKET_MENU_AMT", nullable = false)
	private int menuAmt = 0; //주문메뉴금액
	
	@Column(name = "BASKET_MENU_TOTALAMT", nullable = false)
	private int menuTotalAmt = 0; //주문메뉴총금액
	
	@Column(name = "BASKET_MENU_SEQ", length = 3, nullable = true)
	private String basketSeq = "000"; //메뉴 주문 순서
	
	@Column(name = "BASKET_MENU_PACKING", length = 2)
	private String packing = "0"; //메뉴 포장여부  ( 1:포장일 경우 , 0:포장이 아닐 경우 ) 
	
	@Column(name = "BASKET_ESS_VALUE")
	private String essVal; //필수 선택 VALUE
	
	@Column(name = "BASKET_ESS_NAME")
	private String essName; //필수 선택 NAME
	
	@Column(name = "BASKET_Add_VALUE")
	private String addVal; //추가 선택 VALUE
	
	@Column(name = "BASKET_Add_NAME")
	private String addName; //추가 선택 NAME
	
	@Column(name = "BASKET_SUB_MENU")
	private String submenu;
	
	@Column(name = "BASKET_IMG_SRC")
	private String src;
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private String whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private String whoLastUpdatedBy;

	public StoreOrderBasketList() {}
	
	public StoreOrderBasketList(StoreOrderBasket basket, int basketMenuId, int basketMenuAmount, String basketMenuName, 
			int basketMenuAmt, int basketMenuTotalAmt, String basketSeq, String basketMenuPacking, String basketEssVal,
			String basketEssName, String basketAddVal, String basketAddName, String basketKey, String compSelect){
		this.basket = basket;
		this.menuId = basketMenuId;
		this.menuAmount = basketMenuAmount;
		this.menuName = basketMenuName;
		this.menuAmt = basketMenuAmt;
		this.menuTotalAmt = basketMenuTotalAmt;
		this.basketSeq = basketSeq;
		this.packing = basketMenuPacking;
		this.essVal = basketEssVal;
		this.essName = basketEssName;
		this.addVal = basketAddVal;
		this.addName = basketAddName;
		this.compSelect = compSelect;
		this.submenu = "";
		this.src = "";
		
		touchWhoC(basketKey);
	}
	
	public StoreOrderBasketList(StoreOrderBasket basket, int basketMenuId, int basketMenuAmount, String basketMenuName, 
			int basketMenuAmt, int basketMenuTotalAmt, String basketSeq, String basketMenuPacking, String basketEssVal,
			String basketEssName, String basketAddVal, String basketAddName, String basketKey, String compSelect, String submenu, String src){
		this.basket = basket;
		this.menuId = basketMenuId;
		this.menuAmount = basketMenuAmount;
		this.menuName = basketMenuName;
		this.menuAmt = basketMenuAmt;
		this.menuTotalAmt = basketMenuTotalAmt;
		this.basketSeq = basketSeq;
		this.packing = basketMenuPacking;
		this.essVal = basketEssVal;
		this.essName = basketEssName;
		this.addVal = basketAddVal;
		this.addName = basketAddName;
		this.compSelect = compSelect;
		this.submenu = submenu;
		this.src = src;
		
		touchWhoC(basketKey);
	}

	public void touchWhoC(String basketKey) {
		this.whoCreatedBy = basketKey;
		this.whoCreationDate = new Date();
		touchWho(basketKey);
	}
	
	public void touchWho(String basketKey) {
		this.whoLastUpdatedBy = basketKey;
		this.whoLastUpdateDate = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public StoreOrderBasket getBasket() {
		return basket;
	}

	public void setBasket(StoreOrderBasket basket) {
		this.basket = basket;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getMenuAmount() {
		return menuAmount;
	}

	public void setMenuAmount(int menuAmount) {
		this.menuAmount = menuAmount;
	}

	public String getCompSelect() {
		return compSelect;
	}

	public void setCompSelect(String compSelect) {
		this.compSelect = compSelect;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getMenuAmt() {
		return menuAmt;
	}

	public void setMenuAmt(int menuAmt) {
		this.menuAmt = menuAmt;
	}

	public int getMenuTotalAmt() {
		return menuTotalAmt;
	}

	public void setMenuTotalAmt(int menuTotalAmt) {
		this.menuTotalAmt = menuTotalAmt;
	}

	public String getBasketSeq() {
		return basketSeq;
	}

	public void setBasketSeq(String basketSeq) {
		this.basketSeq = basketSeq;
	}

	public String getPacking() {
		return packing;
	}

	public void setPacking(String packing) {
		this.packing = packing;
	}

	public String getEssVal() {
		return essVal;
	}

	public void setEssVal(String essVal) {
		this.essVal = essVal;
	}

	public String getEssName() {
		return essName;
	}

	public void setEssName(String essName) {
		this.essName = essName;
	}

	public String getAddVal() {
		return addVal;
	}

	public void setAddVal(String addVal) {
		this.addVal = addVal;
	}

	public String getAddName() {
		return addName;
	}

	public void setAddName(String addName) {
		this.addName = addName;
	}

	public String getSubmenu() {
		return submenu;
	}

	public void setSubmenu(String submenu) {
		this.submenu = submenu;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
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
