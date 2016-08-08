package com.vietek.taxioperation.ui.controller;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.DriverIgnoreTrip;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.Env;

public class DriverIgnoreTripDetail extends BasicDetailWindow {

	public DriverIgnoreTripDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		setTitle("Chi tiết hủy cuốc");
		setWidth("400px");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);
		
		Columns cols = new Columns();
		cols.setParent(grid);
		
		Column col = new Column();
		col.setParent(cols);
		col.setHflex("30%");
		
		col = new Column();
		col.setParent(cols);
		col.setHflex("5%");
		
		col = new Column();
		col.setParent(cols);
		col.setHflex("65%");
		
		Rows rows = new Rows();
		rows.setParent(grid);
		Row row = new Row();
		/*row.setParent(rows);
		row.appendChild(new Label("Mã"));
		Editor editor = this.getMapEditor().get("id");
		row.appendChild(editor.getComponent());*/
		row.setParent(rows);
		row.appendChild(new Label("Lái xe"));
		Editor editor = this.getMapEditor().get("driver");
		
		Cell cell = new Cell();
		
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Phương tiện"));
		editor = this.getMapEditor().get("vehicle");
		
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);
		cell.appendChild(editor.getComponent());
		
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Thời gian"));
		editor = this.getMapEditor().get("time");
		
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES)); 
		cell.setStyle("color:red;text-align:center;");
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		// TODO Auto-generated method stub
		AbstractModel abstractModel = this.getModel();
		DriverIgnoreTrip model = (DriverIgnoreTrip)abstractModel;
		if(event.getTarget().equals(this.getBtn_save())){
			if(StringUtils.isEmpty(validateInput(model))){
				this.handleSaveEvent();
			}else{
				Env.getHomePage().showValidateForm(validateInput(model), Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
		}else if(event.getTarget().equals(this.getBtn_cancel())){
//			this.setVisible(false);
//			Env.getHomePage().showNotification("Bỏ qua thay đổi!",
//					Clients.NOTIFICATION_TYPE_INFO);
			super.onEvent(event);
		}
	}
	
	private String validateInput(DriverIgnoreTrip model){
		StringBuilder msg = new StringBuilder("");
		if(model.getDriver() == null){
			msg.append(CommonDefine.DriverIgnoreTrip.DRIVER_DRIVER_IGNORE_TRIP).append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		}
		if(model.getVehicle() == null){
			msg.append(" - ").append(CommonDefine.DriverIgnoreTrip.VEHICLE_DRIVER_IGNORE_TRIP).append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		}
		if(model.getTime() == null || StringUtils.isBlank(model.getTime().toString())){
			msg.append(" - ").append(CommonDefine.DriverIgnoreTrip.TIME_DRIVER_IGNORE_TRIP).append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		}
		return msg.toString();
	}
}
