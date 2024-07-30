package kr.co.paycast.models.pay;

import java.util.Comparator;
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

import kr.co.paycast.utils.Util;


@Entity
@Table(name="PAY_MENU_GROUPS")
public class MenuGroup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "menu_group_seq_gen")
	@SequenceGenerator(name = "menu_group_seq_gen", sequenceName = "PAY_MENU_GROUPS_SEQ")
	@Column(name = "MENU_GROUP_ID")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;
	
	@Column(name = "GROUP_NAME", nullable = false, length = 100)
	private String name;				// 메뉴 그룹 명
	
	@Column(name = "SIBLING_SEQ", nullable = false)
	private int siblingSeq;				// 메뉴 그룹 순서
	
	@Column(name = "PUBLISHED", nullable = false, length = 1)
	private String published = "Y";
	// 2019.11.21 DB 에 저장하지는 않지만 POS에 내려줄때 삭제된 메뉴에 대해서 "D : 삭제"로 하여  셋팅(published)
	
	
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

	
	public MenuGroup() {}
	
	public MenuGroup(Store store, String name, HttpSession session) {
		
		this(store, name, 1000, session);
	}
	
	public MenuGroup(Store store, String name, int siblingSeq, HttpSession session) {
		
		this.store = store;
		this.name = name;
		this.siblingSeq = siblingSeq;
		
		touchWhoC(session);
	}
	
	public MenuGroup(Store store, String name, String published, HttpSession session) {
		
		this.store = store;
		this.name = name;
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

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
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

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}
	
	public static Comparator<MenuGroup> SiblingSeqComparator = new Comparator<MenuGroup>() {
		
    	public int compare(MenuGroup item1, MenuGroup item2) {
    		return Integer.compare(item1.getSiblingSeq(), item2.getSiblingSeq());
    	}
	};
}
