package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Notification;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.Content;
//import com.vietek.taxioperation.util.Content;
import com.vietek.taxioperation.util.Env;
//import com.vietek.taxioperation.util.GCMUtils;
import com.vietek.taxioperation.util.GCMUtils;

public class NotificationsDetail extends BasicDetailWindow{

	/**
	 * @Batt
	 * 12/29/2015
	 */
	private static final long serialVersionUID = 3299782591854895914L;

	public NotificationsDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Thông báo");
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
		row.appendChild(new Label("Chủ đề"));
		Editor editor = getMapEditor().get("subject");
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
		row.appendChild(new Label("Customer"));
		editor = getMapEditor().get("listCustomer");
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
		row.appendChild(new Label("Nội dung"));
		editor = getMapEditor().get("content");
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
		row.appendChild(new Label("Nội dung"));
		editor = getMapEditor().get("type");
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
		row.appendChild(new Label("Ghi chú"));
		editor = getMapEditor().get("note");
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(editor.getComponent());
	}
	@Override
	public void onEvent(Event event) throws Exception {
		if(event.getTarget().equals(this.getBtn_save())){
			AbstractModel model = super.getModel();
			Notification bean = (Notification) model;
			if(bean.getType() == null){
//				 APA91bED6CuV_j-y123_ZNFx9KNXmGx6THCMgZfGvAAaJgEOsFB0dUZEV8JDsCINEH9WMYWUAJyrxq487p9EVXQmb8uKH5jxdc0kql5aQRmh_i7uBayTEeEsMMXLkDK0ohogknXmjgdH
				List<String> lstReg = new ArrayList<String>();
				Content content = Content.createContent(bean.getContent(), lstReg);
				GCMUtils.sendToDevices(CommonDefine.API_KEY_ANDROID,content);
			}else if(bean.getType()== 0){/*-- Send to all devices --*/
				
			}else if(bean.getType() == 1){ /*-- Send to all devices android --*/
				GCMUtils.post(CommonDefine.API_KEY_ANDROID, bean.getContent());
			}else if(bean.getType() == 2){ /*-- Send to all devices IOS --*/
				
			}
			
			super.handleSaveEvent();
		}else if (event.getTarget().equals(this.getBtn_cancel())
				|| (event.getName().equals(Events.ON_CANCEL) && event
						.getTarget().equals(this))) {
			this.setVisible(false);
			Env.getHomePage().showNotification("Bỏ qua thay đổi!",
					Clients.NOTIFICATION_TYPE_INFO);
			this.getListWindow().refresh();
		}
	}
	
	/**
	 * @Batt
	 * validate 
	 * */
//	private String validateNotify(Notification bean){
//		StringBuilder msg = new StringBuilder();
//		if(StringUtils.isEmpty(bean.getSubject())){
//			msg.append("Chủ đề không được trống !");
//		}
//		return msg.toString();
//	}
}
