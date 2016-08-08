package com.vietek.taxioperation.ui.controller;

import org.zkoss.zul.Cell;
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
 */
public class VoipCentersDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VoipCentersDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết voip center");
		this.setWidth("600px");
	}

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);
		Column col = new Column();
		col.setParent(cols);
		col.setHflex("15");

		col = new Column();
		col.setParent(cols);
		col.setHflex("35");

		col = new Column();
		col.setParent(cols);
		col.setHflex("15");

		col = new Column();
		col.setParent(cols);
		col.setHflex("35");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã"));
		Editor editor = this.getMapEditor().get("value");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("Tên"));
		editor = this.getMapEditor().get("name");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Url"));
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		editor = this.getMapEditor().get("url");
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Công ty"));
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		editor = this.getMapEditor().get("sysCompany");
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kích hoạt"));
		editor = this.getMapEditor().get("isActive");
		row.appendChild(editor.getComponent());

	}

}
