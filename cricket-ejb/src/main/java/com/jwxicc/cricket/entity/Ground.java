package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the ground database table.
 * 
 */
@Entity
@Table(name = "ground")
public class Ground implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false)
	private int groundId;

	@Column(nullable = false, length = 45)
	private String groundName;

	@Column(length = 45)
	private String mapRef;

	@Column(length = 45)
	private String streetAddress;

	@Column(length = 45)
	private String suburb;

	// bi-directional many-to-one association to Game
	@OneToMany(mappedBy = "ground")
	private Set<Game> games;

	public Ground() {
	}

	public int getGroundId() {
		return this.groundId;
	}

	public void setGroundId(int groundId) {
		this.groundId = groundId;
	}

	public String getGroundName() {
		return this.groundName;
	}

	public void setGroundName(String groundName) {
		this.groundName = groundName;
	}

	public String getMapRef() {
		return this.mapRef;
	}

	public void setMapRef(String mapRef) {
		this.mapRef = mapRef;
	}

	public String getStreetAddress() {
		return this.streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getSuburb() {
		return this.suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public Set<Game> getGames() {
		return this.games;
	}

	public void setGames(Set<Game> games) {
		this.games = games;
	}

}