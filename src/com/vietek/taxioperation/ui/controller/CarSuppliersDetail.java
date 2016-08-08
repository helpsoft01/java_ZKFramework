package com.vietek.taxioperation.ui.controller;

import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class CarSuppliersDetail extends BasicDetailWindow{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CarSuppliersDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		setTitle("Chi tiết cung cấp xe");
		this.setWidth("400px");
	}
	
	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setWidth("20%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("60%");
		
		Rows rows = new Rows();
		rows.setParent(grid);
		
		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã"));
		Editor editor = this.getMapEditor().get("value");
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên"));
		editor = getMapEditor().get("name");
		row.appendChild(editor.getComponent());
	}

}
