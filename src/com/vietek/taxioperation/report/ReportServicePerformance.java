package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import org.zkoss.bind.annotation.BindingParam;
 import org.zkoss.bind.annotation.Command;
 import org.zkoss.util.media.AMedia;*/
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
//import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ReportQcServicePerformance;
import com.vietek.taxioperation.model.ReportQcServicePerformanceTT;
import com.vietek.taxioperation.model.ShiftworkTms;
import com.vietek.taxioperation.ui.util.VTReportViewer;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportServicePerformance extends Window {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Datebox datefrom;
	private Datebox dateTo;
	private Chosenbox chosenbox;
	private Grid griddata;
	private Button btnsearch;
	private Button btnReport;
	private Button btnExcel;
	private Label labelsum;

	public ReportServicePerformance() {
		this.init();
	}

	public List<ReportQcServicePerformance> displayrptShiftProductivityDetail() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Set<ShiftworkTms> lstShift = chosenbox.getSelectedObjects();
		// Lấy danh sách Id các ca làm việc, ví dụ như 0,1,2..
		StringBuilder lstShiftID = new StringBuilder(10);
		lstShiftID.append("0");
		for (ShiftworkTms shiftworkTms : lstShift) {
			lstShiftID.append("," + shiftworkTms.getId());
		}
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateTo.getValue().getTime());
		List<ReportQcServicePerformance> lstValue = lst
				.getrptShiftProductivityDetail(timedatefrom, timedateto, ""
						+ lstShiftID);
		return lstValue;
	}

	public void exportToReportViewer(Event event) {
		try {
			if (event.getTarget().equals(btnReport)) {
				Set<ShiftworkTms> lstShifts = chosenbox.getSelectedObjects();
				StringBuilder shifts = new StringBuilder();
				shifts.append("0,");
				for (ShiftworkTms shiftworkTms : lstShifts) {
					shifts.append(shiftworkTms.getId()).append(',');
				}
				Timestamp fromdate = new Timestamp(datefrom.getValue().getTime());
				Timestamp todate = new Timestamp(dateTo.getValue().getTime());
				if (fromdate.after(todate)) {
					Messagebox.show("Hãy chọn lại thời gian!");
					return;
				}
				Map<String, Object> mapParams = new HashMap<String, Object>();
				mapParams.put("_fromdate", fromdate);
				mapParams.put("_todate", todate);
				mapParams.put("_shiftID", shifts.toString());	
				
				String reportFile = "rptShiftProductivityDetail";
				String reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO CHI TIẾT HIỆU SUẤT PHỤC VỤ";
				String exportFileName = "bao_cao_chi_tiet_hieu_suat_phuc_vu";				
				String reportName = "BÁO CÁO CHI TIẾT HIỆU SUẤT TỔNG ĐÀI ĐIỀU HÀNH";					
				VTReportViewer reportWindow = new VTReportViewer(reportFile, reportTitle, reportName, 
						exportFileName, this, mapParams);
				reportWindow.onShowing();
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	private void init() {
		this.setShadow(true);
		this.setId("rpPerform");
		this.setSclass("rpPerform");
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

	/**
	 * Tao phan chon tham so truyen vao
	 * 
	 * @param vlayout
	 */
	private void createFilter(Vlayout vlayout) {
		Grid gFilter = new Grid();
		gFilter.setParent(vlayout);
		gFilter.setSclass("gFilter");
//		this.createColsFilter(gFilter);
//		this.createRowsFilter(gFilter);
		this.createRowsFilters(gFilter);
	}
	// Filter Update
	private void createRowsFilters(Grid grid) {
		// dong 1
				Rows rows = new Rows();
				rows.setParent(grid);

				Row row = new Row();
				row.setParent(rows);

				Div div = new Div();
				div.setParent(row);
				div.setStyle("margin-top : 10px; margin-left : 450px");

				Hbox hbox = new Hbox();
				hbox.setParent(div);

				Label label = new Label("BÁO CÁO CHI TIẾT HIỆU SUẤT TỔNG ĐÀI ĐIỀU HÀNH");
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
				
				// Datefrom
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
				
				// Dateto
				dateTo = new Datebox();
				dateTo.setParent(hbox);
				dateTo.setValue(addHour(new Date(), 23, 59));
				dateTo.setWidth("170px");
				dateTo.setFormat("dd/MM/yyyy  HH:mm");
				dateTo.setConstraint("no empty: Không được để trống");
				
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
				
				//Textbox
				this.createchosenboxShift(hbox);
				
				div = new Div();
				div.setParent(hl);
				div.setStyle("margin-top : 2px ; margin-left : 200px");

				hbox = new Hbox();
				hbox.setParent(div);
				
				//btnSearch
				btnsearch = new Button();
				btnsearch.setParent(hbox);
				btnsearch.setLabel("Tìm kiếm");
				btnsearch.setStyle("color : black; font-weight : bold");
				// Bắt sự kiện tại đây hoặc cho vào hàm onevent
				btnsearch.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						long timedateto = dateTo.getValue().getTime();
						long timedatefrom = datefrom.getValue().getTime();
						if (timedateto < timedatefrom) {
							Env.getHomePage().showNotification(
									"Hãy chọn lại thời gian cho báo cáo!",
									Clients.NOTIFICATION_TYPE_ERROR);
							List<ReportQcServicePerformanceTT> lstData = new ArrayList<ReportQcServicePerformanceTT>();
							griddata.setEmptyMessage("Không có dữ liệu !");
							griddata.setModel(new ListModelList<ReportQcServicePerformanceTT>(
									lstData));
							labelsum.setValue("Tổng số bản ghi báo cáo : " + 0);
						} else {
							List<ReportQcServicePerformance> lstValue = displayrptShiftProductivityDetail();
							if (lstValue == null || lstValue.isEmpty()) {
								griddata.setEmptyMessage("Không có dữ liệu");
							} else {
								griddata.setModel(new ListModelList<ReportQcServicePerformance>(
										lstValue));
								labelsum.setValue("Tổng số bản ghi báo cáo : "
										+ lstValue.size());
								Clients.evalJavaScript("hGridPerform()");
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
					public void onEvent(Event event) throws Exception {
						CommonUtils.exportListboxToExcel(griddata,
								"bao_cao_chi_tiet_hieu_suat_phuc_vu.xlsx");
					}
				});
				
				div = new Div();
				div.setParent(hl);
				div.setStyle("margin-top : 15px ; margin-left : 300px");

				hbox = new Hbox();
				hbox.setParent(div);
				
				//labelSum
				labelsum = new Label("Tổng số bản ghi báo cáo : " + 0);
				labelsum.setStyle("color : black ;font-weight : bold");
				labelsum.setParent(hbox);
	}

	private Date addHour(Date date, int h, int m) {
		if (date == null) {
			throw new IllegalArgumentException("Ngày không được để trống");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, h);
		c.set(Calendar.MINUTE, m);
		return c.getTime();
	}

	private void createchosenboxShift(Hbox hbox) {
		chosenbox = new Chosenbox();
		chosenbox.setParent(hbox);
		chosenbox.setWidth("170px");

		ListObjectDatabase serviceperformance = new ListObjectDatabase();
		List<ShiftworkTms> lstValue = serviceperformance.getShift();
		chosenbox.setModel(new ListModelList<>(lstValue));

	}

	/**
	 * Tao grid hien thi du lieu
	 * 
	 * @author MPV
	 * @param vlayout
	 */
	private void createData(Vlayout vlayout) {
		griddata = new Grid();
		griddata.setId("gridPerform");
		griddata.setSclass("gridPerform");
		griddata.setParent(vlayout);
		griddata.setMold("paging");
		griddata.setAutopaging(true);
		griddata.setSizedByContent(true);
//		griddata.setPagingPosition("bottom");
		griddata.setVflex(true);

		this.createColsData(griddata);
		griddata.setEmptyMessage("Không có dữ liệu");
		griddata.setRowRenderer(new RowRenderer<ReportQcServicePerformance>() {

			@Override
			public void render(Row row, ReportQcServicePerformance data,
					int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
				Date shiftday = new Date(data.getShiftday().getTime());
				row.appendChild(new Label("" + ++index));
				row.setAlign("right");
				row.appendChild(new Label("" + dateformat.format(shiftday)));
				row.setAlign("right");
				row.appendChild(new Label("" + data.getShiftname()));
				row.setAlign("right");
				row.appendChild(new Label("" + data.getCustomer()));
				row.setAlign("right");
				row.appendChild(new Label("" + data.getRequestnumber()));
				row.setAlign("right");
				row.appendChild(new Label("" + data.getRepeattime()));
				row.setAlign("right");
				row.appendChild(new Label("" + data.getTotalcall()));
				row.setAlign("right");
				row.appendChild(new Label("" + data.getDeclinecall()));
				row.setAlign("right");
				row.appendChild(new Label("" + data.getAcceptcall()));
				row.setAlign("right");
				row.appendChild(new Label("" + data.getPercent()));
				row.setAlign("right");
			}
		});
	}

	private void createColsData(Grid grid) {
		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("5%");
		col.setLabel("Thứ tự");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Ngày làm việc");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Ca làm việc");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Tổng số khách hàng");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Tổng số cuộc gọi đến");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Tổng số lần nhắc xe");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Số cuộc đã nghe");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Số cuộc từ chối phục vụ");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Số cuộc gọi thành công");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("15%");
		col.setLabel("Tỷ lệ thành công");
		col.setAlign("center");
	}
}
