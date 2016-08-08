package com.vietek.taxioperation.ui.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.zkoss.gmaps.LatLng;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.CustomerController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class CustomersDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ATT_LOCATION = "location";
	private Textbox confirmPassword;
	private Textbox newPassword;
	Button btnUpload;
	Vbox vbox;
	Image img;
	Media media;
	Customer customer;
	private Combobox cbAdd1;
	private Combobox cbAdd2;
	private Combobox cbAdd3;
	private Button btnMap1;
	private Button btnMap2;
	private Button btnMap3;
	Customer currentModel;

	public CustomersDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.currentModel = (Customer) model;
		setTitle(CommonDefine.Customer.TITLE_FORM);
		this.setWidth("750px");
	}

	@Override
	public void createForm() {
		customer = (Customer) this.model;
		Grid grid = new Grid();
		grid.setParent(this);
		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setWidth("16%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("4%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("30%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("16%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("30%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên khách hàng"));
		Editor editor = getMapEditor().get("name");

		Cell cell = new Cell();

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Giới tính"));
		editor = getMapEditor().get("sex");
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		cell = new Cell();
		cell.setRowspan(3);
		btnUpload = new Button("Upload");
		btnUpload.setStyle("width: 80px");
		btnUpload.setUpload("true,maxsize=-1,multiple=false,accept=image/*");
		btnUpload.setParent(row);
		img = new Image();
		img.setWidth("100px");
		img.setHeight("100px");
		this.displayAvatar(customer.getAvatar());
		btnUpload.addEventListener(Events.ON_UPLOAD, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				upLoadImage((UploadEvent) arg0);
			}
		});
		img.setParent(cell);
		cell.setParent(row);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tuổi"));
		editor = getMapEditor().get("age");
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Vùng miền"));
		editor = getMapEditor().get("region");
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Sở thích"));
		editor = getMapEditor().get("favour");
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Số điện thoại"));
		editor = getMapEditor().get("phoneNumber");
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("Email"));
		editor = getMapEditor().get("email");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("VIP"));
		cell = new Cell();
		cell.setParent(row);
		editor = getMapEditor().get("isVIP");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("KH thường xuyên"));
		editor = getMapEditor().get("isFrequently");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Địa chỉ"));

		cbAdd1 = new Combobox();
		cbAdd1.setButtonVisible(false);
		cbAdd1.setWidth("95%");
		cbAdd1.setSclass("cusdetail_cbb_address");
		cbAdd1.setValue(customer.getAddress());
		if (customer.getAddressLat() != null && customer.getAddressLng() != null) {
			cbAdd1.setAttribute(ATT_LOCATION, new LatLng(customer.getAddressLat(), customer.getAddressLng()));
		} else
			cbAdd1.setAttribute(ATT_LOCATION, new LatLng(-1, -1));
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(cbAdd1);
		btnMap1 = new Button();
		btnMap1.setSclass("cusdetail_address");
		btnMap1.setImage("./themes/images/mylocation_30.png");
		btnMap1.setParent(cell);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Địa chỉ 1"));
		cbAdd2 = new Combobox();
		cbAdd2.setButtonVisible(false);
		cbAdd2.setWidth("95%");
		cbAdd2.setSclass("cusdetail_cbb_address");
		cbAdd2.setValue("");
		cbAdd2.setValue(customer.getAddress2());
		if (customer.getAddress2Lat() != null && customer.getAddress2Lng() != null) {
			cbAdd2.setAttribute(ATT_LOCATION, new LatLng(customer.getAddress2Lat(), customer.getAddress2Lng()));
		} else
			cbAdd2.setAttribute(ATT_LOCATION, new LatLng(-1, -1));
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(cbAdd2);
		btnMap2 = new Button();
		btnMap2.setSclass("cusdetail_address");
		btnMap2.setImage("./themes/images/mylocation_30.png");
		btnMap2.setParent(cell);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Địa chỉ 2"));
		cbAdd3 = new Combobox();
		cbAdd3.setButtonVisible(false);
		cbAdd3.setWidth("95%");
		cbAdd3.setSclass("cusdetail_cbb_address");
		cbAdd3.setValue("");
		cbAdd3.setValue(customer.getAddress3());
		if (customer.getAddress3Lat() != null && customer.getAddress3Lng() != null) {
			cbAdd3.setAttribute(ATT_LOCATION, new LatLng(customer.getAddress3Lat(), customer.getAddress3Lng()));
		} else
			cbAdd3.setAttribute(ATT_LOCATION, new LatLng(-1, -1));
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(cbAdd3);
		btnMap3 = new Button();
		btnMap3.setSclass("cusdetail_address");
		btnMap3.setImage("./themes/images/mylocation_30.png");
		btnMap3.setParent(cell);

		btnMap1.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				showMap(customer, cbAdd1, cbAdd2, cbAdd3);
			}
		});
		btnMap2.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				showMap(customer, cbAdd1, cbAdd2, cbAdd3);
			}
		});
		btnMap3.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				showMap(customer, cbAdd1, cbAdd2, cbAdd3);
			}
		});
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Ghi chú"));
		editor = getMapEditor().get("note");
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Veryfy Code "));
		editor = getMapEditor().get("verifyCode");
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mật khẩu"));
		cell = new Cell();
		cell.setParent(row);
		newPassword = new Textbox();
		newPassword.setType("password");
		newPassword.setHflex("1");
		row.appendChild(newPassword);

		row.appendChild(new Label("Nhắc lại mật khẩu"));
		confirmPassword = new Textbox();
		confirmPassword.setType("password");
		confirmPassword.setHflex("1");
		row.appendChild(confirmPassword);

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(4);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES));
		cell.setStyle("color:red;text-align:center;");
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(this.getBtn_save())) {
			Customer customer = (Customer) this.getModel();
			if (customer.getId() <= 0 && !checkExistCustomer(customer.getPhoneNumber())) {
				Env.getHomePage().showValidateForm(CommonDefine.Customer.FORM_CUSTOMER_EXIST_CUSTOMER,
						Clients.NOTIFICATION_TYPE_WARNING);
				return;
			}
			String msg = checkValidatePassword(newPassword, confirmPassword);
			if (StringUtils.isEmpty(msg)) {
				if (newPassword != null && !StringUtils.isEmpty(newPassword.getValue())) {
					customer.setPassword(newPassword.getValue());
				}
			} else {
				Env.getHomePage().showValidateForm(msg, Clients.NOTIFICATION_TYPE_WARNING);
				return;
			}
			if (cbAdd1.getText().length() > 0) {
				customer.setAddress(cbAdd1.getText());
				LatLng latLng = (LatLng) cbAdd1.getAttribute(ATT_LOCATION);
				if (latLng != null) {
					if (latLng.getLatitude() > 0 && latLng.getLongitude() > 0) {
						customer.setAddressLat(latLng.getLatitude());
						customer.setAddressLng(latLng.getLongitude());
					}
				}
			}
			if (cbAdd2.getText().length() > 0) {
				customer.setAddress2(cbAdd2.getText());
				LatLng latLng = (LatLng) cbAdd2.getAttribute(ATT_LOCATION);
				if (latLng != null) {
					if (latLng.getLatitude() > 0 && latLng.getLongitude() > 0) {
						customer.setAddress2Lat(latLng.getLatitude());
						customer.setAddress2Lng(latLng.getLongitude());
					}
				}
			}
			if (cbAdd3.getText().length() > 0) {
				customer.setAddress3(cbAdd3.getText());
				LatLng latLng = (LatLng) cbAdd3.getAttribute(ATT_LOCATION);
				if (latLng != null) {
					if (latLng.getLatitude() > 0 && latLng.getLongitude() > 0) {
						customer.setAddress3Lat(latLng.getLatitude());
						customer.setAddress3Lng(latLng.getLongitude());
					}
				}
			}
			if (!validateFieldRequire(customer)) {
				return;
			} else {
				if (media != null) {
					String imageEncode = convertAimage2String((AImage) media);
					customer.setAvatar(imageEncode.getBytes());
				} else {
					customer.setAvatar(null);
				}
				customer.save();
				CustomerController.reload(customer);
				this.setVisible(false);
				newPassword.setValue("");
				confirmPassword.setValue("");
				this.getListWindow().refresh();
				Env.getHomePage().showNotification("Đã cập nhật thông tin!", Clients.NOTIFICATION_TYPE_INFO);
			}
		} else if (event.getTarget().equals(this.getBtn_cancel())
				|| (event.getName().equals(Events.ON_CANCEL) && event.getTarget().equals(this))) {
			this.setVisible(false);
			newPassword.setValue("");
			confirmPassword.setValue("");
			Env.getHomePage().showNotification("Bỏ qua thay đổi !", Clients.NOTIFICATION_TYPE_INFO);
			this.getListWindow().refresh();
		}
	}

	/**
	 * @batt params Validate field required of customer
	 * 
	 */
	private boolean validateFieldRequire(Customer customer) {
		boolean check = true;
		String msg = "";
		if (StringUtils.isBlank(customer.getName())) {
			check = false;
		}
		if (StringUtils.isBlank(customer.getPhoneNumber())) {
			check = false;
		} else if (!StringUtils.isValidPhoneNumber(customer.getPhoneNumber())) {
			check = false;
			msg = CommonDefine.Customer.FORM_CUSTOMER_PHONE_NUMBER_INVALID;
		}
		if (StringUtils.isNotBlank(customer.getEmail()) && !StringUtils.isValidEmail(customer.getEmail())) {
			msg = CommonDefine.Customer.FORM_CUSTOMER_EMAIL_INVALID;
			check = false;
		}

		if (!check) {
			if (StringUtils.isBlank(msg))
				Env.getHomePage().showValidateForm(CommonDefine.COMMON_VALIDATE_FORM_VALUES,
						Clients.NOTIFICATION_TYPE_WARNING);
			else
				Env.getHomePage().showValidateForm(msg, Clients.NOTIFICATION_TYPE_WARNING);
		}
		return check;
	}

	private boolean checkExistCustomer(String phoneNumber) {
		try {
			CustomerController controller = (CustomerController) ControllerUtils
					.getController(CustomerController.class);
			List<Customer> lstValue = controller.find("from Customer where phoneNumber=?", phoneNumber);
			if (lstValue != null && lstValue.size() > 0) {
				return false;
			}
		} catch (Exception e) {
		}
		return true;
	}

	private String checkValidatePassword(Textbox newPass, Textbox confirmPass) {
		String msg = "";
		if (newPass != null && StringUtils.isNotBlank(newPass.getValue())) {
			if (StringUtils.checkMinLength(newPass.getValue(), 4)) {
				if (confirmPass != null && !StringUtils.equals(newPass.getValue(), confirmPass.getValue())) {
					msg = CommonDefine.Customer.FORM_CUSTOMER_PASSWORD_CONFIRM_INVALID;
				}
			} else {
				msg = CommonDefine.Customer.FORM_CUSTOMER_PASSWORD_LENGHT_INVALID;
			}
		}
		return msg;

	}

	@Override
	public void setDefaultValue() {
		// TODO Auto-generated method stub
		super.setDefaultValue();
		customer = (Customer) this.model;
		this.displayAvatar(customer.getAvatar());
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
				String imageEncode = new String(customer.getAvatar(), StandardCharsets.UTF_8);
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

	private void showMap(Customer customer, Combobox address1, Combobox address2, Combobox address3) {
		AddressCustomerGmaps customerGmaps = new AddressCustomerGmaps(customer, address1, address2, address3);
		customerGmaps.setParent(Env.getHomePage().getDivTab());
		customerGmaps.doHighlighted();
	}

	public Combobox getCbAdd1() {
		return cbAdd1;
	}

	public Combobox getCbAdd2() {
		return cbAdd2;
	}

	public Combobox getCbAdd3() {
		return cbAdd3;
	}

}