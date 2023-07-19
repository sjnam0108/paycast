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

import kr.co.paycast.utils.Util;


@Entity
@Table(name="PAY_UPLOAD_FILES")
public class UploadFile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "upload_file_seq_gen")
	@SequenceGenerator(name = "upload_file_seq_gen", sequenceName = "PAY_UPLOAD_FILES_SEQ")
	@Column(name = "UPLOAD_FILE_ID")
	private int id;
	
	
	@Column(name = "FILENAME", nullable = false, length = 250)
	private String filename;
	
	@Column(name = "ORG_FILENAME", nullable = false, length = 250)
	private String orgFilename;
	
	@Column(name = "FILE_LENGTH", nullable = false)
	private long fileLength;

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;

	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATE_LOGIN", nullable = false)
	private int whoLastUpdateLogin;

	
	public UploadFile() {}
	
	public UploadFile(Store store, String filename, String orgFilename, long fileLength, 
			HttpSession session) {
		
		this.store = store;
		this.filename = filename;
		this.orgFilename = orgFilename;
		this.fileLength = fileLength;
		
		touchWhoC(session);
	}

	private void touchWhoC(HttpSession session) {
		
		this.whoCreatedBy = Util.loginUserId(session);
		this.whoCreationDate = new Date();
		this.whoLastUpdateLogin = Util.loginId(session);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Date getWhoCreationDate() {
		return whoCreationDate;
	}

	public void setWhoCreationDate(Date whoCreationDate) {
		this.whoCreationDate = whoCreationDate;
	}

	public int getWhoCreatedBy() {
		return whoCreatedBy;
	}

	public void setWhoCreatedBy(int whoCreatedBy) {
		this.whoCreatedBy = whoCreatedBy;
	}

	public int getWhoLastUpdateLogin() {
		return whoLastUpdateLogin;
	}

	public void setWhoLastUpdateLogin(int whoLastUpdateLogin) {
		this.whoLastUpdateLogin = whoLastUpdateLogin;
	}

	@Override
	public String toString() {
		return "UploadFile [id=" + id + ", filename=" + filename + ", orgFilename=" + orgFilename + ", fileLength="
				+ fileLength + ", store=" + store + ", whoCreationDate=" + whoCreationDate + ", whoCreatedBy="
				+ whoCreatedBy + ", whoLastUpdateLogin=" + whoLastUpdateLogin + "]";
	}
	
	
	
}
