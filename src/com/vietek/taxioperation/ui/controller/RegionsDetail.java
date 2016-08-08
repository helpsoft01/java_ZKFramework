package com.vietek.taxioperation.ui.controller;

import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class RegionsDetail extends BasicDetailWindow {

	/**
	 * @author hung
	 */
	private static final long serialVersionUID = 1L;

	public RegionsDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Vùng miền");
		this.setWidth("600px");
	}
    
	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setWidth("100%");
		grid.setParent(this);
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
		row.appendChild(new Label("Mã vùng"));
		Label lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		Editor editor = this.getMapEditor().get("value");
		Textbox textbox = (Textbox) editor.getComponent();
		row.appendChild(textbox);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên vùng"));
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		editor = this.getMapEditor().get("RegionName");
		textbox = (Textbox) editor.getComponent();
		row.appendChild(textbox);
		
	}
}
