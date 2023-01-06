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
import javax.servlet.http.HttpSession;

import kr.co.paycast.models.pay.Store;
import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.utils.Util;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="STORE_ORDER_COUPON")
public class StoreOrderCoupon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "order_coupon_seq_gen")
	@SequenceGenerator(name = "order_coupon_seq_gen", sequenceName = "STORE_ORDER_COUPON_SEQ")
	@Column(name = "ORDER_COUPON_ID")
	private int id;
	
	@Column(name = "TEL", nullable = false, length = 20)
	private String tel = "";		//전화 번호 - 스탬프 및 쿠폰을 조회 하는 전체 값
	
	@Column(name = "TYPE", nullable = false, length = 1)
	private String type = "S";		// S: 스탬프, C: 쿠폰

	@Column(name = "STAMP_COUNT", nullable = false)
	private int stampCnt = 0; 		// 적립 스탬프  / 쿠폰일 경우 사용된 스탬프 수
	
	@Column(name = "STAMP_TOTAL", nullable = false)
	private int stampTotal = 0; 	// 적립 스탬프  총 갯수 (적립 스탬프 포함)
	
	@Column(name = "USE_STATE", nullable = false, length = 1)
	private int useState = 0; 		// 쿠폰 등록 / 사용여부  (0 : 쿠폰 등록  / 1 : 쿠폰 사용 완료  )
	
	@Column(name = "END_DATE")
	private Date endDate;			// 쿠폰 사용 마지막 날
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private int whoLastUpdatedBy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_COUPON_ID")
	private StoreCoupon coupon;
	
	public StoreOrderCoupon() {}
	
	public StoreOrderCoupon(String tel, int stampCnt, int stampTotal, Store store, HttpSession session) {
		// 스탬프 저장시 사용
		this.tel = tel;
		this.type = "S";
		this.stampCnt = stampCnt;
		this.stampTotal = stampTotal;
		this.useState = 0;
		this.endDate = new Date();		
		this.store = store;
		this.coupon = null;
		
		touchWhoC(session);
	}
	
	public StoreOrderCoupon(String tel, int stampCnt, Date endDate, Store store, StoreCoupon coupon, HttpSession session) {
		// 쿠폰 저장시 사용
		this.tel = tel;
		this.type = "C";
		this.stampCnt = stampCnt;
		this.stampTotal = 0;
		this.useState = 0;
		this.endDate = endDate;		
		this.store = store;
		this.coupon = coupon;
		
		touchWhoC(session);
	}
	
	private void touchWhoC(HttpSession session) {
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
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStampCnt() {
		return stampCnt;
	}

	public void setStampCnt(int stampCnt) {
		this.stampCnt = stampCnt;
	}

	public int getStampTotal() {
		return stampTotal;
	}

	public void setStampTotal(int stampTotal) {
		this.stampTotal = stampTotal;
	}

	public int getUseState() {
		return useState;
	}

	public void setUseState(int useState) {
		this.useState = useState;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	@JsonIgnore
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@JsonIgnore
	public StoreCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(StoreCoupon coupon) {
		this.coupon = coupon;
	}
}
