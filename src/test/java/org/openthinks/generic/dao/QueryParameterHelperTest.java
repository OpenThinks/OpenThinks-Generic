package org.openthinks.generic.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.openthinks.generic.util.NamingConvention;


public class QueryParameterHelperTest extends QueryParameterHelper {

	private Collection<QueryParameter> params, paramsWithOnlyOne;
	SimpleQueryParameter param;
	ComboQueryParameter cParam;

	@Before
	public void setUp() {
		paramsWithOnlyOne = new ArrayList<QueryParameter>();
		params = new ArrayList<QueryParameter>();

		cParam = new ComboQueryParameter();
		param = new SimpleQueryParameter();
		param.setField("test.name");
		param.setValue("test");
		param.setNamingConvention(NamingConvention.DOT);
		cParam.add(param);

		paramsWithOnlyOne.add(param);

		params.add(cParam);
		params.add(param);

	}

	@Test
	public void testBuildQlStringCollectionOfQueryParameterNameSchema() {
		param.setRelationalOpertor("OR");
		StringBuilder testStrBuilder = QueryParameterHelper.buildWhereClause(
				paramsWithOnlyOne, NamingConvention.UNDERSCORE, true);

		// single OR ro should be ignored
		assertEquals("", testStrBuilder.toString());

		param.setRelationalOpertor("AND");
		testStrBuilder = QueryParameterHelper.buildWhereClause(
				paramsWithOnlyOne, NamingConvention.UNDERSCORE, true);
		assertEquals(" WHERE  test_name = 'test'", testStrBuilder.toString());

	}

	@Test
	public void testBuildQlStringCollectionOfQueryParameter() {
		param.setRelationalOpertor("OR");

		StringBuilder testStr = QueryParameterHelper.buildWhereClause(
				paramsWithOnlyOne, true);
		assertEquals("", testStr.toString());

		testStr = QueryParameterHelper.buildWhereClause(paramsWithOnlyOne,
				false);
		assertEquals(" OR test.name = 'test'", testStr.toString());

		param.setRelationalOpertor("AND");

		testStr = QueryParameterHelper
				.buildWhereClause(paramsWithOnlyOne, true);
		assertEquals(" WHERE  test.name = 'test'", testStr.toString());

		testStr = QueryParameterHelper.buildWhereClause(paramsWithOnlyOne,
				false);
		assertEquals(" AND test.name = 'test'", testStr.toString());

		testStr = QueryParameterHelper.buildWhereClause(params, true);
		assertEquals(" WHERE ( test.name = 'test' ) AND test.name = 'test'",
				testStr.toString());

		cParam.setRelationalOperator("AND");
		testStr = QueryParameterHelper.buildWhereClause(params, true);
		assertEquals(" WHERE  ( test.name = 'test' ) AND test.name = 'test'",
				testStr.toString());

		cParam.setRelationalOperator("OR");
		testStr = QueryParameterHelper.buildWhereClause(params, false);
		assertEquals(" OR ( test.name = 'test' ) AND test.name = 'test'",
				testStr.toString());

		cParam.setRelationalOperator("AND");
		testStr = QueryParameterHelper.buildWhereClause(params, false);
		assertEquals(" AND ( test.name = 'test' ) AND test.name = 'test'",
				testStr.toString());
	}
}
