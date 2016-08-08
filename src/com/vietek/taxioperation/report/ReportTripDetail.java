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
import com.vietek.taxioperation.model.ReportQcTripDetail;
import com.vietek.taxioperation.ui.util.VTReportViewer;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportTripDetail extends Window {

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
	private List<ReportQcTripDetail> lstValues;
	
	public ReportTripDetail() {
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

		Hlayout hl = new Hlayout();
		hl.setParent(row);

		Div div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 1px ; margin-left : 36px");

		Hbox hbox = new Hbox();
		hbox.setParent(div);

		Label label = new Label("Từ ngày");
		label.setStyle("font-weight : bold ");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 1px ; margin-left : 45px");

		hbox = new Hbox();
		hbox.setParent(div);

		datefrom = new Datebox();
		datefrom.setParent(hbox);
		datefrom.setValue(addHour(new Date(), 00, 00));
		datefrom.setWidth("170px");
		datefrom.setFormat("dd/MM/yyyy  HH:mm");
		datefrom.setConstraint("no empty: Không được để trống");

		// dong 2
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

		label = new Label("Số điện thoại");
		label.setStyle("font-weight : bold");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 20px");

		hbox = new Hbox();
		hbox.setParent(div);

		textbox = new Textbox();
		textbox.setParent(hbox);
		textbox.setWidth("170px");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 180px");

		hbox = new Hbox();
		hbox.setParent(div);

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
					List<ReportQcTripDetail> lstData = new ArrayList<ReportQcTripDetail>();
					griddata.setEmptyMessage("Không có dữ liệu !");
					griddata.setModel(new ListModelList<ReportQcTripDetail>(
							lstData));
					labelsum.setValue("Tổng số bản ghi báo cáo : " + 0);
				} else {
					List<ReportQcTripDetail> lstData = displayrptTripDetail();
					if (lstData == null || lstData.isEmpty()) {
						griddata.setEmptyMessage("Không có dữ liệu");
					} else {
						griddata.setModel(new ListModelList<ReportQcTripDetail>(
								lstData));
						labelsum.setValue("Tổng số bản ghi báo cáo : "
								+ lstData.size());
					}
				}
			}
		});

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 2px");

		hbox = new Hbox();
		hbox.setParent(div);

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
		div.setStyle("margin-top : 2px ; margin-left : 2px");

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
	private void createData(Vlayout vlayout) {
		griddata = new Grid();
		griddata.setId("gridreportTrip");
		griddata.setClass("gridreportTrip");
		griddata.setHeight(vlayout.getHeight());
		griddata.setZclass(null);
		griddata.setParent(vlayout);
		griddata.setMold("paging");
		griddata.setAutopaging(true);
		griddata.setVflex(true);			
		
		this.createColsData(griddata);
		griddata.setRowRenderer(new RowRenderer<ReportQcTripDetail>() {

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
	private void createColsData(Grid griddata2) {
		Columns cols = new Columns();
		cols.setParent(griddata2);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("5%");
		col.setLabel("Thứ tự");

		col = new Column();
		col.setParent(cols);
		col.setHflex("8%");
		col.setLabel("Số điện thoại");

		col = new Column();
		col.setParent(cols);
		col.setHflex("30%");
		col.setLabel("Cuốc khách");

		col = new Column();
		col.setParent(cols);
		col.setHflex("8%");
		col.setLabel("Ngày");

		col = new Column();
		col.setParent(cols);
		col.setHflex("8%");
		col.setLabel("Giờ gọi đặt xe");

		col = new Column();
		col.setParent(cols);
		col.setHflex("12%");
		col.setLabel("Thời điểm đăng ký");

		col = new Column();
		col.setParent(cols);
		col.setHflex("20%");
		col.setLabel("Danh sách xe đăng ký");

		col = new Column();
		col.setParent(cols);
		col.setHflex("12%");
		col.setLabel("Giờ đón khách");

		col = new Column();
		col.setParent(cols);
		col.setHflex("6%");
		col.setLabel("Xe đón khách");
	}
	public List<ReportQcTripDetail> displayrptTripDetail() {
		lstValues = new ArrayList<ReportQcTripDetail>();
		ListObjectDatabase lst = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());
		String phonenumber = textbox.getValue();
		List<ReportQcTripDetail> lstValues = lst.getrptTripDetail(timedatefrom, timedateto, phonenumber);
		this.setReportViewerData(lstValues);
		return lstValues;
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
				String phonenumber = textbox.getValue();
				Map<String, Object> mapParams = new HashMap<String, Object>();
				mapParams.put("_fromdate", fromdate);
				mapParams.put("_todate", todate);
				mapParams.put("_phone", phonenumber);

				String reportFile = "rptTripDetailFromOperation";
				String reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO CHI TIẾT CUỐC KHÁCH QUA TỔNG ĐÀI";
				String exportFileName = "bao_cao_chi_tiet_cuoc_khach_tong_dai";
				String reportName = "BÁO CÁO CHI TIẾT CUỐC KHÁCH QUA TỔNG ĐÀI";

				VTReportViewer reportWindow = new VTReportViewer(reportFile,
						reportTitle, reportName, exportFileName, this, mapParams);
				reportWindow.onShowing();
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	public Object[] getReportViewerData(){
		return lstValues.toArray();
	}
	
	public void setReportViewerData(List<ReportQcTripDetail> lstvalues){
		this.lstValues = lstvalues;
	}
}
