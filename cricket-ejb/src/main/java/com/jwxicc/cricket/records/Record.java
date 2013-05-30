package com.jwxicc.cricket.records;

import java.math.BigDecimal;

import com.jwxicc.cricket.entity.Player;

/**
 * Base of the record return objects for different record types, as they do not always return db
 * objects due to averages, strike rates, etc
 * 
 * @author cmurdoch
 * 
 */
public class Record {

	protected Player player;
	protected int aggregate;
	protected BigDecimal average;
	protected BigDecimal strikeRate;
	protected int matchesPlayed;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getAggregate() {
		return aggregate;
	}

	public void setAggregate(int aggregate) {
		this.aggregate = aggregate;
	}

	public BigDecimal getAverage() {
		return average;
	}

	public void setAverage(BigDecimal average) {
		this.average = average;
	}

	public BigDecimal getStrikeRate() {
		return strikeRate;
	}

	public void setStrikeRate(BigDecimal strikeRate) {
		this.strikeRate = strikeRate;
	}

	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}
}
