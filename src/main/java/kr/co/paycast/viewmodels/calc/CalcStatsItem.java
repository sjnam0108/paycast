package kr.co.paycast.viewmodels.calc;

public class CalcStatsItem {
	
	private String boardYn="N"; // 화면에서 사용되는 Y N
	private String statsMonth; // 월
	private String statsDay; // 일자
	private String statsMenu; // 메뉴
	private String statsAmount; //수량
	private String statsAmt; //단가
	private String statsSalesamt; //매출금액
	
	private String compSelect; //메뉴별로 합하기 위해 사용
	
	public String getBoardYn() {
		return boardYn;
	}
	
	public void setBoardYn(String boardYn) {
		this.boardYn = boardYn;
	}
	
	public String getStatsMonth() {
		return statsMonth;
	}
	
	public void setStatsMonth(String statsMonth) {
		this.statsMonth = statsMonth;
	}
	
	public String getStatsDay() {
		return statsDay;
	}
	
	public void setStatsDay(String statsDay) {
		this.statsDay = statsDay;
	}
	
	public String getStatsMenu() {
		return statsMenu;
	}
	
	public void setStatsMenu(String statsMenu) {
		this.statsMenu = statsMenu;
	}
	
	public String getStatsAmount() {
		return statsAmount;
	}
	
	public void setStatsAmount(String statsAmount) {
		this.statsAmount = statsAmount;
	}
	
	public String getStatsAmt() {
		return statsAmt;
	}
	
	public void setStatsAmt(String statsAmt) {
		this.statsAmt = statsAmt;
	}
	
	public String getStatsSalesamt() {
		return statsSalesamt;
	}
	
	public void setStatsSalesamt(String statsSalesamt) {
		this.statsSalesamt = statsSalesamt;
	}
	
	public String getCompSelect() {
		return compSelect;
	}
	
	public void setCompSelect(String compSelect) {
		this.compSelect = compSelect;
	}
	
}
