package com.jwxicc.cricket.entity.dbenum;

public enum InningsClosureType {
	ALLOUT("all out"), CLOSED("closed"), DECLARED("declared"), ABANDONED(
			"abandoned");

	private String displayName;

	private InningsClosureType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public static InningsClosureType forInt(int i) {
		switch (i) {
		case 1:
			return ALLOUT;
		case 2: 
			return CLOSED;
		case 3:
			return DECLARED;
		case 4:
			return ABANDONED;
		default:
			throw new NullPointerException("No closure type exists for this int value: " + i);	
		}
	}
	
}
