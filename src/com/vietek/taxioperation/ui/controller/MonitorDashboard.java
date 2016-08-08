package com.vietek.taxioperation.ui.controller;

import java.lang.management.ManagementFactory;
import java.util.Date;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Timer;

import com.sun.management.OperatingSystemMXBean;
import com.vietek.taxioperation.common.timer.MonitorProcessor;

/**
 *
 * @author VuD
 */
public class MonitorDashboard extends Div {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int mb = 1024 * 1024;
	private Listbox lstbox;

	public MonitorDashboard() {
		super();
		this.init();
		this.setHflex("1");
		this.setVflex("1");
		Timer timer = new Timer();
		timer.setParent(this);
		timer.setRepeats(true);
		timer.setDelay(10000);
		timer.addEventListener(Events.ON_TIMER, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				renderListBox(lstbox);
			}
		});
	}

	private void init() {
		// Panel panel = new Panel();
		// panel.setHflex("1");
		// panel.setVflex("1");
		// panel.setTitle("System Monitor");
		// panel.setParent(this);
		// panel.setSclass("panel-success");
		lstbox = new Listbox();
		lstbox.setParent(this);
		Listhead lstHead = new Listhead();
		lstHead.setParent(lstbox);
		Listheader lstHeader = new Listheader();
		lstHeader.setParent(lstHead);
		lstHeader.setLabel("Thông số");
		lstHeader.setHflex("5");
		lstHeader = new Listheader();
		lstHeader.setParent(lstHead);
		lstHeader.setLabel("Giá trị");
		lstHeader.setHflex("5");
		this.renderListBox(lstbox);
	}

	private void renderListBox(Listbox lstbox) {
		lstbox.getItems().clear();
		MonitorModel monitorModel = this.getMonitorModel();
		Listitem lstItem = new Listitem();
		lstItem.setParent(lstbox);
		Listcell lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel("Thời gian khởi động hệ thống");
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel(MonitorProcessor.startAppTime + "");

		lstItem = new Listitem();
		lstItem.setParent(lstbox);
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel("System cpu usage");
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel(monitorModel.getSystemCpuUsage() + "");

		lstItem = new Listitem();
		lstItem.setParent(lstbox);
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel("Java cpu usage");
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel(monitorModel.getJavaCpuUsage() + "");

		lstItem = new Listitem();
		lstItem.setParent(lstbox);
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel("Active threads");
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel(monitorModel.getActiveThread() + "");

		lstItem = new Listitem();
		lstItem.setParent(lstbox);
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel("System total memory");
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel(monitorModel.getSystemTotalMenory() + "");

		lstItem = new Listitem();
		lstItem.setParent(lstbox);
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel("Java maximum memory");
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel(monitorModel.getJavaMaxMemory() + "");

		lstItem = new Listitem();
		lstItem.setParent(lstbox);
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel("Java total memory");
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel(monitorModel.getJavaTotalMenory() + "");

		lstItem = new Listitem();
		lstItem.setParent(lstbox);
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel("Java use memory");
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel(monitorModel.getJavaUseMemory() + "");

		lstItem = new Listitem();
		lstItem.setParent(lstbox);
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel("Java free memory");
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel(monitorModel.getJavaFreeMemory() + "");

		lstItem = new Listitem();
		lstItem.setParent(lstbox);
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel("Date");
		lstCell = new Listcell();
		lstCell.setParent(lstItem);
		lstCell.setLabel((new Date()).toString());
	}

	private MonitorModel getMonitorModel() {
		OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		Runtime runtime = Runtime.getRuntime();
		double systemCpuUsage = Math.round(osBean.getSystemCpuLoad() * 10000d) / 100d;
		double javaCpuUsage = Math.round(osBean.getProcessCpuLoad() * 10000d) / 100d;
		long activeThread = Thread.activeCount();
		long systemTotalMenory = osBean.getTotalPhysicalMemorySize() / mb;
		long javaTotalMenory = runtime.totalMemory() / mb;
		long javaMaxMemory = runtime.maxMemory() / mb;
		long javaFreeMemory = runtime.freeMemory() / mb;
		long javaUseMemory = (runtime.totalMemory() - runtime.freeMemory()) / mb;
		MonitorModel model = new MonitorModel();
		model.setSystemCpuUsage(systemCpuUsage);
		model.setJavaCpuUsage(javaCpuUsage);
		model.setActiveThread(activeThread);
		model.setSystemTotalMenory(systemTotalMenory);
		model.setJavaTotalMenory(javaTotalMenory);
		model.setJavaMaxMemory(javaMaxMemory);
		model.setJavaFreeMemory(javaFreeMemory);
		model.setJavaUseMemory(javaUseMemory);
		return model;

	}

	private class MonitorModel {
		private double javaCpuUsage;
		private double systemCpuUsage;
		private long activeThread;
		private long systemTotalMenory;
		private long javaTotalMenory;
		private long javaMaxMemory;
		private long javaUseMemory;
		private long javaFreeMemory;

		public double getJavaCpuUsage() {
			return javaCpuUsage;
		}

		public void setJavaCpuUsage(double javaCpuUsage) {
			this.javaCpuUsage = javaCpuUsage;
		}

		public double getSystemCpuUsage() {
			return systemCpuUsage;
		}

		public void setSystemCpuUsage(double systemCpuUsage) {
			this.systemCpuUsage = systemCpuUsage;
		}

		public long getActiveThread() {
			return activeThread;
		}

		public void setActiveThread(long activeThread) {
			this.activeThread = activeThread;
		}

		public long getSystemTotalMenory() {
			return systemTotalMenory;
		}

		public void setSystemTotalMenory(long systemTotalMenory) {
			this.systemTotalMenory = systemTotalMenory;
		}

		public long getJavaTotalMenory() {
			return javaTotalMenory;
		}

		public void setJavaTotalMenory(long javaTotalMenory) {
			this.javaTotalMenory = javaTotalMenory;
		}

		public long getJavaMaxMemory() {
			return javaMaxMemory;
		}

		public void setJavaMaxMemory(long javaMaxMemory) {
			this.javaMaxMemory = javaMaxMemory;
		}

		public long getJavaUseMemory() {
			return javaUseMemory;
		}

		public void setJavaUseMemory(long javaUseMemory) {
			this.javaUseMemory = javaUseMemory;
		}

		public long getJavaFreeMemory() {
			return javaFreeMemory;
		}

		public void setJavaFreeMemory(long javaFreeMemory) {
			this.javaFreeMemory = javaFreeMemory;
		}

	}

}
