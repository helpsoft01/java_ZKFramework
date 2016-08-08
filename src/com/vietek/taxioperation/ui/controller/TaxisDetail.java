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

public class TaxisDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaxisDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		setTitle("Chi tiết Taxi");
		this.setWidth("650px");
	}

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setWidth("10%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("38%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("14%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("38%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã taxi"));
		Editor editor = getMapEditor().get("value");
		row.appendChild(editor.getComponent());

		editor = getMapEditor().get("taxiNumber");
		row.appendChild(new Label("Biển số xe"));
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		editor = getMapEditor().get("taxiType");
		row.appendChild(new Label("Loại taxi"));
		row.appendChild(editor.getComponent());
		editor = getMapEditor().get("taxiGroup");
		row.appendChild(new Label("Đội xe"));
		row.appendChild(editor.getComponent());

	}

}
