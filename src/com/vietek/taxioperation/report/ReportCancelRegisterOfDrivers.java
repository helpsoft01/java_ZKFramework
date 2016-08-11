package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Listbox;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ReportQcCancelRegistrationDriver;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.CommonUtils;

public class ReportCancelRegisterOfDrivers extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ReportQcCancelRegistrationDriver> lstData;

	public ReportCancelRegisterOfDrivers() {
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
		setModelClass(ReportQcCancelRegistrationDriver.class);

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(
				new GridColumn("Số điện thoại khách", 120, String.class, "getPhoneCustomer", "phoneCustomer", getModelClass()));
		lstCols.add(new GridColumn("Thời gian khách gọi", 150, Timestamp.class, "getTimeorder", "timeorder",
				getModelClass()));
		lstCols.add(new GridColumn("Địa điểm yêu cầu", 400, String.class, "getPlaceorder", "placeorder",
				getModelClass()));
		lstCols.add(new GridColumn("Tên lái xe", 130, String.class, "getNameDriver", "nameDriver", getModelClass()));
		lstCols.add(
				new GridColumn("Số tài", 70, String.class, "getVehicleNumber", "vehicleNumber", getModelClass()));
		lstCols.add(new GridColumn("Biển số xe", 100, String.class, "getLicensePlate", "licensePlate", getModelClass()));
		lstCols.add(new GridColumn("Thời điểm đăng ký", 130, String.class, "getTimeRegister", "timeRegister", getModelClass()));
		lstCols.add(new GridColumn("Thời điểm hủy", 130, String.class, "getTimeCancel", "timeCancel", getModelClass()));
		lstCols.add(new GridColumn("Lý do hủy", 120, String.class, "getReasonCancel", "reasonCancel", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Xe Hủy Đăng Ký Cuốc Khách");

	}

	@Override
	public void renderReportWithListBox() {

	}

	@Override
	public void loadData() {
		lstData = displayrptCancelRegistrationDriver();
		setLstModel(lstData);

	}
	
	private List<ReportQcCancelRegistrationDriver> displayrptCancelRegistrationDriver() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(dateFrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateTo.getValue().getTime());
		List<ReportQcCancelRegistrationDriver> lstValue = lst
				.getrptCancelRegistrationDriver(timedatefrom, timedateto);
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
				CommonUtils.exportListboxToExcel(listboxExcel, "bao_cao_xe_huy_dang_ky_cuoc_khach.xlsx");
			}
		} else {
			super.onEvent(event);
		}
	}

	@Override
	public void setMapParams() {
		Timestamp timefrom = new Timestamp(dateFrom.getValue().getTime());
		Timestamp timeto = new Timestamp(dateTo.getValue().getTime());
		
		mapParamsReport.put("datefrom", timefrom);
		mapParamsReport.put("dateto", timeto);

		reportFile = "ReportCancelRegistrationDriver";
		reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO XE HỦY ĐĂNG KÝ CUỐC";
		exportFileName = "bao_cao_xe_huy_dang_ky_cuoc";
		reportName = "BÁO CÁO XE HỦY ĐĂNG KÝ CUỐC";

	}

}
