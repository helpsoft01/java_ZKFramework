package com.vietek.taxioperation.common.timer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.model.VehicleStatusDD;
import com.vietek.taxioperation.util.ControllerUtils;

/**
 *
 * @author VuD
 */
public class LoadVehicleStatusDDProcessor extends Thread {

	public LoadVehicleStatusDDProcessor() {
		super();
		this.setName("LoadVehicleStatusDDTimerTask");
	}

	@Override
	public void run() {
		try {
			List<VehicleRegistedOld> lstVehicleOle = this.getVehicleRegistedOld();
			for (VehicleRegistedOld vehicleRegistedOld : lstVehicleOle) {
				VehicleStatusDD vsd = (VehicleStatusDD) MapCommon.MAP_VEHICLE_STATUS_ID.get(vehicleRegistedOld.getVehicleId() + "");
				if (vsd == null) {
					vsd = new VehicleStatusDD();
					vsd.setVehicleId(vehicleRegistedOld.getVehicleId());
					vsd.setLastRegisted(vehicleRegistedOld.getTimeOrder().getTime());
					vsd.setFree(false);
					MapCommon.MAP_VEHICLE_STATUS_ID.put(vehicleRegistedOld.vehicleId + "", vsd);
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("LoadVehicleStatusDDProcessor", e);
		}finally {
			
		}
	}

	private List<VehicleRegistedOld> getVehicleRegistedOld() {
		List<VehicleRegistedOld> lstResult = new ArrayList<>();
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sip = (SessionImplementor) session;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sip.connection();
			if (conn != null) {
				cs = conn.prepareCall("{call getVehicleRegistedOld}");
				rs = cs.executeQuery();
				while (rs.next()) {
					VehicleRegistedOld vro = new VehicleRegistedOld();
					vro.setVehicleId(rs.getInt("VehicleID"));
					vro.setTimeOrder(rs.getTimestamp("beginOrderTime"));
					lstResult.add(vro);
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

	private class VehicleRegistedOld {
		private int vehicleId;
		private Date timeOrder;

		public Date getTimeOrder() {
			return timeOrder;
		}

		public void setTimeOrder(Date timeOrder) {
			this.timeOrder = timeOrder;
		}

		public int getVehicleId() {
			return vehicleId;
		}

		public void setVehicleId(int vehicleId) {
			this.vehicleId = vehicleId;
		}

	}
}
