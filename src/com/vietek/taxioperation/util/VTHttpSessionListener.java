package com.vietek.taxioperation.util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.zkoss.zk.ui.http.HttpSessionListener;

public class VTHttpSessionListener extends HttpSessionListener {

	@Override
	public void sessionDestroyed(HttpSessionEvent evt) {
		super.sessionDestroyed(evt);
		HttpSession session = evt.getSession();
		if (session.getAttribute("user") != null)
			CheckOnlineUtils.logout(session.getAttribute("user").toString());
		
	}
}
