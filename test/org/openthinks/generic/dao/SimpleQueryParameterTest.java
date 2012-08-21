package org.openthinks.generic.dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleQueryParameterTest {
	@Test
	public void testToString() {
		SimpleQueryParameter param = new SimpleQueryParameter();
		param.setField("name");
		param.setValue("test");

		assertEquals(" AND name = 'test'", param.toString());
		assertEquals(" AND pre.name = 'test'", param.toString("pre."));

		param.setConditionalOperator("IS");
		param.setValue(null);

		assertEquals(" AND name IS null", param.toString());
	}
}
