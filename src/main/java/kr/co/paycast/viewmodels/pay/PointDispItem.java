package kr.co.paycast.viewmodels.pay;

import kr.co.paycast.models.store.StoreOrderPoint;

public class PointDispItem {

	private int id = 0;
	private int point = 0;
	
	public PointDispItem() {}
	
	public PointDispItem(StoreOrderPoint orderPoint) {
		this.point = orderPoint.getPointTotal();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

}
