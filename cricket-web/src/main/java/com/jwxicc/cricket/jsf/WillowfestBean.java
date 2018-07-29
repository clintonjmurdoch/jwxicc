package com.jwxicc.cricket.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.jwxicc.cricket.entity.Competition;
import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.interfaces.CompetitionManager;

@ManagedBean(name = "WFBean")
@ViewScoped
public class WillowfestBean implements Serializable {

	@EJB
	CompetitionManager compManager;

	private Competition comp;
	private int selectedMatchToView;

	public Competition getComp() {

		if (this.comp == null) {
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance()
					.getExternalContext().getRequest();
			String requestURI = req.getRequestURI();

			// get the season
			String tempVal;
			String season = null;
			String comp = null;
			int lastSlash = StringUtils.lastIndexOf(requestURI, '/');
			tempVal = requestURI.substring(lastSlash + 1);
			// it is a filename, remove it and set the last slash again
			if (tempVal.contains(".")) {
				requestURI = requestURI.substring(0, lastSlash);
				lastSlash = StringUtils.lastIndexOf(requestURI, '/');
			}

			// next is the season then remove it
			season = requestURI.substring(lastSlash + 1);
			requestURI = requestURI.substring(0, lastSlash);
			// reset slash
			lastSlash = StringUtils.lastIndexOf(requestURI, '/');

			// next is the comp
			comp = requestURI.substring(lastSlash + 1);

			System.out.println(season);
			System.out.println(comp);

			this.comp = compManager.getCompetitionByAssociationAndSeason(comp, season);
		}
		return comp;
	}

	public String goToMatch() {
		System.out.println("goToMatch");
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context.redirect(context.getRequestContextPath() + "/match.xhtml?matchId="
					+ this.selectedMatchToView);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setComp(Competition comp) {
		this.comp = comp;
	}

	private String getValueFromURIAndRemove(String reqURI) {
		int lastSlash = StringUtils.lastIndexOf(reqURI, '/');
		String val = reqURI.substring(lastSlash + 1);
		reqURI = reqURI.substring(0, lastSlash);
		return val;
	}

	public int getSelectedMatchToView() {
		return selectedMatchToView;
	}

	public void setSelectedMatchToView(int selectedMatchToView) {
		System.out.println("setSelectedMatchToView: " + selectedMatchToView);
		this.selectedMatchToView = selectedMatchToView;
	}
	
	public ArrayList<Game> getGames() {
		return new ArrayList<Game>(getComp().getGames());
	}

}