package com.vietek.taxioperation.ui.controller;

import org.zkoss.zul.Cell;
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

/**
 * 
 * @author VANMANH289VN
 *
 */

public class SysCompanysDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SysCompanysDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết công ty");
		this.setWidth("600px");
	}

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
		col.setHflex("60%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã"));
		Editor editor = this.getMapEditor().get("value");

		Cell cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên công ty"));
		editor = this.getMapEditor().get("name");
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Công ty cha"));
		editor = this.getMapEditor().get("parentId");
		cell = new Cell();
		cell.setParent(row);
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kích hoạt"));
		editor = this.getMapEditor().get("isActive");
		cell = new Cell();
		cell.setParent(row);
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tổng đài"));
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		editor = this.getMapEditor().get("voipCenter");
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Ghi chú"));
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		editor = this.getMapEditor().get("note");
		Textbox textbox = new Textbox();
		textbox.setMultiline(true);
		textbox.setHeight("50px");
		textbox.setWidth("405px");
		editor.setComponent(textbox);
		cell.appendChild(editor.getComponent());

	}
}
