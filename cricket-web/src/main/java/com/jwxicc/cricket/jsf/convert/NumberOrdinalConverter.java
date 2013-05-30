package com.jwxicc.cricket.jsf.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "ordinalConverter")
public class NumberOrdinalConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value.length() > 2) {
			return value.substring(0, value.length() - 2);
		} else {
			return value;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String numString = String.valueOf(value);
		int number = Integer.parseInt(numString);
		if (11 <= number && number <= 13) {
			return number + "th";
		} else {
			String lastNum = numString.substring(numString.length() - 1);
			int last = Integer.parseInt(lastNum);
			switch (last) {
			case 1:
				return number + "st";
			case 2:
				return number + "nd";
			case 3:
				return number + "rd";
			default:
				return number + "th";
			}
		}

	}

}
