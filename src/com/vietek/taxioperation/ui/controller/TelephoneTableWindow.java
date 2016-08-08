package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.controller.TelephoneExtensionTmsController;
import com.vietek.taxioperation.controller.TelephoneTableTmsController;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TelephoneExtensionTms;
import com.vietek.taxioperation.model.TelephoneTableTms;
import com.vietek.taxioperation.model.VoipCenter;
import com.vietek.taxioperation.util.CheckOnlineUtils;
import com.vietek.taxioperation.util.ControllerUtils;

public class TelephoneTableWindow extends Window implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3486837588909318752L;
	private Combobox cbbTelephoneTable;
	private TelephoneTableTms tableTms = null;
	private List<TelephoneExtensionTms> lstExtensions = new ArrayList<TelephoneExtensionTms>();
	private List<VoipCenter> lstVoipCenters = new ArrayList<>();
	private Div divContain;
	private SysUser user;

	TaxiOrdersForm taxiOrders;
	TelephoneTableWindow _this;
	Button btOk;

	public Combobox getCbbTelephoneTable() {
		return cbbTelephoneTable;
	}

	public void setCbbTelephoneTable(Combobox cbbTelephoneTable) {
		this.cbbTelephoneTable = cbbTelephoneTable;
	}

	public TelephoneTableTms getTelephoneTable() {
		return tableTms;
	}

	public void setTelephoneTable(TelephoneTableTms telephoneTable) {
		this.tableTms = telephoneTable;
	}

	public List<VoipCenter> getLstVoipCenters() {
		return lstVoipCenters;
	}

	public void setLstVoipCenters(List<VoipCenter> lstVoipCenters) {
		this.lstVoipCenters = lstVoipCenters;
	}

	public List<TelephoneExtensionTms> getLstExtensions() {
		return lstExtensions;
	}

	public void setLstExtensions(List<TelephoneExtensionTms> lstExtensions) {
		this.lstExtensions = lstExtensions;
	}

	public TelephoneTableWindow(final TaxiOrdersForm taxiOrders, SysUser user) {

		this.taxiOrders = taxiOrders;
		_this = this;
		this.user = user;

		initUI();

		lstVoipCenters = getSourceData_VoipCenter(user.getSysCompany());
		setValueForComboboTableTms();

		this.addEventListener(Events.ON_OK, this);
		this.addEventListener(Events.ON_CLOSE, this);
	}

	private void initUI() {

		this.setClosable(true);
		this.setPosition("center,center");
		this.setAction("show: slideDown;hide: slideUp");

		this.setVisible(false);
		divContain = new Div();
		divContain.setParent(this);
		divContain.setSclass("TelephoneTableWindow_Div");
		divContain.setWidth("100%");
		divContain.setHeight("100%");

		cbbTelephoneTable = new Combobox();
		cbbTelephoneTable.setParent(divContain);
		cbbTelephoneTable.setWidth("100%");
		cbbTelephoneTable.setAutodrop(true);
		cbbTelephoneTable.setAutocomplete(true);

		setTitle("CHỌN BÀN LÀM VIỆC");
		setWidth("300px");

		btOk = new Button("OK");
		btOk.setParent(divContain);
		btOk.setId("TelephoneTableWindow_BtOk");
		btOk.setSclass("TelephoneTableWindow_BtOk");
		btOk.setWidth("100%");
		btOk.setAutodisable("self");
		btOk.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {

				getExtension();
			}
		});
	}

	private void setValueForComboboTableTms() {

		List<TelephoneTableTms> lst = getSourceData_TableTms();
		for (TelephoneTableTms telephoneTableTms : lst) {

			Comboitem item = new Comboitem(telephoneTableTms.getName());
			item.setValue(telephoneTableTms);
			cbbTelephoneTable.getItems().add(item);
		}

		if (cbbTelephoneTable.getItemCount() > 0) {

			cbbTelephoneTable.setSelectedIndex(0);
			cbbTelephoneTable.open();
		}

	}

	private void getExtension() {

		if (cbbTelephoneTable.getSelectedItem() != null) {

			// Clients.showBusy(this, "Đang lấy Extension...");

			TelephoneTableTms tableDraft = null;
			tableTms = (TelephoneTableTms) cbbTelephoneTable.getSelectedItem().getValue();

			tableDraft = CheckOnlineUtils.checkTableTms_InUse(tableTms);
			if (tableDraft != null)
				Messagebox.show("Bàn này đang sử  dụng. Bạn có tiếp tục chọn không ?", "CHỌN BÀN",
						Messagebox.OK | Messagebox.CANCEL, Messagebox.ERROR, new org.zkoss.zk.ui.event.EventListener<Event>() {
							public void onEvent(Event e) {
								if (Messagebox.ON_OK.equals(e.getName())) {
									_this.detach();
									taxiOrders.setTableTms(tableTms);
									getExtensionsOnline_By_Table(tableTms);
								}
							}
						});
			else {
				_this.detach();
				taxiOrders.setTableTms(tableTms);
				getExtensionsOnline_By_Table(tableTms);
			}

		} else
			cbbTelephoneTable.setErrorMessage("Nhập Bàn điện thoại...");

	}

	public List<TelephoneExtensionTms> addTitle_Extension(TelephoneTableTms tableTms) {

		List<TelephoneExtensionTms> resVal = getExtensionsFromTable(tableTms);
		if (tableTms == null)
			return new ArrayList<>();

		lstExtensions = resVal;
		StringBuilder strLb = new StringBuilder(tableTms.getName());

		if (lstExtensions.size() > 0) {
			strLb.append(" [ ");
			for (TelephoneExtensionTms extensionEnable : lstExtensions) {

				strLb.append(extensionEnable.getExtension() + "-");
			}
			if (strLb.lastIndexOf("-") > -1) {
				strLb = strLb.deleteCharAt(strLb.lastIndexOf("-"));
				strLb.append(" ]");
			} else {
				strLb = new StringBuilder();
				strLb.append("[ NOT EXTENSION ]");
			}
		} else {

			strLb.append("[ NOT EXTENSION ]");
		}

		// Label lbTitle = taxiOrders.getLb_title();
		Component parent = taxiOrders.getLeftHeader();

		Label lbTitleTable = new Label(strLb.toString());
		lbTitleTable.setId("lbTitleTable");
		lbTitleTable.setSclass("lbTitleTable");

		Label lbTitleLst = new Label(); // " DANH SÁCH YÊU CẦU BÀN: "
		lbTitleLst.setId("lbTitleLst");
		lbTitleLst.setSclass("lbTitleLst");

		if (parent.getChildren() != null)
			parent.getChildren().clear();
		parent.appendChild(lbTitleLst);
		parent.appendChild(lbTitleTable);

		return resVal;
	}

	public void getExtensionsOnline_By_Table(TelephoneTableTms tableTms) {

		List<TelephoneExtensionTms> lstExtensionsTms = addTitle_Extension(tableTms);

		_this.taxiOrders.setExtensionsTms_ChannelTms_For_CheckOnlineUtils(lstExtensionsTms);
		

	}

	private List<TelephoneExtensionTms> getExtensionsFromTable(TelephoneTableTms tableTms) {

		/*
		 * get list chanel
		 */
		TelephoneExtensionTmsController controllerChanel = (TelephoneExtensionTmsController) ControllerUtils
				.getController(TelephoneExtensionTmsController.class);

		String sql = "from TelephoneExtensionTms where telephoneTable = ? ";
		List<TelephoneExtensionTms> lstExtensions = controllerChanel.find(sql, tableTms);
		this.lstExtensions = lstExtensions;

		return lstExtensions;
	}

	public void show() {
		this.setVisible(true);
		this.doModal();
	}

	private List<TelephoneTableTms> getSourceData_TableTms() {

		List<TelephoneTableTms> lstTableTms = new ArrayList<>();

		/*
		 * get list chanel by company
		 */
		TelephoneTableTmsController controllerChanel = (TelephoneTableTmsController) ControllerUtils
				.getController(TelephoneTableTmsController.class);

		String sql = "from TelephoneTableTms where sysCompany = ?";
		lstTableTms = controllerChanel.find(sql, user.getSysCompany());

		/*
		 * check exists in list online User
		 */
		List<TelephoneTableTms> lstTable_Enable = new ArrayList<TelephoneTableTms>();
		for (TelephoneTableTms telephoneTableTms : lstTableTms) {

			lstTable_Enable.add(telephoneTableTms);
		}

		return lstTable_Enable;
	}

	private List<VoipCenter> getSourceData_VoipCenter(SysCompany company) {

		List<VoipCenter> result = new ArrayList<>();

		if (company != null) {
			Set<VoipCenter> lstVoipCenter = company.getVoipCenter();
			result.addAll(lstVoipCenter);
		}

		return result;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {

		if (arg0.getName().equalsIgnoreCase(Events.ON_OK)) {

			getExtension();
		} else if (arg0.getName().equalsIgnoreCase(Events.ON_CLOSE)) {

		}

	}
}
