package com.vietek.taxioperation.realtime.command.rider;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.hibernate.Session;

import com.vietek.taxioperation.model.PassengerNotification;
import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.command.AbstractCommand;
import com.vietek.taxioperation.realtime.command.MobileCommand;
import com.vietek.taxioperation.util.ControllerUtils;

public class RiderLoginCommand extends AbstractCommand {
	public static final String COMMAND = "$riderlogin=";
	public static final String COMMAND_RETURN = "$login_success=";
	public RiderLoginCommand() {
		super(COMMAND);
	}
	
	private String phoneNumber;
	private String uuid;

	@Override
	public void parseData() {
		super.parseData();
		phoneNumber = jsonObj.getString("phoneNumber");
		uuid = jsonObj.getString("uuid");
	}

	@Override
	public void processData() {
		super.processData();
		
		Rider rider = Rider.getRider(phoneNumber);
		if (rider != null) {
			rider.setTrip(null);
			getHandler().setDevice(rider);
			rider.setHandler(getHandler());
		} else {
			getHandler().getCtx().writeAndFlush(COMMAND_RETURN + "{\"status\":-1}" + MobileCommand.END_CMD);
			return;
		}
		//Send Notification
		Session session = ControllerUtils.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<PassengerNotification> notifications = (List<PassengerNotification>) session.createQuery("from PassengerNotification order by id desc").list();
		session.close();
		PassengerNotification notification = null;
		if (notifications.size() >= 1) {
			notification = notifications.get(0);
		}
		JsonObject jsonObj = Json.createObjectBuilder()
				.add("status", 0)
				.add("notification", notification == null ? "" : notification.getMessage())
				.build();
		String cmd = COMMAND_RETURN + jsonObj + MobileCommand.END_CMD;
		rider.sendToDevice(cmd);
	}
}

