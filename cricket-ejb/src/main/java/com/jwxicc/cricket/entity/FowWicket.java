package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fow_wicket database table.
 * 
 */
@Entity
@Table(name="FOW_WICKET")
public class FowWicket implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int fow_wicketId;

	private int contribution;

	@Column(nullable=false)
	private boolean outStatus;

	//bi-directional many-to-one association to Fow
	@ManyToOne
	@JoinColumn(name="fowId", nullable=false)
	private Fow fow;

	//bi-directional many-to-one association to Player
	@ManyToOne
	@JoinColumn(name="playerId", nullable=false)
	private Player player;

	public FowWicket() {
	}

	public int getFow_wicketId() {
		return this.fow_wicketId;
	}

	public void setFow_wicketId(int fow_wicketId) {
		this.fow_wicketId = fow_wicketId;
	}

	public int getContribution() {
		return this.contribution;
	}

	public void setContribution(int contribution) {
		this.contribution = contribution;
	}

	public boolean isOutStatus() {
		return outStatus;
	}

	public void setOutStatus(boolean outStatus) {
		this.outStatus = outStatus;
	}

	public Fow getFow() {
		return this.fow;
	}

	public void setFow(Fow fow) {
		this.fow = fow;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}