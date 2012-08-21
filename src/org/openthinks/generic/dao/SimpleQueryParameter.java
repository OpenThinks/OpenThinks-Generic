package org.openthinks.generic.dao;

import org.openthinks.generic.util.NamingConvention;
import org.openthinks.generic.util.NamingHelper;

/**
 * Simple Parameter Wrapper for querying
 * 
 * @author Zhang Junlong
 * 
 */
public class SimpleQueryParameter implements QueryParameter {
	private String relationalOpertor = "AND";
	private String field = "'1'";
	private String conditionalOperator = "=";
	private Object value = "'1'";
	private NamingConvention namingConvention = NamingConvention.UNDERSCORE;

	public String getRelationalOpertor() {
		return relationalOpertor;
	}

	public void setRelationalOpertor(String opertor) {
		this.relationalOpertor = opertor;
	}

	public String getField() {
		return field;
	}

	public void setField(String property) {
		this.field = property;
	}

	public Object getValue() {
		if (value instanceof String) {
			StringBuilder tempValue = new StringBuilder(value.toString());
			tempValue.insert(0, "'");
			tempValue.append("'");
			return tempValue.toString();
		} else
			return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getConditionalOperator() {
		return conditionalOperator;
	}

	public void setConditionalOperator(String conditionalOperator) {
		this.conditionalOperator = conditionalOperator;
	}

	public NamingConvention getNamingConvention() {
		return namingConvention;
	}

	public void setNamingConvention(NamingConvention namingConvention) {
		this.namingConvention = namingConvention;
	}

	/**
	 * Assemble all properties to a query string
	 * 
	 * @return
	 */
	public String toString(String preffixOfproperty) {

		StringBuilder ql = new StringBuilder();
		ql.append(" ");
		ql.append(getRelationalOpertor());
		ql.append(" ");
		if (null != preffixOfproperty && !"".equals(preffixOfproperty.trim())) {
			ql.append(preffixOfproperty.trim());
		}
		ql.append(getField());
		ql.append(" ");
		ql.append(getConditionalOperator());
		ql.append(" ");
		ql.append(getValue());
		return ql.toString();
	}

	public String toString() {
		return this.toString(null);
	}

	@Override
	public void switchNamingConvention(NamingConvention newSchema) {
		this.field = NamingHelper.changeNamingConvention(this.field,
				this.namingConvention, newSchema);
		this.namingConvention = newSchema;
	}
}
