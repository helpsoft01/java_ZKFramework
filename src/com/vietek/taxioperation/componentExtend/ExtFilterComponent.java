package com.vietek.taxioperation.componentExtend;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.model.Device;
import com.vietek.taxioperation.model.SysGroup;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TypeTablePrice;

public class ExtFilterComponent extends SelectorComposer<Div> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5676481006409365244L;

	Textbox tb;
	Checkbox cbMany2Many;
	Button btShow;
	Div divInfor;
	Div divShow;

	@Override
	public void doAfterCompose(Div comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		comp.setHeight("100%");

		initUI(comp);
		initListener();
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		// TODO Auto-generated method stub

		return super.doBeforeCompose(page, parent, compInfo);
	}

	private void initUI(Component parent) {

		divInfor = new Div();
		divInfor.setParent(parent);
		divInfor.setVflex("min");
		divInfor.setHflex("1");

		divShow = new Div();
		divShow.setParent(parent);
		divShow.setVflex("1");

		String pathClass = "com.vietek.taxioperation.model.SysUser";
		tb = new Textbox(pathClass);
		tb.setParent(divInfor);

		cbMany2Many = new Checkbox("is Many2Many");
		cbMany2Many.setParent(divInfor);

		btShow = new Button("Show");
		btShow.setParent(divInfor);
	}

	private void initListener() {
		btShow.addEventListener(Events.ON_CLICK, EVENT_CLICK_BTSHOW);
	}

	private EventListener<Event> EVENT_CLICK_BTSHOW = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			// if (cbMany2Many.isChecked()) {
			// M2MEditorExt combobox = new M2MEditorExt(tb.getValue(),"",
			// cbMany2Many.isChecked());
			// combobox.setParent(divShow);
			// combobox.setHflex("1");

			// } else {
			// M2OEditorExt combobox = new M2OEditorExt(tb.getValue());
			// combobox.setParent(divShow);
			// combobox.setHflex("1");
			// }

			O2MEditorExt combobox = new O2MEditorExt(new SysGroup(), "sysGroupLines");
			combobox.getComponent().setParent(divShow);

		}
	};

}
