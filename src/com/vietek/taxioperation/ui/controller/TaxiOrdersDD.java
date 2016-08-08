package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.North;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.South;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.chat.ChatWindow;
import com.vietek.taxioperation.common.AnnonationLinkedTable;
import com.vietek.taxioperation.common.AppConstant;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.EnumCancelReason;
import com.vietek.taxioperation.common.EnumCarTypeCommon;
import com.vietek.taxioperation.common.EnumStatus;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.common.MonitorCommon;
import com.vietek.taxioperation.controller.ChannelTmsController;
import com.vietek.taxioperation.controller.DriverController;
import com.vietek.taxioperation.controller.SysUserController;
import com.vietek.taxioperation.controller.VehicleController;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.model.User;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.model.VehicleDD;
import com.vietek.taxioperation.model.VehicleInfoJson;
import com.vietek.taxioperation.model.VehicleStatusDD;
import com.vietek.taxioperation.mq.TaxiOrderMQ;
import com.vietek.taxioperation.processor.SendCancelSmsProcessor;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.ui.controller.vmap.VMaps;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.EditorFactory;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.IntegerUtils;
import com.vietek.taxioperation.util.MapUtils;
import com.vietek.taxioperation.util.TaxiUtils;

public class TaxiOrdersDD extends AbstractWindowPanel implements Serializable {

	/**
	 * @author VuD Windown cho dieu dam vien
	 */
	private static final long serialVersionUID = 1L;
	private Button btnChuyen;
	private Button btnDKDon;
	private Button btnDDKhach;
	private Button btnHLDiem;
	private Button btnCKenh;
	private Button btnGKRXe;
	private Button btnHuy;
	private Button btnRefesh;

	private List<TaxiOrder> lstTopModel;
	private Listbox lstboxTop;
	private List<TaxiOrder> lstRegistedModel;
	private Listbox lstboxRegisted;
	private List<TaxiOrder> lstNewModel;
	private Listbox lstboxNew;
	private List<TaxiOrder> lstCancelModel;
	private Listbox lstboxCancel;
	private Borderlayout bdlayout;

	protected TaxiOrder currentModel = null;
	private Listbox curListBox = null;
	private Listitem curItem = null;

	private boolean isInAction = false;

	private Label lblInfo;
	protected List<TaxiOrder> lstAllModel;
	private Long lastRefresh = 0l;

	public TaxiOrdersDD() {
		super(true);
		this.addEventListener(Events.ON_CTRL_KEY, this);
		this.setCtrlKeys("#f1#f2#f3#f4#f5#f6#f7^1^2");

		TaxiOrderMQ.subcribleUpdated(this);
		Timer timer = new Timer();
		timer.setParent(this);
		timer.setRepeats(true);
		timer.setDelay(90000);
		timer.addEventListener(Events.ON_TIMER, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				try {
					if (System.currentTimeMillis() - lastRefresh > 90000) {
						if (!isInAction) {
							refreshAllList();
						}
					}
				} catch (Exception e) {
					AppLogger.logDebug.error(MonitorCommon.getSystemCpuInfo(), e);
				} finally {
				}
			}
		});

		timer = new Timer();
		timer.setParent(this);
		timer.setRepeats(true);
		timer.setDelay(1000);
		timer.addEventListener(Events.ON_TIMER, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				setValueLabel();
			}
		});
	}

	@Override
	public void beforInit() {
		this.setDisplayLeftPanel(false);
	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initTopRight() {
	}

	@Override
	public void initRightPanel(int i) {
		Vlayout vlayout = new Vlayout();
		this.setVlayout(vlayout);
		vlayout.setParent(this.getRightPane());
		vlayout.setVflex("1");

		Hlayout hlayout = new Hlayout();
		hlayout.setParent(vlayout);
		this.setHlayout(hlayout);
		this.initTopRight();
		this.initColumns();
		bdlayout = new Borderlayout();
		bdlayout.setParent(vlayout);
		bdlayout.setVflex("1");
		bdlayout.setHflex("1");

		North north = new North();
		north.setParent(bdlayout);
		north.setHeight("28px");
		this.lblInfo = new Label();
		this.lblInfo.setParent(north);
		this.lblInfo.setVflex("1");
		this.lblInfo.setStyle("font-size:20px ;color: red");

		lstTopModel = new ArrayList<TaxiOrder>();
		lstRegistedModel = new ArrayList<TaxiOrder>();
		lstNewModel = new ArrayList<TaxiOrder>();
		lstCancelModel = new ArrayList<TaxiOrder>();
		this.loadDataDD();
		Center center = new Center();
		center.setParent(bdlayout);
		this.initTopGrid(center);

		South south = new South();
		south.setParent(bdlayout);
		south.setCollapsible(true);
		south.setSplittable(true);
		south.setSize("40%");
		this.initBotGrid(south);
	}

	@Listen("setFocusComponent")
	public void setFocusComponent(Event event) {
		if (event.getData() != null) {
			Textbox component = (Textbox) event.getData();
			component.focus();
			component.select();

		}
	}

	@Override
	public void initColumns() {

	}

	@Override
	public void loadData() {

	}

	@Override
	public void onEvent(Event event) throws Exception {
		try {
			if (event.getName().equals(TaxiOrderMQ.TAXI_ORDER_UPDATED_EVENT)) {

				if (!Env.getHomePage().getDesktop().isAlive() || this.getPage() == null) {
					TaxiOrderMQ.unSubcribleUpdated(this);
					return;
				}

				TaxiOrder order = (TaxiOrder) event.getData();
				if (order != null) {
					if (order.getPhoneNumber() != null && order.getPhoneNumber().length() > 0) {
						if (order.getChannel() != null) {
							SysUser user = Env.getUser();
							if (user != null) {
								if (user.getChannel() != null) {
									if (this.lstAllModel.contains(order)) {
										if (order.getChannel().getId() != user.getChannel().getId()
												|| (order.getStatus() == 0 && order.getCancelReason() == 1)) {
											this.lstAllModel.remove(order);
											// refreshMenList();
											Events.echoEvent("focusListBox", this, this.lstboxTop);
										} else {
											this.updateListAllModel(order);
											// refreshMenList();
											Events.echoEvent("focusListBox", this, this.lstboxTop);
										}
									} else if (order.getChannel().getId() == user.getChannel().getId()) {
										if (!order.getIsAutoOperation()) {
											this.updateListAllModel(order);
											// refreshMenList();
											Events.echoEvent("focusListBox", this, this.lstboxTop);
											// refreshAllList();
										}
									}

								}
							}
						}
						// refeshAllList();
					}
					refreshMenList();
				}
				/*
				 * } else if (event.getName().equals(Env.EVENT_TAXI_ORDER_CK)) {
				 * Integer kenhCu = (Integer) event.getData(); if (kenhCu !=
				 * null) { SysUserController controller = (SysUserController)
				 * ControllerUtils .getController(SysUserController.class);
				 * SysUser user = controller.get(SysUser.class,
				 * Env.getUserID()); if (user != null) { if (user.getChannel()
				 * != null) { if (kenhCu == user.getChannel().getId()) {
				 * refreshAllList(); Events.echoEvent("focusListBox", this,
				 * this.lstboxTop); // refreshMenList(); } } } //
				 * refeshAllList(); }
				 */
			} else if (event.getTarget() != null) {
				if (event.getName().equals(Events.ON_SELECT)) {
					if (event.getTarget().equals(lstboxTop)) {
						lstboxRegisted.setSelectedIndex(-1);
						lstboxNew.setSelectedIndex(-1);
						lstboxCancel.setSelectedIndex(-1);
						curItem = lstboxTop.getSelectedItem();
						if (curItem != null) {
							currentModel = curItem.getValue();
							this.displayButtonForTop();
							this.curListBox = this.lstboxTop;
							this.setValueLabel();
						}
					} else if (event.getTarget().equals(lstboxRegisted)) {
						lstboxTop.setSelectedIndex(-1);
						lstboxNew.setSelectedIndex(-1);
						lstboxCancel.setSelectedIndex(-1);
						curItem = lstboxRegisted.getSelectedItem();
						if (curItem != null) {
							currentModel = curItem.getValue();
							this.displayButtonForAction();
							this.curListBox = this.lstboxRegisted;
							this.setValueLabel();
						}
						return;
					} else if (event.getTarget().equals(lstboxNew)) {
						lstboxTop.setSelectedIndex(-1);
						lstboxRegisted.setSelectedIndex(-1);
						lstboxCancel.setSelectedIndex(-1);
						this.curItem = null;
						currentModel = null;
						lblInfo.setValue("");
						this.displayButtonForNew();
						this.curListBox = this.lstboxNew;
					} else if (event.getTarget().equals(lstboxCancel)) {
						lstboxTop.setSelectedIndex(-1);
						lstboxRegisted.setSelectedIndex(-1);
						lstboxNew.setSelectedIndex(-1);
						this.curItem = null;
						this.currentModel = null;
						lblInfo.setValue("");
						this.displayButtonForCancel();
						this.curListBox = this.lstboxCancel;
					}
				} else if (event.getName().equals(Events.ON_DOUBLE_CLICK)) {
					if (event.getTarget().equals(lstboxTop)) {
						lstboxRegisted.setSelectedIndex(-1);
						lstboxNew.setSelectedIndex(-1);
						if (lstboxTop.getSelectedIndex() >= 0) {
							currentModel = lstboxTop.getSelectedItem().getValue();
							this.displayButtonForTop();
						}
						if (currentModel != null) {
							// this.InformationDD(currentModel);
							this.infomationDD2(currentModel);
						}
					} else if (event.getTarget().equals(lstboxRegisted)) {
						lstboxTop.setSelectedIndex(-1);
						lstboxNew.setSelectedIndex(-1);
						if (lstboxRegisted.getSelectedIndex() >= 0) {
							currentModel = lstboxRegisted.getSelectedItem().getValue();
							this.displayButtonForAction();
						}
						if (currentModel != null) {
							if (currentModel.getPickedTaxi() == null) {
								// this.InformationDD(currentModel);
								this.infomationDD2(currentModel);
							}
						}
					} else if (event.getTarget().equals(lstboxNew)) {
						List<Listitem> lstAllItem = lstboxNew.getItems();
						List<TaxiOrder> lstModelEvent = new ArrayList<>();
						for (Listitem listitem : lstAllItem) {
							TaxiOrder orderTmp = listitem.getValue();
							lstModelEvent.add(orderTmp);
							orderTmp.setIsUp(true);
							orderTmp.setTimeIsUpdated(new Timestamp(System.currentTimeMillis()));
							orderTmp.setStatus(EnumStatus.DA_DOC_DAM.getValue());
							// orderTmp.save();
							TripManager.sharedInstance.saveTrip(orderTmp);
						}
						Env.getHomePage().showNotification("Đã chuyển " + lstModelEvent.size() + " yêu cầu thành công",
								Clients.NOTIFICATION_TYPE_INFO);
						// this.refreshMenList();
						Events.echoEvent("focusListBox", this, this.lstboxTop);
						isInAction = false;
						// fireEventRefeshForDTV(lstModelEvent);
					}
				} else if (event.getName().equals(Events.ON_CLICK)) {
					if (event.getTarget() == this.lstboxTop) {
						this.focusRowOfListBox(this.lstboxTop);
					} else if (event.getTarget() == this.lstboxNew) {
						this.focusRowOfListBox(this.lstboxNew);
					} else if (event.getTarget() == this.lstboxRegisted) {
						this.focusRowOfListBox(this.lstboxRegisted);
					} else if (event.getTarget() == this.lstboxCancel) {
						this.focusRowOfListBox(this.lstboxCancel);
					} else if (event.getTarget().equals(btnChuyen)) {
						if (this.lstboxNew.getSelectedIndex() == -1) {
							Env.getHomePage().showNotification("Cần phải chọn yêu cầu mới",
									Clients.NOTIFICATION_TYPE_WARNING);
							return;
						}
						this.handleChuyen();
					} else if (event.getTarget().equals(btnDKDon)) {
						if (currentModel == null) {
							Env.getHomePage().showNotification("Cần phải chọn yêu cầu",
									Clients.NOTIFICATION_TYPE_WARNING);
							return;
						}
						this.handleDangKyDon();
					} else if (event.getTarget().equals(btnDDKhach)) {
						if (currentModel == null) {
							Env.getHomePage().showNotification("Cần phải chọn yêu cầu",
									Clients.NOTIFICATION_TYPE_WARNING);
							return;
						}
						this.handleXeDonKhach();
					} else if (event.getTarget().equals(btnHLDiem)) {
						if (currentModel == null) {
							Env.getHomePage().showNotification("Cần phải chọn yêu cầu",
									Clients.NOTIFICATION_TYPE_WARNING);
							return;
						}
						this.handleHoiLaiDiem();
					} else if (event.getTarget().equals(btnCKenh)) {
						this.handleChuyenKenh();
					} else if (event.getTarget().equals(btnGKRXe)) {
						if (currentModel == null) {
							Env.getHomePage().showNotification("Cần phải chọn yêu cầu",
									Clients.NOTIFICATION_TYPE_WARNING);
							return;
						}
						this.handleGoiKhachRaXe();
					} else if (event.getTarget().equals(btnHuy)) {
						this.handleHuyCuoc();
					} else if (event.getTarget().equals(this.btnRefesh)) {
						refreshAllList();
					}
				} else if (event.getName().equals("onCheckSelectAll")) {
					CheckEvent ev = (CheckEvent) event;
					if (ev.isChecked()) {
						this.focus();
					} else {

					}
				} else if (event.getName().equals(Events.ON_CTRL_KEY)) {
					// TODO: Hotkey
					// if (!event.getTarget().equals(this)) {
					// return;
					// }
					KeyEvent keyEvent = (KeyEvent) event;
					int keyCode = keyEvent.getKeyCode();
					if (!isInAction) {
						if (keyCode == 50) {
							List<Listitem> lstAllItem = lstboxNew.getItems();
							List<TaxiOrder> lstModelEvent = new ArrayList<>();
							for (Listitem listitem : lstAllItem) {
								TaxiOrder orderTmp = listitem.getValue();
								lstModelEvent.add(orderTmp);
								orderTmp.setIsUp(true);
								orderTmp.setTimeIsUpdated(new Timestamp(System.currentTimeMillis()));
								orderTmp.setStatus(EnumStatus.DA_DOC_DAM.getValue());
								// orderTmp.save();
								TripManager.sharedInstance.saveTrip(orderTmp);
							}
							Env.getHomePage().showNotification(
									"Đã chuyển " + lstModelEvent.size() + " yêu cầu thành công",
									Clients.NOTIFICATION_TYPE_INFO);
							// this.refreshMenList();
							Events.echoEvent("focusListBox", this, this.lstboxTop);
							// fireEventRefeshForDTV(lstModelEvent);
						}
					}
					if (currentModel == null) {
						if (keyCode == 49) {
							this.handleChuyen();
						}
					} else {
						if (currentModel.getPickedTaxi() == null) {
							if (keyCode == 112) {
								// f1
								this.handleDangKyDon();
							} else if (keyCode == 113) {
								// f2
								this.handleXeDonKhach();
							} else if (keyCode == 114) {
								// f3
								this.handleHoiLaiDiem();
							} else if (keyCode == 116) {
								// f5
								this.handleGoiKhachRaXe();
							}
						}
					}
					if (keyCode == 115) {
						// f4
						this.handleChuyenKenh();
					} else if (keyCode == 117) {
						// f6
						this.handleHuyCuoc();
					} else if (keyCode == 118) {
						// f7
						refreshAllList();
					}
				}
				super.onEvent(event);
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("OnEvent", e);
			e.printStackTrace();
		}

	}

	@Listen("HandleSelect")
	public void handleSelectTab() {
		AppLogger.logDebug.info("Eco event");
	}

	private void initTopGrid(Component component) {
		this.lstboxTop = new Listbox();
		lstboxTop.setParent(component);
		this.initListboxTop(lstboxTop);
		lstboxTop.setModel(new ListModelList<TaxiOrder>(lstTopModel));
		lstboxTop.addEventListener(Events.ON_CLICK, this);

	}

	private void initBotGrid(Component component) {
		Tabbox tabbox = new Tabbox();
		tabbox.setVflex("1");
		tabbox.setParent(component);
		Tabs tabs = new Tabs();
		tabs.setParent(tabbox);
		Tabpanels tabpanels = new Tabpanels();
		tabpanels.setParent(tabbox);
		tabpanels.setVflex("1");
		Tab tab = new Tab();
		tab.setParent(tabs);
		tab.setLabel("Danh sách mới");

		Toolbar toolbar = new Toolbar();
		toolbar.setParent(tabbox);
		this.initToolbarTab(toolbar);

		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setParent(tabpanels);
		this.lstboxNew = new Listbox();
		this.lstboxNew.addEventListener(Events.ON_DOUBLE_CLICK, this);
		this.lstboxNew.addEventListener(Events.ON_CLICK, this);
		this.lstboxNew.addEventListener("onCheckSelectAll", this);
		this.initListboxNew(lstboxNew);
		this.lstboxNew.setParent(tabpanel);
		ListModelList<TaxiOrder> lstModelList = new ListModelList<TaxiOrder>(this.lstNewModel);
		lstModelList.setMultiple(true);
		this.lstboxNew.setModel(lstModelList);
		tab.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				focusRowOfListBox(lstboxNew);
			}
		});

		tab = new Tab();
		tab.setParent(tabs);
		tab.setLabel("Đã xử lý");
		tabpanel = new Tabpanel();
		tabpanel.setParent(tabpanels);
		this.lstboxRegisted = new Listbox();
		this.initListboxRegisted(lstboxRegisted);
		this.lstboxRegisted.setParent(tabpanel);
		this.lstboxRegisted.setModel(new ListModelList<TaxiOrder>(this.lstRegistedModel));
		tab.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				focusRowOfListBox(lstboxRegisted);
			}
		});

		tab = new Tab();
		tab.setParent(tabs);
		tab.setLabel("Danh sách hủy");
		tabpanel = new Tabpanel();
		tabpanel.setParent(tabpanels);
		this.lstboxCancel = new Listbox();
		this.lstboxCancel.addEventListener(Events.ON_DOUBLE_CLICK, this);
		this.lstboxCancel.addEventListener(Events.ON_CANCEL, this);
		this.initListboxCancel(lstboxCancel);
		this.lstboxCancel.setParent(tabpanel);
		this.lstboxCancel.setModel(new ListModelList<TaxiOrder>(this.lstCancelModel));
		tabbox.setSelectedIndex(0);
	}

	private void initToolbarTab(Toolbar toolbar) {
		btnChuyen = new Button();
		btnChuyen.setParent(toolbar);
		btnChuyen.setZclass("");
		btnChuyen.setSclass("btn-taxiorder-dd");
		btnChuyen.setLabel("Chuyển");
		btnChuyen.addEventListener(Events.ON_CLICK, this);
		btnChuyen.setTooltiptext("Chuyển (Ctrl + 1)");

		btnDKDon = new Button();
		btnDKDon.setParent(toolbar);
		btnDKDon.setSclass("btn-taxiorder-dd");
		btnDKDon.setLabel("ĐK đón");
		btnDKDon.addEventListener(Events.ON_CLICK, this);
		btnDKDon.setTooltiptext("Xe đăng ký đón (F1)");

		btnDDKhach = new Button();
		btnDDKhach.setParent(toolbar);
		btnDDKhach.setSclass("btn-taxiorder-dd");
		btnDDKhach.setLabel("Đón khách");
		btnDDKhach.addEventListener(Events.ON_CLICK, this);
		btnDDKhach.setTooltiptext("Đã đón khách (F2)");

		btnHLDiem = new Button();
		btnHLDiem.setParent(toolbar);
		btnHLDiem.setSclass("btn-taxiorder-dd");
		btnHLDiem.setLabel("Hỏi lại điểm");
		btnHLDiem.addEventListener(Events.ON_CLICK, this);
		btnHLDiem.setTooltiptext("Hỏi lại điểm (F3)");

		btnCKenh = new Button();
		btnCKenh.setParent(toolbar);
		btnCKenh.setSclass("btn-taxiorder-dd");
		btnCKenh.setLabel("Chuyển kênh");
		btnCKenh.addEventListener(Events.ON_CLICK, this);
		btnCKenh.setTooltiptext("Chuyển kênh (F4)");

		btnGKRXe = new Button();
		btnGKRXe.setParent(toolbar);
		btnGKRXe.setSclass("btn-taxiorder-dd");
		btnGKRXe.setLabel("Gọi khách");
		btnGKRXe.addEventListener(Events.ON_CLICK, this);
		btnGKRXe.setTooltiptext("Gọi khách ra xe (F5)");

		btnHuy = new Button();
		btnHuy.setParent(toolbar);
		btnHuy.setSclass("btn-taxiorder-dd");
		btnHuy.setStyle("background:red");
		btnHuy.setLabel("Hủy");
		btnHuy.addEventListener(Events.ON_CLICK, this);
		btnHuy.setTooltiptext("Hủy cuốc (F6)");

		btnRefesh = new Button();
		btnRefesh.setParent(toolbar);
		btnRefesh.setSclass("btn-taxiorder-dd");
		btnRefesh.setStyle("background:red");
		btnRefesh.setLabel("Refesh");
		btnRefesh.addEventListener(Events.ON_CLICK, this);
		btnRefesh.setTooltiptext("Cập nhật bản ghi mới (F7)");

	}

	private void loadDataDD() {
		lstTopModel.clear();
		lstRegistedModel.clear();
		lstNewModel.clear();
		lstCancelModel.clear();
		SysUserController controllerUser = (SysUserController) ControllerUtils.getController(SysUserController.class);
		SysUser user = controllerUser.get(SysUser.class, Env.getUserID());
		/*
		 * String sql =
		 * "from TaxiOrder where isAutoOperation = false and beginOrderTime >= ? and phoneNumber != null and channel = ? order by timeIsUpdated desc, beginOrderTime"
		 * ; lstAllModel = controller.find(sql, new Object[] { (new
		 * Timestamp(System.currentTimeMillis() - (45l * 60l * 1000l))),
		 * user.getChannel() });
		 */
		if (user.getChannel() != null) {
			lstAllModel = TripManager.sharedInstance.getListOrderByChannel(
					ConfigUtil.getConfig("TIMEOUT_REFFRESH_DATA_DTV", CommonDefine.DURATION_VALID_TAXIORDER),
					user.getChannel());
		}
		this.filterOrder();
	}

	private void renderAllList() {
		synchronized (lastRefresh) {
			this.lstboxTop.setModel(new ListModelList<TaxiOrder>(lstTopModel));
			ListModelList<TaxiOrder> lstModelList = new ListModelList<TaxiOrder>(this.lstNewModel);
			lstModelList.setMultiple(true);
			this.lstboxNew.setModel(lstModelList);
			this.lstboxRegisted.setModel(new ListModelList<TaxiOrder>(this.lstRegistedModel));
			this.lstboxCancel.setModel(new ListModelList<TaxiOrder>(lstCancelModel));
			lastRefresh = System.currentTimeMillis();
		}
	}

	private void filterOrder() {
		List<TaxiOrder> lstRemove = new ArrayList<>();
		List<TaxiOrder> lstUp = new ArrayList<>();
		List<TaxiOrder> lstDown = new ArrayList<>();
		if (lstAllModel != null && lstAllModel.size() > 0) {
			for (TaxiOrder taxiOrder : lstAllModel) {
				if (taxiOrder.getPhoneNumber() == null || taxiOrder.getPhoneNumber().length() <= 0) {
					lstRemove.add(taxiOrder);
					continue;
				}
				if (taxiOrder.getBeginOrderTime().getTime() < (System.currentTimeMillis() - (45l * 60l * 1000l))) {
					lstRemove.add(taxiOrder);
					continue;
				}
				if (taxiOrder.getStatus() == EnumStatus.HUY.getValue()) {
					if (taxiOrder.getCancelTime() != null) {
						lstCancelModel.add(taxiOrder);
					}
				} else {
					if (taxiOrder.getIsUp()) {
						if (taxiOrder.getRegistedTaxis().size() > 0 || taxiOrder.getPickedTaxi() != null) {
							lstRegistedModel.add(taxiOrder);
						}
						if (taxiOrder.getStatus() != EnumStatus.XE_DA_DON.getValue()
								&& taxiOrder.getStatus() != EnumStatus.TRA_KHACH.getValue()) {
							if (taxiOrder.getRegistedTaxis().size() <= 0) {
								lstUp.add(taxiOrder);
							} else {
								lstDown.add(taxiOrder);
							}
						}
					} else {
						lstNewModel.add(taxiOrder);
					}
				}
			}
			this.sortListAllModelDesc(lstUp);
			this.sortListAllModelAsc(lstDown);
			this.lstTopModel.addAll(lstUp);
			this.lstTopModel.addAll(lstUp.size(), lstDown);
			this.sortListAllModelAsc(this.lstNewModel);
			this.sortListAllModelAsc(this.lstRegistedModel);
			this.sortListAllModelAsc(this.lstCancelModel);
			for (TaxiOrder taxiOrder : lstRemove) {
				this.lstAllModel.remove(taxiOrder);
			}
		}

	}

	protected void refreshMenList() {
		this.lstTopModel.clear();
		this.lstCancelModel.clear();
		this.lstNewModel.clear();
		this.lstRegistedModel.clear();
		this.filterOrder();
		this.renderAllList();
	}

	private void sortListAllModelAsc(List<TaxiOrder> lstTmp) {
		// Collections.sort(this.lstAllModel, compare);
		Collections.sort(lstTmp, new Comparator<TaxiOrder>() {

			@Override
			public int compare(TaxiOrder o1, TaxiOrder o2) {
				int result = 0;
				if (o1.getBeginOrderTime().getTime() < o2.getBeginOrderTime().getTime()) {
					result = 1;
				} else if (o1.getBeginOrderTime().getTime() > o2.getBeginOrderTime().getTime()) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
			}
		});
	}

	private void sortListAllModelDesc(List<TaxiOrder> lstTmp) {
		Collections.sort(lstTmp, new Comparator<TaxiOrder>() {

			@Override
			public int compare(TaxiOrder o1, TaxiOrder o2) {
				int result = 0;
				if (o1.getBeginOrderTime().getTime() > o2.getBeginOrderTime().getTime()) {
					result = 1;
				} else if (o1.getBeginOrderTime().getTime() < o2.getBeginOrderTime().getTime()) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
			}
		});
	}

	/**
	 * 
	 *
	 * @author VuD
	 * @param lstBox
	 * @param isPaging
	 * @param multiple
	 */
	private void initListboxTop(Listbox lstBox) {
		lstBox.setCheckmark(true);
		lstBox.setCheckmark(true);
		lstBox.setMold("paging");
		lstBox.setPageSize(100);
		lstBox.setPagingPosition("bottom");
		lstBox.setVflex("1");
		lstBox.setVflex(true);
		lstBox.setSizedByContent(false);
		lstBox.setCheckmark(true);
		lstBox.setNonselectableTags("");
		lstBox.setSclass("listbox_dieudam listbox_dieudam_row");
		lstBox.addEventListener(Events.ON_SELECT, this);
		lstBox.addEventListener(Events.ON_OK, this);
		lstBox.addEventListener(Events.ON_DOUBLE_CLICK, this);
		Listhead head = new Listhead();
		head.setParent(lstBox);
		head.setSizable(true);
		Listheader header = null;
		for (int i = 0; i < this.initColumnsTop().size(); i++) {
			GridColumn gridCol = this.initColumnsTop().get(i);
			header = new Listheader(gridCol.getHeader());
			header.setWidth(gridCol.getWidth() + "px");
			header.setStyle("max-width: " + gridCol.getWidth());
			header.setParent(head);
		}
		ListItemRendererDD renderer = new ListItemRendererDD((ArrayList<GridColumn>) this.initColumnsTop(), true, this);
		lstBox.setItemRenderer(renderer);
	}

	/**
	 * 
	 * @author VuD
	 * @param lstBox
	 */
	private void initListboxRegisted(Listbox lstBox) {
		lstBox.setCheckmark(true);
		lstBox.setCheckmark(true);
		lstBox.setMold("paging");
		lstBox.setPageSize(100);
		lstBox.setPagingPosition("bottom");
		lstBox.setVflex("1");
		lstBox.setVflex(true);
		lstBox.setSizedByContent(false);
		lstBox.setCheckmark(true);
		lstBox.setNonselectableTags("");
		lstBox.setSclass("listbox_dieudam listbox_dieudam_row");
		lstBox.addEventListener(Events.ON_SELECT, this);
		lstBox.addEventListener(Events.ON_CLICK, this);
		lstBox.addEventListener(Events.ON_DOUBLE_CLICK, this);
		Listhead head = new Listhead();
		head.setParent(lstBox);
		head.setSizable(true);
		Listheader header = null;
		for (int i = 0; i < this.initColumnsRegisted().size(); i++) {
			GridColumn gridCol = this.initColumnsRegisted().get(i);
			header = new Listheader(gridCol.getHeader());
			header.setWidth(gridCol.getWidth() + "px");
			header.setParent(head);
		}
		ListItemRendererDD renderer = new ListItemRendererDD((ArrayList<GridColumn>) this.initColumnsRegisted(), false,
				this);
		lstBox.setItemRenderer(renderer);
	}

	/**
	 * 
	 * @author VuD
	 * @param lstBox
	 */
	private void initListboxNew(Listbox lstBox) {
		lstBox.setCheckmark(true);
		lstBox.setMultiple(true);
		lstBox.setVflex("1");
		lstBox.setVflex(true);
		lstBox.setSizedByContent(false);
		lstBox.addEventListener(Events.ON_SELECT, this);
		lstBox.addEventListener(Events.ON_OK, this);
		Listhead head = new Listhead();
		head.setParent(lstBox);
		head.setSizable(true);
		Listheader header = null;
		for (int i = 0; i < this.initColumnsNew().size(); i++) {
			GridColumn gridCol = this.initColumnsNew().get(i);
			header = new Listheader(gridCol.getHeader());
			header.setWidth(gridCol.getWidth() + "px");
			header.setParent(head);
		}
		ListItemRendererDD renderer = new ListItemRendererDD((ArrayList<GridColumn>) this.initColumnsNew(), false,
				this);
		lstBox.setItemRenderer(renderer);
	}

	private void initListboxCancel(Listbox lstBox) {
		lstBox.setCheckmark(true);
		lstBox.setMultiple(true);
		lstBox.setVflex("1");
		lstBox.setVflex(true);
		lstBox.setSizedByContent(false);
		lstBox.addEventListener(Events.ON_SELECT, this);
		Listhead head = new Listhead();
		head.setParent(lstBox);
		head.setSizable(true);
		Listheader header = null;
		for (int i = 0; i < this.initColumnsCancel().size(); i++) {
			GridColumn gridCol = this.initColumnsCancel().get(i);
			header = new Listheader(gridCol.getHeader());
			header.setWidth(gridCol.getWidth() + "px");
			header.setParent(head);
		}
		ListItemRendererDD renderer = new ListItemRendererDD((ArrayList<GridColumn>) this.initColumnsCancel(), false,
				this);

		lstBox.setItemRenderer(renderer);

	}

	private List<GridColumn> initColumnsTop() {
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Số điện thoại", 140, String.class, "getPhoneNumber"));
		lstCols.add(new GridColumn("Loại xe", 60, Integer.class, "getOrderCarType", "orderCarType", TaxiOrder.class));
		lstCols.add(new GridColumn("Địa chỉ yêu cầu", 250, String.class, "getBeginOrderAddress"));
		lstCols.add(new GridColumn("Xe đăng ký đón", 350, String.class, "getRegistedTaxis"));
		lstCols.add(new GridColumn("Ghi chú", 200, String.class, "getNote"));
		lstCols.add(new GridColumn("Giờ yêu cầu", 80, Timestamp.class, "getTimeBeginOrder"));
		lstCols.add(new GridColumn("Trạng thái", 110, Integer.class, "getStatus", "status", TaxiOrder.class));
		lstCols.add(new GridColumn("Loại yêu cầu", 100, Integer.class, "getOrderType", "orderType", TaxiOrder.class));
		lstCols.add(new GridColumn("Đón nhanh", 80, null, null));
		return lstCols;
	}

	private List<GridColumn> initColumnsRegisted() {
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Điện thoại viên", 200, User.class, "getUser"));
		lstCols.add(new GridColumn("Số điện thoại", 140, String.class, "getPhoneNumber"));
		lstCols.add(new GridColumn("Loại xe", 60, Integer.class, "getOrderCarType", "orderCarType", TaxiOrder.class));
		lstCols.add(new GridColumn("Địa chỉ yêu cầu", 300, String.class, "getBeginOrderAddress"));
		lstCols.add(new GridColumn("Xe đăng ký đón", 280, String.class, "getRegistedTaxis"));
		lstCols.add(new GridColumn("Xe đón", 150, Vehicle.class, "getPickedTaxi"));
		lstCols.add(new GridColumn("Ghi chú", 200, String.class, "getNote"));
		lstCols.add(new GridColumn("Giờ yêu cầu", 80, Timestamp.class, "getTimeBeginOrder"));
		lstCols.add(new GridColumn("Trạng thái", 110, Integer.class, "getStatus", "status", TaxiOrder.class));
		lstCols.add(new GridColumn("Loại yêu cầu", 100, Integer.class, "getOrderType", "orderType", TaxiOrder.class));
		return lstCols;
	}

	private List<GridColumn> initColumnsNew() {
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Điện thoại viên", 200, User.class, "getUser"));
		lstCols.add(new GridColumn("Số điện thoại", 100, String.class, "getPhoneNumber"));
		lstCols.add(new GridColumn("Địa chỉ yêu cầu", 350, String.class, "getBeginOrderAddress"));
		lstCols.add(new GridColumn("Loại xe", 100, Integer.class, "getOrderCarType", "orderCarType", TaxiOrder.class));
		lstCols.add(new GridColumn("Xe đăng ký đón", 300, String.class, "getRegistedTaxis"));
		lstCols.add(new GridColumn("Giờ yêu cầu", 150, Timestamp.class, "getTimeBeginOrder"));
		// lstCols.add(new GridColumn("Giờ gọi xe", 150, Timestamp.class,
		// "getTimeOrder"));
		lstCols.add(new GridColumn("Loại yêu cầu", 100, Integer.class, "getOrderType", "orderType", TaxiOrder.class));
		return lstCols;
	}

	private List<GridColumn> initColumnsCancel() {
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Điện thoại viên", 200, User.class, "getUser"));
		lstCols.add(new GridColumn("Số điện thoại", 100, String.class, "getPhoneNumber"));
		lstCols.add(new GridColumn("Địa chỉ yêu cầu", 350, String.class, "getBeginOrderAddress"));
		lstCols.add(new GridColumn("Loại xe", 100, Integer.class, "getOrderCarType", "orderCarType", TaxiOrder.class));
		lstCols.add(new GridColumn("Xe đăng ký đón", 300, String.class, "getRegistedTaxis"));
		lstCols.add(new GridColumn("Giờ yêu cầu", 150, Timestamp.class, "getTimeBeginOrder"));
		// lstCols.add(new GridColumn("Giờ gọi xe", 150, Timestamp.class,
		// "getTimeOrder"));
		lstCols.add(
				new GridColumn("Lý do hủy", 100, Integer.class, "getCancelReason", "cancelReason", TaxiOrder.class));
		lstCols.add(new GridColumn("Loại yêu cầu", 100, Integer.class, "getOrderType", "orderType", TaxiOrder.class));
		return lstCols;
	}

	private void displayButtonForTop() {
		this.btnChuyen.setDisabled(true);
		this.btnDKDon.setDisabled(false);
		this.btnDDKhach.setDisabled(false);
		this.btnGKRXe.setDisabled(false);
		this.btnHLDiem.setDisabled(false);
		this.btnHuy.setDisabled(false);
		this.btnCKenh.setDisabled(false);
	}

	private void displayButtonForAction() {
		if (currentModel != null) {
			if (currentModel.getPickedTaxi() != null) {
				this.btnChuyen.setDisabled(true);
				this.btnDKDon.setDisabled(true);
				this.btnDDKhach.setDisabled(true);
				this.btnGKRXe.setDisabled(true);
				this.btnHLDiem.setDisabled(true);
				this.btnHuy.setDisabled(true);
				this.btnCKenh.setDisabled(true);
			} else {
				this.btnChuyen.setDisabled(true);
				this.btnDKDon.setDisabled(false);
				this.btnDDKhach.setDisabled(false);
				this.btnGKRXe.setDisabled(false);
				this.btnHLDiem.setDisabled(false);
				this.btnHuy.setDisabled(false);
				this.btnCKenh.setDisabled(false);
			}
		}
	}

	private void displayButtonForNew() {
		this.btnChuyen.setDisabled(false);
		this.btnDKDon.setDisabled(true);
		this.btnDDKhach.setDisabled(true);
		this.btnGKRXe.setDisabled(true);
		this.btnHLDiem.setDisabled(true);
		this.btnHuy.setDisabled(false);
		this.btnCKenh.setDisabled(false);
	}

	private void displayButtonForCancel() {
		this.btnChuyen.setDisabled(true);
		this.btnDKDon.setDisabled(true);
		this.btnDDKhach.setDisabled(true);
		this.btnHLDiem.setDisabled(true);
		this.btnCKenh.setDisabled(true);
		this.btnGKRXe.setDisabled(true);
		this.btnHuy.setDisabled(true);
	}

	private void handleHuyCuoc() throws NoSuchFieldException, SecurityException {
		if (isInAction) {
			return;
		}
		isInAction = true;
		final Set<Listitem> setSelected = lstboxNew.getSelectedItems();
		if (currentModel == null && setSelected.size() <= 0) {
			Env.getHomePage().showNotification("Bạn chưa yêu cầu để hủy", Clients.NOTIFICATION_TYPE_WARNING);
			return;
		}
		if (currentModel != null) {
			if (currentModel.getPickedTaxi() != null) {
				return;
			}
		}
		final Window window = new Window();
		window.setClosable(true);
		window.setMaximizable(true);
		window.setParent(this);
		window.setVisible(true);
		window.setPosition("right, bottom");
		window.doModal();
		window.setTitle("Lý do hủy cuốc");

		Vlayout vlayout = new Vlayout();
		vlayout.setParent(window);
		vlayout.setHflex("1");
		vlayout.setVflex("1");
		Hlayout hlayout = new Hlayout();
		hlayout.setParent(vlayout);
		hlayout.setHflex("1");
		Field field = TaxiOrder.class.getDeclaredField("cancelReason");
		Editor editor = EditorFactory.getFixedCombobox(currentModel, field);
		final Combobox combobox = (Combobox) editor.getComponent();
		combobox.setHflex("1");
		combobox.setParent(hlayout);
		if (combobox.getItemCount() > 0) {
			combobox.setSelectedIndex(0);
		}
		Hlayout bottom = new Hlayout();
		// bottom.setStyle("padding: 5px");
		bottom.setParent(vlayout);
		bottom.setValign("center");
		Button btnSave = new Button();
		btnSave.setParent(bottom);
		btnSave.setSclass("btn-success");
		btnSave.setLabel("Lưu");
		btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				handleHuyCuocSave(setSelected, combobox, window);
			}
		});

		Button btnCancel = new Button("Đóng");
		btnCancel.setParent(bottom);

		window.addEventListener(Events.ON_OK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				handleHuyCuocSave(setSelected, combobox, window);
			}
		});
		btnCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				window.detach();
				isInAction = false;
			}
		});

		window.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				window.detach();
				isInAction = false;
			}
		});

		window.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				isInAction = false;
				window.detach();
			}
		});

	}

	private void handleHuyCuocSave(Set<Listitem> setSelected, Combobox combobox, Window window) {
		try {
			Comboitem item = combobox.getSelectedItem();
			if (item == null) {
				Env.getHomePage().showNotification("Bạn chưa chọn lý do hủy", Clients.NOTIFICATION_TYPE_INFO);
			} else {
				int value = item.getValue();
				if (currentModel != null) {
					currentModel.setCancelReason(value);
					currentModel.setStatus(EnumStatus.HUY.getValue());
					currentModel.setCancelTime(new Timestamp(System.currentTimeMillis()));
					// currentModel.setIsUp(false);
					// currentModel.save();
					TripManager.sharedInstance.saveTrip(currentModel);
					for (Vehicle vehicle : currentModel.getRegistedTaxis()) {
						updatePickupTaxiStatus(vehicle);
					}
					// removeFromListAllModel(currentModel);
					SendCancelSmsProcessor sendCancelSmsProcessor = new SendCancelSmsProcessor(
							currentModel.getCustomer(), currentModel.getRegistedTaxis());
					sendCancelSmsProcessor.start();
					Env.getHomePage().showNotification("Đã hủy cuốc thành công", Clients.NOTIFICATION_TYPE_INFO);
					currentModel = null;
					// this.fireEventRefreshForDTV(currentModel);
					notifyCancelOrder(currentModel);
				} else {
					if (setSelected.size() > 0) {
						List<TaxiOrder> lstModelEvent = new ArrayList<TaxiOrder>();
						for (Listitem listitem : setSelected) {
							TaxiOrder orderTmp = listitem.getValue();
							orderTmp.setCancelReason(value);
							orderTmp.setCancelTime(new Timestamp(System.currentTimeMillis()));
							orderTmp.setStatus(0);
							orderTmp.setIsUp(false);
							// orderTmp.save();
							TripManager.sharedInstance.saveTrip(orderTmp);
							for (Vehicle vehicle : orderTmp.getRegistedTaxis()) {
								updatePickupTaxiStatus(vehicle);
							}
							lstModelEvent.add(orderTmp);
							removeFromListAllModel(orderTmp);
							notifyCancelOrder(orderTmp);
						}
						Env.getHomePage().showNotification(
								"Đã hủy cuốc thành công cho " + setSelected.size() + " yêu cầu",
								Clients.NOTIFICATION_TYPE_INFO);
						// this.fireEventRefeshForDTV(lstModelEvent);

					}

				}
				isInAction = false;
				window.detach();
				// refreshAllList();
				// refreshMenList();
				currentModel = null;
				Events.echoEvent("focusListBox", this, this.lstboxTop);
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrderDD|HuyCuoc|", e);
			refreshAllList();
			isInAction = false;
			window.detach();
		}

	}

	private void handleChuyen() throws Exception {
		Set<Listitem> setSelected = lstboxNew.getSelectedItems();
		if (setSelected.size() <= 0) {
			Env.getHomePage().showNotification("Cần phải chọn yêu cầu mới", Clients.NOTIFICATION_TYPE_WARNING);
			return;
		}
		List<TaxiOrder> lstModelEvent = new ArrayList<TaxiOrder>();
		for (Listitem listitem : setSelected) {
			TaxiOrder orderTmp = listitem.getValue();
			lstModelEvent.add(orderTmp);
			orderTmp.setIsUp(true);
			orderTmp.setTimeIsUpdated(new Timestamp(System.currentTimeMillis()));
			orderTmp.setStatus(EnumStatus.DA_DOC_DAM.getValue());
			// orderTmp.save();
			TripManager.sharedInstance.saveTrip(orderTmp);
		}
		// this.refreshMenList();
		Events.echoEvent("focusListBox", this, this.lstboxTop);
		// fireEventRefeshForDTV(lstModelEvent);
		Env.getHomePage().showNotification("Đã chuyển " + setSelected.size() + " yêu cầu thành công",
				Clients.NOTIFICATION_TYPE_INFO);
		// this.refreshAllList();
	}

	private void handleGoiKhachRaXe() throws Exception {
		if (currentModel.getUser() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("Gọi khách ra xe: ");
			sb.append(currentModel.getPhoneNumber());
			sb.append("(").append(currentModel.getId()).append(")");
			ChatWindow.sendRequest(currentModel.getUser(), sb.toString());
		}
	}

	private void handleHoiLaiDiem() throws Exception {
		if (currentModel.getUser() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("Hỏi lại điểm: ");
			sb.append(currentModel.getPhoneNumber());
			sb.append("(").append(currentModel.getId()).append(")");
			ChatWindow.sendRequest(currentModel.getUser(), sb.toString());
		}
	}

	private void notifyCancelOrder(TaxiOrder order) {
		if (order.getUser() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("Hủy cuốc: ");
			sb.append(order.getPhoneNumber());
			sb.append("\nLý do hủy: ");
			if (order.getCancelReason().equals(EnumCancelReason.DON_KHACH_KHAC.getValue())) {
				sb.append(EnumCancelReason.DON_KHACH_KHAC.getReason());
			} else if (order.getCancelReason().equals(EnumCancelReason.KHONG_DON_DUOC_KHACH.getValue())) {
				sb.append(EnumCancelReason.KHONG_DON_DUOC_KHACH.getReason());
			} else if (order.getCancelReason().equals(EnumCancelReason.KHACH_HANG_HUY.getValue())) {
				sb.append(EnumCancelReason.KHACH_HANG_HUY.getReason());
			} else if (order.getCancelReason().equals(EnumCancelReason.KHONG_CO_TAI_XE.getValue())) {
				sb.append(EnumCancelReason.KHONG_CO_TAI_XE.getReason());
			} else if (order.getCancelReason().equals(EnumCancelReason.LY_DO_KHAC.getValue())) {
				sb.append(EnumCancelReason.LY_DO_KHAC.getReason());
			}
			sb.append("(").append(order.getId()).append(")");
			ChatWindow.sendRequest(order.getUser(), sb.toString());
		}
	}

	private void handleDangKyDon() throws Exception {
		if (isInAction) {
			return;
		}
		try {
			isInAction = true;
			final List<Vehicle> lstVehicleNew = Arrays.asList(null, null, null, null, null);
			final Set<Vehicle> setVehicleOld = currentModel.getRegistedTaxis();
			int i = 0;
			for (Vehicle vehicle : setVehicleOld) {
				lstVehicleNew.set(i, vehicle);
				i++;
			}
			final Window window = new Window();
			window.setClosable(true);
			window.setMaximizable(true);
			window.setWidth("400px");
			window.setPosition("right, bottom");
			window.setParent(this);
			window.setVisible(true);
			window.doModal();
			window.setTitle("Xe đăng ký đón");

			Listbox lstbox = new Listbox();
			lstbox.setParent(window);
			Listhead lsthead = new Listhead();
			lsthead.setParent(lstbox);
			Listheader lstheader = new Listheader();
			lstheader.setParent(lsthead);
			lstheader.setLabel("Số tài");
			lstheader.setHflex("30%");
			lstheader = new Listheader();
			lstheader.setParent(lsthead);
			lstheader.setLabel("Loại xe");
			lstheader.setHflex("60%");
			lstheader = new Listheader();
			lstheader.setParent(lsthead);
			lstheader.setLabel("Xóa");
			lstheader.setHflex("20%");

			final List<Textbox> lstVehicleNumberCom = new ArrayList<Textbox>();
			final Textbox tb_vh_1 = new Textbox();
			tb_vh_1.setHflex("1");
			tb_vh_1.setFocus(true);
			lstVehicleNumberCom.add(tb_vh_1);
			final Textbox tb_vh_2 = new Textbox();
			tb_vh_2.setHflex("1");
			lstVehicleNumberCom.add(tb_vh_2);
			final Textbox tb_vh_3 = new Textbox();
			tb_vh_3.setHflex("1");
			lstVehicleNumberCom.add(tb_vh_3);
			final Textbox tb_vh_4 = new Textbox();
			tb_vh_4.setHflex("1");
			lstVehicleNumberCom.add(tb_vh_4);
			final Textbox tb_vh_5 = new Textbox();
			tb_vh_5.setHflex("1");
			lstVehicleNumberCom.add(tb_vh_5);

			final List<Button> lstBtn = new ArrayList<Button>();
			final Button btnDel1 = new Button();
			btnDel1.setSclass("btn-dd-dangkidon-del");
			btnDel1.setImage("/themes/images/DeleteRed_12.png");
			btnDel1.setVisible(false);
			lstBtn.add(btnDel1);
			final Button btnDel2 = new Button();
			btnDel2.setSclass("btn-dd-dangkidon-del");
			btnDel2.setImage("/themes/images/DeleteRed_12.png");
			btnDel2.setVisible(false);
			lstBtn.add(btnDel2);
			final Button btnDel3 = new Button();
			btnDel3.setSclass("btn-dd-dangkidon-del");
			btnDel3.setImage("/themes/images/DeleteRed_12.png");
			btnDel3.setVisible(false);
			lstBtn.add(btnDel3);
			final Button btnDel4 = new Button();
			btnDel4.setSclass("btn-dd-dangkidon-del");
			btnDel4.setImage("/themes/images/DeleteRed_12.png");
			btnDel4.setVisible(false);
			lstBtn.add(btnDel4);
			final Button btnDel5 = new Button();
			btnDel5.setSclass("btn-dd-dangkidon-del");
			btnDel5.setImage("/themes/images/DeleteRed_12.png");
			btnDel5.setVisible(false);
			lstBtn.add(btnDel5);
			final List<Textbox> lstTypeVehicleCom = new ArrayList<Textbox>();
			final Textbox tb_tp_1 = new Textbox();
			tb_tp_1.setHflex("1");
			tb_tp_1.setFocus(false);
			tb_tp_1.setReadonly(true);
			lstTypeVehicleCom.add(tb_tp_1);
			final Textbox tb_tp_2 = new Textbox();
			tb_tp_2.setHflex("1");
			tb_tp_2.setReadonly(true);
			lstTypeVehicleCom.add(tb_tp_2);
			final Textbox tb_tp_3 = new Textbox();
			tb_tp_3.setHflex("1");
			tb_tp_3.setReadonly(true);
			lstTypeVehicleCom.add(tb_tp_3);
			final Textbox tb_tp_4 = new Textbox();
			tb_tp_4.setHflex("1");
			tb_tp_4.setReadonly(true);
			lstTypeVehicleCom.add(tb_tp_4);
			final Textbox tb_tp_5 = new Textbox();
			tb_tp_5.setHflex("1");
			tb_tp_5.setReadonly(true);
			lstTypeVehicleCom.add(tb_tp_5);

			i = 0;
			for (Vehicle vehicle : lstVehicleNew) {
				if (vehicle != null) {
					Textbox tmp = lstVehicleNumberCom.get(i);
					tmp.setValue(vehicle.getValue());
					tmp = lstTypeVehicleCom.get(i);
					i++;
				}
			}

			Listitem lstItem = new Listitem();
			lstItem.setParent(lstbox);
			Listcell lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(tb_vh_1);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(tb_tp_1);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(btnDel1);

			lstItem = new Listitem();
			lstItem.setParent(lstbox);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(tb_vh_2);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(tb_tp_2);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(btnDel2);

			lstItem = new Listitem();
			lstItem.setParent(lstbox);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(tb_vh_3);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(tb_tp_3);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(btnDel3);

			lstItem = new Listitem();
			lstItem.setParent(lstbox);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(tb_vh_4);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(tb_tp_4);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(btnDel4);

			lstItem = new Listitem();
			lstItem.setParent(lstbox);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(tb_vh_5);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(tb_tp_5);
			lstCell = new Listcell();
			lstCell.setParent(lstItem);
			lstCell.appendChild(btnDel5);

			// Set cac giai tri khoi tao cho truong
			int k = 1;
			for (Vehicle vehicle2 : setVehicleOld) {
				if (vehicle2 != null) {
					if (k == 1) {
						tb_vh_1.setValue(vehicle2.getFullValue());
						tb_tp_1.setValue(vehicle2.getTaxiType().getName());
						btnDel1.setVisible(true);
					} else if (k == 2) {
						tb_vh_2.setValue(vehicle2.getFullValue());
						tb_tp_2.setValue(vehicle2.getTaxiType().getName());
						btnDel2.setVisible(true);
					} else if (k == 3) {
						tb_vh_3.setValue(vehicle2.getFullValue());
						tb_tp_3.setValue(vehicle2.getTaxiType().getName());
						btnDel3.setVisible(true);
					} else if (k == 4) {
						tb_vh_4.setValue(vehicle2.getFullValue());
						tb_tp_4.setValue(vehicle2.getTaxiType().getName());
						btnDel4.setVisible(true);
					} else if (k == 5) {
						tb_vh_5.setValue(vehicle2.getFullValue());
						tb_tp_5.setValue(vehicle2.getTaxiType().getName());
						btnDel5.setVisible(true);
					}
					k++;
				}
			}
			switch (k) {
			case 2:
				Events.echoEvent("setFocusComponent", this, tb_vh_2);
				break;
			case 3:
				Events.echoEvent("setFocusComponent", this, tb_vh_3);
				break;
			case 4:
				Events.echoEvent("setFocusComponent", this, tb_vh_4);
				break;
			case 5:
				Events.echoEvent("setFocusComponent", this, tb_vh_5);
				break;
			default:
				Events.echoEvent("setFocusComponent", this, tb_vh_1);
				break;

			}

			final Button btnCancel = new Button("Thoát");
			btnCancel.setParent(window);
			btnCancel.setSclass("btn-danger");
			btnCancel.setStyle("width:100%;text-align:center;");
			btnCancel.setVisible(true);

			tb_vh_1.addEventListener(Events.ON_OK, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					subhandleDangKyDonDetail(lstVehicleNew, 0, lstVehicleNumberCom, lstTypeVehicleCom, lstBtn,
							btnCancel);
				}
			});
			tb_vh_2.addEventListener(Events.ON_OK, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					subhandleDangKyDonDetail(lstVehicleNew, 1, lstVehicleNumberCom, lstTypeVehicleCom, lstBtn,
							btnCancel);
				}
			});
			tb_vh_3.addEventListener(Events.ON_OK, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					subhandleDangKyDonDetail(lstVehicleNew, 2, lstVehicleNumberCom, lstTypeVehicleCom, lstBtn,
							btnCancel);
				}
			});
			tb_vh_4.addEventListener(Events.ON_OK, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					subhandleDangKyDonDetail(lstVehicleNew, 3, lstVehicleNumberCom, lstTypeVehicleCom, lstBtn,
							btnCancel);
				}
			});
			tb_vh_5.addEventListener(Events.ON_OK, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					subhandleDangKyDonDetail(lstVehicleNew, 4, lstVehicleNumberCom, lstTypeVehicleCom, lstBtn,
							btnCancel);
				}
			});

			btnDel1.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					subhandleDanKyDonNutXoa(lstVehicleNew, 0, tb_vh_1, tb_tp_1, btnDel1);
					tb_vh_1.focus();
					tb_vh_1.select();
				}
			});

			btnDel2.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					subhandleDanKyDonNutXoa(lstVehicleNew, 1, tb_vh_2, tb_tp_2, btnDel2);
					tb_vh_2.focus();
					tb_vh_2.select();
				}
			});

			btnDel3.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					subhandleDanKyDonNutXoa(lstVehicleNew, 2, tb_vh_3, tb_tp_3, btnDel3);
					tb_vh_3.focus();
					tb_vh_3.select();
				}
			});

			btnDel4.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					subhandleDanKyDonNutXoa(lstVehicleNew, 3, tb_vh_4, tb_tp_4, btnDel4);
					tb_vh_4.focus();
					tb_vh_4.select();
				}
			});

			btnDel5.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					subhandleDanKyDonNutXoa(lstVehicleNew, 4, tb_vh_5, tb_tp_5, btnDel5);
					tb_vh_5.focus();
					tb_vh_5.select();
				}
			});

			btnCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					handleDangKyDonSave(window, setVehicleOld, lstVehicleNew);
				}
			});

			window.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					handleDangKyDonSave(window, setVehicleOld, lstVehicleNew);
				}
			});

			window.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					handleDangKyDonSave(window, setVehicleOld, lstVehicleNew);
				}
			});
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrdersDD|HandleDangKyDon|", e);
			isInAction = false;
		}

	}

	private void handleDangKyDonSave(Window window, Set<Vehicle> setVehicleOld, List<Vehicle> lstVehicleNew) {
		try {
			List<Vehicle> lstOld = new ArrayList<>();
			for (Vehicle vehicle : setVehicleOld) {
				lstOld.add(vehicle);
			}
			boolean isCloseNow = true;
			if (setVehicleOld.size() == 0) {
				for (Vehicle vehicle : lstVehicleNew) {
					if (vehicle != null) {
						isCloseNow = false;
						break;
					}
				}
			} else {
				isCloseNow = false;
			}
			if (isCloseNow) {
				isInAction = false;
				window.detach();
				return;
			}
			List<Vehicle> lstVehicleSms = new ArrayList<>();
			for (Vehicle vehicle : lstVehicleNew) {
				if (vehicle != null) {
					if (!setVehicleOld.contains(vehicle)) {
						lstVehicleSms.add(vehicle);
					}
				}
			}
			setVehicleOld.clear();
			boolean isNone = true;
			for (Vehicle vehicle : lstVehicleNew) {
				if (vehicle != null) {
					setVehicleOld.add(vehicle);
					isNone = false;
				}
			}
			if (isNone) {
				currentModel.setStatus(EnumStatus.DA_DOC_DAM.getValue());
			} else {
				currentModel.setStatus(EnumStatus.XE_DANG_KY_DON.getValue());
			}
			if (currentModel.getStartRegisterTime() == null) {
				currentModel.setStartRegisterTime(new Timestamp(System.currentTimeMillis()));
			}
			try {
				// currentModel.save();
				SendSMSForCustomerProcessor ssfcp = new SendSMSForCustomerProcessor(
						EnumStatus.XE_DANG_KY_DON.getValue(), currentModel.getId(), lstVehicleSms);
				ssfcp.start();
				for (Vehicle vehicle : currentModel.getRegistedTaxis()) {
					if (lstOld.contains(vehicle)) {
						lstOld.remove(vehicle);
					}
					this.updateRegistedStatus(vehicle);
				}
				for (Vehicle vehicle : lstOld) {
					updatePickupTaxiStatus(vehicle);
				}
				this.updateListAllModel(currentModel);
			} catch (Exception e) {
				Env.getHomePage().showNotification("Luu bi loi", Clients.NOTIFICATION_TYPE_ERROR);
				AppLogger.logDebug.error("TaxiOrderDD|DangKyDon|Luu ban tin", e);
				e.printStackTrace();
			}
			isInAction = false;
			window.detach();
			// fireEventRefreshForDTV(currentModel);
			// refreshAllList();
			refreshMenList();
			// currentModel = null;
			Events.echoEvent("focusRowOfListbox", this, null);
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrderDD|HandleDangKyDonSave", e);
			isInAction = false;
			window.detach();
		}

	}

	private void subhandleDanKyDonNutXoa(List<Vehicle> lstVehicleNew, int i, Textbox tb_vh, Textbox tb_tp,
			Button btnDel) {
		tb_vh.setValue("");
		tb_tp.setValue("");
		btnDel.setVisible(false);
		lstVehicleNew.set(i, null);
	}

	protected void subhandleDangKyDonDetail(final List<Vehicle> lstVehicleNew, final int i,
			final List<Textbox> lstVehicleNumberCom, final List<Textbox> lstTypeVehicleCom, final List<Button> lstBtn,
			final Button btnCancel) {
		AppLogger.logDebug.info("Dang ky don detail");
		final Textbox tb_vh = lstVehicleNumberCom.get(i);
		final Textbox tb_tp = lstTypeVehicleCom.get(i);
		final Button btnDel = lstBtn.get(i);
		String value = tb_vh.getValue();
		if (value == null || value.trim().length() <= 0) {
			lstVehicleNew.set(i, null);
			tb_tp.setValue("");
			btnDel.setVisible(false);
			if (i < 4) {
				lstVehicleNumberCom.get(i + 1).setFocus(true);
			} else {
				btnCancel.setFocus(true);
			}
		} else {
			value = value.trim();
			if (IntegerUtils.isString2Integer(value)) {
				if (this.checkExistDangKyDonDetail(value, lstVehicleNew)) {
					Env.getHomePage().showNotification("Bạn đã chọn xe này", Clients.NOTIFICATION_TYPE_WARNING);
					lstVehicleNew.set(i, null);
					tb_tp.setValue("");
				} else {
					VehicleDD vehicleTmp = (VehicleDD) MapCommon.MAP_VEHICLE_INFO.get(value);
					if (vehicleTmp != null) {
						final Vehicle vehicle = new Vehicle(vehicleTmp.getVehicle());
						if (checkRegistedOther(vehicle)) {
							lstVehicleNew.set(i, null);
							tb_tp.setValue("");
							tb_vh.setValue("");
							btnDel.setVisible(false);
							tb_vh.focus();
							Env.getHomePage().showNotification("Xe đã đăng ký đón cuốc khác",
									Clients.NOTIFICATION_TYPE_WARNING);
						} else {
							lstVehicleNew.set(i, vehicle);
							tb_tp.setValue(vehicle.getTaxiType().getName());
							btnDel.setVisible(true);
							if (i < 4) {
								lstVehicleNumberCom.get(i + 1).setFocus(true);
							} else {
								btnCancel.setFocus(true);
							}
							tb_vh.setValue(vehicle.getFullValue());
						}
					} else {
						List<VehicleDD> lstVehicleDD = (List<VehicleDD>) MapCommon.MAP_LIST_VEHICLE_INFO.get(value);
						if (lstVehicleDD != null) {
							lstVehicleNew.set(i, null);
							tb_tp.setValue("");
							btnDel.setVisible(false);
							final Popup pop = new Popup();
							pop.setParent(tb_vh.getParent());
							pop.setStyle("position: overlap");
							final Combobox combo = new Combobox();
							combo.setParent(pop);
							combo.setHflex("1");
							combo.setVflex("1");
							int index = 0;
							for (int j = 0; j < lstVehicleDD.size(); j++) {
								Vehicle vehicle = lstVehicleDD.get(j).getVehicle();
								Comboitem item = new Comboitem();
								item.setParent(combo);
								item.setLabel(vehicle.toString());
								item.setValue(vehicle);
								if (vehicle.getFullValue().startsWith(Env.getCompanyValue())) {
									index = j;
								}
							}
							combo.setFocus(true);
							combo.setSelectedIndex(index);
							combo.addEventListener(Events.ON_OK, new EventListener<Event>() {
								@Override
								public void onEvent(Event arg0) throws Exception {
									if (combo.getSelectedIndex() >= 0) {
										final Vehicle vehicleTmp = combo.getSelectedItem().getValue();
										if (vehicleTmp != null) {
											if (checkRegistedOther(vehicleTmp)) {
												lstVehicleNew.set(i, null);
												tb_tp.setValue("");
												tb_vh.setValue("");
												tb_vh.focus();
												btnDel.setVisible(false);
												Env.getHomePage().showNotification("Xe đã đăng ký đón cuốc khác",
														Clients.NOTIFICATION_TYPE_WARNING);
											} else {
												lstVehicleNew.set(i, vehicleTmp);
												tb_tp.setValue(vehicleTmp.getTaxiType().getName());
												tb_vh.setValue(vehicleTmp.getFullValue());
												btnDel.setVisible(true);
												pop.close();
												if (i < 4) {
													lstVehicleNumberCom.get(i + 1).setFocus(true);
												} else {
													btnCancel.setFocus(true);
												}
											}

										}
									} else {
										lstVehicleNew.set(i, null);
										tb_tp.setValue("");
										tb_vh.setValue("");
										btnDel.setVisible(false);
										tb_vh.focus();
										Env.getHomePage().showNotification("Bạn chưa chọn xe",
												Clients.NOTIFICATION_TYPE_WARNING);
									}
								}
							});
							pop.open(tb_vh.getParent(), "overlap");
						} else {
							Env.getHomePage().showNotification("Không có xe này", Clients.NOTIFICATION_TYPE_WARNING);
							tb_vh.setValue("");
							tb_tp.setValue("");
							lstVehicleNew.set(i, null);
							btnDel.setVisible(false);
						}
					}
				}
			} else {
				String tmp = value.toUpperCase();
				if (this.checkExistDangKyDonDetail(value, lstVehicleNew)) {
					Env.getHomePage().showNotification("Bạn đã chọn xe này", Clients.NOTIFICATION_TYPE_WARNING);
					lstVehicleNew.set(i, null);
					tb_tp.setValue("");
				} else {
					VehicleDD vehicleDD = (VehicleDD) MapCommon.MAP_VEHICLE_INFO_FULL.get(tmp);
					if (vehicleDD != null) {
						if (!checkRegistedOther(vehicleDD.getVehicle())) {
							lstVehicleNew.set(i, vehicleDD.getVehicle());
							tb_tp.setValue(vehicleDD.getVehicleType());
							btnDel.setVisible(true);
							if (i < 4) {
								lstVehicleNumberCom.get(i + 1).setFocus(true);
							} else {
								btnCancel.setFocus(true);
							}
							tb_vh.setValue(vehicleDD.getFullName());
						} else {
							Env.getHomePage().showNotification("Xe đã đăng ký đón cuốc khác",
									Clients.NOTIFICATION_TYPE_WARNING);
							tb_vh.setValue("");
							tb_tp.setValue("");
							lstVehicleNew.set(i, null);
							btnDel.setVisible(false);
						}
					} else {
						Env.getHomePage().showNotification("Không có xe này", Clients.NOTIFICATION_TYPE_WARNING);
						tb_vh.setValue("");
						tb_tp.setValue("");
						lstVehicleNew.set(i, null);
						btnDel.setVisible(false);
					}
				}

			}

		}
	}

	protected void subhandleOkDangKyDon(final List<Vehicle> lstVehicleNew, VehicleController vehicleController,
			final int i, final Textbox tb_vh, final Textbox tb_tp, final Button btnDel) {

	}

	/**
	 * Kiem tra xe da dang ky don o xe khac chua
	 * 
	 * @param vehicle
	 * @return true neu khong du dieu kien, false neu du dieu kien
	 */
	private boolean checkRegistedOther(Vehicle vehicle) {
		boolean result = false;
		VehicleStatusDD vDd = (VehicleStatusDD) MapCommon.MAP_VEHICLE_STATUS_ID.get(vehicle.getId() + "");
		if (vDd != null) {
			if (!vDd.isFree()) {
				if (vDd.getLastRegisted() > 0) {
					if ((System.currentTimeMillis() - vDd.getLastRegisted()) < AppConstant.TIME_OUT_REGISTED) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	private void updateRegistedStatus(Vehicle vehicle) {
		try {
			if (vehicle == null) {
				return;
			}
			VehicleStatusDD vDd = (VehicleStatusDD) MapCommon.MAP_VEHICLE_STATUS_ID.get(vehicle.getId() + "");
			if (vDd == null) {
				vDd = new VehicleStatusDD();
				vDd.setVehicleId(vehicle.getId());
				vDd.setLastRegisted(System.currentTimeMillis());
				vDd.setFree(false);
				MapCommon.MAP_VEHICLE_STATUS_ID.put(vehicle.getId() + "", vDd);
			} else {
				vDd.setLastRegisted(System.currentTimeMillis());
				vDd.setFree(false);
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrderDD|UpdateRegistedStatus", e);
		}
	}

	protected void updatePickupTaxiStatus(Vehicle vehicle) {
		try {
			if (vehicle == null) {
				return;
			}
			VehicleStatusDD vDd = (VehicleStatusDD) MapCommon.MAP_VEHICLE_STATUS_ID.get(vehicle.getId() + "");
			if (vDd == null) {
				vDd = new VehicleStatusDD();
				vDd.setVehicleId(vehicle.getId());
				vDd.setFree(true);
				MapCommon.MAP_VEHICLE_STATUS_ID.put(vehicle.getId() + "", vDd);
			} else {
				vDd.setFree(true);
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrderDD|UpdatePickupTaxiStatus", e);
		}
	}

	private boolean checkExistDangKyDonDetail(String value, List<Vehicle> lstVehicleNew) {
		if (IntegerUtils.isString2Integer(value)) {
			for (Vehicle vehicle : lstVehicleNew) {
				if (vehicle != null) {
					if (vehicle.getValue().equals(value)) {
						return true;
					}
				}
			}
		} else {
			String tmp = value.toUpperCase();
			for (Vehicle vehicle : lstVehicleNew) {
				if (vehicle != null) {
					if (vehicle.getFullValue().equals(tmp)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void handleChuyenKenh() {
		if (isInAction) {
			return;
		}
		isInAction = true;
		final Set<Listitem> setSelected = lstboxNew.getSelectedItems();
		if (currentModel == null && setSelected.size() <= 0) {
			Env.getHomePage().showNotification("Cần phải chọn yêu cầu", Clients.NOTIFICATION_TYPE_WARNING);
			return;
		}
		if (currentModel != null) {
			if (currentModel.getPickedTaxi() != null) {
				return;
			}
		}
		SysUser user = ((SysUserController) ControllerUtils.getController(SysUserController.class)).get(SysUser.class,
				Env.getUserID());
		if (user.getSwitchboard() == null) {
			Env.getHomePage().showNotification("Bạn không thuộc tổng đài nào nên không chọn được kênh",
					Clients.NOTIFICATION_TYPE_ERROR);
			return;
		}
		final Window window = new Window();
		window.setClosable(true);
		window.setSizable(false);
		window.setWidth("250px");
		window.setParent(this.getRoot().getRoot());
		window.setTitle("Chuyển kênh");
		window.setVisible(true);
		window.setPosition("right, bottom");
		window.doModal();
		Hlayout hlayout = new Hlayout();
		hlayout.setParent(window);

		ChannelTmsController channelCtrl = (ChannelTmsController) ControllerUtils
				.getController(ChannelTmsController.class);
		final List<ChannelTms> lstChannel = channelCtrl.find(
				"from ChannelTms where switchboardtms = ? and id != ? and isActive = true",
				new Object[] { user.getSwitchboard(), user.getChannel().getId() });
		final Combobox cbb = new Combobox();
		cbb.setHflex("1");
		if (lstChannel != null && lstChannel.size() > 0) {
			for (ChannelTms channelTms : lstChannel) {
				cbb.appendItem(channelTms.getName());
			}
		}
		if (cbb.getItemCount() > 0) {
			cbb.setSelectedIndex(0);
		}
		hlayout.appendChild(cbb);

		Hlayout bottom = new Hlayout();
		bottom.setStyle("padding: 5px");
		bottom.setParent(window);
		bottom.setValign("center");

		Button btnSave = new Button("Lưu");
		btnSave.setSclass("btn-success");
		btnSave.setParent(bottom);
		btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				try {
					handleChuyenKenhSave(window, cbb, lstChannel, setSelected);
				} catch (Exception e) {
					refreshAllList();
					AppLogger.logDebug.error("TaxiOrderDD|ChuyenKenh|", e);
				}
			}
		});

		window.addEventListener(Events.ON_OK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				try {
					handleChuyenKenhSave(window, cbb, lstChannel, setSelected);
				} catch (Exception e) {
					refreshAllList();
					AppLogger.logDebug.error("TaxiOrderDD|ChuyenKenh|", e);
				}
			}
		});

		Button btnCancel = new Button("Đóng");
		btnCancel.setParent(bottom);
		btnCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				isInAction = false;
				window.detach();
			}
		});

		window.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				isInAction = false;
				window.detach();
			}
		});
		window.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				isInAction = false;
				window.detach();
			}
		});
	}

	private void handleChuyenKenhSave(Window window, Combobox cbb, List<ChannelTms> lstChannel,
			Set<Listitem> setSelected) {
		try {
			int index = cbb.getSelectedIndex();
			if (index == -1) {
				Env.getHomePage().showNotification("Bạn đã chọn sai kênh!", Clients.NOTIFICATION_TYPE_ERROR);
			} else {
				ChannelTms channelNew = lstChannel.get(index);
				if (currentModel != null) {
					currentModel.setChannel(channelNew);
					currentModel.setIsUp(false);
					currentModel.setTimeIsUpdated(null);
					TripManager.sharedInstance.saveTrip(currentModel);
					// currentModel.save();
					// fireEventRefreshForDTV(currentModel);
					// fireEventRefreshCKForDD(currentModel);
					updateListAllModel(currentModel);
					// refreshMenList();
					focusRowOfListbox();
				} else {
					if (setSelected.size() <= 0) {
						Env.getHomePage().showNotification("Bạn chưa chọn yêu cầu", Clients.NOTIFICATION_TYPE_WARNING);
						return;
					}
					List<TaxiOrder> lstModelEvent = new ArrayList<TaxiOrder>();
					for (Listitem listitem : setSelected) {
						TaxiOrder orderTmp = listitem.getValue();
						orderTmp.setChannel(channelNew);
						orderTmp.setIsUp(false);
						orderTmp.setTimeIsUpdated(null);
						// orderTmp.save();
						TripManager.sharedInstance.saveTrip(orderTmp);
						lstModelEvent.add(orderTmp);
						// fireEventRefreshCKForDD(orderTmp);
						removeFromListAllModel(orderTmp);
					}
					// fireEventRefeshForDTV(lstModelEvent);
					Env.getHomePage().showNotification(
							"Đã chuyển kênh thành công cho " + lstModelEvent.size() + " yêu cầu",
							Clients.NOTIFICATION_TYPE_INFO);
					// refreshAllList();
					refreshMenList();
					focusRowOfListbox();
				}
				// refreshAllList();
			}

		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrderDD|DandleChuyenKenhSave", e);
		} finally {
			isInAction = false;
			window.detach();
		}
	}

	private void handleXeDonKhach() throws Exception {
		if (isInAction) {
			return;
		}
		try {

			isInAction = true;
			final Window window = new Window();
			window.setClosable(true);
			window.setSizable(false);
			window.setWidth("400px");
			window.setParent(this);
			window.setTitle("Xe đón khách");
			window.setVisible(true);
			window.setPosition("right, bottom");
			window.doModal();
			Hlayout hlayout = new Hlayout();
			hlayout.setParent(window);
			Listbox lstbox = new Listbox();
			lstbox.setParent(window);
			Listhead lsthead = new Listhead();
			lsthead.setParent(lstbox);
			Listheader lstheader = new Listheader();
			lstheader.setParent(lsthead);
			lstheader.setLabel("Số tài");
			lstheader.setHflex("30%");
			lstheader = new Listheader();
			lstheader.setParent(lsthead);
			lstheader.setLabel("Loại xe");
			lstheader.setHflex("60%");

			final Textbox txtVehicle = new Textbox();
			txtVehicle.setHflex("1");
			final Textbox txtInfo = new Textbox();
			txtInfo.setReadonly(true);
			txtInfo.setHflex("1");

			Listitem lstItem = new Listitem();
			lstItem.setParent(lstbox);
			Listcell cell = new Listcell();
			cell.setParent(lstItem);
			cell.appendChild(txtVehicle);
			cell = new Listcell();
			cell.setParent(lstItem);
			cell.appendChild(txtInfo);

			Hlayout bottom = new Hlayout();
			bottom.setStyle("padding: 5px");
			bottom.setParent(window);
			bottom.setValign("center");

			final Button btnSave = new Button("Lưu");
			btnSave.setSclass("btn-success");
			btnSave.setParent(bottom);

			Button btnCancel = new Button("Đóng");
			btnCancel.setParent(bottom);

			Events.echoEvent("setFocusComponent", this, txtVehicle);
			txtVehicle.addEventListener(Events.ON_OK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					String value = txtVehicle.getValue();
					if (value == null || value.trim().length() < 0) {
						// txtVehicle.setValue("");
						txtInfo.setValue("");
						currentModel.setPickedTaxi(null);
					} else {
						value = value.trim();
						if (IntegerUtils.isString2Integer(value)) {
							VehicleDD vehicleTmp = (VehicleDD) MapCommon.MAP_VEHICLE_INFO.get(value);
							if (vehicleTmp != null) {
								txtInfo.setValue(vehicleTmp.getVehicleType());
								currentModel.setPickedTaxi(vehicleTmp.getVehicle());
								currentModel.setStatus(EnumStatus.XE_DA_DON.getValue());
								btnSave.setFocus(true);
							} else {
								List<VehicleDD> lstVehicleDD = (List<VehicleDD>) MapCommon.MAP_LIST_VEHICLE_INFO.get(value);
								if (lstVehicleDD != null) {
									txtInfo.setValue("");
									Popup pop = new Popup();
									pop.setParent(txtVehicle.getParent());
									pop.setStyle("position: overlap");
									final Combobox combo = new Combobox();
									combo.setParent(pop);
									combo.setHflex("1");
									combo.setVflex("1");
									int index = 0;
									for (int i = 0; i < lstVehicleDD.size(); i++) {
										Vehicle vehicle = lstVehicleDD.get(i).getVehicle();
										Comboitem item = new Comboitem();
										item.setParent(combo);
										item.setLabel(vehicle.toString());
										item.setValue(vehicle);
										if (vehicle.getFullValue().startsWith("LAN")) {
											index = i;
										}
									}
									combo.setSelectedIndex(index);
									combo.setFocus(true);
									combo.addEventListener(Events.ON_OK, new EventListener<Event>() {
										@Override
										public void onEvent(Event arg0) throws Exception {
											if (combo.getSelectedIndex() >= 0) {
												Vehicle vehicleTmp = combo.getSelectedItem().getValue();
												if (vehicleTmp != null) {
													txtInfo.setValue(vehicleTmp.getTaxiType().getName());
													currentModel.setPickedTaxi(vehicleTmp);
													currentModel.setStatus(EnumStatus.XE_DA_DON.getValue());
													btnSave.setFocus(true);
												}
											} else {
												txtVehicle.setValue("");
												txtInfo.setValue("");
												currentModel.setPickedTaxi(null);
												Env.getHomePage().showNotification("Bạn chưa chọn xe",
														Clients.NOTIFICATION_TYPE_WARNING);
											}
										}
									});
									pop.open(txtVehicle.getParent(), "overlap");
								} else {
									Env.getHomePage().showNotification("Không có xe này",
											Clients.NOTIFICATION_TYPE_WARNING);
									txtVehicle.setValue("");
									txtInfo.setValue("");
									currentModel.setPickedTaxi(null);
								}
							}
						} else {
							String valuetmp = value.toUpperCase();
							VehicleDD vehicleTmp = (VehicleDD) MapCommon.MAP_VEHICLE_INFO_FULL.get(valuetmp);
							if (vehicleTmp != null) {
								txtInfo.setValue(vehicleTmp.getVehicleType());
								currentModel.setPickedTaxi(vehicleTmp.getVehicle());
								currentModel.setStatus(EnumStatus.XE_DA_DON.getValue());
								btnSave.setFocus(true);
							} else {
								Env.getHomePage().showNotification("Không có xe này",
										Clients.NOTIFICATION_TYPE_WARNING);
								txtVehicle.setValue("");
								txtInfo.setValue("");
								currentModel.setPickedTaxi(null);
							}
						}
					}
				}
			});

			btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					handleXeDonKhachSave(window);
				}
			});

			window.addEventListener(Events.ON_OK, new EventListener<Event>() {

				@Override
				public void onEvent(Event arg0) throws Exception {
					handleXeDonKhachSave(window);
				}
			});

			btnCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					currentModel.setPickedTaxi(null);
					if (currentModel.getRegistedTaxis().size() > 0) {
						currentModel.setStatus(EnumStatus.XE_DANG_KY_DON.getValue());
					} else {
						currentModel.setStatus(EnumStatus.DA_DOC_DAM.getValue());
					}
					isInAction = false;
					window.detach();
				}
			});
			window.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {

				@Override
				public void onEvent(Event arg0) throws Exception {
					currentModel.setPickedTaxi(null);
					if (currentModel.getRegistedTaxis().size() > 0) {
						currentModel.setStatus(EnumStatus.XE_DANG_KY_DON.getValue());
					} else {
						currentModel.setStatus(EnumStatus.DA_DOC_DAM.getValue());
					}
					isInAction = false;
					window.detach();
				}
			});

			window.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					currentModel.setPickedTaxi(null);
					if (currentModel.getRegistedTaxis().size() > 0) {
						currentModel.setStatus(EnumStatus.XE_DANG_KY_DON.getValue());
					} else {
						currentModel.setStatus(EnumStatus.DA_DOC_DAM.getValue());
					}
					isInAction = false;
					window.detach();
				}
			});
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrdersDD|HandleXeDonKhach|", e);
			isInAction = false;
		}
	}

	private void handleXeDonKhachSave(final Window window) {
		try {
			try {
				// currentModel.save();
				Vehicle vehicle = currentModel.getPickedTaxi();
				if (vehicle != null) {
					List<Vehicle> lstVehicleSms = new ArrayList<>();
					lstVehicleSms.add(vehicle);
					SendSMSForCustomerProcessor ssfcp = new SendSMSForCustomerProcessor(EnumStatus.XE_DA_DON.getValue(),
							currentModel.getId(), lstVehicleSms);
					ssfcp.start();
				}
				this.updatePickupTaxiStatus(currentModel.getPickedTaxi());
				for (Vehicle tmp : currentModel.getRegistedTaxis()) {
					this.updatePickupTaxiStatus(tmp);
				}
				// this.removeFromListAllModel(currentModel);
				// Set total call success order follow customer : @ Batt
				// 03052016
				int totalSuccess = currentModel.getCustomer().getTotalSuccessOrder();
				currentModel.getCustomer().setTotalSuccessOrder(totalSuccess + 1);
				currentModel.getCustomer().save();
				currentModel = null;
			} catch (Exception e) {
				currentModel.setPickedTaxi(null);
				AppLogger.logDebug.error("TaxiOrderDD|XeDonKhach|LuuBanGhi", e);
			}
			Env.getHomePage().showNotification("Đã cập nhật thông tin!", Clients.NOTIFICATION_TYPE_INFO);
			isInAction = false;
			window.detach();
			// fireEventRefreshForDTV(currentModel);
			// refreshAllList();
			refreshMenList();
			Events.echoEvent("focusListBox", this, this.lstboxTop);
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrderDD|XeDonKhach|", e);
			isInAction = false;
			window.detach();
		}
	}

	/**
	 * 
	 * @param order
	 * @deprecated
	 */
	protected void InformationDD(final TaxiOrder order) {
		Popup popup = new Popup();
		popup.setParent(this);
		popup.setStyle("position = before_start");

		Vlayout vlayout = new Vlayout();
		vlayout.setParent(popup);
		vlayout.setWidth("450px");
		Label title = new Label("Danh sách xe ở gần");
		title.setParent(vlayout);
		title.setHflex("1");

		Grid grid = new Grid();
		grid.setParent(vlayout);
		this.createcolsData(grid);
		grid.setRowRenderer(new RowRenderer<VehicleInfoJson>() {

			@Override
			public void render(final Row row, final VehicleInfoJson data, int arg2) throws Exception {
				VehicleDD vehicledata = (VehicleDD) MapCommon.MAP_VEHICLEDD_ID.get(data.getVehicleId()+"");
				row.appendChild(new Label(vehicledata.toString()));
				row.appendChild(new Label(data.getLicensePlace()));
				if (data.getCarType() == 1) {
					row.appendChild(new Label("4 chỗ"));
				} else {
					row.appendChild(new Label("7 chỗ"));
				}
				row.appendChild(new Label(Math.round(data.getDistance()) + ""));
				final Button btnAction = new Button();
				btnAction.setParent(row);
				btnAction.setTooltiptext("Đăng ký nhanh");
				btnAction.setSclass("btn-info-dd");
				btnAction.setImage("/themes/images/Add_12.png");
				btnAction.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						Set<Vehicle> setVehicle = order.getRegistedTaxis();
						if (setVehicle.size() >= 5) {
							Env.getHomePage().showNotification("Đã đủ 5 xe đăng ký. Bạn không thể đăng ký thêm",
									Clients.NOTIFICATION_TYPE_WARNING);
						} else {
							if (checkExistVehicle(setVehicle)) {
								Env.getHomePage().showNotification("Xe đã được đăng ký. Bạn cần chọn xe khác",
										Clients.NOTIFICATION_TYPE_WARNING);
							} else {
								VehicleDD newVehicleDD = (VehicleDD) MapCommon.MAP_VEHICLEDD_ID.get(data.getVehicleId() + "");
								if (newVehicleDD != null) {
									try {
										setVehicle.add(newVehicleDD.getVehicle());
										order.setStatus(EnumStatus.XE_DANG_KY_DON.getValue());
										try {
											// order.save();
											List<Vehicle> lstVehicleSms = new ArrayList<>();
											lstVehicleSms.add(newVehicleDD.getVehicle());
											SendSMSForCustomerProcessor ssfcp = new SendSMSForCustomerProcessor(
													EnumStatus.XE_DANG_KY_DON.getValue(), currentModel.getId(),
													lstVehicleSms);
											ssfcp.start();
										} catch (Exception e) {
											setVehicle.remove(newVehicleDD);
											if (setVehicle.size() == 0) {
												order.setStatus(EnumStatus.DA_DOC_DAM.getValue());
											}
											e.printStackTrace();
											AppLogger.logDebug.error("TaxiOrderDD|InfomationDD|LuuBanGhi|", e);
										}
										updateListAllModel(order);
										// fireEventRefreshForDTV(order);
										Env.getHomePage().showNotification("Bạn đã chọn xe đăng ký thành công",
												Clients.NOTIFICATION_TYPE_WARNING);
										// refreshAllList();
										refreshMenList();
										btnAction.setDisabled(true);
										btnAction.setVisible(false);
										focusRowOfListbox();
									} catch (Exception e) {
										e.printStackTrace();
										AppLogger.logDebug.error("TaxiOrderDD|InfomationDD|", e);
									}
								}
							}
						}
					}

					private boolean checkExistVehicle(Set<Vehicle> setValue) {
						boolean result = false;
						for (Vehicle vehicle : setValue) {
							if (vehicle.getId() == data.getVehicleId()) {
								result = true;
								break;
							}
						}
						return result;
					}
				});
				for (Vehicle vehicle : currentModel.getRegistedTaxis()) {
					if (vehicle != null) {
						if (vehicle.getId() == data.getVehicleId()) {
							btnAction.setVisible(false);
							break;
						}
					}
				}
			}
		});

		int ranger = ConfigUtil.getConfig("DD_RANGER", 500);
		int numvehicle = ConfigUtil.getConfig("DD_NUM_VEHICLE", 5);
		int orderCarType = 0;
		if (order.getOrderCarType() == EnumCarTypeCommon.BON_CHO.getValue()) {
			orderCarType = EnumCarTypeCommon.BON_CHO.getValue();
		} else if (order.getOrderCarType() == EnumCarTypeCommon.BAY_CHO.getValue()) {
			orderCarType = EnumCarTypeCommon.BAY_CHO.getValue();
		}
		List<VehicleInfoJson> lstValue = TaxiUtils.getTaxiAvaiableFromRDS(order.getBeginOrderLon(),
				order.getBeginOrderLat(), ranger, numvehicle, orderCarType);
		grid.setModel(new ListModelList<VehicleInfoJson>(lstValue));
		popup.open(this, "bottom_right");
	}

	private void infomationDD2(final TaxiOrder taxiOrder) {
		Window window = new Window();
		window.setParent(this);
		window.setTitle("Các xe ở gần");
		window.setPosition("right, bottom");
		window.setWidth("800px");
		window.setClosable(true);
		window.setVisible(true);
		window.doModal();
		window.addEventListener(Events.ON_CANCEL, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				window.detach();
			}
		});
		Hlayout hlayout = new Hlayout();
		hlayout.setParent(window);

		int ranger = ConfigUtil.getConfig("DD_RANGER", 500);
		int numvehicle = ConfigUtil.getConfig("DD_NUM_VEHICLE", 5);
		int orderCarType = 0;
		if (taxiOrder.getOrderCarType() == EnumCarTypeCommon.BON_CHO.getValue()) {
			orderCarType = EnumCarTypeCommon.BON_CHO.getValue();
		} else if (taxiOrder.getOrderCarType() == EnumCarTypeCommon.BAY_CHO.getValue()) {
			orderCarType = EnumCarTypeCommon.BAY_CHO.getValue();
		}
		List<VehicleInfoJson> lstValue = TaxiUtils.getTaxiAvaiableFromRDS(taxiOrder.getBeginOrderLon(),
				taxiOrder.getBeginOrderLat(), ranger, numvehicle, orderCarType);

		Listbox lstbox = new Listbox();
		lstbox.setParent(hlayout);
		lstbox.setHflex("5");
		lstbox.setHeight("400px");
		lstbox.setSizedByContent(true);
		this.creadListboxInformationDD(window, lstbox, taxiOrder);
		lstbox.setModel(new ListModelList<>(lstValue));
		Div div = new Div();
		div.setHflex("5");
		div.setVflex("400px");
		div.setParent(hlayout);
		VMaps maps = new VMaps(div);
		this.createMapInfomationDD(maps, taxiOrder, lstValue);

	}

	private void creadListboxInformationDD(final Window window, Listbox lstbox, TaxiOrder taxiOrder) {
		Listhead lstHead = new Listhead();
		lstHead.setParent(lstbox);
		Listheader header = new Listheader();
		header.setParent(lstHead);
		header.setHflex("25%");
		header.setLabel("Số tài");
		header = new Listheader();
		header.setParent(lstHead);
		header.setHflex("25%");
		header.setLabel("Biển số");
		header = new Listheader();
		header.setParent(lstHead);
		header.setHflex("20%");
		header.setLabel("Loại xe");
		header = new Listheader();
		header.setParent(lstHead);
		header.setHflex("15%");
		header.setLabel("KC(m)");
		header = new Listheader();
		header.setParent(lstHead);
		header.setHflex("5%");
		header.setLabel("App");
		header = new Listheader();
		header.setParent(lstHead);
		header.setHflex("15%");
		header.setLabel("Đăng ký");

		lstbox.setItemRenderer(new ListitemRenderer<VehicleInfoJson>() {

			@Override
			public void render(Listitem item, VehicleInfoJson data, int index) throws Exception {
				VehicleDD vehicledata = (VehicleDD) MapCommon.MAP_VEHICLEDD_ID.get(data.getVehicleId() + "");
				if (vehicledata != null) {
					item.appendChild(new Listcell(vehicledata.getVehicle().toString()));
					item.appendChild(new Listcell(vehicledata.getVehicle().getTaxiNumber()));
					if (data.getCarType() == 1) {
						item.appendChild(new Listcell("4 chỗ"));
					} else {
						item.appendChild(new Listcell("7 chỗ"));
					}
					item.appendChild(new Listcell(Math.round(data.getDistance()) + ""));
					String taxiKey = data.getDriverId() + "|" + data.getVehicleId();
					Taxi taxi = Taxi.mapDriver.get(taxiKey);
					if (taxi != null) {
						Listcell cell = new Listcell();
						cell.setParent(item);
						Checkbox checkbox = new Checkbox();
						checkbox.setChecked(taxi.isConnect());
						checkbox.setParent(cell);
					} else {
						Listcell cell = new Listcell();
						cell.setParent(item);
						Checkbox checkbox = new Checkbox();
						checkbox.setChecked(false);
						checkbox.setParent(cell);
					}
					final Button btnAction = new Button();
					Listcell cell = new Listcell();
					cell.setParent(item);
					btnAction.setParent(cell);
					btnAction.setTooltiptext("Đăng ký nhanh");
					btnAction.setSclass("btn-info-dd");
					btnAction.setImage("/themes/images/Add_12.png");
					btnAction.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
						@Override
						public void onEvent(Event arg0) throws Exception {
							Set<Vehicle> setVehicle = taxiOrder.getRegistedTaxis();
							if (setVehicle.size() >= 5) {
								Env.getHomePage().showNotification("Đã đủ 5 xe đăng ký. Bạn không thể đăng ký thêm",
										Clients.NOTIFICATION_TYPE_WARNING);
							} else {
								if (checkExistVehicle(setVehicle)) {
									Env.getHomePage().showNotification("Xe đã được đăng ký. Bạn cần chọn xe khác",
											Clients.NOTIFICATION_TYPE_WARNING);
								} else {
									VehicleDD newVehicleDD = (VehicleDD) MapCommon.MAP_VEHICLEDD_ID.get(data.getVehicleId() + "");
									if (newVehicleDD != null) {
										try {
											setVehicle.add(newVehicleDD.getVehicle());
											taxiOrder.setStatus(EnumStatus.XE_DANG_KY_DON.getValue());
											try {
												// taxiOrder.save();
												List<Vehicle> lstVehicleSms = new ArrayList<>();
												lstVehicleSms.add(newVehicleDD.getVehicle());
												SendSMSForCustomerProcessor ssfcp = new SendSMSForCustomerProcessor(
														EnumStatus.XE_DANG_KY_DON.getValue(), currentModel.getId(),
														lstVehicleSms);
												ssfcp.start();
												updateRegistedStatus(newVehicleDD.getVehicle());
											} catch (Exception e) {
												setVehicle.remove(newVehicleDD);
												if (setVehicle.size() == 0) {
													taxiOrder.setStatus(EnumStatus.DA_DOC_DAM.getValue());
												}
												// e.printStackTrace();
												AppLogger.logDebug.error("TaxiOrderDD|InfomationDD|LuuBanGhi|", e);
											}
											updateListAllModel(taxiOrder);
											// fireEventRefreshForDTV(taxiOrder);
											Env.getHomePage().showNotification("Bạn đã chọn xe đăng ký thành công",
													Clients.NOTIFICATION_TYPE_WARNING);
											// refreshAllList();
											refreshMenList();
											btnAction.setDisabled(true);
											btnAction.setVisible(false);
											// focusRowOfListbox();
											window.focus();
										} catch (Exception e) {
											e.printStackTrace();
											AppLogger.logDebug.error("TaxiOrderDD|InfomationDD|", e);
										}
									}
								}
							}
						}

						private boolean checkExistVehicle(Set<Vehicle> setValue) {
							boolean result = false;
							for (Vehicle vehicle : setValue) {
								if (vehicle.getId() == data.getVehicleId()) {
									result = true;
									break;
								}
							}
							return result;
						}
					});
					if (checkRegistedOther(vehicledata.getVehicle())) {
						btnAction.setVisible(false);
					} else {
						for (Vehicle vehicle : currentModel.getRegistedTaxis()) {
							if (vehicle != null) {
								if (vehicle.getId() == data.getVehicleId()) {
									btnAction.setVisible(false);
									break;
								}
							}
						}
					}
				}
			}
		});
	}

	private void createMapInfomationDD(VMaps gmaps, TaxiOrder taxiOrder, List<VehicleInfoJson> lstValue) {
		VMarker marker = new VMarker();
		marker.setParent(gmaps);
		marker.setIconImage("./themes/images/passenger_48.png");
		marker.setPosition(new LatLng(taxiOrder.getBeginOrderLat(), taxiOrder.getBeginOrderLon()));
		for (VehicleInfoJson vehicleInfoJson : lstValue) {
			if (vehicleInfoJson.getLatitude() != 0 && vehicleInfoJson.getLongitude() != 0) {
				VehicleDD vehicledata = (VehicleDD) MapCommon.MAP_VEHICLEDD_ID.get(vehicleInfoJson.getVehicleId() + "");
				if (vehicledata != null) {
					VMarker car = new VMarker();
					car.setParent(gmaps);
					car.setPosition(new LatLng(vehicleInfoJson.getLatitude(), vehicleInfoJson.getLongitude()));
					// car.setIconImage("./themes/images/icon_car_32.png");
					car.setIconImage(CommonDefine.TaxiIcon.ICON_7SEATS_PROCESSING);
					car.setLabel(vehicledata.getFullName());
					car.setOpen(false);
				}
			}

		}
		MapUtils.scaleMap(gmaps);
	}

	public void forcusRowOfListTop() {
		if (lstTopModel != null && currentModel != null) {
			for (int i = 0; i < lstTopModel.size(); i++) {
				TaxiOrder order = lstTopModel.get(i);
				if (currentModel.getId() == order.getId()) {
					lstboxTop.setSelectedIndex(i);
					lstboxTop.focus();
				}
			}
		}
	}

	public void focusRowOfListbox() {
		if (curListBox == null) {
			forcusRowOfListTop();
			return;
		}
		if (curListBox.equals(lstboxTop)) {
			if (currentModel == null) {
				if (lstTopModel.size() > 0) {
					lstboxTop.setSelectedIndex(0);
					currentModel = lstboxTop.getSelectedItem().getValue();
					lstboxTop.focus();
				}
			} else {
				for (int i = 0; i < lstTopModel.size(); i++) {
					TaxiOrder order = lstTopModel.get(i);
					if (currentModel.getId() == order.getId()) {
						lstboxTop.setSelectedIndex(i);
						lstboxTop.focus();
					}
				}
			}
		} else if (curListBox.equals(lstboxRegisted)) {
			if (currentModel == null) {
				if (lstRegistedModel.size() > 0) {
					lstboxRegisted.setSelectedIndex(0);
					currentModel = lstboxRegisted.getSelectedItem().getValue();
					lstboxRegisted.focus();
				}
			} else {
				for (int i = 0; i < lstRegistedModel.size(); i++) {
					TaxiOrder order = lstRegistedModel.get(i);
					if (currentModel.getId() == order.getId()) {
						lstboxRegisted.setSelectedIndex(i);
						lstboxRegisted.focus();
					}
				}
			}
		}

	}

	public void focusListBox(Event event) {
		Listbox lstbox = (Listbox) event.getData();
		if (lstbox != null) {
			this.focusRowOfListBox(lstbox);
		}
	}

	private void focusRowOfListBox(Listbox lstbox) {
		if (lstbox != null) {
			if (lstbox.equals(lstboxTop)) {
				lstboxRegisted.setSelectedIndex(-1);
				lstboxNew.setSelectedIndex(-1);
				lstboxCancel.setSelectedIndex(-1);
				int index = 0;
				if (currentModel != null) {
					for (int i = 0; i < this.lstTopModel.size(); i++) {
						TaxiOrder tmp = this.lstTopModel.get(i);
						if (currentModel.equals(tmp)) {
							index = i;
							break;
						}
					}
				}
				if (lstboxTop.getItems().size() > index) {
					// if (lstboxTop.getSelectedIndex() < 0) {
					lstboxTop.setSelectedIndex(index);
					curItem = lstboxTop.getSelectedItem();
					currentModel = curItem.getValue();
					// }
				}
				this.displayButtonForTop();
				this.curListBox = this.lstboxTop;
			} else if (lstbox.equals(lstboxRegisted)) {
				lstboxTop.setSelectedIndex(-1);
				lstboxNew.setSelectedIndex(-1);
				lstboxCancel.setSelectedIndex(-1);
				// if (lstboxRegisted.getItems().size() > 0) {
				// if (lstboxRegisted.getSelectedIndex() < 0) {
				// lstboxRegisted.setSelectedIndex(0);
				// curItem = lstboxRegisted.getSelectedItem();
				// currentModel = curItem.getValue();
				// }
				// }

				int index = 0;
				if (currentModel != null) {
					for (int i = 0; i < this.lstRegistedModel.size(); i++) {
						TaxiOrder tmp = this.lstRegistedModel.get(i);
						if (currentModel.equals(tmp)) {
							index = i;
							break;
						}
					}
				}
				if (lstboxRegisted.getItems().size() > index) {
					// if (lstboxTop.getSelectedIndex() < 0) {
					lstboxRegisted.setSelectedIndex(index);
					curItem = lstboxRegisted.getSelectedItem();
					currentModel = curItem.getValue();
					// }
				}
				this.displayButtonForAction();
				this.curListBox = this.lstboxRegisted;
			} else if (lstbox.equals(lstboxNew)) {
				lstboxTop.setSelectedIndex(-1);
				lstboxRegisted.setSelectedIndex(-1);
				lstboxCancel.setSelectedIndex(-1);
				if (lstboxNew.getItems().size() > 0) {
					if (lstboxNew.getSelectedIndex() < 0) {
						lstboxNew.setSelectedIndex(0);
					}
				}
				this.curItem = null;
				currentModel = null;
				this.displayButtonForNew();
				this.curListBox = this.lstboxNew;
			} else if (lstbox.equals(lstboxCancel)) {
				lstboxTop.setSelectedIndex(-1);
				lstboxRegisted.setSelectedIndex(-1);
				lstboxNew.setSelectedIndex(-1);
				if (lstboxCancel.getItems().size() > 0) {
					if (lstboxCancel.getSelectedIndex() < 0) {
						lstboxCancel.setSelectedIndex(0);
					}
				}
				this.curItem = null;
				this.currentModel = null;
				this.displayButtonForCancel();
				this.curListBox = this.lstboxCancel;
			}
			lstbox.focus();
		}
	}

	private void createcolsData(Grid gridTaxi2) {
		Columns cols = new Columns();
		cols.setParent(gridTaxi2);

		Column col = new Column();

		col = new Column();
		col.setParent(cols);
		col.setHflex("20%");
		col.setLabel("Số tài");

		col = new Column();
		col.setParent(cols);
		col.setHflex("25%");
		col.setLabel("Biển số xe");

		col = new Column();
		col.setParent(cols);
		col.setHflex("20%");
		col.setLabel("Loại xe");

		col = new Column();
		col.setParent(cols);
		col.setHflex("20%");
		col.setLabel("K/c (m)");
		col = new Column();
		col.setParent(cols);
		col.setHflex("15%");
		col.setLabel("Đăng ký");
	}

	private void refreshAllList() {
		this.loadDataDD();
		this.renderAllList();
		Events.echoEvent("focusListBox", this, this.lstboxTop);
	}

	protected void refreshNewList() {
		this.loadDataDD();
		ListModelList<TaxiOrder> lstModelList = new ListModelList<TaxiOrder>(this.lstNewModel);
		lstModelList.setMultiple(true);
		this.lstboxNew.setModel(lstModelList);
	}

	protected Driver getDriver(int deviceId) {
		Driver result = null;
		int driverId = TaxiUtils.getDriverIdWs(deviceId + "");
		if (driverId != -1) {
			DriverController controller = (DriverController) ControllerUtils.getController(DriverController.class);
			List<Driver> lstDriver = controller.find("from Driver where id = ?", driverId);
			if (lstDriver != null && !lstDriver.isEmpty()) {
				result = lstDriver.get(0);
			}
		}
		if (result == null) {
			result = this.getDefaultDriver();
		}
		return result;
	}

	private Driver getDefaultDriver() {
		Driver result = null;
		try {
			DriverController controller = (DriverController) ControllerUtils.getController(DriverController.class);
			List<Driver> lstDriver = controller.find("from Driver where value = 'KhongXoa'");
			if (lstDriver != null && !lstDriver.isEmpty()) {
				result = lstDriver.get(0);
			} else {
				if (this.insertDefaultDriver()) {
					lstDriver = controller.find("from Driver where value = 'KhongXoa'");
					if (lstDriver != null && !lstDriver.isEmpty()) {
						result = lstDriver.get(0);
					}
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrderDD|GetDefaultDriver", e);
		}

		return result;
	}

	private boolean insertDefaultDriver() {
		boolean result = false;
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO txm_tracking.lst_driver(DriverCode, DriverName, AgentID, Printed, Writed, rate, StaffCard, PhoneNumber, PhoneOffice) "
				+ "VALUES('KhongXoa', 'No Name', 12, 0, 0, 5, '999999', '0123456789', '0123456789')";
		try {
			conn = sessionImplementor.connection();
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				int i = ps.executeUpdate();
				// conn.commit();
				if (i > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
				session.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	private void updateListAllModel(TaxiOrder taxiOrder) {
		if (this.lstAllModel.contains(taxiOrder)) {
			this.lstAllModel.remove(taxiOrder);
		}
		this.lstAllModel.add(taxiOrder);
	}

	private void removeFromListAllModel(TaxiOrder taxiOrder) {
		this.lstAllModel.remove(taxiOrder);
	}

	private void setValueLabel() {
		if (currentModel != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(currentModel.getPhoneNumber());
			if (currentModel.getOrderCarType() == 1) {
				sb.append(" 😄  ").append("4C");
			} else if (currentModel.getOrderCarType() == 2) {
				sb.append(" 😄  ").append("7C");
			} else {
				sb.append(" 😄  ").append("Bất kỳ");
			}
			sb.append(" 😄  ").append(currentModel.getBeginOrderAddress());
			if (currentModel.getNote() != null) {
				sb.append(" 😄 ").append(currentModel.getNote());
			}
			lblInfo.setValue(sb.toString());
		} else {
			lblInfo.setValue("");
		}
	}

}

class ListItemRendererDD implements ListitemRenderer<TaxiOrder> {
	private boolean isFast = false;
	private TaxiOrdersDD taxiOrdersDD;
	private ArrayList<GridColumn> gridColumns;

	public ListItemRendererDD(ArrayList<GridColumn> gridColumns, boolean isFast, TaxiOrdersDD taxiOrdersDD) {
		this.isFast = isFast;
		this.taxiOrdersDD = taxiOrdersDD;
		this.gridColumns = gridColumns;
	}

	@Override
	public void render(Listitem item, TaxiOrder data, int index) throws Exception {
		item.setValue(data);
		for (int i = 0; i < gridColumns.size(); i++) {
			GridColumn column = gridColumns.get(i);
			if (column.getGetDataMethod() != null) {
				Method method = data.getClass().getMethod(column.getGetDataMethod());
				Object val = method.invoke(data);
				if (column.getFieldName() != null && column.getFieldName().length() > 0) {
					Field field = column.getModelClazz().getDeclaredField(column.getFieldName());
					if (field.isAnnotationPresent(FixedCombobox.class)) {
						this.createCellFixedCombobox(item, field, val);
						continue;
					} else if (field.isAnnotationPresent(AnnonationLinkedTable.class)) {
						this.createCellLinkedMap(item, field, val);
						continue;
					}
				}
				if (column.getClazz().equals(Boolean.class)) {
					Checkbox checkbox = new Checkbox();
					checkbox.setLabel("");
					if (val == null || !(boolean) val) {
						checkbox.setChecked(false);
					} else {
						checkbox.setChecked(true);
					}
					Listcell lstCell = new Listcell();
					lstCell.setParent(item);
					lstCell.appendChild(checkbox);
				} else {
					String value = "";
					if (val != null) {
						value = val.toString();
					}
					Listcell lstCell = new Listcell(value);
					item.appendChild(lstCell);
				}
			}
		}
		if (data.getStatus() == EnumStatus.HUY.getValue()) {
			item.setStyle("background: #D15FEE");
		} else if (data.getRepeatTime() > 1) {
			item.setStyle("background: #FFCC99");
		} else if (data.getRegistedTaxis().size() > 0 || data.getPickedTaxi() != null) {
			item.setSclass("listbox_dieudam_docdam");
		}
		if (isFast) {
			Button btnFast = new Button();
			btnFast.setSclass("btn-info-dd");
			btnFast.setImage("/themes/images/right.png");
			Listcell listcell = new Listcell();
			listcell.appendChild(btnFast);
			item.appendChild(listcell);
			btnFast.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					if (data != null) {
						Set<Vehicle> set = data.getRegistedTaxis();
						if (set.size() == 1) {
							try {
								for (Vehicle vehicle : set) {
									data.setPickedTaxi(vehicle);
									data.setStatus(EnumStatus.XE_DA_DON.getValue());
									// data.save();

									// Set total call success order follow
									// customer : @ Batt 03052016
									int totalSuccess = data.getCustomer().getTotalSuccessOrder();
									data.getCustomer().setTotalSuccessOrder(totalSuccess + 1);
									data.getCustomer().save();
									taxiOrdersDD.updatePickupTaxiStatus(vehicle);
									List<Vehicle> lstVehicleSms = new ArrayList<>();
									lstVehicleSms.add(vehicle);
									SendSMSForCustomerProcessor ssfcp = new SendSMSForCustomerProcessor(
											EnumStatus.XE_DA_DON.getValue(), data.getId(), lstVehicleSms);
									ssfcp.start();
									Env.getHomePage().showNotification(
											"Xe " + vehicle.getValue() + " đã đón cuốc " + data.getPhoneNumber(),
											Clients.NOTIFICATION_TYPE_WARNING);
									taxiOrdersDD.currentModel = null;
									taxiOrdersDD.lstAllModel.remove(data);
									taxiOrdersDD.refreshMenList();
									// taxiOrdersDD.fireEventRefreshForDTV(data);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (set.size() > 1) {
							Env.getHomePage().showNotification("Số lượng xe đăng ký đón nhiều hơn 1",
									Clients.NOTIFICATION_TYPE_WARNING);
						} else {
							Env.getHomePage().showNotification("Không có xe đăng ký đón",
									Clients.NOTIFICATION_TYPE_WARNING);
						}
					}
				}
			});
		}
	}

	private void createCellFixedCombobox(Listitem item, Field field, Object val) {
		if (val == null) {
			item.appendChild(new Listcell(val == null ? "" : val.toString()));
		} else {
			Annotation ann = field.getAnnotation(FixedCombobox.class);
			FixedCombobox fixedCombobox = (FixedCombobox) ann;
			boolean isNotExist = true;
			for (int j = 0; j < fixedCombobox.value().length; j++) {
				if (fixedCombobox.value()[j] == (int) val) {
					item.appendChild(new Listcell(fixedCombobox.label()[j]));
					isNotExist = false;
					break;
				}
			}
			if (isNotExist) {
				item.appendChild(new Listcell(""));
			}
		}
	}

	private void createCellLinkedMap(Listitem item, Field field, Object val)
			throws ClassNotFoundException, NoSuchFieldException, SecurityException {
		if (val == null || (int) val == 0) {
			item.appendChild(new Listcell(""));
		} else {
			Annotation ann = field.getAnnotation(AnnonationLinkedTable.class);
			AnnonationLinkedTable annLinkedTable = (AnnonationLinkedTable) ann;
			String clazzName = "com.vietek.taxioperation.model." + annLinkedTable.modelClazz();
			Class<? extends Object> clazz = Class.forName(clazzName);
			String modelClazz = annLinkedTable.modelClazz();
			List<?> lstValue = this.getListValue(modelClazz);
			if (lstValue != null) {
				for (Object object : lstValue) {
					if (object.getClass() == clazz) {
						Field fieldTmp = clazz.getDeclaredField("id");
						try {
							fieldTmp.setAccessible(true);
							int id = fieldTmp.getInt(object);
							if (id == (int) val) {
								fieldTmp = clazz.getDeclaredField(annLinkedTable.displayFieldName());
								fieldTmp.setAccessible(true);
								String labelValue = (String) fieldTmp.get(object);
								item.appendChild(new Listcell(labelValue));
								break;
							}
						} catch (Exception e) {
							item.appendChild(new Listcell(""));
							e.printStackTrace();
						}

					}
				}

			}
		}
	}

	private List<?> getListValue(String modelClazz) {
		String controllerName = "com.vietek.taxioperation.controller." + modelClazz + "Controller";
		String whereString = "";
		List<?> lstModel = null;
		try {
			lstModel = ControllerUtils.getController(Class.forName(controllerName))
					.find("from " + modelClazz + whereString);
		} catch (Exception e) {
			lstModel = null;
			e.printStackTrace();
		}
		return lstModel;
	}
}

class SendSMSForCustomerProcessor extends Thread {
	private int status;
	private int taxiOrderId;
	private List<Vehicle> lstVehicle;

	public SendSMSForCustomerProcessor(int status, int taxiOrderId, List<Vehicle> lstVehicle) {
		super();
		this.setName("SendSMSForCustomerProcessor");
		this.taxiOrderId = taxiOrderId;
		this.lstVehicle = lstVehicle;
		this.status = status;
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try {
			Trip trip = TripManager.sharedInstance.getTrip(taxiOrderId + "");
			if (trip != null) {
				if (lstVehicle != null && lstVehicle.size() > 0) {
					for (Vehicle vehicle : lstVehicle) {
						if (vehicle != null) {
							Driver driver = null;
							if (vehicle.getDeviceID() != null) {
								driver = this.getDriver(vehicle.getDeviceID());
							}
							if (driver != null) {
								if (status == EnumStatus.XE_DANG_KY_DON.getValue()) {
									trip.driverRegisterInDD(Taxi.getTaxi(driver, vehicle));
								} else if (status == EnumStatus.XE_DA_DON.getValue()) {
									Taxi taxi = Taxi.getTaxi(driver, vehicle);
									if (taxi != null) {
										trip.driverPickUpRider(taxi);
									}
								}
							}
						}
					}
				} else {
					TripManager.sharedInstance.saveTrip(trip.getOrder());
				}

			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
		AppLogger.logDebug.info("Thoi gian gui tin: " + (System.currentTimeMillis() - start));
	}

	private Driver getDriver(int deviceId) {
		Driver result = null;
		int driverId = TaxiUtils.getDriverIdWs(deviceId + "");
		if (driverId != -1) {
			DriverController controller = (DriverController) ControllerUtils.getController(DriverController.class);
			List<Driver> lstDriver = controller.find("from Driver where id = ?", driverId);
			if (lstDriver != null && !lstDriver.isEmpty()) {
				result = lstDriver.get(0);
			}
		}
		if (result == null) {
			result = this.getDefaultDriver();
		}
		return result;
	}

	private Driver getDefaultDriver() {
		Driver result = null;
		DriverController controller = (DriverController) ControllerUtils.getController(DriverController.class);
		List<Driver> lstDriver = controller.find("from Driver where value = 'KhongXoa'");
		if (lstDriver != null && !lstDriver.isEmpty()) {
			result = lstDriver.get(0);
		} else {
			if (this.insertDefaultDriver()) {
				lstDriver = controller.find("from Driver where value = 'KhongXoa'");
				if (lstDriver != null && !lstDriver.isEmpty()) {
					result = lstDriver.get(0);
				}
			}
		}

		return result;
	}

	private boolean insertDefaultDriver() {
		boolean result = false;
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO txm_tracking.lst_driver(DriverCode, DriverName, AgentID, Printed, Writed, rate, StaffCard, PhoneNumber, PhoneOffice) "
				+ "VALUES('KhongXoa', 'No Name', 12, 0, 0, 5, '999999', '0123456789', '0123456789')";
		try {
			conn = sessionImplementor.connection();
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				int i = ps.executeUpdate();
				// conn.commit();
				if (i > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
				session.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}