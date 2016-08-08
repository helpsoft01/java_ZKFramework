package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.tsp.TimeStampReq;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import com.vietek.taxioperation.controller.SOSNotificationController;
import com.vietek.taxioperation.model.SOSNotification;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class SOSNotifications extends AbstractWindowPanel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SOSNotificationsDetail detailWindow = null;

	public SOSNotifications() {
		super(true);
		this.setTitle("Danh mục SOS");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstGridColumn = new ArrayList<GridColumn>();
		lstGridColumn.add(new GridColumn("Tài xế",250,String.class,"getDriver"));
		lstGridColumn.add(new GridColumn("Thời gian bắt đầu",180,TimeStampReq.class,"getStartTime"));
		lstGridColumn.add(new GridColumn("Thời gian kết thúc",180,TimeStampReq.class,"getEndTime"));
		lstGridColumn.add(new GridColumn("Kinh độ",150,Double.class,"getLongtitude"));
		lstGridColumn.add(new GridColumn("Vĩ độ",150,Double.class,"getLatitude"));
		setGridColumns(lstGridColumn);
		}

	@Override
	public void loadData() {
		SOSNotificationController controller = (SOSNotificationController)ControllerUtils.getController(SOSNotificationController.class);
		List<SOSNotification> lst = controller.find("from SOSNotification");
		setLstModel(lst);
		setModelClass(SOSNotification.class);
	}
	
	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new SOSNotificationsDetail(getCurrentModel(), this);
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
