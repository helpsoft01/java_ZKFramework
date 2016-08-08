package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.DistrictTmsController;
import com.vietek.taxioperation.model.DistrictTms;
import com.vietek.taxioperation.model.Province;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

/**
 * 
 * @author VuD
 * 
 */

public class DistrictTmss extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private DistrictTms modeltmp = null;
	private DistrictTmsDetail detailWindow = null;

	public DistrictTmss() {
		super(true);
	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã quận/huyện", 100, String.class, "getValue","value",getModelClass()));
		lstCols.add(new GridColumn("Tên quận/huyện", 200, String.class, "getName","name", getModelClass()));
		lstCols.add(new GridColumn("Loại", 200, String.class, "getType","type",getModelClass()));
		lstCols.add(new GridColumn("Tỉnh/Thành phố", 200, Province.class, "getProvince","province",getModelClass()));
		lstCols.add(new GridColumn("Vị trí", 200, String.class, "getLocation"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		DistrictTmsController controller = (DistrictTmsController) ControllerUtils
				.getController(DistrictTmsController.class);
		List<DistrictTms> lstModel = controller.find("from DistrictTms");
		this.setModelClass(DistrictTms.class);
		this.setLstModel(lstModel);
	}
	
	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new DistrictTmsDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

//	@Override
//	public void onEvent(Event event) throws Exception {
//		if (event.getTarget().equals(getBt_refresh())) {
//			if (event.getName().equals(Events.ON_CLICK)) {
//				this.refresh();
//			}
//		}
//
//		if (event.getTarget().equals(getBt_add())) {
//			AbstractModel model = super.getCurrentModel();
//			modeltmp = (DistrictTms) model;
//			modeltmp = (DistrictTms) createNewModel();
//			showDetail(modeltmp);
//		} else if (event.getName().equals(Events.ON_SELECT)
//				&& event.getTarget().equals(getListbox())) {
//			AbstractModel model = super.getCurrentModel();
//			modeltmp = (DistrictTms) model;
//			modeltmp = (DistrictTms) getListModel().getElementAt(
//					getListbox().getSelectedIndex());
//			getBt_delete().setDisabled(false);
//			getBt_edit().setDisabled(false);
//		} else if ((event.getName().equals(Events.ON_OK) || event.getName()
//				.equals(Events.ON_DOUBLE_CLICK))
//				&& event.getTarget().equals(getListbox())
//				|| event.getTarget().equals(getBt_edit())) {
//			if (getListbox().getSelectedIndex() >= 0) {
//				AbstractModel model = super.getCurrentModel();
//				modeltmp = (DistrictTms) model;
//				modeltmp = (DistrictTms) getListModel().getElementAt(
//						getListbox().getSelectedIndex());
//				if (getCurrentModel() != null) {
//					showDetail(getCurrentModel());
//				}
//			}
//		}
//		super.onEvent(event);
//	}

}
