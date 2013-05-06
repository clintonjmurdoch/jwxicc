package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Set;


/**
 * The persistent class for the player database table.
 * 
 */
@Entity
@Table(name="PLAYER")
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int playerId;

	@Column(length=45)
	private String scorecardName;

	@Column(nullable=false)
	private boolean status;

	//bi-directional many-to-one association to Batting
	@OneToMany(mappedBy="player")
	private Set<Batting> battings;

	//bi-directional many-to-one association to Bowling
	@OneToMany(mappedBy="player")
	private Set<Bowling> bowlings;
	
	@Transient
	private boolean captain;

	//bi-directional many-to-one association to FowWicket
	@OneToMany(mappedBy="player")
	private Set<FowWicket> fowWickets;

	@Transient
	private boolean keeper;
	
	//bi-directional many-to-one association to GamePlayerDesignation
	@OneToMany(mappedBy="player")
	private Set<GamePlayerDesignation> gameplayerdesignations;

	//bi-directional many-to-one association to Team
	@ManyToOne
	@JoinColumn(name="teamId", nullable=false)
	private Team team;

	//bi-directional many-to-one association to WicketDetail
	@OneToMany(mappedBy="player")
	private Set<WicketDetail> wicketDetails;

	//bi-directional one-to-one association to PlayerDetail
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="playerDetailId")
	//@JoinColumn(name="playerId", referencedColumnName="playerId", nullable=false, insertable=false, updatable=false)
	private PlayerDetail playerDetail;
	
	@Transient
	private boolean editable;

	public Player() {
	}

	public int getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getScorecardName() {
		return this.scorecardName;
	}

	public void setScorecardName(String scorecardName) {
		this.scorecardName = scorecardName;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Set<Batting> getBattings() {
		return this.battings;
	}

	public void setBattings(Set<Batting> battings) {
		this.battings = battings;
	}

	public Set<Bowling> getBowlings() {
		return this.bowlings;
	}

	public void setBowlings(Set<Bowling> bowlings) {
		this.bowlings = bowlings;
	}

	public Set<FowWicket> getFowWickets() {
		return this.fowWickets;
	}

	public void setFowWickets(Set<FowWicket> fowWickets) {
		this.fowWickets = fowWickets;
	}

	public Set<GamePlayerDesignation> getGameplayerdesignations() {
		return this.gameplayerdesignations;
	}

	public void setGameplayerdesignations(Set<GamePlayerDesignation> gameplayerdesignations) {
		this.gameplayerdesignations = gameplayerdesignations;
	}

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Set<WicketDetail> getWicketDetails() {
		return this.wicketDetails;
	}

	public void setWicketDetails(Set<WicketDetail> wicketDetails) {
		this.wicketDetails = wicketDetails;
	}

	public PlayerDetail getPlayerDetail() {
		return this.playerDetail;
	}

	public void setPlayerDetail(PlayerDetail playerDetail) {
		this.playerDetail = playerDetail;
	}

	public boolean isCaptain() {
		return captain;
	}

	public void setCaptain(boolean captain) {
		this.captain = captain;
	}

	public boolean isKeeper() {
		return keeper;
	}

	public void setKeeper(boolean keeper) {
		this.keeper = keeper;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

}