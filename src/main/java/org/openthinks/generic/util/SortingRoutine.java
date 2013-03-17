package org.openthinks.generic.util;

/**
 * Routine program for sorting.
 *
 * @Author: Zhang Junlong
 */
public class SortingRoutine {
	/**
	 * Direct insert the candidate of specified set to a sorted set orderly.
	 *
	 * @param array array to be sorting
	 * @throws Exception
	 */
	public static void directInsertSort(int[] array) throws Exception {
		for (int i = 1; i < array.length; i++) {
			int j = i - 1;
			int tmp = array[i];
			while (j >= 0 && tmp < array[j]) {
				array[j + 1] = array[j];
				j--;
			}
			array[j + 1] = tmp;
		}
	}
}
