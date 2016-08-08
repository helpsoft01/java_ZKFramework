package com.vietek.taxioperation.realtime.command;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.command.driver.DriverLoginCommand;
import com.vietek.taxioperation.realtime.command.rider.RiderLoginCommand;
import com.vietek.taxioperation.realtime.socket.MobileServerHander;

public class MobileCommand {
	public static final String END_CMD = "$end";
	public static final String RET_CMD = "->";
	static ExecutorService executor = Executors.newCachedThreadPool();
	public static MobileCommand sharedInstance = new MobileCommand();

	public void processCommand(String command, MobileServerHander handler) {
		CommandWorker worker = new CommandWorker();
		worker.command = command;
		worker.handler = handler;
		Thread thread = new Thread(worker);
		executor.execute(thread);
//		try {
//			AbstractCommand cmd = null;
//			if (handler.getDevice() == null
//					|| command.startsWith(RiderLoginCommand.COMMAND)
//					|| (command.startsWith(DriverLoginCommand.COMMAND))) {
//				//Rider login
//				if (command.startsWith(RiderLoginCommand.COMMAND)) {
//					cmd = new RiderLoginCommand();
//				}
//				//Driver login
//				else if (command.startsWith(DriverLoginCommand.COMMAND)) {
//					cmd = new DriverLoginCommand();
//				}
//				else {
//					return;
//				}
//				cmd.setHandler(handler);
//				cmd.setData(command);
//				cmd.parseData();
//				cmd.processData();
//			}
//			else {
//				//Rider
//				if (handler.getDevice() instanceof Rider) {
//					Rider rider = (Rider)handler.getDevice();
//					rider.processCommand(command);
//				}
//				//Driver
//				else {
//					Taxi driver = (Taxi)handler.getDevice();
//					driver.processCommand(command);
//				}
//			}
//		} catch (Exception e) {
//			logger.error("[ERROR]-Command: " + command + "|" + e);
//		}
	}
	
	class CommandWorker implements Runnable{
		String command;
		MobileServerHander handler;
		@Override
		public void run() {
			try {
				AbstractCommand cmd = null;
				if (handler.getDevice() == null
						|| command.startsWith(RiderLoginCommand.COMMAND)
						|| (command.startsWith(DriverLoginCommand.COMMAND))) {
					//Rider login
					if (command.startsWith(RiderLoginCommand.COMMAND)) {
						cmd = new RiderLoginCommand();
					}
					//Driver login
					else if (command.startsWith(DriverLoginCommand.COMMAND)) {
						cmd = new DriverLoginCommand();
					}
					else {
						return;
					}
					cmd.setHandler(handler);
					cmd.setData(command);
					cmd.parseData();
					cmd.processData();
				}
				else {
					//Rider
					if (handler.getDevice() instanceof Rider) {
						Rider rider = (Rider)handler.getDevice();
						rider.processCommand(command);
					}
					//Driver
					else {
						Taxi driver = (Taxi)handler.getDevice();
						driver.processCommand(command);
					}
				}
			} catch (Exception e) {
				AppLogger.logDebug.error("[ERROR]-Command: " + command + "|" + e);
			}
		}
	}
}
