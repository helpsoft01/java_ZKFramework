package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.vietek.taxioperation.common.IntFormat;
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.RptTotalBussinessAgent;
import com.vietek.taxioperation.model.RptTotalViolateQc31;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;

public class ReportTotalBussinessAgent extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<RptTotalBussinessAgent> lstData;
	
	public ReportTotalBussinessAgent() {
		super();
		this.removeComponent(this.chosenVehicle, true);
		this.removeComponent(this.chosenDriver, true);
		this.removeComponent(this.txtVehicleNumber, true);
		this.removeComponent(this.txtLicensePlate, true);
	}

	@Override
	public void loadModel() {
		setModelClass(RptTotalBussinessAgent.class);

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Chi nhánh", 250, String.class, "getAgentName", "agentName", getModelClass()));
		lstCols.add(new GridColumn("Đội xe", 250, String.class, "getAgentGroup", "agentGroup", getModelClass()));
		lstCols.add(new GridColumn("Số điện thoại", 120, String.class, "getPhone", "phone", getModelClass()));
		lstCols.add(new GridColumn("Số Fax", 120, String.class, "getFax", "fax", getModelClass()));
		lstCols.add(new GridColumn("Thời điểm", 130, Timestamp.class, "getTimeLog", "timeLog",getModelClass()));
		lstCols.add(new GridColumn("Tổng số xe", 120, Integer.class, "getTotalVehicle", "totalVehicle",getModelClass()));
		lstCols.add(new GridColumn("Tổng xe hoạt động", 120, Integer.class, "getTotalActiveVehicle", "totalActiveVehicle", getModelClass()));
		lstCols.add(new GridColumn("Tổng xe về giao ca", 120, Integer.class, "getTotalFinishVehicle", "totalFinishVehicle", getModelClass()));
		lstCols.add(new GridColumn("Tổng xe đúp ca", 120, Integer.class, "getTotalDuplicateVehicle", "totalDuplicateVehicle", getModelClass()));
		lstCols.add(new GridColumn("Tổng xe Online", 120, Integer.class, "getTotalOnlineVehicle", "totalOnlineVehicle", getModelClass()));
		lstCols.add(new GridColumn("Doanh thu", 100, Integer.class, "getMoney", "money", getModelClass()));
		lstCols.add(new GridColumn("Bình quân", 100, Integer.class, "getAvgMoney", "avgMoney", getModelClass()));
		lstCols.add(new GridColumn("Tỷ lệ xe theo doanh thu", 150, Double.class, "getPercent", "percent", getModelClass()));
		lstCols.add(new GridColumn("Vào xưởng", 100, Integer.class, "getInWorkShop", "inWorkShop", getModelClass()));
		lstCols.add(new GridColumn("Tai nạn", 100, Integer.class, "getInAccident", "inAccident", getModelClass()));

		setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Tổng Hợp Doanh Thu Và Hoạt Động Taxi Theo Đơn Vị");

	}

	@Override
	public void renderReportWithListBox() {
		setRenderReportWithListBox(false);

	}

	@Override
	public void loadData() {
		lstData = displayRptTotalBussinessAgent();
		setLstModel(lstData);

	}

	@Override
	public void setMapParams() {
		Timestamp fromdate = new Timestamp(dateFrom.getValue().getTime());
		Timestamp todate = new Timestamp(dateTo.getValue().getTime());
		String stragentid = ""
				+ (getStrAgentId() == null ? "0" : (getStrAgentId().length() == 0) ? "0" : "" + getStrAgentId());
		String strgrouptid = ""
				+ (getStrGroupId() == null ? "0" : (getStrGroupId().length() == 0) ? "0" : "" + getStrGroupId());
		mapParamsReport.put("_fromdate", fromdate);
		mapParamsReport.put("_todate", todate);
		mapParamsReport.put("lst_agentId", stragentid);
		mapParamsReport.put("lst_groupId", strgrouptid);
		mapParamsReport.put("_user", user);

		reportFile = "ReportTotalBussinessAgent";
		reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO TỔNG HỢP DOANH THU VÀ HOẠT ĐỘNG TAXI";
		exportFileName = "bao_cao_tong_hop_doanh_thu_va_hoat_dong_taxi";
		reportName = "BÁO CÁO TỔNG HỢP DOANH THU VÀ HOẠT ĐỘNG THEO TAXI";

	}
	@Override
	public void renderExtraReport() {
		gridData.setRowRenderer(new RowRenderer<RptTotalBussinessAgent>() {

			@Override
			public void render(Row row, RptTotalBussinessAgent data, int index) throws Exception {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + data.getAgentName()));
				row.appendChild(new Label("" + data.getAgentGroup()));
				row.appendChild(new Label("" + data.getPhone()));
				row.appendChild(new Label("" + data.getFax()));
				row.appendChild(new Label("" + sdf.format(data.getTimeLog())));
				row.appendChild(new Label("" + data.getTotalVehicle()));
				row.appendChild(new Label("" + data.getTotalActiveVehicle()));
				row.appendChild(new Label("" + data.getTotalFinishVehicle()));
				row.appendChild(new Label("" + data.getTotalDuplicateVehicle()));
				row.appendChild(new Label("" + data.getTotalOnlineVehicle()));
				row.appendChild(new Label("" + IntFormat.formatTypeInt("###,###", data.getMoney())));
				row.appendChild(new Label("" + IntFormat.formatTypeInt("###,###", data.getAvgMoney())));
				row.appendChild(new Label("" + data.getPercent()));
				row.appendChild(new Label("" + data.getInWorkShop()));
				row.appendChild(new Label("" + data.getInAccident()));
				
			}
		});
	}
	
	// Load du lieu tu DB
	private List<RptTotalBussinessAgent> displayRptTotalBussinessAgent(){
		ListObjectDatabase lstObj = new ListObjectDatabase();
		if (getDateTo().getValue().getTime() < getDateFrom().getValue().getTime()) {
			Env.getHomePage().showNotification("Hãy chọn lại thời gian cho báo cáo!", Clients.NOTIFICATION_TYPE_ERROR);
		}
		Timestamp timedatefrom = new Timestamp(getDateFrom().getValue().getTime());
		Timestamp timedateto = new Timestamp(getDateTo().getValue().getTime());

		String stragentid = "" + (getStrAgentId() == null ? "0" : "" + getStrAgentId());
		String strgroupid = ""
				+ (getStrGroupId() == null ? "0" : (getStrGroupId().length() == 0) ? "0" : "" + getStrGroupId());

		List<RptTotalBussinessAgent> lstvalue = lstObj.getReportTotalBussinessAgent(timedatefrom, timedateto, stragentid,
				strgroupid, user);
		return lstvalue;

	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(btnExcel)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				Grid gridExcel = (Grid) getGridData();
				Iterator<Component> component = gridExcel.getChildren().iterator();
				while (component.hasNext()) {
					Component value = component.next();
					if (((value instanceof Frozen)) || ((value instanceof Auxhead))) {
						gridExcel.removeChild((Component) value);
						component = gridExcel.getChildren().iterator();
					}
				}
				CommonUtils.exportListboxToExcel(gridExcel, "bao_cao_tong_hop_doanh_thu_va hoat_dong_taxi.xlsx");
			}
		} else {
			super.onEvent(event);
		}
	}

}
