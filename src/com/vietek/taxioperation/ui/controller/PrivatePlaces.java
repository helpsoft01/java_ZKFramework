package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.West;

import com.vietek.taxioperation.controller.PointGroupController;
import com.vietek.taxioperation.controller.PrivatePlaceController;
import com.vietek.taxioperation.model.PointGroup;
import com.vietek.taxioperation.model.PrivatePlace;
import com.vietek.taxioperation.ui.editor.FindAddressEditor;
import com.vietek.taxioperation.ui.editor.FindAddressHandler;
import com.vietek.taxioperation.util.Address;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.MapUtils;
import com.vietek.tracking.ui.model.TreePlace;

public class PrivatePlaces extends Div implements FindAddressHandler, EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Gmaps gmapPrivate;
	private FindAddressEditor findaddress;
	private Gmarker addgmarker;
	private Tree treeplace;
	private Map<Integer, Gmarker> mapmarker;
	private PrivatePlacesDetail detailWindow;
	private PrivatePlace currentModel;

	public PrivatePlaces() {
		this.setHeight("100%");
		Borderlayout borderlayout = new Borderlayout();
		borderlayout.setParent(this);
		West west = new West();
		west.setSize("400px");
		west.setVflex("true");
		west.setParent(borderlayout);

		Center center = new Center();
		center.setParent(borderlayout);
		creatFrmRight(center);
		creatFrmLeft(west);
	}

	private void creatFrmLeft(West parent) {
		Div div = new Div();
		div.setStyle("width:100%; height:100%");
		div.setParent(parent);
		treeplace = new Tree();
		treeplace.setParent(div);
		treeplace.setVflex("true");
		Treecols cols = new Treecols();
		cols.setParent(treeplace);
		Treecol col = new Treecol("Địa điểm riêng");
		col.setWidth("85%");
		col.setParent(cols);
		col = new Treecol();
		col.setWidth("15%");
		col.setParent(cols);
		treeplace.setModel(new PlaceTreeModel(getRootPlace()));
		treeplace.setItemRenderer(new TreePlaceRender());
		treeplace.addEventListener(Events.ON_SELECT, this);
		treeplace.addEventListener(Events.ON_RIGHT_CLICK, this);
		treeplace.addEventListener(Events.ON_SELECT, EVENT_TREE_SELECT);

	}

	private void creatFrmRight(Component parent) {
		Div div = new Div();
		div.setStyle("width:100%; height: 100%");
		div.setZindex(1);
		div.setParent(parent);
		gmapPrivate = new Gmaps();
		gmapPrivate.setParent(div);
		gmapPrivate.setSclass("z-gmap");
		gmapPrivate.setHflex("1");
		gmapPrivate.setVflex("true");
		gmapPrivate.setVersion("3.9");
		gmapPrivate.setShowSmallCtrl(true);
		gmapPrivate.setCenter(new LatLng(20.971715711074314, 465.83903365796465));
		gmapPrivate.setZoom(17);
		gmapPrivate.addEventListener("onMapClick", EVENT_GMAP);
		gmapPrivate.addEventListener("onMapDrop", EVENT_GMAP);
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
		findaddress = new FindAddressEditor("Tìm địa chỉ", gmapPrivate);
		findaddress.getComponent().setButtonVisible(false);
		findaddress.getComponent().setParent(parent);
		findaddress.getComponent().setStyle("margin-left:5px ! important;font-size: 14px ! important");
		findaddress.handler = this;

	}

	@Override
	public void onEvent(Event events) throws Exception {
		if (events.getTarget().equals(treeplace) && events.getName().equals(Events.ON_SELECT)) {
			TreePlace treedata = ((PlaceTreeNode) treeplace.getSelectedItem().getValue()).getData();
			if (treedata instanceof PrivatePlaces) {

			}

		} else if (events.getTarget().equals(detailWindow)) {
			if (events.getName().equals(Events.ON_CLOSE)) {
				PrivatePlace model = (PrivatePlace) detailWindow.getModel();
				Gmarker marker = mapmarker.get(model.getId());
				if (marker != null) {
					marker.setIconImage(model.getGroup().getSrc());
					marker.setDraggingEnabled(false);
					marker.setOpen(false);
				}

			} else if (events.getName().equals(Events.ON_CHANGE)) {
				refresh();
			}
		}
	}

	@Override
	public void onChangeAddress(Address address, int type) {
		if (address.getLatitude() != 0.0 && address.getLongitude() != 0.0) {
			if (addgmarker.getParent() == null) {
				addgmarker.setParent(gmapPrivate);
			}
			addgmarker.setLat(address.getLatitude());
			addgmarker.setLng(address.getLongitude());
			gmapPrivate.setCenter(address.getLatitude(), address.getLongitude());
			gmapPrivate.setZoom(17);
		}

	}

	private PlaceTreeNode getRootPlace() {
		PlaceTreeNode root = null;
		PointGroupController controller = (PointGroupController) ControllerUtils
				.getController(PointGroupController.class);
		List<PointGroup> lstValue = controller.find("from PointGroup");
		List<PlaceTreeNode> lstroot = new ArrayList<PlaceTreeNode>();

		if (lstValue != null) {
			gmapPrivate.getChildren().clear();
			if (mapmarker != null && mapmarker.size() > 0) {
				mapmarker.clear();
			}

			for (PointGroup pointGroup : lstValue) {
				if (pointGroup != null) {
					List<PrivatePlace> lstchil = getListChildren(pointGroup);
					List<PlaceTreeNode> lstnodechil = new ArrayList<PrivatePlaces.PlaceTreeNode>();
					if (lstchil != null) {
						for (PrivatePlace privatePlace : lstchil) {
							PlaceTreeNode place = new PlaceTreeNode(privatePlace);
							addPointToMap(privatePlace);
							lstnodechil.add(place);
						}
					}
					PlaceTreeNode plate = new PlaceTreeNode(pointGroup, lstnodechil);
					lstroot.add(plate);
				}

			}
			root = new PlaceTreeNode(null, lstroot);
		}
		return root;
	}

	private void addPointToMap(PrivatePlace place) {
		if (mapmarker == null) {
			mapmarker = new HashMap<Integer, Gmarker>();
		}
		Gmarker marker = new Gmarker();
		marker.setIconImage(place.getGroup().getSrc());
		marker.setLat(place.getLati());
		marker.setLng(place.getLongi());
		marker.setContent("Mã Bãi:" + place.getPointName());
		marker.setAttribute("PointData", place);
		gmapPrivate.appendChild(marker);
		mapmarker.put(place.getId(), marker);

	}

	private List<PrivatePlace> getListChildren(PointGroup parent) {
		List<PrivatePlace> listresult = null;
		PrivatePlaceController controller = (PrivatePlaceController) ControllerUtils
				.getController(PrivatePlaceController.class);
		listresult = controller.find("From PrivatePlace where group = ? ", parent);
		return listresult;
	}

	private class PlaceTreeModel extends DefaultTreeModel<TreePlace> {

		public PlaceTreeModel(TreeNode<TreePlace> root) {
			super(root);
		}

		private static final long serialVersionUID = 1L;

	}

	private class PlaceTreeNode extends DefaultTreeNode<TreePlace> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public PlaceTreeNode(TreePlace data) {
			super(data);
		}

		public PlaceTreeNode(TreePlace data, boolean nullAsMax) {
			super(data, nullAsMax);
		}

		public PlaceTreeNode(TreePlace arg0, Collection<? extends TreeNode<TreePlace>> arg1, boolean arg2) {
			super(arg0, arg1, arg2);
		}

		public PlaceTreeNode(TreePlace data, Collection<? extends TreeNode<TreePlace>> children) {
			super(data, children);
		}

		public PlaceTreeNode(TreePlace data, TreeNode<TreePlace>[] children) {
			super(data, children);
		}

	}

	private class TreePlaceRender implements TreeitemRenderer<PlaceTreeNode> {

		@Override
		public void render(Treeitem treeitem, PlaceTreeNode group, int arg2) throws Exception {
			TreePlace pgoup = group.getData();
			treeitem.setValue(group);
			Treerow row = new Treerow();
			row.setParent(treeitem);
			Treecell cell = new Treecell();
			cell.setParent(row);
			Image img = new Image(pgoup.getSrc());
			img.setStyle("margin-right: 6px;");
			cell.appendChild(img);
			cell.appendChild(new Label(pgoup.getPlaceName()));
			cell = new Treecell();
			cell.setParent(row);
			if (pgoup instanceof PointGroup) {
				img.setWidth("20px");
				Toolbarbutton btnadd = new Toolbarbutton();
				btnadd.setAttribute("item", treeitem);
				btnadd.addEventListener(Events.ON_CLICK, EVENT_ADD_CLICK);
				btnadd.setImage("./themes/images/Add_16.png");
				cell.appendChild(btnadd);
			} else if (pgoup instanceof PrivatePlace) {
				Toolbarbutton btnedit = new Toolbarbutton();
				btnedit.setAttribute("item", treeitem);
				btnedit.setImage("./themes/images/edit.png");
				btnedit.addEventListener(Events.ON_CLICK, EVENT_EDIT_CLICK);
				cell.appendChild(btnedit);
				Toolbarbutton btndelete = new Toolbarbutton();
				btndelete.setImage("./themes/images/DeleteRed_16.png");
				btndelete.setAttribute("item", treeitem);
				btndelete.addEventListener(Events.ON_CLICK, EVENT_DELETE);
				cell.appendChild(btndelete);
				Gmarker marker = mapmarker.get(((PrivatePlace) pgoup).getId());
				if (marker != null) {
					marker.setAttribute("item", treeitem);
				}
			}

		}

	}

	private PrivatePlacesDetail getDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new PrivatePlacesDetail(currentModel, null);
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

	public void showDetail(PrivatePlace currentModel) {
		if (this.currentModel != null) {
			Gmarker martmp = mapmarker.get(this.currentModel.getId());
			if (martmp != null) {
				martmp.setIconImage(this.currentModel.getGroup().getSrc());
				martmp.setOpen(false);
				martmp.setDraggingEnabled(false);
			}

		}
		this.currentModel = currentModel;
		getDetailWindow().setModel(currentModel);
		getDetailWindow().doOverlapped();
	}

	private void initmap(int id) {
		Gmarker marker = mapmarker.get(id);
		if (marker != null) {
			gmapPrivate.setCenter(marker.getLat(), marker.getLng() - 0.002);
			gmapPrivate.setZoom(17);
			marker.setDraggingEnabled(true);
			marker.setIconImage("./themes/images/map-object-blue-geo-point-32.png");
		}

	}

	private void refresh() {
		gmapPrivate.getChildren().clear();
		if (mapmarker != null && mapmarker.size() > 0) {
			mapmarker.clear();
		}
		treeplace.setModel(new PlaceTreeModel(getRootPlace()));
	}

	private EventListener<Event> EVENT_ADD_CLICK = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			PrivatePlace model = PrivatePlace.class.newInstance();
			Treeitem item = (Treeitem) event.getTarget().getAttribute("item");
			treeplace.setSelectedItem(item);
			PointGroup group = (PointGroup) ((PlaceTreeNode) item.getValue()).getData();
			model.setGroup(group);
			if (findaddress.getComponent().getValue().equals("")) {
				Messagebox.show("Click chuột trên bản đồ để lấy vị trí bãi xe!", "Thông báo",
						new Messagebox.Button[] { Messagebox.Button.OK }, Messagebox.INFORMATION,
						new EventListener<Messagebox.ClickEvent>() {

					@Override
					public void onEvent(Messagebox.ClickEvent event) throws Exception {
						if (event.getButton().equals(Messagebox.Button.OK)) {
							showDetail(model);
						}
					}
				});
			} else {
				model.setAddress(findaddress.getComponent().getValue());
				model.setLati(addgmarker.getLat());
				model.setLongi(addgmarker.getLng());
				showDetail(model);
			}

		}
	};

	private EventListener<Event> EVENT_DELETE = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			Treeitem item = (Treeitem) event.getTarget().getAttribute("item");
			try {
				currentModel = (PrivatePlace) (((PlaceTreeNode) item.getValue()).getData());
				Messagebox.show("Bạn có muốn chắc chắn xoá bản ghi ?", "Xác nhận xóa",
						new Messagebox.Button[] { Messagebox.Button.OK, Messagebox.Button.CANCEL }, Messagebox.QUESTION,
						new EventListener<Messagebox.ClickEvent>() {
					@Override
					public void onEvent(ClickEvent event) throws Exception {
						try {
							if (event.getButton() != null && event.getButton().equals(Messagebox.Button.OK)) {
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
	};

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

	private EventListener<Event> EVENT_EDIT_CLICK = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			Treeitem item = (Treeitem) event.getTarget().getAttribute("item");
			treeplace.setSelectedItem(item);
			TreePlace data = ((PlaceTreeNode) item.getValue()).getData();
			if (data instanceof PrivatePlace) {
				PrivatePlace place = (PrivatePlace) data;
				showDetail(place);
				initmap(place.getId());
			}

		}
	};
	private EventListener<Event> EVENT_TREE_SELECT = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			Treeitem item = treeplace.getSelectedItem();
			TreePlace data = ((PlaceTreeNode) item.getValue()).getData();
			if (data instanceof PrivatePlace) {
				PrivatePlace place = (PrivatePlace) data;
				showDetail(place);
				initmap(place.getId());
			}

		}
	};
	private EventListener<Event> EVENT_GMAP = new EventListener<Event>() {

		@SuppressWarnings("deprecation")
		@Override
		public void onEvent(Event events) throws Exception {
			if (events.getName().equals("onMapClick")) {
				MapMouseEvent event = (MapMouseEvent) events;
				AbstractComponent comp = (AbstractComponent) event.getReference();
				if (comp instanceof Gmarker && !comp.equals(addgmarker)) {
					((Gmarker) comp).setOpen(true);
					PrivatePlace place = (PrivatePlace) ((Gmarker) comp).getAttribute("PointData");
					if (place != null) {
						gmapPrivate.setCenter(((Gmarker) comp).getLat(), ((Gmarker) comp).getLng() - 0.002);
						gmapPrivate.setZoom(17);
						((Gmarker) comp).setDraggingEnabled(true);
						((Gmarker) comp).setIconImage("./themes/images/map-object-blue-geo-point-32.png");
						showDetail(place);
					}
				} else {
					if (addgmarker.getParent() == null) {
						addgmarker.setParent(gmapPrivate);
					}
					addgmarker.setLat(event.getLat());
					addgmarker.setLng(event.getLng());
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

				}
			} else if (events.getName().equals("onMapDrop")) {
				MapDropEvent event = (MapDropEvent) events;
				if (event.getDragged().equals(addgmarker)) {
					findaddress.getComponent()
							.setText(MapUtils.convertLatLongToAddrest(event.getLat(), event.getLng()));
					if (detailWindow != null && detailWindow.isVisible()) {
						detailWindow.updateAddress(findaddress.getComponent().getText(),
								new LatLng(addgmarker.getLat(), addgmarker.getLng()));
					}

				} else if (event.getDragged() instanceof Gmarker) {
					int id = ((PrivatePlace) detailWindow.getModel()).getId();
					Gmarker mar = (Gmarker) event.getDragged();
					if (detailWindow != null && detailWindow.isVisible() && mapmarker.get(id).equals(mar)) {
						detailWindow.updateAddress(MapUtils.convertLatLongToAddrest(mar.getLat(), mar.getLng()),
								new LatLng(mar.getLat(), mar.getLng()));
					}
				}

			}

		}

	};
}