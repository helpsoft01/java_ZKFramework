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
import com.vietek.taxioperation.ui.editor.EditorFactory;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class TaxiGroupFormDetail extends BasicDetailWindow {

	private static final long serialVersionUID = 1L;

	public TaxiGroupFormDetail(AbstractModel model, AbstractWindowPanel listWindow) {

		super(model, listWindow);
		this.setWidth("500px");
		this.setTitle("Chi tiết Nhóm xe");
	}

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("30%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("5%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("65%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã Nhóm "));
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
		row.appendChild(new Label("Tên Nhóm "));
		editor = this.getMapEditor().get("name");

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
		row.appendChild(new Label("Người Phụ trách "));
		editor = this.getMapEditor().get("leader");
		row.appendChild(new Label());
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Địa chỉ"));
		editor = this.getMapEditor().get("address");
		row.appendChild(new Label());
		row.appendChild(editor.getComponent());

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
		row.appendChild(new Label("Hính thức"));
		editor = this.getMapEditor().get("type");

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
		editor = this.getMapEditor().get("desription");
		row.appendChild(new Label());
		((Textbox) editor.getComponent()).setMultiline(true);
		((Textbox) editor.getComponent()).setHeight("60px");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Active"));
		editor = this.getMapEditor().get("isActive");
		row.appendChild(new Label());
		row.appendChild(editor.getComponent());

	}

	@Override
	public void createMapEditor() {
		super.createMapEditor();
		Editor editor = EditorFactory.getMany2OneEditor(this.getModel(), "agent");
		editor.setValueChangeListener(this);
		this.getMapEditor().put("agent", editor);
	}

}