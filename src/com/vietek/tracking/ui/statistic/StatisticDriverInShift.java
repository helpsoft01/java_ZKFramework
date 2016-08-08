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
import org.zkoss.zul.Checkbox;
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
import com.vietek.taxioperation.model.ParkingArea;
import com.vietek.taxioperation.model.RptDriverInShift;
import com.vietek.taxioperation.model.RptDriverInShiftFilter;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.ui.util.ComponentsReport;
import com.vietek.taxioperation.util.Env;

public class StatisticDriverInShift extends Window implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Chosenbox chosenAgent;
	private Chosenbox chosenGroup;
	private Chosenbox chosenParkingArea;
	private Datebox datefrom;
	private Button btnSearch;
	private Grid gridSearchData;
	private StringBuilder strAgentID;
	private StringBuilder strGroupID;
	private StringBuilder strParkAreaID;
	private List<RptDriverInShift> lstdata;
	private Textbox txtVehicleNumberFilter;
	private Textbox txtLicensePlateFilter;
	private Textbox txtStateFilter;
	private Textbox txtParkingFilter;
	private Textbox txtGroupFilter;
	private Textbox txtTypeVehicleFilter;
	private Textbox txtPhoneNumberFilter;
	private Textbox txtStaffCardFilter;
	private Textbox txtDriverNameFilter;
	private Textbox txtTimeDriveContFilter;
	private Textbox txtTimeDrivePerDayFilter;
	private Textbox txtDateFilter;
	private Checkbox chbpark;
	private Checkbox chbworkshop;
	private Checkbox chbaccident;

	public StatisticDriverInShift() throws Exception {
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

		this.createStatisticDriverInShift(vlayout);

	}

	private void createStatisticDriverInShift(Vlayout vlayout) throws Exception {
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
		panel.setTitle("ĐIỀU KIỆN TÌM DANH SÁCH LÊN CA");
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
		// Tao chosenbox đội xe
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
		// Tao chosenbox bãi giao ca
		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		label = new Label("Bãi giao ca");
		label.setParent(hbox);
		label.setStyle("font-weight : bold; font-size : 14px");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 3px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		this.createChosenParkingArea(hbox);

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
				lstdata = displayRptDriverInShift();
				if (lstdata == null || lstdata.isEmpty()) {
					gridSearchData.setEmptyMessage("Không có dữ liệu");
				} else {
					gridSearchData.setModel(new ListModelList<RptDriverInShift>(lstdata));
					txtLicensePlateFilter.setValue("");
					txtVehicleNumberFilter.setValue("");
					txtStateFilter.setValue("");
					txtParkingFilter.setValue("");
					txtGroupFilter.setValue("");
					txtTypeVehicleFilter.setValue("");
					txtPhoneNumberFilter.setValue("");
					txtStaffCardFilter.setValue("");
					txtDriverNameFilter.setValue("");
					txtTimeDriveContFilter.setValue("");
					txtTimeDrivePerDayFilter.setValue("");
					txtDateFilter.setValue("");
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
		auxheader.setColspan(18);
		auxheader.setLabel("DANH SÁCH LÊN CA");
		auxheader.setStyle("color : black; font-size : 14px; font-weight : bold; margin-left : 20px");

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
		col.setLabel("Giờ lên ca");
		col.setWidth("100px");
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
		col.setLabel("Lái liên tục");
		col.setWidth("120px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Lái trong ngày");
		col.setWidth("120px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Bãi giao ca");
		col.setWidth("250px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Đội xe");
		col.setWidth("250px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Loại xe");
		col.setWidth("200px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Xe nằm bãi");
		col.setAlign("center");
		col.setWidth("100px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Xe nằm xưởng");
		col.setAlign("center");
		col.setWidth("100px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Xe nằm đoàn");
		col.setAlign("center");
		col.setWidth("100px");
		col.setStyle("color : black ; font-weight : bold");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Ghi chú");
		col.setWidth("400px");
		col.setStyle("color : black ; font-weight : bold");

		auxhead = new Auxhead();
		auxhead.setParent(gridSearchData);
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("");
		auxheader.setColspan(2);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		Image image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtDateFilter = new Textbox();
		txtDateFilter.setPlaceholder("dd/MM/yyyy");
		txtDateFilter.setWidth("80%");
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
		txtLicensePlateFilter = new Textbox();
		txtLicensePlateFilter.setPlaceholder("Tìm biển số");
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
		txtVehicleNumberFilter.setWidth("80%");
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
		txtStaffCardFilter.setWidth("80%");
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
		txtDriverNameFilter.setWidth("80%");
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
		txtPhoneNumberFilter.setWidth("80%");
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
		txtStateFilter.setWidth("80%");
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
		txtTimeDriveContFilter = new Textbox();
		txtTimeDriveContFilter.setPlaceholder("Số giờ lái");
		txtTimeDriveContFilter.setWidth("80%");
		txtTimeDriveContFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtTimeDriveContFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtTimeDriveContFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtTimeDrivePerDayFilter = new Textbox();
		txtTimeDrivePerDayFilter.setPlaceholder("Số giờ lái");
		txtTimeDrivePerDayFilter.setWidth("80%");
		txtTimeDrivePerDayFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtTimeDrivePerDayFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtTimeDrivePerDayFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtParkingFilter = new Textbox();
		txtParkingFilter.setPlaceholder("Tìm bãi giao ca");
		txtParkingFilter.setWidth("80%");
		txtParkingFilter.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {

			@Override
			public void onEvent(InputEvent event) throws Exception {
				txtParkingFilter.setValue(event.getValue());
				pushDataFilterToGrid();
			}
		});
		auxheader.appendChild(image);
		auxheader.appendChild(txtParkingFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");
		txtGroupFilter = new Textbox();
		txtGroupFilter.setPlaceholder("Tìm đội xe");
		txtGroupFilter.setWidth("80%");
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
		txtTypeVehicleFilter.setWidth("80%");
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
		auxheader.setColspan(4);

		gridSearchData.setRowRenderer(new RowRenderer<RptDriverInShift>() {

			@Override
			public void render(Row row, RptDriverInShift data, int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat timeformat = new SimpleDateFormat(" dd/MM/yyyy HH:mm");
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + dateformat.format(data.getIssueDate())));
				row.appendChild(new Label("" + timeformat.format(data.getTimeLog() == null ? "" : data.getTimeLog())));
				row.appendChild(new Label("" + data.getLicensePlate()));
				row.appendChild(new Label("" + data.getVehicleNumber()));
				row.appendChild(new Label("" + (data.getStaffCard() == null ? "" : "" + data.getStaffCard())));
				row.appendChild(new Label("" + data.getDriverName()));
				row.appendChild(new Label("" + data.getPhoneNumber()));
				row.appendChild(new Label("" + data.getDriverState()));
				row.appendChild(new Label("" + data.getTimeDrivingContinuous()));
				row.appendChild(new Label("" + data.getTimeDrivingPerDay()));
				row.appendChild(new Label("" + data.getParkingName()));
				row.appendChild(new Label("" + data.getGroupName()));
				row.appendChild(new Label("" + data.getTypeName()));
				chbpark = new Checkbox();
				chbpark.setChecked(data.getInparking() == 1 ? true : false);
				row.appendChild(chbpark);
				chbworkshop = new Checkbox();
				chbworkshop.setChecked(data.getInworkshop() == 1 ? true : false);
				row.appendChild(chbworkshop);
				chbaccident = new Checkbox();
				chbaccident.setChecked(data.getInaccident() == 1 ? true : false);
				row.appendChild(chbaccident);
				row.appendChild(new Label("" + data.getNote()));
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
	
	private void createChosenParkingArea(Hbox hbox) {
		chosenParkingArea = ComponentsReport.ChosenboxReportInput(ParkingArea.class);
		chosenParkingArea.setWidth("170px");
		chosenParkingArea.setParent(hbox);
		chosenParkingArea.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Set<ParkingArea> lstObjChosen = chosenParkingArea.getSelectedObjects();
				strParkAreaID = new StringBuilder();
				int temp = 0;
				for (ParkingArea reportQcParkingArea : lstObjChosen) {
					if (temp < lstObjChosen.size() - 1) {
						strParkAreaID.append(reportQcParkingArea.getId()).append(",");
					} else {
						strParkAreaID.append(reportQcParkingArea.getId());
					}
					temp++;
				}
			}
		});
	}

	public List<RptDriverInShift> displayRptDriverInShift() {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		String stragentid = "" + (strAgentID == null ? "0" : "" + strAgentID);
		String strgroupid = "" + (strGroupID == null ? "0" : (strGroupID.length() == 0) ? "0" : "" + strGroupID);
		String strparkareaid = ""
				+ (strParkAreaID == null ? "0" : (strParkAreaID.length() == 0) ? "0" : "" + strParkAreaID);
		List<RptDriverInShift> lstvalue = lstObj.getDriverInShift(stragentid, strgroupid, strparkareaid, timedatefrom,
				0, 0, "admin");
		return lstvalue;
	}

	// Function Filter Data with textbox
	public List<RptDriverInShift> getFilterRptDriverInShift(RptDriverInShiftFilter filter) {
		List<RptDriverInShift> lstsearch = new ArrayList<RptDriverInShift>();
		String licenseplate = filter.getLicensePlate().toLowerCase();
		String vehiclenumber = filter.getVehicleNumber().toLowerCase();
		String state = filter.getState().toLowerCase();
		String parking = filter.getParkarea().toLowerCase();
		String group = filter.getGroup().toLowerCase();
		String typeveh = filter.getTypeVehicle().toLowerCase();
		String phonenumber = filter.getMobilePhone().toLowerCase();
		String staffcard = filter.getStaffCard().toLowerCase();
		String namedriver = filter.getNameDriver().toLowerCase();
		String timedrivercont = filter.getTimeDrivingCont().toLowerCase();
		String timedreverperday = filter.getTimeDrivingPerDay().toLowerCase();
		String strdate = filter.getStrdate().toLowerCase();

		if (lstdata == null || lstdata.size() == 0) {
			Env.getHomePage().showNotification("Chưa nhập điều kiện tìm kiếm danh sách lên ca !",
					Clients.NOTIFICATION_TYPE_ERROR);

		} else {

			for (Iterator<RptDriverInShift> i = lstdata.iterator(); i.hasNext();) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				RptDriverInShift temp = i.next();
				if (temp.getLicensePlate().toLowerCase().contains(licenseplate)
						&& temp.getVehicleNumber().toLowerCase().contains(vehiclenumber)
						&& temp.getDriverState().toLowerCase().contains(state)
						&& temp.getParkingName().toLowerCase().contains(parking)
						&& temp.getGroupName().toLowerCase().contains(group)
						&& temp.getTypeName().toLowerCase().contains(typeveh)
						&& temp.getPhoneNumber().toLowerCase().contains(phonenumber)
						&& temp.getStaffCard().toLowerCase().contains(staffcard)
						&& temp.getDriverName().toLowerCase().contains(namedriver)
						&& temp.getTimeDrivingContinuous().toLowerCase().contains(timedrivercont)
						&& temp.getTimeDrivingPerDay().toLowerCase().contains(timedreverperday)
						&& sdf.format(new Date(temp.getTimeLog().getTime())).toString().toLowerCase()
								.contains(strdate)) {
					lstsearch.add(temp);
				}
			}
		}
		return lstsearch;
	}

	// Function Push Data to Grid after Filter Data
	@SuppressWarnings("null")
	public void pushDataFilterToGrid() {
		RptDriverInShiftFilter tempfilter = new RptDriverInShiftFilter();
		tempfilter.setLicensePlate(txtLicensePlateFilter.getValue());
		tempfilter.setVehicleNumber(txtVehicleNumberFilter.getValue());
		tempfilter.setState(txtStateFilter.getValue());
		tempfilter.setParkarea(txtParkingFilter.getValue());
		tempfilter.setGroup(txtGroupFilter.getValue());
		tempfilter.setTypeVehicle(txtTypeVehicleFilter.getValue());
		tempfilter.setMobilePhone(txtPhoneNumberFilter.getValue());
		tempfilter.setStaffCard(txtStaffCardFilter.getValue());
		tempfilter.setNameDriver(txtDriverNameFilter.getValue());
		tempfilter.setTimeDrivingCont(txtTimeDriveContFilter.getValue());
		tempfilter.setTimeDrivingPerDay(txtTimeDrivePerDayFilter.getValue());
		tempfilter.setStrdate(txtDateFilter.getValue());
		List<RptDriverInShift> lst = getFilterRptDriverInShift(tempfilter);
		if (lst != null || lst.size() > 0) {
			gridSearchData.setModel(new ListModelList<RptDriverInShift>(lst));
		} else {
			gridSearchData.setModel(new ListModelList<RptDriverInShift>(lstdata));
		}
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
	}
}