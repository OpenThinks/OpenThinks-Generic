package org.openthinks.generic.util;

/**
 * Helper utility for naming.
 * 
 * @author Zhang Junlong
 * 
 */
public class NamingHelper {

	public static String changeNamingConvention(String name,
			NamingConvention currentConvention, NamingConvention newConvention) {
		return name.replaceAll("\\" + currentConvention.getValue(),
				newConvention.getValue());
	}

}
