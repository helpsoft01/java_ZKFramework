package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import com.vietek.taxioperation.controller.DriverGCMInfoController;
import com.vietek.taxioperation.model.DriverGCMInfo;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class DriverGCMInfos extends AbstractWindowPanel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DriverGCMInfosDetail detailWindow = null;

	public DriverGCMInfos() {
		super(true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lst = new ArrayList<GridColumn>();
		lst.add(new GridColumn("Tài xế", 250, String.class, ""));
		lst.add(new GridColumn("Reg Id",250, String.class, ""));
		setGridColumns(lst);
		
	}

	@Override
	public void loadData() {
		DriverGCMInfoController controller = (DriverGCMInfoController) ControllerUtils.getController(DriverGCMInfoController.class);
		List<DriverGCMInfo> list = controller.find("from DriverGCMInfo");
		setLstModel(list);
		setModelClass(DriverGCMInfo.class);
	}
	
	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new DriverGCMInfosDetail(getCurrentModel(), this);
		}
		return detailWindow;

	}
	@Override
	public void onEvent(Event event) throws Exception {
	
			try {
				super.onEvent(event);
			} catch (Exception e) {
				Messagebox.show(e.getMessage());
			}
	}
}
