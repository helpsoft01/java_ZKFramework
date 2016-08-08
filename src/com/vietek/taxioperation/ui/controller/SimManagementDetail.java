package com.vietek.taxioperation.ui.controller;

import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.editor.DateTimeEditor;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.TextEditor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class SimManagementDetail extends BasicDetailWindow{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimManagementDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Thông tin chi tiết");
		this.setWidth("800px");
	}
	
	@Override
	public void createForm(){
		Grid grid = new Grid();
		grid.setParent(this);
		
		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setWidth("30%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("70%");

		Rows rows = new Rows();
		rows.setParent(grid);
		
		
		Row row = new Row();
		row.setParent(rows);
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(1);
		cell.appendChild(new Label("Số SIM "));
		Label lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		row.appendChild(getMapEditor().get("SimNumber").getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Nhà mạng"));
		row.appendChild((Combobox)getMapEditor().get("simProvider").getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Loại thuê bao"));
		row.appendChild(getMapEditor().get("SimType").getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Ngày kích hoạt"));
		Editor editor = getMapEditor().get("simActiveDate");
		((DateTimeEditor) editor).setSclass("");
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Nội dung sim"));
		editor = getMapEditor().get("SimLogs");
		((TextEditor) editor).setRows(2);
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label(""));
		Button btnUpdate = new Button("Hỏi thông tin SIM");
		row.appendChild(btnUpdate);
	}
}
