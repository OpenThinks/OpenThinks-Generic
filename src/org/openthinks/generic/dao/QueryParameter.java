package org.openthinks.generic.dao;

import org.openthinks.generic.util.NamingConvention;

/**
 * Query parameter wrapper interface, this type should be converted to a query
 * language string fragment.
 * 
 * @author Zhang Junlong
 * 
 */
public interface QueryParameter {

	/**
	 * Switch name schema of query parameter field.
	 * 
	 * @param schema
	 */
	void switchNamingConvention(NamingConvention schema);

	/**
	 * To query language string fragment with a suffix adding to all properties.
	 * 
	 * @param suffix
	 * @return
	 */
	String toString(String suffix);

	/**
	 * To query language string.
	 * 
	 * @return
	 */
	String toString();
}
