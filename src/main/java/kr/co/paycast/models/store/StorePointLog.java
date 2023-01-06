package kr.co.paycast.models.store;

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

import kr.co.paycast.models.pay.Store;
import kr.co.paycast.utils.Util;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="STORE_POINT_LOG")
public class StorePointLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "point_log_seq_gen")
	@SequenceGenerator(name = "point_log_seq_gen", sequenceName = "STORE_POINT_LOG_SEQ")
	@Column(name = "ORDER_POINTLOG_ID")
	private int id;
	
	@Column(name = "TEL", nullable = false, length = 20)
	private String tel = "";		//전화 번호 - 스탬프 및 쿠폰을 조회 하는 전체 값

	@Column(name = "POINT_COUNT", nullable = false)
	private int pointCnt = 0; 		// 적립 포인트 / 사용 포인트
	
	@Column(name = "POINT_TOTAL", nullable = false)
	private int pointTotal = 0; 	// 총 적립 포인트  
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private int whoLastUpdatedBy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;
	
	public StorePointLog() {}
	
	public StorePointLog(String tel, int pointCnt, int pointTotal, Store store) {
		
		this(tel, pointCnt, pointTotal, store, null);
	}
	
	public StorePointLog(String tel, int pointCnt, int pointTotal, Store store, HttpSession session) {
		this.tel = tel;
		this.pointCnt = pointCnt;
		this.pointTotal = pointTotal;
		this.store = store;
		
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
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getPointCnt() {
		return pointCnt;
	}

	public void setPointCnt(int pointCnt) {
		this.pointCnt = pointCnt;
	}

	public int getPointTotal() {
		return pointTotal;
	}

	public void setPointTotal(int pointTotal) {
		this.pointTotal = pointTotal;
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

	@JsonIgnore
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
}
