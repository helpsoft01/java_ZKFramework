package com.vietek.trackingOnline.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Agent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8572596733476829141L;
	int AgentID;
	String AgentName;

	public int getAgentID() {
		return AgentID;
	}

	public void setAgentID(int agentID) {
		AgentID = agentID;
	}

	public String getAgentName() {
		return AgentName;
	}

	public void setAgentName(String agentName) {
		AgentName = agentName;
	}

	public Agent(ResultSet rs) {
		try {
			setAgentID(rs.getInt("AgentID"));
			setAgentName(rs.getString("AgentName"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
