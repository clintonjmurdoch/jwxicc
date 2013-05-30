package com.jwxicc.cricket.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.records.TeamRecordsManager;
import com.jwxicc.cricket.records.WinLossDrawRecord;
import com.jwxicc.cricket.util.JwxiccUtils;

@ManagedBean(name = "teamRecordsBean")
@ViewScoped
public class TeamRecordsBean implements Serializable {

	@EJB(name = "teamRecordsManager")
	TeamRecordsManager teamRecords;

	private int jwTeamId = JwxiccUtils.JWXICC_TEAM_ID;
	private List<Inning> highestFor;
	private List<Inning> lowestFor;
	private List<Inning> highestAgainst;
	private List<Inning> lowestAgainst;
	private WinLossDrawRecord overallRecord;
	private int selectedMatchToView;

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

	public List<Inning> getHighestFor() {
		if (highestFor == null) {
			highestFor = teamRecords.getHighestInningsFor();
		}
		return highestFor;
	}

	public void setHighestFor(List<Inning> highestFor) {
		this.highestFor = highestFor;
	}

	public List<Inning> getLowestFor() {
		if (lowestFor == null) {
			lowestFor = teamRecords.getLowestInningsFor();
		}
		return lowestFor;
	}

	public void setLowestFor(List<Inning> lowestFor) {
		this.lowestFor = lowestFor;
	}

	public List<Inning> getHighestAgainst() {
		if (highestAgainst == null) {
			highestAgainst = teamRecords.getHighestInningsAgainst();
		}
		return highestAgainst;
	}

	public void setHighestAgainst(List<Inning> highestAgainst) {
		this.highestAgainst = highestAgainst;
	}

	public List<Inning> getLowestAgainst() {
		if (lowestAgainst == null) {
			lowestAgainst = teamRecords.getLowestInningsAgainst();
		}
		return lowestAgainst;
	}

	public void setLowestAgainst(List<Inning> lowestAgainst) {
		this.lowestAgainst = lowestAgainst;
	}

	public WinLossDrawRecord getOverallRecord() {
		if (overallRecord == null) {
			overallRecord = teamRecords.getOverallRecord();
		}
		return overallRecord;
	}

	public void setOverallRecord(WinLossDrawRecord overallRecord) {
		this.overallRecord = overallRecord;
	}

	public int getJwTeamId() {
		return jwTeamId;
	}

	public int getSelectedMatchToView() {
		return selectedMatchToView;
	}

	public void setSelectedMatchToView(int selectedMatchToView) {
		this.selectedMatchToView = selectedMatchToView;
	}

}
