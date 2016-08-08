package com.vietek.taxioperation.ui.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

public class GridRowRenderer<T> implements RowRenderer<T> {

	ArrayList<GridColumn> gridColumns;

	public GridRowRenderer(ArrayList<GridColumn> gridColumns) {
		super();
		this.gridColumns = gridColumns;
	}

	@Override
	public void render(Row row, T data, int index) throws Exception {
		for (int i = 0; i < gridColumns.size(); i++) {
			GridColumn column = gridColumns.get(i);
			Method method = data.getClass().getMethod(column.getGetDataMethod());
			Object val = method.invoke(data);
			row.appendChild(new Label(val.toString()));
		}
	}
}
