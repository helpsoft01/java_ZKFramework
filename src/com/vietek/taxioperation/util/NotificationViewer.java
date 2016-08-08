package com.vietek.taxioperation.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.model.UserNotification;

public class NotificationViewer {
	public static ConcurrentHashMap<Integer, UserNotification> notices = new ConcurrentHashMap<>();

	public synchronized void setEmpty(){

		if (notices.size() > 0) {
			notices.clear();
		}
	}
	
	public ConcurrentHashMap<Integer, UserNotification> getNotices() {
		return notices;
	}
	
	public synchronized void loadNotices(){
		List<UserNotificationForm> lstResults = new ArrayList<>();
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sip = (SessionImplementor) session;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "Call cmdGetAllDeviceStatus()";
		try {
			conn = sip.connection();
			if (conn != null) {
				cs = conn.prepareCall(sql);
				rs = cs.executeQuery();
				while (rs.next()) {
					UserNotificationForm vro = new UserNotificationForm();
					vro.setNoticeid(rs.getInt("notice_id"));
					vro.setUserid(rs.getInt("user_id"));
					lstResults.add(vro);
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
	}
	
	public synchronized void addNotice(){
		
	}
	
	public synchronized void removeNotice(){
		
	}
	
	
	
	private class UserNotificationForm {
		private int noticeid;
		private int userid;
		private int typeid;
		private Date begindate;
		private Date finishdate;
		private List<String> vehicleinfo;
				
		public List<String> getVehicleinfo() {
			return vehicleinfo;
		}
		public void setVehicleinfo(List<String> vehicleinfo) {
			this.vehicleinfo = vehicleinfo;
		}
		public int getTypeid() {
			return typeid;
		}
		public void setTypeid(int typeid) {
			this.typeid = typeid;
		}
		public Date getBegindate() {
			return begindate;
		}
		public void setBegindate(Date begindate) {
			this.begindate = begindate;
		}
		public Date getFinishdate() {
			return finishdate;
		}
		public void setFinishdate(Date finishdate) {
			this.finishdate = finishdate;
		}
		public int getNoticeid() {
			return noticeid;
		}
		public void setNoticeid(int noticeid) {
			this.noticeid = noticeid;
		}
		public int getUserid() {
			return userid;
		}
		public void setUserid(int userid) {
			this.userid = userid;
		}
				
	}
}
