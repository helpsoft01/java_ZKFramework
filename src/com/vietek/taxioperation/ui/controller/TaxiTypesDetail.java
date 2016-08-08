package com.vietek.taxioperation.ui.controller;

import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class TaxiTypesDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaxiTypesDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		setTitle("Chi tiết loại taxi");
		this.setWidth("700px");
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
		col.setWidth("38%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("10%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("37%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(1);
		cell.appendChild(new Label("Mã "));
		Label lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		row.appendChild(getMapEditor().get("value").getComponent());
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(1);
		cell.appendChild(new Label("Tên "));
		lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		row.appendChild(getMapEditor().get("name").getComponent());

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(1);
		cell.appendChild(new Label("Hãng xe "));
		lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		row.appendChild(getMapEditor().get("carSupplier").getComponent());
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(1);
		cell.appendChild(new Label("Hiệu xe "));
		lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		row.appendChild(getMapEditor().get("tradeMark").getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Số chỗ"));
		row.appendChild(getMapEditor().get("seats").getComponent());
		row.appendChild(new Label("Màu sắc"));
		row.appendChild(getMapEditor().get("color").getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Loại bảng cước đi giá"));
		row.appendChild(getMapEditor().get("typeTablePrice").getComponent());
	}

}
