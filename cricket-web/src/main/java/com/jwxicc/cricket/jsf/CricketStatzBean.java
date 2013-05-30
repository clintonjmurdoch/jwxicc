package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.jwxicc.cricket.entity.Competition;
import com.jwxicc.cricket.interfaces.CompetitionManager;
import com.jwxicc.cricket.parse.CricketParseDataException;
import com.jwxicc.cricket.parse.ImportedGameParser;

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
			parseBean.parseGameText(cricketStatzText, selectedCompId);
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

}