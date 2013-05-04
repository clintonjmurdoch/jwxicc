package com.jwxicc.cricket.records;

public class WinLossDrawRecord {

	private int matches;
	private int won;
	private int lost;
	private int tied;
	private int noResult;
	
	public WinLossDrawRecord(int matches, int won, int lost, int tied,
			int noResult) {
		super();
		this.matches = matches;
		this.won = won;
		this.lost = lost;
		this.tied = tied;
		this.noResult = noResult;
	}

	public int getMatches() {
		return matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}

	public int getWon() {
		return won;
	}

	public void setWon(int won) {
		this.won = won;
	}

	public int getLost() {
		return lost;
	}

	public void setLost(int lost) {
		this.lost = lost;
	}

	public int getTied() {
		return tied;
	}

	public void setTied(int tied) {
		this.tied = tied;
	}

	public int getNoResult() {
		return noResult;
	}

	public void setNoResult(int noResult) {
		this.noResult = noResult;
	}
}
