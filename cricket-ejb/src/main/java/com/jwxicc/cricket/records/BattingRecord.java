package com.jwxicc.cricket.records;

public class BattingRecord extends Record {

	private int innings;
	private int notOuts;
	private int bestBattingScore;
	private int bestBattingBalls;
	private boolean bestBattingOutStatus;
	private int ballsFaced;
	private int fifties;
	private int hundreds;

	public BattingRecord() {
	}

	public int getInnings() {
		return innings;
	}

	public void setInnings(int innings) {
		this.innings = innings;
	}

	public int getNotOuts() {
		return notOuts;
	}

	public void setNotOuts(int notOuts) {
		this.notOuts = notOuts;
	}

	public int getBestBattingScore() {
		return bestBattingScore;
	}

	public void setBestBattingScore(int bestBattingScore) {
		this.bestBattingScore = bestBattingScore;
	}

	public int getBestBattingBalls() {
		return bestBattingBalls;
	}

	public void setBestBattingBalls(int bestBattingBalls) {
		this.bestBattingBalls = bestBattingBalls;
	}

	public boolean isBestBattingOutStatus() {
		return bestBattingOutStatus;
	}

	public void setBestBattingOutStatus(boolean bestBattingOutStatus) {
		this.bestBattingOutStatus = bestBattingOutStatus;
	}

	public int getBallsFaced() {
		return ballsFaced;
	}

	public void setBallsFaced(int ballsFaced) {
		this.ballsFaced = ballsFaced;
	}

	public int getFifties() {
		return fifties;
	}

	public void setFifties(int fifties) {
		this.fifties = fifties;
	}

	public int getHundreds() {
		return hundreds;
	}

	public void setHundreds(int hundreds) {
		this.hundreds = hundreds;
	}
}
