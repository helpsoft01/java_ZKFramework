package com.vietek.taxioperation.common;

public enum EnumStatus {

	/**
	 * value = -1. Dang xu ly
	 */
	CREATING(-1, "Đang xử lý"),
	/**
	 * value = 0. Huy cuoc
	 */
	HUY(0, "Hủy"),
	/**
	 * value = 1. Cuoc moi
	 */
	MOI(1, "Mới"),
	/**
	 * value = 2. Xe dang ky don
	 */
	XE_DANG_KY_DON(2, "Đăng ký đón"),
	/**
	 * value = 3. Xe da don khach
	 */
	XE_DA_DON(3, "Đã đón khách"),
	/**
	 * value = 4. Xe da tra khach
	 */
	TRA_KHACH(4, "Đã trả khách"),
	/**
	 * value = 5. Da doc dam
	 */
	DA_DOC_DAM(5, "Đã đọc đàm");

	private final int value;
	private final String label;

	EnumStatus(final int newValue, String label) {
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
