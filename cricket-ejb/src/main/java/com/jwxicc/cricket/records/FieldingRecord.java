package com.jwxicc.cricket.records;

import com.jwxicc.cricket.entity.Player;

public class FieldingRecord {

	private Player player;
	private int matches;
	private int catches;
	private int wkCatches;
	private int stumpings;
	private int dismissals;
	private int runouts;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getMatches() {
		return matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}

	public int getCatches() {
		return catches;
	}

	public void setCatches(int catches) {
		this.catches = catches;
	}

	public int getWkCatches() {
		return wkCatches;
	}

	public void setWkCatches(int wkCatches) {
		this.wkCatches = wkCatches;
	}

	public int getStumpings() {
		return stumpings;
	}

	public void setStumpings(int stumpings) {
		this.stumpings = stumpings;
	}

	public int getDismissals() {
		return dismissals;
	}

	public void setDismissals(int dismissals) {
		this.dismissals = dismissals;
	}

	public int getRunouts() {
		return runouts;
	}

	public void setRunouts(int runouts) {
		this.runouts = runouts;
	}

}
