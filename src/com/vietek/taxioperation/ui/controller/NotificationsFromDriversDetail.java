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

public class NotificationsFromDriversDetail extends BasicDetailWindow{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6154969463980637614L;

	public NotificationsFromDriversDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Thông báo từ tài xế");
		this.setWidth("600px");
		// TODO Auto-generated constructor stub
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
		row.appendChild(new Label("Loại thông báo"));
		editor = getMapEditor().get("typeNotification");
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
		row.appendChild(new Label("Thời gian gửi"));
		editor = getMapEditor().get("timeSend");
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(editor.getComponent());
		
	}

}
