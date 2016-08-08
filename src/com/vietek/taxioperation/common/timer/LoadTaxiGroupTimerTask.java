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

public class LoadTaxiGroupTimerTask extends TimerTask {

	@Override
	public void run() {
		try {
			List<TaxiGroup> lstTmp = getAllTaxiGroup();
			if (lstTmp != null) {
				for (TaxiGroup taxiGroup : lstTmp) {
					TaxiGroup tmp = MapCommon.MAP_TAXI_GROUP.putIfAbsent(taxiGroup.getId(), taxiGroup);
					if (tmp != null) {
						ObjectUtils.copyObjectSyn(taxiGroup, tmp);
					}
				}
				List<TaxiGroup> lstRemove = new ArrayList<>();
				Iterator<Integer> ite = MapCommon.MAP_TAXI_GROUP.keySet().iterator();
				while (ite.hasNext()) {
					Integer key = (Integer) ite.next();
					TaxiGroup tmpRemove = MapCommon.MAP_TAXI_GROUP.get(key);
					if (!lstTmp.contains(tmpRemove)) {
						lstRemove.add(tmpRemove);
					}
				}
				for (TaxiGroup taxiGroup : lstRemove) {
					MapCommon.MAP_TAXI_GROUP.remove(taxiGroup.getId());
				}
			}
		} catch (Exception e) {
			AppLogger.logUserAction.error("LoadTaxiGroupTimerTask", e);
		}
	}
	
	private List<TaxiGroup> getAllTaxiGroup() {
		List<TaxiGroup> lstResult = new ArrayList<>();
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sip = (SessionImplementor) session;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sip.connection();
			if (conn != null) {
				cs = conn.prepareCall("{call txm_tracking.getAllTaxiGroupInfo()}");
				rs = cs.executeQuery();
				while (rs.next()) {
//					Agent agent = new Agent();
//					agent.setId(rs.getInt("AgentID"));
//					agent.setValue(rs.getString("AgentCode"));
//					TaxiGroup taxiGroup = new TaxiGroup();
//					taxiGroup.setId(rs.getInt("GroupID"));
//					taxiGroup.setAgent(agent);
//					Seat seat = new Seat();
//					seat.setId(rs.getInt("CommonID"));
//					seat.setName(rs.getString("CommonName"));
//					seat.setType(rs.getString("CommonCodeType"));
//					TaxiType taxiType = new TaxiType();
//					taxiType.setId(rs.getInt("TypeID"));
//					taxiType.setName(rs.getString("TypeName"));
//					taxiType.setValue(rs.getString("TypeCode"));
//					taxiType.setSeats(seat);
//					Vehicle vehicle = new Vehicle();
//					vehicle.setId(rs.getInt("VehicleID"));
//					vehicle.setTaxiNumber(rs.getString("LicensePlate"));
//					vehicle.setValue(rs.getString("VehicleNumber"));
//					vehicle.setTaxiGroup(taxiGroup);
//					vehicle.setTaxiType(taxiType);
//					vehicle.setDeviceID(rs.getInt("DeviceID"));
//					VehicleDD veDD = new VehicleDD();
//					veDD.setVehicle(vehicle);
//					veDD.setSeat(rs.getInt("seat"));
//					veDD.setVehicleType(rs.getString("TypeName"));
//					veDD.setFullName(rs.getString("FullName"));
//					lstResult.add(veDD);
					
					TaxiGroup msg = new TaxiGroup();
					msg.setId(rs.getInt("GroupID"));
					msg.setValue(rs.getString("GroupCode"));
					msg.setName(rs.getString("GroupName"));
					msg.setIsActive(rs.getBoolean("Active"));
					msg.setType(rs.getInt("GroupType"));
					msg.setIsDelete(rs.getInt("IsDelete"));
					Agent agent = new Agent();
					agent.setId(rs.getInt("AgentID"));
					msg.setAgent(agent);
					lstResult.add(msg);
					
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
		return lstResult;
	}


}
