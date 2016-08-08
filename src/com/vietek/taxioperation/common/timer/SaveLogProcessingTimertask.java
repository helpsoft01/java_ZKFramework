package com.vietek.taxioperation.common.timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import com.vietek.taxioperation.model.LoggingUserAction;

public class SaveLogProcessingTimertask extends TimerTask{

	protected BlockingQueue<LoggingUserAction> queueLoggingUserAction = null;
	private final static Object lock = new Object();
	
    public SaveLogProcessingTimertask(BlockingQueue<LoggingUserAction> queue) {
		// TODO Auto-generated constructor stub
    	this.queueLoggingUserAction = queue;
	}
	
	@Override
	public void run() {
		this.doWork();
	}
	
	public void addQueue(LoggingUserAction log){
		queueLoggingUserAction.offer(log);
	}
	
	public LoggingUserAction deQueue(){
		LoggingUserAction log = queueLoggingUserAction.poll();
		return log;
	}	
	
	public boolean clearQueue(){
		synchronized (lock) {
            boolean removed = false;
            Iterator<LoggingUserAction> iterator = queueLoggingUserAction.iterator();

            while(iterator.hasNext()){
            	iterator.remove();
                removed = true;
            }
            return removed;
        }
	}
	
	public void doWork(){
		// TODO Auto-generated method stub
		List<LoggingUserAction> logs = new ArrayList<LoggingUserAction>();
		synchronized (lock) {		
			int count = 0;
			while (count < 200) {					
				if (!queueLoggingUserAction.isEmpty()) {
					LoggingUserAction log = deQueue();
					if (log != null) {
						logs.add(log);		
					}					
					count++;
				}else {
					break;
				}				
			}					
		}
		if (logs.size() > 0) {
			LoggingUserAction.bulkInsert(logs, LoggingUserAction.class);
		}		
	}
}
