package com.vietek.taxioperation.ui.controller;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.zkoss.zhtml.impl.ContentTag;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.East;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.EnumCancelReason;
import com.vietek.taxioperation.common.EnumCarTypeCommon;
import com.vietek.taxioperation.common.EnumOrderType;
import com.vietek.taxioperation.common.EnumStatus;
import com.vietek.taxioperation.common.EnumUserAction;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.CustomerController;
import com.vietek.taxioperation.controller.TablePriceController;
import com.vietek.taxioperation.controller.TypeTablePriceController;
import com.vietek.taxioperation.googlemapSearch.ConvertLatLongToAddress;
import com.vietek.taxioperation.googlemapSearch.DistanceBetweenTwoPoints_Run;
import com.vietek.taxioperation.googlemapSearch.FindNearChannel;
import com.vietek.taxioperation.googlemapSearch.GoogleMapUntil;
import com.vietek.taxioperation.googlemapSearch.TaxiOrderUtil;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.TablePrice;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.model.TelephoneExtensionTms;
import com.vietek.taxioperation.model.TypeTablePrice;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.model.VehicleInfoJson;
import com.vietek.taxioperation.model.VehicleStatusDD;
import com.vietek.taxioperation.model.VoipCenter;
import com.vietek.taxioperation.processor.SendCancelSmsProcessor;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.taxiorder.TaxiOrderSaveWorker;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvent;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvents;
import com.vietek.taxioperation.ui.editor.CarTypeSelectorContainer;
import com.vietek.taxioperation.ui.editor.CarTypeValue;
import com.vietek.taxioperation.ui.editor.FindAddressEditor;
import com.vietek.taxioperation.ui.editor.FindAddressHandler;
import com.vietek.taxioperation.ui.editor.M2OGridEditor;
import com.vietek.taxioperation.ui.editor.NearAddressControl;
import com.vietek.taxioperation.ui.editor.NearAddressControlHandler;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.TaxiOrderGmap;
import com.vietek.taxioperation.util.Address;
import com.vietek.taxioperation.util.CallInfo;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.ExtensionOnlineUtil;
import com.vietek.taxioperation.util.SaveLogToQueue;
import com.vietek.taxioperation.util.StandandPhoneNumber;
import com.vietek.taxioperation.util.TaxiOrderDetailHandler;
import com.vietek.taxioperation.util.TaxiUtils;
import com.vietek.taxioperation.webservice.TaxiResult;

public class TaxiOrdersDetailForm extends BasicDetailWindow implements FindAddressHandler, NearAddressControlHandler {

	private static final long serialVersionUID = 1L;
	private Center center;
	private East east;
	private Textbox tbNoteForCustmer;
	private Textbox tbNoteForTaxiOrder;
	private Textbox tbPhone;
	private Combobox cbOptionloadkm;
	private TypeTablePriceController controller;
	private Textbox tbOptionAirPricekm;
	private Textbox tbOptionPrice;
	private Textbox tbOptionAirPricekmct;
	private Textbox tbOptionTimect;
	private Textbox tbOptionPricect;
	private Textbox tbOptionPricekmctadd;
	private Textbox tbOptionPricetimectadd;
	private Textbox tbOptionPricectadd;
	private Textbox tbMoney;
	private Textbox tbOptionPriceadd;
	private Radio rdOptionConTract;
	private Radio rdOptionNormal;
	private Radio rdOptionAirtwoway;
	private Radio rdOptionAironeway;
	private Radio rdOptionAirtwowayct;
	private Radio rdOptionAironewayct;
	private Datebox dbReceiveDate;
	private Button btOptionloadct;
	private Button btloadMoney;
	private CarTypeSelectorContainer carTypeSelectorContainer;
	private Div divContainMap;
	private Div divHeaderInforCall;
	private Div divGroupChildNearAddress;
	private Div divParentContain;
	private Button btShowMap;
	private Radio rdOptionStartNow;
	private Radio rdOptionSomeTime;
	private Radio rdOptionAirStation;
	private Radio rdOptionSomePerson;
	private Combobox cbExtension;
	private Div divShow_VehicleRegister;
	private Label lbOptionMoney;
	private Label lbOptionMoneyct;
	private Label lbOptionAirkm;
	private Label lbOptionAirkmct;
	private Label lbOptionAirtime;
	private Label lbOptionVND;
	private Label lbOptionKm;
	private Label lbOptionKmct;
	private Label lbOptionTimect;
	private Label lbOptionVNDctadd;
	private Label lbMoney;
	private Label lbVND;
	private Label lbOptionVNDct;
	private Label lbOptionadd;
	private Label lbOptionaddct;
	private Label lbOptionVNDcttimeadd;
	private Label lbOptiontimeaddct;
	private Label lbOptionkmctadd;
	private Label lbOptionkmaddct;
	private Label lbOptionVNDadd;
	private Div divAirgroup;
	private Div divContractgroup;
	private Button btCancelOrder;
	private Button btNewOrder;
	private Button btCallOut;
	private Label headerInforCall;
	private Label lbSearchAddrssEnd_F4;
	private Label lb_F6;
	private Label lbSearchAddressStart_F3;
	private Label lbOrder_F2;
	private List<ChannelTms> lstChannel;
	private M2OGridEditor chanelSelector;
	private NearAddressControl nearAddress;
	private List<Vehicle> lstVehicleRegister = new ArrayList<>();
	private final String onEventChangeStartAddress = "onEventChangeStartAddress";
	public TaxiOrderDetailHandler handlerTaxiOrderDetail;
	public Tab tabContain;

	private TaxiOrdersDetailForm _this;

	private FindAddressEditor findAddressEditorStart;
	private FindAddressEditor findAddressEditorEnd;
	TaxiOrder beanTemp = new TaxiOrder();

	private CallInfo callInfo;

	private TaxiOrderGmap map;

	public CallInfo getCallInfo() {
		if (callInfo == null) {
			callInfo = new CallInfo("", "", new Timestamp(System.currentTimeMillis()), "", "",
					CallInfo.CALL_EVENT_OTHER);
		}
		return callInfo;
	}

	public void setCallInfo(CallInfo callInfo) {
		this.callInfo = callInfo;
	}

	private TaxiOrder getTaxiOrder() {
		return (TaxiOrder) getModel();
	}

	public static String EVENT_RELOAD_MAP_TAXI_REGISTED = "event_reload_gmap_taxi_registed";

	public TaxiOrdersDetailForm(AbstractWindowPanel listWindow, TaxiOrder model) {

		super(model, listWindow);
		this.setHeight("100%");
		this.setWidth("100%");

		this.setBorder("none");
		this.setSclass("z-window_Detail modal-content_Detail");
		this.setClosable(false);
		this.setSizable(false);
		this.setMaximizable(false);
		this.setMinimizable(false);
		this.setShadow(false);
		this.setTopmost();
		_this = this;

		this.setCtrlKeys("#f1#f2#f3#f4#f5#f6#f7#f8#f9#f10^s^c");
		/*
		 * set events
		 */
		this.addEventListener(Events.ON_CLOSE, this); // "onClose"
		this.addEventListener(Events.ON_CTRL_KEY, EVENT_CTRL_KEY_FORM);
		tbPhone.addEventListener(Events.ON_OK, EVENT_ON_CHANGING_TEXTBOXPHONE);
		tbPhone.addEventListener(Events.ON_BLUR, EVENT_ON_CHANGING_TEXTBOXPHONE);
		btCancelOrder.addEventListener(Events.ON_CLICK, EVENT_ON_CLICK_CANCEL_TAXIORDER);
		btNewOrder.addEventListener(Events.ON_CLICK, EVENT_ON_CLICK_NEW_TAXIORDER);
		btCallOut.addEventListener(Events.ON_CLICK, EVENT_CALLOUT);
		btOptionloadct.addEventListener(Events.ON_CLICK, EVENT_ON_CLICK_PRICE);
		btloadMoney.addEventListener(Events.ON_CLICK, EVENT_ON_CLICK_MONEY);
		findAddressEditorStart.getComponent().addEventListener(onEventChangeStartAddress, EVENT_STARTADDRESS_CHANGE);
		/*
		 * auto disable
		 */
		getBtn_save().setAutodisable("self");
		getBtn_cancel().setAutodisable("self");
		btCancelOrder.setAutodisable("self");
		btCallOut.setAutodisable("self");
	}

	@Override
	public void beforInit() {
		this.getDefaultData();
	}

	@Override
	public void createForm() {

		divParentContain = new Div();
		divParentContain.setParent(this);
		divParentContain.setId("divDetailForm");
		divParentContain.setSclass("divCommon divDetailForm");
		divParentContain.setVisible(true);
		divParentContain.setHeight("92%");
		Borderlayout borderlayout = new Borderlayout();
		borderlayout.setParent(divParentContain);
		borderlayout.setSclass("complex-layout");
		borderlayout.setVflex("1");
		borderlayout.setHflex("1");

		/* Quan ly thong tin nguoi dung */
		center = new Center();
		center.setParent(borderlayout);

		Div divCenter = new Div();
		divCenter.setParent(center);
		divCenter.setId("divCenter");
		divCenter.setSclass("divCommon divCenter");
		this.createCenterLayout(divCenter);

		/* Quan ly ban do */
		east = new East();
		east.setCollapsible(true);
		east.setSplittable(true);
		east.setParent(borderlayout);
		east.setSize("38%");

		createEastLayout(east);

	}

	@Override
	public void createMapEditor() {

		return;
	}

	@Override
	public void initUI() {

		super.initUI();

		try {
			/*
			 * set label css
			 */

			Button btCancel = this.getBtn_cancel();
			btCancel.setClass("taxiOrder2BtCancel");
			btCancel.setLabel("Bỏ qua (Esc)");

			Button btSave = this.getBtn_save();
			btSave.setLabel("Lưu (Ctrl + s)");

			/*
			 * 
			 */

			Hlayout divBt = super.getBottonLayout();
			btCancelOrder = new Button("Hủy chuyến");
			btCancelOrder.setParent(divBt);
			btCancelOrder.setSclass("btCancelOrder");
			btCancelOrder.setImage("/themes/images/taxiDelete.png");
			btCancelOrder.setVisible(false);
		} catch (Exception ex) {

		}
	};

	public Tab getTab() {
		return tabContain;
	}

	public TaxiOrdersForm getTaxiOrderForm() {
		return (TaxiOrdersForm) getListWindow();
	}

	public void setTab(Tab tab) {
		this.tabContain = tab;
	}

	public void showDetailWidow(String phone) {
		this.setParent(Env.getHomePage().getDivTab());
	}

	@Override
	public void setDefaultValue() {
		super.setDefaultValue();
		if (getModel() != null) {
		}
	}

	public static void detachTyleVehilce(List<Component> list) {

		int size = list.size();
		for (int i = size - 1; i >= 0; --i) {
			Component child = list.get(i);
			List<Component> _list = child.getChildren();
			if (_list.size() > 0)
				detachTyleVehilce(_list);
			child.detach();
		}
	}

	private void createCenterLayout(Component parent) {

		createGroup_InforCallLeft(parent);
	}

	private void createGroup_InforCallLeft(Component parent) {

		/*
		 * header
		 */
		divHeaderInforCall = new Div();
		divHeaderInforCall.setId("divHeaderInforCall");
		divHeaderInforCall.setParent(parent);
		divHeaderInforCall.setZclass("page-header "); // page-header_Detail

		headerInforCall = new Label("");// ContentTag("H4");
		headerInforCall.setId("h4HeaderInforCallID");
		headerInforCall.setParent(divHeaderInforCall);
		headerInforCall.setSclass("h4HeaderInforCall");

		Div divCotainInforCall = new Div();
		divCotainInforCall.setParent(parent);
		divCotainInforCall.setId("divCotainInforCall");
		divCotainInforCall.setSclass("container");

		Div divChildContainInforCallDetail = new Div();
		divChildContainInforCallDetail.setParent(divCotainInforCall);
		divChildContainInforCallDetail.setId("divChildContainInforCallDetail");
		divChildContainInforCallDetail.setSclass("panel-body");

		Div divChildInfor_Body = new Div();
		divChildInfor_Body.setParent(divChildContainInforCallDetail);
		divChildInfor_Body.setId("divChildInfor_Body");
		divChildInfor_Body.setSclass("panel panel-primary"); // panel-default

		Div divChildInfor_BodyHeading = new Div();
		divChildInfor_BodyHeading.setParent(divChildInfor_Body);
		divChildInfor_BodyHeading.setId("divChildInfor_BodyHeading");
		divChildInfor_BodyHeading.setSclass("panel-heading");

		ContentTag lbHeader = new ContentTag("span");
		lbHeader.setParent(divChildInfor_BodyHeading);
		lbHeader.setContent("THÔNG TIN KHÁCH HÀNG");

		Div divShowmap = new Div();
		divShowmap.setId("divChilddivChildInforAdvance_BodyHeadingShowmap");
		divShowmap.setParent(divChildInfor_BodyHeading);
		divShowmap.setSclass("optionCommonPanel divShowmap ");

		btNewOrder = new Button("( Ctrl+C ) CHUYẾN MỚI");
		btNewOrder.setParent(divShowmap);
		btNewOrder.setSclass("btn btn-default active btCollapse btNewOrder");
		btNewOrder.setVisible(false);

		btShowMap = new Button();
		btShowMap.setParent(divShowmap);
		btShowMap.setLabel("( F10 ) XEM BẢN ĐỒ XE");
		btShowMap.setSclass("btn btn-default active btCollapse btShowMap");
		btShowMap.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {

				onChangeStartAddress(findAddressEditorStart.getSearchValue());
				// cbShowMap.setChecked(!cbShowMap.isChecked());

			}
		});

		Div divChildInfor_BodyDetail = new Div();
		divChildInfor_BodyDetail.setParent(divChildInfor_Body);
		divChildInfor_BodyDetail.setId("divChildInfor_BodyDetail");
		divChildInfor_BodyDetail.setSclass("panel-body");

		/*
		 * position start and end
		 */
		createChildInfor_Customer(divChildInfor_BodyDetail);
		createChildInfor_AdvanceTypeVehicle(divChildInfor_BodyDetail);
		createChildInfor_TaxiOrderNote(divChildInfor_BodyDetail);

		createChildInfor_PointStart_(divChildInfor_BodyDetail);
		createChildInfor_PointEnd_(divChildInfor_BodyDetail);
		createChildInfor_NearAddress(divChildInfor_BodyDetail);
		createChildInfor_StartNow(divChildInfor_BodyDetail);
		createChildInfor_AdvanceShapeTypeTrack(divChildInfor_BodyDetail);
		createGroup_Contract(divChildInfor_BodyDetail);
		createChildInfor_AdvanceChanel(lstChannel, divChildInfor_BodyDetail);

	}

	private void createChildInfor_Customer(Component parent) {

		Div divContain = new Div();
		divContain.setParent(parent);
		divContain.setSclass("row");
		/*
		 * phone
		 */
		Div divPhone = new Div();
		divPhone.setParent(divContain);
		divPhone.setSclass("col-sm-3 infor_CustomerPhone");

		tbPhone = new Textbox();

		tbPhone.setId("tbPhone");
		tbPhone.setParent(divPhone);
		tbPhone.setPlaceholder("Số điện thoại...");
		tbPhone.setSclass("form-control infor_Customer");

		divPhone = new Div();
		divPhone.setParent(divContain);
		divPhone.setSclass("col-sm-6 infor_CustomerDesCustmer");

		tbNoteForCustmer = new Textbox();// ContentTag("span");
		tbNoteForCustmer.setParent(divPhone);
		tbNoteForCustmer.setPlaceholder("(F1) Mô tả khách hàng");
		tbNoteForCustmer.setSclass("form-control infor_Customer");

		divPhone = new Div();
		divPhone.setParent(divContain);
		divPhone.setSclass("col-sm-3 infor_CustomerDesCustmer");

		Div divCallOut = new Div();
		divCallOut.setParent(divPhone);

		cbExtension = new Combobox();
		cbExtension.setParent(divCallOut);
		cbExtension.setWidth("43%");
		cbExtension.setReadonly(true);

		btCallOut = new Button("GỌI KHÁCH");
		btCallOut.setParent(divCallOut);
		btCallOut.setWidth("57%");
		btCallOut.setZclass("form-control infor_Customer callOut");
		btCallOut.setClass("btn-primary");
		btCallOut.setTabindex(-1);
	}

	private void getListExtensionCallOut() {

		List<TelephoneExtensionTms> lstExtension = getTaxiOrderForm().getLstExtensions();

		if (lstExtension.size() > 0) {
			Comboitem item = new Comboitem("MÁY");
			item.setValue("MÁY");
			cbExtension.getItems().add(item);
			cbExtension.setSelectedIndex(0);

			for (TelephoneExtensionTms telephoneExtensionTms : lstExtension) {

				item = new Comboitem(telephoneExtensionTms.getExtension());
				item.setValue(telephoneExtensionTms.getExtension());

				cbExtension.getItems().add(item);
			}
		} else {
			btCallOut.setDisabled(true);
		}
	}

	private void createChildInfor_TaxiOrderNote(Component parent) {

		Div divGroupChildTaxiOrderNote = new Div();
		divGroupChildTaxiOrderNote.setId("divdivGroupChildTaxiOrderNote");
		divGroupChildTaxiOrderNote.setParent(parent);
		divGroupChildTaxiOrderNote.setSclass("form-group");

		lb_F6 = new Label("F6");
		lb_F6.setParent(divGroupChildTaxiOrderNote);
		lb_F6.setAttribute("for", "tbNoteForTaxiOrder");
		lb_F6.setSclass("lbF2 infor_Customer");

		tbNoteForTaxiOrder = new Textbox();
		tbNoteForTaxiOrder.setId("tbNoteForTaxiOrder");
		tbNoteForTaxiOrder.setParent(divGroupChildTaxiOrderNote);
		tbNoteForTaxiOrder.setPlaceholder(" Mô tả chuyến đi");
		tbNoteForTaxiOrder.setSclass("form-control infor_Customer z-textbox divCommonComponent"); // infor_Customer
																									// divCommonComponent
		tbNoteForTaxiOrder.setHflex("1");
	}

	private void createChildInfor_NearAddress(Component parent) {

		divGroupChildNearAddress = new Div();
		divGroupChildNearAddress.setId("divGroupChildNearAddress");
		divGroupChildNearAddress.setParent(parent);
		divGroupChildNearAddress.setSclass("form-group divGroupChildNearAddress");

	}

	private void createChildInfor_PointStart_(Component parent) {

		Div divGroupChildPointStartForm = new Div();
		divGroupChildPointStartForm.setId("divdivGroupChildPointStartForm_");
		divGroupChildPointStartForm.setParent(parent);
		divGroupChildPointStartForm.setSclass("form-group");

		lbSearchAddressStart_F3 = new Label("F3");
		lbSearchAddressStart_F3.setParent(divGroupChildPointStartForm);
		lbSearchAddressStart_F3.setAttribute("for", "findAddressEditorStart");
		lbSearchAddressStart_F3.setSclass("lbF2 infor_Customer");

		findAddressEditorStart = new FindAddressEditor(" Nhập địa chỉ đón", lbSearchAddressStart_F3);
		findAddressEditorStart.setSearchType(0);
		findAddressEditorStart.getComponent().setParent(divGroupChildPointStartForm);
		findAddressEditorStart.getComponent().setId("findAddressEditorStart");
		findAddressEditorStart.getComponent().setSclass("divCommonComponent");
		findAddressEditorStart.handler = this;

	}

	private void createChildInfor_PointEnd_(Component parent) {

		Div divGroupChildPointEndForm = new Div();
		divGroupChildPointEndForm.setId("divGroupChildPointEndForm_");
		divGroupChildPointEndForm.setParent(parent);
		divGroupChildPointEndForm.setSclass("form-group");

		lbSearchAddrssEnd_F4 = new Label("F4");
		lbSearchAddrssEnd_F4.setParent(divGroupChildPointEndForm);
		lbSearchAddrssEnd_F4.setAttribute("for", "findAddressEditorEnd");
		lbSearchAddrssEnd_F4.setSclass("lbF2 infor_Customer");

		findAddressEditorEnd = new FindAddressEditor(" Nhập địa chỉ trả...", lbSearchAddrssEnd_F4);
		findAddressEditorEnd.setSearchType(1);
		findAddressEditorEnd.getComponent().setId("findAddressEditorEnd");
		findAddressEditorEnd.getComponent().setParent(divGroupChildPointEndForm);
		findAddressEditorEnd.getComponent().setSclass("divCommonComponent");
		findAddressEditorEnd.handler = this;
	}

	private void createChildInfor_VehicleRegister(Component parent) {

		Div divGroupChildPointVehicleRegisterForm = new Div();
		divGroupChildPointVehicleRegisterForm.setId("divGroupChildPointVehicleRegisterForm");
		divGroupChildPointVehicleRegisterForm.setParent(parent);
		divGroupChildPointVehicleRegisterForm.setSclass("form-group");

		divShow_VehicleRegister = new Div();
		divShow_VehicleRegister.setParent(parent);// divGroupChildPointVehicleRegisterForm);
		divShow_VehicleRegister.setId("divShow_VehicleRegister");
		divShow_VehicleRegister.setClass("taxiOrder2divTbShow");
	}

	private void createChildInfor_StartNow(Component parent) {

		Div divChildInfor_AdvanceShapeCommon_PanelBody = new Div();
		divChildInfor_AdvanceShapeCommon_PanelBody.setParent(parent);
		divChildInfor_AdvanceShapeCommon_PanelBody.setId("divChildInfor_AdvanceShapeCommon_PanelBody");
		divChildInfor_AdvanceShapeCommon_PanelBody.setSclass("form-group");
		/*
		 * detail left
		 */

		Div divLeft = new Div();
		divLeft.setParent(divChildInfor_AdvanceShapeCommon_PanelBody);
		divLeft.setClass("taxiOrder2divLeft");

		Radiogroup groupOption = new Radiogroup();
		groupOption.setParent(divLeft);
		rdOptionStartNow = new Radio();
		rdOptionStartNow.setParent(divLeft);
		rdOptionStartNow.setId("rdOptionStartNow");
		rdOptionStartNow.setLabel("Đi Ngay");
		rdOptionStartNow.setSclass("col-sm-4 optionCommonPanel");
		rdOptionStartNow.setRadiogroup(groupOption);
		rdOptionStartNow.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (rdOptionStartNow.isChecked()) {
					dbReceiveDate.setDisabled(true);
					rdOptionSomeTime.setChecked(false);
				}
			}
		});

		rdOptionSomeTime = new Radio();
		rdOptionSomeTime.setParent(divLeft);
		rdOptionSomeTime.setId("rbOptionSomeTime");
		rdOptionSomeTime.setLabel("Thời Gian");
		rdOptionSomeTime.setSclass("col-sm-2 optionCommonPanel");
		rdOptionSomeTime.setRadiogroup(groupOption);
		rdOptionSomeTime.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (rdOptionSomeTime.isChecked()) {
					dbReceiveDate.setDisabled(false);
					rdOptionStartNow.setChecked(false);
				}
			}
		});
		/*
		 * time
		 */
		Div divChildInfor_AdvanceShapeTime = new Div();
		divChildInfor_AdvanceShapeTime.setParent(divLeft);
		divChildInfor_AdvanceShapeTime.setId("divChildInfor_AdvanceShapeTime");
		divChildInfor_AdvanceShapeTime.setSclass("AdvanceShapeTime");

		dbReceiveDate = new Datebox();
		dbReceiveDate.setParent(divChildInfor_AdvanceShapeTime);
		dbReceiveDate.setId("divChildInfor_AdvancedbdbReceiveDate");
		dbReceiveDate.setSclass("optionCommonPanel");
		dbReceiveDate.setCols(9);
		dbReceiveDate.setHeight("30px");
		dbReceiveDate.setWidth("160px");
		dbReceiveDate.setFormat("HH:mm dd/MM/yyyy");
		dbReceiveDate.setLocale("vi");

		dbReceiveDate.addEventListener(Events.ON_FOCUS, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				dbReceiveDate.setSelectionRange(0, 2);

			}
		});
	}

	private void createChildInfor_AdvanceShapeTypeTrack(Component parent) {

		Div divChildInfor_AdvanceShapeTypeTrack_Panel = new Div();
		divChildInfor_AdvanceShapeTypeTrack_Panel.setParent(parent);
		divChildInfor_AdvanceShapeTypeTrack_Panel.setId("divChildInfor_AdvanceShapeTypeTrack_Panel");
		divChildInfor_AdvanceShapeTypeTrack_Panel.setSclass("form-group");// panel-info
		/*
		 * detail
		 */

		Radiogroup groupOption = new Radiogroup();
		groupOption.setParent(divChildInfor_AdvanceShapeTypeTrack_Panel);

		rdOptionNormal = new Radio();
		rdOptionNormal.setParent(divChildInfor_AdvanceShapeTypeTrack_Panel);
		rdOptionNormal.setId("rdOptionNormal");
		rdOptionNormal.setLabel("Đi Thường");
		rdOptionNormal.setSclass("optionCommonPanel");
		rdOptionNormal.setRadiogroup(groupOption);
		rdOptionNormal.setStyle("margin-left:15px");
		rdOptionNormal.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (rdOptionNormal.isChecked()) {
					rdOptionConTract.setChecked(false);
					rdOptionAirStation.setChecked(false);
					rdOptionSomePerson.setChecked(false);
					divAirgroup.setVisible(false);
					divContractgroup.setVisible(false);
				}
			}
		});

		rdOptionConTract = new Radio();
		rdOptionConTract.setParent(divChildInfor_AdvanceShapeTypeTrack_Panel);
		rdOptionConTract.setId("rdOptionConTract");
		rdOptionConTract.setLabel("Đi Hợp Đồng");
		rdOptionConTract.setSclass("optionCommonPanel");
		rdOptionConTract.setRadiogroup(groupOption);
		rdOptionConTract.setStyle("margin-left:40px");
		rdOptionConTract.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (rdOptionConTract.isChecked()) {
					rdOptionNormal.setChecked(false);
					rdOptionAirStation.setChecked(false);
					rdOptionSomePerson.setChecked(false);
					divAirgroup.setVisible(false);
					divContractgroup.setVisible(true);
				}
			}
		});

		rdOptionAirStation = new Radio();
		rdOptionAirStation.setParent(divChildInfor_AdvanceShapeTypeTrack_Panel);
		rdOptionAirStation.setId("rdOptionAirStation");
		rdOptionAirStation.setLabel("Đi Sân Bay");
		rdOptionAirStation.setSclass("optionCommonPanel");
		rdOptionAirStation.setRadiogroup(groupOption);
		rdOptionAirStation.setStyle("margin-left:40px");
		rdOptionAirStation.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (rdOptionAirStation.isChecked()) {
					rdOptionNormal.setChecked(false);
					rdOptionConTract.setChecked(false);
					rdOptionSomePerson.setChecked(false);
					divAirgroup.setVisible(true);
					divContractgroup.setVisible(false);
				}
			}
		});

		rdOptionSomePerson = new Radio();
		rdOptionSomePerson.setParent(divChildInfor_AdvanceShapeTypeTrack_Panel);
		rdOptionSomePerson.setId("rdOptionSomePerson");
		rdOptionSomePerson.setLabel("Đi Chung");
		rdOptionSomePerson.setSclass("optionCommonPanel");
		rdOptionSomePerson.setRadiogroup(groupOption);
		rdOptionSomePerson.setStyle("margin-left:40px");
		rdOptionSomePerson.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (rdOptionSomePerson.isChecked()) {
					rdOptionNormal.setChecked(false);
					rdOptionConTract.setChecked(false);
					rdOptionAirStation.setChecked(false);
					divAirgroup.setVisible(false);
					divContractgroup.setVisible(false);
				}
			}
		});
		/*
		 * opiton hop dong
		 */
		divContractgroup = new Div();
		divContractgroup.setParent(divChildInfor_AdvanceShapeTypeTrack_Panel);
		divContractgroup.setClass("air-heading");
		divContractgroup.setId("divContractgroup");
		divContractgroup.setStyle("margin-top:40px");
		divContractgroup.setVisible(false);

		Radiogroup groupct = new Radiogroup();
		groupct.setParent(divContractgroup);

		rdOptionAironewayct = new Radio();
		rdOptionAironewayct.setParent(divContractgroup);
		rdOptionAironewayct.setId("rdOptionAironewayct");
		rdOptionAironewayct.setLabel("1 chiều");
		rdOptionAironewayct.setSclass("airway optionCommonPanel");
		rdOptionAironewayct.setRadiogroup(groupct);
		rdOptionAironewayct.setStyle("margin-right:150px; margin-left:100px");
		rdOptionAironewayct.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (rdOptionAironewayct.isChecked()) {
					AbstractModel.setValue(getModel(), "twoWay", false);
					rdOptionAirtwowayct.setChecked(false);
				}
			}
		});

		rdOptionAirtwowayct = new Radio();
		rdOptionAirtwowayct.setParent(divContractgroup);
		rdOptionAirtwowayct.setId("rdOptionAirtowwayct");
		rdOptionAirtwowayct.setLabel("2 chiều");
		rdOptionAirtwowayct.setSclass("airway optionCommonPanel");
		rdOptionAirtwowayct.setRadiogroup(groupct);
		rdOptionAirtwowayct.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (rdOptionAirtwowayct.isChecked()) {
					AbstractModel.setValue(getModel(), "twoWay", true);
					rdOptionAironewayct.setChecked(false);
				}
			}
		});
		/*
		 * km du tÃ­nh
		 */
		Div divChildAirct = new Div();
		divChildAirct.setParent(divContractgroup);
		divChildAirct.setClass("air-heading");
		divChildAirct.setId("divChildAirct");
		divChildAirct.setStyle("margin-top:10px");

		lbOptionAirkmct = new Label("Km Dự Tính:");
		lbOptionAirkmct.setParent(divChildAirct);
		lbOptionAirkmct.setId("lbOptionAirKmct");
		lbOptionAirkmct.setClass("air optionCommonPanel");
		lbOptionAirkmct.setStyle("margin-right:28px; margin-left:80px;");

		tbOptionAirPricekmct = new Textbox();
		tbOptionAirPricekmct.setParent(divChildAirct);
		tbOptionAirPricekmct.setId("tbOptionAirPricekmct");
		tbOptionAirPricekmct.setClass("air optionCommonPanel");
		tbOptionAirPricekmct.setStyle("margin-right:5px");
		tbOptionAirPricekmct.setWidth("120px");

		lbOptionKmct = new Label("Km");
		lbOptionKmct.setParent(divChildAirct);
		lbOptionKmct.setId("lbOptionKmct");
		lbOptionKmct.setClass("air optionCommonPanel");
		lbOptionKmct.setStyle("Margin-right:20px");

		lbOptionAirtime = new Label("T.Gian Dự Tính:");
		lbOptionAirtime.setParent(divChildAirct);
		lbOptionAirtime.setId("lbOptionAirtime");
		lbOptionAirtime.setClass("air optionCommonPanel");
		lbOptionAirtime.setStyle("margin-right:5px; margin-left:30px;");

		tbOptionTimect = new Textbox();
		tbOptionTimect.setParent(divChildAirct);
		tbOptionTimect.setId("tbOptionTimect");
		tbOptionTimect.setClass("air optionCommonPanel");
		tbOptionTimect.setStyle("margin-right:5px");
		tbOptionTimect.setWidth("100px");

		lbOptionTimect = new Label("Phút");
		lbOptionTimect.setParent(divChildAirct);
		lbOptionTimect.setId("lbOptionTimect");
		lbOptionTimect.setClass("air optionCommonPanel");
		lbOptionTimect.setStyle("Margin-right:30px");

		/*
		 * so tien
		 */

		Div divMoneyct = new Div();
		divMoneyct.setParent(divContractgroup);
		divMoneyct.setClass("air-heading");
		divMoneyct.setId("divMoneyct");
		divMoneyct.setStyle("margin-top:10px");

		lbOptionMoneyct = new Label("Số Tiền Dự Tính:");
		lbOptionMoneyct.setParent(divMoneyct);
		lbOptionMoneyct.setId("lbOptionMoneyct");
		lbOptionMoneyct.setClass("air optionCommonPanel");
		lbOptionMoneyct.setStyle("margin-right:5px; margin-left:80px;");

		tbOptionPricect = new Textbox();
		tbOptionPricect.setParent(divMoneyct);
		tbOptionPricect.setId("tbOptionPricect");
		tbOptionPricect.setClass("air optionCommonPanel");
		tbOptionPricect.setStyle("margin-right:5px");
		tbOptionPricect.setWidth("120px");

		lbOptionVNDct = new Label("VND");
		lbOptionVNDct.setParent(divMoneyct);
		lbOptionVNDct.setId("lbOptionVNDct");
		lbOptionVNDct.setClass("air optionCommonPanel");
		lbOptionVNDct.setStyle("margin-right:63px");

		cbOptionloadkm = new Combobox("--Bảng Giá--");
		cbOptionloadkm.setParent(divMoneyct);
		cbOptionloadkm.setId("cbOptionloadkm");
		cbOptionloadkm.setClass("air optionCommonPanel");
		cbOptionloadkm.setWidth("150px");
		cbOptionloadkm.setReadonly(true);
		cbOptionloadkm.setStyle("margin-right:78px");

		btOptionloadct = new Button("Lấy Dữ Liệu");
		btOptionloadct.setId("btOptionloadct");
		btOptionloadct.setParent(divMoneyct);
		btOptionloadct.setClass("air optionCommonPanel");
		btOptionloadct.setWidth("120px");
		/*
		 * tgian phu troi
		 */
		Div divMoneytimeaddct = new Div();
		divMoneytimeaddct.setParent(divContractgroup);
		divMoneytimeaddct.setClass("air-heading");
		divMoneytimeaddct.setId("divMoneytimeaddct");
		divMoneytimeaddct.setStyle("margin-top:15px");

		lbOptionkmaddct = new Label("Km Phụ Trội:");
		lbOptionkmaddct.setParent(divMoneytimeaddct);
		lbOptionkmaddct.setId("lbOptionkmaddct");
		lbOptionkmaddct.setClass("air optionCommonPanel");
		lbOptionkmaddct.setStyle("margin-right:27px; margin-left:80px;");

		tbOptionPricekmctadd = new Textbox();
		tbOptionPricekmctadd.setParent(divMoneytimeaddct);
		tbOptionPricekmctadd.setId("tbOptionPricekmctadd");
		tbOptionPricekmctadd.setClass("air optionCommonPanel");
		tbOptionPricekmctadd.setStyle("margin-right:5px");
		tbOptionPricekmctadd.setWidth("120px");

		lbOptionkmctadd = new Label("Km");
		lbOptionkmctadd.setParent(divMoneytimeaddct);
		lbOptionkmctadd.setId("lbOptionkmctadd");
		lbOptionkmctadd.setClass("air optionCommonPanel");

		lbOptiontimeaddct = new Label("T.Gian Phụ Trội:");
		lbOptiontimeaddct.setParent(divMoneytimeaddct);
		lbOptiontimeaddct.setId("lbOptiontimeaddct");
		lbOptiontimeaddct.setClass("air optionCommonPanel");
		lbOptiontimeaddct.setStyle("margin-right:5px; margin-left:50px;");

		tbOptionPricetimectadd = new Textbox();
		tbOptionPricetimectadd.setParent(divMoneytimeaddct);
		tbOptionPricetimectadd.setId("tbOptionPricetimectadd");
		tbOptionPricetimectadd.setClass("air optionCommonPanel");
		tbOptionPricetimectadd.setStyle("margin-right:5px");
		tbOptionPricetimectadd.setWidth("100px");

		lbOptionVNDcttimeadd = new Label("Phút");
		lbOptionVNDcttimeadd.setParent(divMoneytimeaddct);
		lbOptionVNDcttimeadd.setId("lbOptionVNDcttimeadd");
		lbOptionVNDcttimeadd.setClass("air optionCommonPanel");
		lbOptionVNDcttimeadd.setStyle("margin-right:110px");

		/*
		 * phÃ­ phu troi
		 */

		Div divMoneyaddct = new Div();
		divMoneyaddct.setParent(divContractgroup);
		divMoneyaddct.setClass("air-heading");
		divMoneyaddct.setId("divMoneyaddct");
		divMoneyaddct.setStyle("margin-top:15px");

		lbOptionaddct = new Label("Số Tiền Phụ Trội:");
		lbOptionaddct.setParent(divMoneyaddct);
		lbOptionaddct.setId("lbOptionaddct");
		lbOptionaddct.setClass("air optionCommonPanel");
		lbOptionaddct.setStyle("margin-right:5px; margin-left:80px");

		tbOptionPricectadd = new Textbox();
		tbOptionPricectadd.setParent(divMoneyaddct);
		tbOptionPricectadd.setId("tbOptionPricectadd");
		tbOptionPricectadd.setClass("air optionCommonPanel");
		tbOptionPricectadd.setStyle("margin-right:5px");
		tbOptionPricectadd.setWidth("120px");

		lbOptionVNDctadd = new Label("VND");
		lbOptionVNDctadd.setParent(divMoneyaddct);
		lbOptionVNDctadd.setId("lbOptionVNDctadd");
		lbOptionVNDctadd.setClass("air optionCommonPanel");
		lbOptionVNDctadd.setStyle("margin-right:110px");

		/*
		 * tong tien
		 */
		Div lbMoneyTotal = new Div();
		lbMoneyTotal.setParent(divContractgroup);
		lbMoneyTotal.setClass("air-heading");
		lbMoneyTotal.setId("lbMoneyTotal");
		lbMoneyTotal.setStyle("margin-top:10px");

		lbMoney = new Label("Tổng Tiền:");
		lbMoney.setParent(lbMoneyTotal);
		lbMoney.setId("lbMoney");
		lbMoney.setClass("air optionCommonPanel");
		lbMoney.setStyle("margin-right:39px; margin-left:80px");

		tbMoney = new Textbox();
		tbMoney.setParent(lbMoneyTotal);
		tbMoney.setId("tbMoney");
		tbMoney.setClass("air optionCommonPanel");
		tbMoney.setStyle("margin-right:5px");
		tbMoney.setWidth("120px");

		lbVND = new Label("VND");
		lbVND.setParent(lbMoneyTotal);
		lbVND.setId("lbVND");
		lbVND.setClass("air optionCommonPanel");
		lbVND.setStyle("margin-right:290px");

		btloadMoney = new Button("Lấy Tổng Tiền");
		btloadMoney.setId("btloadMoney");
		btloadMoney.setParent(lbMoneyTotal);
		btloadMoney.setClass("air optionCommonPanel");
		btloadMoney.setWidth("120px");

		// lay bang gia

		controller = (TypeTablePriceController) ControllerUtils.getController(TypeTablePriceController.class);
		String sql = "from TypeTablePrice";
		List<TypeTablePrice> lstValue = controller.find(sql);
		cbOptionloadkm.setItemRenderer(new ComboitemRenderer<TypeTablePrice>() {
			@Override
			public void render(Comboitem item, TypeTablePrice data, int arg2) throws Exception {
				if (data != null) {
					item.setLabel(data.getTablepricename());
					item.setValue(data);
				} else {
					item.setLabel("");
					item.setValue(null);
				}
			}
		});
		cbOptionloadkm.setModel(new ListModelList<TypeTablePrice>(lstValue));

		/*
		 * option sÃ¢n bay
		 */
		divAirgroup = new Div();
		divAirgroup.setParent(divChildInfor_AdvanceShapeTypeTrack_Panel);
		divAirgroup.setClass("air-heading");
		divAirgroup.setId("divAirgroup");
		divAirgroup.setStyle("margin-top:40px");
		divAirgroup.setVisible(false);

		Radiogroup group = new Radiogroup();
		group.setParent(divAirgroup);

		rdOptionAironeway = new Radio();
		rdOptionAironeway.setParent(divAirgroup);
		rdOptionAironeway.setId("rdOptionAironeway");
		rdOptionAironeway.setLabel("1 chiều");
		rdOptionAironeway.setSclass("airway optionCommonPanel");
		rdOptionAironeway.setRadiogroup(group);
		rdOptionAironeway.setStyle("margin-right:150px; margin-left:90px");
		rdOptionAironeway.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (rdOptionAironeway.isChecked()) {
					AbstractModel.setValue(getModel(), "twoWay", false);
				}
			}
		});

		rdOptionAirtwoway = new Radio();
		rdOptionAirtwoway.setParent(divAirgroup);
		rdOptionAirtwoway.setId("rdOptionAirtowway");
		rdOptionAirtwoway.setLabel("2 chiều");
		rdOptionAirtwoway.setSclass("airway optionCommonPanel");
		rdOptionAirtwoway.setRadiogroup(group);
		rdOptionAirtwoway.setStyle("margin-right:400px");
		rdOptionAirtwoway.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (rdOptionAirtwoway.isChecked()) {
					AbstractModel.setValue(getModel(), "twoWay", true);
				}
			}
		});

		/*
		 * km du tÃ­nh
		 */

		Div divChildAir = new Div();
		divChildAir.setParent(divAirgroup);
		divChildAir.setClass("air-heading");
		divChildAir.setId("divChildAir");
		divChildAir.setStyle("margin-top:10px");

		lbOptionAirkm = new Label("Km Dự Tính:");
		lbOptionAirkm.setParent(divChildAir);
		lbOptionAirkm.setId("lbOptionAirKm");
		lbOptionAirkm.setClass("air optionCommonPanel");
		lbOptionAirkm.setStyle("margin-right:24px; margin-left:80px;");

		tbOptionAirPricekm = new Textbox();
		tbOptionAirPricekm.setParent(divChildAir);
		tbOptionAirPricekm.setId("tbOptionAirPricekm");
		tbOptionAirPricekm.setClass("air optionCommonPanel");
		tbOptionAirPricekm.setStyle("margin-right:5px");
		tbOptionAirPricekm.setWidth("120px");

		lbOptionKm = new Label("Km");
		lbOptionKm.setParent(divChildAir);
		lbOptionKm.setId("lbOptionKm");
		lbOptionKm.setClass("air optionCommonPanel");
		/*
		 * so tien
		 */
		Div divMoney = new Div();
		divMoney.setParent(divAirgroup);
		divMoney.setClass("air-heading");
		divMoney.setId("divMoney");
		divMoney.setStyle("margin-top:10px");

		lbOptionMoney = new Label("Số Tiền:");
		lbOptionMoney.setParent(divMoney);
		lbOptionMoney.setId("lbOptionMoney");
		lbOptionMoney.setClass("air optionCommonPanel");
		lbOptionMoney.setStyle("margin-right:47px; margin-left:80px;");

		tbOptionPrice = new Textbox();
		tbOptionPrice.setParent(divMoney);
		tbOptionPrice.setId("tbOptionPrice");
		tbOptionPrice.setClass("air optionCommonPanel");
		tbOptionPrice.setStyle("margin-right:5px");
		tbOptionPrice.setWidth("120px");

		lbOptionVND = new Label("VND");
		lbOptionVND.setParent(divMoney);
		lbOptionVND.setId("lbOptionVND");
		lbOptionVND.setClass("air optionCommonPanel");
		lbOptionVND.setStyle("margin-right:50px");

		/*
		 * phÃ­ phu troi
		 */

		Div divMoneyadd = new Div();
		divMoneyadd.setParent(divAirgroup);
		divMoneyadd.setClass("air-heading");
		divMoneyadd.setId("divMoneyadd");
		divMoneyadd.setStyle("margin-top:10px");

		lbOptionadd = new Label("Phụ Trội:");
		lbOptionadd.setParent(divMoneyadd);
		lbOptionadd.setId("lbOptionadd");
		lbOptionadd.setClass("air optionCommonPanel");
		lbOptionadd.setStyle("margin-right:42px; margin-left:80px");

		tbOptionPriceadd = new Textbox();
		tbOptionPriceadd.setParent(divMoneyadd);
		tbOptionPriceadd.setId("tbOptionPriceadd");
		tbOptionPriceadd.setClass("air optionCommonPanel");
		tbOptionPriceadd.setStyle("margin-right:5px");
		tbOptionPriceadd.setWidth("120px");

		lbOptionVNDadd = new Label("VND");
		lbOptionVNDadd.setParent(divMoneyadd);
		lbOptionVNDadd.setId("lbOptionVNDadd");
		lbOptionVNDadd.setClass("air optionCommonPanel");
		lbOptionVNDadd.setStyle("margin-right:110px");
	}

	private void createGroup_Contract(Component parent) {
		Div divContract = new Div();
		divContract.setParent(parent);
		divContract.setClass("contract-group");
		divContract.setId("divContract");
		divContract.setWidth("100%");
		divContract.setStyle("margin-top:20px");

	}

	private void createChildInfor_AdvanceTypeVehicle(Component parent) {

		carTypeSelectorContainer = new CarTypeSelectorContainer();
		carTypeSelectorContainer.setId("divChildInfor_AdvanceTypeVehicle");
		carTypeSelectorContainer.setParent(parent);
		carTypeSelectorContainer.setSclass("form-group divTypeVehicle divCommonComponent");

		lbOrder_F2 = new Label("F2");
		lbOrder_F2.setParent(parent);
		lbOrder_F2.setSclass("lbF2 infor_Customer");
		lbOrder_F2.setWidth("18px");

	}

	private void createChildInfor_AdvanceChanel(List<ChannelTms> lstChannelTms, Component comp) {

		@SuppressWarnings("unchecked")
		List<AbstractModel> tmpList = (List<AbstractModel>) (Object) lstChannelTms;
		chanelSelector = new M2OGridEditor(tmpList);
		chanelSelector.getComponent().setParent(comp);
	}

	public void focusDefaultControl() {

		tbNoteForCustmer.setVisible(true);
		tbNoteForCustmer.setFocus(true);
	}

	private void getDefaultData() {
		this.lstChannel = getTaxiOrderForm().getLstChannel_In_Switchboard();
	}

	private void callIn_End() {

		handlerTaxiOrderDetail.onTaxiOrderCloseForm(tabContain, getCallInfo());
	};

	private void cleanValueForm() {
		/*
		 * clear error message
		 */
		tbPhone.clearErrorMessage();
		tbNoteForCustmer.clearErrorMessage();
		tbNoteForTaxiOrder.clearErrorMessage();
		findAddressEditorEnd.getComponent().clearErrorMessage();
		findAddressEditorStart.getComponent().clearErrorMessage();
		dbReceiveDate.clearErrorMessage();
		tbOptionAirPricekm.clearErrorMessage();
		tbOptionAirPricekmct.clearErrorMessage();
		tbOptionPricect.clearErrorMessage();
		/*
		 * clear value
		 */
		tbPhone.setValue("");
		tbNoteForCustmer.setValue("");
		tbNoteForTaxiOrder.setValue("");

		divShow_VehicleRegister.getChildren().clear();
		divHeaderInforCall.getChildren().clear();
		divGroupChildNearAddress.getChildren().clear();

		cbExtension.getItems().clear();
		findAddressEditorEnd.getComponent().getItems().clear();
		findAddressEditorStart.getComponent().getItems().clear();

		findAddressEditorStart.getComponent().setText("");
		findAddressEditorEnd.getComponent().setText("");

		dbReceiveDate.setText("");

		lstVehicleRegister.clear();
	}

	public void setValue_UI(boolean isTextSearch) {
		cleanValueForm();
		try {
			this.setHeight("100%");
			this.setWidth("100%");

			/*
			 * set title Form
			 */
			StringBuilder strTitle = new StringBuilder();
			strTitle.append(" " + getCallInfo().getVoipExtension());

			/*
			 * set channel for search address
			 */
			List<TelephoneExtensionTms> lstExtensions = getTaxiOrderForm().getLstExtensions();
			String extensionNumber = getCallInfo().getVoipExtension();
			if (getCallInfo().getCallerNumber() == "") {
				if (lstExtensions.size() > 0) {
					TelephoneExtensionTms firstExtension = lstExtensions.get(0);
					extensionNumber = firstExtension.getExtension();
				}
			}
			for (TelephoneExtensionTms extensionTms : lstExtensions) {
				if (extensionTms.getExtension().equalsIgnoreCase(extensionNumber)) {
					findAddressEditorStart.setChannel(extensionTms.getChannel());
					findAddressEditorEnd.setChannel(extensionTms.getChannel());
				}
			}

			handlerTaxiOrderDetail.onTaxiOrderChangeTitle(tabContain, strTitle.toString());

			/*
			 * get taxi Order
			 */

			Customer cust = getTaxiOrder().getCustomer();
			if (cust.getId() > 0) {
				didLoadCustomer(cust);
			}
			/*
			 * Data
			 */

			if (getTaxiOrder().getId() <= 0) {
				rdOptionStartNow.setChecked(true);
				dbReceiveDate.setDisabled(true);
				rdOptionNormal.setChecked(true);
				rdOptionAironeway.setChecked(true);
				rdOptionAironewayct.setChecked(true);
			}

			int repeatTime = getTaxiOrder().getRepeatTime() - 1;

			String strLastCall = "[0:0]";
			if (cust.getLastTimeCall() != null)
				strLastCall = new SimpleDateFormat("H:mm [dd-MM-yyyy]").format(cust.getLastTimeCall());// getTaxiOrder().getTimeGoiNhac());
			// //
			// 9:00
			addLabelHeader_aboutInforCustomer(divHeaderInforCall, repeatTime, cust.getTotalSuccessOrder(), strLastCall);

			/*-- set channel --*/
			Address addressCurrent = new Address();
			if (getTaxiOrder().getBeginAddress() != null) {
				addressCurrent = new Address(getTaxiOrder().getBeginAddress(), getTaxiOrder().getBeginLat(),
						getTaxiOrder().getBeginLon());
			}
			if (getTaxiOrder().getId() > 0) { // Incase edit taxi Order
				addressCurrent = new Address(getTaxiOrder().getBeginAddress(), getTaxiOrder().getBeginLat(),
						getTaxiOrder().getBeginLon());
				if (getTaxiOrder().getChannel() != null) {
					chanelSelector.setValue(getTaxiOrder().getChannel());
					map.setRiderMarker(addressCurrent.getLatitude(), addressCurrent.getLongitude());
					GoogleMapUntil.scaleMap(map.getGmaps());
				}
			} else {
				FindNearChannel findNearChannel = new FindNearChannel();
				ChannelTms channel = findNearChannel.getChannel(addressCurrent.getLongitude(),
						addressCurrent.getLatitude(), lstChannel);
				if (channel != null) {
					chanelSelector.setValue(channel);
				}
			}

			if (getTaxiOrder().getId() > 0) {
				if (getTaxiOrder().getSomeTime()) {
					rdOptionSomeTime.setChecked(true);
					dbReceiveDate.setValue(getTaxiOrder().getBeginOrderTime());
				} else {
					rdOptionStartNow.setChecked(true);
					dbReceiveDate.setDisabled(true);
				}

				if (getTaxiOrder().getGoNormal()) {
					rdOptionNormal.setChecked(true);
				}

				if (getTaxiOrder().getConTract()) {
					divContractgroup.setVisible(true);
					rdOptionConTract.setChecked(true);
					tbOptionAirPricekmct.setValue(getTaxiOrder().getPriceOrderKm());
					tbOptionAirPricekmct.setDisabled(false);
					tbMoney.setValue(getTaxiOrder().getMoneytotal().toString());
					tbMoney.setDisabled(false);
					tbOptionPricetimectadd.setValue(getTaxiOrder().getTimeAdd().toString());
					tbOptionPricetimectadd.setDisabled(false);
					tbOptionPricekmctadd.setValue(getTaxiOrder().getPirceKmadd().toString());
					tbOptionPricekmctadd.setDisabled(false);
					tbOptionTimect.setValue(getTaxiOrder().getTimect().toString());
					tbOptionTimect.setDisabled(false);
					tbOptionPricect.setValue(getTaxiOrder().getFareEstimate().toString());
					tbOptionPricect.setDisabled(false);
					tbOptionPricectadd.setValue(getTaxiOrder().getFareAdd().toString());
					tbOptionPricectadd.setDisabled(false);
					cbOptionloadkm.setValue(getTaxiOrder().getLoadData().toString());

					btOptionloadct.setVisible(false);
					btloadMoney.setVisible(false);

					if (getTaxiOrder().getTwoWay()) {
						rdOptionAirtwowayct.setChecked(true);
					} else {
						rdOptionAironewayct.setChecked(true);
					}
				}

				if (getTaxiOrder().getAirStation()) {
					rdOptionAirStation.setChecked(true);
					tbOptionAirPricekm.setValue(getTaxiOrder().getPriceOrderKm());
					tbOptionAirPricekm.setDisabled(false);
					tbOptionPrice.setValue(getTaxiOrder().getFareEstimate().toString());
					tbOptionPrice.setDisabled(false);
					tbOptionPriceadd.setValue(getTaxiOrder().getFareAdd().toString());
					tbOptionPriceadd.setDisabled(false);
					divAirgroup.setVisible(true);
					if (getTaxiOrder().getTwoWay()) {
						rdOptionAirtwoway.setChecked(true);
					} else {
						rdOptionAironeway.setChecked(true);
					}
				}

				if (getTaxiOrder().getSomePerson()) {
					rdOptionSomePerson.setChecked(true);
				}
			}

			findAddressEditorStart.getComponent().getItems().clear();
			Address address;
			Comboitem item = new Comboitem(addressCurrent.getName());
			item.setValue(addressCurrent);
			findAddressEditorStart.getComponent().getItems().add(item);
			findAddressEditorStart.getComponent().setSelectedItem(item);
			findAddressEditorStart.setValue(addressCurrent);

			/*
			 * end address
			 */
			if (!getTaxiOrder().getEndOrderLat().equals(0.0) && !getTaxiOrder().getEndOrderLon().equals(0.0)) {

				address = new Address();
				address.setName((getTaxiOrder().getEndOrderAddress() == null) ? ""
						: getTaxiOrder().getEndOrderAddress().trim());
				address.setLatitude(getTaxiOrder().getEndOrderLat());
				address.setLongitude(getTaxiOrder().getEndOrderLon());
				item = new Comboitem(address.getName());
				item.setValue(address);
				findAddressEditorEnd.getComponent().getItems().add(item);
				findAddressEditorEnd.getComponent().setSelectedIndex(0);

				map.markerAddrTo(new LatLng(0, 0));
				// getDistance_DirectionBetweenTwoAddress();
			} else
				map.markerAddrTo(new LatLng(0, 0));

			/*
			 * order car type
			 */

			carTypeSelectorContainer.setCarType(getTaxiOrder().getOrderCarType());
			if (getTaxiOrder().getId() > 0) {
				carTypeSelectorContainer.disable();
			}

			Set<Vehicle> lstRegisterVehicle = getTaxiOrder().getRegistedTaxis();
			String strLstVehicleRegister = "";
			for (Vehicle vehicle : lstRegisterVehicle) {
				strLstVehicleRegister += vehicle.getId() + ",";
			}

			if (!strLstVehicleRegister.isEmpty()) {
				strLstVehicleRegister = strLstVehicleRegister.substring(0, strLstVehicleRegister.length() - 1);
				LoadTaxiRegistedWorker loadTaxiRegistedWorder = new LoadTaxiRegistedWorker(strLstVehicleRegister);
				loadTaxiRegistedWorder.start();
			} else {
				divShow_VehicleRegister.detach();
			}

			/*
			 * note for taxi order
			 */
			try {
				tbNoteForTaxiOrder.setValue(getTaxiOrder().getNote());
			} catch (Exception e) {
			}

			/*
			 * phone number note for customer
			 */

			tbNoteForCustmer.setValue(cust.getNote());
			// cust.getId() > 0 &&
			if (cust.getPhoneNumber().length() > 5)
				tbPhone.setValue(cust.getPhoneNumber());

			if (!isTextSearch) {
				findAddressEditorStart.getComponent().setFocus(true);
			} else {
				tbPhone.setFocus(true);
			}

			tbPhone.setDisabled(false);
			tbPhone.setFocus(true);

			/*
			 * disabled button save
			 */

			if (getTaxiOrder().getStatus() != EnumStatus.CREATING.getValue()
					&& getTaxiOrder().getStatus() != EnumStatus.DA_DOC_DAM.getValue()
					&& getTaxiOrder().getStatus() != EnumStatus.MOI.getValue()
					&& getTaxiOrder().getStatus() != EnumStatus.XE_DANG_KY_DON.getValue()) {

				getBtn_save().setVisible(false);
			}
			/*
			 * add button HỦY CUỐC
			 */
			if (getTaxiOrder().getStatus() == EnumStatus.MOI.getValue()
					|| getTaxiOrder().getStatus() == EnumStatus.DA_DOC_DAM.getValue()
					|| getTaxiOrder().getStatus() == EnumStatus.XE_DANG_KY_DON.getValue()) {

				btCancelOrder.setVisible(true);
				btNewOrder.setVisible(true);

				dbReceiveDate.setValue(getTaxiOrder().getBeginOrderTime());
			} else {

				btCancelOrder.setVisible(false);
				btNewOrder.setVisible(false);
			}

			getListExtensionCallOut();
			/*
			 * set default focus
			 */
			if (getTaxiOrder().getPhoneNumber().isEmpty())
				tbPhone.setFocus(true);
			else
				findAddressEditorStart.setFocus(true);
		} catch (Exception ex) {
			AppLogger.logTaxiorder.info("[Error]Update value on UI: " + getTaxiOrder().getPhoneNumber());
		}

	}

	public void updateRegistedTaxiNotification(List<TaxiResult> _lstVehicle, String destination_LatLong) {

		Clients.showBusy(divShow_VehicleRegister, "ĐANG ƯỚC TÍNH CÁC XE ĐANG KÝ ĐÓN...");
		DistanceBetweenTwoPoints_Run distance = new DistanceBetweenTwoPoints_Run(_lstVehicle, destination_LatLong,
				divShow_VehicleRegister, this.getDesktop());
		Thread tr = new Thread(distance);
		tr.setPriority(Thread.MAX_PRIORITY);
		tr.start();

	}

	private EventListener<Event> EVENT_STARTADDRESS_CHANGE = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			Address add = (Address) arg0.getData();

			findAddressEditorStart.getComponent().getItems().clear();
			Comboitem item = new Comboitem(add.getName());
			item.setValue(add);
			findAddressEditorStart.getComponent().getItems().add(item);
			findAddressEditorStart.getComponent().setSelectedItem(item);
			findAddressEditorStart.setValue(add);
		}

	};

	private void addLabelHeader_aboutInforCustomer(Component parent, int repeatTime, int totalCall,
			String strLastCall) {

		if (parent != null) {
			parent.getChildren().clear();

			ContentTag iconRepeatTime = new ContentTag("span");
			iconRepeatTime.setId(parent.getId() + "iconRepeatTime ");
			iconRepeatTime.setSclass("glyphicon glyphicon-star");
			iconRepeatTime.setAttribute("aria-hidden", "true");

			ContentTag iconLastCall = new ContentTag("span");
			iconLastCall.setId(parent.getId() + "iconLastCall");
			iconLastCall.setSclass("glyphicon glyphicons-history");
			iconLastCall.setAttribute("aria-hidden", "true");

			ContentTag iconTotalCall = new ContentTag("span");
			iconTotalCall.setId(parent.getId() + "iconTotalCall");
			iconTotalCall.setSclass("glyphicon glyphicon-ok-circle");
			iconTotalCall.setAttribute("aria-hidden", "true");

			Label lbRepeatTime = new Label("*GỌI NHẮC:");
			Label lbLastCall = new Label("*GỌI CUỐI:");
			Label lbTotalCall = new Label("*CHUYẾN THÀNH CÔNG:");

			Label lbRepeatTimeVal = new Label(String.valueOf(repeatTime));
			Label lbTotalCallVal = new Label(String.valueOf(totalCall));
			Label lbLastCallVal = new Label(String.valueOf(strLastCall));

			lbRepeatTime.setSclass("lbHeaderCommon");
			lbRepeatTimeVal.setSclass("lbHeaderValCommon lbRepeatTimeVal");

			lbTotalCall.setSclass("lbHeaderCommon");
			lbTotalCallVal.setSclass("lbHeaderValCommon lbTotalCallVal");

			lbLastCall.setSclass("lbHeaderCommon");
			lbLastCallVal.setSclass("lbHeaderValCommon lbLastCallVal");

			lbRepeatTime.setParent(parent);
			lbRepeatTimeVal.setParent(parent);

			lbLastCall.setParent(parent);
			lbLastCallVal.setParent(parent);

			lbTotalCall.setParent(parent);
			lbTotalCallVal.setParent(parent);
		}

	}

	private void setOptionPrice(Checkbox cb) {

		if (findAddressEditorEnd.getComponent() != null) {
			if (cb.isChecked()) {
				dbReceiveDate.setValue(new java.util.Date());
			}
		}
	}

	private void getDistance_DirectionBetweenTwoAddress() {

		Address form = (findAddressEditorStart.getValue() == null ? new Address()
				: findAddressEditorStart.getSearchValue());
		Address to = (findAddressEditorEnd.getValue() == null ? new Address() : findAddressEditorEnd.getSearchValue());

		if (form.getLatitude() == 0.0 || form.getLongitude() == 0.0 || to.getLatitude() == 0.0
				|| to.getLongitude() == 0.0)
			return;

	}

	private List<TablePrice> getListTablePriceByType(TypeTablePrice bean) {
		List<TablePrice> lst = new ArrayList<TablePrice>();
		TablePriceController controller = (TablePriceController) ControllerUtils
				.getController(TablePriceController.class);
		lst = controller.find("from TablePrice WHERE typeTablePrice = ? ORDER BY km ASC", bean);
		return lst;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if ((event.getName().equals(Events.ON_SELECT) || event.getName().equals(Events.ON_CHANGE))
				&& event.getTarget().equals(cbOptionloadkm)) {
			if (cbOptionloadkm.getSelectedIndex() >= 0) {
				TypeTablePrice price = cbOptionloadkm.getSelectedItem().getValue();
				List<TablePrice> lst = getListTablePriceByType(price);
				for (TablePrice tablePrice : lst) {
					tablePrice.getKm();
				}
			}
		}
		if (event.getTarget() == this.getBtn_save()) {

			if (saveInBackground()) {
				callIn_End();
				this.setVisible(false);
			}
		}

		else if (event.getTarget() == this.getBtn_cancel() || event.getName().equals(Events.ON_CANCEL)) {
			TaxiOrder taxiOrder = this.getTaxiOrder();
			callIn_End();
			cleanValueForm();
			this.setVisible(false);
			Env.getHomePage().showNotification("Bỏ qua thay đổi!", Clients.NOTIFICATION_TYPE_INFO);
			// Tuanpa: refresh order take too long time, so run it in background
			// thread
			RefreshOrderWorker worker = new RefreshOrderWorker();
			worker.order = taxiOrder;
			worker.start();
		}
	}

	class RefreshOrderWorker extends Thread {
		TaxiOrder order;

		public void run() {
			synchronized (_this) {
				try {
					if (order.getId() > 0)
						order.refresh();
				} catch (Exception e) {
					// ignore
				}
			}
		}
	}

	public void didUpdateStartAddressName(String addressName) {
		Address address = findAddressEditorStart.getSearchValue();
		address.setName(addressName);
		findAddressEditorStart.setValue(address);
		findAddressEditorStart.getComponent().setValue(addressName);
		Clients.clearBusy(_this.findAddressEditorStart.getComponent().getParent());
	}

	private void onMoveRiderMarker(double lat, double lng) {
		Clients.showBusy(_this.findAddressEditorStart.getComponent().getParent(), "Đang lấy tên đường...");
		ConvertLatLongToAddress convert;
		Address newAddress = new Address();
		newAddress.setLatitude(lat);
		newAddress.setLongitude(lng);
		if (newAddress != null) {
			onChangeStartAddress(newAddress);
		}
		findAddressEditorStart.setValue(newAddress);
		convert = new ConvertLatLongToAddress(lat, lng, getTaxiOrderForm().getDesktop(), this);
		Thread tr;
		tr = new Thread(convert, "ConvertLatLongToAddress");
		tr.setPriority(Thread.MAX_PRIORITY);
		tr.start();
	}

	/**
	 * Xu ly su kien lien quan den ban do
	 *
	 * @author VuD
	 * @param event
	 */
	public void handbleGmap(Event event) {

		switch (event.getName()) {
		case VMapEvents.ON_VMAP_DRAG_END:
			VMapEvent mapEvent = (VMapEvent) event;
			getTaxiOrderForm().getDesktop().enableServerPush(true);
			Clients.showBusy(_this.findAddressEditorStart.getComponent().getParent(), "Đang lấy tên đường...");
			onMoveRiderMarker(map.getGmaps().getCenter().lat, map.getGmaps().getCenter().lng);
			break;
		case VMapEvents.ON_VMAP_CLICK:
			mapEvent = (VMapEvent) event;
			Clients.showBusy(_this.findAddressEditorStart.getComponent().getParent(), "Đang lấy tên đường...");
			onMoveRiderMarker(mapEvent.getLat(), mapEvent.getLng());
			break;
		case VMapEvents.ON_VMARKER_DRAG_END:
			try {
				mapEvent = (VMapEvent) event;
				if (mapEvent.getComponent().equals(map.getRiderMarker())) {
					onMoveRiderMarker(mapEvent.getLat(), mapEvent.getLng());
				}
			} catch (Exception e2) {
				AppLogger.logDebug.error("event onMarkerDragEnd", e2);
			}
			break;

		}
	}

	private void onChangeEndAddress(Address address) {
		map.markerAddrTo(new LatLng(address.getLatitude(), address.getLongitude()));
	}

	class LoadTaxiRegistedWorker extends Thread {
		String lstVehicleRegister;

		public LoadTaxiRegistedWorker(String lstVehicleRegister) {
			super();
			this.lstVehicleRegister = lstVehicleRegister;
		}

		@Override
		public void run() {
			final List<TaxiResult> lstResult = new ArrayList<TaxiResult>();
			;
			Session session = ControllerUtils.getCurrentSession();
			SessionImplementor sessionImplementor = (SessionImplementor) session;
			Connection conn = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			try {
				conn = sessionImplementor.connection();
				if (conn == null) {
					return;
				}
				cs = conn.prepareCall("call txm_tracking.cmdGetCarRegister(?)");
				cs.setString(1, lstVehicleRegister);
				rs = cs.executeQuery();
				while (rs.next()) {
					TaxiResult tx = new TaxiResult();
					tx.setId(rs.getInt("id"));
					tx.setTimeLog(rs.getTimestamp("TimeLog"));
					tx.setLongitude(rs.getDouble("Longi"));
					tx.setLatitude(rs.getDouble("Lati"));
					tx.setLastGpsSpeed(rs.getInt("LastGPSSpeed"));
					tx.setLicensePlace(rs.getString("LicensePlate"));
					tx.setVehicleNumber(rs.getString("VehicleNumber"));

					lstResult.add(tx);

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
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
			Desktop desktop = _this.getListWindow().getDesktop();
			if (!desktop.isServerPushEnabled()) {
				desktop.enableServerPush(true);
			}
			Executions.schedule(desktop, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					String destination_LatLong = Double.toString(findAddressEditorStart.getSearchValue().getLatitude())
							+ "," + Double.toString(findAddressEditorStart.getSearchValue().getLongitude());
					divShow_VehicleRegister.setVisible(true);
					updateRegistedTaxiNotification(lstResult, destination_LatLong);
				}
			}, null);
		}
	}

	class SearchNearTaxiWorker extends Thread {

		private double longitude;
		private double latitude;

		public SearchNearTaxiWorker(double longitude, double latitude) {
			super();
			this.longitude = longitude;
			this.latitude = latitude;
		}

		@Override
		public void run() {
			List<TaxiResult> lstResult = new ArrayList<TaxiResult>();
			int ranger = ConfigUtil.getConfig("DD_RANGER", 500);
			int numvehicle = ConfigUtil.getConfig("DD_NUM_VEHICLE", 5);
			int orderCarType = 0;
			List<VehicleInfoJson> lstValue = TaxiUtils.getTaxiAvaiableFromRDS(longitude, latitude, ranger, numvehicle,
					orderCarType);
			for (VehicleInfoJson jsonVehicle : lstValue) {
				TaxiResult tx = new TaxiResult();
				tx.setLongitude(jsonVehicle.getLongitude());
				tx.setLatitude(jsonVehicle.getLatitude());
				tx.setDriverName(jsonVehicle.getDriverName());
				tx.setMobileNumber(jsonVehicle.getPhoneNumber());
				tx.setVehicleNumber(jsonVehicle.getVehicleNumber());
				tx.setDis(jsonVehicle.getDistance());
				tx.setCarType(jsonVehicle.getCarType());
				lstResult.add(tx);
			}
			Desktop desktop = _this.getListWindow().getDesktop();
			if (!desktop.isServerPushEnabled()) {
				desktop.enableServerPush(true);
			}
			Executions.schedule(desktop, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					if (lstResult.size() > 0) {
						map.setMarkerVehicle(lstResult);
						GoogleMapUntil.scaleMap(map.getGmaps());
					}
				}
			}, null);
		}

	}

	private void onChangeStartAddress(Address address) {
		if (address == null)
			return;
		if (address.getLatitude() != 0 && address.getLongitude() != 0) {
			map.setRiderMarker(address.getLatitude(), address.getLongitude());
			// Set channel auto
			if (((TaxiOrder) this.getModel()).getId() <= 0) {
				FindNearChannel mostNearChannel = new FindNearChannel();

				ChannelTms channel = mostNearChannel.getChannel(address.getLongitude(), address.getLatitude(),
						lstChannel);
				if (channel != null) {
					chanelSelector.setValue(channel);
				}
			}
			// Search near taxis
			SearchNearTaxiWorker worker = new SearchNearTaxiWorker(address.getLongitude(), address.getLatitude());
			worker.start();
		}
	}

	private void createEastLayout(Component parent) {
		divContainMap = new Div();
		divContainMap.setId("divEastLayout");
		divContainMap.setSclass("divCommon divEastLayout");
		divContainMap.setParent(parent);
		divContainMap.addEventListener(Events.ON_SIZE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {

			}
		});

		createChildInfor_VehicleRegister(divContainMap);
		map = new TaxiOrderGmap(this, divContainMap);
	}

	public boolean saveInBackground() {

		boolean retVal = false;
		boolean newTrip = false;
		List<CarTypeValue> newList_TyleVehicleForOrder;
		Customer cust;
		List<TaxiOrder> lstOrder;
		try {
			Clients.showBusy(getBtn_save(), "Saving...");
			// update value to TaxiOrder
			newList_TyleVehicleForOrder = carTypeSelectorContainer.getCarTypes();
			updateTaxiOrder(getTaxiOrder(), newList_TyleVehicleForOrder);

			if (checkValidateValue(getTaxiOrder())) {

				retVal = true;

				boolean isNewCust = getTaxiOrder().getCustomer() == null ? true : false;
				/*
				 * thoi gian dat xe
				 */
				if (getTaxiOrder().getSomeTime()) {
					Timestamp timestamp = new Timestamp(dbReceiveDate.getValue().getTime());
					getTaxiOrder().setBeginOrderTime(timestamp);
				} else {
					Timestamp sysDateTime = new Timestamp(System.currentTimeMillis());
					getTaxiOrder().setBeginOrderTime(sysDateTime);
				}

				/*
				 * so km, phu troi, so tien
				 */
				if (getTaxiOrder().getConTract()) {
					getTaxiOrder().setPriceOrderKm(tbOptionAirPricekmct.getValue());
					getTaxiOrder().setMoneytotal(tbMoney.getValue());
					getTaxiOrder().setTimect(tbOptionTimect.getValue());
					getTaxiOrder().setFareEstimate(new BigDecimal(tbOptionPricect.getValue()));

					if (tbOptionPricectadd.getValue().length() > 0) {
						getTaxiOrder().setFareAdd(new BigDecimal(tbOptionPricectadd.getValue()));
					}
					if (tbOptionPricekmctadd.getText().length() > 0) {
						getTaxiOrder().setPriceKmadd(new BigDecimal(tbOptionPricekmctadd.getValue()));
					}
					if (tbOptionPricetimectadd.getText().length() > 0) {
						getTaxiOrder().setTimeAdd(new BigDecimal(tbOptionPricetimectadd.getValue()));
					}
				}

				if (getTaxiOrder().getAirStation()) {
					getTaxiOrder().setPriceOrderKm(tbOptionAirPricekm.getValue());
					getTaxiOrder().setFareEstimate(new BigDecimal(tbOptionPrice.getValue()));

					if (tbOptionPriceadd.getText().length() > 0) {
						getTaxiOrder().setTimeAdd(new BigDecimal(tbOptionPriceadd.getValue()));
					}
				}

				/*
				 * customer
				 */
				cust = changeCustomer(getTaxiOrder(), isNewCust);
				getTaxiOrder().setPhoneNumber(tbPhone.getValue());
				/*
				 * update taxi order
				 */

				// tbPhone.isVisible()
				if (getTaxiOrder().getStatus() == EnumStatus.CREATING.getValue() || isNewCust) {
					/*
					 * phone
					 */
					Timestamp timeNow = new Timestamp(System.currentTimeMillis());
					getTaxiOrder().setPhoneNumber(cust.getPhoneNumber());
					getTaxiOrder().setUser(Env.getUser());
					getTaxiOrder().setOrderTime(timeNow);
					getTaxiOrder().setStatus(EnumStatus.MOI.getValue());
					/*
					 * chuyến mới
					 */
					newTrip = true;
				}

				getTaxiOrder().setCustomer(cust);

				lstOrder = new ArrayList<>();
				if (newTrip) {
					for (CarTypeValue carTypeValue : newList_TyleVehicleForOrder) {
						for (int i = 0; i < carTypeValue.getNumber(); ++i) {
							/*
							 * taxi
							 */
							TaxiOrder order = TaxiOrderUtil.createTaxiOrder(getTaxiOrder());
							order.setCustomer(cust);
							/*
							 * type car
							 */

							if (carTypeValue.getCarType().getValue() == EnumCarTypeCommon.BON_CHO.getValue()) {

								order.setOrderCarType(EnumCarTypeCommon.BON_CHO.getValue());
							} else if (carTypeValue.getCarType().getValue() == EnumCarTypeCommon.BAY_CHO.getValue()) {

								order.setOrderCarType(EnumCarTypeCommon.BAY_CHO.getValue());
							} else
								order.setOrderCarType(EnumCarTypeCommon.ALL.getValue());
							/*
							 * add
							 */
							lstOrder.add(order);
						}
					}
				} else {
					CarTypeValue carTypeValue = newList_TyleVehicleForOrder.get(0);
					if (carTypeValue.getCarType().getValue() == EnumCarTypeCommon.BON_CHO.getValue()) {

						getTaxiOrder().setOrderCarType(EnumCarTypeCommon.BON_CHO.getValue());
					} else if (carTypeValue.getCarType().getValue() == EnumCarTypeCommon.BAY_CHO.getValue()) {

						getTaxiOrder().setOrderCarType(EnumCarTypeCommon.BAY_CHO.getValue());
					} else
						getTaxiOrder().setOrderCarType(EnumCarTypeCommon.ALL.getValue());

					lstOrder.add(getTaxiOrder());
				}
				
				List<TaxiOrder> temp = lstOrder;
				
				AppLogger.logTaxiorder
						.info("TaxiOrderSave|1. Order Save: Thread Start, phone:+ " + getTaxiOrder().getPhoneNumber());
				
				TaxiOrderSaveWorker tSave = new TaxiOrderSaveWorker(lstOrder, cust, newTrip);
				Thread th = new Thread(tSave);
				th.setPriority(Thread.MAX_PRIORITY);
				th.start();
				
				if (newTrip) {
					SaveLogToQueue savelog = new SaveLogToQueue(temp, EnumUserAction.INSERT, Env.getHomePage().getCurrentFunction(), Env.getUserID());
					savelog.start();
				}else {
					SaveLogToQueue savelog = new SaveLogToQueue(temp, EnumUserAction.UPDATE, Env.getHomePage().getCurrentFunction(), Env.getUserID());
					savelog.start();
				}
			}
		} catch (Exception ex) {
			AppLogger.logDebug.info(ex.getMessage());
		} finally {
			Clients.clearBusy(getBtn_save());
			newList_TyleVehicleForOrder = null;
			cust = null;
			lstOrder = null;
		}
		return retVal;
	}

	public void refeshData(TaxiOrder order) {
		getTaxiOrderForm().refresh(order);
	}

	@SuppressWarnings("unused")
	private String checkValidateTypeVehicle() {

		String message = "";
		List<Component> list = carTypeSelectorContainer.getContainTyleVehicle().getChildren();
		for (Component child : list) {
			if (child instanceof Div) {

				// spinner
				Spinner spinner = (Spinner) child.getChildren().get(0);
				if (spinner.getValue() == null) {
					spinner.setErrorMessage("KHÔNG ĐƯỢC ĐỂ TRỐNG");
					spinner.setFocus(true);
					// spinner.select();
					message = "KHÔNG ĐƯỢC ĐỂ TRỐNG";

					break;
				}

				// combobox
				Combobox combo = (Combobox) child.getChildren().get(1);
				if (combo.getSelectedItem() == null) {
					message = "CHỌN LẠI LOẠI XE...";
					combo.setErrorMessage(message);

					break;
				}
			}
		}
		return message;
	}

	@SuppressWarnings("deprecation")
	private boolean checkValidateValue(TaxiOrder taxiOrder) {

		if (tbPhone.getValue().trim().isEmpty()) {

			tbPhone.setErrorMessage("NHẬP SỐ ĐIỆN THOẠI...");
			tbPhone.focus();
			tbPhone.select();
			return false;
		} else if (tbPhone.getValue().trim().length() < 10 || tbPhone.getValue().trim().length() > 15) {

			tbPhone.setErrorMessage("ĐỘ DÀI SỐ ĐIỆN THOẠI TỪ 10 - 15 SỐ...");
			tbPhone.focus();
			tbPhone.select();
			return false;
		} else if (!NumberUtils.isDigits(tbPhone.getValue().trim())) {

			tbPhone.setErrorMessage("NHẬP LẠI SỐ ĐIỆN THOẠI...");
			tbPhone.focus();
			tbPhone.select();
			return false;
		}
		Address addStart = findAddressEditorStart.getSearchValue();
		// nearAddress.getValue() == null &&
		if (addStart.getLatitude() == 0 && addStart.getLongitude() == 0 && addStart.getName().equals("")) {

			findAddressEditorStart.getComponent().setErrorMessage("NHẬP ĐỊA CHỈ ĐÓN...");
			findAddressEditorStart.getComponent().focus();
			findAddressEditorStart.getComponent().select();
			return false;
		}
		/*
		 * di thoi gian check
		 */
		if (taxiOrder.getSomeTime()) {
			if (dbReceiveDate.getValue() == null
					|| StringUtils.equals(dbReceiveDate.getValue().toString().trim(), "")) {
				dbReceiveDate.setErrorMessage("NGÀY THÁNG KHÔNG ĐƯỢC ĐỂ TRỐNG...");
				dbReceiveDate.focus();
				dbReceiveDate.select();
				return false;
			} else {
				Date sysDate = null;
				Date dbDate;
				sysDate = new Date(System.currentTimeMillis());
				dbDate = new Date(dbReceiveDate.getValue().getYear(), dbReceiveDate.getValue().getMonth(),
						dbReceiveDate.getValue().getDate(), dbReceiveDate.getValue().getHours(),
						dbReceiveDate.getValue().getMinutes());
				if (dbDate.before(sysDate)) {
					dbReceiveDate.setErrorMessage("SAI NGÀY THÁNG HOẶC THỜI GIAN...");
					dbReceiveDate.focus();
					dbReceiveDate.select();
					return false;
				}
				if (dbDate.before(sysDate)) {
					dbReceiveDate.setErrorMessage("THỜI GIAN CHỌN LỚN HƠN HOẶC BẰNG THỜI GIAN HIÊN TẠI...");
					dbReceiveDate.focus();
					dbReceiveDate.select();
					return false;
				}
			}
		}

		if (taxiOrder.getConTract()) {

			if (CommonUtils.containsNumber(tbOptionAirPricekmct.getValue().trim()) == false) {
				tbOptionAirPricekmct.setErrorMessage("NHẬP KM DẠNG SỐ...");
			}
			if (CommonUtils.containsNumber(tbOptionAirPricekmct.getValue().trim()) == false) {
				tbOptionAirPricekmct.setErrorMessage("NHẬP KM DỰ TÍNH DẠNG SỐ...");
				tbOptionAirPricekmct.focus();
				tbOptionAirPricekmct.select();
				return false;
			}
			if (tbOptionAirPricekmct.getValue() == null
					|| StringUtils.equals(tbOptionAirPricekmct.getValue().toString().trim(), "")) {
				tbOptionAirPricekmct.setErrorMessage("NHẬP SỐ KM");
			}
			if (tbOptionAirPricekmct.getValue() == null
					|| StringUtils.equals(tbOptionAirPricekmct.getValue().toString().trim(), "")) {
				tbOptionAirPricekmct.setErrorMessage("NHẬP SỐ KM DỰ TÍNH");
				tbOptionAirPricekmct.focus();
				tbOptionAirPricekmct.select();
				return false;
			}

			if (CommonUtils.containsNumber(tbOptionPricekmctadd.getValue().trim()) == false) {
				tbOptionPricekmctadd.setErrorMessage("NHẬP KM PHỤ TRỘI DẠNG SỐ...");
				tbOptionPricekmctadd.focus();
				tbOptionPricekmctadd.select();
				return false;
			}

			if (CommonUtils.containsNumber(tbOptionTimect.getValue().trim()) == false) {
				tbOptionTimect.setErrorMessage("NHẬP T.GIAN DỰ TÍNH DẠNG SỐ...");
				tbOptionTimect.focus();
				tbOptionTimect.select();
				return false;
			}

			if (CommonUtils.containsNumber(tbOptionPricetimectadd.getValue().trim()) == false) {
				tbOptionPricetimectadd.setErrorMessage("NHẬP T.GIAN PHỤ TRỘI DẠNG SỐ...");
				tbOptionPricetimectadd.focus();
				tbOptionPricetimectadd.select();
				return false;
			}

			if (CommonUtils.containsNumber(tbOptionPricect.getValue()) == false) {
				tbOptionPricect.setErrorMessage("NHẬP SỐ TIÊN DẠNG SỐ...");
				tbOptionPricect.focus();
				tbOptionPricect.select();
				return false;
			}

			if (tbOptionPricect.getValue() == null
					|| StringUtils.equals(tbOptionPricect.getValue().toString().trim(), "")) {
				tbOptionPricect.setErrorMessage("NHẬP SỐ TIỀN");
				tbOptionPricect.focus();
				tbOptionPricect.select();
				return false;
			}

			if (CommonUtils.containsNumber(tbOptionPricectadd.getValue()) == false
					&& tbOptionAirPricekmct.getValue() != null) {
				tbOptionPricectadd.setErrorMessage("NHẬP SỐ TIÊN PHỤ TRỘI DẠNG SỐ...");
				tbOptionPricectadd.focus();
				tbOptionPricectadd.select();
				return false;
			}
			if (tbMoney.getValue() == null || StringUtils.equals(tbMoney.getValue().toString().trim(), "")) {
				tbMoney.setErrorMessage("NHẬP SỐ TIỀN");
				tbMoney.focus();
				tbMoney.select();
				return false;
			}

			if (CommonUtils.containsNumber(tbMoney.getValue()) == false && tbMoney.getValue() != null) {
				tbMoney.setErrorMessage("NHẬP SỐ TIÊN PHỤ TRỘI DẠNG SỐ...");
				tbMoney.focus();
				tbMoney.select();
				return false;
			}

		}

		if (taxiOrder.getAirStation()) {

			if (CommonUtils.containsNumber(tbOptionAirPricekm.getValue().trim()) == false) {
				tbOptionAirPricekm.setErrorMessage("NHẬP KM DẠNG SỐ...");
				tbOptionAirPricekm.focus();
				tbOptionAirPricekm.select();
				return false;
			}
			if (tbOptionAirPricekm.getValue() == null
					|| StringUtils.equals(tbOptionAirPricekm.getValue().toString().trim(), "")) {
				tbOptionAirPricekm.setErrorMessage("NHẬP SỐ KM");
				tbOptionAirPricekm.focus();
				tbOptionAirPricekm.select();
				return false;
			}

			if (CommonUtils.containsNumber(tbOptionPrice.getValue().trim()) == false) {
				tbOptionPrice.setErrorMessage("NHẬP SỐ TIỀN DẠNG SỐ...");
				tbOptionPrice.focus();
				tbOptionPrice.select();
				return false;
			}
			if (tbOptionPrice.getValue() == null
					|| StringUtils.equals(tbOptionPrice.getValue().toString().trim(), "")) {
				tbOptionPrice.setErrorMessage("NHẬP SỐ TIỀN");
				tbOptionPrice.focus();
				tbOptionPrice.select();
				return false;
			}

			if (CommonUtils.containsNumber(tbOptionPriceadd.getValue()) == false) {
				tbOptionPriceadd.setErrorMessage("NHẬP SỐ TIÊN PHỤ TRỘI DẠNG SỐ...");
				tbOptionPriceadd.focus();
				tbOptionPriceadd.select();
				return false;
			}
		}
		/*
		 * chanel
		 */
		if (chanelSelector.getValue() == null) {
			tbPhone.setErrorMessage("CHỌN KÊNH...");
			return false;
		}
		if (tbNoteForCustmer.getValue().trim().length() > 50) {

			tbNoteForCustmer.setErrorMessage("ĐỘ DÀI NHỎ HƠN 50 KÝ TỰ ?...");
			return false;
		}
		if (tbNoteForTaxiOrder.getValue().trim().length() > 200) {

			tbNoteForTaxiOrder.setErrorMessage("ĐỘ DÀI NHỎ HƠN 200 KÝ TỰ ?...");
			return false;
		}
		return true;
	}

	private void updateTaxiOrder(TaxiOrder taxiOrder, List<CarTypeValue> newList_TyleVehicleForOrder) {

		/*
		 * order type
		 */

		taxiOrder.setOrderType(EnumOrderType.GOI_TONG_DAI.getValue());

		taxiOrder.setTimeGoiNhac(new Timestamp(System.currentTimeMillis()));

		Address address;
		if (findAddressEditorStart.getSearchValue() != null) {
			address = (Address) findAddressEditorStart.getSearchValue();
			taxiOrder.setBeginOrderAddress(address.getName());
			taxiOrder.setBeginOrderLat(address.getLatitude());
			taxiOrder.setBeginOrderLon(address.getLongitude());
			taxiOrder.setBeginAddress(address.getName());
			taxiOrder.setBeginLat(address.getLatitude());
			taxiOrder.setBeginLon(address.getLongitude());
		}

		/*
		 * end address order
		 */

		if (findAddressEditorEnd.getSearchValue() != null) {
			address = findAddressEditorEnd.getSearchValue();
			taxiOrder.setEndOrderAddress(address.getName());
			taxiOrder.setEndOrderLat(address.getLatitude());
			taxiOrder.setEndOrderLon(address.getLongitude());
			taxiOrder.setEndAddress(address.getName());
			taxiOrder.setEndLat(address.getLatitude());
			taxiOrder.setEndLon(address.getLongitude());
			// }
		}
		/*
		 * way
		 */
		taxiOrder.setTwoWay(((TaxiOrder) getModel()).getTwoWay());
		/*
		 * start now
		 */
		taxiOrder.setStartNow(rdOptionStartNow.isChecked());
		/*
		 * di thoi gian
		 */
		taxiOrder.setSomeTime(rdOptionSomeTime.isChecked());
		/*
		 * di chung
		 */
		taxiOrder.setSomePerson(rdOptionSomePerson.isChecked());
		/*
		 * di thuong
		 */
		taxiOrder.setGoNormal(rdOptionNormal.isChecked());
		/*
		 * di hop dong
		 */
		taxiOrder.setConTract(rdOptionConTract.isChecked());
		/*
		 * air
		 */
		taxiOrder.setAirStation(rdOptionAirStation.isChecked());
		/*
		 * loaddata
		 */
		taxiOrder.setLoadData(cbOptionloadkm.getValue());

		/*
		 * note
		 */
		taxiOrder.setNote(tbNoteForTaxiOrder.getValue().trim());
		/*
		 * chanel
		 */
		taxiOrder.setChannel((ChannelTms) chanelSelector.getValue());
		/*
		 * created
		 */
		taxiOrder.setCreateBy(Env.getUser().getId());
		taxiOrder.setUpdateBy(Env.getUser().getId());
		/*
		 * user
		 */
		taxiOrder.setUser(Env.getUser());
		/*
		 * Update to database
		 */

		setModel(taxiOrder);
	}

	private Customer changeCustomer(TaxiOrder taxiOrder, boolean isNew) {

		Customer cust = taxiOrder.getCustomer();

		if (cust == null || cust.getId() == 0) {
			cust = CustomerController.getOrCreateCustomer(tbPhone.getValue().trim());
		}

		// Update customer in case create new order
		if (taxiOrder.getId() <= 0) {
			// Increase total call order
			int total = cust.getTotalCallOrder();
			cust.setTotalCallOrder(total + 1);
			// Move Address 2 to address 3
			if (StringUtils.isNotEmpty(cust.getAddress2())) {
				cust.setAddress3(cust.getAddress2());
				cust.setAddress3Lat(cust.getAddress2Lat());
				cust.setAddress3Lng(cust.getAddress2Lng());
			}
			// Move Address 1 to address 2
			if (StringUtils.isNotEmpty(cust.getAddress())) {
				cust.setAddress2(cust.getAddress());
				cust.setAddress2Lat(cust.getAddressLat());
				cust.setAddress2Lng(cust.getAddressLng());
			}
			// set new Address Order to address 1
			cust.setAddress(taxiOrder.getBeginOrderAddress());
			cust.setAddressLat(taxiOrder.getBeginOrderLat());
			cust.setAddressLng(taxiOrder.getBeginOrderLon());
		} else { // In case edit order
			cust.setAddress(taxiOrder.getBeginOrderAddress());
			cust.setAddressLat(taxiOrder.getBeginOrderLat());
			cust.setAddressLng(taxiOrder.getBeginOrderLon());

		}
		// if (isNew)
		cust.setLastTimeCall(new Timestamp(System.currentTimeMillis()));
		cust.setNote(tbNoteForCustmer.getValue());

		return cust;
	}

	private EventListener<Event> EVENT_ON_CLICK_MONEY = new EventListener<Event>() {
		@Override
		public void onEvent(Event arg0) throws Exception {
			if (tbOptionPricect.getValue() == null
					|| StringUtils.equals(tbOptionPricect.getValue().toString().trim(), "")) {
				tbOptionPricect.setErrorMessage("Chưa Nhập Số Tiền Dự Tính");
			}
			int money = Integer.parseInt(tbOptionPricect.getValue());
			if (tbOptionPricectadd.getValue() == null
					|| StringUtils.equals(tbOptionPricectadd.getValue().toString().trim(), "")) {
				tbMoney.setValue(money + "");
			} else {
				int moneyadd = Integer.parseInt(tbOptionPricectadd.getValue());
				tbMoney.setValue((money + moneyadd) + "");
			}
		}
	};

	private EventListener<Event> EVENT_ON_CLICK_PRICE = new EventListener<Event>() {
		// TODO
		@Override
		public void onEvent(Event arg0) throws Exception {
			if (cbOptionloadkm.getSelectedItem() == null) {
				cbOptionloadkm.setErrorMessage("Chưa Chọn Bảng Giá!");
			} else {
				TypeTablePrice price = cbOptionloadkm.getSelectedItem().getValue();
				List<TablePrice> lst = getListTablePriceByType(price);
				TablePrice bean = null;
				if (StringUtils.isInteger(tbOptionAirPricekmct.getValue())) {
					int priceValue = Integer.parseInt(tbOptionAirPricekmct.getValue());
					if (lst != null && lst.size() > 0) {
						for (TablePrice tablePrice : lst) {
							if (tablePrice.getKm().intValue() - priceValue >= 0) {
								bean = tablePrice;
								break;
							}
						}
					}
					if (bean == null) {
						// TO DO: Thong bao ko co bang gia nao phu hop
						cbOptionloadkm.setErrorMessage("Không Có Bảng Giá Nào Phù Hợp");
					} else {
						if (rdOptionAironewayct.isChecked()) {
							tbOptionPricect.setValue(bean.getPrice1c().intValue() + "");
							tbOptionTimect.setValue(bean.getTime1c().intValue() + "");
						}
						if (rdOptionAirtwowayct.isChecked()) {
							tbOptionPricect.setValue(bean.getPrice2c().intValue() + "");
							tbOptionTimect.setValue(bean.getTime2c().intValue() + "");
						}
					}
				} else {
					// TO DO: thong bao gia tri nhap ko hop le
					tbOptionAirPricekmct.setErrorMessage("Nhập Số Km Không Hợp Lệ");
				}

			}

		}
	};
	private EventListener<Event> EVENT_ON_CLICK_NEW_TAXIORDER = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			Address addressCurrent = findAddressEditorStart.getSearchValue();
			TaxiOrder newOrder = TaxiOrderUtil.createTaxiOrder(((TaxiOrder) getModel()).getPhoneNumber(),
					addressCurrent);
			_this.setModel(newOrder);
			setValue_UI(false);
		}

	};
	private EventListener<Event> EVENT_ON_CLICK_CANCEL_TAXIORDER = new EventListener<Event>() {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void onEvent(Event arg0) throws Exception {

			Messagebox.show("...BẠN ĐANG HỦY CUỐC, SĐT:" + tbPhone.getValue(), "HỦY CUỐC",
					Messagebox.OK | Messagebox.CANCEL, Messagebox.ERROR, new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event e) {
							if (Messagebox.ON_OK.equals(e.getName())) {

								/*
								 * close event
								 */
								callIn_End();

								/*
								 * update data
								 */

								TaxiOrder taxiOrder = (TaxiOrder) getModel();
								taxiOrder.setCancelReason(EnumCancelReason.KHACH_HANG_HUY.getValue());
								taxiOrder.setCancelTime(new Timestamp(System.currentTimeMillis()));
								Trip trip = TripManager.sharedInstance.getTrip(taxiOrder.getId() + "");
								trip.cancelTrip(EnumCancelReason.KHACH_HANG_HUY.getValue());
								VehicleStatusDD.updateStatusPickup(taxiOrder);
								SendCancelSmsProcessor sendCancelSmsProcessor = new SendCancelSmsProcessor(
										taxiOrder.getCustomer(), taxiOrder.getRegistedTaxis());
								sendCancelSmsProcessor.start();

								_this.setVisible(false);
								getListWindow().refresh();
								Env.getHomePage().showNotification("THÔNG TIN ĐÃ CẬP NHẬT!",
										Clients.NOTIFICATION_TYPE_INFO);

								/*
								 * clean
								 */
								cleanValueForm();

							} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
								// Cancel is clicked
							}
						}
					});
		}

	};

	/**
	 * 
	 * @author tuanpa
	 *
	 */
	class LoadCustomerWorker extends Thread {
		String phoneNumber;

		@Override
		public void run() {
			// Maybe this take long time
			Customer customer = CustomerController.getOrCreateCustomer(StandandPhoneNumber.standandPhone(phoneNumber));
			didLoadCustomer(customer);
		}
	}

	private void refreshRecentAddress() {
		// Remove
		if (nearAddress != null && nearAddress.getParent() == divGroupChildNearAddress) {
			nearAddress.setParent(null);
			nearAddress = null;
		}

		TaxiOrder taxiOrder = ((TaxiOrder) getModel());
		Customer cust = taxiOrder.getCustomer();
		Address address;
		List<Address> lstAddress = new ArrayList<>();
		if (cust.getAddress() != null)
			if (cust.getAddress().trim() != "") {

				address = new Address();
				address.setName(cust.getAddress().trim());
				address.setLatitude(cust.getAddressLat() == null ? 0 : cust.getAddressLat());
				address.setLongitude(cust.getAddressLng() == null ? 0 : cust.getAddressLng());
				lstAddress.add(address);
			}
		if (cust.getAddress2() != null)
			if (cust.getAddress2().trim() != "") {

				address = new Address();
				address.setName(cust.getAddress2().trim());
				address.setLatitude(cust.getAddress2Lat() == null ? 0 : cust.getAddress2Lat());
				address.setLongitude(cust.getAddress2Lng() == null ? 0 : cust.getAddress2Lng());
				lstAddress.add(address);
			}
		if (cust.getAddress3() != null)
			if (cust.getAddress3().trim() != "") {

				address = new Address();
				address.setName(cust.getAddress3().trim());
				address.setLatitude(cust.getAddress3Lat() == null ? 0 : cust.getAddress3Lat());
				address.setLongitude(cust.getAddress3Lng() == null ? 0 : cust.getAddress3Lng());
				lstAddress.add(address);
			}
		nearAddress = new NearAddressControl(lstAddress, this);
		nearAddress.setParent(divGroupChildNearAddress);

		// auto set value for start address if it blank
		if ((findAddressEditorStart.getSearchValue().getName() == null
				|| findAddressEditorStart.getSearchValue().getName().trim().length() == 0) && lstAddress.size() > 0) {
			Address addressCurrent = lstAddress.get(0);
			Comboitem item = new Comboitem(addressCurrent.getName());
			item.setValue(addressCurrent);
			findAddressEditorStart.getComponent().getItems().add(item);
			findAddressEditorStart.getComponent().setSelectedItem(item);
			findAddressEditorStart.setValue(addressCurrent);
			findAddressEditorStart.onChangeValue();
		}

		int repeatTime = getTaxiOrder().getRepeatTime() - 1;

		String strLastCall = "[0:0]";
		if (cust.getLastTimeCall() != null)
			strLastCall = new SimpleDateFormat("H:mm [dd-MM-yyyy]").format(cust.getLastTimeCall());// getTaxiOrder().getTimeGoiNhac());
		addLabelHeader_aboutInforCustomer(divHeaderInforCall, repeatTime, cust.getTotalSuccessOrder(), strLastCall);

	}

	private void didLoadCustomer(Customer customer) {
		TaxiOrder taxiOrder = ((TaxiOrder) getModel());
		taxiOrder.setCustomer(customer);

		if (!this.getDesktop().isServerPushEnabled()) {
			this.getDesktop().enableServerPush(true);
		}
		Executions.schedule(this.getDesktop(), new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				Clients.clearBusy(tbPhone);
				refreshRecentAddress();
			}
		}, null);
	}

	private EventListener<Event> EVENT_ON_CHANGING_TEXTBOXPHONE = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {

			if (event.getName().equals(Events.ON_OK) || event.getName().equals(Events.ON_BLUR)) {

				try {

					String phoneNumber = tbPhone.getValue().trim();

					boolean isNumber = CommonUtils.containsNumber(phoneNumber);
					if (isNumber == false) {

						tbPhone.setErrorMessage("NHẬP ĐIỆN THOẠI DẠNG SỐ...");
						tbPhone.focus();
						tbPhone.select();
					} else {

						if (!StandandPhoneNumber.standandPhone(getTaxiOrder().getCustomer().getPhoneNumber())
								.equals(StandandPhoneNumber.standandPhone(phoneNumber))) {
							Clients.showBusy(tbPhone, "Đang tìm kiếm");
							LoadCustomerWorker worker = new LoadCustomerWorker();
							worker.phoneNumber = phoneNumber;
							worker.start();
						}
					}

				} catch (Exception ex) {

				}
			}

		}
	};

	private EventListener<Event> EVENT_CALLOUT = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			try {

				String extension = cbExtension.getSelectedItem().getValue();
				if (NumberUtils.isNumber(extension)) {
					List<VoipCenter> lstExtension = getTaxiOrderForm().getLstVoipCenters();
					if (lstExtension.size() == 0) {
						Clients.showNotification("XEM LAI VOIPCENTER ...", Clients.NOTIFICATION_TYPE_INFO, btCallOut,
								"RightBottom", 2500);
						return;
					}

					if (ExtensionOnlineUtil.getExtension_Notuse(lstExtension.get(0), extension, true,
							Env.getUser().getId())) {

						// if
						// (CheckOnlineUtils.getUserCallIn_ByExtention(extension,
						// false) != null) {
						//
						// String reval =
						// CallCenterEventWS.makeACall(tbPhone.getValue().trim(),
						// extension);
						// if (!reval.equalsIgnoreCase("200")) {
						// Clients.showNotification("CUỘC GỌI HỎNG, BẠN THỰC
						// HIỆN LẠI !",
						// Clients.NOTIFICATION_TYPE_ERROR, btCallOut,
						// "RightBottom", 2500);
						// }
						// }
					} else {
						Comboitem itemSelected = null;
						for (Comboitem item : cbExtension.getItems()) {

							if (!item.getValue().toString().equals(extension)) {

								cbExtension.setSelectedItem(item);
								itemSelected = item;
								break;
							}
						}
						StringBuilder message = new StringBuilder("ĐIỆN THOẠI ĐANG SỬ DỤNG");
						if (itemSelected != null)
							message.append(", CÓ THỂ DÙNG MÁY:" + itemSelected.getValue().toString());
						Clients.showNotification(message.toString(), Clients.NOTIFICATION_TYPE_ERROR, btCallOut,
								"RightBottom", 2500);
					}
				} else {

					Clients.showNotification("CHỌN 1 TRONG CÁC ĐIỆN THOẠI ĐỂ GỌI RA", Clients.NOTIFICATION_TYPE_ERROR,
							btCallOut, "RightBottom", 2500);
				}
				// cbExtension.setErrorMessage("CHỌN 1 TRONG CÁC ĐIỆN THOẠI ĐỂ
				// GỌI
				// RA");
			} catch (Exception ex) {

				Clients.showNotification("CUỘC GỌI HỎNG, BẠN THỰC HIỆN LẠI !", Clients.NOTIFICATION_TYPE_ERROR,
						btCallOut, "RightBottom", 2500);
			}
			cbExtension.select();
			cbExtension.focus();
			cbExtension.setOpen(true);
			cbExtension.setAutodrop(true);
		}
	};

	private EventListener<Event> EVENT_CTRL_KEY_FORM = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			AppLogger.logDebug.info("EVENT_CTRL_KEY" + event);
			try {
				KeyEvent keyEvent = (KeyEvent) event;
				int keyCode = keyEvent.getKeyCode();
				switch (keyCode) {
				case 71: // 71: g Ä‘i giÃ¡
					rdOptionConTract.setChecked(!rdOptionConTract.isChecked());
					setOptionPrice(rdOptionConTract);
					break;
				case 112: // f1
					tbNoteForCustmer.select();
					tbNoteForCustmer.setFocus(true);
					break;
				case 113: // f2
					List<Component> listComp = carTypeSelectorContainer.getContainTyleVehicle().getChildren();
					if (listComp.size() > 0) {
						List<Component> div = listComp.get(0).getChildren();
						Spinner spinner = (Spinner) div.get(0);
						spinner.select();
						spinner.setFocus(true);
					}
					break;
				case 114: // f3
					findAddressEditorStart.getComponent().select();
					findAddressEditorStart.getComponent().setFocus(true);
					findAddressEditorStart.getComponent().open();
					break;
				case 115: // f4
					findAddressEditorEnd.getComponent().select();
					findAddressEditorEnd.getComponent().setFocus(true);
					findAddressEditorEnd.getComponent().open();
					break;
				case 116: // f5
					/*
					 * cbbChannel.select(); cbbChannel.setFocus(true); break;
					 */
				case 117: // f6
					tbNoteForTaxiOrder.setFocus(true);
					tbNoteForTaxiOrder.select();
					break;
				case 118: // f7
					carTypeSelectorContainer.addCarTypeSelector();
					break;
				case 119: // f8
					break;
				case 120: // f9
					break;
				case 121: // f10
					if (findAddressEditorEnd.getComponent().getSelectedIndex() >= 0) {
						Comboitem itemCB = findAddressEditorEnd.getComponent().getSelectedItem();
						Address address = itemCB.getValue();
						map.markerAddrTo(new LatLng(address.getLatitude(), address.getLongitude()));
						getDistance_DirectionBetweenTwoAddress();
					} else
						onChangeStartAddress(findAddressEditorStart.getSearchValue());
					break;
				case 83: // Ctrl + s
					/*
					 * save
					 */
					if (saveInBackground())
						/*
						 * fire event
						 */
						callIn_End();
					break;
				case 67:
					Events.sendEvent(Events.ON_CLICK, btNewOrder, null);
					break;

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onChangeAddress(Address address, int type) {

		if (type == 0) {
			onChangeEndAddress(new Address());
			onChangeStartAddress(address);
			// tbNoteForTaxiOrder.setValue(address.getNote());
		} else if (type == 1) {
			onChangeEndAddress(address);
			getDistance_DirectionBetweenTwoAddress();
		}
	}

	@Override
	public void onChangeAddress(Address address) {
		findAddressEditorStart.getComponent().getItems().clear();
		Comboitem item = new Comboitem(address.getName());
		item.setValue(address);
		findAddressEditorStart.getComponent().getItems().add(item);
		findAddressEditorStart.getComponent().setSelectedItem(item);
		findAddressEditorStart.setValue(address);

		onChangeAddress(address, 0);
	}

}
