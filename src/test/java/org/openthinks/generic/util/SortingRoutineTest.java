/*
 * Copyright (c) 2013 author or authors as indicated by the @author tags or express
 * copyright attribution statements applied by the authors. All rights reserved.
 */

package org.openthinks.generic.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * @Author: Zhang Junlong
 */
public class SortingRoutineTest {

	private int[] arr = {1, 3, 4, 5, 7, 8, 9, 0, 2, 6};

	@Before
	public void setup() {

	}

	@Test
	public void testDirectInsertSort() throws Exception {
		SortingRoutine.directInsertSort(arr);
		if (!this.isSequence(arr, false))
			fail();
	}

	private boolean isSequence(int[] arr, boolean reverseOrder) {
		boolean flag = true;
		for (int i = 1; i < arr.length; i++) {
			if (!reverseOrder) {
				if (arr[i] < arr[i - 1])
					flag = false;
			} else {
				if (arr[i] > arr[i - 1])
					flag = false;
			}

			System.out.println(arr[i]);
		}
		return flag;
	}
}
