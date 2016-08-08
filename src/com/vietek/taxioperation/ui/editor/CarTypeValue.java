package com.vietek.taxioperation.ui.editor;

import com.vietek.taxioperation.common.EnumCarTypeCommon;

/**
 * @author tuanpa
 */
public class CarTypeValue {
	private EnumCarTypeCommon carType;
	private int number;
	public CarTypeValue(EnumCarTypeCommon carType, int number) {
		super();
		this.carType = carType;
		this.number = number;
	}
	public EnumCarTypeCommon getCarType() {
		return carType;
	}
	public void setCarType(EnumCarTypeCommon carType) {
		this.carType = carType;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	
}