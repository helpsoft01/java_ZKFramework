package com.vietek.taxioperation.ui.controller;

import java.util.List;

import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.controller.ConfigController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Config;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class ConfigsDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Textbox textbox;
	
	public ConfigsDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết cấu hình");
		this.setWidth("400px");
	}
	
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
		col.setWidth("5%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("75%");
		
		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên"));
		Editor editor = getMapEditor().get("name");
		Cell cell = new Cell();
		
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
		row.appendChild(new Label("Giá trị"));
		editor = getMapEditor().get("value");
		
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
		row.appendChild(new Label("Ghi chú"));
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);
		editor = this.getMapEditor().get("note");
		textbox=new Textbox();
		textbox.setMultiline(true);
		textbox.setHeight("100px");
		textbox.setWidth("283px");
		editor.setComponent(textbox);
		cell.appendChild(editor.getComponent());
	}
	@Override
	public void handleSaveEvent() {
		Config config = (Config)this.getModel();
		String msg = validateValue(config);
		if(StringUtils.isEmpty(msg)){
			config.setNote(textbox.getValue());
			config.save();
			ConfigUtil.reloadMap(config.getName(),config.getValue());
			Env.getHomePage().showNotification("Đã cập nhật thông tin!", Clients.NOTIFICATION_TYPE_INFO);
		}else{
			Env.getHomePage().showNotification(msg, Clients.NOTIFICATION_TYPE_INFO);
		}
		this.setVisible(false);
		this.getListWindow().refresh();
	}
	
	private String validateValue(Config config){
		String msg = "";
		if(StringUtils.isEmpty(config.getName())){
			msg = "Tên không được để trống";
		}else if(!checkExistKey(config)){
			msg = "Tên đã được định nghĩa trong hệ thống !";
		}
		if(StringUtils.isEmpty(config.getValue())){
			msg = "Giá trị không được để trống";
		}
		return msg;
	} 
	
	private boolean checkExistKey(Config config){
		if(config.getId() <= 0){
			ConfigController configController = (ConfigController) ControllerUtils.getController(ConfigController.class);
			List<Config> tmp = configController.find("from Config where name = ?", config.getName());
			if(tmp != null && tmp.size() > 0){
				return false;
			}
		}else{
			ConfigController configController = (ConfigController) ControllerUtils.getController(ConfigController.class);
			List<Config> lstModel = configController.find("from Config where name = ? and id != ?",new Object[] { config.getName(), config.getId() });
			if (lstModel.size() > 0) {
				return false;
			} 
		}
		return true;
	}
}
