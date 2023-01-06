package kr.co.paycast.models.pay;

public class PayUploadTransModel {

	private int storeId = -1;
	private String type = "NO";
	private String message = "";
	
	private String saveUrl = "/pay/common/uploadsave";

	private String allowedExtensions = "";
	private String code = "";
	
	public PayUploadTransModel() { }

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAllowedExtensions() {
		return allowedExtensions;
	}

	public void setAllowedExtensions(String allowedExtensions) {
		this.allowedExtensions = allowedExtensions;
	}

	public String getSaveUrl() {
		return saveUrl;
	}

	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}
}
