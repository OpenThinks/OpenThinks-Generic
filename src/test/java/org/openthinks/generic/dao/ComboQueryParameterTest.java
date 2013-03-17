package org.openthinks.generic.dao;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComboQueryParameterTest {

	@Test
	public void testToStringString() {
		ComboQueryParameter comboQueryParameter = new ComboQueryParameter();

		assertEquals("", comboQueryParameter.toString());
		assertEquals("", comboQueryParameter.toString("test"));

		SimpleQueryParameter simpleQueryParameter = new SimpleQueryParameter();
		simpleQueryParameter.setField("test");
		simpleQueryParameter.setValue("test");

		comboQueryParameter.add(simpleQueryParameter);
		String excepted = " OR ( test = 'test' )";
		assertEquals(excepted, comboQueryParameter.toString());
	}

}
