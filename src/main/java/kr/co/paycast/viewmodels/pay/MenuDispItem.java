package kr.co.paycast.viewmodels.pay;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import kr.co.paycast.info.PayGlobalInfo;
import kr.co.paycast.models.pay.Menu;
import kr.co.paycast.models.pay.StorePolicy;
import kr.co.paycast.models.pay.service.CouponPointService;
import kr.co.paycast.utils.SolUtil;
import kr.co.paycast.utils.Util;


public class MenuDispItem {

	private int id;
	private String code;
	private int siblingSeq;
	private float price;
	
	private String name;
	private String flagType;
	private String intro;
	private String imgPathFilename;
	private String status;
	
	private boolean published;
	private boolean soldOut;
	
	private List<OptionalMenuItem> manMenus = new ArrayList<OptionalMenuItem>();
	private List<OptionalMenuItem> optMenus = new ArrayList<OptionalMenuItem>();
	
	
	public MenuDispItem() {}
	
	public MenuDispItem(Menu menu) {
		
		if (menu != null) {
			this.id = menu.getId();
			this.code = menu.getCode();
			this.siblingSeq = menu.getSiblingSeq();
			this.name = menu.getName();
			this.published = Util.isValid(menu.getPublished()) && menu.getPublished().equals("Y");
			
			this.status = "D".equals(menu.getPublished())? "D":"";
			
			this.price = menu.getPrice() == null ? 0 : menu.getPrice();
			this.flagType = menu.getFlagType();
			this.intro = menu.getIntro();
			
			this.imgPathFilename = SolUtil.getUploadFilename(menu.getMenuImageId(), 
					SolUtil.getUrlRoot("Menu", menu.getStore().getId()) + "/",
					PayGlobalInfo.DefaultMenuImage);
			
			this.soldOut = menu.isSoldOut();
		}
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSiblingSeq() {
		return siblingSeq;
	}

	public void setSiblingSeq(int siblingSeq) {
		this.siblingSeq = siblingSeq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getFlagType() {
		return flagType;
	}

	public void setFlagType(String flagType) {
		this.flagType = flagType;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getImgPathFilename() {
		return imgPathFilename;
	}

	public void setImgPathFilename(String imgPathFilename) {
		this.imgPathFilename = imgPathFilename;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isSoldOut() {
		return soldOut;
	}

	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}

	public List<OptionalMenuItem> getManMenus() {
		return manMenus;
	}

	public void setManMenus(List<OptionalMenuItem> manMenus) {
		this.manMenus = manMenus;
	}

	public List<OptionalMenuItem> getOptMenus() {
		return optMenus;
	}

	public void setOptMenus(List<OptionalMenuItem> optMenus) {
		this.optMenus = optMenus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
