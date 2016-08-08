package com.vietek.taxioperation.common;

/**
 * Enum loai xe 4 cho, 7 cho hay ca hai. BON_CHO dai dien cho loai xe 4 cho, co
 * gia tri bang 1. BAY_CHO dai dien cho xe 7 cho, co gia tri bang 2. ALL dai
 * dien cho ca hai loai xe, co gia tri bang 0
 * 
 * @author VuD
 */
public enum EnumCarTypeCommon {
	/** Dai dien cho xe 4 cho va 7 cho. Co gia tri bang 0 */
	ALL(0, "Bất kỳ"),
	/** Dai dien cho xe 4 cho. Co gia tri bang 1 */
	BON_CHO(1, "4 C"),
	/** Dai dien cho xe 7 cho. Co gia tri bang 2 */
	BAY_CHO(2, "7 C");
	private final int value;
	private final String label;

	private EnumCarTypeCommon(int value, String name) {
		this.value = value;
		this.label = name;
	}

	public int getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}
	
	public static EnumCarTypeCommon getValueOfCode(int code) {
		EnumCarTypeCommon retVal = null;
		for (EnumCarTypeCommon val : EnumCarTypeCommon.values()) {
			if (val.getValue() == code) {
				retVal = val;
				break;
			}
		}
		return retVal;
	}
}
