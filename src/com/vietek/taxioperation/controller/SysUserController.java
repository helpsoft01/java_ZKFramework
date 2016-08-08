package com.vietek.taxioperation.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.util.SecurityUtils;

/**
 * 
 * @author VuD
 * 
 */
@Repository
public class SysUserController extends BasicController<SysUser> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int login(String accountName, String pass) {
		String encryptPass = SecurityUtils.encryptMd5(pass);
		List<SysUser> user = this.find("from SysUser where user=? and password=?", accountName, encryptPass);
		if (user.size() == 1 && user.get(0).getIsActive())
			return user.get(0).getId();
		return -1;
	}

	public List<SysUser> getListUser() {
		List<SysUser> user = this.find("from SysUser order by name");
		return user;
	}

	public List<SysUser> getLstUser(String user, String value) {
		List<SysUser> lstUser;
		if (value == "")
			lstUser = this.find("from SysUser where user!=? order by name", user);
		else
			lstUser = this.find("from SysUser where user!=? and name like ? order by name", user, "%" + value + "%");
		return lstUser;
	}

	public SysUser loginUser(String accountName, String pass) {
		SysUser user = null;
		String encryptPass = SecurityUtils.encryptMd5(pass);
		List<SysUser> lstUser = this.find("from SysUser where user=?", accountName);
		if (lstUser != null && lstUser.size() > 0) {
			user = lstUser.get(0);
		}
		if (user != null) {
			if (!user.getPassword().equals(encryptPass)) {
				user = null;
			}
		}
		return user;
	}

}
