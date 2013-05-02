package com.jwxicc.cricket.parse.equal_split;

import org.apache.commons.lang.StringUtils;

public class KeyValueLine {
	protected static final String SPLIT_STRING = "=";
	protected static final String NULL_KEY_STRING = "No Key Value";
	protected String key;
	protected String value;

	public KeyValueLine(String line) {
		String[] splitString = line.trim().split(SPLIT_STRING);
		if (splitString.length != 1) {
			this.key = splitString[0].trim();
			this.value = splitString[1].trim();
		} else {
			this.key = NULL_KEY_STRING;
			this.value = null;
		}
	}

	public KeyValueLine(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public boolean keyEquals(String checkVal) {
		// check if key = checkVal
		return key.equalsIgnoreCase(checkVal);
	}

	public boolean keyStartsWith(String checkVal) {
		// check if key starts with checkVal, ignore case
		return StringUtils.startsWithIgnoreCase(key, checkVal);
	}

	public boolean keyIsNumber(int num) {
		return key.endsWith(String.valueOf(num));
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getIntValue() throws NumberFormatException {
		return Integer.valueOf(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return key + SPLIT_STRING + value;
	}
}