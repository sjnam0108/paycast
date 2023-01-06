package kr.co.paycast.models;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.paycast.utils.Util;

public class PayUserCookie extends UserCookie {

	private String viewCodeStore = "A";

	public PayUserCookie() {}
	
	public PayUserCookie(HttpServletRequest request) {
		
		super(request);
		
		setViewCodeStore(Util.cookieValue(request, "viewCodeStore"));
	}

	public String getViewCodeStore() {
		return viewCodeStore;
	}

	public void setViewCodeStore(String viewCodeStore) {
		if (Util.isValid(viewCodeStore)) {
			this.viewCodeStore = viewCodeStore;
		}
	}

	public void setViewCodeStore(String viewCodeStore, HttpServletResponse response) {
		if (Util.isValid(viewCodeStore)) {
			this.viewCodeStore = viewCodeStore;
			response.addCookie(Util.cookie("viewCodeStore", viewCodeStore));
		}
	}

}
