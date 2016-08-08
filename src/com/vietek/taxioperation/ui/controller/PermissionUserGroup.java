package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.West;

import com.vietek.taxioperation.controller.MapSysGroupCompanyController;
import com.vietek.taxioperation.controller.MapSysGroupVehicleController;
import com.vietek.taxioperation.controller.SysCompanyController;
import com.vietek.taxioperation.controller.SysGroupController;
import com.vietek.taxioperation.controller.VehicleController;
import com.vietek.taxioperation.model.MapSysGroupCompany;
import com.vietek.taxioperation.model.MapSysGroupVehicle;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.SysGroup;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.model.VoipCenter;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.ui.util.ListItemRenderer;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

/**
 * 
 * @author sonvh
 *
 */
public class PermissionUserGroup extends AbstractWindowPanel implements Serializable {

	private Listbox lboxSysGroup;
	private List<SysGroup> lstSysGroupModel;
	private List<MapSysGroupCompany> lstSysGroupCompanyModel;
	private List<MapSysGroupVehicle> lstSysGroupVehicleModel;

	private Listbox lbCompany;
	private Listbox lbCompanySelect;
	private List<SysCompany> lstCompanyModel_source;
	private List<SysCompany> lstCompanyModel;
	private List<SysCompany> lstCompanyModelSelect;

	private Listbox lbVehicle;
	private Listbox lbVehicleSelect;
	private List<Vehicle> lstVehicleModel_source;
	private List<Vehicle> lstVehicleModel;
	private List<Vehicle> lstVehicleModelSelect;

	Button btUp_cpn;
	Button btDown_cpn;
	Button btSave_cpn;
	Button btCancel_cpn;
	Textbox tbName_cpn;

	Button btUp_vhc;
	Button btDown_vhc;
	Button btSave_vhc;
	Button btCancel_vhc;
	Textbox tbName_vhc;
	public PermissionUserGroup _this;
	Tabbox tabbox;

	public PermissionUserGroup() {
		super(true);
		_this = this;
		// TODO Auto-generated constructor stub
		setTitle("Danh sách");
		setDetailTitle("PHÂN QUYỀN");
		initUI();
		setData();
	}

	private void initUI() {

		btUp_cpn.addEventListener(Events.ON_CLICK, EVENT_ONCLICK_BT_UP_COMPANY);
		btDown_cpn.addEventListener(Events.ON_CLICK, EVENT_ONCLICK_BT_DOWN_COMPANY);
		btSave_cpn.addEventListener(Events.ON_CLICK, EVENT_ONCLICK_SAVE_COMPANY);

		btUp_vhc.addEventListener(Events.ON_CLICK, EVENT_ONCLICK_BT_UP_VEHICLE);
		btDown_vhc.addEventListener(Events.ON_CLICK, EVENT_ONCLICK_BT_DOWN_VEHICLE);
		btSave_vhc.addEventListener(Events.ON_CLICK, EVENT_ONCLICK_SAVE_VEHICLE);

		tbName_cpn.addEventListener(Events.ON_OK, EVENT_ONCLICK_SEARCH_COMPANY);
		tbName_vhc.addEventListener(Events.ON_OK, EVENT_ONCLICK_SEARCH_VEHICLE);
		/*
		 * select default
		 */
		lboxSysGroup.setSelectedIndex(0);
	}

	private void setData() {
		// lbCompany.setModel(new ListModelList<SysCompany>(lstCompanyModel));
		// lbVehicle.setModel(new ListModelList<Vehicle>(lstVehicleModel));
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

		this.setWidth("100%");
		this.setHeight("100%");

		Borderlayout layout = new Borderlayout();
		layout.setParent(this);
		layout.setHeight("100%");

		West west = new West();
		west.setParent(layout);
		west.setTitle("Danh sách nhóm quyền");
		west.setSize("30%");
		west.setFlex(true);
		west.setSplittable(true);
		west.setCollapsible(true);

		Center center = new Center();
		center.setParent(layout);
		center.setBorder("none");
		center.setFlex(true);
		// East east = new East();
		// east.setParent(layout);
		// east.setSize("70%");
		// east.setBorder("none");
		// east.setFlex(true);

		Div divLeft = initPanelLeft();
		divLeft.setParent(west);
		divLeft.setVflex("1");

		Borderlayout layoutRight = initPanelRight();
		layoutRight.setParent(center);
	}

	private Div initPanelLeft() {
		Div result = new Div();
		lboxSysGroup = initListBox(initColumnsSysGroup(), "sysGroup");
		lboxSysGroup.setModel(new ListModelList<SysGroup>(lstSysGroupModel));
		lboxSysGroup.setParent(result);
		return result;
	}

	private Borderlayout initPanelRight() {

		Borderlayout layout = new Borderlayout();
		Center center = new Center();
		center.setParent(layout);
		center.setBorder("0");
		center.setFlex(true);
		tabbox = initTabbox();
		tabbox.setParent(center);
		return layout;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1720435382616522888L;

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

		return;

	}

	@Override
	public void initDetailRightPanel() {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void initColumns() {
		// TODO Auto-generated method stub

	}

	public void loadDataCompany(SysGroup group) {

		if (tabbox.getSelectedTab().getId().equals("company")) {
			/**
			 * load data for list SysGroupCompany
			 */

			lstSysGroupCompanyModel.clear();
			MapSysGroupCompanyController controllerMapSysGroupCompany = (MapSysGroupCompanyController) ControllerUtils
					.getController(MapSysGroupCompanyController.class);

			lstSysGroupCompanyModel = controllerMapSysGroupCompany
					.find("from MapSysGroupCompany m where sysGroup= " + group.getId());

			/**
			 * backup
			 */
			lstCompanyModel.clear();
			for (SysCompany sysCompany : lstCompanyModel_source) {
				lstCompanyModel.add(sysCompany);
			}
			lstCompanyModelSelect.clear();
			if (lstSysGroupCompanyModel.size() > 0) {
				for (MapSysGroupCompany object : lstSysGroupCompanyModel) {
					try {
						MapSysGroupCompany objClone = (MapSysGroupCompany) object.clone();
						lstCompanyModelSelect.add(objClone.getSysCompany());
						lstCompanyModel.remove(objClone.getSysCompany());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			/*
			 * show data
			 */
			lbCompany.setModel(new ListModelList<SysCompany>(lstCompanyModel));
			lbCompanySelect.setModel(new ListModelList<SysCompany>(lstCompanyModelSelect));
		} else {
			/**
			 * load data for list Vehicle
			 */

			lstSysGroupVehicleModel.clear();
			MapSysGroupVehicleController controllerMapSysGroupVehicle = (MapSysGroupVehicleController) ControllerUtils
					.getController(MapSysGroupVehicleController.class);
			lstSysGroupVehicleModel = controllerMapSysGroupVehicle
					.find("from MapSysGroupVehicle m where sysGroup= " + group.getId());

			lstVehicleModel.clear();
			for (Vehicle vehicle : lstVehicleModel_source) {
				lstVehicleModel.add(vehicle);
			}
			lstVehicleModelSelect.clear();
			if (lstSysGroupVehicleModel.size() > 0) {
				for (MapSysGroupVehicle object : lstSysGroupVehicleModel) {
					try {
						MapSysGroupVehicle objClone = (MapSysGroupVehicle) object.clone();
						lstVehicleModelSelect.add(objClone.getVehicle());
						lstVehicleModel.remove(objClone.getVehicle());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			/*
			 * show data
			 */
			lbVehicle.setModel(new ListModelList<Vehicle>(lstVehicleModel));
			lbVehicleSelect.setModel(new ListModelList<Vehicle>(lstVehicleModelSelect));

		}
	}

	public void loadDataVehicle() {
		if (lstVehicleModel_source == null)
			lstVehicleModel_source = new ArrayList<>();
		else
			lstVehicleModel_source.clear();
		VehicleController controller = (VehicleController) ControllerUtils.getController(VehicleController.class);

		StringBuffer query = new StringBuffer();
		query.append("from Vehicle");
		if (tbName_vhc != null)
			if (!tbName_vhc.getValue().trim().isEmpty()) {
				query.append(" where value like '%" + tbName_vhc.getValue().trim() + "%'");
			}

		lstVehicleModel_source = controller.find(query.toString()); // select

		lstVehicleModel.clear();
		for (Vehicle vehicle : lstVehicleModel_source) {
			lstVehicleModel.add(vehicle);
		}
	}

	public void loadDataCompany() {
		/**
		 * load data for list company
		 */
		if (lstCompanyModel_source == null)
			lstCompanyModel_source = new ArrayList<SysCompany>();
		else
			lstCompanyModel_source.clear();
		SysCompanyController controllerCompany = (SysCompanyController) ControllerUtils
				.getController(SysCompanyController.class);

		StringBuffer query = new StringBuffer();
		query.append("from SysCompany");
		if (tbName_cpn != null)
			if (!tbName_cpn.getValue().trim().isEmpty()) {
				query.append(" where name like '%" + tbName_cpn.getValue().trim() + "%'");
			}
		lstCompanyModel_source = controllerCompany.find(query.toString());

		lstCompanyModel.clear();
		for (SysCompany company : lstCompanyModel_source) {
			lstCompanyModel.add(company);
		}

	}

	@Override
	public void loadData() {
		/**
		 * load data for list SysGroup
		 */
		if (lstSysGroupModel == null)
			lstSysGroupModel = new ArrayList<SysGroup>();
		else
			lstSysGroupModel.clear();
		SysGroupController controller = (SysGroupController) ControllerUtils.getController(SysGroupController.class);

		lstSysGroupModel = controller.find("from SysGroup"); // select
																// value,name,isActive

		if (lstSysGroupCompanyModel == null)
			lstSysGroupCompanyModel = new ArrayList<MapSysGroupCompany>();
		if (lstSysGroupVehicleModel == null)
			lstSysGroupVehicleModel = new ArrayList<MapSysGroupVehicle>();

		/**
		 * company select
		 */
		if (lstCompanyModel == null)
			lstCompanyModel = new ArrayList<SysCompany>();
		else
			lstCompanyModel.clear();
		if (lstCompanyModelSelect == null)
			lstCompanyModelSelect = new ArrayList<SysCompany>();
		else
			lstCompanyModelSelect.clear();

		/**
		 * vehicle select
		 */
		if (lstVehicleModel == null)
			lstVehicleModel = new ArrayList<Vehicle>();
		else
			lstVehicleModel.clear();
		if (lstVehicleModelSelect == null)
			lstVehicleModelSelect = new ArrayList<Vehicle>();
		else
			lstVehicleModelSelect.clear();

		loadDataCompany();
		loadDataVehicle();
	}

	/**
	 * create tabbox
	 */

	private Tabbox initTabbox() {

		Tabbox result = new Tabbox();
		result.setVflex("1");
		result.setHflex("1");

		Tabs tbs = new Tabs();
		tbs.setParent(result);

		Tabpanels tbpnels = new Tabpanels();
		tbpnels.setParent(result);
		/*
		 * tabs
		 */
		Tab tb = new Tab();
		tb.setParent(tbs);
		tb.setLabel("CÔNG TY");
		tb.setId("company");

		tb = new Tab();
		tb.setParent(tbs);
		tb.setLabel("XE");
		tb.setId("vehicle");
		/*
		 * tab panels
		 */
		Tabpanel tbpnel = new Tabpanel();
		tbpnel.setParent(tbpnels);
		lbCompanySelect = initLboxCompanySelect();
		lbCompany = initLboxCompany();
		btUp_cpn = new Button("UP");
		btDown_cpn = new Button("DOWN");
		btSave_cpn = new Button("Save");
		btCancel_cpn = new Button("Cancel");
		tbName_cpn = new Textbox();
		tbName_cpn.setPlaceholder("Tìm theo tên");
		Component cpn = initPanelCompany(lbCompany, lbCompanySelect, tbName_cpn, btUp_cpn, btDown_cpn, btSave_cpn,
				btCancel_cpn);
		cpn.setParent(tbpnel);

		tbpnel = new Tabpanel();
		tbpnel.setParent(tbpnels);
		lbVehicleSelect = initLboxVehicleSelect();
		lbVehicle = initLboxVehicle();
		btUp_vhc = new Button("UP");
		btDown_vhc = new Button("DOWN");
		btSave_vhc = new Button("Save");
		btCancel_vhc = new Button("Cancel");
		tbName_vhc = new Textbox();
		tbName_vhc.setPlaceholder("Tìm theo số tài");
		cpn = initPanelCompany(lbVehicle, lbVehicleSelect, tbName_vhc, btUp_vhc, btDown_vhc, btSave_vhc, btCancel_vhc);
		cpn.setParent(tbpnel);

		return result;
	}

	private Component initPanelCompany(Listbox lb, Listbox lbSelect, Textbox tbSearch, Button btUp, Button btDown,
			Button btSave, Button btCanel) {

		String heightButton = "37px";
		Borderlayout layout = new Borderlayout();
		North north = new North();
		north.setParent(layout);
		north.setSize("50%");
		north.setBorder("none");
		north.setSplittable(true);
		north.setCollapsible(false);

		Center center = new Center();
		center.setParent(layout);
		center.setBorder("none");
		center.setAutoscroll(true);
		center.setFlex(true);

		South south = new South();
		south.setParent(layout);
		south.setSize(heightButton);
		south.setBorder("none");
		/**
		 * company select
		 */
		lbSelect.setParent(north);

		/**
		 * company
		 */
		Borderlayout layoutChild = new Borderlayout();
		layoutChild.setParent(center);
		North northChild = new North();
		northChild.setParent(layoutChild);
		northChild.setSize(heightButton);

		Center centerChild = new Center();
		centerChild.setParent(layoutChild);
		centerChild.setBorder("0");
		centerChild.setFlex(true);

		Component divDirectButton = initDirectButton(btUp, btDown, tbSearch);
		divDirectButton.setParent(northChild);

		lb.setParent(centerChild);
		/**
		 * button action
		 */
		Component divButtonActionCpn = initButtonActionCompay(btSave, btCanel);
		divButtonActionCpn.setParent(south);

		return layout;
	}

	private Component initButtonActionCompay(Button btSave, Button btCancel) {

		Borderlayout layout = new Borderlayout();
		West east = new West();
		east.setParent(layout);
		east.setSize("40%");
		east.setBorder("none");
		Center center = new Center();
		center.setParent(layout);
		center.setBorder("none");

		Div div = new Div();
		div.setParent(center);

		btSave.setParent(div);
		btCancel.setSclass("");

		btSave.setParent(div);
		btCancel.setSclass("");

		btSave.setAutodisable("self");
		return layout;
	}

	private Component initDirectButton(Button btUp, Button btDown, Textbox tbSearch) {

		Borderlayout layout = new Borderlayout();
		West east = new West();
		east.setParent(layout);
		east.setSize("40%");
		east.setBorder("none");

		tbSearch.setParent(east);
		tbSearch.setHeight("100%");

		Center center = new Center();
		center.setParent(layout);
		center.setBorder("none");

		Div div = new Div();
		div.setParent(center);

		btUp.setParent(div);
		btUp.setSclass("");

		btDown.setParent(div);
		btDown.setSclass("");

		return layout;
	}

	/**
	 * init listbox
	 * 
	 * @return
	 */
	private Listbox initLboxCompanySelect() {

		Listbox result = new Listbox();
		result = new Listbox();

		result = initListBox(initColumnsCompany(), "companySelect");
		result.setModel(new ListModelList<SysCompany>());
		return result;
	}

	private Listbox initLboxCompany() {

		Listbox result = new Listbox();

		result = initListBox(initColumnsCompany(), "company");
		result.setModel(new ListModelList<SysCompany>());
		return result;
	}

	private Listbox initLboxVehicleSelect() {

		Listbox result = new Listbox();
		result = new Listbox();

		result = initListBox(initColumnsVehicle(), "vehicleSelect");
		result.setModel(new ListModelList<Vehicle>());
		return result;
	}

	private Listbox initLboxVehicle() {

		Listbox result = new Listbox();

		result = initListBox(initColumnsVehicle(), "vehicle");
		result.setModel(new ListModelList<Vehicle>());
		return result;
	}

	private Listbox initListBox(List<GridColumn> lstCol, String nameGrid) {
		Listbox lstBox = new Listbox();
		lstBox.setName(nameGrid);
		lstBox.setCheckmark(true);
		lstBox.setMultiple(true);
		lstBox.setMold("paging");
		lstBox.setPageSize(100);
		lstBox.setPagingPosition("bottom");

		lstBox.setVflex(true);
		lstBox.setHflex("1");

		lstBox.setSizedByContent(false);
		lstBox.setNonselectableTags("");
		lstBox.setSclass("listbox_dieudam listbox_dieudam_row");
		lstBox.addEventListener(Events.ON_SELECT, this);
		lstBox.addEventListener(Events.ON_OK, this);
		lstBox.addEventListener(Events.ON_DOUBLE_CLICK, this);
		lstBox.addEventListener(Events.ON_CLICK, this);
		Listhead head = new Listhead();
		head.setParent(lstBox);
		head.setSizable(true);
		Listheader header = null;

		for (int i = 0; i < lstCol.size(); i++) {
			GridColumn gridCol = lstCol.get(i);
			header = new Listheader(gridCol.getHeader());
			header.setWidth(gridCol.getWidth() + "px");
			header.setStyle("max-width: " + gridCol.getWidth());
			header.setParent(head);
		}
		ListItemRenderer<Class<?>> renderer = new ListItemRenderer<Class<?>>((ArrayList<GridColumn>) lstCol);
		lstBox.setItemRenderer(renderer);
		return lstBox;
	}

	/**
	 * init columns
	 * 
	 * @return
	 */
	private List<GridColumn> initColumnsSysGroup() {
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã nhóm", 100, String.class, "getValue"));
		lstCols.add(new GridColumn("Tên nhóm", 200, String.class, "getName"));
		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class, "getIsActive"));
		return lstCols;
	}

	public List<GridColumn> initColumnsCompany() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã", 200, String.class, "getValue"));
		lstCols.add(new GridColumn("Tên", 300, String.class, "getName"));
		lstCols.add(new GridColumn("Công ty cha", 300, Integer.class, "getParentId"));
		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class, "getIsActive"));
		lstCols.add(new GridColumn("Tổng đài", 300, VoipCenter.class, "getVoipCenter"));
		lstCols.add(new GridColumn("Ghi chú", 300, String.class, "getNote"));
		return lstCols;
	}

	public List<GridColumn> initColumnsVehicle() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã xe", 70, String.class, "getId"));
		lstCols.add(new GridColumn("Biển kiểm soát", 200, String.class, "getTaxiNumber"));
		lstCols.add(new GridColumn("Số tài", 150, String.class, "getValue"));
		lstCols.add(new GridColumn("Loại xe", 300, String.class, "getTaxiType"));
		lstCols.add(new GridColumn("Đội xe", 300, String.class, "getTaxiGroup"));
		return lstCols;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		// TODO Auto-generated method stub
		super.onEvent(event);

		if (event.getName().equals(Events.ON_DOUBLE_CLICK) && event.getTarget().equals(lboxSysGroup)) {
			SysGroup sysGroup = lstSysGroupModel.get(lboxSysGroup.getSelectedIndex());
			loadDataCompany(sysGroup);
		}
	}

	private EventListener<Event> EVENT_ONCLICK_SAVE_COMPANY = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			/*
			 * delete all company by sysgroup
			 */
			SysGroup sysGroup = lstSysGroupModel.get(lboxSysGroup.getSelectedIndex());
			if (sysGroup != null) {
				for (MapSysGroupCompany object : lstSysGroupCompanyModel) {
					object.delete();
					// DELETE FROM tableName WHERE ID IN (1,2)
				}
				lstSysGroupCompanyModel.clear();
				/*
				 * add new
				 */
				for (SysCompany sysCompany : lstCompanyModelSelect) {

					MapSysGroupCompany object = new MapSysGroupCompany();
					object.setSysCompany(sysCompany);
					object.setSysGroup(sysGroup);
					object.save();
					lstSysGroupCompanyModel.add(object);
				}

				// show notification
				Env.getHomePage().showNotification("Đã thay đổi!", Clients.NOTIFICATION_TYPE_INFO);
			}
		}
	};
	private EventListener<Event> EVENT_ONCLICK_SAVE_VEHICLE = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			/*
			 * delete all company by sysgroup
			 */
			SysGroup sysGroup = lstSysGroupModel.get(lboxSysGroup.getSelectedIndex());
			if (sysGroup != null) {
				for (MapSysGroupVehicle object : lstSysGroupVehicleModel) {
					object.delete();
				}
				lstSysGroupVehicleModel.clear();
				/*
				 * add new
				 */
				for (Vehicle vehicle : lstVehicleModelSelect) {

					MapSysGroupVehicle object = new MapSysGroupVehicle();
					object.setVehicle(vehicle);
					object.setSysGroup(sysGroup);
					object.save();

					lstSysGroupVehicleModel.add(object);
				}

				// show notification
				Env.getHomePage().showNotification("Đã thay đổi!", Clients.NOTIFICATION_TYPE_INFO);
			}
		}
	};

	private EventListener<Event> EVENT_ONCLICK_BT_UP_COMPANY = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			if (lbCompany.getItemCount() <= 0 || lbCompany.getSelectedIndex() < 0)
				return;

			SysCompany cpn = lstCompanyModel.get(lbCompany.getSelectedIndex());
			if (cpn != null) {
				if (lstCompanyModel.contains(cpn)) {
					lstCompanyModel.remove(cpn);
					lstCompanyModelSelect.add(0, cpn);

					lbCompany.setModel(new ListModelList<SysCompany>(lstCompanyModel));
					lbCompanySelect.setModel(new ListModelList<SysCompany>(lstCompanyModelSelect));
				}
			}
		}
	};
	private EventListener<Event> EVENT_ONCLICK_BT_UP_VEHICLE = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			if (lbVehicle.getItemCount() <= 0 || lbVehicle.getSelectedIndex() < 0)
				return;

			Vehicle cpn = lstVehicleModel.get(lbVehicle.getSelectedIndex());
			if (cpn != null) {
				if (lstVehicleModel.contains(cpn)) {
					lstVehicleModel.remove(cpn);
					lstVehicleModelSelect.add(0, cpn);

					lbVehicle.setModel(new ListModelList<Vehicle>(lstVehicleModel));
					lbVehicleSelect.setModel(new ListModelList<Vehicle>(lstVehicleModelSelect));
				}
			}
		}
	};

	private EventListener<Event> EVENT_ONCLICK_BT_DOWN_COMPANY = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
			if (lbCompanySelect.getItemCount() <= 0 || lbCompanySelect.getSelectedIndex() < 0)
				return;

			SysCompany cpn = lstCompanyModelSelect.get(lbCompanySelect.getSelectedIndex());
			if (cpn != null) {
				if (lstCompanyModelSelect.contains(cpn)) {
					lstCompanyModelSelect.remove(cpn);
					lstCompanyModel.add(0, cpn);

					lbCompany.setModel(new ListModelList<SysCompany>(lstCompanyModel));
					lbCompanySelect.setModel(new ListModelList<SysCompany>(lstCompanyModelSelect));
				}
			}

		}
	};
	private EventListener<Event> EVENT_ONCLICK_BT_DOWN_VEHICLE = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
			if (lbVehicleSelect.getItemCount() <= 0 || lbVehicleSelect.getSelectedIndex() < 0)
				return;

			Vehicle cpn = lstVehicleModelSelect.get(lbVehicleSelect.getSelectedIndex());
			if (cpn != null) {
				if (lstVehicleModelSelect.contains(cpn)) {
					lstVehicleModelSelect.remove(cpn);
					lstVehicleModel.add(0, cpn);

					lbVehicle.setModel(new ListModelList<Vehicle>(lstVehicleModel));
					lbVehicleSelect.setModel(new ListModelList<Vehicle>(lstVehicleModelSelect));
				}
			}

		}
	};

	private EventListener<Event> EVENT_ONCLICK_SEARCH_COMPANY = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			// TODO Auto-generated method stub

			loadDataCompany();
			lbCompany.setModel(new ListModelList<SysCompany>(lstCompanyModel));
		}

	};
	private EventListener<Event> EVENT_ONCLICK_SEARCH_VEHICLE = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			loadDataVehicle();
			lbVehicle.setModel(new ListModelList<Vehicle>(lstVehicleModel));
		}

	};
}
