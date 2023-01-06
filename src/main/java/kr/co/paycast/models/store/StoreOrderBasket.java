package kr.co.paycast.models.store;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="STORE_BASKET")
public class StoreOrderBasket {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_basket_seq_gen")
	@SequenceGenerator(name = "store_basket_seq_gen", sequenceName = "STORE_BASKET_SEQ")
	@Column(name = "STORE_BASKET_ID")
	private int id;

	@Column(name = "STORE_ID")
	private int storeId; //상점 ID
	
	@Column(name = "STORE_BASKET_KEY", length = 8, nullable = false)
	private String basketKey; //장바구니 고유 번호 (RAMDOM 8 자리 - /menu 페이지 진입시 생성)
	
	@Column(name = "STORE_BASKET_TOTAL", nullable = false)
	private int basketTotal = 0; //메뉴 주문 총 수량
	
	@Column(name = "STORE_BASKET_AMT", nullable = false)
	private int basketAmt = 0; //메뉴 주문 총 금액
	
	@Column(name = "STORE_BASKET_DISCOUNT", nullable = false)
	private int basketDiscount = 0; //할인 금액 , 쿠폰 사용금액 및 포인트 사용 에 대한 할인 
	
	@Column(name = "STORE_BASKET_PAYMENT", nullable = false)
	private int basketPayment = 0; //실 결제 금액 (할인금액 제외)
	
	@Column(name = "STORE_ORDER_COUPONID")
	private int couponId = 0; //쿠폰 사용 매장일 경우 사용된 쿠폰 ID
	
	@Column(name = "STORE_SAVINGTYPE")
	private String savingType = ""; //쿠폰, 포인트에 대한 저장 type /CP= 쿠폰, PO= 포인트 (NO는 저장하지 않음)
	
	@Column(name = "STORE_ORDER_TABLE")
	private String orderTable; //매장에서 사용되는 테이블 번호를 넣어준다.
	
	@Column(name = "STORE_ORDER_INTIME")
	private String orderIntime; //진입할때 생성된 시간
	
	@Column(name = "STORE_ORDER_NUMBER")
	private String orderNumber; //주문번호
	
	@Column(name = "STORE_REFILL_TEL")
	private String refiTel; //리필시 사용 되는 전화 번호
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private String whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private String whoLastUpdatedBy;
	
	@OneToMany(mappedBy = "basket", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<StoreOrderBasketList> basketList = new HashSet<StoreOrderBasketList>(0);
	
	public StoreOrderBasket() {}
	
	public StoreOrderBasket(int storeId, String basketKey, int basketTotal, 
			int basketAmt, String orderTable, String orderIntime) {
		this.storeId = storeId;
		this.basketKey = basketKey;
		this.basketTotal = basketTotal;
		this.basketAmt = basketAmt;
		this.orderTable = orderTable;
		this.orderIntime = orderIntime;
		this.orderNumber = "";
		
		touchWhoC(basketKey);
	}

	private void touchWhoC(String basketKey) {
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

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getBasketKey() {
		return basketKey;
	}

	public void setBasketKey(String basketKey) {
		this.basketKey = basketKey;
	}

	public int getBasketTotal() {
		return basketTotal;
	}

	public void setBasketTotal(int basketTotal) {
		this.basketTotal = basketTotal;
	}

	public int getBasketAmt() {
		return basketAmt;
	}

	public void setBasketAmt(int basketAmt) {
		this.basketAmt = basketAmt;
	}

	public int getBasketDiscount() {
		return basketDiscount;
	}

	public void setBasketDiscount(int basketDiscount) {
		this.basketDiscount = basketDiscount;
	}

	public int getBasketPayment() {
		return basketPayment;
	}

	public void setBasketPayment(int basketPayment) {
		this.basketPayment = basketPayment;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public String getSavingType() {
		return savingType;
	}

	public void setSavingType(String savingType) {
		this.savingType = savingType;
	}

	public String getOrderTable() {
		return orderTable;
	}

	public void setOrderTable(String orderTable) {
		this.orderTable = orderTable;
	}

	public String getOrderIntime() {
		return orderIntime;
	}

	public void setOrderIntime(String orderIntime) {
		this.orderIntime = orderIntime;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getRefiTel() {
		return refiTel;
	}

	public void setRefiTel(String refiTel) {
		this.refiTel = refiTel;
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

	public Set<StoreOrderBasketList> getBasketList() {
		return basketList;
	}

	public void setBasketList(Set<StoreOrderBasketList> basketList) {
		this.basketList = basketList;
	}
}
