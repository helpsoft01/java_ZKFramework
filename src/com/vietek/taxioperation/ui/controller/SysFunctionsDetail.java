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

/**
 * 
 * @author VuD
 * 
 */

public class SysFunctionsDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SysFunctionsDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết chức năng");
		this.setWidth("150px");
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
		col.setHflex("60%");

		Rows rows = new Rows();
		rows.setParent(grid);
		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên chức năng"));
		Editor editor = this.getMapEditor().get("name");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Class"));
		editor = this.getMapEditor().get("clazz");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("zul file"));
		editor = this.getMapEditor().get("zulFile");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Hành động"));
		editor = this.getMapEditor().get("sysAction");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kich hoạt"));
		editor = this.getMapEditor().get("isActive");
		row.appendChild(editor.getComponent());

	}

}
