package com.vietek.trackingOnline.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Group implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8023209096865336505L;

	int AgentID;
	int GroupID;
	String GroupName;

	public int getAgentID() {
		return AgentID;
	}

	public void setAgentID(int agentID) {
		AgentID = agentID;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public int getGroupID() {
		return GroupID;
	}

	public void setGroupID(int groupID) {
		GroupID = groupID;
	}

	public Group(ResultSet rs) {
		try {
			setAgentID(rs.getInt("AgentID"));
			setGroupID(rs.getInt("GroupID"));
			setGroupName(rs.getString("GroupName"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
