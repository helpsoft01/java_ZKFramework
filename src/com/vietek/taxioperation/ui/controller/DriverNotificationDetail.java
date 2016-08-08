package com.vietek.taxioperation.ui.controller;

import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

/**

author Viet Ha Ca

 */
public class DriverNotificationDetail extends BasicDetailWindow {

	public DriverNotificationDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		setTitle(CommonDefine.DriverNotification.TITLE_FROM);
		setWidth("400px");
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void createForm() {
		// TODO Auto-generated method stub
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
	/*	Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã"));
		Editor editor = this.getMapEditor().get("id");
		row.appendChild(editor.getComponent());*/
		
		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Thông báo"));
		Editor editor = this.getMapEditor().get("message");
		
		Cell cell = new Cell();
		
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		
	/*	cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);*/
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Lái xe"));
		editor = this.getMapEditor().get("drivers");
		
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		
		/*cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);*/
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kich hoạt"));
		editor = this.getMapEditor().get("isActive");
		cell = new Cell();
		cell.setParent(row);
//		Messagebox.show("Kich hoat component class: "+editor.getComponent().toString());
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES)); 
		cell.setStyle("color:red;text-align:center;");
	}
}
