package org.openthinks.generic.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A abstract class for test cases depending SpringFramework
 * 
 * Basic Spring container resources are loaded, including:
 * /resources/spring/applicationContext-*.xml /resources/spring/test/*.xml
 * 
 * @author Zhang Junlong
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/spring/applicationContext-*.xml",
		"/spring/test/*.xml" })
public abstract class SpringTestSupport {

}
