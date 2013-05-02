package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.jwxicc.cricket.entityutils.EntityMethods;

import java.math.BigDecimal;


/**
 * The persistent class for the bowling database table.
 * 
 */
@Entity
@Table(name="BOWLING")
public class Bowling implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int bowlingId;

	private int bowlingPos;
	
	@Transient
	private BigDecimal economy;

	private int maidens;

	private int noBalls;

	@Column(precision=10, scale=1)
	private BigDecimal overs;

	private int runs;

	private int wickets;

	private int wides;

	//bi-directional many-to-one association to Inning
	@ManyToOne
	@JoinColumn(name="inningsId", nullable=false)
	private Inning inning;

	//bi-directional many-to-one association to Player
	@ManyToOne
	@JoinColumn(name="playerId", nullable=false)
	private Player player;

	public Bowling() {
	}

	public int getBowlingId() {
		return this.bowlingId;
	}

	public void setBowlingId(int bowlingId) {
		this.bowlingId = bowlingId;
	}

	public int getBowlingPos() {
		return this.bowlingPos;
	}

	public void setBowlingPos(int bowlingPos) {
		this.bowlingPos = bowlingPos;
	}

	public BigDecimal getEconomy() {
		this.economy = EntityMethods.calculateEconomy(runs, overs);
		return economy;
	}

	public void setEconomy(BigDecimal economy) {
		this.economy = economy;
	}

	public int getMaidens() {
		return this.maidens;
	}

	public void setMaidens(int maidens) {
		this.maidens = maidens;
	}

	public int getNoBalls() {
		return this.noBalls;
	}

	public void setNoBalls(int noBalls) {
		this.noBalls = noBalls;
	}

	public BigDecimal getOvers() {
		return this.overs;
	}

	public void setOvers(BigDecimal overs) {
		this.overs = overs;
	}

	public int getRuns() {
		return this.runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getWickets() {
		return this.wickets;
	}

	public void setWickets(int wickets) {
		this.wickets = wickets;
	}

	public int getWides() {
		return this.wides;
	}

	public void setWides(int wides) {
		this.wides = wides;
	}

	public Inning getInning() {
		return this.inning;
	}

	public void setInning(Inning inning) {
		this.inning = inning;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}