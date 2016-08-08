package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.controller.AgentController;
import com.vietek.taxioperation.controller.ArrangementTaxiController;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.ArrangementTaxi;
import com.vietek.taxioperation.model.TripInsideArrangement;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.tracking.ui.utility.TrackingHistory;

public class ListOrderFromToArrangement extends Div implements Serializable,EventListener<Event>{
	
	/**
	 * @author Batt
	 */
	private static final long serialVersionUID = 1L;
	private Combobox cbLstAgent;
	private Combobox cbLstArrangement;
	private Combobox cbStatus;
	private int status = 1;
	private ArrangementTaxi selectedArrangement;
	private Agent selectedAgent;
	private Datebox dbFromTime;
	private Datebox dbToTime;
	private Button btnSearch;
	private Button btnExportExcel;
	private ArrangementTaxiController argmentController;
	private AgentController agentController;
	private List<Agent> lstAgent = new ArrayList<Agent>();
	private List<ArrangementTaxi> lstArrangement = new ArrayList<ArrangementTaxi>();
	private Listbox lstBox;
	private List<TripInsideArrangement> lstModel;
	private Textbox txtVehicleNumber;
	private Textbox txtLicensePlate;
	private Textbox txtBeginTime;
	private Textbox txtBeginPosition;
	private Textbox txtStopTime;
	private Textbox txtstopPosition;
	private Textbox txtMoney;
	private Textbox txtPath;
	
	public ListOrderFromToArrangement(){
		this.setHeight("100%");
		Div div = new Div();
		div.setParent(this);
		div.setStyle("margin-top:15px;");
		getLstAgent();
		initUI(div);
	}
	private void initUI(Component parent){
		// list sys company
		Div div1 = new Div();
		div1.setParent(parent);
		div1.setClass("form-marginRight-20");
		cbLstAgent = new Combobox();
		initUIBranch(cbLstAgent,div1,lstAgent);
		
		// list arrangement taxi by company
		cbLstArrangement = new Combobox();
		cbLstArrangement.setClass("form-marginRight-20");
		initUIArrangement(cbLstArrangement, div1);
		
		// status search
		cbStatus = new Combobox();
		cbStatus.setClass("form-marginRight-20");
		initUISelectSearch(cbStatus,div1);
		
		// from time
		initUIFromTime(div1);
		// to time
		initUIToTime(div1);
		
		// button search
		btnSearch = new Button("Tìm kiếm");
		btnSearch.addEventListener(Events.ON_CLICK, this);
		btnSearch.setClass("form-marginRight-20");
		btnSearch.setParent(div1);
		
		// btutton export excel
		btnExportExcel = new Button("Xuất Excel");
		btnExportExcel.setClass("form-marginRight-20");
		btnExportExcel.addEventListener(Events.ON_CLICK, this);
		btnExportExcel.setParent(div1);
		
		// grid data
		Div div2 = new Div();
		div2.setParent(parent);
		div2.setStyle("width:100%; height:100%");
		div2.setStyle("margin:10px");
		initGridData(div2);
	}
	
	private void initGridData(Component parent){
		Panel panel = new Panel();
		panel.setSclass("z-history-panel panel panel-noborder panel-noheader panel-noframe");
		panel.setParent(parent);
		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);
		Toolbar toolbar = new Toolbar();
		toolbar.appendChild(new Label("Danh sách quốc khách"));
		toolbar.setSclass("z-toolbar-history");
		toolbar.setAlign("center");
		toolbar.setParent(panelchildren);
		lstBox = new Listbox();
		lstBox.setEmptyMessage("Không có dữ liệu");
		lstBox.addEventListener(Events.ON_SELECT, this);
		lstBox.setSizedByContent(true);
		lstBox.setSpan(true);
		lstBox.setMold("paging");
		lstBox.setAutopaging(true);
		lstBox.setPagingPosition("both");
		lstBox.addEventListener("onPaging", this);
		lstBox.setHeight("420px");
		lstBox.setParent(panelchildren);
		Listhead head = new Listhead();
		head.setParent(lstBox);
		head.setSizable(true);
		Listheader header = new Listheader("TT");
		header.setWidth("50px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Số tài");
		header.setWidth("70px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("BKS");
		header.setWidth("100px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Giờ đón");
		header.setWidth("140px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Điểm đón");
		header.setWidth("300px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Giờ trả");
		header.setWidth("140px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Điểm trả");
		header.setWidth("300px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Tiền cuốc");
		header.setWidth("120px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Quãng đường (km)");
		header.setWidth("120px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Xem lịch sử");
		header.setWidth("100px");
		header.setAlign("center");
		head.appendChild(header);
		
		Auxhead auxs = new Auxhead();
		auxs.setParent(lstBox);
		Auxheader aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		txtVehicleNumber = new Textbox();
		txtVehicleNumber.setSclass("z-textbox-search");
		txtVehicleNumber.setAttribute("datafield", "");
		txtVehicleNumber.setPlaceholder("Số tài");
		txtVehicleNumber.setParent(aux);
		txtVehicleNumber.addEventListener(Events.ON_OK, this);
		txtVehicleNumber.addForward(Events.ON_CHANGE, txtVehicleNumber, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		txtLicensePlate = new Textbox();
		txtLicensePlate.setSclass("z-textbox-search");
		txtLicensePlate.setAttribute("datafield", "");
		txtLicensePlate.setPlaceholder("BKS");
		txtLicensePlate.setParent(aux);
		txtLicensePlate.addEventListener(Events.ON_OK, this);
		txtLicensePlate.addForward(Events.ON_CHANGE, txtLicensePlate, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		txtBeginTime = new Textbox();
		txtBeginTime.setSclass("z-textbox-search");
		txtBeginTime.setAttribute("datafield", "");
		txtBeginTime.setPlaceholder("Giờ đi");
		txtBeginTime.setParent(aux);
		txtBeginTime.addEventListener(Events.ON_OK, this);
		txtBeginTime.addForward(Events.ON_CHANGE, txtBeginTime, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		txtBeginPosition = new Textbox();
		txtBeginPosition.setSclass("z-textbox-search");
		txtBeginPosition.setAttribute("datafield", "");
		txtBeginPosition.setPlaceholder("Điểm đón");
		txtBeginPosition.setParent(aux);
		txtBeginPosition.addEventListener(Events.ON_OK, this);
		txtBeginPosition.addForward(Events.ON_CHANGE, txtBeginPosition, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		txtStopTime = new Textbox();
		txtStopTime.setSclass("z-textbox-search");
		txtStopTime.setAttribute("datafield", "");
		txtStopTime.setPlaceholder("Giờ trả");
		txtStopTime.setParent(aux);
		txtStopTime.addEventListener(Events.ON_OK, this);
		txtStopTime.addForward(Events.ON_CHANGE, txtStopTime, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		txtstopPosition = new Textbox();
		txtstopPosition.setSclass("z-textbox-search");
		txtstopPosition.setAttribute("datafield", "");
		txtstopPosition.setPlaceholder("Điểm trả");
		txtstopPosition.setParent(aux);
		txtstopPosition.addEventListener(Events.ON_OK, this);
		txtstopPosition.addForward(Events.ON_CHANGE, txtstopPosition, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		txtMoney = new Textbox();
		txtMoney.setSclass("z-textbox-search");
		txtMoney.setAttribute("datafield", "");
		txtMoney.setPlaceholder("Tiền cuốc");
		txtMoney.setParent(aux);
		txtMoney.addEventListener(Events.ON_OK, this);
		txtMoney.addForward(Events.ON_CHANGE, txtMoney, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		txtPath = new Textbox();
		txtPath.setSclass("z-textbox-search");
		txtPath.setAttribute("datafield", "");
		txtPath.setPlaceholder("Quãng đường");
		txtPath.setParent(aux);
		txtPath.addEventListener(Events.ON_OK, this);
		txtPath.addForward(Events.ON_CHANGE, txtPath, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		auxs.appendChild(aux);
		
		lstBox.setItemRenderer(new ListitemRenderer<TripInsideArrangement>() {
			
			@Override
			public void render(Listitem items, TripInsideArrangement data, int index) throws Exception {
				items.setValue(data);
				Label lb = new Label();
				lb.setValue(index + 1 + "");
				Listcell listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getVehicleNumber());
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getLicensePlate());
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getBeginTime()+"");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getBeginPosition());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getStopTime()+"");
				listcell = new Listcell();
				listcell.setStyle("text-align: center");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getStopPosition());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getMoney());
				listcell = new Listcell();
				listcell.setStyle("text-align: right");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getPath()+"");
				listcell = new Listcell();
				listcell.setStyle("text-align: right");
				listcell.appendChild(lb);
				listcell.setParent(items);
				Toolbarbutton btnViewHistory = new Toolbarbutton();
				btnViewHistory.setImage("./themes/images/searchadd.png");
				btnViewHistory.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						 TripInsideArrangement trip = lstBox.getSelectedItem().getValue();
						 showHistoryTrip(trip);
					}
				});
				listcell = new Listcell();
				listcell.appendChild(btnViewHistory);
				listcell.setParent(items);
			}
		});
		
	}
	
	private void showHistoryTrip(TripInsideArrangement trip){
		 Date fromTime = new Date(trip.getBeginTime().getTime());
		 Date toTime = new Date(trip.getStopTime().getTime());
		 int id = Integer.parseInt(trip.getVehicleId());
		 TrackingHistory hist = new TrackingHistory(fromTime, toTime, id);
		 Window window = new Window();
		 window.setWidth("100%");
		 window.setHeight("100%");
		 window.setParent(this);
		 hist.setParent(window);
		 window.doModal();	
	}
	
	private void initUIFromTime(Component parent){
		Label lb = new Label();
		lb.setValue("Từ ngày: ");
		lb.setClass("form-marginRight-20");
		lb.setParent(parent);
		dbFromTime = new Datebox();
		dbFromTime.setHeight("30px");
		dbFromTime.setClass("form-marginRight-20");
		dbFromTime.setWidth("150px");
		dbFromTime.setFormat("dd/MM/yyyy HH:mm");
		dbFromTime.setValue(new Date(System.currentTimeMillis() - (24l *60l * 60l * 1000)));
		dbFromTime.setParent(parent);
		dbFromTime.addEventListener(Events.ON_CHANGE, this);
		
		
	}
	
	private void initUIToTime(Component parent){
		Label lb = new Label();
		lb.setValue("Đến ngày: ");
		lb.setClass("form-marginRight-20");
		lb.setParent(parent);
		dbToTime = new Datebox();
		dbToTime.setHeight("30px");
		dbToTime.setClass("form-marginRight-20");
		dbToTime.setWidth("150px");
		dbToTime.setFormat("dd/MM/yyyy HH:mm");
		dbToTime.setValue(new Date(System.currentTimeMillis()));
		dbToTime.setParent(parent);
		dbToTime.addEventListener(Events.ON_CHANGE, this);
	}
	
	private void initUIBranch(Combobox cb, Component parent, List<Agent> lst){
		cb.getChildren().clear();
		cb.setParent(parent);
		cb.setAutocomplete(true);
		cb.setAutodrop(true);
		cb.setWidth("220px");
		cb.setPlaceholder("-- Chi nhánh --");
		cb.addEventListener(Events.ON_CHANGE, this);
		if(lst.size() > 0){
			for (Agent agent : lst) {
				Comboitem item = new Comboitem();
				item.setAttribute("agent", agent);
				item.setLabel(agent.getAgentName());
				cb.appendChild(item);
			}
		}
	}
	
	private void initUIArrangement(Combobox cb, Component parent){
		cb.setParent(parent);
		cb.setAutocomplete(true);
		cb.setAutodrop(true);
		cb.setWidth("220px");
		cb.setPlaceholder("-- Chọn điểm --");
		cb.addEventListener(Events.ON_CHANGE, this);
	}
	
	private void initUISelectSearch(Combobox cb, Component parent){
		List<BeanSelect> lst = new ArrayList<BeanSelect>();
		BeanSelect bean1 = new BeanSelect("-- Đi từ điểm này --", 1);
		BeanSelect bean2 = new BeanSelect("-- Đến điểm này --", 2);
		lst.add(bean1);
		lst.add(bean2);
		cb.setParent(parent);
		cb.setAutocomplete(true);
		cb.setAutodrop(true);
		cb.setWidth("200px");
		cb.setItemRenderer(new ComboitemRenderer<BeanSelect>() {
			@Override
			public void render(Comboitem paramComboitem, BeanSelect bean,int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				if(bean.getValue() == 1){
					cb.setSelectedItem(paramComboitem);
				}
				
			}
		});
		cb.setModel(new ListModelList<BeanSelect>(lst));
		cb.setWidth("150px");
		cb.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				status = (int)cb.getSelectedItem().getValue();
			}
		});
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		if(event.getName().equals(Events.ON_CLICK)){
			if(event.getTarget().equals(btnSearch)){ // query data
				Timestamp fromTime = new Timestamp(dbFromTime.getValue().getTime());
				Timestamp toTime = new Timestamp(dbToTime.getValue().getTime());
				if(selectedArrangement != null){
					lstModel = getTripsByArrangement(selectedArrangement.getId(), fromTime, toTime, status);
					renderListBox(lstModel);
				}else{
					Env.getHomePage().showNotification("Chưa chọn điểm tiếp thị !", Clients.NOTIFICATION_TYPE_WARNING);
				}
			}else if(event.getTarget().equals(btnExportExcel)){ // export excel
				if(lstBox != null && lstBox.getVisibleItemCount() > 0){
					CommonUtils.exportListboxToExcel(lstBox, "danh sach cuoc");
				}
			}
		}else if(event.getName().equals(Events.ON_CHANGE)){
			if(event.getTarget().equals(cbLstAgent)){
				if(cbLstAgent.getSelectedItem() != null){
					Comboitem item = cbLstAgent.getSelectedItem();
					selectedAgent = (Agent) item.getAttribute("agent");
					@SuppressWarnings("unchecked")
					List<ArrangementTaxi> lst = (List<ArrangementTaxi>)item.getAttribute("arrangements");
					if(lst == null || lst.size() == 0){
						lst = getLstByAgent(selectedAgent);
					}
					item.setAttribute("arrangements", lst);
					renderCbArrangement(lst);
				}
			}else if(event.getTarget().equals(cbLstArrangement)){
				if(cbLstArrangement.getSelectedItem() != null){
					selectedArrangement = (ArrangementTaxi)cbLstArrangement.getSelectedItem().getValue();
				}else{
					selectedArrangement = null;
				}
			}else if(event.getTarget().equals(dbFromTime)){
				if(dbFromTime.getValue() == null ){
					dbFromTime.setValue(new Date(System.currentTimeMillis() - (24 * 1000 * 60 * 60)));
				}else if(dbFromTime.getValue().getTime() > dbToTime.getValue().getTime()){
					dbToTime.setValue(new Date(System.currentTimeMillis()));
				}
			}else if(event.getTarget().equals(dbToTime)){
				if(dbFromTime.getValue() == null ){
					dbFromTime.setValue(new Date(System.currentTimeMillis()));
				}else if(dbFromTime.getValue().getTime() > dbToTime.getValue().getTime()){
					dbToTime.setValue(new Date(System.currentTimeMillis()));
				}
			}
		}
		
		if(event.getTarget().equals(txtVehicleNumber)){
			txtVehicleNumber.setAttribute("datafield", txtVehicleNumber.getValue());
			executeSearch();
		}else if(event.getTarget().equals(txtLicensePlate)){
			txtLicensePlate.setAttribute("datafield", txtLicensePlate.getValue());
			executeSearch();
		}else if(event.getTarget().equals(txtBeginTime)){
			txtBeginTime.setAttribute("datafield", txtBeginTime.getValue());
			executeSearch();
		}else if(event.getTarget().equals(txtBeginPosition)){
			txtBeginPosition.setAttribute("datafield", txtBeginPosition.getValue());
			executeSearch();
		}else if(event.getTarget().equals(txtStopTime)){
			txtStopTime.setAttribute("datafield", txtStopTime.getValue());
			executeSearch();
		}else if(event.getTarget().equals(txtstopPosition)){
			txtstopPosition.setAttribute("datafield", txtstopPosition.getValue());
			executeSearch();
		}else if(event.getTarget().equals(txtMoney)){
			txtMoney.setAttribute("datafield", txtMoney.getValue());
			executeSearch();
		}else if(event.getTarget().equals(txtPath)){
			txtPath.setAttribute("datafield", txtPath.getValue());
			executeSearch();
		}
	}
	
	private void executeSearch() {
		Predicate pre = new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				TripInsideArrangement mar = (TripInsideArrangement) arg0;
				String vehicleNumberValue = (String) txtVehicleNumber.getAttribute("datafield");
				String licensePlateValue = (String) txtLicensePlate.getAttribute("datafield");
				String beginTimeValue = (String) txtBeginTime.getAttribute("datafield");
				String beginPositionValue = (String) txtBeginPosition.getAttribute("datafield");
				String stopTimeValue = (String) txtStopTime.getAttribute("datafield");
				String stopPositionValue = (String) txtstopPosition.getAttribute("datafield");
				String moneyValue = (String) txtMoney.getAttribute("datafield");
				String pathValue = (String) txtPath.getAttribute("datafield");
				// vehicle number
				boolean check1 = true;
				if((mar.getVehicleNumber() == null && !vehicleNumberValue.equals("")) 
						|| (mar.getVehicleNumber() != null && !mar.getVehicleNumber().toUpperCase().contains(vehicleNumberValue.toUpperCase()))){
					check1 = false;
				}
				// license plate
				boolean check2 = true;
				if((mar.getLicensePlate() == null && !licensePlateValue.equals("")) 
						|| (mar.getLicensePlate() != null && !mar.getLicensePlate().toUpperCase().contains(licensePlateValue.toUpperCase()))){
					check2 = false;
				}
				// begin time
				boolean check3 = true;
				if((mar.getBeginTime() == null && !beginTimeValue.equals("")) 
						|| (mar.getBeginTime() != null && !mar.getBeginTime().toString().toUpperCase().contains(beginTimeValue.toUpperCase()))){
					check3 = false;
				}
				// begin position
				boolean check4 = true;
				if((mar.getBeginPosition() == null && !beginPositionValue.equals("")) 
						|| (mar.getBeginPosition() != null && !mar.getBeginPosition().toUpperCase().contains(beginPositionValue.toUpperCase()))){
					check4 = false;
				}
				// stop time
				boolean check5 = true;
				if((mar.getStopTime() == null && !stopTimeValue.equals("")) 
						|| (mar.getStopTime() != null && !mar.getStopTime().toString().toUpperCase().contains(stopTimeValue.toUpperCase()))){
					check5 = false;
				}
				// stop position
				boolean check6 = true;
				if((mar.getStopPosition() == null && !stopPositionValue.equals("")) 
						|| (mar.getStopPosition() != null && !mar.getStopPosition().toUpperCase().contains(stopPositionValue.toUpperCase()))){
					check6 = false;
				}
				// money
				boolean check7 = true;
				if((mar.getMoney() == null && !moneyValue.equals("")) 
						|| (mar.getMoney() != null && !mar.getMoney().replace(",", "").toUpperCase().contains(moneyValue.toUpperCase()))){
					check7 = false;
				}
				// path
				boolean check8 = true;
				if((mar.getPath() == null && !pathValue.equals("")) 
						|| (mar.getPath() != null && !mar.getPath().toString().toUpperCase().contains(pathValue.toUpperCase()))){
					check8 = false;
				}
				if (check1 && check2 && check3 && check4 && check5 && check6 && check7 && check8) {
					return true;
				}

				return false;
			}
		};
		@SuppressWarnings("unchecked")
		Collection<TripInsideArrangement> coll = CollectionUtils.select(lstModel, pre);
		List<TripInsideArrangement> lsttmp = new ArrayList<TripInsideArrangement>(coll);
		renderListBox(lsttmp);

	}
	
	private void renderListBox(List<TripInsideArrangement> lstModel){
		lstBox.setModel(new ListModelList<TripInsideArrangement>(lstModel));
	}
	
	
	private void renderCbArrangement(List<ArrangementTaxi> lst){
		if(cbLstArrangement.getSelectedItem() != null){
			cbLstArrangement.setSelectedItem(null);
		}
		cbLstArrangement.getChildren().clear();
		cbLstArrangement.setText("");
		if( lst != null && lst.size() > 0){
			for (ArrangementTaxi arrangementTaxi : lst) {
				Comboitem temp = new Comboitem();
				temp.setValue(arrangementTaxi);
				temp.setLabel(arrangementTaxi.getName());
				temp.setAttribute("ArrangementTaxi", arrangementTaxi);
				cbLstArrangement.appendChild(temp);
			}
		}
	}
	
	class BeanSelect{
		private String title;
		private int value;
		
		public BeanSelect(String title, int value){
			this.title = title;
			this.value = value;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
	
	private List<TripInsideArrangement> getTripsByArrangement(int id, Timestamp fromTime, Timestamp toTime, int type){
		List<TripInsideArrangement> lst = new ArrayList<TripInsideArrangement>();
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor)session;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sessionImplementor.connection();
			if(conn == null){
				return null;
			}
			cs = conn.prepareCall("call txm_tracking.cmdGetTripInsidePavilionTMS(?,?,?,?)");
			cs.setInt(1, id);
			cs.setInt(2, type);
			cs.setTimestamp(3, fromTime);
			cs.setTimestamp(4, toTime);
			rs = cs.executeQuery();
			while(rs.next()){
				TripInsideArrangement trip = new TripInsideArrangement();
				trip.setVehicleId(rs.getString("VehicleID"));
				trip.setVehicleNumber(rs.getString("VehicleNumber"));
				trip.setLicensePlate(rs.getString("LicensePlate"));
				trip.setBeginPosition(rs.getString("BeginPosition"));
				trip.setBeginTime(rs.getTimestamp("BeginTime"));
				trip.setStopPosition(rs.getString("StopPosition"));
				trip.setStopTime(rs.getTimestamp("StopTime"));
				int money = rs.getInt("Money");
				String moneyF = NumberFormat.getIntegerInstance().format(money);
				trip.setMoney(moneyF);
				trip.setPath(rs.getDouble("Path"));
				lst.add(trip);
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.close();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return lst;
	}
	
	private List<Agent> getLstAgent(){
		if(agentController == null){
			agentController = (AgentController) ControllerUtils.getController(AgentController.class);
		}
		lstAgent = agentController.find("from Agent");
		return lstAgent;
	} 
	
	private List<ArrangementTaxi> getLstByAgent(Agent agent){
		if(argmentController == null){
			argmentController = (ArrangementTaxiController)ControllerUtils.getController(ArrangementTaxiController.class);
		}
		lstArrangement  = argmentController.find("from ArrangementTaxi where agent = ?", agent);
		return lstArrangement;
	}
}
