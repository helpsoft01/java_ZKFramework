package com.vietek.taxioperation.services;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

import com.vietek.taxioperation.util.Env;

public class AuthenticationInit implements Initiator {

	public void doInit(Page page, Map<String, Object> args) throws Exception {
		
		int userID = Env.getUserID();
		if(userID == -1){
			Executions.sendRedirect("/login.zul");
			return;
		}
	}
}