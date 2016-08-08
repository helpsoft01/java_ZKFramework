package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderContext;
import org.zkoss.pivot.PivotRenderer;
import org.zkoss.pivot.Pivottable;
import org.zkoss.pivot.impl.TabularPivotModel;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.ui.util.VTReportViewer;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportCustomersOrderVehiclesTT extends Window {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Datebox datefrom;
	private Datebox dateto;
	private Grid gridFilter;
	private Button btnSearch;
	private Button btnReport;
	private Button btnExcel;
	private Pivottable pivot;
	private TabularPivotModel model;

	public ReportCustomersOrderVehiclesTT() {
		this.init();
	}

	private void init() {
		this.setShadow(true);
		this.setHflex("1");
		this.setVflex("1");
		this.setClosable(true);

		Vlayout vlayout = new Vlayout();
		vlayout.setParent(this);
		vlayout.setHeight("100%");

		this.createFilter(vlayout);
		this.createData(vlayout);
	}

	private void createFilter(Vlayout vlayout) {
		gridFilter = new Grid();
		gridFilter.setParent(vlayout);
		this.createRowsFilter(gridFilter);
	}

	private void createRowsFilter(Grid gridFilter2) {
		Rows rows = new Rows();
		rows.setParent(gridFilter2);

		Row row = new Row();
		row.setParent(rows);

		Div div = new Div();
		div.setParent(row);
		div.setStyle("margin-top : 10px; margin-left : 400px; margin-bottom : 12px");

		Hbox hbox = new Hbox();
		hbox.setParent(div);

		Label label = new Label(
				"BÁO CÁO TỔNG HỢP BIẾN ĐỘNG LƯỢNG KHÁCH ĐẶT XE THEO THỜI GIAN");
		label.setStyle("color : black;font-weight : bold; font-size : 16px");
		label.setParent(hbox);

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
		div.setStyle("margin-top : 1px ; margin-left : 64px");

		hbox = new Hbox();
		hbox.setParent(div);

		datefrom = new Datebox();
		datefrom.setParent(hbox);
		datefrom.setValue(addHour(new Date(), 00, 00));
		datefrom.setWidth("200px");
		datefrom.setFormat("dd/MM/yyyy  HH:mm");
		datefrom.setConstraint("no empty: Không được để trống ngày");

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
		div.setStyle("margin-top : 1px ; margin-left : 58px");

		hbox = new Hbox();
		hbox.setParent(div);

		dateto = new Datebox();
		dateto.setParent(hbox);
		dateto.setValue(addHour(new Date(), 23, 59));
		dateto.setWidth("200px");
		dateto.setFormat("dd/MM/yyyy  HH:mm");
		dateto.setConstraint("no empty: Không được để trống");

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 2px ; margin-left : 212px");

		hbox = new Hbox();
		hbox.setParent(div);

		// btnSearch
		btnSearch = new Button();
		btnSearch.setParent(hbox);
		btnSearch.setLabel("Tìm kiếm");
		btnSearch.setStyle("color : black; font-weight : bold");
		btnSearch.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				long timedateto = dateto.getValue().getTime();
				long timedatefrom = datefrom.getValue().getTime();
				if (timedateto < timedatefrom) {
					Env.getHomePage().showNotification(
							"Hãy chọn lại thời gian cho báo cáo!",
							Clients.NOTIFICATION_TYPE_ERROR);
				} else {
					model = getModel();
					pivot.setModel(model);
					pivot.setPageSize(14);
					model.setFieldType("Giờ", PivotField.Type.COLUMN);
					model.setFieldType("Ngày", PivotField.Type.ROW);
					model.setFieldType("Số lượng", PivotField.Type.DATA);
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
				String filename = "bao_cao_tong_hop_bien_dong_luong_khach_dat_xe_theo_thoi_gian";
				CommonUtils.exportPivotTableToExcel(pivot,filename);

			}
		});

		div = new Div();
		div.setParent(hl);
		div.setStyle("margin-top : 15px ; margin-left : 300px");

		hbox = new Hbox();
		hbox.setParent(div);

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

	private void createData(Vlayout vlayout) {
		pivot = new Pivottable();
		pivot.setParent(vlayout);
		model = this.getModel();
		pivot.setModel(model);
		pivot.setPageSize(20);
		model.setFieldType("Giờ", PivotField.Type.COLUMN);
		model.setFieldType("Ngày", PivotField.Type.ROW);
		model.setFieldType("Số lượng", PivotField.Type.DATA);
		pivot.setPivotRenderer(new PivotRenderer() {
			@Override
			public String renderSubtotalField(Object data, Pivottable table,
					PivotField field, Calculator calculator) {
				String calLabel = calculator.getLabel();
				String dataLabel = data == null ? "" : data.toString();
				return dataLabel + " " + calLabel;
			}

			@Override
			public String renderGrandTotalField(Pivottable table,
					PivotField field) {
				if (field == null)
					return "Tổng cộng";
				return "Tổng";
			}

			@Override
			public String renderField(Object data, Pivottable table,
					PivotField field) {
				return field.getType() == PivotField.Type.DATA ? field
						.getTitle()
						: field.getType() == PivotField.Type.ROW ? String
								.valueOf(data) : String.valueOf(data) + "h";
			}

			@Override
			public String renderDataField(PivotField field) {
				return field.getFieldName();
			}

			@Override
			public String renderCell(Number data, Pivottable table,
					PivotHeaderContext rowContext,
					PivotHeaderContext columnContext, PivotField dataField) {
				return (data == null) ? "0" : "" + data;
			}

			@Override
			public int getRowSize(Pivottable table, PivotHeaderContext rowc,
					PivotField field) {
				return 20;
			}

			@Override
			public int getColumnSize(Pivottable table, PivotHeaderContext colc,
					PivotField field) {
				if (field.getType() == PivotField.Type.ROW)
					return 100;
				if (colc.isGrandTotal())
					return 65;
				if (field.getType() == PivotField.Type.DATA) {
					return 49;
				}
				return 100;
			}
		});

		Div div = new Div();
		div.setParent(pivot);

		Label label = new Label("");
		label.setParent(div);

		div = new Div();
		div.setParent(pivot);
		div.setStyle("margin-left : 10px");

		label = new Label("Lượng khách đặt xe theo giờ");
		label.setStyle("color : black;font-weight : bold; font-size : 14px");
		label.setParent(div);

		div = new Div();
		div.setParent(pivot);
		div.setStyle("margin-left : 0px");

		label = new Label("Ngày theo dõi");
		label.setStyle("color : black;font-weight : bold; font-size : 14px");
		label.setParent(div);
	}

	private TabularPivotModel getModel() {
		return new TabularPivotModel(this.getModelforPivotTable(),
				this.getCols());
	}

	private List<String> getCols() {
		return Arrays.asList(new String[] { "Ngày", "Giờ", "Loại hình đặt",
				"Số lượng" });
	}

	private List<List<Object>> getModelforPivotTable() {
		Timestamp timedatefrom = new Timestamp(datefrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateto.getValue().getTime());
		ListObjectDatabase lst = new ListObjectDatabase();
		List<List<Object>> lstValue = lst.getrptCustomersOrderVehiclesTT(
				timedatefrom, timedateto, "0");
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

				String reportFile = "rptCustomerOrderVehicles";
				String reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO TỔNG HỢP BIẾN ĐỘNG LƯỢNG KHÁCH ĐẶT XE THEO THỜI GIAN";
				String exportFileName = "bao_cao_tong_hop_bien_dong_luong_khach_dat_xe_theo_thoi_gian";
				String reportName = "BÁO CÁO TỔNG HỢP BIẾN ĐỘNG LƯỢNG KHÁCH ĐẶT XE THEO THỜI GIAN";

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
