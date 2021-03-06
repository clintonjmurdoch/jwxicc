package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Set;

/**
 * The persistent class for the player database table.
 * 
 */
@Entity
@Table(name = "player")
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false)
	private int playerId;

	@Column(length = 45)
	private String playerName;
	
	@Column(length = 45)
	private String scorecardName;

	@Column(nullable = false)
	private boolean status;

	// bi-directional many-to-one association to Batting
	@OneToMany(mappedBy = "player")
	private Set<Batting> battings;

	// bi-directional many-to-one association to Bowling
	@OneToMany(mappedBy = "player")
	private Set<Bowling> bowlings;

	@Transient
	private boolean captain;

	// bi-directional many-to-one association to PartnershipPlayer
	@OneToMany(mappedBy = "player")
	private Set<PartnershipPlayer> partnershipPlayers;

	@Transient
	private boolean keeper;

	// bi-directional many-to-one association to GamePlayerDesignation
	@OneToMany(mappedBy = "player")
	private Set<GamePlayerDesignation> gameplayerdesignations;

	@OneToMany(mappedBy = "owningPlayer")
	private Set<PlayerRelationship> relationships;

	@OneToMany(mappedBy = "otherPlayer")
	private Set<PlayerRelationship> passiveRelationshipInstances;

	// bi-directional many-to-one association to Team
	@ManyToOne
	@JoinColumn(name = "teamId", nullable = false)
	private Team team;

	// bi-directional many-to-one association to WicketDetail
	@OneToMany(mappedBy = "player")
	private Set<WicketDetail> wicketDetails;

	// bi-directional one-to-one association to PlayerDetail
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "playerDetailId")
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

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
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

	public Set<GamePlayerDesignation> getGameplayerdesignations() {
		return this.gameplayerdesignations;
	}

	public void setGameplayerdesignations(Set<GamePlayerDesignation> gameplayerdesignations) {
		this.gameplayerdesignations = gameplayerdesignations;
	}

	public Set<PartnershipPlayer> getPartnershipPlayers() {
		return partnershipPlayers;
	}

	public void setPartnershipPlayers(Set<PartnershipPlayer> partnershipPlayers) {
		this.partnershipPlayers = partnershipPlayers;
	}

	public Set<PlayerRelationship> getRelationships() {
		return relationships;
	}

	public void setRelationships(Set<PlayerRelationship> relationships) {
		this.relationships = relationships;
	}

	public Set<PlayerRelationship> getPassiveRelationshipInstances() {
		return passiveRelationshipInstances;
	}

	public void setPassiveRelationshipInstances(Set<PlayerRelationship> passiveRelationshipInstances) {
		this.passiveRelationshipInstances = passiveRelationshipInstances;
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