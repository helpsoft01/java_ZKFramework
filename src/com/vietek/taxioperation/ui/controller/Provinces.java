package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

import com.vietek.taxioperation.controller.ProvinceController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.National;
import com.vietek.taxioperation.model.Province;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class Provinces extends AbstractWindowPanel implements Serializable {
	
	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Province modeltmp = null;
	private ProvinceDetail detailWindow = null;
	public Provinces() {
		super(true);
		this.setTitle("Danh mục tỉnh thành");
	}
	@Override
	public void initLeftPanel() {
	}
	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Mã tỉnh thành", 150, String.class, "getValue","value",getModelClass()));
		lstCol.add(new GridColumn("Tên tỉnh thành", 200, String.class, "getProvinceName","ProvinceName",getModelClass()));
		lstCol.add(new GridColumn("Tên quốc gia", 200, National.class, "getNational","national",getModelClass()));
		lstCol.add(new GridColumn("Loại", 150, String.class, "getType","type",getModelClass()));
		setGridColumns(lstCol);
	}

	@Override
	public void loadData() {
		ProvinceController controller = (ProvinceController) ControllerUtils.getController(ProvinceController.class);
		List<Province> lstValue = controller.find("from Province");
		setLstModel(lstValue);
		setModelClass(Province.class);

	}
	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new ProvinceDetail(getCurrentModel(), this);
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
			modeltmp = (Province) model;
			modeltmp = (Province) createNewModel();
			showDetail(modeltmp);
		} else if (event.getName().equals(Events.ON_SELECT)
				&& event.getTarget().equals(getListbox())) {
			AbstractModel model = super.getCurrentModel();
			modeltmp = (Province) model;
			modeltmp = (Province) getListModel().getElementAt(
					getListbox().getSelectedIndex());
			getBt_delete().setDisabled(false);
			getBt_edit().setDisabled(false);
		} else if ((event.getName().equals(Events.ON_OK) || event.getName()
				.equals(Events.ON_DOUBLE_CLICK))
				&& event.getTarget().equals(getListbox())
				|| event.getTarget().equals(getBt_edit())) {
			if (getListbox().getSelectedIndex() >= 0) {
				AbstractModel model = super.getCurrentModel();
				modeltmp = (Province) model;
				modeltmp = (Province) getListModel().getElementAt(
						getListbox().getSelectedIndex());
				if (getCurrentModel() != null) {
					showDetail(getCurrentModel());
				}
			}
		}
		super.onEvent(event);
	
	}

}
