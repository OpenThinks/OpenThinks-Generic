package org.openthinks.generic.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The CalendarNumbers class is an class that provides methods for get different
 * number in Calendar to assist date program
 * 
 * @author Zhang Junlong
 * 
 */
public class CalendarNumbers {

	/**
	 * get the number of days of the indicated date
	 * 
	 * @param date
	 * @return
	 */
	public int getDaysNumberOfMonth(Date date) {
		Calendar calendar = new GregorianCalendar();
		((Calendar) calendar).setTime(date);

		int daysNumber = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		return daysNumber;
	}
}
