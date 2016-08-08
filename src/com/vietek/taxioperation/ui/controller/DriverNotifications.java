package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.model.DriverNotification;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * author Viet Ha Ca
 */
public class DriverNotifications extends AbstractWindowPanel implements Serializable {

	public DriverNotifications() {
		super(true);
		this.setTitle("Thông báo cho lái xe");
	}

	private DriverNotificationDetail detailWindow = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void initLeftPanel() {
		
	}

	@Override
	public void initColumns() {
		// TODO Auto-generated method stub
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã", 100, String.class, "getId"));
		lstCols.add(new GridColumn("Nội dung", 600, String.class, "getMessage"));
		lstCols.add(new GridColumn("Lái xe", 300, String.class, "getDrivers"));
		lstCols.add(new GridColumn("Kích hoạt", 100, String.class,
				"getIsActive"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		setModelClass(DriverNotification.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new DriverNotificationDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}
}
