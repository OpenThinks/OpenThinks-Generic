package org.openthinks.generic.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QueryStringUtilityTest {

	@Test
	public void testContainsGroupByStatement() {
		String ql = "select GROUP BY";
		assertTrue(QueryStringUtility.containsGroupByStatement(ql));

		ql = "select GrOUP by";
		assertTrue(QueryStringUtility.containsGroupByStatement(ql));

		ql = "select GROUP    by";
		assertTrue(QueryStringUtility.containsGroupByStatement(ql));

		ql = "select GROUPby";
		assertFalse(QueryStringUtility.containsGroupByStatement(ql));

		ql = "select GROUP+by";
		assertFalse(QueryStringUtility.containsGroupByStatement(ql));
	}

	@Test
	public void testContainsSubquery() {
		String ql = "select * fRom ( select* from p) p";
		QueryStringUtility.containsSubquery(ql);
	}

	@Test
	public void testGenerateCountQl() throws Exception {

		String ql = "select p.id  pi.name from Product p where ";
		String countQl = QueryStringUtility.generateCountQl(ql);
		assertEquals("select COUNT(p) from Product p where", countQl);

		ql = "select product from Product product";
		countQl = QueryStringUtility.generateCountQl(ql);
		assertEquals("select COUNT(product) from Product product", countQl);

		ql = "select product from (subquerying(subquerying)) p";
		countQl = QueryStringUtility.generateCountQl(ql);
		assertEquals("select COUNT(p) from from (subquerying) p", countQl);
	}
}
