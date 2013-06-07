package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.jwxicc.cricket.entity.dbenum.RelationshipViewType;

/**
 * Entity implementation class for Entity: PlayerRelationship
 * 
 */
@Entity
@Table(name = "PLAYER_RELATIONSHIP")
public class PlayerRelationship implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int relationshipId;

	@Column(length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	private RelationshipViewType ownerViewOfRelationship;

	@ManyToOne
	@JoinColumn(name = "owningPlayerId", nullable = false)
	private Player owningPlayer;

	@ManyToOne
	@JoinColumn(name = "otherPlayerId", nullable = false)
	private Player otherPlayer;

	public int getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(int relationshipId) {
		this.relationshipId = relationshipId;
	}

	public RelationshipViewType getOwnerViewOfRelationship() {
		return ownerViewOfRelationship;
	}

	public void setOwnerViewOfRelationship(RelationshipViewType ownerViewOfRelationship) {
		this.ownerViewOfRelationship = ownerViewOfRelationship;
	}

	public Player getOwningPlayer() {
		return owningPlayer;
	}

	public void setOwningPlayer(Player owningPlayer) {
		this.owningPlayer = owningPlayer;
	}

	public Player getOtherPlayer() {
		return otherPlayer;
	}

	public void setOtherPlayer(Player otherPlayer) {
		this.otherPlayer = otherPlayer;
	}

}
