package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the team database table.
 * 
 */
@Entity
@Table(name = "team")
public class Team implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false)
	private int teamId;

	@Column(nullable = false, length = 45)
	private String teamName;

	// bi-directional many-to-one association to Game
	@OneToMany(mappedBy = "awayTeam")
	private Set<Game> games1;

	// bi-directional many-to-one association to Game
	@OneToMany(mappedBy = "homeTeam")
	private Set<Game> games2;

	// bi-directional many-to-one association to Game
	@OneToMany(mappedBy = "winner")
	private Set<Game> winningGames;

	// bi-directional many-to-one association to Game
	@OneToMany(mappedBy = "toss")
	private Set<Game> winningTosses;

	// bi-directional many-to-one association to Inning
	@OneToMany(mappedBy = "team")
	private Set<Inning> innings;

	// bi-directional many-to-one association to Player
	@OneToMany(mappedBy = "team")
	private Set<Player> players;

	public Team() {
	}

	public int getTeamId() {
		return this.teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Set<Game> getGames1() {
		return this.games1;
	}

	public void setGames1(Set<Game> games1) {
		this.games1 = games1;
	}

	public Set<Game> getGames2() {
		return this.games2;
	}

	public void setGames2(Set<Game> games2) {
		this.games2 = games2;
	}

	public Set<Game> getWinningGames() {
		return winningGames;
	}

	public void setWinningGames(Set<Game> winningGames) {
		this.winningGames = winningGames;
	}

	public Set<Game> getWinningTosses() {
		return winningTosses;
	}

	public void setWinningTosses(Set<Game> winningTosses) {
		this.winningTosses = winningTosses;
	}

	public Set<Inning> getInnings() {
		return this.innings;
	}

	public void setInnings(Set<Inning> innings) {
		this.innings = innings;
	}

	public Set<Player> getPlayers() {
		return this.players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

}