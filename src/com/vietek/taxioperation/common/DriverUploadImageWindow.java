package com.vietek.taxioperation.common;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.ImageUtils;

public class DriverUploadImageWindow extends Window implements Serializable, EventListener<Event>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hlayout bottonLayout;
	private Button btn_save;
	private Button btn_cancel;
	private String defaultFolder = "D:/avatar";
	Textbox pathFolder;
	public Map<String, String> map = new HashMap<String, String>();
	public StringBuilder str = new StringBuilder();
	
	public List<Integer> lstParams = new ArrayList<Integer>();
	
	public DriverUploadImageWindow(){
		this.addEventListener(Events.ON_OK, this);
		this.addEventListener(Events.ON_CANCEL, this);
		init();
	}
	private void init() {
		this.setClosable(true);
		this.setMaximizable(true);
		setTitle("Cập nhật ảnh đại diện tài xế");
		this.setWidth("400px");
		this.addEventListener(Events.ON_CANCEL, this);
		createForm();
		initUI();
		// Events.echoEvent("setDefaultValue", this, null);
	}
	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(btn_save)) {
			String result = validateFolder(pathFolder.getValue());
			if(StringUtils.isEmpty(result)){
				confirmUpload(pathFolder.getValue());
			}else if(StringUtils.equals(result, "1")){
				Env.getHomePage().showNotificationErrorSelect("Đường dẫn thư mục không được để trống !", Clients.NOTIFICATION_TYPE_INFO);
			}else if(StringUtils.equals(result, "2")){
				Env.getHomePage().showNotificationErrorSelect("Đường dẫn thư mục không hợp lệ !", Clients.NOTIFICATION_TYPE_INFO);
			}
		} else if (event.getTarget().equals(btn_cancel)
				|| (event.getName().equals(Events.ON_CANCEL) && event.getTarget().equals(this))) {
			this.setVisible(false);
		}
		
	}
	
	public void initUI() {
		bottonLayout = new Hlayout();
		bottonLayout.setStyle("padding:10px; text-align: center;");
		bottonLayout.setParent(this);
		bottonLayout.setValign("center");
		btn_save = new Button(CommonDefine.Tittle.TITLE_BTN_UPLOAD);
		btn_save.setImage("/themes/images/save_16.png");
		// btn_save.setSclass("btn-success");
		btn_save.setParent(bottonLayout);
		btn_save.addEventListener(Events.ON_CLICK, this);
		btn_cancel = new Button(CommonDefine.Tittle.TITLE_BTN_CLOSE);
		btn_cancel.setParent(bottonLayout);
		btn_cancel.setImage("/themes/images/close_16.png");
		btn_cancel.addEventListener(Events.ON_CLICK, this);
	}
	
	public void createForm() {
		
		Grid grid = new Grid();
		grid.setStyle("border: none");
		grid.setParent(this);
		// setup columns
		Columns columns = new Columns();
		columns.setParent(grid);
		Column column = new Column();
		column.setWidth("30%");
		column.setAlign("right");
		column.setParent(columns);
		column = new Column();
		column.setWidth("70%");
		column.setParent(columns);

		// setup rows
		Rows rows = new Rows();
		rows.setParent(grid);
		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Thư mục upload"));
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setHflex("1");
		pathFolder = new Textbox();
		pathFolder.setWidth("100%");
		pathFolder.setValue(defaultFolder);
		cell.appendChild(pathFolder);
		
	}
	
	private String validateFolder(String path){
		if(StringUtils.isEmpty(path)){
			return "1";
		}else if(!checkIsFolder(path)){
			return "2";
		}
		return "";
	}
	
	private boolean checkIsFolder(String path){
		File file = new File(path);
		if(file.exists()){
			return true;
		}
		return false;
	}
	
	private void confirmUpload(final String path){
		Messagebox.show("Bạn có muốn import ảnh tài xế ? ", "Xác nhận import",
				new Messagebox.Button[] { Messagebox.Button.OK, Messagebox.Button.CANCEL },
				Messagebox.QUESTION, new EventListener<Messagebox.ClickEvent>() {
					@Override
					public void onEvent(ClickEvent event) throws Exception {
						try {
							if (event.getButton() != null&& event.getButton().equals(Messagebox.Button.OK)) {
								importImage2DB(path);
								Env.getHomePage().showNotificationErrorSelect("Import thành công", Clients.NOTIFICATION_TYPE_INFO);
							}
						} catch (Exception e) {
							Env.getHomePage().showNotificationErrorSelect("Có lỗi xảy ra trong quá trình import", Clients.NOTIFICATION_TYPE_INFO);
						}
					}
				});
	}
	
	private void importImage2DB(String folder){
			File[] lstFile = ImageUtils.lstFile(folder);
			ImageUtils.convertToByteArray(lstFile,str, map,lstParams);
			try {
				String sql = "from Driver where staffCard in (:IdList)";
				/*StringBuilder strApp = str.replace(str.toString().length()-1, str.toString().length(), "");
				sql += strApp.toString() + ")";*/
				Session session = ControllerUtils.getCurrentSession();
				@SuppressWarnings("unchecked")
				List<Driver> lstDriver = session.createQuery(sql).setParameterList("IdList", lstParams).list();
				session.close();
				if(lstDriver != null && lstDriver.size() > 0){
					for (int i = 0; i < lstDriver.size(); i++) {
						Driver driver = lstDriver.get(i);
						String key = driver.getStaffCard().toString();
						if(key.length()==4){
							key = "00"+key;
						}else if(key.length()==5){
							key = "0"+key;
						}
						driver.setAvatar(map.get(key).getBytes());
						driver.save();
					}
				}
				this.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	}
	
}
