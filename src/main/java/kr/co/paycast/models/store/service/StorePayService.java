package kr.co.paycast.models.store.service;

import java.util.List;
import java.util.Locale;

import kr.co.paycast.models.store.StoreOrderPay;
import kr.co.paycast.viewmodels.store.StoreSalesView;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StorePayService {
	// Common
	public void flush();

	public StoreOrderPay getOrderPay(int idInt);
	
	public void saveOrUpdate(StoreOrderPay orderPay);
	
	public List<StoreSalesView> getRead(String fromDate, int storeId, Locale locale);

}
