package com.vietek.trackingOnline.tracker;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.zkoss.zul.Image;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.GPSOnlineParamsController;
import com.vietek.taxioperation.model.GPSOnlineParams;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.MapUtils;

public class OnlineInfo {
//	private GPSOnline gpsOnline;
	private VMarker marker;
	private TrackingRDS2Json rds2Json;
	private GPSOnlineParams gpsOnlineParams;
	private String content;
	RevenueInfo revenueInfo;

	public OnlineInfo(int deviceId, VMarker marker) {
//		this.gpsOnline = getGPSOnline(deviceId);
		this.marker = marker;
		this.rds2Json = MapCommon.TRACKING_RDS.get(deviceId);
		this.revenueInfo = getRevenue(deviceId);
		GPSOnlineParamsController gpsOnlineParamsController = (GPSOnlineParamsController) ControllerUtils
				.getController(GPSOnlineParamsController.class);
		gpsOnlineParams = gpsOnlineParamsController.get(GPSOnlineParams.class, rds2Json.getDeviceId());
		setContent();
	}

//	private GPSOnline getGPSOnline(int deviceId) {
//		GPSOnline online = null;
//		try {
//			GPSOnlineController controller = (GPSOnlineController) ControllerUtils
//					.getController(GPSOnlineController.class);
//			online = controller.find("from GPSOnline where deviceID=?", deviceId).get(0);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			AppLogger.logTracking.error("Loi tracking", e);
//		}
//		return online;
//	}

	public void setContent() {
		StringBuffer strContent = new StringBuffer();

		strContent.append("<div class=tab_panel style='overflow-x: hidden;overflow-y: auto;'><ul class=tab_link>");
		strContent.append("<li id=" + this.marker.getId() + "_tab1><a onclick=clickTab(&#39;" + this.marker.getId()
				+ "&#39;,1)>Hiện trạng</a></li>");
		strContent.append("<li id=" + this.marker.getId() + "_tab2><a onclick=clickTab(&#39;" + this.marker.getId()
				+ "&#39;,2)>BGT</a></li>");
		strContent.append("<li id=" + this.marker.getId() + "_tab3><a onclick=clickTab(&#39;" + this.marker.getId()
				+ "&#39;,3)>Thiết bị</a></li>");
		strContent.append("<li id=" + this.marker.getId() + "_tab4><a onclick=clickTab(&#39;" + this.marker.getId()
				+ "&#39;,4)>Doanh thu</a></li></ul>");
		strContent.append("<div class=content_area><div id=" + this.marker.getId() + "_cont1 class=active>");
		strContent.append(trackingInfo()).append("</div>");
		strContent.append("<div id=" + this.marker.getId() + "_cont2 class=inactive>");
		strContent.append(driverInfo()).append("</div>");
		strContent.append("<div id=" + this.marker.getId() + "_cont3 class=inactive>");
		strContent.append(deviceInfo()).append("</div>");
		strContent.append("<div id=" + this.marker.getId() + "_cont4 class=inactive>");
		strContent.append(doanhThu()).append("</div></div></div>");
		content = strContent.toString();
	}

	public String getContent() {
		return content;
	}
	
	String label;
	public String getMarkerLabel(){
		return label;
	}

	private String trackingInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<table><tr><td class=markerOnline_left>BKS</td><td class=markerOnline_right>");
		if (rds2Json.getVehicleNumber() != null) {
			String taxiNumber = marker.getLabel();
			label = taxiNumber;
			info.append(taxiNumber);
		}

		info.append("</td></tr><tr><td class=markerOnline_left>Vị trí</td><td class=markerOnline_right>");
		if (rds2Json.getLatitude() != 0.0 || rds2Json.getLongitude() != 0.0) {
			String address = MapUtils.convertLatLongToAddrest(rds2Json.getLatitude(), rds2Json.getLongitude());
			info.append(address);
		} else {
			if (rds2Json.getLatitude() != 0 || rds2Json.getLongitude() != 0) {
				String address = MapUtils.convertLatLongToAddrest(rds2Json.getLatitude(), rds2Json.getLongitude());
				info.append(address);
			}
		}

		info.append("</td></tr><tr><td class=markerOnline_left>Thời điểm</td><td class=markerOnline_right>");
		if (rds2Json.getTimeLog() != null) {
			info.append(StringUtils.valueOfTimestamp(rds2Json.getTimeLog()));
		} 
//		else if (gpsOnline.getTimeLog() != null) {
//			info.append(StringUtils.valueOfTimestamp(gpsOnline.getTimeLog()));
//		}

		info.append("</td></tr><tr><td class=markerOnline_left>V.GPS-V.Cơ</td><td class=markerOnline_right>");
		info.append(rds2Json.getGpsSpeed()).append(" km/h - ").append(rds2Json.getMetterSpeed()).append(" km/h");
		info.append("</td></tr><tr><td class=markerOnline_left>TT máy bật/ tắt</td><td class=markerOnline_right>");

		if (rds2Json.getGpsSpeed() < 5)
			info.append("Xe dừng/đỗ");
		else {
			info.append("Xe chạy");
		}
		info.append("</td></tr><tr><td class=markerOnline_left>Động cơ</td><td class=markerOnline_right>");
		if(revenueInfo.getEngine() == 1)
			info.append("Bật");
		else
			info.append("Tắt");
			
		info.append("</td></tr><tr><td class=markerOnline_left>Điều hòa</td><td class=markerOnline_right>");
		if (revenueInfo.getAirConditioner() == 0)
			info.append("Bật");
		else
			info.append("Tắt");
		info.append("</td></tr></table>");
		return info.toString();
	}

	private String driverInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<table><tr><td class=markerOnline_left>Họ tên lái xe</td><td class=markerOnline_right>")
				.append(gpsOnlineParams.getDriverName());
		info.append("</td></tr><tr><td class=markerOnline_left>Điện thoại</td><td class=markerOnline_right>")
				.append(gpsOnlineParams.getMobileNumber());
		info.append("</td></tr><tr><td class=markerOnline_left>Số GPLX</td><td class=markerOnline_right>")
				.append(gpsOnlineParams.getRegisterNumber());
		info.append("</td></tr><tr><td class=markerOnline_left>Số lần vượt tốc</td><td class=markerOnline_right>")
				.append(revenueInfo.getTotalSpeedCount()).append(" lần");
		info.append("</td></tr><tr><td class=markerOnline_left>L.Xe trong ngày</td><td class=markerOnline_right>")
				.append(revenueInfo.getTimeDrivingPerday());
		info.append("</td></tr><tr><td class=markerOnline_left>L.Xe liên tục</td><td class=markerOnline_right>")
				.append(revenueInfo.getTimeDrivingContinuous());
		info.append("</td></tr><tr><td class=markerOnline_left>Q.đường trong ngày</td><td class=markerOnline_right>")
				.append(revenueInfo.getAllPath());
		info.append("</td></tr></table>");
		return info.toString();//
	}

	private String deviceInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<table><tr><td class=markerOnline_left>Device ID</td><td class=markerOnline_right>")
				.append(revenueInfo.getDeviceId());
		info.append("</td></tr><tr><td class=markerOnline_left>AcQuy</td><td class=markerOnline_right>")
				.append(revenueInfo.getAcquyPower());
		info.append("</td></tr><tr><td class=markerOnline_left>Pin</td><td class=markerOnline_right>")
				.append(revenueInfo.getPinPower());
		info.append("</td></tr><tr><td class=markerOnline_left>SDCard</td><td class=markerOnline_right>")
				.append(revenueInfo.getSDCard());
		info.append("</td></tr><tr><td class=markerOnline_left>Sim</td><td class=markerOnline_right>")
				.append(getSimNumber(revenueInfo.getDeviceId()));
		info.append("</td></tr></table>");
		return info.toString();
	}
	
	private String doanhThu(){
		StringBuffer info = new StringBuffer();
		info.append("<table>");
		info.append("<tr><td class=markerOnline_left>Giờ GPS</td><td class=markerOnline_right>");
		info.append(StringUtils.valueOfTimestamp(rds2Json.getTimeLog()));
		info.append("</td></tr><tr><td class=markerOnline_left>Đồng hồ</td><td class=markerOnline_right>");
		info.append(revenueInfo.getMeterConection());
		info.append("</td></tr><tr><td class=markerOnline_left>Máy in</td><td class=markerOnline_right>");
		String print = "Bình thường";
		if (revenueInfo.getPrintState() == 0) {
			print = "Không có";        
		}
		if (revenueInfo.getPrintState() == 2) {
			print = "Hết giấy";
		}	
		info.append(print);
		info.append("</td></tr><tr><td class=markerOnline_left>Trạng thái</td><td class=markerOnline_right>");
		if(rds2Json.getInTrip()==0)
			info.append("KHÔNG KHÁCH");
		else
			info.append("CÓ KHÁCH");
		
		info.append("</td></tr><tr><td class=markerOnline_left>Km cuốc trong ca</td><td class=markerOnline_right>");
		info.append(revenueInfo.getPathTrip()*0.1).append(" km");
		info.append("</td></tr><tr><td class=markerOnline_left>Tiền cuốc</td><td class=markerOnline_right>");
		info.append(revenueInfo.getMoneyTrip()*1000).append(" VNĐ");
		info.append("</td></tr><tr><td class=markerOnline_left>Tiền ca</td><td class=markerOnline_right>");
		info.append(StringUtils.priceWithoutDecimal(revenueInfo.getTotalMoneyShift()*1000)).append(" VNĐ");
		info.append("</td></tr><tr><td class=markerOnline_left>Cuốc trong ca</td><td class=markerOnline_right>");
		info.append(revenueInfo.getTotalTrip()).append(" cuốc");
		info.append("</td></tr><tr><td class=markerOnline_left>Km rỗng trong ca</td><td class=markerOnline_right>");
		info.append(revenueInfo.getEmptyPath()*0.1).append(" km");
		Double kmvandoanh = (revenueInfo.getTripPath() + revenueInfo.getEmptyPath())*0.1;
		info.append(new DecimalFormat("###.#").format(kmvandoanh));
		info.append("</td></tr><tr><td class=markerOnline_left>Tỉ lệ</td><td class=markerOnline_right>");
		Double tilekinhdoanh = ((revenueInfo.getTripPath() * 0.1) / kmvandoanh) * 100;
		info.append(tilekinhdoanh).append(" %");
		info.append("</td></tr><tr><td class=markerOnline_left>Tổng tiền đồng hồ</td><td class=markerOnline_right>");
		info.append(StringUtils.priceWithoutDecimal(revenueInfo.getTotalMoney()*1000)).append(" VNĐ");
		info.append("</td></tr><tr><td class=markerOnline_left>Vào ca</td><td class=markerOnline_right>");
		info.append(StringUtils.valueOfTimestamp(revenueInfo.getLastShiftBegin())).append("</td></tr></table>");
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
		} finally {
			session.close();
		}

		return simNumber;
	}

	public static TrackingVehicleInfo getTrackingInfo(TrackingRDS2Json json) {
		TrackingVehicleInfo info = new TrackingVehicleInfo();
		long diffTime = (System.currentTimeMillis() - json.getTimeLog().getTime()) / 1000 / 60; // tinh
																								// phut
		if (diffTime >= 4320) { // Mat tin hieu 3 ngay tro len
			info.setImageSrc(CommonDefine.TaxiIcon.ICON_MAINTAIN_16PX);
			info.setImage(new Image(CommonDefine.TaxiIcon.ICON_MAINTAIN_16PX));
			info.setLostDigital(true);
		} else {
			// Mat gsm
			if ((diffTime >= 15 && json.getGpsSpeed() > 10) || (diffTime >= 30 && json.getGpsSpeed() <= 10)) {
				info.setImageSrc(CommonDefine.TaxiIcon.ICON_LOST_GSM);
				info.setImage(new Image(CommonDefine.TaxiIcon.ICON_LOST_GSM));
				info.setLostDigital(true);
			} else {
				// Mat gps
				if (json.getLatitude() <= 0 && json.getLongitude() <= 0) {
					info.setImageSrc(CommonDefine.TaxiIcon.ICON_LOST_GPS);
					info.setImage(new Image(CommonDefine.TaxiIcon.ICON_LOST_GPS));
					info.setLostDigital(true);
				} else { // Tin hieu bt
					info.setLostDigital(false);
					if (json.getCarType() == 1) {
						if (json.getGpsSpeed() > 5) {
							if (json.getInTrip() == 0) {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_4SEATS_NON_PROCESSING);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_4SEATS_NON_PROCESSING));
							} else {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_4SEATS_PROCESSING);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_4SEATS_PROCESSING));
							}
						} else {
							if (json.getEngine() == 0) {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_4SEATS_STOP);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_4SEATS_STOP));
							} else {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_4SEATS_PAUSE);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_4SEATS_PAUSE));
							}
						}
					} else {
						if (json.getGpsSpeed() > 5) {
							if (json.getInTrip() == 0) {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_7SEATS_NON_PROCESSING);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_7SEATS_NON_PROCESSING));
							} else {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_7SEATS_PROCESSING);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_7SEATS_PROCESSING));
							}
						} else {
							if (json.getEngine() == 0) {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_7SEATS_STOP);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_7SEATS_STOP));
							} else {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_7SEATS_PAUSE);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_7SEATS_PAUSE));
							}
						}
					}
				}
			}
		}
		return info;
	}

	public static boolean isLostDigital(TrackingRDS2Json json) {
		boolean isLostDiagital = false;

		return isLostDiagital;
	}

	public static String getImageSrc(TrackingRDS2Json json) {
		String strImg = "";
		long diffTime = (System.currentTimeMillis() - json.getTimeLog().getTime()) / 1000 / 60; // tinh
																								// phut
		if (diffTime >= 4320) { // Mat tin hieu 3 ngay tro len
			strImg = CommonDefine.TaxiIcon.ICON_MAINTAIN;
		} else {
			// Mat gsm
			if ((diffTime >= 15 && json.getGpsSpeed() > 10) || (diffTime >= 30 && json.getGpsSpeed() <= 10)) {
				strImg = CommonDefine.TaxiIcon.ICON_LOST_GSM;
			} else {
				// Mat gps
				if (json.getLatitude() <= 0 && json.getLongitude() <= 0) {
					strImg = CommonDefine.TaxiIcon.ICON_LOST_GPS;
				} else { // Tin hieu bt
					if (json.getCarType() == 1) {
						if (json.getInTrip() == 0) {
							strImg = CommonDefine.TaxiIcon.ICON_4SEATS_NON_PROCESSING;
						} else
							strImg = CommonDefine.TaxiIcon.ICON_4SEATS_PROCESSING;
					} else {
						if (json.getInTrip() == 0) {
							strImg = CommonDefine.TaxiIcon.ICON_7SEATS_NON_PROCESSING;
						} else {
							strImg = CommonDefine.TaxiIcon.ICON_7SEATS_PROCESSING;
						}
					}
				}
			}
		}
		return strImg;
	}
	
	private RevenueInfo getRevenue(int deviceId){
		RevenueInfo info = null;
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sip = (SessionImplementor) session;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sip.connection();
			if (conn != null) {
				cs = conn.prepareCall("{call txm_tracking.cmdGetOnlineDetailTms("+deviceId+")}");
				rs = cs.executeQuery();
				while(rs.next()){
					info = new RevenueInfo();
					info.setTimeLog(rs.getTimestamp("TimeLog"));
					info.setDeviceId(rs.getInt("DeviceID"));
					info.setPinPower(rs.getInt("PinPower"));
					info.setAcquyPower(rs.getInt("AcquyPower"));
					info.setDigitalStatus(rs.getInt("DigitalStatus"));
					info.setEngine(rs.getInt("Engine"));
					info.setDoor(rs.getInt("Door"));
					info.setAirConditioner(rs.getInt("AirConditioner"));
					info.setSOS(rs.getInt("SOS"));
					info.setDigital1(rs.getInt("Digital1"));
					info.setDigital2(rs.getInt("Digital2"));
					info.setGSM(rs.getInt("GSM"));
					info.setGPS(rs.getInt("GPS"));
					info.setSDCard(rs.getInt("SDCard"));
					info.setMeterConection(rs.getString("MeterConection"));
					info.setTimeDrivingPerday(rs.getString("TimeDrivingPerday"));
					info.setTimeDrivingContinuous(rs.getString("TimeDrivingContinuous"));
					info.setTolalStopCount(rs.getInt("TolalStopCount"));
					info.setTotalDoorCount(rs.getInt("TotalDoorCount"));
					info.setTotalSpeedCount(rs.getInt("TotalSpeedCount"));
					info.setTotalMoney(rs.getDouble("TotalMoney"));
					info.setTotalMoneyShift(rs.getDouble("TotalMoneyShift"));
					info.setEmptyPath(rs.getInt("EmptyPath"));
					info.setTripPath(rs.getInt("TripPath"));
					info.setTotalTrip(rs.getInt("TotalTrip"));
					info.setMoneyTrip(rs.getDouble("MoneyTrip"));
					info.setPathTrip(rs.getInt("PathTrip"));
					info.setTaxiStatus(rs.getInt("TaxiStatus"));
					info.setLinkedDevice(rs.getInt("LinkedDevice"));
					info.setInTrip(rs.getInt("InTrip"));
					info.setIrState(rs.getInt("IrState"));
					info.setPrintState(rs.getInt("PrintState"));
					info.setIrBreak(rs.getInt("IrBreak"));
					info.setShiftID(rs.getInt("ShiftID"));
					info.setStayTimeCounting(rs.getInt("StayTimeCounting"));
					info.setStayTimeString(rs.getString("StayTimeString"));
					info.setDoorTimeCounting(rs.getInt("DoorTimeCounting"));
					info.setAllPath(rs.getInt("AllPath"));
					info.setAllTripPath(rs.getInt("AllTripPath"));
					info.setLastShiftBegin(rs.getTimestamp("LastShiftBegin"));
					break;
				}
			}
		} catch (Exception e) {
			AppLogger.logMap.error("Loi lay du lieu doanh thu", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return info;
	}
}
