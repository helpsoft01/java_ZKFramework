package com.vietek.tracking.ui.statistic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ParkingArea;
import com.vietek.taxioperation.model.RptDriverInShift;
import com.vietek.taxioperation.report.AbstractReportWindow;
import com.vietek.taxioperation.ui.util.ComponentsReport;
import com.vietek.taxioperation.ui.util.GridColumn;

public class StatisticDriverInShifts extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<RptDriverInShift> lstData;
	private Chosenbox chosenParking;

	public StatisticDriverInShifts() {
		super();
		this.removeComponent(this.getDateTo(), true);
		this.removeComponent(this.getChosenVehicle(), true);
		this.removeComponent(this.getChosenDriver(), true);
		this.removeComponent(this.getTxtVehicleNumber(), true);
		this.removeComponent(this.getTxtLicensePlate(), true);
		getBtnReport().setVisible(false);
		getBtnExcel().setVisible(false);
	}

	@Override
	public void loadModel() {
		setModelClass(RptDriverInShift.class);
	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("", 80, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Ngày", 120, Timestamp.class, "getIssueDate", "issueDate", getModelClass()));
		lstCols.add(new GridColumn("Giờ lên ca", 120, Timestamp.class, "getTimeLog", "timeLog", getModelClass()));
		lstCols.add(new GridColumn("BKS", 100, String.class, "getLicensePlate", "licensePlate", getModelClass()));
		lstCols.add(new GridColumn("Số tài", 100, String.class, "getVehicleNumber", "vehicleNumber", getModelClass()));
		lstCols.add(new GridColumn("MSNV", 100, String.class, "getStaffCard", "staffCard", getModelClass()));
		lstCols.add(new GridColumn("Tài xế", 150, String.class, "getDriverName", "driverName", getModelClass()));
		lstCols.add(new GridColumn("Điện thoại", 120, String.class, "getPhoneNumber", "phoneNumber", getModelClass()));
		lstCols.add(new GridColumn("Trạng thái", 100, String.class, "getDriverState", "driverState", getModelClass()));
		lstCols.add(new GridColumn("Lái liên tục", 100, String.class, "getTimeDrivingContinuous","timeDrivingContinuous", getModelClass()));
		lstCols.add(new GridColumn("Lái trong ngày", 100, String.class, "getTimeDrivingPerDay", "timeDrivingPerDay",getModelClass()));
		lstCols.add(new GridColumn("Bãi giao ca", 300, String.class, "getParkingName", "parkingName", getModelClass()));
		lstCols.add(new GridColumn("Đội xe", 300, String.class, "getGroupName", "groupName", getModelClass()));
		lstCols.add(new GridColumn("Loại xe", 200, String.class, "getTypeName", "typeName", getModelClass()));
		lstCols.add(new GridColumn("Xe nằm bãi", 100, Integer.class, "getInparking", "inparking", getModelClass()));
		lstCols.add(new GridColumn("Xe nằm xưởng", 100, Integer.class, "getInworkshop", "inworkshop", getModelClass()));
		lstCols.add(new GridColumn("Xe nằm đoàn", 100, Integer.class, "getInaccident", "inaccident", getModelClass()));
		lstCols.add(new GridColumn("Ghi chú", 200, String.class, "getNote", "note", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Danh Sách Lên Ca");

	}

	@Override
	public void initExtraRows(Rows rows) {
		Row row = new Row();
		row.setParent(rows);

		Label label = new Label("Bãi xe");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);

		this.createChosenParking(row);
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
		lstData = displayRptDriverInShift();
		setLstModel(lstData);

	}

	@Override
	public void setMapParams() {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderExtraReport() {
		getGridData().setRowRenderer(new RowRenderer<RptDriverInShift>() {

			@Override
			public void render(Row row, RptDriverInShift data, int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat timeformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				
				Button btnUpdate = new Button("Update");
				btnUpdate.setStyle("color : black;font-size : 12px");
				btnUpdate.setHeight("25px");
				row.appendChild(btnUpdate);
				btnUpdate.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						updateDriverInShift(data);
					}
				});
				
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + dateformat.format(data.getIssueDate())));
				Label lbTimeLog = new Label("" + timeformat.format(data.getTimeLog() == null ? "" : data.getTimeLog()));
				row.appendChild(lbTimeLog);
//				row.appendChild(new Label("" + timeformat.format(data.getTimeLog() == null ? "" : data.getTimeLog())));
				row.appendChild(new Label("" + data.getLicensePlate()));
				row.appendChild(new Label("" + data.getVehicleNumber()));
				Label lbStaffCard = new Label("" + (data.getStaffCard() == null ? "" : "" + data.getStaffCard()));
				row.appendChild(lbStaffCard);
//				row.appendChild(new Label("" + (data.getStaffCard() == null ? "" : "" + data.getStaffCard())));
				row.appendChild(new Label("" + data.getDriverName()));
				row.appendChild(new Label("" + data.getPhoneNumber()));
				row.appendChild(new Label("" + data.getDriverState()));
				row.appendChild(new Label("" + data.getTimeDrivingContinuous()));
				row.appendChild(new Label("" + data.getTimeDrivingPerDay()));
				row.appendChild(new Label("" + data.getParkingName()));
				row.appendChild(new Label("" + data.getGroupName()));
				row.appendChild(new Label("" + data.getTypeName()));
				Checkbox chbpark = new Checkbox();
				chbpark.setChecked(data.getInparking() == 1 ? true : false);
				row.appendChild(chbpark);
				Checkbox chbworkshop = new Checkbox();
				chbworkshop.setChecked(data.getInworkshop() == 1 ? true : false);
				row.appendChild(chbworkshop);
				Checkbox chbaccident = new Checkbox();
				chbaccident.setChecked(data.getInaccident() == 1 ? true : false);
				row.appendChild(chbaccident);
				row.appendChild(new Label("" + data.getNote()));
			}
		});
	}

	public List<RptDriverInShift> displayRptDriverInShift() {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		Timestamp timedatefrom = new Timestamp(getDateFrom().getValue().getTime());
		String stragentid = "" + (getStrAgentId() == null ? "0" : "" + getStrAgentId());
		String strgroupid = ""
				+ (getStrGroupId() == null ? "0" : (getStrGroupId().length() == 0) ? "0" : "" + getStrGroupId());
		String strparkareaid = "" + (getStrPakingAreaId() == null ? "0"
				: (getStrPakingAreaId().length() == 0) ? "0" : "" + getStrPakingAreaId());
		List<RptDriverInShift> lstvalue = lstObj.getDriverInShift(stragentid, strgroupid, strparkareaid, timedatefrom,
				0, 0, "" + getUser());
		return lstvalue;
	}

	private void createChosenParking(Row row) {
		chosenParking = ComponentsReport.ChosenboxReportInput(ParkingArea.class);
		chosenParking.setWidth("160px");
		chosenParking.setParent(row);
		chosenParking.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Set<ParkingArea> lstObjChosen = chosenParking.getSelectedObjects();
				int temp = 0;
				for (ParkingArea reportQcParkingArea : lstObjChosen) {
					if (temp < lstObjChosen.size() - 1) {
						getStrPakingAreaId().append(reportQcParkingArea.getId()).append(",");
					} else {
						getStrPakingAreaId().append(reportQcParkingArea.getId());
					}
					temp++;
				}
			}
		});
	}
	
	private void updateDriverInShift(RptDriverInShift data){
		Window winUpdate = new Window();
		winUpdate.setParent(this);
		winUpdate.setTitle("Sửa thông tin");
		winUpdate.setClosable(true);
		winUpdate.setHeight("400px");
		winUpdate.setWidth("400px");
		winUpdate.doModal();
		
		Div div = new Div();
		div.setParent(winUpdate);
		div.setVflex("1");
		div.setHflex("1");
		
		Label lb = new Label("Tao Gid cho viec Update");
		lb.setParent(div);
		
	}

}
