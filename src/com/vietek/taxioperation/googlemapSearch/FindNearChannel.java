package com.vietek.taxioperation.googlemapSearch;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.util.MapUtils;

public class FindNearChannel {

	protected double lat;
	protected double lng;
	protected List<ChannelTms> lstChanel;
	protected double minHypot = 0;
	protected int idChanel = -1;

	public int getIdChanel() {
		return idChanel;
	}

	public void setIdChanel(int idChanel) {
		this.idChanel = idChanel;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public List<ChannelTms> getLstChanel() {
		return lstChanel;
	}

	public void setLstChanel(List<ChannelTms> lstChanel) {
		this.lstChanel = lstChanel;
	}

	public void setMinHypot(double minHypot) {
		this.minHypot = minHypot;
	}

	public FindNearChannel() {

	}

	public FindNearChannel(double lat, double lng, List<ChannelTms> lstChanel) {

		setLat(lat);
		setLat(lat);
		setLstChanel(lstChanel);

	}

	public List<ChannelTms> sortListChannel() {

		return sortListChannel(lat, lng, lstChanel);
	}

	public List<ChannelTms> sortListChannel(final double lat, final double lng, List<ChannelTms> lstChanel) {
		Collections.sort(lstChanel, new Comparator<ChannelTms>() {

			@Override
			public int compare(ChannelTms o1, ChannelTms o2) {
				int retVal = 0;
				if (o1.getLongitude() == null || o1.getLatitude() == null || o2.getLongitude() == null
						|| o2.getLatitude() == null || o1.getLongitude() == 0 || o1.getLatitude() == 0
						|| o2.getLongitude() == 0 || o2.getLatitude() == 0)
					return 1;

				double distance1 = MapUtils.distance(lng, lat, o1.getLongitude(), o1.getLatitude());
				double distance2 = MapUtils.distance(lng, lat, o2.getLongitude(), o2.getLatitude());
				if (distance1 < distance2)
					retVal = -1;
				else if (distance1 > distance2)
					retVal = 1;
				return retVal;
			}
		});
		return lstChanel;
	}

	public void printListSortChannel(List<ChannelTms> lst, double lat, double lng) {
		for (ChannelTms el : lst) {
			if (el.getLongitude() != null && el.getLatitude() != null) {
				double distance1 = MapUtils.distance(lng, lat, el.getLongitude(), el.getLatitude());
				AppLogger.logDebug.info("hypot:" + distance1 + ", channnel:" + el.getName());
			}
		}

	}

	public double getMinHypot(double lng, double lat, List<ChannelTms> lstChanel) {

		double hypot;
		minHypot = -1;
		idChanel = -1;
		for (ChannelTms channelTms : lstChanel) {

			if (channelTms.getLongitude() > 0 && channelTms.getLatitude() > 0) {

				hypot = MapUtils.distance(lng, lat, channelTms.getLongitude(), channelTms.getLatitude());

				if (minHypot == -1) {
					minHypot = hypot;
					idChanel = channelTms.getId();
				}

				if (minHypot > hypot) {
					minHypot = hypot;
					idChanel = channelTms.getId();
				}
			}
		}
		return idChanel;
	}
	
	public ChannelTms getChannel(double lng, double lat, List<ChannelTms> lstChannel) {
		double hypot = 0;
		double minDistance = -1;
		if(lstChannel != null && lstChannel.size() > 0){
			ChannelTms channel = lstChannel.get(0);
			for (int i = 0; i < lstChannel.size(); i++) {
				ChannelTms channelTms = lstChannel.get(i);
				try {
					if (channelTms.getLongitude() > 0 && channelTms.getLatitude() > 0) {
						hypot = MapUtils.distance(lng, lat, channelTms.getLongitude(), channelTms.getLatitude());
						if (minDistance == -1) {
							minDistance = hypot;
							channel = channelTms;
						}
						if(minDistance > hypot){
							minDistance = hypot;
							channel = channelTms;
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			return channel;
		}else {
			return null;
		}
		
	}
}
