package com.vietek.taxioperation.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Batt
 * 
 *         params
 * 
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validation {
	String title() default "";

	boolean nullable() default true;

	boolean isEmail() default false;

	int minLegth() default 0;

	int maxLength() default 255;

	String regex() default "";

	boolean isHasSpecialChar() default true;

	boolean alowrepeat() default true;

}
