package com.vietek.taxioperation.dashboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;

public class TMSDateTime extends Div implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private Label lbTime;
	private Label lbDate;
	private Div divContent;
	private Button btnAction;

	public TMSDateTime() {
		this.setSclass("dash_div_datetime");
		btnAction = new Button();
		btnAction.setImage("./themes/images/nav_right.png");
		btnAction.setSclass("btn_datetime_action");
		btnAction.setParent(this);
		btnAction.addEventListener(Events.ON_CLICK, this);
		divContent = new Div();
		divContent.setStyle("width: 250px; height:100%");
		divContent.setParent(this);
		Div div = new Div();
		div.setWidth("100%");
		div.setStyle("text-align: center");
		div.setParent(divContent);
		lbTime = new Label();
		lbTime.setSclass("dash_time");
		lbTime.setParent(div);
		lbDate = new Label();
		lbDate.setSclass("dash_date");
		lbDate.setParent(divContent);
		timer = new Timer(1000);
		timer.setParent(this);
		timer.setRepeats(true);
		timer.addEventListener(Events.ON_TIMER, this);
		timer.start();
	}

	private void showDateTime() {
		Date date = new Date(System.currentTimeMillis());
		DateFormat dateFormat = new SimpleDateFormat("HH : mm : ss");
		String strDate = dateFormat.format(date);
		lbTime.setValue(strDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		StringBuffer day = new StringBuffer(changeDay(dayOfWeek));
		dateFormat = new SimpleDateFormat("dd");
		day.append(", ngày ").append(dateFormat.format(date));
		dateFormat = new SimpleDateFormat("MM");
		day.append(" tháng ").append(dateFormat.format(date));
		dateFormat = new SimpleDateFormat("yyyy");
		day.append(" năm ").append(dateFormat.format(date));
		
		lbDate.setValue(day.toString());
	}

	private String changeDay(int day) {
		String dayOfWeek = "";
		switch (day) {
		case 1:
			dayOfWeek = "Chủ nhật";
			break;
		case 2:
			dayOfWeek = "Thứ hai";
			break;
		case 3:
			dayOfWeek = "Thứ ba";
			break;
		case 4:
			dayOfWeek = "Thứ tư";
			break;
		case 5:
			dayOfWeek = "Thứ năm";
			break;
		case 6:
			dayOfWeek = "Thứ sáu";
			break;
		case 7:
			dayOfWeek = "Thứ bảy";
			break;
		}
		return dayOfWeek;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if(event.getName().equals(Events.ON_TIMER)){
			showDateTime();
		} else if(event.getName().equals(Events.ON_CLICK)){
			if(event.getTarget().equals(btnAction)){
				if(divContent.isVisible()){
					divContent.setVisible(false);
					btnAction.setImage("./themes/images/nav_left.png");
				}
				else{
					divContent.setVisible(true);
					btnAction.setImage("./themes/images/nav_right.png");
				}
			}
		}
	}

}
