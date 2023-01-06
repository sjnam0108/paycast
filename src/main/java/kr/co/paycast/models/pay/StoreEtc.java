package kr.co.paycast.models.pay;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="PAY_STORE_ETCS")
public class StoreEtc {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "store_etc_seq_gen")
	@SequenceGenerator(name = "store_etc_seq_gen", sequenceName = "PAY_STORE_ETCS_SEQ")
	@Column(name = "STORE_ETC_ID")
	private int id;

	@Column(name = "STORE_PAY_GUBUN", length = 2)
	private String storePayGubun = "ME";
	
	@Column(name = "SP_STORE_KEY", nullable = true, length = 100)
	private String spStoreKey = "";
	
	@Column(name = "SP_AUTH_KEY", nullable = true, length = 100)
	private String spAuthKey = "";
	
	@Column(name = "SP_CANCEL_CODE", nullable = true, length = 50)
	private String spCancelCode = "";

	@Column(name = "EP_STORE_KEY", nullable = true, length = 100)
	private String epStoreKey = "";
	
	@Column(name = "ODER_POSSIBLE_TIME", length = 100)
	private Timestamp oder_possible_Time;			// 주문 시작 시간
		
	@Column(name = "ODER_SETTING_TIME", length = 100)
	private int oder_setting_Time;					// 주문 가능 시간 설정 분 단위설정 값 

	@Column(name = "ODER_POSSIBLE_CHECK", nullable = false)
	private String oderPossiblCheck; 				// 주문 가능 여부  : O= 항상 가능 , P= 일시적 중단, C= 주문 불가
	
	@Column(name = "SAVING_TYPE", nullable = false, length = 4)
	private String savingType = "NO"; 				// 상점 적립 사용 여부 : NO= 사용안함, CP= 쿠폰, PO= 포인트 

	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false, unique = true)
	private Store store;

	
	public StoreEtc() {}
	
	public StoreEtc(Store store) {
		this(store, null);
		setTime();
	}
	
	public StoreEtc(Store store, HttpSession session) {
		setTime();
		this.store = store;
		
		this.savingType = "NO";
		
		touchWhoC(session);
	}

	private void touchWhoC(HttpSession session) {
		this.whoCreationDate = new Date();
		touchWho(session);
	}
	
	public void touchWho(HttpSession session) {
		this.whoLastUpdateDate = new Date();
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStorePayGubun() {
		return storePayGubun;
	}

	public void setStorePayGubun(String storePayGubun) {
		this.storePayGubun = storePayGubun;
	}

	public String getSpStoreKey() {
		return spStoreKey;
	}

	public void setSpStoreKey(String spStoreKey) {
		this.spStoreKey = spStoreKey;
	}

	public String getSpAuthKey() {
		return spAuthKey;
	}

	public void setSpAuthKey(String spAuthKey) {
		this.spAuthKey = spAuthKey;
	}

	public String getSpCancelCode() {
		return spCancelCode;
	}

	public void setSpCancelCode(String spCancelCode) {
		this.spCancelCode = spCancelCode;
	}

	public String getEpStoreKey() {
		return epStoreKey;
	}

	public void setEpStoreKey(String epStoreKey) {
		this.epStoreKey = epStoreKey;
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

	@JsonIgnore
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Timestamp getOder_possible_Time() {
		return oder_possible_Time;
	}

	public void setOder_possible_Time(Timestamp oder_possible_Time) {
		this.oder_possible_Time = oder_possible_Time;
	}

	public int getOder_setting_Time() {
		return oder_setting_Time;
	}

	public void setOder_setting_Time(int oder_setting_Time) {
		this.oder_setting_Time = oder_setting_Time;
	}

	public String getOderPossiblCheck() {
		return oderPossiblCheck;
	}

	public void setOderPossiblCheck(String oderPossiblCheck) {
		this.oderPossiblCheck = oderPossiblCheck;
	}
	
	public String getSavingType() {
		return savingType;
	}

	public void setSavingType(String savingType) {
		this.savingType = savingType;
	}

	public void setTime (){
		Calendar calendar = Calendar.getInstance();
		int setYear = calendar .get(Calendar.YEAR);
		int setMonth = calendar.get(Calendar.MONDAY)+1;
		int setDay = calendar.get(Calendar.DATE-1);
		this.oder_possible_Time=Timestamp.valueOf(String.format("%04d-%02d-%02d 00:00:00", setYear, setMonth, setDay));
		this.oderPossiblCheck="O";
	}

	@Override
	public String toString() {
		return String
				.format("StoreEtc [oder_possible_Time=%s, oder_setting_Time=%s, oderPossiblCheck=%s]",
						oder_possible_Time, oder_setting_Time, oderPossiblCheck);
	}	
	
	
}
