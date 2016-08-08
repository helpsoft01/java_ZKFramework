package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
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
import com.vietek.taxioperation.model.ReportQcTripDetail;
import com.vietek.taxioperation.ui.util.ComboboxRender;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.DateUtils;
import com.vietek.taxioperation.util.Env;

public class StatisticTripDetail extends Window implements EventListener<Event> {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Datebox datefrom;
	private Datebox datefromFilter;
	private Datebox dateto;
	private Datebox datetoFilter;

	private Textbox txtCustomerPhone;
	private Textbox txtVehicleNumber;
	private Textbox txtPhoneFilter;
	private Combobox cbxRiderState;
	private Grid gridStatisticData;

	private Button btnsearch;
	private Button btnExcel;
	private Label labelsum;

	public StatisticTripDetail() throws Exception {
		this.init();
	}

	private void init() throws Exception {
		this.setShadow(true);
		this.setHflex("1");
		this.setVflex("1");
		this.setClosable(true);

		Vlayout vlayout = new Vlayout();
		vlayout.setParent(this);
		vlayout.setHflex("1");
		vlayout.setVflex("1");

		this.createStatistic(vlayout);
	}

	private void createStatistic(Vlayout vlayout) throws Exception {

		Hlayout hlayout = new Hlayout();
		hlayout.setParent(vlayout);
		hlayout.setVflex("1");
		hlayout.setHflex("1");

		createStatisticInput(hlayout);
		createStatisticData(hlayout);

	}

	private void createStatisticInput(Hlayout hlayout) throws Exception {

		Panel panel = new Panel();
		panel.setParent(hlayout);
		panel.setTitle("ĐIỀU KIỆN ĐẦU VÀO");
		panel.setStyle("color : black; font-size : 14px; font-weight : bold;");
		panel.setHflex("2.5");
		panel.setVflex("1");
		panel.setBorder("normal");

		Panelchildren pan = new Panelchildren();
		pan.setParent(panel);

		Div div = new Div();
		div.setParent(pan);

		Vlayout vlayout = new Vlayout();
		vlayout.setParent(div);
		vlayout.setVflex("1");

		Hlayout hl = new Hlayout();
		hl.setParent(vlayout);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");
		Hbox hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		Label label = new Label("Từ ngày : ");
		label.setStyle("font-weight : bold");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 30px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		datefrom = new Datebox();
		datefrom.setParent(hbox);
		datefrom.setWidth("140px");
		datefrom.setValue(DateUtils.addHour(new Date(), 00, 00));
		datefrom.setFormat("dd/MM/yyyy  HH:mm");

		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		label = new Label("Đến ngày : ");
		label.setStyle("font-weight : bold");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 23px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		dateto = new Datebox();
		dateto.setParent(hbox);
		dateto.setWidth("140px");
		dateto.setValue(DateUtils.addHour(new Date(), 23, 59));
		dateto.setFormat("dd/MM/yyyy  HH:mm");

		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 0px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		label = new Label("Số tài Đ.ký: ");
		label.setStyle("font-weight : bold");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 21px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		txtVehicleNumber = new Textbox();
		txtVehicleNumber.setParent(hbox);
		txtVehicleNumber.setWidth("100px");

		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		label = new Label("Số điện thoại : ");
		label.setStyle("font-weight : bold");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 4px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		txtCustomerPhone = new Textbox();
		txtCustomerPhone.setWidth("100px");
		txtCustomerPhone.setParent(hbox);

		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		label = new Label("Lái xe Đ.ký : ");
		label.setStyle("font-weight : bold");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 10px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		createRiderStateCombobox(hbox);

		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px;");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		labelsum = new Label("Tổng số bản ghi báo cáo: " + 0);
		labelsum.setStyle("color: black; font-weight: bold");
		labelsum.setParent(hbox);

		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 10px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		btnsearch = new Button();
		btnsearch.setLabel("Thống kê");
		btnsearch.setStyle("color: black; font-weight: bold");
		btnsearch.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 5px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		btnExcel = new Button("Xuất Excel");
		btnExcel.setStyle("color: black; font-weight: bold");
		btnExcel.setParent(hbox);
		btnExcel.addEventListener(Events.ON_CLICK, EXCEL_EXPORT_EVENT);

		btnsearch.addEventListener(Events.ON_CLICK, SEARCH_BUTTON_CLICK_EVENT);
		txtCustomerPhone.addEventListener(Events.ON_OK, SEARCH_BUTTON_CLICK_EVENT);
		txtVehicleNumber.addEventListener(Events.ON_OK, SEARCH_BUTTON_CLICK_EVENT);
	}

	private void createStatisticData(Hlayout hlayout) {
		gridStatisticData = new Grid();
		gridStatisticData.setParent(hlayout);
		gridStatisticData.setHflex("7.5");
		gridStatisticData.setVflex("1");
		gridStatisticData.setMold("paging");
		gridStatisticData.setAutopaging(true);
		gridStatisticData.setClass("gridreportTrip");

		Columns cols = new Columns();
		cols.setParent(gridStatisticData);
		cols.setSizable(true);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("5%");
		col.setLabel("TT");

		col = new Column();
		col.setParent(cols);
		col.setHflex("13%");
		col.setLabel("Số điện thoại");

		col = new Column();
		col.setParent(cols);
		col.setHflex("22%");
		col.setLabel("Điểm đón khách");

		col = new Column();
		col.setParent(cols);
		col.setHflex("13%");
		col.setLabel("Xe đăng ký");

		col = new Column();
		col.setParent(cols);
		col.setHflex("5%");
		col.setLabel("Xe đón");

		col = new Column();
		col.setParent(cols);
		col.setHflex("12%");
		col.setLabel("Ngày");

		col = new Column();
		col.setParent(cols);
		col.setHflex("5%");
		col.setLabel("Giờ gọi");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Hình thức đặt xe");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Hình thức Đ.ký");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Ghi chú");

		gridStatisticData.setRowRenderer(new RowRenderer<ReportQcTripDetail>() {

			@Override
			public void render(Row row, ReportQcTripDetail data, int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + data.getPhoneNumber()));
				row.appendChild(new Label("" + data.getBeginOrderAddress()));
				row.appendChild(new Label("" + data.getListRegisterVeh()));
				row.appendChild(new Label("" + data.getSelectedVeh()));
				row.appendChild(new Label(
						"" + (data.getBeginOrderTime() == null ? "" : dateformat.format(data.getBeginOrderTime()))));
				row.appendChild(new Label(
						"" + (data.getBeginOrderTime() == null ? "" : timeformat.format(data.getBeginOrderTime()))));
				row.appendChild(new Label("" + data.getOrderCarType()));
				row.appendChild(new Label("" + data.getOrderStatus()));
				row.appendChild(new Label("" + data.getCancelReason()));
			}
		});
	}

	public List<ReportQcTripDetail> lstReportQcTripDetail() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());
		String phonenumber = txtCustomerPhone.getValue();
		String vehicle = txtVehicleNumber.getValue();
		int state = cbxRiderState.getSelectedItem().getValue();
		List<ReportQcTripDetail> lstValue = lst.getrptCallingByOperation(timedatefrom, timedateto, phonenumber, vehicle,
				state);
		return lstValue;
	}

	public List<ReportQcTripDetail> lstReportQcTripDetailFilter() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Timestamp timedatefromfilter = new Timestamp(datefromFilter.getValue().getTime());
		Timestamp timedatetofilter = new Timestamp(datetoFilter.getValue().getTime());
		String phonenumberfilter = txtPhoneFilter.getValue();
		String vehicle = txtVehicleNumber.getValue();
		int state = cbxRiderState.getSelectedItem().getValue();
		List<ReportQcTripDetail> lstValueFilter = lst.getrptCallingByOperation(timedatefromfilter, timedatetofilter,
				phonenumberfilter, vehicle, state);
		return lstValueFilter;
	}

	private EventListener<Event> SEARCH_BUTTON_CLICK_EVENT = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			long timedateto = dateto.getValue().getTime();
			long timedatefrom = datefrom.getValue().getTime();

			if (timedateto < timedatefrom) {
				Env.getHomePage().showNotification("Hãy chọn lại thời gian cho báo cáo!",
						Clients.NOTIFICATION_TYPE_ERROR);
				List<ReportQcTripDetail> lstData = new ArrayList<ReportQcTripDetail>();
				gridStatisticData.setEmptyMessage("Không có dữ liệu !");
				gridStatisticData.setModel(new ListModelList<ReportQcTripDetail>(lstData));
				labelsum.setValue("Tổng số bản ghi báo cáo : " + 0);
			} else {
				List<ReportQcTripDetail> lstData = lstReportQcTripDetail();
				if (lstData == null || lstData.isEmpty()) {
					gridStatisticData.setEmptyMessage("Không có dữ liệu");
				} else {
					gridStatisticData.setModel(new ListModelList<ReportQcTripDetail>(lstData));
					labelsum.setValue("Tổng số bản ghi báo cáo : " + lstData.size());
				}
			}
		}
	};

	private EventListener<Event> EXCEL_EXPORT_EVENT = new EventListener<Event>() {
		@Override
		public void onEvent(Event arg0) throws Exception {
			// gridStatisticData.renderAll();
			CommonUtils.exportListboxToExcel(gridStatisticData, "thong_ke_cuoc_goi_tong_dai.xlsx");
		}
	};

	private void createRiderStateCombobox(Hbox hbox) {
		String[] titles = { "Tất cả", "Đ.ký Tổng đài", "Đ.ký qua App" };
		String styles = "";
		ComboboxRender comboboxRender = new ComboboxRender();
		cbxRiderState = comboboxRender.ComboboxRendering(titles, styles, 140, 0);
		cbxRiderState.setSclass("combobox_rider_state");
		cbxRiderState.setParent(hbox);
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

	}
}