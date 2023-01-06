package kr.co.paycast.viewmodels.store;

public class MenuXmlItem  {
	
	private String foldername ;	//foldername /경로

	private String filename; //파일 명
	
	private String filelength; //파일 크기
	
	private String stbfileid; //기기에서 파일 받았는지 확인하기 위해 사용되는 dplySchdCondFile ID 값
	
	private String playatonce; 
	private String kfileid; 
	private String kroot;
	
	public MenuXmlItem(String foldername, String filename, String filelength) {
		this.foldername = foldername;
		this.filename = filename;
		this.filelength = filelength;
		this.playatonce = "Y";
		this.kfileid = "-1";
		this.kroot = "";
	}
	
	public MenuXmlItem() {}

	public String getFoldername() {
		return foldername;
	}
	
	public void setFoldername(String foldername) {
		this.foldername = foldername;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getFilelength() {
		return filelength;
	}
	
	public void setFilelength(String filelength) {
		this.filelength = filelength;
	}

	public String getStbfileid() {
		return stbfileid;
	}

	public void setStbfileid(String stbfileid) {
		this.stbfileid = stbfileid;
	}

	public String getPlayatonce() {
		return playatonce;
	}
	
	public void setPlayatonce(String playatonce) {
		this.playatonce = playatonce;
	}
	
	public String getKfileid() {
		return kfileid;
	}
	
	public void setKfileid(String kfileid) {
		this.kfileid = kfileid;
	}
	
	public String getKroot() {
		return kroot;
	}
	
	public void setKroot(String kroot) {
		this.kroot = kroot;
	} 
	
}
