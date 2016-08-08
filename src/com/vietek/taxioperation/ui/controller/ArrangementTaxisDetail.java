package com.vietek.taxioperation.ui.controller;

import java.util.LinkedList;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.ListCommon;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.ArrangementTaxi;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.Env;

public class ArrangementTaxisDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Editor editoradd;
	private ArrangementTaxi curModel;
	public ArrangementTaxisDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết điểm sắp tài");
		this.setWidth("500px");
	}

	@Override
	public void createForm(){
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
		col.setHflex("75%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã điểm"));
		Editor editor = this.getMapEditor().get("value");

		Cell cell = new Cell();
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên điểm"));
		editor = this.getMapEditor().get("name");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());
	
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Địa chỉ"));
		editoradd = this.getMapEditor().get("address");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editoradd.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Chi nhánh"));
		editor = this.getMapEditor().get("agent");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Bán kính"));
		editor = this.getMapEditor().get("radius");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setColspan(3);
		cell.setParent(row);
		cell.appendChild(new Label("Xe tối thiểu"));
		editor = this.getMapEditor().get("minCar");
		((Intbox) editor.getComponent()).setStyle("width: 80px !important;margin-left: 25px;margin-right: 5px;");
		cell.appendChild(editor.getComponent());
		Label lb = new Label("Xe");
		lb.setStyle("margin-right:20px");
		cell.appendChild(lb);
		cell.appendChild(new Label("Xe tối đa"));
		editor = this.getMapEditor().get("maxCar");
		((Intbox) editor.getComponent()).setStyle("width: 80px !important;margin-left: 25px;margin-right: 5px;");
		cell.appendChild(editor.getComponent());
		cell.appendChild(new Label(" Xe"));
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Điểm tiếp thị"));
		editor = this.getMapEditor().get("isMarketing");

		cell = new Cell();
		cell.setParent(row);

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kích hoạt"));
		editor = this.getMapEditor().get("isActive");

		cell = new Cell();
		cell.setParent(row);

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES));
		cell.setStyle("color:red;text-align:center;");
	}
	
	public void updateAddress(String add, LatLng latlng) {
		((ArrangementTaxi)getModel()).setLatitude(latlng.lat);
		((ArrangementTaxi)getModel()).setLongitude(latlng.lng);
		((Textbox) editoradd.getComponent()).setValue(add);
		Events.postEvent(new InputEvent(Events.ON_CHANGING, editoradd.getComponent(), add, ""));
	}

	@Override
	public void handleSaveEvent() {
		this.curModel = (ArrangementTaxi)getModel();
		String temp = validateValue(this.curModel);
		if(StringUtils.isEmpty(temp)){
			curModel.save();
			Events.postEvent(Events.ON_CHANGE, this, null);
			boolean isNew = true;
			for (ArrangementTaxi marketingPlace : ListCommon.LIST_ARRANGAMENT_PLACE) {
				if (marketingPlace.getId() == curModel.getId()) {
					ListCommon.LIST_ARRANGAMENT_PLACE.remove(marketingPlace);
					ListCommon.LIST_ARRANGAMENT_PLACE.add(curModel);
					MapCommon.MAP_ARRANGEMENT.put(curModel.getId() + "", curModel);
					isNew = false;
					break;
				}
			}
			if (isNew) {
				ListCommon.LIST_ARRANGAMENT_PLACE.add(curModel);
			}
			if (MapCommon.MAP_TAXI_LIST.get(curModel.getId()) == null) {
				LinkedList<Taxi> lst = new LinkedList<>();
				MapCommon.MAP_TAXI_LIST.put(curModel.getId(), lst);
			}
		}else{
			Env.getHomePage().showValidateForm(temp, Clients.NOTIFICATION_TYPE_WARNING);
		}
	}
	
	private String validateValue(ArrangementTaxi bean){
		String msg = "";
		if(bean.getAgent() == null){
			msg += "Chưa chọn chi nhánh ! ";
		}
		if(bean.getRadius() < 0){
			msg += "Bán kính không hợp lệ ! ";
		}
		if(bean.getMinCar() < 0 || bean.getMaxCar() < 0){
			msg += "Số lượng xe không hợp lệ ! ";
		}else if(bean.getMinCar() > bean.getMaxCar()){
			msg += "Số lượng xe tối đa không hợp lệ ! ";
		}
		return msg;
	}
}
