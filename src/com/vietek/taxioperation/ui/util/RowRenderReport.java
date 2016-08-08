package com.vietek.taxioperation.ui.util;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

public class RowRenderReport<T> implements RowRenderer<T> {
	
	ArrayList<GridColumn> gridColumns;
	
	public RowRenderReport(ArrayList<GridColumn> gridColumns) {
		super();
		this.gridColumns = gridColumns;
	}

	@Override
	public void render(Row row, T data, int index) throws Exception {
		for(int i = 0; i < gridColumns.size(); i++){
			if(i == 0) {
				row.appendChild(new Label(""+ ++index));
			} else {
				GridColumn col = gridColumns.get(i);
				Method method = data.getClass().getMethod(col.getGetDataMethod());
				Object val = method.invoke(data);
				if(val instanceof Timestamp){
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"dd/MM/yyyy HH:mm");
					row.appendChild(new Label(dateformat.format(val)));
				} else {
					row.appendChild(new Label(val.toString()));
				}
			}
		}
	}

}
