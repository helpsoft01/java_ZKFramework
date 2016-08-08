package com.vietek.trackingOnline.tracker;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.GPSOnlineParamsController;
import com.vietek.taxioperation.model.GPSOnline;
import com.vietek.taxioperation.model.GPSOnlineParams;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.MapUtils;

public class GmarkerOnline {

	/**
	 * 
	 */
	private GPSOnline gpsOnline;
	private TrackingRDS2Json rds2Json;
	private GPSOnlineParams gpsOnlineParams;
	private String content;

	public GmarkerOnline(GPSOnline gpsOnline, TrackingRDS2Json trackingRDS2Json) {
		if (gpsOnline == null) {
			return;
		}
		this.gpsOnline = gpsOnline;
		this.rds2Json = trackingRDS2Json;
		GPSOnlineParamsController gpsOnlineParamsController = (GPSOnlineParamsController) ControllerUtils
				.getController(GPSOnlineParamsController.class);
		gpsOnlineParams = gpsOnlineParamsController.get(GPSOnlineParams.class, rds2Json.getDeviceId());
		setContent();
	}

	public void setContent() {
		StringBuffer strContent = new StringBuffer();
		strContent.append("<div class='tab-panel'><ul class='tab-link'><li class='active'><a href='#FirstTab'>");
		strContent.append("Hiện trạng").append("</a></li><li><a href='#SecondTab'>BGT</a></li>");
		strContent.append("<li><a href='#ThirdTab'>Thiết bị</a></li></ul><div class='content-area'>");
		strContent.append("<div id='FirstTab' class='active'>");
		strContent.append(trackingInfo()).append("</div>");
		strContent.append("<div id='SecondTab' class='inactive'>");
		strContent.append(driverInfo()).append("</div>");
		strContent.append("<div id='ThirdTab' class='inactive'>");
		strContent.append(deviceInfo()).append("</div></div></div>");
		content = strContent.toString();
	}

	public String getContent() {
		return content;
	}

	private String trackingInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<table><tr><td class='markerOnline_left'>BKS</td><td class='markerOnline_right'>")
				.append(rds2Json.getVehicleNumber());
		info.append("</td></tr><tr><td class='markerOnline_left'>Vị trí</td><td class='markerOnline_right'>");
		String address = MapUtils.convertLatLongToAddrest(rds2Json.getLatitude(), rds2Json.getLongitude());
		info.append(address);
		info.append("</td></tr><tr><td class='markerOnline_left'>Thời điểm</td><td class='markerOnline_right'>");
		info.append(StringUtils.valueOfTimestamp(gpsOnline.getTimeLog()));
		info.append("</td></tr><tr><td class='markerOnline_left'>V.GPS-V.Cơ</td><td class='markerOnline_right'>");
		info.append(rds2Json.getGpsSpeed()).append(" - ").append(rds2Json.getMetterSpeed());
		info.append("</td></tr><tr><td class='markerOnline_left'>TT máy bật/ tắt</td><td class='markerOnline_right'>");
		if (rds2Json.getMetterSpeed() < 5)
			info.append("Xe dừng/đỗ");
		else {
			info.append("Xe chạy");
		}
		info.append("</td></tr><tr><td class='markerOnline_left'>Động cơ</td><td class='markerOnline_right'>");
		if (gpsOnline.getEngine() == 0)
			info.append("Mở");
		else
			info.append("Tắt");
		info.append("</td></tr><tr><td class='markerOnline_left'>Điều hòa</td><td class='markerOnline_right'>");
		if (gpsOnline.getAirConditioner() == 0)
			info.append("Bật");
		else
			info.append("Tắt");
		info.append("</td></tr></table>");
		return info.toString();
	}

	private String driverInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<table><tr><td class='markerOnline_left'>Họ tên lái xe</td><td class='markerOnline_right'>")
				.append(gpsOnlineParams.getDriverName());
		info.append("</td></tr><tr><td class='markerOnline_left'>Điện thoại</td><td class='markerOnline_right'>")
				.append(gpsOnlineParams.getMobileNumber());
		info.append("</td></tr><tr><td class='markerOnline_left'>Số GPLX</td><td class='markerOnline_right'>")
				.append(gpsOnlineParams.getRegisterNumber());
		info.append("</td></tr><tr><td class='markerOnline_left'>Số lần vượt tốc</td><td class='markerOnline_right'>")
				.append(gpsOnline.getSpeedTimeCounting());
		info.append("</td></tr><tr><td class='markerOnline_left'>L.Xe trong ngày</td><td class='markerOnline_right'>")
				.append(gpsOnline.getTimeDrivingPerday());
		info.append("</td></tr><tr><td class='markerOnline_left'>L.Xe liên tục</td><td class='markerOnline_right'>")
				.append(gpsOnline.getTimeDrivingContinuous());
		info.append(
				"</td></tr><tr><td class='markerOnline_left'>Q.đường trong ngày</td><td class='markerOnline_right'>")
				.append(gpsOnline.getPathPerday());
		info.append("</td></tr></table>");
		return info.toString();
	}

	private String deviceInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<table><tr><td class='markerOnline_left'>Device ID</td><td class='markerOnline_right'>")
				.append(gpsOnline.getDeviceID());
		info.append("</td></tr><tr><td class='markerOnline_left'>AcQuy</td><td class='markerOnline_right'>")
				.append(gpsOnline.getAcquyPower());
		info.append("</td></tr><tr><td class='markerOnline_left'>Pin</td><td class='markerOnline_right'>")
				.append(gpsOnline.getPinPower());
		info.append("</td></tr><tr><td class='markerOnline_left'>SDCard</td><td class='markerOnline_right'>")
				.append(gpsOnline.getSDCard());
		info.append("</td></tr><tr><td class='markerOnline_left'>Sim</td><td class='markerOnline_right'>")
				.append(getSimNumber(gpsOnline.getDeviceID()));
		info.append("</td></tr></table>");
		return info.toString();
	}

	private String getSimNumber(int deviceID) {
		String simNumber = "";
		Session session = ControllerUtils.getCurrentSession();
		try {
			Query query = session.createQuery("select SimNumber from Device where id=" + deviceID + "");
			simNumber = query.list().get(0).toString();
		} catch (Exception ex) {
			simNumber = "Khong xac dinh";
		}
		session.close();
		return simNumber;
	}

}
