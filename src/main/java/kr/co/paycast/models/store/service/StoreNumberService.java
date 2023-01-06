package kr.co.paycast.models.store.service;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import kr.co.paycast.models.store.StoreOrderNumber;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StoreNumberService {
	// Common
	public void flush();
	
	public void saveOrUpdate(StoreOrderNumber orderNum);

	public StoreOrderNumber getOrderNumbyStoreID(int storeId);
	
	public int getOrderNumMobile(int storeId, HttpSession session, Locale locale);
	
	public int getOrderNum(int storeId, String deviceId, HttpSession session, Locale locale);



}
