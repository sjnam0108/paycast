package kr.co.paycast.models.pay;

import java.util.Comparator;

import kr.co.paycast.viewmodels.pay.BasicItem;

public class PayComparator {

	public static Comparator<Store> StoreStoreNameComparator =
    		new Comparator<Store>() {
    	public int compare(Store item1, Store item2) {
    		return item1.getStoreName().compareTo(item2.getStoreName());
    	}
    };
    
    public static Comparator<Device> DeviceDeviceSeqComparator =
    		new Comparator<Device>() {
    	public int compare(Device item1, Device item2) {
    		return item1.getDeviceSeq().compareTo(item2.getDeviceSeq());
    	}
    };
    
    public static Comparator<BasicItem> BasicItemNameComparator =
    		new Comparator<BasicItem>() {
    	public int compare(BasicItem item1, BasicItem item2) {
    		return item1.getName().compareTo(item2.getName());
    	}
    };
    
    public static Comparator<Content> ContentIdReverseComparator =
    		new Comparator<Content>() {
    	public int compare(Content item1, Content item2) {
    		return Integer.compare(item2.getId(), item1.getId());
    	}
    };
}
