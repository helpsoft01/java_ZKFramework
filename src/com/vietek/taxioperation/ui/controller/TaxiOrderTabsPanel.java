package com.vietek.taxioperation.ui.controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.CallInfo;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.TaxiOrderDetailHandler;

public class TaxiOrderTabsPanel extends Window implements TaxiOrderDetailHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8454255238287466450L;
	private Tabbox tabbox;
	private Tabs tabs;
	private Tabpanels tabpanels;
	protected TaxiOrdersForm taxiOrderForm;
	private TaxiOrderTabsPanel _this;

	public TaxiOrderTabsPanel(TaxiOrdersForm taxiOrderForm) {
		_this = this;
		this.taxiOrderForm = taxiOrderForm;
		initUI();
	}

	private void initUI() {

		tabbox = new Tabbox();
		tabs = new Tabs();
		tabpanels = new Tabpanels();

		this.setParent(this.taxiOrderForm);
		tabbox.setParent(this);
		tabs.setParent(tabbox);
		tabpanels.setParent(tabbox);

		this.setMode(Mode.POPUP);
		this.setPosition("left,top");
		this.setBorder("normal");
		this.setHeight("0%");
		this.setWidth("0%");
		this.setSclass("windowTabsDetailForm");
		this.setVisible(false);

		tabbox.setHeight("100%");
		tabbox.setWidth("100%");

		this.setCtrlKeys("^n^b");
		this.addEventListener(Events.ON_CTRL_KEY, EVENT_CTRL_KEY_FORM);
		tabbox.addEventListener(Events.ON_SELECT, EVENT_SELECTTAB);
	}

	private EventListener<Event> EVENT_SELECTTAB = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			List<Component> lst = tabbox.getSelectedPanel().getChildren();
			for (Component component : lst) {
				if (component instanceof TaxiOrdersDetailForm) {

					tabbox.getSelectedPanel().setFocus(true);
					((TaxiOrdersDetailForm) component).focusDefaultControl();

					return;
				}
			}

		}

	};

	private EventListener<Event> EVENT_CTRL_KEY_FORM = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {

			KeyEvent keyEvent = (KeyEvent) event;
			int keyCode = keyEvent.getKeyCode();

			Tab tab = null;
			switch (keyCode) {
			case 78: // ctrl + n

				tab = (Tab) tabbox.getSelectedTab().getNextSibling();

				break;
			case 66: // ctrl + b

				tab = (Tab) tabbox.getSelectedTab().getPreviousSibling();
				break;
			}
			if (tab != null) {
				tabbox.setSelectedTab(tab);
				Events.sendEvent(Events.ON_SELECT, tabbox, tab);
			}

		}

	};

	public void createTab() {
		for (int i = 0; i < 10; ++i) {

			Tab tab = new Tab();
			tab.setLabel("i:" + i);
			tab.setParent(tabs);
			tab.setId("i:" + i);
			tab.setClosable(true);

			Tabpanel tabPanel = new Tabpanel();
			tabPanel.setParent(tabpanels);
			tabPanel.setContext("i:" + i);
		}
	}

	public void showWindowCallIn(CallInfo callInfo, int position, TaxiOrder order, Desktop desktop) {

		try {

			CallInWindow callIn = new CallInWindow(taxiOrderForm, callInfo, order, desktop);
			callIn.show(position);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void addDetailForm(BasicDetailWindow detailWindow) {

		try {
			Tab tab = new Tab();
			tab.setSclass("background_colorCommon tabDetailForm");
			tab.setParent(tabs);
			tab.setImage("themes/images/logo_ml.png"); // logo_small.png
			tab.setClosable(false);

			Tabpanel tabPanel = new Tabpanel();
			tabPanel.setParent(tabpanels);

			tabPanel.appendChild(detailWindow);
			Div divDetail = new Div();
			divDetail.setParent(tabPanel);
			divDetail.setVisible(true);

			detailWindow.setMode(Mode.EMBEDDED);
			detailWindow.setListWindow(taxiOrderForm);

			((TaxiOrdersDetailForm) detailWindow).handlerTaxiOrderDetail = this;
			((TaxiOrdersDetailForm) detailWindow).setTab(tab);
			((TaxiOrdersDetailForm) detailWindow).setValue_UI(false);

			if (tabs.getTabbox() == null)
				tab.setSelected(true);

			this.setHeight("100%");
			this.setWidth("100%");

			showWindow();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void addTaxiOrder(TaxiOrdersForm parent, TaxiOrder taxiOrder, CallInfo callInfo) {

		try {
			for (Component panel :  tabpanels.getChildren()) {
				if (panel instanceof Tabpanel) {
					for (Component comp : panel.getChildren()) {
						if (comp instanceof TaxiOrdersDetailForm) {
							TaxiOrdersDetailForm detailForm = (TaxiOrdersDetailForm) comp;
							if (detailForm.getCallInfo().getCallUuid().equals(callInfo.getCallUuid())) {
								return;
							}
						}
					}
				}
			}
			TaxiOrdersDetailForm detailForm = new TaxiOrdersDetailForm(parent, taxiOrder);
			if (callInfo != null) {
				callInfo.setEvent(CallInfo.CALL_EVENT_ANSWER);
			}
			detailForm.setCallInfo(callInfo);
			addDetailForm(detailForm);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	public void removeTabDetailForm(Window detailWindow) {

	}

	public void showWindow() {

		this.setVisible(true);
		this.doModal();// Popup();
	}

	public void hiddenWindow() {

		this.setVisible(false);
	}

	public void removeTab(Tab tab) {
		tab.close();
		if (tabs.getChildren().size() == 0) {
			_this.setVisible(false);
			taxiOrderForm.setFocusDefaultForm();
			Env.getHomePage().showNotification("THÔNG TIN ĐÃ CẬP NHẬT!", Clients.NOTIFICATION_TYPE_INFO);
		}
	}

	@Override
	public void onTaxiOrderChangeTitle(Tab tab, String title) {

		tab.setLabel(title);
	}

	@Override
	public void onTaxiOrderCloseForm(Tab tab, CallInfo call) {
		removeTab(tab);
	}

}
