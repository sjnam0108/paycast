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
@Table(name="STORE_ORDER_VERIFICATION")
public class StoreOrderVerification {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_order_verification_seq_gen")
	@SequenceGenerator(name = "store_order_verification_seq_gen", sequenceName = "STORE_ORDER_VERIFICATION_SEQ")
	@Column(name = "STORE_CANCEL_VERIFICATIONID")
	private int id;

	@Column(name = "STORE_ID")
	private int storeId; //매장 번호

	@Column(name = "STORE_ORDER_ID")
	private int storeOrderId; //StoreOrder ID 승인번호 재 요청시 기존에 생성된 승인번호는 삭제를 위해 사용
	
	@Column(name = "STORE_ORDER_NUMBER", nullable = false)
	private String orderNumber; //주문번호
	
	@Column(name = "STORE_CANCEL_ID")
	private int storeCancelId; //
	
	@Column(name = "VERIFICATION_CODE")
	private String verificationCode;
	
	@Column(name = "STATUS", nullable = false, length = 1)
	private String status;// 해당 승인번호가 사용이 가능한 번호 인지 체크 (Y : 사용가능 , N : 사용 불가)
	
	@Column(name = "DEST_DATE", nullable = false)
	private Date destDate;
	
	@Column(name = "CANCEL_DATE", nullable = false)
	private Date cancelDate;
	
	//2019.04.29 취소 패스워드 매장 등록시 저장변경으로 인하여 사용 하지 않음
	@Column(name = "CANCEL_STORE_AUTH")
	private String cancelStoreAuth; // 스마일 페이 취소시에 필요한 패스워드(해당 패스워드는 스마일페이 홈페이지에서 등록한다. )
	
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
	
	public StoreOrderVerification() {}
	
	public StoreOrderVerification(int storeId, int storeOrderId, String orderNumber, String verificationCode, String status, 
			Date destDate, Date cancelDate, HttpSession session) {
		this.storeId = storeId;
		this.verificationCode = verificationCode;
		this.storeOrderId = storeOrderId;
		this.status = status;
		this.orderNumber = orderNumber;
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

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getStoreOrderId() {
		return storeOrderId;
	}

	public void setStoreOrderId(int storeOrderId) {
		this.storeOrderId = storeOrderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getStoreCancelId() {
		return storeCancelId;
	}

	public void setStoreCancelId(int storeCancelId) {
		this.storeCancelId = storeCancelId;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
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

	public String getCancelStoreAuth() {
		return cancelStoreAuth;
	}

	public void setCancelStoreAuth(String cancelStoreAuth) {
		this.cancelStoreAuth = cancelStoreAuth;
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
