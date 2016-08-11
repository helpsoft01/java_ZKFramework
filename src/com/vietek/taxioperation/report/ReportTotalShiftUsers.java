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
import com.vietek.taxioperation.model.ReportQcShiftUsersTT;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.ui.util.GridColumn;

public class ReportTotalShiftUsers extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Chosenbox chosenUser;
	private List<ReportQcShiftUsersTT> lstData;

	public ReportTotalShiftUsers() {
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
		setModelClass(ReportQcShiftUsersTT.class);

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 60, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Điện thoại viên", 220, String.class, "getUsername", "username", getModelClass()));
		lstCols.add(new GridColumn("Tổng cuộc gọi đã nhận", 150, String.class, "getTotalcall", "totalcall",
				getModelClass()));
		lstCols.add(new GridColumn("Tổng cuộc gọi đáp ứng", 150, String.class, "getAcceptcall", "acceptcall",
				getModelClass()));
		lstCols.add(new GridColumn("Tổng cuộc gọi từ chối", 150, String.class, "getDeclinecall", "declinecall",
				getModelClass()));
		lstCols.add(new GridColumn("Tỷ lệ (%)", 150, String.class, "getPercent", "percent", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Tổng Hợp Ngày Làm Việc Theo Điện Thoại Viên");

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
		// TODO Auto-generated method stub

	}

	@Override
	public void loadData() {
		lstData = displayrptTotalShiftUsers();
		setLstModel(lstData);

	}

	private List<ReportQcShiftUsersTT> displayrptTotalShiftUsers() {
		ListObjectDatabase lst = new ListObjectDatabase();
		// Set<ShiftworkTms> lstShift = chosenbox.getSelectedObjects();
		// StringBuilder shifts = new StringBuilder();
		// shifts.append("0,");
		// for (ShiftworkTms shiftworkTms : lstShift) {
		// shifts.append(shiftworkTms.getId()).append(',');
		// }
		Set<SysUser> lstSysUser = chosenUser.getSelectedObjects();
		StringBuilder users = new StringBuilder();
		users.append("0,");
		for (SysUser sysUser : lstSysUser) {
			users.append(sysUser.getId()).append(',');
		}
		Timestamp timedatefrom = new Timestamp(dateFrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateTo.getValue().getTime());
		List<ReportQcShiftUsersTT> lstValue = lst.getrptShiftWorkingByTelephonist(timedatefrom, timedateto, "0",
				"" + users.toString());
		return lstValue;
	}

	@Override
	public void setMapParams() {
		// TODO Auto-generated method stub

	}

}
