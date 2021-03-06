package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.jwxicc.cricket.entity.dbenum.InningsClosureType;
import com.jwxicc.cricket.entityutils.EntityMethods;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The persistent class for the innings database table.
 * 
 */
@Entity
@Table(name = "innings")
public class Inning implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int inningsId;

	private int byes;

	@Column(length = 12)
	@Enumerated(EnumType.STRING)
	private InningsClosureType closureType;

	private int inningsOfMatch;

	@Column(name = "leg_byes")
	private int legByes;

	@Column(name = "no_balls")
	private int noBalls;

	@Column(precision = 10, scale = 1)
	private BigDecimal oversFaced;

	private int penalties;

	private int runsScored;

	private int wicketsLost;

	private int wides;

	// bi-directional many-to-one association to Batting
	@OneToMany(mappedBy = "inning")
	@OrderBy("battingPos ASC")
	private Set<Batting> battings;

	// bi-directional many-to-one association to Bowling
	@OneToMany(mappedBy = "inning")
	@OrderBy("bowlingPos ASC")
	private Set<Bowling> bowlings;

	// bi-directional many-to-one association to Partnership
	@OneToMany(mappedBy = "inning")
	private Set<Partnership> partnerships;

	// bi-directional many-to-one association to Game
	@ManyToOne
	@JoinColumn(name = "gameId", nullable = false)
	private Game game;

	// bi-directional many-to-one association to Team
	@ManyToOne
	@JoinColumn(name = "teamId", nullable = false)
	private Team team;

	public Inning() {
	}

	public int getInningsId() {
		return this.inningsId;
	}

	public void setInningsId(int inningsId) {
		this.inningsId = inningsId;
	}

	public int getByes() {
		return this.byes;
	}

	public void setByes(int byes) {
		this.byes = byes;
	}

	public InningsClosureType getClosureType() {
		return closureType;
	}

	public void setClosureType(InningsClosureType closureType) {
		this.closureType = closureType;
	}

	public int getInningsOfMatch() {
		return this.inningsOfMatch;
	}

	public void setInningsOfMatch(int inningsOfMatch) {
		this.inningsOfMatch = inningsOfMatch;
	}

	public int getLegByes() {
		return this.legByes;
	}

	public void setLegByes(int legByes) {
		this.legByes = legByes;
	}

	public int getNoBalls() {
		return this.noBalls;
	}

	public void setNoBalls(int noBalls) {
		this.noBalls = noBalls;
	}

	public BigDecimal getOversFaced() {
		return this.oversFaced;
	}

	public void setOversFaced(BigDecimal oversFaced) {
		this.oversFaced = oversFaced;
	}

	public Set<Partnership> getPartnerships() {
		return partnerships;
	}

	public void setPartnerships(Set<Partnership> partnerships) {
		this.partnerships = partnerships;
	}

	public int getPenalties() {
		return this.penalties;
	}

	public void setPenalties(int penalties) {
		this.penalties = penalties;
	}

	public int getRunsScored() {
		return this.runsScored;
	}

	public void setRunsScored(int runsScored) {
		this.runsScored = runsScored;
	}
	
	public BigDecimal getRunRate() {
		return EntityMethods.calculateEconomy(runsScored, oversFaced);
	}

	public int getWicketsLost() {
		return this.wicketsLost;
	}

	public void setWicketsLost(int wicketsLost) {
		this.wicketsLost = wicketsLost;
	}

	public int getWides() {
		return this.wides;
	}

	public void setWides(int wides) {
		this.wides = wides;
	}

	public Set<Batting> getBattings() {
		return this.battings;
	}

	public void setBattings(Set<Batting> battings) {
		this.battings = battings;
	}
	
	public List<Batting> getBattingsExDnb() {
		ArrayList<Batting> battingList = new ArrayList<Batting>();
		for (Batting b : getBattings()) {
			if (b.getHowout() != null && b.getHowout().getHowOutid() != 0) {
				battingList.add(b);
			}
		}
		return battingList;
	}
	
	public List<Batting> getDnbBattings() {
		ArrayList<Batting> battingList = new ArrayList<Batting>();
		for (Batting b : getBattings()) {
			if (b.getHowout() != null && b.getHowout().getHowOutid() == 0) {
				battingList.add(b);
			}
		}
		return battingList;
	}

	public Set<Bowling> getBowlings() {
		return this.bowlings;
	}

	public void setBowlings(Set<Bowling> bowlings) {
		this.bowlings = bowlings;
	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}