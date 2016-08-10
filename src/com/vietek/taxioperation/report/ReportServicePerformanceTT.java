package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.vietek.taxioperation.model.ReportQcServicePerformanceTT;
import com.vietek.taxioperation.model.ShiftworkTms;
import com.vietek.taxioperation.ui.util.VTReportViewer;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportServicePerformanceTT extends Window implements
		EventListener<Event> {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Datebox datefrom;
	private Datebox dateto;
	private Chosenbox chosenbox;
	private Grid griddata;
	private Grid gFilter;
	private Button btnsearch;
	private Button btnreport;
	private Button btnExcel;
	private Label labelsum;

	public ReportServicePerformanceTT() {
		this.init();
	}

	private void init() {
		this.setShadow(true);
		this.setId("rpPerformTT");
		this.setSclass("rpPerformTT");
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
		gFilter = new Grid();
		gFilter.setParent(vlayout);
		gFilter.setId("gFilter");
		gFilter.setSclass("gFilter");
//		this.createColsFilter(gFilter);
//		this.createRowsFilter(gFilter);
		this.createRowsFilters(gFilter);

	}
	
	private void createRowsFilters(Grid grid){
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

		Label label = new Label("BÁO CÁO TỔNG HỢP HIỆU SUẤT TỔNG ĐÀI ĐIỀU HÀNH");
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
		
		//Datefrom
		datefrom = new Datebox();
		datefrom.setParent(hbox);
		datefrom.setValue(addHour(new Date(), 00, 00));
		datefrom.setWidth("170px");
		datefrom.setFormat("dd/MM/yyyy  HH:mm");
		datefrom.setConstraint("no empty : Không được để trống");
		
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
		
		//Dateto
		dateto = new Datebox();
		dateto.setParent(hbox);
		dateto.setValue(addHour(new Date(), 23, 59));
		dateto.setWidth("170px");
		dateto.setFormat("dd/MM/yyyy  HH:mm");
		dateto.setConstraint("no empty : Không được để trống");
		
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
		div.setStyle("margin-top : 2px ; margin-left : 220px");

		hbox = new Hbox();
		hbox.setParent(div);
		
		//btnSearch
		btnsearch = new Button();
		btnsearch.setParent(hbox);
		btnsearch.setLabel("Tìm kiếm");
		btnsearch.setStyle("color : black; font-weight : bold");
		btnsearch.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub

			}
		});
		// Bắt sự kiện tại đây hoặc cho vào hàm onevent
		btnsearch.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				long timedateto = dateto.getValue().getTime();
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
					List<ReportQcServicePerformanceTT> lstData = displayrptShiftProductivity();
					if (lstData == null || lstData.size() <= 0) {
						griddata.setEmptyMessage("Không có dữ liệu !");
					} else {
						griddata.setModel(new ListModelList<ReportQcServicePerformanceTT>(
								lstData));
						labelsum.setValue("Tổng số bản ghi báo cáo : "
								+ lstData.size());
//						Clients.evalJavaScript("hGridPerformTT()");
					}
				}
			}
		});
		
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);
		
		//btnReport
		btnreport = new Button("Báo cáo");
		btnreport.setParent(hbox);
		btnreport.setStyle("color : black; font-weight : bold");
		btnreport.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				exportToReportPerformTTViewer(event);

			}
		});
		
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);
		
		//btnExcel
		btnExcel = new Button("Xuất Excel");
		btnExcel.setParent(hbox);
		btnExcel.setStyle("color : black; font-weight : bold");
		btnExcel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				CommonUtils.exportListboxToExcel(griddata,
						"bao_cao_tong_hop_hieu_suat_phuc_vu.xlsx");

			}
		});
		
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 15px ; margin-left : 300px");

		hbox = new Hbox();
		hbox.setParent(div);
		
		//LabelSum
		labelsum = new Label("Tổng số bản ghi báo cáo : " + 0);
		labelsum.setStyle("color : black ;font-weight : bold");
		labelsum.setParent(hbox);
	}

	private Date addHour(Date date, int h, int m) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
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

	private void createData(Vlayout vlayout) {
		griddata = new Grid();
		griddata.setId("gridPerformTT");
		griddata.setClass("gridPerformTT");
		griddata.setParent(vlayout);
//		griddata.setMold("paging");
//		griddata.setAutopaging(true);
//		griddata.setVflex(true);

		this.createColsData(griddata);
		griddata.setEmptyMessage("Không có dữ liệu !");
		griddata.setRowRenderer(new RowRenderer<ReportQcServicePerformanceTT>() {

			@Override
			public void render(Row row, ReportQcServicePerformanceTT data,
					int index) throws Exception {
				row.appendChild(new Label("" + ++index));
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

	private void createColsData(Grid griddata2) {
		Columns cols = new Columns();
		cols.setParent(griddata2);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Thứ tự");

		col = new Column();
		col.setParent(cols);
		col.setHflex("15%");
		col.setLabel("Ca làm việc");

		col = new Column();
		col.setParent(cols);
		col.setHflex("15%");
		col.setLabel("Tổng số khách hàng");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Tổng số cuộc gọi đến");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Tổng số lần nhắc xe");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Số cuộc đáp ứng");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Số cuộc từ chối phục vụ");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Số cuộc gọi thành công");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Tỷ lệ đón thành công");
	}

	public List<ReportQcServicePerformanceTT> displayrptShiftProductivity() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Set<ShiftworkTms> lstShift = chosenbox.getSelectedObjects();
		// Lấy danh sách Id các ca làm việc, ví dụ như 0,1,2..
		StringBuilder lstShiftID = new StringBuilder();
		lstShiftID.append("0");
		for (ShiftworkTms shiftworkTms : lstShift) {
			lstShiftID.append("," + shiftworkTms.getId());
		}
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());
		List<ReportQcServicePerformanceTT> lstValue = lst
				.getrptShiftProductivity(timedatefrom, timedateto, ""
						+ lstShiftID);
		return lstValue;
	}

	public void exportToReportPerformTTViewer(Event event) {
		try {
			if (event.getTarget().equals(btnreport)) {
				Set<ShiftworkTms> lstShifts = chosenbox.getSelectedObjects();
				StringBuilder shifts = new StringBuilder();
				shifts.append("0,");
				for (ShiftworkTms shiftworkTms : lstShifts) {
					shifts.append(shiftworkTms.getId()).append(',');
				}
				Timestamp fromdate = new Timestamp(datefrom.getValue().getTime());
				Timestamp todate = new Timestamp(dateto.getValue().getTime());
				if (fromdate.after(todate)) {
					Messagebox.show("Hãy chọn lại thời gian!");
					return;
				}

				Map<String, Object> mapParams = new HashMap<String, Object>();						
				mapParams.put("_fromdate", fromdate);
				mapParams.put("_todate", todate);
				mapParams.put("_shiftID", shifts.toString());				
				
				String reportFile = "rptShiftProductivity";
				String reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO TỔNG HỢP HIỆU SUẤT PHỤC VỤ";
				String exportFileName = "bao_cao_tong_hop_hieu_suat_phuc_vu";				
				String reportName = "BÁO CÁO TỔNG HỢP HIỆU SUẤT TỔNG ĐÀI ĐIỀU HÀNH";
				VTReportViewer reportWindow = new VTReportViewer(reportFile, reportTitle, reportName, 
						exportFileName, this, mapParams);
				reportWindow.onShowing();
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

