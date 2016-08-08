package com.vietek.taxioperation.chat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

import com.vietek.taxioperation.controller.TaxiOrderController;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

/**
 * 
 * @author Habv
 *
 */
public class MessageBox extends Vbox {

	private static final long serialVersionUID = 1L;

	TaxiOrderController txOrderController;
	private Label userLabel;
	private Label msgLabel;
	private Button btnDetail;
	private long date;
	private String sender;
	private String message;
	private boolean isButton;
	private int requestID;

	public MessageBox(String sender, String message, long date, boolean isButton, int requestID) {
		setSclass("messVbox");
		this.sender = sender;
		this.message = message;
		this.date = date;
		this.isButton = isButton;
		this.requestID = requestID;
		
		init();
	}

	private void init() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String strDate = dateFormat.format(new Date(date));
		userLabel = new Label();
		String str = sender + " @ " + strDate;
		userLabel.setValue(str);
		userLabel.setStyle("font-weight: bold; margin-left: 5px; color: blue;");
		userLabel.setParent(this);
		msgLabel = new Label();
		msgLabel.setValue(message);
		msgLabel.setSclass("messLabel");
		msgLabel.setMultiline(true);
		msgLabel.setParent(this);
		if (isButton) {
			if (Env.getIsDTV(Env.getUser())) {
				btnDetail = new Button();
				btnDetail.setSclass("btnDetail");
				btnDetail.setLabel("Chi tiết");
				btnDetail.setParent(this);
				btnDetail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						showDetail(requestID, true);
					}
				});
			}

		}
	}

	/**
	 * @author Habv Show detail of request
	 * @param orderId
	 *            - id of request
	 * @param isDTV
	 *            - true or false
	 */
	private void showDetail(int orderId, boolean isDTV) {
		if(txOrderController==null)
			txOrderController = (TaxiOrderController) ControllerUtils.getController(TaxiOrderController.class);
		TaxiOrder order = txOrderController.get(TaxiOrder.class, orderId);
	}

	@Override
	public void setParent(Component parent) {
		super.setParent(parent);
		Clients.scrollIntoView(msgLabel);
	}

	public Label getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(Label userLabel) {
		this.userLabel = userLabel;
	}

	public Label getMsgLabel() {
		return msgLabel;
	}

	public void setMsgLabel(Label msgLabel) {
		this.msgLabel = msgLabel;
	}

	public Button getBtnDetail() {
		return btnDetail;
	}

	public void setBtnDetail(Button btnDetail) {
		this.btnDetail = btnDetail;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
}
