package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the batting database table.
 * 
 */
@Entity
@Table(name = "BATTING")
public class Batting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int battingId;

	private int balls;

	private int battingPos;

	private int fours;

	private int score;

	private int sixes;

	// bi-directional many-to-one association to Howout
	@ManyToOne
	@JoinColumn(name = "howOutId")
	private Howout howout;

	// bi-directional many-to-one association to Inning
	@ManyToOne
	@JoinColumn(name = "inningsId", nullable = false)
	private Inning inning;

	// bi-directional many-to-one association to Player
	@ManyToOne
	@JoinColumn(name = "playerId", nullable = false)
	private Player player;

	// bi-directional many-to-one association to WicketDetail
	@OneToMany(mappedBy = "batting")
	private Set<WicketDetail> wicketDetails;

	public Batting() {
	}

	public int getBattingId() {
		return this.battingId;
	}

	public void setBattingId(int battingId) {
		this.battingId = battingId;
	}

	public int getBalls() {
		return this.balls;
	}

	public void setBalls(int balls) {
		this.balls = balls;
	}

	public int getBattingPos() {
		return this.battingPos;
	}

	public void setBattingPos(int battingPos) {
		this.battingPos = battingPos;
	}

	public int getFours() {
		return this.fours;
	}

	public void setFours(int fours) {
		this.fours = fours;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getSixes() {
		return this.sixes;
	}

	public void setSixes(int sixes) {
		this.sixes = sixes;
	}

	public Howout getHowout() {
		return this.howout;
	}

	public void setHowout(Howout howout) {
		this.howout = howout;
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

	public Set<WicketDetail> getWicketDetails() {
		return this.wicketDetails;
	}

	public void setWicketDetails(Set<WicketDetail> wicketDetails) {
		this.wicketDetails = wicketDetails;
	}

}