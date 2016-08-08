package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.SwitchboardTMS;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.SysGroup;
import com.vietek.taxioperation.model.SysRule;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * 
 * @author VuD
 * 
 */

public class SysUsers extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysUsersDetail detailWindow = null;

	public SysUsers() {
		super(true);
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã nhân viên", 150, String.class,
				"getValue","value",getModelClass()));
		lstCols.add(new GridColumn("Họ tên", 200, String.class, "getName","name",getModelClass()));
		lstCols.add(new GridColumn("Tên đăng nhập", 200, String.class,
				"getUser","user",getModelClass()));
		lstCols.add(new GridColumn("Công ty", 200, SysCompany.class,
				"getSysCompany"));
		lstCols.add(new GridColumn("Tổng đài", 200, SwitchboardTMS.class,
				"getSwitchboard"));
		lstCols.add(new GridColumn("Kênh", 150, ChannelTms.class, "getChannel"));
		lstCols.add(new GridColumn("Nhóm", 200, SysGroup.class, "getSysGroup"));
		lstCols.add(new GridColumn("Rule", 200, SysRule.class, "getSetSysRule"));
		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class,
				"getIsActive"));
		lstCols.add(new GridColumn("Số điện thoại viên", 100, String.class,
				"getExtNumber"));
		lstCols.add(new GridColumn("Điều đàm viên", 150, Boolean.class,
				"getIsRadio"));
		lstCols.add(new GridColumn("Điện thoại viên",150,Boolean.class, "getIsTelephonist"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
//		SysUserController controller = (SysUserController) ControllerUtils
//				.getController(SysUserController.class);
//		List<SysUser> lstModel = controller.find("from SysUser");
		this.setModelClass(SysUser.class);
		this.setStrQuery("from SysUser");
//		this.setLstModel(lstModel);

	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new SysUsersDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

}
