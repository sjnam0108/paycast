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
import javax.servlet.http.HttpSession;

@Entity
@Table(name="PAY_Ad")
public class Ad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ad_seq_gen")
	@SequenceGenerator(name = "ad_seq_gen", sequenceName = "PAY_AD_SEQ")
	@Column(name = "AD_ID")
	private int id;
	
	@Column(name = "FILE_NAME", nullable = false, length = 50)
	private String fileName;
	
	@Column(name = "FILE_INDEX", nullable = false, length = 50)
	private int fileIndex;

	@Column(name = "STORAGE_PATH", nullable = false, length = 50)
	private String storagePath;
	
	@Column(name = "ENABLED", nullable = false, length = 50)
	private String enabled;
	
	@Column(name = "CREATE_DATE", nullable = false, length = 50)
	private Date createDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false, length = 50)
	private Date lastUpdateDate;
	
	@Column(name = "ORG_FILENAME", nullable = false, length = 250)
	private String orgFilename;
	
	@Column(name = "FILE_LENGTH", nullable = false)
	private long fileLength;
	
	@Column(name = "TYPE", nullable = false)
	private String type;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;


	public Ad() {}
	
	public Ad(Store store, String filename, String orgFilename, long fileLength, int fileIndex,String type, String enabled, String storagePath,
			HttpSession session) {
		
		this.store = store;
		this.fileName = filename;
		this.orgFilename = orgFilename;
		this.fileLength = fileLength;
		this.fileIndex = fileIndex + 1;
		this.type = type;
		this.enabled = enabled;
		this.storagePath = storagePath;
		
		touchWhoC(session);
	}

	private void touchWhoC(HttpSession session) {

		this.createDate = new Date();
		this.lastUpdateDate = new Date();
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public int getIndex() {
		return fileIndex;
	}


	public void setIndex(int fileIndex) {
		this.fileIndex = fileIndex + 1;
	}


	public String getStoragePath() {
		return storagePath;
	}


	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}


	public String getEnabled() {
		return enabled;
	}


	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}


	public Store getStore() {
		return store;
	}


	public void setStore(Store store) {
		this.store = store;
	}



	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}


	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	

	public String getOrgFilename() {
		return orgFilename;
	}


	public void setOrgFilename(String orgFilename) {
		this.orgFilename = orgFilename;
	}


	public long getFileLength() {
		return fileLength;
	}


	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Ad [id=" + id + ", fileName=" + fileName + ", fileIndex=" + fileIndex + ", storagePath=" + storagePath
				+ ", enabled=" + enabled + ", createDate=" + createDate + ", lastUpdateDate=" + lastUpdateDate
				+ ", orgFilename=" + orgFilename + ", fileLength=" + fileLength + ", type=" + type + ", store=" + store
				+ "]";
	}



	
	
	
}
