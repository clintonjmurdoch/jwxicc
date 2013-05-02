package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the fow database table.
 * 
 */
@Entity
@Table(name="FOW")
public class Fow implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int fowId;

	@Column(nullable=false)
	private float overs;

	@Column(nullable=false)
	private int score;

	@Column(nullable=false)
	private int wicket;

	//bi-directional many-to-one association to Inning
	@ManyToOne
	@JoinColumn(name="inningsId", nullable=false)
	private Inning inning;

	//bi-directional many-to-one association to FowWicket
	@OneToMany(mappedBy="fow")
	private Set<FowWicket> fowWickets;

	public Fow() {
	}

	public int getFowId() {
		return this.fowId;
	}

	public void setFowId(int fowId) {
		this.fowId = fowId;
	}

	public float getOvers() {
		return this.overs;
	}

	public void setOvers(float overs) {
		this.overs = overs;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWicket() {
		return this.wicket;
	}

	public void setWicket(int wicket) {
		this.wicket = wicket;
	}

	public Inning getInning() {
		return this.inning;
	}

	public void setInning(Inning inning) {
		this.inning = inning;
	}

	public Set<FowWicket> getFowWickets() {
		return this.fowWickets;
	}

	public void setFowWickets(Set<FowWicket> fowWickets) {
		this.fowWickets = fowWickets;
	}

}