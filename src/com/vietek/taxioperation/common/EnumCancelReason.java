package com.vietek.taxioperation.common;

public enum EnumCancelReason {

	CREATING(0, "Đang tạo"), KHACH_HANG_HUY(1, "Khách hàng hủy"), KHONG_CO_TAI_XE(2,
			"Không có tài xế"), KHONG_DON_DUOC_KHACH(3, "Không đón được khách"), DON_KHACH_KHAC(4,
					"Đón khách khác"), LY_DO_KHAC(5, "Lý do khác");

	private final int value;
	private final String reason;

	EnumCancelReason(final int newValue, final String reason) {
		value = newValue;
		this.reason = reason;
	}

	public int getValue() {
		return value;
	}

	public String getReason() {
		return reason;
	}
}
