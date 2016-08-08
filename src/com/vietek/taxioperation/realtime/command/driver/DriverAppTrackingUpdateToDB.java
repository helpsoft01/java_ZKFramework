package com.vietek.taxioperation.realtime.command.driver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hibernate.Session;

import com.vietek.taxioperation.model.DriverAppTracking;
import com.vietek.taxioperation.util.ControllerUtils;

public class DriverAppTrackingUpdateToDB implements Runnable{
	private BlockingQueue<DriverAppTracking> drop;
	public DriverAppTrackingUpdateToDB(BlockingQueue<DriverAppTracking> temp){
		this.drop = temp;
	}
	@Override
	public void run() {
		int i = 0;
		Session session = ControllerUtils.getCurrentSession();
		try {
			while(!drop.isEmpty()){
				DriverAppTracking bean = drop.poll();
				session.save(bean);
				i ++;
				if(i % 200 == 0){
				   //flush a batch of inserts and release memory:
			        session.flush();
			        session.clear();
				}
			}
		} catch (Exception e) {
			session.close();
		}
		session.close();
	}
	
	public void UpdateToDB(){
		try {
			ExecutorService pool = Executors.newFixedThreadPool(5);
			pool.submit(new DriverAppTrackingUpdateToDB(drop));
			pool.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
