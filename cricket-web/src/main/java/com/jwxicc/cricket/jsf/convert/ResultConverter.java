package com.jwxicc.cricket.jsf.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.jwxicc.cricket.entity.Game;

@FacesConverter(value = "resultConverter")
public class ResultConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Game game = (Game) value;
		if (game.getWinner() == null) {
			return game.getWinType().getWinTypeName();
		}

		String returnString = null;
		if (game.getWinType() != null && game.getWinner() != null) {
		returnString = game.getWinner().getTeamName() + " won by " + game.getWinMargin()
				+ " ";
		if (game.getWinMargin() == 1) {
			returnString += game.getWinType().getWinTypeName().toLowerCase().replace("runs", "run")
					.replace("wickets", "wicket");
		} else {
			returnString += game.getWinType().getWinTypeName().toLowerCase();
		}
		}
		
		return returnString;
	}
}
