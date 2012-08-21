package org.openthinks.generic.dao;

import java.util.Collection;

/**
 * Generic DAO interface. Defining basic CRUD(creations, reads, updates and
 * deletions) methods and essential querying methods. Finders are added with
 * interface inheritance and AOP introductions for concrete implementations
 * 
 * Extended interfaces may declare methods starting with find... list...
 * iterate... or scroll... They will execute a pre-configured query that is
 * looked up based on the rest of the method name
 * 
 * @author Per Mellqvist
 * 
 * @param <T>
 * @param <PK>
 */
public interface GenericDao<T, PK> {
	/**
	 * Persist the newInstance object into database
	 * 
	 * @param newInstance
	 */
	void create(T newInstance) throws Exception;

	/**
	 * Retrieve an object that was previously persisted to the database using
	 * the indicated id as primary key
	 * 
	 * @param id
	 * @return
	 */
	T read(PK id) throws Exception;

	/**
	 * Save changes made to a persistent object.
	 * 
	 * @param transientObject
	 * @return
	 */
	T update(T transientObject) throws Exception;

	/**
	 * Remove an object from persistent storage in the database
	 * 
	 * @param persistentObject
	 */
	void delete(T persistentObject) throws Exception;

	/**
	 * Querying using a query language
	 * 
	 * @param ql
	 */
	Collection<T> query(String ql, Object... parameters) throws Exception;

	/**
	 * Query persisted objects by custom query string and return paginated
	 * results.
	 * 
	 * @param ql
	 *            query language string
	 * @param size
	 *            max size of a single page, 0 or negative number means infinity
	 * @param page
	 *            page number of paginating results
	 * @return
	 */
	public PaginatedResult<T> read(String ql, int size, int page)
			throws Exception;

	/**
	 * Read out all persisted objects
	 * 
	 * @return
	 * @throws Exception
	 */
	Collection<T> readAll() throws Exception;

}
