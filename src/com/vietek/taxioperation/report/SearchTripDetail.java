package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListModelSet;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ReportQcTripDetail;
import com.vietek.taxioperation.util.Env;

public class SearchTripDetail extends Window implements EventListener<Event> {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;

	private Datebox datefrom;
	private Datebox dateto;

	private Textbox txtCustomerPhone;
	private Textbox txtVehicleNumberRegister;

	private Label labelSum;
	private Grid gridSearchData;

	private Button btnSearch;
	private Button btnExcel;

	public SearchTripDetail() throws Exception {
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

		this.createSearchTripDetail(vlayout);

		// Tao su kien tai day
		btnSearch.addEventListener(Events.ON_CLICK, SEARCH_BUTTON_CLICK_EVENT);
	}

	private void createSearchTripDetail(Vlayout vlayout) throws Exception {
		Hlayout hlayout = new Hlayout();
		hlayout.setParent(vlayout);
		hlayout.setHflex("1");
		hlayout.setVflex("1");

		createInput(hlayout);
		createData(hlayout);
	}

	private void createInput(Hlayout hlayout) throws Exception {
		Panel panel = new Panel();
		panel.setParent(hlayout);
		panel.setTitle("ĐIỀU KIỆN TÌM KIẾM");
		panel.setStyle("color : black; font-size : 14px; font-weight : bold");
		panel.setHflex("2.5");
		panel.setVflex("1");
		panel.setBorder("normal");

		Panelchildren panchild = new Panelchildren();
		panchild.setParent(panel);

		Div div = new Div();
		div.setParent(panchild);

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
		label.setParent(hbox);
		label.setStyle("font-weight : bold");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 30px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		datefrom = new Datebox();
		datefrom.setParent(hbox);
		datefrom.setWidth("150px");
		datefrom.setValue(addHour(new Date(), 00, 00));
		datefrom.setFormat("dd/MM/yyyy HH:mm");

		hl = new Hlayout();
		hl.setParent(vlayout);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");

		label = new Label("Đến ngày : ");
		label.setParent(hbox);
		label.setStyle("font-weight : bold");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 23px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		dateto = new Datebox();
		dateto.setParent(hbox);
		dateto.setWidth("150px");
		dateto.setValue(addHour(new Date(), 23, 59));
		dateto.setFormat("dd/MM/yyyy HH:mm");

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
		div.setStyle("margin-top : 10px; margin-left : 20px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		txtVehicleNumberRegister = new Textbox();
		txtVehicleNumberRegister.setParent(hbox);
		txtVehicleNumberRegister.setWidth("150px");

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
		txtCustomerPhone.setWidth("150px");
		txtCustomerPhone.setParent(hbox);

		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px;");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		labelSum = new Label("Tổng số cuốc khách tìm kiếm : " + 0);
		labelSum.setStyle("color: black; font-weight: bold");
		labelSum.setParent(hbox);

		hl = new Hlayout();
		hl.setParent(vlayout);
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 10px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		btnSearch = new Button();
		btnSearch.setLabel("Tìm kiếm");
		btnSearch.setStyle("color: black; font-weight: bold");
		btnSearch.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px; margin-left : 5px");
		hbox = new Hbox();
		hbox.setParent(div);
		hbox.setStyle("");
		btnExcel = new Button("Xuất Excel");
		btnExcel.setStyle("color: black; font-weight: bold");
		btnExcel.setParent(hbox);
		// btnExcel.addEventListener(Events.ON_CLICK, EXCEL_EXPORT_EVENT);

	}

	private void createData(Hlayout hlayout) throws Exception {
		gridSearchData = new Grid();
		gridSearchData.setParent(hlayout);
		gridSearchData.setHflex("7.5");
		gridSearchData.setVflex("1");
		gridSearchData.setMold("paging");
		gridSearchData.setAutopaging(true);
		gridSearchData.setClass("gridreportTrip");
		
		Auxhead auxhead = new Auxhead();
		auxhead.setParent(gridSearchData);
		Auxheader auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setColspan(9);
		auxheader.setLabel("Tìm kiếm cuốc khách");
		auxheader.setStyle("font-size : 14px, font-weight : bold, margin-left : 20px");
		
		auxhead = new Auxhead();
		auxhead.setParent(gridSearchData);
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("A1");
		
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("A2");
		
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("A3");
		
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("A4");
		
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("A5");
		
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("A6");
		
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("A7");
		
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("A8");
		
		auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setLabel("A9");

		Columns cols = new Columns();
		cols.setParent(gridSearchData);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("3%");
		col.setLabel("TT");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Số điện thoại");

		col = new Column();
		col.setParent(cols);
		col.setHflex("25%");
		col.setLabel("Cuốc khách");

		col = new Column();
		col.setParent(cols);
		col.setHflex("12%");
		col.setLabel("Ngày");

		col = new Column();
		col.setParent(cols);
		col.setHflex("8%");
		col.setLabel("Giờ gọi xe");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Thời điểm đ/ký");

		col = new Column();
		col.setParent(cols);
		col.setHflex("12%");
		col.setLabel("D/sách xe đ/ký");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Giờ đón khách");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Xe đón khách");

		gridSearchData.setRowRenderer(new RowRenderer<ReportQcTripDetail>() {

			@Override
			public void render(Row row, ReportQcTripDetail data, int index)
					throws Exception {

				SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + data.getPhoneNumber()));
				row.appendChild(new Label("" + data.getBeginOrderAddress()));
				row.appendChild(new Label(""
						+ (data.getBeginOrderTime() == null ? "" : dateformat
								.format(data.getBeginOrderTime()))));
				row.appendChild(new Label(""
						+ (data.getBeginOrderTime() == null ? "" : timeformat
								.format(data.getBeginOrderTime()))));
				row.appendChild(new Label(
						""
								+ (data.getStartRegisterTime() == null ? ""
										: timeformat.format(data
												.getStartRegisterTime()))));
				row.appendChild(new Label("" + data.getListRegisterVeh()));
				row.appendChild(new Label(""
						+ (data.getTimeIsUpdated() == null ? "" : timeformat
								.format(data.getTimeIsUpdated()))));
				row.appendChild(new Label("" + data.getSelectedVeh()));

			}
		});
	}

	public List<ReportQcTripDetail> displayrptTripDetail() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());
		String phonenumber = txtCustomerPhone.getValue();
		List<ReportQcTripDetail> lstValue = lst.getrptTripDetail(timedatefrom,
				timedateto, phonenumber);
		return lstValue;
	}

	private EventListener<Event> SEARCH_BUTTON_CLICK_EVENT = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			long timedateto = dateto.getValue().getTime();
			long timedatefrom = datefrom.getValue().getTime();
			if (timedateto < timedatefrom) {
				Env.getHomePage().showNotification(
						"Hãy chọn lại thời gian tìm kiếm!",
						Clients.NOTIFICATION_TYPE_ERROR);
				List<ReportQcTripDetail> lstData = new ArrayList<ReportQcTripDetail>();
				gridSearchData.setEmptyMessage("Không có dữ liệu !");
				gridSearchData.setModel(new ListModelList<ReportQcTripDetail>(lstData));
				labelSum.setValue("Tổng số bản ghi báo cáo : " + 0);
			} else {
				List<ReportQcTripDetail> lstData = displayrptTripDetail();
				if (lstData == null || lstData.isEmpty()) {
					gridSearchData.setEmptyMessage("Không có dữ liệu");
				} else {
					gridSearchData.setModel(new ListModelSet<ReportQcTripDetail>(lstData));
					labelSum.setValue("Tổng số bản ghi báo cáo : " + 0);
				}
			}
		}
	};

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

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

	}
}
