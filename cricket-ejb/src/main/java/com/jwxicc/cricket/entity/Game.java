package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * The persistent class for the game database table.
 * 
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int gameId;

	@Lob
	private String comment;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date date;

	@Column(length = 20)
	private String gameState;

	@Column(nullable = false, length = 3)
	private String round;

	private int winMargin;

	// bi-directional many-to-one association to Competition
	@ManyToOne
	@JoinColumn(name = "competitionId", nullable = false)
	private Competition competition;

	// bi-directional many-to-one association to Ground
	@ManyToOne
	@JoinColumn(name = "groundId", nullable = false)
	private Ground ground;

	// bi-directional one-to-one association to Review
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "reviewId")
	private Review review;

	// bi-directional many-to-one association to Team
	@ManyToOne
	@JoinColumn(name = "awayTeamId", nullable = false)
	private Team awayTeam;

	// bi-directional many-to-one association to Team
	@ManyToOne
	@JoinColumn(name = "homeTeamId", nullable = false)
	private Team homeTeam;

	// bi-directional many-to-one association to Team
	@ManyToOne
	@JoinColumn(name = "toss")
	private Team toss;

	// bi-directional many-to-one association to Team
	@ManyToOne
	@JoinColumn(name = "winner")
	private Team winner;

	// bi-directional many-to-one association to WinType
	@ManyToOne
	@JoinColumn(name = "winTypeId")
	private WinType winType;

	// bi-directional many-to-one association to GamePlayerDesignation
	@OneToMany(mappedBy = "game")
	private Set<GamePlayerDesignation> gameplayerdesignations;

	// bi-directional many-to-one association to Inning
	@OneToMany(mappedBy = "game")
	private Set<Inning> innings;

	public Game() {
	}

	public int getGameId() {
		return this.gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getGameState() {
		return this.gameState;
	}

	public void setGameState(String gameState) {
		this.gameState = gameState;
	}

	public String getRound() {
		return this.round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public Team getToss() {
		return toss;
	}

	public void setToss(Team toss) {
		this.toss = toss;
	}

	public int getWinMargin() {
		return this.winMargin;
	}

	public void setWinMargin(int winMargin) {
		this.winMargin = winMargin;
	}

	public Team getWinner() {
		return winner;
	}

	public void setWinner(Team winner) {
		this.winner = winner;
	}

	public Competition getCompetition() {
		return this.competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public Ground getGround() {
		return this.ground;
	}

	public void setGround(Ground ground) {
		this.ground = ground;
	}

	public Review getReview() {
		return this.review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public Team getAwayTeam() {
		return this.awayTeam;
	}

	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}

	public Team getHomeTeam() {
		return this.homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public WinType getWinType() {
		return this.winType;
	}

	public void setWinType(WinType winType) {
		this.winType = winType;
	}

	public Set<GamePlayerDesignation> getGameplayerdesignations() {
		return this.gameplayerdesignations;
	}

	public void setGameplayerdesignations(Set<GamePlayerDesignation> gameplayerdesignations) {
		this.gameplayerdesignations = gameplayerdesignations;
	}

	public Set<Inning> getInnings() {
		return this.innings;
	}

	public void setInnings(Set<Inning> innings) {
		this.innings = innings;
	}

}