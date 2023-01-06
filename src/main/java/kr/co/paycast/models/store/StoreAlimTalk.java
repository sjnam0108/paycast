package kr.co.paycast.models.store;

import javax.persistence.Entity;

@Entity
public class StoreAlimTalk {
	
	private String shortName;	//매장 id
	private String name;		//매장명
	private String phone;		//매장전화번호
	private String orderSeq;	//주문번호
	private String allMenu;		//주문메뉴
	private String finMenu;		//완료메뉴
	private String telNumber;	//고객 전화 번호
	private String senderKey;	//발신 프로필 키
	private String tmplCd;		//템플릿코드
	private String subject;		//제목
	private String msg;			//메시지
	private String smsmsg;		//sms메시지
	
	private String date;		//조회시 결과 date
	
	//set 할경우 알기 쉽게 분리
	private int totalCnt = 0;	//알림톡 + SMS count
	private int alimCnt = 0;	//알림톡 count
	private int smsCnt = 0;		//SMS count
	
	public StoreAlimTalk() {}

	public StoreAlimTalk(String date, String shortName, String name, int alimCnt, int smsCnt){
		this.date = date;
		this.shortName = shortName;
		this.name = name;
		this.alimCnt = alimCnt;
		this.smsCnt = smsCnt;
	}
	
	public StoreAlimTalk(String shortName, String name, String phone, String orderSeq, String allMenu, String finMenu, String telNumber,
			String senderKey, String tmplCd, String subject, String msg, String smsmsg){
		this.shortName = shortName;
		this.name = name;
		this.phone = phone;
		this.orderSeq = orderSeq;
		this.allMenu = allMenu;
		this.finMenu = finMenu;
		this.telNumber = telNumber;
		this.senderKey = senderKey;
		this.tmplCd = tmplCd;
		this.subject = subject;
		this.msg = msg;
		this.smsmsg = smsmsg;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getAllMenu() {
		return allMenu;
	}

	public void setAllMenu(String allMenu) {
		this.allMenu = allMenu;
	}

	public String getFinMenu() {
		return finMenu;
	}

	public void setFinMenu(String finMenu) {
		this.finMenu = finMenu;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getSenderKey() {
		return senderKey;
	}

	public void setSenderKey(String senderKey) {
		this.senderKey = senderKey;
	}

	public String getTmplCd() {
		return tmplCd;
	}

	public void setTmplCd(String tmplCd) {
		this.tmplCd = tmplCd;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSmsmsg() {
		return smsmsg;
	}

	public void setSmsmsg(String smsmsg) {
		this.smsmsg = smsmsg;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	public int getAlimCnt() {
		return alimCnt;
	}

	public void setAlimCnt(int alimCnt) {
		this.alimCnt = alimCnt;
	}

	public int getSmsCnt() {
		return smsCnt;
	}

	public void setSmsCnt(int smsCnt) {
		this.smsCnt = smsCnt;
	}

}
