package com.vietek.taxioperation.dashboard;

import java.sql.Timestamp;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.VisibilityChangeEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.util.ControllerUtils;

public class StatisticOrderByDay extends SelectorComposer<Component> implements EventListener<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	Div div_statistic_order;
	private Vlayout vlMain;
	private Button btnUpdate;
	private Label lbNumSum;
	private Label lbTextPerSum;
	private Label lbNumPerSum;
	private Label lbNumSuccess;
	private Label lbTextPerSuccess;
	private Label lbNumPerSuccess;
	private Label lbPerOrder;

	private int oldSumOrder;
	private int oldOrderSuccess;
	private int newSumOrder;
	private int newOrderSuccess;

	public StatisticOrderByDay() {

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.oldSumOrder = -1;
		this.oldOrderSuccess = -1;
		init();
		loadValue();
		Timer timer2 = new Timer();
		timer2.setDelay(10000);
		timer2.setRepeats(true);
		timer2.setParent(div_statistic_order);
		timer2.addEventListener(Events.ON_TIMER, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				loadData();
			}
		});
		timer2.start();
	}

	private void init() {
		vlMain = new Vlayout();
		vlMain.setWidth("100%");
		vlMain.setSclass("vl_main_sta_order");
		vlMain.setParent(div_statistic_order);
		// Ket qua dat xe hom nay
		Div div = new Div();
		div.setSclass("sta_order_div");
		div.setParent(vlMain);
		// Label label = new Label("thống kê đặt xe hôm nay");
		// label.setSclass("sta_order_title");
		// label.setParent(div);

		Hlayout hlayout = new Hlayout();
		hlayout.setSclass("hl_sta_order");
		hlayout.setParent(vlMain);

		// Tong so cuoc
		Hlayout hlComp = new Hlayout();
		hlComp.setSclass("comp_sta_order");
		hlComp.setParent(hlayout);

		// Image image = new Image();
		// image.setSrc("./themes/images/statis_order_booking_64.png");
		// image.setSclass("img_statitis_order");
		// image.setParent(hlComp);
		Div div2 = new Div();
		div2.setSclass("img_statitis_order");
		div2.setParent(hlComp);
		lbNumSum = new Label();// Tong so cuoc
		lbNumSum.setSclass("text_main_lb_statitis_order");
		lbNumSum.setParent(div2);

		Vlayout vlChild = new Vlayout();
		vlChild.setSclass("vl_comp_static_order");
		vlChild.setParent(hlComp);

		Label label = new Label("Tổng số cuốc đặt");
		label.setSclass("label_statitis_order");
		label.setParent(vlChild);

		Hlayout hlayout2 = new Hlayout();
		hlayout2.setParent(vlChild);

		lbTextPerSum = new Label();
		lbTextPerSum.setSclass("label_statitis_order");
		lbTextPerSum.setParent(hlayout2);

		lbNumPerSum = new Label();// Tang so voi hom truoc
		lbNumPerSum.setSclass("text_lb_statitis_order");
		lbNumPerSum.setParent(hlayout2);

		// Tong so cuoc thanh cong
		hlComp = new Hlayout();
		hlComp.setSclass("comp_sta_order");
		hlComp.setParent(hlayout);
		// Image image = new Image();
		// image.setSrc("./themes/images/statis_order_success_64.png");
		// image.setSclass("img_statitis_order");
		// image.setParent(hlComp);
		div2 = new Div();
		div2.setSclass("img_statitis_order");
		div2.setParent(hlComp);
		lbNumSuccess = new Label();// Tong so cuoc
		lbNumSuccess.setSclass("text_main_lb_statitis_order");
		lbNumSuccess.setParent(div2);

		vlChild = new Vlayout();
		vlChild.setParent(hlComp);

		label = new Label("Số cuốc thành công");
		label.setSclass("label_statitis_order");
		label.setParent(vlChild);

		hlayout2 = new Hlayout();
		hlayout2.setParent(vlChild);

		lbTextPerSuccess = new Label();
		lbTextPerSuccess.setSclass("label_statitis_order");
		lbTextPerSuccess.setParent(hlayout2);

		lbNumPerSuccess = new Label();// Tang so voi hom truoc
		lbNumPerSuccess.setSclass("text_lb_statitis_order");
		lbNumPerSuccess.setParent(hlayout2);

		// Ty le cuoc thanh conng
		hlComp = new Hlayout();
		hlComp.setSclass("comp_sta_order");
		hlComp.setParent(hlayout);

		// image = new Image();
		// image.setSrc("./themes/images/statis_order_percent_64.png");
		// image.setSclass("img_statitis_order");
		// image.setParent(hlComp);
		div2 = new Div();
		div2.setSclass("img_statitis_order");
		div2.setParent(hlComp);
		lbPerOrder = new Label();// Tong so cuoc
		lbPerOrder.setSclass("text_main_lb_statitis_order");
		lbPerOrder.setStyle("font-size: 16px;");
		lbPerOrder.setParent(div2);

		vlChild = new Vlayout();
		vlChild.setParent(hlComp);

		label = new Label("Tỉ lệ cuốc thành công");
		label.setSclass("label_statitis_order");
		label.setParent(vlChild);
	}

	@SuppressWarnings({ "deprecation" })
	private void loadValue() {
		oldSumOrder = 0;
		oldOrderSuccess = 0;
		newSumOrder = 0;
		newOrderSuccess = 0;

		Timestamp startTime = new Timestamp(System.currentTimeMillis());
		Timestamp endTime = new Timestamp(System.currentTimeMillis());
		int date = new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 24).getDate();
		if (startTime.getDate() == 1) {
			startTime.setMonth(startTime.getMonth() - 1);
			endTime.setMonth(endTime.getMonth() - 1);
		}
		startTime.setDate(date);
		startTime.setHours(0);
		startTime.setMinutes(0);
		startTime.setSeconds(0);
		startTime.setNanos(0);

		endTime.setDate(date);
		endTime.setHours(23);
		endTime.setMinutes(59);
		endTime.setSeconds(59);
		endTime.setNanos(59);
		Session session = ControllerUtils.getCurrentSession();
		try {
			Query query = session.createQuery("select count(*) from TaxiOrder where beginOrderTime BETWEEN '"
					+ startTime + "' AND '" + endTime + "'");
			oldSumOrder = Integer.parseInt(query.list().get(0).toString());
			query = session.createQuery("select count(*) from TaxiOrder where beginOrderTime BETWEEN '" + startTime
					+ "' AND '" + endTime + "' AND status>=3");
			oldOrderSuccess = Integer.parseInt(query.list().get(0).toString());
			startTime = new Timestamp(System.currentTimeMillis());
			startTime.setHours(0);
			startTime.setMinutes(0);
			startTime.setSeconds(0);
			startTime.setNanos(0);
			endTime = new Timestamp(System.currentTimeMillis());
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			endTime.setNanos(59);
			query = session.createQuery("select count(*) from TaxiOrder where beginOrderTime >= '" + startTime
					+ "' AND beginOrderTime<='" + endTime + "'");
			newSumOrder = Integer.parseInt(query.list().get(0).toString());
			query = session.createQuery("select count(*) from TaxiOrder where beginOrderTime >= '" + startTime
					+ "' AND beginOrderTime<='" + endTime + "' AND status>=3");
			newOrderSuccess = Integer.parseInt(query.list().get(0).toString());
			session.close();
		} catch (Exception e) {
			session.close();
		}
		lbNumSum.setValue(String.valueOf(newSumOrder));
		if (newSumOrder - oldSumOrder < 0) {
			lbTextPerSum.setValue("Giảm ");
		} else
			lbTextPerSum.setValue("Tăng ");
		double percent = 0;
		if (oldSumOrder == 0) {
			if (newSumOrder == 0)
				lbNumPerSum.setValue("0%");
			else
				lbNumPerSum.setValue("100%");
		} else {
			percent = (double) Math.abs(newSumOrder - oldSumOrder) / oldSumOrder * 100;
			lbNumPerSum.setValue(String.format("%.2f", percent) + "%");
		}
		if (newOrderSuccess - oldOrderSuccess < 0)
			lbTextPerSuccess.setValue("Giảm ");
		else
			lbTextPerSuccess.setValue("Tăng ");
		if (oldOrderSuccess == 0) {
			if (newOrderSuccess == 0)
				lbNumPerSuccess.setValue("0%");
			else
				lbNumPerSuccess.setValue("100%");
		} else {
			percent = (double) Math.abs(newOrderSuccess - oldOrderSuccess) / oldOrderSuccess * 100;
			lbNumPerSuccess.setValue(String.format("%.2f", percent) + "%");
		}
		lbNumSuccess.setValue(String.valueOf(newOrderSuccess));
		if (newSumOrder == 0) {
			lbPerOrder.setValue("0%");
		} else {
			percent = ((double) newOrderSuccess / newSumOrder) * 100;
			lbPerOrder.setValue(String.format("%.2f", percent) + "%");
		}
	}

	@Override
	public void onEvent(Event event) throws Exception {
		try {
			if (event.getName().equals(Events.ON_VISIBILITY_CHANGE)) {
				VisibilityChangeEvent visibilityEvent = (VisibilityChangeEvent) event;
				if (visibilityEvent.isHidden()) {
					AppLogger.logDebug.info("hidden statistic order");
				}
			}
			if (event.getName().equals(Events.ON_CLICK)) {
				if (event.getTarget().equals(btnUpdate)) {
					loadValue();
				}
				if (event.getName().equals(Events.ON_TIMER)) {
					loadData();
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("Statistic", e);
		}
	}

	// class UpdateStatisticAuto extends TimerTask {
	// @Override
	// public void run() {
	// loadData();
	// }
	// }

	@SuppressWarnings("deprecation")
	private void loadData() {
		newSumOrder = 0;
		newOrderSuccess = 0;
		Timestamp startTime = new Timestamp(System.currentTimeMillis());
		Timestamp endTime = new Timestamp(System.currentTimeMillis());
		Session session = ControllerUtils.getCurrentSession();
		try {
			startTime.setHours(0);
			startTime.setMinutes(0);
			startTime.setSeconds(0);
			startTime.setNanos(0);
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			Query query = session.createQuery("select count(*) from TaxiOrder where beginOrderTime>='" + startTime
					+ "' AND beginOrderTime<= '" + endTime + "'");
			Object obj = query.list().get(0);
			if (obj != null)
				newSumOrder = Integer.parseInt(query.list().get(0).toString());
			query = session.createQuery("select count(*) from TaxiOrder where beginOrderTime>='" + startTime
					+ "' AND beginOrderTime<='" + endTime + "' AND status>=3");
			obj = query.list().get(0);
			if (obj != null)
				newOrderSuccess = Integer.parseInt(query.list().get(0).toString());
			session.close();
		} catch (Exception e) {
			session.close();
		}
		lbNumSum.setValue(String.valueOf(newSumOrder));
		if (newSumOrder - oldSumOrder < 0) {
			lbTextPerSum.setValue("Giảm ");
		} else
			lbTextPerSum.setValue("Tăng ");
		double percent = 0;
		if (oldSumOrder == 0) {
			if (newSumOrder == 0)
				lbNumPerSum.setValue("0%");
			else
				lbNumPerSum.setValue("100%");
		} else {
			percent = (double) Math.abs(newSumOrder - oldSumOrder) / oldSumOrder * 100;
			lbNumPerSum.setValue(String.format("%.2f", percent) + "%");
		}
		if (newOrderSuccess - oldOrderSuccess < 0)
			lbTextPerSuccess.setValue("Giảm ");
		else
			lbTextPerSuccess.setValue("Tăng ");
		if (oldOrderSuccess == 0) {
			if (newOrderSuccess == 0)
				lbNumPerSuccess.setValue("0%");
			else
				lbNumPerSuccess.setValue("100%");
		} else {
			percent = (double) Math.abs(newOrderSuccess - oldOrderSuccess) / oldOrderSuccess * 100;
			lbNumPerSuccess.setValue(String.format("%.2f", percent) + "%");
		}
		lbNumSuccess.setValue(String.valueOf(newOrderSuccess));
		if (newSumOrder == 0) {
			lbPerOrder.setValue("0%");
		} else {
			percent = ((double) newOrderSuccess / newSumOrder) * 100;
			lbPerOrder.setValue(String.format("%.2f", percent) + "%");
		}
	}
}