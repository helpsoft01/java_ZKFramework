package com.vietek.taxioperation.util;

import com.vietek.taxioperation.model.ChannelTms;

public class DistanceChannel {

	private ChannelTms channel = null;
	private double distance = 0.0;

	public double getDistance() {

		return distance;
	}

	public double getDistance(double lng, double lat,ChannelTms channel) {

		distance = MapUtils.distance(lng, lat, channel.getLongitude(),
				channel.getLatitude());
		return distance;
	}

	public void setDistance(double hypot) {
		this.distance = hypot;
	}

	public ChannelTms getChannel() {
		return channel;
	}

	public void setChannel(ChannelTms channel) {
		this.channel = channel;
	}

}
