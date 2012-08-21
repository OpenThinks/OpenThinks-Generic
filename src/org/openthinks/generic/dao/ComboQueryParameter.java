package org.openthinks.generic.dao;

import org.openthinks.generic.util.NamingConvention;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Combo query parameter wrapper, all query parameters included would act a
 * group query conditions wrapped by brackets.
 * 
 * @author Zhang Junlong
 * 
 */
public class ComboQueryParameter implements QueryParameter {
	private String relationalOperator = "OR";

	private Collection<QueryParameter> queryParameterSet = new ArrayList<QueryParameter>();

	private NamingConvention namingConvention;

	public String getRelationalOperator() {
		return relationalOperator;
	}

	public void setRelationalOperator(String relationalOperator) {
		this.relationalOperator = relationalOperator;
	}

	public Collection<QueryParameter> getQueryParameterSet() {
		return queryParameterSet;
	}

	public void setQueryParameterSet(
			Collection<QueryParameter> queryParameterSet) {
		this.queryParameterSet = queryParameterSet;
	}

	@Override
	public String toString(String preffix) {
		if (null == this.queryParameterSet
				|| this.queryParameterSet.size() == 0)
			return "";
		else {
			StringBuilder ql = new StringBuilder();

			ql.append(" ");
			ql.append(this.relationalOperator);
			ql.append(" (");
			for (QueryParameter sqp : this.queryParameterSet) {
				if (null != namingConvention)
					sqp.switchNamingConvention(namingConvention);
				ql.append(sqp.toString(preffix));
			}
			ql.append(" )");

			return ql.toString().replaceAll("\\( AND|\\( OR", "(");
		}
	}

	public void add(QueryParameter queryParamter) {
		this.queryParameterSet.add(queryParamter);
	}

	@Override
	public String toString() {
		return this.toString(null);
	}

	@Override
	public void switchNamingConvention(NamingConvention schema) {
		this.namingConvention = schema;
	}
}