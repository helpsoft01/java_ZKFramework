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

public class SOSNotificationsDetail extends BasicDetailWindow{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SOSNotificationsDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("SOS Notification");
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
		col.setWidth("16%");
		
		col = new Column();
		col.setParent(cols);
		col.setWidth("4%");
	
		col = new Column();
		col.setParent(cols);
		col.setWidth("16%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("30%");

		Rows rows = new Rows();
		rows.setParent(grid);
		
		Row row = new Row();

		row.setParent(rows);
		row.appendChild(new Label("Tài xế"));
		Editor editor = getMapEditor().get("driver");
		Cell cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		cell.appendChild(editor.getComponent());
		
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kinh độ"));
		editor = getMapEditor().get("longtitude");
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		cell.appendChild(editor.getComponent());
		
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Vĩ độ"));
		editor = getMapEditor().get("latitude");
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		cell.appendChild(editor.getComponent());
		
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Thời gian bắt đầu"));
		editor = getMapEditor().get("startTime");
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		cell.appendChild(editor.getComponent());
		
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Thời gian kết thúc"));
		editor = getMapEditor().get("endTime");
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(editor.getComponent());
	}

}
