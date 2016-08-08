package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.zkoss.zk.ui.util.Clients;

import com.vietek.taxioperation.model.ParkingArea;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class Vehicles extends AbstractWindowPanel implements Serializable {

	public Vehicles() {
		super(true);
		setTitle("Quản lý phương tiện");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VehiclesDetail detailWindow = null;

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Số tài", 150, String.class, "getValue",
				"value", getModelClass()));
		lstCols.add(new GridColumn("Biển kiểm soát", 200, String.class,
				"getTaxiNumber", "taxiNumber", getModelClass()));
		lstCols.add(new GridColumn("Thiết bị", 150, String.class, "getDevice",
				"device", getModelClass()));
		lstCols.add(new GridColumn("Loại xe", 300, String.class, "getTaxiType",
				"taxiType", getModelClass()));
		lstCols.add(new GridColumn("Đội xe", 300, String.class, "getTaxiGroup",
				"taxiGroup", getModelClass()));
		lstCols.add(new GridColumn("Bãi xe", 200, ParkingArea.class,
				"getParking", "Parking", getModelClass()));
		lstCols.add(new GridColumn("Giới hạn tốc đọ", 200, Integer.class,
				"getSpeedLimit"));
		lstCols.add(new GridColumn("Xung/km", 200, Integer.class,
				"getPulsePerKm"));
		lstCols.add(new GridColumn("Active", 200, Boolean.class,
				"getIsActive"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		setModelClass(Vehicle.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new VehiclesDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

	@Override
	protected String prepareQuerySearch() {
		String Result = super.prepareQuerySearch();
		Result = Result.replace("Parking.id", "ParkingID");
		Result = Result.replace("device.id", "DeviceID");
	    Result = Result.replace("taxiGroup.id","VehicleGroupID");
	    Result = Result.replace("taxiType.id","VehicleTypeID");
	    
		return Result;
	}

	@Override
	public boolean handleEventDelete() {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		java.sql.CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sessionImplementor.connection();
			if (conn == null) {
				return false;
			}
			Vehicle modeltmp = (Vehicle) currentModel;
			cs = conn.prepareCall("call txm_tracking.lst_vehicle_Delete( ? )");
			cs.setInt(1, modeltmp.getId());
			int lrs = cs.executeUpdate();
			if (lrs > 0) {
				refresh();
				Env.getHomePage().showNotification("Đã xoá bản ghi !",
						Clients.NOTIFICATION_TYPE_INFO);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}

	}
}
