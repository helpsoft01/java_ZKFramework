package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.LatLng;
import org.zkoss.gmaps.event.MapDropEvent;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Frozen;
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
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.West;

import com.vietek.taxioperation.model.ParkingArea;
import com.vietek.taxioperation.ui.editor.FindAddressEditor;
import com.vietek.taxioperation.ui.editor.FindAddressHandler;
import com.vietek.taxioperation.util.Address;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.MapUtils;

public class ParkingAreas extends Div implements Serializable, FindAddressHandler, EventListener<Event> {

	/**
	 * @author hung
	 */
	private static final long serialVersionUID = 1L;
	private Gmaps gmapParking;
	private Listbox listboxParking;
	private List<ParkingArea> listmodel;
	private Button btnThem;
	private Div paneldetail;
	private ParkingAreaDetail detailWindow;
	private ParkingArea currentModel;
	private HashMap<ParkingArea, Gmarker> mapmarker;
	private Gmarker addgmarker;
	private FindAddressEditor findaddress;
	private Textbox searchpathcode;
	private Textbox searchpathAddress;
	private Textbox searchpathgroup;
	private boolean isadd = false;

	public ParkingAreas() {
		this.setHeight("100%");
		Borderlayout borderlayout = new Borderlayout();
		borderlayout.setParent(this);
		West west = new West();
		west.setSize("400px");
		west.setVflex("true");
		west.setParent(borderlayout);

		Center center = new Center();
		center.setParent(borderlayout);
		creatFrmLeft(west);
		creatFrmRight(center);
		loadData();
	}

	private void creatFrmRight(Component parent) {
		Div div = new Div();
		div.setStyle("width:100%; height: 100%");
		div.setZindex(1);
		div.setParent(parent);
		gmapParking = new Gmaps();
		gmapParking.setParent(div);
		gmapParking.setSclass("z-gmap");
		gmapParking.setHflex("1");
		gmapParking.setVflex("true");
		gmapParking.setVersion("3.9");
		gmapParking.setShowSmallCtrl(true);
		gmapParking.setCenter(new LatLng(20.971715711074314, 465.83903365796465));
		gmapParking.setZoom(17);
		gmapParking.addEventListener("onMapClick", this);
		gmapParking.addEventListener("onMapDrop", this);
		addgmarker = new Gmarker();
		addgmarker.setDraggingEnabled(true);
		addgmarker.setIconImage("./themes/images/map-object-blue-geo-point-32.png");
		Div divrightdetail = new Div();
		divrightdetail.setParent(div);
		divrightdetail.setSclass("z-div-right-detail z-parking");
		divrightdetail.setStyle("background-color: green;border-radius: 5px;padding-left: 7px;");
		divrightdetail.setZindex(1);
		creatfrmDetail(divrightdetail);
	}

	private void creatfrmDetail(Div parent) {
		paneldetail = new Div();
		paneldetail.setParent(parent);
		paneldetail.setSclass("z-panel-layout-history");
		Image img = new Image("./themes/images/searchpak.png");
		img.setParent(parent);
		findaddress = new FindAddressEditor("Tìm địa chỉ", gmapParking);
		findaddress.getComponent().setButtonVisible(false);
		findaddress.getComponent().setParent(parent);
		findaddress.getComponent().setStyle("margin-left:5px ! important;font-size: 14px ! important");
		findaddress.handler = this;

	}

	private void creatFrmLeft(Component parent) {
		Div div = new Div();
		div.setStyle("width:100%; height:100%");
		div.setParent(parent);
		Panel panel = new Panel();
		panel.setSclass("z-history-panel panel panel-noborder panel-noheader panel-noframe");
		panel.setParent(div);
		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);
		Toolbar toolbar = new Toolbar();
		toolbar.appendChild(new Label("Danh sách bãi giao ca"));
		toolbar.setSclass("z-toolbar-history");
		toolbar.setAlign("center");
		toolbar.setParent(panelchildren);
		listboxParking = new Listbox();
		listboxParking.setEmptyMessage("Không có dữ liệu");
		listboxParking.addEventListener(Events.ON_SELECT, this);
		listboxParking.setSizedByContent(true);
		listboxParking.setSpan(true);
		listboxParking.setMold("paging");
		listboxParking.setAutopaging(true);
		listboxParking.setPagingPosition("both");
		listboxParking.addEventListener("onPaging", this);
		listboxParking.setHeight("450px");
		listboxParking.setParent(panelchildren);
		Frozen frozen = new Frozen();
		frozen.setStart(0);
		frozen.setColumns(2);
		frozen.setParent(listboxParking);
		Listhead head = new Listhead();
		head.setParent(listboxParking);
		head.setSizable(true);
		Listheader header = new Listheader("TT");
		head.appendChild(header);
		header = new Listheader("Mã bãi");
		header.setWidth("80px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Tên địa điểm");
		// header.setStyle("min-width: 150px;");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Đội xe");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Kinh độ");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Vĩ độ");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Số lượng");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Del");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Edit");
		header.setAlign("center");
		head.appendChild(header);
		Auxhead auxs = new Auxhead();
		auxs.setParent(listboxParking);
		Auxheader aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		searchpathcode = new Textbox();
		searchpathcode.setSclass("z-textbox-search");
		searchpathcode.setId("value");
		searchpathcode.setAttribute("datafield", "");
		searchpathcode.setPlaceholder("Mã bãi");
		searchpathcode.setParent(aux);
		searchpathcode.addEventListener(Events.ON_OK, this);
		searchpathcode.addForward(Events.ON_CHANGE, searchpathcode, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		searchpathAddress = new Textbox();
		searchpathAddress.setAttribute("datafield", "");
		searchpathAddress.setId("Address");
		searchpathAddress.setSclass("z-textbox-search");
		searchpathAddress.setPlaceholder("Địa chỉ");
		searchpathAddress.setParent(aux);
		searchpathAddress.addEventListener(Events.ON_OK, this);
		searchpathAddress.addForward(Events.ON_CHANGE, searchpathAddress, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		searchpathgroup = new Textbox();
		searchpathgroup.setAttribute("datafield", "");
		searchpathgroup.setId("taxigroup");
		searchpathgroup.setSclass("z-textbox-search");
		searchpathgroup.setPlaceholder("Đội xe");
		searchpathgroup.setParent(aux);
		searchpathgroup.addEventListener(Events.ON_OK, this);
		searchpathgroup.addForward(Events.ON_CHANGE, searchpathgroup, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);

		listboxParking.setItemRenderer(new ListitemRenderer<ParkingArea>() {

			@Override
			public void render(Listitem items, ParkingArea data, int index) throws Exception {
				items.setValue(data);
				Label lb = new Label();
				lb.setValue(index + 1 + "");
				Listcell listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getValue());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getAddress());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				if (data.getTaxigroup() != null) {
					lb.setValue(data.getTaxigroup().toString());
				}
				listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(String.valueOf(data.getLongi()));
				listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(String.valueOf(data.getLati()));
				listcell = new Listcell();
				listcell.setStyle("text-align: right");
				listcell.appendChild(lb);
				listcell.setParent(items);

				lb = new Label();
				lb.setValue(data.getQuota() + "");
				listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				Toolbarbutton btndel = new Toolbarbutton();
				btndel.setImage("./themes/images/DeleteRed_16.png");
				btndel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						listboxParking.setSelectedIndex(index);
						try {
							currentModel = (ParkingArea) listboxParking.getSelectedItem().getValue();
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
												"Không thể xóa vì bản ghi bạn chọn đã có liên kết với dữ liệu ở danh mục khác !",
												Clients.NOTIFICATION_TYPE_INFO);
									}
								}

							});
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
				listcell = new Listcell();
				listcell.appendChild(btndel);
				listcell.setParent(items);
				Toolbarbutton btnedit = new Toolbarbutton();
				btnedit.setImage("./themes/images/edit.png");
				btnedit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						listboxParking.setSelectedIndex(index);
						try {
							ParkingArea model = (ParkingArea) listboxParking.getSelectedItem().getValue();
							showDetail(model);
							initmap(model);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				listcell = new Listcell();
				listcell.appendChild(btnedit);
				listcell.setParent(items);
			}

		});
		Toolbar toolb = new Toolbar();
		toolb.setVflex("true");
		toolb.setAlign("center");
		toolb.setParent(div);
		btnThem = new Button("Thêm Mới");
		btnThem.setParent(toolb);
		btnThem.setSclass("z-btn-new-parking");
		btnThem.addEventListener(Events.ON_CLICK, this);
		btnThem.setStyle("width: 85%;border-radius: 15px;background-color: #1BA339;font-weight: bold;color: white;");
	}

	private void handleEventDelete() {
		if (currentModel != null) {
			currentModel.delete();
			if (detailWindow.isVisible()) {
				detailWindow.detach();
			}
			refresh();
			Env.getHomePage().showNotification("Đã xóa thành công", Clients.NOTIFICATION_TYPE_INFO);
		}

	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		Session session = ControllerUtils.getCurrentSession();
		Query query;
		query = session.createQuery("from ParkingArea");
		try {
			listmodel = query.list();
			renderListBox(listmodel);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	private void renderListBox(List<ParkingArea> lstmodel) {
		if (lstmodel != null) {
			if (mapmarker != null && mapmarker.size() > 0) {
				mapmarker.clear();
			}
			gmapParking.getChildren().clear();
			listboxParking.setModel(new ListModelList<ParkingArea>(lstmodel));
			for (ParkingArea parkingArea : lstmodel) {

				if (parkingArea.getLati() != null && parkingArea.getLongi() != null) {
					if (!parkingArea.getLati().equals(0.0) && !parkingArea.getLongi().equals(0.0)) {
						addMarkerToMap(parkingArea, new LatLng(parkingArea.getLati(), parkingArea.getLongi()));
					}
				}
			}
			MapUtils.scaleMap(gmapParking);
		}
	}

	private void addMarkerToMap(ParkingArea park, LatLng latLng) {
		if (mapmarker == null) {
			mapmarker = new HashMap<ParkingArea, Gmarker>();
		}
		Gmarker marker = new Gmarker(park.getValue());
		marker.setIconImage("./themes/images/ML20.png");
		marker.setAttribute("index", listmodel.indexOf(park));
		marker.setLat(latLng.getLatitude());
		marker.setLng(latLng.getLongitude());
		marker.setContent("Mã Bãi:" + park.getValue());
		marker.setAttribute("Parking", park);
		gmapParking.appendChild(marker);
		mapmarker.put(park, marker);
	}

	private ParkingAreaDetail getDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new ParkingAreaDetail(currentModel, null);
			detailWindow.setTitle("Thông tin chi tiết");
			detailWindow.setAction("show: slideDown;hide: slideUp");
			detailWindow.addEventListener(Events.ON_CLOSE, this);
			detailWindow.addEventListener(Events.ON_CHANGE, this);
		}

		detailWindow.setParent(this);
		detailWindow.setStyle("position: absolute;width: 300px;left: 417px;top: 171px;");
		detailWindow.setVisible(false);
		return detailWindow;
	}

	public void showDetail(ParkingArea currentModel) {
		if (this.currentModel != null) {
			Gmarker martmp = mapmarker.get(this.currentModel);
			if (martmp != null) {
				martmp.setIconImage("./themes/images/ML20.png");
				martmp.setOpen(false);
				martmp.setDraggingEnabled(false);
			}

		}
		this.currentModel = currentModel;
		getDetailWindow().setModel(currentModel);
		getDetailWindow().doOverlapped();
		btnThem.setDisabled(true);
	}

	@Override
	public void onEvent(Event events) throws Exception {
		if (events.getTarget().equals(btnThem)) {
			handlerBtnThem();

		} else if (events.getTarget().equals(listboxParking)) {
			handlerListBox(events);

		} else if (events.getTarget().equals(gmapParking)) {
			handlerGmap(events);
		} else if (events.getTarget().equals(detailWindow)) {
			if (events.getName().equals(Events.ON_CLOSE)) {
				btnThem.setDisabled(false);
				ParkingArea parmodel = ((ParkingArea) detailWindow.getModel());
				parmodel.refresh();
				Gmarker marker = mapmarker.get(parmodel);
				if (marker != null) {
					marker.setLat(parmodel.getLati());
					marker.setLng(parmodel.getLongi());
					marker.setIconImage("./themes/images/ML20.png");
					marker.setDraggingEnabled(false);
					marker.setOpen(false);
				}
			} else if (events.getName().equals(Events.ON_CHANGE)) {
				refresh();
				btnThem.setDisabled(false);
			}
		} else if (events.getTarget().equals(searchpathcode)) {
			searchpathcode.setAttribute("datafield", searchpathcode.getValue());
			executeSearch();
		} else if (events.getTarget().equals(searchpathAddress)) {
			searchpathAddress.setAttribute("datafield", searchpathAddress.getValue());
			executeSearch();
		} else if (events.getTarget().equals(searchpathgroup)) {
			searchpathgroup.setAttribute("datafield", searchpathgroup.getValue());
			executeSearch();
		}
	}

	@SuppressWarnings("unchecked")
	private void executeSearch() {
		Predicate pre = new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
				ParkingArea par = (ParkingArea) arg0;
				String code = (String) searchpathcode.getAttribute("datafield");
				String address = (String) searchpathAddress.getAttribute("datafield");
				String group = (String) searchpathgroup.getAttribute("datafield");
				if (par.getValue().toUpperCase().contains(code.toUpperCase())
						&& par.getAddress().toUpperCase().contains(address.toUpperCase()) && par.getTaxigroup() != null
						&& par.getTaxigroup().toString().toUpperCase().contains(group.toUpperCase())) {
					return true;
				}
				return false;
			}
		};
		Collection<ParkingArea> coll = CollectionUtils.select(listmodel, pre);
		List<ParkingArea> lsttmp = new ArrayList<ParkingArea>(coll);
		renderListBox(lsttmp);

	}

	@SuppressWarnings("deprecation")
	private void handlerGmap(Event events) {
		if (events.getName().equals("onMapClick")) {
			MapMouseEvent event = (MapMouseEvent) events;
			AbstractComponent comp = (AbstractComponent) event.getReference();
			if (comp instanceof Gmarker && !comp.equals(addgmarker)) {
				((Gmarker) comp).setOpen(true);
				ParkingArea model = (ParkingArea) ((Gmarker) comp).getAttribute("Parking");
				if (model != null) {
					showDetail(model);
					initmap(model);
				}
			} else {
				if (addgmarker.getParent() == null) {
					addgmarker.setParent(gmapParking);
				}
				addgmarker.setLat(event.getLat());
				addgmarker.setLng(event.getLng());
				Address add = new Address();
				add.setLatitude(event.getLat());
				add.setLongitude(event.getLng());
				findaddress.getComponent().setText(MapUtils.convertLatLongToAddrest(event.getLat(), event.getLng()));
				findaddress.setValue(add);
				if (detailWindow != null && detailWindow.isVisible() && detailWindow.getModel().getId() == 0) {
					detailWindow.updateAddress(findaddress.getComponent().getText(),
							new LatLng(addgmarker.getLat(), addgmarker.getLng()));
				}
				if (isadd) {
					Events.sendEvent(Events.ON_CLICK, btnThem, null);
					isadd = false;
				}
			}
		} else if (events.getName().equals("onMapDrop")) {
			MapDropEvent event = (MapDropEvent) events;
			if (event.getDragged().equals(addgmarker)) {
				findaddress.getComponent().setText(MapUtils.convertLatLongToAddrest(event.getLat(), event.getLng()));
				if (detailWindow != null && detailWindow.isVisible()) {
					detailWindow.updateAddress(findaddress.getComponent().getText(),
							new LatLng(addgmarker.getLat(), addgmarker.getLng()));
				}

			} else if (event.getDragged() instanceof Gmarker) {
				ParkingArea data = ((ParkingArea) detailWindow.getModel());
				Gmarker mar = (Gmarker) event.getDragged();
				if (detailWindow != null && detailWindow.isVisible() && mapmarker.get(data).equals(mar)) {
					detailWindow.updateAddress(MapUtils.convertLatLongToAddrest(mar.getLat(), mar.getLng()),
							new LatLng(mar.getLat(), mar.getLng()));
				}
			}

		}
	}

	private void handlerBtnThem() {
		if (!findaddress.getComponent().getValue().equals("")) {
			try {
				ParkingArea model = ParkingArea.class.newInstance();
				model.setAddress(findaddress.getComponent().getValue());
				model.setLati(addgmarker.getLat());
				model.setLongi(addgmarker.getLng());
				gmapParking.setCenter(addgmarker.getLat(), addgmarker.getLng() - 0.002);
				gmapParking.setZoom(17);
				showDetail(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Messagebox.show("Click chuột trên bản đồ để lấy vị trí bãi xe!", "Thông báo", Messagebox.OK,
					Messagebox.INFORMATION);
			isadd = true;
		}

	}

	private void handlerListBox(Event events) {
		if (events.getName().equals(Events.ON_SELECT)) {
			ParkingArea model = (ParkingArea) listboxParking.getSelectedItem().getValue();
			if (model != null) {
				showDetail(model);
				initmap(model);
			}
		}

	}

	private void initmap(ParkingArea park) {
		Gmarker marker = mapmarker.get(park);
		if (marker != null) {
			gmapParking.setCenter(marker.getLat(), marker.getLng() - 0.002);
			gmapParking.setZoom(17);
			marker.setDraggingEnabled(true);
			marker.setIconImage("./themes/images/map-object-blue-geo-point-32.png");
		}

	}

	public void refresh() {
		gmapParking.getChildren().clear();
		if (detailWindow.isVisible()) {
			detailWindow.detach();
		}
		loadData();
	}

	public Gmaps getGmapParking() {
		return gmapParking;
	}

	public void setGmapParking(Gmaps gmapParking) {
		this.gmapParking = gmapParking;
	}

	public Listbox getListboxParking() {
		return listboxParking;
	}

	public void setListboxParking(Listbox listboxParking) {
		this.listboxParking = listboxParking;
	}

	@Override
	public void onChangeAddress(Address address, int type) {

		if (address.getLatitude() != 0.0 && address.getLongitude() != 0.0) {
			if (addgmarker.getParent() == null) {
				addgmarker.setParent(gmapParking);
			}
			addgmarker.setLat(address.getLatitude());
			addgmarker.setLng(address.getLongitude());
			gmapParking.setCenter(address.getLatitude(), address.getLongitude());
			gmapParking.setZoom(17);
			if (detailWindow != null && detailWindow.isVisible()
					&& ((ParkingArea) detailWindow.getModel()).getId() == 0) {
				detailWindow.updateAddress(address.getName(),
						new LatLng(address.getLatitude(), address.getLongitude()));
			}
		}
	}

}