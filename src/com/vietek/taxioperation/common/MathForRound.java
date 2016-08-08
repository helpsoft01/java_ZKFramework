package com.vietek.taxioperation.common;

public class MathForRound {
	public static float round(float number, int digit){
		if(digit > 0) {
			int temp = 1;
			for(int i=0; i < digit; i++)
				temp = temp*10;
				number = number*temp;
				number = Math.round(number);
				number = number/temp;
				return number;
		} else {
			return 0.0f;		}
	}
}
