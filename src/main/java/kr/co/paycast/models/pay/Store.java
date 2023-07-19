package kr.co.paycast.models.pay;

import java.sql.Timestamp;
import java.util.Calendar;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import kr.co.paycast.models.fnd.Site;
import kr.co.paycast.utils.Util;

import org.codehaus.jackson.annotate.JsonIgnore;


@Entity
@Table(name="PAY_STORES", uniqueConstraints = {
	@javax.persistence.UniqueConstraint(columnNames = {"SITE_ID", "SHORT_NAME"}),
	@javax.persistence.UniqueConstraint(columnNames = {"SITE_ID", "STORE_NAME"})
})
public class Store {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_seq_gen")
	@SequenceGenerator(name = "store_seq_gen", sequenceName = "PAY_STORES_SEQ")
	@Column(name = "STORE_ID")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SITE_ID", nullable = false)
	private Site site;
	
	
	@Column(name = "STORE_NAME", nullable = false, length = 50)
	private String storeName;
	
	@Column(name = "SHORT_NAME", nullable = false, length = 50)
	private String shortName;

	@Column(name = "STORE_KEY", nullable = false, length = 8, unique = true)
	private String storeKey;
	
	@Column(name = "EFFECTIVE_START_DATE", nullable = false)
	private Date effectiveStartDate;
	
	@Column(name = "EFFECTIVE_END_DATE")
	private Date effectiveEndDate;
	
	@Column(name = "MEMO", nullable = true, length = 300)
	private String memo;
	
	@Column(name = "OPEN_TYPE", nullable = false, length = 1)
	private String openType = "O";		// O: 영업중, C: 영업마감
	
	@Column(name = "MO_ALLOWED", nullable = false)
	private boolean mobileOrderAllowed = true;
	
	@Column(name = "KO_ALLOWED", nullable = false)
	private boolean kioskOrderAllowed = true;
	
	@Column(name = "KP_ALLOWED", nullable = false)
	private boolean kitchenPadAllowed = true;
	
	@Column(name = "AT_ALLOWED", nullable = false)
	private boolean alimTalkAllowed = true;
	
	//모바일 주문 가능 여부
	@Column(name = "MO_ENABLED", nullable = false)
	private boolean mobileOrderEnabled = true;
	
	//키오스크 주문 가능 여부
	@Column(name = "KO_ENABLED", nullable = false)
	private boolean kioskOrderEnabled = true;
	
	
	
	@Column(name = "BIZ_NAME", length = 100)
	private String bizName;
	
	@Column(name = "BIZ_REP", length = 100)
	private String bizRep;
	
	@Column(name = "BIZ_NUM", length = 100)
	private String bizNum;
	
	@Column(name = "PHONE", length = 100)
	private String phone;
	
	@Column(name = "LOCAL_CODE", length = 15)
	private String localCode;
	
	@Column(name = "LOCAL_NAME", length = 100)
	private String localName;
	
	@Column(name = "ADDR2", length = 100)
	private String addr2;
	
	@Column(name = "OPEN_HOURS", length = 100)
	private String openHours;
	
	//영업시작 시간
	@Column(name = "START_TIME", length = 100)
	private Timestamp startTime;
	
	//영업종료 시간
	@Column(name = "END_TIME", length = 100)
	private Timestamp endTime;
	
	//24시간 영업 여부
	@Column(name = "OPEN_HOUR_24", nullable = false)
	private boolean openHour24 = true;
	
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

	
	@OneToOne(mappedBy = "store", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private StoreEtc storeEtc;
	
	@OneToOne(mappedBy = "store", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private StoreOpt storeOpt;
	
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Device> devices = new HashSet<Device>(0);
	
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<DeviceTask> deviceTasks = new HashSet<DeviceTask>(0);
	
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<StoreUser> storeUsers = new HashSet<StoreUser>(0);
	
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Content> contents = new HashSet<Content>(0);
	
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<UploadFile> uploadFiles = new HashSet<UploadFile>(0);
	
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Ad> ads = new HashSet<Ad>(0);
	
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Menu> menus = new HashSet<Menu>(0);
	
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<MenuGroup> menuGroups = new HashSet<MenuGroup>(0);
	
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<CouponPolicy> couponPolicys = new HashSet<CouponPolicy>(0);
	
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<StoreDeliveryPay> deliveryPays = new HashSet<StoreDeliveryPay>(0);
	
	
	public Store() {}
	
	public Store(Site site, String storeName, String shortName, String storeKey,
			Date effectiveStartDate, HttpSession session) {
		
		this(site, storeName, shortName, storeKey, effectiveStartDate, null, null, session);
	}
	
	public Store(Site site, String storeName, String shortName, String storeKey,
			Date effectiveStartDate, Date effectiveEndDate,
			String memo,
			HttpSession session) {
				
		this.site = site;
		
		this.storeName = storeName;
		this.shortName = shortName;
		this.storeKey = storeKey;
		this.effectiveStartDate = Util.removeTimeOfDate(effectiveStartDate == null ? new Date() : effectiveStartDate);
		this.effectiveEndDate = Util.setMaxTimeOfDate(effectiveEndDate);
		this.memo = memo;
		
    	Calendar calendar =Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		
		int setYear = calendar.get(Calendar.YEAR);
		int setMonth = calendar.get(Calendar.MONDAY)+1;
		int setDay = calendar.get(Calendar.DATE-1);
		
		this.startTime=Timestamp.valueOf(String.format("%04d-%02d-%02d 00:00:00", setYear, setMonth, setDay));
		this.endTime=Timestamp.valueOf(String.format("%04d-%02d-%02d 23:00:00", setYear, setMonth, setDay));
			
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

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getStoreKey() {
		return storeKey;
	}

	public void setStoreKey(String storeKey) {
		this.storeKey = storeKey;
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public StoreEtc getStoreEtc() {
		return storeEtc;
	}

	public void setStoreEtc(StoreEtc storeEtc) {
		this.storeEtc = storeEtc;
	}

	public StoreOpt getStoreOpt() {
		return storeOpt;
	}

	public void setStoreOpt(StoreOpt storeOpt) {
		this.storeOpt = storeOpt;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getBizRep() {
		return bizRep;
	}

	public void setBizRep(String bizRep) {
		this.bizRep = bizRep;
	}

	public String getBizNum() {
		return bizNum;
	}

	public void setBizNum(String bizNum) {
		this.bizNum = bizNum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getOpenHours() {
		return openHours;
	}

	public void setOpenHours(String openHours) {
		this.openHours = openHours;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public boolean isMobileOrderAllowed() {
		return mobileOrderAllowed;
	}

	public void setMobileOrderAllowed(boolean mobileOrderAllowed) {
		this.mobileOrderAllowed = mobileOrderAllowed;
	}

	public boolean isKioskOrderAllowed() {
		return kioskOrderAllowed;
	}

	public void setKioskOrderAllowed(boolean kioskOrderAllowed) {
		this.kioskOrderAllowed = kioskOrderAllowed;
	}

	public boolean isKitchenPadAllowed() {
		return kitchenPadAllowed;
	}

	public void setKitchenPadAllowed(boolean kitchenPadAllowed) {
		this.kitchenPadAllowed = kitchenPadAllowed;
	}

	public boolean isAlimTalkAllowed() {
		return alimTalkAllowed;
	}

	public void setAlimTalkAllowed(boolean alimTalkAllowed) {
		this.alimTalkAllowed = alimTalkAllowed;
	}

	public boolean isMobileOrderEnabled() {
		return mobileOrderEnabled;
	}

	public void setMobileOrderEnabled(boolean mobileOrderEnabled) {
		this.mobileOrderEnabled = mobileOrderEnabled;
	}

	public boolean isKioskOrderEnabled() {
		return kioskOrderEnabled;
	}

	public void setKioskOrderEnabled(boolean kioskOrderEnabled) {
		this.kioskOrderEnabled = kioskOrderEnabled;
	}

	@JsonIgnore
	public Set<UploadFile> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(Set<UploadFile> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	@JsonIgnore
	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}
	
	@JsonIgnore
	public Set<DeviceTask> getDeviceTasks() {
		return deviceTasks;
	}

	public void setDeviceTasks(Set<DeviceTask> deviceTasks) {
		this.deviceTasks = deviceTasks;
	}

	@JsonIgnore
	public Set<StoreUser> getStoreUsers() {
		return storeUsers;
	}

	public void setStoreUsers(Set<StoreUser> storeUsers) {
		this.storeUsers = storeUsers;
	}
	
	@JsonIgnore
	public Set<Menu> getMenus() {
		return menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

	@JsonIgnore
	public Set<MenuGroup> getMenuGroups() {
		return menuGroups;
	}

	public void setMenuGroups(Set<MenuGroup> menuGroups) {
		this.menuGroups = menuGroups;
	}

	@JsonIgnore
	public Set<Content> getContents() {
		return contents;
	}

	public void setContents(Set<Content> contents) {
		this.contents = contents;
	}

	public String getAddress() {
		
		if (Util.isValid(localName)) {
			return localName.trim() + " " + addr2;
		} else {
			return addr2;
		}
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public boolean isOpenHour_24() {
		return openHour24;
	}

	public void setOpenHour_24(boolean openHour24) {
		this.openHour24 = openHour24;
	}
	
	@JsonIgnore
	public Set<CouponPolicy> getCouponPolicys() {
		return couponPolicys;
	}

	public void setCouponPolicys(Set<CouponPolicy> couponPolicys) {
		this.couponPolicys = couponPolicys;
	}
	
	@JsonIgnore
	public Set<Ad> getAd() {
		return ads;
	}

	public void setAd(Set<Ad> ads) {
		this.ads = ads;
	}
	
}
