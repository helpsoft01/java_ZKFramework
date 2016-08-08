package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ReportQcCancelRegistrationDriver;
import com.vietek.taxioperation.ui.util.VTReportViewer;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportCancelRegisterOfDriver extends Window {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Datebox datefrom;
	private Datebox dateto;
	private Button btnReport;
	private Button btnsearch;
	private Button btnExcel;
	private Grid griddata;
	private Grid gridFilter;
	private Textbox textbox;
	private Label labelsum;

	public ReportCancelRegisterOfDriver() {
		this.init();
	}

	private void init() {
		this.setShadow(true);
		this.setId("rpTrip");
		this.setSclass("rpTrip");
		this.setHflex("1");
		this.setVflex("1");
		this.setClosable(true);

		Vlayout vlayout = new Vlayout();
		vlayout.setId("vGrid");
		vlayout.setSclass("vGrid");
		vlayout.setStyle("height:100%");
		vlayout.setParent(this);

		this.createFilter(vlayout);
		this.createData(vlayout);
	}

	private void createFilter(Vlayout vlayout) {
		gridFilter = new Grid();
		gridFilter.setParent(vlayout);
		gridFilter.setId("gridFilter");
		gridFilter.setSclass("gridFilter");
		this.createRowsFilters(gridFilter);
	}

	private void createRowsFilters(Grid grid) {
		// dong 1
		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);

		Div div = new Div();
		div.setParent(row);
		div.setStyle("margin-top : 10px; margin-left : 500px");

		Hbox hbox = new Hbox();
		hbox.setParent(div);

		Label label = new Label("BÁO CÁO XE HỦY ĐĂNG KÝ CUỐC KHÁCH");
		label.setStyle("color : black;font-weight : bold; font-size : 16px");
		label.setParent(hbox);

		// dong 2
		row = new Row();
		row.setParent(rows);

		Hlayout hl = new Hlayout();
		hl.setParent(row);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 1px ; margin-left : 36px");

		hbox = new Hbox();
		hbox.setParent(div);

		label = new Label("Từ ngày");
		label.setStyle("font-weight : bold ");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 1px ; margin-left : 45px");

		hbox = new Hbox();
		hbox.setParent(div);

		// datefrom
		datefrom = new Datebox();
		datefrom.setParent(hbox);
		datefrom.setValue(addHour(new Date(), 00, 00));
		datefrom.setWidth("170px");
		datefrom.setFormat("dd/MM/yyyy  HH:mm");
		datefrom.setConstraint("no empty: Không được để trống");

		// dong 3
		row = new Row();
		row.setParent(rows);

		hl = new Hlayout();
		hl.setParent(row);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 35px");

		hbox = new Hbox();
		hbox.setParent(div);

		label = new Label("Đến ngày");
		label.setStyle("font-weight : bold");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 39px");

		hbox = new Hbox();
		hbox.setParent(div);

		// dateto
		dateto = new Datebox();
		dateto.setParent(hbox);
		dateto.setValue(addHour(new Date(), 23, 59));
		dateto.setWidth("170px");
		dateto.setFormat("dd/MM/yyyy  HH:mm");
		dateto.setConstraint("no empty: Không được để trống");

		// dong 4
		row = new Row();
		row.setParent(rows);

		hl = new Hlayout();
		hl.setParent(row);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 35px");

		hbox = new Hbox();
		hbox.setParent(div);

		label = new Label("Lý do hủy");
		label.setStyle("font-weight : bold");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 38px");

		hbox = new Hbox();
		hbox.setParent(div);

		// textbox ly do huy
		textbox = new Textbox();
		textbox.setParent(hbox);
		textbox.setWidth("170px");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 212px");

		hbox = new Hbox();
		hbox.setParent(div);

		// btnSearch
		btnsearch = new Button();
		btnsearch.setParent(hbox);
		btnsearch.setLabel("Tìm kiếm");
		btnsearch.setStyle("color : black; font-weight : bold");
		btnsearch.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				long timedateto = dateto.getValue().getTime();
				long timedatefrom = datefrom.getValue().getTime();
				if (timedateto < timedatefrom) {
					Env.getHomePage().showNotification(
							"Hãy chọn lại thời gian cho báo cáo!",
							Clients.NOTIFICATION_TYPE_ERROR);
					List<ReportQcCancelRegistrationDriver> lstData = new ArrayList<ReportQcCancelRegistrationDriver>();
					griddata.setEmptyMessage("Không có dữ liệu !");
					griddata.setModel(new ListModelList<ReportQcCancelRegistrationDriver>(
							lstData));
					labelsum.setValue("Tổng số bản ghi báo cáo : " + 0);
				} else {
					List<ReportQcCancelRegistrationDriver> lstData = displayrptCancelRegistrationDriver();
					if (lstData == null || lstData.isEmpty()) {
						griddata.setEmptyMessage("Không có dữ liệu");
					} else {
						griddata.setModel(new ListModelList<ReportQcCancelRegistrationDriver>(
								lstData));
						labelsum.setValue("Tổng số bản ghi báo cáo : "
								+ lstData.size());
					}
				}
			}
		});

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);

		// btnReport
		btnReport = new Button("Báo cáo");
		btnReport.setParent(hbox);
		btnReport.setStyle("color : black; font-weight : bold");
		btnReport.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				exportToReportViewer(event);
			}
		});

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);

		// btnExcel
		btnExcel = new Button("Xuất Excel");
		btnExcel.setParent(hbox);
		btnExcel.setStyle("color : black; font-weight : bold");
		btnExcel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				CommonUtils.exportListboxToExcel(griddata,
						"bao_cao_cuoc_khach_dat_xe_qua_tong_dai.xlsx");
			}
		});

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 15px ; margin-left : 300px");

		hbox = new Hbox();
		hbox.setParent(div);

		// labelSum
		labelsum = new Label("Tổng số bản ghi báo cáo : " + 0);
		labelsum.setStyle("color : black ;font-weight : bold");
		labelsum.setParent(hbox);
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

	// Tạo Grid để hiển thị dữ liệu
	private void createData(Vlayout vlayout) {
		griddata = new Grid();
		griddata.setId("gridreportTrip");
		griddata.setClass("gridreportTrip");
		griddata.setHeight(vlayout.getHeight());
		griddata.setZclass(null);
		griddata.setParent(vlayout);
		griddata.setMold("paging");
		griddata.setAutopaging(true);
		griddata.setSizedByContent(true);
		griddata.setVflex(true);

		this.createColsData(griddata);
		griddata.setRowRenderer(new RowRenderer<ReportQcCancelRegistrationDriver>() {

			@Override
			public void render(Row row, ReportQcCancelRegistrationDriver data,
					int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + data.getPhoneCustomer()));
				row.appendChild(new Label("" + (data.getTimeorder() == null ? "" : dateformat
								.format(data.getTimeorder()))));
				row.appendChild(new Label("" + data.getPlaceorder()));
				row.appendChild(new Label("" + data.getNameDriver()));
				row.appendChild(new Label("" + data.getVehicleNumber()));
				row.appendChild(new Label("" + data.getLicensePlate()));
				row.appendChild(new Label("" + data.getTimeRegister()));
				row.appendChild(new Label("" + data.getTimeCancel()));
				row.appendChild(new Label("" + data.getReasonCancel()));
			}
		});

	}

	private void createColsData(Grid griddata2) {
		Columns cols = new Columns();
		cols.setParent(griddata2);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("5%");
		col.setLabel("Thứ tự");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Số điện thoại khách");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Thời gian khách gọi");

		col = new Column();
		col.setParent(cols);
		col.setHflex("20%");
		col.setLabel("Địa điểm yêu cầu");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Tên lái xe");

		col = new Column();
		col.setParent(cols);
		col.setHflex("5%");
		col.setLabel("Số tài");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Biển số xe");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Thời điểm đăng ký");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Thời điểm hủy");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Lý do hủy");
	}

	public List<ReportQcCancelRegistrationDriver> displayrptCancelRegistrationDriver() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());
		List<ReportQcCancelRegistrationDriver> lstValue = lst
				.getrptCancelRegistrationDriver(timedatefrom, timedateto);
		return lstValue;
	}

	public void exportToReportViewer(Event event) {
		try {
			if (event.getTarget().equals(btnReport)) {
				Timestamp fromdate = new Timestamp(datefrom.getValue()
						.getTime());
				Timestamp todate = new Timestamp(dateto.getValue().getTime());
				if (fromdate.after(todate)) {
					Messagebox.show("Hãy chọn lại thời gian!");
					return;
				}
				Map<String, Object> mapParams = new HashMap<String, Object>();
				mapParams.put("datefrom", fromdate);
				mapParams.put("dateto", todate);

				String reportFile = "ReportCancelRegistrationDriver";
				String reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO XE HỦY ĐĂNG KÝ CUỐC";
				String exportFileName = "bao_cao_xe_huy_dang_ky_cuoc";
				String reportName = "BÁO CÁO XE HỦY ĐĂNG KÝ CUỐC";

				VTReportViewer reportWindow = new VTReportViewer(reportFile,
						reportTitle, reportName, exportFileName, this,
						mapParams);
				reportWindow.onShowing();
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

}
