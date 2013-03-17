/*
 * Copyright (c) 2012 author or authors as indicated by the @author tags or express
 * copyright attribution statements applied by the authors. All rights reserved.
 */

package org.openthinks.generic.util.charset;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

/**
 * Java bean URL encoding utility
 *
 * @author Zhang Junlong
 */
public class BeanEncoder {

	/**
	 * 给对象的字符串属性做编码
	 *
	 * @param obj
	 * @param charset
	 * @param urlEncoding
	 * @throws Exception
	 */
	public static void encodeProperties(Object obj, String charset,
										boolean urlEncoding) throws Exception {

		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();

		if (propertyDescriptors != null) {
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
				if (propertyDescriptor != null) {

					Method readMethod = propertyDescriptor.getReadMethod();
					Method writeMethod = propertyDescriptor.getWriteMethod();
					Class<?> propertyType = propertyDescriptor
							.getPropertyType();

					if (String.class.isAssignableFrom(propertyType)) {
						String fieldValue = "";
						if (readMethod != null) {
							fieldValue = (String) readMethod.invoke(obj);
						}

						fieldValue = encode(fieldValue, charset, urlEncoding);

						if (writeMethod != null) {
							writeMethod.invoke(obj, fieldValue);
						}
					}
				}
			}
		}
	}

	private static String encode(String orginalStr, String charset,
								 boolean urlEncoding) {

		String encodedStr = orginalStr;
		try {
			encodedStr = new String(orginalStr.getBytes(charset));

			if (urlEncoding) {
				encodedStr = URLEncoder.encode(encodedStr, "UTF-8");
			}

			return encodedStr;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			return orginalStr;
		}
	}
}