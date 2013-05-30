package com.jwxicc.cricket.jsf.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.dbenum.InningsClosureType;

@FacesConverter(value = "wicketDisplay")
public class WicketDisplayConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String display = "";
		Inning i = (Inning) value;
		InningsClosureType closureType = i.getClosureType();

		switch (closureType) {
		case ALLOUT:
			display += "(all out; ";
			if (i.getWicketsLost() < 10) {
				display += i.getWicketsLost() + " wickets; ";
			}
			return display;
		case ABANDONED:
			display += "(abandoned; ";
			display += i.getWicketsLost() + " wickets; ";
			return display;
		case CLOSED:
			display += "(" + i.getWicketsLost() + " wickets; ";
			return display;
		case DECLARED:
			display += "(" + i.getWicketsLost() + " wickets dec; ";
			return display;
		}

		return display;
	}

}
