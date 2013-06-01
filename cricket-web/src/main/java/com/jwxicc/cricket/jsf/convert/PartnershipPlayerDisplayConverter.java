package com.jwxicc.cricket.jsf.convert;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.jwxicc.cricket.entity.PartnershipPlayer;

@FacesConverter(value = "partnershipPlayerConverter")
public class PartnershipPlayerDisplayConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String display;
		PartnershipPlayer player = (PartnershipPlayer) value;
		
		display = player.getPlayer().getScorecardName();
		if (!player.isOutStatus())
			display += "*";
		if (player.getContribution() != 0)
			display += " (" + player.getContribution() + ")";

		return display;
	}

}
