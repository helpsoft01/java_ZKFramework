package com.vietek.taxioperation.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import com.vietek.taxioperation.common.EnumUserAction;
import com.vietek.taxioperation.controller.SysUserController;
import com.vietek.taxioperation.model.MapSysGroupCompany;
import com.vietek.taxioperation.model.MapSysGroupVehicle;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.SysGroup;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.util.CheckOnlineUtils;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.SaveLogToQueue;

public class AuthenticationService {

	public SysUser login(String name, String pass) {
		SysUserController userController = (SysUserController) ControllerUtils.getController(SysUserController.class);
		SysUser user = userController.loginUser(name, pass);
		if (user != null) {
			if (user.getId() != Env.getUserID()) {
				Env.getListCurrentFunction().clear();
				Env.getMapUserRule().clear();
				Env.setContext(Env.COMPANY_VALUE, "");
				Env.setContext(Env.COMPANY_ID, -1);
				Env.setContext(Env.LIST_COMPANY, null);
				Env.setContext(Env.LIST_COMPANY_ID, null);
				Env.setContext(Env.LIST_VEHICLE, null);
			}
			Env.setContext(Env.EXT_NUM, 99999);
			Env.setContext(Env.USER_ID, user.getId() + "");
			Env.setContext(Env.USER_FULL_NAME, user.getName());
			Env.setContext(Env.USER_NAME, user.getUserName());
			Env.setContext("user", name);
			Env.setUser(user);
			Env.setIsOpen_Tab_DTV(false);
			SysCompany company = user.getSysCompany();
			Env.setCompany(company);
			if (company != null) {
				Env.setContext(Env.COMPANY_VALUE, company.getValue());
				Env.setContext(Env.COMPANY_ID, company.getId());
			} else {
				Env.setContext(Env.COMPANY_VALUE, "");
				Env.setContext(Env.COMPANY_ID, -1);
			}
			List<Integer> lstCompanyId = new ArrayList<>();
			List<SysCompany> lstCompany = new ArrayList<>();
			List<Integer> lstVehicle = new ArrayList<>();
			Set<SysGroup> group = user.getSysGroup();
			for (SysGroup sysGroup : group) {
				Set<MapSysGroupCompany> setCompany = sysGroup.getSetCompany();
				for (MapSysGroupCompany mapSysGroupCompany : setCompany) {
					SysCompany companyTmp = mapSysGroupCompany.getSysCompany();
					if (companyTmp != null) {
						if (companyTmp.getIsActive()) {
							lstCompany.add(companyTmp);
							lstCompanyId.add(companyTmp.getId());
						}
					}
				}
				Set<MapSysGroupVehicle> setVehicle = sysGroup.getSetVehicle();
				for (MapSysGroupVehicle mapSysGroupVehicle : setVehicle) {
					lstVehicle.add(mapSysGroupVehicle.getVehicle().getId());
				}
			}
			if (!lstCompany.contains(company)) {
				if (company != null) {
					lstCompany.add(company);
					lstCompanyId.add(company.getId());
				}
			}
			if (lstCompanyId.size() <= 0) {
				lstCompanyId.add(-1);
			}
			if (lstVehicle.size() <= 0) {
				lstVehicle.add(-1);
			}
			Env.setContext(Env.LIST_COMPANY, lstCompany);
			Env.setContext(Env.LIST_COMPANY_ID, lstCompanyId);
			Env.setContext(Env.LIST_VEHICLE, lstVehicle);
			
			//dungnd
			SaveLogToQueue savelog = new SaveLogToQueue(user, EnumUserAction.LOGIN, null, Env.getUserID());
			savelog.start();
		}
		return user;
	}

	public void logout() {
		//dungnd
		SaveLogToQueue savelog = new SaveLogToQueue(Env.getUser(), EnumUserAction.LOGOUT, null, Env.getUserID());
		savelog.start();
		
		CheckOnlineUtils.logout(Env.getUser().getUserName());
		Env.setContext(Env.USER_ID, "-1");
		Env.setIsLogged(false);
		Session session = Executions.getCurrent().getSession();
		session.removeAttribute(Env.PROPERTIES_ATT);
		session.invalidate();
	}
}