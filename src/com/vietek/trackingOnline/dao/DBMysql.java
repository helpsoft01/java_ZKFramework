package com.vietek.trackingOnline.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.vietek.trackingOnline.model.User;

public class DBMysql {

	protected User user = null;

	public DBMysql() {
		if (this.user == null) {
			this.user = Login("admin", "admin");
		}
	}

	public User Login(String UserId, String Pass) {
		User user = new User();
		user.setUserId("admin");
		user.setUserName("user ");
		return user;
	}

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	// static final String DB_URL =
	// "jdbc:mysql://115.146.122.50:3306/gps_tracking?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=UTF-8";
	static final String DB_URL = "jdbc:mysql://125.212.226.54:3306/txm_tracking?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=UTF-8";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "admin@123"; // bkict2014

	public static Connection connection = openConnection();

	public static Connection openConnection() {
		Properties properties = new Properties();
		properties.put("user", USER);
		properties.put("password", PASS);
		properties.put("characterEncoding", "ISO-8859-1");
		properties.put("useUnicode", "true");

		try {
			Class.forName(JDBC_DRIVER).newInstance();
			Connection c = DriverManager.getConnection(DB_URL, properties);
			c.setAutoCommit(true);
			c.setReadOnly(true);
			return c;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
