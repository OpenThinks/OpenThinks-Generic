package org.openthinks.generic.dao;

import java.util.Collection;

import org.openthinks.generic.util.NamingConvention;


public class QueryParameterHelper {

	/**
	 * Build a WHERE clause of query, naming field with specified schema.
	 * 
	 * @param queryParameters
	 * @param schema
	 * @param integrally
	 *            if to build a integral where clause or a fragment.
	 * @return
	 */
	public static StringBuilder buildWhereClause(
			Collection<QueryParameter> queryParameters, NamingConvention schema,
			boolean integrally) {
		StringBuilder clause = new StringBuilder();
		if (null != queryParameters) {
			for (QueryParameter param : queryParameters) {
				param.switchNamingConvention(schema);
				clause.append(param);
			}
		}

		if (integrally)
			addWhereKeyword(clause);

		return clause;
	}

	/**
	 * Build where clause for a query.
	 * 
	 * @param queryParameters
	 * @param integrally
	 *            if to build a complete where clause or a fragment.
	 * @return
	 */
	public static StringBuilder buildWhereClause(
			Collection<QueryParameter> queryParameters, boolean integrally) {
		StringBuilder clause = new StringBuilder();
		if (null != queryParameters) {
			for (QueryParameter param : queryParameters) {
				clause.append(param);
			}
		}

		if (integrally)
			addWhereKeyword(clause);

		return clause;
	}

	/**
	 * Add keyword WHERE to a clause.
	 * 
	 * @param clause
	 */
	public static void addWhereKeyword(StringBuilder clause) {
		int paramNumberAnd, paramNumberOr = 0;
		String[] tmpAndStringArray = clause.toString().toLowerCase()
				.split(" and ");
		paramNumberAnd = tmpAndStringArray.length - 1;
		for (String tmpAndString : tmpAndStringArray) {
			String[] tmpOrStringArray = tmpAndString.split(" or ");
			paramNumberOr += tmpOrStringArray.length - 1;
		}

		// Delete entire clause if there is only a "OR" clause
		if ((1 == paramNumberAnd + paramNumberOr) && (1 == paramNumberOr))
			clause.delete(0, clause.length());

		if (clause.length() > 0) {
			clause.delete(0, 4);
			clause.insert(0, " WHERE ");
		}
	}
}
