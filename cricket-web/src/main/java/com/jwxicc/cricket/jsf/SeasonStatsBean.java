package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.jwxicc.cricket.entity.Competition;
import com.jwxicc.cricket.interfaces.CompetitionManager;
import com.jwxicc.cricket.records.BattingRecord;
import com.jwxicc.cricket.records.BattingRecordsManager;
import com.jwxicc.cricket.records.BowlingRecord;
import com.jwxicc.cricket.records.BowlingRecordsManager;

@ManagedBean(name = "seasonStats")
@ViewScoped
public class SeasonStatsBean implements Serializable {

	@EJB
	CompetitionManager compManager;

	@EJB
	BattingRecordsManager battingRecordsManager;

	@EJB
	BowlingRecordsManager bowlingRecordsManager;

	private List<Competition> competitions;
	private List<BattingRecord> battingRecords;
	private List<BowlingRecord> bowlingRecords;

	private Competition selectedComp;
	
	// sorting

	@PostConstruct
	public void loadCompetitions() {
		competitions = compManager.getAll();
		// order them by season
		Collections.sort(competitions);
		Collections.reverse(competitions);
		// set the first one selected
		this.selectedComp = competitions.get(0);
	}

	public List<Competition> getCompetitions() {
		return competitions;
	}

	public void setCompetitions(List<Competition> competitions) {
		this.competitions = competitions;
	}

	public List<BattingRecord> getBattingRecords() {
		return battingRecords;
	}

	public void setBattingRecords(List<BattingRecord> battingRecords) {
		this.battingRecords = battingRecords;
	}

	public List<BowlingRecord> getBowlingRecords() {
		return bowlingRecords;
	}

	public void setBowlingRecords(List<BowlingRecord> bowlingRecords) {
		this.bowlingRecords = bowlingRecords;
	}

	public Competition getSelectedComp() {
		return selectedComp;
	}

	public void setSelectedComp(Competition selectedComp) {
		this.selectedComp = selectedComp;
	}

	public int getSelectedCompId() {
		return selectedComp.getCompetitionId();
	}

	public void setSelectedCompId(int selectedCompId) {
		for (Competition c : competitions) {
			if (c.getCompetitionId() == selectedCompId) {
				this.selectedComp = c;
				break;
			}
		}
		battingRecords = battingRecordsManager.getBySeason(selectedCompId);
		bowlingRecords = bowlingRecordsManager.getBySeason(selectedCompId);
	}
}
