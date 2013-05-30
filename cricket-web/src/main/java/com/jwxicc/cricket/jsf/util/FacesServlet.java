package com.jwxicc.cricket.jsf.util;

import java.io.IOException;

import javax.faces.application.ViewExpiredException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class FacesServlet implements Servlet {

	public final static String FACES_SERVLET_PARAM = "facesClass";
	public final static String FACES_SERVLET_DEFAULT_CLASS = "javax.faces.webapp.FacesServlet";

	private Servlet delegate;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		String facesClass = servletConfig.getInitParameter(FACES_SERVLET_PARAM);
		if (facesClass == null) {
			facesClass = FACES_SERVLET_DEFAULT_CLASS;
		}

		try {
			delegate = (Servlet) Class.forName(facesClass).newInstance();
			delegate.init(servletConfig);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public String getServletInfo() {
		return delegate.getServletInfo();
	}

	@Override
	public ServletConfig getServletConfig() {
		return delegate.getServletConfig();
	}

	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException,
			IOException, ViewExpiredException {
		try {
			delegate.service(req, resp);
		} catch (ServletException e) {
			Throwable t = e.getRootCause();
			if (t instanceof ViewExpiredException) {
				throw (ViewExpiredException) t;
			} else if (t instanceof IllegalStateException) {
				throw (IllegalStateException) t;
			} else {
				throw e;
			}
		}
	}

	@Override
	public void destroy() {
		delegate.destroy();
	}
}