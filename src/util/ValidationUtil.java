package util;

import java.util.regex.Pattern;

public class ValidationUtil {
	private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	private static final Pattern PHONE = Pattern.compile("^\\d{8,15}$");

	public static boolean isValidEmail(String email) {
		if (email == null) return false;
		return EMAIL.matcher(email).matches();
	}

	public static boolean isValidPhone(String phone) {
		if (phone == null) return false;
		String digits = phone.replaceAll("\\D", "");
		return PHONE.matcher(digits).matches();
	}

	public static boolean isPositiveInt(String s) {
		if (s == null) return false;
		try {
			return Integer.parseInt(s) > 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
