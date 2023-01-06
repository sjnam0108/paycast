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
@Table(name="STORE_DELIVERY")
public class StoreDelivery {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_delivery_seq_gen")
	@SequenceGenerator(name = "store_delivery_seq_gen", sequenceName = "STORE_DELIVERY_SEQ")
	@Column(name = "STORE_DELIVERY_ID")
	private int id;
	
	@Column(name = "STORE_ID")
	private int storeId; //매장 번호
	
	@Column(name = "STORE_ORDER_NUMBER")
	private String orderNumber; //주문번호
	
	@Column(name = "MESSAGE")
	private String storeMsg; //매장 요청 메시지
	
	@Column(name = "DELIVERY_MESSAGE")
	private String deliMsg; //배달 요청 메시지
	
	@Column(name = "ADDR1", length = 200)
	private String roadAddr;
	
	@Column(name = "ADDR2", length = 200)
	private String addrDetail;
	
	@Column(name = "BOOKING_TIME", length = 50)
	private String bookingTime;

	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private int whoLastUpdatedBy;

	public StoreDelivery() {}
	
	public StoreDelivery(int storeId, String orderNumber, String storeMsg,
			String deliMsg, String roadAddr, String addrDetail, String bookingTime, HttpSession session) {
		this.storeId = storeId;
		this.orderNumber = orderNumber;
		this.storeMsg = storeMsg;
		this.deliMsg = deliMsg;
		this.roadAddr = roadAddr;
		this.addrDetail = addrDetail;
		this.bookingTime = bookingTime;
		
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
	
	public void touchWho(int userId) {
		this.whoLastUpdatedBy = userId;
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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getStoreMsg() {
		return storeMsg;
	}

	public void setStoreMsg(String storeMsg) {
		this.storeMsg = storeMsg;
	}

	public String getDeliMsg() {
		return deliMsg;
	}

	public void setDeliMsg(String deliMsg) {
		this.deliMsg = deliMsg;
	}

	public String getRoadAddr() {
		return roadAddr;
	}

	public void setRoadAddr(String roadAddr) {
		this.roadAddr = roadAddr;
	}

	public String getAddrDetail() {
		return addrDetail;
	}

	public void setAddrDetail(String addrDetail) {
		this.addrDetail = addrDetail;
	}

	public String getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
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

}
