package com.vietek.taxioperation.ui.controller;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;

import com.vietek.taxioperation.model.RequestBaggageFinding;
import com.vietek.taxioperation.model.RequestType;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

public class RequestBaggageFindings extends AbstractWindowPanel {

	public RequestBaggageFindings() {
		super(true);
		this.setCtrlKeys("#f2");
		this.addEventListener(Events.ON_CTRL_KEY, EVENT_FINED);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RequestBaggageFindingsDetail detailwindow;
	private BaggageFinding findwindow;

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {

		ArrayList<GridColumn> lstcols = new ArrayList<GridColumn>();
		lstcols.add(new GridColumn("Ngày nhận yêu cầu", 200, Timestamp.class, "getReceiveDate","ReceiveDate",getModelClass()));
		lstcols.add(new GridColumn("Trạng thái", 200, RequestType.class, "getRequesttype","Requesttype",getModelClass()));
		lstcols.add(new GridColumn("Tên Khách hàng", 200, String.class, "getCustomerName","CustomerName",getModelClass()));
		lstcols.add(new GridColumn("Số điện thoại", 200, String.class, "getPhoneNumber","PhoneNumber",getModelClass()));
		lstcols.add(new GridColumn("Địa chỉ đón", 200, String.class, "getBeginAddr"));
		lstcols.add(new GridColumn("Thời điểm đón", 200, Timestamp.class, "getBeginTime"));
		setGridColumns(lstcols);

	}

	@Override
	public void loadData() {
		setModelClass(RequestBaggageFinding.class);
		setStrQuery("from RequestBaggageFinding where DATE_FORMAT(ReceiveDate,'%Y-%m-%d') = DATE_FORMAT(current_date(),'%Y-%m-%d')");

	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailwindow == null) {
			detailwindow = new RequestBaggageFindingsDetail(getCurrentModel(), this);
		}
		return detailwindow;
	}

	private BaggageFinding getFindFormDetail() {
		if (findwindow == null) {
			findwindow = new BaggageFinding((RequestBaggageFinding) getCurrentModel(),this);
		}
		return findwindow;
	}

	private void showFindForm() {
		BaggageFinding formFind = getFindFormDetail();
		if (formFind != null) {
			formFind.setModel((RequestBaggageFinding) getCurrentModel());
			formFind.setParent(this);
			formFind.doModal();
		}
	}

	private EventListener<Event> EVENT_FINED = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			try {
				KeyEvent keyEvent = (KeyEvent) event;
				int keyCode = keyEvent.getKeyCode();

				switch (keyCode) {				
					case 113: // f2
						showFindForm();
						break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	};
}