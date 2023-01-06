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


@Entity
@Table(name="PAY_TOKEN_ACTIVITIES")
public class TokenActivity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "token_activity_seq_gen")
	@SequenceGenerator(name = "token_activity_seq_gen", sequenceName = "PAY_TOKEN_ACTIVITIES_SEQ")
	@Column(name = "TOKEN_ACTIVITY_ID")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DEVICE_ID", nullable = false)
	private Device device;
	
	@Column(name = "ACT_TYPE", nullable = false, length = 1)
	private String actType;		// S: 서버전송, D: 기기전송, E: 기타
	
	@Column(name = "ACT_CODE", length = 20)
	private String actCode;
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;

	
	public TokenActivity() {}
	
	public TokenActivity(Device device, String actType, String actCode) {
		
		this.device = device;
		this.actType = actType;
		this.actCode = actCode;
		this.whoCreationDate = new Date();
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public String getActCode() {
		return actCode;
	}

	public void setActCode(String actCode) {
		this.actCode = actCode;
	}

	public Date getWhoCreationDate() {
		return whoCreationDate;
	}

	public void setWhoCreationDate(Date whoCreationDate) {
		this.whoCreationDate = whoCreationDate;
	}
}
