package com.vietek.taxioperation.util;

public class GoogleKey {
	private static String[] keys = {"AIzaSyAocRnSkbbpbc2yed6l-JnO28vmLo5Z0n0",
									"AIzaSyBO_GJDyViq9-TO3Wbwitgh4JfboqCmOTs",
									"AIzaSyDcZpeI-H0LEkJSQGb2yTAnHw9cLt-_eqA",
									"AIzaSyDpgva8OkTfIRc2ti8qY4fiKrN5vcE2PF0",
									"AIzaSyAwdtISopf1irIJi-ZE5o8SzpKkNTHyl2M",
									"AIzaSyATEJK-a1j_E0Bi6aGsjbmef14HUJP2RUo",
									"AIzaSyCui8lxVKVku1AmcriBPiSJ9O8BSI_d_HM",
									"AIzaSyAl8uYmjwIooSXJ8dbWMhqH4zHFmkH-3Co",
									"AIzaSyB4V4FdbVGjrHy_enECQjv9RGD9kV24STQ"};
	private static int count = 0;
	public static String getKey() {
		String key = keys[count];
		count++;
		if (count >= keys.length) {
			count = 0;
		}
		return key;
	}
}
