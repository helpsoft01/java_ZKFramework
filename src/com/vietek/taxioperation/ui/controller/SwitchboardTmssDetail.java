package com.vietek.taxioperation.ui.controller;

//import java.util.List;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
//import org.zkoss.zk.ui.event.Events;
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
import com.vietek.taxioperation.controller.SwitchboardTMSController;
import com.vietek.taxioperation.model.AbstractModel;
// import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.SwitchboardTMS;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
// import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class SwitchboardTmssDetail extends BasicDetailWindow {

	/**
	 * @author VANMANH289VN
	 */
	private static final long serialVersionUID = 1L;
	//private SwitchboardTMS modeltmp;

	public SwitchboardTmssDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Tổng đài điều hành");
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
		col.setHflex("30%");
		
		col = new Column();
		col.setParent(cols);
		col.setHflex("5%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("70%");

		Rows rows = new Rows();
		rows.setParent(grid);
		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã tổng đài"));
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
		row.appendChild(new Label("Tên tổng đài"));
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
		row.appendChild(new Label("Ghi chú"));
		editor = this.getMapEditor().get("note");
		
		cell = new Cell();
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

	@Override
	public void onEvent(Event event) throws Exception {
		AbstractModel model = super.getModel();
		SwitchboardTMS modeltmp = (SwitchboardTMS)model;
		 if (StringUtils.isHasWhiteSpace(modeltmp.getValue())) {
			  String valuetmp = modeltmp.getValue().trim();
			 modeltmp.setValue(valuetmp);
			 super.setModel(modeltmp);
		}
		if (event.getTarget().equals(this.getBtn_save())) {
			if(StringUtils.isEmpty(validateInput(modeltmp))){
				super.handleSaveEvent();
			}else{
				Env.getHomePage().showNotification(validateInput(modeltmp),
						Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
		} else if (event.getTarget().equals(this.getBtn_cancel())
				|| (event.getName().equals(Events.ON_CANCEL) && event
						.getTarget().equals(this))) {
			this.setVisible(false);
			Env.getHomePage().showNotification("Bỏ qua thay đổi!",
					Clients.NOTIFICATION_TYPE_INFO);
			this.getListWindow().refresh();
		} 
	}
	
	private String validateInput(SwitchboardTMS model){
		StringBuilder msg = new StringBuilder("");
		
		// validate code
		if (StringUtils.isEmpty(model.getValue())) {
			msg.append(CommonDefine.SwitchboardDefine.CODE_SWITCHBOARD).append(
					CommonDefine.ERRORS_STRING_IS_EMPTY);
		} else {
			if (StringUtils.isHasSpecialChar(model.getValue())) {
				msg.append(CommonDefine.SwitchboardDefine.CODE_SWITCHBOARD).append(
						CommonDefine.ERRORS_STRING_IS_HAS_SPECIAL);
			}
			if (!StringUtils.checkMaxLength(model.getValue(),
					CommonDefine.CODE_MAX_LENGTH)) {
				msg.append(CommonDefine.SwitchboardDefine.CODE_SWITCHBOARD).append(
						CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
			}
		}
//			else if(StringUtils.isHasWhiteSpaceBeginEnd(model.getValue()) || StringUtils.isHasSpecialChar(model.getValue())){
//				msg.append(CommonDefine.SwitchboardTMS.CODE_SWITCHBOARDTMS).append(CommonDefine.ERRORS_STRING_INVALID);
//			}
		// validate name
			if(StringUtils.isEmpty(model.getName())){
				msg.append(CommonDefine.SwitchboardTMS.NAME_SWITCHBOARDTMS).append(CommonDefine.ERRORS_STRING_IS_EMPTY) ;
			}
			// In case save
			
			if(model.getId() <= 0){
				if(getSwitchboardTMSByValue(model.getValue()) != null){ // check exist code
					msg.append(CommonDefine.SwitchboardTMS.CODE_SWITCHBOARDTMS).append(CommonDefine.ERRORS_STRING_IS_EXIST) ;
				}
			}else{ // In case edit
				SwitchboardTMS tms = getSwitchboardTMSByValue(model.getValue());
				if(tms != null && tms.getId() != model.getId())
					msg.append(CommonDefine.SwitchboardTMS.CODE_SWITCHBOARDTMS).append(CommonDefine.ERRORS_STRING_IS_EXIST) ;
			}
			
		return msg.toString();
	}
	
	private SwitchboardTMS getSwitchboardTMSByValue(String value){
		 SwitchboardTMSController controller = (SwitchboardTMSController)
		 ControllerUtils.getController(SwitchboardTMSController.class);
		 String sql = "from SwitchboardTMS where value = ?";
		 List<SwitchboardTMS> lstvalue = controller.find(sql, value);
		 if (lstvalue == null || lstvalue.size()> 0) 
			 return (SwitchboardTMS)lstvalue.get(0);
		 else
			 return null;
	}
	
	
	}
	