package com.vietek.tracking.ui.statistic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.ParkingArea;
import com.vietek.taxioperation.model.RptDriverInShift;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.ui.util.ComponentsReport;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.DateUtils;
import com.vietek.taxioperation.util.Env;

public class WarningWorkingHour extends Window implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Datebox datebox;
	private Combobox cbxagent;
	private Combobox cbxgroup;
	private Combobox cbxparking;		
	private Intbox ibxranger;
	private Button btnsearch;
	private Button btnexcel;
	private Listbox griddata;
	
	public WarningWorkingHour() {
		this.setShadow(true);
		this.setVflex("1");
		this.setHflex("1");
		this.setClosable(true);

		Hlayout hlayout = new Hlayout();
		hlayout.setParent(this);
		hlayout.setVflex("1");
		hlayout.setHflex("1");
		
		this.initLeftPanel(hlayout);
		this.initRightPanel(hlayout);
	}
	
	public void initLeftPanel(Hlayout hlayout){
		Panel toppanel = new Panel();
		toppanel.setParent(hlayout);
		toppanel.setHeight("100%");
		toppanel.setHflex("2.5");
		toppanel.setStyle("color : black; font-size : 14px; font-weight : bold;");	
		toppanel.setTitle("ĐIỀU KIỆN TÌM KIẾM");
		
		Panelchildren child = new Panelchildren();
		child.setParent(toppanel);		
		
		Window window = new Window();
		window.setParent(child);
		window.setHflex("1");
		window.setVflex("1");
		
		Grid filter = new Grid();
		filter.setParent(window);
		filter.setStyle("border: none");
		window.setHflex("1");
		filter.setVflex("1");
		
		// columns
		Columns cols = new Columns();		
		cols.setParent(filter);
		// title
		Column col = new Column();			
		col.setParent(cols);
		col.setWidth("30%");
		// value
		col = new Column();
		col.setParent(cols);
		col.setWidth("70%");
				
		Rows rows = new Rows();
		rows.setParent(filter);

		Row row = new Row();
		row.setParent(rows);		
		Label label = new Label("Ngày");
		label.setStyle("font-weight : bold; font-size : 12px;");
		label.setParent(row);
		datebox = new Datebox();		
		datebox.setStyle("font-size : 14px;");
		datebox.setFormat("dd/MM/yyyy");
		datebox.setValue(DateUtils.addHour(new Date(), 0, 0));
		datebox.setConstraint("no empty: Không được để trống");
		datebox.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		label = new Label("Đơn vị");
		label.setStyle("font-weight: bold; font-size: 12px;");
		label.setParent(row);
		cbxagent = ComponentsReport.ComboboxReportInput(Agent.class);
		cbxagent.setSclass("comboboxtripsearching");
		cbxagent.setParent(row);
		cbxagent.setWidth("180px");
		
		row = new Row();
		row.setParent(rows);
		label = new Label("Đội xe");
		label.setStyle("font-weight: bold; font-size: 12px;");
		label.setParent(row);
		cbxgroup = ComponentsReport.ComboboxReportInput(TaxiGroup.class);
		cbxgroup.setParent(row);
		cbxgroup.setSclass("comboboxtripsearching");
		cbxgroup.setWidth("180px");
		
		row = new Row();
		row.setParent(rows);
		label = new Label("Bãi giao ca");
		label.setStyle("font-weight: bold; font-size: 12px;");
		label.setParent(row);		
		cbxparking = ComponentsReport.ComboboxReportInput(ParkingArea.class);
		cbxparking.setParent(row);
		cbxparking.setSclass("comboboxtripsearching");
		cbxparking.setWidth("180px");
		
		row = new Row();
		row.setParent(rows);
		label = new Label("Cảnh báo sớm");
		label.setStyle("font-weight: bold; font-size: 12px;");
		label.setParent(row);
		Div ctrl = new Div();
		ctrl.setParent(row);	
		ibxranger = new Intbox();
		ibxranger.setParent(ctrl);
		ibxranger.setStyle("float: left; font-weight: bold;");
		ibxranger.setWidth("100");
		label = new Label("phút");
		label.setParent(ctrl);
		label.setStyle("float: left; font-weight: bold; margin-left: 5px; margin-top: 5px;");
		
		row = new Row();
		row.setParent(rows);
		label = new Label("");
		label.setParent(row);
		ctrl = new Div();
		ctrl.setParent(row);	
		btnsearch = new Button("Thống kê");
		btnsearch.setStyle("color: black; font-weight: bold; float: left; margin-right: 5px;");		
		btnsearch.setParent(ctrl);		
		btnexcel = new Button("Excel");		
		btnexcel.setStyle("color: black; font-weight: bold; float: left; margin-right: 5px;");
		btnexcel.setParent(ctrl);	
		
		// add listener
		btnsearch.addEventListener(Events.ON_CLICK, this);
		btnexcel.addEventListener(Events.ON_CLICK, this);
		
		cbxagent.addEventListener(Events.ON_SELECT, this);
		cbxgroup.addEventListener(Events.ON_SELECT, this);
		
		ibxranger.addEventListener(Events.ON_OK, this);
	}
	
	public void initRightPanel(Hlayout hlayout){
		griddata = new Listbox();
		griddata.setParent(hlayout);
		griddata.setHflex("7.5");
		griddata.setVflex("1");
		griddata.setMold("paging");
		griddata.setAutopaging(true);
		griddata.setSclass("gridcus");
		
		Listhead cols = new Listhead();
		cols.setParent(griddata);
		cols.setSizable(true);
		
		// columns
		Listheader col = new Listheader("STT");
		col.setParent(cols);	
		col.setWidth("5%");
		
		// STT
		col = new Listheader("Ngày làm việc");			
		col.setParent(cols);
		col.setWidth("10%");
		//
		col = new Listheader("BKS");			
		col.setParent(cols);
		col.setWidth("5%");
		//
		col = new Listheader("Số tài");
		col.setParent(cols);		
		col.setWidth("5%");
		col = new Listheader("Tài xế");
		col.setParent(cols);		
		col.setWidth("15%");
		//
		col = new Listheader("MSNV");
		col.setParent(cols);		
		col.setWidth("5%");
		// 
		col = new Listheader("Điện thoại");
		col.setParent(cols);		
		col.setWidth("10%");
		// 
		col = new Listheader("Lái liên tục");
		col.setParent(cols);		
		col.setWidth("10%");
		// 
		col = new Listheader("Lái trong ngày");
		col.setParent(cols);		
		col.setWidth("10%");
		//
		col = new Listheader("Bãi giao ca");
		col.setParent(cols);		
		col.setWidth("15%");
		// 
		col = new Listheader("Đội xe");
		col.setParent(cols);		
		col.setWidth("15%");
		
		griddata.setItemRenderer(new ListitemRenderer<RptDriverInShift>(){

			@Override
			public void render(Listitem row, RptDriverInShift data, int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
				new Listcell("" + ++index).setParent(row);
				new Listcell("" + dateformat.format(data.getIssueDate())).setParent(row);
				new Listcell("" + data.getLicensePlate()).setParent(row);
				new Listcell("" + data.getVehicleNumber()).setParent(row);
				new Listcell("" + data.getDriverName()).setParent(row);
				new Listcell("" + data.getStaffCard()).setParent(row);			
				new Listcell("" + data.getPhoneNumber()).setParent(row);
				new Listcell("" + data.getDriverState()).setParent(row);
				new Listcell("" + data.getTimeDrivingContinuous()).setParent(row);
				new Listcell("" + data.getTimeDrivingPerDay()).setParent(row);
				new Listcell("" + data.getParkingName()).setParent(row);
				new Listcell("" + data.getGroupName()).setParent(row);
			}
		});
	}
	
	public void getWarningList() {
		try {
			if (cbxagent.getSelectedItem().getValue().equals("0")) {
				Env.getHomePage().showNotification("Chưa chọn đơn vị", Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
			if (ibxranger.getValue() == null || ibxranger.getValue() < 1) {
				Env.getHomePage().showNotification("Chưa chọn thời gian cảnh báo sớm", Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
			ListObjectDatabase listObjectDatabase = new ListObjectDatabase();
			int agentid = 0;
			try {
				Agent agent = cbxagent.getSelectedItem().getValue();	
				agentid = agent.getId();
			} catch (Exception e) {
				// TODO: handle exception
			}
			int groupid = 0;
			try {
				TaxiGroup group = cbxgroup.getSelectedItem().getValue();
				groupid = group.getId();
			} catch (Exception e) {
				// TODO: handle exception
			}
			int parkingid = 0;
			try {
				ParkingArea parking = cbxparking.getSelectedItem().getValue();
				parkingid = parking.getId();
			} catch (Exception e) {
				// TODO: handle exception
			}			
			
			Timestamp shifttime = new Timestamp(datebox.getValue().getTime());
			int ranger = ibxranger.getValue();
			
			List<RptDriverInShift> results = listObjectDatabase.getWarningWorkingHour(Integer.toString(agentid), Integer.toString(groupid), 
					Integer.toString(parkingid), shifttime, ranger, Env.getUser().getUser());
			
			if (results == null || results.isEmpty()) {
				griddata.setEmptyMessage("Không có dữ liệu");
			}else {
				griddata.setModel(new ListModelList<RptDriverInShift>(results));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_SELECT)) {
			if (event.getTarget().equals(cbxagent)) {
				Agent agent = cbxagent.getSelectedItem().getValue();
				cbxgroup = (Combobox) ComponentsReport.reloadChosenboxGroup(cbxgroup, "" + agent.getId());
			}
			if (event.getTarget().equals(cbxgroup)) {
				TaxiGroup taxigroup = cbxgroup.getSelectedItem().getValue();				
				cbxparking = (Combobox) ComponentsReport.reloadChosenboxParking(cbxparking, "" + taxigroup.getId());
			}
		}
		if(event.getName().equals(Events.ON_CLICK)){
			if(event.getTarget().equals(btnsearch)){
				this.getWarningList();
			}else if (event.getTarget().equals(btnexcel)) {
				CommonUtils.exportListboxToExcel(griddata, "canh_bao_gio_lam_viec.xlsx");
			}
		}else if (event.getName().equals(Events.ON_OK)) {
			if(event.getTarget().equals(ibxranger)){
				this.getWarningList();
			}
		}
	}

}
