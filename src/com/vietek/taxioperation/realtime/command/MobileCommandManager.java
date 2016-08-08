package com.vietek.taxioperation.realtime.command;

import java.util.concurrent.ConcurrentHashMap;

import com.vietek.taxioperation.realtime.socket.MobileServerHander;

public class MobileCommandManager {
	static ConcurrentHashMap<MobileServerHander, StringBuffer> cmdMap = new ConcurrentHashMap<>();
//	static ExecutorService executor = Executors.newCachedThreadPool(); 
	public static void addConnection(MobileServerHander handler) {
		StringBuffer cmds = new StringBuffer();
		cmdMap.put(handler, cmds);
	}
	public static void removeConnection(MobileServerHander handler) {
		cmdMap.remove(handler);
	}
	public static StringBuffer getCommands(MobileServerHander handler) {
		StringBuffer retVal = cmdMap.get(handler); 
		if (retVal == null) {
			retVal = new StringBuffer();
			cmdMap.put(handler, retVal);
		}
		return retVal;
	}
	
	public static void addCommand(MobileServerHander handler, String command) {
		StringBuffer commands = MobileCommandManager.getCommands(handler);
		if (commands == null)
			return;
		synchronized (handler) {
			commands.append(command);
			int endCmdIndex = commands.indexOf(MobileCommand.END_CMD);
			int commandCount = 0;
			while (endCmdIndex > -1 && commandCount < 5) { //Xu ly nhieu nhat 5 cau lenh
				commandCount += 1;
				String subCommand = commands.substring(0, endCmdIndex);
				commands = commands.delete(0, endCmdIndex + MobileCommand.END_CMD.length());
				MobileCommand.sharedInstance.processCommand(subCommand.trim(), handler);
				endCmdIndex = commands.indexOf(MobileCommand.END_CMD);
			}
		}
//		Worker worker = new Worker(handler);
//		executor.execute(worker);
	}
}
//class Worker extends Thread {
//	private MobileServerHander handler;
//	public Worker(MobileServerHander handler) {
//		this.handler = handler;
//	}
//	public void run() {
//		StringBuffer commands = MobileCommandManager.getCommands(handler);
//		if (commands == null)
//			return;
//		synchronized (handler) {
//			int endCmdIndex = commands.indexOf(MobileCommand.END_CMD);
//			int commandCount = 0;
//			while (endCmdIndex > -1 && commandCount < 5) { //Xu ly nhieu nhat 5 cau lenh
//				commandCount += 1;
//				String command = commands.substring(0, endCmdIndex);
//				commands = commands.delete(0, endCmdIndex + MobileCommand.END_CMD.length());
//				MobileCommand.sharedInstance.processCommand(command.trim(), handler);
//				endCmdIndex = commands.indexOf(MobileCommand.END_CMD);
//			}
//		}
//	}
//}
