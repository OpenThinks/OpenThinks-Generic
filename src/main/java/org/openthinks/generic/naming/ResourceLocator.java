package org.openthinks.generic.naming;

import java.io.FileInputStream;
import java.util.Properties;

import javax.naming.*;

/**
 * A resource locator Class which can be used to locate resources in various
 * environment
 * 
 * @author Administrator
 * 
 * @param <Type>
 */
public class ResourceLocator<Type> {

	private InitialContext initialContext;

	/**
	 * This constructor will initiate a default context
	 * 
	 * @throws Exception
	 */
	public ResourceLocator() throws Exception {
		try {
			initialContext = new InitialContext();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * This constructor will load the specified file of properties in classpath
	 * 
	 * @param propsFileName
	 * @throws Exception
	 */
	public ResourceLocator(String propsFileName) throws Exception {
		try {
			initialContext = getInitialContext(propsFileName);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public ResourceLocator(FileInputStream input) throws Exception {
		try {
			Properties props = new Properties();
			props.load(input);
			initialContext = new InitialContext(props);
		} catch (Exception e) {
			throw e;
		}
	}

	public ResourceLocator(Properties props) throws NamingException {
		initialContext = new InitialContext(props);
	}

	/**
	 * Get a initial context using the specified properties file
	 * 
	 * @param propsFileName
	 * @return
	 * @throws Exception
	 */
	private InitialContext getInitialContext(String propsFileName)
			throws Exception {
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(propsFileName));
			return new InitialContext(props);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * Look up a jndi resource using specified argument
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Type getResource(String name) throws Exception {

		try {
			return (Type) initialContext.lookup(name);
		} catch (NamingException e) {
			System.out
					.println("A Naming Exception occured when looking up from resource context");
			throw e;
		} catch (ClassCastException e) {
			System.out
					.println("A Class CastingType Exception occured when casting the type of object looked up from context");
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Close context resources
	 */
	public void destory() {
		try {
			initialContext.close();
		} catch (Exception e) {
			System.out
					.println("A Exception Occured when closing the intial context");
			e.printStackTrace();
		}
	}

}
