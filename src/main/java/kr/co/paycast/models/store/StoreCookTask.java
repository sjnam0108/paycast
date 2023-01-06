/*
 * IRIS(Integrated SW Repository for Information Sharing) version 1.0
 *
 * Copyright ⓒ 2017 kt corp. All rights reserved.
 *
 * This is a proprietary software of kt corp, and you may not use this file except in
 * compliance with license agreement with kt corp. Any redistribution or use of this
 * software, with or without modification shall be strictly prohibited without prior written
 * approval of kt corp, and the copyright notice above does not evidence any actual or
 * intended publication of such software.
 *
 */

package kr.co.paycast.models.store;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import kr.co.paycast.utils.Util;

@Entity
@Table(name="STORE_ORDER_TASKS")
public class StoreCookTask {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_order_tasks_seq_gen")
	@SequenceGenerator(name = "store_order_tasks_seq_gen", sequenceName = "STORE_ORDER_TASKS_SEQ")
	@Column(name = "TASK_ID")
	private int id;
	
	@Column(name = "COMMAND", nullable = false, length = 50)
	private String command;
	
	@Column(name = "STATUS", nullable = false, length = 1)
	private String status;
	
	@Column(name = "DEST_DATE", nullable = false)
	private Date destDate;
	
	@Column(name = "CANCEL_DATE", nullable = false)
	private Date cancelDate;
	
	@Column(name = "SITE_ID")
	private int siteId;
	
	@Column(name = "STORE_ID")
	private int storeId; //매장 번호
	
	@Column(name = "STB_ID")
	private int stbId;
	
	@Column(name = "DEVICE_ID", length = 8)
	private String deviceID;	// FCM 전송되는 기기 ID
	
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
	
	public StoreCookTask() {}
	
	public StoreCookTask(int siteId, int storeId, int stbId, String deviceID, String command, String status,
			Date destDate, Date cancelDate) {
		this.siteId = siteId;
		this.storeId = storeId;
		this.stbId = stbId;
		this.deviceID = deviceID;
		this.command = command;
		this.status = status;
		this.destDate = destDate;
		this.cancelDate = cancelDate;
		this.whoCreatedBy = -1;
		this.whoCreationDate = new Date();
		this.whoLastUpdatedBy = -1;
		this.whoLastUpdateDate = new Date();
		this.whoLastUpdateLogin = -1;
	}
	
	public StoreCookTask(int siteId, int storeId, int stbId, String deviceID, String command, String status,
			Date destDate, Date cancelDate, HttpSession session) {
		this.siteId = siteId;
		this.storeId = storeId;
		this.stbId = stbId;
		this.deviceID = deviceID;
		this.command = command;
		this.status = status;
		this.destDate = destDate;
		this.cancelDate = cancelDate;
		
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

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getStbId() {
		return stbId;
	}

	public void setStbId(int stbId) {
		this.stbId = stbId;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
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
