package com.vietek.taxioperation.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author VuD
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AnnonationLinkedTable {
	/**
	 * Khai bao model hien thi
	 *
	 * @author VuD
	 * @return
	 */
	public String modelClazz() default "";

	/**
	 * Khai bao dieu kien loc
	 *
	 * @author VuD
	 * @return
	 */
	public String whereClause() default "";
	
	public String displayFieldName() default "name";

}
