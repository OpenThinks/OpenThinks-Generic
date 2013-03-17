package org.openthinks.generic.util;

/**
 * Definition of frequently-used naming conventions
 * 
 * @author Zhang Junlong
 * 
 */
public enum NamingConvention {
	NO(null), CAMEL(""), UNDERSCORE("_"), HYPHENATE("-"), SPACE(" "), DOT(".");

	private String value;

	NamingConvention(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
