package com.vietek.tracking.ui.statistic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.RptDriverInShiftLog;
import com.vietek.taxioperation.model.RptDriverInShiftLogFilter;
import com.vietek.taxioperation.model.RptTripSearchingOnline;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.ui.util.ComponentsReport;
import com.vietek.taxioperation.util.Env;

public class StatisticDriverInShiftLog extends Window implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Chosenbox chosenAgent;
	private Chosenbox chosenGroup;
	private Datebox datefrom;
	private Datebox dateto;
	private Button btnSearch;
	private Grid gridSearchData;

	private StringBuilder strAgentID;
	private StringBuilder strGroupID;
	private List<RptDriverInShiftLog> lstdata;

	private Textbox txtDateFilter;
	private Textbox txtTimeFilter;
	private Textbox txtLicensePlateFilter;
	private Textbox txtVehicleNumberFilter;
	private Textbox txtStaffCardFilter;
	private Textbox txtDriverNameFilter;
	private Textbox txtPhoneNumberFilter;
	private Textbox txtStateFilter;
	private Textbox txtGroupFilter;
	private Textbox txtTypeVehicleFilter;
	private Textbox txtParkingFilter;

	public StatisticDriverInShiftLog() throws Exception {
		this.init();
	}

	public void init() throws Exception {
		this.setShadow(true);
		this.setHflex("1");
		this.setVflex("1");
		this.setClosable(true);

		Vlayout vlayout = new Vlayout();
		vlayout.setParent(this);
		vlayout.setHflex("100%");
		vlayout.setVflex("100%");

		this.createStatisticDriverInShiftLog(vlayout);

	}

	private void createStatisticDriverInShiftLog(Vlayout vlayout) throws Exception {
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
		panel.setTitle("ĐIỀU KIỆN TÌM LỊCH SỬ LÊN CA");
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

		// tao cac chosenbox
		Hlayout hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 12px; margin-left : 1px");

		Hbox hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		Label label = new Label("Chi nhánh");
		label.setParent(hbox);
		label.setStyle("font-weight : bold; font-size : 14px; color : black");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 9px");
		hbox = new Hbox();
		hbox.setParent(div);

		this.createChosenAgent(hbox);
		// Tao chosenbox Đội xe
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
		label.setStyle("font-weight : bold; font-size : 14px");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 36px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		this.createChosenGroup(hbox);

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
		datefrom.setConstraint("no empty : Không để trống");

		// Đến ngày
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
		dateto.setConstraint("no empty : Không để trống");

		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 168px");
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
					Env.getHomePage().showNotification("Hãy chọn chi nhánh tìm kiếm !",
							Clients.NOTIFICATION_TYPE_ERROR);
				} else if (timedateto < timedatefrom) {
					Env.getHomePage().showNotification("Hãy chọn lại thời gian tìm kiếm",
							Clients.NOTIFICATION_TYPE_ERROR);
					List<RptTripSearchingOnline> lstData = new ArrayList<RptTripSearchingOnline>();
					gridSearchData.setEmptyMessage("Không có dữ liệu !");
					gridSearchData.setModel(new ListModelList<RptTripSearchingOnline>(lstData));
				} else {
					lstdata = displayRptDriverInShiftLog();
					if (lstdata == null || lstdata.isEmpty()) {
						gridSearchData.setEmptyMessage("Không có dữ liệu");
					} else {
						gridSearchData.setModel(new ListModelList<RptDriverInShiftLog>(lstdata));
						txtDateFilter.setValue("");
						txtTimeFilter.setValue("");
						txtLicensePlateFilter.setValue("");
						txtVehicleNumberFilter.setValue("");
						txtStaffCardFilter.setValue("");
						txtDriverNameFilter.setValue("");
						txtPhoneNumberFilter.setValue("");
						txtStateFilter.setValue("");
						txtGroupFilter.setValue("");
						txtTypeVehicleFilter.setValue("");
						txtParkingFilter.setValue("");
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
		auxheader.setColspan(12);
		auxheader.setLabel("LỊCH SỬ LÊN CA");
		auxheader.setStyle("color : black; font-size : 14px; font-weight : bold; margin-left : 50px");

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
		col.setLabel("Ngày");
		col.setWidth("100px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Thời điểm");
		col.setWidth("90px");
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
		col.setLabel("Số tài");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("MSNV");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Tài xế");
		col.setWidth("160px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Điện thoại");
		col.setWidth("100px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Trạng thái");
		col.setWidth("120px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Đội xe");
		col.setWidth("220px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Loại xe");
		col.setWidth("220px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Bãi giao ca");
		col.setWidth("280px");
		col.setStyle("color : black ; font-weight : bold");

		// Header Filter
		auxhead = new Auxhead();
		auxhead.setParent(gridSearchData);
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("");
		auxheader.setColspan(1);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		Image image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtDateFilter = new Textbox();
		txtDateFilter.setPlaceholder("dd/MM/yyyy");
		txtDateFilter.setWidth("85%");
		txtDateFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtDateFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtDateFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtTimeFilter = new Textbox();
		txtTimeFilter.setPlaceholder("HH:mm");
		txtTimeFilter.setWidth("85%");
		txtTimeFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtTimeFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtTimeFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtLicensePlateFilter = new Textbox();
		txtLicensePlateFilter.setPlaceholder("Tìm BKS");
		txtLicensePlateFilter.setWidth("85%");
		txtLicensePlateFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtLicensePlateFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtLicensePlateFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtVehicleNumberFilter = new Textbox();
		txtVehicleNumberFilter.setPlaceholder("Số tài");
		txtVehicleNumberFilter.setWidth("85%");
		txtVehicleNumberFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtVehicleNumberFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtVehicleNumberFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtStaffCardFilter = new Textbox();
		txtStaffCardFilter.setPlaceholder("MSNV");
		txtStaffCardFilter.setWidth("85%");
		txtStaffCardFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtStaffCardFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtStaffCardFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtDriverNameFilter = new Textbox();
		txtDriverNameFilter.setPlaceholder("Tìm lái xe");
		txtDriverNameFilter.setWidth("90%");
		txtDriverNameFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtDriverNameFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtDriverNameFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtPhoneNumberFilter = new Textbox();
		txtPhoneNumberFilter.setPlaceholder("SĐT");
		txtPhoneNumberFilter.setWidth("85%");
		txtPhoneNumberFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtPhoneNumberFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtPhoneNumberFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtStateFilter = new Textbox();
		txtStateFilter.setPlaceholder("Trạng thái");
		txtStateFilter.setWidth("90%");
		txtStateFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtStateFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtStateFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtGroupFilter = new Textbox();
		txtGroupFilter.setPlaceholder("Tìm đội xe");
		txtGroupFilter.setWidth("95%");
		txtGroupFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtGroupFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtGroupFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtTypeVehicleFilter = new Textbox();
		txtTypeVehicleFilter.setPlaceholder("Tìm loại xe");
		txtTypeVehicleFilter.setWidth("95%");
		txtTypeVehicleFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtTypeVehicleFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtTypeVehicleFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtParkingFilter = new Textbox();
		txtParkingFilter.setPlaceholder("Tìm bãi giao ca");
		txtParkingFilter.setWidth("95%");
		txtParkingFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtParkingFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtParkingFilter);

		gridSearchData.setRowRenderer(new RowRenderer<RptDriverInShiftLog>() {

			@Override
			public void render(Row row, RptDriverInShiftLog data, int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + dateformat.format(data.getTimeLog() == null ? "" : data.getTimeLog())));
				row.appendChild(new Label("" + timeformat.format(data.getTimeLog() == null ? "" : data.getTimeLog())));
				row.appendChild(new Label("" + data.getLicensePlate()));
				row.appendChild(new Label("" + data.getVehicleNumber()));
				row.appendChild(new Label("" + (data.getStaffCard() == null ? "" : "" + data.getStaffCard())));
				row.appendChild(new Label("" + data.getDriverName() == null ? "" : "" + data.getDriverName()));
				row.appendChild(new Label("" + data.getPhoneNumber() == null ? "" : "" + data.getPhoneNumber()));
				row.appendChild(new Label("" + data.getDriverState()));
				row.appendChild(new Label("" + data.getGroupName()));
				row.appendChild(new Label("" + data.getTypeName()));
				row.appendChild(new Label("" + data.getParkingName()));

			}
		});

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

	private void createChosenAgent(Hbox hbox) {
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

	private void createChosenGroup(Hbox hbox) {
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

	public List<RptDriverInShiftLog> displayRptDriverInShiftLog() {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());
		String stragentid = "" + (strAgentID == null ? "0" : "" + strAgentID);
		String strgroupid = "" + (strGroupID == null ? "0" : (strGroupID.length() == 0) ? "0" : "" + strGroupID);
		List<RptDriverInShiftLog> lstvalue = lstObj.getDriverInShiftLog(stragentid, strgroupid, "0", timedatefrom,
				timedateto, "admin");
		return lstvalue;
	}

	// Function Filter Data with textbox
	public List<RptDriverInShiftLog> getFilterRptDriverInShiftLog(RptDriverInShiftLogFilter filter) {
		List<RptDriverInShiftLog> lstsearch = new ArrayList<RptDriverInShiftLog>();
		String date = filter.getDateFilter().toLowerCase();
		String time = filter.getTimeFilter().toLowerCase();
		String licensePlate = filter.getLicensePlateFilter().toLowerCase();
		String vehicleNum = filter.getVehicleNumberFilter().toLowerCase();
		String staffCard = filter.getStaffCardFilter().toLowerCase();
		String driverName = filter.getDriverNameFilter().toLowerCase();
		String phone = filter.getPhoneNumberFilter().toLowerCase();
		String state = filter.getStateFilter().toLowerCase();
		String group = filter.getGroupFilter().toLowerCase();
		String typeVehicle = filter.getTypeVehicleFilter().toLowerCase();
		String parking = filter.getParkingFilter().toLowerCase();

		if (lstdata == null || lstdata.size() == 0) {
			Env.getHomePage().showNotification("Chưa nhập điều kiện tìm kiếm lịch sử lên ca !",
					Clients.NOTIFICATION_TYPE_ERROR);

		} else {

			for (Iterator<RptDriverInShiftLog> i = lstdata.iterator(); i.hasNext();) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
				RptDriverInShiftLog temp = i.next();
				if (temp.getLicensePlate().toLowerCase().contains(licensePlate)
						&& temp.getVehicleNumber().toLowerCase().contains(vehicleNum)
						&& temp.getStaffCard().toLowerCase().contains(staffCard)
						&& temp.getDriverName().toLowerCase().contains(driverName)
						&& temp.getPhoneNumber().toLowerCase().contains(phone)
						&& temp.getDriverState().toLowerCase().contains(state)
						&& temp.getGroupName().toLowerCase().contains(group)
						&& temp.getTypeName().toLowerCase().contains(typeVehicle)
						&& temp.getParkingName().toLowerCase().contains(parking)
						&& sdf.format(new Date(temp.getTimeLog().getTime())).toString().toLowerCase().contains(date)
						&& timeformat.format(new Date(temp.getTimeLog().getTime())).toString().toLowerCase()
								.contains(time)) {
					lstsearch.add(temp);
				}
			}
		}
		return lstsearch;
	}

	// Function Push Data to Grid after Filter Data
	@SuppressWarnings("null")
	public void pushDataFilterToGrid() {
		RptDriverInShiftLogFilter tempfilter = new RptDriverInShiftLogFilter();
		tempfilter.setDateFilter(txtDateFilter.getValue());
		tempfilter.setTimeFilter(txtTimeFilter.getValue());
		tempfilter.setLicensePlateFilter(txtLicensePlateFilter.getValue());
		tempfilter.setVehicleNumberFilter(txtVehicleNumberFilter.getValue());
		tempfilter.setStaffCardFilter(txtStaffCardFilter.getValue());
		tempfilter.setDriverNameFilter(txtDriverNameFilter.getValue());
		tempfilter.setPhoneNumberFilter(txtPhoneNumberFilter.getValue());
		tempfilter.setStateFilter(txtStateFilter.getValue());
		tempfilter.setGroupFilter(txtGroupFilter.getValue());
		tempfilter.setTypeVehicleFilter(txtTypeVehicleFilter.getValue());
		tempfilter.setParkingFilter(txtParkingFilter.getValue());
		List<RptDriverInShiftLog> lst = getFilterRptDriverInShiftLog(tempfilter);
		if (lst != null || lst.size() > 0) {
			gridSearchData.setModel(new ListModelList<RptDriverInShiftLog>(lst));
		} else {
			gridSearchData.setModel(new ListModelList<RptDriverInShiftLog>(lstdata));
		}
	}

	@Override
	public void onEvent(Event arg0) throws Exception {

	}

}