package kr.co.paycast.models.pay;

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
@Table(name="PAY_DEVICES", uniqueConstraints = {
	@javax.persistence.UniqueConstraint(columnNames = {"STORE_ID", "DEVICE_TYPE", "DEVICE_SEQ"})
})
public class Device {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "device_seq_gen")
	@SequenceGenerator(name = "device_seq_gen", sequenceName = "PAY_DEVICES_SEQ")
	@Column(name = "DEVICE_ID")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;
	
	
	@Column(name = "DEVICE_TYPE", nullable = false, length = 1)
	private String deviceType;		// K: 키오스크, D: 주방용패드, N: 알리미, P: 프린터
	
	@Column(name = "DEVICE_SEQ", nullable = false)
	private Integer deviceSeq = 0;
	
	@Column(name = "FAMILIAR_NAME", length = 50)
	private String familiarName;
	
	@Column(name = "UKID", length = 8, unique = true)
	private String ukid;
	
	@Column(name = "FCM_TOKEN", length = 152)
	private String fcmToken;
	
	@Column(name = "MEMO", length = 300)
	private String memo;
	
	
	@Column(name = "STATUS", nullable = false, length = 1)
	private String status = "0";	// 0: 미확인, 2: 장비꺼짐, 4: 정상 가능성 높음, 6: 정상

	@Column(name = "LAST_REPORTED")
	private Date lastReported;
	
	@Column(name = "TOKEN_SENT")
	private Date tokenSent;

	
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

	
	@OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<TokenActivity> tokenActivities = new HashSet<TokenActivity>(0);
	
	@OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ContentFile> contentFiles = new HashSet<ContentFile>(0);
	
	@OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<DeviceTask> deviceTasks = new HashSet<DeviceTask>(0);

	
	public Device() {}
	
	public Device(Store store, String deviceType, HttpSession session) {
		
		this.store = store;
		this.deviceType = deviceType;
		this.deviceSeq = 1000;
		
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

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Integer getDeviceSeq() {
		return deviceSeq;
	}

	public void setDeviceSeq(Integer deviceSeq) {
		this.deviceSeq = deviceSeq;
	}

	public String getFamiliarName() {
		return familiarName;
	}

	public void setFamiliarName(String familiarName) {
		this.familiarName = familiarName;
	}

	public String getUkid() {
		return ukid;
	}

	public void setUkid(String ukid) {
		this.ukid = ukid;
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public Date getLastReported() {
		return lastReported;
	}

	public void setLastReported(Date lastReported) {
		this.lastReported = lastReported;
	}

	public Date getTokenSent() {
		return tokenSent;
	}

	public void setTokenSent(Date tokenSent) {
		this.tokenSent = tokenSent;
	}

	@JsonIgnore
	public Set<TokenActivity> getTokenActivities() {
		return tokenActivities;
	}

	public void setTokenActivities(Set<TokenActivity> tokenActivities) {
		this.tokenActivities = tokenActivities;
	}

	@JsonIgnore
	public Set<ContentFile> getContentFiles() {
		return contentFiles;
	}

	public void setContentFiles(Set<ContentFile> contentFiles) {
		this.contentFiles = contentFiles;
	}

	@JsonIgnore
	public Set<DeviceTask> getDeviceTasks() {
		return deviceTasks;
	}

	public void setDeviceTasks(Set<DeviceTask> deviceTasks) {
		this.deviceTasks = deviceTasks;
	}
}
