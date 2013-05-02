package com.jwxicc.cricket.parse.cricketstatz;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTypeConverter {
	public static Date parseStringDate(String dateString) throws ParseException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date date = df.parse(dateString);
		
		return date;
	}
}
