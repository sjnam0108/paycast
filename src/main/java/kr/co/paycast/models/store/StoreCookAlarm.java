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
@Table(name="STORE_ORDER_COOKALARM")
public class StoreCookAlarm {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "storeordercookalarm_seq_gen")
	@SequenceGenerator(name = "storeordercookalarm_seq_gen", sequenceName = "STORE_ORDER_COOKALARM_SEQ")
	@Column(name = "ORDER_COOKALARM_ID")
	private int id;

	@Column(name = "STORE_ID")
	private int storeId; //매장 번호
	
	@Column(name = "STORE_ORDER_ID")
	private int storeOrderId; //주문에 대한 ID (TABLE:STORE_ORDER 의 ID값)

	@Column(name = "STORE_ORDER_NUMBER")
	private String storeOrderNum; //주문메뉴 번호(storeOrderList를 조회 하기 위한 번호)
	
	@Column(name = "STB_ID")
	private int stbId; //알람을 울리는 기기의 ID 값
	
	@Column(name = "DEVICE_ID", length = 8)
	private String deviceID;	// FCM 전송되는 기기 ID
	
	@Column(name = "ORDER_DID_COMPLETE", length = 2)
	private String orderDIDComplete; //전송 성공여부  ( Y:DID가 정상적으로 가져감 , N:DID에서 전송완료가 아직 안됨 )

	@Column(name = "ORDER_ALARM_GUBUN", length = 2)
	private String orderAlarmGubun; //메뉴의 대기 상태의 알람인지,완료 상태의 알림인지 구분하여  StoreCookTask에 따라서 내려주기 위해서 : S(대기 상태), C(완료 상태)
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private int whoLastUpdatedBy;

	public StoreCookAlarm() {}
	
	public StoreCookAlarm(int storeId, int storeOrderId, String storeOrderNum, int stbId, String deviceID) {
		this.storeId = storeId;
		this.storeOrderId = storeOrderId;
		this.storeOrderNum = storeOrderNum;
		this.stbId = stbId;
		this.deviceID = deviceID;
		this.orderDIDComplete = "N";
		this.whoCreatedBy = -1;
		this.whoCreationDate = new Date();
		this.whoLastUpdatedBy = -1;
		this.whoLastUpdateDate = new Date();
	}
	
	public StoreCookAlarm(int storeId, int storeOrderId, String storeOrderNum, int stbId, String deviceID, HttpSession session) {
		this.storeId = storeId;
		this.storeOrderId = storeOrderId;
		this.storeOrderNum = storeOrderNum;
		this.stbId = stbId;
		this.deviceID = deviceID;
		this.orderDIDComplete = "N";
		
		touchWhoC(session);
	}

	public void touchWhoC(HttpSession session) {
		this.whoCreatedBy = Util.loginUserId(session);
		this.whoCreationDate = new Date();
		touchWho(session);
	}
	
	public void touchWho(HttpSession session) {
		this.whoLastUpdatedBy = Util.loginUserId(session);
		this.whoLastUpdateDate = new Date();
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

	public String getStoreOrderNum() {
		return storeOrderNum;
	}

	public void setStoreOrderNum(String storeOrderNum) {
		this.storeOrderNum = storeOrderNum;
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

	public String getOrderDIDComplete() {
		return orderDIDComplete;
	}

	public void setOrderDIDComplete(String orderDIDComplete) {
		this.orderDIDComplete = orderDIDComplete;
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

	public String getOrderAlarmGubun() {
		return orderAlarmGubun;
	}

	public void setOrderAlarmGubun(String orderAlarmGubun) {
		this.orderAlarmGubun = orderAlarmGubun;
	}

	@Override
	public String toString() {
		return "StoreCookAlarm [id=" + id + ", storeId=" + storeId
				+ ", storeOrderId=" + storeOrderId + ", storeOrderNum="
				+ storeOrderNum + ", stbId=" + stbId + ", deviceID=" + deviceID
				+ ", orderDIDComplete=" + orderDIDComplete
				+ ", whoCreationDate=" + whoCreationDate
				+ ", whoLastUpdateDate=" + whoLastUpdateDate
				+ ", whoCreatedBy=" + whoCreatedBy + ", whoLastUpdatedBy="
				+ whoLastUpdatedBy + "]";
	}

}
