package com.vietek.taxioperation.util;

public class StandandPhoneNumber {
	// TODO: VuD| Em thay class nay chi can dung mot ham static o duoi nen em
	// xoa cac thuoc tinh khong dung cua anh di
	public static synchronized String standandPhone(String phoneNumber) {
		String phone = phoneNumber.trim();
		if (phone.startsWith("+84"))
			phone = phone.replace("+84", "0");
		else if (!phone.startsWith("0"))
			phone = "0" + phone;
		return phone;
	}

}
