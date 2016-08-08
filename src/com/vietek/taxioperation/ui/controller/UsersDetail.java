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

public class UsersDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsersDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
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
		col.setWidth("15%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("35%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("15%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("35%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã người dùng"));
		Editor editor = getMapEditor().get("extNumber");
		row.appendChild(editor.getComponent());
		editor = getMapEditor().get("fullName");
		row.appendChild(new Label("Tên đầy đủ"));
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		editor = getMapEditor().get("accountName");
		row.appendChild(new Label("Tài khoản"));
		row.appendChild(editor.getComponent());
		editor = getMapEditor().get("password");
		
		row.appendChild(new Label("Mật khẩu"));
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		editor = getMapEditor().get("isActive");
		row.appendChild(new Label("Kích hoạt"));
		row.appendChild(editor.getComponent());
		editor = getMapEditor().get("birthDay");
		row.appendChild(new Label("Ngày sinh"));
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Địa chỉ"));
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(getMapEditor().get("address").getComponent());
		
		row = new Row();
		row.setParent(rows);
		editor = getMapEditor().get("email");
		row.appendChild(new Label("Email"));
		row.appendChild(editor.getComponent());
		editor = getMapEditor().get("phoneNumber");
		row.appendChild(new Label("Số điện thoại"));
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		editor = getMapEditor().get("channel");
		row.appendChild(new Label("Kênh"));
		row.appendChild(editor.getComponent());
	}

}
