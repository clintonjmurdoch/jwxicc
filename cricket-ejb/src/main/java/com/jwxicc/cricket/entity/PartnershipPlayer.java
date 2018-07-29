package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the PARTNERSHIP_PLAYER database table.
 * 
 */
@Entity
@Table(name = "partnership_player")
public class PartnershipPlayer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int partnershipPlayerId;

	private int battingPosition;

	private int contribution;

	@Column(nullable = false)
	private boolean outStatus;
	
	@Column(nullable = false)
	private boolean retiredNotOutStatus;

	// bi-directional many-to-one association to Partnership
	@ManyToOne
	@JoinColumn(name = "partnershipId", nullable = false)
	private Partnership partnership;

	// bi-directional many-to-one association to Player
	@ManyToOne
	@JoinColumn(name = "playerId", nullable = false)
	private Player player;

	public PartnershipPlayer() {
	}

	public int getBattingPosition() {
		return battingPosition;
	}

	public void setBattingPosition(int battingPosition) {
		this.battingPosition = battingPosition;
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

	public boolean isRetiredNotOutStatus() {
		return retiredNotOutStatus;
	}

	public void setRetiredNotOutStatus(boolean retiredNotOutStatus) {
		this.retiredNotOutStatus = retiredNotOutStatus;
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