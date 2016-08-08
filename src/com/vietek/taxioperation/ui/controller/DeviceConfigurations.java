package com.vietek.taxioperation.ui.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.FunctionCommon;
import com.vietek.taxioperation.controller.DeviceConfigController;
import com.vietek.taxioperation.controller.DeviceController;
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.Device;
import com.vietek.taxioperation.model.DeviceConfig;
import com.vietek.taxioperation.model.DeviceConfiguration;
import com.vietek.taxioperation.model.DriverInVehicle;
import com.vietek.taxioperation.ui.util.ComboboxRender;
import com.vietek.taxioperation.util.ControllerUtils;

public class DeviceConfigurations extends Window implements EventListener<Event>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Listbox listbox;
	private Tree configtree;
	private Button btnedit;
	private Button btnreset;
	ArrayList<Textbox> txtList;
	List<DeviceConfiguration> lstvalue;
	
	public DeviceConfigurations() {
		this.setTitle("Cấu hình thiết bị");
		this.setHflex("100%");
		this.setVflex("100%");
		this.init();
		this.loadData();
	}
	
	public void init(){	
		Hlayout hlayout = new Hlayout(); 
		hlayout.setParent(this);
		hlayout.setHflex("1");
		hlayout.setVflex("1");
		txtList = new ArrayList<Textbox>();
		
		this.initConfigViewer(hlayout);
		this.initContent(hlayout);
	}
	
	public void initConfigViewer(Hlayout hlayout){
		Vlayout vlayout = new Vlayout();
		vlayout.setParent(hlayout);
		vlayout.setVflex("1");
		vlayout.setHflex("3");
		vlayout.setStyle("margin-bottom: 0px;");
		
		// thong tin cau hinh cua moi thiet bi khi chon		
		configtree = new Tree();
		configtree.setParent(vlayout);
		configtree.setVflex("1");
		configtree.setHflex("1");
		configtree.setSclass("tree-config");
		Treecols treecols = new Treecols();
		treecols.setParent(configtree);
		treecols.setSizable(true);
		//
		Treecol treecol = new Treecol();
		treecol.setParent(treecols);
		treecol.setHflex("6");
		treecol.setLabel("Tên cấu hình");
		//	
		treecol = new Treecol();
		treecol.setParent(treecols);
		treecol.setHflex("4");
		treecol.setLabel("Giá trị");
		
		configtree.setItemRenderer(new TreeitemRenderer<ConfigTreeNode<DeviceConfig>>() {
			@Override
			public void render(Treeitem treeitem, ConfigTreeNode<DeviceConfig> node, int index) throws Exception {
				// TODO Auto-generated method stub
				Treerow treerow = new Treerow();
				treerow.setParent(treeitem);
				DeviceConfig deviceconfig = (DeviceConfig) node.getDeviceconfig();
				if (deviceconfig.getCanedit() == 0) {
					treerow.setSclass("z-treerow-disabled");
				}
				Treecell treecell = new Treecell();
				treecell.setParent(treerow);
				treecell.appendChild(new Label(deviceconfig.getConfigname()));
				//			
				treecell = new Treecell();
				treecell.setParent(treerow);
				treecell.appendChild(new Label(deviceconfig.getConfigvalue()));
				treeitem.setValue(node);
				treeitem.setOpen(true);
			}
		});
		configtree.setModel(new DefaultTreeModel<DeviceConfig>(new DeviceConfigUtil().getRoot()));
		
		configtree.addEventListener(Events.ON_DOUBLE_CLICK, this);
	}

	public void initContent(Hlayout hlayout){	
		Vlayout vlayout = new Vlayout();
		vlayout.setParent(hlayout);
		vlayout.setVflex("1");
		vlayout.setHflex("7");
		
		this.initControl(vlayout);
		this.initViewer(vlayout);	
		this.dataRender();
	}
	
	public void initControl(Vlayout vlayout){
		Hlayout hlayout = new Hlayout();
		hlayout.setParent(vlayout);
		hlayout.setVflex("1");
		hlayout.setHflex("1");
		hlayout.setStyle("margin-bottom: 5px;");	
		
		Hlayout leftside = new Hlayout();
		leftside.setParent(hlayout);
		leftside.setHflex("7.5");
		leftside.setVflex("1");
		
		Hlayout rightside = new Hlayout();
		rightside.setParent(hlayout);
		rightside.setHflex("2.5");
		rightside.setVflex("1");
		
		// cac button dieu khien chuc nang
		Div divtemp = new Div();
		divtemp.setSclass("btn_group");
		divtemp.setParent(rightside);
		divtemp.setStyle("float: right");
	    btnedit = new Button("Cập nhật");
		btnedit.setParent(divtemp);		
		btnedit.setImage("/themes/images/Edit_12.png");
		btnedit.addEventListener(Events.ON_CLICK, this);
		
		divtemp = new Div();
		divtemp.setSclass("btn_group");
		divtemp.setParent(rightside);
		divtemp.setStyle("float: right");
		btnreset = new Button("Refresh");
		btnreset.setParent(divtemp);		
		btnreset.setImage("/themes/images/Refresh_12.png");
		btnreset.addEventListener(Events.ON_CLICK, this);
	}
	
	public void initViewer(Vlayout vlayout){
		listbox = new Listbox();
		listbox.setParent(vlayout);		
		listbox.setVflex("9");
		listbox.setHflex("1");		
		listbox.setMold("paging");
		listbox.setAutopaging(true);	

		Listhead cols = new Listhead();
		cols.setParent(listbox);
		cols.setSizable(true);
		
		Auxhead auxhead = new Auxhead();
		auxhead.setParent(listbox);
		
		// col1
		Listheader col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("Imei");
		col.setAttribute("fieldname", "Imei");
		
		// col2
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("15%");
		col.setLabel("Đội xe");
		col.setAttribute("fieldname", "GroupName");
		// col3
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("Bãi giao ca");
		col.setAttribute("fieldname", "ParkingName");
		// col4
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("Loại xe");
		col.setAttribute("fieldname", "TypeName");
		// col5
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("8%");
		col.setLabel("BKS");			
		col.setAttribute("fieldname", "LicensePlate");
		// col6
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("6%");
		col.setLabel("Số tài");
		col.setAttribute("fieldname", "VehicleNumber");
		// col7
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("VIN");
		col.setAttribute("fieldname", "VinNumber");
		// col8
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("5%");
		col.setLabel("Digital");
		col.setAttribute("fieldname", "Digital");
		// col9
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("5%");
		col.setLabel("V.Max");
		col.setAttribute("fieldname", "SpeedLimit");
		// col10
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("5%");
		col.setLabel("Xung");
		col.setAttribute("fieldname", "PulsePerKm");
		// col11
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("2.5%");
		col.setLabel("Tạo file?");
		col.setAttribute("fieldname", "CreatedFileConfig");
		// col12
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("2.5%");
		col.setLabel("Nhận conf");
		col.setAttribute("fieldname", "ReceivedConfig");
		// col13
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("Lần cuối nhận");
		col.setAttribute("fieldname", "LastetTimeReceivedConfig");
		
		for (Component component : cols.getChildren()) {
			if (component instanceof Listheader)
	        {
	        	Listheader listheader = (Listheader)component;
	        	if (listheader.getAttribute("fieldname").equals("CreatedFileConfig") ||
	        		listheader.getAttribute("fieldname").equals("ReceivedConfig") ||
	        		listheader.getAttribute("fieldname").equals("LastetTimeReceivedConfig") ) {
	        		continue;
				}
	            Textbox txtb = new Textbox();
	    		txtb.setSclass("z-textbox-search");
	    		txtb.setPlaceholder("Tìm " + listheader.getLabel());
	    		txtb.setAttribute("fieldSearch", listheader.getAttribute("fieldname"));
	    		Auxheader auxheader = new Auxheader();
	    		//auxheader.appendChild(new Image("./themes/images/filter.png"));
	    		txtb.setParent(auxheader);
	    		auxheader.setParent(auxhead);		    
	    		txtb.addEventListener(Events.ON_OK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						// TODO Auto-generated method stub
						filterDataAfterRending();
					}
				});
	    		txtList.add(txtb);	    		
	        }
		}
		
	}	
	
	public void dataRender(){
		if (listbox != null) {
			listbox.setItemRenderer(new ListitemRenderer<DeviceConfiguration>(){
				@Override
				public void render(Listitem listitem, DeviceConfiguration data, int index)
						throws Exception {
					SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
					new Listcell(data.getImei()).setParent(listitem);
					new Listcell(data.getGroupName()).setParent(listitem);
					new Listcell(data.getParkingName()).setParent(listitem);				
					new Listcell(data.getTypeName()).setParent(listitem);
					new Listcell(data.getLicensePlate()).setParent(listitem);
					new Listcell(data.getVehicleNumber()).setParent(listitem);
					new Listcell(data.getVinNumber()).setParent(listitem);
					new Listcell("" + data.getDigital()).setParent(listitem);
					new Listcell("" + data.getSpeedLimit()).setParent(listitem);
					new Listcell("" + data.getPulsePerKm()).setParent(listitem);
				
					Listcell cell = new Listcell();
					cell.setParent(listitem);
					Div div = new Div();
					Checkbox checkbox = new Checkbox();
					checkbox.setChecked(data.isCreatedFileConfig());
					checkbox.setParent(div);
					cell.appendChild(div);
					
					cell = new Listcell();
					cell.setParent(listitem);
					div = new Div();
					checkbox = new Checkbox();
					checkbox.setChecked(data.isReceivedConfig());
					checkbox.setParent(div);
					cell.appendChild(div);
					
					cell = new Listcell();
					cell.setParent(listitem);
					cell.appendChild(new Label("" + (data.getLastetTimeReceivedConfig() == null ? "" : 
						dateformat.format(data.getLastetTimeReceivedConfig()))));
					
					listitem.setValue(data);
				}
			});
			
			listbox.addEventListener(Events.ON_SELECT, this);
		}
	}
	
	public Auxhead addAuxheadAtListbox(Listbox listbox) {
		Collection<Component> components =  listbox.getHeads();
		Auxhead auxhead = null;
		if (components.contains(Auxhead.class))
		{
		    for (Component component : components)
		    {
		        if (component instanceof Auxhead)
		        {
		            auxhead = (Auxhead)component;
		            break;
		        }
		    }
		} else {
		    auxhead = new Auxhead();
		}
		if (components.contains(Listhead.class)) {
			for (Component component : components)
		    {
		        if (component instanceof Listheader)
		        {
		        	Listheader col = (Listheader)component;
		            Textbox txtb = new Textbox();
		    		txtb.setPlaceholder("Tìm " + col.getLabel());
		    		txtb.setAttribute("fieldSearch", txtb.getAttribute("fieldname"));
		    		Auxheader auxheader = new Auxheader();
		    		auxheader.appendChild(new Image("./themes/images/filter.png"));
		    		txtb.setParent(auxheader);
		    		auxheader.setParent(auxhead);		    		
		    		txtList.add(txtb);
		        }
		    }
		}			
		auxhead.setParent(listbox);
		return auxhead;
	}
		
	public void filterDataAfterRending(){
		if (txtList.size() > 0) {
			ArrayList<DeviceConfiguration> lstfilter = (ArrayList<DeviceConfiguration>) lstvalue;
			for (Iterator<Textbox> iterator = txtList.iterator(); iterator.hasNext();) {
				Textbox textbox = (Textbox) iterator.next();
				if (textbox.getText().length() > 0) {
					lstfilter = filterComponent((String) textbox.getAttribute("fieldSearch"), textbox.getText(), lstfilter);
				}
			}
			listbox.setModel(new ListModelList<DeviceConfiguration>(lstfilter, true));
		}
	}
	
	public ArrayList<DeviceConfiguration> filterComponent(String search, String input, 
			ArrayList<DeviceConfiguration> searchinglist){
		switch (search) {
		case "Imei":
			searchinglist = (ArrayList<DeviceConfiguration>) searchinglist.stream()
			.filter(p -> p.getImei().contains(input)).collect(Collectors.toList());
			break;
		case "GroupName":
			searchinglist = (ArrayList<DeviceConfiguration>) searchinglist.stream()
			.filter(p -> p.getGroupName().toLowerCase().contains(input.toLowerCase()))
			.collect(Collectors.toList());
			break;
		case "ParkingName":
			searchinglist = (ArrayList<DeviceConfiguration>) searchinglist.stream()
			.filter(p -> p.getParkingName().toLowerCase().contains(input.toLowerCase()))
			.collect(Collectors.toList());
			break;
		case "TypeName":
			searchinglist = (ArrayList<DeviceConfiguration>) searchinglist.stream()
			.filter(p -> p.getTypeName().toLowerCase().contains(input.toLowerCase()))
			.collect(Collectors.toList());
			break;
		case "LicensePlate":
			searchinglist = (ArrayList<DeviceConfiguration>) searchinglist.stream()
			.filter(p -> p.getLicensePlate().toLowerCase().contains(input.toLowerCase()))
			.collect(Collectors.toList());
			break;
		case "VehicleNumber":
			searchinglist = (ArrayList<DeviceConfiguration>) searchinglist.stream()
			.filter(p -> p.getVehicleNumber().toLowerCase().contains(input.toLowerCase()))
			.collect(Collectors.toList());
			break;
		case "VinNumber":
			searchinglist = (ArrayList<DeviceConfiguration>) searchinglist.stream()
			.filter(p -> p.getVinNumber().toLowerCase().contains(input.toLowerCase()))
			.collect(Collectors.toList());
			break;
		case "Digital":
			searchinglist = (ArrayList<DeviceConfiguration>) searchinglist.stream()
			.filter(p -> p.getDigital() == Integer.parseInt(input))
			.collect(Collectors.toList());
			break;
		case "SpeedLimit":
			searchinglist = (ArrayList<DeviceConfiguration>) searchinglist.stream()
			.filter(p -> p.getSpeedLimit() == Integer.parseInt(input))
			.collect(Collectors.toList());
			break;
		case "PulsePerKm":
			searchinglist = (ArrayList<DeviceConfiguration>) searchinglist.stream()
			.filter(p -> p.getPulsePerKm() == Integer.parseInt(input))
			.collect(Collectors.toList());
			break;
		default:
			break;
		}
		return searchinglist;
	}
	
	public void loadData() {		
		ListObjectDatabase listObjectDatabase = new ListObjectDatabase();
		lstvalue = listObjectDatabase.getDeviceNoneConfig("0", "admin");
		if (listbox != null) {
			if (lstvalue == null || lstvalue.isEmpty()) {
				lstvalue = new ArrayList<DeviceConfiguration>();
				listbox.setEmptyMessage("Không có dữ liệu");				
			} 
			listbox.setModel(new ListModelList<DeviceConfiguration>(lstvalue, true));
		}		
	}

	@Override
	public void onEvent(Event event) throws Exception {
		// TODO Auto-generated method stub
		if(event.getName().equals(Events.ON_SELECT)){
			if(event.getTarget().equals(listbox)){
				Listitem item = listbox.getSelectedItem();
				DeviceConfiguration deviceConfiguration = item.getValue();
				configtree.setModel(new DefaultTreeModel<DeviceConfig>(new DeviceConfigUtil().
						getRoot(deviceConfiguration.getDeviceID())));				
			}
		}
		if(event.getName().equals(Events.ON_DOUBLE_CLICK)){
			if(event.getTarget().equals(configtree)){
				Treeitem treeitem = configtree.getSelectedItem();
				ConfigTreeNode<DeviceConfig> node = treeitem.getValue();
				if (node.getDeviceconfig().getCanedit() == 0) {
					showNotify("Không sửa cấu hình này", configtree);
					return;
				}
				Listitem listitem = listbox.getSelectedItem();				
				if (listitem == null) {
					showNotify("Chưa chọn thiết bị để cấu hình", listbox);
				} else {
					ShowDeviceConfigComponent shower = 
							new ShowDeviceConfigComponent(node.getDeviceconfig(), listitem.getValue(), this);
					shower.showModal();
				}
			}
		}
		if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget().equals(btnedit)) {
				Listitem item = listbox.getSelectedItem();
				DeviceConfiguration deviceConfiguration = item.getValue();								
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("uid", "admin");
				params.put("pwd", "WZJ[P");
				params.put("cmd", "SETCFG");
				params.put("param", Integer.toString(deviceConfiguration.getDeviceID()));
				String url = "http://taxionline.mailinhapp.com/";
				String entity = "Admin/ReceiveRequest";
				// Lam lan luot tung thiet bi, khong tach thread, dam bao file cau hinh duoc tao
				String response = FunctionCommon.sendPost(url, entity, params);
				if (response.equals(String.format("\"Err\""))) {
					Messagebox.show("Không thành công", "Lỗi", Messagebox.OK, Messagebox.ERROR);
				}
				if (response.equals(String.format("\"OK\""))) {
					Messagebox.show("Thành công", "Thông báo", Messagebox.OK, Messagebox.INFORMATION);
				}
			}
			if (event.getTarget().equals(btnreset)) {
				this.loadData();
			}
		}
	}	
	
	public void editConfigComponent(DeviceConfiguration obj){
		
	}
	
	private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "info", ref, "end_center", 3000);
    }
	
	class DeviceConfigUtil{
		private ConfigTreeNode<DeviceConfig> rootCfg;	
		
		public ConfigTreeNode<DeviceConfig> getRoot() {
			DeviceConfigController deviceConfigController = 
					(DeviceConfigController) ControllerUtils.getController(DeviceConfigController.class);
			List<DeviceConfig> lstconfigs = deviceConfigController.find("From DeviceConfig");
			DeviceConfig parent = (DeviceConfig)lstconfigs.stream().filter(p -> p.getParentid() == 0).findFirst().get();
			rootCfg = this.getSubNodeCollection(parent, lstconfigs);
			return rootCfg;
		}			
		
		public ConfigTreeNode<DeviceConfig> getRoot(int deviceid){
			DeviceConfigController deviceConfigController = 
					(DeviceConfigController) ControllerUtils.getController(DeviceConfigController.class);
			List<DeviceConfig> lstconfigs = deviceConfigController.getDeviceConfig(deviceid);
			DeviceConfig parent = (DeviceConfig)lstconfigs.stream().filter(p -> p.getParentid() == 0).findFirst().get();
			rootCfg = this.getSubNodeCollection(parent, lstconfigs);
			return rootCfg;
		}
		
		public ConfigTreeNode<DeviceConfig> getSubNodeCollection(DeviceConfig parent, List<DeviceConfig> listnodes){
			List<DeviceConfig> childs = (List<DeviceConfig>) 
				listnodes.stream().filter(p -> p.getParentid() == parent.getId()).collect(Collectors.toList());
			if (childs.size() == 0) {
				ConfigTreeNode<DeviceConfig> node = new ConfigTreeNode<DeviceConfig>(parent);
				listnodes.remove(parent);
				return node;
			} else {
				ConfigTreeNodeCollection<DeviceConfig> collect = new ConfigTreeNodeCollection<>();
				for (DeviceConfig cfg : childs) {
					ConfigTreeNode<DeviceConfig> node = getSubNodeCollection(cfg, listnodes);
					collect.add(node);
				}
				ConfigTreeNode<DeviceConfig> node = new ConfigTreeNode<DeviceConfig>(parent, collect, true);
				return node;
			}
		}
	}
	
	@SuppressWarnings("hiding")
	class ConfigTreeNode<DeviceConfig> extends DefaultTreeNode<DeviceConfig> {
	    private static final long serialVersionUID = -8085873079938209759L;
	    private boolean open = false;
	    private DeviceConfig deviceconfig;
	    
	    public ConfigTreeNode(DeviceConfig data, ConfigTreeNodeCollection<DeviceConfig> children, boolean open) {
	        super(data, children);
	        this.setOpen(open);
	        this.setDeviceconfig(data);
	    }	 
	    public ConfigTreeNode(DeviceConfig data, ConfigTreeNodeCollection<DeviceConfig> children) {
	        super(data, children);
	        this.setDeviceconfig(data);	        		
	    }
	    public ConfigTreeNode(DeviceConfig data) {
	        super(data);
	        this.setDeviceconfig(data);
	    }	 
	    public boolean isOpen() {
	        return open;
	    }	 
	    public void setOpen(boolean open) {
	        this.open = open;
	    }
		public DeviceConfig getDeviceconfig() {
			return deviceconfig;
		}
		public void setDeviceconfig(DeviceConfig deviceconfig) {
			this.deviceconfig = deviceconfig;
		}
	}
	
	@SuppressWarnings("hiding")
	class ConfigTreeNodeCollection<DeviceConfig> extends LinkedList<DefaultTreeNode<DeviceConfig>> {		 
	    private static final long serialVersionUID = -716538636175940429L;	    
	}
	
	class ShowDeviceConfigComponent extends Window implements EventListener<Event>{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private DeviceConfig configcomponent;
		private DeviceConfiguration deviceinfo;
		private Component parent;
		private Combobox cbxValtype;
		private Textbox txtVal;
		private Button btnAccept;
		private Window compwindow;
		
		public ShowDeviceConfigComponent(DeviceConfig deviceconfig, DeviceConfiguration deviceinfo, Component parent) {
			// TODO Auto-generated constructor stub
			this.configcomponent = deviceconfig;
			this.deviceinfo = deviceinfo;
			this.parent = parent;
			this.init();
		}
		
		public void init(){
			compwindow = new Window();
			compwindow.setClosable(true);
			compwindow.setAction("show: slideDown;hide: slideUp");
			compwindow.setPosition("center,center");			
			compwindow.setTitle("Imei:" + deviceinfo.getImei() + 
								" - Xe: " + deviceinfo.getLicensePlate() + 
								" - Đội: " + deviceinfo.getGroupName());
			compwindow.setWidth("600px");
			compwindow.setParent(parent);
			compwindow.setSizable(true);
			
			Vlayout vlayout = new Vlayout();
			vlayout.setParent(compwindow);
			vlayout.setHflex("1");
			vlayout.setVflex("1");
			
			Grid grid = new Grid();
			grid.setParent(vlayout);
			grid.setHflex("1");
			grid.setVflex("1");
			
			Columns cols = new Columns();
			cols.setParent(grid);
			
			Column col = new Column("Tên cấu hình");
			col.setParent(cols);
			col.setHflex("3");
			col = new Column("Giá trị");
			col.setParent(cols);
			col.setHflex("3");
			col = new Column("Kiểu giá trị");
			col.setParent(cols);
			col.setHflex("2");
			col = new Column();
			col.setParent(cols);
			col.setHflex("2");
			
			Rows rows = new Rows();
			rows.setParent(grid);
			
			Row row = new Row();
			row.setParent(rows);
			// col 1
			Label label = new Label(configcomponent.getConfigname());
			label.setParent(row);
			// col 2
			txtVal = new Textbox(this.getDeviceConfigValue());
			txtVal.setRows(1);
			txtVal.setParent(row);
			// col 3
			this.createCbxValueType(configcomponent.getConfigtypeval(configcomponent.getConfigtype()));
			cbxValtype.setParent(row);
			
			// col 4
			btnAccept = new Button("Cập nhật");
			btnAccept.setParent(row);		
			btnAccept.addEventListener(Events.ON_CLICK, this);
		}
		
		public void createCbxValueType(int index){		
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("0", "int");
			data.put("1", "string");
			data.put("2", "boolean");
			data.put("3", "real");
			data.put("4", "array");
			data.put("5", "object");			
			ComboboxRender renderCbo = new ComboboxRender();
			cbxValtype = renderCbo.ComboboxRendering(data, "", "combobox_rider_state", 80, 25, index, false);
			cbxValtype.setReadonly(true);
		}
		
		public String getDeviceConfigValue() {
			if (!configcomponent.getConfigcode().equals("driver_table")) {
				return configcomponent.getConfigvalue();
			} else {
				ListObjectDatabase lod = new ListObjectDatabase();				
				List<DriverInVehicle> lstdrivers = lod.getDriversInVehicle(deviceinfo.getDeviceID());
				if (lstdrivers.size() == 0)
                {
                    return "[]";
                }
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (DriverInVehicle dv : lstdrivers)
                {
                    sb.append("{");
                    sb.append(String.format("\"d_name\":\"%1s\",\"d_license\":\"%2s\",\"d_uid\":\"%3s\"",
                            dv.getDrivername(),dv.getDriverlicense(), dv.getStaffcard()));
                    sb.append("}");
                    sb.append(",");
                }     
                sb.append("]");                
                String outStr = sb.toString().replace(",]", "]");
                return outStr;
			}
		}
		
		public void showModal(){
			// show window
			compwindow.doModal();
		}
		
		@Override
		public void onEvent(Event event) throws Exception {
			// TODO Auto-generated method stub
			if(event.getName().equals(Events.ON_CLICK)){
				if(event.getTarget().equals(btnAccept)){
					ListObjectDatabase lod = new ListObjectDatabase();
					lod.insertLogDeviceConfig(configcomponent.getConfigid(), deviceinfo.getDeviceID(), txtVal.getValue());
					lod.updateDeviceConfigIntoOnline(deviceinfo.getDeviceID());
					DeviceController devicecontroller = (DeviceController) ControllerUtils
							.getController(DeviceController.class);
					List<Device> lstvalue = devicecontroller.find("From Device Where id = " + deviceinfo.getDeviceID());
					if (lstvalue.size() > 0) {
						Device device = lstvalue.get(0);
						device.setCreatedFileConfig(1);
						device.setReceivedConfig(1);
						device.save();
					}
					compwindow.detach();
				}
			}
		}
		
	}
}
