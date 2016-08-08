package com.vietek.taxioperation.ui.controller;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.event.MapDropEvent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.googlemapSearch.SearchGooglePlaces;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.util.MapUtils;

public class AddressCustomerGmaps extends Window {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Vlayout vlayout;
	private Div divMaps;
	private Gmaps gmaps;
	private Gmarker gmarkerAdd01;
	private Gmarker gmarkerAdd02;
	private Gmarker gmarkerAdd03;
	private Combobox cboAdd01;
	private Combobox cboAdd02;
	private Combobox cboAdd03;
	private Button btnSave;
	private final Desktop desktop;
	private Customer customer;
	private Combobox address1;
	private Combobox address2;
	private Combobox address3;

	public AddressCustomerGmaps(Customer customer, Combobox address1, Combobox address2, Combobox address3) {
		desktop = Executions.getCurrent().getDesktop();
		if (!desktop.isServerPushEnabled())
			desktop.enableServerPush(true);
		// this.setId("add_customer_id");
		this.setSclass("address_customer_gmaps");
		this.setWidth("80%");
		this.setHeight("90%");
		this.setStyle("background-color: #019733; min-width:300px");
		this.setClosable(true);
		this.setTitle("Địa chỉ khách hàng");
		this.customer = customer;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.init();
		// checkLocation();
		loadAddress();
		Clients.evalJavaScript("fixUserLocation()");
	}

	private void init() {
		divMaps = new Div();
		divMaps.setId("divMaps");
		divMaps.setSclass("div_Maps");
		divMaps.setWidth("100%");
		divMaps.setHeight("100%");
		divMaps.setStyle("position : relative");
		divMaps.setParent(this);
		initMaps();
		Div divAdd = new Div();
		divAdd.setParent(this);
		divAdd.setStyle("z-index: 1; position: absolute; top: 45px;");
		createInputForm(divAdd);
	}

	private void createInputForm(Div divAdd) {
		final Panel panels = new Panel();
		panels.setSclass("pane_add_info");
		panels.setAction("show: slideDown;hide: slideUp");
		panels.setBorder(true);
		panels.setCollapsible(true);
		panels.setParent(divAdd);
		panels.setWidth("350px");
		Panelchildren panel = new Panelchildren();
		panel.setParent(panels);

		vlayout = new Vlayout();
		vlayout.setParent(panel);

		Hlayout hlAddress = new Hlayout();
		hlAddress.setId("hl_address_info");
		hlAddress.setSclass("hl_address_info");
		hlAddress.setParent(vlayout);
		Image imgAdd = new Image(CommonDefine.GoogleMap.URL_ICON_ADDRESS1);
		imgAdd.setSclass("img_add_info");
		imgAdd.setParent(hlAddress);
		cboAdd01 = new Combobox();
		cboAdd01.setId("cb_home_info");
		cboAdd01.setParent(hlAddress);
		cboAdd01.setButtonVisible(false);
		cboAdd01.setSclass("cb_add_info");
		if (customer.getAddress() != null) {
			cboAdd01.setText(customer.getAddress());
		}

		cboAdd01.addEventListener("onChanging", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				InputEvent event = (InputEvent) arg0;
				String dataSearch = event.getValue();
				SearchGooglePlaces a1 = new SearchGooglePlaces(cboAdd01, dataSearch, desktop);
				Thread t1 = new Thread(a1);
				t1.setPriority(Thread.MAX_PRIORITY);
				t1.start();
			}
		});
		cboAdd01.addEventListener("onChange", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				handInputAddress(event);
			}
		});
		hlAddress = new Hlayout();
		hlAddress.setSclass("hl_address_info");
		hlAddress.setParent(vlayout);
		imgAdd = new Image(CommonDefine.GoogleMap.URL_ICON_ADDRESS2);
		imgAdd.setSclass("img_add_info");
		imgAdd.setParent(hlAddress);
		cboAdd02 = new Combobox();
		cboAdd02.setId("cb_comp_info");
		cboAdd02.setParent(hlAddress);
		cboAdd02.setButtonVisible(false);
		cboAdd02.setSclass("cb_add_info");
		if (customer.getAddress2() != null)
			cboAdd02.setText(customer.getAddress2());
		cboAdd02.addEventListener("onChanging", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				InputEvent event = (InputEvent) arg0;
				String dataSearch = event.getValue();
				SearchGooglePlaces a1 = new SearchGooglePlaces(cboAdd02, dataSearch, desktop);
				Thread t1 = new Thread(a1);
				t1.setPriority(Thread.MAX_PRIORITY);
				t1.start();
			}
		});
		cboAdd02.addEventListener("onChange", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				handInputAddress(event);
			}
		});
		hlAddress = new Hlayout();
		hlAddress.setSclass("hl_address_info");
		hlAddress.setParent(vlayout);
		imgAdd = new Image(CommonDefine.GoogleMap.URL_ICON_ADDRESS3);
		imgAdd.setSclass("img_add_info");
		imgAdd.setParent(hlAddress);
		cboAdd03 = new Combobox();
		cboAdd03.setId("cb_usually_info");
		cboAdd03.setSclass("cb_add_info");
		cboAdd03.setParent(hlAddress);
		cboAdd03.setButtonVisible(false);
		if (customer.getAddress3() != null)
			cboAdd03.setText(customer.getAddress3());
		cboAdd03.addEventListener("onChanging", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				InputEvent event = (InputEvent) arg0;
				String dataSearch = event.getValue();
				SearchGooglePlaces a1 = new SearchGooglePlaces(cboAdd03, dataSearch, desktop);
				Thread t1 = new Thread(a1);
				t1.setPriority(Thread.MAX_PRIORITY);
				t1.start();
			}
		});
		cboAdd03.addEventListener("onChange", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				handInputAddress(event);
			}
		});
		btnSave = new Button();
		btnSave.setId("btn_add_info");
		btnSave.setSclass("btn_add_info");
		btnSave.setParent(vlayout);
		btnSave.setLabel("Chọn xong");
		btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				address1.setValue(cboAdd01.getValue());
				address1.setAttribute(CustomersDetail.ATT_LOCATION,
						new LatLng(gmarkerAdd01.getLat(), gmarkerAdd01.getLng()));
				address2.setValue(cboAdd02.getValue());
				address2.setAttribute(CustomersDetail.ATT_LOCATION,
						new LatLng(gmarkerAdd02.getLat(), gmarkerAdd02.getLng()));
				address3.setValue(cboAdd03.getValue());
				address3.setAttribute(CustomersDetail.ATT_LOCATION,
						new LatLng(gmarkerAdd03.getLat(), gmarkerAdd03.getLng()));
				onClose();
			}
		});

	}

	private void initMaps() {
		gmaps = new Gmaps();
		gmarkerAdd01 = new Gmarker();
		gmarkerAdd02 = new Gmarker();
		gmarkerAdd03 = new Gmarker();
		gmaps.setId("gmaps_TaxiId");
		gmaps.setSclass("gmaps_Taxi");
		gmaps.setVflex("1");
		gmaps.setParent(divMaps);
		gmaps.setStyle("width : 100% ; height : 100%");
		gmaps.setVersion("3.9");
		gmaps.setShowSmallCtrl(true);
		gmaps.setShowPanCtrl(false);// *
		gmaps.setLanguage("vi");
		gmaps.addEventListener("onMapDrop", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				MapDropEvent evt = (MapDropEvent) event;
				if (evt.getDragged().equals(gmarkerAdd01)) {
					cboAdd01.setValue(MapUtils.convertLatLongToAddrest(gmarkerAdd01.getLat(), gmarkerAdd01.getLng()));

				} else if (evt.getDragged().equals(gmarkerAdd02)) {
					cboAdd02.setValue(MapUtils.convertLatLongToAddrest(gmarkerAdd02.getLat(), gmarkerAdd02.getLng()));
				} else if (evt.getDragged().equals(gmarkerAdd03)) {
					cboAdd03.setValue(MapUtils.convertLatLongToAddrest(gmarkerAdd03.getLat(), gmarkerAdd03.getLng()));
				}
			}
		});
	}

	private void handInputAddress(Event event) {
		String tmpAdd = ((Combobox) event.getTarget()).getValue();
		if (tmpAdd.length() == 0) {
			if (event.getTarget().equals(cboAdd01)) {
				gmarkerAdd01.setLat(-1);
				gmarkerAdd01.setLng(-1);
				if (gmarkerAdd01.getParent().equals(gmaps))
					gmaps.removeChild(gmarkerAdd01);
			}
			if (event.getTarget().equals(cboAdd02)) {
				gmarkerAdd02.setLat(-1);
				gmarkerAdd02.setLng(-1);
				if (gmarkerAdd01.getParent().equals(gmaps))
					gmaps.removeChild(gmarkerAdd02);
			}
			if (event.getTarget().equals(cboAdd03)) {
				gmarkerAdd03.setLat(-1);
				gmarkerAdd03.setLng(-1);
				if (gmarkerAdd01.getParent().equals(gmaps))
					gmaps.removeChild(gmarkerAdd03);
			}
		} else {
			LatLng latlng = MapUtils.convertAddresstoLatLng(tmpAdd);
			if (latlng != null) {
				if (event.getTarget().equals(cboAdd01)) {
					gmarkerAdd01.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS1);
					gmarkerAdd01.setLat(latlng.lat);
					gmarkerAdd01.setLng(latlng.lng);
					gmarkerAdd01.setDraggingEnabled(true);
					gmarkerAdd01.setParent(gmaps);
					// gmaps.setCenter(gmarkerAdd01.getLat(),
					// gmarkerAdd01.getLng());
				} else if (event.getTarget().equals(cboAdd02)) {
					gmarkerAdd02.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS2);
					gmarkerAdd02.setLat(latlng.lat);
					gmarkerAdd02.setLng(latlng.lng);
					gmarkerAdd02.setDraggingEnabled(true);
					gmarkerAdd02.setParent(gmaps);
					// gmaps.setCenter(gmarkerAdd02.getLat(),
					// gmarkerAdd02.getLng());
				} else {
					gmarkerAdd03.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS3);
					gmarkerAdd03.setLat(latlng.lat);
					gmarkerAdd03.setLng(latlng.lng);
					gmarkerAdd03.setDraggingEnabled(true);
					gmarkerAdd03.setParent(gmaps);
					// gmaps.setCenter(gmarkerAdd03.getLat(),
					// gmarkerAdd03.getLng());
				}
//				MapUtils.scaleMap(gmaps);
			}
		}

	}

	private void loadAddress() {
		cboAdd01.setValue(address1.getValue());
		if (address1.getAttribute(CustomersDetail.ATT_LOCATION) != null) {
			LatLng latLng = (LatLng) address1.getAttribute(CustomersDetail.ATT_LOCATION);
			if (latLng != null) {
				gmarkerAdd01.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS1);
				gmarkerAdd01.setLat(latLng.lat);
				gmarkerAdd01.setLng(latLng.lng);
				gmarkerAdd01.setDraggingEnabled(true);
				gmarkerAdd01.setParent(gmaps);
			}
		}
		cboAdd02.setValue(address2.getValue());
		if (address2.getAttribute(CustomersDetail.ATT_LOCATION) != null) {
			LatLng latLng = (LatLng) address2.getAttribute(CustomersDetail.ATT_LOCATION);
			if (latLng != null) {
				gmarkerAdd02.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS2);
				gmarkerAdd02.setLat(latLng.lat);
				gmarkerAdd02.setLng(latLng.lng);
				gmarkerAdd02.setDraggingEnabled(true);
				gmarkerAdd02.setParent(gmaps);
			}
		}
		cboAdd03.setValue(address3.getValue());
		if (address3.getAttribute(CustomersDetail.ATT_LOCATION) != null) {
			LatLng latLng = (LatLng) address3.getAttribute(CustomersDetail.ATT_LOCATION);
			if (latLng != null) {
				gmarkerAdd03.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS3);
				gmarkerAdd03.setLat(latLng.lat);
				gmarkerAdd03.setLng(latLng.lng);
				gmarkerAdd03.setDraggingEnabled(true);
				gmarkerAdd03.setParent(gmaps);
			}
		}
	}

	public void checkLocation() {
		if (customer.getAddress() != null) {
			if (customer.getAddress().length() > 0) {
				LatLng latLng1 = MapUtils.convertAddresstoLatLng(customer.getAddress());
				if (customer.getAddressLat() == null && customer.getAddressLng() == null) {
					try {
						LatLng latLng = MapUtils.convertAddresstoLatLng(customer.getAddress());
						if (latLng != null) {
							gmarkerAdd01.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS1);
							gmarkerAdd01.setLat(latLng.lat);
							gmarkerAdd01.setLng(latLng.lng);
							gmarkerAdd01.setDraggingEnabled(true);
							gmarkerAdd01.setParent(gmaps);
						}
					} catch (Exception e) {

					}
				} else if (customer.getAddressLat() <= 0 && customer.getAddressLng() <= 0) {
					try {
						LatLng latLng = MapUtils.convertAddresstoLatLng(customer.getAddress());
						if (latLng != null) {
							gmarkerAdd01.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS1);
							gmarkerAdd01.setLat(latLng.lat);
							gmarkerAdd01.setLng(latLng.lng);
							gmarkerAdd01.setDraggingEnabled(true);
							gmarkerAdd01.setParent(gmaps);
						}
					} catch (Exception e) {

					}
				} else {
					gmarkerAdd01.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS1);
					gmarkerAdd01.setDraggingEnabled(true);
					gmarkerAdd01.setParent(gmaps);
					gmarkerAdd01.setLat(customer.getAddressLat());
					gmarkerAdd01.setLng(customer.getAddressLng());
				}
			}
		}
		if (customer.getAddress2() != null) {
			if (customer.getAddress2().length() > 0) {
				if (customer.getAddress2Lat() == null && customer.getAddress2Lng() == null) {
					try {
						LatLng latLng = MapUtils.convertAddresstoLatLng(customer.getAddress2());
						if (latLng != null) {
							gmarkerAdd02.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS2);
							gmarkerAdd02.setLat(latLng.lat);
							gmarkerAdd02.setLng(latLng.lng);
							gmarkerAdd02.setDraggingEnabled(true);
							gmarkerAdd02.setParent(gmaps);
						}
					} catch (Exception e) {

					}
				} else if (customer.getAddress2Lat() <= 0 && customer.getAddress2Lng() <= 0) {
					try {
						LatLng latLng = MapUtils.convertAddresstoLatLng(customer.getAddress2());
						if (latLng != null) {
							gmarkerAdd02.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS2);
							gmarkerAdd02.setLat(latLng.lat);
							gmarkerAdd02.setLng(latLng.lng);
							gmarkerAdd02.setDraggingEnabled(true);
							gmarkerAdd02.setParent(gmaps);
						}
					} catch (Exception e) {

					}
				} else {
					gmarkerAdd02.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS2);
					gmarkerAdd02.setLat(customer.getAddress2Lat());
					gmarkerAdd02.setLng(customer.getAddress2Lng());
					gmarkerAdd02.setDraggingEnabled(true);
					gmarkerAdd02.setParent(gmaps);
				}
			}
		}
		if (customer.getAddress3() != null) {
			if (customer.getAddress3().length() > 0) {
				if (customer.getAddress3Lat() == null && customer.getAddress3Lng() == null) {
					try {
						LatLng latLng = MapUtils.convertAddresstoLatLng(customer.getAddress3());
						if (latLng != null) {
							gmarkerAdd03.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS3);
							gmarkerAdd03.setLat(latLng.lat);
							gmarkerAdd03.setLng(latLng.lng);
							gmarkerAdd03.setDraggingEnabled(true);
							gmarkerAdd03.setParent(gmaps);
						}
					} catch (Exception e) {

					}
				} else if (customer.getAddress3Lat() <= 0 && customer.getAddress3Lng() <= 0) {
					try {
						LatLng latLng = MapUtils.convertAddresstoLatLng(customer.getAddress3());
						if (latLng != null) {
							gmarkerAdd03.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS3);
							gmarkerAdd03.setLat(latLng.lat);
							gmarkerAdd03.setLng(latLng.lng);
							gmarkerAdd03.setDraggingEnabled(true);
							gmarkerAdd03.setParent(gmaps);
						}
					} catch (Exception e) {

					}
				} else {
					gmarkerAdd03.setIconImage(CommonDefine.GoogleMap.URL_ICON_ADDRESS3);
					gmarkerAdd03.setLat(customer.getAddress3Lat());
					gmarkerAdd03.setLng(customer.getAddress3Lng());
					gmarkerAdd03.setDraggingEnabled(true);
					gmarkerAdd03.setParent(gmaps);
				}
			}
		}
//		MapUtils.scaleMap(gmaps);
	}

}