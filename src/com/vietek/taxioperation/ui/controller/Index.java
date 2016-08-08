package com.vietek.taxioperation.ui.controller;

import org.zkoss.zkmax.zul.Portalchildren;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.East;
import org.zkoss.zul.Include;
import org.zkoss.zul.North;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

/**
 * author Viet Ha Ca
 */
public class Index extends Div {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Borderlayout mainLayout;

	public Index() {
		super();
		// setStyle("width:100%; height: 100%; position: absolute;
		// background-image: url('./themes/images/homepage_bgr.png');
		// background-size: contain; position: absolute; background-position:
		// center; background-repeat: no-repeat; z-index: 0;");

		// this.setStyle("overflow:scroll");
		this.init();
		setStyle("height: 100%");
	}
	
	private void init() {
		mainLayout = new Borderlayout();
		mainLayout.setParent(this);
		
		Center centerMain=new Center();
		centerMain.setStyle("height: 100%;");
		centerMain.setParent(mainLayout);
		
		East east = new East();
		east.setTitle("Các hoạt động gần đây");
		east.setCollapsible(true);
		east.setSclass("east_notifboard");
		east.setSize("25%");
		east.setStyle(" top: 0px !important; height: 100%;    border: 1px solid #cfcfcf !important; border-left: none !important;");
		east.setParent(mainLayout);
		
		Include include = new Include("/zul/tms_notification.zul");
		include.setSclass("inc_notif_board");
		include.setParent(east);
		
		Borderlayout borderlayout=new Borderlayout();
		borderlayout.setParent(centerMain);
		
		
		
		North north = new North();
		north.setTitle("thống kê đặt xe hôm nay");
		north.setSclass("north_statistic_order");
		north.setParent(borderlayout);
		north.setCollapsible(false);
		include = new Include("/zul/statistic_order_day.zul");
		include.setParent(north);
		
		Center center = new Center();
		center.setSclass("center_home_board");
		center.setParent(borderlayout);
		
		Div div=new Div();
		div.setSclass("div_center");
		div.setParent(center);
		Portallayout portallayout = new Portallayout();
		portallayout.setParent(div);
		portallayout.setStyle("overflow-y: auto; height: auto;");
		portallayout.setMaximizedMode("whole");
		// portallayout.setStyle("overflow:scroll");

		Portalchildren portalChild = new Portalchildren();
		portalChild.setParent(portallayout);
		// portalChild.setWidth("60%");
		portalChild.setStyle("padding: 5px;");
		portalChild.setHflex("1");
		portalChild.setVflex("1");
		Panel panel = new Panel();
		panel.setSclass("panel_child_center");
		panel.setParent(portalChild);
		panel.setTitle("Khách hàng online");
		panel.setBorder("normal");
		// panel.setCollapsible(true);
		// panel.setMaximizable(true);
		// panel.setClosable(true);
		// panel.setHeight("300px");
		Panelchildren panelChild = new Panelchildren();
		panelChild.setParent(panel);
		Include incl = new Include("/zul/RiderOnline.zul");
		incl.setParent(panelChild);

		panel = new Panel();
		panel.setSclass("panel_child_center");
		panel.setParent(portalChild);
		panel.setTitle("Trạng thái các cuốc đang xử lý tự động");
		panel.setBorder("normal");
		panelChild = new Panelchildren();
		panelChild.setParent(panel);
		incl = new Include("/zul/TripIsProcessing.zul");
		incl.setParent(panelChild);

		portalChild = new Portalchildren();
		portalChild.setParent(portallayout);
		// portalChild.setWidth("40%");
		portalChild.setStyle("padding: 5px;");
		portalChild.setHflex("1");
		portalChild.setVflex("1");

		panel = new Panel();
		panel.setSclass("panel_child_center");
		panel.setParent(portalChild);
		panel.setTitle("Tài xế online");
		panel.setBorder("normal");		
		// panel.setHeight("500px");
		panelChild = new Panelchildren();
		panelChild.setParent(panel);
		incl = new Include("/zul/TaxiOnline.zul");
		incl.setParent(panelChild);

		panel = new Panel();
		panel.setSclass("panel_child_center");
		panel.setParent(portalChild);
		panel.setTitle("Biến động lượng khách đặt xe theo thời gian");
		panel.setBorder("normal");
		// panel.setHeight("380px");
		panelChild = new Panelchildren();
		panelChild.setParent(panel);
		incl = new Include("/zul/RiderVariation.zul");
		incl.setParent(panelChild);
	}
	
}
