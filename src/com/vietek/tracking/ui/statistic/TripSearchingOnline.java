package com.vietek.tracking.ui.statistic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.IntFormat;
import com.vietek.taxioperation.common.MathForRound;
import com.vietek.taxioperation.controller.CommonValueController;
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.CommonValue;
import com.vietek.taxioperation.model.RptTripSearchingOnline;
import com.vietek.taxioperation.model.RptTripSearchingOnlineFilter;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.ui.util.ComboboxRender;
import com.vietek.taxioperation.ui.util.ComponentsReport;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.tracking.ui.utility.TrackingHistory;

public class TripSearchingOnline extends Window implements EventListener<Event> {

	/**
	 * Tim kiem cuoc khach
	 */
	private static final long serialVersionUID = 1L;

	private Chosenbox chosenAgent;
	private Chosenbox chosenGroup;
	// private Chosenbox chosenParkingArea;
	private Textbox txtVehicleNumber;
	private Textbox txtLicensePlate;
	private Datebox datefrom;
	private Datebox dateto;
	private Button btnSearch;
	private Grid gridSearchData;
	private StringBuilder strAgentID;
	private StringBuilder strGroupID;
	private StringBuilder strParkAreaID;
	private List<RptTripSearchingOnline> lstdata;
	private Textbox txtGroupNameFilter;
	private Textbox txtVehicleNumberFilter;
	private Textbox txtLicensePlateFilter;
	private Textbox txtPlaceStartFilter;
	private Textbox txtPlaceFinishFilter;
	private Textbox txtDriverFilter;
	private Textbox txtTypeVehicleFilter;
	private Textbox txtDateStartFilter;
	private Textbox txtDateFinishFilter;
	private Button btnHistory;

	// private Textbox txtInputReduce;
	// private Combobox cboInputReason;

	public TripSearchingOnline() throws Exception {
		this.init();
	}

	private void init() throws Exception {
		this.setShadow(true);
		this.setHflex("1");
		this.setVflex("1");
		this.setClosable(true);

		Vlayout vlayout = new Vlayout();
		vlayout.setParent(this);
		vlayout.setHflex("100%");
		vlayout.setVflex("100%");

		this.createTripSearchingOnline(vlayout);
	}

	private void createTripSearchingOnline(Vlayout vlayout) throws Exception {
		Hlayout hlayout = new Hlayout();
		hlayout.setParent(vlayout);
		hlayout.setHflex("100%");
		hlayout.setVflex("100%");
		hlayout.setStyle("overflow : auto");

		createInput(hlayout);
		createData(hlayout);
	}

	private void createInput(Hlayout hlayout) throws Exception {
		Panel panel = new Panel();
		panel.setParent(hlayout);
		panel.setTitle("ĐIỀU KIỆN TÌM KIẾM CUỐC");
		panel.setStyle("color : black; font-size : 14px; font-weight : bold; overflow : auto");
		panel.setHflex("2.5");
		panel.setBorder("normal");

		Panelchildren panchild = new Panelchildren();
		panchild.setParent(panel);

		Div div = new Div();
		div.setParent(panchild);

		Vlayout vlayout = new Vlayout();
		vlayout.setParent(div);
		vlayout.setHeight("100%");

		// tao cac cbo
		Hlayout hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 12px; margin-left : 1px");

		Hbox hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		Label label = new Label("Đơn vị");
		label.setParent(hbox);
		label.setStyle("font-weight : bold; font-size : 14px; color : black");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 33px");
		hbox = new Hbox();
		hbox.setParent(div);

		this.createCboAgent(hbox);
		// Tao cbo doi xe
		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		label = new Label("Đội xe");
		label.setParent(hbox);
		label.setStyle("font-weight : bold; font-size : 14px; color :black");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 36px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		this.createCboGroup(hbox);

		// Tao txt so tai
		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		label = new Label("Số tài");
		label.setParent(hbox);
		label.setStyle("font-weight : bold; font-size : 14px");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 40px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		txtVehicleNumber = new Textbox();
		txtVehicleNumber.setParent(hbox);
		txtVehicleNumber.setWidth("170px");
		// Tao txt BKS
		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		label = new Label("BKS");
		label.setParent(hbox);
		label.setStyle("font-weight : bold; font-size : 14px");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 50px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		txtLicensePlate = new Textbox();
		txtLicensePlate.setParent(hbox);
		txtLicensePlate.setWidth("170px");

		hl = new Hlayout();
		hl.setParent(vlayout);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		label = new Label("Từ ngày");
		label.setParent(hbox);
		label.setStyle("font-weight : bold; font-size : 14px");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 22px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		datefrom = new Datebox();
		datefrom.setParent(hbox);
		datefrom.setWidth("170px");
		datefrom.setValue(addHour(new Date(), 00, 00));
		datefrom.setFormat("dd/MM/yyyy HH:mm");
		datefrom.setConstraint("no empty : Không để trống thời gian");

		hl = new Hlayout();
		hl.setParent(vlayout);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		label = new Label("Đến ngày");
		label.setParent(hbox);
		label.setStyle("font-weight : bold; font-size : 14px");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 15px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		dateto = new Datebox();
		dateto.setParent(hbox);
		dateto.setWidth("170px");
		dateto.setValue(addHour(new Date(), 23, 59));
		dateto.setFormat("dd/MM/yyyy HH:mm");
		dateto.setConstraint("no empty : Không để trống thời gian !");

		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 170px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		btnSearch = new Button();
		btnSearch.setLabel("Tìm kiếm");
		btnSearch.setStyle("color: black; font-weight: bold; color : black");
		btnSearch.setParent(hbox);
		btnSearch.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				long timedateto = dateto.getValue().getTime();
				long timedatefrom = datefrom.getValue().getTime();
				if (strAgentID == null) {
					Env.getHomePage().showNotification("Hãy chọn đơn vị tìm kiếm !", Clients.NOTIFICATION_TYPE_ERROR);
				} else if (timedateto < timedatefrom) {
					Env.getHomePage().showNotification("Hãy chọn lại thời gian tìm kiếm cuốc khách",
							Clients.NOTIFICATION_TYPE_ERROR);
					List<RptTripSearchingOnline> lstData = new ArrayList<RptTripSearchingOnline>();
					gridSearchData.setEmptyMessage("Không có dữ liệu !");
					gridSearchData.setModel(new ListModelList<RptTripSearchingOnline>(lstData));
				} else {
					lstdata = displayRptTripSearchingOnline();
					if (lstdata == null || lstdata.isEmpty()) {
						gridSearchData.setEmptyMessage("Không có dữ liệu");
					} else {
						gridSearchData.setModel(new ListModelList<RptTripSearchingOnline>(lstdata));
						txtGroupNameFilter.setValue("");
						txtVehicleNumberFilter.setValue("");
						txtLicensePlateFilter.setValue("");
						txtPlaceStartFilter.setValue("");
						txtPlaceFinishFilter.setValue("");
						txtDriverFilter.setValue("");
						txtTypeVehicleFilter.setValue("");
						txtDateStartFilter.setValue("");
						txtDateFinishFilter.setValue("");
					}
				}

			}
		});

	}

	private void createData(Hlayout hlayout) throws Exception {
		gridSearchData = new Grid();
		gridSearchData.setParent(hlayout);
		gridSearchData.setHflex("7.5");
		gridSearchData.setVflex("1");
		gridSearchData.setMold("paging");
		gridSearchData.setAutopaging(true);

		Auxhead auxhead = new Auxhead();
		auxhead.setParent(gridSearchData);
		Auxheader auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setColspan(17);
		auxheader.setLabel("DANH SÁCH CUỐC KHÁCH TÌM KIẾM");
		auxheader.setStyle("color : black; font-size : 16px, font-weight : bold, margin-left : 20px");

		Columns cols = new Columns();
		cols.setParent(gridSearchData);

		Column col = new Column();
		col.setParent(cols);
		col.setLabel("TT");
		col.setWidth("40px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Đội xe");
		col.setWidth("220px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Số tài");
		col.setWidth("70px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("BKS");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Loại xe");
		col.setWidth("220px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Giờ đón");
		col.setWidth("130px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Giờ trả");
		col.setWidth("130px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Điểm đón");
		col.setWidth("350px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Điểm trả");
		col.setWidth("350px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Đồng hồ");
		col.setAlign("right");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Giảm trừ");
		col.setAlign("right");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Tiền cuốc");
		col.setAlign("right");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Lý do");
		col.setAlign("right");
		col.setWidth("150px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Km");
		col.setAlign("right");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Tài xế");
		col.setWidth("160px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Hành trình");
		col.setWidth("1000px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Lịch sử");
		col.setWidth("100px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		auxhead = new Auxhead();
		auxhead.setParent(gridSearchData);
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("");

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		Image image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtGroupNameFilter = new Textbox();
		txtGroupNameFilter.setPlaceholder("Tìm đội xe");
		txtGroupNameFilter.setWidth("90%");
		txtGroupNameFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtGroupNameFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});

		auxheader.appendChild(image);
		auxheader.appendChild(txtGroupNameFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		txtVehicleNumberFilter = new Textbox();
		txtVehicleNumberFilter.setPlaceholder("Số tài");
		txtVehicleNumberFilter.setWidth("80%");
		txtVehicleNumberFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtVehicleNumberFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		auxheader.appendChild(image);
		auxheader.appendChild(txtVehicleNumberFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		txtLicensePlateFilter = new Textbox();
		txtLicensePlateFilter.setPlaceholder("Tìm BKS");
		txtLicensePlateFilter.setWidth("80%");
		txtLicensePlateFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtLicensePlateFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		auxheader.appendChild(image);
		auxheader.appendChild(txtLicensePlateFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		txtTypeVehicleFilter = new Textbox();
		txtTypeVehicleFilter.setPlaceholder("Tìm theo loại xe");
		txtTypeVehicleFilter.setWidth("92%");
		txtTypeVehicleFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtTypeVehicleFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		auxheader.appendChild(image);
		auxheader.appendChild(txtTypeVehicleFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		txtDateStartFilter = new Textbox();
		txtDateStartFilter.setPlaceholder("dd-MM-yyyy");
		txtDateStartFilter.setWidth("90%");
		txtDateStartFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtDateStartFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		auxheader.appendChild(image);
		auxheader.appendChild(txtDateStartFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		txtDateFinishFilter = new Textbox();
		txtDateFinishFilter.setPlaceholder("dd-MM-yyyy");
		txtDateFinishFilter.setWidth("90%");
		txtDateFinishFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtDateFinishFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		auxheader.appendChild(image);
		auxheader.appendChild(txtDateFinishFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		txtPlaceStartFilter = new Textbox();
		txtPlaceStartFilter.setPlaceholder("Tìm theo điểm đón");
		txtPlaceStartFilter.setWidth("95%");
		txtPlaceStartFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtPlaceStartFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		auxheader.appendChild(image);
		auxheader.appendChild(txtPlaceStartFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		txtPlaceFinishFilter = new Textbox();
		txtPlaceFinishFilter.setPlaceholder("Tìm theo điểm trả");
		txtPlaceFinishFilter.setWidth("95%");
		txtPlaceFinishFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtPlaceFinishFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		auxheader.appendChild(image);
		auxheader.appendChild(txtPlaceFinishFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setColspan(5);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		txtDriverFilter = new Textbox();
		txtDriverFilter.setPlaceholder("Tìm tài xế");
		txtDriverFilter.setWidth("90%");
		txtDriverFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtDriverFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		auxheader.appendChild(image);
		auxheader.appendChild(txtDriverFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setColspan(1);

		gridSearchData.setRowRenderer(new RowRenderer<RptTripSearchingOnline>() {

			@Override
			public void render(Row row, RptTripSearchingOnline data, int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + data.getGroupName()));
				row.appendChild(new Label("" + data.getVehicleNumber()));
				row.appendChild(new Label("" + data.getLicensePlate()));
				row.appendChild(new Label("" + data.getVehicleType()));
				row.appendChild(new Label("" + dateformat.format(data.getTimeStart())));
				row.appendChild(new Label("" + dateformat.format(data.getTimeFinish())));
				row.appendChild(new Label("" + data.getPlaceStart()));
				row.appendChild(new Label("" + data.getPlaceFinish()));
				row.appendChild(new Label("" + IntFormat.formatTypeInt("###,###", data.getClock())));
				Textbox txtInputReduce = new Textbox();
				txtInputReduce.setInplace(true);
				txtInputReduce.setWidth("90%");
				txtInputReduce.setValue("" + data.getReduce());

				row.appendChild(txtInputReduce);

				Label lb = new Label("" + IntFormat.formatTypeInt("###,###", data.getPriceTrip()));
				row.appendChild(lb);
				Combobox cboInputReason = createCboInputReason(row, data);
				cboInputReason.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {

						Comboitem item = cboInputReason.getSelectedItem();
						data.setReason(item.getValue());
						updateTripSearchingOnline(data.getShiftId(), data.getTripId(), data.getPriceTrip(),
								data.getReason(), "admin");
					}
				});
				row.appendChild(new Label("" + MathForRound.round(data.getKm(), 1)));
				row.appendChild(
						new Label("" + (data.getDriver().trim().toLowerCase() == null ? "" : "" + data.getDriver())));
				row.appendChild(new Label("" + data.getTripDetail()));
				txtInputReduce.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						data.setPriceTrip(data.getClock() - Integer.parseInt(txtInputReduce.getValue()));
						lb.setValue("" + data.getPriceTrip());

						updateTripSearchingOnline(data.getShiftId(), data.getTripId(), data.getPriceTrip(),
								data.getReason(), "admin");
					}
				});
				txtInputReduce.addForward(Events.ON_CHANGE, txtInputReduce, Events.ON_OK);

				btnHistory = new Button("Lịch sử");
				btnHistory.setStyle("color : black;font-size : 12px");
				btnHistory.setHeight("25px");
				row.appendChild(btnHistory);
				btnHistory.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						showHistory(data);
					}
				});

			}
		});

	}

	private void showHistory(RptTripSearchingOnline data) {
		TrackingHistory history = new TrackingHistory(new java.sql.Date(data.getTimeStart().getTime()),
				new java.sql.Date(data.getTimeFinish().getTime()), data.getVehicleId());
		Window window = new Window();
		window.setWidth("100%");
		window.setHeight("100%");
		window.setClosable(true);
		window.setTitle("Lịch sử cuốc chi tiết");
		window.setParent(this);
		window.appendChild(history);
		window.doModal();

	}

	private Date addHour(Date date, int h, int m) {
		if (date == null) {
			throw new IllegalArgumentException("");

		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, h);
		c.set(Calendar.MINUTE, m);
		return c.getTime();
	}

	private void createCboAgent(Hbox hbox) {
		chosenAgent = ComponentsReport.ChosenboxReportInput(Agent.class);
		chosenAgent.setWidth("170px");
		chosenAgent.setParent(hbox);		
		chosenAgent.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				Set<Agent> lstObjChosen = chosenAgent.getSelectedObjects();
				strAgentID = new StringBuilder();
				int temp = 0;
				for (Agent reportQcAgent : lstObjChosen) {
					if (temp < lstObjChosen.size() - 1) {
						strAgentID.append(reportQcAgent.getId()).append(",");
					} else {
						strAgentID.append(reportQcAgent.getId());
					}
					temp++;
				}
				ComponentsReport.reloadChosenboxGroup(chosenGroup, "" + strAgentID);
			}
		});
	}

	private void createCboGroup(Hbox hbox) {
		chosenGroup = ComponentsReport.ChosenboxReportInput(TaxiGroup.class);
		chosenGroup.setWidth("170px");
		chosenGroup.setParent(hbox);
		chosenGroup.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				Set<TaxiGroup> lstObjchosen = chosenGroup.getSelectedObjects();
				strGroupID = new StringBuilder();
				int temp = 0;
				for (TaxiGroup reportQcVehicleGroup : lstObjchosen) {
					if (temp < lstObjchosen.size() - 1) {
						strGroupID.append(
								reportQcVehicleGroup.getId()).append(",");
					} else {
						strGroupID.append(reportQcVehicleGroup.getId());
					}
					temp++;
				}
			}
		});
	}

	// Tao CboReason mới
	private Combobox createCboInputReason(Row row, RptTripSearchingOnline data) {

		List<CommonValue> lstcommonvalue = getCommonValue("REDUCEMONEY");
		HashMap<String, String> mapValue = new HashMap<String, String>();
		for (CommonValue common : lstcommonvalue) {
			mapValue.put(common.getCode(), common.getName());
		}
		Combobox cboinput = new Combobox();
		ComboboxRender renderCbo = new ComboboxRender();
		cboinput = renderCbo.ComboboxRendering(mapValue, "", "", 120, 8, data.getReason(), false);
		row.appendChild(cboinput);
		cboinput.setInplace(true);
		cboinput.setSclass("comboboxtripsearching");
		return cboinput;

	}

	private List<CommonValue> getCommonValue(String codetype) {
		CommonValueController controller = (CommonValueController) ControllerUtils
				.getController(CommonValueController.class);
		String sql = "from CommonValue where codetype = ?";
		List<CommonValue> lstvalue = controller.find(sql, codetype);
		if (lstvalue == null || lstvalue.size() > 0)
			return lstvalue;
		else
			return null;
	}

	public List<RptTripSearchingOnline> displayRptTripSearchingOnline() {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());

		String stragentid = "" + (strAgentID == null ? "0" : "" + strAgentID);
		String strgroupid = "" + (strGroupID == null ? "0" : (strGroupID.length() == 0) ? "0" : "" + strGroupID);
		String strparkareaid = ""
				+ (strParkAreaID == null ? "0" : (strParkAreaID.length() == 0) ? "0" : "" + strParkAreaID);

		List<RptTripSearchingOnline> lstvalue = lstObj.getTripSearchingOnline(timedatefrom, timedateto, "admin",
				stragentid, strgroupid, strparkareaid, "" + txtLicensePlate.getValue(),
				"" + txtVehicleNumber.getValue());
		return lstvalue;

	}

	public void updateTripSearchingOnline(int shiftId, int tripId, int cash, int reason, String userId) {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		lstObj.updateTripSearchingOnline(shiftId, tripId, cash, reason, userId);
		return;

	}

	// Hàm tìm cuốc Detail
	public List<RptTripSearchingOnline> displayRptTripSearchingOnline(Timestamp datefrom, Timestamp dateto, String user,
			String agent, String group, String park, String license, String vehNum) {
		ListObjectDatabase lstObj = new ListObjectDatabase();

		List<RptTripSearchingOnline> lstvalue = lstObj.getTripSearchingOnline(datefrom, dateto, user, agent, group,
				park, license, vehNum);
		return lstvalue;

	}

	// Function Filter Data RptTripSearchingOnline with textbox
	public List<RptTripSearchingOnline> getFilterRptTripSearchingOnline(RptTripSearchingOnlineFilter filter) {
		List<RptTripSearchingOnline> lstsearch = new ArrayList<RptTripSearchingOnline>();
		String groupName = filter.getGroupName().toLowerCase();
		String vehicleNo = filter.getVehicleNumber().toLowerCase();
		String licensePlate = filter.getLicensePlate().toLowerCase();
		String placeStart = filter.getPlaceStart().toLowerCase();
		String placeFinish = filter.getPlaceFinish().toLowerCase();
		String nameDriver = filter.getNameDriver().toLowerCase();
		String typevehicle = filter.getTypeVehicle().toLowerCase();
		String strdatestart = filter.getStrdatestartfilter().toLowerCase();
		String strdatefinish = filter.getStrdatefinishfilter().toLowerCase();

		if (lstdata == null || lstdata.size() == 0) {
			Env.getHomePage().showNotification("Chưa nhập điều kiện tìm kiếm cuốc !", Clients.NOTIFICATION_TYPE_ERROR);

		} else {

			for (Iterator<RptTripSearchingOnline> i = lstdata.iterator(); i.hasNext();) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				RptTripSearchingOnline temp = i.next();
				if (temp.getGroupName().toLowerCase().contains(groupName)
						&& temp.getVehicleNumber().toLowerCase().contains(vehicleNo)
						&& temp.getLicensePlate().toLowerCase().contains(licensePlate)
						&& temp.getPlaceStart().toLowerCase().contains(placeStart)
						&& temp.getPlaceFinish().toLowerCase().contains(placeFinish)
						&& temp.getDriver().toLowerCase().contains(nameDriver)
						&& temp.getVehicleType().toLowerCase().contains(typevehicle)
						&& sdf.format(new Date(temp.getTimeStart().getTime())).toString().toLowerCase()
								.contains(strdatestart)
						&& sdf.format(new Date(temp.getTimeFinish().getTime())).toString().toLowerCase()
								.contains(strdatefinish)) {
					lstsearch.add(temp);
				}
			}
		}
		return lstsearch;
	}

	// Function Push Data to Grid after Filter Data
	@SuppressWarnings("null")
	public void pushDataFilterToGrid() {
		RptTripSearchingOnlineFilter tempfilter = new RptTripSearchingOnlineFilter();
		tempfilter.setGroupName(txtGroupNameFilter.getValue());
		tempfilter.setVehicleNumber(txtVehicleNumberFilter.getValue());
		tempfilter.setLicensePlate(txtLicensePlateFilter.getValue());
		tempfilter.setPlaceStart(txtPlaceStartFilter.getValue());
		tempfilter.setPlaceFinish(txtPlaceFinishFilter.getValue());
		tempfilter.setNameDriver(txtDriverFilter.getValue());
		tempfilter.setTypeVehicle(txtTypeVehicleFilter.getValue());
		tempfilter.setStrdatestartfilter(txtDateStartFilter.getValue());
		tempfilter.setStrdatefinishfilter(txtDateFinishFilter.getValue());

		List<RptTripSearchingOnline> lst = getFilterRptTripSearchingOnline(tempfilter);
		if (lst != null || lst.size() > 0) {
			gridSearchData.setModel(new ListModelList<RptTripSearchingOnline>(lst));
		} else {
			gridSearchData.setModel(new ListModelList<RptTripSearchingOnline>(lstdata));
		}
	}

	@Override
	public void onEvent(Event arg0) throws Exception {

	}
}