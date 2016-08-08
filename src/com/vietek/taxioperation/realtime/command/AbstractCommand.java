package com.vietek.taxioperation.realtime.command;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.vietek.taxioperation.realtime.RealtimeDevice;
import com.vietek.taxioperation.realtime.socket.MobileServerHander;

public abstract class AbstractCommand {
	private String data;
	protected String command;
	protected JsonObject jsonObj;
	private RealtimeDevice device;
	private MobileServerHander handler;
	public AbstractCommand(String command) {
		this.command = command;
	}
	public void parseData() {
		data = data.replace(command, "");
		JsonReader reader = Json.createReader(new StringReader(getData()));
		jsonObj = reader.readObject();
		reader.close();
	}
	public void processData() {
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public RealtimeDevice getDevice() {
		return device;
	}
	public void setDevice(RealtimeDevice device) {
		this.device = device;
	}
	public MobileServerHander getHandler() {
		return handler;
	}
	public void setHandler(MobileServerHander handler) {
		this.handler = handler;
	}
	
}
