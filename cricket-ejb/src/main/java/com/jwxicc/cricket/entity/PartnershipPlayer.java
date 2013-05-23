package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the fow_wicket database table.
 * 
 */
@Entity
@Table(name = "PARTNERSHIP_PLAYER")
public class PartnershipPlayer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int partnershipPlayerId;

	private int contribution;

	@Column(nullable = false)
	private boolean outStatus;

	// bi-directional many-to-one association to Fow
	@ManyToOne
	@JoinColumn(name = "partnershipId", nullable = false)
	private Partnership partnership;

	// bi-directional many-to-one association to Player
	@ManyToOne
	@JoinColumn(name = "playerId", nullable = false)
	private Player player;

	public PartnershipPlayer() {
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

	public int getPartnershipPlayerId() {
		return partnershipPlayerId;
	}

	public void setPartnershipPlayerId(int partnershipPlayerId) {
		this.partnershipPlayerId = partnershipPlayerId;
	}

	public Partnership getPartnership() {
		return partnership;
	}

	public void setPartnership(Partnership partnership) {
		this.partnership = partnership;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}