package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;

import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.EditorFactory;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class AgentDetail extends BasicDetailWindow implements Serializable {

	public AgentDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setClosable(true);
		this.setTitle("Danh mục chi nhánh ");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);
		grid.setWidth("800px");
		Columns cols = new Columns();
		cols.setParent(grid);
		cols = new Columns();
		Column col = new Column();
		col.setParent(cols);
		col.setWidth("100px");
		col = new Column();
		col.setParent(cols);
		col.setWidth("300px");
		col = new Column();
		col.setParent(cols);
		col.setWidth("100px");
		col = new Column();
		col.setParent(cols);
		col.setWidth("300px");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã chi nhánh:"));
		Cell cell = new Cell();
		cell.setColspan(3);
		cell.setParent(row);
		Label lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		Editor editor = getMapEditor().get("value");
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên chi nhánh:"));
		cell = new Cell();
		cell.setColspan(3);
		cell.setParent(row);
		lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		editor = getMapEditor().get("AgentName");
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Số điện thoại:"));
		editor = getMapEditor().get("Phone");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("Số Fax:"));
		editor = getMapEditor().get("Fax");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Địa chỉ :"));
		cell = new Cell();
		cell.setColspan(3);
		cell.setParent(row);
		editor = getMapEditor().get("AgentAddress");
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Trực thuộc:"));
		cell = new Cell();
		cell.setColspan(3);
		cell.setParent(row);
		lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		editor = getMapEditor().get("company");
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Khu vực:"));
		editor = getMapEditor().get("zone");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("Tỉnh thành:"));
		editor = getMapEditor().get("province");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã số thuế:"));
		editor = getMapEditor().get("TaxCode");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label(" Số Tổng đài:"));
		editor = getMapEditor().get("CallCenterNumber");
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kích hoạt :"));
		cell = new Cell();
		cell.setColspan(3);
		cell.setParent(row);
		editor = getMapEditor().get("isActive");
		cell.appendChild(editor.getComponent());
	}

	@Override
	public void createMapEditor() {
		super.createMapEditor();
		Editor editor = EditorFactory.getMany2OneEditor(this.getModel(), "company");
		editor.setValueChangeListener(this);
		this.getMapEditor().put("company", editor);
	}
}