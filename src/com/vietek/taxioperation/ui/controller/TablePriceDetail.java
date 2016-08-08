package com.vietek.taxioperation.ui.controller;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.TablePrice;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class TablePriceDetail extends BasicDetailWindow {

	public TablePriceDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setWidth("500px");
		this.setTitle("Bảng giá");
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
		col.setWidth("20%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("30%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("20%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("30%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);

		row.appendChild(new Label("Loại bảng giá"));
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		Editor editor = getMapEditor().get("typeTablePrice");
		cell.appendChild(editor.getComponent());

		row.appendChild(cell);

		row = new Row();
		row.setParent(rows);
		Hlayout hlayout = new Hlayout();
		Label lb = new Label("Km");
		hlayout.appendChild(lb);
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		hlayout.appendChild(lb);
		row.appendChild(hlayout);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		editor = getMapEditor().get("km");
		cell.appendChild(editor.getComponent());
		row.appendChild(cell);

		row = new Row();
		row.setParent(rows);

		Auxhead auh = new Auxhead();
		auh.setParent(row);
		Auxheader auhx = new Auxheader("THỜI GIAN (PHÚT)");
		auhx.setStyle("text-align:center;");
		auhx.setColspan(4);
		auhx.setParent(auh);

		row = new Row();
		row.setParent(rows);
		hlayout = new Hlayout();
		lb = new Label("Một chiều");
		hlayout.appendChild(lb);
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		hlayout.appendChild(lb);
		row.appendChild(hlayout);
		editor = getMapEditor().get("time1c");
		row.appendChild(editor.getComponent());

		hlayout = new Hlayout();
		lb = new Label("Hai chiều");
		hlayout.appendChild(lb);
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		hlayout.appendChild(lb);
		row.appendChild(hlayout);
		editor = getMapEditor().get("time2c");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);

		auh = new Auxhead();
		auh.setParent(row);
		auhx = new Auxheader("GIÁ(VNĐ)");
		auhx.setStyle("text-align:center;");
		auhx.setColspan(4);
		auhx.setParent(auh);

		row = new Row();
		row.setParent(rows);
		hlayout = new Hlayout();
		lb = new Label("Một chiều");
		hlayout.appendChild(lb);
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		hlayout.appendChild(lb);
		row.appendChild(hlayout);
		editor = getMapEditor().get("price1c");
		row.appendChild(editor.getComponent());
		hlayout = new Hlayout();
		lb = new Label("Hai chiều");
		hlayout.appendChild(lb);
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		hlayout.appendChild(lb);
		row.appendChild(hlayout);
		editor = getMapEditor().get("price2c");
		row.appendChild(editor.getComponent());

		auh = new Auxhead();
		auh.setParent(row);
		auhx = new Auxheader("THỜI GIAN ÁP DỤNG");
		auhx.setStyle("text-align:center;");
		auhx.setColspan(4);
		auhx.setParent(auh);

		row = new Row();
		row.setParent(rows);
		hlayout = new Hlayout();
		lb = new Label("Từ ngày");
		hlayout.appendChild(lb);
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		hlayout.appendChild(lb);
		row.appendChild(hlayout);
		editor = getMapEditor().get("fromDate");
		row.appendChild(editor.getComponent());

		hlayout = new Hlayout();
		lb = new Label("Đến ngày");
		hlayout.appendChild(lb);
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		hlayout.appendChild(lb);
		row.appendChild(hlayout);
		editor = getMapEditor().get("toDate");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(4);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES));
		cell.setStyle("color:red;text-align:center;");
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		if(event.getTarget().equals(super.getBtn_save())){
			TablePrice tablePrice=(TablePrice)super.getModel();
			String msg=validate(tablePrice);
			if(!StringUtils.isEmpty(msg)){
				Clients.showNotification(msg, "error", null, "middle-center", 3000, true);
				return;
			} else {
				super.handleSaveEvent();
			}
		}
		else if (event.getTarget().equals(super.getBtn_cancel())){
			this.onClose();
		}
	}
	
	private String validate(TablePrice tablePrice){
		StringBuffer msg=new StringBuffer();
		if(tablePrice.getKm()==null || tablePrice.getKm()<=0){
			msg.append(CommonDefine.TablePriceDefine.TABLE_PRICE_KM+ " phải lớn hơn 0");
		}
		else if(tablePrice.getTime1c()==null || tablePrice.getTime1c()<0){
			msg.append(CommonDefine.TablePriceDefine.TABLE_PRICE_TIME + " phải lớn hơn 0");
		}
		else if(tablePrice.getTime2c()==null || tablePrice.getTime2c() < tablePrice.getTime1c()){
			msg.append(CommonDefine.TablePriceDefine.TABLE_PRICE_TIME + " 2 chiều phải lớn hơn thời gian đi 1 chiều");
		}
		else if(tablePrice.getPrice1c()==null || tablePrice.getPrice1c()<=0){
			msg.append(CommonDefine.TablePriceDefine.TABLE_PRICE_MONEY+" phải lớn hơn 0");
		}
		else if(tablePrice.getPrice2c()==null || tablePrice.getPrice2c()<=0){
			msg.append(CommonDefine.TablePriceDefine.TABLE_PRICE_MONEY+" phải lớn hơn 0");
		}
		else if(tablePrice.getFromDate()==null){
			msg.append(CommonDefine.TablePriceDefine.TABLE_PRICE_DATE + " không được trống");
		}
		else if(tablePrice.getToDate()==null || tablePrice.getToDate().getTime()<=tablePrice.getFromDate().getTime()){
			msg.append(CommonDefine.TablePriceDefine.TABLE_PRICE_DATE + " không phù hợp");
		}
		return msg.toString();
	}
}
