package com.vietek.taxioperation.dashboard;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.mq.TaxiOrderMQ;
import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.ImageUtils;
import com.vietek.taxioperation.util.MapUtils;

public class DashboardNotification extends Div implements EventListener<Event>, AfterCompose {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vlayout vlBoard;

	public DashboardNotification() {

	}

	@Override
	public void afterCompose() {
		init();

		TaxiOrderMQ.subcribleCreated(this);
		TaxiOrderMQ.subcribleUpdated(this);
	}

	private void init() {
		Div divMain = new Div();
		divMain.setStyle("width:100%;background: white;");
		divMain.setParent(this);

		Div div = new Div();
		div.setSclass("div_notif_board");
		div.setParent(divMain);
		vlBoard = new Vlayout();
		vlBoard.setSclass("dash_notif_board");
		vlBoard.setStyle("width:100%");
		vlBoard.setDraggable("true");
		vlBoard.setParent(div);
	}

	public Vlayout getBoard() {
		return this.vlBoard;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		try {
			if (event.getName().equals(TaxiOrderMQ.TAXI_ORDER_NEW_SAVED_EVENT)) {
				if (!Env.getHomePage().getDesktop().isAlive() || this.getPage() == null) {
					TaxiOrderMQ.unSubcribleCreated(this);
					TaxiOrderMQ.unSubcribleUpdated(this);
					return;
				}
				TaxiOrder order = (TaxiOrder) event.getData();
				if (order != null) {
					if (vlBoard.getChildren().size() >= 100) {
						vlBoard.removeChild(vlBoard.getChildren().get(vlBoard.getChildren().size() - 1));
					}
					addNotifNewOrder(order);
				}
			} else if (event.getName().equals(TaxiOrderMQ.TAXI_ORDER_UPDATED_EVENT)) {
				if (!Env.getHomePage().getDesktop().isAlive() || this.getPage() == null) {
					TaxiOrderMQ.unSubcribleCreated(this);
					TaxiOrderMQ.unSubcribleUpdated(this);
					return;
				}
				TaxiOrder order = (TaxiOrder) event.getData();
				if (order != null) {
					if (vlBoard.getChildren().size() >= 100) {
						vlBoard.removeChild(vlBoard.getChildren().get(vlBoard.getChildren().size() - 1));
					}
					if (order.getStatus() == 2) { // Xe dang ky don
						addNotifRegisterTaxi(order);
					} else if (order.getStatus() == 3) { // Xe da don
						addNotifPickedTaxi(order);
					} else if (order.getStatus() == 4) { // Xe da tra khach
						addNotifFinishOrder(order);
					} else if (order.getStatus() == 0) { // Da huy
						addNotifCancelOrder(order);
					}
					// else if (order.getStatus() == 1) {
					// addNotifNewOrder(order);
					// }
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("DashboardNotifycation", e);
		}
	}

	public void addNotifDriverPickUpGuest(Trip trip) {
		StringBuffer noun = new StringBuffer();
		StringBuffer content = new StringBuffer();
		if (trip.getOrder() != null) {
			if (trip.getTaxi().getDriver() != null) {
				if (trip.getTaxi() != null) {
					if (trip.getTaxi().getVehicle() != null) {
						noun.append("Số tài: ").append(trip.getTaxi().getVehicle().getValue());
						content.append(" vừa đón khách vãng lai tại ");
						if (StringUtils.isNotEmpty(trip.getOrder().getBeginAddress()))
							content.append(trip.getOrder().getBeginAddress());
						else
							content.append(trip.getOrder().getBeginOrderLat()).append(", ")
									.append(trip.getOrder().getBeginOrderLon());
						Hlayout hlayout = createNotif(noun.toString(), trip.getTaxi().getDriver().getAvatar(),
								content.toString(), null, null);
						if (vlBoard.getChildren().size() > 0) {
							vlBoard.insertBefore(hlayout, vlBoard.getChildren().get(0));
						} else {
							vlBoard.appendChild(hlayout);
						}
						Clients.scrollIntoView(vlBoard.getChildren().get(0));
					}
				}
			}
		}
	}

	public void addNotifDriverPostGuestOrder(Rider rider) {
		String noun = new String();
		StringBuffer content = new StringBuffer();
		if (rider.getCustomer() != null) {
			if (rider.getCustomer().getName().contains(":")) {
				noun = rider.getCustomer().getName().substring(rider.getCustomer().getName().indexOf("-") + 1,
						rider.getCustomer().getName().length() - 1);
				if (rider.getTrip() != null) {
					if (rider.getTrip().getPostGuestTaxi() != null)
						noun = "Số tài: " + rider.getTrip().getPostGuestTaxi().getVehicle().getValue();
				}
				content.append(" đã báo có khách vẫy");
				if (rider.getTrip() != null) {
					if (rider.getTrip().getOrder() != null) {
						TaxiOrder order = rider.getTrip().getOrder();
						if (StringUtils.isNotEmpty(order.getBeginAddress())) {
							content.append(" tại ").append(order.getBeginAddress());
						} else {
							content.append(" tại ").append(order.getBeginOrderLat()).append(", ")
									.append(order.getBeginOrderLon());
						}
					}
				}
				Hlayout hlayout = createNotif(noun, null, content.toString(), null, null);
				if (vlBoard.getChildren().size() > 0) {
					vlBoard.insertBefore(hlayout, vlBoard.getChildren().get(0));
				} else {
					vlBoard.appendChild(hlayout);
				}
				Clients.scrollIntoView(vlBoard.getChildren().get(0));
			} else {
				noun = "Khách vãng lai";
				if (rider.getTrip() != null) {
					if (rider.getTrip().getOrder() != null) {
						TaxiOrder order = rider.getTrip().getOrder();
						if (StringUtils.isNotEmpty(order.getBeginAddress())) {
							content.append(" đặt xe tại ").append(order.getBeginAddress());
						} else {
							content.append(" đặt xe tại ").append(order.getBeginOrderLat()).append(", ")
									.append(order.getBeginOrderLon());
						}
						Hlayout hlayout = createNotif(noun, null, content.toString(), "", null);
						if (vlBoard.getChildren().size() > 0) {
							vlBoard.insertBefore(hlayout, vlBoard.getChildren().get(0));
						} else {
							vlBoard.appendChild(hlayout);
						}
						Clients.scrollIntoView(vlBoard.getChildren().get(0));
					}
				}
			}
		}
	}

	private void addNotifNewOrder(TaxiOrder order) {
		StringBuffer customerName = new StringBuffer("");
		byte[] imageByte = null;
		if (order.getCustomer() != null) {
			customerName.append("Khách: ");
			if (StringUtils.isNotEmpty(order.getCustomer().getName()))
				customerName.append(order.getCustomer().getName());
			else
				customerName.append(order.getCustomer().getPhoneNumber());
			imageByte = order.getCustomer().getAvatar();
		} else if (order.getPhoneNumber() != null) {
			customerName.append("Khách: ");
			customerName.append(order.getPhoneNumber());
		}
		String content = "";
		switch (order.getOrderType()) {
		case 1:
			content = " đã đặt xe qua tổng đài";
			break;
		case 2:
			content = " đã đặt xe qua app mobile";
			break;
		case 3:
			content = " đã đặt xe qua web";
			break;
		case 4:
			content = " đã đặt xe qua sms";
			break;
		case 5:
			content = " đã đặt xe qua chat";
			break;
		}
		if (customerName.toString().length() > 0) {
			Hlayout hlayout = createNotif(customerName.toString(), imageByte, content, null, null);
			if (vlBoard.getChildren().size() > 0) {
				vlBoard.insertBefore(hlayout, vlBoard.getChildren().get(0));
			} else {
				vlBoard.appendChild(hlayout);
			}
			Clients.scrollIntoView(vlBoard.getChildren().get(0));
		}
	}

	private void addNotifRegisterTaxi(TaxiOrder order) {
		StringBuffer driverStaff = new StringBuffer();
		if (order.getRegistedTaxis() != null) {
			if (order.getRegistedTaxis().size() > 0) {
				driverStaff.append("Số tài: ");
				for (Vehicle vehicle : order.getRegistedTaxis()) {
					driverStaff.append(vehicle.getValue()).append(", ");
					// Driver driver = getDriver(vehicle.getDeviceId());
					// if (driver != null)
					// driverStaff.append("Số tài: ")
					// .append(driver.getStaffCard()).append(", ");
				}
				String drivers = "";
				if (driverStaff.length() > 0)
					drivers = driverStaff.substring(0, driverStaff.length() - 2);
				if (drivers.length() > 0) { // Co tai xe dang ky don
					String cusName = "";
					// Khach vang lai do tai bao
					if (order.getPhoneNumber() == null && order.getCustomer() == null) {
						Hlayout hlayout = createNotif(drivers, null, " đã đăng ký đón ", "Khách vãng lai", null);
						vlBoard.appendChild(hlayout);
						Clients.scrollIntoView(hlayout);
					} else {
						if (order.getPhoneNumber() != null)
							cusName = order.getPhoneNumber();
						if (order.getCustomer() != null) {
							cusName = order.getCustomer().getName();
						}
						if (vlBoard.getChildren().size() >= 100) {
							vlBoard.removeChild(vlBoard.getChildren().get(vlBoard.getChildren().size() - 1));
						}
						Hlayout hlayout = createNotif(drivers, null, " đã đăng ký đón khách ", cusName, null);
						if (vlBoard.getChildren().size() > 0) {
							vlBoard.insertBefore(hlayout, vlBoard.getChildren().get(0));
						} else {
							vlBoard.appendChild(hlayout);
						}
						Clients.scrollIntoView(vlBoard.getChildren().get(0));
					}
				}
			}
		}

	}

	private void addNotifPickedTaxi(TaxiOrder order) {
		String cusName = ""; // order.getPhoneNumber();
		String driver = "";
		byte[] imgDriver = null;
		if (order.getDriver() != null) {
			// driver="Số tài: " + order.getDriver().getStaffCard().toString();
			imgDriver = order.getDriver().getAvatar();
		}

		if (order.getPickedTaxi() != null) {
			driver = "Số tài: " + order.getPickedTaxi().getValue();
		}
		// Khach vang lai do tai bao
		if (order.getPhoneNumber() == null && order.getCustomer() == null) {
			if (driver.length() > 0) {
				Hlayout hlayout = createNotif(driver, imgDriver, " đã đón ", "Khách vãng lai", null);
				if (vlBoard.getChildren().size() > 0) {
					vlBoard.insertBefore(hlayout, vlBoard.getChildren().get(0));
				} else {
					vlBoard.appendChild(hlayout);
				}
				Clients.scrollIntoView(vlBoard.getChildren().get(0));
			}
		} else {
			cusName = order.getPhoneNumber();
			if (StringUtils.isNotEmpty(order.getCustomer().getName())) {
				cusName = order.getCustomer().getName();
			}
			if (driver.length() > 0) {
				Hlayout hlayout = createNotif(driver, imgDriver, " đã đón khách ", cusName, null);
				if (vlBoard.getChildren().size() > 0) {
					vlBoard.insertBefore(hlayout, vlBoard.getChildren().get(0));
				} else {
					vlBoard.appendChild(hlayout);
				}
				Clients.scrollIntoView(vlBoard.getChildren().get(0));
			}
		}

	}

	private void addNotifFinishOrder(TaxiOrder order) {
		if (order.getDriver() != null) {
			String driver = "Số tài: ";
			byte[] imgDriver = order.getDriver().getAvatar();
			if (order.getPickedTaxi() != null) {
				driver = "Số tài: " + order.getPickedTaxi().getValue();
			} else if (order.getVehicle() != null) {
				driver = "Số tài: " + order.getVehicle().getValue();
			}
			String cusName = "";
			StringBuffer endAdd = new StringBuffer();
			if (order.getEndLat() > 0 && order.getEndLon() > 0) {
				endAdd.append(" tại ");
				if (StringUtils.isNotEmpty(order.getEndAddress())) {
					endAdd.append(order.getEndAddress());
				} else {
					String add = MapUtils.convertLatLongToAddrest(order.getEndLat(), order.getEndLon());
					endAdd.append(add);
				}
			}
			Hlayout hlayout;
			if (order.getPhoneNumber() == null && order.getCustomer() == null) {
				if (endAdd.toString().length() == 0)
					hlayout = createNotif(driver, imgDriver, " đã trả khách ", "Khách vãng lai", null);
				else
					hlayout = createNotif(driver, imgDriver, " đã trả khách ", "Khách vãng lai", endAdd.toString());
				if (vlBoard.getChildren().size() > 0) {
					vlBoard.insertBefore(hlayout, vlBoard.getChildren().get(0));
				} else {
					vlBoard.appendChild(hlayout);
				}
				Clients.scrollIntoView(vlBoard.getChildren().get(0));
			} else {
				cusName = order.getPhoneNumber();
				if (StringUtils.isNotEmpty(order.getCustomer().getName())) {
					cusName = order.getCustomer().getName();
				}
				if (endAdd.toString().length() == 0)
					hlayout = createNotif(driver, imgDriver, " đã trả khách ", cusName, null);
				else
					hlayout = createNotif(driver, imgDriver, " đã trả khách ", cusName, endAdd.toString());
				if (vlBoard.getChildren().size() > 0) {
					vlBoard.insertBefore(hlayout, vlBoard.getChildren().get(0));
				} else {
					vlBoard.appendChild(hlayout);
				}
				Clients.scrollIntoView(vlBoard.getChildren().get(0));
			}
		}
	}

	private void addNotifCancelOrder(TaxiOrder order) {
		StringBuffer customerName = new StringBuffer();
		byte[] imageByte = null;
		if (order.getPhoneNumber() == null && order.getCustomer() == null) {
			customerName.append("Khách vãng lai");
		} else {
			customerName.append("Khách: ");
			if (order.getCustomer() != null) {
				if (StringUtils.isNotEmpty(order.getCustomer().getName()))
					customerName.append(order.getCustomer().getName());
				else
					customerName.append(order.getCustomer().getPhoneNumber());
				imageByte = order.getCustomer().getAvatar();
			} else
				customerName.append(order.getPhoneNumber());
		}

		StringBuffer content = new StringBuffer();
		content.append(" đặt cuốc đã bị hủy");
		switch (order.getCancelReason()) {
		case 1:
			content.append("do: Khách hàng huỷ");
			break;
		case 2:
			content.append("do: Không có tài xế");
			break;
		case 3:
			content.append("do: Không đón được khách");
			break;
		case 4:
			content.append("do: Đón khách khác");
			break;
		case 5:
			content.append("");
			break;
		}
		Hlayout hlayout = createNotif(customerName.toString(), imageByte, content.toString(), null, null);
		if (vlBoard.getChildren().size() > 0) {
			vlBoard.insertBefore(hlayout, vlBoard.getChildren().get(0));
		} else {
			vlBoard.appendChild(hlayout);
		}
		Clients.scrollIntoView(vlBoard.getChildren().get(0));
	}

	public Hlayout createNotif(String noun, byte[] imageByte, String content1, String obj, String content2) {
		if (vlBoard.getChildren().size() > 0) {
			Separator separator = new Separator("horizontal");
			separator.setStyle("height: 1px; background: darkgray; margin: 0px 5% 0px 5%;");
			vlBoard.insertBefore(separator, vlBoard.getChildren().get(0));
		}
		Hlayout hlayout = new Hlayout();
		hlayout.setSclass("hl_comp_notifboard");
		Div div = new Div();
		div.setSclass("left_hl_comp_notifboard");
		div.setParent(hlayout);
		Image image = new Image();
		image.setSclass("img_comp_notifboard");
		if (imageByte == null)
			image.setSrc("./themes/images/mlg_circle.png");
		else {
			try {
				String imageEncode = new String(imageByte, StandardCharsets.UTF_8);
				AImage aImage = ImageUtils.convertString2AImage(imageEncode);
				image.setContent(aImage);
			} catch (Exception e) {
				image.setSrc("./themes/images/mlg_circle.png");
			}
		}
		image.setParent(div);

		Vlayout vlayout = new Vlayout();
		vlayout.setWidth("85%");
		vlayout.setParent(hlayout);
		div = new Div();
		div.setSclass("right_hl_comp_notifboard");
		div.setParent(vlayout);
		Label label = new Label(noun);
		label.setSclass("img_cus_comp_notifboard");
		label.setParent(div);
		label = new Label(content1);
		label.setParent(div);
		if (obj != null) {
			label = new Label(obj);
			label.setSclass("img_cus_comp_notifboard");
			label.setParent(div);
		}
		if (content2 != null) {
			label = new Label(content2);
			label.setParent(div);
		}
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		label = new Label(dateFormat.format(new Date(System.currentTimeMillis())));
		label.setSclass("date_comp_notifboard");
		label.setParent(vlayout);
		return hlayout;
	}

	// private Driver getDriver(int deviceId) {
	// Driver result = null;
	// int driverId = FunctionCommon.getDriverIdWs(deviceId + "");
	// if (driverId != -1) {
	// DriverController controller = (DriverController) ControllerUtils
	// .getController(DriverController.class);
	// List<Driver> lstDriver = controller.find(
	// "from Driver where id = ?", driverId);
	// if (lstDriver != null && !lstDriver.isEmpty()) {
	// result = lstDriver.get(0);
	// }
	// }
	// if (result == null) {
	// }
	// return result;
	// }

}