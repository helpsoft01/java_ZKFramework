package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Messagebox;

import com.vietek.taxioperation.common.EnumKeyCode;
import com.vietek.taxioperation.controller.CustomerNotificationController;
import com.vietek.taxioperation.model.CustomerNotification;
import com.vietek.taxioperation.model.SimProvider;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;

public class CustomerNotifications extends AbstractWindowPanel implements Serializable {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomerNotificationDetail customerNotificationDetail = null;
	
	public CustomerNotifications() {
		super(true);
		this.setTitle("Thông báo khách hàng");
		this.setCtrlKeys("^i");
		this.addEventListener(Events.ON_CTRL_KEY,this);
	}

	@Override
	public void initColumns() {
		// TODO Auto-generated method stub
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Tên thông báo", 200, String.class,
				"getName","name",getModelClass()));		
		lstCol.add(new GridColumn("Tiêu đề", 300, String.class,
				"getSubject","subject",getModelClass()));		
		lstCol.add(new GridColumn("Nội dung", 450, SimProvider.class, 
				"getContent","content", getModelClass()));
		lstCol.add(new GridColumn("Ngày bắt đầu", 100, Integer.class,
				"getBegindate","begindate", getModelClass()));
		lstCol.add(new GridColumn("Ngày kết thúc", 100, String.class,
				"getFinishdate", "finishdate", getModelClass()));		
		lstCol.add(new GridColumn("Loại hình", 150, String.class, 
				"getType", "type", getModelClass()));
		lstCol.add(new GridColumn("", 50, Boolean.class, "getActive"));
		
		setGridColumns(lstCol);
	}
	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		CustomerNotificationController customerNotificationController = (CustomerNotificationController) ControllerUtils
				.getController(CustomerNotificationController.class);
		List<CustomerNotification> lstvalue = customerNotificationController.find("From CustomerNotification");
		setLstModel(lstvalue);
		setModelClass(CustomerNotification.class);
	}
	@Override
	public BasicDetailWindow modifyDetailWindow(){
		if (customerNotificationDetail == null) {
			customerNotificationDetail = new CustomerNotificationDetail(getCurrentModel(), this);
		}
		return customerNotificationDetail;
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub
		
	}	
	
	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CTRL_KEY)) {
			KeyEvent keyEvent = (KeyEvent)event;
			int keyCode = keyEvent.getKeyCode();
			if (keyCode == EnumKeyCode.KEY_I.getValue()) {
				Messagebox.show("Hệ thống sẽ cập nhật thông báo, có tiếp tục?", "Thông tin dịch vụ", new Messagebox.Button[]{
				        Messagebox.Button.YES, Messagebox.Button.NO}, Messagebox.QUESTION, clickListener);
			}			
		}else {
			super.onEvent(event);
		}
	}
	
	EventListener<Messagebox.ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

		@Override
		public void onEvent(Messagebox.ClickEvent clickevent) throws Exception {
			// TODO Auto-generated method stub
			if (Messagebox.Button.YES == clickevent.getButton()) {
				new ConfigUtil().setPropValues("NOTICE_UPDATE", "1");
			}
		}
		
	};
}
