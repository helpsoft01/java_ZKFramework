package com.vietek.taxioperation.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Vlayout;

//import com.vietek.taxioperation.ui.util.TaxiOrderController;

public class CountNumberOrderThread implements Runnable  {
		Vlayout vLayCount;
		Hlayout hLayCount;
		public CountNumberOrderThread(Vlayout vl) {
			this.vLayCount = vl;
			hLayCount = new Hlayout();
		}
		public void run() {
			vLayCount.removeChild(hLayCount);
			hLayCount = new Hlayout();
			hLayCount.setParent(vLayCount);
			hLayCount.setVisible(true);
			Panel tmpPanel = new Panel();
			tmpPanel.setVflex("1");
			tmpPanel.setHflex("true");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			/*String strTime = df.format(new java.util.Date());*/
			Calendar cal = Calendar.getInstance();
			cal.setTime(new java.util.Date());
			cal.add(Calendar.MINUTE, 10);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(new java.util.Date());
			cal2.add(Calendar.MINUTE, -10);
			String queryCount = "FROM TaxiOrder WHERE TaxiOrder WHERE beginOrderTime LIKE '" + dateFormat.format(new java.util.Date())+"%' beginOrderTime < '" + df.format(cal.getTime()) +"' AND beginOrderTime > '" + df.format(cal2.getTime()) + "' ";
//			TaxiOrderController orderCtrl = (TaxiOrderController) ControllerUtils
//					.getController(TaxiOrderController.class);
//			List<TaxiOrder> orders = orderCtrl
//					.find("from TaxiOrder order by id desc");
//			setLstModel(orders);
			/*String newTime = df.format(cal.getTime());*/
			String strArangeTime =  df.format(cal2.getTime()).substring(11) + " - " +  df.format(cal.getTime()).substring(11);
			tmpPanel.setTitle("Số yêu cầu từ (" + strArangeTime + ")");
			tmpPanel.setVisible(true);
			tmpPanel.setParent(hLayCount);
			tmpPanel.setSclass("panel-success");
			Panelchildren tmpPanelChild = new Panelchildren();
			tmpPanelChild.setStyle("overflow:auto; background:red; text-align:center;");
			tmpPanel.appendChild(tmpPanelChild);
			Label lb = new Label("20");
			lb.setStyle("font-size: 50px; text-align:center");
			tmpPanelChild.appendChild(lb);
	 }  
}
