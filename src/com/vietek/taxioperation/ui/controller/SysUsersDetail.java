package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.ChannelTmsController;
import com.vietek.taxioperation.controller.SysUserController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.SwitchboardTMS;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.M2OEditor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.SecurityUtils;

/**
 * 
 * @author VuD
 *
 */

public class SysUsersDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox textbox;
	private Combobox cbbTongDai;
	private Combobox cbbChannel;
	// private M2OEditor editorChannel;
	private ChannelTmsController controller;

	public SysUsersDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		setTitle("Chi tiết người dùng");
		this.setWidth("650px");
	}

	@Override
	public void beforInit() {
		cbbChannel = new Combobox();
		cbbChannel.setHflex("1");
		cbbChannel.setZclass("");
	}

	@Override
	public void setDefaultValue() {
		super.setDefaultValue();
		if (cbbChannel != null) {
			cbbChannel.setValue(null);

		}

		String sql = "from ChannelTms where switchboardtms = ? order by name";
		List<ChannelTms> lstValue = controller.find(sql, ((SysUser) getModel()).getSwitchboard());
		cbbChannel.setModel(new ListModelList<ChannelTms>(lstValue));
		Events.echoEvent("setDefaultValueChannel", this, null);
	}

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("18%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("4%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("28%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("18%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("4%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("28%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã nhân viên"));
		Label mandatory = new Label("(*)");
		mandatory.setStyle("color:red");
		row.appendChild(mandatory);
		Editor editor = this.getMapEditor().get("value");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("Họ tên"));
		mandatory = new Label("(*)");
		mandatory.setStyle("color:red");
		row.appendChild(mandatory);
		editor = this.getMapEditor().get("name");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên đăng nhập"));
		mandatory = new Label("(*)");
		mandatory.setStyle("color:red");
		row.appendChild(mandatory);
		editor = this.getMapEditor().get("user");
		row.appendChild(editor.getComponent());

		row.appendChild(new Label("Mật khẩu"));
		mandatory = new Label("(*)");
		mandatory.setStyle("color:red");
		row.appendChild(mandatory);
		textbox = new Textbox();
		textbox.setType("password");
		textbox.setHflex("1");
		row.appendChild(textbox);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Công ty"));
		row.appendChild(new Label());
		editor = this.getMapEditor().get("sysCompany");
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(4);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tổng đài"));
		row.appendChild(new Label());
		editor = this.getMapEditor().get("switchboard");
		cbbTongDai = (Combobox) editor.getComponent();
		cbbTongDai.addEventListener(Events.ON_SELECT, this);
		cbbTongDai.addEventListener(Events.ON_CHANGE, this);
		row.appendChild(cbbTongDai);
		row.appendChild(new Label("Kênh"));
		row.appendChild(new Label());
		// editorChannel = (M2OEditor) this.getMapEditor().get("channel");
		controller = (ChannelTmsController) ControllerUtils.getController(ChannelTmsController.class);
		String sql = "from ChannelTms where switchboardtms = ? order by name";
		List<ChannelTms> lstValue = controller.find(sql, ((SysUser) getModel()).getSwitchboard());
		cbbChannel.setItemRenderer(new ComboitemRenderer<ChannelTms>() {
			@Override
			public void render(Comboitem item, ChannelTms data, int arg2) throws Exception {
				if (data != null) {
					item.setLabel(data.toString());
					item.setValue(data);
				} else {
					item.setLabel("");
					item.setValue(null);
				}
			}
		});
		cbbChannel.setModel(new ListModelList<ChannelTms>(lstValue));
		ChannelTms channel = (ChannelTms) AbstractModel.getValue(this.getModel(), "channel");
		if (channel != null) {
			List<Comboitem> lstItem = cbbChannel.getItems();
			for (Comboitem comboitem : lstItem) {
				ChannelTms tmp = comboitem.getValue();
				if (tmp.getId() == channel.getId()) {
					cbbChannel.setSelectedItem(comboitem);
				}
			}

		}
		cbbChannel.addEventListener(Events.ON_SELECT, this);
		cbbChannel.addEventListener(Events.ON_CHANGE, this);
		cbbChannel.addEventListener(Events.ON_CHANGING, this);
		row.appendChild(cbbChannel);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Nhóm"));
		row.appendChild(new Label());
		editor = this.getMapEditor().get("sysGroup");
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(4);
		cell.appendChild(editor.getComponent());
		row.appendChild(cell);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Rule"));
		row.appendChild(new Label());
		editor = this.getMapEditor().get("setSysRule");
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(4);
		cell.appendChild(editor.getComponent());
		row.appendChild(cell);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kích hoạt"));
		row.appendChild(new Label());
		editor = this.getMapEditor().get("isActive");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("Số điện thoại viên"));
		row.appendChild(new Label());
		editor = this.getMapEditor().get("extNumber");
		row.appendChild(editor.getComponent());

		/*
		 * row = new Row();
		 * 
		 * row = new Row(); row.setParent(rows); row.appendChild(new Label(
		 * "Điều đàm viên")); editor = getMapEditor().get("isRadio"); cell = new
		 * Cell(); cell.setParent(row); row.appendChild(editor.getComponent());
		 * 
		 * row.appendChild(new Label("Điện thoại viên")); editor =
		 * getMapEditor().get("isTelephonist"); cell = new Cell();
		 * cell.setParent(row); row.appendChild(editor.getComponent());
		 */
		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES));
		cell.setStyle("color:red;text-align:center;");
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if ((event.getName().equals(Events.ON_CHANGE) || event.getName().equals(Events.ON_SELECT)
				|| event.getName().equals(Events.ON_CHANGING)) && event.getTarget().equals(cbbTongDai)) {
			cbbChannel.setValue(null);
			if (cbbTongDai.getSelectedIndex() >= 0) {
				if (cbbTongDai.getSelectedItem().getValue().equals(M2OEditor.LOAD_MORE)) {

				} else {
					SwitchboardTMS tongdai = cbbTongDai.getSelectedItem().getValue();
					if (tongdai.getId() != 0) {
						String sql = "from ChannelTms where switchboardtms = ?";
						List<ChannelTms> lstValue = controller.find(sql, tongdai);
						cbbChannel.setModel(new ListModelList<ChannelTms>(lstValue));
					}
				}
			} else {
				cbbChannel.setModel(new ListModelList<ChannelTms>(new ArrayList<ChannelTms>()));
			}
			AbstractModel.setValue(getModel(), "channel", null);
		} else if (event.getTarget().equals(cbbChannel)) {
			if (cbbChannel.getSelectedIndex() >= 0) {
				ChannelTms channel = cbbChannel.getSelectedItem().getValue();
				AbstractModel.setValue(getModel(), "channel", channel);
			} else {
				AbstractModel.setValue(getModel(), "channel", null);
			}
		}
		if (event.getTarget().equals(this.getBtn_save())) {
			AbstractModel model = super.getModel();
			SysUser user = (SysUser) model;
			String errMsg = validateSysUser(user);
			if (StringUtils.isNotEmpty(textbox.getValue())) {
				user.setPassword(textbox.getValue());
				String encryptPass = SecurityUtils.encryptMd5(user.getPassword());
				AbstractModel.setValue(this.getModel(), "password", encryptPass);
			}
			if (StringUtils.isEmpty(errMsg)) {
				super.handleSaveEvent();
				textbox.setValue("");
			} else {
				Env.getHomePage().showNotificationErrorSelect(errMsg, Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
		} else if (event.getTarget().equals(this.getBtn_cancel())
				|| (event.getName().equals(Events.ON_CANCEL) && event.getTarget().equals(this))) {
			this.setVisible(false);
			textbox.setValue("");
			Env.getHomePage().showNotification("Bỏ qua thay đổi!", Clients.NOTIFICATION_TYPE_INFO);
			/*
			 * sonvh 
			 * Anh bỏ vì cancel không cần lấy lại dữ liệu mới
			 */
			// this.getListWindow().refresh();
		}
	}

	protected boolean checkValue() {
		boolean result = false;
		SysUser model = (SysUser) this.getModel();
		String value = model.getValue();
		if (value == null) {
			Env.getHomePage().showNotification("Bạn không được để trống mã người dùng",
					Clients.NOTIFICATION_TYPE_ERROR);
		} else if (value.length() <= 0) {
			Env.getHomePage().showNotification("Bạn không được để trống mã người dùng",
					Clients.NOTIFICATION_TYPE_ERROR);
		} else if (value.trim().length() <= 0) {
			Env.getHomePage().showNotification("Bạn không được để mã người dùng toàn khoảng trắng",
					Clients.NOTIFICATION_TYPE_ERROR);
		} else if (model.getId() == 0) {
			value = value.trim();
			SysUserController controller = (SysUserController) ControllerUtils.getController(SysUserController.class);
			List<SysUser> lstModel = controller.find("from SysUser where value = ?", value);
			if (lstModel.size() > 0) {
				Env.getHomePage().showNotification("Mã nhân viên bị trùng. Bạn cần tạo mã khác",
						Clients.NOTIFICATION_TYPE_ERROR);
			} else {
				result = true;
			}
			model.setValue(value);
		} else {
			value = value.trim();
			SysUserController controller = (SysUserController) ControllerUtils.getController(SysUserController.class);
			List<SysUser> lstModel = controller.find("from SysUser where value = ? and id != ?",
					new Object[] { value, model.getId() });
			if (lstModel.size() > 0) {
				Env.getHomePage().showNotification("Mã nhân viên bị trùng. Bạn cần tạo mã khác",
						Clients.NOTIFICATION_TYPE_ERROR);
			} else {
				result = true;
			}
			model.setValue(value);
		}
		return result;
	}

	private String validateSysUser(SysUser user) {
		StringBuffer msg = new StringBuffer("");
		// Check full name
		if (StringUtils.isEmpty(user.getName()))
			msg.append(" - Họ tên không được để trống !");
		else if (StringUtils.isHasWhiteSpaceBeginEnd(user.getName()))
			msg.append(" - Họ tên không được chứa khoảng trắng ở đầu và cuối !");

		// Check user name
		if (StringUtils.isEmpty(user.getUserName()))
			msg.append(" - Tên đăng nhập không được để trống !");
		else if (StringUtils.isHasWhiteSpace(user.getUserName()))
			msg.append(" - Tên đăng nhập không được chứa khoảng trắng !");
		else if (StringUtils.isHasSpecialChar(user.getUserName()))
			msg.append(" - Tên đăng nhập không được chứa ký tự đặc biệt !");
		else {
			SysUser sysUserByUserName = checkExistUserField("user", user.getUserName());
			// In case add User
			if (user.getId() <= 0) {
				if (sysUserByUserName != null)
					msg.append(" - Tên đăng nhập đã tồn tại trong hệ thống !");
			} else { // In case edit user
				if (sysUserByUserName != null && sysUserByUserName.getId() != user.getId())
					msg.append(" - Tên đăng nhập đã tồn tại trong hệ thống !");
			}
		}

		// Check code user
		if (StringUtils.isEmpty(user.getValue()))
			msg.append(" - Mã người dùng không được để trống !");
		else if (StringUtils.isHasWhiteSpaceBeginEnd(user.getValue()))
			msg.append(" - Mã người dùng không được chứa khoảng trắng ở đầu và cuối !");
		else if (StringUtils.isHasSpecialChar(user.getValue()))
			msg.append(" - Mã người dùng không được chứa ký tự đặc biệt !");
		else if (!StringUtils.checkLength(user.getValue(), 4, 15))
			msg.append(" - Mã người dùng chỉ từ 4 đến 15");
		else {
			SysUser sysUserByCodeUser = checkExistUserField("value", user.getValue());
			// In case add User
			if (user.getId() <= 0) {
				if (sysUserByCodeUser != null)
					msg.append(" - Mã người dùng đã tồn tại trong hệ thống !");
			} else { // In case edit user
				if (sysUserByCodeUser != null && sysUserByCodeUser.getId() != user.getId())
					msg.append(" - Mã người dùng đã tồn tại trong hệ thống !");
			}
		}
		// Check password
		if (user.getId() <= 0) { // In case add new User
			if (StringUtils.isEmpty(textbox.getValue()))
				msg.append(" - Mật khẩu không được để trống !");
			else if (!StringUtils.checkLength(textbox.getValue(), 4, 12))
				msg.append(" - Độ dài mật khẩu chỉ từ 4 đến 12 ký tự !");
		} else { // In case edit User
			if (StringUtils.isNotEmpty(textbox.getValue()) && !StringUtils.checkLength(textbox.getValue(), 4, 12))
				msg.append(" - Độ dài mật khẩu chỉ từ 4 đến 12 ký tự !");
		}
		// Check mobile number
		if (StringUtils.isNotEmpty(user.getExtNumber()) && !StringUtils.isValidPhoneNumber(user.getExtNumber())) {
			msg.append(" - Số điện thoại không hợp lệ !");
		}

		return msg.toString();
	}

	private SysUser checkExistUserField(String fieldName, String value) {
		SysUserController controller = (SysUserController) ControllerUtils.getController(SysUserController.class);
		List<SysUser> lstModel = controller.find("from SysUser where " + fieldName + " = ?", value);
		if (lstModel != null && lstModel.size() > 0)
			return lstModel.get(0);
		return null;
	}

	protected boolean checkName() {
		boolean result = false;
		SysUser model = (SysUser) this.getModel();
		String username = model.getUserName();
		if (username == null) {
			Env.getHomePage().showNotification("Bạn không được để trống tên đăng nhập", Clients.NOTIFICATION_TYPE_INFO);
		} else if (username.length() <= 0) {
			Env.getHomePage().showNotification("Bạn không được để trống tên đăng nhập", Clients.NOTIFICATION_TYPE_INFO);
		} else if (username.trim().length() <= 0) {
			Env.getHomePage().showNotification("Bạn không được để tên đăng nhập toàn khoảng trắng",
					Clients.NOTIFICATION_TYPE_INFO);
		} else if (model.getId() == 0) {
			username = username.trim();
			SysUserController controller = (SysUserController) ControllerUtils.getController(SysUserController.class);
			List<SysUser> lstModel = controller.find("from SysUser where user = ?", username);
			if (lstModel.size() > 0) {
				Env.getHomePage().showNotification("Tên đăng nhập bị trùng. Bạn cần chọn tên khác",
						Clients.NOTIFICATION_TYPE_ERROR);
			} else {
				result = true;
			}
			model.setUser(username);
		} else {
			username = username.trim();
			SysUserController controller = (SysUserController) ControllerUtils.getController(SysUserController.class);
			List<SysUser> lstModel = controller.find("from SysUser where user = ? and id != ?",
					new Object[] { username, model.getId() });
			if (lstModel.size() > 0) {
				Env.getHomePage().showNotification("Tên đăng nhập bị trùng. Bạn cần chọn tên khác",
						Clients.NOTIFICATION_TYPE_ERROR);
			} else {
				result = true;
			}
			model.setUser(username);
		}
		return result;
	}

	public void setDefaultValueChannel() {
		ChannelTms channel = (ChannelTms) AbstractModel.getValue(this.getModel(), "channel");
		if (channel != null) {
			List<Comboitem> lstItem = cbbChannel.getItems();
			for (Comboitem comboitem : lstItem) {
				ChannelTms tmp = comboitem.getValue();
				if (tmp.getId() == channel.getId()) {
					cbbChannel.setSelectedItem(comboitem);
				}
			}

		} else {
			cbbChannel.setSelectedIndex(-1);
			cbbChannel.setSelectedItem(null);
		}
	}

}
