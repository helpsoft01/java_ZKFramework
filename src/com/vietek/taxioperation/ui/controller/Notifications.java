package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.tsp.TimeStampReq;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import com.vietek.taxioperation.controller.NotificationController;
import com.vietek.taxioperation.model.Notification;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class Notifications extends AbstractWindowPanel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NotificationsDetail detailWindow = null;
	public Notifications() {
		super(true);
		this.setTitle("Danh mục thông báo");
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstColumn = new ArrayList<GridColumn>();
		lstColumn.add(new GridColumn("Chủ đề", 150, String.class, "getSubject"));
		lstColumn.add(new GridColumn("Nội dung", 200, String.class, "getContent"));
		lstColumn.add(new GridColumn("Thời gian gửi", 120, TimeStampReq.class,"getTimeSend"));
		lstColumn.add(new GridColumn("Ghi chú",200,String.class,"getNote"));
		setGridColumns(lstColumn);
	}

	@Override
	public void loadData() {
		NotificationController notificationController = (NotificationController) ControllerUtils
				.getController(NotificationController.class);
		List<Notification> lstValue = notificationController
				.find("from Notification");
		setLstModel(lstValue);
		setModelClass(Notification.class);
	}
	
	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new NotificationsDetail(getCurrentModel(), this);
		}
		return detailWindow;

	}
	@Override
	public void onEvent(Event event) throws Exception {
	
			try {
				super.onEvent(event);
			} catch (Exception e) {
				Messagebox.show(e.getMessage());
			}
	}
}
