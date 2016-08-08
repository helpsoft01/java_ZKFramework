package com.vietek.taxioperation.ui.controller;

import org.zkoss.gmaps.LatLng;
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
import com.vietek.taxioperation.model.MarketingPlace;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

/**
 *
 * @author VuD
 */
public class MarketingPlacesDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Editor editoradd;

	public MarketingPlacesDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Điểm Tiếp thị ");
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
		row.appendChild(new Label("Mã điểm"));
		Editor editor = this.getMapEditor().get("PavilionCode");

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
		row.appendChild(new Label("Tên điểm"));
		editor = this.getMapEditor().get("PavilionName");

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
		row.appendChild(new Label("Loại điểm"));
		editor = this.getMapEditor().get("Paviliontype");

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
		row.appendChild(new Label("Chi nhánh"));
		editor = this.getMapEditor().get("agent");

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
		editor = this.getMapEditor().get("Active");

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

	public void updateAddress(String add, LatLng latlng) {
		((MarketingPlace) getModel()).setLati(latlng.getLatitude());
		((MarketingPlace) getModel()).setLongi(latlng.getLongitude());
		((Textbox) editoradd.getComponent()).setValue(add);
		Events.postEvent(new InputEvent(Events.ON_CHANGING, editoradd.getComponent(), add, ""));
	}

	@Override
	public void handleSaveEvent() {
		super.handleSaveEvent();
		Events.postEvent(Events.ON_CHANGE, this, null);
	}

}