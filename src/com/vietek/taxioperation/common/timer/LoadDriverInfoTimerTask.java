package com.vietek.taxioperation.common.timer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.ObjectUtils;

/**
 *
 * Jul 5, 2016
 *
 * @author VuD
 *
 */

public class LoadDriverInfoTimerTask extends TimerTask {

	@Override
	public void run() {
		try {
			long start = System.currentTimeMillis();
			List<Driver> lstTmp = this.getListDriver();
			if (lstTmp != null) {
				for (Driver driver : lstTmp) {
					Driver tmp = MapCommon.MAP_DRIVER.putIfAbsent(driver.getId(), driver);
					if (tmp != null) {
						ObjectUtils.copyObjectSyn(driver, tmp);
					}
				}
				List<Driver> lstRemove = new ArrayList<>();
				Iterator<Integer> ite = MapCommon.MAP_DRIVER.keySet().iterator();
				while (ite.hasNext()) {
					Integer key = (Integer) ite.next();
					Driver tmpRemove = MapCommon.MAP_DRIVER.get(key);
					if (!lstTmp.contains(tmpRemove)) {
						lstRemove.add(tmpRemove);
					}
				}
				for (Driver driver : lstRemove) {
					MapCommon.MAP_DRIVER.remove(driver.getId());
				}
			}
			AppLogger.logUserAction.info("LoadDriverInfoTimerTask|Thoi gian reload: " + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			AppLogger.logUserAction.error("LoadDriverInfoTimerTask", e);
		}
	}

	private List<Driver> getListDriver() {
		List<Driver> result = new ArrayList<Driver>();
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sip = (SessionImplementor) session;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sip.connection();
			if (conn != null) {
				cs = conn.prepareCall("{call txm_tracking.getAllDriverInfo()}");
				rs = cs.executeQuery();
				while (rs.next()) {
					Driver driver = new Driver();
					driver.setId(rs.getInt("DriverID"));
					driver.setValue(rs.getString("DriverCode"));
					driver.setName(rs.getString("DriverName"));
					driver.setPhoneNumber(rs.getString("PhoneNumber"));
					driver.setPhoneOffice(rs.getString("PhoneOffice"));
					driver.setStaffCard(rs.getInt("StaffCard"));
					driver.setMobileUUID(rs.getString("mobile_uuid"));
					driver.setIsAppRegister(rs.getBoolean("isAppRegister"));
					driver.setPassword(rs.getString("password"));
					int groupId = rs.getInt("GroupID");
					TaxiGroup taxiGroup = MapCommon.MAP_TAXI_GROUP.get(groupId);
					driver.setTaxiGroup(taxiGroup);
					driver.setVehicleGroupID(groupId);
					int agentId = rs.getInt("AgentID");
					Agent agent = MapCommon.MAP_AGENT.get(agentId);
					driver.setAgent(agent);
					result.add(driver);
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return result;

	}

}
