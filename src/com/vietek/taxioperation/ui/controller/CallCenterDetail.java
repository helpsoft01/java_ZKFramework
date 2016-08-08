package com.vietek.taxioperation.ui.controller;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.CallCenterController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.CallCenter;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

/**
 * @author hung
 *
 */
public class CallCenterDetail extends BasicDetailWindow {
      private 	Textbox note;
	public CallCenterDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("CallCenter");
		this.setWidth("400px");
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
		col.setHflex("25%");

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
		row.appendChild(new Label("CallCenter"));
		Label lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		Editor editor = this.getMapEditor().get("callname");
	    row.appendChild(editor.getComponent());
	    
	    row = new Row();
	    row.setParent(rows);
	    row.appendChild(new Label("Số điện thoại"));
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		editor = this.getMapEditor().get("phonenmber");
	    row.appendChild(editor.getComponent());
	    
	    row = new Row();
	    row.setParent(rows);
	    row.appendChild(new Label("Ghi chú"));
		lb = new Label("");
		row.appendChild(lb);
		editor = this.getMapEditor().get("node");
		note = (Textbox) editor.getComponent();
		note.setMultiline(true);
		note.setHeight("100px");
	    row.appendChild(note);
	    
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		AbstractModel model = super.getModel();
		CallCenter modeltmp = (CallCenter)model;
		String textvalue = modeltmp.getCallname();
		modeltmp.setNode(note.getValue());
		if (StringUtils.isHasWhiteSpaceBeginEnd(textvalue)) {
			
		     modeltmp.setCallname(textvalue.trim());
		     this.setModel(modeltmp);
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

	private String validateInput(CallCenter model){
		StringBuilder msg = new StringBuilder("");
		
		// validate name center
		if (StringUtils.isEmpty(model.getCallname())) {
			msg.append(CommonDefine.CallCenterDefine.NAME_CALLCENTER).append(
					CommonDefine.ERRORS_STRING_IS_EMPTY);
		} else {
//			if (StringUtils.isHasSpecialChar(model.getCallname())) {
//				msg.append(CommonDefine.CallCenterDefine.NAME_CALLCENTER).append(
//						CommonDefine.ERRORS_STRING_IS_HAS_SPECIAL);
//			}
//			if (!StringUtils.checkMaxLength(model.getCallname(),
//					CommonDefine.NAME_MAX_LENGTH)) {
//				msg.append(CommonDefine.CallCenterDefine.NAME_CALLCENTER).append(
//						CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
//			}
		}
		// validate phone number
		if (StringUtils.isEmpty(model.getPhonenmber())) {
			msg.append(CommonDefine.CallCenterDefine.NAME_CALLCENTER).append(
					CommonDefine.ERRORS_STRING_IS_EMPTY);
		}
			// In case save
			
			if(model.getId() <= 0){
				if(getCallCenterByValue(model.getCallname()) != null){ // check exist code
					msg.append(CommonDefine.CallCenterDefine.NAME_CALLCENTER).append(CommonDefine.ERRORS_STRING_IS_EXIST) ;
				}
			}else{ // In case edit
				CallCenter tms = getCallCenterByValue(model.getCallname());
				if(tms != null && tms.getId() != model.getId())
					msg.append(CommonDefine.CallCenterDefine.NAME_CALLCENTER).append(CommonDefine.ERRORS_STRING_IS_EXIST) ;
			}
			
		return msg.toString();
	}
	
	private CallCenter getCallCenterByValue(String value){
		CallCenterController controller = (CallCenterController)
		 ControllerUtils.getController(CallCenterController.class);
		 String sql = "from CallCenter where callname = ?";
		 List<CallCenter> lstvalue = controller.find(sql, value);
		 if (lstvalue == null || lstvalue.size()> 0) 
			 return (CallCenter)lstvalue.get(0);
		 else
			 return null;
	}
	

}
