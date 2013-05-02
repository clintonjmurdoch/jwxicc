package com.jwxicc.cricket.jsf.exception;

import javax.faces.context.ExceptionHandlerFactory;

public class FacesExceptionHandlerFactory extends ExceptionHandlerFactory {

	private final ExceptionHandlerFactory parent;

	public FacesExceptionHandlerFactory(
			final ExceptionHandlerFactory parent) {
		this.parent = parent;
	}

	@Override
	public FacesExceptionHandler getExceptionHandler() {
		return new FacesExceptionHandler(this.parent.getExceptionHandler());
	}

}
