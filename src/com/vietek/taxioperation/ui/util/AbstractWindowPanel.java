package com.vietek.taxioperation.ui.util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.gmaps.LatLng;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.ConstantValueSearch;
import com.vietek.taxioperation.common.EnumLevelUser;
import com.vietek.taxioperation.common.EnumUserAction;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.SysAction;
import com.vietek.taxioperation.model.SysFunction;
import com.vietek.taxioperation.model.SysGroup;
import com.vietek.taxioperation.model.SysGroupLine;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.ui.controller.CustomersDetail;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.EditorFactory;
import com.vietek.taxioperation.util.ComboboxThread;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.LocalizationUtils;
import com.vietek.taxioperation.util.SaveLogToQueue;

public abstract class AbstractWindowPanel extends Div implements Serializable, EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4717358162081144526L;
	private List<?> lstModel;
	protected AbstractListModel<?> listModel;
	private String strQuery;
	private String strOrderBy;
	private boolean isDisplayLeftPanel = true;
	protected int currentIndex = -1;

	// UI Element
	Div leftPane = new Div();
	Div rightPane = new Div();
	Div leftHeader = null;
	Vlayout vlayout = null;
	Hlayout hlayout;
	Label lb_title = new Label();
	Button bt_add = new Button(CommonDefine.Tittle.TITLE_BTN_ADD_NEW);
	Button bt_edit = new Button(CommonDefine.Tittle.TITLE_BTN_EDIT);
	Button bt_delete = new Button(CommonDefine.Tittle.TITLE_BTN_DELETE);
	Button bt_refresh = new Button(CommonDefine.Tittle.TITLE_BTN_REFRESH);
	Button bt_selectView;
	Grid grid;
	protected Listbox listbox;
	private boolean isUserListbox = true;
	BasicDetailWindow detailWindow = null;
	protected AbstractModel currentModel = null;
	Class<?> modelClass;
	String detailTitle;
	// DUNGNM_START_ADD_05082015
	ArrayList<Textbox> txtTemp;
	ArrayList<Integer> isOnClickCb;
	Boolean isShowAll = false;
	Button btnRestore = new Button();
	ArrayList<Checkbox> cbTemp;
	ArrayList<String> cbTempName;
	ArrayList<Combobox> intCbTemp;
	ArrayList<Datebox> dbTemp;
	ArrayList<String> dbTempName;
	ArrayList<Intbox> inTemp;
	ArrayList<String> inTempName;
	ArrayList<Decimalbox> debTemp;
	ArrayList<String> debTempName;
	ArrayList<Agent> lstTemp;
	ArrayList<String> lstTempName;
	ArrayList<ComboboxSearch> lstM2OSearch;
	HashMap<String, Field> mapSearch = new HashMap<String, Field>();

	int valueSelectDriver = 0;
	int valueSelectAgent = 0;
	Vlayout vlaySearch;
	int countComSearchBasic = 0;
	Cell cellMap;
	StringBuffer querySearch = new StringBuffer();
	// DUNGNM_END_ADD_05082015
	// DUNGNM_START_ADD_07082015
	ArrayList<AdvanceSearchItem> arrListAdvSearchItem = new ArrayList<AdvanceSearchItem>();
	private Editor editor;
	private Component component;
	private SecureRandom random = new SecureRandom();
	Button btnSearch;
	Radiogroup rg;
	Hlayout hlayoutBtn;
	Vlayout vlayoutBtn;
	String query;
	ComboboxThread t;
	TreeMap<String, DropboxAdvanceSearchItem> data2;
	Hlayout hlayCount;
	int i = 0;
	final Desktop desktop = Executions.getCurrent().getDesktop();
	private ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();

	// DUNGNM_END_ADD_07082015

	public AbstractWindowPanel(boolean isUserListbox) {
		this.isUserListbox = isUserListbox;
		beforInit();
		loadData();
		init();
		initLeftPanel();
		initRightPanel(0);
		this.checkPermissionAction();
		// Tuanpa: Set selected index
		if (lstModel != null) {
			if (listModel != null) {
				if (listModel.getSize() > 0) {
					currentIndex = 0;
				} else
					currentIndex = -1;
				goToCurrentRow();
			}
		}
	}

	private void goToCurrentRow() {
		if (currentIndex >= listModel.getSize()) {
			currentIndex = listModel.getSize() - 1;
		}
		if (currentIndex >= 0 && currentIndex < listModel.getSize()) {
			listbox.setSelectedIndex(currentIndex);
		}
	}

	public abstract void initLeftPanel();

	public abstract void initColumns();

	public abstract void loadData();

	public void beforInit() {

	}

	protected void init() {
		this.setVflex("1");
		Hlayout homeLayout = new Hlayout();
		homeLayout.setVflex("1");
		homeLayout.setParent(this);
		leftPane.setParent(homeLayout);
		leftPane.setHflex("1");
		leftPane.setVflex("1");
		leftPane.setVisible(false);
		Div cc = new Div();
		cc = new Div();
		cc.setHflex("true");
		cc.setHflex("4");
		cc.setVflex("1");
		cc.setParent(homeLayout);
		rightPane.setParent(cc);
		rightPane.setVflex("1");
	}

	public void initRightPanel(int i) {
		vlayout = new Vlayout();
		vlayout.setParent(rightPane);
		vlayout.setVflex("1");
		hlayout = new Hlayout();
		hlayout.setParent(vlayout);
		this.initTopRight();
		this.initDetailRightPanel();
		this.bt_add.setVisible(false);
		this.bt_edit.setVisible(false);
		this.bt_delete.setVisible(false);
	}

	public void initDetailRightPanel() {
		int k = 0;
		// initTopRight();
		initColumns();
		// load anotation
		if (modelClass != null) {
			Field[] fields = modelClass.getDeclaredFields();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					try {
						ArrayList<Annotation> ann = new ArrayList<Annotation>(Arrays.asList(field.getAnnotations()));
						if (ann != null) {
							for (int j = 0; j < ann.size(); j++) {
								if (ann.get(j).annotationType().equals(Searchable.class)) {
									if (mapSearch == null) {
										mapSearch = new HashMap<String, Field>();
									}
									mapSearch.put(field.getName(), field);
								}
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		// setupColumns();
		if (k == 0) {
			setupColumns();
			renderGrid();
		} else {
			// drawMaps();
		}

	}

	public void initTopRight() {
		leftHeader = new Div();
		leftHeader.setHflex("true");
		leftHeader.setParent(hlayout);
		// leftHeader.setStyle("background: purple");
		Hlayout rightHeader = new Hlayout();
		rightHeader.setParent(hlayout);

		lb_title.setStyle("margin: 10px; font-size: 20px; font-weight: bold; color: green;");
		lb_title.setParent(leftHeader);

		Div tmpDiv = new Div();
		tmpDiv.setSclass("btn_group");
		tmpDiv.setParent(rightHeader);
		// bt_add.setSclass("btn-success");
		bt_add.setImage("/themes/images/Add_12.png");
		bt_add.setParent(tmpDiv);
		bt_add.addEventListener(Events.ON_CLICK, this);

		tmpDiv = new Div();
		tmpDiv.setSclass("btn_group");
		tmpDiv.setParent(rightHeader);

		// bt_edit.setSclass("btn-danger");
		bt_edit.setParent(tmpDiv);
		bt_edit.setImage("/themes/images/Edit_12.png");
		bt_edit.addEventListener(Events.ON_CLICK, this);
		bt_edit.setDisabled(true);

		tmpDiv = new Div();
		tmpDiv.setSclass("btn_group");
		tmpDiv.setParent(rightHeader);

		// bt_delete.setSclass("btn-danger");
		bt_delete.setParent(tmpDiv);
		bt_delete.setImage("/themes/images/DeleteRed_12.png");
		bt_delete.addEventListener(Events.ON_CLICK, this);
		bt_delete.setDisabled(true);

		tmpDiv = new Div();
		tmpDiv.setParent(rightHeader);
		bt_refresh.setParent(tmpDiv);
		bt_refresh.setStyle("padding-left:10px;");
		bt_refresh.setImage("/themes/images/Refresh_12.png");
		bt_refresh.addEventListener(Events.ON_CLICK, this);
	}

	public void setTitle(String title) {
		lb_title.setValue(title);
	}

	public void setupColumns() {
		if (!isUserListbox) {
			// grid setup
			grid = new Grid();
			// grid.setParent(cellMap);
			grid.setParent(vlayout);
			Columns columns = new Columns();
			columns.setParent(grid);
			Auxhead auxs = new Auxhead();
			auxs.setParent(grid);

			// setup columns
			for (int i = 0; i < lstCols.size(); i++) {

				GridColumn gridCol = lstCols.get(i);

				if (mapSearch.get(gridCol.getFieldName()) != null) {
					Field field = mapSearch.get(gridCol.getFieldName());
					Auxheader aux = new Auxheader();
					aux.setSclass("");
					aux.setColspan(1);
					aux.setParent(auxs);
					Searchable search = field.getAnnotation(Searchable.class);
					createSearchComponent(aux, field.getType(), field.getName(), search);

				} else {
					Auxheader aux = new Auxheader();
					aux.setColspan(1);
					aux.setParent(auxs);
				}
				Column column = new Column(gridCol.getHeader());
				column.setWidth(gridCol.getWidth() + "px");
				column.setParent(columns);
			}
			// Add Edit, Delete column
			// Column column = new Column();
			// column.setHflex("min");
			// column.setParent(columns);
			// column = new Column();
			// column.setHflex("min");
			// column.setParent(columns);

			// setup RowRenderer
			GridRowRenderer<Class<?>> renderer = new GridRowRenderer<Class<?>>(lstCols);
			grid.setRowRenderer(renderer);
		} else {
			listbox = new Listbox();
			listbox.addEventListener(Events.ON_SELECT, this);
			listbox.addEventListener(Events.ON_OK, this);
			listbox.addEventListener(Events.ON_DOUBLE_CLICK, this);
			listbox.setParent(vlayout);
			listbox.setCheckmark(true);
			listbox.setNonselectableTags("");
			listbox.setMold("paging");
			listbox.setAutopaging(true);
			listbox.setVflex(true);
			listbox.setPagingPosition("both");
			Events.echoEvent("focus", listbox, null);
			Listhead head = new Listhead();
			head.setParent(listbox);
			head.setMenupopup("auto");
			head.setSizable(true);
			// setup columns
			Auxhead auxs = new Auxhead();
			auxs.setParent(listbox);
			Listheader header = null;
			Auxheader aux = null;
			for (int i = 0; i < lstCols.size(); i++) {
				GridColumn gridCol = lstCols.get(i);
				header = new Listheader(gridCol.getHeader());
				if (i == lstCols.size() - 1 && i < 5) {
					header.setHflex("1");
				} else {
					header.setWidth(gridCol.getWidth() + "px");
				}
				header.setStyle("text-align:center;");
				header.setParent(head);
				aux = new Auxheader();
				aux.setParent(auxs);
				if (mapSearch.get(gridCol.getFieldName()) != null) {
					Field field = mapSearch.get(gridCol.getFieldName());
					Searchable search = field.getAnnotation(Searchable.class);
					aux.appendChild(new Image("./themes/images/filter.png"));
					createSearchComponent(aux, field.getType(), field.getName(), search);
				}
			}

			ListItemRenderer<Class<?>> renderer = new ListItemRenderer<Class<?>>(lstCols);
			listbox.setItemRenderer(renderer);
			// ((AbstractComponent)
			// listbox.getItems()).addEventListener(Events.ON_DOUBLE_CLICK,
			// this);
		}
	}

	public String getStrOrderBy() {
		return strOrderBy;
	}

	public void setStrOrderBy(String strOrderBy) {
		this.strOrderBy = strOrderBy;
	}

	@SuppressWarnings("rawtypes")
	public void renderGrid() {
		try {
			if (strQuery != null && !strQuery.isEmpty()) {
				listModel = new PagingListModel(modelClass.getName(), strQuery, strOrderBy);
			} else {
				listModel = new PagingListModel(modelClass.getName());
			}
			if (!isUserListbox) {
				grid.setModel(listModel);
			} else {
				listbox.setModel(listModel);
			}
			setLstModel(((PagingListModel) listModel).getCache());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@SuppressWarnings("rawtypes")
	public void renderGrid(String query) {
		listModel = new PagingListModel(modelClass.getName(), query, strOrderBy);
		if (!isUserListbox) {
			grid.setModel(listModel);
		} else {
			listbox.setModel(listModel);
		}

		setLstModel(((PagingListModel) listModel).getCache());
	}

	public Desktop getDesktop() {
		return desktop;
	}

	private BasicDetailWindow getDetailWindow() {
		detailWindow = modifyDetailWindow();
		if (detailWindow == null) {
			detailWindow = new BasicDetailWindow(currentModel, this);
			detailWindow.setTitle(detailTitle);
			// detailWindow.setParent(Env.getMainApp().getDiv_content().getParent());
		}
		// detailWindow.setParent(this.getRoot());
		detailWindow.setParent(this);
		detailWindow.setVisible(false);
		return detailWindow;
	}

	public void showDetail(AbstractModel currentModel) {
		this.currentModel = currentModel;
		getDetailWindow().setModel(currentModel);
		setCustomValueDetailWindow();
		// habv - reload address of customer
		if (getDetailWindow() instanceof CustomersDetail) {
			CustomersDetail cusDetail = (CustomersDetail) getDetailWindow();
			Customer customer = (Customer) this.currentModel;
			cusDetail.getCbAdd1().setValue(customer.getAddress());
			if (customer.getAddressLat() != null && customer.getAddressLng() != null) {
				cusDetail.getCbAdd1().setAttribute(CustomersDetail.ATT_LOCATION,
						new LatLng(customer.getAddressLat(), customer.getAddressLng()));
			} else
				cusDetail.getCbAdd1().setAttribute(CustomersDetail.ATT_LOCATION, new LatLng(-1, -1));

			cusDetail.getCbAdd2().setValue(customer.getAddress2());
			if (customer.getAddress2Lat() != null && customer.getAddress2Lng() != null) {
				cusDetail.getCbAdd2().setAttribute(CustomersDetail.ATT_LOCATION,
						new LatLng(customer.getAddress2Lat(), customer.getAddress2Lng()));
			} else
				cusDetail.getCbAdd2().setAttribute(CustomersDetail.ATT_LOCATION, new LatLng(-1, -1));

			cusDetail.getCbAdd3().setValue(customer.getAddress3());
			if (customer.getAddress3Lat() != null && customer.getAddress3Lng() != null) {
				cusDetail.getCbAdd3().setAttribute(CustomersDetail.ATT_LOCATION,
						new LatLng(customer.getAddress3Lat(), customer.getAddress3Lng()));
			} else
				cusDetail.getCbAdd3().setAttribute(CustomersDetail.ATT_LOCATION, new LatLng(-1, -1));
		} // end add - habv
		getDetailWindow().doModal();
		// Events.echoEvent(Events.ON_MODAL, getDetailWindow(), null);
	}

	public void setCustomValueDetailWindow() {
	}

	public void refresh() {
		renderGrid();
		currentModel = null;
		currentIndex = 0;
		bt_delete.setDisabled(true);
		bt_edit.setDisabled(true);
		setFocusForListbox();
		// goToCurrentRow();
	}

	protected void setFocusForListbox() {
		Events.echoEvent("focus", listbox, null);
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(bt_refresh)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				refresh();
			}
		}
		if (event.getTarget().equals(bt_add)) {
			if (this.bt_add.isVisible()) {
				currentModel = createNewModel();
				showDetail(currentModel);
				// currentModel = createNewModel();
				// showDetail(currentModel);
			}
		} else if (event.getTarget().equals(bt_delete)) {
			if (this.bt_delete.isVisible()) {
				if (currentModel != null) {
					Messagebox.show("Bạn có muốn chắc chắn xoá bản ghi ?", "Xác nhận xóa",
							new Messagebox.Button[] { Messagebox.Button.OK, Messagebox.Button.CANCEL },
							Messagebox.QUESTION, new EventListener<Messagebox.ClickEvent>() {

								@Override
								public void onEvent(ClickEvent event) throws Exception {
									try {
										if (event.getButton() != null
												&& event.getButton().equals(Messagebox.Button.OK)) {
											handleEventDelete();
											return;
										}
									} catch (Exception e) {
										Env.getHomePage().showNotificationErrorSelect(
												"Không thể xóa vì bản ghi bạn chọn đã có liên kết với dữ liệu ở danh mục khác ! ",
												Clients.NOTIFICATION_TYPE_INFO);
									}
								}
							});
				} else {
					Env.getHomePage().showNotification("Phải chọn bản ghi cần xoá !",
							Clients.NOTIFICATION_TYPE_WARNING);
				}
			}
		} else if (event.getName().equals(Events.ON_SELECT) && event.getTarget().equals(listbox)) {
			currentModel = (AbstractModel) listModel.getElementAt(listbox.getSelectedIndex());
			currentIndex = listbox.getSelectedIndex();
			bt_delete.setDisabled(false);
			bt_edit.setDisabled(false);
		} else if (((event.getName().equals(Events.ON_OK) || event.getName().equals(Events.ON_DOUBLE_CLICK))
				&& event.getTarget().equals(listbox) || event.getTarget().equals(bt_edit))) {
			if (this.bt_edit.isVisible()) {
				if (listbox.getSelectedIndex() >= 0) {
					currentModel = (AbstractModel) listModel.getElementAt(listbox.getSelectedIndex());
					currentIndex = listbox.getSelectedIndex();
					if (currentModel != null) {
						showDetail(currentModel);
					}
				}
			}
		}
	}

	public boolean handleEventDelete() {
		try {
			SaveLogToQueue savelog = new SaveLogToQueue(currentModel, EnumUserAction.DELETE, Env.getHomePage().getCurrentFunction(), Env.getUserID());
			savelog.start();
			currentModel.delete();
			refresh();
			Env.getHomePage().showNotification("Đã xoá bản ghi !", Clients.NOTIFICATION_TYPE_INFO);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// DUNGNM_START_END_17082015
	/**
	 * ADVANCE SEARCH Create Panel contain all item of Advance Search
	 * 
	 * @param vLaySearch
	 */
	public void createPanel(Vlayout vLaySearch, TreeMap<String, DropboxAdvanceSearchItem> data) {
		Panel paneAdvance = new Panel();
		paneAdvance.setVflex("2");
		paneAdvance.setHflex("true");
		paneAdvance
				.setTitle(LocalizationUtils.getString("advance.search.title", ConstantValueSearch.CAN_NOT_FOUND_TITLE));
		paneAdvance.setVisible(true);
		paneAdvance.setParent(vLaySearch);
		paneAdvance.setSclass("panel-success");
		paneAdvance.setVflex("1");
		paneAdvance.setHflex("true");
		paneAdvance.setClosable(true);
		Panelchildren childAdvance2 = new Panelchildren();
		childAdvance2.setStyle("overflow:auto");
		paneAdvance.appendChild(childAdvance2);
		createContent(childAdvance2, data);
	}

	/**
	 * ADVANCE SEARCH Create advance search item
	 * 
	 * @param panelchild
	 */
	private void createContent(final Panelchildren panelchild, final TreeMap<String, DropboxAdvanceSearchItem> data) {
		data2 = data;
		boolean isRoot = false;
		final Combobox cbb = new Combobox();
		// AdvanceSearchItem tempAdvSearchItem = new AdvanceSearchItem();
		final Vlayout vlayoutAd = new Vlayout();
		// Set id for vlayout ( it is also key of arrListAdvSearchItem )
		vlayoutAd.setId(nextSessionId());
		vlayoutAd.setParent(panelchild);
		final AdvanceSearchItem tmpAdvanceSearchItem = new AdvanceSearchItem();
		tmpAdvanceSearchItem.setVlayout(vlayoutAd);
		int count = 0;
		if (arrListAdvSearchItem != null) {
			for (int i = 0; i < arrListAdvSearchItem.size(); i++) {
				if (arrListAdvSearchItem.get(i).getStatus() == AdvanceSearchItem.ITEM_ADD)
					count++;
			}
			if (count < 1)
				isRoot = true;
			if (!isRoot) {
				// Display radio box to chosen condition AND - OR
				tmpAdvanceSearchItem.setRgAdvanceSearchItem(
						createRadioGroup(panelchild, vlayoutAd, data, AdvanceSearchItem.INT_AND, true));
			} else {
				// Disable radio box to chosen condition AND - OR
				tmpAdvanceSearchItem.setType(AdvanceSearchItem.CONDITION_ROOT);
				tmpAdvanceSearchItem.setRgAdvanceSearchItem(
						createRadioGroup(panelchild, vlayoutAd, data, AdvanceSearchItem.INT_AND, false));
			}
		}
		// Create and fill data for COMBOBOX
		cbb.setStyle("margin: 10px 0px 0px 0px; font-size: 14px; width:100%; font-style:normal; ");
		if (data != null) {
			for (String key : data.keySet()) {
				cbb.appendItem(key);
			}
		}
		cbb.setParent(vlayoutAd);
		tmpAdvanceSearchItem.setCbb(cbb);
		hlayoutBtn = new Hlayout();
		hlayoutBtn.setParent(vlayoutAd);
		// Display button DELETE for advance search item is add
		if (!isRoot)
			tmpAdvanceSearchItem
					.setBtnDel(createButtonDel(panelchild, tmpAdvanceSearchItem, vlayoutAd, hlayoutBtn, data, true));

		// Event on change of combobox
		cbb.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				DropboxAdvanceSearchItem drSearchItem = new DropboxAdvanceSearchItem();
				if (cbb != null && data != null && cbb.getSelectedItem() != null) {
					drSearchItem = data.get(cbb.getSelectedItem().getLabel());
					Messagebox.show(cbb.getSelectedItem().getLabel() + " : " + drSearchItem.getValue());
					String placehoder = drSearchItem.getKey().toLowerCase();
					// Check exist item in array list
					if (!arrListAdvSearchItem.contains(tmpAdvanceSearchItem)) {
						tmpAdvanceSearchItem.setStrKey(drSearchItem.getValue());
						vlayoutAd.removeChild(hlayoutBtn);
						editor = EditorFactory.getEditor(cbb.getSelectedItem(), drSearchItem.getClazz(),
								drSearchItem.getValue(), placehoder);
						if (editor != null) {
							component = editor.getComponent();
							component.setParent(vlayoutAd);
							tmpAdvanceSearchItem.setComponent(component);
						}
						tmpAdvanceSearchItem.setClazz(drSearchItem.getClazz());
						tmpAdvanceSearchItem.setEditor(editor);
						hlayoutBtn = new Hlayout();
						hlayoutBtn.setParent(vlayoutAd);
						tmpAdvanceSearchItem.setStatus(AdvanceSearchItem.ITEM_ADD);
						tmpAdvanceSearchItem.setBtnAdd(createButtonAdd(panelchild, vlayoutAd, hlayoutBtn, data, true));
						if (arrListAdvSearchItem.size() < 1) {
							tmpAdvanceSearchItem.setBtnDel(createButtonDel(panelchild, tmpAdvanceSearchItem, vlayoutAd,
									hlayoutBtn, data, false));
						} else {
							tmpAdvanceSearchItem.setBtnDel(createButtonDel(panelchild, tmpAdvanceSearchItem, vlayoutAd,
									hlayoutBtn, data, true));
						}
						arrListAdvSearchItem.add(tmpAdvanceSearchItem);
					} else {
						arrListAdvSearchItem.get(arrListAdvSearchItem.indexOf(tmpAdvanceSearchItem))
								.setStrKey(drSearchItem.getValue());
						editor = EditorFactory.getEditor(cbb.getSelectedItem(), drSearchItem.getClazz(),
								drSearchItem.getValue(), placehoder);
						boolean oldStatusBtnAdd = arrListAdvSearchItem
								.get(arrListAdvSearchItem.indexOf(tmpAdvanceSearchItem)).getBtnAdd().isVisible();
						boolean oldStatusBtnDel = arrListAdvSearchItem
								.get(arrListAdvSearchItem.indexOf(tmpAdvanceSearchItem)).getBtnDel().isVisible();
						if (editor != null) {
							vlayoutAd.removeChild(arrListAdvSearchItem
									.get(arrListAdvSearchItem.indexOf(tmpAdvanceSearchItem)).getComponent());
							vlayoutAd.removeChild(hlayoutBtn);
							component = editor.getComponent();
							component.setParent(vlayoutAd);
							arrListAdvSearchItem.get(arrListAdvSearchItem.indexOf(tmpAdvanceSearchItem))
									.setComponent(component);
						}
						arrListAdvSearchItem.get(arrListAdvSearchItem.indexOf(tmpAdvanceSearchItem)).setEditor(editor);
						arrListAdvSearchItem.get(arrListAdvSearchItem.indexOf(tmpAdvanceSearchItem))
								.setClazz(drSearchItem.getClazz());
						hlayoutBtn = new Hlayout();
						hlayoutBtn.setParent(vlayoutAd);
						arrListAdvSearchItem.get(arrListAdvSearchItem.indexOf(tmpAdvanceSearchItem))
								.setBtnAdd(createButtonAdd(panelchild, vlayoutAd, hlayoutBtn, data, oldStatusBtnAdd));
						arrListAdvSearchItem.get(arrListAdvSearchItem.indexOf(tmpAdvanceSearchItem)).setBtnDel(
								createButtonDel(panelchild, null, vlayoutAd, hlayoutBtn, data, oldStatusBtnDel));
					}
				}
			}
		});
		// Events ON_CHANGING
		cbb.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>() {
			@Override
			public void onEvent(InputEvent arg0) throws Exception {
				InputEvent inputEvent = (InputEvent) arg0;
				String inputText = inputEvent.getValue();
				cbb.getChildren().clear();
				t = new ComboboxThread(inputText, data2);
				if (t.getResult() != null) {
					for (String key : t.getResult().keySet()) {
						cbb.appendItem(key);
					}
				}
				cbb.setAutodrop(true);
				cbb.open();
			}
		});
		// Display button search
		if (btnSearch != null) {
			panelchild.removeChild(btnSearch);
		}
		createButtonSearch(panelchild);

	}

	public Button createButtonAdd(final Panelchildren panelchild, final Vlayout vlayout, Hlayout hlayout,
			final TreeMap<String, DropboxAdvanceSearchItem> data, boolean oldStatus) {
		Button btnAdd = new Button();
		btnAdd.setImage("/themes/images/add-icon.png");
		btnAdd.setStyle("margin: 10px 0px 1px 0px; width:25px; height:25px;");
		btnAdd.setParent(hlayout);
		btnAdd.setVisible(oldStatus);
		btnAdd.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				AdvanceSearchItem adTemp = new AdvanceSearchItem();
				adTemp.setVlayout(vlayout);
				arrListAdvSearchItem.get(arrListAdvSearchItem.indexOf(adTemp)).getBtnAdd().setVisible(false);
				createContent(panelchild, data);
			}
		});
		return btnAdd;
	}

	public Button createButtonSearch(Panelchildren vlayout) {
		btnSearch = new Button();
		btnSearch.setImage("/themes/images/search-icon.gif");
		btnSearch.setStyle("margin: 10px 0px 1px 0px; width:25px; height:25px;");
		btnSearch.setParent(vlayout);
		btnSearch.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				StringBuffer strBuffQuery = new StringBuffer();
				// strBuffQuery.append("FROM " + modelClass.getName());
				for (int i = 0; i < arrListAdvSearchItem.size(); i++) {
					AdvanceSearchItem adTmp = new AdvanceSearchItem();
					adTmp = arrListAdvSearchItem.get(i);
					if (adTmp.getStatus() == AdvanceSearchItem.ITEM_ADD) {
						if (i == 0) {
							strBuffQuery.append("AND " + adTmp.getStrKey() + " ");
						} else {
							if (adTmp.getRgAdvanceSearchItem().getValue() == AdvanceSearchItem.INT_AND) {
								strBuffQuery.append("AND " + adTmp.getStrKey() + " ");
							} else {
								strBuffQuery.append("OR " + adTmp.getStrKey() + " ");
							}
						}
						if (adTmp.getClazz().equals(String.class)) {
							strBuffQuery.append("LIKE '%" + adTmp.getEditor().getValue().toString() + "%' ");
						} else if (adTmp.getClazz().equals(Timestamp.class)) {
							String strDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
									.format((Date) adTmp.getEditor().getValue());
							strBuffQuery.append("= " + strDate.toString() + " ");
						} else if (adTmp.getClazz().equals(Integer.class) || adTmp.getClazz().equals(int.class)) {
							strBuffQuery.append("= " + Integer.parseInt(adTmp.getEditor().getValue().toString()) + " ");
						} else if (adTmp.getClazz().equals(BigDecimal.class)) {
							DecimalFormat nf = (DecimalFormat) NumberFormat.getInstance();
							nf.setParseBigDecimal(true);
							BigDecimal bd = (BigDecimal) nf.parse(adTmp.getEditor().getValue().toString(),
									new ParsePosition(0));
							strBuffQuery.append("= " + bd + " ");
						} else if (adTmp.getClazz().equals(Boolean.class)) {
							strBuffQuery.append("= " + adTmp.getEditor().getValue().toString() + " ");
						}
					}
				}
				searchData(strBuffQuery.toString());
			}
		});
		return btnSearch;
	}

	public RadioGroupAdvanceSearchItem createRadioGroup(Panelchildren panelchild, final Vlayout vlayoutAd,
			TreeMap<String, DropboxAdvanceSearchItem> data, int oldChecked, boolean oldStatus) {
		final RadioGroupAdvanceSearchItem rgAdvanceSearhItem = new RadioGroupAdvanceSearchItem();
		Grid grid = new Grid();
		grid.setVisible(oldStatus);
		grid.setStyle("margin: 10px 0px 1px 0px;");
		rg = new Radiogroup();
		grid.setParent(vlayoutAd);
		rgAdvanceSearhItem.setGrid(grid);
		// Build a rows component
		Rows rows = new Rows();
		rows.setParent(grid);
		// Build the rows
		Row row1 = new Row();
		row1.setParent(rows);
		final Radio rb1 = new Radio("AND");
		rb1.setRadiogroup(rg);
		if (oldChecked == AdvanceSearchItem.INT_AND) {
			rb1.setChecked(true);
		} else {
			rb1.setChecked(false);
		}
		row1.appendChild(rb1);

		final Radio rb2 = new Radio("OR");
		rb2.setRadiogroup(rg);
		if (oldChecked == AdvanceSearchItem.INT_OR) {
			rb2.setChecked(true);
		} else {
			rb2.setChecked(false);
		}
		row1.appendChild(rb2);
		rg.setStyle("margin: 10px 0px 1px 0px; width:25px; height:25px;");
		rg.setParent(vlayoutAd);
		rg.setSelectedItem(rb1);
		rg.setVisible(oldStatus);
		rgAdvanceSearhItem.setRg(rg);
		rgAdvanceSearhItem.setValue(AdvanceSearchItem.INT_AND);
		rg.addEventListener(Events.ON_CHECK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				AdvanceSearchItem adTemp = new AdvanceSearchItem();
				adTemp.setVlayout(vlayoutAd);

				if (rb1.isChecked()) {
					rb2.setChecked(false);
					rgAdvanceSearhItem.setValue(AdvanceSearchItem.INT_AND);
				} else {
					rb2.setChecked(true);
					rgAdvanceSearhItem.setValue(AdvanceSearchItem.INT_OR);
				}
				if (arrListAdvSearchItem.contains(adTemp)) {
					arrListAdvSearchItem.get(arrListAdvSearchItem.indexOf(adTemp))
							.setRgAdvanceSearchItem(rgAdvanceSearhItem);
				}
			}
		});
		return rgAdvanceSearhItem;
	}

	public Button createButtonDel(Panelchildren panelchild, final AdvanceSearchItem advanceSearchItem,
			final Vlayout vlayoutAd, final Hlayout hlayout, TreeMap<String, DropboxAdvanceSearchItem> data,
			boolean oldStatus) {
		Button btnDel = new Button();
		btnDel.setImage("/themes/images/del-icon.png");
		btnDel.setStyle("margin: 10px 0px 1px 0px; width:25px; height:25px;");
		btnDel.setParent(hlayout);
		btnDel.setVisible(oldStatus);
		btnDel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				AdvanceSearchItem tmpAdvanceSearchItem = new AdvanceSearchItem();
				tmpAdvanceSearchItem.setVlayout(vlayoutAd);
				if (arrListAdvSearchItem.contains(tmpAdvanceSearchItem)) {
					int index = arrListAdvSearchItem.indexOf(tmpAdvanceSearchItem);
					arrListAdvSearchItem.get(index).setStatus(AdvanceSearchItem.ITEM_DEL);
					// Remove item
					vlayoutAd.removeChild(arrListAdvSearchItem.get(index).getComponent());
					vlayoutAd.removeChild(hlayout);
					vlayoutAd.removeChild(arrListAdvSearchItem.get(index).getCbb());
					vlayoutAd.removeChild(arrListAdvSearchItem.get(index).getRgAdvanceSearchItem().getGrid());
					vlayoutAd.removeChild(arrListAdvSearchItem.get(index).getRgAdvanceSearchItem().getRg());
				} else {
					vlayoutAd.removeChild(hlayout);
					vlayoutAd.removeChild(advanceSearchItem.getCbb());
					vlayoutAd.removeChild(advanceSearchItem.getRgAdvanceSearchItem().getGrid());
				}
				for (int i = arrListAdvSearchItem.size() - 1; i >= 0; i--) {
					if (arrListAdvSearchItem.get(i).getStatus() == AdvanceSearchItem.ITEM_ADD) {
						arrListAdvSearchItem.get(i).getBtnAdd().setVisible(true);
						break;
					}
				}
			}
		});
		return btnDel;
	}

	public String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	// DUNGNM_START_ADD_04082015
	public void createSearchComponent(Auxheader panelChildren, Class<?> type, String name, Searchable search) {
		if (search.typevariable().equals("Boolean") || type.equals(Boolean.class)) {
			if (intCbTemp == null) {
				intCbTemp = new ArrayList<Combobox>();
			}
			final Combobox cb = new Combobox();
			cb.setHflex("true");
			cb.setSclass("z-combobox-search");
			cb.setAttribute("fieldSearch", name);
			Comboitem item = new Comboitem();
			item.setLabel("Chọn");
			item.setValue(1);
			cb.appendChild(item);
			item = new Comboitem();
			item.setLabel("không Chọn");
			item.setValue(0);
			cb.appendChild(item);
			item = new Comboitem();
			item.setLabel("Tất cả");
			item.setValue(3);
			cb.appendChild(item);
			cb.setPlaceholder(search.placehoder());
			cb.setStyle("margin-left: 5px; font-size: 14px; width:85%; font-style:normal; ");
			intCbTemp.add(cb);
			cb.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					prepareSearchData();
				};
			});
			panelChildren.setStyle("text-align: center;");
			cb.setParent(panelChildren);
		} else if (type.equals(String.class)) {
			if (txtTemp == null) {
				txtTemp = new ArrayList<Textbox>();
			}
			final Textbox txtb = new Textbox();
			txtb.setErrorboxSclass("z-error-search");
			txtb.setSclass("z-textbox-search");
			txtb.setPlaceholder(search.placehoder());
			txtb.setConstraint(new EventNumberConstraint());
			txtb.setAttribute("fieldSearch", name);
			txtTemp.add(txtb);
			txtb.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					InputEvent inputEvent = (InputEvent) event;
					String dataFind = inputEvent.getValue();
					txtTemp.get(txtTemp.indexOf(event.getTarget())).setAttribute("dataFind", dataFind);
					prepareSearchData();
				};
			});
			txtb.setParent(panelChildren);
		}
		/*
		 * else if (type.equals(Boolean.class)) { if (cbTemp == null) { cbTemp =
		 * new ArrayList<Checkbox>(); cbTempName = new ArrayList<String>();
		 * isOnClickCb = new ArrayList<Integer>(); } Checkbox cb = new
		 * Checkbox(); cb.setStyle(
		 * "margin: 10px 0px 10px 0px; font-size: 14px;font-style:normal; ");
		 * cbTempName.add(name); cbTemp.add(cb); isOnClickCb.add(0);
		 * setCbTemp(cbTemp); setCbTempName(cbTempName);
		 * cb.addEventListener(Events.ON_CHECK, new EventListener<Event>() {
		 * 
		 * @Override public void onEvent(Event event) throws Exception {
		 * prepareSearchData(); }; }); cb.setParent(panelChildren); }
		 */else if (type.equals(Timestamp.class)) {
			if (dbTemp == null) {
				dbTemp = new ArrayList<Datebox>();
				dbTempName = new ArrayList<String>();
			}
			Datebox db = new Datebox();
			db.setFormat("yyyy-MM-dd");
			db.setStyle("font-size: 14px; width:85%; font-style:normal; ");
			db.setPlaceholder(search.placehoder());
			db.setHeight("25px");
			dbTempName.add(name);
			dbTemp.add(db);
			setDbTemp(dbTemp);
			setDbTempName(dbTempName);
			db.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					prepareSearchData();
				};
			});
			db.setParent(panelChildren);
		} else if ((type.equals(Integer.class) || type.equals(int.class))) {
			if (inTemp == null) {
				inTemp = new ArrayList<Intbox>();
				inTempName = new ArrayList<String>();
			}
			final Intbox in = new Intbox();
			// in.setId("in" + name);
			in.setSclass("z-textbox-search");
			in.setPlaceholder(search.placehoder());
			in.setHeight("25px");
			inTempName.add(name);
			inTemp.add(in);
			setInTemp(inTemp);
			setInTempName(inTempName);
			in.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					InputEvent inputEvent = (InputEvent) event;
					String dataFind = inputEvent.getValue();
					if (!StringUtils.isEmpty(dataFind)) {
						if (StringUtils.isInteger(dataFind)) {
							inTemp.get(inTemp.indexOf(in)).setAttribute("dataFind", dataFind);
						}
					} else {
						inTemp.get(inTemp.indexOf(in)).setAttribute("dataFind", null);
						inTemp.get(inTemp.indexOf(in)).setValue(null);
					}

					prepareSearchData();
				};
			});
			in.setParent(panelChildren);
		} else if (type.equals(BigDecimal.class)) {
			if (debTemp == null) {
				debTemp = new ArrayList<Decimalbox>();
				debTempName = new ArrayList<String>();
			}
			final Decimalbox dc = new Decimalbox();
			dc.setStyle("margin: 10px 0px 10px 0px; font-size: 14px; width:100%; font-style:normal; ");
			dc.setPlaceholder(search.placehoder());
			dc.setHeight("25px");
			debTempName.add(name);
			debTemp.add(dc);
			setDebTemp(debTemp);
			setDebTempName(debTempName);
			dc.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					InputEvent inputEvent = (InputEvent) event;
					String dataFind = inputEvent.getValue();
					DecimalFormat pf = new DecimalFormat("0.0");
					debTemp.get(debTemp.indexOf(dc)).setAttribute("dataFind", (BigDecimal) pf.parse(dataFind));
					prepareSearchData();
				};
			});
			dc.setParent(panelChildren);
		} else if (type.getName().contains("com.vietek.taxioperation.model")) {
			String contenquery = " From " + type.getName();
			final ComboboxSearch comp = new ComboboxSearch(type, contenquery);
			comp.setHflex("true");
			comp.setSclass("z-combobox-search");
			comp.setPlaceholder(search.placehoder());
			if (lstM2OSearch == null) {
				lstM2OSearch = new ArrayList<ComboboxSearch>();
			}
			comp.setAttribute("fieldSearch", name);
			lstM2OSearch.add(comp);
			comp.addEventListener(ComboboxSearch.EVENT_ON_SELECT_VALUE, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					AppLogger.logDebug.info(arg0.getName());
					prepareSearchData();
				}
			});
			comp.setParent(panelChildren);
		}
		// else if (type.equals(Agent.class)) {
		// List<Agent> result =
		// Agent.getAgentListByUser(Env.getContext(Env.USER_NAME));
		//
		// String[] titles = new String[result.size()];
		// int[] values = new int[result.size()];
		// int count = 0;
		// for (@SuppressWarnings("rawtypes")
		// Iterator iterator = result.iterator(); iterator.hasNext();) {
		// Agent obj = (Agent) iterator.next();
		// titles[count] = obj.getAgentName();
		// values[count] = obj.getId();
		// count++;
		// }
		//
		// String styles = "line-height: 20px !important; font-size: 12px
		// !important; padding: 3px;";
		// String sClass = "cus-agent";
		//
		// ComboboxRender comborender = new ComboboxRender();
		// Combobox combo = comborender.ComboboxRendering(titles, values,
		// styles, sClass, 200, 0, true);
		//
		// combo.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
		// @Override
		// public void onEvent(Event event) throws Exception {
		// valueSelectAgent = (int) combo.getSelectedItem().getValue();
		// prepareSearchData();
		// };
		// });
		// panelChildren.appendChild(combo);
		// }
	}

	public void createSearchComponent(Panelchildren panelChildren) {
		List<CommonObjSearch> lstDriverStatus = new ArrayList<CommonObjSearch>();
		CommonObjSearch bean1 = new CommonObjSearch();
		bean1.setTitle("-- Tất cả --");
		bean1.setValue(0);
		lstDriverStatus.add(bean1);
		CommonObjSearch bean2 = new CommonObjSearch();
		bean2.setTitle("-- Đã dùng app --");
		bean2.setValue(1);
		lstDriverStatus.add(bean2);
		CommonObjSearch bean3 = new CommonObjSearch();
		bean3.setTitle("-- Chưa dùng app --");
		bean3.setValue(2);
		lstDriverStatus.add(bean3);
		Combobox comb = new Combobox();
		comb.setStyle("line-height: 24px !important;font-size: 12px !important;padding:3px;");
		comb.setItemRenderer(new ComboitemRenderer<CommonObjSearch>() {
			@Override
			public void render(final Comboitem paramComboitem, CommonObjSearch bean, int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				if (bean.getValue() == 0) {
					comb.setSelectedItem(paramComboitem);
				}
			}
		});
		comb.setModel(new ListModelList<CommonObjSearch>(lstDriverStatus));
		comb.setWidth("200px");
		comb.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				valueSelectDriver = (int) comb.getSelectedItem().getValue();
				prepareSearchData();
			};
		});
		panelChildren.appendChild(comb);
	}

	/*
	 * 20160327 - dungnd: rename DriverSearch --> CommonObjSearch
	 */
	class CommonObjSearch {
		private String title;
		private int value;

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

	protected String prepareQuerySearch() {
		StringBuffer query = new StringBuffer();
		query.append("FROM " + modelClass.getName());
		StringBuffer whereClause = new StringBuffer(" WHERE 1 = 1 ");
		// Text box
		if (txtTemp != null) {
			for (int i = 0; i < txtTemp.size(); i++) {
				if (txtTemp.get(i) != null && txtTemp.get(i).getValue() != null
						&& !("").equals(txtTemp.get(i).getAttribute("dataFind"))
						&& txtTemp.get(i).getAttribute("dataFind") != null) {
					if (whereClause.toString().length() > 0) {
						whereClause.append(" AND ");
					}
					whereClause.append(txtTemp.get(i).getAttribute("fieldSearch") + " LIKE '%"
							+ txtTemp.get(i).getAttribute("dataFind") + "%'");
				}
			}
		}

		// Check box
		// if (cbTemp != null) {
		// for (int i = 0; i < cbTemp.size(); i++) {
		// if (whereClause.toString().length() > 0) {
		// whereClause.append(" AND ");
		// }
		// whereClause.append(cbTempName.get(i) + " ="
		// + cbTemp.get(i).isChecked());
		// }
		// }
		/*-- user for search Integer as boolean value --*/
		if (intCbTemp != null) {
			for (int i = 0; i < intCbTemp.size(); i++) {
				if (intCbTemp.get(i).getSelectedItem() != null) {
					Object selected = intCbTemp.get(i).getSelectedItem().getValue();
					if ((Integer) selected < 3) {
						if (whereClause.toString().length() > 0) {
							whereClause.append(" AND ");
						}
						whereClause.append(intCbTemp.get(i).getAttribute("fieldSearch") + " =" + selected);
					}
				}
			}
		}

		/*-- user for search DateTime value --*/
		if (dbTemp != null) {
			for (int i = 0; i < dbTemp.size(); i++) {
				if (dbTemp.get(i).getValue() != null && !("").equals(dbTemp.get(i))) {

					if (whereClause.toString().length() > 0) {
						whereClause.append(" AND ");
					}
					String strDate = new SimpleDateFormat("yyyy-MM-dd").format(dbTemp.get(i).getValue());
					whereClause.append("str(" +dbTempName.get(i)+ ")" + "like" + "'%" + strDate + "%'");
				}
			}
		}

		/*-- user for search integer value --*/
		if (inTemp != null) {
			for (int i = 0; i < inTemp.size(); i++) {
				if (inTemp.get(i).getAttribute("dataFind") != null
						&& !("").equals(inTemp.get(i).getAttribute("dataFind"))) {
					if (whereClause.toString().length() > 0) {
						whereClause.append(" AND ");
					}
					whereClause.append(inTempName.get(i) + " = " + inTemp.get(i).getAttribute("dataFind"));
				}
			}

			/*-- user for search double value --*/
			if (debTemp != null) {
				for (int i = 0; i < debTemp.size(); i++) {
					if (debTemp.get(i).getAttribute("dataFind") != null
							&& !("").equals(debTemp.get(i).getAttribute("dataFind"))) {
						if (whereClause.toString().length() > 0) {
							whereClause.append(" AND ");
						}
						whereClause.append(debTempName.get(i) + " =" + debTemp.get(i).getAttribute("dataFind"));
					}
				}
			}

		}
		/*-- user for search link many to one --*/
		if (lstM2OSearch != null) {
			for (int i = 0; i < lstM2OSearch.size(); i++) {
				if (lstM2OSearch.get(i) != null) {
					if (lstM2OSearch.get(i).getSelectedItem() != null) {
						AbstractModel value = (AbstractModel) lstM2OSearch.get(i).getSelectedItem().getValue();
						if (whereClause.toString().length() > 0) {
							whereClause.append(" AND ");
						}
						whereClause
								.append(lstM2OSearch.get(i).getAttribute("fieldSearch") + ".id" + " =" + value.getId());
					}
				}

			}

		}
		/*-- Use for search of taxi order 
		if (StringUtils.equals(modelClass.getName(),
				TaxiOrder.class.getName())) {
			StringBuffer str = new StringBuffer();
			DateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date = new Date(System.currentTimeMillis()
					- (45l * 60l * 1000l));
			str.append(" and orderTime >= '")
					.append(dateFormat.format(date))
					.append("' and status in (" + EnumStatus.MOI.getValue()
							+ "," + EnumStatus.DA_DOC_DAM.getValue() + ","
							+ EnumStatus.XE_DANG_KY_DON.getValue() + ")")
					.append("and createBy=" + Env.getUserID());
			query.append(str.toString());
		}
		--*/

		if (whereClause.toString().length() > 0) {
			query.append(whereClause.toString());
		}

		return query.toString();

	}

	protected void prepareSearchData() {

		if (btnRestore != null && isShowAll) {
			isOnClickCb = null;
			isShowAll = false;
		} else {
			strQuery = prepareQuerySearch();
			loadDataGrid();
			Thread t = new Thread(new reloadGrid());
			t.start();
		}
	}

	public void searchData(String query) {
		this.renderGrid(query);
	}

	class reloadGrid implements Runnable {
		@SuppressWarnings("rawtypes")
		@Override
		public void run() {
			setLstModel(((PagingListModel) listModel).getCache());
		}

	}

	@SuppressWarnings("rawtypes")
	public void loadDataGrid() {
		if (strQuery != null && !strQuery.isEmpty()) {
			listModel = new PagingListModel(modelClass.getName(), strQuery, strOrderBy);
		} else {
			listModel = new PagingListModel(modelClass.getName());
		}
		if (!isUserListbox) {
			grid.setModel(listModel);
		} else {
			listbox.setModel(listModel);
		}
	}

	protected void initLeftPanelTmp() {
		// set display left panel
		vlayout = new Vlayout();
		vlayout.setHflex("true");
		vlayout.setVflex("1.5");
		vlayout.setParent(leftPane);
		Hlayout hl = new Hlayout();
		hl.setParent(vlayout);
		setVlaySearch(vlayout);
		this.getLeftPane().setVisible(true);
		Panel paneBasicSearch = new Panel();
		paneBasicSearch.setParent(hl);
		paneBasicSearch.setSclass("panel-success");
		paneBasicSearch.setVflex("1");
		paneBasicSearch.setHflex("true");

		paneBasicSearch
				.setTitle(LocalizationUtils.getString("basic.search.title", ConstantValueSearch.CAN_NOT_FOUND_TITLE));
		paneBasicSearch.setVisible(true);
		Panelchildren childBasic = new Panelchildren();
		childBasic.setStyle("overflow:auto");
		paneBasicSearch.appendChild(childBasic);
		Vlayout vlayout1 = new Vlayout();
		// vlayout1.setId("vlayout1");
		vlayout1.setParent(childBasic);
		setVlaySearch(vlayout1);

		boolean isShowSearchPanel = false;
		// Add item annotations
		if (modelClass != null) {
			Field[] fields = modelClass.getDeclaredFields();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					try {
						ArrayList<Annotation> ann = new ArrayList<Annotation>(Arrays.asList(field.getAnnotations()));
						if (ann != null) {
							for (int j = 0; j < ann.size(); j++) {
								if (ann.get(j).annotationType().equals(Searchable.class)) {
									if (mapSearch == null) {
										mapSearch = new HashMap<String, Field>();
									}
									mapSearch.put(field.getName(), field);
								}
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (!isShowSearchPanel) {
				leftPane.setParent(null);
			}
			if (StringUtils.equals(modelClass.getName(), Driver.class.getName())) {
				createSearchComponent(childBasic);
			}
		}
	}

	public BasicDetailWindow modifyDetailWindow() {
		return detailWindow;
	}

	public Hlayout getHlayout() {
		return hlayout;
	}

	public void setHlayout(Hlayout hlayout) {
		this.hlayout = hlayout;
	}

	public Label getLb_title() {
		return lb_title;
	}

	public void setLb_title(Label lb_title) {
		this.lb_title = lb_title;
	}

	public AbstractModel createNewModel() {
		AbstractModel model = null;
		try {
			model = (AbstractModel) modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * 
	 *
	 * @author VuD
	 */
	public void checkPermissionAction() {
		SysUser user = Env.getUser();
		if (!user.getUserName().equals(EnumLevelUser.ADMIN.toString())
				&& !user.getUserName().equals(EnumLevelUser.SUPER_USER.toString())) {
			Set<SysGroup> setGroup = user.getSysGroup();
			if (setGroup.size() > 0) {
				List<SysGroupLine> lstGroupLine = new ArrayList<SysGroupLine>();
				for (SysGroup sysGroup : setGroup) {
					Set<SysGroupLine> setLine = sysGroup.getSysGroupLines();
					for (SysGroupLine sysGroupLine : setLine) {
						SysFunction function = sysGroupLine.getSysFunction();
						String fuctionClazz = "com.vietek.taxioperation.ui.controller." + function.getClazz();
						String currentClass = this.getClass().getName();
						if (fuctionClazz.equals(currentClass)) {
							lstGroupLine.add(sysGroupLine);
						}
					}
				}
				List<SysAction> lstAction = new ArrayList<SysAction>();
				for (SysGroupLine sysGroupLine : lstGroupLine) {
					Set<SysAction> setAction = sysGroupLine.getSysAction();
					lstAction.addAll(setAction);
				}
				for (SysAction sysAction : lstAction) {
					if (sysAction.getValue().equals("Add")) {
						this.bt_add.setVisible(true);
					} else if (sysAction.getValue().equals("Edit")) {
						this.bt_edit.setVisible(true);
					} else if (sysAction.getValue().equals("Del")) {
						this.bt_delete.setVisible(true);
					}
				}
			} else {
				// SysFunctionController functionController =
				// (SysFunctionController)
				// ControllerUtils.getController(SysFunctionController.class);
				// SysFunction function = functionController.fi
			}

		} else {
			this.bt_add.setVisible(true);
			this.bt_edit.setVisible(true);
			this.bt_delete.setVisible(true);
		}
	}

	public Div getLeftHeader() {
		return leftHeader;
	}

	public void setLeftHeader(Div leftHeader) {
		this.leftHeader = leftHeader;
	}

	public Listbox getListbox() {
		return listbox;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public void setListbox(Listbox listbox) {
		this.listbox = listbox;
	}

	public Button getBt_add() {
		return bt_add;
	}

	public void setBt_add(Button bt_add) {
		this.bt_add = bt_add;
	}

	public Button getBt_edit() {
		return bt_edit;
	}

	public void setBt_edit(Button bt_edit) {
		this.bt_edit = bt_edit;
	}

	public Button getBt_delete() {
		return bt_delete;
	}

	public void setBt_delete(Button bt_delete) {
		this.bt_delete = bt_delete;
	}

	public Button getBt_refresh() {
		return bt_refresh;
	}

	public void setBt_refresh(Button bt_refresh) {
		this.bt_refresh = bt_refresh;
	}

	public Div getLeftPane() {
		return leftPane;
	}

	public void setLeftPane(Div leftPane) {
		this.leftPane = leftPane;
	}

	public ArrayList<Textbox> getTxtTemp() {
		return txtTemp;
	}

	public void setTxtTemp(ArrayList<Textbox> txtTemp) {
		this.txtTemp = txtTemp;
	}

	public ArrayList<Integer> getIsOnClickCb() {
		return isOnClickCb;
	}

	public void setIsOnClickCb(ArrayList<Integer> isOnClickCb) {
		this.isOnClickCb = isOnClickCb;
	}

	public ArrayList<Checkbox> getCbTemp() {
		return cbTemp;
	}

	public void setCbTemp(ArrayList<Checkbox> cbTemp) {
		this.cbTemp = cbTemp;
	}

	public ArrayList<String> getCbTempName() {
		return cbTempName;
	}

	public void setCbTempName(ArrayList<String> cbTempName) {
		this.cbTempName = cbTempName;
	}

	public ArrayList<Datebox> getDbTemp() {
		return dbTemp;
	}

	public void setDbTemp(ArrayList<Datebox> dbTemp) {
		this.dbTemp = dbTemp;
	}

	public ArrayList<String> getDbTempName() {
		return dbTempName;
	}

	public void setDbTempName(ArrayList<String> dbTempName) {
		this.dbTempName = dbTempName;
	}

	public ArrayList<Intbox> getInTemp() {
		return inTemp;
	}

	public void setInTemp(ArrayList<Intbox> inTemp) {
		this.inTemp = inTemp;
	}

	public ArrayList<String> getInTempName() {
		return inTempName;
	}

	public void setInTempName(ArrayList<String> inTempName) {
		this.inTempName = inTempName;
	}

	public ArrayList<Decimalbox> getDebTemp() {
		return debTemp;
	}

	public void setDebTemp(ArrayList<Decimalbox> debTemp) {
		this.debTemp = debTemp;
	}

	public ArrayList<String> getDebTempName() {
		return debTempName;
	}

	public void setDebTempName(ArrayList<String> debTempName) {
		this.debTempName = debTempName;
	}

	public Vlayout getVlaySearch() {
		return vlaySearch;
	}

	public void setVlaySearch(Vlayout vlaySearch) {
		this.vlaySearch = vlaySearch;
	}

	public List<?> getLstModel() {
		return lstModel;
	}

	public void setLstModel(List<?> lstModel) {
		this.lstModel = lstModel;
	}

	public String getDetailTitle() {
		return detailTitle;
	}

	public void setDetailTitle(String detailTitle) {
		this.detailTitle = detailTitle;
	}

	public Class<?> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<?> modelClass) {
		this.modelClass = modelClass;
	}

	public AbstractModel getCurrentModel() {
		return currentModel;
	}

	public void setCurrentModel(AbstractModel currentModel) {
		this.currentModel = currentModel;
	}

	public String getStrQuery() {
		return strQuery;
	}

	public void setStrQuery(String strQuery) {
		this.strQuery = strQuery;
	}

	public boolean isDisplayLeftPanel() {
		return isDisplayLeftPanel;
	}

	public void setDisplayLeftPanel(boolean isDisplayLeftPanel) {
		this.isDisplayLeftPanel = isDisplayLeftPanel;
		leftPane.setVisible(isDisplayLeftPanel);
	}

	public AbstractListModel<?> getListModel() {
		return listModel;
	}

	public void setListModel(AbstractListModel<?> listModel) {
		this.listModel = listModel;
	}

	public ArrayList<GridColumn> getGridColumns() {
		return lstCols;
	}

	public void setGridColumns(ArrayList<GridColumn> gridColumns) {
		this.lstCols = gridColumns;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Vlayout getVlayout() {
		return vlayout;
	}

	public void setVlayout(Vlayout vlayout) {
		this.vlayout = vlayout;
	}

	public Div getRightPane() {
		return rightPane;
	}

	public void setRightPane(Div rightPane) {
		this.rightPane = rightPane;
	}

	class RemindTask extends TimerTask {

		public void run() {
			if (true) {
				try {
					Executions.activate(desktop);
					getVlaySearch().removeChild(hlayCount);
					hlayCount = new Hlayout();
					hlayCount.setParent(getVlaySearch());
					hlayCount.setVisible(true);
					Panel tmpPanel = new Panel();
					tmpPanel.setVflex("1");
					tmpPanel.setHflex("true");
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					cal.setTime(new java.util.Date());
					cal.add(Calendar.MINUTE, 10);
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(new java.util.Date());
					cal2.add(Calendar.MINUTE, -10);
					String queryCount = "Select count(*) FROM TaxiOrder WHERE beginOrderTime LIKE '"
							+ dateFormat.format(new java.util.Date()) + "%' AND beginOrderTime < '"
							+ df.format(cal.getTime()) + "' AND beginOrderTime > '" + df.format(cal2.getTime()) + "' ";
					Session session = ControllerUtils.getCurrentSession();
					Query query = session.createQuery(queryCount);
					int totalSize = ((Long) query.uniqueResult()).intValue();
					String strArangeTime = df.format(cal2.getTime()).substring(11) + " - "
							+ df.format(cal.getTime()).substring(11);
					tmpPanel.setTitle("Số yêu cầu từ (" + strArangeTime + ")");
					tmpPanel.setVisible(true);
					tmpPanel.setParent(hlayCount);
					tmpPanel.setSclass("panel-success");
					Panelchildren tmpPanelChild = new Panelchildren();
					tmpPanelChild.setStyle("overflow:auto; background:red; text-align:center;");
					tmpPanel.appendChild(tmpPanelChild);
					Label lb = new Label(totalSize + "");
					lb.setStyle("font-size: 50px; text-align:center");
					tmpPanelChild.appendChild(lb);
					session.close();
					Executions.deactivate(desktop);
				} catch (DesktopUnavailableException e) {

				} catch (InterruptedException e) {

				}
			}
		}
	}

	private class EventNumberConstraint implements Constraint {
		public void validate(Component comp, Object value) throws WrongValueException {
			char[] chararray = ((String) value).toCharArray();
			for (int i = 0; i < chararray.length; i++) {
				if (!((Character) (chararray[i])).equals('-') && !((Character) (chararray[i])).equals('.')) {
					if (StringUtils.checkRegexStr(String.valueOf(chararray[i]), CommonDefine.SEARCH_PATTERN))
						throw new WrongValueException(comp, "Không cho phép các ký tự đặc biệt!");
				}

			}

		}
	}
}
