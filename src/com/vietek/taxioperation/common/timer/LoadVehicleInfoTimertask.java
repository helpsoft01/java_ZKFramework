package com.vietek.taxioperation.common.timer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.common.MonitorCommon;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.Seat;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.model.TaxiType;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.model.VehicleDD;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.ObjectUtils;

/**
 *
 * @author VuD
 * @deprecated
 */
public class LoadVehicleInfoTimertask extends TimerTask {

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try {
			this.refreshMapVehicle();
		} catch (Exception e) {
			e.printStackTrace();
			AppLogger.logDebug.error(MonitorCommon.getSystemCpuInfo(), e);
		} finally {
			AppLogger.logDebug.info("LoadVehicleInfoTimertask|TimeFinish:" + (System.currentTimeMillis() - start));
		}
	}

	private void refreshMapVehicle() {
		List<VehicleDD> lstVehicleDD = getAllVehicle();
		for (VehicleDD vehicleDD : lstVehicleDD) {
			if (vehicleDD.getVehicle() == null) {
				continue;
			}
			String key = vehicleDD.getFullName();
			MapCommon.MAP_VEHICLE_INFO_FULL.put(key, vehicleDD);
			VehicleDD tmp = vehicleDD;
			MapCommon.MAP_VEHICLEDD_ID.put(vehicleDD.getVehicle().getId() + "", vehicleDD);

			tmp = (VehicleDD) MapCommon.MAP_VEHICLE_INFO.get(vehicleDD.getVehicle().getValue());
			List<VehicleDD> lstTmp = (List<VehicleDD>) MapCommon.MAP_LIST_VEHICLE_INFO.get(vehicleDD.getVehicle().getValue());
			if (tmp == null && lstTmp == null) {
				MapCommon.MAP_VEHICLE_INFO.put(vehicleDD.getVehicle().getValue(), vehicleDD);
			} else if (tmp == null && lstTmp != null) {
				// lstTmp.add(vehicleDD);
				boolean isNew = true;
				for (VehicleDD vehicleDD2 : lstTmp) {
					if (vehicleDD.getVehicle().getId() == vehicleDD2.getVehicle().getId()) {
						ObjectUtils.copyObjectSyn(vehicleDD, vehicleDD2);
						isNew = false;
						break;
					}
				}
				if (isNew) {
					lstTmp.add(vehicleDD);
				}
			} else if (tmp != null && lstTmp == null) {
				// String oldKey = tmp.getFullName();
				// if (key.equals(oldKey)) {
				// ObjectUtils.copyObjectSyn(vehicleDD, tmp);
				// } else {
				MapCommon.MAP_VEHICLE_INFO.remove(vehicleDD.getVehicle().getValue());
				lstTmp = new ArrayList<>();
				lstTmp.add(tmp);
				lstTmp.add(vehicleDD);
				MapCommon.MAP_LIST_VEHICLE_INFO.put(vehicleDD.getVehicle().getValue(), lstTmp);
				// }
			}
		}
	}

	private List<VehicleDD> getAllVehicle() {
		List<VehicleDD> lstResult = new ArrayList<>();
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sip = (SessionImplementor) session;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sip.connection();
			if (conn != null) {
				cs = conn.prepareCall("{call txm_tracking.getAllVehicleInfo()}");
				rs = cs.executeQuery();
				while (rs.next()) {
					Agent agent = new Agent();
					agent.setId(rs.getInt("AgentID"));
					agent.setValue(rs.getString("AgentCode"));
					TaxiGroup taxiGroup = new TaxiGroup();
					taxiGroup.setId(rs.getInt("GroupID"));
					taxiGroup.setAgent(agent);
					Seat seat = new Seat();
					seat.setId(rs.getInt("CommonID"));
					seat.setName(rs.getString("CommonName"));
					seat.setType(rs.getString("CommonCodeType"));
					TaxiType taxiType = new TaxiType();
					taxiType.setId(rs.getInt("TypeID"));
					taxiType.setName(rs.getString("TypeName"));
					taxiType.setValue(rs.getString("TypeCode"));
					taxiType.setSeats(seat);
					Vehicle vehicle = new Vehicle();
					vehicle.setId(rs.getInt("VehicleID"));
					vehicle.setTaxiNumber(rs.getString("LicensePlate"));
					vehicle.setValue(rs.getString("VehicleNumber"));
					vehicle.setTaxiGroup(taxiGroup);
					vehicle.setTaxiType(taxiType);
					vehicle.setDeviceID(rs.getInt("DeviceID"));
					VehicleDD veDD = new VehicleDD();
					veDD.setVehicle(vehicle);
					veDD.setSeat(rs.getInt("seat"));
					veDD.setVehicleType(rs.getString("TypeName"));
					veDD.setFullName(rs.getString("FullName"));
					lstResult.add(veDD);
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