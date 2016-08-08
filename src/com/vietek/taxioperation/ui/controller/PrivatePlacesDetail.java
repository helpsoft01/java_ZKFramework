package com.vietek.taxioperation.ui.controller;

import org.zkoss.gmaps.LatLng;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.PrivatePlace;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class PrivatePlacesDetail extends BasicDetailWindow {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Editor editoradd;
	public PrivatePlacesDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setWidth("400px");
		this.setTitle("Chi tiết điểm");
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
		row.appendChild(new Label("Tên bãi"));
		Editor editor = this.getMapEditor().get("PointName");

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
		row.appendChild(new Label("Lượng xe"));
		editor = this.getMapEditor().get("Radius");
		
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
		row.appendChild(new Label("Mô tả"));
		editor = this.getMapEditor().get("Description");
		
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
	public void updateAddress(String add , LatLng latlng){
		((PrivatePlace)getModel()).setLati(latlng.getLatitude());
		((PrivatePlace)getModel()).setLongi(latlng.getLongitude());
		((Textbox)editoradd.getComponent()).setValue(add);
        Events.postEvent(new InputEvent(Events.ON_CHANGING,editoradd.getComponent(),add,""));		
	}
	@Override
	public void handleSaveEvent() {
		super.handleSaveEvent();
		Events.postEvent(Events.ON_CHANGE,this,null);
	}
}
