package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author sonvh
 *
 */
@Entity
@Table(name = "ad_userGroupVehicle")
public class MapSysGroupVehicle extends AbstractModel implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3980194160041549479L;

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private SysGroup sysGroup;

	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SysGroup getSysGroup() {
		return sysGroup;
	}

	public void setSysGroup(SysGroup sysGroup) {
		this.sysGroup = sysGroup;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

}
