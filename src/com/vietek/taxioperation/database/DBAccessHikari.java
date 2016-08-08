package com.vietek.taxioperation.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * 
 * @author VuD
 * 
 */

public class DBAccessHikari {
	private static HikariDataSource hds = null;
	private static Boolean is_load_config = false;
	private static String dbUrl;
	private static String dbHost;
	private static String dbPort;
	private static String dbSchema;
	private static String dbUser;
	private static String dbPassword;
	private static Integer minPoolSize;
	private static Integer maxPoolSize;
	private static String driverClass;

	private DBAccessHikari() {

	}

	public static Connection getConnection() {
		if (!is_load_config) {
			loadConfig();
			is_load_config = true;
		}
		hds = getDataSource();
		if (hds == null) {
			return null;
		}
		Connection conn = null;
		try {
			conn = hds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;

	}

	public static void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public static void closeCallableStatement(CallableStatement cs) {
		if (cs != null) {
			try {
				cs.close();
			} catch (Exception e) {
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}

	public static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
	}

	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
			}
		}
	}

	private static HikariDataSource getDataSource() {
		if (hds != null && !hds.isClosed()) {
			return hds;
		} else {
			try {
				hds = new HikariDataSource();
				hds.setDriverClassName(driverClass);
				hds.setJdbcUrl(getConnectionURL());
				hds.setUsername(dbUser);
				hds.setPassword(dbPassword);
				hds.setMaximumPoolSize(maxPoolSize);
				hds.setMaximumPoolSize(minPoolSize);
				hds.setAutoCommit(false);
				hds.addDataSourceProperty("cachePrepStmts", true);
				hds.addDataSourceProperty("prepStmtCacheSize", 250);
				hds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
				hds.addDataSourceProperty("useServerPrepStmts", true);
				hds.addDataSourceProperty("characterEncoding", "utf-8");
			} catch (Exception e) {
				// e.printStackTrace();
				hds = null;
			}
			return hds;
		}

	}

	private static void loadConfig() {
		dbUrl = "jdbc:mysql:";
		dbHost = "125.212.226.54";
		dbPort = "22306";
		dbSchema = "taxioperation";
		dbUser = "root";
		dbPassword = "admin@123";
		minPoolSize = 5;
		maxPoolSize = 10;
		driverClass = "com.mysql.jdbc.Driver";
		
	}

	private static String getConnectionURL() {
		return dbUrl + "//" + dbHost + ":" + dbPort + "/" + dbSchema;
	}

}