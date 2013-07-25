package org.openthinks.generic.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ContextConfiguration;

/**
 * A abstract class for testing suite depending SpringFramworks
 * 
 * Basic spring container resources are loaded, including:
 * /resources/spring/applicationContext-*.xml /resources/spring/test/*.xml
 * 
 * @author Zhang Junlong
 * 
 */
@RunWith(Suite.class)
@ContextConfiguration({ "/spring/applicationContext-*.xml",
		"/spring/test/*.xml" })
public abstract class SpringTestSuiteSupport {

}
