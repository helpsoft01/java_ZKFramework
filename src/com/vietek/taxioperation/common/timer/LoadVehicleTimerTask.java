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
import com.vietek.taxioperation.model.Seat;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.model.TaxiType;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.model.VehicleDD;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.ObjectUtils;

/**
 *
 * Jul 5, 2016
 *
 * @author VuD
 *
 */

public class LoadVehicleTimerTask extends TimerTask {

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try {
			this.reloadMapVehicle();
		} catch (Exception e) {
			e.printStackTrace();
			AppLogger.logDebug.error(MonitorCommon.getSystemCpuInfo(), e);
		} finally {
			AppLogger.logDebug.info("LoadVehicleTimerTask|TimeFinish:" + (System.currentTimeMillis() - start));
		}
	}

	private void reloadMapVehicle() {
		List<Vehicle> lstVehicle = this.getListVehicle();
		for (Vehicle vehicle : lstVehicle) {
			this.updateMapVehicle(vehicle);
			this.updateMapVehicleDD(vehicle);
		}
	}

	private void updateMapVehicle(Vehicle vehicle) {
		Vehicle tmp = MapCommon.MAP_VEHICLE_ID.putIfAbsent(vehicle.getId(), vehicle);
		if (tmp != null) {
			ObjectUtils.copyObjectSyn(vehicle, tmp);
		}
	}

	private void updateMapVehicleDD(Vehicle vehicle) {
		VehicleDD vehicleDD = this.createVehicleDD(vehicle);
		if (vehicleDD != null) {
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
		} else {
		}
	}

	private VehicleDD createVehicleDD(Vehicle vehicle) {
		VehicleDD result = null;
		try {
			result = new VehicleDD();
			result.setVehicle(vehicle);
			int seatNumber = 0;
			if (vehicle.getTaxiType().getSeats().getId() == 66) {
				seatNumber = 2;
			} else {
				seatNumber = 1;
			}
			result.setSeat(seatNumber);
			result.setFullName(vehicle.getFullValue());
			result.setVehicleType(vehicle.getTaxiType().getName());
		} catch (Exception e) {
			AppLogger.logUserAction.error("Tao VehicleDD|", e);
			result = null;
		}
		return result;
	}

	private List<Vehicle> getListVehicle() {
		List<Vehicle> lstResult = new ArrayList<>();
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
					Vehicle vehicle = new Vehicle();
					vehicle.setId(rs.getInt("VehicleID"));
					vehicle.setTaxiNumber(rs.getString("LicensePlate"));
					vehicle.setValue(rs.getString("VehicleNumber"));
					vehicle.setDeviceID(rs.getInt("DeviceID"));
					int taxiGroupId = rs.getInt("GroupID");
					TaxiGroup taxiGroup = MapCommon.MAP_TAXI_GROUP.get(taxiGroupId);
					vehicle.setTaxiGroup(taxiGroup);
					vehicle.setVehicleGroupID(taxiGroupId);
					Seat seat = new Seat();
					seat.setId(rs.getInt("CommonID"));
					seat.setName(rs.getString("CommonName"));
					seat.setType(rs.getString("CommonCodeType"));
					TaxiType taxiType = new TaxiType();
					taxiType.setId(rs.getInt("TypeID"));
					taxiType.setName(rs.getString("TypeName"));
					taxiType.setValue(rs.getString("TypeCode"));
					taxiType.setSeats(seat);
					vehicle.setTaxiType(taxiType);
					vehicle.setVehicleTypeID(taxiType.getId());
					lstResult.add(vehicle);
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
