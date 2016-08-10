package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ReportQcShiftUsers;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.ui.util.GridColumn;

public class ReportDetailShiftUsers extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Chosenbox chosenUser;
	private List<ReportQcShiftUsers> lstData;

	public ReportDetailShiftUsers() {
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
		setModelClass(ReportQcShiftUsers.class);

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 60, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Điện thoại viên", 220, String.class, "getUsername", "username", getModelClass()));
		lstCols.add(
				new GridColumn("Ngày làm việc", 200, Timestamp.class, "getOrdertime", "ordertime", getModelClass()));
		lstCols.add(new GridColumn("Line làm việc", 150, String.class, "getCallnumber", "callnumber", getModelClass()));
		lstCols.add(new GridColumn("Tổng cuộc gọi đã nhận", 150, String.class, "getTotalcall", "totalcall",
				getModelClass()));
		lstCols.add(new GridColumn("Tổng cuộc gọi đáp ứng", 150, String.class, "getAcceptcall", "acceptcall",
				getModelClass()));
		lstCols.add(new GridColumn("Tỷ lệ (%)", 150, String.class, "getPercent", "percent", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Chi Tiết Ngày Làm Việc Theo Điện Thoại Viên");

	}

	@Override
	public void initExtraRows(Rows rows) {
		Row row = new Row();
		row.setParent(rows);
		Label label = new Label("ĐTV");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);

		chosenUser = new Chosenbox();
		chosenUser.setParent(row);
		chosenUser.setWidth("160px");
		ListObjectDatabase lstsysuser = new ListObjectDatabase();
		List<SysUser> lstValue = lstsysuser.getSysuser();
		chosenUser.setModel(new ListModelList<>(lstValue));
	}

	@Override
	public void renderReportWithListBox() {

	}

	@Override
	public void loadData() {
		lstData = displayDetailShiftUsers();
		setLstModel(lstData);

	}

	private List<ReportQcShiftUsers> displayDetailShiftUsers() {
		ListObjectDatabase lst = new ListObjectDatabase();
		// Set<ShiftworkTms> lstShift = chosenbox.getSelectedObjects();
		// StringBuilder lstShiftID = new StringBuilder();
		// lstShiftID.append("0");
		// for (ShiftworkTms shiftworkTms : lstShift) {
		// lstShiftID.append("," + shiftworkTms.getId());
		// }
		Set<SysUser> lstsysuser = chosenUser.getSelectedObjects();
		StringBuilder lstsysuserID = new StringBuilder();
		lstsysuserID.append("0");
		for (SysUser sysUser : lstsysuser) {
			lstsysuserID.append("," + sysUser.getId());
		}
		Timestamp timedatefrom = new Timestamp(dateFrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateTo.getValue().getTime());
		List<ReportQcShiftUsers> lstValue = lst.getrptShiftWorkingDetailByTelephonist(timedatefrom, timedateto, "0",
				"" + lstsysuserID);
		return lstValue;
	}

	@Override
	public void setMapParams() {
		// TODO Auto-generated method stub

	}

}
