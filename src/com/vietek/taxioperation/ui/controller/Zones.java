package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

import com.vietek.taxioperation.controller.ZoneController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Region;
import com.vietek.taxioperation.model.Zone;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class Zones extends AbstractWindowPanel implements Serializable {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Zone modeltmp = null;
	private ZoneDetail detailWindow = null;

	public Zones() {
		super(true);
		this.setTitle("Danh mục khu vực");
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Mã khu vực", 250, String.class,
				"getValue","value",getModelClass()));
		lstCol.add(new GridColumn("Tên khu vực", 250, String.class,
				"getZoneName","ZoneName",getModelClass()));
		lstCol.add(new GridColumn("Vùng miền", 300, Region.class, "getRegion","region",getModelClass()));
		setGridColumns(lstCol);
	}

	@Override
	public void loadData() {
		ZoneController zonecontroller = (ZoneController) ControllerUtils
				.getController(ZoneController.class);
		List<Zone> lstvalue = zonecontroller.find("from Zone");
		setLstModel(lstvalue);
		setModelClass(Zone.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new ZoneDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(getBt_refresh())) {
			if (event.getName().equals(Events.ON_CLICK)) {
				this.refresh();
			}
		}

		if (event.getTarget().equals(getBt_add())) {
			AbstractModel model = super.getCurrentModel();
			modeltmp = (Zone) model;
			modeltmp = (Zone) createNewModel();
			showDetail(modeltmp);
		} else if (event.getName().equals(Events.ON_SELECT)
				&& event.getTarget().equals(getListbox())) {
			AbstractModel model = super.getCurrentModel();
			modeltmp = (Zone) model;
			modeltmp = (Zone) getListModel().getElementAt(
					getListbox().getSelectedIndex());
			getBt_delete().setDisabled(false);
			getBt_edit().setDisabled(false);
		} else if ((event.getName().equals(Events.ON_OK) || event.getName()
				.equals(Events.ON_DOUBLE_CLICK))
				&& event.getTarget().equals(getListbox())
				|| event.getTarget().equals(getBt_edit())) {
			if (getListbox().getSelectedIndex() >= 0) {
				AbstractModel model = super.getCurrentModel();
				modeltmp = (Zone) model;
				modeltmp = (Zone) getListModel().getElementAt(
						getListbox().getSelectedIndex());
				if (getCurrentModel() != null) {
					showDetail(getCurrentModel());
				}
			}
		}
		super.onEvent(event);
	}
}
