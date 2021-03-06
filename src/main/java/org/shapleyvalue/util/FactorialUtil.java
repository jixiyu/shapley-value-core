package org.shapleyvalue.util;

public class FactorialUtil {

	public static long factorial(long n) {
		if(n>20) throw new ArithmeticException("Capacity exceded for factorial " + n);
		return n > 1 ? n * factorial(n - 1) : 1;
	}



}
