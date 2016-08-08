package com.vietek.taxioperation.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 
 * @author VuD
 * 
 */

public class ObjectUtils {
	/**
	 * 
	 * Copy gia tri cac tuong tu objectFrom sang objectTo
	 * 
	 * @author VuD
	 * @param objectFrom
	 * @param objectTo
	 * @return true neu copy duoc. false neu khong copy duoc
	 */
	public static <X extends Object, Y extends Object> boolean copyObject(X objectFrom, Y objectTo) {
		try {
			if (objectFrom == null || objectTo == null) {
				return false;
			} else {
				Class<? extends Object> fromClass = objectFrom.getClass();
				Field[] fFields = fromClass.getDeclaredFields();

				Class<? extends Object> toClass = objectTo.getClass();
				try {
					for (int i = 0; i < fFields.length; i++) {
						Field fField = fFields[i];
						if (!Modifier.isStatic(fField.getModifiers()) && !Modifier.isFinal(fField.getModifiers())) {
							Field tField = toClass.getDeclaredField(fField.getName());
							if (tField != null) {

								if (fField.getType().equals(tField.getType())) {
									fField.setAccessible(true);
									tField.setAccessible(true);
									tField.set(objectTo, fField.get(objectFrom));
								}
							}
						}

					}
				} catch (Exception e) {
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public synchronized static <X extends Object, Y extends Object> boolean copyObjectSyn(X objectFrom, Y objectTo) {
		if (objectFrom == null || objectTo == null) {
			return false;
		} else {
			Class<? extends Object> fromClass = objectFrom.getClass();
			Field[] fFields = fromClass.getDeclaredFields();

			Class<? extends Object> toClass = objectTo.getClass();
			try {
				for (int i = 0; i < fFields.length; i++) {
					Field fField = fFields[i];
					if (!Modifier.isStatic(fField.getModifiers()) && !Modifier.isFinal(fField.getModifiers())) {
						Field tField = toClass.getDeclaredField(fField.getName());
						if (tField != null) {
							if (fField.getType().equals(tField.getType())) {
								fField.setAccessible(true);
								tField.setAccessible(true);
								tField.set(objectTo, fField.get(objectFrom));
							}
						}
					}
				}
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}

}
