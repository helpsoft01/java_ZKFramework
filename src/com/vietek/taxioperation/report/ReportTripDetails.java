package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ReportQcTripDetail;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.CommonUtils;

public class ReportTripDetails extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ReportQcTripDetail> lstData;
	private Textbox txtPhone;

	public ReportTripDetails() {
		super();
		this.removeComponent(this.chosenAgent, true);
		this.removeComponent(this.chosenGroup, true);
		this.removeComponent(this.chosenVehicle, true);
		this.removeComponent(this.chosenDriver, true);
		this.removeComponent(this.txtVehicleNumber, true);
		this.removeComponent(this.txtLicensePlate, true);
	}

	@Override
	public void loadModel() {
		setModelClass(ReportQcTripDetail.class);

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(
				new GridColumn("Số điện thoại", 120, String.class, "getPhoneNumber", "phoneNumber", getModelClass()));
		lstCols.add(new GridColumn("Cuốc khách", 450, Timestamp.class, "getBeginOrderAddress", "beginOrderAddress",
				getModelClass()));
		lstCols.add(new GridColumn("Ngày", 120, String.class, "getBeginOrderTime", "beginOrderTime", getModelClass()));
		lstCols.add(new GridColumn("Giờ gọi đặt xe", 130, String.class, "getBeginOrderTime", "beginOrderTime",
				getModelClass()));
		lstCols.add(new GridColumn("Thời điểm đăng ký", 130, String.class, "getStartRegisterTime", "startRegisterTime",
				getModelClass()));
		lstCols.add(new GridColumn("Danh sách xe đăng ký", 120, String.class, "getListRegisterVeh", "listRegisterVeh",
				getModelClass()));
		lstCols.add(new GridColumn("Giờ đón khách", 130, String.class, "getTimeIsUpdated", "timeIsUpdated",
				getModelClass()));
		lstCols.add(
				new GridColumn("Xe đón khách", 120, String.class, "getSelectedVeh", "selectedVeh", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Chi Tiết Cuốc Khách");

	}

	@Override
	public void initExtraRows(Rows rows) {
		Row row = new Row();
		row.setParent(rows);
		Label label = new Label("Số điện thoại");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);
		txtPhone = new Textbox();
		txtPhone.setParent(row);
	}
	
	@Override
	public void renderExtraReport() {
		gridData.setRowRenderer(new RowRenderer<ReportQcTripDetail>() {

			@Override
			public void render(Row row, ReportQcTripDetail data, int index) throws Exception {
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

	@Override
	public void renderReportWithListBox() {
		setRenderReportWithListBox(false);
	}
	@Override
	public void setStyleForGridData() {
		getGridData().setVflex("true");
		getGridData().setMold("paging");
		gridData.setAutopaging(true);
		getGridData().setSclass("grid_report_total");
	}


	@Override
	public void loadData() {
		lstData = displayrptTripDetail();
		setLstModel(lstData);

	}

	private List<ReportQcTripDetail> displayrptTripDetail() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(dateFrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateTo.getValue().getTime());
		String phonenumber = txtPhone.getValue();
		List<ReportQcTripDetail> lstValues = lst.getrptTripDetail(timedatefrom, timedateto, phonenumber);
		return lstValues;
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(btnExcel)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				Listbox listboxExcel = (Listbox) getListbox();
				Iterator<Component> component = listboxExcel.getChildren().iterator();
				while (component.hasNext()) {
					Component value = component.next();
					if (((value instanceof Frozen)) || ((value instanceof Auxhead))) {
						listboxExcel.removeChild((Component) value);
						component = listboxExcel.getChildren().iterator();
					}
				}
				CommonUtils.exportListboxToExcel(listboxExcel, "bao_cao_chi_tiet_cuoc_khach.xlsx");
			}
		} else {
			super.onEvent(event);
		}
	}


	@Override
	public void setMapParams() {
		Timestamp timefrom = new Timestamp(dateFrom.getValue().getTime());
		Timestamp timeto = new Timestamp(dateTo.getValue().getTime());
		String phonenumber = txtPhone.getValue().trim();
		
		mapParamsReport.put("_fromdate", timefrom);
		mapParamsReport.put("_todate", timeto);
		mapParamsReport.put("_phone", phonenumber);

		reportFile = "rptTripDetailFromOperation";
		reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO CHI TIẾT CUỐC KHÁCH QUA TỔNG ĐÀI";
		exportFileName = "bao_cao_chi_tiet_cuoc_khach_qua_tong_dai";
		reportName = "BÁO CÁO CHI TIẾT CUỐC KHÁCH QUA TỔNG ĐÀI";

	}

}
