package com.vietek.taxioperation.ui.controller;

import org.zkoss.gmaps.LatLng;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.ParkingArea;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.M2OEditor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class ParkingAreaDetail extends BasicDetailWindow {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Editor editoradd;

	public ParkingAreaDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết bãi giao ca");
		this.setMaximizable(false);
		this.setWidth("500px");
	}

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("20%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("5%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("75%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã bãi giao ca "));
		Editor editor = this.getMapEditor().get("value");

		Cell cell = new Cell();
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên bãi"));
		editor = this.getMapEditor().get("ParkingName");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Địa chỉ"));
		editoradd = this.getMapEditor().get("Address");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editoradd.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Đội xe"));
		editor = this.getMapEditor().get("taxigroup");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setColspan(3);
		cell.setParent(row);
		cell.appendChild(new Label("Bán kính"));
		editor = this.getMapEditor().get("Radius");
		((Intbox) editor.getComponent()).setStyle("width: 80px;margin-left: 25px;margin-right: 5px;");
		// ((Intbox)editor.getComponent()).setWidth("100px");
		cell.appendChild(editor.getComponent());
		Label lb = new Label(" m");
		lb.setStyle("margin-right:20px");
		cell.appendChild(lb);
		cell.appendChild(new Label("Lượng xe"));
		editor = this.getMapEditor().get("Quota");
		((Intbox) editor.getComponent()).setStyle("width: 80px;margin-left: 25px;margin-right: 5px;");
		cell.appendChild(editor.getComponent());
		cell.appendChild(new Label(" Xe"));

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mô tả"));
		editor = this.getMapEditor().get("Decription");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kích hoạt"));
		editor = this.getMapEditor().get("isActive");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);

		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES));
		cell.setStyle("color:red;text-align:center;");
	}

	@Override
	public void onEditorChangeValue(Event e) {
		if (e.getData() instanceof M2OEditor && ((M2OEditor) e.getData()).getDataField().equals("taxigroup")) {
			Agent agent = ((TaxiGroup) ((M2OEditor) e.getData()).getValue()).getAgent();
			((ParkingArea) getModel()).setAgent(agent);
			super.onEditorChangeValue(e);
		} else {
			super.onEditorChangeValue(e);
		}
	}

	public void updateAddress(String add, LatLng latlng) {
		((ParkingArea) getModel()).setLati(latlng.getLatitude());
		((ParkingArea) getModel()).setLongi(latlng.getLongitude());
		((Textbox) editoradd.getComponent()).setValue(add);
		Events.postEvent(new InputEvent(Events.ON_CHANGING, editoradd.getComponent(), add, ""));
	}

	@Override
	public void handleSaveEvent() {
		super.handleSaveEvent();
		Events.postEvent(Events.ON_CHANGE, this, null);
	}

}
