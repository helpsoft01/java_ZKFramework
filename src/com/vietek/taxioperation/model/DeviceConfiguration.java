package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import com.vietek.taxioperation.common.ApplyEditor;
import com.vietek.taxioperation.common.Searchable;

public class DeviceConfiguration {
	private int DeviceID;
	@Searchable(placehoder = "Imei")
	private String Imei;
	@Searchable(placehoder = "Tìm đội xe")
	private String GroupName;
	@Searchable(placehoder = "Tìm bãi giao ca")
	private String ParkingName;
	@Searchable(placehoder = "Tìm loại xe")
	private String TypeName;
	@Searchable(placehoder = "Tìm BKS")
	private String LicensePlate;
	@Searchable(placehoder = "Tìm Số tài")
	private String VehicleNumber;	
	private String VinNumber;
	private String DigitalValue;
	private int Digital;
	private int SpeedLimit;
	private int PulsePerKm;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private boolean CreatedFileConfig;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private boolean ReceivedConfig;
	@ApplyEditor(classNameEditor = "DateTimeEditor")
	private Timestamp LastetTimeReceivedConfig;
	
	public int getDeviceID() {
		return DeviceID;
	}
	public void setDeviceID(int deviceID) {
		DeviceID = deviceID;
	}
	public String getImei() {
		return Imei;
	}
	public void setImei(String imei) {
		Imei = imei;
	}
	public String getGroupName() {
		return GroupName;
	}
	public void setGroupName(String groupName) {
		GroupName = groupName;
	}
	public String getParkingName() {
		return ParkingName;
	}
	public void setParkingName(String parkingName) {
		ParkingName = parkingName;
	}
	public String getTypeName() {
		return TypeName;
	}
	public void setTypeName(String typeName) {
		TypeName = typeName;
	}
	public String getLicensePlate() {
		return LicensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		LicensePlate = licensePlate;
	}
	public String getVehicleNumber() {
		return VehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		VehicleNumber = vehicleNumber;
	}
	public String getVinNumber() {
		return VinNumber;
	}
	public void setVinNumber(String vinNumber) {
		VinNumber = vinNumber;
	}
	public String getDigitalValue() {
		return DigitalValue;
	}
	public void setDigitalValue(String digitalValue) {
		DigitalValue = digitalValue;
	}
	public int getDigital() {
		return Digital;
	}
	public void setDigital(int digital) {
		Digital = digital;
	}
	public int getSpeedLimit() {
		return SpeedLimit;
	}
	public void setSpeedLimit(int speedLimit) {
		SpeedLimit = speedLimit;
	}
	public int getPulsePerKm() {
		return PulsePerKm;
	}
	public void setPulsePerKm(int pulsePerKm) {
		PulsePerKm = pulsePerKm;
	}
	public boolean isCreatedFileConfig() {
		return CreatedFileConfig;
	}
	public void setCreatedFileConfig(boolean createdFileConfig) {
		CreatedFileConfig = createdFileConfig;
	}
	public boolean isReceivedConfig() {
		return ReceivedConfig;
	}
	public void setReceivedConfig(boolean receivedConfig) {
		ReceivedConfig = receivedConfig;
	}
	public Timestamp getLastetTimeReceivedConfig() {
		return LastetTimeReceivedConfig;
	}
	public void setLastetTimeReceivedConfig(Timestamp lastetTimeReceivedConfig) {
		LastetTimeReceivedConfig = lastetTimeReceivedConfig;
	}	
}

