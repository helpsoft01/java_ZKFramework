package com.vietek.taxioperation.ui.controller;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;

import com.vietek.taxioperation.controller.TelephoneTableTmsController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.TelephoneTableTms;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;

public class TelephoneTableTmsDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TelephoneTableTmsDetail(AbstractModel model, AbstractWindowPanel listWindow) {

		super(model, listWindow);

	}

	@Override
	public void beforInit() {

		setTitle("BÀN ĐIỆN THOẠI VIÊN");
		setWidth("400px");
	}

	@Override
	public void createForm() {

		super.createForm();
	}

	@Override

	public void onEvent(Event event) throws Exception {
		// super.onEvent(event);

		if (event.getTarget().equals(getBtn_save()) || event.getName().equals(Events.ON_OK)) {

			String valCodeOfTable = "";
			SysCompany valOfCompany = null;

			Editor editorCode = this.getMapEditor().get("value");
			valCodeOfTable = editorCode.getValue().toString();

			Editor editorCompany = this.getMapEditor().get("sysCompany");
			valOfCompany = (SysCompany) editorCompany.getValue();

			TelephoneTableTmsController controller = (TelephoneTableTmsController) ControllerUtils
					.getController(TelephoneTableTmsController.class);
			String sql = "from TelephoneTableTms where sysCompany = ? and value=?";
			List<TelephoneTableTms> lstvalue = controller.find(sql, valOfCompany, valCodeOfTable);

			TelephoneTableTms obj = (TelephoneTableTms) getModel();

			if (lstvalue.size() == 0) {

				obj.save();

				getListWindow().refresh();
				this.setVisible(false);
			} else {
				Clients.showNotification(
						"Đã tồn tại mã bàn:" + editorCode.getValue() + " trong công ty:" + valOfCompany.getName(),
						Clients.NOTIFICATION_TYPE_INFO, editorCode.getComponent(), "Left", 3000, true);
			}

		} else if (event.getTarget().equals(getBtn_cancel())) {
			this.setVisible(false);
		}

	}

}
