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
 * @author VuD
 */
public class SysRulesDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SysRulesDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
	}

	public SysRulesDetail(AbstractWindowPanel listWindow) {
		super(listWindow);
	}

	@Override
	public void createForm() {
		this.setTitle("Phân quyền dữ liệu");
		this.setMaximizable(true);
		this.setWidth("800px");
		this.setClosable(true);
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
		row.appendChild(new Label("Kích hoạt"));
		editor = this.getMapEditor().get("isActive");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Model"));
		editor = this.getMapEditor().get("modelName");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("Mức độ"));
		editor = this.getMapEditor().get("priority");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mô tả"));
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		editor = this.getMapEditor().get("description");
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("HQL"));
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		editor = this.getMapEditor().get("hql");
		Textbox txtbox = (Textbox) editor.getComponent();
		txtbox.setMultiline(true);
		txtbox.setHeight("50px");
		cell.appendChild(txtbox);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Nhóm"));
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		editor = this.getMapEditor().get("setSysGroup");
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Người dùng"));
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		editor = this.getMapEditor().get("setSysUser");
		cell.appendChild(editor.getComponent());

	}

}