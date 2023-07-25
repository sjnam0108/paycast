package kr.co.paycast.viewmodels.pay;

import java.util.ArrayList;
import java.util.List;

public class BannerInfo {
	
	private boolean e_menu_type;
	
	private List<DownloadFiles> bannerList = new ArrayList<DownloadFiles>();

	public boolean isE_menu_type() {
		return e_menu_type;
	}

	public void setE_menu_type(boolean e_menu_type) {
		this.e_menu_type = e_menu_type;
	}

	public List<DownloadFiles> getBannerList() {
		return bannerList;
	}

	public void setBannerList(List<DownloadFiles> bannerList) {
		this.bannerList = bannerList;
	}

	@Override
	public String toString() {
		return "BannerInfo [e_menu_type=" + e_menu_type + ", bannerList=" + bannerList + "]";
	}
	

}
