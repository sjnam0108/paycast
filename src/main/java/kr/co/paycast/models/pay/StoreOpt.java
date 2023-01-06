package kr.co.paycast.models.pay;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.co.paycast.utils.Util;

@Entity
@Table(name="PAY_STORE_OPTS")
public class StoreOpt {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_opt_seq_gen")
	@SequenceGenerator(name = "store_opt_seq_gen", sequenceName = "PAY_STORE_OPTS_SEQ")
	@Column(name = "STORE_OPT_ID")
	private int id;
	
	@Column(name = "K_LOGO_TYPE", nullable = false, length = 1)
	private String kioskLogoType = "";		// I: 이미지, T: 텍스트
	
	@Column(name = "K_LOGO_TEXT")
	private String kioskLogoText;			// 키오스크 매장 로고 텍스트
	
	@Column(name = "M_LOGO_TYPE", nullable = false, length = 1)
	private String mobileLogoType = "";		// I: 이미지, T: 텍스트
	
	@Column(name = "M_LOGO_TEXT")
	private String mobileLogoText;			// 모바일 매장 로고 텍스트
	
	@Column(name = "MENU_MATRIX", nullable = false, length = 3)
	private String menuMatrix = "3x3";		// 키오스크에서 사용되는 메뉴 Matrix(2x2, 3x3, 3x4, 4x4 등)
	
	@Column(name = "ORDER_TYPE", nullable = false, length = 5)
	private String orderType = "type2";		// 모바일에서 사용되는 TYPE(type1 / type2 / type3)

	
	@Column(name = "K_LOGO_IMG_ID")
	private Integer kioskLogoImageId;

	@Column(name = "M_LOGO_IMG_ID")
	private Integer mobileLogoImageId;

	
	@Transient
	private UploadFile kioskLogoImage;
	
	@Transient
	private UploadFile mobileLogoImage;

	
	@Column(name = "STORE_OPER_END_DATE")
	private Date operEndDate;

	@Column(name = "STORE_ORDERSEQ")
	private int storeOrderSeq; 				//모바일에서 결제되는 seq를 확인하기 위해 사용 하는
	
	@Column(name = "M_ORDER_RSVPTIME")
	private String rsvpTime = "";			// 모바일에서 사용되는 예약시간을 "," 형식으로 넣어둔 값 예)5,10,30,60,110
	
	// OPEN_TIME / CLOSE_TIME은 자동으로 영업 시작과 영업 마감을 진행 하기위해서 매번 날짜가 변경 되며
	// NEXT_DAY_CLOSE 다음날 영업 종료일 경우 해당 내용이 true 값이 된다.
	@Column(name = "OPEN_TIME", nullable = false)
	private Date openTime;					//영업시작 시간

	@Column(name = "CLOSE_TIME", nullable = false)
	private Date closeTime;					//영업종료 시간
	
	@Column(name = "NEXT_DAY_CLOSE")
	private boolean nextDayClose = false;	//다음날까지 영업종료할때 체크
	
	// AUTO 일 경우 시스템이 open close  
	// MANUAL 일 경우 고객이 직접 open cloase  
	@Column(name = "CUSTOM_OPERATION")
	private boolean customOperation = false;	//AUTO : false / MANUAL : true
	
	@Column(name = "M_IMMEDIATE")
	private boolean immediate = false;	//즉시 취소 ON / OFF
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private int whoLastUpdatedBy;
	
	@Column(name = "LAST_UPDATE_LOGIN", nullable = false)
	private int whoLastUpdateLogin;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false, unique = true)
	private Store store;

	
	public StoreOpt() {}
	
	public StoreOpt(Store store) {
		this(store, null);
	}
	
	public StoreOpt(Store store, HttpSession session) {
		this.store = store;
		
		touchWhoC(session);
	}
	
	private void touchWhoC(HttpSession session) {
		this.openTime = new Date();
		this.closeTime = new Date();
		
		this.whoCreatedBy = Util.loginUserId(session);
		this.whoCreationDate = new Date();
		touchWho(session);
	}
	
	public void touchWho(HttpSession session) {
		this.whoLastUpdatedBy = Util.loginUserId(session);
		this.whoLastUpdateDate = new Date();
		this.whoLastUpdateLogin = Util.loginId(session);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKioskLogoType() {
		return kioskLogoType;
	}

	public void setKioskLogoType(String kioskLogoType) {
		this.kioskLogoType = kioskLogoType;
	}

	public String getKioskLogoText() {
		return kioskLogoText;
	}

	public void setKioskLogoText(String kioskLogoText) {
		this.kioskLogoText = kioskLogoText;
	}

	public String getMobileLogoType() {
		return mobileLogoType;
	}

	public void setMobileLogoType(String mobileLogoType) {
		this.mobileLogoType = mobileLogoType;
	}

	public String getMobileLogoText() {
		return mobileLogoText;
	}

	public void setMobileLogoText(String mobileLogoText) {
		this.mobileLogoText = mobileLogoText;
	}

	public String getMenuMatrix() {
		return menuMatrix;
	}

	public void setMenuMatrix(String menuMatrix) {
		this.menuMatrix = menuMatrix;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Date getOperEndDate() {
		return operEndDate;
	}

	public void setOperEndDate(Date operEndDate) {
		this.operEndDate = operEndDate;
	}

	public int getStoreOrderSeq() {
		return storeOrderSeq;
	}

	public void setStoreOrderSeq(int storeOrderSeq) {
		this.storeOrderSeq = storeOrderSeq;
	}

	public String getRsvpTime() {
		return rsvpTime;
	}

	public void setRsvpTime(String rsvpTime) {
		this.rsvpTime = rsvpTime;
	}

	public Integer getKioskLogoImageId() {
		return kioskLogoImageId;
	}

	public void setKioskLogoImageId(Integer kioskLogoImageId) {
		this.kioskLogoImageId = kioskLogoImageId;
	}

	public Integer getMobileLogoImageId() {
		return mobileLogoImageId;
	}

	public void setMobileLogoImageId(Integer mobileLogoImageId) {
		this.mobileLogoImageId = mobileLogoImageId;
	}

	public UploadFile getKioskLogoImage() {
		return kioskLogoImage;
	}

	public void setKioskLogoImage(UploadFile kioskLogoImage) {
		this.kioskLogoImage = kioskLogoImage;
	}

	public UploadFile getMobileLogoImage() {
		return mobileLogoImage;
	}

	public void setMobileLogoImage(UploadFile mobileLogoImage) {
		this.mobileLogoImage = mobileLogoImage;
	}
	
	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public boolean isNextDayClose() {
		return nextDayClose;
	}

	public void setNextDayClose(boolean nextDayClose) {
		this.nextDayClose = nextDayClose;
	}

	public boolean isCustomOperation() {
		return customOperation;
	}

	public void setCustomOperation(boolean customOperation) {
		this.customOperation = customOperation;
	}

	public boolean isImmediate() {
		return immediate;
	}

	public void setImmediate(boolean immediate) {
		this.immediate = immediate;
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

	public int getWhoLastUpdateLogin() {
		return whoLastUpdateLogin;
	}

	public void setWhoLastUpdateLogin(int whoLastUpdateLogin) {
		this.whoLastUpdateLogin = whoLastUpdateLogin;
	}
	
	@JsonIgnore
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
}
