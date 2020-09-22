/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.aps.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;

/**
 * Utility class for date handling. 
 * @author E.Santoboni
 */
public class DateConverter {

	private static final EntLogger _logger = EntLogFactory.getSanitizedLogger(DateConverter.class);
	/**
	 * Utility method. Return a formatted string representing the given date
	 * @param date the date object to convert into a string
	 * @param pattern the pattern to handle the date with
	 * @param langCode The lang code
	 * @return the string of the resulting date
	 */
	public static String getFormattedDate(Date date, String pattern, String langCode) {
		if (null == langCode) return getFormattedDate(date, pattern);
		String dateString = "";
		if (null != date) {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern, new Locale(langCode, ""));
			dateString = formatter.format(date);
		}
		return dateString;
	}
	
	/**
	 * Utility method. Return a formatted string representing the given date
	 * @param date the date object to convert into a string
	 * @param pattern the pattern to handle the date with
	 * @return the string of the resulting date
	 */
	public static String getFormattedDate(Date date, String pattern) {
		String dateString = "";
		if (null != date) {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			dateString = formatter.format(date);
		}
		return dateString;
	}
	
	/**
	 * Utility method. Parses the input string to a date object, given a valid pattern DATE_FORMAT. Return null
	 * if the string itself is either null or empty (or if the format is invalid of course)
	 * @param stringData the string representing the date to convert 
	 * @param pattern used to perform the conversion
	 * @return the result of the conversion
	 */
	public static Date parseDate(String stringData, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date data = null;
		try {
			if (null != stringData && stringData.length() > 0) {
				data = format.parse(stringData);
			}
		} catch (ParseException ex) {
			_logger.error("Wrong date format detected : {}", stringData, ex);
		}
		return data;
	}

}
