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
import com.vietek.taxioperation.model.ReportQcShiftUsersTT;
import com.vietek.taxioperation.model.ShiftworkTms;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.ui.util.VTReportViewer;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportShiftUsersTT extends Window {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Datebox datefrom;
	private Datebox dateto;
	private Chosenbox chosenbox;
	private Chosenbox chosenboxusers;
	private Grid griddata;
	private Grid gridFilter;
	private Button btnsearch;
	private Button btnReport;
	private Button btnExcel;
	private Label labelsum;

	public ReportShiftUsersTT() {
		this.init();
	}

	private void init() {
		this.setShadow(true);
		this.setId("rpshiftusersTT");
		this.setSclass("rpshiftusersTT");
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
		gridFilter.setId("gridFilter");
		gridFilter.setSclass("gridFilter");
		gridFilter.setParent(vlayout);
		this.createRowsFilters(gridFilter);

	}

	private void createRowsFilters(Grid grid) {
		Rows rows = new Rows();
		rows.setParent(grid);

		// Hang 1
		Row row = new Row();
		row.setParent(rows);

		Div div = new Div();
		div.setParent(row);
		div.setStyle("margin-top : 10px; margin-left : 400px; margin-bottom : 12px");

		Hbox hbox = new Hbox();
		hbox.setParent(div);

		Label label = new Label(
				"BÁO CÁO TỔNG HỢP NGÀY LÀM VIỆC THEO ĐIỆN THOẠI VIÊN");
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

		// Datefrom
		datefrom = new Datebox();
		datefrom.setParent(hbox);
		datefrom.setValue(addHour(new Date(), 00, 00));
		datefrom.setWidth("170px");
		datefrom.setFormat("dd/MM/yyyy  HH:mm");
		datefrom.setConstraint("no empty: Không được để trống");

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

		// ChosenboxShift
		this.createchosenboxShift(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 50px");

		hbox = new Hbox();
		hbox.setParent(div);

		// btnSearch
		btnsearch = new Button("Tìm kiếm");
		btnsearch.setParent(hbox);
		btnsearch.setStyle("color : black; font-weight : bold");
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
					List<ReportQcShiftUsersTT> lstData = new ArrayList<ReportQcShiftUsersTT>();
					griddata.setEmptyMessage("Không có dữ liệu !");
					griddata.setModel(new ListModelList<ReportQcShiftUsersTT>(
							lstData));
					labelsum.setValue("Tổng số bản ghi báo cáo : " + 0);
				} else {
					List<ReportQcShiftUsersTT> lstData = displayrptShiftWorking();
					if (lstData == null || lstData.isEmpty()) {
						griddata.setEmptyMessage("Không có dữ liệu !");
					} else {
						griddata.setModel(new ListModelList<ReportQcShiftUsersTT>(
								lstData));
						labelsum.setValue("Tổng số bản ghi báo cáo : "
								+ lstData.size());
						// Clients.evalJavaScript("hGridShiftUsersTT()");
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
						"bao_cao_tong_hop_ca_lam_viec_cua_nhan_vien.xlsx");
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

		// Dateto
		dateto = new Datebox();
		dateto.setParent(hbox);
		dateto.setValue(addHour(new Date(), 23, 59));
		dateto.setWidth("170px");
		dateto.setFormat("dd/MM/yyyy  HH:mm");
		dateto.setConstraint("no empty: Không được để trống");

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

		// Chosenbox Users
		this.createchosenboxUsers(hbox);

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 10px ; margin-left : 400px");

		hbox = new Hbox();
		hbox.setParent(div);

		// LabelSum
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
		griddata.setId("gridShiftUsersTT");
		griddata.setClass("gridShiftUsersTT");
		griddata.setParent(vlayout);

		this.createColsData(griddata);
		griddata.setRowRenderer(new RowRenderer<ReportQcShiftUsersTT>() {

			@Override
			public void render(Row row, ReportQcShiftUsersTT data, int index)
					throws Exception {
				row.appendChild(new Label("" + ++index));
				row.setAlign("right");
				row.appendChild(new Label("" + data.getUsername()));
				row.appendChild(new Label("" + data.getTotalcall()));
				row.appendChild(new Label("" + data.getAcceptcall()));
				row.appendChild(new Label("" + data.getDeclinecall()));
				row.appendChild(new Label("" + data.getPercent()));

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
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("30%");
		col.setLabel("Điện thoại viên");

		col = new Column();
		col.setParent(cols);
		col.setHflex("20%");
		col.setLabel("Tổng số cuộc gọi đã nhận");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("20%");
		col.setLabel("Tổng cuộc gọi đáp ứng");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("14%");
		col.setLabel("Tổng cuộc gọi từ chối");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setHflex("6%");
		col.setLabel("Tỷ lệ (%)");
		col.setAlign("center");
	}

	public List<ReportQcShiftUsersTT> displayrptShiftWorking() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Set<ShiftworkTms> lstShift = chosenbox.getSelectedObjects();
		StringBuilder shifts = new StringBuilder();
		shifts.append("0,");
		for (ShiftworkTms shiftworkTms : lstShift) {
			shifts.append(shiftworkTms.getId()).append(',');
		}
		Set<SysUser> lstSysUser = chosenboxusers.getSelectedObjects();
		StringBuilder users = new StringBuilder();
		users.append("0,");
		for (SysUser sysUser : lstSysUser) {
			users.append(sysUser.getId()).append(',');
		}
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());
		List<ReportQcShiftUsersTT> lstValue = lst
				.getrptShiftWorkingByTelephonist(timedatefrom, timedateto, ""
						+ lstShift.toString(), "" + users.toString());
		return lstValue;
	}

	public void exportToReportViewer(Event event) {
		try {
			if (event.getTarget().equals(btnReport)) {
				Set<ShiftworkTms> lstShift = chosenbox.getSelectedObjects();
				StringBuilder shifts = new StringBuilder();
				for (ShiftworkTms shiftworkTms : lstShift) {
					shifts.append(shiftworkTms.getId()).append(',');
				}
				Set<SysUser> lstSysUser = chosenboxusers.getSelectedObjects();
				StringBuilder users = new StringBuilder();
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

				String reportFile = "rptShiftWorkingByTelephonist";
				String reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO TỔNG HỢP HOẠT ĐỘNG THEO ĐIỆN THOẠI VIÊN";
				String exportFileName = "bao_cao_tong_hop_lam_viec_dtv";
				String reportName = "BÁO CÁO TỔNG HỢP THEO ĐIỆN THOẠI VIÊN";

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
