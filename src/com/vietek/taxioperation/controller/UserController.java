package com.vietek.taxioperation.controller;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.vietek.taxioperation.model.User;

@Repository
public class UserController extends BasicController<User> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6917301546642516266L;

	public int login(String accountName, String pass) {
		List<User> user = this.find("from User where accountName=? and password=?", accountName, pass);
		if (user.size() == 1 && user.get(0).isActive())
			return user.get(0).getId();
		return -1;
	}
	
	public List<User> getListUser() {
		List<User> user = this.find("from User order by fullName");
		return user;
	}
}
