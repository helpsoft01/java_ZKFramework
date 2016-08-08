package com.vietek.taxioperation.ui.controller;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.CustomerFeedBack;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.Env;

public class CustomerFeedBackDetail extends BasicDetailWindow {

	public CustomerFeedBackDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setWidth("400px");
		this.setTitle("Đánh giá từ người dùng");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Combobox cmbcustomer;
	private Combobox cmbdriver;

	@Override
	public void createForm() {
			Grid grid = new Grid();
			grid.setParent(this);
			Columns cols = new Columns();
			cols.setParent(grid);
			Column col = new Column();
			col.setWidth("25%");
			col.setParent(cols);
			col = new Column();
			col.setWidth("5%");
			col.setParent(cols);
			col = new Column();
			col.setWidth("70%");
			col.setParent(cols);
			Rows rows = new Rows();
			rows.setParent(grid);
			Row row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Loại đánh giá"));
			row.appendChild(new Label(""));
			Editor editor = getMapEditor().get("feedbacktype");
			final Combobox comp = (Combobox)editor.getComponent();
			comp.setReadonly(true);
			comp.addEventListener("onSelect", new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					if (comp.getSelectedIndex() == 0) {
				          cmbdriver.setDisabled(true);
				          cmbcustomer.setDisabled(false);
						
					} else if (comp.getSelectedIndex() == 1) {
						cmbcustomer.setDisabled(true);
						 cmbdriver.setDisabled(false);
					}
				}
			});
			row.appendChild(comp);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Khách hàng"));
			row.appendChild(new Label(""));
			editor = getMapEditor().get("customer");
			cmbcustomer = (Combobox)editor.getComponent();
			row.appendChild(cmbcustomer);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Tài xế"));
			row.appendChild(new Label(""));
			editor = getMapEditor().get("driver");
			cmbdriver = (Combobox)editor.getComponent();
			row.appendChild(cmbdriver);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Điểm đánh giá"));
			row.appendChild(new Label(""));
			editor = getMapEditor().get("point");
		    Doublebox db = (Doublebox)editor.getComponent();
		    db.setPlaceholder("Điểm đánh giá từ 0 -> 10");
			row.appendChild(db);
			
			row = new Row();
			row.setParent(rows);
			row.appendChild(new Label("Nội dung"));
			row.appendChild(new Label(""));
			editor = getMapEditor().get("conten");
			Textbox tx = (Textbox)editor.getComponent();
			tx.setPlaceholder("Nội dung");
			tx.setMultiline(true);
			tx.setHeight("100px");
			row.appendChild(tx);
		
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		AbstractModel model = super.getModel();
		CustomerFeedBack modeltmp = (CustomerFeedBack) model;
		
		if (event.getTarget().equals(this.getBtn_save())) {
			if (StringUtils.isEmpty(validateInput(modeltmp))) {
				super.handleSaveEvent();
			} else {
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
	private String validateInput(CustomerFeedBack model) {
		StringBuilder msg = new StringBuilder("");

		// validate code
		if (model.getFeedType() == null) {
			msg.append(CommonDefine.FeedBackCustomer.TYPE_FEED)
					.append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		} if (model.getCustomer() == null && model.getDriver() == null) {
			msg.append(CommonDefine.FeedBackCustomer.NAME_USE)
			.append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		} if (model.getPoint() < 0 || model.getPoint() > 10) {
			msg.append(CommonDefine.FeedBackCustomer.VALUE_POIN)
			.append(CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
		}
		
		// In case save

		return msg.toString();
	}
	
}
