package com.vietek.tracking.ui.model;

import java.sql.Date;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.util.MapUtils;

public class GpsTrackingMsg {
	public static final int CO_KHACH = 1;
	public static final int KHONG_KHACH = 0;
	public final long DELTA_STOP_TIME = 300000;

	private int taxiID;
	/* Thoi gian tao ban tin */
	private Date timeLog;
	/* Index ban tin */
	private int indexMsg;
	/* Index lai xe */
	private int indexDriver;
	private int adc1;
	private int adc2;
	private int adc3;
	/* Trang thai tin hieu */
	private int digitalStatus;
	/* Duoc parse tu digitalStatus */
	private int engine;
	/* Duoc parse tu digitalStatus */
	private int door;
	/* Duoc parse tu digitalStatus */
	private int airConditioner;
	/* Duoc parse tu digitalStatus */
	private int sos;
	/* Duoc parse tu digitalStatus */
	private int digital1;
	/* Duoc parse tu digitalStatus */
	private int digital2;
	/* Duoc parse tu digitalStatus */
	private int gsm;
	/* Duoc parse tu digitalStatus */
	private int gps;
	/* Duoc parse tu digitalStatus */
	private int sdCard;
	/* Duoc parse tu digitalStatus */
	private int fM25v02;
	/* Duoc parse tu digitalStatus */
	private int s25FL256;
	/* Duoc parse tu digitalStatus */
	private int gPSAntenaShort;
	/* Duoc parse tu digitalStatus */
	private int gPSAntenaOpen;
	/* Duoc parse tu digitalStatus */
	private int option1;
	/* Duoc parse tu digitalStatus */
	private int option2;

	private Float longitude;
	private Float latitude;
	/* Van toc GPS */
	private int[] gpsSepeed;
	/* Van toc co */
	private int meterSpeed;
	/* Vi sai kinh do */
	private int[] lonDiff;
	/* Vi sai vi do */
	private int[] latDiff;
	/* Thong tin nhien lieu */
	private int sulInfo;
	/* Cuong do GSM */
	private int gsmIntensity;
	/* Cuong do GPS */
	private int gpsIntensity;
	/* Dien ap Pin */
	private int pinPower;
	/* Dien ap nguon */
	private int acquiPower;

	private int pathContinuous = 0;
	/* Quang duong di trong ngay */
	private int pathPerDay;
	/* Thoi gian lai xe trong ngay */
	private int timeDrivingPerDay;
	/* Thoi gian lai xe lien tuc */
	private int timeDrivingContinuous;

	/* Trang thai taxi */
	private int statusTaxi;
	/* Duoc parse tu statusTaxi */
	private int linkedDevice;
	/* Duoc parse tu statusTaxi */
	private int inTrip;
	/* Duoc parse tu statusTaxi */
	private int irState;
	/* Duoc parse tu statusTaxi */
	private int printState;
	/* Duoc parse tu statusTaxi */
	private int irBreak;

	/* Tong doanh thu dong ho */
	private long totalMoney;
	/* Tong doanh thu ca */
	private int totalMoneyShift;
	/* Quang duong khong khach */
	private int emptyPath;
	/* Quang duong co khach */
	private int tripPath;
	/* Tong so cuoc */
	private int totalTrip;
	/* Tien trong cuoc */
	private int moneyTrip;
	/* Quang duong trong cuoc */
	private int pathTrip;
	private Date receivetime;
	private boolean isUpdate = true;
	private byte[] rawData;
	private long rawTime;
	private double angle = 0d;
	/* Số giờ hoạt động */
	/* row id */
	private String rowid;
	private GpsTrackingMsg msgOld;
	private boolean isStartTrip = false;
	private String address = null;

	private String pathSrc = "";

	public String getPathSrc() {
		return pathSrc;
	}

	public void setPathSrc(String pathSrc) {
		this.pathSrc = pathSrc;
	}

	public boolean isStartTrip() {
		return isStartTrip;
	}

	public void setStartTrip(boolean isStartTrip) {
		this.isStartTrip = isStartTrip;
		if (isStartTrip) {
			this.pathSrc = VehicleMarker.PATH_VEHICLE_BEGIN_TRIP;
		}

	}

	public void setActivityPoint(boolean isActivityPoint) {
		this.isActivityPoint = isActivityPoint;
		if (isActivityPoint) {
			this.pathSrc = VehicleMarker.PATH_POINT_START;
		}

	}

	public boolean isActivityPoint() {
		return isActivityPoint;
	}

	public int getTaxiID() {
		return taxiID;
	}

	public void setTaxiID(int devID) {
		this.taxiID = devID;
	}

	public Date getReceivetime() {
		return receivetime;
	}

	public void setReceivetime(Date receivetime) {
		this.receivetime = receivetime;
	}

	public Date getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(Date timeLog) {
		this.timeLog = timeLog;
	}

	public int getIndexMsg() {
		return indexMsg;
	}

	public long getCountStop() {
		return countStop;
	}

	public void setCountStop(long countStop) {
		this.countStop = countStop;
	}

	public void setIndexMsg(int indexMsg) {
		this.indexMsg = indexMsg;
	}

	public int getIndexDriver() {
		return indexDriver;
	}

	public void setIndexDriver(int indexDriver) {
		this.indexDriver = indexDriver;
	}

	public int getAdc1() {
		return adc1;
	}

	public void setAdc1(int adc1) {
		this.adc1 = adc1;
	}

	public int getAdc2() {
		return adc2;
	}

	public void setAdc2(int adc2) {
		this.adc2 = adc2;
	}

	public int getAdc3() {
		return adc3;
	}

	public void setAdc3(int adc3) {
		this.adc3 = adc3;
	}

	public int getDigitalStatus() {
		return digitalStatus;
	}

	public void setDigitalStatus(int digitalStatus) {
		this.digitalStatus = digitalStatus;

		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_ENGINE) == TaxiMessageInterface.TRACKING_STATUS_ENGINE) {
			this.engine = 1;
		} else {
			this.engine = 0;
		}

		if ((digitalStatus & TaxiMessageInterface.TRACKING_STATUS_DOOR) == TaxiMessageInterface.TRACKING_STATUS_DOOR) {
			this.door = 1;
		} else {
			this.door = 0;
		}

		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_AIRCONDITIONER) == TaxiMessageInterface.TRACKING_STATUS_AIRCONDITIONER) {
			this.airConditioner = 1;
		} else {
			this.airConditioner = 0;
		}

		if ((digitalStatus & TaxiMessageInterface.TRACKING_STATUS_SOS) == TaxiMessageInterface.TRACKING_STATUS_SOS) {
			this.sos = 1;
		} else {
			this.sos = 0;
		}

		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_DIGITAL1) == TaxiMessageInterface.TRACKING_STATUS_DIGITAL1) {
			this.digital1 = 1;
		} else {
			this.digital2 = 0;
		}

		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_DIGITAL2) == TaxiMessageInterface.TRACKING_STATUS_DIGITAL2) {
			this.digital2 = 1;
		} else {
			this.digital2 = 0;
		}

		if ((digitalStatus & TaxiMessageInterface.TRACKING_STATUS_GSM) == TaxiMessageInterface.TRACKING_STATUS_GSM) {
			this.gsm = 1;
		} else {
			this.gsm = 0;
		}

		if ((digitalStatus & TaxiMessageInterface.TRACKING_STATUS_GPS) == TaxiMessageInterface.TRACKING_STATUS_GPS) {
			this.gps = 1;
		} else {
			this.gps = 0;
		}

		int value1 = 0;
		int value2 = 0;
		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_SDCARD1) == TaxiMessageInterface.TRACKING_STATUS_SDCARD1) {
			value1 = 1;
		}
		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_SDCARD2) == TaxiMessageInterface.TRACKING_STATUS_SDCARD2) {
			value2 = 1;
		}
		this.sdCard = value1 + value2;

		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_FM25V02) == TaxiMessageInterface.TRACKING_STATUS_FM25V02) {
			this.fM25v02 = 1;
		} else {
			this.fM25v02 = 0;
		}

		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_S25FL256) == TaxiMessageInterface.TRACKING_STATUS_S25FL256) {
			this.s25FL256 = 1;
		} else {
			this.s25FL256 = 0;
		}

		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_GPS_ANTENASHORT) == TaxiMessageInterface.TRACKING_STATUS_GPS_ANTENASHORT) {
			this.gPSAntenaShort = 1;
		} else {
			this.gPSAntenaShort = 0;
		}

		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_GPS_ANTENAOPEN) == TaxiMessageInterface.TRACKING_STATUS_GPS_ANTENAOPEN) {
			this.gPSAntenaOpen = 1;
		} else {
			this.gPSAntenaOpen = 0;
		}

		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_OPTION_1) == TaxiMessageInterface.TRACKING_STATUS_OPTION_1) {
			this.option1 = 1;
		} else {
			this.option1 = 0;
		}

		if ((digitalStatus
				& TaxiMessageInterface.TRACKING_STATUS_OPTION_2) == TaxiMessageInterface.TRACKING_STATUS_OPTION_2) {
			this.option2 = 1;
		} else {
			this.option2 = 0;
		}

	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public int[] getGpsSepeed() {
		return gpsSepeed;
	}

	public void setGpsSepeed(int[] spd_buf) {
		this.gpsSepeed = spd_buf;
	}

	public int getMeterSpeed() {
		return meterSpeed;
	}

	public void setMeterSpeed(int meterSpeed) {
		this.meterSpeed = meterSpeed;
	}

	public int[] getLonDiff() {
		return lonDiff;
	}

	public void setLonDiff(int[] lonDiff) {
		this.lonDiff = lonDiff;
	}

	public int[] getLatDiff() {
		return latDiff;
	}

	public void setLatDiff(int[] latDiff) {
		this.latDiff = latDiff;
	}

	public int getSulInfo() {
		return sulInfo;
	}

	public void setSulInfo(int sulInfo) {
		this.sulInfo = sulInfo;
	}

	public int getGsmIntensity() {
		return gsmIntensity;
	}

	public void setGsmIntensity(int gsmIntensity) {
		this.gsmIntensity = gsmIntensity;
	}

	public int getGpsIntensity() {
		return gpsIntensity;
	}

	public void setGpsIntensity(int gpsIntensity) {
		this.gpsIntensity = gpsIntensity;
	}

	public int getPinPower() {
		return pinPower;
	}

	public void setPinPower(int pinPower) {
		this.pinPower = pinPower;
	}

	public int getAcquiPower() {
		return acquiPower;
	}

	public void setAcquiPower(int acquiPower) {
		this.acquiPower = acquiPower;
	}

	public int getPathPerDay() {
		return pathPerDay;
	}

	public void setPathPerDay(int pathPerDay) {
		this.pathPerDay = pathPerDay;
	}

	public int getTimeDrivingPerDay() {
		return timeDrivingPerDay;
	}

	public void setTimeDrivingPerDay(int timeDrivingPerDay) {
		this.timeDrivingPerDay = timeDrivingPerDay;
	}

	public int getTimeDrivingContinuous() {
		return timeDrivingContinuous;
	}

	public void setTimeDrivingContinuous(int timeDrivingContinuous) {
		this.timeDrivingContinuous = timeDrivingContinuous;
	}

	public int getStatusTaxi() {
		return statusTaxi;
	}

	public void setStatusTaxi(int statusTaxi) {
		this.statusTaxi = statusTaxi;
		int linkedDevice = 0;
		if ((statusTaxi
				& TaxiMessageInterface.TRACKING_TAXI_STATUS_LINKED_DEVICE) == TaxiMessageInterface.TRACKING_TAXI_STATUS_LINKED_DEVICE) {
			linkedDevice = 1;
		}
		this.setLinkedDevice(linkedDevice);

		int inTrip = 1;
		if ((statusTaxi
				& TaxiMessageInterface.TRACKING_TAXI_STATUS_IN_TRIP) == TaxiMessageInterface.TRACKING_TAXI_STATUS_IN_TRIP) {
			inTrip = 0;
		}
		this.setInTrip(inTrip);

		int irState1 = 0;
		int irState2 = 0;
		if ((statusTaxi
				& TaxiMessageInterface.TRACKING_TAXI_STATUS_IR_BREAK_1) == TaxiMessageInterface.TRACKING_TAXI_STATUS_IR_BREAK_1) {
			irState1 = 1;
		}
		if ((statusTaxi
				& TaxiMessageInterface.TRACKING_TAXI_STATUS_IR_STATE_2) == TaxiMessageInterface.TRACKING_TAXI_STATUS_IR_STATE_2) {
			irState2 = 0;
		}
		this.setIrState(irState1 + irState2);

		int printState1 = 0;
		int printState2 = 0;
		if ((statusTaxi
				& TaxiMessageInterface.TRACKING_TAXI_STATUS_PRINT_STATE_1) == TaxiMessageInterface.TRACKING_TAXI_STATUS_PRINT_STATE_1) {
			printState1 = 1;
		}
		if ((statusTaxi
				& TaxiMessageInterface.TRACKING_TAXI_STATUS_PRINT_STATE_2) == TaxiMessageInterface.TRACKING_TAXI_STATUS_PRINT_STATE_2) {
			printState2 = 1;
		}
		this.setPrintState(printState1 + printState2);

		int irBreak1 = 0;
		int irBreak2 = 0;
		if ((statusTaxi
				& TaxiMessageInterface.TRACKING_TAXI_STATUS_IR_BREAK_1) == TaxiMessageInterface.TRACKING_TAXI_STATUS_IR_BREAK_1) {
			irBreak1 = 1;
		}
		if ((statusTaxi
				& TaxiMessageInterface.TRACKING_TAXI_STATUS_IR_BREAK_2) == TaxiMessageInterface.TRACKING_TAXI_STATUS_IR_BREAK_2) {
			irBreak2 = 1;
		}
		this.setIrBreak(irBreak1 + irBreak2);
	}

	public long getTotalMoney() {
		return totalMoney * 1000;
	}

	public void setTotalMoney(long totalMoney) {
		this.totalMoney = totalMoney;
	}

	public int getTotalMoneyShift() {
		return totalMoneyShift * 1000;
	}

	public void setTotalMoneyShift(int totalMoneyShift) {
		this.totalMoneyShift = totalMoneyShift;
	}

	public int getEmptyPath() {
		return emptyPath;
	}

	public void setEmptyPath(int emptyPath) {
		this.emptyPath = emptyPath;
	}

	public Integer getPathInShiff() {
		return emptyPath + tripPath;

	}

	public int getTripPath() {
		return tripPath;
	}

	public void setTripPath(int tripPath) {
		this.tripPath = tripPath;
	}

	public int getTotalTrip() {
		return totalTrip;
	}

	public void setTotalTrip(int totalTrip) {
		this.totalTrip = totalTrip;
	}

	public int getMoneyTrip() {

		return moneyTrip * 1000;
	}

	public void setMoneyTrip(int moneyTrip) {
		this.moneyTrip = moneyTrip;
	}

	public Double getPathTrip() {
		return (((Integer) this.pathTrip).doubleValue()) * 0.1;
	}

	public void setPathTrip(int pathTrip) {
		this.pathTrip = pathTrip;
	}

	public int getEngine() {
		return engine;
	}

	public void setEngine(int engine) {
		this.engine = engine;
	}

	public int getDoor() {
		return door;
	}

	public void setDoor(int door) {
		this.door = door;
	}

	public int getAirConditioner() {
		return airConditioner;
	}

	public void setAirConditioner(int airConditioner) {
		this.airConditioner = airConditioner;
	}

	public int getSos() {
		return sos;
	}

	public void setSos(int sos) {
		this.sos = sos;
	}

	public int getDigital1() {
		return digital1;
	}

	public void setDigital1(int digital1) {
		this.digital1 = digital1;
	}

	public int getDigital2() {
		return digital2;
	}

	public void setDigital2(int digital2) {
		this.digital2 = digital2;
	}

	public int getGsm() {
		return gsm;
	}

	public void setGsm(int gsm) {
		this.gsm = gsm;
	}

	public int getGps() {
		return gps;
	}

	public void setGps(int gps) {
		this.gps = gps;
	}

	public int getSdCard() {
		return sdCard;
	}

	public void setSdCard(int sdCard) {
		this.sdCard = sdCard;
	}

	public int getfM25v02() {
		return fM25v02;
	}

	public void setfM25v02(int fM25v02) {
		this.fM25v02 = fM25v02;
	}

	public int getS25FL256() {
		return s25FL256;
	}

	public void setS25FL256(int s25fl256) {
		s25FL256 = s25fl256;
	}

	public int getgPSAntenaShort() {
		return gPSAntenaShort;
	}

	public void setgPSAntenaShort(int gPSAntenaShort) {
		this.gPSAntenaShort = gPSAntenaShort;
	}

	public int getgPSAntenaOpen() {
		return gPSAntenaOpen;
	}

	public void setgPSAntenaOpen(int gPSAntenaOpen) {
		this.gPSAntenaOpen = gPSAntenaOpen;
	}

	public int getOption1() {
		return option1;
	}

	public void setOption1(int option1) {
		this.option1 = option1;
	}

	public int getOption2() {
		return option2;
	}

	public void setOption2(int option2) {
		this.option2 = option2;
	}

	public int getPathContinuous() {
		return pathContinuous;
	}

	public void setPathContinuous(int pathContinuous) {
		this.pathContinuous = pathContinuous;
	}

	public int getLinkedDevice() {
		return linkedDevice;
	}

	public void setLinkedDevice(int linkedDevice) {
		this.linkedDevice = linkedDevice;
	}

	public int getInTrip() {
		return inTrip;
	}

	public void setInTrip(int inTrip) {
		this.inTrip = inTrip;
	}

	public int getIrState() {
		return irState;
	}

	public void setIrState(int irState) {
		this.irState = irState;
	}

	public int getPrintState() {
		return printState;
	}

	public void setPrintState(int printState) {
		this.printState = printState;
	}

	public int getIrBreak() {
		return irBreak;
	}

	public void setIrBreak(int irBreak) {
		this.irBreak = irBreak;
	}

	public byte[] getRawData() {
		return rawData;
	}

	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public long getRawTime() {
		return rawTime;
	}

	public void setRawTime(long rawTime) {
		this.rawTime = rawTime;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public LatLng getLatlng() {
		return new LatLng(latitude, longitude);
	}

	public Boolean isLostGPS() {
		boolean result = false;
		if (latitude == 0 || longitude == 0) {
			result = true;
		}
		return result;
	}

	public String getRowid() {
		return rowid;
	}

	public void setRowid(String rowid) {
		this.rowid = rowid;
	}

	public String getAddress() {
		if (address == null) {
//			if (!isLostGPS()) {
//				if (msgOld != null && gpsSepeed[9] < 5) {
//					this.address = msgOld.getAddress();
//
//				} else {
					address = MapUtils.getAddressFromIMap(longitude, latitude);
//				}
//			}else {
//				address = "Mat GPS";
//			}

		}
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInTripStatus() {
		String status = "";
		if (inTrip == 0) {
			status = "Không khách";
		} else if (inTrip == 1) {
			status = "Có Khách";
		}
		return status;
	}

	public boolean isStopPoint() {
		return isStopPoint;
	}

	public void setStopPoint(boolean isStopPoint) {
		this.isStopPoint = isStopPoint;
		if (isStopPoint) {
			this.pathSrc = VehicleMarker.PATH_POINT_STOP;
		}

	}

	public long getTimeactivity() {
		return timeactivity;
	}

	public void setTimeactivity(long timeactivity) {
		this.timeactivity = timeactivity;
	}

	public long getdeltaStopTime() {
		return deltaStopTime;
	}

	public void setdeltaStopTime(long deltaStopTime) {
		this.deltaStopTime = deltaStopTime;
	}

	public GpsTrackingMsg getMsgOld() {
		return msgOld;
	}

	public void setMsgOld(GpsTrackingMsg msgOld) {
		this.msgOld = msgOld;
		calculatorTimeStop();
		calculatorTimeActivity();
		angle = MapUtils.getBearing2Point(msgOld.getLatitude(), msgOld.getLongitude(), this.getLatitude(),
				this.getLongitude());

	}

	/* Tính số giờ hoạt động */
	private long timeactivity = 0;
	private boolean isActivityPoint = false;

	private void calculatorTimeActivity() {
		this.setTimeactivity(msgOld.getTimeactivity());
		long time = timeLog.getTime() - msgOld.getTimeLog().getTime();
		if (this.getGpsSepeed()[9] >= 5) {
			timeactivity = timeactivity + time;
			if (timestop > 0) {
				setActivityPoint(true);
				timestop = 0;
			}
			deltaStopTime = 0;

		}
	}

	public boolean isPointSpecial() {
		return isActivityPoint || isStopPoint || isStartTrip;
	}

	public long getDeltaStopTime() {
		return deltaStopTime;
	}

	public void setDeltaStopTime(long deltaStopTime) {
		this.deltaStopTime = deltaStopTime;
	}

	public long getTimestop() {
		return timestop;
	}

	public void setTimestop(long timestop) {
		this.timestop = timestop;
	}

	public void updateTimeStop() {
		this.timestop = deltaStopTime;
		this.countStop++;
		if (deltaStopTime > 0) {
			this.msgOld.updateTimeStop();
			if (msgOld.deltaStopTime == 0) {
				setStopPoint(true);
			}
		}

	}

	/* Tính Số giờ dừng đỗ */
	private long deltaStopTime = 0;
	private long countStop = 0;
	private long timestop = 0;
	private boolean isStopPoint = false;

	private void calculatorTimeStop() {
		this.setTimestop(msgOld.getTimestop());
		this.setCountStop(msgOld.getCountStop());
		this.setdeltaStopTime(msgOld.getdeltaStopTime());
		long time = timeLog.getTime() - msgOld.getTimeLog().getTime();
		if (gpsSepeed[9] < 5) {
			if (this.timestop > 0) {
				timestop = timestop + time;
			} else {
				deltaStopTime = deltaStopTime + time;
				if (deltaStopTime >= DELTA_STOP_TIME) {
					this.timestop = deltaStopTime;
					this.msgOld.updateTimeStop();
					deltaStopTime = 0;
					countStop++;
				}
			}
		}
	}
}