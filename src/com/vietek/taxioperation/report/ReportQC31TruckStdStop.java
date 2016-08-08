package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Listbox;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.RptQcTruckStdStop;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportQC31TruckStdStop extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<RptQcTruckStdStop> lstData;

	public ReportQC31TruckStdStop() {
		super();
		this.removeComponent(this.chosenAgent, true);
		this.removeComponent(this.chosenDriver, true);
		this.removeComponent(this.txtVehicleNumber, true);
		this.removeComponent(this.txtLicensePlate, true);
	}

	@Override
	public void loadModel() {
		setModelClass(RptQcTruckStdStop.class);
	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("BKS", 100, String.class, "getLicensePlate", "licensePlate", getModelClass()));
		lstCols.add(new GridColumn("Tên tài xế", 150, String.class, "getDriverName", "driverName", getModelClass()));
		lstCols.add(new GridColumn("GPLX", 100, String.class, "getDriverLicense", "driverLicense", getModelClass()));
		lstCols.add(new GridColumn("Loại kinh doanh", 100, String.class, "getTypeName", "typeName", getModelClass()));
		lstCols.add(new GridColumn("Thời điểm bắt đầu", 130, Timestamp.class, "getTimeStart", "timeStart",
				getModelClass()));
		lstCols.add(
				new GridColumn("Thời điểm kết thúc", 130, Timestamp.class, "getTimeEnd", "timeEnd", getModelClass()));
		lstCols.add(new GridColumn("Thời gian", 100, String.class, "getTimeOverStr", "timeOverStr", getModelClass()));
		lstCols.add(new GridColumn("Tọa độ", 150, String.class, "getLocation", "location", getModelClass()));
		lstCols.add(new GridColumn("Địa điểm", 450, String.class, "getAddr", "addr", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Dừng Đỗ");
	}

	@Override
	public void loadData() {
		lstData = displayRptQcTruckStop();
		setLstModel(lstData);
	}

	// Ham tim list Model
	private List<RptQcTruckStdStop> displayRptQcTruckStop() {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		if (dateTo.getValue().getTime() < dateFrom.getValue().getTime()) {
			Env.getHomePage().showNotification("Hãy chọn lại thời gian cho báo cáo!", Clients.NOTIFICATION_TYPE_ERROR);
		}

		Timestamp timedatefrom = new Timestamp(dateFrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateTo.getValue().getTime());
		String strgroupid = "" + (strGroupId == null ? "0" : (strGroupId.length() == 0) ? "0" : "" + strGroupId);
		String strvehicleid = ""
				+ (strVehicleId == null ? "0" : (strVehicleId.length() == 0) ? "0" : "" + strVehicleId);
		List<RptQcTruckStdStop> lstValue = lstObj.getQcTruckStdStop(user, timedatefrom, timedateto, strgroupid,
				strvehicleid);
		return lstValue;
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

				CommonUtils.exportListboxToExcel(listboxExcel, "bao_cao_dung_do.xlsx");
			}
		} else {
			super.onEvent(event);
		}

	}

	@Override
	public void setMapParams() {
		Timestamp fromdate = new Timestamp(dateFrom.getValue().getTime());
		Timestamp todate = new Timestamp(dateTo.getValue().getTime());
		String strvehicleid = ""
				+ (strVehicleId == null ? "0" : (strVehicleId.length() == 0) ? "0" : "" + strVehicleId);
		mapParamsReport.put("_fromdate", fromdate);
		mapParamsReport.put("_todate", todate);
		mapParamsReport.put("lst_groupId", strGroupId);
		mapParamsReport.put("lst_vehicleId", strvehicleid);
		mapParamsReport.put("_user", user);

		reportFile = "ReportQC31TruckStop";
		reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO DỪNG ĐỖ";
		exportFileName = "bao_cao_dung_do";
		reportName = "BÁO CÁO DỪNG ĐỖ";
	}

	@Override
	public void renderReportWithListBox() {
		// TODO Auto-generated method stub
		
	}
}
