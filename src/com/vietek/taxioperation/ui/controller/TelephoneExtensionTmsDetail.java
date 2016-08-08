package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.controller.SysCompanyController;
import com.vietek.taxioperation.controller.TelephoneExtensionTmsController;
import com.vietek.taxioperation.controller.TelephoneTableTmsController;
import com.vietek.taxioperation.controller.VoipCenterController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.TelephoneExtensionTms;
import com.vietek.taxioperation.model.TelephoneTableTms;
import com.vietek.taxioperation.model.VoipCenter;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;

public class TelephoneExtensionTmsDetail extends BasicDetailWindow {

	public TelephoneExtensionTmsDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7544478814071394009L;
	private int maxExtensionInTable = 0;

	@Override
	public void beforInit() {

		setWidth("400px");
		setTitle("Extension Number");
		super.beforInit();
	}

	@Override
	public void initUI() {
		super.initUI();

		if (maxExtensionInTable == 0)
			maxExtensionInTable = ConfigUtil.getValueConfig("MAX_EXTENSION_IN_TABLE",
					CommonDefine.MAX_EXTENSION_IN_TABLE);
		getBtn_save().setAutodisable("self");
	}

	private List<TelephoneExtensionTms> getExtensionByCompany(SysCompany company) {
		List<TelephoneExtensionTms> result = new ArrayList<>();

		/*
		 * get table
		 */
		TelephoneTableTmsController controllerTable = (TelephoneTableTmsController) ControllerUtils
				.getController(TelephoneTableTmsController.class);
		String sql = "from TelephoneTableTms where sysCompany=?";

		List<TelephoneTableTms> lstTable = controllerTable.find(sql, company);

		for (TelephoneTableTms tableTms : lstTable) {
			/*
			 * get lst Extension
			 */
			TelephoneExtensionTmsController controllerExtension = (TelephoneExtensionTmsController) ControllerUtils
					.getController(TelephoneExtensionTmsController.class);
			sql = "from TelephoneExtensionTms where telephoneTable=?";

			List<TelephoneExtensionTms> lstExtension = controllerExtension.find(sql, tableTms);
			result.addAll(lstExtension);
		}

		return result;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		// super.onEvent(event);

		if (event.getTarget().equals(getBtn_save()) || event.getName().equals(Events.ON_OK)) {

			Clients.showBusy(getBtn_save(), "Đang xử lý");

			String valCodeOfExtension = "";
			TelephoneTableTms valOfTable = null;
			SysCompany valOfCopany = null;

			Editor editorExtension = this.getMapEditor().get("extension");
			valCodeOfExtension = editorExtension.getValue().toString();

			Editor editorTable = this.getMapEditor().get("telephoneTable");
			valOfTable = (TelephoneTableTms) editorTable.getValue();
			valOfCopany = valOfTable.getSysCompany();

			/*
			 * get list voiperCenter
			 */
			SysCompanyController controller = (SysCompanyController) ControllerUtils
					.getController(SysCompanyController.class);
			String sql = "from SysCompany where id=?";

			List<SysCompany> lstCompany = controller.find(sql, valOfCopany.getId());

			if (lstCompany.size() > 0) {
				/*
				 * get list company
				 */
				Set<VoipCenter> voipCenter = (Set<VoipCenter>) lstCompany.get(0).getVoipCenter();
				VoipCenterController controllerVoip = (VoipCenterController) ControllerUtils
						.getController(VoipCenterController.class);
				sql = "from VoipCenter where id=?";

				List<VoipCenter> lstVoiper = controllerVoip.find(sql, voipCenter.iterator().next().getId());
				Set<SysCompany> sysCompany = lstVoiper.get(0).getSysCompany();

				List<TelephoneExtensionTms> lstExtension = new ArrayList<>();

				for (SysCompany itemCompany : sysCompany) {
					lstExtension.addAll(getExtensionByCompany(itemCompany));
				}

				/*
				 * check exists extension in Lst
				 */
				TelephoneExtensionTms currentModel = (TelephoneExtensionTms) getModel();
				for (TelephoneExtensionTms itemExtensionTms : lstExtension) {
					if (itemExtensionTms.getExtension().equals(valCodeOfExtension) && currentModel.getId() != 0
							&& itemExtensionTms.getId() != currentModel.getId()) {

						Clients.showNotification(
								"Bạn chọn 1 extension khác, vì :" + editorExtension.getValue() + " đã tồn tại !",
								Clients.NOTIFICATION_TYPE_INFO, editorExtension.getComponent(), "Left", 3000, true);
						Clients.clearBusy(getBtn_save());
						return;
					}
				}
				/*
				 * total extensions in table
				 */
				int count = 0;
				for (TelephoneExtensionTms itemExtensionTms : lstExtension) {
					if (itemExtensionTms.getTelephoneTable().getId() == valOfTable.getId()) {
						++count;
					}
				}
				if (maxExtensionInTable == 0)
					maxExtensionInTable = ConfigUtil.getValueConfig("MAX_EXTENSION_IN_TABLE",
							CommonDefine.MAX_EXTENSION_IN_TABLE);

				if (count >= maxExtensionInTable && currentModel.getId() == 0) {
					Clients.showNotification(
							"Tối đa được tạo " + maxExtensionInTable + " extension trong bàn, bạn chọn bàn khác:",
							Clients.NOTIFICATION_TYPE_INFO, editorExtension.getComponent(), "Left", 3000, true);
					Clients.clearBusy(getBtn_save());
					return;

				}
				/*
				 * save
				 */
				TelephoneExtensionTms obj = (TelephoneExtensionTms) getModel();
				obj.save();

				getListWindow().refresh();
				this.setVisible(false);

				Clients.clearBusy(getBtn_save());
			}
		} else if (event.getTarget().equals(getBtn_cancel())) {
			this.setVisible(false);
		}

	}
}
