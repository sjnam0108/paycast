package kr.co.paycast.models.pay;

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
@Table(name="PAY_OPTIONAL_LIST")
public class OptionalMenuList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "optional_list_seq_gen")
	@SequenceGenerator(name = "optional_list_seq_gen", sequenceName = "PAY_OPTIONAL_LIST_SEQ")
	@Column(name = "OPT_MENU_LIST_ID")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "OPT_MENU_ID", nullable = false)
	private OptionalMenu optMenu;
	
	@Column(name = "MENU_NAME", nullable = false, length = 100)
	private String name;				// 옵션 메뉴 명
	
	@Column(name = "MENU_CODE", nullable = false, length = 100)
	private String code;				// 옵션 메뉴 명
	
	@Column(name = "IMAGE", nullable = false, length = 100)
	private String image;				// 옵션 메뉴 명
	
	@Column(name = "PRICE", nullable = false)
	private Float price = 0f;			// 옵션 가격
	
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
	
	@Column(name = "SOLD_OUT", nullable = false)
	private boolean soldOut = false;	// 매진
	
	public OptionalMenuList() {}
	
	public OptionalMenuList(OptionalMenu optMenu, String name, Float price, HttpSession session) {
		
		this.optMenu = optMenu;
		this.name = name;
		this.price = price;
		
		touchWhoC(session);
	}
	
	public OptionalMenuList(OptionalMenu optMenu, String name, Float price,String image, String menuCode, HttpSession session) {
		
		this.optMenu = optMenu;
		this.name = name;
		this.price = price;
		this.image = image;
		this.code = menuCode;
		
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
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

	public OptionalMenu getOptMenu() {
		return optMenu;
	}

	public void setOptMenu(OptionalMenu optMenu) {
		this.optMenu = optMenu;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isSoldOut() {
		return soldOut;
	}

	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}
	
	
	
}
