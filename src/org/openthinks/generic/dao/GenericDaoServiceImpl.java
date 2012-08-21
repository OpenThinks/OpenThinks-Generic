package org.openthinks.generic.dao;

import java.util.Collection;

public class GenericDaoServiceImpl<T, PK> implements GenericDaoService<T, PK> {

	private GenericDao<T, PK> genericDao;

	@Override
	public void create(T instance) throws Exception {
		genericDao.create(instance);
	}

	@Override
	public T find(PK id) throws Exception {
		return (T) genericDao.read(id);
	}

	@Override
	public void update(T instance) throws Exception {
		genericDao.update(instance);
	}

	@Override
	public void delete(PK id) throws Exception {
		genericDao.delete(genericDao.read(id));
	}

	@Override
	public Collection<T> findAll() throws Exception {
		return genericDao.readAll();
	}

	public void setGenericDao(GenericDao<T, PK> genericDao) {
		this.genericDao = genericDao;
	}

}
