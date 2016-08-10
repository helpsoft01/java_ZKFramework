package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.ConstantValueSearch;
import com.vietek.taxioperation.common.EnumLevelUser;
import com.vietek.taxioperation.common.EnumStatus;
import com.vietek.taxioperation.controller.ChannelTmsController;
import com.vietek.taxioperation.controller.CustomerController;
import com.vietek.taxioperation.googlemapSearch.TaxiOrderUtil;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.model.TelephoneExtensionTms;
import com.vietek.taxioperation.model.TelephoneTableTms;
import com.vietek.taxioperation.model.VoipCenter;
import com.vietek.taxioperation.mq.TaxiOrderMQ;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.ui.editor.TextboxSearch;
import com.vietek.taxioperation.ui.editor.TextboxSearchHandler;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.CallInfo;
import com.vietek.taxioperation.util.CheckOnlineUtils;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.LocalizationUtils;
import com.vietek.taxioperation.util.StandandPhoneNumber;

public class TaxiOrdersForm extends AbstractWindowPanel implements Serializable, TextboxSearchHandler {

	/*
	 * Declare extend event
	 */
	private int timeOutSecondsFetchData = 0; // second fetch data

	private TelephoneTableWindow telephoneTableWindow = null;

	private static final String ON_EVENT_SHOW_TELEPHONETABLE = "onEventShowTelephoneTabble";
	boolean isphone_1 = true;

	private TextboxSearch tbPhoneSearch;
	private TextboxSearch tbAddressStartSearch;
	private TextboxSearch tbAddressEndSearch;
	private TextboxSearch tbRegVeh;
	private TextboxSearch tbSelectedVeh;
	private Button btCleanValueSearch;

	public boolean isFistLoadMap = true;

	private List<TaxiOrder> currentListModel = new ArrayList<>();

	private SysUser user;
	private VoipCenter voip;
	private TaxiOrderTabsPanel taxiOrderTabsPanel;

	private List<ChannelTms> lstChannel_In_Switchboard = new ArrayList<ChannelTms>();
	/**
	 * 
	 */

	private TaxiOrdersForm _this;

	private static final long serialVersionUID = -2423992205373693064L;
	private List<TaxiOrder> orders;

	public TaxiOrdersForm() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InstantiationException, InvocationTargetException {

		super(true);
		this._this = this;
		initUI();
		if (user == null)
			user = Env.getUser();
		cleanControlSearchs();
		setFocusDefaultForm();
		TaxiOrderMQ.subcribleCreated(this);
		TaxiOrderMQ.subcribleUpdated(this);
		TaxiOrderMQ.subcribleCallInDial(this);
	}

	public synchronized void addListModel(List<TaxiOrder> lstModel) {

		synchronized (this.currentListModel) {
			this.currentListModel.addAll(lstModel);
			setListModel(getListModel());// (this.currentListModel);
		}
	}

	protected void initUI() {

		setTitle("DANH SÁCH YÊU CẦU");
		setDetailTitle("Chi tiết yêu cầu");
		setModelClass(TaxiOrder.class);

		getDefaultData();

		this.setSclass("taxiOrdersForm");
		this.getBt_add().setVisible(true);
		this.getBt_delete().setVisible(false);
		this.getBt_edit().setVisible(false);

		this.getBt_add().setSclass("btn-success");
		this.getBt_refresh().setSclass("btn-info");
		this.getBt_add().setImage(""); /// themes/images/circle-add-plus-new-outline-stroke-16.png
		this.getBt_refresh().setImage("");

		this.getBt_add().setAutodisable("self");
		this.getBt_refresh().setAutodisable("self");

		this.getBt_add().setLabel("( F1 ) Thêm chuyến"); //
		this.getBt_refresh().setLabel("( F2 ) Lấy dữ liệu mới "); //

		getListbox().setSclass("listbox_dieudam_docdam");

		setDisplayLeftPanel(true);

		/*
		 * set ctr key
		 */
		this.setCtrlKeys("#f1#f2^f^e^s^x");
		/*
		 * set events
		 */
		this.addEventListener(ON_EVENT_SHOW_TELEPHONETABLE, EVENT_SHOW_TELEPHONETABLE);
		this.addEventListener(Events.ON_CTRL_KEY, EVENT_CTRL_KEY_FORM);
		getListbox().addEventListener(Events.ON_OK, EVENT_CTRL_KEY_FORM);
		btCleanValueSearch.addEventListener(Events.ON_CLICK, EVENT_CLEANSEARCH);

		getListbox().removeEventListener(Events.ON_SELECT, this);
		this.removeEventListener(Events.ON_SELECT, this);
		this.removeEventListener(Events.ON_OK, this);

		Events.postEvent(ON_EVENT_SHOW_TELEPHONETABLE, _this, null);

		createTabsDetailForm();
	}
	/*
	 * list online user and listening
	 */

	public TelephoneTableWindow getTelephoneTableWindow() {
		return telephoneTableWindow;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TelephoneExtensionTms> getLstExtensionEnable() {
		if (telephoneTableWindow != null)
			return telephoneTableWindow.getLstExtensions();
		else
			return new ArrayList();

	}

	public List<ChannelTms> getLstChannel_In_Switchboard() {

		if (this.lstChannel_In_Switchboard == null || this.lstChannel_In_Switchboard.size() == 0) {

			ChannelTmsController controllerChanel = (ChannelTmsController) ControllerUtils
					.getController(ChannelTmsController.class);

			String sql = "from ChannelTms where switchboardtms = ? ";
			this.lstChannel_In_Switchboard = controllerChanel.find(sql, Env.getUser().getSwitchboard());
		}

		return lstChannel_In_Switchboard;
	}

	public void setLstChannel_In_Switchboard(List<ChannelTms> lstChannel_In_Switchboard) {
		this.lstChannel_In_Switchboard = lstChannel_In_Switchboard;
	}

	public List<VoipCenter> getLstVoipCenters() {
		return telephoneTableWindow.getLstVoipCenters();
	}

	public void setTelephoneTableWindow(TelephoneTableWindow telephoneTableWindow) {
		this.telephoneTableWindow = telephoneTableWindow;
	}

	private synchronized void getDefaultData() {

		/*
		 * get list chanel
		 */

		if (this.lstChannel_In_Switchboard == null || this.lstChannel_In_Switchboard.size() == 0) {
			ChannelTmsController controllerChanel = (ChannelTmsController) ControllerUtils
					.getController(ChannelTmsController.class);

			String sql = "from ChannelTms where switchboardtms = ? ";
			this.lstChannel_In_Switchboard = controllerChanel.find(sql, Env.getUser().getSwitchboard());
		}

	}

	public TelephoneTableTms getTableTms() {
		return telephoneTableWindow.getTelephoneTable();
	}

	public List<TelephoneExtensionTms> getLstExtensions() {
		if (telephoneTableWindow != null)
			return telephoneTableWindow.getLstExtensions();
		else
			return new ArrayList<TelephoneExtensionTms>();
	}

	public void setTableTms(TelephoneTableTms tableTms) {
		Env.setTableTms(tableTms);
		CheckOnlineUtils.addTelephoneTableForUser(tableTms, user, getDesktop());
	}

	public void setExtensionsTms_ChannelTms_For_CheckOnlineUtils(List<TelephoneExtensionTms> lstxtensions) {

		List<VoipCenter> lstVoipCeter = getLstVoipCenters();
		if (lstVoipCeter.size() > 0)
			voip = lstVoipCeter.get(0);
		else
			voip = null;

		CheckOnlineUtils.setExtensions_VoipCenter_ForUser(Env.getUser(), lstxtensions, voip, getDesktop());

		if (timeOutSecondsFetchData == 0)
			timeOutSecondsFetchData = ConfigUtil.getValueConfig("TIMEOUT_REFFRESH_DATA_DTV",
					CommonDefine.TIMEOUT_REFFRESH_DATA_DTV);

		setFocusDefaultForm();
	}

	@Override
	protected void prepareSearchData() {

		super.prepareSearchData();
	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Số ĐT", 170, String.class, "getPhoneNumber"));
		lstCols.add(new GridColumn("Gọi", 60, Integer.class, "getRepeatTime"));
		lstCols.add(new GridColumn("Điểm đón", 250, String.class, "getBeginOrderAddress"));
		lstCols.add(new GridColumn("Y/c xe", 60, Integer.class, "getOrderCarType", "orderCarType", TaxiOrder.class));
		lstCols.add(new GridColumn("Xe đăng ký đón", 220, String.class, "getRegistedTaxis"));
		lstCols.add(new GridColumn("Xe đón", 90, String.class, "getPickedTaxi"));

		lstCols.add(new GridColumn("Trạng thái", 180, Integer.class, "getStatus", "status", TaxiOrder.class));
		lstCols.add(new GridColumn("Giờ Đ.Ký", 90, Timestamp.class, "getTimeOrder"));
		lstCols.add(new GridColumn("Giờ đón ", 90, Timestamp.class, "getTimeBeginOrder"));

		setGridColumns(lstCols);
	}

	@Override
	public void renderGrid() {
		orders = TripManager.sharedInstance.getListOrderForDTV(CommonDefine.DURATION_VALID_TAXIORDER, Env.getUser());
		listModel = new ListModelList<>(orders);
		getListbox().setModel(listModel);
		return;
	}

	@Override
	public void loadData() {

		if (user == null)
			user = Env.getUser();
		setModelClass(TaxiOrder.class);
		setStrQuery("");
	}

	/*
	 * chỉ lấy dữ liệu từ database khi - lần đầu và nhấn - nhấn F2
	 */
	@Override
	public void refresh() {
		try {
			renderGrid();
			currentModel = null;
			currentIndex = 0;

			setFocusForListbox();
			// goToCurrentRow();
			if (getListbox().getSelectedIndex() < 0 && getListbox().getItems().size() > 0)
				getListbox().setSelectedIndex(0);

		} catch (Exception ex) {
			AppLogger.logDebug.error("", ex);
		}
	}

	synchronized public void removeOrder(TaxiOrder order) {
		if (orders.contains(order)) {
			orders.remove(order);
		}
	}

	synchronized public void refresh(TaxiOrder order) {
		if (orders.contains(order)) {
			orders.remove(order);
			orders.add(0, order);
		} else {
			orders.add(0, order);
		}
		listModel = new ListModelList<>(orders);
		getListbox().setModel(listModel);
	}

	@Override
	protected void setFocusForListbox() {
		return;
	}

	public void setFocusDefaultForm() {

		tbPhoneSearch.setFocus(true);
	}

	public void selectFirtRowListbox() {

		getListbox().setFocus(true);
		if (getListbox().getItems().size() > 0)
			getListbox().setSelectedIndex(0);

	}

	public void cleanControlSearchs() {
		tbPhoneSearch.setValue("");
		tbAddressEndSearch.setValue("");
		tbAddressStartSearch.setValue("");

		tbRegVeh.setValue("");
		tbSelectedVeh.setValue("");
	}

	private void createTabsDetailForm() {

		if (taxiOrderTabsPanel == null)
			taxiOrderTabsPanel = new TaxiOrderTabsPanel(this);
	}

	public void unResgisted_Extension(String extension) {

		telephoneTableWindow.addTitle_Extension(telephoneTableWindow.getTelephoneTable());

	}

	private int position = 0;

	public void callInHidePopup() {
		position--;
		if (position < 0)
			position = 0;
	}

	public TaxiOrder callInShowPopup(CallInfo callInfo) {

		TaxiOrder order = getOrder(callInfo.getCallerNumber());
		taxiOrderTabsPanel.showWindowCallIn(callInfo, position, order, getDesktop());
		position++;
		return order;
	}

	/**
	 * @author tuanpa
	 * @param callInfo
	 */
	private void handleDialEvent(CallInfo callInfo) {
		// Check User
		if (Env.isResponsibleForCallEvent(callInfo) && (callInfo.getEvent() == CallInfo.CALL_EVENT_DIALING
				|| callInfo.getEvent() == CallInfo.CALL_EVENT_ANSWER)) {
			if (callInfo.getEvent() == CallInfo.CALL_EVENT_DIALING) {
				// Show popup
				callInShowPopup(callInfo);
			} else if (callInfo.getEvent() == CallInfo.CALL_EVENT_ANSWER) {
				// Hide popup, show detail form
				callInAnswerTaxiOrder(callInfo);
			}
		}
	}

	public TaxiOrder getOrder(String phone) {
		TaxiOrder reVal = null;

		TaxiOrder order = null;
		TaxiOrder orderExist = null;
		// boolean inList = false;
		for (Listitem item : getListbox().getItems()) {
			order = (TaxiOrder) item.getValue();

			if (order != null && order.getPhoneNumber().equals(phone)) {

				orderExist = order;
				// inList = true;
				/*
				 * check 45 minutes
				 */
				long diff = System.currentTimeMillis() - order.getOrderTime().getTime();
				long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

				if (minutes < CommonDefine.DURATION_VALID_TAXIORDER && (order.getStatus() == EnumStatus.MOI.getValue()
						|| order.getStatus() == EnumStatus.DA_DOC_DAM.getValue()
						|| order.getStatus() == EnumStatus.XE_DANG_KY_DON.getValue())) {

					reVal = order;
					return reVal;
				}
			}
		}
		if (reVal == null) {
			if (orderExist != null) {
				reVal = TaxiOrderUtil.getTaxiOrder(phone);
				reVal.getCustomer().setLastTimeCall(orderExist.getCustomer().getLastTimeCall());
				reVal.getCustomer().setTotalSuccessOrder(orderExist.getCustomer().getTotalSuccessOrder());
			}

		}
		return reVal;
	}

	public TaxiOrder getInforTimeCall(TaxiOrder order) {

		return order;
	}

	public TaxiOrder callInAnswerTaxiOrder(CallInfo call) {

		if (taxiOrderTabsPanel == null)
			createTabsDetailForm();
		String numberPhone = StandandPhoneNumber.standandPhone(call.getCallerNumber());
		TaxiOrder order = getOrder(numberPhone);
		order.setRepeatTime(order.getRepeatTime() + 1);
		// order.getCustomer().setLastTimeCall(new
		// Timestamp(System.currentTimeMillis()));
		taxiOrderTabsPanel.addTaxiOrder(this, order, call);
		call.setEvent(CallInfo.CALL_EVENT_TRIM);
		TaxiOrderMQ.pushCallInDial(new Event(TaxiOrderMQ.SERVER_PUSH_EVENT_QUEUE_DIAL, null, call));
		return order;
	}

	public void detailForm_showInforByPhone(String numberPhone) {

	}

	@Override
	public TaxiOrder createNewModel() {
		TaxiOrder taxiOrder = TaxiOrderUtil.createTaxiOrder("");
		return taxiOrder;
	}

	private void tripView() {

		if (getListbox().getItemCount() <= 0 || getListbox().getSelectedIndex() < 0)
			return;

		TaxiOrder taxiOrder = (TaxiOrder) getListModel().getElementAt(getListbox().getSelectedIndex());
		taxiOrderTabsPanel.addTaxiOrder(this, taxiOrder, null);

	}

	private void tripNew() {
		TaxiOrder order = TaxiOrderUtil.getTaxiOrder("");
		taxiOrderTabsPanel.addTaxiOrder(this, order, null);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (!Env.getHomePage().getDesktop().isAlive() || _this.getPage() == null) {
			TaxiOrderMQ.unSubcribleCallInDial(_this);
			return;
		}

		if (event.getName().equals(TaxiOrderMQ.TAXI_ORDER_UPDATED_EVENT)
				|| event.getName().equals(TaxiOrderMQ.TAXI_ORDER_NEW_SAVED_EVENT)) {
			TaxiOrder order = (TaxiOrder) event.getData();
			if (order.getStatus() == EnumStatus.XE_DA_DON.getValue())
				order.getCustomer().setTotalSuccessOrder(order.getCustomer().getTotalSuccessOrder() + 1);
			refeshEnable(order);
		} else if (event.getName().equals(TaxiOrderMQ.SERVER_PUSH_EVENT_QUEUE_DIAL.toString())) {
			// count++;
			// AppLogger.logTaxiorder.info("[Tuanpa]Subcribled: Count
			// TaxiOrderForm: " + count);
			if (event.getData() instanceof CallInfo) {
				CallInfo callInfo = (CallInfo) event.getData();
				handleDialEvent(callInfo);
			}
		}

		else if (event.getTarget() != null)
			if (event.getName().equals(Events.ON_DOUBLE_CLICK) || event.getTarget().equals(getBt_edit())) {

				tripView();

			} else if (event.getTarget() == getBt_add()) {

				tripNew();
			}
		if (event.getTarget() != null)
			if (event.getTarget() != getBt_add() && event.getTarget() != getBt_edit()
					&& event.getTarget() != getListbox() && !event.getName().equals(Events.ON_DOUBLE_CLICK))
				super.onEvent(event);

	}

	private void refeshEnable(TaxiOrder order) {

		if (order.getUser() == null)
			return;

		boolean isChange = false;
		boolean isExsist = false;
		for (TaxiOrder item : orders) {
			if (item.getId() == order.getId()) {
				orders.remove(order);
				orders.add(0, order);

				isExsist = true;
				isChange = true;
				break;
			}
		}
		if (!isExsist) {
			if (order.getUser() != null && Env.getUser() != null)
				if (order.getUser().getId() == Env.getUser().getId()) {
					orders.add(0, order);
					isChange = true;
				}
		}
		if (isChange) {
			listModel = new ListModelList<>(orders);
			getListbox().setModel(listModel);
		}
	}

	private EventListener<Event> EVENT_CTRL_KEY_FORM = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			AppLogger.logDebug.info("EVENT_CTRL_KEY" + event);
			try {
				KeyEvent keyEvent = (KeyEvent) event;
				int keyCode = keyEvent.getKeyCode();
				switch (keyCode) {
				case 13: // enter
					tripView();
					break;
				case 112: // f1
					Events.sendEvent(Events.ON_CLICK, getBt_add(), null);
					break;
				case 113: // f2
					break;
				case 70: // f
					tbPhoneSearch.setFocus(true);
					break;
				case 83: // s
					tbAddressStartSearch.setFocus(true);
					break;
				case 69: // e
					tbAddressEndSearch.setFocus(true);
					break;
				case 88: // x
					Events.sendEvent(Events.ON_CLICK, btCleanValueSearch, null);
					break;
				}
			} catch (Exception ex) {
				AppLogger.logDebug.error("", ex);
			}
		}
	};

	private EventListener<Event> EVENT_SHOW_TELEPHONETABLE = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			SysUser sysUser = Env.getUser();
			if (sysUser.getName().equalsIgnoreCase(EnumLevelUser.ADMIN.toString())
					|| sysUser.getName().equalsIgnoreCase(EnumLevelUser.SUPER_USER.toString()))
				return;

			if (telephoneTableWindow == null) {

				telephoneTableWindow = new TelephoneTableWindow(_this, Env.getUser());
				telephoneTableWindow.setParent(_this);
				telephoneTableWindow.show();
			}
		}

	};
	private EventListener<Event> EVENT_CLEANSEARCH = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			tbPhoneSearch.getComponent().setValue("");
			tbAddressStartSearch.getComponent().setValue("");
			tbAddressEndSearch.getComponent().setValue("");
			tbRegVeh.getComponent().setValue("");
			tbSelectedVeh.getComponent().setValue("");

		}

	};

	@Override
	protected void initLeftPanelTmp() {
		return;
	}

	@Override

	public void initLeftPanel() {

		Panel paneBasicSearch = new Panel();
		paneBasicSearch.setParent(getLeftPane());
		paneBasicSearch.setSclass("panel-success");
		paneBasicSearch.setVflex("1");
		// paneBasicSearch.setHflex("true");
		paneBasicSearch.setWidth("100%");

		paneBasicSearch
				.setTitle(LocalizationUtils.getString("basic.search.title", ConstantValueSearch.CAN_NOT_FOUND_TITLE));
		paneBasicSearch.setVisible(true);
		Panelchildren childBasic = new Panelchildren();
		childBasic.setStyle("overflow:auto");
		paneBasicSearch.appendChild(childBasic);

		/*
		 * find phone
		 */

		Div div = new Div();
		div.setParent(childBasic);
		div.setSclass("divSearchTaxiorder");
		Label lb = new Label("Ctrl+F");
		lb.setParent(div);
		lb.setAttribute("for", "findAddressEditorStart");
		lb.setSclass("lbF2 infor_Customer");
		tbPhoneSearch = new TextboxSearch("Số điện thoại", div);

		/*
		 * fond start address
		 */
		div = new Div();
		div.setParent(childBasic);
		div.setSclass("divSearchTaxiorder");
		lb = new Label("Ctrl+S");
		lb.setParent(div);
		lb.setAttribute("for", "findAddressEditorStart");
		lb.setSclass("lbF2 infor_Customer");
		tbAddressStartSearch = new TextboxSearch("Địa chỉ đón", childBasic);
		/*
		 * fond end address
		 */
		div = new Div();
		div.setParent(childBasic);
		div.setSclass("divSearchTaxiorder");
		lb = new Label("Ctrl+E");
		lb.setParent(div);
		lb.setAttribute("for", "findAddressEditorStart");
		lb.setSclass("lbF2 infor_Customer");
		tbAddressEndSearch = new TextboxSearch("Địa chỉ kết thúc", childBasic);

		/*
		 * dungnd: 20160401 - them tim kiem voi so tai
		 */
		div = new Div();
		div.setParent(childBasic);
		div.setSclass("divSearchTaxiorder");
		lb = new Label("Ctrl+D");
		lb.setParent(div);
		lb.setSclass("lbF2 infor_Customer");
		tbRegVeh = new TextboxSearch("Số tài đăng ký", childBasic);

		div = new Div();
		div.setParent(childBasic);
		div.setSclass("divSearchTaxiorder");
		lb = new Label("Ctrl+U");
		lb.setParent(div);
		lb.setSclass("lbF2 infor_Customer");
		tbSelectedVeh = new TextboxSearch("Số tài đã đón", childBasic);

		tbRegVeh.setHandler(this);
		tbSelectedVeh.setHandler(this);
		tbPhoneSearch.setHandler(this);
		tbAddressStartSearch.setHandler(this);
		tbAddressEndSearch.setHandler(this);

		btCleanValueSearch = new Button("(Ctrl+X) Xóa tìm kiếm");
		btCleanValueSearch.setParent(childBasic);
		btCleanValueSearch.setSclass("btn-danger btCleanValueSearch");
		btCleanValueSearch.setWidth("100%");

		return;

	}

	@Override
	public void onChanging(String value) {
		refresh();
	}
}

class CallInWindow extends Window implements Serializable, EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2924347187185054297L;
	Timer timer;
	private int timeOutSeconds = 0;
	Desktop desktop;
	TaxiOrder order;
	/*
	 * Declare extend event
	 */
	boolean isPhoneCallIn_1 = true;

	public static String position_right_bottom = "right, bottom";
	public static String position_left_bottom = "left, bottom";

	TaxiOrdersForm taxiOrderForm;
	CallInfo call;
	Customer customer = null;

	Div div;
	Borderlayout borderlayout;
	Vlayout vlayout;
	Grid grid;
	Rows rows;

	Label lbPhoneNumber;
	Label lbNameCustomer;
	Label lbLastTimeCall;
	Label lbRepeatTime;
	Div divContainCommand;
	Button btAccept;
	Button btIgnore;
	TaxiOrder modelTaxiOrder;

	CallInWindow _this;

	public CallInfo getCall() {
		return call;
	}

	public void setCall(CallInfo call) {
		this.call = call;
	}

	public TaxiOrder getModelTaxiOrder() {
		return modelTaxiOrder;
	}

	public TaxiOrder getOrder() {
		return order;
	}

	public void setOrder(TaxiOrder order) {
		this.order = order;
	}

	public Desktop getDesktop() {
		return desktop;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
	}

	public void setModelTaxiOrder(TaxiOrder modelTaxiOrder) {
		this.modelTaxiOrder = modelTaxiOrder;
	}

	public int getSecondsTimeOut() {
		return timeOutSeconds;
	}

	public void setSecondsTimeOut(int secondsTimeOut) {
		this.timeOutSeconds = secondsTimeOut;
	}

	public boolean isPhoneCallIn_1() {
		return isPhoneCallIn_1;
	}

	public void setPhoneCallIn_1(boolean isPhoneCallIn_1) {
		this.isPhoneCallIn_1 = isPhoneCallIn_1;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public TaxiOrdersForm getTaxiOrderForm() {
		return taxiOrderForm;
	}

	public void setTaxiOrder(TaxiOrdersForm taxiOrder) {
		this.taxiOrderForm = taxiOrder;
	}

	public CallInWindow(Component parent, CallInfo call, TaxiOrder order, Desktop desktop) {

		super();
		_this = this;
		if (timeOutSeconds == 0)
			timeOutSeconds = ConfigUtil.getValueConfig("TIMEOUT_CALLINWINDOW", CommonDefine.TIMEOUT_CALLINWINDOW);
		this.call = call;
		this.taxiOrderForm = (TaxiOrdersForm) parent;

		/*
		 * infor
		 */

		this.order = order;
		this.customer = CustomerController.getCustomer(StandandPhoneNumber.standandPhone(call.getCallerNumber()));

		/*
		 * 
		 */
		this.desktop = desktop;
		/*
		 * create ui
		 */

		initUI();

		/*
		 * set time out
		 */
		timer = new Timer(timeOutSeconds * 1000);
		timer.setParent(this);
		timer.start();
		timer.addEventListener(Events.ON_TIMER, this);
	}

	public void stopTimer() {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}

	public void detachUI() {
		try {
			TaxiOrderMQ.unSubcribleCallInDial(this);
			taxiOrderForm.callInHidePopup();
			this.setVisible(false);
			this.detach();
			this.stopTimer();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

	private void initUI() {
		TaxiOrderMQ.subcribleCallInDial(this);

		/*
		 * this
		 */

		this.setClosable(false);
		this.setMinimizable(false);
		this.setMaximizable(false);
		this.setBorder("normal");
		/*
		 * div contain
		 */

		div = new Div();
		div.setParent(this);
		div.setWidth("100%");
		div.setHeight("100%");
		div.setClass("taxiorder2CallInDiv");

		grid = new Grid();
		grid.setParent(div);
		grid.setWidth("100%");
		grid.setHeight("100%");

		rows = new Rows();
		rows.setParent(grid);

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("1");

		Row row = new Row();

		row = new Row();
		row.setParent(rows);
		if (customer != null)
			lbNameCustomer = new Label(
					(customer.getName() == null || customer.getName() == "") ? "~" : customer.getName());
		else
			lbNameCustomer = new Label(call.getCallerNumber());
		lbNameCustomer.setParent(row);

		row = new Row();
		row.setParent(rows);
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);
		divContainCommand = new Div();
		divContainCommand.setParent(cell);

		lbRepeatTime = new Label(Integer.toString(order.getRepeatTime()));
		lbRepeatTime.setParent(divContainCommand);
		lbRepeatTime.setClass("taxiorder2CallInlbRepeatTime");

		long distance;
		if (order.getRepeatTime() == 0 || order.getCustomer() == null)
			distance = 0;
		else
			distance = System.currentTimeMillis() - CustomerController
					.getCustomer(StandandPhoneNumber.standandPhone(order.getPhoneNumber())).getLastTimeCall().getTime();
		distance = distance / 1000;
		String strLastCall = (distance / 60) + ":" + (distance % 60);
		lbLastTimeCall = new Label(strLastCall);
		lbLastTimeCall.setParent(divContainCommand);
		lbLastTimeCall.setClass("taxiorder2CallInlbLastTimeCall");

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);
		divContainCommand = new Div();
		divContainCommand.setParent(cell);

		btAccept = new Button();
		btAccept.setParent(divContainCommand);
		btAccept.setClass("taxiorder2CallInbtAccept");

		btIgnore = new Button();
		btIgnore.setParent(divContainCommand);
		btIgnore.setClass("taxiorder2CallInbtIgnore");

		Caption caption = new Caption(call.getCallerNumber());
		caption.setParent(this);
		caption.setStyle("");

		/*
		 * events
		 */
		btAccept.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {

				call.setEvent(CallInfo.CALL_EVENT_ANSWER);
				TaxiOrderMQ.pushCallInDial(new Event(TaxiOrderMQ.SERVER_PUSH_EVENT_QUEUE_DIAL, null, call));
				_this.detachUI();
			}
		});
		btIgnore.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {

				_this.detachUI();
			}
		});
		this.setVisible(false);
	}

	public void setPositionPhone(int position) {

		try {

			this.setPosition("left,bottom");
			this.setSclass("CallInWindowPhoneCommon taxiOrder2_CallInWindowPhone" + position);
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {

		}

	}

	public void show(int position) {

		try {

			this.setParent(Env.getHomePage().getDivTab());
			this.setPosition("left,bottom");
			this.setSclass("CallInWindowPhoneCommon taxiOrder2_CallInWindowPhone" + position);
			this.setMode(Mode.OVERLAPPED);
			this.setTopmost();
			this.doOverlapped();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_TIMER)) {
			detachUI();
		} else if (event.getName().equals(TaxiOrderMQ.SERVER_PUSH_EVENT_QUEUE_DIAL.toString())) {
			if (event.getData() instanceof CallInfo) {
				CallInfo callInfo = (CallInfo) event.getData();
				if (callInfo.getEvent() == CallInfo.CALL_EVENT_TRIM) {
					if (call.getCallUuid().equals(callInfo.getCallUuid())) {
						AppLogger.logTaxiorder.info("4.2. Hangup: calluuid:" + callInfo.getCallUuid());
						detachUI();
					} else if (callInfo.getCallUuid() == "") {
						AppLogger.logTaxiorder.info("4.2. Hangup: calluuid : Empty or Null" + "- User:");
						return;
					}
				}
			}
		}
	}
}
