package com.vietek.taxioperation.common;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.vietek.taxioperation.controller.SysUserController;
import com.vietek.taxioperation.controller.VoipCenterController;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.VoipCenter;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

/**
 *
 * @author VuD
 */
public class CallCenterCommon {

	private static ConcurrentHashMap<String, VoipCenter> mapVoipCenter = new ConcurrentHashMap<>();

	public static String getUrlCenter(String callCenterId) {
		String result = "";
		try {
			VoipCenterController controller = (VoipCenterController) ControllerUtils
					.getController(VoipCenterController.class);
			List<VoipCenter> lstCenter = controller.find("from VoipCenter");
			if (lstCenter != null) {
				for (VoipCenter voipCenter : lstCenter) {
					if (voipCenter.getValue().equalsIgnoreCase(callCenterId)) {
						result = voipCenter.getUrl();
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static VoipCenter getVoipCenter_By_CallCenterId(String callCenterId) {
		// Tuanpa: caching
		VoipCenter result = mapVoipCenter.get(callCenterId);
		if (result == null) {
			try {
				VoipCenterController controller = (VoipCenterController) ControllerUtils
						.getController(VoipCenterController.class);
				List<VoipCenter> lstCenter = controller.find("from VoipCenter");
				if (lstCenter != null) {
					for (VoipCenter voipCenter : lstCenter) {
						if (voipCenter.getValue().equalsIgnoreCase(callCenterId)) {
							result = voipCenter;
							mapVoipCenter.put(callCenterId, result);
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String getCurrentUrlCallCenter() {
		String result = null;
		int id = Env.getUserID();
		SysUserController controller = (SysUserController) ControllerUtils.getController(SysUserController.class);
		SysUser sysUser = controller.get(SysUser.class, id);
		if (sysUser != null) {
			if (!sysUser.getUserName().equals("admin") && !sysUser.getUserName().equals("SupperUser")) {
				SysCompany sysCompany = sysUser.getSysCompany();
				if (sysCompany != null) {
					Set<VoipCenter> setVoip = sysCompany.getVoipCenter();
					for (VoipCenter voipCenter : setVoip) {
						result = voipCenter.getUrl();
						break;
					}
				}
			}
		}
		return result;
	}
}
