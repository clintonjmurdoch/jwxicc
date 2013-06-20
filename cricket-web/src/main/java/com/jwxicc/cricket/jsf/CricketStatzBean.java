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

	private List<String> parseLog;
	private List<Future<Game>> parseResponseList;
	
	private boolean pollEnabled;
	private boolean parseEnabled = true;

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
		try {
			this.parseLog = Collections.synchronizedList(new ArrayList<String>());
			parseLog.add("Beginning parse of Cricket Statz text, please wait...");
			this.pollEnabled = true;
			this.parseEnabled = false;
			// trim whitespace
			this.cricketStatzText = cricketStatzText.trim();
			// remove stuff from the start and end
			// also remove the first match start text

			String[] gameTextArray = CricketStatzParseUtil
					.splitCricketStatzTextToMatches(this.cricketStatzText);
			// store the async responses to parse operation
			parseResponseList = Collections.synchronizedList(new ArrayList<Future<Game>>());
			for (int z = 0; z < gameTextArray.length; z++) {
				parseResponseList.add(parseBean.parseSingleGameText(gameTextArray[z],
						selectedCompId, parseLog));
				System.out.println("Submitted job number " + z + " to game parser");
			}

			this.cricketStatzText = "";

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

	public List<String> getParseLog() {
		return parseLog;
	}

	public void pullFromLog() {
		System.out.println("pullFromLog");
		boolean allDone = true;
		for (Future<Game> response : parseResponseList) {
			if (!response.isDone()) {
				allDone = false;
				break;
			}
		}
		if (allDone) {
			this.pollEnabled = false;
			this.parseEnabled = true;
		}
	}

	public boolean isPollEnabled() {
		return pollEnabled;
	}

	public boolean isParseEnabled() {
		return parseEnabled;
	}

}