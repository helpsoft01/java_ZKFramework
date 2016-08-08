package com.vietek.tracking.ui.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Toolbar;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.controller.vmap.VMaps;
import com.vietek.taxioperation.ui.controller.vmap.VPolyline;
import com.vietek.taxioperation.util.MapUtils;

public class ListBoxTabUnit extends Listbox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Checkbox hienVgpsChitiet;
	private Checkbox hienVgps;
	private Checkbox hienVco;
	private Checkbox hienhanhtrinh;
	private Vehicle vehicle;
	private Toolbar toolControl;
	private VehicleMarker markerrun;
	private List<?> lstmodel;
	private VMaps vmapViewer;
	List<LatLng> points;
	private LatLng point;
	private boolean isRunEnd = false;

	public ListBoxTabUnit(Vehicle vehicle) {
		this.vehicle = vehicle;
		points = new ArrayList<LatLng>();
		point = new LatLng(0, 0);
		initControl();
		initUI();
	}

	private void initControl() {
		toolControl = new Toolbar();
		hienVco = new Checkbox("V.cơ");
		hienVco.setStyle("margin-left: 5px");
		hienVco.addEventListener(Events.ON_CLICK, LISTEN_CHECKBOX);
		toolControl.appendChild(hienVco);
		hienVgps = new Checkbox("V.gps");
		hienVgps.setChecked(true);
		hienVgps.setStyle("margin-left: 10px");
		hienVgps.addEventListener(Events.ON_CLICK, LISTEN_CHECKBOX);
		toolControl.appendChild(hienVgps);
		hienVgpsChitiet = new Checkbox("V.gps chi tiết");
		hienVgpsChitiet.setStyle("margin-left: 10px");
		hienVgpsChitiet.addEventListener(Events.ON_CLICK, LISTEN_CHECKBOX);
		toolControl.appendChild(hienVgpsChitiet);
		hienhanhtrinh = new Checkbox("H.Trình xe");
		hienhanhtrinh.setChecked(true);
		hienhanhtrinh.setStyle("margin-left: 10px");
		hienhanhtrinh.addEventListener(Events.ON_CLICK, LISTEN_CHECKBOX);
		toolControl.appendChild(hienhanhtrinh);
	}

	private void initUI() {
		this.setSclass("List_box_unit");
		this.setVflex("1");
		this.setStyle("overflow: auto;");
		this.setEmptyMessage("Không có dữ liệu");
		this.addEventListener(Events.ON_SELECT, LISTENER_LISBOX);
		this.setSizedByContent(true);
		this.setSpan(true);
		Listhead head = new Listhead();
		head.setParent(this);
		head.setSizable(true);
		Listheader header = new Listheader("Điểm");
		head.appendChild(header);
		header = new Listheader("Thời điểm");
		head.appendChild(header);
		header = new Listheader("Ghi nhận");
		head.appendChild(header);
		header = new Listheader("V.Gps");
		hienVgps.setAttribute("Header", header);
		head.appendChild(header);
		header = new Listheader("V.Gps chi tiết");
		header.setVisible(false);
		hienVgpsChitiet.setAttribute("Header", header);
		head.appendChild(header);
		header = new Listheader("V.cơ");
		header.setVisible(false);
		hienVco.setAttribute("Header", header);
		head.appendChild(header);
		header = new Listheader("Trạng thái");
		head.appendChild(header);
		header = new Listheader("QD ca");
		head.appendChild(header);
		this.setItemRenderer(new ListitemRenderer<GpsTrackingMsg>() {
			@Override
			public void render(Listitem items, GpsTrackingMsg data, int index) throws Exception {
				items.setValue(data);

				if (data.isPointSpecial()) {
					Image img = new Image(data.getPathSrc());
					Listcell point = new Listcell();
					point.setParent(items);
					point.appendChild(img);
				} else {
					new Listcell().setParent(items);
				}
				StringBuilder str = new StringBuilder("");
				for (int i = 0; i < data.getGpsSepeed().length; i++) {
					str.append(data.getGpsSepeed()[i] + ",");
				}
				SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
				new Listcell("" + dateformat.format(data.getTimeLog())).setParent(items);
				new Listcell("" + dateformat.format(data.getReceivetime())).setParent(items);
				new Listcell("" + data.getGpsSepeed()[9]).setParent(items);
				new Listcell(str.toString()).setParent(items);
				new Listcell("" + data.getMeterSpeed()).setParent(items);
				new Listcell(data.getInTripStatus()).setParent(items);
				new Listcell(StringUtils.doubleFormat(data.getPathTrip())).setParent(items);
				if (data.isLostGPS()) {
					items.setStyle("background-color:#ff5050");
				}
			}

		});

	}

	private EventListener<Event> LISTEN_CHECKBOX = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			if (event.getTarget().equals(hienhanhtrinh)) {
				Iterator<Component> ite = vmapViewer.getChildren().iterator();
				while (ite.hasNext()) {
					Component comp = ite.next();

					if (comp.getAttribute("CheckControl") != null
							&& comp.getAttribute("CheckControl").equals(hienhanhtrinh)) {
						comp.setVisible(hienhanhtrinh.isChecked());
					}
				}
			} else {
				((Listheader) event.getTarget().getAttribute("Header"))
						.setVisible(((Checkbox) event.getTarget()).isChecked());
			}

		}

	};

	public void nextSelectedItem() {
		if (this.getSelectedIndex() < 0) {
			this.setSelectedIndex(0);
			Events.postEvent(Events.ON_SELECT, this, null);
		} else {
			int selectedindex = this.getSelectedIndex();
			this.setSelectedIndex(selectedindex + 1);
			Events.postEvent(Events.ON_SELECT, this, null);
		}
	}

	public void previousSelectedItem() {
		int selectedindex = this.getSelectedIndex();
		if ((selectedindex - 1) >= 0) {
			this.setSelectedIndex(selectedindex - 1);
			Events.postEvent(Events.ON_SELECT, this, null);
		}
	}

	public Listbox generatedListBoxExcel() {
		Listbox lsttmp = new Listbox();
		Listhead head = new Listhead();
		head.setParent(lsttmp);
		head.setSizable(true);
		Listheader header = new Listheader("STT");
		head.appendChild(header);
		header = new Listheader("Thời điểm");
		head.appendChild(header);
		header = new Listheader("V.gps (km/h)");
		head.appendChild(header);
		header = new Listheader("V.cơ (km/h)");
		head.appendChild(header);
		header = new Listheader("Trạng thái");
		head.appendChild(header);
		header = new Listheader("Vị trí");
		head.appendChild(header);
		header = new Listheader("Kinh độ");
		head.appendChild(header);
		header = new Listheader("Vĩ độ");
		head.appendChild(header);
		header = new Listheader("Tiền cuốc (VND)");
		head.appendChild(header);
		header = new Listheader("Km cuốc");
		head.appendChild(header);
		header = new Listheader("Tiền ca (VND)");
		head.appendChild(header);
		header = new Listheader("Km ca");
		head.appendChild(header);
		header = new Listheader("Số cuốc");
		head.appendChild(header);
		header = new Listheader("Tổng tiền đồng hồ");
		head.appendChild(header);
		lsttmp.setItemRenderer(new ListitemRenderer<GpsTrackingMsg>() {
			@Override
			public void render(Listitem items, GpsTrackingMsg msg, int index) throws Exception {
				new Listcell(index + "").setParent(items);
				new Listcell(new SimpleDateFormat("HH:mm:ss").format(msg.getTimeLog())).setParent(items);
				new Listcell(msg.getGpsSepeed()[9] + "").setParent(items);
				new Listcell(msg.getMeterSpeed() + "").setParent(items);
				new Listcell(msg.getInTrip() == 1 ? "Có khách" : "Không khách" + "").setParent(items);
				new Listcell(msg.getAddress()).setParent(items);
				new Listcell(msg.getLatitude() + "").setParent(items);
				new Listcell(msg.getLongitude() + "").setParent(items);
				new Listcell(msg.getMoneyTrip() + "").setParent(items);
				new Listcell(msg.getTripPath() + "").setParent(items);
				new Listcell(msg.getTotalMoneyShift() + "").setParent(items);
				new Listcell(msg.getPathInShiff() + "").setParent(items);
				new Listcell(msg.getTotalTrip() + "").setParent(items);
				new Listcell(msg.getTotalMoney() + "").setParent(items);

			}

		});
		lsttmp.setModel(new ListModelList<>(lstmodel));
		return lsttmp;
	}

	public VehicleMarker getMarkerrun() {
		if (markerrun == null) {
			markerrun = new VehicleMarker(null, VehicleMarker.TYPE_NORMAL);
		}
		return markerrun;
	}

	public void updateInforMarkerRun(GpsTrackingMsg data) {
		if (markerrun == null) {
			markerrun = new VehicleMarker(data, VehicleMarker.TYPE_NORMAL);
			AppLogger.logDebug.info("MarkerRun is Null");
		} else {
			markerrun.setLatLng(data.getLatlng());
			markerrun.setAngle(data.getAngle());
		}
		markerrun.setParent(vmapViewer);
	}

	public void setMarkerrun(VehicleMarker markerrun) {
		this.markerrun = markerrun;
	}

	public List<?> getLstmodel() {
		return lstmodel;
	}

	public void setLstmodel(List<?> lstmodel) {
		this.lstmodel = lstmodel;
		this.setModel(new ListModelList<>(lstmodel));
		this.renderAll();
		this.setSelectedIndex(0);
		GpsTrackingMsg gps = (GpsTrackingMsg) this.getSelectedItem().getValue();
		if (gps != null) {
			this.setPoint(gps.getLatlng());
		}
	}

	public int getSize() {
		return lstmodel.size();
	}

	public LatLng getPoint() {
		return point;
	}

	public void setPoint(LatLng point) {
		this.point = point;
	}

	public Toolbar getToolControl() {
		return toolControl;
	}

	public void setToolControl(Toolbar toolControl) {
		this.toolControl = toolControl;
	}

	public void refresh() {
		this.setModel(new ListModelList<>());
		// this.setSelectedIndex(-1);
		// this.setPoint(new LatLng(0, 0));
		refreshVmap();
	}

	public void refreshVmap() {
		Iterator<Component> ite = vmapViewer.getChildren().iterator();
		while (ite.hasNext()) {
			Component comp = ite.next();

			if (comp.getAttribute("CheckControl") != null && comp.getAttribute("CheckControl").equals(hienhanhtrinh)) {
				ite.remove();
				vmapViewer.removeChild(comp);

			}
		}
	}

	private EventListener<Event> LISTENER_LISBOX = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			Listbox listbox = (Listbox) event.getTarget();
			GpsTrackingMsg gpstmp = (GpsTrackingMsg) listbox.getSelectedItem().getValue();
			if (gpstmp != null) {
				if (markerrun == null) {
					markerrun = new VehicleMarker(gpstmp, VehicleMarker.TYPE_NORMAL);
				} else {
					markerrun.setData(gpstmp);
					markerrun.setOpen(true);
				}
				markerrun.setParent(vmapViewer);
				point = gpstmp.getLatlng();
				vmapViewer.setCenter(gpstmp.getLatlng());
			}
			if (isRunEnd) {
				isRunEnd = false;
			}
		}
	};

	public void updateListbox(GenaralValue data) {
		List<GpsTrackingMsg> lstdataupdate = data.getPointsStop();
		for (GpsTrackingMsg gpsTrackingMsg : lstdataupdate) {
			int index = lstmodel.indexOf(gpsTrackingMsg);
			if (index != -1) {
				((ListModelList<Object>) this.getListModel()).set(index, gpsTrackingMsg);
				this.setupVmarker(vmapViewer, gpsTrackingMsg, VehicleMarker.TYPE_POINT_STOP, hienhanhtrinh.isChecked());
			}
		}

	}

	public void drawHistory(List<GpsTrackingMsg> lispoint, boolean isShow) {
		hienhanhtrinh.setChecked(isShow);
		List<LatLng> path = new ArrayList<LatLng>();
		int state = lispoint.get(0).getInTrip();
		GpsTrackingMsg pointStop = null;
		long timePrevious = 0;
		ListIterator<GpsTrackingMsg> ite = lispoint.listIterator();
		while (ite.hasNext()) {
			GpsTrackingMsg gpselement = ite.next();
			if (!gpselement.isLostGPS()) {
				if ((gpselement.getInTrip()) == state) {
					if (path.size() > 0) {
						LatLng pointLast = path.get(path.size() - 1);
						double distance = MapUtils.distance(pointLast.lng, pointLast.lat, gpselement.getLongitude(),
								gpselement.getLatitude());
						if (timePrevious > 0) {
							long deltatime = gpselement.getTimeLog().getTime() - timePrevious;
							if (deltatime < TimeUnit.MINUTES.toMillis(5)) {
								if (distance > 10 || gpselement.isPointSpecial()) {
									path.add(gpselement.getLatlng());

								}
							} else {
								setupGpolyline(vmapViewer, path, state, isShow);
								path = new ArrayList<LatLng>();
								path.add(gpselement.getLatlng());
								state = gpselement.getInTrip();
							}
						}
						timePrevious = gpselement.getTimeLog().getTime();

					} else {
						path.add(gpselement.getLatlng());

					}

				} else {

					if (state == GpsTrackingMsg.KHONG_KHACH) {
						setupVmarker(vmapViewer, gpselement, VehicleMarker.TYPE_BEGIN_TRIP, isShow);
						gpselement.setStartTrip(true);
					}
					path.add(gpselement.getLatlng());
					setupGpolyline(vmapViewer, path, state, isShow);
					path = new ArrayList<LatLng>();
					path.add(gpselement.getLatlng());
					state = gpselement.getInTrip();
				}
				if (gpselement.isStopPoint()) {
					// setupVmarker(vmapViewer, gpselement,
					// VehicleMarker.TYPE_POINT_STOP);
					pointStop = gpselement;
				} else if (gpselement.isActivityPoint()) {
					if (pointStop != null) {
						pointStop.setTimestop(gpselement.getMsgOld().getTimestop());
						setupVmarker(vmapViewer, pointStop, VehicleMarker.TYPE_POINT_STOP, isShow);
						pointStop = null;
					}
					setupVmarker(vmapViewer, gpselement, VehicleMarker.TYPE_POINT_START, isShow);
				}
			} else {
				ite.remove();
				path = new ArrayList<LatLng>();
			}
		}
		if (path.size() > 1) {
			setupGpolyline(vmapViewer, path, state, isShow);
		}
		MapUtils.scaleMap(vmapViewer);
	}

	public void setupGpolyline(VMaps vmapViewer, List<LatLng> lstpath, int linestate, boolean isShow) {
		VPolyline gpline = new VPolyline();
		gpline.setSclass("z-polyline-history");
		gpline.setVisible(isShow);
		gpline.setWidth("2px");
		gpline.setPath((ArrayList<LatLng>) lstpath);
		if (linestate == GpsTrackingMsg.CO_KHACH) {
			gpline.setColor("#009900");
		} else if (linestate == GpsTrackingMsg.KHONG_KHACH) {
			gpline.setColor("#fc060c");
		}
		gpline.setAttribute("CheckControl", hienhanhtrinh);
		vmapViewer.appendChild(gpline);
	}

	public void setupVmarker(VMaps vmap, GpsTrackingMsg data, int typemarker, boolean isShow) {
		VehicleMarker marker = new VehicleMarker(data, typemarker);
		marker.setVisible(isShow);
		marker.setAttribute("CheckControl", hienhanhtrinh);
		marker.setParent(vmap);
	}

	public Checkbox getHienVgpsChitiet() {
		return hienVgpsChitiet;
	}

	public void setHienVgpsChitiet(Checkbox hienVgpsChitiet) {
		this.hienVgpsChitiet = hienVgpsChitiet;
	}

	public Checkbox getHienVgps() {
		return hienVgps;
	}

	public void setHienVgps(Checkbox hienVgps) {
		this.hienVgps = hienVgps;
	}

	public Checkbox getHienVco() {
		return hienVco;
	}

	public void setHienVco(Checkbox hienVco) {
		this.hienVco = hienVco;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public VMaps getvmapViewer() {
		return vmapViewer;
	}

	public void setvmapViewer(VMaps vmapViewer) {
		this.vmapViewer = vmapViewer;
	}

	public boolean isRunEnd() {
		return isRunEnd;
	}

	public void setRunEnd(boolean isRunEnd) {
		this.isRunEnd = isRunEnd;
	}

}
