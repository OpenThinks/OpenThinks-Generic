package org.openthinks.generic.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

/**
 * Generic DAO implementation depending on Java Persistence API(JPA). Providing
 * basic JPA EntityManager management and essential data access methods.
 * 
 * @author Zhang Junlong
 * 
 * @param <T>
 * @param <PK>
 */
public class GenericDaoJpaImpl<T, PK> implements GenericDao<T, PK> {

	@PersistenceContext
	protected EntityManager em;

	private Class<T> type;

	private void setType(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public GenericDaoJpaImpl() {
		super();
		try {
			Class<T> type = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];

			this.setType(type);
		} catch (Exception e) {
			this.setType((Class<T>) Object.class);
		}
	}

	public GenericDaoJpaImpl(Class<T> type) {
		super();
		this.type = type;
	}

	@Override
	public void create(T newInstance) throws Exception {
		em.persist(newInstance);
	}

	@Override
	public T read(PK id) throws Exception {
		return em.find(type, id);
	}

	@Override
	public T update(T transientObject) throws Exception {
		return em.merge(transientObject);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<T> readAll() throws Exception {
		StringBuilder ql = new StringBuilder("select t from ");
		ql.append(type.getSimpleName());
		ql.append(" t");

		return em.createQuery(ql.toString()).getResultList();
	}

	@Override
	public void delete(T persistentObject) throws Exception {
		persistentObject = em.merge(persistentObject);
		em.remove(persistentObject);

	}

	@SuppressWarnings("unchecked")
	public PaginatedResult<T> read(String ql, int size, int section)
			throws Exception {

		int totalSection = 1;
		int totalRowsCount = 0;

		String countQl = QueryStringUtility.generateCountQl(ql);

		// Count total entities numbers
		Query query = em.createQuery(countQl);

		List<Object> countSqlResult = query.getResultList();

		if (QueryStringUtility.containsGroupByStatement(countQl))
			totalRowsCount = countSqlResult.size();
		else
			totalRowsCount = ((Number) countSqlResult.get(0)).intValue();

		// Query persistent entities
		query = em.createQuery(ql);
		// divide data to sections when parameter size greater then 0
		if (size > 0) {
			if (totalRowsCount % size == 0) {
				totalSection = totalRowsCount / size;
			} else {
				totalSection = (int) Math.floor(totalRowsCount / size) + 1;
			}

			int startRow = size * (section - 1);
			query.setFirstResult(startRow);
			query.setMaxResults(size);
		}

		PaginatedResult<T> sr = new PaginatedResult<T>();
		sr.setResultSet(query.getResultList());
		sr.setTotalPages(totalSection);
		sr.setPage(section);
		sr.setSize(size);
		sr.setRecordsCount(totalRowsCount);

		return sr;
	}

	@SuppressWarnings("unchecked")
	public PaginatedResult<Object> readSpecifiedSectionWithSql(String sql,
			int size, int section, String resultMappingName) throws Exception {

		int totalSection = 1;
		int totalRowsCount = 0;

		String countQl = "SELECT COUNT(1) FROM ( " + sql + " ) tmpTbl";

		// Count total entities numbers
		Query query = em.createNativeQuery(countQl);

		Number countSqlResult = (Number) query.getSingleResult();

		totalRowsCount = countSqlResult.intValue();

		// Query persistent entities
		query = em.createNativeQuery(sql, resultMappingName);
		// divide data to sections when parameter size greater then 0
		if (size > 0) {
			if (totalRowsCount % size == 0) {
				totalSection = totalRowsCount / size;
			} else {
				totalSection = (int) Math.floor(totalRowsCount / size) + 1;
			}

			int startRow = size * (section - 1);
			query.setFirstResult(startRow);
			query.setMaxResults(size);
		}

		PaginatedResult<Object> sr = new PaginatedResult<Object>();
		sr.setResultSet(query.getResultList());
		sr.setTotalPages(totalSection);
		sr.setPage(section);
		sr.setSize(size);
		sr.setRecordsCount(totalRowsCount);

		return sr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<T> query(String ql, Object... parameters)
			throws Exception {
		// Query persistent entities
		Query query = em.createQuery(ql);

		int index = 0;
		for (Object parameter : parameters) {
			query.setParameter(index, parameter);
			index++;
		}

		return query.getResultList();
	}

}
