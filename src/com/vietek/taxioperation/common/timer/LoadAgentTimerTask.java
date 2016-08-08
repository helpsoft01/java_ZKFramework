package com.vietek.taxioperation.common.timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.controller.AgentController;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.ObjectUtils;

/**
 *
 * Jul 5, 2016
 *
 * @author VuD
 *
 */

public class LoadAgentTimerTask extends TimerTask {

	@Override
	public void run() {
		try {
			AgentController controller = (AgentController) ControllerUtils.getController(AgentController.class);
			List<Agent> lstTmp = controller.find("from Agent");
			if (lstTmp != null) {
				for (Agent agent : lstTmp) {
					Agent tmp = MapCommon.MAP_AGENT.putIfAbsent(agent.getId(), agent);
					if (tmp != null) {
						ObjectUtils.copyObjectSyn(agent, tmp);
					}
				}
				List<Agent> lstRemove = new ArrayList<>();
				Iterator<Integer> ite = MapCommon.MAP_AGENT.keySet().iterator();
				while (ite.hasNext()) {
					Integer key = (Integer) ite.next();
					Agent tmpRemove = MapCommon.MAP_AGENT.get(key);
					if (!lstTmp.contains(tmpRemove)) {
						lstRemove.add(tmpRemove);
					}
				}
				for (Agent agent : lstRemove) {
					MapCommon.MAP_AGENT.remove(agent.getId());
				}
			}
		} catch (Exception e) {
			AppLogger.logUserAction.error("LoadAgentTimerTask", e);
		}
	}

}
