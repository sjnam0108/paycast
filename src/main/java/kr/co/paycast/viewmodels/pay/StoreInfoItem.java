package kr.co.paycast.viewmodels.pay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kr.co.paycast.models.pay.Store;

public class StoreInfoItem {
	
	private int id;

	private String openType;
	private String bizName;
	private String bizRep;
	private String bizNum;
	private String phone;
	private String address;
	private String localCode;
	private String addr2;
	private String openHours;
	private String startTime;
	private String endTime;
	private String oderPossibleCheck;
	private String oderPosibleTimeFomat;

	private int settingTime;
	
	private boolean openHour24;
	private boolean nextDayClose = false;	// 다음날 영업종료
	
	
	private List<String> rsvpTimeList;

	public StoreInfoItem(Store store) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a",Locale.US);
		Calendar cal = Calendar.getInstance();
		this.id = store.getId();
		
		if(store.getStoreEtc().getOder_possible_Time()!=null){
			cal.setTime(store.getStoreEtc().getOder_possible_Time());	
		}
		cal.add(Calendar.MINUTE, store.getStoreEtc().getOder_setting_Time());
		
		this.openType = store.getOpenType();
		this.bizName = store.getBizName();
		this.bizRep = store.getBizRep();
		this.bizNum = store.getBizNum();
		this.phone = store.getPhone();
		this.address = store.getAddress();
		this.localCode = store.getLocalCode();
		this.addr2 = store.getAddr2();
		
		this.settingTime = store.getStoreEtc().getOder_setting_Time();
		if(!openHour24){
			this.startTime = store.getStartTime()!=null?sdf.format(store.getStartTime())+"":"00:00 AM";
			this.endTime = store.getEndTime()!=null?sdf.format(store.getEndTime())+"":"00:00 AM";
		}else{
			this.startTime = "00:00 AM";
			this.endTime = "12:00 PM";
		}
		
		switch (store.getStoreEtc().getOderPossiblCheck()) {
		case "O":
			this.oderPosibleTimeFomat="언제나 가능";
			break;
		case "C":
			this.oderPosibleTimeFomat="주문 불가능";
			break;	
		default:
			this.oderPosibleTimeFomat=sdf.format(new Date(cal.getTimeInMillis()))+" 이후";
			break;
		}
		this.oderPossibleCheck=store.getStoreEtc().getOderPossiblCheck();
		this.openHour24 = store.isOpenHour_24();
		this.openHours = store.getOpenHours();
		
		// String 형식으로 저장되어 있는 예약시간을 List 형식으로 변경하여 VIEW로 전송
		String[] rsvpTimeArray = store.getStoreOpt().getRsvpTime().split(",");
		List<String> rsvpTimeList = new ArrayList<String>(Arrays.asList(rsvpTimeArray));
		this.rsvpTimeList = rsvpTimeList;
		
		this.nextDayClose = store.getStoreOpt().isNextDayClose();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getBizRep() {
		return bizRep;
	}

	public void setBizRep(String bizRep) {
		this.bizRep = bizRep;
	}

	public String getBizNum() {
		return bizNum;
	}

	public void setBizNum(String bizNum) {
		this.bizNum = bizNum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getOpenHours() {
		return openHours;
	}

	public void setOpenHours(String openHours) {
		this.openHours = openHours;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getOderPosibleTimeFomat() {
		return oderPosibleTimeFomat;
	}

	public void setOderPosibleTimeFomat(String oderPosibleTimeFomat) {
		this.oderPosibleTimeFomat = oderPosibleTimeFomat;
	}

	public String getOderPossibleCheck() {
		return oderPossibleCheck;
	}

	public void setOderPossibleCheck(String oderPossibleCheck) {
		this.oderPossibleCheck = oderPossibleCheck;
	}

	public boolean isOpenHour24() {
		return openHour24;
	}

	public void setOpenHour24(boolean openHour24) {
		this.openHour24 = openHour24;
	}

	public List<String> getRsvpTimeList() {
		return rsvpTimeList;
	}

	public void setRsvpTimeList(List<String> rsvpTimeList) {
		this.rsvpTimeList = rsvpTimeList;
	}

	public int getSettingTime() {
		return settingTime;
	}

	public void setSettingTime(int settingTime) {
		this.settingTime = settingTime;
	}

	public boolean isNextDayClose() {
		return nextDayClose;
	}

	public void setNextDayClose(boolean nextDayClose) {
		this.nextDayClose = nextDayClose;
	}
	
}
