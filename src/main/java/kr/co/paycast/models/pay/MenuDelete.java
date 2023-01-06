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


@Entity
@Table(name="PAY_MENUS_DELETE")
public class MenuDelete {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "menu_seq_delete_gen")
	@SequenceGenerator(name = "menu_seq_delete_gen", sequenceName = "PAY_MENUS_DELETE_SEQ")
	@Column(name = "MENU_DELETE_ID")
	private int id;
	
	@Column(name = "MENU_ID")
	private int menuId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;
	
	@Column(name = "MENU_NAME", nullable = false, length = 100)
	private String name;				// 메뉴 명
	
	@Column(name = "SIBLING_SEQ", nullable = false)
	private int siblingSeq;				// 메뉴 순서
	
	@Column(name = "PRICE", nullable = false)
	private Float price = 0f;				// 가격
	
	@Column(name = "GROUP_ID")
	private Integer groupId;			// 그룹 시퀀스 번호
	
	@Column(name = "IMAGE_ID")
	private Integer menuImageId;		// 메뉴 이미지 시퀀스 번호

	@Column(name = "FLAG_TYPE", nullable = false, length = 1)
	private String flagType = "";		// N: 신메뉴, R: 추천메뉴, I: 리필가능메뉴
	// 2019.11.12 개발할 경우 제한에 대한 내요이 없으므로 무제한 아니면 none으로 처리
	// 리필 : infinity(무제한) / limit(제한) / none(아님)
	
	@Column(name = "INTRO", length = 300)
	private String intro; 				// 메뉴 설명
	
	@Column(name = "SOLD_OUT", nullable = false)
	private boolean soldOut = false;	// 매진
	
	@Column(name = "PUBLISHED", nullable = false, length = 1)
	private String published = "Y";
	
	
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

	
	@OneToMany(mappedBy = "menuDelete", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<OptionalMenuDelete> optionalMenuDeletes = new HashSet<OptionalMenuDelete>(0);

	
	public MenuDelete() {}
	
	public MenuDelete(int menuId, Store store, String name, int siblingSeq, Float price, Integer menuImageId, String flagType, 
			String intro, boolean soldOut, String published, HttpSession session) {
		
		this.menuId = menuId;
		this.store = store;
		this.name = name;
		this.siblingSeq = siblingSeq;
		this.price = price;
		this.menuImageId = menuImageId;
		this.flagType = flagType;
		this.intro = intro;
		this.soldOut = soldOut;
		this.published = published;
		
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

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
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

	@JsonIgnore
	public Set<OptionalMenuDelete> getOptionalMenuDeletes() {
		return optionalMenuDeletes;
	}

	public void setOptionalMenuDeletes(Set<OptionalMenuDelete> optionalMenuDeletes) {
		this.optionalMenuDeletes = optionalMenuDeletes;
	}
	
	public static Comparator<MenuDelete> SiblingSeqComparator = new Comparator<MenuDelete>() {
		
    	public int compare(MenuDelete item1, MenuDelete item2) {
    		return Integer.compare(item1.getSiblingSeq(), item2.getSiblingSeq());
    	}
	};
}
