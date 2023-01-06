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

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.co.paycast.utils.Util;

@Entity
@Table(name="PAY_CONTENTS")
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "content_seq_gen")
	@SequenceGenerator(name = "content_seq_gen", sequenceName = "PAY_CONTENTS_SEQ")
	@Column(name = "CONTENT_ID")
	private int id;
	
	@Column(name = "CONTENT_NAME", nullable = false, length = 100)
	private String contentName;
	
	@Column(name = "CONTENT_TYPE", nullable = false, length = 1)
	private String contentType;					// M: 키오스크 메뉴컨텐츠

	// 컨텐츠 자료가 물리적인 파일을 포함할 경우, 준비 시차가 발생되는데
	// (파일을 FTP 서버의 서비스 가능한 폴더로 복사 등의 이유)
	// statusCode가 처음에는 "N'이었다가, 모든 물리파일 준비가 완료되면 "Y"로 변경
	@Column(name = "STATUS_CODE", nullable = false, length = 1)
	private String statusCode;

	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATE_LOGIN", nullable = false)
	private int whoLastUpdateLogin;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;

	
	@OneToMany(mappedBy = "content", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ContentFile> contentFiles = new HashSet<ContentFile>(0);
	
	
	public Content() {}
	
	public Content(Store store, String contentType, String contentName, HttpSession session) {
		this.store = store;
		this.contentType = contentType;
		this.contentName = contentName;
		
		this.statusCode = "N";
		
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

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
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

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@JsonIgnore
	public Set<ContentFile> getContentFiles() {
		return contentFiles;
	}

	public void setContentFiles(Set<ContentFile> contentFiles) {
		this.contentFiles = contentFiles;
	}
	
}
