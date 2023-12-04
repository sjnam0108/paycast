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

import kr.co.paycast.utils.Util;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="PAY_STORE_EVENT")
public class StoreEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_event_seq_gen")
	@SequenceGenerator(name = "store_event_seq_gen", sequenceName = "PAY_STORE_EVENT_SEQ")
	@Column(name = "STORE_EVENT_ID")
	private int id;
	
	@Column(name = "NAME", nullable = false, length = 100)
	private String name = "";		// 쿠폰명
	
	@Column(name = "EFFECTIVE_START_DATE", nullable = false)
	private Date effectiveStartDate;
	
	@Column(name = "EFFECTIVE_END_DATE")
	private Date effectiveEndDate;
	
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
	
	public StoreEvent() {}
	
	public StoreEvent(String name,Date effectiveStartDate, Date effectiveEndDate, Store store, HttpSession session) {
		this.name = name;
		this.store = store;
		this.effectiveStartDate = Util.removeTimeOfDate(effectiveStartDate == null ? new Date() : effectiveStartDate);
		this.effectiveEndDate = Util.setMaxTimeOfDate(effectiveEndDate);
		
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


	
	public String getEventName() {
		return name;
	}

	public void setEventName(String name) {
		this.name = name;
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

	@JsonIgnore
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@Override
	public String toString() {
		return "StoreEvent [id=" + id + ", name=" + name + ", effectiveStartDate=" + effectiveStartDate
				+ ", effectiveEndDate=" + effectiveEndDate + ", whoCreationDate=" + whoCreationDate
				+ ", whoLastUpdateDate=" + whoLastUpdateDate + ", whoCreatedBy=" + whoCreatedBy + ", whoLastUpdatedBy="
				+ whoLastUpdatedBy + ", store=" + store + "]";
	}



	
}
