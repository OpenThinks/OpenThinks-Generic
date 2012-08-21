package org.openthinks.generic.math;

public class NumberTrim {
	public static double roundOff (double number,int precision) {
		int base=1;
		
		for(int i=0;i<precision;i++)
			base=base*10;
		
		long temp = Math.round(number * base); // cents
		return temp / (base*1.0);
		
	}
}
