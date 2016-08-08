package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.common.VehicleApi;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.util.Env;
import com.vietek.trackingOnline.common.AbstractWarning;
import com.vietek.trackingOnline.common.CutSignalPulseVehicle;
import com.vietek.trackingOnline.common.KickSignalPulseVehicle;
import com.vietek.trackingOnline.common.RowWarningReport;
import com.vietek.trackingOnline.common.TrackingCommon;
import com.vietek.trackingOnline.common.VietekWarning;
import com.vietek.trackingOnline.tracker.TrackingRDS2Json;

public class ListTotalWarning extends Div implements Serializable, EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Hlayout hlMain;
	private Label lbAll;
	private Label lbGSM;
	private Label lbSOS;
	private Label lbCutSignal;
	private Label lbKickSignal;
	private Label lbLostworking;
	private Label lbVuotToc;

	private Div divEAll;
	private Div divELostGsm;
	private Div divESos;
	private Div divECutSignal;
	private Div divEKickSignal;
	private Div divELostworking;
	private Div divEVuottoc;

	private Listbox listBoxAll;
	private Listbox listBoxLostGsm;
	private Listbox listBoxSOS;
	private Listbox listBoxCutSignal;
	private Listbox listBoxKickSignal;
	private Listbox listBoxLostWorking;
	private Listbox listBoxVuotToc;

	private Tab tabAll;
	private Tab tabLostGsm;
	private Tab tabSOS;
	private Tab tabCutSignal;
	private Tab tabKickSignal;
	private Tab tabLostConnect;
	private Tab tabVuotToc;

	private Textbox tbVehicleNumber;
	private Textbox tbLicensePlate;
	private Textbox tbStartTime;
	private Textbox tbStopTime;

	private Textbox tbVehicleNumberLostGSM;
	private Textbox tbLicensePlateLostGSM;
	private Textbox tbStartTimeLostGSM;
	private Textbox tbStopTimeLostGSM;

	private Textbox tbVehicleNumberVuotToc;
	private Textbox tbLicensePlateVuotToc;
	private Textbox tbVanToc;
	private Textbox tbStartTimeVuotToc;
	private Textbox tbStopTimeVuotToc;

	private List<SysCompany> lstSysCompany;
	private List<Vehicle> listVehicle;
	private List<TrackingRDS2Json> listTracking;
	private List<TrackingRDS2Json> listTrackingVuotToc;
	private List<TrackingRDS2Json> listTrackingLostGSM;
	private List<RowWarningReport> listTrackingLostKick;
	private List<RowWarningReport> listTrackingLostCut;
	private List<TrackingRDS2Json> listTrackingOld = new ArrayList<TrackingRDS2Json>();
	private List<TrackingRDS2Json> lstSearchGrid;
	private int selectedTab;

	public ListTotalWarning() {
		this.setHeight("100%");
		Div div = new Div();
		div.setParent(this);
		div.setStyle("margin-top:10px;");
		initUI(div);
		Timer timer = new Timer();
		timer.setParent(this);
		timer.setDelay(30000);
		timer.setRepeats(true);
		timer.addEventListener(Events.ON_TIMER, new EventListener<Event>() {
			@Override
			public void onEvent(Event paramT) throws Exception {
				if (listVehicle == null || listVehicle.size() <= 0) {
					listVehicle = getListVehicleBySysCompany();
					if (listVehicle != null && listVehicle.size() > 0) {
						lbAll.setValue(listVehicle.size() + "");
					}
				}
				if (listTracking != null && listTracking.size() > 0) {
					listTrackingOld.clear();
					listTrackingOld.addAll(listTracking);
				}
				listTracking = TrackingCommon.getListTrackingByListVehicle(listVehicle);
				resetValueSearch();
				updateListWarning(listTracking, listVehicle);
				updateWarningVietek(listVehicle);
			}
		});
		timer.start();
	}

	protected void updateWarningVietek(List<Vehicle> listVehicle2) {
		listTrackingLostCut = new ArrayList<>();
		listTrackingLostKick = new ArrayList<>();
		for (Vehicle vehicle : listVehicle2) {
			VietekWarning vwarning = MapCommon.MAP_VIETEK_WARNING.get(vehicle.getId());
			if (vwarning != null) {
				for (AbstractWarning warning : vwarning.getListWarning()) {
					if (warning instanceof CutSignalPulseVehicle) {
						RowWarningReport data = warning.getData();
						if (data != null) {
							listTrackingLostCut.add(data);
						}
					} else if (warning instanceof KickSignalPulseVehicle) {
						RowWarningReport data = warning.getData();
						if (data != null) {
							listTrackingLostKick.add(data);
						}
					}
				}
			}
		}
		listBoxCutSignal.setModel(new ListModelList<>(listTrackingLostCut));
		lbCutSignal.setValue(listTrackingLostCut.size() + "");
		listBoxKickSignal.setModel(new ListModelList<>(listTrackingLostKick));
		lbKickSignal.setValue(listTrackingLostKick.size() + "");

	}

	private void updateListWarning(List<TrackingRDS2Json> listTracking, List<Vehicle> lstVehicle) {
		if (listTracking != null && listTracking.size() > 0 && listVehicle != null && listVehicle.size() > 0) {
			listTrackingVuotToc = new ArrayList<TrackingRDS2Json>();
			listTrackingLostGSM = new ArrayList<TrackingRDS2Json>();
			for (TrackingRDS2Json tracking : listTracking) {
				// Vuot toc
				for (Vehicle vehicle : listVehicle) {
					if (vehicle.getDeviceID() == tracking.getDeviceId()
							&& vehicle.getSpeedLimit() + 5 < tracking.getGpsSpeed()) {
						listTrackingVuotToc.add(tracking);
					}
				}
				// Mat tin hieu : time bản tin + 15 min > now
				if (tracking.getTimeLog() != null
						&& tracking.getTimeLog().getTime() + 15 * 60 * 1000 < System.currentTimeMillis()) {
					listTrackingLostGSM.add(tracking);
				}
			}
			if (listTrackingVuotToc != null && listTrackingVuotToc.size() > 0) {
				listBoxVuotToc.setModel(new ListModelList<TrackingRDS2Json>(listTrackingVuotToc));
				lbVuotToc.setValue(listTrackingVuotToc.size() + "");
			} else {
				lbVuotToc.setValue("0");
			}

			if (listTrackingLostGSM != null && listTrackingLostGSM.size() > 0) {
				listBoxLostGsm.setModel(new ListModelList<TrackingRDS2Json>(listTrackingLostGSM));
				lbGSM.setValue(listTrackingLostGSM.size() + "");
			} else {
				lbGSM.setValue("0");
			}

		}
	}

	@SuppressWarnings("unchecked")
	private List<Vehicle> getListVehicleBySysCompany() {
		// sysCompany = Env.getCompany();
		lstSysCompany = (List<SysCompany>) Env.getContext(Env.LIST_COMPANY);
		if (lstSysCompany != null && lstSysCompany.size() > 0) {
			SysCompany[] arr = lstSysCompany.toArray(new SysCompany[lstSysCompany.size()]);
			listVehicle = VehicleApi.getVehicle(arr);
		}
		return listVehicle;
	}
    
	private void initUI(Component parent) {
		hlMain = new Hlayout();
		hlMain.setParent(parent);
		Tabbox tabox = new Tabbox();
		tabox.setStyle("margin-top:10px;");
		tabox.setParent(parent);
		Tabs tabs = new Tabs();
		tabs.setParent(tabox);
		Tabpanels tabpanels = new Tabpanels();
		tabpanels.setParent(tabox);

		// UI all
		Div divAll = new Div();
		divAll.setStyle("margin-left:50px");
		divAll.setParent(hlMain);
		initUIAll(divAll);
		initGridAll(tabs, tabpanels);

		// UI lost GSM
		Div divGsm = new Div();
		divGsm.setStyle("margin-left:50px");
		divGsm.setParent(hlMain);
		initUILostGSM(divGsm);
		initGridLostGSM(tabs, tabpanels);

		// UI SOS
		Div divSOS = new Div();
		divSOS.setStyle("margin-left:50px");
		divSOS.setParent(hlMain);
		initUISOS(divSOS);
		initGridSOS(tabs, tabpanels);

		// UI cat xung
		Div divXung = new Div();
		divXung.setStyle("margin-left:50px");
		divXung.setParent(hlMain);
		initUICutSignal(divXung);
		initGridCutSignal(tabs, tabpanels);

		// UI kick xung
		Div divKickXung = new Div();
		divKickXung.setStyle("margin-left:50px");
		divKickXung.setParent(hlMain);
		initUIKickSignal(divKickXung);
		initGridKickSignal(tabs, tabpanels);

		// UI Lost working
		Div divLostworking = new Div();
		divLostworking.setStyle("margin-left:50px");
		divLostworking.setParent(hlMain);
		initUILostConnect(divLostworking);
		initGridLostWorking(tabs, tabpanels);

		// UI vuot toc
		Div divVuotToc = new Div();
		divVuotToc.setStyle("margin-left:50px");
		divVuotToc.setParent(hlMain);
		initUIVuotToc(divVuotToc);
		initGridVuotToc(tabs, tabpanels);
	}

	private void renderGrid(Tabpanel tabpanel, Listbox lstBox, Textbox tb01, Textbox tb02, Textbox tb03, Textbox tb04,
			ListitemRenderer<?> render) {
		lstBox.setSizedByContent(true);
		lstBox.setEmptyMessage("Không có dữ liệu");
		lstBox.setSizedByContent(true);
		lstBox.setSpan(true);
		lstBox.setMold("paging");
		lstBox.setAutopaging(true);
		lstBox.setPagingPosition("both");
		lstBox.addEventListener("onPaging", this);
		lstBox.setHeight("420px");
		lstBox.setParent(tabpanel);
		Listhead head = new Listhead();
		head.setParent(lstBox);
		head.setSizable(true);
		Listheader header = new Listheader("TT");
		header.setWidth("50px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Số tài");
		header.setWidth("70px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("BKS");
		header.setWidth("150px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Thời gian bắt đầu");
		header.setWidth("150px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Thời gian kết thúc");
		header.setWidth("150px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Xử lý");
		header.setWidth("120px");
		header.setAlign("center");
		head.appendChild(header);

		Auxhead auxs = new Auxhead();
		auxs.setParent(lstBox);
		Auxheader aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		tb01 = new Textbox();
		tb01.setSclass("z-textbox-search");
		tb01.setAttribute("datafield", "");
		tb01.setPlaceholder("Số tài");
		tb01.setParent(aux);
		tb01.addEventListener(Events.ON_OK, this);
		tb01.addForward(Events.ON_CHANGE, tb01, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		tb02 = new Textbox();
		tb02.setSclass("z-textbox-search");
		tb02.setAttribute("datafield", "");
		tb02.setPlaceholder("BKS");
		tb02.setParent(aux);
		tb02.addEventListener(Events.ON_OK, this);
		tb02.addForward(Events.ON_CHANGE, tb02, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		tb03 = new Textbox();
		tb03.setSclass("z-textbox-search");
		tb03.setAttribute("datafield", "");
		tb03.setPlaceholder("Bắt đầu");
		tb03.setParent(aux);
		tb03.addEventListener(Events.ON_OK, this);
		tb03.addForward(Events.ON_CHANGE, tb03, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		tb04 = new Textbox();
		tb04.setSclass("z-textbox-search");
		tb04.setAttribute("datafield", "");
		tb04.setPlaceholder("Kết thúc");
		tb04.setParent(aux);
		tb04.addEventListener(Events.ON_OK, this);
		tb04.addForward(Events.ON_CHANGE, tb04, Events.ON_OK);
		auxs.appendChild(aux);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		if (render == null) {
			lstBox.setItemRenderer(new ListitemRenderer<TrackingRDS2Json>() {
				@Override
				public void render(Listitem items, TrackingRDS2Json data, int index) throws Exception {
					items.setValue(data);
					Label lb = new Label();
					lb.setValue(index + 1 + "");
					Listcell listcell = new Listcell();
					listcell.appendChild(lb);
					listcell.setParent(items);
					lb = new Label();
					lb.setValue(data.getVehicleNumber());
					listcell = new Listcell();
					listcell.setStyle("text-align: center");
					listcell.appendChild(lb);
					listcell.setParent(items);
					lb = new Label();
					lb.setValue(data.getLicensePlate());
					listcell = new Listcell();
					listcell.setStyle("text-align: center");
					listcell.appendChild(lb);
					listcell.setParent(items);
					lb = new Label();
					lb.setValue(data.getTimeLog() + "");
					listcell = new Listcell();
					listcell.setStyle("text-align: center");
					listcell.appendChild(lb);
					listcell.setParent(items);
					Toolbarbutton btnViewHistory = new Toolbarbutton();
					btnViewHistory.setImage("./themes/images/searchadd.png");
					btnViewHistory.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
						@Override
						public void onEvent(Event arg0) throws Exception {
							Clients.showNotification("Xử lý", "error", null, "bottom_center", 3000, true);
						}
					});
				}

			});
		}
	}

	private void initGridVuotToc(Tabs tabs, Tabpanels tabpanels) {
		tabVuotToc = new Tab("Vượt tốc");
		tabVuotToc.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setVflex("1");
		tabpanel.setParent(tabpanels);
		// renderGrid(tabpanel,listBoxVuotToc, tbVehicleNumber, tbLicensePlate,
		// tbStartTime, tbStopTime);
		listBoxVuotToc = new Listbox();
		listBoxVuotToc.setSizedByContent(true);
		listBoxVuotToc.setEmptyMessage("Không có dữ liệu");
		listBoxVuotToc.setSizedByContent(true);
		listBoxVuotToc.setSpan(true);
		listBoxVuotToc.setMold("paging");
		listBoxVuotToc.setAutopaging(true);
		listBoxVuotToc.setPagingPosition("both");
		listBoxVuotToc.addEventListener("onPaging", this);
		listBoxVuotToc.setHeight("420px");
		listBoxVuotToc.setParent(tabpanel);
		Listhead head = new Listhead();
		head.setParent(listBoxVuotToc);
		head.setSizable(true);
		Listheader header = new Listheader("TT");
		header.setWidth("50px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Số tài");
		header.setWidth("70px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("BKS");
		header.setWidth("150px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Vận tốc");
		header.setWidth("150px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Thời gian bắt đầu");
		header.setWidth("150px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Thời gian kết thúc");
		header.setWidth("150px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Xử lý");
		header.setWidth("120px");
		header.setAlign("center");
		head.appendChild(header);

		Auxhead auxs = new Auxhead();
		auxs.setParent(listBoxVuotToc);
		Auxheader aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		tbVehicleNumberVuotToc = new Textbox();
		tbVehicleNumberVuotToc.setSclass("z-textbox-search");
		tbVehicleNumberVuotToc.setAttribute("datafield", "");
		tbVehicleNumberVuotToc.setPlaceholder("Số tài");
		tbVehicleNumberVuotToc.setParent(aux);
		tbVehicleNumberVuotToc.addEventListener(Events.ON_OK, this);
		tbVehicleNumberVuotToc.addForward(Events.ON_CHANGE, tbVehicleNumberVuotToc, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		tbLicensePlateVuotToc = new Textbox();
		tbLicensePlateVuotToc.setSclass("z-textbox-search");
		tbLicensePlateVuotToc.setAttribute("datafield", "");
		tbLicensePlateVuotToc.setPlaceholder("BKS");
		tbLicensePlateVuotToc.setParent(aux);
		tbLicensePlateVuotToc.addEventListener(Events.ON_OK, this);
		tbLicensePlateVuotToc.addForward(Events.ON_CHANGE, tbLicensePlateVuotToc, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		tbVanToc = new Textbox();
		tbVanToc.setSclass("z-textbox-search");
		tbVanToc.setAttribute("datafield", "");
		tbVanToc.setPlaceholder("Vận tốc");
		tbVanToc.setParent(aux);
		tbVanToc.addEventListener(Events.ON_OK, this);
		tbVanToc.addForward(Events.ON_CHANGE, tbVanToc, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		tbStartTimeVuotToc = new Textbox();
		tbStartTimeVuotToc.setSclass("z-textbox-search");
		tbStartTimeVuotToc.setAttribute("datafield", "");
		tbStartTimeVuotToc.setPlaceholder("Bắt đầu");
		tbStartTimeVuotToc.setParent(aux);
		tbStartTimeVuotToc.addEventListener(Events.ON_OK, this);
		tbStartTimeVuotToc.addForward(Events.ON_CHANGE, tbStartTimeVuotToc, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		tbStopTimeVuotToc = new Textbox();
		tbStopTimeVuotToc.setSclass("z-textbox-search");
		tbStopTimeVuotToc.setAttribute("datafield", "");
		tbStopTimeVuotToc.setPlaceholder("Kết thúc");
		tbStopTimeVuotToc.setParent(aux);
		tbStopTimeVuotToc.addEventListener(Events.ON_OK, this);
		tbStopTimeVuotToc.addForward(Events.ON_CHANGE, tbStopTimeVuotToc, Events.ON_OK);
		auxs.appendChild(aux);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		listBoxVuotToc.setItemRenderer(new ListitemRenderer<TrackingRDS2Json>() {
			@Override
			public void render(Listitem items, TrackingRDS2Json data, int index) throws Exception {
				items.setValue(data);
				Label lb = new Label();
				lb.setValue(index + 1 + "");
				Listcell listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getVehicleNumber());
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getLicensePlate());
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getGpsSpeed() + "");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getTimeLog() + "");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue("");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				Toolbarbutton btnXuly = new Toolbarbutton();
				btnXuly.setImage("./themes/images/searchadd.png");
				btnXuly.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						Clients.showNotification("Xử lý", "error", null, "bottom_center", 3000, true);
					}
				});
				listcell = new Listcell();
				listcell.appendChild(btnXuly);
				listcell.setParent(items);
			}

		});
	}

	private void initGridLostWorking(Tabs tabs, Tabpanels tabpanels) {
		tabLostConnect = new Tab("Giờ làm việc");
		tabLostConnect.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setVflex("1");
		tabpanel.setParent(tabpanels);
		listBoxLostWorking = new Listbox();
		renderGrid(tabpanel, listBoxLostWorking, tbVehicleNumber, tbLicensePlate, tbStartTime, tbStopTime, null);
	}

	private void initGridKickSignal(Tabs tabs, Tabpanels tabpanels) {
		tabKickSignal = new Tab("Kích xung");
		tabKickSignal.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setVflex("1");
		tabpanel.setParent(tabpanels);
		ListitemRenderer<RowWarningReport> render = new ListitemRenderer<RowWarningReport>() {
			@Override
			public void render(Listitem items, RowWarningReport data, int index) throws Exception {
				items.setValue(data);
				Label lb = new Label();
				lb.setValue(index + 1 + "");
				Listcell listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getVehiclenumber());
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getLicensePlate());
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getBegintime() + "");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue("");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				Toolbarbutton btnXuly = new Toolbarbutton();
				btnXuly.setImage("./themes/images/searchadd.png");
				btnXuly.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						Clients.showNotification("Xử lý", "error", null, "bottom_center", 3000, true);
					}
				});
				listcell = new Listcell();
				listcell.appendChild(btnXuly);
				listcell.setParent(items);

			}
		};
		listBoxKickSignal = new Listbox();
		renderGrid(tabpanel, listBoxKickSignal, tbVehicleNumber, tbLicensePlate, tbStartTime, tbStopTime, render);
	}

	private void initGridCutSignal(Tabs tabs, Tabpanels tabpanels) {
		tabCutSignal = new Tab("Cắt xung");
		tabCutSignal.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setVflex("1");
		tabpanel.setParent(tabpanels);
		ListitemRenderer<RowWarningReport> render = new ListitemRenderer<RowWarningReport>() {
			@Override
			public void render(Listitem items, RowWarningReport data, int index) throws Exception {

				items.setValue(data);
				Label lb = new Label();
				lb.setValue(index + 1 + "");
				Listcell listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getVehiclenumber());
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getLicensePlate());
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getBegintime() + "");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue("");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				Toolbarbutton btnXuly = new Toolbarbutton();
				btnXuly.setImage("./themes/images/searchadd.png");
				btnXuly.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						Clients.showNotification("Xử lý", "error", null, "bottom_center", 3000, true);
					}
				});
				listcell = new Listcell();
				listcell.appendChild(btnXuly);
				listcell.setParent(items);

			}
		};
		listBoxCutSignal = new Listbox();
		renderGrid(tabpanel, listBoxCutSignal, tbVehicleNumber, tbLicensePlate, tbStartTime, tbStopTime, render);
	}

	private void initGridSOS(Tabs tabs, Tabpanels tabpanels) {
		tabSOS = new Tab("SOS");
		tabSOS.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setVflex("1");
		tabpanel.setParent(tabpanels);
		listBoxSOS = new Listbox();
		renderGrid(tabpanel, listBoxSOS, tbVehicleNumber, tbLicensePlate, tbStartTime, tbStopTime, null);
	}

	private void initGridLostGSM(Tabs tabs, Tabpanels tabpanels) {
		tabLostGsm = new Tab("Mất GSM");
		tabLostGsm.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setVflex("1");
		tabpanel.setParent(tabpanels);
		// renderGrid(tabpanel,listBoxLostGsm, tbVehicleNumber, tbLicensePlate,
		// tbStartTime, tbStopTime);

		listBoxLostGsm = new Listbox();
		listBoxLostGsm.setSizedByContent(true);
		listBoxLostGsm.setEmptyMessage("Không có dữ liệu");
		listBoxLostGsm.setSizedByContent(true);
		listBoxLostGsm.setSpan(true);
		listBoxLostGsm.setMold("paging");
		listBoxLostGsm.setAutopaging(true);
		listBoxLostGsm.setPagingPosition("both");
		listBoxLostGsm.addEventListener("onPaging", this);
		listBoxLostGsm.setHeight("420px");
		listBoxLostGsm.setParent(tabpanel);
		Listhead head = new Listhead();
		head.setParent(listBoxLostGsm);
		head.setSizable(true);
		Listheader header = new Listheader("TT");
		header.setWidth("50px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Số tài");
		header.setWidth("70px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("BKS");
		header.setWidth("150px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Thời gian bắt đầu");
		header.setWidth("150px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Thời gian kết thúc");
		header.setWidth("150px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Xử lý");
		header.setWidth("120px");
		header.setAlign("center");
		head.appendChild(header);

		Auxhead auxs = new Auxhead();
		auxs.setParent(listBoxLostGsm);
		Auxheader aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		tbVehicleNumberLostGSM = new Textbox();
		tbVehicleNumberLostGSM.setSclass("z-textbox-search");
		tbVehicleNumberLostGSM.setAttribute("datafield", "");
		tbVehicleNumberLostGSM.setPlaceholder("Số tài");
		tbVehicleNumberLostGSM.setParent(aux);
		tbVehicleNumberLostGSM.addEventListener(Events.ON_OK, this);
		tbVehicleNumberLostGSM.addForward(Events.ON_CHANGE, tbVehicleNumberLostGSM, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		tbLicensePlateLostGSM = new Textbox();
		tbLicensePlateLostGSM.setSclass("z-textbox-search");
		tbLicensePlateLostGSM.setAttribute("datafield", "");
		tbLicensePlateLostGSM.setPlaceholder("BKS");
		tbLicensePlateLostGSM.setParent(aux);
		tbLicensePlateLostGSM.addEventListener(Events.ON_OK, this);
		tbLicensePlateLostGSM.addForward(Events.ON_CHANGE, tbLicensePlateLostGSM, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		tbStartTimeLostGSM = new Textbox();
		tbStartTimeLostGSM.setSclass("z-textbox-search");
		tbStartTimeLostGSM.setAttribute("datafield", "");
		tbStartTimeLostGSM.setPlaceholder("Bắt đầu");
		tbStartTimeLostGSM.setParent(aux);
		tbStartTimeLostGSM.addEventListener(Events.ON_OK, this);
		tbStartTimeLostGSM.addForward(Events.ON_CHANGE, tbStartTimeLostGSM, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		tbStopTimeLostGSM = new Textbox();
		tbStopTimeLostGSM.setSclass("z-textbox-search");
		tbStopTimeLostGSM.setAttribute("datafield", "");
		tbStopTimeLostGSM.setPlaceholder("Kết thúc");
		tbStopTimeLostGSM.setParent(aux);
		tbStopTimeLostGSM.addEventListener(Events.ON_OK, this);
		tbStopTimeLostGSM.addForward(Events.ON_CHANGE, tbStopTimeLostGSM, Events.ON_OK);
		auxs.appendChild(aux);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		listBoxLostGsm.setItemRenderer(new ListitemRenderer<TrackingRDS2Json>() {
			@Override
			public void render(Listitem items, TrackingRDS2Json data, int index) throws Exception {
				items.setValue(data);
				Label lb = new Label();
				lb.setValue(index + 1 + "");
				Listcell listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getVehicleNumber());
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getLicensePlate());
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getTimeLog() + "");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue("");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				Toolbarbutton btnXuly = new Toolbarbutton();
				btnXuly.setImage("./themes/images/searchadd.png");
				btnXuly.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						Clients.showNotification("Xử lý", "error", null, "bottom_center", 3000, true);
					}
				});
				listcell = new Listcell();
				listcell.appendChild(btnXuly);
				listcell.setParent(items);
			}

		});

	}

	private void initGridAll(Tabs tabs, Tabpanels tabpanels) {
		tabAll = new Tab("Tất cả");
		tabAll.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setVflex("1");
		tabpanel.setParent(tabpanels);
		listBoxAll = new Listbox();
		listBoxAll.setSizedByContent(true);
		listBoxAll.setEmptyMessage("Không có dữ liệu");
		listBoxAll.setSizedByContent(true);
		listBoxAll.setSpan(true);
		listBoxAll.setMold("paging");
		listBoxAll.setAutopaging(true);
		listBoxAll.setPagingPosition("both");
		listBoxAll.addEventListener("onPaging", this);
		listBoxAll.setHeight("420px");
		listBoxAll.setParent(tabpanel);

	}

	private void initUIAll(Component parent) {
		Vlayout vlout = new Vlayout();
		vlout.setParent(parent);
		divEAll = new Div();
		divEAll.setSclass("img_statitis_order");
		divEAll.setParent(parent);
		divEAll.addEventListener(Events.ON_CLICK, this);
		lbAll = new Label();
		lbAll.setSclass("text_main_lb_statitis_order");
		lbAll.setParent(divEAll);
		Label lb = new Label("Tất cả");
		lb.setParent(vlout);
		lb.setStyle("margin-left:15px");
		lb.setStyle("font-size: 14px;font-weight: bold;");
	}

	private void initUIVuotToc(Component parent) {
		Vlayout vlout = new Vlayout();
		vlout.setParent(parent);
		divEVuottoc = new Div();
		divEVuottoc.setSclass("img_statitis_order");
		divEVuottoc.setParent(parent);
		divEVuottoc.addEventListener(Events.ON_CLICK, this);
		lbVuotToc = new Label();
		lbVuotToc.setSclass("text_main_lb_statitis_order");
		lbVuotToc.setParent(divEVuottoc);
		Label lb = new Label("Vượt tốc");
		lb.setParent(vlout);
		lb.setStyle("margin-left:15px");
		lb.setStyle("font-size: 14px;font-weight: bold;");
	}

	private void initUILostConnect(Component parent) {
		Vlayout vlout = new Vlayout();
		vlout.setParent(parent);
		divELostworking = new Div();
		divELostworking.setSclass("img_statitis_order");
		divELostworking.setParent(parent);
		divELostworking.addEventListener(Events.ON_CLICK, this);
		lbLostworking = new Label();
		lbLostworking.setSclass("text_main_lb_statitis_order");
		lbLostworking.setParent(divELostworking);
		Label lb = new Label("Giờ làm việc");
		lb.setParent(vlout);
		lb.setStyle("margin-left:15px");
		lb.setStyle("font-size: 14px;font-weight: bold;");
	}

	private void initUIKickSignal(Component parent) {
		Vlayout vlout = new Vlayout();
		vlout.setParent(parent);
		divEKickSignal = new Div();
		divEKickSignal.setSclass("img_statitis_order");
		divEKickSignal.setParent(parent);
		divEKickSignal.addEventListener(Events.ON_CLICK, this);
		lbKickSignal = new Label();
		lbKickSignal.setSclass("text_main_lb_statitis_order");
		lbKickSignal.setParent(divEKickSignal);
		Label lb = new Label("Kích xung");
		lb.setParent(vlout);
		lb.setStyle("margin-left:15px");
		lb.setStyle("font-size: 14px;font-weight: bold;");
	}

	private void initUICutSignal(Component parent) {
		Vlayout vlout = new Vlayout();
		vlout.setParent(parent);
		divECutSignal = new Div();
		divECutSignal.setSclass("img_statitis_order");
		divECutSignal.setParent(parent);
		divECutSignal.addEventListener(Events.ON_CLICK, this);
		lbCutSignal = new Label();
		lbCutSignal.setSclass("text_main_lb_statitis_order");
		lbCutSignal.setParent(divECutSignal);
		Label lb = new Label("Cắt xung");
		lb.setParent(vlout);
		lb.setStyle("margin-left:15px");
		lb.setStyle("font-size: 14px;font-weight: bold;");
	}

	private void initUISOS(Component parent) {
		Vlayout vlout = new Vlayout();
		vlout.setParent(parent);
		divESos = new Div();
		divESos.setSclass("img_statitis_order");
		divESos.setParent(parent);
		divESos.addEventListener(Events.ON_CLICK, this);
		lbSOS = new Label();
		lbSOS.setSclass("text_main_lb_statitis_order");
		lbSOS.setParent(divESos);
		Label lb = new Label("SOS");
		lb.setParent(vlout);
		lb.setStyle("margin-left:15px");
		lb.setStyle("font-size: 14px;font-weight: bold;");
	}

	private void initUILostGSM(Component parent) {
		Vlayout vlout = new Vlayout();
		vlout.setParent(parent);
		divELostGsm = new Div();
		divELostGsm.setSclass("img_statitis_order");
		divELostGsm.setParent(parent);
		divELostGsm.addEventListener(Events.ON_CLICK, this);
		lbGSM = new Label();// Tong so cuoc
		lbGSM.setSclass("text_main_lb_statitis_order");
		lbGSM.setParent(divELostGsm);
		Label lb = new Label("Mất GSM");
		lb.setParent(vlout);
		lb.setStyle("font-size: 14px;font-weight: bold;");
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget().equals(divEAll)) {
				tabAll.setSelected(true);
				lstSearchGrid = listTracking;
				selectedTab = 1;
			} else if (event.getTarget().equals(divELostGsm)) {
				tabLostGsm.setSelected(true);
				lstSearchGrid = listTrackingLostGSM;
				selectedTab = 2;
			} else if (event.getTarget().equals(divESos)) {
				tabSOS.setSelected(true);
				lstSearchGrid = listTrackingLostGSM;
				selectedTab = 2;
			} else if (event.getTarget().equals(divECutSignal)) {
				tabCutSignal.setSelected(true);
				lstSearchGrid = listTrackingLostGSM;
				selectedTab = 2;
			} else if (event.getTarget().equals(divEKickSignal)) {
				tabKickSignal.setSelected(true);
				lstSearchGrid = listTrackingLostGSM;
				selectedTab = 2;
			} else if (event.getTarget().equals(divELostworking)) {
				tabLostConnect.setSelected(true);
				lstSearchGrid = listTrackingLostGSM;
				selectedTab = 2;
			} else if (event.getTarget().equals(divEVuottoc)) {
				tabVuotToc.setSelected(true);
				lstSearchGrid = listTrackingVuotToc;
				selectedTab = 8;
			}
		}
		// Lost GSM
		if (event.getTarget().equals(tbVehicleNumberLostGSM)) {
			tbVehicleNumberLostGSM.setAttribute("datafield", tbVehicleNumberLostGSM.getValue());
			executeSearchLostGSM();
		} else if (event.getTarget().equals(tbLicensePlateLostGSM)) {
			tbLicensePlateLostGSM.setAttribute("datafield", tbLicensePlateLostGSM.getValue());
			executeSearchLostGSM();
		} else if (event.getTarget().equals(tbStartTimeLostGSM)) {
			tbStartTimeLostGSM.setAttribute("datafield", tbStartTimeLostGSM.getValue());
			executeSearchLostGSM();
		} else if (event.getTarget().equals(tbStopTimeLostGSM)) {
			tbStopTimeLostGSM.setAttribute("datafield", tbStopTimeLostGSM.getValue());
			executeSearchLostGSM();
		}

		// Vuot Toc
		if (event.getTarget().equals(tbVehicleNumberVuotToc)) {
			tbVehicleNumberVuotToc.setAttribute("datafield", tbVehicleNumberVuotToc.getValue());
			executeSearchVuotToc();
		} else if (event.getTarget().equals(tbLicensePlateVuotToc)) {
			tbLicensePlateVuotToc.setAttribute("datafield", tbLicensePlateVuotToc.getValue());
			executeSearchVuotToc();
		} else if (event.getTarget().equals(tbVanToc)) {
			tbVanToc.setAttribute("datafield", tbVanToc.getValue());
			executeSearchVuotToc();
		} else if (event.getTarget().equals(tbStartTimeVuotToc)) {
			tbStartTimeVuotToc.setAttribute("datafield", tbStartTimeVuotToc.getValue());
			executeSearchVuotToc();
		} else if (event.getTarget().equals(tbStopTimeVuotToc)) {
			tbStopTimeVuotToc.setAttribute("datafield", tbStopTimeVuotToc.getValue());
			executeSearchVuotToc();
		}
	}

	private void resetValueSearch() {
		tbVehicleNumberVuotToc.setValue("");
		tbLicensePlateVuotToc.setValue("");
		tbVanToc.setValue("");
		tbStartTimeVuotToc.setValue("");
		tbStopTimeVuotToc.setValue("");

		tbVehicleNumberLostGSM.setValue("");
		tbLicensePlateLostGSM.setValue("");
		tbStartTimeLostGSM.setValue("");
		tbStopTimeLostGSM.setValue("");
	}

	private void executeSearchLostGSM() {
		Predicate pre = new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				TrackingRDS2Json mar = (TrackingRDS2Json) arg0;
				String vehicleNumberValue = (String) tbVehicleNumberLostGSM.getAttribute("datafield");
				String licensePlateValue = (String) tbLicensePlateLostGSM.getAttribute("datafield");
				String startTimeValue = (String) tbStartTimeLostGSM.getAttribute("datafield");
				// String stopTimeValue = (String)
				// tbStopTimeLostGSM.getAttribute("datafield");
				// vehicle number
				boolean check1 = true;
				if ((mar.getVehicleNumber() == null && !vehicleNumberValue.equals(""))
						|| (mar.getVehicleNumber() != null
								&& !mar.getVehicleNumber().toUpperCase().contains(vehicleNumberValue.toUpperCase()))) {
					check1 = false;
				}
				// license plate
				boolean check2 = true;
				if ((mar.getLicensePlate() == null && !licensePlateValue.equals("")) || (mar.getLicensePlate() != null
						&& !mar.getLicensePlate().toUpperCase().contains(licensePlateValue.toUpperCase()))) {
					check2 = false;
				}
				// start time
				boolean check3 = true;
				if ((mar.getTimeLog() == null && !startTimeValue.equals("")) || (mar.getLicensePlate() != null
						&& !mar.getTimeLog().toString().toUpperCase().contains(startTimeValue.toUpperCase()))) {
					check3 = false;
				}
				/*
				 * // stop time boolean check4 = true; if((mar.getTimeLog() ==
				 * null && !startTimeValue.equals("")) || (mar.getLicensePlate()
				 * != null &&
				 * !mar.getTimeLog().toString().toUpperCase().contains(
				 * startTimeValue.toUpperCase()))){ check4 = false; }
				 */
				if (check1 && check2 && check3) {
					return true;
				}

				return false;
			}
		};
		@SuppressWarnings("unchecked")
		Collection<TrackingRDS2Json> coll = CollectionUtils.select(lstSearchGrid, pre);
		List<TrackingRDS2Json> lsttmp = new ArrayList<TrackingRDS2Json>(coll);
		renderListBox(selectedTab, lsttmp);
	}

	private void executeSearchVuotToc() {
		Predicate pre = new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				TrackingRDS2Json mar = (TrackingRDS2Json) arg0;
				String vehicleNumberValue = (String) tbVehicleNumberVuotToc.getAttribute("datafield");
				String licensePlateValue = (String) tbLicensePlateVuotToc.getAttribute("datafield");
				String vantocValue = (String) tbVanToc.getAttribute("datafield");
				String startTimeValue = (String) tbStartTimeVuotToc.getAttribute("datafield");
				// String stopTimeValue = (String)
				// tbStopTimeVuotToc.getAttribute("datafield");
				// vehicle number
				boolean check1 = true;
				if ((mar.getVehicleNumber() == null && !vehicleNumberValue.equals(""))
						|| (mar.getVehicleNumber() != null
								&& !mar.getVehicleNumber().toUpperCase().contains(vehicleNumberValue.toUpperCase()))) {
					check1 = false;
				}
				// license plate
				boolean check2 = true;
				if ((mar.getLicensePlate() == null && !licensePlateValue.equals("")) || (mar.getLicensePlate() != null
						&& !mar.getLicensePlate().toUpperCase().contains(licensePlateValue.toUpperCase()))) {
					check2 = false;
				}
				// van toc
				boolean check3 = true;
				if ((String.valueOf(mar.getGpsSpeed()) == null && !vantocValue.equals(""))
						|| (String.valueOf(mar.getGpsSpeed()) != null && !String.valueOf(mar.getGpsSpeed())
								.toUpperCase().contains(vantocValue.toUpperCase()))) {
					check3 = false;
				}
				// start time
				boolean check4 = true;
				if ((mar.getTimeLog() == null && !startTimeValue.equals("")) || (mar.getLicensePlate() != null
						&& !mar.getTimeLog().toString().toUpperCase().contains(startTimeValue.toUpperCase()))) {
					check4 = false;
				}
				/*
				 * // stop time boolean check4 = true; if((mar.getTimeLog() ==
				 * null && !startTimeValue.equals("")) || (mar.getLicensePlate()
				 * != null &&
				 * !mar.getTimeLog().toString().toUpperCase().contains(
				 * startTimeValue.toUpperCase()))){ check4 = false; }
				 */
				if (check1 && check2 && check3 && check4) {
					return true;
				}

				return false;
			}
		};
		@SuppressWarnings("unchecked")
		Collection<TrackingRDS2Json> coll = CollectionUtils.select(lstSearchGrid, pre);
		List<TrackingRDS2Json> lsttmp = new ArrayList<TrackingRDS2Json>(coll);
		renderListBox(selectedTab, lsttmp);
	}

	private void renderListBox(int tab, List<TrackingRDS2Json> lstModel) {
		switch (tab) {
		case 1:
			break;
		case 2:
			listBoxLostGsm.setModel(new ListModelList<TrackingRDS2Json>(lstModel));
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			listBoxVuotToc.setModel(new ListModelList<TrackingRDS2Json>(lstModel));
			break;
		default:
		}

	}

}
