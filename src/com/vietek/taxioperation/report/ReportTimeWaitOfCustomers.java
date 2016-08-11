package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ReportQcTimeWaitOfCustomer;
import com.vietek.taxioperation.ui.util.ComponentsReport;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.CommonUtils;

public class ReportTimeWaitOfCustomers extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Datebox datetime;
	private Textbox txtMinute;
	private Textbox txtZone;
	private List<ReportQcTimeWaitOfCustomer> lstData;

	public ReportTimeWaitOfCustomers() {
		super();
		this.removeComponent(this.dateFrom, true);
		this.removeComponent(this.dateTo, true);
		this.removeComponent(this.chosenAgent, true);
		this.removeComponent(this.chosenGroup, true);
		this.removeComponent(this.chosenVehicle, true);
		this.removeComponent(this.chosenDriver, true);
		this.removeComponent(this.txtVehicleNumber, true);
		this.removeComponent(this.txtLicensePlate, true);
	}

	@Override
	public void loadModel() {
		setModelClass(ReportQcTimeWaitOfCustomer.class);

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(
				new GridColumn("Số điện thoại", 120, String.class, "getNumberMobile", "numberMobile", getModelClass()));
		lstCols.add(new GridColumn("Thời điểm đăng ký", 150, Timestamp.class, "getBeginOrderTime", "beginOrderTime",
				getModelClass()));
		lstCols.add(new GridColumn("Địa điểm yêu cầu", 450, String.class, "getBeginOrderAddress", "beginOrderAddress",
				getModelClass()));
		lstCols.add(new GridColumn("Số phút chờ", 100, String.class, "getMinute", "minute", getModelClass()));
		lstCols.add(
				new GridColumn("Số lần gọi nhắc", 100, String.class, "getRepeatTime", "repeatTime", getModelClass()));
		lstCols.add(new GridColumn("Trạng thái", 120, String.class, "getStatus", "status", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Khách Chờ Lâu Chưa Có Xe");

	}
	
	@Override
	public void initExtraRows(Rows rows) {
		ComponentsReport comReport = new ComponentsReport();
		
		Row row = new Row();
		row.setParent(rows);
		Label label = new Label("Thời điểm");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);
		datetime = comReport.dateboxDisplay(true, row, "dd/MM/yyyy HH:mm:ss", 160, 00, 00, 00);
		datetime.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		label = new Label("Số phút chờ");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);
		txtMinute = new Textbox();
		txtMinute.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		label = new Label("Khu vực");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);
		txtZone = new Textbox();
		txtZone.setParent(row);
		
	}

	@Override
	public void renderReportWithListBox() {

	}

	@Override
	public void loadData() {
		lstData = displayrptTimeWaitOfCustomer();
		setLstModel(lstData);

	}
	
	private List<ReportQcTimeWaitOfCustomer> displayrptTimeWaitOfCustomer() {
		ListObjectDatabase lst = new ListObjectDatabase();
		Timestamp time = new Timestamp(datetime.getValue().getTime());
		String minute = txtMinute.getValue().trim();
		List<ReportQcTimeWaitOfCustomer> lstValue = lst
				.getrptTimeWaitOfCustomer(time, minute);
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
				CommonUtils.exportListboxToExcel(listboxExcel, "bao_cao_khach_cho_xe.xlsx");
			}
		} else {
			super.onEvent(event);
		}
	}

	@Override
	// Xem lại
	public void setMapParams() {
		Timestamp time = new Timestamp(datetime.getValue().getTime());
		String minute = txtMinute.getValue().trim();
		
		mapParamsReport.put("maxMinute", minute);
		mapParamsReport.put("time", time);

		reportFile = "ReportTimeWaitCustomer";
		reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO KHÁCH CHỜ LÂU CHƯA CÓ XE";
		exportFileName = "bao_cao_khach_cho_lau_chua_co_xe";
		reportName = "BÁO CÁO KHÁCH CHỜ XE";

	}

}
