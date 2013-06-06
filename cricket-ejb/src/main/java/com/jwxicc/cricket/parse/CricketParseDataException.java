package com.jwxicc.cricket.parse;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CricketParseDataException extends Exception {
	private static final long serialVersionUID = 2383149673096044954L;

	public CricketParseDataException(String message, Exception cause) {
		super(message, cause);
	}

}