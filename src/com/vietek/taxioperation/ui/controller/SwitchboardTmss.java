package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;

import com.vietek.taxioperation.controller.ChannelTmsController;
import com.vietek.taxioperation.controller.SwitchboardTMSController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.SwitchboardTMS;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class SwitchboardTmss extends AbstractWindowPanel implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SwitchboardTmssDetail detailWindow = null;
	private SwitchboardTMS modeltmp = null;

	public SwitchboardTmss() {
		super(true);
		this.setTitle("Danh mục tổng đài");
	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Mã tổng đài", 250, String.class, "getValue","value",getModelClass()));
		lstCol.add(new GridColumn("Tên tổng đài", 250, String.class, "getName","name",getModelClass()));
		lstCol.add(new GridColumn("Ghi chú", 400, String.class, "getNote"));
		setGridColumns(lstCol);
	}

	@Override
	public void loadData() {
		SwitchboardTMSController switchboardtmscontroller = (SwitchboardTMSController) ControllerUtils
				.getController(SwitchboardTMSController.class);
		List<SwitchboardTMS> lstValue = switchboardtmscontroller
				.find("from SwitchboardTMS");
		setLstModel(lstValue);
		setModelClass(SwitchboardTMS.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new SwitchboardTmssDetail(getCurrentModel(), this);

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
			modeltmp = (SwitchboardTMS) model;
			modeltmp = (SwitchboardTMS) createNewModel();
			showDetail(modeltmp);
		} else if (event.getTarget().equals(getBt_delete())) {

			AbstractModel model = super.getCurrentModel();
			modeltmp = (SwitchboardTMS) model;
			// Kiểm tra sự ràng buộc giữa kênh và tổng đài
			boolean exist = this.checkexistrelation(modeltmp.getId());

			if (exist) {
				Env.getHomePage()
						.showNotification(
								"Tổng đài đã được sử dụng. Cần xóa các kênh của tổng đài!",
								Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
		} else if (event.getName().equals(Events.ON_SELECT)
				&& event.getTarget().equals(getListbox())) {

			AbstractModel model = super.getCurrentModel();
			modeltmp = (SwitchboardTMS) model;

			modeltmp = (SwitchboardTMS) getListModel().getElementAt(
					getListbox().getSelectedIndex());
			getBt_delete().setDisabled(false);
			getBt_edit().setDisabled(false);
		} else if (((event.getName().equals(Events.ON_OK) || event.getName()
				.equals(Events.ON_DOUBLE_CLICK))
				&& event.getTarget().equals(getListbox()) || event.getTarget()
				.equals(getBt_edit()))) {
			if (getListbox().getSelectedIndex() >= 0) {

				AbstractModel model = super.getCurrentModel();
				modeltmp = (SwitchboardTMS) model;
				modeltmp = (SwitchboardTMS) getListModel().getElementAt(
						getListbox().getSelectedIndex());
				if (getCurrentModel() != null) {
					showDetail(getCurrentModel());
				}
			}
		}
		super.onEvent(event);
	}
	// Hàm kiểm tra sự tồn tại quan hệ ràng buộc giữa tổng đài và các kênh trong tổng đài
	private boolean checkexistrelation(int id) {
		ChannelTmsController controller = (ChannelTmsController) ControllerUtils
				.getController(ChannelTmsController.class);
		String sql = "from ChannelTms where switchboard_id = ?";
		List<ChannelTms> lstvalue = controller.find(sql, id);
		if (lstvalue == null || lstvalue.isEmpty()) {
			return false;
		}
		return true;
	}

}
