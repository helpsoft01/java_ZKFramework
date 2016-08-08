package com.vietek.taxioperation.ui.controller;

import java.util.List;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.event.MapDropEvent;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
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
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.ChannelTmsController;
import com.vietek.taxioperation.googlemapSearch.SearchGooglePlaces;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.SwitchboardTMS;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.MapUtils;

public class ChannelTmssDetail extends BasicDetailWindow {

	/**
	 * @author VANMANH289VN
	 */
	private static final long serialVersionUID = 1L;
	private Gmarker gmarker;
	private Gmaps gmap;
	private Combobox comboxsearch;
	private Doublebox dblati;
	private Doublebox dblongi;
	private Desktop desktop = Executions.getCurrent().getDesktop();
	private ChannelTms modeltmp;
	private boolean check = false;

	public ChannelTmssDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Kênh điều hành");
		this.setWidth("900px");

	}

	@Override
	public void setModel(AbstractModel model) {
		modeltmp = (ChannelTms) model;
		comboxsearch.setText("");
		if (modeltmp.getLatitude() != null & modeltmp.getLongitude() != null) {
			gmap.panTo(modeltmp.getLatitude(), modeltmp.getLongitude());
			String address = MapUtils.convertLatLongToAddrest(modeltmp.getLatitude(), modeltmp.getLongitude());
			comboxsearch.setText(address);

			gmarker.setLat(gmap.getLat());
			gmarker.setLng(gmap.getLng());
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
		grid.setWidth("360px");
		Columns cols = new Columns();
		cols.setParent(grid);
		Column col = new Column();
		col.setParent(cols);
		col.setHflex("20%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("70%");
		Rows rows = new Rows();
		rows.setParent(grid);
		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã kênh"));
		Label lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		Editor editor = this.getMapEditor().get("value");
		Textbox textbox = (Textbox) editor.getComponent();
		row.appendChild(textbox);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên kênh"));
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		editor = this.getMapEditor().get("name");
		textbox = (Textbox) editor.getComponent();
		row.appendChild(textbox);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tổng đài"));
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		editor = this.getMapEditor().get("switchboardtms");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kinh độ"));
		row.appendChild(new Label(""));
		editor = this.getMapEditor().get("longitude");
		dblongi = (Doublebox) editor.getComponent();
		row.appendChild(dblongi);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Vĩ độ"));
		row.appendChild(new Label(""));
		editor = this.getMapEditor().get("latitude");
		dblati = (Doublebox) editor.getComponent();
		row.appendChild(dblati);
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

		grid = new Grid();
		cols = new Columns();
		cols.setParent(grid);
		col = new Column();
		col.setParent(cols);
		rows = new Rows();
		rows.setParent(grid);
		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		gmap = new Gmaps();
		gmap.setStyle("width:500px; height:400px");
		gmap.setVersion("3.9");
		gmap.setShowSmallCtrl(true);
		gmap.setZoom(17);
		gmarker = new Gmarker();
		gmarker.setLat(gmap.getLat());
		gmarker.setLng(gmap.getLng());
		gmarker.setDraggingEnabled(true);
		gmap.appendChild(gmarker);
		gmap.addEventListener("onMapClick", new EventListener<Event>() {
			@SuppressWarnings("deprecation")
			@Override
			public void onEvent(Event events) throws Exception {

				MapMouseEvent event = (MapMouseEvent) events;
				gmarker.setLat(event.getLat());
				gmarker.setLng(event.getLng());
				dblati.setValue(gmarker.getLat());
				dblongi.setValue(gmarker.getLng());
				gmap.panTo(gmarker.getLat(), gmarker.getLng());
				comboxsearch.setText(MapUtils.convertLatLongToAddrest(gmarker.getLat(), gmarker.getLng()));

			}
		});
		gmap.addEventListener("onMapDrop", new EventListener<Event>() {
			@SuppressWarnings("deprecation")
			@Override
			public void onEvent(Event events) throws Exception {

				MapDropEvent event = (MapDropEvent) events;
				gmarker.setLat(event.getLat());
				gmarker.setLng(event.getLng());
				dblati.setValue(gmarker.getLat());
				dblongi.setValue(gmarker.getLng());
				gmap.panTo(gmarker.getLat(), gmarker.getLng());

				comboxsearch.setText(MapUtils.convertLatLongToAddrest(gmarker.getLat(), gmarker.getLng()));
			}
		});

		cell.appendChild(gmap);
		row = new Row();
		row.setParent(rows);
		comboxsearch = new Combobox();
		comboxsearch.setText("");
		comboxsearch.setWidth("500px");
		comboxsearch.setPlaceholder("Nhập địa chỉ");
		// comboxsearch.setAutocomplete(true);
		comboxsearch.setButtonVisible(false);
		comboxsearch.addEventListener("onChanging", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				if (check) {
					check = false;
					return;
				}
				InputEvent event = (InputEvent) arg0;
				String datasearch = event.getValue();
				SearchGooglePlaces m1 = new SearchGooglePlaces(comboxsearch, datasearch, desktop);
				Thread t1 = new Thread(m1);
				t1.setPriority(Thread.MAX_PRIORITY);
				t1.start();

			}
		});
		comboxsearch.addEventListener("onChange", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				InputEvent event = (InputEvent) arg0;
				LatLng latlng = MapUtils.convertAddresstoLatLng(event.getValue());
				if (latlng != null) {
					gmarker.setLat(latlng.lat);
					gmarker.setLng(latlng.lng);
					dblati.setValue(gmarker.getLat());
					dblongi.setValue(gmarker.getLng());
					gmap.panTo(gmarker.getLat(), gmarker.getLng());
				}
			}
		});
		comboxsearch.addEventListener("onSelect", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				check = true;
			}

		});

		row.appendChild(comboxsearch);

		hbox.appendChild(grid);
	}

	@Override
	public void onEvent(Event event) throws Exception {
		// TODO Auto-generated method stub

		if (dblati.getValue() > 0 && dblongi.getValue() > 0) {
			modeltmp.setLatitude(dblati.getValue());
			modeltmp.setLongitude(dblongi.getValue());
		}
		String textvalue = modeltmp.getValue();
		if (StringUtils.isHasWhiteSpaceBeginEnd(textvalue)) {

			modeltmp.setValue(textvalue.trim());
			this.setModel(modeltmp);
		}
		if (event.getTarget().equals(this.getBtn_save())) {
			if (StringUtils.isEmpty(validateInput(modeltmp))) {
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

	private String validateInput(ChannelTms model) {
		StringBuilder msg = new StringBuilder("");
		int idchannelexist = -1;

		// validate code
		if (StringUtils.isEmpty(model.getValue())) {
			msg.append(CommonDefine.Channel.CODE_CHANNEL).append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		} else {
			if (StringUtils.isHasSpecialChar(model.getValue())) {
				msg.append(CommonDefine.Channel.CODE_CHANNEL).append(CommonDefine.ERRORS_STRING_IS_HAS_SPECIAL);
			}
			if (!StringUtils.checkMaxLength(model.getValue(), CommonDefine.CODE_MAX_LENGTH)) {
				msg.append(CommonDefine.Channel.CODE_CHANNEL).append(CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
			}
		}

		// validate name
		if (StringUtils.isEmpty(model.getName())) {
			msg.append(CommonDefine.Channel.NAME_CHANNEL).append(CommonDefine.ERRORS_STRING_IS_EMPTY);

		} else {

			if (!StringUtils.checkMaxLength(model.getValue(), CommonDefine.NAME_MAX_LENGTH)) {
				msg.append(CommonDefine.Channel.NAME_CHANNEL).append(CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
			}
		}
		// validate SwitchBoard
		if (model.getSwitchboardtms() == null) {
			msg.append(CommonDefine.Channel.CODE_SWITCHBOARD).append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		} else {
			idchannelexist = checkSwitchboardByName(model.getName(), model.getSwitchboardtms().getId());
		}
		// In case save

		if (model.getId() <= 0) {
			if (getChannelTMSByValue(model.getValue()) != null) { // check exist
																	// code
				msg.append(CommonDefine.Channel.CODE_CHANNEL).append(CommonDefine.ERRORS_STRING_IS_EXIST);
			}

			if (idchannelexist >= 0 && idchannelexist != model.getId())
				msg.append(CommonDefine.Channel.NAME_CHANNEL).append(CommonDefine.ERRORS_STRING_IS_EXIST);

		} else { // In case edit
			ChannelTms tms = getChannelTMSByValue(model.getValue());
			if (tms != null && tms.getId() != model.getId())
				msg.append(CommonDefine.Channel.CODE_CHANNEL).append(CommonDefine.ERRORS_STRING_IS_EXIST);

			if (idchannelexist >= 0 && idchannelexist != model.getId())
				msg.append(CommonDefine.Channel.NAME_CHANNEL).append(CommonDefine.ERRORS_STRING_IS_EXIST);
		}

		return msg.toString();
	}

	// get model from value
	private ChannelTms getChannelTMSByValue(String value) {
		ChannelTmsController controller = (ChannelTmsController) ControllerUtils
				.getController(ChannelTmsController.class);
		String sql = "from ChannelTms where value = ?";
		List<ChannelTms> lstvalue = controller.find(sql, value);
		if (lstvalue == null || lstvalue.size() > 0)
			return (ChannelTms) lstvalue.get(0);
		else
			return null;
	}

	// kiem tra su ton tai cua tong dai
	private int checkSwitchboardByName(String name, int idswitch) {
		int resultidchannel = -1;
		ChannelTmsController controller = (ChannelTmsController) ControllerUtils
				.getController(ChannelTmsController.class);
		String sql = "from ChannelTms where name = ?";
		List<ChannelTms> lstvalue = controller.find(sql, name);
		if (lstvalue == null || lstvalue.size() > 0) {
			for (ChannelTms channelTms : lstvalue) {
				SwitchboardTMS swp = channelTms.getSwitchboardtms();
				if (swp.getId() == idswitch) {
					resultidchannel = channelTms.getId();
					return resultidchannel;
				}
			}

		}
		return resultidchannel;
	}

}
