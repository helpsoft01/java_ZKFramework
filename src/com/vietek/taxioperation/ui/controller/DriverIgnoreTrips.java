package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.DriverIgnoreTrip;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * author Viet Ha Ca
 */
public class DriverIgnoreTrips extends AbstractWindowPanel implements
		Serializable {

	public DriverIgnoreTrips() {
		super(true);
		setTitle("Danh sách lái xe hủy cuốc");
		setDetailTitle("Chi tiết hủy cuốc");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DriverIgnoreTripDetail detailWindow = null;

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã", 100, int.class, "getId"));
		lstCols.add(new GridColumn("Lái xe", 300, Driver.class, "getDriver"));
		lstCols.add(new GridColumn("Phương tiện", 100, Vehicle.class,
				"getVehicle"));
		lstCols.add(new GridColumn("Thời gian", 200, Timestamp.class, "getTime"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		setModelClass(DriverIgnoreTrip.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new DriverIgnoreTripDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}
}
