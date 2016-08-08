package com.vietek.taxioperation.ui.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Frozen;
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
import org.zkoss.zul.West;

import com.vietek.taxioperation.controller.VehicleController;
import com.vietek.taxioperation.model.ParkingArea;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.util.ComboboxRender;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class FuelParams extends Div implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vehicle currentModel;
	private Listbox listboxvehicle;
	private Textbox searchlicenseplace;
	private Textbox searchgroup;
	private Textbox searchvehicletype;
	private List<Vehicle> listmodel;
	private Button btnUploadFile;
	private Button btnLoad;
	private Combobox cmbsheet;
	private Textbox filename;
	private Button btnNap;
	private Media medias;
	private Listbox listboxfuel;
	private Toolbarbutton btnDownload;
	private InputStream datastream;
	private Workbook workbox;

	public FuelParams() {
		initUI();
	}

	private void initUI() {
		this.setHeight("100%");
		Borderlayout borderlayout = new Borderlayout();
		borderlayout.setParent(this);
		West west = new West();
		west.setSize("600px");
		west.setVflex("true");
		west.setParent(borderlayout);

		Center center = new Center();
		center.setParent(borderlayout);
		creatFrmLeft(west);
		creatFrmRight(center);
		loadData();

	}

	private void loadData() {
		VehicleController controler = (VehicleController) ControllerUtils.getController(VehicleController.class);
		listmodel = controler.find("From Vehicle");
        listboxvehicle.setModel(new ListModelList<>(listmodel));
	}

	private void creatFrmRight(Center center) {
       Div div = new Div();
//       div.setStyle("");
       div.setParent(center);
       Div toolbar = new Div();
       toolbar.setParent(div);
       toolbar.setStyle("background-color:rgb(207, 207, 207);border-radius: 9px;");
       btnDownload = new Toolbarbutton("Tải file mẫu");
       btnDownload.setParent(toolbar);
       btnDownload.setStyle("font-weight: bold;text-decoration: underline;");
       btnDownload.addEventListener(Events.ON_CLICK,downloadevent);
       btnUploadFile = new Button("Chọn File");
       btnUploadFile.setStyle("width: 74px; margin-top:5px; margin-bottom:5px;height:25px;margin-left:10px;margin-right:10px;font-weight: bold;color: green;");
       btnUploadFile.setUpload("true,maxsize=300");
       btnUploadFile.setParent(toolbar);
       btnUploadFile.addEventListener(Events.ON_UPLOAD,UploadEvent);
       filename = new Textbox();
       filename.setPlaceholder("Tên File");
       filename.setParent(toolbar);
       filename.setStyle("");
       cmbsheet = new Combobox();
       cmbsheet.setParent(toolbar);
//       cmbsheet.setStyle("height:25px!important;font-size: 14px !important;");
       cmbsheet.setSclass("z-combobox-search");
       cmbsheet.setStyle("width:120px !important; margin-left : 10px");
       cmbsheet.setPlaceholder("Chọn Sheet");
//       cmbsheet.setButtonVisible(false);
       
       Div divcontrol = new Div();
       divcontrol.setSclass("z-div-control");
       divcontrol.setStyle("float:right");
       divcontrol.setParent(toolbar);
       btnLoad = new Button("Load");
       btnLoad.setParent(divcontrol);
       btnLoad.setStyle("width:150px;height:30px;margin-bottom: 2px;margin-top: 2px;margin-right: 10px;");
       listboxfuel = new Listbox();
       listboxfuel.setVflex("true");
       listboxfuel.setParent(div);
       Listhead head = new Listhead();
		head.setParent(listboxfuel);
		head.setSizable(true);
		Listheader header = new Listheader("TT");
		header.setWidth("40px");
		head.appendChild(header);
		header = new Listheader("Fuel ID");
		header.setWidth("80px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Fuel From");
		header.setWidth("100px");
		// header.setStyle("min-width: 150px;");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Fuel To");
		header.setWidth("100px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Pulse From");
		header.setWidth("100px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Pulse To");
		header.setWidth("100px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Pulse Mean");
		header.setVflex("1");
		header.setAlign("center");
		head.appendChild(header);
	}

	private void creatFrmLeft(West west) {
		Div div = new Div();
		div.setStyle("width:100%; height:100%");
		div.setParent(west);
		Panel panel = new Panel();
		panel.setSclass("z-history-panel panel panel-noborder panel-noheader panel-noframe");
		panel.setParent(div);
		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);
		Toolbar toolbar = new Toolbar();
		toolbar.appendChild(new Label("Danh sách Xe"));
		toolbar.setSclass("z-toolbar-history");
		toolbar.setAlign("center");
		toolbar.setParent(panelchildren);
		listboxvehicle = new Listbox();
		listboxvehicle.setEmptyMessage("Không có dữ liệu");
		listboxvehicle.addEventListener(Events.ON_SELECT, this);
		listboxvehicle.setSizedByContent(true);
		listboxvehicle.setSpan(true);
		listboxvehicle.setMold("paging");
		listboxvehicle.setAutopaging(true);
		listboxvehicle.setPagingPosition("both");
		listboxvehicle.addEventListener("onPaging", this);
		listboxvehicle.setHeight("450px");
		listboxvehicle.setParent(panelchildren);
		listboxvehicle.setMultiple(true);
		listboxvehicle.setCheckmark(true);
		Frozen frozen = new Frozen();
		frozen.setStart(0);
		frozen.setColumns(2);
		frozen.setParent(listboxvehicle);
		Listhead head = new Listhead();
		head.setParent(listboxvehicle);
		head.setSizable(true);
		Listheader header = new Listheader("TT");
		head.appendChild(header);
		header = new Listheader("Biển kiểm soát");
		header.setWidth("80px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Đội xe");
		// header.setStyle("min-width: 150px;");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Loại xe");
		header.setAlign("center");
		head.appendChild(header);

		Auxhead auxs = new Auxhead();
		auxs.setParent(listboxvehicle);
		Auxheader aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		searchlicenseplace = new Textbox();
		searchlicenseplace.setSclass("z-textbox-search");
		searchlicenseplace.setId("value");
		searchlicenseplace.setAttribute("datafield", "");
		searchlicenseplace.setPlaceholder("Biển số");
		searchlicenseplace.setParent(aux);
		searchlicenseplace.addEventListener(Events.ON_OK, this);
		searchlicenseplace.addForward(Events.ON_CHANGE, searchlicenseplace, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		searchgroup = new Textbox();
		searchgroup.setAttribute("datafield", "");
		searchgroup.setId("Address");
		searchgroup.setSclass("z-textbox-search");
		searchgroup.setPlaceholder("Đội xe");
		searchgroup.setParent(aux);
		searchgroup.addEventListener(Events.ON_OK, this);
		searchgroup.addForward(Events.ON_CHANGE, searchgroup, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		searchvehicletype = new Textbox();
		searchvehicletype.setAttribute("datafield", "");
		searchvehicletype.setId("taxigroup");
		searchvehicletype.setSclass("z-textbox-search");
		searchvehicletype.setPlaceholder("Loại xe");
		searchvehicletype.setParent(aux);
		searchvehicletype.addEventListener(Events.ON_OK, this);
		searchvehicletype.addForward(Events.ON_CHANGE, searchvehicletype, Events.ON_OK);
		auxs.appendChild(aux);

		listboxvehicle.setItemRenderer(new ListitemRenderer<Vehicle>() {

			@Override
			public void render(Listitem items, Vehicle data, int index) throws Exception {
				items.setValue(data);
				Label lb = new Label();
				lb.setValue("");
				Listcell listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getTaxiNumber());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getTaxiGroup().getName());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getTaxiType().getName());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
			}
		});
		Toolbar toolb = new Toolbar();
		toolb.setVflex("true");
		toolb.setAlign("center");
		toolb.setParent(div);
		btnNap = new Button("NẠP HỆ SỐ NHIÊN LIỆU");
		btnNap.setParent(toolb);
		btnNap.setSclass("z-btn-new-parking");
		btnNap.addEventListener(Events.ON_CLICK, this);
		btnNap.setStyle("width: 85%;border-radius: 15px;background-color: #1BA339;font-weight: bold;color: white;");
	}
	
	@SuppressWarnings("unchecked")
	private void executeSearch() {
		Predicate pre = new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
				Vehicle vehicle = (Vehicle) arg0;
				String licenseplace = (String) searchlicenseplace.getAttribute("datafield");
				String group = (String) searchgroup
						.getAttribute("datafield");
				String type = (String) searchvehicletype
						.getAttribute("datafield");
				if (vehicle.getTaxiNumber().toUpperCase().contains(licenseplace.toUpperCase())
						&& vehicle.getTaxiGroup().getName().toUpperCase()
								.contains(group.toUpperCase())
						&& vehicle.getTaxiType() != null
						&& vehicle.getTaxiType().getName().toUpperCase()
								.contains(type.toUpperCase())) {
					return true;
				}
				return false;
			}
		};
		Collection<ParkingArea> coll = CollectionUtils.select(listmodel, pre);
		List<ParkingArea> lsttmp = new ArrayList<ParkingArea>(coll);
		listboxvehicle.setModel(new ListModelList<>(lsttmp));

	}

	@Override
	public void onEvent(Event arg0) throws Exception {
	}
  private EventListener<Event> UploadEvent = new EventListener<Event>() {

	@Override
	public void onEvent(Event events) throws Exception {
		UploadEvent event = (UploadEvent)events;
		if (event.getMedia() != null) {
			Media media = event.getMedia();
			if (!media.getFormat().contains("xls")) {
				Env.getHomePage().showNotification("File Không đúng định dạng !", Clients.NOTIFICATION_TYPE_WARNING);
			}else {
				filename.setText(media.getName());
			datastream = media.getStreamData();
			workbox = WorkbookFactory.create(datastream);
			  List<ComboboxRender> lstComboboxs = new ArrayList<ComboboxRender>();
	            for (int i = 0; i < workbox.getNumberOfSheets(); i++) {
					ComboboxRender bean = new ComboboxRender();
					bean.setTitle(workbox.getSheetName(i));
					bean.setValue(i);
					lstComboboxs.add(bean);
				}
	            cmbsheet.setModel(new ListModelList<ComboboxRender>(lstComboboxs));    		    	
	            datastream.close();
			}
			
		}
		
		
	}
	  
};

private EventListener<Event> downloadevent = new EventListener<Event>() {

	@Override
	public void onEvent(Event arg0) throws Exception {
		Filedownload.save("./FileServer/bangxungnhienlieu.xls",null);
	}
	
};
}
