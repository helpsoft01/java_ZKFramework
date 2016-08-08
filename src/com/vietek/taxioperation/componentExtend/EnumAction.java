package com.vietek.taxioperation.componentExtend;

public enum EnumAction {

	NEW_ACTION(1, "THÊM MỚI"), EDIT_ACTION(2, "SỬA"), DELETE_ACTION(3, "XÓA");
	private int value;
	private String label;

	private EnumAction(final int newValue, String label) {
		this.value = newValue;
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

}
