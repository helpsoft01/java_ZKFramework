package com.vietek.taxioperation.ui.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Splitter;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.SysUserController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.CustomerNotification;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.ui.editor.DateTimeEditor;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.TextEditor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class CustomerNotificationDetail extends BasicDetailWindow{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid grid;
	private Grid cusgrid;
	private Set<SysUser> selectedusers;
	private CustomerNotification customerNotification;
	
	public CustomerNotificationDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Thông tin chi tiết");
		this.setHflex("1");
		this.setVflex("1");
		this.setMaximizable(true);
		selectedusers = new HashSet<SysUser>();
		customerNotification = (CustomerNotification)model;
	}
	
	public void createControlGrid(Hbox hbox){
		grid = new Grid();
		grid.setHflex("6");	
		grid.setVflex("1");
		grid.setParent(hbox);
		
		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("15%");
		
		col = new Column();
		col.setParent(cols);
		col.setHflex("85%");
		
		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên thông báo"));
		row.appendChild(getMapEditor().get("name").getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tiêu đề"));
		row.appendChild(getMapEditor().get("subject").getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Ngày bắt đầu"));
		Editor editor = getMapEditor().get("begindate");
		((DateTimeEditor) editor).setSclass("date_none_time");
		row.appendChild((Datebox) editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Ngày kết thúc"));
		editor = getMapEditor().get("finishdate");
		((DateTimeEditor) editor).setSclass("date_none_time");
		row.appendChild((Datebox)editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Loại hình"));
		row.appendChild((Combobox)getMapEditor().get("type").getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Nội dung"));
		editor = getMapEditor().get("content");
		((TextEditor) editor).setRows(15);
		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kích hoạt"));
		row.appendChild((getMapEditor().get("active")).getComponent());
	}
	
	public void createCustomerGrid(Hbox hbox){
		cusgrid = new Grid();
		cusgrid.setHflex("4");
		cusgrid.setVflex("1");
		cusgrid.setParent(hbox);
		cusgrid.setMold("paging");
		cusgrid.setPageSize(15);
		cusgrid.setAutopaging(true);
		
		Columns cols = new Columns();
		cols.setParent(cusgrid);

		Column col = new Column();
		col.setParent(cols);
		col.setLabel("");
		col.setHflex("5%");
		
		col = new Column();
		col.setParent(cols);
		col.setLabel("Tài khoản");
		col.setHflex("20%");
		
		col = new Column();
		col.setParent(cols);
		col.setLabel("Tên đầy đủ");
		col.setHflex("30%");
		
		col = new Column();
		col.setParent(cols);
		col.setLabel("Công ty");
		col.setHflex("50%");
		
		cusgrid.setRowRenderer(new RowRenderer<SysUser>() {

			@Override
			public void render(Row row, SysUser sysUser, int index) throws Exception {
				// TODO Auto-generated method stub
				Checkbox onechoose = new Checkbox();
				onechoose.setChecked(false);	
				for (SysUser oneuser : customerNotification.getCustomerSets()) {
					if (oneuser.equals(sysUser)) {
						onechoose.setChecked(true);
						break;
					}
				}						
				row.appendChild(onechoose);
				row.appendChild(new Label("" + sysUser.getUserName()));
				row.appendChild(new Label("" + sysUser.getName()));
				row.appendChild(new Label(sysUser.getSysCompany() == null ? "": "" + sysUser.getSysCompany()));
				
				onechoose.addEventListener(Events.ON_CHECK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						// TODO Auto-generated method stub
						if (onechoose.isChecked()) {
							if (!selectedusers.contains(sysUser)) {
								selectedusers.add(sysUser);	
							}															
						}else{
							if (selectedusers.contains(sysUser)) {
								selectedusers.remove(sysUser);
							}
						}					
						customerNotification.setCustomerSets(selectedusers);
					}
				});
			}			
		});
		SysUserController controller = (SysUserController) ControllerUtils
				.getController(SysUserController.class);
		List<SysUser> lstModel = controller.find("from SysUser");
		cusgrid.setModel(new ListModelList<SysUser>(lstModel));
	}
	
	public void createSpliter(Hbox hbox){
		Splitter splitter = new Splitter();
		splitter.setCollapse("before");
		splitter.setParent(hbox);
	}
	
	@Override
	public void createForm(){
		Hlayout hlayout = new Hlayout();
		hlayout.setParent(this);
		
		
		Hbox hbox = new Hbox();
		hbox.setHflex("1");
		hbox.setVflex("1");
		hbox.setParent(hlayout);
		
		this.createControlGrid(hbox);
		this.createSpliter(hbox);
		this.createCustomerGrid(hbox);
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		if (customerNotification.getCustomerSets().size() == 0) {
			Messagebox.show("Thông báo chưa được gán với user nào, có tiếp tục?", "Cập nhật", new Messagebox.Button[]{
			        Messagebox.Button.YES, Messagebox.Button.NO}, Messagebox.QUESTION, clickListener);
		} else{
			this.processingData();
		}		
	}
	
	EventListener<Messagebox.ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

		@Override
		public void onEvent(Messagebox.ClickEvent clickevent) throws Exception {
			// TODO Auto-generated method stub
			if (Messagebox.Button.YES == clickevent.getButton()) {
				processingData();
			}
		}
		
	};
	
	public void processingData(){
		if (StringUtils.isEmpty(validateInput(customerNotification))) {
			this.setModel(customerNotification);
			super.handleSaveEvent();
		} else {
			Env.getHomePage().showNotification(validateInput(customerNotification), Clients.NOTIFICATION_TYPE_ERROR);
			return;
		}	
	}
	
	private String validateInput(CustomerNotification model) {
		StringBuilder msg = new StringBuilder("");

		// validate code
		if (StringUtils.isEmpty(model.getName())) {
			msg.append(CommonDefine.CustomerNotification.NOTICE_NAME).append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		} 
		else if (StringUtils.isEmpty(model.getSubject())){
			msg.append(CommonDefine.CustomerNotification.NOTICE_OBJECT).append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		}
		else if (model.getBegindate() == null) {
			msg.append(CommonDefine.CustomerNotification.NOTICE_F_DATE).append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		}
		else if (model.getFinishdate() == null) {
			msg.append(CommonDefine.CustomerNotification.NOTICE_T_DATE).append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		}
		else if (model.getBegindate().after(model.getFinishdate())) {
			msg.append(" Ngày bắt đầu không được lớn hơn ngày kết thúc!");
		}
		return msg.toString();
	}
}
