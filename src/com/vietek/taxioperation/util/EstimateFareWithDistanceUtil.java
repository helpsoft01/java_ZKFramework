package com.vietek.taxioperation.util;

import com.vietek.taxioperation.common.EnumCarType;

public class EstimateFareWithDistanceUtil {

	public EstimateFareWithDistanceUtil() {

	}

	public static double getEstimateFareWithDistance(double distance, EnumCarType enumCarType) {

		double retVal = 0;
		int _4sitFare = 0;
		int _7sitFare = 0;
		double distanceTmp = distance;

		distance = distance - 719;
		_4sitFare += 11000;
		if (distance > 30000) {
			_4sitFare += 30 * 15300;
			int km = (int) ((distance - 30000) / 1000.0 + 0.5);
			_4sitFare += 12000 * km;
		} else {
			int km = (int) ((distance / 1000.0) + 0.5);
			_4sitFare += 15300 * km;
		}

		distance = distanceTmp;
		distance = distance - 698;
		_7sitFare += 12000;
		if (distance > 30000) {
			_7sitFare += 30 * 17200;
			int km = (int) ((distance - 30000) / 1000.0 + 0.5);
			_7sitFare += 17200 * km;
		} else {
			int km = (int) ((distance / 1000.0) + 0.5);
			_7sitFare += 17200 * km;
		}

		if (enumCarType.getValue() == EnumCarType._4SIT.getValue())
			retVal = _4sitFare;
		else if (enumCarType.getValue() == EnumCarType._7SIT.getValue())
			retVal = _7sitFare;
		else
			retVal = (_4sitFare + _7sitFare) / 2;
		return retVal;
	}
}
