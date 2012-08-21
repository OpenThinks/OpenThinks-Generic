package org.openthinks.generic.dao;

import java.util.Collection;

/**
 * A generic facade service for DAO.
 * 
 * @author Zhang Junlong
 * 
 */
public interface GenericDaoService<T, PK> {
	void create(T instance) throws Exception;

	T find(PK id) throws Exception;

	void update(T instance) throws Exception;

	void delete(PK id) throws Exception;

	Collection<T> findAll() throws Exception;
}
