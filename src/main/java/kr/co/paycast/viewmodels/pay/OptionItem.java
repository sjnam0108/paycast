package kr.co.paycast.viewmodels.pay;

import org.codehaus.jackson.annotate.JsonIgnore;

public class OptionItem {
	public static enum OptType {
		GlobalSite, Site, Store, Device
	}

	private String name;
	private String value;
	private OptType optType;
	
	public OptionItem(String name, String value, OptType optType) {
		this.name = name;
		this.value = value;
		this.optType = optType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@JsonIgnore
	public OptType getOptType() {
		return optType;
	}

	public void setOptType(OptType optType) {
		this.optType = optType;
	}

}
