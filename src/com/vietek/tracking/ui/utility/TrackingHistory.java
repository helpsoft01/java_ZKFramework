package com.vietek.tracking.ui.utility;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Slider;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.West;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.database.MongoAction;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.controller.vmap.LatLngBounds;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvent;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvents;
import com.vietek.taxioperation.ui.controller.vmap.VMaps;
import com.vietek.taxioperation.ui.util.ComboboxSearch;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.DateUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.MapUtils;
import com.vietek.tracking.ui.model.GenaralHistoryWorker;
import com.vietek.tracking.ui.model.GenaralTab;
import com.vietek.tracking.ui.model.GpsTrackingMsg;
import com.vietek.tracking.ui.model.ListboxTab;
import com.vietek.tracking.ui.model.ListBoxTabUnit;
import com.vietek.tracking.ui.model.VehicleMarker;

public class TrackingHistory extends Div implements EventListener<Event> {

	/**
	 * @author hung
	 * @category Vẽ lại hành trình cho xe
	 */
	private static final long serialVersionUID = 1L;

	private VMaps vmaphistory;
	private Datebox fromdate;
	private Datebox todate;
	private ComboboxSearch cbgroupvehicle;
	private ComboboxSearch cbvehicle;
	private Button btnXem;
	private Slider slide;
	private Toolbarbutton btnchay;
	private Toolbarbutton btndung;
	private Toolbarbutton btnlui;
	private Toolbarbutton btntien;
	private Toolbarbutton btnexportexcel;
	private Div divcenter;
	private Listbox listboxhistory;
	private Panel paneldetail;
	private Timer timerun;
	private Borderlayout bddetail;
	private Checkbox xemcungbando;
	private Checkbox hienHanhtrinh;
	private GenaralTab genaralTab;
	private ListboxTab listHistoryTab;
	final Desktop desktop = Executions.getCurrent().getDesktop();

	public TrackingHistory() {
		initUI();
	}

	/**
	 * Hàm khởi tạo vẽ lại hành trình cho các báo cáo
	 * 
	 * @param FromDate
	 *            (Date)
	 * @param ToDate
	 *            (Date)
	 * @param VehicleID
	 *            (int)
	 */
	@SuppressWarnings("unchecked")
	public TrackingHistory(Date fromdate, Date todate, int vehicleid) {
		initUI();
		this.fromdate.setValue(fromdate);
		this.todate.setValue(todate);
		String sql = "From Vehicle where id=" + vehicleid;
		Session session = ControllerUtils.getCurrentSession();
		Query query;
		List<Vehicle> modeltmp = null;
		query = session.createQuery(sql);
		try {
			modeltmp = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (modeltmp != null && modeltmp.size() > 0) {
			cbgroupvehicle.setValue(modeltmp.get(0).getTaxiGroup());
			cbvehicle.setValue(modeltmp.get(0));
			Events.sendEvent(Events.ON_CLICK, btnXem, modeltmp.get(0).getDeviceID());
		}

	}

	private void initUI() {
		this.setHeight("100%");
		Borderlayout borderlayout = new Borderlayout();
		borderlayout.setParent(this);
		West west = new West();
		west.setSize("300px");
		west.setCollapsible(true);
		west.setParent(borderlayout);
		Center center = new Center();
		Clients.showBusy(center, "dadadsadsd");
		center.setParent(borderlayout);
		creatFrmLeft(west);
		creatFrmRight(center, west);
		genaralTab.addChildrenLink(listHistoryTab);
		listHistoryTab.addChildrenLink(genaralTab);
	}

	private void creatFrmRight(Component parent, West west) {
		divcenter = new Div();
		divcenter.setStyle("width:100%; height: 100%");
		divcenter.setParent(parent);
		vmaphistory = new VMaps(divcenter);
		vmaphistory.setSclass("z-gmap");
		vmaphistory.setWidth("100%");
		vmaphistory.setHeight("100%");
		vmaphistory.setCenter(new LatLng(20.971715711074314, 465.83903365796465));
		vmaphistory.setZoom(15);
		vmaphistory.addEventListener(VMapEvents.ON_VMARKER_CLICK, LISTENER_GMAP);
		Div divrightdetail = new Div();
		divrightdetail.setParent(divcenter);
		divrightdetail.setSclass("z-div-right-detail");
		divrightdetail.setZindex(1);
		Image imgback = new Image("./themes/images/back_in.png");
		imgback.setStyle("cursor: pointer;");
		imgback.setParent(divrightdetail);
		imgback.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (west.isOpen()) {
					west.setOpen(false);
					imgback.setSrc("./themes/images/back_out.png");
				} else {
					west.setOpen(true);
					imgback.setSrc("./themes/images/back_in.png");
				}
			}

		});
		west.addEventListener(Events.ON_OPEN, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (west.isOpen()) {
					imgback.setSrc("./themes/images/back_in.png");
				} else {
					imgback.setSrc("./themes/images/back_out.png");
				}
			}
		});
		creatFrmRightDetail(divrightdetail);

	}

	private void creatFrmRightDetail(Component parent) {
		paneldetail = new Panel();
		paneldetail.setParent(parent);
		paneldetail.setTitle("Chi tiết hành trình");
		paneldetail.setCollapsible(true);
		paneldetail.setOpen(false);
		paneldetail.setSclass("z-panel-layout-history");
		Panelchildren panelch = new Panelchildren();
		panelch.setParent(paneldetail);
		slide = new Slider();
		slide.setSclass("z-slide-history");
		slide.setMold("sphere");
		slide.setMinpos(1);
		slide.setCurpos(40);
		slide.setParent(panelch);
		slide.addEventListener(Events.ON_SCROLL, this);
		Toolbar toolbar = new Toolbar();
		toolbar.setParent(panelch);
		toolbar.setSclass("z-toolbar-history");

		Hbox hb = new Hbox();
		hb.setParent(toolbar);
		Div divcontrol = new Div();
		divcontrol.setSclass("z-div-control-history");
		divcontrol.setParent(hb);
		btnlui = new Toolbarbutton();
		btnlui.setImage("./themes/images/backward.png");
		btnlui.setParent(divcontrol);
		btnlui.addEventListener(Events.ON_CLICK, this);
		btnchay = new Toolbarbutton();
		btnchay.setTooltip("Chạy");
		btnchay.setImage("./themes/images/play.png");
		btnchay.setParent(divcontrol);
		btnchay.setParent(divcontrol);
		timerun = new Timer();
		timerun.setParent(this);
		timerun.setDelay(300);
		timerun.setRepeats(true);
		timerun.addEventListener(Events.ON_TIMER, LISTENER_TIMER_RUN);
		timerun.stop();
		btndung = new Toolbarbutton();
		btnchay.addEventListener(Events.ON_CLICK, this);
		btndung.setTooltip("Dừng");
		btndung.setImage("./themes/images/pause.png");
		btndung.setParent(divcontrol);
		btndung.addEventListener(Events.ON_CLICK, this);
		btntien = new Toolbarbutton();
		btntien.setImage("./themes/images/forward.png");
		btntien.setParent(divcontrol);
		btntien.addEventListener(Events.ON_CLICK, this);
		btnexportexcel = new Toolbarbutton();
		btnexportexcel.setTooltip("Xuất Excel");
		btnexportexcel.setImage("./themes/images/excelex.png");
		btnexportexcel.setParent(hb);
		btnexportexcel.setStyle("float:right;margin-left: 125px;");
		btnexportexcel.addEventListener(Events.ON_CLICK, this);
		listHistoryTab = new ListboxTab();
		listHistoryTab.setParent(panelch);
		listHistoryTab.setDesktop(desktop);
	}

	private void creatFrmLeft(Component parent) {
		Div div = new Div();
		div.setStyle("width:100%; height:100%");
		div.setParent(parent);
		Panel panel = new Panel();
		panel.setSclass("z-history-panel panel panel-noborder panel-noheader panel-noframe");
		panel.setParent(div);
		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);
		creatToolbar(panelchildren, parent);
		Grid grid = new Grid();
		grid.setParent(panelchildren);
		Columns cols = new Columns();
		cols.setParent(grid);
		Column col = new Column();
		col.setWidth("20%");
		col.setParent(cols);
		col = new Column();
		col.setWidth("80%");
		col.setParent(cols);
		Rows rows = new Rows();
		rows.setParent(grid);
		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Từ:"));
		fromdate = new Datebox();
		fromdate.setSclass("z-history-input");
		fromdate.setFormat("dd/MM/yyyy HH:mm");
		fromdate.setTimeZone("GMT+07");

		row.appendChild(fromdate);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Đến:"));
		todate = new Datebox();
		todate.setSclass("z-history-input");
		todate.setFormat("dd/MM/yyyy HH:mm");
		todate.setTimeZone("GMT+07");
		row.appendChild(todate);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Đội xe:"));
		cbgroupvehicle = new ComboboxSearch(TaxiGroup.class, "From TaxiGroup");
		cbgroupvehicle.setButtonVisible(true);
		cbgroupvehicle.setPlaceholder("Chọn đội xe");
		cbgroupvehicle.setSclass("z-combobox-history");
		cbgroupvehicle.setAttribute("modelid", 0);
		cbgroupvehicle.addEventListener(Events.ON_SELECT, LISTENER_COMBOBOX);
		row.appendChild(cbgroupvehicle);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Xe:"));
		cbvehicle = new ComboboxSearch();
		cbvehicle.setPlaceholder("Chọn xe");
		cbvehicle.setSclass("z-combobox-history");
		cbvehicle.setButtonVisible(true);
		row.appendChild(cbvehicle);

		row = new Row();
		row.setParent(rows);
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);
		xemcungbando = new Checkbox("Xem trên một bản đồ");
		xemcungbando.setStyle("Margin-left:25px");
		xemcungbando.addEventListener(Events.ON_CHECK, LISTENER_CHECKBOX);
		xemcungbando.setAttribute("listHeader", new ArrayList<Listheader>());
		cell.appendChild(xemcungbando);

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);

		hienHanhtrinh = new Checkbox("Hiện hành trình");
		hienHanhtrinh.setChecked(true);
		hienHanhtrinh.setStyle("Margin-left:25px");
		hienHanhtrinh.addEventListener(Events.ON_CHECK, LISTENER_CHECKBOX);
		hienHanhtrinh.setAttribute("listHeader", new ArrayList<Listheader>());
		cell.appendChild(hienHanhtrinh);

		Toolbar tb = new Toolbar();
		tb.setParent(panelchildren);
		tb.setAlign("center");
		btnXem = new Button("Xem");
		btnXem.setSclass("btn-default btn z-btn-history");
		btnXem.addEventListener(Events.ON_CLICK, LISTENER_BTNXEM);
		tb.appendChild(btnXem);
		initTime();
		genaralTab = new GenaralTab();
		genaralTab.setSclass("z-history-panel panel panel-noborder panel-noheader panel-noframe");
		genaralTab.setVflex("1");
		genaralTab.setWidth("295px");
		genaralTab.setParent(div);
	}

	private void creatToolbar(Panelchildren panelchildren, Component parent) {
		Toolbar toolbar = new Toolbar();
		toolbar.appendChild(new Label("Điều kiện Tìm kiếm"));
		toolbar.setSclass("z-toolbar-history");
		toolbar.setParent(panelchildren);
		Div div = new Div();
		div.setParent(toolbar);
		div.setStyle("float:right;margin-left:85px;");
		Toolbarbutton btnRefresh = new Toolbarbutton();
		btnRefresh.setImage("./themes/images/refresh_history_24.png");
		btnRefresh.addEventListener(Events.ON_CLICK, LISTENER_REFRESH);
		div.appendChild(btnRefresh);
	}

	private void initTime() {
		fromdate.setValue(DateUtils.getDateNow());
		todate.setValue(new Timestamp(System.currentTimeMillis()));
	}

	private void loadDataToCombobox(ComboboxSearch compb, String sql) {
		Session session = ControllerUtils.getCurrentSession();
		Query query;
		query = session.createQuery(sql);
		try {
			@SuppressWarnings("unchecked")
			List<AbstractModel> modeltmp = query.list();
			compb.setLstmodel(modeltmp);
			;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Override
	public void onEvent(Event event) throws Exception {
		if ((event.getTarget().equals(btnchay) || event.getTarget().equals(btndung) || event.getTarget().equals(btnlui)
				|| event.getTarget().equals(btntien)) && event.getName().equals(Events.ON_CLICK)) {
			handlerEventControl(event);
		} else if (event.getTarget().equals(slide)) {
			setSpeedRun();
		} else if (event.getTarget().equals(btnexportexcel)) {
			Tab tab = listHistoryTab.getSelectedTab();
			if (tab != null) {
				ListBoxTabUnit listunit = tab.getValue();
				CommonUtils.exportListboxToExcel(listunit.generatedListBoxExcel(),
						"History_" + tab.getLabel() + ".xlsx");
			} else {
				Env.getHomePage().showNotification("Không có dữ liệu", Clients.NOTIFICATION_TYPE_INFO);
			}
		}
	}

	private void setSpeedRun() {
		int speed = 12000 / slide.getCurpos();
		timerun.setDelay(speed);
	}

	private void handlerEventControl(Event event) {
		if (event.getTarget().equals(btnlui)) {
			if (listHistoryTab.getSelectedTabUnit() != null) {
				listHistoryTab.getSelectedTabUnit().previousSelectedItem();
			}
		} else if (event.getTarget().equals(btntien)) {
			if (listHistoryTab.getSelectedTabUnit() != null) {
				listHistoryTab.getSelectedTabUnit().nextSelectedItem();
			}
		} else if (event.getTarget().equals(btnchay)) {
			if (!timerun.isRunning()) {
				if (listHistoryTab.isBeginRun()) {
					clearMap();
					listHistoryTab.setBeginRun(false);
				}
				listHistoryTab.setParentMarkerRun(vmaphistory);
				listHistoryTab.setContenMarkerRun(false);
				Events.postEvent(Events.ON_TIMER, timerun, null);
				timerun.start();

			} else {
				Env.getHomePage().showNotification("Lịch sử đang chạy! ", Clients.NOTIFICATION_TYPE_WARNING);
			}

		} else if (event.getTarget().equals(btndung)) {
			timerun.stop();
		}
	}

	private void refresh() {
		clearMap();
		listHistoryTab.refresh();
		genaralTab.refresh();
	}

	private void clearMap() {
		vmaphistory.removeAllChild();
	}

	private EventListener<Event> LISTENER_COMBOBOX = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			if (event.getTarget().equals(cbgroupvehicle)) {
				if (cbgroupvehicle.getSelectedIndex() >= 0) {
					cbgroupvehicle.setAttribute("modelid",
							((TaxiGroup) cbgroupvehicle.getSelectedItem().getValue()).getId());
					cbvehicle.setSelectedIndex(-1);
				} else {
					cbvehicle.setModel(null);
					cbgroupvehicle.setAttribute("modelid", 0);
				}
			}

			if ((Integer) cbgroupvehicle.getAttribute("modelid") > 0) {
				String sql = "From Vehicle where 1=1 and VehicleGroupID = " + cbgroupvehicle.getAttribute("modelid");
				loadDataToCombobox(cbvehicle, sql);
			}

		}

	};
	private EventListener<Event> LISTENER_REFRESH = new EventListener<Event>() {
		@Override
		public void onEvent(Event arg0) throws Exception {
			initTime();
			if (!timerun.isRunning()) {
				cbgroupvehicle.setSelectedIndex(-1);
				cbvehicle.setSelectedIndex(-1);
				refresh();
			} else {
				Env.getHomePage().showValidateForm("Bạn đang chạy lại hành trình! Vui lòng dừng lại trước khi tạo mới",
						Clients.NOTIFICATION_TYPE_WARNING);
			}

		}
	};
	private EventListener<Event> LISTENER_BTNXEM = new EventListener<Event>() {
		@Override
		public void onEvent(Event arg0) throws Exception {
			if (!timerun.isRunning()) {
				Integer deviceid = null;
				Vehicle vehicle = null;
				long fromtime = TimeUnit.MILLISECONDS
						.toSeconds(fromdate.getValue().getTime() + TimeUnit.HOURS.toMillis(7));
				long totime = TimeUnit.MILLISECONDS.toSeconds(todate.getValue().getTime() + TimeUnit.HOURS.toMillis(7));
				String msgerror = "";
				if (totime - fromtime < 0) {
					msgerror = msgerror + "Thời gian bắt đầu phải lớn hơn thời gian hiện tại! <br>";
				}
				if ((totime - fromtime) > TimeUnit.DAYS.toSeconds(1)) {
					msgerror = msgerror + "Thời gian xem lịch sử vượt quá 1 ngày!<br>";
				}
				if (cbvehicle.getSelectedItem() != null) {
					vehicle = (Vehicle) (cbvehicle.getSelectedItem().getValue());
					deviceid = vehicle.getDeviceID();
				} else if (arg0.getData() != null) {
					deviceid = (Integer) arg0.getData();
				} else {
					msgerror = msgerror + "Bạn chưa chọn xe! <br>";
				}
				if (!msgerror.equals("")) {
					Env.getHomePage().showValidateForm(msgerror, Clients.NOTIFICATION_TYPE_WARNING);
				} else {
					Clients.showBusy(divcenter, "Processing...!");
					if (xemcungbando.isChecked()
							&& (genaralTab.getMapGenaral().size() > 1 || listHistoryTab.getMapTabList().size() > 1)) {
						Env.getHomePage().showNotification("Chỉ được phép xem cùng lúc 2 xe!",
								Clients.NOTIFICATION_TYPE_INFO);
					} else {
						paneldetail.setOpen(true);
						MongoAction mgaction = new MongoAction();
						List<GpsTrackingMsg> historys = mgaction.getHistoryVehicle(fromtime, totime, deviceid);
						if (historys.size() > 0) {
							if (!xemcungbando.isChecked() && listHistoryTab.getMapTabList().size() > 0) {
								refresh();
							}
							genaralTab.putGenaral(vehicle);
							generalHistory(historys, vehicle);
							listHistoryTab.putTabList(vehicle, historys, vmaphistory, hienHanhtrinh.isChecked());

						} else {
							Env.getHomePage().showNotification("Không có dữ liệu !", Clients.NOTIFICATION_TYPE_INFO);
						}
					}
					Clients.clearBusy(divcenter);
				}

			} else {
				Env.getHomePage().showNotification("Bạn đang chạy lại hành trình!", Clients.NOTIFICATION_TYPE_INFO);
			}

		}

	};

	private EventListener<Event> LISTENER_GMAP = new EventListener<Event>() {
		@Override
		public void onEvent(Event events) throws Exception {
			if (events.getName().equals(VMapEvents.ON_VMARKER_CLICK)) {
				VMapEvent event = (VMapEvent) events;
				AbstractComponent comp = (AbstractComponent) event.getComponent();
				if (comp instanceof VehicleMarker) {
					((VehicleMarker) comp).setOpen(true);
				}
			}

		}
	};

	private EventListener<Event> LISTENER_TIMER_RUN = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			Boolean checkend = null;
			for (Tab tab : listHistoryTab.getMapTabList().values()) {
				if (!((ListBoxTabUnit) tab.getValue()).isRunEnd()) {
					runHistory(tab.getValue(), (Timer) event.getTarget());
				}
				if (checkend == null) {
					checkend = ((ListBoxTabUnit) tab.getValue()).isRunEnd();
				} else {
					checkend = checkend && ((ListBoxTabUnit) tab.getValue()).isRunEnd();
				}
			}
			if (checkend != null && checkend == true) {
				timerun.stop();
			}

		}
	};

	private void runHistory(ListBoxTabUnit listboxhistory, Timer timer) {
		int locationbegin = listboxhistory.getSelectedIndex();
		if (locationbegin < 0) {
			locationbegin = 0;
			listboxhistory.setSelectedIndex(0);
		}
		List<LatLng> latlngs = new ArrayList<LatLng>();
		GpsTrackingMsg gpsbegin = (GpsTrackingMsg) listboxhistory.getItemAtIndex(locationbegin).getValue();
		if (locationbegin == 0) {
			listboxhistory.updateInforMarkerRun(gpsbegin);
			if (gpsbegin.isPointSpecial()) {
				if (gpsbegin.isStopPoint()) {
					listboxhistory.setupVmarker(vmaphistory, gpsbegin, VehicleMarker.TYPE_POINT_STOP, true);
				} else if (gpsbegin.isActivityPoint()) {
					listboxhistory.setupVmarker(vmaphistory, gpsbegin, VehicleMarker.TYPE_POINT_START, true);
				} else if (gpsbegin.isStartTrip()) {
					listboxhistory.setupVmarker(vmaphistory, gpsbegin, VehicleMarker.TYPE_BEGIN_TRIP, true);
				}
			}
		}
		latlngs.add(listboxhistory.getPoint());
		if (locationbegin < listboxhistory.getSize() - 1) {
			listboxhistory.setSelectedIndex(locationbegin + 1);
			GpsTrackingMsg gpsend = (GpsTrackingMsg) listboxhistory.getItemAtIndex(locationbegin + 1).getValue();
			if (gpsbegin != null && gpsend != null) {
				long deltatime = gpsend.getTimeLog().getTime() - gpsbegin.getTimeLog().getTime();
				if (deltatime < TimeUnit.MINUTES.toMillis(5)) {
					double distance = MapUtils.distance(gpsbegin.getLongitude(), gpsbegin.getLatitude(),
							gpsend.getLongitude(), gpsend.getLatitude());
					if (distance > 10) {
						latlngs.add(gpsend.getLatlng());
						listboxhistory.updateInforMarkerRun(gpsend);
						listboxhistory.setupGpolyline(vmaphistory, latlngs, gpsbegin.getInTrip(), true);
						listboxhistory.setPoint(gpsend.getLatlng());
					}
				} else {
					listboxhistory.setPoint(gpsend.getLatlng());
				}
				if (gpsend.isPointSpecial()) {
					if (gpsend.isStopPoint()) {
						listboxhistory.setupVmarker(vmaphistory, gpsend, VehicleMarker.TYPE_POINT_STOP, true);
					} else if (gpsend.isActivityPoint()) {
						listboxhistory.setupVmarker(vmaphistory, gpsend, VehicleMarker.TYPE_POINT_START, true);
					} else if (gpsend.isStartTrip()) {
						listboxhistory.setupVmarker(vmaphistory, gpsend, VehicleMarker.TYPE_BEGIN_TRIP, true);
					}
				}
				if (checkPointOutGmap(gpsend.getLatlng())) {
					if (listHistoryTab.getMapTabList().size() > 1) {
						List<LatLng> bounds = listHistoryTab.getBoundsRunIcon();
						if (bounds.size() > 0) {
							MapUtils.setCenter(bounds, vmaphistory, 0.2);
						}
					} else {
						vmaphistory.setCenter(gpsend.getLatlng());
					}

				}
			} else {
				AppLogger.logDebug.info("null point" + locationbegin);
			}
		} else {
			listboxhistory.setRunEnd(true);
		}
	}

	public boolean checkPointOutGmap(LatLng point) {
		boolean result = false;
		LatLngBounds bound = vmaphistory.getBounds();
		LatLng rightTop = bound.northEast;
		LatLng leftBottom = bound.southWest;
		if (rightTop != null && leftBottom != null) {
			result = (point.lat < leftBottom.lat) || (point.lat > rightTop.lat) || (point.lng < leftBottom.lng)
					|| (point.lng > rightTop.lng);
		}

		return result;
	}

	private EventListener<Event> LISTENER_CHECKBOX = new EventListener<Event>() {

		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(Event event) throws Exception {
			if (event.getTarget().equals(hienHanhtrinh)) {
				List<Component> lstcomp = vmaphistory.getChildren();
				for (Component component : lstcomp) {
					component.setVisible(hienHanhtrinh.isChecked());
				}
			} else {
				ArrayList<Listheader> lstheader = (ArrayList<Listheader>) (event.getTarget())
						.getAttribute("listHeader");
				if (lstheader != null && lstheader.size() > 0) {
					for (Listheader listheader : lstheader) {
						listheader.setVisible(((Checkbox) event.getTarget()).isChecked());
					}
				}
			}
		}

	};

	private void generalHistory(List<GpsTrackingMsg> lsthistorytmp, Vehicle vehicle) {
		Tab tab = genaralTab.getMapGenaral().get(vehicle.getId());
		Clients.showBusy(tab, "Đang tổng hợp!");
		GenaralHistoryWorker genaralworker = new GenaralHistoryWorker(desktop, lsthistorytmp, vehicle.getId(),
				genaralTab);
		genaralworker.start();
	}

	public Datebox getFromdate() {
		return fromdate;
	}

	public void setFromdate(Datebox fromdate) {
		this.fromdate = fromdate;
	}

	public Datebox getTodate() {
		return todate;
	}

	public void setTodate(Datebox todate) {
		this.todate = todate;
	}

	public Combobox getCbgroupvehicle() {
		return cbgroupvehicle;
	}

	public void setCbgroupvehicle(ComboboxSearch cbgroupvehicle) {
		this.cbgroupvehicle = cbgroupvehicle;
	}

	public Combobox getCbvehicle() {
		return cbvehicle;
	}

	public void setCbvehicle(ComboboxSearch cbvehicle) {
		this.cbvehicle = cbvehicle;
	}

	public Button getBtnXem() {
		return btnXem;
	}

	public void setBtnXem(Button btnXem) {
		this.btnXem = btnXem;
	}

	public Button getBtnchay() {
		return btnchay;
	}

	public Borderlayout getBddetail() {
		return bddetail;
	}

	public void setBddetail(Borderlayout bddetail) {
		this.bddetail = bddetail;
	}

	public void setBtnchay(Toolbarbutton btnchay) {
		this.btnchay = btnchay;
	}

	public Button getBtndung() {
		return btndung;
	}

	public void setBtndung(Toolbarbutton btndung) {
		this.btndung = btndung;
	}

	public Toolbarbutton getBtnlui() {
		return btnlui;
	}

	public void setBtnlui(Toolbarbutton btnlui) {
		this.btnlui = btnlui;
	}

	public Toolbarbutton getBtntien() {
		return btntien;
	}

	public void setBtntien(Toolbarbutton btntien) {
		this.btntien = btntien;
	}

	public Toolbarbutton getBtnexportexcel() {
		return btnexportexcel;
	}

	public void setBtnexportexcel(Toolbarbutton btnexportexcel) {
		this.btnexportexcel = btnexportexcel;
	}

	public Desktop getDesktop() {
		return desktop;
	}

	public Listbox getListboxhistory() {
		return listboxhistory;
	}

	public void setListboxhistory(Listbox listboxhistory) {
		this.listboxhistory = listboxhistory;
	}

}