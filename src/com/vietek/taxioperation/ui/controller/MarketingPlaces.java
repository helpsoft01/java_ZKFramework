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

import com.vietek.taxioperation.model.MarketingPlace;
import com.vietek.taxioperation.ui.editor.FindAddressEditor;
import com.vietek.taxioperation.ui.editor.FindAddressHandler;
import com.vietek.taxioperation.util.Address;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.MapUtils;

/**
 *
 * @author VuD
 */
public class MarketingPlaces extends Div implements Serializable, FindAddressHandler, EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Gmaps gmapMarketing;
	private Listbox listboxMarketing;
	private List<MarketingPlace> listmodel;
	private Button btnThem;
	private MarketingPlacesDetail detailWindow;
	private MarketingPlace currentModel;
	private HashMap<MarketingPlace, Gmarker> mapmarker;
	private Gmarker addgmarker;
	private FindAddressEditor findaddress;
	private Textbox searchcode;
	private Textbox searchname;
	private Textbox searchAddress;
	private Textbox searchagent;
	private boolean isadd = false;

	public MarketingPlaces() {
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
		gmapMarketing = new Gmaps();
		gmapMarketing.setParent(div);
		gmapMarketing.setSclass("z-gmap");
		gmapMarketing.setHflex("1");
		gmapMarketing.setVflex("true");
		gmapMarketing.setVersion("3.9");
		gmapMarketing.setShowSmallCtrl(true);
		gmapMarketing.setCenter(new LatLng(20.971715711074314, 465.83903365796465));
		gmapMarketing.setZoom(17);
		gmapMarketing.addEventListener("onMapClick", this);
		gmapMarketing.addEventListener("onMapDrop", this);
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
		Div div = new Div();
		div.setParent(parent);
		div.setSclass("z-panel-layout-history");
		Image img = new Image("./themes/images/searchpak.png");
		img.setParent(parent);
		findaddress = new FindAddressEditor("Tìm địa chỉ", gmapMarketing);
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
		toolbar.appendChild(new Label("Danh sách điểm tiếp thị"));
		toolbar.setSclass("z-toolbar-history");
		toolbar.setAlign("center");
		toolbar.setParent(panelchildren);
		listboxMarketing = new Listbox();
		listboxMarketing.setEmptyMessage("Không có dữ liệu");
		listboxMarketing.addEventListener(Events.ON_SELECT, this);
		listboxMarketing.setSizedByContent(true);
		listboxMarketing.setSpan(true);
		listboxMarketing.setMold("paging");
		listboxMarketing.setAutopaging(true);
		listboxMarketing.setPagingPosition("both");
		listboxMarketing.addEventListener("onPaging", this);
		listboxMarketing.setHeight("450px");
		listboxMarketing.setParent(panelchildren);
		Frozen frozen = new Frozen();
		frozen.setStart(0);
		frozen.setColumns(2);
		frozen.setParent(listboxMarketing);
		Listhead head = new Listhead();
		head.setParent(listboxMarketing);
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
		header = new Listheader("Loai Diem");
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
		auxs.setParent(listboxMarketing);
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

		listboxMarketing.setItemRenderer(new ListitemRenderer<MarketingPlace>() {

			@Override
			public void render(Listitem items, MarketingPlace data, int index) throws Exception {
				items.setValue(data);
				Label lb = new Label();
				lb.setValue(index + 1 + "");
				Listcell listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getPavilionCode());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getPavilionName());
				listcell = new Listcell();
				listcell.setStyle("text-align: left");
				listcell.appendChild(lb);
				listcell.setParent(items);
				lb = new Label();
				lb.setValue(data.getPaviliontypename());
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
					lb.setValue(data.getAgent().toString());
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
				lb.setValue(data.getQuota() + "");
				listcell = new Listcell();
				listcell.appendChild(lb);
				listcell.setParent(items);
				Toolbarbutton btndel = new Toolbarbutton();
				btndel.setImage("./themes/images/DeleteRed_16.png");
				btndel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						listboxMarketing.setSelectedIndex(index);
						try {
							currentModel = (MarketingPlace) listboxMarketing.getSelectedItem().getValue();
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
						listboxMarketing.setSelectedIndex(index);
						try {
							MarketingPlace model = (MarketingPlace) listboxMarketing.getSelectedItem().getValue();
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
		query = session.createQuery("from MarketingPlace");
		try {
			listmodel = query.list();
			renderListBox(listmodel);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	private void renderListBox(List<MarketingPlace> lstmodel) {
		if (lstmodel != null) {
			if (mapmarker != null && mapmarker.size() > 0) {
				mapmarker.clear();
			}
			gmapMarketing.getChildren().clear();
			listboxMarketing.setModel(new ListModelList<MarketingPlace>(lstmodel));
			for (MarketingPlace MarketingPlace : lstmodel) {

				if (MarketingPlace.getLati() != null && MarketingPlace.getLongi() != null) {
					if (!MarketingPlace.getLati().equals(0.0) && !MarketingPlace.getLongi().equals(0.0)) {
						addMarkerToMap(MarketingPlace, new LatLng(MarketingPlace.getLati(), MarketingPlace.getLongi()));
					}
				}
			}
			MapUtils.scaleMap(gmapMarketing);
		}
	}

	private void addMarkerToMap(MarketingPlace mark, LatLng latLng) {
		if (mapmarker == null) {
			mapmarker = new HashMap<MarketingPlace, Gmarker>();
		}
		Gmarker marker = new Gmarker(mark.getPavilionCode());
		marker.setIconImage("./themes/images/ML20.png");
		// marker.setId(String.valueOf(listmodel.indexOf(park)));
		marker.setAttribute("Marketing", mark);
		marker.setLat(latLng.getLatitude());
		marker.setLng(latLng.getLongitude());
		marker.setContent("Mã Bãi:" + mark.getPavilionCode());
		gmapMarketing.appendChild(marker);
		mapmarker.put(mark, marker);
	}

	private MarketingPlacesDetail getDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new MarketingPlacesDetail(currentModel, null);
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

	public void showDetail(MarketingPlace currentModel) {
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

		} else if (events.getTarget().equals(listboxMarketing)) {
			handlerListBox(events);

		} else if (events.getTarget().equals(gmapMarketing)) {
			handlerGmap(events);
		} else if (events.getTarget().equals(detailWindow)) {
			if (events.getName().equals(Events.ON_CLOSE)) {
				btnThem.setDisabled(false);
				MarketingPlace model = ((MarketingPlace) detailWindow.getModel());
				Gmarker marker = mapmarker.get(model);
				model.refresh();
				if (marker != null) {
					marker.setIconImage("./themes/images/ML20.png");
					marker.setLat(model.getLati());
					marker.setLng(model.getLongi());
					marker.setDraggingEnabled(false);
					marker.setOpen(false);
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

	@SuppressWarnings("unchecked")
	private void executeSearch() {
		Predicate pre = new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
				MarketingPlace mar = (MarketingPlace) arg0;
				String code = (String) searchcode.getAttribute("datafield");
				String name = (String) searchname.getAttribute("datafield");
				String address = (String) searchAddress.getAttribute("datafield");
				String agent = (String) searchagent.getAttribute("datafield");
				boolean pathcheck1 = true;
				if ((mar.getPavilionCode() == null && !code.equals("")) || (mar.getPavilionCode() != null
						&& !mar.getPavilionCode().toUpperCase().contains(code.toUpperCase()))) {
					pathcheck1 = false;
				}
				boolean pathcheck2 = true;
				if ((mar.getPavilionName() == null && !name.equals("")) || (mar.getPavilionName() != null
						&& !mar.getPavilionName().toUpperCase().contains(name.toUpperCase()))) {
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
		Collection<MarketingPlace> coll = CollectionUtils.select(listmodel, pre);
		List<MarketingPlace> lsttmp = new ArrayList<MarketingPlace>(coll);
		renderListBox(lsttmp);

	}

	@SuppressWarnings("deprecation")
	private void handlerGmap(Event events) {
		if (events.getName().equals("onMapClick")) {
			MapMouseEvent event = (MapMouseEvent) events;
			AbstractComponent comp = (AbstractComponent) event.getReference();
			if (comp instanceof Gmarker && !comp.equals(addgmarker)) {
				((Gmarker) comp).setOpen(true);
				MarketingPlace model = (MarketingPlace) ((Gmarker) comp).getAttribute("Marketing");
				if (model != null) {
					showDetail(model);
					initmap(model);
				}
			} else {
				if (addgmarker.getParent() == null) {
					addgmarker.setParent(gmapMarketing);
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
				detailWindow.updateAddress(findaddress.getComponent().getText(),
						new LatLng(addgmarker.getLat(), addgmarker.getLng()));
			} else if (event.getDragged() instanceof Gmarker) {
				MarketingPlace data = ((MarketingPlace) detailWindow.getModel());
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
				MarketingPlace model = MarketingPlace.class.newInstance();
				model.setAddress(findaddress.getComponent().getValue());
				model.setLati(addgmarker.getLat());
				model.setLongi(addgmarker.getLng());
				gmapMarketing.setCenter(addgmarker.getLat(), addgmarker.getLng() - 0.002);
				gmapMarketing.setZoom(17);
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
			MarketingPlace model = (MarketingPlace) listboxMarketing.getSelectedItem().getValue();
			if (model != null) {
				showDetail(model);
				initmap(model);
			}
		}

	}

	private void initmap(MarketingPlace mark) {
		Gmarker marker = mapmarker.get(mark);
		if (marker != null) {
			gmapMarketing.setCenter(marker.getLat(), marker.getLng() - 0.002);
			gmapMarketing.setZoom(17);
			marker.setDraggingEnabled(true);
			marker.setIconImage("./themes/images/map-object-blue-geo-point-32.png");
		}
	}

	public void refresh() {
		gmapMarketing.getChildren().clear();
		if (detailWindow.isVisible()) {
			detailWindow.detach();
		}
		loadData();
	}

	public Gmaps getgmapMarketing() {
		return gmapMarketing;
	}

	public void setgmapMarketing(Gmaps gmapMarketing) {
		this.gmapMarketing = gmapMarketing;
	}

	public Listbox getlistboxMarketing() {
		return listboxMarketing;
	}

	public void setlistboxMarketing(Listbox listboxMarketing) {
		this.listboxMarketing = listboxMarketing;
	}

	@Override
	public void onChangeAddress(Address address, int type) {

		if (address.getLatitude() != 0.0 && address.getLongitude() != 0.0) {
			if (addgmarker.getParent() == null) {
				addgmarker.setParent(gmapMarketing);
			}
			addgmarker.setLat(address.getLatitude());
			addgmarker.setLng(address.getLongitude());
			gmapMarketing.setCenter(address.getLatitude(), address.getLongitude());
			gmapMarketing.setZoom(17);
			if (detailWindow.isVisible() && ((MarketingPlace) detailWindow.getModel()).getId() == 0) {
				detailWindow.updateAddress(address.getName(),
						new LatLng(address.getLatitude(), address.getLongitude()));
			}
		}
	}

}