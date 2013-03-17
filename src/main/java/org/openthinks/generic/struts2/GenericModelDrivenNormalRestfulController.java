package org.openthinks.generic.struts2;

import org.apache.struts2.rest.HttpHeaders;

/**
 * Generic model-driven restful controller with normal declarations of REST
 * (POST, GET, PUT and DELETE) request handling methods.
 * 
 * @author Zhang Junlong
 * 
 * @param <T>
 *            Request Object Type
 */
public abstract class GenericModelDrivenNormalRestfulController<T> extends
		GenericModelDrivenEmptyRestfulController<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Handling GET method without resource ID for getting all resources.
	 * 
	 * 
	 * HTTP Method: GET
	 * 
	 * URN Pattern: /{resources}
	 * 
	 * @return HttpHeaders
	 */

	abstract public HttpHeaders index();

	/**
	 * Handling GET method with a resource ID for getting a resource.
	 * 
	 * 
	 * HTTP Method: GET
	 * 
	 * URN Pattern: /{resources}/{id}
	 * 
	 * @return HttpHeaders
	 */
	abstract public HttpHeaders show();

	/**
	 * Handling POST method for creating a resource, respond a resource ID after
	 * the resource was created.
	 * 
	 * 
	 * URN Pattern: /{resources}/order
	 * 
	 * @return HttpHeaders
	 */
	abstract public HttpHeaders create();

	/**
	 * Handling PUT method with a resource ID for updating a resource.
	 * 
	 * 
	 * URN Pattern: /{resources}/{id}
	 * 
	 * @return HttpHeaders
	 */
	abstract public HttpHeaders update();

	/**
	 * Handling DELETE method with a resource ID for deleting a resource.
	 * 
	 * 
	 * URN Pattern: /{resources}/{ID}
	 * 
	 * @return HttpHeaders
	 */
	abstract public HttpHeaders destroy();

}