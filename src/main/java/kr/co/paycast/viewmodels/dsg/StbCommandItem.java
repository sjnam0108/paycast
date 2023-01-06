package kr.co.paycast.viewmodels.dsg;

public class StbCommandItem {
	private int id;
	private String command;
	private String params;
	private String execTime;
	private int stbId;
	
	public StbCommandItem(int id, String command, String params, String execTime) {
		this(id, command, params, execTime, 0);
	}
	
	public StbCommandItem(int id, String command, String params, String execTime,
			int stbId) {
		this.id = id;
		this.command = command;
		this.params = params;
		this.execTime = execTime == null ? "" : execTime;
		this.stbId = stbId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getExecTime() {
		return execTime;
	}

	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	public int getStbId() {
		return stbId;
	}

	public void setStbId(int stbId) {
		this.stbId = stbId;
	}
	
}
