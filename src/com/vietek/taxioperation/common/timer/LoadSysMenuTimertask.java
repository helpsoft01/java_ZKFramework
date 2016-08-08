package com.vietek.taxioperation.common.timer;

import java.util.List;
import java.util.TimerTask;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.ListCommon;
import com.vietek.taxioperation.controller.SysMenuController;
import com.vietek.taxioperation.model.SysMenu;
import com.vietek.taxioperation.util.ControllerUtils;

/**
 *
 * @author VuD
 */
public class LoadSysMenuTimertask extends TimerTask {

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try {
			SysMenuController controller = (SysMenuController) ControllerUtils.getController(SysMenuController.class);
			List<SysMenu> lstModel = controller.find("from SysMenu order by sequence");
			if (lstModel != null && lstModel.size() > 0) {
				ListCommon.LIST_SYS_MENU.clear();
				for (SysMenu sysMenu : lstModel) {
					ListCommon.LIST_SYS_MENU.add(sysMenu);
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("LoadSysMenuTimertask", e);
		} finally {
			AppLogger.logDebug.info("LoadSysMenuTimertask|TimeFinish:" + (System.currentTimeMillis() - start));
		}
	}

}
