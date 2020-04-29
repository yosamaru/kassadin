package com.yosamaru.kassadin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final Calendar DEFAULT_CALENDAR;

	public DateUtil() {
	}

	public static long daysBetween(final Calendar startDate, final Calendar endDate) {
		LocalDate start = startDate.getTime().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();
		LocalDate end = endDate.getTime().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();
		return ChronoUnit.DAYS.between(start, end);
	}

	public static Date getQuarterStartTime(Date date, int differ) {
		return getQuarterTime(date, differ, DateUtil.QuarterTimeType.QUARTER_TIME_TYPE_START);
	}

	public static Date getQuarterEndTime(Date date, int differ) {
		return getQuarterTime(date, differ, DateUtil.QuarterTimeType.QUARTER_TIME_TYPE_END);
	}

	private static Date getQuarterTime(Date date, int differ,
			DateUtil.QuarterTimeType quaterTimeType) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date.getTime());
		int currentMonth = c.get(2) + 1;
		Date now = new Date();
		int month = 0;
		int[] quarterStartMonthsArray = new int[]{0, 3, 6, 9};
		int[] quarterEndMonthsArray = new int[]{2, 5, 8, 11};
		int[] quarterMonthsArray = null;
		if (quaterTimeType == DateUtil.QuarterTimeType.QUARTER_TIME_TYPE_END) {
			quarterMonthsArray = quarterEndMonthsArray;
		} else {
			quarterMonthsArray = quarterStartMonthsArray;
		}

		if (currentMonth >= 1 && currentMonth <= 3) {
			month = quarterMonthsArray[0];
		} else if (currentMonth >= 4 && currentMonth <= 6) {
			month = quarterMonthsArray[1];
		} else if (currentMonth >= 7 && currentMonth <= 9) {
			month = quarterMonthsArray[2];
		} else if (currentMonth >= 10 && currentMonth <= 12) {
			month = quarterMonthsArray[3];
		}

		month += differ * 3;
		c.set(2, month);
		c.set(5, 1);
		now.setTime(c.getTimeInMillis());
		if (quaterTimeType == DateUtil.QuarterTimeType.QUARTER_TIME_TYPE_START) {
			now = setZeroTime(setStartOfMonth(now));
		} else {
			now = setMaxTime(setEndOfMonth(now));
		}

		return now;
	}

	public static long daysBetween(final Date startDate, final Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		return daysBetween(startCal, endCal);
	}

	public static Date addDays(final Date date, final int numberOfDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(5, numberOfDays);
		return cal.getTime();
	}

	public static Date addMonths(final Date date, final int numberOfMonths) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(2, numberOfMonths);
		return cal.getTime();
	}

	public static Date addYears(final Date date, final int numberOfYears) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(1, numberOfYears);
		return cal.getTime();
	}

	public static Date getLastDayOfQuarter(int quarter, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(1, year);
		switch (quarter) {
			case 1:
				cal.set(2, 2);
				cal.set(5, 31);
				break;
			case 2:
				cal.set(2, 5);
				cal.set(5, 30);
				break;
			case 3:
				cal.set(2, 8);
				cal.set(5, 30);
				break;
			case 4:
				cal.set(2, 11);
				cal.set(5, 31);
		}

		return cal.getTime();
	}

	public static boolean isSameDay(final Date date1, final Date date2) {
		if (date1 != null && date2 != null) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(date1);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(date2);
			int[] var4 = new int[]{1, 2, 5};
			int var5 = var4.length;

			for (int var6 = 0; var6 < var5; ++var6) {
				int q = var4[var6];
				if (c1.get(q) != c2.get(q)) {
					return false;
				}
			}
		} else if (date1 != null && date2 == null || date2 != null && date1 == null) {
			return false;
		}

		return true;
	}

	public static Date setEndOfMonth(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(5, calendar.getActualMaximum(5));
		return setMaxTime(calendar.getTime());
	}

	public static Date setStartOfMonth(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(5, calendar.getActualMinimum(5));
		return setZeroTime(calendar.getTime());
	}

	public static Date setMaxTime(final Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int[] var2 = new int[]{11, 12, 13, 14};
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			int p = var2[var4];
			cal.set(p, cal.getMaximum(p));
		}

		return cal.getTime();
	}

	public static Date setZeroTime(final Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int[] var2 = new int[]{11, 12, 13, 14};
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			int p = var2[var4];
			cal.set(p, cal.getMinimum(p));
		}

		return cal.getTime();
	}

	public static Calendar getDefaultCalendarDate() {
		return DEFAULT_CALENDAR;
	}

	public static Date getDefaultDate() {
		return DEFAULT_CALENDAR.getTime();
	}

	public static int getDefaultDateYear() {
		return DEFAULT_CALENDAR.get(1);
	}

	public static Date overrideTimeForGivenDateBySourceDate(final Date givenDate,
			final Date sourceDate) {
		Calendar src = Calendar.getInstance();
		Calendar dst = Calendar.getInstance();
		src.setTime(sourceDate);
		dst.setTime(givenDate);
		dst.set(11, src.get(11));
		dst.set(12, src.get(12));
		dst.set(13, src.get(13));
		dst.set(14, src.get(14));
		return dst.getTime();
	}

	public static Calendar toCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public static boolean isWeekEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int caldate = cal.get(7);
		return caldate == 7 || caldate == 1;
	}

	public static Date toStartOfYear(final Date year) {
		Calendar calendarYear = Calendar.getInstance();
		calendarYear.setTime(year);
		int yearInt = calendarYear.get(1);
		Calendar calendar = Calendar.getInstance();
		calendar.set(1, yearInt);
		calendar.set(2, 0);
		calendar.set(5, 1);
		calendar.set(11, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.set(14, 0);
		return calendar.getTime();
	}

	public static Date toEndOfYear(final Date year) {
		Calendar calendarYear = Calendar.getInstance();
		calendarYear.setTime(year);
		int yearInt = calendarYear.get(1);
		Calendar calendar = Calendar.getInstance();
		calendar.set(1, yearInt);
		calendar.set(2, 11);
		calendar.set(5, 31);
		calendar.set(11, 23);
		calendar.set(12, 59);
		calendar.set(13, 59);
		calendar.set(14, 999);
		return calendar.getTime();
	}

	public static boolean isSameMonth(final Date date1, final Date date2) {
		if (date1 != null && date2 != null) {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			return isSameMonth(cal1, cal2);
		} else {
			throw new IllegalArgumentException("The date must not be null");
		}
	}

	public static boolean isSameMonth(final Calendar cal1, final Calendar cal2) {
		if (cal1 != null && cal2 != null) {
			return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(2) == cal2.get(2);
		} else {
			throw new IllegalArgumentException("The date must not be null");
		}
	}

	public static boolean isWithinPeriod(final Date startDate, final Date endDate, final Date now) {
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.setTime(startDate);
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(endDate);
		Calendar nowDateCal = Calendar.getInstance();
		nowDateCal.setTime(now);
		return nowDateCal.after(startDateCal) && nowDateCal.before(endDateCal);
	}

	public static Date setTimeToDate(final Date date, final int hour, final int minutes,
			final int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(11, hour);
		calendar.set(12, minutes);
		calendar.set(13, seconds);
		return calendar.getTime();
	}

	public static Date addMinutes(final Date date, final int numberOfMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(12, numberOfMinutes);
		return cal.getTime();
	}

	public static Date getNextDayOfWeek(final int dayOfWeek) {
		Calendar date = Calendar.getInstance();
		int diff = dayOfWeek - date.get(7) <= 0 ? dayOfWeek - date.get(7) + 7 : dayOfWeek - date.get(7);
		date.add(5, diff);
		return date.getTime();
	}

	public static Date parseDate(final Date dateStr, final DateUtil.Format format)
			throws UtilException {
		SimpleDateFormat sdf = new SimpleDateFormat(format.toString());
		return parse(sdf.format(dateStr), format);
	}

	public static Date parse(final String dateStr, final DateUtil.Format format)
			throws UtilException {
		SimpleDateFormat sdf = new SimpleDateFormat(format.toString());
		new Date();

		try {
			Date d = sdf.parse(dateStr);
			return d;
		} catch (ParseException var5) {
			throw new UtilException(String
					.format("Cannot parse date \"%s\": not compatible with any of standard forms (%s)",
							dateStr, format));
		}
	}

	public static String format(final Date date, final DateUtil.Format format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format.toString());
		return sdf.format(date);
	}

	static {
		Calendar calendar = Calendar.getInstance();
		calendar.set(1950, 0, 1);
		calendar.set(9, 0);
		calendar.set(10, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.set(14, 0);
		DEFAULT_CALENDAR = calendar;
	}

	private static enum QuarterTimeType {
		QUARTER_TIME_TYPE_START("start"),
		QUARTER_TIME_TYPE_END("end");

		private final String value;

		private QuarterTimeType(final String value) {
			this.value = value;
		}

		public String toString() {
			return this.value;
		}
	}

	public static enum Format {
		DATE("yyyy-MM-dd"),
		DATETIME("yyyy-MM-dd HH:mm:ss"),
		DATETIME_WITH_MILLISECONDS("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
		DATE_WITHOUT_HYPHEN("yyyyMMdd"),
		DAY_MONTH_YEAR_WITH_HYPHEN("dd-MM-yyyy"),
		YEAR_MONTH_WITH_HYPHEN("yyyy-MM"),
		SIMPLE_TIME("HH:mm"),
		TIME("HH:mm:ss"),
		DATETIME_JOIN_MILLISECONDS("yyyyMMddHHmmssSSSZ"),
		DATETIME_JOIN_MILLISECONDS_WITHOUT_TIMEZONE("yyyyMMddHHmmss");

		private final String value;

		private Format(final String value) {
			this.value = value;
		}

		public String toString() {
			return this.value;
		}
	}
}

