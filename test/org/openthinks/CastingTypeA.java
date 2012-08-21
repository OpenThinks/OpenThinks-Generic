package org.openthinks;

public class CastingTypeA implements CastingType {
	private String key = "a";
	private Object value = new Object();

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
