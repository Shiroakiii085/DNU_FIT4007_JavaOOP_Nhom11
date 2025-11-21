package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
	private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

	public static String today() {
		return LocalDate.now().format(ISO);
	}

	public static String addMonths(String isoDate, int months) {
		try {
			LocalDate d = LocalDate.parse(isoDate, ISO);
			return d.plusMonths(months).format(ISO);
		} catch (DateTimeParseException e) {
			return isoDate;
		}
	}

	public static boolean isBeforeOrEqual(String isoDate, String otherIsoDate) {
		try {
			LocalDate d1 = LocalDate.parse(isoDate, ISO);
			LocalDate d2 = LocalDate.parse(otherIsoDate, ISO);
			return !d1.isAfter(d2);
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	public static boolean isValidISO(String isoDate) {
		try {
			LocalDate.parse(isoDate, ISO);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
}
