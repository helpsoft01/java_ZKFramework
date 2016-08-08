package com.vietek.taxioperation.ui.controller;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

/**
 * 
 * @author VuD
 * 
 */

public class DriversDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Button btnUpload;
	Vbox vbox;
	Image img;
	Media media;
	Driver driver;

	public DriversDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		setTitle("Danh sách lái xe");
		this.setWidth("700px");
	}

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);
		driver = (Driver) this.model;
		Columns cols = new Columns();
		cols.setParent(grid);
		Column col = new Column();
		col.setParent(cols);
		col.setHflex("15%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("35%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("15%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("35%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		Cell cell = new Cell();
		cell.setColspan(4);
		cell.setParent(row);
		Auxhead auxs = new Auxhead();
		auxs.setParent(cell);
		Auxheader aux = new Auxheader("Thông tin lái xe");
		aux.setColspan(4);
		auxs.appendChild(aux);

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setColspan(1);
		cell.setParent(row);
		cell.appendChild(new Label("MSNV "));
		Label lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		Editor editor = this.getMapEditor().get("staffCard");
		row.appendChild(editor.getComponent());
		cell = new Cell();
		cell.setColspan(1);
		cell.setParent(row);
		cell.appendChild(new Label("Họ tên "));
		lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		editor = this.getMapEditor().get("name");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Số cá nhân"));
		editor = this.getMapEditor().get("phoneNumber");
		row.appendChild(editor.getComponent());
		cell = new Cell();
		cell.setColspan(1);
		cell.setParent(row);
		cell.appendChild(new Label("VHS "));
		lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		editor = this.getMapEditor().get("phoneOffice");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("UUID"));
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.setHflex("1");
		editor = this.getMapEditor().get("mobileUUID");
		Textbox txttmp = (Textbox) editor.getComponent();
		txttmp.setReadonly(true);
		cell.appendChild(txttmp);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Chi nhánh"));
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.setHflex("1");
		editor = this.getMapEditor().get("agent");
		lb = new Label("(*) ");
		lb.setStyle("color:Red");
		cell.appendChild(lb);
		cell.appendChild(editor.getComponent());

		/*--- use for image --*/
		row = new Row();
		btnUpload = new Button("Upload");
		btnUpload.setStyle("width: 80px");
		btnUpload.setUpload("true,maxsize=-1,multiple=false,accept=image/*");
		btnUpload.setParent(row);
		img = new Image();
		img.setWidth("100px");
		img.setHeight("100px");
		this.displayAvatar(driver.getAvatar());
		btnUpload.addEventListener(Events.ON_UPLOAD, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				upLoadImage((UploadEvent) arg0);
			}
		});
		img.setParent(row);
		row.setParent(rows);

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setColspan(4);
		cell.setParent(row);
		auxs = new Auxhead();
		auxs.setParent(cell);
		aux = new Auxheader("Quản lý App");
		aux.setColspan(4);
		auxs.appendChild(aux);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Đăng ký App"));
		editor = this.getMapEditor().get("isAppRegister");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("Đánh giá"));
		editor = this.getMapEditor().get("rate");
		row.appendChild(editor.getComponent());

	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(this.getBtn_save())) {
			driver = (Driver) this.model;
			if (media != null) {
				String imageEncode = convertAimage2String((AImage) media);
				driver.setAvatar(imageEncode.getBytes());
			}else {
				media = null;
				img.setSrc("./themes/images/male_cus_64.png");
			}
			// // else {
			// // driver.setAvatar(null);
			// // }
			// driver.save();
			// this.setVisible(false);
			// this.getListWindow().refresh();
			// Env.getHomePage().showNotification("Đã cập nhật thông tin!",
			// Clients.NOTIFICATION_TYPE_INFO);
		}
		super.onEvent(event);
		// else if (event.getTarget().equals(this.getBtn_cancel())
		// || (event.getName().equals(Events.ON_CANCEL) &&
		// event.getTarget().equals(this))) {
		// this.setVisible(false);
		// Env.getHomePage().showNotification("Bỏ qua thay đổi !",
		// Clients.NOTIFICATION_TYPE_INFO);
		// this.getListWindow().refresh();
		// }
	
	}

	@Override
	public void setDefaultValue() {
		// TODO Auto-generated method stub
		super.setDefaultValue();
		driver = (Driver) this.model;
		this.displayAvatar(driver.getAvatar());
	}

	private void upLoadImage(UploadEvent event) {
		media = event.getMedia();
		if (media instanceof AImage) {
			if (media.getByteData().length > 1000 * 1024) {
				Clients.showNotification("Dung lượng file ảnh không được vượt quá 1MB !", "error", null,
						"bottom_center", 3000, true);
			} else
				img.setContent((AImage) media);
		} else {
			Clients.showNotification("Định dạng file ảnh không hợp lệ !", "error", null, "bottom_center", 3000,
					true);
		}
	}

	private void displayAvatar(byte[] avatar) {
		if (avatar == null)
			img.setSrc("./themes/images/male_cus_64.png");
		else {
			try {
				String imageEncode = new String(driver.getAvatar(), StandardCharsets.UTF_8);
				AImage image = convertString2AImage(imageEncode);
				img.setContent(image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public AImage convertString2AImage(String base64Img) {
		AImage decodedimg = null;
		if (base64Img == null)
			return null;
		byte[] decodedString = Base64.decodeBase64(base64Img);
		if (decodedString.length > 10) {
			try {
				decodedimg = new AImage("img", decodedString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return decodedimg;
	}

	public static String convertAimage2String(AImage aImage) {
		if (aImage == null)
			return null;
		try {
			String imageEncoded = Base64.encodeBase64String(aImage.getByteData());
			return imageEncoded;
		} catch (Exception e) {
			return null;
		}

	}

}
