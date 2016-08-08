package com.vietek.taxioperation.ui.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Query;
import org.hibernate.Session;
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

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.EnumCarTypeCommon;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.googlemapSearch.GoogleMapUntil;
import com.vietek.taxioperation.model.ArrangementTaxi;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvent;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvents;
import com.vietek.taxioperation.ui.controller.vmap.VMaps;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;
import com.vietek.taxioperation.ui.editor.FindAddressEditor;
import com.vietek.taxioperation.ui.editor.FindAddressHandler;
import com.vietek.taxioperation.util.Address;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.MapUtils;
import com.vietek.taxioperation.util.TaxiUtils;
import com.vietek.taxioperation.util.VehicleTmp;

public class ArrangementTaxis extends Div implements Serializable, FindAddressHandler, EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VMaps gmapArrangement;
	private Listbox listboxArrangement;
	private List<ArrangementTaxi> listmodel;
	private Button btnThem;
	private ArrangementTaxisDetail detailWindow;
	private ArrangementTaxi currentModel;
	private HashMap<ArrangementTaxi, VMarker> mapmarker;
	private VMarker addgmarker;
	private FindAddressEditor findaddress;
	private Textbox searchcode;
	private Textbox searchname;
	private Textbox searchAddress;
	private Textbox searchagent;
	private boolean isadd = false;
	private List<VMarker> lstMarkerCar;
	
	public int MAX_DRIVER_TO_ASK = ConfigUtil.getValueConfig("MAX_DRIVER_TO_ASK", CommonDefine.MAX_DRIVER_TO_ASK);
	public int MAX_DISTANCE = ConfigUtil.getValueConfig("MAX_DISTANCE", CommonDefine.MAX_DISTANCE); // 1km
	private List<VehicleTmp> lstNearTaxi;
	public ArrangementTaxis() {
		this.setHeight("100%");
		Borderlayout borderlayout = new Borderlayout();
		borderlayout.setParent(this);
		West west = new West();
		west.setSize("350px");
		west.setVflex("true");
		west.setParent(borderlayout);

		Center center = new Center();
		center.setParent(borderlayout);
		createFrmLeft(west);
		createFrmRight(center);
		loadData();
	}
	
	private void createFrmRight(Component parent) {
		Div div = new Div();
		div.setStyle("width:100%; height: 100%");
		div.setZindex(1);
		div.setParent(parent);
		gmapArrangement = new VMaps(div);
		gmapArrangement.setParent(div);
		gmapArrangement.setSclass("z-gmap");
		gmapArrangement.setCenter(new LatLng(20.971715711074314, 465.83903365796465));
		gmapArrangement.setZoom(17);
//		gmapArrangement.addEventListener(VMapEvents.ON_VMAP_CLICK, this);
//		gmapArrangement.addEventListener(VMapEvents.ON_VMARKER_DRAG_END, this);
		gmapArrangement.addEventListener(VMapEvents.ON_VMARKER_DRAG_END, ON_EVENT_VMAP);
		gmapArrangement.addEventListener(VMapEvents.ON_VMAP_CLICK, ON_EVENT_VMAP);
		addgmarker = new VMarker();
		addgmarker.setDraggable(true);
		addgmarker.setIconImage("./themes/images/map-object-blue-geo-point-32.png");
		Div divrightdetail = new Div();
		divrightdetail.setParent(div);
		divrightdetail.setSclass("z-div-right-detail z-parking");
		divrightdetail.setStyle("background-color: green;border-radius: 5px;padding-left: 7px;");
		divrightdetail.setZindex(1);
		createfrmDetail(divrightdetail);
	}
	
	private EventListener<Event> ON_EVENT_VMAP = new EventListener<Event>() {
		@Override
		public void onEvent(Event events) throws Exception {
			VMapEvent event = (VMapEvent) events;
			if(event.getVMaps().equals(gmapArrangement)){
				handlerGmap(event);
			}
		}
	};
	
	private void createfrmDetail(Div parent) {
		Div div = new Div();
		div.setParent(parent);
		div.setSclass("z-panel-layout-history");
		Image img = new Image("./themes/images/searchpak.png");
		img.setParent(parent);
		findaddress = new FindAddressEditor("Tìm địa chỉ", gmapArrangement);
		findaddress.getComponent().setButtonVisible(false);
		findaddress.getComponent().setParent(parent);
		findaddress.getComponent().setStyle("margin-left:5px ! important;font-size: 14px ! important; width: 400px !important;");
		findaddress.handler = this;

	}
	
	private void createFrmLeft(Component comp){
		Div div = new Div();
		div.setStyle("width:100%; height:100%");
		div.setParent(comp);
		Panel panel = new Panel();
		panel.setSclass("z-history-panel panel panel-noborder panel-noheader panel-noframe");
		panel.setParent(div);
		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);
		Toolbar toolbar = new Toolbar();
		toolbar.appendChild(new Label("Danh sách điểm tiếp thị"));
		toolbar.setSclass("z-toolbar-history");
		toolbar.setAlign("center");
		toolbar.setParent(panelchildren);
		listboxArrangement = new Listbox();
		listboxArrangement.setEmptyMessage("Không có dữ liệu");
		listboxArrangement.addEventListener(Events.ON_SELECT, this);
		listboxArrangement.setSizedByContent(true);
		listboxArrangement.setSpan(true);
		listboxArrangement.setMold("paging");
		listboxArrangement.setAutopaging(true);
		listboxArrangement.setPagingPosition("both");
		listboxArrangement.addEventListener("onPaging", this);
		listboxArrangement.setHeight("450px");
		listboxArrangement.setParent(panelchildren);
		Frozen frozen = new Frozen();
		frozen.setStart(0);
		frozen.setColumns(2);
		frozen.setParent(listboxArrangement);
		Listhead head = new Listhead();
		head.setParent(listboxArrangement);
		head.setSizable(true);
		Listheader header = new Listheader("TT");
		head.appendChild(header);
		header = new Listheader("Mã điển");
		header.setWidth("80px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Tên điểm");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Loại điểm");
		header.setWidth("80px");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Địa chỉ");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Chi nhánh");
		header.setAlign("center");
		head.appendChild(header);
		header = new Listheader("Bán kính");
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
		auxs.setParent(listboxArrangement);
		Auxheader aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		searchcode = new Textbox();
		searchcode.setSclass("z-textbox-search");
		searchcode.setAttribute("datafield", "");
		searchcode.setPlaceholder("Mã điểm");
		searchcode.setParent(aux);
		searchcode.addEventListener(Events.ON_OK, this);
		searchcode.addForward(Events.ON_CHANGE, searchcode, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		searchname = new Textbox();
		searchname.setAttribute("datafield", "");
		searchname.setSclass("z-textbox-search");
		searchname.setPlaceholder("Tên điểm");
		searchname.setParent(aux);
		searchname.addEventListener(Events.ON_OK, this);
		searchname.addForward(Events.ON_CHANGE, searchname, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		searchAddress = new Textbox();
		searchAddress.setAttribute("datafield", "");
		searchAddress.setSclass("z-textbox-search");
		searchAddress.setPlaceholder("Địa chỉ");
		searchAddress.setParent(aux);
		searchAddress.addEventListener(Events.ON_OK, this);
		searchAddress.addForward(Events.ON_CHANGE, searchAddress, Events.ON_OK);
		auxs.appendChild(aux);
		aux = new Auxheader();
		aux.setColspan(1);
		searchagent = new Textbox();
		searchagent.setAttribute("datafield", "");
		searchagent.setSclass("z-textbox-search");
		searchagent.setPlaceholder("Chi nhánh");
		searchagent.setParent(aux);
		searchagent.addEventListener(Events.ON_OK, this);
		searchagent.addForward(Events.ON_CHANGE, searchagent, Events.ON_OK);
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
		
		listboxArrangement.setItemRenderer(new ListitemRenderer<ArrangementTaxi>() {

			@Override
			public void render(Listitem items, ArrangementTaxi data, int index) throws Exception {
				items.setValue(data);
				Label lb = new Label();
				lb.setValue(index + 1 + "");
				Listcell listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getValue());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getName());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				if(data.getIsMarketing()){
					lb.setValue("Điểm tiếp thị");
				}else{
					lb.setValue("Điểm sắp tài");
				}
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(String.valueOf(data.getAddress()));
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				if (data.getAgent() != null) {
					lb.setValue(data.getAgent().getAgentName().toString());
				}
				listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(String.valueOf(data.getRadius()));
				listcell = new Listcell();
				listcell.setStyle("text-align: right");
				listcell.appendChild(lb);
				listcell.setParent(items);

				lb = new Label();
				lb.setValue(data.getMaxCar() + "");
				listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				Toolbarbutton btndel = new Toolbarbutton();
				btndel.setImage("./themes/images/DeleteRed_16.png");
				btndel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						listboxArrangement.setSelectedIndex(index);
						try {
							currentModel = (ArrangementTaxi) listboxArrangement.getSelectedItem().getValue();
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
						listboxArrangement.setSelectedIndex(index);
						try {
							ArrangementTaxi model = (ArrangementTaxi) listboxArrangement.getSelectedItem().getValue();
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
			LinkedList<Taxi> lstTmp = MapCommon.MAP_TAXI_LIST.get(currentModel.getId());
			if(lstTmp != null && lstTmp.size() > 0){
				for (Taxi taxi : lstTmp) {
					taxi.setArrangementResult(null);
					taxi.setArrangementTaxi(null);
					taxi.driverGetMarketingPlaceInfo();
					lstTmp.remove(this); 
				}
			}
			currentModel.delete();
			if (detailWindow.isVisible()) {
				detailWindow.detach();
			}
			refresh();
			Env.getHomePage().showNotification("Đã xóa thành công", Clients.NOTIFICATION_TYPE_INFO);
		}

	}
	
	public void refresh() {
		gmapArrangement.getChildren().clear();
		if (detailWindow.isVisible()) {
			detailWindow.detach();
		}
		loadData();
	}
	
	@SuppressWarnings("unchecked")
	private void loadData() {
		Session session = ControllerUtils.getCurrentSession();
		Query query;
		query = session.createQuery("from ArrangementTaxi");
		try {
			listmodel = query.list();
			renderListBox(listmodel);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void showDetail(ArrangementTaxi currentModel) {
		if (this.currentModel != null) {
			VMarker martmp = mapmarker.get(this.currentModel);
			if (martmp != null) {
				martmp.setIconImage("./themes/images/ML20.png");
				martmp.setDraggable(false);
				martmp.setContent(this.currentModel.getName());
				martmp.setOpen(true);
			}

		}
		this.currentModel = currentModel;
		getDetailWindow().setModel(currentModel);
		getDetailWindow().doOverlapped();
		btnThem.setDisabled(true);
	}
	
	private ArrangementTaxisDetail getDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new ArrangementTaxisDetail(currentModel, null);
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
	
	private void renderListBox(List<ArrangementTaxi> lstmodel) {
		if (lstmodel != null) {
			if (mapmarker != null && mapmarker.size() > 0) {
				mapmarker.clear();
			}
			gmapArrangement.getChildren().clear();
			listboxArrangement.setModel(new ListModelList<ArrangementTaxi>(lstmodel));
			for (ArrangementTaxi arrangementTaxi : lstmodel) {

				if (arrangementTaxi.getLatitude() != null && arrangementTaxi.getLongitude() != null) {
					if (!arrangementTaxi.getLatitude().equals(0.0) && !arrangementTaxi.getLongitude().equals(0.0)) {
						addMarkerToMap(arrangementTaxi, new LatLng(arrangementTaxi.getLatitude(), arrangementTaxi.getLongitude()));
					}
				}
			}
			GoogleMapUntil.scaleMap(gmapArrangement);
		}
	}
	
	private void addMarkerToMap(ArrangementTaxi mark, LatLng latLng) {
		if (mapmarker == null) {
			mapmarker = new HashMap<ArrangementTaxi, VMarker>();
		}
		VMarker marker = new VMarker();
		marker.setIconImage("./themes/images/ML20.png");
		marker.setAttribute("ArrangementTaxi", mark);
		marker.setPosition(latLng);
		marker.setOpen(true);
		marker.setContent("Mã Bãi: " + mark.getValue() == null ? "" : mark.getValue());
		gmapArrangement.appendChild(marker);
		mapmarker.put(mark, marker);
	}
	
	private void initmap(ArrangementTaxi mark) {
		if(addgmarker != null){
			addgmarker.destroy();
		}
		addgmarker = mapmarker.get(mark);
		if (addgmarker != null) {
			gmapArrangement.setCenter(addgmarker.getLat(), addgmarker.getLng() - 0.002);
			gmapArrangement.setZoom(17);
			addgmarker.setDraggable(true);
			addgmarker.setIconImage("./themes/images/map-object-blue-geo-point-32.png");
		}
	}

	@Override
	public void onEvent(Event events) throws Exception {
			if (events.getTarget().equals(btnThem)) {
				handlerBtnThem();
			} else if (events.getTarget().equals(listboxArrangement)) {
				handlerListBox(events);
			} else if (events.getTarget().equals(detailWindow)) {
				if (events.getName().equals(Events.ON_CLOSE)) {
					btnThem.setDisabled(false);
					ArrangementTaxi model = ((ArrangementTaxi) detailWindow.getModel());
					if(mapmarker != null){
						VMarker marker = mapmarker.get(model);
						model.refresh();
						if (marker != null) {
							marker.setIconImage("./themes/images/ML20.png");
							marker.setPosition(new LatLng(model.getLatitude(),model.getLongitude()));
							marker.setDraggable(false);
							marker.setOpen(true);
						}
					}
				} else if (events.getName().equals(Events.ON_CHANGE)) {
					btnThem.setDisabled(false);
					refresh();
				}
			} else if (events.getTarget().equals(searchcode)) {
				searchcode.setAttribute("datafield", searchcode.getValue());
				executeSearch();
			} else if (events.getTarget().equals(searchAddress)) {
				searchAddress.setAttribute("datafield", searchAddress.getValue());
				executeSearch();
			} else if (events.getTarget().equals(searchagent)) {
				searchagent.setAttribute("datafield", searchagent.getValue());
				executeSearch();
			} else if (events.getTarget().equals(searchname)) {
				searchname.setAttribute("datafield", searchname.getValue());
				executeSearch();
			}
	}
	
	private void executeSearch() {
		Predicate pre = new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
				ArrangementTaxi mar = (ArrangementTaxi) arg0;
				String code = (String) searchcode.getAttribute("datafield");
				String name = (String) searchname.getAttribute("datafield");
				String address = (String) searchAddress.getAttribute("datafield");
				String agent = (String) searchagent.getAttribute("datafield");
				boolean pathcheck1 = true;
				if ((mar.getValue() == null && !code.equals("")) || (mar.getValue() != null
						&& !mar.getValue().toUpperCase().contains(code.toUpperCase()))) {
					pathcheck1 = false;
				}
				boolean pathcheck2 = true;
				if ((mar.getName() == null && !name.equals("")) || (mar.getName() != null
						&& !mar.getName().toUpperCase().contains(name.toUpperCase()))) {
					pathcheck2 = false;
				}
				boolean pathcheck3 = true;
				if ((mar.getAddress() == null && !address.equals("")) || (mar.getAddress() != null
						&& !mar.getAddress().toUpperCase().contains(address.toUpperCase()))) {
					pathcheck2 = false;
				}
				boolean pathcheck4 = true;
				if ((mar.getAgent() == null && !agent.equals("")) || (mar.getAgent() != null
						&& !mar.getAgent().toString().toUpperCase().contains(agent.toUpperCase()))) {
					pathcheck2 = false;
				}

				if (pathcheck1 && pathcheck2 && pathcheck3 && pathcheck4) {
					return true;
				}

				return false;
			}
		};
		@SuppressWarnings("unchecked")
		Collection<ArrangementTaxi> coll = CollectionUtils.select(listmodel, pre);
		List<ArrangementTaxi> lsttmp = new ArrayList<ArrangementTaxi>(coll);
		renderListBox(lsttmp);

	}
	
	private void handlerGmap(Event events) {
		if (events.getName().equals(VMapEvents.ON_VMAP_CLICK)) {
			VMapEvent event = (VMapEvent) events;
			AbstractComponent comp = (AbstractComponent) event.getComponent();
			if (comp instanceof VMarker && !comp.equals(addgmarker)) {
				((VMarker) comp).setOpen(true);
				ArrangementTaxi model = (ArrangementTaxi) ((VMarker) comp).getAttribute("ArrangementTaxi");
				if (model != null) {
					showDetail(model);
					initmap(model);
				}
			} else {
				if(addgmarker != null){
					addgmarker.destroy();
				}
				addgmarker = new VMarker();
				addgmarker.setIconImage("./themes/images/map-object-blue-geo-point-32.png");
				addgmarker.setPosition(event.getPosition());
				addgmarker.setParent(gmapArrangement);
				addgmarker.setDraggable(true);
				gmapArrangement.setCenter(addgmarker.getLat(), addgmarker.getLng() - 0.002);
				gmapArrangement.setZoom(17);
				Address add = new Address();
				add.setLatitude(event.getLat());
				add.setLongitude(event.getLng());
				findaddress.getComponent()
						.setText(MapUtils.convertLatLongToAddrest(event.getLat(), event.getLng()));
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
		} else if (events.getName().equals(VMapEvents.ON_VMARKER_DRAG_END)) {
			VMapEvent event = (VMapEvent) events;
			if (event.getComponent().equals(addgmarker)) {
				findaddress.getComponent()
						.setText(MapUtils.convertLatLongToAddrest(event.getLat(), event.getLng()));
				if(detailWindow != null){
					detailWindow.updateAddress(findaddress.getComponent().getText(),
							new LatLng(addgmarker.getLat(), addgmarker.getLng()));
				}
			} else if (event.getComponent() instanceof VMarker) {
				ArrangementTaxi data = ((ArrangementTaxi) detailWindow.getModel());
				VMarker mar = (VMarker) event.getComponent();
				if (detailWindow != null && detailWindow.isVisible() && mapmarker.get(data).equals(mar)) {
					detailWindow.updateAddress(MapUtils.convertLatLongToAddrest(mar.getLat(), mar.getLng()),
							new LatLng(mar.getLat(), mar.getLng()));
				}
			}

		}
	}
	
	private void handlerListBox(Event events) {
		if (events.getName().equals(Events.ON_SELECT)) {
			ArrangementTaxi model = (ArrangementTaxi) listboxArrangement.getSelectedItem().getValue();
			if (model != null) {
				showDetail(model);
				initmap(model);
				lstNearTaxi = getNearTaxi(model);
				if(lstNearTaxi != null && lstNearTaxi.size() > 0){
					markerCarOnMap(lstNearTaxi);
				}
			}
		}
	}
	
	private void markerCarOnMap(List<VehicleTmp> lst){
		clearVehicleMarker();
		for (int i = 0; i < lst.size(); i++) {
			VehicleTmp taxi = lst.get(i);
			if (taxi.getLatitude() > 0 && taxi.getLongitude() > 0) {
				if(lstMarkerCar == null){
					lstMarkerCar = new ArrayList<VMarker>();
				}
				VMarker markerCar = new VMarker();
				markerCar.setPosition(new LatLng(taxi.getLatitude(),taxi.getLongitude()));
				markerCar.setIconImage("./themes/images/icon_car_32.png");
				markerCar.setContent("ST: " + taxi.getVehicleNumber()  + "  " +  taxi.getDriverName());
				markerCar.setParent(gmapArrangement);
				markerCar.setOpen(true);
				gmapArrangement.appendChild(markerCar);
				lstMarkerCar.add(markerCar);
			}
		}
	}
	
	private void clearVehicleMarker() {
		if (lstMarkerCar != null && lstMarkerCar.size() > 0) {
			for (Iterator<VMarker> it = lstMarkerCar.iterator(); it.hasNext();) {
				VMarker abscomponent = it.next();
				abscomponent.destroy();
			}
			lstMarkerCar.clear();
		}
	}
	
	public static void addNewArrangment(double lat, double lng, String address){
		
	}
	
	private List<VehicleTmp> getNearTaxi(ArrangementTaxi argment){
		List<VehicleTmp> lst = new ArrayList<VehicleTmp>();
		lst = TaxiUtils.getListVehicleAroundPoint(argment.getLongitude(), argment.getLatitude(), EnumCarTypeCommon.ALL.getValue(),-1 ,argment.getRadius(), 0);
		return lst;
	}
	
	private void handlerBtnThem() {
		if (!findaddress.getComponent().getValue().equals("")) {
			try {
				ArrangementTaxi model = ArrangementTaxi.class.newInstance();
				model.setAddress(findaddress.getComponent().getValue());
				model.setLatitude(addgmarker.getLat());
				model.setLongitude(addgmarker.getLng());
				gmapArrangement.setCenter(addgmarker.getLat(), addgmarker.getLng() - 0.002);
				gmapArrangement.setZoom(17);
				showDetail(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Messagebox.show("Click chuột trên bản đồ để lấy vị trí điểm!", "Thông báo", Messagebox.OK,
					Messagebox.INFORMATION);
			isadd = true;
		}

	}

	@Override
	public void onChangeAddress(Address address, int type) {
		if (address.getLatitude() != 0.0 && address.getLongitude() != 0.0) {
			if (addgmarker.getParent() == null) {
				addgmarker.setParent(gmapArrangement);
			}
			addgmarker.setPosition(new LatLng(address.getLatitude(), address.getLongitude()));
			gmapArrangement.setCenter(address.getLatitude(), address.getLongitude());
			gmapArrangement.setZoom(17);
			if (detailWindow != null && detailWindow.isVisible() && (ArrangementTaxi) detailWindow.getModel()!= null && ((ArrangementTaxi) detailWindow.getModel()).getId() == 0) {
				detailWindow.updateAddress(address.getName(),
						new LatLng(address.getLatitude(), address.getLongitude()));
			}
		}
	}
}
