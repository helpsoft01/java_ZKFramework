package com.vietek.taxioperation.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.DeviceConfig;
import com.vietek.taxioperation.model.DeviceConfiguration;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.DriverExcelTemp;
import com.vietek.taxioperation.model.DriverInVehicle;
import com.vietek.taxioperation.model.ParkingArea;
import com.vietek.taxioperation.model.ReportCustomerRelative;
import com.vietek.taxioperation.model.ReportDetailOverTenHour;
import com.vietek.taxioperation.model.ReportInputAgent;
import com.vietek.taxioperation.model.ReportInputVehicle;
import com.vietek.taxioperation.model.ReportInputVehicleGroup;
import com.vietek.taxioperation.model.ReportInputVehicleID;
import com.vietek.taxioperation.model.ReportQcActivity;
import com.vietek.taxioperation.model.ReportQcCancelRegistrationDriver;
import com.vietek.taxioperation.model.ReportQcParkingArea;
import com.vietek.taxioperation.model.ReportQcServicePerformance;
import com.vietek.taxioperation.model.ReportQcServicePerformanceTT;
import com.vietek.taxioperation.model.ReportQcShiftUsers;
import com.vietek.taxioperation.model.ReportQcShiftUsersTT;
import com.vietek.taxioperation.model.ReportQcTimeWaitOfCustomer;
import com.vietek.taxioperation.model.ReportQcTripDetail;
import com.vietek.taxioperation.model.ReportQcrptTruckStdDriving;
import com.vietek.taxioperation.model.RptDriverInShift;
import com.vietek.taxioperation.model.RptDriverInShiftLog;
import com.vietek.taxioperation.model.RptGetShiftInfo;
import com.vietek.taxioperation.model.RptQcTruckStdActivityByVehicle;
import com.vietek.taxioperation.model.RptQcTruckStdOverSpeed;
import com.vietek.taxioperation.model.RptQcTruckStdStop;
import com.vietek.taxioperation.model.RptQcTrunkStdDriving;
import com.vietek.taxioperation.model.RptTotalBussinessAgent;
import com.vietek.taxioperation.model.RptTotalViolateQc31;
import com.vietek.taxioperation.model.RptTripSearchingOnline;
import com.vietek.taxioperation.model.ShiftworkTms;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.webservice.TaxiGetOnline;
import com.vietek.taxioperation.webservice.TaxiResult;

public class ListObjectDatabase {
	List<?> ouputList;

	// Danh sach bai giao ca (Khong kem theo groupid)
	public List<ReportQcParkingArea> getParkingArea() {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT lp.ParkingID, lp.ParkingCode, lp.ParkingName, lp.VehicleGroupID FROM txm_tracking.lst_parkingarea lp";
		List<ReportQcParkingArea> lstResult = new ArrayList<ReportQcParkingArea>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcParkingArea msg = new ReportQcParkingArea();
					msg.setPakingareaId(rs.getInt(1));
					msg.setParkingareaCode(rs.getString(2));
					msg.setParkingareaName(rs.getString(3));
					msg.setVehicleGroupId(rs.getInt(4));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	// Danh sach ParkingArea tim theo groupid
	public List<ReportQcParkingArea> getParkingArea(int groupid) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT lp.ParkingID, lp.ParkingCode, lp.ParkingName, lp.VehicleGroupID FROM txm_tracking.lst_parkingarea lp where lp.VehicleGroupID = ?";
		List<ReportQcParkingArea> lstResult = new ArrayList<ReportQcParkingArea>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, groupid);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcParkingArea msg = new ReportQcParkingArea();
					msg.setPakingareaId(rs.getInt(1));
					msg.setParkingareaCode(rs.getString(2));
					msg.setParkingareaName(rs.getString(3));
					msg.setVehicleGroupId(rs.getInt(4));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	// Danh sach parkingArea tim theo lstGroupId
	public List<ReportQcParkingArea> getParkingArea(String lstGroupId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT lp.ParkingID, lp.ParkingCode, lp.ParkingName, lp.VehicleGroupID FROM txm_tracking.lst_parkingarea lp where lp.VehicleGroupID IN ("
				+ lstGroupId + ")";
		List<ReportQcParkingArea> lstResult = new ArrayList<ReportQcParkingArea>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcParkingArea msg = new ReportQcParkingArea();
					msg.setPakingareaId(rs.getInt(1));
					msg.setParkingareaCode(rs.getString(2));
					msg.setParkingareaName(rs.getString(3));
					msg.setVehicleGroupId(rs.getInt(4));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	// Danh sach Agent
	public List<ReportInputAgent> getAgent() {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT la.AgentID, la.AgentCode, la.AgentName FROM txm_tracking.lst_agent la";
		List<ReportInputAgent> lstResult = new ArrayList<ReportInputAgent>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportInputAgent msg = new ReportInputAgent();
					msg.setAgentId(rs.getInt(1));
					msg.setAgentCode(rs.getString(2));
					msg.setAgentName(rs.getString(3));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	public List<ReportInputVehicleGroup> getVehicleGroup() {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT lv.GroupID, lv.GroupName FROM txm_tracking.lst_vehiclegroup lv";
		List<ReportInputVehicleGroup> lstResult = new ArrayList<ReportInputVehicleGroup>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportInputVehicleGroup msg = new ReportInputVehicleGroup();
					msg.setGroupId(rs.getInt(1));
					msg.setGroupName(rs.getString("GroupName"));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	// Tim VehicleGroup theo agentId
	public List<ReportInputVehicleGroup> getVehicleGroup(int agentid) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT lv.GroupID, lv.GroupName, lv.AgentID FROM txm_tracking.lst_vehiclegroup lv where lv.AgentID = ?";
		List<ReportInputVehicleGroup> lstResult = new ArrayList<ReportInputVehicleGroup>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, agentid);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportInputVehicleGroup msg = new ReportInputVehicleGroup();
					msg.setGroupId(rs.getInt(1));
					msg.setGroupName(rs.getString("GroupName"));
					msg.setAgentId(rs.getInt(3));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	// Tim Group theo lstAgentId
	public List<TaxiGroup> getVehicleGroup(String lstAgentId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT lv.GroupID, lv.GroupName, lv.AgentID FROM txm_tracking.lst_vehiclegroup lv where lv.AgentID IN ("
				+ lstAgentId + ")";
		List<TaxiGroup> lstResult = new ArrayList<TaxiGroup>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					TaxiGroup msg = new TaxiGroup();
					msg.setId(rs.getInt(1));
					msg.setName(rs.getString("GroupName"));
					msg.setAgentID(rs.getInt(3));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	// Get list vehicle
	public List<ReportInputVehicle> getVehicle() {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT lv.VehicleID, lv.VehicleNumber, lv.LicensePlate, lv.DeviceID FROM txm_tracking.lst_vehicle lv";
		List<ReportInputVehicle> lstResult = new ArrayList<ReportInputVehicle>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportInputVehicle msg = new ReportInputVehicle();
					msg.setVehicleId(rs.getInt(1));
					msg.setVehicleNumber(rs.getInt(2));
					msg.setLicensePlate(rs.getString(3));
					msg.setDeviceId(rs.getInt(4));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	// Tìm Vehicle theo lstGroupId
	public List<ReportInputVehicle> getVehicle(String lstGroupVehId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM txm_tracking.lst_vehicle lv where lv.VehicleGroupID IN (" + lstGroupVehId + ")";
		List<ReportInputVehicle> lstResult = new ArrayList<ReportInputVehicle>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportInputVehicle msg = new ReportInputVehicle();
					msg.setVehicleId(rs.getInt(1));
					msg.setVehicleNumber(rs.getInt(2));
					msg.setLicensePlate(rs.getString(3));
					msg.setDeviceId(rs.getInt(5));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	public List<ParkingArea> getParking(String lstGroupVehId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM txm_tracking.lst_parkingarea Where FIND_IN_SET(VehicleGroupID,'" + lstGroupVehId
				+ "') And Active = 1;";
		List<ParkingArea> lstResult = new ArrayList<ParkingArea>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					ParkingArea msg = new ParkingArea();
					msg.setId(rs.getInt(1));
					msg.setParkingName(rs.getString(4));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	// Tim Driver theo GroupId
	public List<Driver> getDriver(String lstGroupVehId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM txm_tracking.lst_driver ld where ld.VehicleGroupID IN (" + lstGroupVehId + ")";
		List<Driver> lstResult = new ArrayList<Driver>();

		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					Driver msg = new Driver();
					msg.setId(rs.getInt(1));
					msg.setName(rs.getString(3));
					lstResult.add(msg);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Dong Resultset
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong Prepare Statement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong connection
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Dong session
			session.close();
		}
		return lstResult;
	}

	public List<ReportInputVehicleID> getVehicleID() {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT lv.VehicleID, lv.VehicleNumber, lv.LicensePlate, lv.VehicleGroupID FROM txm_tracking.lst_vehicle lv";
		List<ReportInputVehicleID> lstResult = new ArrayList<ReportInputVehicleID>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportInputVehicleID msg = new ReportInputVehicleID();
					msg.setVehicleID(rs.getInt(1));
					msg.setVehicleNumber(rs.getString(2));
					msg.setLicensePlate(rs.getString(3));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	public List<ReportInputVehicleID> getVehicleID(int groupID) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT lv.VehicleID, lv.VehicleNumber, lv.LicensePlate, lv.VehicleGroupID FROM txm_tracking.lst_vehicle lv where lv.VehicleGroupID = ?";
		List<ReportInputVehicleID> lstResult = new ArrayList<ReportInputVehicleID>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, groupID);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportInputVehicleID msg = new ReportInputVehicleID();
					msg.setVehicleID(rs.getInt(1));
					msg.setVehicleNumber(rs.getString(2));
					msg.setLicensePlate(rs.getString(3));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Tập các bản ghi thông tin hiển thị lên GridData theo các tham số
	public List<ReportQcrptTruckStdDriving> getrptTruckStdDriving(String x, Timestamp l, Timestamp m, String y,
			String z, int k) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.rptTruckStdDriving(?, ?, ?, ?, ?, ?)";
		List<ReportQcrptTruckStdDriving> lstResult = new ArrayList<ReportQcrptTruckStdDriving>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, x);
				ps.setTimestamp(2, l);
				ps.setTimestamp(3, m);
				ps.setString(4, y);
				ps.setString(5, z);
				ps.setInt(6, k);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcrptTruckStdDriving msg = new ReportQcrptTruckStdDriving();
					msg.setRptID(rs.getInt(1));
					msg.setCompanyName(rs.getString(2));
					msg.setGroupName(rs.getString(3));
					msg.setVehicletruckNumber(rs.getString(4));
					msg.setDriverName(rs.getString(5));
					msg.setLicensePlate(rs.getString(6));
					msg.setVehicleNumber(rs.getInt(7));
					msg.setDriverLicense(rs.getString(8));
					msg.setTypeName(rs.getString(9));
					msg.setBeginTime(rs.getDate(10));
					msg.setEndTime(rs.getDate(11));
					msg.setTimeOver(rs.getInt(12));
					msg.setTimeOverStr(rs.getString(13));
					msg.setBeginLocation(rs.getString(14));
					msg.setEndLocation(rs.getString(15));
					msg.setBeginAddr(rs.getString(16));
					msg.setFinishAddr(rs.getString(17));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	public List<TaxiResult> getListVehicle(double latitude, double longitude) {
		List<TaxiResult> lstResult = null;
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		java.sql.CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sessionImplementor.connection();
			if (conn == null) {
				return lstResult;
			}
			lstResult = new ArrayList<TaxiResult>();
			cs = conn.prepareCall("call txm_tracking.cmdGetCarMobile(?, ?, ?, ?, ?, ?)");
			cs.setDouble(1, longitude);
			cs.setDouble(2, latitude);
			cs.setInt(3, 500);
			cs.setInt(4, 5);
			cs.setInt(5, 4);
			cs.setString(6, "admin");
			rs = cs.executeQuery();
			while (rs.next()) {
				TaxiResult tx = new TaxiResult();
				tx.setId(rs.getInt("id"));
				tx.setTimeLog(rs.getTimestamp("TimeLog"));
				tx.setLongitude(rs.getDouble("Longi"));
				tx.setLatitude(rs.getDouble("Lati"));
				tx.setLastGpsSpeed(rs.getInt("LastGPSSpeed"));
				tx.setLicensePlace(rs.getString("LicensePlate"));
				tx.setVehicleNumber(rs.getString("VehicleNumber"));
				tx.setGroupName(rs.getString("GroupName"));
				tx.setTypeName(rs.getString("TypeName"));
				tx.setDriverName(rs.getString("DriverName"));
				tx.setRegisterNumber(rs.getString("RegisterNumber"));
				tx.setMobileNumber(rs.getString("MobileNumber"));
				tx.setDis(rs.getDouble("dis"));
				tx.setAngle(rs.getDouble("Angle"));
				lstResult.add(tx);

			}
		} catch (Exception e) {
			e.printStackTrace();
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
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}
		return lstResult;
	}

	public List<TaxiGetOnline> getListTaxiOnline(double latitude, double longitude) {
		List<TaxiGetOnline> lstResultOnline = null;
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		java.sql.CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sessionImplementor.connection();
			if (conn == null) {
				return lstResultOnline;
			}
			lstResultOnline = new ArrayList<TaxiGetOnline>();
			cs = conn.prepareCall("call txm_tracking.cmdGetCarOnline(?, ?, ?, ?, ?, ?)");
			cs.setDouble(1, longitude);
			cs.setDouble(2, latitude);
			cs.setInt(3, 500);
			cs.setInt(4, 5);
			cs.setInt(5, 4);
			cs.setString(6, "admin");
			rs = cs.executeQuery();
			while (rs.next()) {
				TaxiGetOnline tgo = new TaxiGetOnline();
				tgo.setIdVehicle(rs.getInt("vehicleID"));
				tgo.setLongitude(rs.getDouble("Longi"));
				tgo.setLatitude(rs.getDouble("Lati"));
				tgo.setLicensePlate(rs.getString("LicensePlate"));
				tgo.setVehicleNumber(rs.getString("vehiclenumbers"));
				tgo.setTypeName(rs.getInt("typename"));
				lstResultOnline.add(tgo);

			}
		} catch (Exception e) {
			e.printStackTrace();
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
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}
		return lstResultOnline;
	}

	public List<SysUser> getSysuser() {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM ad_user au where isTelephonist = true";
		List<SysUser> lstResult = new ArrayList<SysUser>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					SysUser msg = new SysUser();
					msg.setId(rs.getInt(1));
					msg.setName(rs.getString(5));
					msg.setUser(rs.getString(9));
					msg.setValue(rs.getString(10));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	public List<ShiftworkTms> getShift() {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM cat_shiftwork";
		List<ShiftworkTms> lstResult = new ArrayList<ShiftworkTms>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					ShiftworkTms msg = new ShiftworkTms();
					msg.setId(rs.getInt(1));
					msg.setName(rs.getString(5));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Tập các bản ghi thông tin chi tiết về ca làm việc hiển thị lên GridData
	// theo các
	// tham số
	public List<ReportQcShiftUsers> getrptShiftWorkingDetailByTelephonist(Timestamp a, Timestamp b, String x,
			String y) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL rptShiftWorkingDetailByTelephonist(?, ?, ?, ?)";
		List<ReportQcShiftUsers> lstResult = new ArrayList<ReportQcShiftUsers>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, a);
				ps.setTimestamp(2, b);
				ps.setString(3, x);
				ps.setString(4, y);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcShiftUsers msg = new ReportQcShiftUsers();
					msg.setUserid(rs.getInt(1));
					msg.setOrdertime(rs.getTimestamp(2));
					msg.setUsername(rs.getString(3));
					msg.setShiftname(rs.getString(4));
					msg.setCallnumber(rs.getString(5));
					msg.setLogintime(rs.getDate(6));
					msg.setLogouttime(rs.getDate(7));
					msg.setTotalcall(rs.getInt(8));
					msg.setAcceptcall(rs.getInt(9));
					msg.setDeclinecall(rs.getInt(10));
					msg.setPercent(rs.getFloat(11));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Tập các bản ghi thông tin tổng hợp về ca làm việc hiển thị lên GridData
	// theo các tham số
	public List<ReportQcShiftUsersTT> getrptShiftWorkingByTelephonist(Timestamp a, Timestamp b, String x, String y) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL rptShiftWorkingByTelephonist(?, ?, ?, ?)";
		List<ReportQcShiftUsersTT> lstResult = new ArrayList<ReportQcShiftUsersTT>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, a);
				ps.setTimestamp(2, b);
				ps.setString(3, x);
				ps.setString(4, y);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcShiftUsersTT msg = new ReportQcShiftUsersTT();
					msg.setUserid(rs.getInt(1));
					msg.setUsername(rs.getString(2));
					msg.setTotalcall(rs.getInt(3));
					msg.setAcceptcall(rs.getInt(4));
					msg.setDeclinecall(rs.getInt(5));
					msg.setPercent(rs.getFloat(6));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Tập các bản ghi thông tin chi tiết về hiệu suất phục vụ hiển thị lên
	// GridData theo các tham số
	public List<ReportQcServicePerformance> getrptShiftProductivityDetail(Timestamp a, Timestamp b, String x) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL rptShiftProductivityDetail(?, ?, ?)";
		List<ReportQcServicePerformance> lstResult = new ArrayList<ReportQcServicePerformance>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, a);
				ps.setTimestamp(2, b);
				ps.setString(3, x);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcServicePerformance msg = new ReportQcServicePerformance();
					msg.setShiftday(rs.getDate(1));
					msg.setShiftname(rs.getString(2));
					msg.setCustomer(rs.getInt(3));
					msg.setRequestnumber(rs.getInt(4));
					msg.setRepeattime(rs.getInt(5));
					msg.setTotalcall(rs.getInt(6));
					msg.setAcceptcall(rs.getInt(7));
					msg.setDeclinecall(rs.getInt(8));
					msg.setPercent(rs.getFloat(9));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Tập các bản ghi thông tin tổng hợp về hiệu suất phục vụ hiển thị lên
	// GridData theo
	// các tham số
	public List<ReportQcServicePerformanceTT> getrptShiftProductivity(Timestamp a, Timestamp b, String x) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL rptShiftProductivity(?, ?, ?)";
		List<ReportQcServicePerformanceTT> lstResult = new ArrayList<ReportQcServicePerformanceTT>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, a);
				ps.setTimestamp(2, b);
				ps.setString(3, x);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcServicePerformanceTT msg = new ReportQcServicePerformanceTT();
					msg.setShiftname(rs.getString(1));
					msg.setCustomer(rs.getInt(2));
					msg.setRequestnumber(rs.getInt(3));
					msg.setRepeattime(rs.getInt(4));
					msg.setTotalcall(rs.getInt(5));
					msg.setAcceptcall(rs.getInt(6));
					msg.setDeclinecall(rs.getInt(7));
					msg.setPercent(rs.getFloat(8));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}
	
	public List<ReportQcTripDetail> getrptCallingByOperation(Timestamp fromdate, Timestamp todate, String phone,
			String veh, int state) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL rptCallingByOperation(?,?,?,?,?)";
		List<ReportQcTripDetail> lstResult = new ArrayList<ReportQcTripDetail>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, fromdate);
				ps.setTimestamp(2, todate);
				ps.setString(3, phone);
				ps.setString(4, veh);
				ps.setInt(5, state);
				rs = ps.executeQuery();

				while (rs.next()) {
					ReportQcTripDetail msg = new ReportQcTripDetail();
					msg.setId(rs.getInt(1));
					msg.setBeginOrderAddress(rs.getString(2));
					msg.setBeginOrderTime(rs.getTimestamp(3));
					msg.setPhoneNumber(rs.getString(4));
					msg.setStartRegisterTime(rs.getTimestamp(5));
					msg.setListRegisterVeh(rs.getString(6));
					msg.setTimeIsUpdated(rs.getTimestamp(7));
					msg.setOrderCarType(rs.getString(8));
					msg.setCancelReason(rs.getString(9));
					msg.setOrderStatus(rs.getString(10));
					msg.setTaxi_id(rs.getInt(11));
					msg.setSelectedVeh(rs.getString(12));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	public List<ReportQcTripDetail> getrptCallingByOperationDetail(Timestamp fromdate, Timestamp todate, String phone) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL rptCallingByOperationDetail(?,?,?)";
		List<ReportQcTripDetail> lstResult = new ArrayList<ReportQcTripDetail>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, fromdate);
				ps.setTimestamp(2, todate);
				ps.setString(3, phone);
				rs = ps.executeQuery();

				while (rs.next()) {
					ReportQcTripDetail msg = new ReportQcTripDetail();
					msg.setId(rs.getInt(1));
					msg.setBeginOrderAddress(rs.getString(2));
					msg.setBeginOrderTime(rs.getTimestamp(3));
					msg.setPhoneNumber(rs.getString(4));
					msg.setStartRegisterTime(rs.getTimestamp(5));
					msg.setListRegisterVeh(rs.getString(6));
					msg.setTimeIsUpdated(rs.getTimestamp(7));
					msg.setOrderCarType(rs.getString(8));
					msg.setCancelReason(rs.getString(9));
					msg.setOrderStatus(rs.getString(10));
					msg.setTaxi_id(rs.getInt(11));
					msg.setSelectedVeh(rs.getString(12));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Tập các bản ghi thông tin cuốc khách đặt xe qua tổng đài hiển thị lên
	// GridData theo
	// các tham số
	public List<ReportQcTripDetail> getrptTripDetail(Timestamp fromdate, Timestamp todate, String phone) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL rptTripDetailFromOperation(?,?,?)";
		List<ReportQcTripDetail> lstResult = new ArrayList<ReportQcTripDetail>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, fromdate);
				ps.setTimestamp(2, todate);
				ps.setString(3, phone);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcTripDetail msg = new ReportQcTripDetail();
					msg.setId(rs.getInt(1));
					msg.setBeginOrderAddress(rs.getString(2));
					msg.setBeginOrderTime(rs.getTimestamp(3));
					msg.setPhoneNumber(rs.getString(4));
					msg.setStartRegisterTime(rs.getTimestamp(5));
					msg.setListRegisterVeh(rs.getString(6));
					msg.setTimeIsUpdated(rs.getTimestamp(7));
					msg.setTaxi_id(rs.getInt(8));
					msg.setSelectedVeh(rs.getString(9));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Tập các bản ghi thông tin khách chờ lâu chưa có xe (tính từ thời điểm
	// hiện tại)

	public List<ReportQcTimeWaitOfCustomer> getrptTimeWaitOfCustomer(Timestamp x, String y) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.rptTimeWaitOfCustomers(?,?)";
		List<ReportQcTimeWaitOfCustomer> lstResult = new ArrayList<ReportQcTimeWaitOfCustomer>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, x);
				ps.setString(2, y);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcTimeWaitOfCustomer msg = new ReportQcTimeWaitOfCustomer();
					msg.setBeginOrderTime(rs.getString(1));
					msg.setNumberMobile(rs.getString(2));
					msg.setBeginOrderAddress(rs.getString(3));
					msg.setMinute(rs.getString(4));
					msg.setRepeatTime(rs.getString(5));
					msg.setStatus(rs.getString(6));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Tập các bản ghi thông tin danh sách lái xe hủy đăng ký cuốc khách

	public List<ReportQcCancelRegistrationDriver> getrptCancelRegistrationDriver(Timestamp x, Timestamp y) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL vt_tms.rptCancelRegistrationDrivers(?,?)";
		List<ReportQcCancelRegistrationDriver> lstResult = new ArrayList<ReportQcCancelRegistrationDriver>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, x);
				ps.setTimestamp(2, y);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcCancelRegistrationDriver msg = new ReportQcCancelRegistrationDriver();
					msg.setPhoneCustomer(rs.getString(1));
					msg.setTimeorder(rs.getTimestamp(2));
					msg.setPlaceorder(rs.getString(3));
					msg.setNameDriver(rs.getString(4));
					msg.setVehicleNumber(rs.getInt(5));
					msg.setLicensePlate(rs.getString(6));
					msg.setTimeRegister(rs.getString(7));
					msg.setTimeCancel(rs.getString(8));
					msg.setReasonCancel(rs.getString(9));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Tập các bản ghi thông tin lượng khách đặt xe theo thời gian (Tổng hợp)

	public List<List<Object>> getrptCustomersOrderVehiclesTT(Timestamp x, Timestamp y, String z) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL vt_tms.rptCustomerOrderVehicleTT(?,?,?)";
		List<List<Object>> lstResult = new ArrayList<List<Object>>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, x);
				ps.setTimestamp(2, y);
				ps.setString(3, z);
				rs = ps.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
					List<Object> row = new ArrayList<Object>();
					row.add(dateformat.format(rs.getTimestamp(1)));
					row.add(rs.getInt(2));
					row.add(rs.getString(3));
					row.add(rs.getInt(4));
					lstResult.add(row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	/* */
	public List<Agent> getAgentsByUserPermissions(String username) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdGetAgentByUserOnline(?,?)";
		List<Agent> lstResult = new ArrayList<Agent>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, username);
				ps.setInt(2, 0);
				rs = ps.executeQuery();
				while (rs.next()) {
					Agent row = new Agent();
					row.setId(rs.getInt(1));
					row.setAgentName(rs.getString(2));
					lstResult.add(row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	/**
	 * @author Dungnd
	 * @return Thong ke khach hang thuong xuyen, kem dieu kien thoi gian
	 */

	public List<ReportCustomerRelative> getrptCustomerRelative(Timestamp fromdate, Timestamp todate, String phone,
			int amount) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL vt_tms.rptCustomerRelative(?,?,?,?)";
		List<ReportCustomerRelative> lstResult = new ArrayList<ReportCustomerRelative>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, fromdate);
				ps.setTimestamp(2, todate);
				ps.setString(3, phone);
				ps.setInt(4, amount);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportCustomerRelative row = new ReportCustomerRelative();
					row.setId(rs.getInt(1));
					row.setPhone(rs.getString(2));
					row.setCallAmount(rs.getInt(3));
					row.setCallSuccess(rs.getInt(4));
					row.setLastOrderTime(rs.getTimestamp(5));
					row.setLastOrderAddress(rs.getString(6));
					lstResult.add(row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Danh sach Tim Kiem Cuoc Khach
	public List<RptTripSearchingOnline> getTripSearchingOnline(Timestamp fromdate, Timestamp todate, String userid,
			String agentid, String vehgroupid, String parkingid, String truckno, String trucknumber) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.frmTripSearchingOnline(?,?,?,?,?,?,?,?)";
		List<RptTripSearchingOnline> lstResult = new ArrayList<RptTripSearchingOnline>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, fromdate);
				ps.setTimestamp(2, todate);
				ps.setString(3, userid);
				ps.setString(4, agentid);
				ps.setString(5, vehgroupid);
				ps.setString(6, parkingid);
				ps.setString(7, truckno);
				ps.setString(8, trucknumber);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptTripSearchingOnline msg = new RptTripSearchingOnline();
					msg.setTripId(rs.getInt(1));
					msg.setShiftId(rs.getInt(2));
					msg.setVehicleId(rs.getInt(3));
					msg.setGroupName(rs.getString(4));
					msg.setVehicleNumber(rs.getString(5));
					msg.setLicensePlate(rs.getString(6));
					msg.setVehicleType(rs.getString(7));
					msg.setTimeStart(rs.getTimestamp(8));
					msg.setTimeFinish(rs.getTimestamp(9));
					msg.setPlaceStart(rs.getString(10));
					msg.setPlaceFinish(rs.getString(11));
					msg.setClock(rs.getInt(12));
					msg.setPriceTrip(rs.getInt(13));
					msg.setReduce(rs.getInt(14));
					msg.setKm(rs.getFloat(15));
					msg.setTripDetail(rs.getString(16));
					msg.setDriver("" + (rs.getString(17) == null ? "" : "" + rs.getString(17)));
					msg.setReason(rs.getInt(20));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Update cuoc khach
	public void updateTripSearchingOnline(int shiftId, int tripId, int cash, int reason, String userId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdUpdateTripInfo(?,?,?,?,?)";
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, shiftId);
				ps.setInt(2, tripId);
				ps.setInt(3, cash);
				ps.setInt(4, reason);
				ps.setString(5, userId);
				rs = ps.executeQuery();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
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

	// Tìm kiếm cuốc theo ShiftId
	public List<RptTripSearchingOnline> getTripSearchingOnlineDetail(int shiftId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdGetTripByShift(?)";
		List<RptTripSearchingOnline> lstResult = new ArrayList<RptTripSearchingOnline>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, shiftId);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptTripSearchingOnline msg = new RptTripSearchingOnline();
					msg.setGroupName("");
					msg.setVehicleNumber(rs.getString(4));
					msg.setLicensePlate(rs.getString(3));
					msg.setStaffCard(rs.getString(6));
					msg.setVehicleType(rs.getString(7));
					msg.setTimeStart((rs.getTimestamp(8)));
					msg.setTimeFinish(rs.getTimestamp(9));
					msg.setPlaceStart(rs.getString(10));
					msg.setPlaceFinish(rs.getString(11));
					msg.setClock(rs.getInt(12));
					msg.setPriceTrip(rs.getInt(13));
					msg.setReduce(rs.getInt(14));
					msg.setKm(rs.getFloat(15));
					msg.setEmpKm(rs.getFloat(16));
					msg.setTripDetail("");
					msg.setDriver("" + (rs.getString(5) == null ? "" : "" + rs.getString(5)));
					msg.setReason(rs.getInt(17));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Danh sach len ca
	@SuppressWarnings("finally")
	public List<RptDriverInShift> getDriverInShift(String agentid, String vehgroupid, String parkingid,
			Timestamp shifttime, int lastId, int amount, String userId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdGetDriverInShift(?,?,?,?,?,?,?)";
		List<RptDriverInShift> lstResult = new ArrayList<RptDriverInShift>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, agentid);
				ps.setString(2, vehgroupid);
				ps.setString(3, parkingid);
				ps.setTimestamp(4, shifttime);
				ps.setInt(5, lastId);
				ps.setInt(6, amount);
				ps.setString(7, userId);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptDriverInShift msg = new RptDriverInShift();
					msg.setIssueDate(rs.getTimestamp(3));
					msg.setLicensePlate(rs.getString(4));
					msg.setVehicleNumber(rs.getString(5));
					msg.setStaffCard(rs.getString(6) == null ? "" : "" + rs.getString(6));
					msg.setDriverName(rs.getString(7));
					msg.setPhoneNumber(rs.getString(8));
					msg.setDriverState(rs.getString(9));
					msg.setTimeDrivingContinuous(rs.getString(11) == null ? "" : "" + rs.getString(11));
					msg.setTimeDrivingPerDay(rs.getString(10) == null ? "" : "" + rs.getString(10));
					msg.setParkingName(rs.getString(12));
					msg.setGroupName(rs.getString(13));
					msg.setTypeName(rs.getString(14));
					msg.setTimeLog(rs.getTimestamp(15).toString() == null ? new Timestamp(0l) : rs.getTimestamp(15));
					msg.setInworkshop(rs.getInt(16));
					msg.setInaccident(rs.getInt(17));
					msg.setInparking(rs.getInt(18));
					msg.setNote(rs.getString(19));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return lstResult;
		}
	}

	// Lich su len ca
	public List<RptDriverInShiftLog> getDriverInShiftLog(String agentid, String vehgroupid, String parkingid,
			Timestamp starttime, Timestamp stoptime, String userId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdGetDriverInShiftLogFull(?,?,?,?,?,?)";
		List<RptDriverInShiftLog> lstResult = new ArrayList<RptDriverInShiftLog>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, agentid);
				ps.setString(2, vehgroupid);
				ps.setString(3, parkingid);
				ps.setTimestamp(4, starttime);
				ps.setTimestamp(5, stoptime);
				ps.setString(6, userId);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptDriverInShiftLog msg = new RptDriverInShiftLog();
					msg.setTimeLog(rs.getTimestamp(11));
					msg.setLicensePlate(rs.getString(3));
					msg.setVehicleNumber(rs.getString(4));
					msg.setStaffCard(rs.getString(7) == null ? "" : "" + rs.getString(7));
					msg.setDriverName(rs.getString(5) == null ? "" : "" + rs.getString(5));
					msg.setPhoneNumber(rs.getString(6) == null ? "" : "" + rs.getString(6));
					msg.setDriverState(rs.getString(13));
					msg.setParkingName(rs.getString(8) == null ? "" : "" + rs.getString(8));
					msg.setGroupName(rs.getString(9));
					msg.setTypeName(rs.getString(10));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Dữ liệu chốt ca
	public List<RptGetShiftInfo> getShiftInfo(Timestamp starttime, Timestamp stoptime, String agentid,
			String vehgroupid, String parkingid, int payment, String userId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdGetShiftInfo(?,?,?,?,?,?,?)";
		List<RptGetShiftInfo> lstResult = new ArrayList<RptGetShiftInfo>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, starttime);
				ps.setTimestamp(2, stoptime);
				ps.setString(3, agentid);
				ps.setString(4, vehgroupid);
				ps.setString(5, parkingid);
				ps.setInt(6, payment);
				ps.setString(7, userId);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptGetShiftInfo msg = new RptGetShiftInfo();
					msg.setShiftId(rs.getInt(1));
					msg.setTimeBegin(rs.getTimestamp(2));
					msg.setTimeEnd(rs.getTimestamp(3));
					msg.setLicensePlate(rs.getString(4));
					msg.setVehicleNumber(rs.getString(5));
					msg.setDriverName(rs.getString(6));
					msg.setStaffCard(rs.getString(7));
					msg.setBusinessType(rs.getString(8));
					msg.setHourOfShift(rs.getInt(9));
					msg.setPath(rs.getFloat(10));
					msg.setTripPath(rs.getFloat(11));
					msg.setTripNumber(rs.getInt(12));
					msg.setMoney(rs.getInt(13));
					msg.setReduceMoney(rs.getInt(14));
					msg.setRealShiftMoney(rs.getInt(15));
					msg.setConfirm(rs.getInt(16));
					msg.setConfirmStr(rs.getString(17));
					msg.setMoneyMeter(rs.getInt(18));
					msg.setPrint(rs.getInt(19));
					msg.setPulseCut(rs.getInt(20));
					msg.setPulseLance(rs.getInt(21));
					msg.setGpsLost(rs.getInt(22));
					msg.setPowerLost(rs.getInt(23));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	public List<DeviceConfiguration> getDeviceNoneConfig(String groupid, String username) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdGetNoneConfigDevice(?,?)";
		List<DeviceConfiguration> lstResult = new ArrayList<DeviceConfiguration>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, groupid);
				ps.setString(2, username);
				rs = ps.executeQuery();
				while (rs.next()) {
					DeviceConfiguration msg = new DeviceConfiguration();
					msg.setDeviceID(rs.getInt(1));
					msg.setImei(rs.getString(2));
					msg.setGroupName(rs.getString(3));
					msg.setParkingName(rs.getString(4));
					msg.setTypeName(rs.getString(5));
					msg.setLicensePlate(rs.getString(6));
					msg.setVehicleNumber(rs.getString(7));
					msg.setVinNumber(rs.getString(8));
					msg.setDigitalValue(rs.getString(9));
					msg.setDigital(rs.getInt(10));
					msg.setSpeedLimit(rs.getInt(11));
					msg.setPulsePerKm(rs.getInt(12));
					msg.setCreatedFileConfig(rs.getBoolean(13));
					msg.setReceivedConfig(rs.getBoolean(14));
					msg.setLastetTimeReceivedConfig(rs.getTimestamp(15));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	public List<DeviceConfig> getDeviceConfigById(int deviceid) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdGetDeviceConfig(?)";
		List<DeviceConfig> lstResult = new ArrayList<DeviceConfig>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, deviceid);
				rs = ps.executeQuery();
				while (rs.next()) {
					DeviceConfig msg = new DeviceConfig();
					msg.setConfigid(rs.getInt(1));
					msg.setConfigname(rs.getString(2));
					msg.setConfigcode(rs.getString(3));
					msg.setConfigvalue(rs.getString(4));
					msg.setParentid(rs.getInt(5));
					msg.setConfigtype(rs.getString(6));
					msg.setCanedit(rs.getInt(7));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	public List<DeviceConfig> insertLogDeviceConfig(int configid, int deviceid, String value) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdChangeLogConfig(?,?,?,?,?)";
		List<DeviceConfig> lstResult = new ArrayList<DeviceConfig>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, configid);
				ps.setInt(2, deviceid);
				ps.setString(3, value);
				ps.setTimestamp(4, new Timestamp(new Date().getTime()));
				ps.setInt(5, 1);
				rs = ps.executeQuery();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	public List<DeviceConfig> updateDeviceConfigIntoOnline(int deviceid) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdUpdateBaseConfigInOnline(?)";
		List<DeviceConfig> lstResult = new ArrayList<DeviceConfig>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, deviceid);
				rs = ps.executeQuery();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	public List<DriverInVehicle> getDriversInVehicle(int deviceid) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdGetAllDriverInVehicle(?)";
		List<DriverInVehicle> lstResult = new ArrayList<DriverInVehicle>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, deviceid);
				rs = ps.executeQuery();
				while (rs.next()) {
					DriverInVehicle msg = new DriverInVehicle();
					msg.setDrivername(rs.getString(1));
					msg.setDriverlicense(rs.getString(2));
					msg.setStaffcard(rs.getInt(3));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	public int updateDriverFromExcelData(List<DriverExcelTemp> input) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.lst_Driver_Import(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				conn.setAutoCommit(false);
				CallableStatement stmt = conn.prepareCall(sql);
				java.sql.Date sqlDate = null;
				for (DriverExcelTemp driverExcelTemp : input) {
					stmt.setInt(1, driverExcelTemp.getAgentid());
					stmt.setInt(2, driverExcelTemp.getGroupid());
					stmt.setString(3, driverExcelTemp.getDrivercode());
					stmt.setString(4, driverExcelTemp.getDrivername());
					stmt.setString(5, Integer.toString(driverExcelTemp.getStaffcard()));
					stmt.setString(6, driverExcelTemp.getDriverlicense());
					stmt.setInt(7, driverExcelTemp.getLicensetypeid());
					try {
						sqlDate = new java.sql.Date(driverExcelTemp.getRegisterdate().getTime());
					} catch (Exception e) {
						// TODO: handle exception
					}
					stmt.setDate(8, sqlDate);
					try {
						sqlDate = new java.sql.Date(driverExcelTemp.getExpiatedate().getTime());
					} catch (Exception e) {
						// TODO: handle exception
					}
					stmt.setDate(9, sqlDate);
					stmt.setString(10, driverExcelTemp.getPhonenumber());
					stmt.setString(11, driverExcelTemp.getPhoneoffice());
					stmt.setString(12, driverExcelTemp.getIdentitycard());
					try {
						sqlDate = new java.sql.Date(driverExcelTemp.getBirthday().getTime());
					} catch (Exception e) {
						// TODO: handle exception
					}
					stmt.setDate(13, sqlDate);
					stmt.setInt(14, driverExcelTemp.getSexid());
					stmt.setInt(15, driverExcelTemp.getBloodtypeid());
					stmt.addBatch();
				}
				int[] updatesCount = stmt.executeBatch();

				// manually commit
				conn.commit();
				if (updatesCount.length != input.size()) {
					return -1;
				}
				return 0;
			}
		} catch (Exception e) {
			return -1;
		} finally {
			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return -1;
	}

	public List<RptDriverInShift> getWarningWorkingHour(String agent, String group, String parking, Timestamp shifttime,
			int ranger, String username) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdGetDriverWorkingHours(?,?,?,?,?,?)";
		List<RptDriverInShift> lstResult = new ArrayList<RptDriverInShift>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, agent);
				ps.setString(2, group);
				ps.setString(3, parking);
				ps.setTimestamp(4, shifttime);
				ps.setInt(5, ranger);
				ps.setString(6, username);

				rs = ps.executeQuery();
				while (rs.next()) {
					RptDriverInShift msg = new RptDriverInShift();
					msg.setIssueDate(rs.getTimestamp(3));
					msg.setDriverName(rs.getString(4));
					msg.setPhoneNumber(rs.getString(5));
					msg.setLicensePlate(rs.getString(6));
					msg.setVehicleNumber(rs.getString(7));
					msg.setStaffCard(rs.getString(8) == null ? "" : "" + rs.getString(8));
					msg.setTimeDrivingPerDay(rs.getString(9) == null ? "" : "" + rs.getString(9));
					msg.setTimeDrivingContinuous(rs.getString(10) == null ? "" : "" + rs.getString(10));
					msg.setParkingName(rs.getString(11));
					msg.setGroupName(rs.getString(12));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Qc31 Bc thoi gian lai xe lien tuc
	public List<RptQcTrunkStdDriving> getQcTrunkStdDriving(String userid, Timestamp fromdate, Timestamp todate,
			String vehgroupid, String trucknumber, int timetype) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.rptTruckStdDriving(?,?,?,?,?,?)";
		List<RptQcTrunkStdDriving> lstResult = new ArrayList<RptQcTrunkStdDriving>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, userid);
				ps.setTimestamp(2, fromdate);
				ps.setTimestamp(3, todate);
				ps.setString(4, vehgroupid);
				ps.setString(5, trucknumber);
				ps.setInt(6, timetype);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptQcTrunkStdDriving msg = new RptQcTrunkStdDriving();
					msg.setVehicleId(rs.getInt(2));
					msg.setLicensePlate(rs.getString(9));
					msg.setDriverName(rs.getString(8));
					msg.setDriverLicense(rs.getString(11));
					msg.setTypeName(rs.getString(12));
					msg.setBeginTime(rs.getTimestamp(13));
					msg.setBeginLocation(rs.getString(21));
					msg.setBeginAddr(rs.getString(23));
					msg.setEndTime(rs.getTimestamp(14));
					msg.setEndLocation(rs.getString(22));
					msg.setEndAddr(rs.getString(24));
					msg.setTimeOverStr(rs.getString(16));
					msg.setKmGPS(rs.getFloat(25));
					msg.setStateTrip(rs.getString(26));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}
	
	// DetailViolate QC31 with 4h
		public List<RptQcTrunkStdDriving> getDetailTrunkStdDrivingOverTime(String userid, Timestamp fromdate, Timestamp todate,
				String vehgroupid, String driverId) {
			Session session = ControllerUtils.getCurrentSession();
			SessionImplementor sessionImplementor = (SessionImplementor) session;
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sql = "CALL txm_tracking.rptDetailTruckDriving(?,?,?,?,?)";
			List<RptQcTrunkStdDriving> lstResult = new ArrayList<RptQcTrunkStdDriving>();
			try {
				conn = sessionImplementor.connection();
				if (conn != null && !conn.isClosed()) {
					ps = conn.prepareStatement(sql);
					ps.setString(1, userid);
					ps.setTimestamp(2, fromdate);
					ps.setTimestamp(3, todate);
					ps.setString(4, vehgroupid);
					ps.setString(5, driverId);
					rs = ps.executeQuery();
					while (rs.next()) {
						RptQcTrunkStdDriving msg = new RptQcTrunkStdDriving();
						msg.setVehicleId(rs.getInt(2));
						msg.setDriverId(rs.getInt(4));
						msg.setLicensePlate(rs.getString(9));
						msg.setDriverName(rs.getString(8));
						msg.setDriverLicense(rs.getString(11));
						msg.setTypeName(rs.getString(12));
						msg.setBeginTime(rs.getTimestamp(13));
						msg.setBeginLocation(rs.getString(21));
						msg.setBeginAddr(rs.getString(23));
						msg.setEndTime(rs.getTimestamp(14));
						msg.setEndLocation(rs.getString(22));
						msg.setEndAddr(rs.getString(24));
						msg.setTimeOverStr(rs.getString(16));
						msg.setKmGPS(rs.getFloat(25));
						msg.setStateTrip(rs.getString(26));
						lstResult.add(msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				/* Dong result set */
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				/* Dong prepare statement */
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				/* Dong connection */
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				session.close();
			}
			return lstResult;
		}

	// b/c qua toc do gioi han
	public List<RptQcTruckStdOverSpeed> getQcTruckStdSpeed(String userid, Timestamp fromdate, Timestamp todate,
			String vehgroupid, String trucknumber) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.rptTruckStdSpeed(?,?,?,?,?)";
		List<RptQcTruckStdOverSpeed> lstResult = new ArrayList<RptQcTruckStdOverSpeed>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, userid);
				ps.setTimestamp(2, fromdate);
				ps.setTimestamp(3, todate);
				ps.setString(4, vehgroupid);
				ps.setString(5, trucknumber);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptQcTruckStdOverSpeed msg = new RptQcTruckStdOverSpeed();

					msg.setVehicleId(rs.getInt(12));
					msg.setLicensePlate(rs.getString(2));
					msg.setTypeName(rs.getString(3));
					msg.setSpeed(rs.getInt(4));
					msg.setSpeedLimit(rs.getInt(5));
					msg.setTimeStart(rs.getTimestamp(6));
					msg.setTimeStop(rs.getTimestamp(7));
					msg.setTime(rs.getString(8));
					msg.setAddrBegin(rs.getString(9));
					msg.setAddrFinish(rs.getString(10));
					msg.setKm(rs.getFloat(11));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}
	
	//Danh sach chi tiet cho bao cao vi pham toc do (Dung cho b/c tong hop vi pham qcvn31
	public List<RptQcTruckStdOverSpeed> getDetailQcTruckStdSpeed(String userid, Timestamp fromdate, Timestamp todate,
			String vehgroupid,String driverId) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.rptDetailTruckStdSpeed(?,?,?,?,?)";
		List<RptQcTruckStdOverSpeed> lstResult = new ArrayList<RptQcTruckStdOverSpeed>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, userid);
				ps.setTimestamp(2, fromdate);
				ps.setTimestamp(3, todate);
				ps.setString(4, vehgroupid);
				ps.setString(5, driverId);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptQcTruckStdOverSpeed msg = new RptQcTruckStdOverSpeed();

					msg.setVehicleId(rs.getInt(12));
					msg.setLicensePlate(rs.getString(2));
					msg.setTypeName(rs.getString(3));
					msg.setSpeed(rs.getInt(4));
					msg.setSpeedLimit(rs.getInt(5));
					msg.setTimeStart(rs.getTimestamp(6));
					msg.setTimeStop(rs.getTimestamp(7));
					msg.setTime(rs.getString(8));
					msg.setAddrBegin(rs.getString(9));
					msg.setAddrFinish(rs.getString(10));
					msg.setKm(rs.getFloat(11));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// b/c dung do
	public List<RptQcTruckStdStop> getQcTruckStdStop(String userid, Timestamp fromdate, Timestamp todate,
			String vehgroupid, String trucknumber) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.rptTruckStdStop(?,?,?,?,?)";
		List<RptQcTruckStdStop> lstResult = new ArrayList<RptQcTruckStdStop>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setString(1, userid);
				ps.setTimestamp(2, fromdate);
				ps.setTimestamp(3, todate);
				ps.setString(4, vehgroupid);
				ps.setString(5, trucknumber);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptQcTruckStdStop msg = new RptQcTruckStdStop();
					msg.setLicensePlate(rs.getString(6));
					msg.setDriverName(rs.getString(5) == null ? "" : "" + rs.getString(5));
					msg.setDriverLicense(rs.getString(7) == null ? "" : "" + rs.getString(7));
					msg.setTypeName(rs.getString(8));
					msg.setTimeStart(rs.getTimestamp(9));
					msg.setTimeEnd(rs.getTimestamp(10));
					msg.setTimeOverStr(rs.getString(15));
					msg.setLocation(rs.getString(13));
					msg.setAddr(rs.getString(16) == null ? "" : "" + rs.getString(16));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Bao cao tong hop hoat dong theo xe
	public List<RptQcTruckStdActivityByVehicle> getQcTruckStdActivityByVehicle(Timestamp fromdate, Timestamp todate,
			String vehgroupid, String trucknumber, String userid) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.rptTruckStdActivityByVehicle(?,?,?,?,?)";
		List<RptQcTruckStdActivityByVehicle> lstResult = new ArrayList<RptQcTruckStdActivityByVehicle>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, fromdate);
				ps.setTimestamp(2, todate);
				ps.setString(3, vehgroupid);
				ps.setString(4, trucknumber);
				ps.setString(5, userid);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptQcTruckStdActivityByVehicle msg = new RptQcTruckStdActivityByVehicle();
					msg.setLicensePlate(rs.getString(3));
					msg.setTypeName(rs.getString(5));
					msg.setKmGps(rs.getInt(6));
					msg.setFivePerPath(rs.getFloat(7));
					msg.setFiveTime(rs.getInt(8));
					msg.setTenPerPath(rs.getFloat(9));
					msg.setTenTime(rs.getInt(10));
					msg.setTwentyPerPath(rs.getFloat(11));
					msg.setTwentyTime(rs.getInt(12));
					msg.setThirthPerPath(rs.getFloat(13));
					msg.setThirthTime(rs.getInt(14));
					msg.setStopCounting(rs.getInt(15));
					msg.setNote(rs.getString(16));

					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Bao cao tong hop hoat dong theo tai xe
	public List<RptQcTruckStdActivityByVehicle> getQcTruckStdActivityByDriver(Timestamp fromdate, Timestamp todate,
			String vehgroupid, String driverid, String userid) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.rptTruckStdActivity(?,?,?,?,?)";
		List<RptQcTruckStdActivityByVehicle> lstResult = new ArrayList<RptQcTruckStdActivityByVehicle>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, fromdate);
				ps.setTimestamp(2, todate);
				ps.setString(3, vehgroupid);
				ps.setString(4, driverid);
				ps.setString(5, userid);
				rs = ps.executeQuery();
				while (rs.next()) {
					RptQcTruckStdActivityByVehicle msg = new RptQcTruckStdActivityByVehicle();
					msg.setNameDriver(rs.getString(3));
					msg.setDriverLicense(rs.getString(4));
					msg.setKmGps(rs.getInt(5));
					msg.setFivePerPath(rs.getFloat(6));
					msg.setFiveTime(rs.getInt(7));
					msg.setTenPerPath(rs.getFloat(8));
					msg.setTenTime(rs.getInt(9));
					msg.setTwentyPerPath(rs.getFloat(10));
					msg.setTwentyTime(rs.getInt(11));
					msg.setThirthPerPath(rs.getFloat(12));
					msg.setThirthTime(rs.getInt(13));
					msg.setOverFourHour(rs.getInt(14));
					msg.setNote(rs.getString(15));

					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}

	// Bao cao tong hop vi pham
	public List<ReportQcActivity> getQcTruckActivity(Timestamp fromdate, Timestamp todate, String vehgroupid,
			String driverid) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "CALL txm_tracking.cmdGetTotalDriverActivity(?,?,?,?)";
		List<ReportQcActivity> lstResult = new ArrayList<ReportQcActivity>();
		try {
			conn = sessionImplementor.connection();
			if (conn != null && !conn.isClosed()) {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, fromdate);
				ps.setTimestamp(2, todate);
				ps.setString(3, vehgroupid);
				ps.setString(4, driverid);
				rs = ps.executeQuery();
				while (rs.next()) {
					ReportQcActivity msg = new ReportQcActivity();
					msg.setDriverId(rs.getInt(1));
					msg.setDriverName(rs.getString(2));
					msg.setDriverCard(rs.getString(3));
					msg.setGroupName(rs.getString(4));
					msg.setOver4hour(rs.getInt(5));
					msg.setOver10hour(rs.getInt(6));
					msg.setOverspeed(rs.getInt(7));
					lstResult.add(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}
		
		// Bao cao tong hop vi pham QC31
				public List<RptTotalViolateQc31> getReportTotalViolateQc31(Timestamp fromdate, Timestamp todate, String strAgentId, String strGroupId) {
					Session session = ControllerUtils.getCurrentSession();
					SessionImplementor sessionImplementor = (SessionImplementor) session;
					Connection conn = null;
					PreparedStatement ps = null;
					ResultSet rs = null;
					String sql = "CALL txm_tracking.rptTotalViolateQCVN31(?,?,?,?)";
					List<RptTotalViolateQc31> lstResult = new ArrayList<RptTotalViolateQc31>();
					try {
						conn = sessionImplementor.connection();
						if (conn != null && !conn.isClosed()) {
							ps = conn.prepareStatement(sql);
							ps.setTimestamp(1, fromdate);
							ps.setTimestamp(2, todate);
							ps.setString(3, strAgentId);
							ps.setString(4, strGroupId);
							rs = ps.executeQuery();
							while (rs.next()) {
								RptTotalViolateQc31 msg = new RptTotalViolateQc31();
								msg.setDriverName(rs.getString("DriverName"));
								msg.setDriverId(rs.getInt("DriverID"));
								msg.setStaffCard(rs.getString("StaffCard"));
								msg.setDriverLicense(rs.getString("DriverLicense"));
								msg.setGroupName(rs.getString("GroupName"));
								msg.setOverFourHour(rs.getInt(6));
								msg.setOverTenHour(rs.getInt(7));
								msg.setOverSpeed(rs.getInt(8));
								lstResult.add(msg);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}	
				
	// Dữ liệu DetailOver10Hour
				public List<ReportDetailOverTenHour> getDetailOverTenHour(Timestamp fromdate, Timestamp todate, String strAgentId, String strGroupId, String driverId) {
					Session session = ControllerUtils.getCurrentSession();
					SessionImplementor sessionImplementor = (SessionImplementor) session;
					Connection conn = null;
					PreparedStatement ps = null;
					ResultSet rs = null;
					String sql = "CALL txm_tracking.rptDetailTruckDrivingOverTenHour(?,?,?,?,?)";
					List<ReportDetailOverTenHour> lstResult = new ArrayList<ReportDetailOverTenHour>();
					try {
						conn = sessionImplementor.connection();
						if (conn != null && !conn.isClosed()) {
							ps = conn.prepareStatement(sql);
							ps.setTimestamp(1, fromdate);
							ps.setTimestamp(2, todate);
							ps.setString(3, strAgentId);
							ps.setString(4, strGroupId);
							ps.setString(5, driverId);
							rs = ps.executeQuery();
							while (rs.next()) {
								ReportDetailOverTenHour msg = new ReportDetailOverTenHour();
								msg.setDriverName(rs.getString("DriverName"));
								msg.setStaffCard(rs.getString("StaffCard"));
								msg.setDriverLicense(rs.getString("DriverLicense"));
								msg.setTimeLog(rs.getTimestamp("Timelog"));
								msg.setGroupName(rs.getString("GroupName"));
								msg.setTimeOver(rs.getString("Over10Hour"));
								lstResult.add(msg);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}
				
	// Lay ReportTotalBussinessAgent
				public List<RptTotalBussinessAgent> getReportTotalBussinessAgent(Timestamp fromdate, Timestamp todate, String strAgentId, String strGroupId, String user) {
					Session session = ControllerUtils.getCurrentSession();
					SessionImplementor sessionImplementor = (SessionImplementor) session;
					Connection conn = null;
					PreparedStatement ps = null;
					ResultSet rs = null;
					String sql = "CALL txm_tracking.rptBussinessAgent(?,?,?,?,?)";
					List<RptTotalBussinessAgent> lstResult = new ArrayList<RptTotalBussinessAgent>();
					try {
						conn = sessionImplementor.connection();
						if (conn != null && !conn.isClosed()) {
							ps = conn.prepareStatement(sql);
							ps.setTimestamp(1, fromdate);
							ps.setTimestamp(2, todate);
							ps.setString(3, strAgentId);
							ps.setString(4, strGroupId);
							ps.setString(5, user);
							rs = ps.executeQuery();
							while (rs.next()) {
								RptTotalBussinessAgent msg = new RptTotalBussinessAgent();
								msg.setRptId(rs.getInt("rptID"));
								msg.setAgentName(rs.getString("AgentName"));
								msg.setAgentGroup(rs.getString("VehicleGroupName"));
								msg.setPhone(rs.getString("Phone"));
								msg.setFax(rs.getString("Fax"));
								msg.setTotalVehicle(rs.getInt("TotalVehicle"));
								msg.setTotalActiveVehicle(rs.getInt("TotalActiveVehicle"));
								msg.setTotalFinishVehicle(rs.getInt("TotalFinishVehicle"));
								msg.setTotalDuplicateVehicle(rs.getInt("TotalDuplicateVehicle"));
								msg.setTotalOnlineVehicle(rs.getInt("TotalOnlineVehicle"));
								msg.setMoney(rs.getInt("Money"));
								msg.setAvgMoney(rs.getInt("AvgMoney"));
								msg.setPercent(rs.getDouble("Percent"));
								msg.setInWorkShop(rs.getInt("InWorkshop"));
								msg.setInAccident(rs.getInt("InAccident"));
								msg.setTimeLog(rs.getTimestamp("Timelog"));
								lstResult.add(msg);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
			/* Dong result set */
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong prepare statement */
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/* Dong connection */
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.close();
		}
		return lstResult;
	}
}
