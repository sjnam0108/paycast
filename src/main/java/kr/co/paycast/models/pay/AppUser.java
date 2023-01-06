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

import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.models.fnd.User;
import kr.co.paycast.utils.Util;


@Entity
@Table(name="PAY_APP_USERS", uniqueConstraints = 
	@javax.persistence.UniqueConstraint(columnNames = {"SITE_ID", "USER_ID", "DEVICE_NAME"}))
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "app_user_seq_gen")
	@SequenceGenerator(name = "app_user_seq_gen", sequenceName = "PAY_APP_USERS_SEQ")
	@Column(name = "APP_USER_ID")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SITE_ID", nullable = false)
	private Site site;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;
	

	@Column(name = "DEVICE_NAME", nullable = false, length = 100)
	private String deviceName;
	
	@Column(name = "FCM_TOKEN", length = 152)
	private String fcmToken;
	
	@Column(name = "OS_TYPE", length = 1)
	private String osType;
	
	@Column(name = "OS_VER", length = 10)
	private String osVer;
	
	@Column(name = "LANG", nullable = false, length = 2)
	private String lang;
	
	@Column(name = "STAUS", nullable = false, length = 1)
	private String status = "R";
	
	
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

	
	public AppUser() {}
	
	public AppUser(Site site, User user, String deviceName, String fcmToken, String osType, String osVer, HttpSession session) {
		this.site = site;
		this.user = user;
		
		this.deviceName = deviceName;
		this.fcmToken = fcmToken;
		this.osType = osType;
		this.osVer = osVer;
		
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

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getOsVer() {
		return osVer;
	}

	public void setOsVer(String osVer) {
		this.osVer = osVer;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	
	public String getOsVerDisp() {

		if (Util.isValid(osType)) {
			if (osType.equals("A")) {
				if (osVer.equals("19")) { return "v19 KitKat"; }
				else if (osVer.equals("20")) { return "v20 Kitkat"; }
				else if (osVer.equals("21")) { return "v21 Lollipop"; }
				else if (osVer.equals("22")) { return "v22 Lollipop"; }
				else if (osVer.equals("23")) { return "v23 Marshmallow"; }
				else if (osVer.equals("24")) { return "v24 Nougat"; }
				else if (osVer.equals("25")) { return "v25 Nougat"; }
				else if (osVer.equals("26")) { return "v26 Oreo"; }
				else if (osVer.equals("27")) { return "v27 Oreo"; }
				else if (osVer.equals("28")) { return "v28 Pie"; }
			}
		}
		
		return "v" + osVer;
	}
	
	public String getOsIconDisp() {
		
		if (Util.isValid(osType)) {
			if (osType.equals("A")) {
				return "fab fa-android text-green";
			} else if (osType.equals("I")) {
				return "fab fa-apple text-dark";
			}
		}
		
		return "";
	}
	
	public String getOsTypeDisp() {
		
		if (Util.isValid(osType)) {
			if (osType.equals("A")) {
				return "Android";
			} else if (osType.equals("I")) {
				return "iOS";
			}
		}
		
		return "";
	}
}
