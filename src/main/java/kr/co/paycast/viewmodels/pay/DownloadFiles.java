package kr.co.paycast.viewmodels.pay;

public class DownloadFiles {
	
	private String local_filename;
	private String url;
	private String uid;
	private long file_size;
	
	public String getLocal_filename() {
		return local_filename;
	}
	public void setLocal_filename(String local_filename) {
		this.local_filename = local_filename;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getFile_size() {
		return file_size;
	}
	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@Override
	public String toString() {
		return "DownloadFiles [local_filename=" + local_filename + ", url=" + url + ", uid=" + uid + ", file_size="
				+ file_size + "]";
	}
	

	
	

}
