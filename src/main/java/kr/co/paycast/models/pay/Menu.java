package kr.co.paycast.models.pay;

import java.util.Comparator;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.co.paycast.utils.Util;


@Entity(name="StoreMenu")
@Table(name="PAY_MENUS")
public class Menu {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "menu_seq_gen")
	@SequenceGenerator(name = "menu_seq_gen", sequenceName = "PAY_MENUS_SEQ")
	@Column(name = "MENU_ID")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;
	

	
	@Column(name = "MENU_NAME", nullable = false, length = 100)
	private String name;	
	// 메뉴 명
	@Column(name = "MENU_CODE", length = 100)
	private String code;				// 메뉴 명
	
	@Column(name = "EVENT_NAME")
	private String event;
	
	@Column(name = "EVENT_CODE")
	private String eventCode;
	
	@Column(name = "DISCOUNT", nullable = false)
	private double discount = 0; 			// 포인트 적립 퍼센트
	
	@Column(name = "SIBLING_SEQ", nullable = false)
	private int siblingSeq;				// 메뉴 순서
	
	@Column(name = "PRICE", nullable = false)
	private Float price = 0f;				// 가격
	
	@Column(name = "DISCOUNT_PRICE", nullable = false)
	private Float discountPrice = 0f;				// 할인 가격
	
	@Column(name = "GROUP_ID")
	private Integer groupId;			// 그룹 시퀀스 번호
	
	@Column(name = "IMAGE_ID")
	private Integer menuImageId;		// 메뉴 이미지 시퀀스 번호

	@Column(name = "FLAG_TYPE", nullable = false, length = 1)
	private String flagType = "";		// N: 신메뉴, R: 추천메뉴, I: 리필가능메뉴 
	// 2019.11.12 개발할 경우 제한에 대한 내용이 없으므로 무제한 아니면 none으로 처리
	// 리필 : infinity(무제한) / limit(제한) / none(아님)
	
	@Column(name = "INTRO", length = 300)
	private String intro; 				// 메뉴 설명
	
	@Column(name = "IMAGE", length = 300)
	private String image; 				// 메뉴 설명
	
	@Column(name = "SOLD_OUT", nullable = false)
	private boolean soldOut = false;	// 매진
	
	@Column(name = "PUBLISHED", nullable = false, length = 1)
	private String published = "Y";
	
	@Column(name = "UPDATEYN", nullable = false, length = 1)
	private Date updateYN = new Date();
	// 2019.11.21 DB 에 저장하지는 않지만 POS에 내려줄때 삭제된 메뉴에 대해서 "D : 삭제"로 하여  셋팅(published)
	
	@Transient
	private UploadFile menuImage;
	
	
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

	
	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<OptionalMenu> optionalMenus = new HashSet<OptionalMenu>(0);

	
	public Menu() {}
	
	public Menu(Store store, String name, HttpSession session) {
		
		this(store, name, 1000, session);
	}
	
	public Menu(Store store, String name, int siblingSeq, HttpSession session) {
		
		this.store = store;
		this.name = name;
		this.siblingSeq = siblingSeq;
		
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
		this.whoLastUpdateLogin = Util.loginId(session);
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	public String getEvent() {
		return event;
	}
	
	public void setEvent(String event) {
		this.event = event;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSiblingSeq() {
		return siblingSeq;
	}

	public void setSiblingSeq(int siblingSeq) {
		this.siblingSeq = siblingSeq;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		
		this.price = price;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getMenuImageId() {
		return menuImageId;
	}

	public void setMenuImageId(Integer menuImageId) {
		this.menuImageId = menuImageId;
	}

	public String getFlagType() {
		return flagType;
	}

	public void setFlagType(String flagType) {
		this.flagType = flagType;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public boolean isSoldOut() {
		return soldOut;
	}

	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}

	public UploadFile getMenuImage() {
		return menuImage;
	}

	public void setMenuImage(UploadFile menuImage) {
		this.menuImage = menuImage;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonIgnore
	public Set<OptionalMenu> getOptionalMenus() {
		return optionalMenus;
	}

	public void setOptionalMenus(Set<OptionalMenu> optionalMenus) {
		this.optionalMenus = optionalMenus;
	}
	
	public static Comparator<Menu> SiblingSeqComparator = new Comparator<Menu>() {
		
    	public int compare(Menu item1, Menu item2) {
    		return Integer.compare(item1.getSiblingSeq(), item2.getSiblingSeq());
    	}
	};


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getUpdateYN() {
		return updateYN;
	}

	public void setUpdateYN(Date updateYN) {
		this.updateYN = updateYN;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public Float getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Float discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	
	
	
	
}
