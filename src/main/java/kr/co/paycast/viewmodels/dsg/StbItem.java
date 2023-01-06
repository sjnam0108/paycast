package kr.co.paycast.viewmodels.dsg;

public class StbItem {
	private int id;
	private String stbName;
	
	public StbItem(int id, String stbName) {
		this.id = id;
		this.stbName = stbName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStbName() {
		return stbName;
	}

	public void setStbName(String stbName) {
		this.stbName = stbName;
	}
}
