package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
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
import com.vietek.taxioperation.model.ReportQcTimeWaitOfCustomer;
import com.vietek.taxioperation.ui.util.VTReportViewer;
import com.vietek.taxioperation.util.CommonUtils;

public class ReportTimeWaitOfCustomer extends Window {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Datebox datefrom;
	private Textbox txtMinute;
	private Button btnReport;
	private Button btnsearch;
	private Button btnExcel;
	private Grid griddata;
	private Grid gridFilter;
	private Textbox txtZone;
	private Label labelsum;

	public ReportTimeWaitOfCustomer() {
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
	
	private void createRowsFilters(Grid grid){
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

				Label label = new Label("BÁO CÁO KHÁCH CHỜ LÂU CHƯA CÓ XE");
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

				label = new Label("Thời điểm đăng ký cuốc");
				label.setStyle("font-weight : bold ");
				label.setParent(hbox);

				div = new Div();
				div.setParent(hl);
				div.setStyle("margin-top : 1px ; margin-left : 14px");

				hbox = new Hbox();
				hbox.setParent(div);
				
				// Datebox
				datefrom = new Datebox();
				datefrom.setParent(hbox);
				datefrom.setValue(new Date());
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

				label = new Label("Số phút chờ tối đa");
				label.setStyle("font-weight : bold");
				label.setParent(hbox);

				div = new Div();
				div.setParent(hl);
				div.setStyle("margin-top : 2px ; margin-left : 48px");

				hbox = new Hbox();
				hbox.setParent(div);
				
				// Text Minutes
				txtMinute = new Textbox();
				txtMinute.setParent(hbox);
				txtMinute.setWidth("170px");
				
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

				label = new Label("Khu vực");
				label.setStyle("font-weight : bold");
				label.setParent(hbox);

				div = new Div();
				div.setParent(hl);
				div.setStyle("margin-top : 2px ; margin-left : 102px");

				hbox = new Hbox();
				hbox.setParent(div);
				
				// Textbox Khu vưc
				txtZone = new Textbox();
				txtZone.setParent(hbox);
				txtZone.setWidth("170px");
				
				div = new Div();
				div.setParent(hl);
				div.setStyle("margin-top : 2px ; margin-left : 150px");

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
						List<ReportQcTimeWaitOfCustomer> lstData = displayrptTimeWaitOfCustomer();
						if (lstData == null || lstData.isEmpty()) {
							griddata.setEmptyMessage("Không có dữ liệu !");
							griddata.setModel(new ListModelList<ReportQcTimeWaitOfCustomer>(
									lstData));
							labelsum.setValue("Tổng số bản ghi báo cáo : " + 0);
						} else {
							griddata.setModel(new ListModelList<ReportQcTimeWaitOfCustomer>(
									lstData));
							labelsum.setValue("Tổng số bản ghi báo cáo : "
									+ lstData.size());
						}
					}
				});
				
				div = new Div();
				div.setParent(hl);
				div.setStyle("margin-top : 2px ; margin-left : 2px");

				hbox = new Hbox();
				hbox.setParent(div);
				
				//btnReport
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
				
				//btnExcel
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
				
				//label
				labelsum = new Label("Tổng số bản ghi báo cáo :" + 0);
				labelsum.setStyle("color : black ;font-weight : bold");
				labelsum.setParent(hbox);
	}

	private void createData(Vlayout vlayout) {
		griddata = new Grid();
		griddata.setId("gridreportTrip");
		griddata.setClass("gridreportTrip");
		griddata.setHeight(vlayout.getHeight());
		griddata.setZclass(null);
		griddata.setParent(vlayout);
		this.createColsData(griddata);
		griddata.setEmptyMessage("Không có dữ liệu");
		griddata.setMold("paging");
		griddata.setAutopaging(true);
		griddata.setSizedByContent(true);
		griddata.setVflex(true);
		griddata.setRowRenderer(new RowRenderer<ReportQcTimeWaitOfCustomer>() {

			@Override
			public void render(Row row, ReportQcTimeWaitOfCustomer data,
					int index) throws Exception {
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + (data.getNumberMobile() == null? "" : data.getNumberMobile())));
				row.appendChild(new Label("" + data.getBeginOrderTime()));
				row.appendChild(new Label("" + data.getBeginOrderAddress()));
				row.appendChild(new Label("" + data.getMinute()));
				row.appendChild(new Label("" + data.getRepeatTime()));
				row.appendChild(new Label("" + data.getStatus()));
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
		col.setHflex("15%");
		col.setLabel("Số điện thoại");

		col = new Column();
		col.setParent(cols);
		col.setHflex("15%");
		col.setLabel("Thời điểm đăng ký");

		col = new Column();
		col.setParent(cols);
		col.setHflex("35%");
		col.setLabel("Địa điểm yêu cầu");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Số phút chờ");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Số lần gọi nhắc");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Trạng thái");

	}

	public List<ReportQcTimeWaitOfCustomer> displayrptTimeWaitOfCustomer() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Timestamp date = new Timestamp(datefrom.getValue().getTime());
		String minute = txtMinute.getValue().trim();
		List<ReportQcTimeWaitOfCustomer> lstValue = lst
				.getrptTimeWaitOfCustomer(date, minute);
		return lstValue;
	}

	public void exportToReportViewer(Event event) {
		try {
			if (event.getTarget().equals(btnReport)) {

				String maxMinute = txtMinute.getValue().trim();
				Timestamp datetime = new Timestamp(datefrom.getValue()
						.getTime());
				if (maxMinute == null || maxMinute.length() == 0) {
					Messagebox.show("Bạn hãy nhập thời gian chờ xe tối đa !");
					return;
				}
				Map<String, Object> mapParams = new HashMap<String, Object>();

				mapParams.put("maxMinute", maxMinute);
				mapParams.put("time", datetime);

				String reportFile = "ReportTimeWaitCustomer";
				String reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO KHÁCH CHỜ LÂU CHƯA CÓ XE";
				String exportFileName = "bao_cao_khach_cho_lau_chua_co_xe";
				String reportName = "BÁO CÁO KHÁCH CHỜ XE";

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