package org.openthinks.generic.dao;

import java.util.Collection;

/**
 * Paginated resultSet wrapper
 * 
 * @author Zhang Junlong
 * 
 * @param <T>
 */
public class PaginatedResult<T> {
	// set of resultSet
	private Collection<T> resultSet;

	// page number of current result set
	private int page;

	// max size of resultSet in a single page
	private int size;

	// total number of pages
	private int totalPages;

	// total count of all records
	private int recordsCount;

	public Collection<T> getResultSet() {
		return resultSet;
	}

	public void setResultSet(Collection<T> resultList) {
		this.resultSet = resultList;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int section) {
		this.page = section;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int sectionSize) {
		this.size = sectionSize;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalSection) {
		this.totalPages = totalSection;
	}

	public int getRecordsCount() {
		return recordsCount;
	}

	public void setRecordsCount(int recordsCount) {
		this.recordsCount = recordsCount;
	}
}
