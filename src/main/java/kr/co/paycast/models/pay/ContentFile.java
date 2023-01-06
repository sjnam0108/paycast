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
@Table(name="PAY_CONTENT_FILES")
public class ContentFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "content_file_seq_gen")
	@SequenceGenerator(name = "content_file_seq_gen", sequenceName = "PAY_CONTENT_FILES_SEQ")
	@Column(name = "CONTENT_FILE_ID")
	private int id;
	
	@Column(name = "FOLDER_NAME", nullable = false, length = 200)
	private String folderName;
	
	@Column(name = "FILENAME", nullable = false, length = 100)
	private String filename;
	
	@Column(name = "FILE_LENGTH", nullable = false)
	private long fileLength;
	
	@Column(name = "TRANSFERRED", nullable = false, length = 1)
	private String transferred = "N";
	
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATE_LOGIN", nullable = false)
	private int whoLastUpdateLogin;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DEVICE_ID", nullable = false)
	private Device device;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CONTENT_ID", nullable = false)
	private Content content;
	

	public ContentFile() {}
	
	public ContentFile(Content content, Device device, String folderName, String filename,
			long fileLength, HttpSession session) {
		this.content = content;
		this.device = device;
		this.folderName = folderName;
		this.filename = filename;
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

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
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

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getTransferred() {
		return transferred;
	}

	public void setTransferred(String transferred) {
		this.transferred = transferred;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
	
}
