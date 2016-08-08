package com.vietek.tracking.ui.statistic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Detail;
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
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.RptGetShiftInfo;
import com.vietek.taxioperation.model.RptGetShiftInfoFilter;
import com.vietek.taxioperation.model.RptTripSearchingOnline;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.ui.util.ComboboxRender;
import com.vietek.taxioperation.ui.util.ComponentsReport;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.tracking.ui.utility.TrackingHistory;

public class StatisticShiftInfo extends Window implements
		EventListener<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Chosenbox chosenAgent;
	private Chosenbox chosenGroup;
	private Datebox datefrom;
	private Datebox dateto;
	private Combobox cboState;
	private Button btnSearch;
	private Grid gridSearchData;
	//private Grid gridDetail;
	private Button btnExcel;
//	private Button btnPrintTrip;
//	private Button btnHistory;
//	private Checkbox chkbox;
//	private Detail detail;
	private List<RptGetShiftInfo> lstdata;
//	private List<RptTripSearchingOnline> lstdataDetail;
	private StringBuilder strAgentID;
	private StringBuilder strGroupID;

	private Textbox txtTimeBeginFilter;
	private Textbox txtTimeEndFilter;
	private Textbox txtVehicleNumberFilter;
	private Textbox txtLicensePlateFilter;
	private Textbox txtDriverNameFilter;
	private Textbox txtStaffCardFilter;
	private Textbox txtTripNumberFilter;
	private Textbox txtBusinessTypeFilter;
	private Textbox txtPrintFilter;
	private Textbox txtPulseCutFilter;
	private Textbox txtPulseLanceFilter;
	private Textbox txtGpsLostFilter;
	private Textbox txtPowerLostFilter;

	public StatisticShiftInfo() throws Exception {
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

		this.createStatisticGetShiftInfo(vlayout);

	}

	private void createStatisticGetShiftInfo(Vlayout vlayout) throws Exception {
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
		panel.setTitle("ĐIỀU KIỆN TÌM KIẾM");
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

		// Tao Combobox Trạng thái
		hl = new Hlayout();
		hl.setParent(vlayout);
		hl.setHeight("35px");
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		label = new Label("Trạng thái");
		label.setParent(hbox);
		label.setStyle("font-weight : bold; font-size : 14px");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 10px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		this.createCboState(hbox);

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
		div.setStyle("margin-top : 10px; margin-left: 30px");
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
					Env.getHomePage().showNotification(
							"Hãy chọn chi nhánh tìm kiếm !",
							Clients.NOTIFICATION_TYPE_ERROR);
				} else if (timedateto < timedatefrom) {
					Env.getHomePage().showNotification(
							"Hãy chọn lại thời gian tìm kiếm",
							Clients.NOTIFICATION_TYPE_ERROR);
					List<RptGetShiftInfo> lstData = new ArrayList<RptGetShiftInfo>();
					gridSearchData.setEmptyMessage("Không có dữ liệu !");
					gridSearchData.setModel(new ListModelList<RptGetShiftInfo>(
							lstData));
				} else {
					lstdata = displayRptGetShiftInfo();
					if (lstdata == null || lstdata.isEmpty()) {
						gridSearchData.setEmptyMessage("Không có dữ liệu");
					} else {
						gridSearchData
								.setModel(new ListModelList<RptGetShiftInfo>(
										lstdata));
						txtTimeBeginFilter.setValue("");
						txtTimeEndFilter.setValue("");
						txtVehicleNumberFilter.setValue("");
						txtLicensePlateFilter.setValue("");
						txtDriverNameFilter.setValue("");
						txtStaffCardFilter.setValue("");
						txtTripNumberFilter.setValue("");
						txtBusinessTypeFilter.setValue("");
						txtPrintFilter.setValue("");
						txtPulseCutFilter.setValue("");
						txtPulseLanceFilter.setValue("");
						txtGpsLostFilter.setValue("");
						txtPowerLostFilter.setValue("");
					}
				}
			}
		});

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 10px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		btnExcel = new Button();
		btnExcel.setLabel("Xuất Excel");
		btnExcel.setStyle("color: black; font-weight: bold; color : black");
		btnExcel.setParent(hbox);
		
		btnExcel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				CommonUtils.exportListboxToExcel(gridSearchData,
						"bao_cao_chi_tiet_du_lieu_chot_ca_lam_viec.xlsx");
			}
		});
		
		
	}

	private void createData(Hlayout hlayout) throws Exception {
		gridSearchData = new Grid();
		gridSearchData.setParent(hlayout);
		gridSearchData.setHflex("7.5");
		gridSearchData.setVflex("1");
		gridSearchData.setStyle("overflow: auto !important;");
		gridSearchData.setMold("paging");
		gridSearchData.setSpan(true);
		gridSearchData.setSizedByContent(true);

		Auxhead auxhead = new Auxhead();
		auxhead.setParent(gridSearchData);
		Auxheader auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setColspan(24);
		auxheader.setLabel("DỮ LIỆU CHỐT CA");
		auxheader
				.setStyle("color : black; font-size : 14px; font-weight : bold; margin-left : 50px");
		Columns cols = new Columns();
		cols.setParent(gridSearchData);

		Column col = new Column();
		col.setParent(cols);
		col.setLabel("");
		col.setWidth("50px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("TT");
		col.setWidth("40px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("In cuốc");
		col.setWidth("80px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Bắt đầu ca");
		col.setWidth("150px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Kết thúc ca");
		col.setWidth("150px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Số tài");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("BKS");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Tài xế");
		col.setWidth("150px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("MSNV");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Giờ KD");
		col.setWidth("60px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("KMVD");
		col.setWidth("60px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("KMCK");
		col.setWidth("60px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Số cuốc");
		col.setWidth("80px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Tiền Đ.Hồ");
		col.setWidth("80px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("right");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Giảm trừ");
		col.setWidth("80px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("right");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Tiền ca");
		col.setWidth("80px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("right");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Đã thu tiền");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Số lưu ĐHTT");
		col.setWidth("100px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Loại hình");
		col.setWidth("90px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Số lần in");
		col.setWidth("130px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Số lần chích xung");
		col.setWidth("130px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Số lần kích xung");
		col.setWidth("130px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Số lần lỗi GPS");
		col.setWidth("130px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Số lần lỗi nguồn");
		col.setWidth("130px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		auxhead = new Auxhead();
		auxhead.setParent(gridSearchData);
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setColspan(3);
		auxheader.setLabel("");

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		Image image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtTimeBeginFilter = new Textbox();
		txtTimeBeginFilter.setPlaceholder("dd/MM/yyyy");
		txtTimeBeginFilter.setWidth("90%");
		txtTimeBeginFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

					@Override
					public void onEvent(InputEvent event) throws Exception {
						txtTimeBeginFilter.setValue(event.getValue());
						pushDataFilterToGrid();
					}
				});

		auxheader.appendChild(image);
		auxheader.appendChild(txtTimeBeginFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtTimeEndFilter = new Textbox();
		txtTimeEndFilter.setPlaceholder("dd/MM/yyyy");
		txtTimeEndFilter.setWidth("90%");
		txtTimeEndFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

					@Override
					public void onEvent(InputEvent event) throws Exception {
						txtTimeEndFilter.setValue(event.getValue());
						pushDataFilterToGrid();
					}
				});

		auxheader.appendChild(image);
		auxheader.appendChild(txtTimeEndFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtVehicleNumberFilter = new Textbox();
		txtVehicleNumberFilter.setPlaceholder("Số tài");
		txtVehicleNumberFilter.setWidth("85%");
		txtVehicleNumberFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

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

		txtLicensePlateFilter = new Textbox();
		txtLicensePlateFilter.setPlaceholder("BKS");
		txtLicensePlateFilter.setWidth("85%");
		txtLicensePlateFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

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

		txtDriverNameFilter = new Textbox();
		txtDriverNameFilter.setPlaceholder("Tài xế");
		txtDriverNameFilter.setWidth("90%");
		txtDriverNameFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

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

		txtStaffCardFilter = new Textbox();
		txtStaffCardFilter.setPlaceholder("MSNV");
		txtStaffCardFilter.setWidth("85%");
		txtStaffCardFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

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
		auxheader.setColspan(3);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtTripNumberFilter = new Textbox();
		txtTripNumberFilter.setPlaceholder("Số cuốc");
		txtTripNumberFilter.setWidth("85%");
		txtTripNumberFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

					@Override
					public void onEvent(InputEvent event) throws Exception {
						txtTripNumberFilter.setValue(event.getValue());
						pushDataFilterToGrid();
					}
				});

		auxheader.appendChild(image);
		auxheader.appendChild(txtTripNumberFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setColspan(5);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtBusinessTypeFilter = new Textbox();
		txtBusinessTypeFilter.setPlaceholder("Loại hình");
		txtBusinessTypeFilter.setWidth("85%");
		txtBusinessTypeFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

					@Override
					public void onEvent(InputEvent event) throws Exception {
						txtBusinessTypeFilter.setValue(event.getValue());
						pushDataFilterToGrid();
					}
				});

		auxheader.appendChild(image);
		auxheader.appendChild(txtBusinessTypeFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtPrintFilter = new Textbox();
		txtPrintFilter.setPlaceholder("Số lần in");
		txtPrintFilter.setWidth("90%");
		txtPrintFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

					@Override
					public void onEvent(InputEvent event) throws Exception {
						txtPrintFilter.setValue(event.getValue());
						pushDataFilterToGrid();
					}
				});

		auxheader.appendChild(image);
		auxheader.appendChild(txtPrintFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtPulseCutFilter = new Textbox();
		txtPulseCutFilter.setPlaceholder("Số lần chích xung");
		txtPulseCutFilter.setWidth("90%");
		txtPulseCutFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

					@Override
					public void onEvent(InputEvent event) throws Exception {
						txtPulseCutFilter.setValue(event.getValue());
						pushDataFilterToGrid();
					}
				});

		auxheader.appendChild(image);
		auxheader.appendChild(txtPulseCutFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtPulseLanceFilter = new Textbox();
		txtPulseLanceFilter.setPlaceholder("Số lần kích xung");
		txtPulseLanceFilter.setWidth("90%");
		txtPulseLanceFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

					@Override
					public void onEvent(InputEvent event) throws Exception {
						txtPulseLanceFilter.setValue(event.getValue());
						pushDataFilterToGrid();
					}
				});

		auxheader.appendChild(image);
		auxheader.appendChild(txtPulseLanceFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtGpsLostFilter = new Textbox();
		txtGpsLostFilter.setPlaceholder("Số lỗi GPS");
		txtGpsLostFilter.setWidth("90%");
		txtGpsLostFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

					@Override
					public void onEvent(InputEvent event) throws Exception {
						txtGpsLostFilter.setValue(event.getValue());
						pushDataFilterToGrid();
					}
				});

		auxheader.appendChild(image);
		auxheader.appendChild(txtGpsLostFilter);

		auxheader = new Auxheader();
		auxheader.setParent(auxhead);

		image = new Image();
		image.setSrc("./themes/images/filter.png");
		image.setWidth("15px");

		txtPowerLostFilter = new Textbox();
		txtPowerLostFilter.setPlaceholder("Số lỗi nguồn");
		txtPowerLostFilter.setWidth("90%");
		txtPowerLostFilter.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {

					@Override
					public void onEvent(InputEvent event) throws Exception {
						txtPowerLostFilter.setValue(event.getValue());
						pushDataFilterToGrid();
					}
				});

		auxheader.appendChild(image);
		auxheader.appendChild(txtPowerLostFilter);

		gridSearchData.setRowRenderer(new RowRenderer<RptGetShiftInfo>() {

			@Override
			public void render(Row row, RptGetShiftInfo data, int index)
					throws Exception {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				Detail detail = new Detail();
				row.appendChild(detail);
				row.appendChild(new Label("" + ++index));
				Button btnPrintTrip = new Button("In cuốc");
				btnPrintTrip
						.setStyle("color : black;font-size : 14px, font-weight : bold");
				btnPrintTrip.setHeight("28px");
				row.appendChild(btnPrintTrip);
				row.appendChild(new Label("" + sdf.format(data.getTimeBegin())));
				row.appendChild(new Label("" + sdf.format(data.getTimeEnd())));
				row.appendChild(new Label("" + data.getVehicleNumber()));
				row.appendChild(new Label("" + data.getLicensePlate()));
				row.appendChild(new Label("" + data.getDriverName()));
				row.appendChild(new Label("" + data.getStaffCard()));
				row.appendChild(new Label("" + data.getHourOfShift()));
				row.appendChild(new Label("" + data.getPath()));
				row.appendChild(new Label("" + data.getTripPath()));
				row.appendChild(new Label("" + data.getTripNumber()));
				row.appendChild(new Label(""
						+ IntFormat.formatTypeInt("###,###", data.getMoney())));
				row.appendChild(new Label("" + data.getReduceMoney()));
				row.appendChild(new Label(""
						+ IntFormat.formatTypeInt("###,###",
								data.getRealShiftMoney())));
				Checkbox chkbox = new Checkbox();
				chkbox.setChecked(data.getConfirm() == 1 ? true : false);
				row.appendChild(chkbox);
				row.appendChild(new Label("" + IntFormat.formatTypeInt("###,###", data.getMoneyMeter())));
				row.appendChild(new Label("" + data.getBusinessType()));
				row.appendChild(new Label("" + data.getPrint()));
				row.appendChild(new Label("" + data.getPulseCut()));
				row.appendChild(new Label("" + data.getPulseLance()));
				row.appendChild(new Label("" + data.getGpsLost()));
				row.appendChild(new Label("" + data.getPowerLost()));
				btnPrintTrip.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {					
						Row row = (Row) btnPrintTrip.getParent();
						List<Component> components = row.getChildren();						
						for (Component component : components) {
							if (component instanceof Detail) {
								Detail detail = (Detail) component;
								List<Component> components2 = detail.getChildren();
								for (Component component2 : components2) {
									if (component2 instanceof Grid) {
										Grid grid = (Grid) component2;
										
										List<Component> components3 = grid.getChildren();
										for(Component component3 : components3){
											if(component3 instanceof Auxhead){
												grid.removeChild(component3);
												break;
											}
										}
										
										if (grid.getModel() != null) {
											CommonUtils.exportListboxToExcel(grid, "bao_cao_danh_sach_cuoc_khach_chi_tiet.xlsx");
										}
										break;
									}
								}
								break;
							}
						}
					}
				});				
				detail.addEventListener(Events.ON_OPEN, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						List<Component> components = detail.getChildren();
						Grid grid = null;
						for (Component component : components) {
							if (component instanceof Grid) {
								grid = (Grid) component;
								break;
							}
						}
						createGridDetail(detail, grid, data.getShiftId());
					}
				});

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

	private void createCboState(Hbox hbox) {
		String[] titles = { "Chưa thu tiền", "Đã thu tiền", "Chọn tất cả" };
		int[] values = { 0, 1, 2 };
		ComboboxRender renderCbo = new ComboboxRender();
		cboState = renderCbo.ComboboxRendering(titles, values, "", "", 170, 8,
				false);
		cboState.setParent(hbox);
		cboState.setSclass("comboboxtripsearching");

	}

	public void createGridDetail(Detail detail, Grid gridDetail, int shiftId) {		
		if (gridDetail == null) {
			gridDetail = new Grid();
			gridDetail.setParent(detail);
			gridDetail.setWidth("2040px");
			gridDetail.setHeight("100%");
			gridDetail.setMold("paging");
			gridDetail.setPageSize(5);
			gridDetail.setSpan(true);
			gridDetail.setSizedByContent(true);
			//gridDetail.setAutopaging(true);

			Auxhead auxhead = new Auxhead();
			auxhead.setParent(gridDetail);
			Auxheader auxheader = new Auxheader();
			auxheader.setParent(auxhead);
			auxheader.setColspan(14);
			auxheader.setLabel("DỮ LIỆU CHI TIẾT");
			auxheader.setStyle("color : black; font-size : 12px; font-weight : bold; margin-left : 50px");
			Columns cols = new Columns();
			cols.setParent(gridDetail);

			Column col = new Column();
			col.setParent(cols);
			col.setLabel("TT");
			col.setWidth("40px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("center");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Giờ đón");
			col.setWidth("150px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Giờ trả");
			col.setWidth("150px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Tài xế");
			col.setWidth("150px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("MSNV");
			col.setWidth("90px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Tiền ĐH");
			col.setWidth("80px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("right");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Giảm trừ");
			col.setWidth("80px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("right");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Tiền cuốc");
			col.setWidth("80px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("right");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Lý do");
			col.setWidth("100px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("center");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Km");
			col.setWidth("60px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("center");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Km rỗng");
			col.setWidth("60px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("center");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Điểm đón");
			col.setWidth("450px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Điểm trả");
			col.setWidth("450px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Hành trình");
			col.setWidth("100px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("center");

			gridDetail.setRowRenderer(new RowRenderer<RptTripSearchingOnline>() {

				@Override
				public void render(Row row, RptTripSearchingOnline data, int index)
						throws Exception {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"dd-MM-yyyy HH:mm");
					row.appendChild(new Label("" + ++index));
					row.appendChild(new Label(""
							+ dateformat.format(data.getTimeStart())));
					row.appendChild(new Label(""
							+ dateformat.format(data.getTimeFinish())));
					row.appendChild(new Label(""
							+ (data.getDriver().trim().toLowerCase() == null ? ""
									: "" + data.getDriver())));
					row.appendChild(new Label("" + data.getStaffCard()));
					row.appendChild(new Label(""
							+ IntFormat.formatTypeInt("###,###", data.getClock())));
					row.appendChild(new Label("" + data.getReduce()));
					row.appendChild(new Label(""
							+ IntFormat.formatTypeInt("###,###",
									data.getPriceTrip())));
					row.appendChild(new Label("" + data.getReason()));
					row.appendChild(new Label(""
							+ MathForRound.round(data.getKm(), 1)));
					row.appendChild(new Label(""
							+ MathForRound.round(data.getEmpKm(), 1)));
					row.appendChild(new Label("" + data.getPlaceStart()));
					row.appendChild(new Label("" + data.getPlaceFinish()));
					Button btnHistory = new Button("Lịch sử");
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
		
		List<RptTripSearchingOnline> lstdataDetail = displayRptTripSearchingOnlineDetail(shiftId);
		if (lstdataDetail == null || lstdataDetail.isEmpty()) {
			gridDetail.setEmptyMessage("Không có dữ liệu");
		} else {
			gridDetail.setModel(new ListModelList<RptTripSearchingOnline>(lstdataDetail));
		}

	}

	private void showHistory(RptTripSearchingOnline data) {
		TrackingHistory history = new TrackingHistory(new java.sql.Date(data.getTimeStart()
				.getTime()), new java.sql.Date(data.getTimeFinish().getTime()),
				data.getVehicleId());
		Window window = new Window();
		window.setWidth("1000px");
		window.setHeight("1000px");
		window.setClosable(true);
		window.setTitle("Lịch sử cuốc chi tiết");
		window.setParent(this);
		window.appendChild(history);
		window.doModal();

	}

	public List<RptGetShiftInfo> displayRptGetShiftInfo() {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());
		Comboitem item = cboState.getSelectedItem();
		int payment = item.getValue();

		String stragentid = "" + (strAgentID == null ? "0" : "" + strAgentID);
		String strgroupid = ""
				+ (strGroupID == null ? "0" : (strGroupID.length() == 0) ? "0"
						: "" + strGroupID);
		List<RptGetShiftInfo> lstvalue = lstObj.getShiftInfo(timedatefrom,
				timedateto, stragentid, strgroupid, "0", payment, "admin");
		return lstvalue;

	}

	// Hàm tìm cuốc Detail
	public List<RptTripSearchingOnline> displayRptTripSearchingOnlineDetail(
			int shiftId) {
		ListObjectDatabase lstObj = new ListObjectDatabase();

		List<RptTripSearchingOnline> lstvalue = lstObj
				.getTripSearchingOnlineDetail(shiftId);
		return lstvalue;

	}

	// Function Push Data to Grid after Filter Data
	@SuppressWarnings("null")
	public void pushDataFilterToGrid() {
		RptGetShiftInfoFilter tempfilter = new RptGetShiftInfoFilter();
		tempfilter.setTimeBeginFilter(txtTimeBeginFilter.getValue());
		tempfilter.setTimeEndFilter(txtTimeEndFilter.getValue());
		tempfilter.setVehicleNumberFilter(txtVehicleNumberFilter.getValue());
		tempfilter.setLicensePlateFilter(txtLicensePlateFilter.getValue());
		tempfilter.setDriverNameFilter(txtDriverNameFilter.getValue());
		tempfilter.setStaffCardFilter(txtStaffCardFilter.getValue());
		tempfilter.setTripNumberFilter(txtTripNumberFilter.getValue());
		tempfilter.setBusinessTypeFilter(txtBusinessTypeFilter.getValue());
		tempfilter.setPrintFilter(txtPrintFilter.getValue());
		tempfilter.setPulseCutFilter(txtPulseCutFilter.getValue());
		tempfilter.setPulseLanceFilter(txtPulseLanceFilter.getValue());
		tempfilter.setGpsLostFilter(txtGpsLostFilter.getValue());
		tempfilter.setPowerLostFilter(txtPowerLostFilter.getValue());

		List<RptGetShiftInfo> lst = getFilterRptGetShiftInfo(tempfilter);
		if (lst != null || lst.size() > 0) {
			gridSearchData.setModel(new ListModelList<RptGetShiftInfo>(lst));
		} else {
			gridSearchData
					.setModel(new ListModelList<RptGetShiftInfo>(lstdata));
		}
	}

	// Function Filter Data RptGetShiftInfo with textbox
	public List<RptGetShiftInfo> getFilterRptGetShiftInfo(
			RptGetShiftInfoFilter filter) {
		List<RptGetShiftInfo> lstsearch = new ArrayList<RptGetShiftInfo>();
		String timeBegin = filter.getTimeBeginFilter().toLowerCase();
		String timeEnd = filter.getTimeEndFilter().toLowerCase();
		String vehicleNum = filter.getVehicleNumberFilter().toLowerCase();
		String licensePlate = filter.getLicensePlateFilter().toLowerCase();
		String driverName = filter.getDriverNameFilter().toLowerCase();
		String staffCard = filter.getStaffCardFilter().toLowerCase();
		String tripNum = filter.getTripNumberFilter().toLowerCase();
		String businessType = filter.getBusinessTypeFilter().toLowerCase();
		String printNum = filter.getPrintFilter().toLowerCase();
		String pulseCut = filter.getPulseCutFilter().toLowerCase();
		String pulseLance = filter.getPulseLanceFilter().toLowerCase();
		String gpsLost = filter.getGpsLostFilter().toLowerCase();
		String powerLost = filter.getPowerLostFilter().toLowerCase();

		if (lstdata == null || lstdata.size() == 0) {
			Env.getHomePage().showNotification(
					"Chưa nhập điều kiện tìm kiếm cuốc !",
					Clients.NOTIFICATION_TYPE_ERROR);

		} else {

			for (Iterator<RptGetShiftInfo> i = lstdata.iterator(); i.hasNext();) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				RptGetShiftInfo temp = i.next();
				if (temp.getVehicleNumber().toLowerCase().contains(vehicleNum)
						&& temp.getLicensePlate().toLowerCase()
								.contains(licensePlate)
						&& temp.getDriverName().toLowerCase()
								.contains(driverName)
						&& temp.getStaffCard().toLowerCase()
								.contains(staffCard)
						&& ("" + temp.getTripNumber()).toLowerCase().contains(
								tripNum)
						&& temp.getBusinessType().toLowerCase()
								.contains(businessType)
						&& ("" + temp.getPrint()).toLowerCase().contains(
								printNum)
						&& ("" + temp.getPulseCut()).toLowerCase().contains(
								pulseCut)
						&& ("" + temp.getPulseLance()).toLowerCase().contains(
								pulseLance)
						&& ("" + temp.getGpsLost()).toLowerCase().contains(
								gpsLost)
						&& ("" + temp.getPowerLost()).toLowerCase().contains(
								powerLost)
						&& sdf.format(new Date(temp.getTimeBegin().getTime()))
								.toString().toLowerCase().contains(timeBegin)
						&& sdf.format(new Date(temp.getTimeEnd().getTime()))
								.toString().toLowerCase().contains(timeEnd)) {
					lstsearch.add(temp);
				}
			}
		}
		return lstsearch;
	}

	@Override
	public void onEvent(Event event) throws Exception {

//		if(event.getName().equals(Events.ON_CLICK) && event.getTarget().equals(btnPrintTrip)){
//			CommonUtils.exportListboxToExcel(gridDetail,
//					 "bao_cao_chi_tiet_tim_kiem_cuoc_khach_theo_ca.xlsx");
//					 
//		}
		
	}

}
