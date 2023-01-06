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
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

import kr.co.paycast.utils.Util;

@Entity
@Table(name="PAY_DEVICE_TASKS")
public class DeviceTask {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "device_task_seq_gen")
	@SequenceGenerator(name = "device_task_seq_gen", sequenceName = "PAY_DEVICE_TASKS_SEQ")
	@Column(name = "DEVICE_TASK_ID")
	private int id;
	
	@Column(name = "COMMAND", nullable = false, length = 50)
	private String command;
	
	@Column(name = "PARAMS", nullable = true, length = 2000)
	private String params = "";
	
	@Column(name = "STATUS", nullable = false, length = 1)
	private String status = "R";		// R: 등록, W: 통지, S: 성공, P: 성공(수락), F: 실패, C: 자동 취소
	
	@Column(name = "DEST_DATE", nullable = false)
	private Date destDate;
	
	@Column(name = "CANCEL_DATE", nullable = false)
	private Date cancelDate;

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DEVICE_ID", nullable = false)
	private Device device;
	
	
	@Transient
	private String univCommand;
	
	@Transient
	private String statusTip;
	
	
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
	
	
	public DeviceTask() {}
	
	public DeviceTask(Store store, Device device, String command, HttpSession session) {
		
		this.store = store;
		this.device = device;
		this.command = command;
		
		this.destDate = new Date();
		this.cancelDate = Util.setMaxTimeOfDate(new Date());

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

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDestDate() {
		return destDate;
	}

	public void setDestDate(Date destDate) {
		this.destDate = destDate;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getUnivCommand() {
		return univCommand;
	}

	public void setUnivCommand(String univCommand) {
		this.univCommand = univCommand;
	}

	public String getStatusTip() {
		return statusTip;
	}

	public void setStatusTip(String statusTip) {
		this.statusTip = statusTip;
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
	
}
