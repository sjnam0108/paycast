package kr.co.paycast.viewmodels.pay;

import kr.co.paycast.models.pay.StoreCoupon;
import kr.co.paycast.models.store.StoreOrderCoupon;

public class CouponDispItem {

	private int id;
	private int price;
	
	private String name;
	
	public CouponDispItem() {}
	
	public CouponDispItem(StoreOrderCoupon coupon) {
		if (coupon != null) {
			StoreCoupon goodCoupon = coupon.getCoupon();
			if(goodCoupon != null){
				this.id = coupon.getId();
				this.price = goodCoupon.getPrice();
				this.name = goodCoupon.getName();
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
