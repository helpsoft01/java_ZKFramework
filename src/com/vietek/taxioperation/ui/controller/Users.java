package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.UserController;
import com.vietek.taxioperation.model.User;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class Users extends AbstractWindowPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsersDetail usersDetail = null;
	public Users() {
		super(true);
		this.setTitle("Danh mục người dùng");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}
	

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã người dùng", 100, String.class, "getExtNumber"));
		lstCols.add(new GridColumn("Tên đầy đủ", 200, String.class, "getFullName"));
		lstCols.add(new GridColumn("Tài khoản", 100, String.class, "getAccountName"));
		lstCols.add(new GridColumn("Mật khẩu", 100, String.class, "getPassword"));
		lstCols.add(new GridColumn("Kích hoạt", 80, Boolean.class, "getIsActive"));
		lstCols.add(new GridColumn("Ngày sinh", 80, Timestamp.class, "getBirthDay"));
		lstCols.add(new GridColumn("Địa chỉ", 200, String.class, "getAddress"));
		lstCols.add(new GridColumn("Email", 200, String.class, "getEmail"));
		lstCols.add(new GridColumn("Số điện thoại", 100, String.class, "getPhoneNumber"));
//		lstCols.add(new GridColumn("Vai trò", 200, Role.class, "getRoles"));
		setGridColumns(lstCols);
	}

	@Override
	public void loadData() {
		UserController userController = (UserController) ControllerUtils.getController(UserController.class);
		List<User> lstValue = userController.find("from User");
		setLstModel(lstValue);
		setModelClass(User.class);
	}
	
	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (usersDetail == null) {
			usersDetail = new UsersDetail(getCurrentModel(), this);
		}
		return usersDetail;
	}
}
