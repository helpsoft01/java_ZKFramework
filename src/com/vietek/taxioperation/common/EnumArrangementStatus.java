package com.vietek.taxioperation.common;

public enum EnumArrangementStatus {
	FALSE(0, "False"),
	/**
	 * value:1. name:Thanh cong
	 */
	THANH_CONG(1, "Thành công"),
	/**
	 * value:2. name:Queue day
	 */
	QUE_DAY(2, "Queue đầy"),
	/**
	 * value:3. name:Ngoai vung xep tai
	 */
	NGOAI_VUNG(3, "Ngoài vùng"),
	/**
	 * value:4. name:Xe dang co khach
	 */
	TRONG_CUOC(4, "Trong cuốc");
	private final int VALUE;
	private final String NAME;

	EnumArrangementStatus(int value, String name) {
		this.VALUE = value;
		this.NAME = name;
	}

	public int getVALUE() {
		return VALUE;
	}

	public String getNAME() {
		return NAME;
	}

}
