package kr.co.paycast.viewmodels.pay;

public class StoreTime {

	private int timeNum;
	private String timeName;
	private boolean clickTF = true;
	
	public StoreTime(int timeNum, String timeName, boolean clickTF) {
		this.timeNum = timeNum;
		this.timeName = timeName;
		this.clickTF = clickTF;
	}

	public int getTimeNum() {
		return timeNum;
	}

	public void setTimeNum(int timeNum) {
		this.timeNum = timeNum;
	}

	public String getTimeName() {
		return timeName;
	}

	public void setTimeName(String timeName) {
		this.timeName = timeName;
	}

	public boolean isClickTF() {
		return clickTF;
	}

	public void setClickTF(boolean clickTF) {
		this.clickTF = clickTF;
	}
	
}
