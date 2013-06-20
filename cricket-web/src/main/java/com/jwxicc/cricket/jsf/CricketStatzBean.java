package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.jwxicc.cricket.entity.Competition;
import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.interfaces.CompetitionManager;
import com.jwxicc.cricket.parse.CricketParseDataException;
import com.jwxicc.cricket.parse.ImportedGameParser;
import com.jwxicc.cricket.util.parse.CricketStatzParseUtil;

@ManagedBean(name = "parseBean")
@ViewScoped
public class CricketStatzBean implements Serializable {
	private static final long serialVersionUID = 4743172469642937769L;

	@EJB(name = "cs9Parser")
	ImportedGameParser parseBean;

	@EJB
	CompetitionManager compsBean;

	private String cricketStatzText;
	private int selectedCompId;

	private List<Future<Game>> parseResponseList;

	public List<SelectItem> getCompetitionsSelectItems() {
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		List<Competition> compList = compsBean.findAll();
		for (Competition comp : compList) {
			SelectItem selectItem = new SelectItem(comp.getCompetitionId(), comp.getCompetitionId()
					+ ": " + comp.getAssociationName() + " " + comp.getGrade() + " "
					+ comp.getSeason());
			selectItems.add(selectItem);
		}
		return selectItems;
	}

	public String parseText() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Parse Operation requested, selected comp is " + selectedCompId));
		try {
			// trim whitespace
			this.cricketStatzText = cricketStatzText.trim();
			// remove stuff from the start and end
			// also remove the first match start text
			
			String[] gameTextArray = CricketStatzParseUtil.splitCricketStatzTextToMatches(this.cricketStatzText);
			// store the async responses to parse operation
			parseResponseList = Collections.synchronizedList(new ArrayList<Future<Game>>());
			for (int z = 0; z < gameTextArray.length; z++) {
				parseResponseList.add(parseBean.parseSingleGameText(gameTextArray[z],
						selectedCompId));
				System.out.println("Submitted job number " + z + " to game parser");
			}
			
			this.cricketStatzText = "Submitted " + gameTextArray.length + " games to be imported";

		} catch (CricketParseDataException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			e.printStackTrace();
		}

		return "";
	}

	public String getCricketStatzText() {
		return cricketStatzText;
	}

	public void setCricketStatzText(String cricketStatzText) {
		this.cricketStatzText = cricketStatzText;
	}

	public int getSelectedCompId() {
		return selectedCompId;
	}

	public void setSelectedCompId(int selectedCompId) {
		this.selectedCompId = selectedCompId;
	}

	public List<Future<Game>> getParseResponseList() {
		return parseResponseList;
	}

	public void setParseResponseList(List<Future<Game>> parseResponseList) {
		this.parseResponseList = parseResponseList;
	}

}