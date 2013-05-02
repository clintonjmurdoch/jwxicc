package com.jwxicc.cricket.jsf.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "IdConverter")
public class IdConverter implements Converter {

	public IdConverter() {

	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		try {
			if (arg2 == null || Integer.parseInt(arg2) == 0) {
				return null;
			} else {
				return arg2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//Most likely this is a parse exception
			//Therefore, if not numeric, send 0
			return 0;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		if (Integer.parseInt(arg2.toString()) == 0) {
			return null;
		} else {
			return arg2.toString();
		}
	}

}
