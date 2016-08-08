package com.vietek.taxioperation.componentExtend;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.South;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public abstract class WindowEditor extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3996088701915507471L;
	Button btOk;
	Button btCancel;
	Component parent;

	public WindowEditor(Component parent) {
		this.parent = parent;
		initUI();
	}

	private void initUI() {

		this.setClass("SearchEditorExt");

		Borderlayout layout = new Borderlayout();
		layout.setParent(this);

		Center center = new Center();
		center.setParent(layout);
		center.setBorder("none");

		South south = new South();
		south.setParent(layout);
		south.setSize("35px");

		south.setBorder("none");
		/**
		 * append child componet
		 */
		center.appendChild(createCenter());
		south.appendChild(createSouth());
		/**
		 * listener
		 */
		this.addEventListener(Events.ON_CLOSE, EVENTCLOSE_WINDOW);

		initUIAfter();
	}

	private Div createCenter() {
		Div result = new Div();

		result.setVflex("1");

		initDetailUI(result);
		return result;
	}

	private Div createSouth() {
		Div result = new Div();
		result.setHflex("1");
		result.setVflex("1");
		/*
		 * create
		 */
		btOk = new Button("	__OK__ ");
		btOk.setParent(result);
		btOk.setHeight("100%");
		// btOk.setHflex("1");
		btOk.setClass("btn-success");

		Label lb = new Label(" Hoặc ");
		lb.setParent(result);

		btCancel = new Button("CANCEL");
		btCancel.setParent(result);
		btCancel.setHeight("100%");
		// btCancel.setHflex("1");
		btCancel.setClass("btn-default");
		/*
		 * event
		 */
		btOk.addEventListener(Events.ON_CLICK, EVENTCLICK_OK);
		btCancel.addEventListener(Events.ON_CLICK, EVENTCLOSE_WINDOW);
		return result;
	}

	EventListener<Event> EVENTCLOSE_WINDOW = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			eventOnClose_Window();
		}
	};
	EventListener<Event> EVENTCLICK_OK = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			eventOnCLick_OK();
		}
	};

	public Component getParentBasicDetailWindow(Component child) {
		Component retVal = child.getParent();
		if (retVal != null) {
			// !retVal.getClass().getSuperclass().equals(BasicDetailWindow.class)
			// &&
			if (!retVal.getClass().equals(Div.class)) {
				retVal = getParentBasicDetailWindow(retVal);
			}
		}
		return retVal;
	}

	public void show() {
		try {

			// Events.sendEvent(onEventLoad, _this, null);

			if (this.getParent() == null) {
				Component detailWindow = (Component) getParentBasicDetailWindow(parent);
				if (detailWindow != null)
					this.setParent(detailWindow);
				else
					this.setParent(parent);
				setPosition("center,center");

				setMinimizable(false);
				setMaximizable(true);
				setClosable(true);
				setBorder("normal");
				setWidth("60%");
				setHeight("80%");
				setTitle("TÌM KIẾM MỞ RỘNG");
			}
			doModal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	};

	public abstract void initDetailUI(Component parent);

	public abstract void initUIAfter();

	public abstract void initEvents();

	public void eventOnClose_Window() {
		setVisible(false);
	}

	public void eventOnCLick_OK() {
		setVisible(false);
	}
}
