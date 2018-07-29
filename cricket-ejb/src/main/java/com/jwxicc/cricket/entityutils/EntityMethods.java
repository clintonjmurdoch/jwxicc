package com.jwxicc.cricket.entityutils;

import java.math.BigDecimal;

import org.apache.commons.lang.math.Fraction;

public class EntityMethods {

	public static BigDecimal calulateOverDecimal(BigDecimal overs) {
		// number of completed overs
		int fullOvers = (int) Math.floor(overs.doubleValue());
		// number of balls in uncompleted over
		BigDecimal fractionOvers = overs.subtract(BigDecimal.valueOf(fullOvers));
		System.out.println("fraction overs: " + fractionOvers);
		System.out.println("fraction overs scale: " + fractionOvers.scale());
		if (fractionOvers.equals(BigDecimal.ZERO)) {
			// just return the number of overs
			System.out.println("return num of overs");
			return overs;
		} else {
			// convert the fractional part (which is now an int) back to
			// fraction where number of balls per 6 is represented as a decimal
			BigDecimal toReturn = fractionOvers.multiply(
					BigDecimal.valueOf(Fraction.getFraction(5, 3).doubleValue())).add(
					BigDecimal.valueOf(fullOvers));
			System.out.println(toReturn.doubleValue());
			System.out.println("return modified value for overs");
			return toReturn;
		}
	}

	public static BigDecimal calculateEconomy(int runs, BigDecimal overs) {

		if (overs != null && BigDecimal.ZERO.compareTo(overs) < 0) {
			return BigDecimal.valueOf(runs).divide(calulateOverDecimal(overs), 3,
					BigDecimal.ROUND_HALF_UP);
		} else {
			return null;
		}
	}
}
