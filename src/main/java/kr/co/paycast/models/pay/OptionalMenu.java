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
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.co.paycast.utils.Util;


@Entity
@Table(name="PAY_OPTIONAL_MENUS")
public class OptionalMenu {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "optional_menu_seq_gen")
	@SequenceGenerator(name = "optional_menu_seq_gen", sequenceName = "PAY_OPTIONAL_MENUS_SEQ")
	@Column(name = "OPT_MENU_ID")
	private int id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MENU_ID", nullable = false)
	private Menu menu;
	
	@Column(name = "MENU_NAME", nullable = false, length = 100)
	private String name;				// 옵션 메뉴 명
	
	@Column(name = "SIBLING_SEQ", nullable = false)
	private int siblingSeq;				// 메뉴 순서

	@Column(name = "OPT_TYPE", nullable = false, length = 1)
	private String optType = "";		// M: 필수 메뉴, O: 추가 메뉴

	@Column(name = "MENU_LIST", length = 4000)
	private String menuList;
	
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
	
	@OneToMany(mappedBy = "optMenu", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<OptionalMenuList> OptionalMenuLists = new HashSet<OptionalMenuList>(0);

	
	public OptionalMenu() {}
	
	public OptionalMenu(Menu menu, String optType, String menuName, int siblingSeq, HttpSession session) {
		
		this.menu = menu;
		this.optType = optType;
		this.name = menuName;
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

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
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

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public String getMenuList() {
		return menuList;
	}

	public void setMenuList(String menuList) {
		this.menuList = menuList;
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
	public Set<OptionalMenuList> getOptionalMenuLists() {
		return OptionalMenuLists;
	}

	public void setOptionalMenus(Set<OptionalMenuList> OptionalMenuLists) {
		this.OptionalMenuLists = OptionalMenuLists;
	}

	public static Comparator<OptionalMenu> SiblingSeqComparator = new Comparator<OptionalMenu>() {
		
    	public int compare(OptionalMenu item1, OptionalMenu item2) {
    		return Integer.compare(item1.getSiblingSeq(), item2.getSiblingSeq());
    	}
	};

	
	
}
