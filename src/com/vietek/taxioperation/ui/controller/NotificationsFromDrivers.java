package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.tsp.TimeStampReq;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import com.vietek.taxioperation.controller.NotificationsFromDriverController;
import com.vietek.taxioperation.model.NotificationsFromDriver;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class NotificationsFromDrivers extends AbstractWindowPanel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5575655305943536015L;
	private NotificationsFromDriversDetail detailWindow = null;

	public NotificationsFromDrivers() {
		super(true);
		this.setTitle("Thông báo từ tài xế");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstColumn = new ArrayList<GridColumn>();
		lstColumn.add(new GridColumn("Tài xế",200,String.class,"getDriver"));
		lstColumn.add(new GridColumn("Loại thông báo",200,Integer.class,"getTypeNotification"));
		lstColumn.add(new GridColumn("Kinh độ",180,Double.class,"getLongtitude"));
		lstColumn.add(new GridColumn("Vĩ độ",180,Double.class,"getLatitude"));
		lstColumn.add(new GridColumn("Thời gian gửi",180,TimeStampReq.class,"getTimeSend"));
		setGridColumns(lstColumn);
	}

	@Override
	public void loadData() {
		NotificationsFromDriverController controller = (NotificationsFromDriverController)ControllerUtils.getController(NotificationsFromDriverController.class);
		List<NotificationsFromDriver> lst = controller.find("from NotificationsFromDriver");
		setLstModel(lst);
		setModelClass(NotificationsFromDriver.class);
	}
	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new NotificationsFromDriversDetail(getCurrentModel(), this);
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
