package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.jwxicc.cricket.records.IndividualRecordsManager;
import com.jwxicc.cricket.records.Record;

@ManagedBean(name="indivRecordsBean")
@ViewScoped
public class IndivRecordsBean implements Serializable {
	
	@EJB(name="indivRecordsManager")
	IndividualRecordsManager indivRecords;
	
	private List<Record> mostMatches;
	private List<Record> mostMOTMs;
	private List<Record> mostCaptain;

	public List<Record> getMostMatches() {
		if (mostMatches == null) {
			mostMatches = indivRecords.getMostMatches();
		}
		return mostMatches;
	}

	public void setMostMatches(List<Record> mostMatches) {
		this.mostMatches = mostMatches;
	}

	public List<Record> getMostMOTMs() {
		if (mostMOTMs == null) {
			mostMOTMs = indivRecords.getMostMOTMs();
		}
		return mostMOTMs;
	}

	public void setMostMOTMs(List<Record> mostMOTMs) {
		this.mostMOTMs = mostMOTMs;
	}

	public List<Record> getMostCaptain() {
		if (mostCaptain == null) {
			mostCaptain = indivRecords.getMostMatchesAsCaptain();
		}
		return mostCaptain;
	}

	public void setMostCaptain(List<Record> mostCaptain) {
		this.mostCaptain = mostCaptain;
	}

}
