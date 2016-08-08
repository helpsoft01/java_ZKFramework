package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import com.vietek.taxioperation.controller.ChannelTmsController;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.SwitchboardTMS;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class ChannelTmss extends AbstractWindowPanel implements Serializable {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private ChannelTmssDetail detailWindow = null;

	public ChannelTmss() {
		super(true);
		this.setTitle("Danh mục kênh");
	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Mã kênh", 150, String.class, "getValue","value",getModelClass()));
		lstCol.add(new GridColumn("Tên kênh", 200, String.class, "getName","name",getModelClass()));
		lstCol.add(new GridColumn("Tổng đài", 200, SwitchboardTMS.class,
				"getSwitchboardtms","switchboardtms",getModelClass()));
		lstCol.add(new GridColumn("Kinh đỗ",200, Double.class, "getLongitude"));
		GridColumn gcl = new GridColumn("Vĩ độ",200, Double.class, "getLatitude");
		lstCol.add(gcl);
		lstCol.add(new GridColumn("Active",200, Boolean.class, "getIsActive","isActive",getModelClass()));
		setGridColumns(lstCol);
	}
  
	@Override
	public void loadData() {
		ChannelTmsController channeltmscontroller = (ChannelTmsController) ControllerUtils
				.getController(ChannelTmsController.class);
		List<ChannelTms> lstValue = channeltmscontroller
				.find("from ChannelTms");
		setLstModel(lstValue);
		setModelClass(ChannelTms.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new ChannelTmssDetail(getCurrentModel(), this);
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
