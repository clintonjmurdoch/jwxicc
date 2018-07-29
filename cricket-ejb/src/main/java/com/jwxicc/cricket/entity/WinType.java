package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the win_type database table.
 * 
 */
@Entity
@Table(name = "win_type")
public class WinType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int winTypeId;

	@Column(length = 45)
	private String winTypeName;

	// bi-directional many-to-one association to Game
	@OneToMany(mappedBy = "winType")
	private Set<Game> games;

	public WinType() {
	}

	public int getWinTypeId() {
		return this.winTypeId;
	}

	public void setWinTypeId(int winTypeId) {
		this.winTypeId = winTypeId;
	}

	public String getWinTypeName() {
		return this.winTypeName;
	}

	public void setWinTypeName(String winTypeName) {
		this.winTypeName = winTypeName;
	}

	public Set<Game> getGames() {
		return this.games;
	}

	public void setGames(Set<Game> games) {
		this.games = games;
	}

}