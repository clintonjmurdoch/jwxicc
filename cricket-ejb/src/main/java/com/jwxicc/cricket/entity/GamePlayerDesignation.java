package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.jwxicc.cricket.entity.dbenum.DesignationType;

/**
 * The persistent class for the gameplayerdesignation database table.
 * 
 */
@Entity
@Table(name = "game_player_designation")
public class GamePlayerDesignation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int designationId;

	@Column(nullable = false, length = 15)
	@Enumerated(EnumType.STRING)
	private DesignationType designationType;

	// bi-directional many-to-one association to Game
	@ManyToOne
	@JoinColumn(name = "gameId", nullable = false)
	private Game game;

	// bi-directional many-to-one association to Player
	@ManyToOne
	@JoinColumn(name = "playerId", nullable = false)
	private Player player;

	public GamePlayerDesignation() {
	}

	public int getDesignationId() {
		return this.designationId;
	}

	public void setDesignationId(int designationId) {
		this.designationId = designationId;
	}

	public DesignationType getDesignationType() {
		return designationType;
	}

	public void setDesignationType(DesignationType designationType) {
		this.designationType = designationType;
	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}