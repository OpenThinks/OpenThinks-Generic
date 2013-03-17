package org.openthinks.generic.struts2;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

/**
 * Restful HttpHeaders aware interface.
 * 
 * Defined some useful HttpHeaders of response. Creation of HttpHeaders with
 * dynamic resource ID should be implemented or extended.
 * 
 * @author Zhang Junlong
 * 
 */
public interface RestfulHttpHeadersAware {

	/**
	 * Create HttpHeaders object for 201 Created status.
	 * 
	 * @param resourceId
	 * @return
	 */
	DefaultHttpHeaders createCreatedHttpHeaders(Object resourceId);

	/**
	 * HttpHeaders object for 202 Accepted status.
	 */
	static final HttpHeaders ACCEPTED = new DefaultHttpHeaders()
			.withStatus(202);

	/**
	 * HttpHeaders object for 500 Internal Server Error status.
	 */
	static final HttpHeaders INTERNAL_SERVER_ERROR = new DefaultHttpHeaders()
			.withStatus(500);

	/**
	 * HttpHeaders object for 501 Not Implemented status.
	 */
	static final HttpHeaders NOT_IMPLEMENTED = new DefaultHttpHeaders()
			.withStatus(501);

	/**
	 * HttpHeaders object for 200 OK status.
	 */
	static HttpHeaders OK = new DefaultHttpHeaders("success");

	/**
	 * HttpHeaders object for 200 OK status with 'success' message and disabled
	 * caching.
	 */
	static final HttpHeaders DISABLED_CACHING_OK = new DefaultHttpHeaders(
			"success").disableCaching();

	/**
	 * HttpHeaders object for 200 OK status with 'index' message and disabled
	 * caching.
	 */
	static final DefaultHttpHeaders DISABLED_CACHING_INDEX_OK = new DefaultHttpHeaders(
			"index").disableCaching();

	/**
	 * HttpHeaders object for 200 OK status with 'show' message and disabled
	 * caching.
	 */
	static final DefaultHttpHeaders DISABLED_CACHING_SHOW_OK = new DefaultHttpHeaders(
			"show").disableCaching();

}
