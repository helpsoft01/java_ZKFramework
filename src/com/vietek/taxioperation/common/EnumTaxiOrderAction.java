package com.vietek.taxioperation.common;

public enum EnumTaxiOrderAction {

	NEW_TAXI_ORDER(-1, "THÊM MỚI"), CALL_IN(0, "ĐIỆN THOẠI GỌI TỚI"), VIEW_EDIT(1,
			"XEM TỪ DANH SÁCH"), TEXTBOX_SEARCH_PHONE(2, "NHẬP SỐ ĐIỆN THOẠI BẰNG TAY"), POPUP_WINDOW(3,
					"CHỌN HIỂN THỊ QUA POPUP WINDIW CALL IN"), DUPLICATE_TAXI_ORDER(4,
							"TẠO CHUYẾN MỚI CÙNG SỐ ĐIỆN THOẠI");
	private final int value;
	private final String label;

	private EnumTaxiOrderAction(final int newValue, String label) {
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
