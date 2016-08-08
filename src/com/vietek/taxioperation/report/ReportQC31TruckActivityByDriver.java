package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Listbox;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.RptQcTruckStdActivityByVehicle;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportQC31TruckActivityByDriver extends AbstractReportWindow implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<RptQcTruckStdActivityByVehicle> lstData;

	public ReportQC31TruckActivityByDriver() {
		super();
		this.removeComponent(this.chosenAgent, true);
		this.removeComponent(this.chosenVehicle, true);
		this.removeComponent(this.txtVehicleNumber, true);
		this.removeComponent(this.txtLicensePlate, true);
	}

	@Override
	public void loadModel() {
		setModelClass(RptQcTruckStdActivityByVehicle.class);
	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Lái xe", 150, String.class, "getNameDriver", "nameDriver", getModelClass()));
		lstCols.add(new GridColumn("GPLX", 130, String.class, "getDriverLicense", "driverLicense", getModelClass()));
		lstCols.add(new GridColumn("Tổng Km", 130, Integer.class, "getKmGps", "kmGps", getModelClass()));
		lstCols.add(new GridColumn("5km - 10km", 150, Float.class, "getFivePerPath", "fivePerPath", getModelClass()));
		lstCols.add(new GridColumn("10km - 20km", 150, Float.class, "getTenPerPath", "tenPerPath", getModelClass()));
		lstCols.add(
				new GridColumn("20km - 30km", 150, Float.class, "getTwentyPerPath", "twentyPerPath", getModelClass()));
		lstCols.add(
				new GridColumn("Trên 30km", 150, Float.class, "getThirthPerPath", "thirthPerPath", getModelClass()));
		lstCols.add(new GridColumn("5km - 10km", 130, Integer.class, "getFiveTime", "fiveTime", getModelClass()));
		lstCols.add(new GridColumn("10km - 20km", 130, Integer.class, "getTenTime", "tenTime", getModelClass()));
		lstCols.add(new GridColumn("20km - 30km", 130, Integer.class, "getTwentyTime", "twentyTime", getModelClass()));
		lstCols.add(new GridColumn("Trên 30km", 130, Integer.class, "getThirthTime", "thirthTime", getModelClass()));
		lstCols.add(new GridColumn("Tổng số lần lái xe quá 4h", 150, Integer.class, "getOverFourHour", "overFourHour",
				getModelClass()));
		lstCols.add(new GridColumn("Ghi chú", 200, String.class, "getNote", "note", getModelClass()));

		setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Tổng Hợp Hoạt Động Theo Tài Xế");
	}

	@Override
	public void createHeaderExtra(Listbox listbox) {
		Auxhead head = new Auxhead();
		head.setParent(listbox);

		Auxheader header = new Auxheader();
		header.setParent(head);
		header.setColspan(4);

		header = new Auxheader("Tỷ lệ quá tốc độ giới hạn/Tổng km (%)");
		header.setParent(head);
		header.setColspan(4);
		header.setAlign("center");

		header = new Auxheader("Số lần quá tốc độ giới hạn /Tổng km (%)");
		header.setParent(head);
		header.setColspan(4);
		header.setAlign("center");

		header = new Auxheader();
		header.setParent(head);
		header.setColspan(2);

	}

	@Override
	public void loadData() {
		lstData = displayRptQcTruckActivityByDriver();
		setLstModel(lstData);
	}

	// Ham tim list Model
	private List<RptQcTruckStdActivityByVehicle> displayRptQcTruckActivityByDriver() {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		if (dateTo.getValue().getTime() < dateFrom.getValue().getTime()) {
			Env.getHomePage().showNotification("Hãy chọn lại thời gian cho báo cáo!", Clients.NOTIFICATION_TYPE_ERROR);
		}

		Timestamp timedatefrom = new Timestamp(dateFrom.getValue().getTime());
		Timestamp timedateto = new Timestamp(dateTo.getValue().getTime());
		String strgroupid = "" + (strGroupId == null ? "0" : (strGroupId.length() == 0) ? "0" : "" + strGroupId);
		String strdriverid = "" + (strDriverId == null ? "0" : (strDriverId.length() == 0) ? "0" : "" + strDriverId);
		List<RptQcTruckStdActivityByVehicle> lstValue = lstObj.getQcTruckStdActivityByDriver(timedatefrom, timedateto,
				strgroupid, strdriverid, user);
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

				CommonUtils.exportListboxToExcel(listboxExcel, "bao_cao_tong_hop_hoat_dong_theo_tai_xe.xlsx");
			}
		} else {
			super.onEvent(event);
		}
	}

	@Override
	public void setMapParams() {
		Timestamp fromdate = new Timestamp(dateFrom.getValue().getTime());
		Timestamp todate = new Timestamp(dateTo.getValue().getTime());
		String strdriverid = "" + (strDriverId == null ? "0" : (strDriverId.length() == 0) ? "0" : "" + strDriverId);
		mapParamsReport.put("_fromdate", fromdate);
		mapParamsReport.put("_todate", todate);
		mapParamsReport.put("lst_groupId", strGroupId);
		mapParamsReport.put("lst_driverId", strdriverid);
		mapParamsReport.put("_user", user);

		reportFile = "ReportQC31TruckActivityByDriver";
		reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO TỔNG HỢP HOẠT ĐỘNG THEO TÀI XẾ";
		exportFileName = "bao_cao_tong_hop_hoat_dong_theo_tai_xe";
		reportName = "BÁO CÁO TỔNG HỢP HOẠT ĐỘNG THEO TÀI XẾ";
	}

	@Override
	public void renderReportWithListBox() {
		// TODO Auto-generated method stub
		
	}
}
