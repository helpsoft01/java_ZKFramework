package com.vietek.taxioperation.processor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.common.MonitorCommon;
import com.vietek.taxioperation.model.Device;
import com.vietek.taxioperation.model.Seat;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.model.TaxiType;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.model.VehicleDD;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.ObjectUtils;
import com.vietek.trackingOnline.common.VietekWarning;

/**
 *
 * Aug 1, 2016
 *
 * @author VuD
 *
 */

public class LoadVehicleProcessor extends Thread {
	public LoadVehicleProcessor() {
		super();
		this.setName("LoadVehicleProcessor");
	}

	@Override
	public void run() {
		while (true) {
			try {

				long start = System.currentTimeMillis();
				try {
					this.reloadMapVehicle();
				} catch (Exception e) {
					e.printStackTrace();
					AppLogger.logDebug.error(MonitorCommon.getSystemCpuInfo(), e);
				} finally {
					long timefinish = System.currentTimeMillis() - start;
					AppLogger.logDebug.info("TimeFinish:" + timefinish);
				}
				Thread.sleep(900 * 1000);
			} catch (Exception e) {
				AppLogger.logDebug.error(MonitorCommon.getSystemCpuInfo(), e);
			}
		}

	}

	private void reloadMapVehicle() {
		this.getListVehicle();
		// List<VehicleDD> lstVehicle = this.getListVehicle();
		// for (int i = 0; i < lstVehicle.size(); i++) {
		// VehicleDD vehicleDD = lstVehicle.get(i);
		// this.updateMapVehicle(vehicleDD);
		// this.updateMapVehicleDD(vehicleDD);
		// }
		// for (Vehicle vehicle : lstVehicle) {
		// this.updateMapVehicle(vehicle);
		// this.updateMapVehicleDD(vehicle);
		// }
	}

	private void updateMapVehicle(VehicleDD vehicleDD) {
		try {
			Vehicle tmp = MapCommon.MAP_VEHICLE_ID.putIfAbsent(vehicleDD.getVehicleId(), vehicleDD.getVehicle());
			if (tmp != null) {
				ObjectUtils.copyObjectSyn(vehicleDD.getVehicle(), tmp);
			}
		} catch (Exception e) {
			AppLogger.logDebug.error(MonitorCommon.getSystemCpuInfo(), e);
		}
	}

	private void updateMapVehicleDD(VehicleDD vehicleDD) {
		try {

			if (vehicleDD != null) {
				String key = vehicleDD.getFullName();
				MapCommon.MAP_VEHICLE_INFO_FULL.put(key, vehicleDD);
				VehicleDD tmp = vehicleDD;

				MapCommon.MAP_VEHICLEDD_ID.put(vehicleDD.getVehicle().getId() + "", vehicleDD);

				tmp = (VehicleDD) MapCommon.MAP_VEHICLE_INFO.get(vehicleDD.getVehicle().getValue());
				@SuppressWarnings("unchecked")
				List<VehicleDD> lstTmp = (List<VehicleDD>) MapCommon.MAP_LIST_VEHICLE_INFO
						.get(vehicleDD.getVehicle().getValue());
				if (tmp == null && lstTmp == null) {
					MapCommon.MAP_VEHICLE_INFO.put(vehicleDD.getVehicle().getValue(), vehicleDD);
				} else if (tmp == null && lstTmp != null) {
					List<VehicleDD> lstAdd = new ArrayList<>();
					boolean isNew = true;
					for (VehicleDD vehicleDD2 : lstTmp) {
						if (!lstAdd.contains(vehicleDD2)) {
							lstAdd.add(vehicleDD2);
						}
					}
					for (VehicleDD vehicleDD2 : lstAdd) {
						if (vehicleDD.getVehicle().getId() == vehicleDD2.getVehicle().getId()) {
							ObjectUtils.copyObjectSyn(vehicleDD, vehicleDD2);
							isNew = false;
							break;
						}
					}
					if (isNew) {
						lstTmp.add(vehicleDD);
					}
					MapCommon.MAP_LIST_VEHICLE_INFO.put(vehicleDD.getVehicle().getValue(), lstAdd);
				} else if (tmp != null && lstTmp == null) {
					MapCommon.MAP_VEHICLE_INFO.remove(vehicleDD.getVehicle().getValue());
					lstTmp = new ArrayList<>();
					lstTmp.add(tmp);
					lstTmp.add(vehicleDD);
					MapCommon.MAP_LIST_VEHICLE_INFO.put(vehicleDD.getVehicle().getValue(), lstTmp);
				}
			} else {
			}
		} catch (Exception e) {
			AppLogger.logDebug.error(MonitorCommon.getSystemCpuInfo(), e);
		}
	}

	@SuppressWarnings("unused")
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

	private List<VehicleDD> getListVehicle() {
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
					VehicleDD vehicleDD = new VehicleDD();
					Vehicle vehicle = new Vehicle();
					int vehicleId = rs.getInt("VehicleID");
					vehicle.setId(vehicleId);
					vehicle.setTaxiNumber(rs.getString("LicensePlate"));
					vehicle.setValue(rs.getString("VehicleNumber"));
					vehicle.setDeviceID(rs.getInt("DeviceID"));
					int taxiGroupId = rs.getInt("GroupID");
					TaxiGroup taxiGroup = MapCommon.MAP_TAXI_GROUP.get(taxiGroupId);
					vehicle.setTaxiGroup(taxiGroup);
					vehicle.setVehicleGroupID(taxiGroupId);
					Seat seat = new Seat();
					int seatId = rs.getInt("CommonID");
					seat.setId(seatId);
					seat.setName(rs.getString("CommonName"));
					seat.setType(rs.getString("CommonCodeType"));
					TaxiType taxiType = new TaxiType();
					taxiType.setId(rs.getInt("TypeID"));
					String vehicleType = rs.getString("TypeName");
					taxiType.setName(vehicleType);
					taxiType.setValue(rs.getString("TypeCode"));
					taxiType.setSeats(seat);
					vehicle.setTaxiType(taxiType);
					vehicle.setVehicleTypeID(taxiType.getId());
					Device device = new Device();
					device.setId(vehicle.getDeviceID());
					device.setDeltaSpeedInCut(rs.getInt("DeltaSpeedInCut"));
					device.setSpeedGpsStartInCut(rs.getInt("SpeedGpsStartInCut"));
					device.setSpeedGpsStopInCut(rs.getInt("SpeedGpsStopInCut"));
					device.setTimerInCut(rs.getInt("TimerInCut"));
					device.setDeltaSpeedStartInKick(rs.getInt("DeltaSpeedStartInKick"));
					device.setDeltaSpeedStopInKick(rs.getInt("DeltaSpeedStopInKick"));
					device.setRepeatInKick(rs.getInt("RepeatInKick"));
					device.setSpeedGpsStartInKick(rs.getInt("SpeedGpsStartInKick"));
					device.setSpeedMeterStopInKick(rs.getInt("SpeedMeterStopInKick"));
					device.setTimeStartInKick(rs.getInt("TimeStartInKick"));
					device.setTimeStopInKick(rs.getInt("TimeStopInKick"));
					vehicle.setDevice(device);
					vehicle.setMeterPingTime(rs.getTimestamp("MeterPingTime"));
					int seatNumber = 1;
					if (seatId == 66) {
						seatNumber = 2;
					} else {
						seatNumber = 1;
					}
					vehicleDD.setSeat(seatNumber);
					vehicleDD.setFullName(rs.getString("FullName"));
					vehicleDD.setVehicleType(vehicleType);
					vehicleDD.setVehicle(vehicle);
					vehicleDD.setVehicleId(vehicleId);
					lstResult.add(vehicleDD);
					this.updateMapVehicle(vehicleDD);
					this.updateMapVehicleDD(vehicleDD);
					this.updateListWarning(vehicleDD);
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

	private void updateListWarning(VehicleDD vehicledd) {
		VietekWarning vwarning = new VietekWarning(vehicledd.getVehicle());
		MapCommon.MAP_VIETEK_WARNING.put(vehicledd.getVehicleId(), vwarning);
	}
}
