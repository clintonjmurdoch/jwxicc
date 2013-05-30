package com.jwxicc.cricket.jsf.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.collections.CollectionUtils;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.WicketDetail;
import com.jwxicc.cricket.entity.dbenum.WicketDetailType;

@FacesConverter(value = "howoutDisplay")
public class HowoutDisplayConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// This will not be used, as it is for display only
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// TODO Auto-generated method stub
		// The object is a batting object
		// Display the required information in the howout column

		Batting batting = (Batting) value;

		int howOutid = batting.getHowout().getHowOutid();
		List<WicketDetail> assistList = null;
		Map<WicketDetailType, WicketDetail> assisters = null;
		boolean hasAssists = false;

		String toReturn = null; // required by some wickets

		// if there are wicket details, save them to a map indexed by the detail type
		if (CollectionUtils.isNotEmpty(batting.getWicketDetails())) {
			assistList = new ArrayList<WicketDetail>(batting.getWicketDetails());
			hasAssists = true;
			assisters = new HashMap<WicketDetailType, WicketDetail>(2);
			for (WicketDetail detail : assistList) {
				assisters.put(detail.getWicketType(), detail);
			}
		}

		switch (howOutid) {
		// show nothing for dnb
		case 0:
			return null;
			// wickets to just a bowler
		case 2:
		case 5:
		case 6:
			return batting.getHowout().getDismissalShort() + " "
					+ assisters.get(WicketDetailType.B).getPlayer().getScorecardName();
			// run outs have to be done differently
		case 8:
			toReturn = batting.getHowout().getDismissalType();
			if (hasAssists) {
				if (assistList.size() == 1) {
					toReturn += " ("
							+ assisters.get(WicketDetailType.RO_1).getPlayer().getScorecardName()
							+ ")";
				} else if (assistList.size() == 2) {
					toReturn += " ("
							+ assisters.get(WicketDetailType.RO_1).getPlayer().getScorecardName()
							+ "/"
							+ assisters.get(WicketDetailType.RO_2).getPlayer().getScorecardName()
							+ ")";
				}
			}
			return toReturn;
			// wickets to a fielder and bowler
		case 3:
			toReturn = "c ";

		case 18:
			if (toReturn == null) {
				toReturn = "c †";
			}
		case 9:
			if (toReturn == null) {
				toReturn = "st †";
			}
			if (assisters.get(WicketDetailType.CT) != null) {
				toReturn += assisters.get(WicketDetailType.CT).getPlayer().getScorecardName() + " ";
			} else if (assisters.get(WicketDetailType.ST) != null) {
				toReturn += assisters.get(WicketDetailType.ST).getPlayer().getScorecardName() + " ";
			} else {
				toReturn += "? ";
			}
			toReturn += "b " + assisters.get(WicketDetailType.B).getPlayer().getScorecardName();
			return toReturn;
			// caught and bowled
		case 4:
			return "c&b " + assistList.get(0).getPlayer().getScorecardName();
			// wickets to nobody
		case 1:
		case 7:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
			return batting.getHowout().getDismissalType();
		}

		return batting.getHowout().getDismissalType();
	}

}
