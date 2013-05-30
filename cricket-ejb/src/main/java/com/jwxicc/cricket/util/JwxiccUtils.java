package com.jwxicc.cricket.util;

public class JwxiccUtils {

	public static final int JWXICC_TEAM_ID = 2;
	public static final int RECORDS_TO_SHOW = 10;

	public static String formatError(Throwable e, String message) {
		String error = null;
		while (e.getCause() != null) {
			e = e.getCause();
		}

		// StringWriter sw = new StringWriter();
		// e.printStackTrace(new PrintWriter(sw));

		// error = message + System.lineSeparator() + e.getStackTrace()[0].toString();
		error = message + getLineSeparator() + e.getMessage();
		return error;
	}

	public static String getLineSeparator() {
		return System.lineSeparator();

	}

}
