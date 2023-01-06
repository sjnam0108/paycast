package kr.co.paycast.models;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import kr.co.paycast.utils.Util;

@Component
public class PayMessageManager {
    
	@Autowired
	private MessageSource messages;

	@Autowired
	private MessageManager msgMgr;
	
	public PayMessageManager() { }
	
	
	private String message(String code, Object[] args, Locale locale) {
		return messages.getMessage(code, args, code, locale);
	}
	
	private void addViewMessages(Model model, Locale locale, Message[] msgs) {
		if (msgs != null) {
			for(Message msg : msgs) {
				model.addAttribute(msg.getIdInView(), message(msg.getCode(), msg.getArgs(), locale));
			}
		}
	}
	
	
	public void addCommonMessages(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		msgMgr.addCommonMessages(model, locale, session, request);
		
		addViewMessages(model, locale,
    			new Message[] {
        				new Message("storeMenu_StoreInfo", "mainmenu.StoreInfo"),
        				new Message("storeMenu_StoreDevice", "mainmenu.StoreDevice"),
        				new Message("storeMenu_StoreUser", "mainmenu.StoreUser"),
        				new Message("storeMenu_StoreSetting", "mainmenu.StoreSetting"),

        				new Message("part_kioskOrder", "part.kioskOrder"),
        				new Message("part_mobileOrder", "part.mobileOrder"),
        				new Message("part_kitchenPad", "part.kitchenPad"),
        				
    					new Message("switch_open", "switch.open"),
    					new Message("switch_closed", "switch.closed"),
    					new Message("switch_kioskEnabled", "switch.kioskEnabled"),
    					new Message("switch_kioskDisabled", "switch.kioskDisabled"),
    					new Message("switch_mobileEnabled", "switch.mobileEnabled"),
    					new Message("switch_mobileDisabled", "switch.mobileDisabled"),
        				
    					new Message("deviceType_kiosk", "deviceType.kiosk"),
    					new Message("deviceType_kitchenPad", "deviceType.kitchenPad"),
    					new Message("deviceType_notifier", "deviceType.notifier"),
    					new Message("deviceType_printer", "deviceType.printer"),

        				new Message("btn_selectOtherStore", "selectstore.selectOtherStore"),
    			});
	}
	
	
	public void checkStoreSelectionMessage(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		if (session != null) {
			String msgCode = (String) session.getAttribute("msg");
			String newStoreName = (String) session.getAttribute("newStoreName");
			String oldStoreName = (String) session.getAttribute("oldStoreName");
			
			if (Util.isValid(msgCode)) {
				if (msgCode.equals("autoSelected")) {
					model.addAttribute("notifMsg", msgMgr.message("selectstore.server.msg.autoSelected", locale)
							.replace("{0}", newStoreName));
				} else if (msgCode.equals("onlyOneSelected")) {
					model.addAttribute("notifMsg", msgMgr.message("selectstore.server.msg.onlyOneSelected", locale)
							.replace("{0}", newStoreName));
				} else if (msgCode.equals("deffSelected")) {
					model.addAttribute("notifMsg", msgMgr.message("selectstore.server.msg.deffSelected", locale)
							.replace("{0}", oldStoreName).replace("{1}", newStoreName));
				}
			}
			
			session.removeAttribute("msg");
			session.removeAttribute("newStoreName");
			session.removeAttribute("oldStoreName");
		}
	}
}
