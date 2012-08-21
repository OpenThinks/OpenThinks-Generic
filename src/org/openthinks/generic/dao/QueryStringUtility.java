package org.openthinks.generic.dao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for generic querying string handling
 * 
 * @author Zhang Junlong
 * 
 */
public class QueryStringUtility {
	/**
	 * Judge if the specified querying string contain GROUP BY statement
	 * 
	 * @param ql
	 */
	public static boolean containsGroupByStatement(String ql) {
		String regEx = "group[\\s]+by";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(ql.toLowerCase());
		return m.find();
	}

	/**
	 * If the specified querying string has a sub-querying
	 * 
	 * @param ql
	 * @return
	 */
	public static boolean containsSubquery(String ql) {
		String regEx = "from (.])";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(ql.toUpperCase());
		return m.find();
	}

	public static String generateCountQl(String ql) throws Exception {
		StringBuilder countQl = new StringBuilder(ql.trim());
		countQl.trimToSize();

		// Find the index of "from"
		int indexOfFrom = 0;
		int index = 0;
		indexOfFrom = ql.toLowerCase().indexOf("from");
		index = indexOfFrom;
		if (index < 0)
			throw new Exception("Illegal JPQL statment, no 'FROM' was found.");

		// TODO to be continue
		// Substring after from
		String subString = countQl.substring(index + 4).trim();

		index = subString.indexOf(" ");
		subString = subString.substring(index, subString.length()).trim();
		index = subString.indexOf(" ");
		if (index > 0)
			subString = subString.substring(0, index);

		countQl.delete(6, indexOfFrom);

		countQl.insert(6, " COUNT(" + subString + ") ");

		return countQl.toString();
	}
}
