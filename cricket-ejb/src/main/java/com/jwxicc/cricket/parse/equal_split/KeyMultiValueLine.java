package com.jwxicc.cricket.parse.equal_split;


public class KeyMultiValueLine extends KeyValueLine {
	protected String[] valueArray;

	public KeyMultiValueLine(String line) {
		super(line);
		this.valueArray = this.value.split(";");
	}

	public KeyMultiValueLine(String key, String value) {
		super(key, value);
		this.valueArray = this.value.split(";");
	}

	public KeyMultiValueLine(KeyValueLine kvl) {
		this(kvl.getKey(), kvl.getValue());
	}

	public String getValueAt(int i) {
		return valueArray[i].trim();
	}

	public int getIntValueAt(int i) throws NumberFormatException {
		return Integer.parseInt(getValueAt(i));
	}

	public String[] getValueArray() {
		return valueArray;
	}

	public void setValueArray(String[] valueArray) {
		this.valueArray = valueArray;
	}
}