package kr.co.paycast.models.store.service;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.paycast.models.store.StoreOrderBasket;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Transactional
public interface EasyPayService {
	// Common
	public void flush();
	
	// 메뉴 주문 승인요청
	public String easyPaySubmit(int storeId, StoreOrderBasket basket, String sp_mall_id, String sp_res_cd, String sp_res_msg, String sp_tr_cd, String sp_ret_pay_type, String sp_trace_no, String sp_order_no, 
			String sp_sessionkey, String sp_encrypt_data, String sp_mobilereserved1, String sp_mobilereserved2, String sp_reserved1, String sp_reserved2, String sp_reserved3,
			String sp_reserved4, String sp_card_code, String sp_eci_code, String sp_card_req_type, String sp_save_useyn, String sp_card_prefix, String sp_card_no_7,
			String sp_spay_cp, String sp_prepaid_cp, Model model, HttpServletRequest request, Locale locale, HttpSession session);

	// 메뉴 취소 
	public String easyPayCancel(int storeId, int storeOrderId, String verifiCode, HttpServletRequest request, Locale locale, HttpSession session);

}
