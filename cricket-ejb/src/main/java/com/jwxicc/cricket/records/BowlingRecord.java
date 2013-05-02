package com.jwxicc.cricket.records;

import java.math.BigDecimal;

public class BowlingRecord extends Record {

	private BigDecimal overs;
	private int maidens;
	private int runsConceded;
	private int wickets;
	private int bestBowlingWickets;
	private int bestBowlingRuns;
	private int fiveWktInns;
	private BigDecimal economy;

	public BowlingRecord() {

	}

	public BigDecimal getOvers() {
		return overs;
	}

	public void setOvers(BigDecimal overs) {
		this.overs = overs;
	}

	public int getMaidens() {
		return maidens;
	}

	public void setMaidens(int maidens) {
		this.maidens = maidens;
	}

	public int getRunsConceded() {
		return runsConceded;
	}

	public void setRunsConceded(int runsConceded) {
		this.runsConceded = runsConceded;
	}

	public int getWickets() {
		return wickets;
	}

	public void setWickets(int wickets) {
		this.wickets = wickets;
	}

	public int getBestBowlingWickets() {
		return bestBowlingWickets;
	}

	public void setBestBowlingWickets(int bestBowlingWickets) {
		this.bestBowlingWickets = bestBowlingWickets;
	}

	public int getBestBowlingRuns() {
		return bestBowlingRuns;
	}

	public void setBestBowlingRuns(int bestBowlingRuns) {
		this.bestBowlingRuns = bestBowlingRuns;
	}

	public int getFiveWktInns() {
		return fiveWktInns;
	}

	public void setFiveWktInns(int fiveWktInns) {
		this.fiveWktInns = fiveWktInns;
	}

	public BigDecimal getEconomy() {
		return economy;
	}

	public void setEconomy(BigDecimal economy) {
		this.economy = economy;
	}

}
