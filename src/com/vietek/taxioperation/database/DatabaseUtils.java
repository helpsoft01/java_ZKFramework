package com.vietek.taxioperation.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.zkoss.zk.ui.util.Clients;

import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class DatabaseUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Connection Open() {
		Connection cn = null;
		try {
			Session session = ControllerUtils.getCurrentSession();
			SessionImplementor sessionImplementor = (SessionImplementor) session;
			cn = sessionImplementor.connection();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cn;

	}

	public static boolean Close(Connection cn) {

		if (cn != null) {

			try {
				cn.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;

	}

	public static int getExecuteStor(String phone) {
		ResultSet rs = null;
		int result = 0;
		Connection cn = Open();
		PreparedStatement ps = null;

		if (cn != null) {
			try {
				ps = cn.prepareStatement("CALL tms_dev.cmd_getSumCall(?)");
				ps.setString(1, phone);
				rs = ps.executeQuery();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Close(cn);
			}

		}

		if (rs != null) {
			try {

				result = rs.getInt("sumvalue");

			} catch (Exception e) {
			}

		}
		return result;
	}

	public static int executeUpdate(String storename, List<Object> parameters)
	{
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		int result = -1;
		java.sql.CallableStatement cs = null;
		try {
			conn = sessionImplementor.connection();
			if (conn == null) {
				Env.getHomePage().showNotification("Không thể kết nối lên Server!", Clients.NOTIFICATION_TYPE_ERROR);
				return result;
			}
			cs = conn.prepareCall(storename);
			for (int i = 0; i < parameters.size(); i++) {
				cs.setObject(i + 1, parameters.get(i));
			}
			result = cs.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			Env.getHomePage().showNotification("Không thể cập nhật!", Clients.NOTIFICATION_TYPE_ERROR);
		} finally {
			session.close();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		}

		return result;
	}

}
