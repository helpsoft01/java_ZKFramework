package com.vietek.taxioperation.common.timer;

import java.util.concurrent.ConcurrentHashMap;

import com.vietek.taxioperation.realtime.Trip;

public class TripManagerWorker extends Thread{
	ConcurrentHashMap<String, Trip> mapTrips = new ConcurrentHashMap<>();
	int minute = 0;
	public TripManagerWorker(ConcurrentHashMap<String, Trip> map, int minute){
		this.mapTrips = map;
		this.minute = minute;
	}
	@Override
	public void run(){
		if(mapTrips.isEmpty()){
			return;
		}
		for (String key : mapTrips.keySet()) {
			Trip trip = mapTrips.get(key);
			if (trip.getOrder().getBeginOrderTime().getTime() < (System.currentTimeMillis() - (minute * 60l * 1000l))) {
				mapTrips.remove(key);
			}
		}
	}
	
	
}
