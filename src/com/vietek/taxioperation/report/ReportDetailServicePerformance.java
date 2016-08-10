package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ReportQcServicePerformance;
import com.vietek.taxioperation.ui.util.GridColumn;

public class ReportDetailServicePerformance extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ReportQcServicePerformance> lstData;

	public ReportDetailServicePerformance() {
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
		setModelClass(ReportQcServicePerformance.class);

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 60, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Ngày làm việc", 150, Timestamp.class, "getShiftday", "shiftday", getModelClass()));
		lstCols.add(new GridColumn("Ca làm việc", 150, String.class, "getShiftname", "shiftname", getModelClass()));
		lstCols.add(
				new GridColumn("Tổng số khách hàng", 150, Integer.class, "getCustomer", "customer", getModelClass()));
		lstCols.add(new GridColumn("Tổng số cuộc gọi đến", 150, Integer.class, "getRequestnumber", "requestnumber",
				getModelClass()));
		lstCols.add(new GridColumn("Tổng số lần nhắc xe", 150, Integer.class, "getRepeattime", "repeattime",
				getModelClass()));
		lstCols.add(
				new GridColumn("Số cuộc đã nghe", 150, Integer.class, "getTotalcall", "totalcall", getModelClass()));
		lstCols.add(new GridColumn("Số cuộc từ chối phục vụ", 150, Integer.class, "getDeclinecall", "declinecall",
				getModelClass()));
		lstCols.add(new GridColumn("Số cuộc gọi thành công", 150, Integer.class, "getAcceptcall", "acceptcall",
				getModelClass()));
		lstCols.add(new GridColumn("Tỷ lệ thành công (%)", 150, Float.class, "getPercent", "percent", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Chi Tiết Hiệu Suất Tổng Đài Điều Hành");

	}

	@Override
	public void renderReportWithListBox() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadData() {
		lstData = displayRptDetailServicePerformance();
		setLstModel(lstData);

	}

	private List<ReportQcServicePerformance> displayRptDetailServicePerformance() {
		ListObjectDatabase lst = new ListObjectDatabase();
		// Set<ShiftworkTms> lstShift = chosenbox.getSelectedObjects();
		// StringBuilder lstShiftID = new StringBuilder(10)
		// lstShiftID.append("0");
		// for (ShiftworkTms shiftworkTms : lstShift) {
		// lstShiftID.append("," + shiftworkTms.getId());
		// }
		Timestamp timedatefrom = new Timestamp(dateFrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateTo.getValue().getTime());
		List<ReportQcServicePerformance> lstValue = lst.getrptShiftProductivityDetail(timedatefrom, timedateto, "0");
		return lstValue;
	}

	@Override
	public void setMapParams() {
		// TODO Auto-generated method stub

	}

}
