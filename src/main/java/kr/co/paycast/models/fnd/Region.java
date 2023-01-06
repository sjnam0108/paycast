package kr.co.paycast.models.fnd;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.co.paycast.models.pay.SiteRegion;
import kr.co.paycast.utils.Util;

@Entity
@Table(name="FND_REGIONS")
public class Region {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "fnd_region_seq_gen")
	@SequenceGenerator(name = "fnd_region_seq_gen", sequenceName = "FND_REGIONS_SEQ")
	@Column(name = "REGION_ID")
	private int id;
	
	@Column(name = "REGION_CODE", nullable = false, length = 15, unique = true)
	private String regionCode;
	
	@Column(name = "REGION_NAME", nullable = false, length = 100, unique = true)
	private String regionName;
	
	@Column(name = "X", nullable = true, length = 20)
	private String x;
	
	@Column(name = "Y", nullable = true, length = 20)
	private String y;
	
	@Column(name = "COUNTRY_CODE", nullable = false, length = 2)
	private String countryCode;

	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private int whoLastUpdatedBy;
	
	@Column(name = "LAST_UPDATE_LOGIN", nullable = false)
	private int whoLastUpdateLogin;
	
	@OneToMany(mappedBy = "region", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<SiteRegion> siteRegions = new HashSet<SiteRegion>(0);
	
	public Region() {}
	
	public Region(String regionCode, String regionName, String x, String y, String countryCode) {
		this(regionCode, regionName, x, y, countryCode, null);
	}
	
	public Region(String regionCode, String regionName, String x, String y, String countryCode, 
			HttpSession session) {
		this.regionCode = regionCode;
		this.regionName = regionName;
		
		this.x = x;
		this.y = y;
		this.countryCode = countryCode;
		
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
		this.whoLastUpdateLogin = Util.loginId(session);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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

	public int getWhoLastUpdateLogin() {
		return whoLastUpdateLogin;
	}

	public void setWhoLastUpdateLogin(int whoLastUpdateLogin) {
		this.whoLastUpdateLogin = whoLastUpdateLogin;
	}

	@JsonIgnore
	public Set<SiteRegion> getSiteRegions() {
		return siteRegions;
	}

	public void setSiteRegions(Set<SiteRegion> siteRegions) {
		this.siteRegions = siteRegions;
	}
}
