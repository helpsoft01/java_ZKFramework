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
import com.vietek.taxioperation.model.ReportQcShiftUsers;
import com.vietek.taxioperation.model.ShiftworkTms;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.ui.util.VTReportViewer;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportShiftUsers extends Window {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Datebox datefrom;
	private Datebox dateto;
	private Chosenbox chosenbox;
	private Chosenbox chosenboxusers;
	private Grid griddata;
	private Grid gFilter1;
	private Button btnsearch;
	private Button btnReport;
	private Button btnExcel;
	private Label labelsum;

	public ReportShiftUsers() {
		this.init();
	}

	private void init() {
		this.setShadow(true);
		this.setId("rpPerform1");
		this.setSclass("rpPerform1");
		this.setHflex("1");
		this.setVflex("1");
		this.setClosable(true);

		Vlayout vlayout = new Vlayout();
		vlayout.setId("vGrid1");
		vlayout.setSclass("vGrid1");
		vlayout.setStyle("height:100%");
		vlayout.setParent(this);

		this.createFilter(vlayout);
		this.createData(vlayout);
	}

	private void createFilter(Vlayout vlayout) {
		gFilter1 = new Grid();
		gFilter1.setId("gFilter1");
		gFilter1.setSclass("gFilter1");
		gFilter1.setParent(vlayout);
		this.createRowsFilters(gFilter1);
	}

	private void createRowsFilters(Grid grid) {
		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);

		Div div = new Div();
		div.setParent(row);
		div.setStyle("margin-top : 10px; margin-left : 400px; margin-bottom : 12px");

		Hbox hbox = new Hbox();
		hbox.setParent(div);

		Label label = new Label(
				"BÁO CÁO CHI TIẾT NGÀY LÀM VIỆC THEO ĐIỆN THOẠI VIÊN");
		label.setStyle("color : black;font-weight : bold; font-size : 16px");
		label.setParent(hbox);

		// Hang 2
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
		div.setStyle("margin-top : 1px ; margin-left : 30px");

		hbox = new Hbox();
		hbox.setParent(div);

		datefrom = new Datebox();
		datefrom.setParent(hbox);
		datefrom.setValue(addHour(new Date(), 00, 00));
		datefrom.setWidth("170px");
		datefrom.setFormat("dd/MM/yyyy  HH:mm");
		datefrom.setConstraint("no empty: Không được để trống ngày");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 1px ; margin-left : 30px");

		hbox = new Hbox();
		hbox.setParent(div);

		label = new Label("Ca làm việc");
		label.setStyle("font-weight : bold ");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 1px ; margin-left : 30px");

		hbox = new Hbox();
		hbox.setParent(div);

		this.createchosenboxShift(hbox);
		// nut tim kiem
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 50px");

		hbox = new Hbox();
		hbox.setParent(div);

		btnsearch = new Button("Tìm kiếm");
		btnsearch.setParent(hbox);
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
					List<ReportQcShiftUsers> lstData = new ArrayList<ReportQcShiftUsers>();
					griddata.setEmptyMessage("Không có dữ liệu !");
					griddata.setModel(new ListModelList<ReportQcShiftUsers>(
							lstData));
					labelsum.setValue("Tổng số bản ghi báo cáo : " + 0);
				} else {
					List<ReportQcShiftUsers> lstData = displayrptShiftWorkingDetail();
					if (lstData == null || lstData.isEmpty()) {
						griddata.setEmptyMessage("Không có dữ liệu !");
					} else {
						griddata.setModel(new ListModelList<ReportQcShiftUsers>(
								lstData));
						labelsum.setValue("Tổng số bản ghi báo cáo : "
								+ lstData.size());
						Clients.evalJavaScript("hGridShiftUsers()");
					}
				}
			}
		});

		// Nut xuat bao cao
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);

		btnReport = new Button("Báo cáo");
		btnReport.setParent(div);
		btnReport.setStyle("color : black; font-weight : bold");
		btnReport.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				exportToReportViewer(event);
			}
		});

		// Xuat Excel
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 1px");

		hbox = new Hbox();
		hbox.setParent(div);

		btnExcel = new Button("Xuất Excel");
		btnExcel.setParent(div);
		btnExcel.setStyle("color : black; font-weight : bold");
		btnExcel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				CommonUtils.exportListboxToExcel(griddata,
						"bao_cao_chi_tiet_ca_lam_viec_cua_nhan_vien.xlsx");

			}
		});

		// Dong 3
		row = new Row();
		row.setParent(rows);

		hl = new Hlayout();
		hl.setParent(row);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 1px ; margin-left : 36px");

		hbox = new Hbox();
		hbox.setParent(div);

		label = new Label("Đến ngày");
		label.setStyle("font-weight : bold ");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 1px ; margin-left : 24px");

		hbox = new Hbox();
		hbox.setParent(div);

		dateto = new Datebox();
		dateto.setParent(hbox);
		dateto.setValue(addHour(new Date(), 23, 59));
		dateto.setWidth("170px");
		dateto.setFormat("dd/MM/yyyy  HH:mm");
		dateto.setConstraint("no empty: Không được để trống");

		// chosen box
		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 1px ; margin-left : 30px");

		hbox = new Hbox();
		hbox.setParent(div);

		label = new Label("Điện thoại viên");
		label.setStyle("font-weight : bold");
		label.setParent(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 1px ; margin-left : 13px");

		hbox = new Hbox();
		hbox.setParent(div);

		this.createchosenboxUsers(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px ; margin-left : 400px");

		hbox = new Hbox();
		hbox.setParent(div);

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
		chosenbox.setWidth("300px");
		ListObjectDatabase shiftwork = new ListObjectDatabase();
		List<ShiftworkTms> lstValue = shiftwork.getShift();
		chosenbox.setModel(new ListModelList<>(lstValue));
	}

	private void createchosenboxUsers(Hbox hbox) {
		chosenboxusers = new Chosenbox();
		chosenboxusers.setParent(hbox);
		chosenboxusers.setWidth("300px");

		ListObjectDatabase lstsysuser = new ListObjectDatabase();
		List<SysUser> lstValue = lstsysuser.getSysuser();
		chosenboxusers.setModel(new ListModelList<>(lstValue));
	}

	private void createData(Vlayout vlayout) {
		griddata = new Grid();
		griddata.setId("gridShiftUsers");
		griddata.setSclass("gridShiftUsers");
		griddata.setHeight(vlayout.getHeight());
		griddata.setZclass(null);
		griddata.setParent(vlayout);
		griddata.setMold("paging");
		griddata.setAutopaging(true);
		griddata.setSizedByContent(true);

		this.createColsData(griddata);
		griddata.setRowRenderer(new RowRenderer<ReportQcShiftUsers>() {

			@Override
			public void render(Row row, ReportQcShiftUsers data, int index)
					throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + data.getUsername()));
				row.appendChild(new Label(""
						+ (data.getOrdertime() == null ? "" : dateformat
								.format(data.getOrdertime()))));
				row.appendChild(new Label("" + data.getCallnumber()));
				row.appendChild(new Label("" + data.getTotalcall()));
				row.appendChild(new Label("" + data.getAcceptcall()));
				row.appendChild(new Label("" + data.getPercent()));
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
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Điện thoại viên");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("8%");
		col.setLabel("Ngày làm việc");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("12%");
		col.setLabel("Line làm việc");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Tổng cuộc gọi đã nhận");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Tổng cuộc gọi đáp ứng");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setLabel("Tỷ lệ (%)");
		col.setAlign("center");
	}

	public List<ReportQcShiftUsers> displayrptShiftWorkingDetail() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Set<ShiftworkTms> lstShift = chosenbox.getSelectedObjects();
		StringBuilder lstShiftID = new StringBuilder();
		lstShiftID.append("0");
		for (ShiftworkTms shiftworkTms : lstShift) {
			lstShiftID.append("," + shiftworkTms.getId());
		}
		Set<SysUser> lstsysuser = chosenboxusers.getSelectedObjects();
		StringBuilder lstsysuserID = new StringBuilder();
		lstsysuserID.append("0");
		for (SysUser sysUser : lstsysuser) {
			lstsysuserID.append("," + sysUser.getId());
		}
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());
		List<ReportQcShiftUsers> lstValue = lst
				.getrptShiftWorkingDetailByTelephonist(timedatefrom,
						timedateto, "" + lstShiftID, "" + lstsysuserID);
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
				Set<SysUser> lstSysUser = chosenboxusers.getSelectedObjects();
				StringBuilder users = new StringBuilder();
				users.append("0,");
				for (SysUser sysUser : lstSysUser) {
					users.append(sysUser.getId()).append(',');
				}
				Timestamp fromdate = new Timestamp(datefrom.getValue()
						.getTime());
				Timestamp todate = new Timestamp(dateto.getValue().getTime());
				if (fromdate.after(todate)) {
					Messagebox.show("Hãy chọn lại thời gian!");
					return;
				}
				Map<String, Object> mapParams = new HashMap<String, Object>();
				mapParams.put("_fromdate", fromdate);
				mapParams.put("_todate", todate);
				mapParams.put("_shiftID", shifts.toString());
				mapParams.put("_listUserID", users.toString());

				String reportFile = "rptShiftWorkingDetailByTelephonist";
				String reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO CHI TIẾT CA LÀM VIỆC ĐIỆN THOẠI VIÊN";
				String exportFileName = "bao_cao_chi_tiet_ca_lam_viec_dtv";
				String reportName = "BÁO CÁO CHI TIẾT CA LÀM VIỆC ĐIỆN THOẠI VIÊN";

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
