package com.vietek.taxioperation.ui.controller.vmap;

import java.util.LinkedList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;

import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.IDGenerator;

public abstract class VComponent extends Div {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5179608487646890216L;
	private LinkedList<String> scriptQueue = new LinkedList<>();
	private VComponent self = this;
	protected Boolean isLoaded = false;
	private VComponent parent = null;
	private Desktop destop;
	private CheckWidgetCreatedWorker _checkWorker;
	private boolean isRendered = false;
	protected synchronized void addJSScriptSynch(String jsScript) {
		scriptQueue.add(jsScript);
		if (scriptQueue.size() == 1) {
			runJSScriptSynch();
		}
	}
	
	public VComponent() {
		this(false);
	}
	
	public VComponent(Boolean isNeedCheckRendered) {
		super();
		destop = Env.getHomePage().getDesktop();
		this.setId(IDGenerator.generateStringID());
		if (isNeedCheckRendered) {
			_checkWorker = new CheckWidgetCreatedWorker();
			_checkWorker.start();
		}
		else {
			isRendered =  true;
		}
		this.addEventListener("onFinishJSCall", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null && event.getData().equals("didCreateObject")) {
					isRendered  = true;
					return;
				}
				scriptQueue.pop();
				isLoaded = true;
				runJSScriptSynch();
			}
		});
	}
	public void runJSScriptSynch() {
		if (scriptQueue.isEmpty())
			return;
		String script = scriptQueue.getFirst();
		Events.postEvent(new Event("runJSScript", this, script));
	}
	
	public void runJSScript(Event event) {
		String script = (String)event.getData();
		RunJSWorker worker = new RunJSWorker(script);
		worker.start();
	}
	
	class CheckWidgetCreatedWorker extends Thread {
		String jsString;
		public CheckWidgetCreatedWorker() {
			this.jsString = "vietek.checkWidget('" + self.getId() + "')";
		}
		public void run() {
			while (!isRendered){
				try {
					Executions.schedule(destop, new EventListener<Event>() {
						@Override
						public void onEvent(Event arg0) throws Exception {
							Clients.evalJavaScript(jsString);
						}
					}, null);
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class RunJSWorker extends Thread {
		String jsString;
		public RunJSWorker(String jsString) {
			this.jsString = jsString + ";vietek.runJSCallBack('" + self.getId() + "')";
		}
		public void run() {
			while ((parent != null && !parent.isLoaded) || !isRendered){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Executions.schedule(destop, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					Clients.evalJavaScript(jsString);
				}
			}, null);
			
		}
	}

	@Override
	public void setParent(Component parent) {
		if (parent instanceof VComponent) {
			this.parent = (VComponent) parent;
		}
		super.setParent(parent);
	}

	
}
