package com.vietek.trackingOnline.tracker;

import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class InfoWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Button btnClose;
	private Label lbTitle;
	
	public InfoWindow() {
		super.setClosable(false);
		super.setMaximizable(false);
		super.setMinimizable(false);
		initUI();
	}
	
	private void initUI(){
		btnClose = new Button();
		btnClose.setImage("./themes/images/DeleteRed_16.png");
		btnClose.setStyle("");
		btnClose.setParent(this);
		lbTitle = new Label();
		lbTitle.setParent(this);
		lbTitle.setStyle("");
	}
	
	@Override
	public void setTitle(String title) {
		lbTitle.setValue(title);
	}
	
	@Override
	public void setClosable(boolean closable) {
		btnClose.setVisible(closable);
	}

}
