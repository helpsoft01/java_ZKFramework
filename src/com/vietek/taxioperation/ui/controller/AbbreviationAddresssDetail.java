package com.vietek.taxioperation.ui.controller;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Splitter;
import org.zkoss.zul.Textbox;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.AgentController;
import com.vietek.taxioperation.googlemapSearch.SearchGooglePlaces;
import com.vietek.taxioperation.model.AbbreviationAddress;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvent;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvents;
import com.vietek.taxioperation.ui.controller.vmap.VMaps;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;
import com.vietek.taxioperation.ui.editor.DoubleEditor;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.MapUtils;

/**
 * 
 * @author hung
 *
 */
public class AbbreviationAddresssDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Desktop desktop = Executions.getCurrent().getDesktop();
	private VMarker gmarker;
	private VMaps gmap;
	private Combobox comboxsearch;
	private Doublebox dblati;
	private Doublebox dblongi;
	private Textbox txtaddres;
	private Combobox comboxagent;
	private Grid grid2;
	private AbbreviationAddress modeltmp;

	public AbbreviationAddresssDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setClosable(false);
		setTitle("Chi tiết bảng địa chỉ viết tắt");
		this.setWidth("1000px");
		gmap.addEventListener(VMapEvents.ON_VMAP_CLICK, MAP_CLICK_EVENT);
		gmap.addEventListener(VMapEvents.ON_VMARKER_DRAG_END, MAP_DROP_EVENT);
		comboxsearch.addEventListener(Events.ON_OK, SEARCHBOX_ENTER_EVENT);
		comboxsearch.addEventListener(Events.ON_BLUR, SEARCHBOX_BLUR_EVENT);
		comboxagent.addEventListener(Events.ON_CHANGE, AGENT_SELECT_EVENT);
	}

	@Override
	public void setModel(AbstractModel model) {
		modeltmp = (AbbreviationAddress) model;
		comboxsearch.setText("");
		if (modeltmp.getLati() != null & modeltmp.getLati() != null) {
			if (grid2 != null) {
				Row r = (Row) grid2.getRows().getChildren().get(0);
				Cell c = (Cell) r.getFirstChild();
				gmap.setParent(c);
			}

			gmap.panTo(modeltmp.getLati(), modeltmp.getLongi());
			String address = modeltmp.getDescription();
			comboxsearch.setText(address);
			gmap.setZoom(17);
			gmarker = new VMarker();
			gmarker.setPosition(gmap.getCenter());
			gmarker.setDraggable(true);
			gmap.appendChild(gmarker);
		} else {
			gmap.panTo(20.9715796, 105.8384714);
		}

		super.setModel(model);
	}

	@Override
	public void createForm() {
		Hbox hbox = new Hbox();
		hbox.setParent(this);
		Grid grid = new Grid();
		grid.setWidth("400px");
		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setWidth("100px");
		col = new Column();
		col.setParent(cols);
		col.setWidth("20px");
		col = new Column();
		col.setParent(cols);
		col.setWidth("280px");
		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Địa chỉ viết tắt"));
		Label lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		Editor editor = getMapEditor().get("value");
		Textbox cp = (Textbox) editor.getComponent();
		cp.setMultiline(true);
		cp.setStyle("Height:15px");
		row.appendChild(cp);

		row = new Row();
		row.setParent(rows);
		lb = new Label("Địa chỉ đầy đủ");
		row.appendChild(lb);
		row.appendChild(new Label(""));
		editor = getMapEditor().get("description");
		txtaddres = new Textbox();
		txtaddres = (Textbox) editor.getComponent();
		row.appendChild(txtaddres);

		row = new Row();
		row.setParent(rows);
		lb = new Label("Chi nhánh");
		row.appendChild(lb);
		row.appendChild(new Label(""));
		editor = getMapEditor().get("agent");
		comboxagent = new Combobox();
		comboxagent = (Combobox) editor.getComponent();
		comboxagent.setSclass("combobox_rider_state");
		row.appendChild(comboxagent);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kinh độ"));
		row.appendChild(new Label(""));
		editor = (DoubleEditor) this.getMapEditor().get("longi");
		dblongi = (Doublebox) editor.getComponent();
		row.appendChild(dblongi);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Vĩ độ"));
		row.appendChild(new Label(""));
		editor = (DoubleEditor) this.getMapEditor().get("lati");
		dblati = (Doublebox) editor.getComponent();
		row.appendChild(dblati);
		row = new Row();
		row.setParent(rows);
		lb = new Label("Mô tả");
		row.appendChild(lb);
		row.appendChild(new Label(""));
		editor = getMapEditor().get("note");
		Textbox textbox = new Textbox();
		textbox = (Textbox) editor.getComponent();
		textbox.setHeight("50px");
		textbox.setMultiline(true);
		row.appendChild(textbox);

		row = new Row();
		row.setParent(rows);
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES));
		cell.setStyle("color:red;text-align:center;");
		hbox.appendChild(grid);
		Splitter spliter = new Splitter();
		spliter.setCollapse("before");
		hbox.appendChild(spliter);

		grid2 = new Grid();
		cols = new Columns();
		cols.setParent(grid2);
		col = new Column();
		col.setParent(cols);
		rows = new Rows();
		rows.setParent(grid2);
		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		gmap = new VMaps(cell);
		gmap.setWidth("640px");
		gmap.setHeight("480px");
		gmap.setZoom(17);
		gmarker = new VMarker();
		gmarker.setPosition(gmap.getCenter());
		gmarker.setDraggable(true);
		gmap.appendChild(gmarker);
		row = new Row();
		row.setParent(rows);
		comboxsearch = new Combobox();
		comboxsearch.setText("");
		comboxsearch.setWidth("100%");
		comboxsearch.setPlaceholder("Nhập địa chỉ");
		comboxsearch.setAutocomplete(false);
		comboxsearch.setButtonVisible(false);

		row.appendChild(comboxsearch);
		hbox.appendChild(grid2);

	}

	private EventListener<Event> MAP_CLICK_EVENT = new EventListener<Event>() {
		@Override
		public void onEvent(Event events) throws Exception {
			VMapEvent event = (VMapEvent) events;
			gmarker.setPosition(event.getPosition());
			dblati.setValue(gmarker.getLat());
			dblongi.setValue(gmarker.getLng());
			gmap.panTo(gmarker.getLat(), gmarker.getLng());
			comboxsearch.setText(MapUtils.convertLatLongToAddrest(gmarker.getLat(), gmarker.getLng()));
			txtaddres.setText(comboxsearch.getValue());
		}
	};
	private EventListener<Event> MAP_DROP_EVENT = new EventListener<Event>() {
		@Override
		public void onEvent(Event events) throws Exception {

			VMapEvent event = (VMapEvent) events;
			gmarker.setPosition(event.getPosition());
			dblati.setValue(gmarker.getLat());
			dblongi.setValue(gmarker.getLng());
			gmap.panTo(gmarker.getLat(), gmarker.getLng());

			comboxsearch.setText(MapUtils.convertLatLongToAddrest(gmarker.getLat(), gmarker.getLng()));
			txtaddres.setText(comboxsearch.getValue());
		}
	};

	private EventListener<Event> SEARCHBOX_ENTER_EVENT = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			try {
				String strAddress = comboxsearch.getValue();
				Thread thread = null;
				SearchGooglePlaces searchThread = new SearchGooglePlaces(comboxsearch, strAddress, desktop);
				thread = new Thread(searchThread);
				thread.setPriority(Thread.MAX_PRIORITY);
				thread.start();
			} catch (Exception e) {
				AppLogger.logDebug.error("SEARCHBOX_CHANGED_EVENT", e);
			}
		}
	};

	private EventListener<Event> AGENT_SELECT_EVENT = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			try {
				Agent agent = comboxagent.getSelectedItem().getValue();
				AgentController agentController = (AgentController) ControllerUtils
						.getController(AgentController.class);
				SysCompany company = (SysCompany) agentController.find("From Agent Where id = " + agent.getId()).get(0)
						.getCompany();
				modeltmp.setCompany(company);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	private EventListener<Event> SEARCHBOX_BLUR_EVENT = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			String strAddress = comboxsearch.getValue();
			LatLng latlng = MapUtils.convertAddresstoLatLng(strAddress);
			if (latlng != null) {
				gmarker.setPosition(latlng);
				// gmarker.setLat(latlng.getLatitude());
				// gmarker.setLng(latlng.getLongitude());
				gmarker.setPosition(latlng);
				dblati.setValue(gmarker.getLat());
				dblongi.setValue(gmarker.getLng());
				gmap.panTo(gmarker.getLat(), gmarker.getLng());
				txtaddres.setText(comboxsearch.getValue());
			}
		}
	};

	@Override
	public void onEvent(Event event) throws Exception {
		String textvalue = modeltmp.getValue();
		if (dblati.getValue() > 0 && dblongi.getValue() > 0 & txtaddres.getValue() != null) {
			modeltmp.setLati(dblati.getValue());
			modeltmp.setLongi(dblongi.getValue());
			modeltmp.setDescription(txtaddres.getValue());
		}
		if (StringUtils.isHasWhiteSpaceBeginEnd(textvalue)) {
			modeltmp.setValue(textvalue.trim());
			this.setModel(modeltmp);
		}
		if (event.getTarget().equals(this.getBtn_save())) {
			if (StringUtils.isEmpty(validateInput(modeltmp))) {
				this.setModel(modeltmp);
				super.handleSaveEvent();
			} else {
				Env.getHomePage().showNotification(validateInput(modeltmp), Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
		} else if (event.getTarget().equals(this.getBtn_cancel())
				|| (event.getName().equals(Events.ON_CANCEL) && event.getTarget().equals(this))) {
			this.setVisible(false);
			Env.getHomePage().showNotification("Bỏ qua thay đổi!", Clients.NOTIFICATION_TYPE_INFO);
			this.getListWindow().refresh();
		}

	}

	private String validateInput(AbbreviationAddress model) {
		StringBuilder msg = new StringBuilder("");

		// validate code
		if (StringUtils.isEmpty(model.getValue())) {
			msg.append(CommonDefine.AbbreviationAddress.CODE_ABB_ADDRESS).append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		} else {
			if (!StringUtils.checkMaxLength(model.getValue(), CommonDefine.CODE_MAX_LENGTH)) {
				msg.append(CommonDefine.AbbreviationAddress.CODE_ABB_ADDRESS)
						.append(CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
			}
		}

		return msg.toString();
	}

}