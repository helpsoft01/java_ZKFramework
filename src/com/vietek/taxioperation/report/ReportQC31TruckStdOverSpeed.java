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
import com.vietek.taxioperation.model.RptQcTruckStdOverSpeed;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportQC31TruckStdOverSpeed extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<RptQcTruckStdOverSpeed> lstData;

	public ReportQC31TruckStdOverSpeed() {
		super();
		this.removeComponent(this.chosenAgent, true);
		this.removeComponent(this.chosenDriver, true);
		this.removeComponent(this.txtVehicleNumber, true);
		this.removeComponent(this.txtLicensePlate, true);
	}

	@Override
	public void loadModel() {
		setModelClass(RptQcTruckStdOverSpeed.class);
	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("BKS", 100, String.class, "getLicensePlate", "licensePlate", getModelClass()));
		lstCols.add(new GridColumn("Loại kinh doanh", 100, String.class, "getTypeName", "typeName", getModelClass()));
		lstCols.add(new GridColumn("Tốc độ", 130, Integer.class, "getSpeed", "speed", getModelClass()));
		lstCols.add(
				new GridColumn("Tốc độ cho phép", 130, Integer.class, "getSpeedLimit", "speedLimit", getModelClass()));
		lstCols.add(new GridColumn("Thời điểm bắt đầu", 130, Timestamp.class, "getTimeStart", "timeStart",
				getModelClass()));
		lstCols.add(
				new GridColumn("Thời điểm kết thúc", 130, Timestamp.class, "getTimeStop", "timeStop", getModelClass()));
		lstCols.add(new GridColumn("Thời gian", 100, String.class, "getTime", "time", getModelClass()));
		lstCols.add(new GridColumn("Vị trí bắt đầu", 450, String.class, "getAddrBegin", "addrBegin", getModelClass()));
		lstCols.add(
				new GridColumn("Vị trí kết thúc", 450, String.class, "getAddrFinish", "addrFinish", getModelClass()));
		lstCols.add(new GridColumn("Quãng đường(km)", 150, String.class, "getKm", "km", getModelClass()));
		lstCols.add(new GridColumn("Hành trình", 120, Boolean.class, "getIsHistory", "isHistory", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Quá Tốc Độ Giới Hạn");
	}

	@Override
	public void loadData() {
		lstData = displayRptQcTruckSpeed();
		setLstModel(lstData);
	}

	// Ham tim list Model
	private List<RptQcTruckStdOverSpeed> displayRptQcTruckSpeed() {
		ListObjectDatabase lstObj = new ListObjectDatabase();

		if (dateTo.getValue().getTime() < dateFrom.getValue().getTime()) {
			Env.getHomePage().showNotification("Hãy chọn lại thời gian cho báo cáo!", Clients.NOTIFICATION_TYPE_ERROR);
		}
		Timestamp timedatefrom = new Timestamp(dateFrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateTo.getValue().getTime());

		String strgroupid = "" + (strGroupId == null ? "0" : (strGroupId.length() == 0) ? "0" : "" + strGroupId);
		String strvehicleid = ""
				+ (strVehicleId == null ? "0" : (strVehicleId.length() == 0) ? "0" : "" + strVehicleId);
		List<RptQcTruckStdOverSpeed> lstValue = lstObj.getQcTruckStdSpeed(user, timedatefrom, timedateto, strgroupid,
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

				CommonUtils.exportListboxToExcel(listboxExcel, "bao_cao_qua_toc_do_gioi_han.xlsx");
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

		reportFile = "ReportQC31TruckOverSpeed";
		reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO QUÁ TỐC ĐỘ GIỚI HẠN";
		exportFileName = "bao_cao_qua_toc_do_gioi_han";
		reportName = "BÁO CÁO QUÁ TỐC ĐỘ GIỚI HẠN";
	}

	@Override
	public void renderReportWithListBox() {
		// TODO Auto-generated method stub
		
	}
}
