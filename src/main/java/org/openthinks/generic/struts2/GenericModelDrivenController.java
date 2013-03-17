package org.openthinks.generic.struts2;

import java.lang.reflect.ParameterizedType;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.ValidationAwareSupport;

/**
 * Abstract model-driven controller with a generic model requesting and
 * responding template
 * 
 * @author Zhang Junlong
 * 
 * @param <T>
 */
public class GenericModelDrivenController<T> extends ValidationAwareSupport
		implements ModelDriven<Object> {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Model of request
	 */
	protected T request;

	/**
	 * Default response content
	 */
	protected Object response;

	boolean returnResponse = false;

	public GenericModelDrivenController() {
		super();

		@SuppressWarnings("unchecked")
		Class<T> type = ((Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0]);

		try {
			request = type.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getModel() {
		if (returnResponse)
			return response;
		else
			return request;
	}

	/**
	 * Set a successful response with data in model
	 */
	protected void setSuccessfulResponse() {
		this.returnResponse = true;
		GenericResponseContent response = new GenericResponseContent(
				GenericResponseContent.SUCCESS);

		response.setExtend(request);
		this.setResponse(response);
	}

	/**
	 * Set a content of response
	 * 
	 * @param data
	 */
	protected void setResponse(Object data) {
		this.returnResponse = true;

		response = data;
	}

	/**
	 * Set a failed response with a description message
	 * 
	 * @param description
	 */
	protected void setFailedResponse(String description) {
		this.returnResponse = true;
		GenericResponseContent response = new GenericResponseContent(
				GenericResponseContent.FAILURE);
		response.setDescription(description);

		this.setResponse(response);
	}

	/**
	 * Set a failed response content
	 */
	protected void setFailedResponse() {
		this.returnResponse = true;
		response = new GenericResponseContent(GenericResponseContent.FAILURE);
	}

}