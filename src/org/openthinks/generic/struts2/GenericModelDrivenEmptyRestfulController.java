package org.openthinks.generic.struts2;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.rest.DefaultHttpHeaders;

/**
 * Generic model-driven restful controller without any definition of request
 * handling method.
 * 
 * 
 * @author Zhang Junlong
 * 
 */
public abstract class GenericModelDrivenEmptyRestfulController<T> extends
		GenericModelDrivenController<T> implements RestfulHttpHeadersAware,
		SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Map<String, Object> session;

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	/**
	 * Create a HttpHeaders object for new created resource.
	 * 
	 * @param id
	 * @return DefaultHttpHeaders
	 */
	@Override
	public DefaultHttpHeaders createCreatedHttpHeaders(Object id) {
		return new DefaultHttpHeaders("success").setLocationId(id);
	}
}