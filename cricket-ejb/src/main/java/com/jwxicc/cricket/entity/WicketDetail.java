package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.jwxicc.cricket.entity.dbenum.WicketDetailType;


/**
 * The persistent class for the wicket_detail database table.
 * 
 */
@Entity
@Table(name="WICKET_DETAIL")
public class WicketDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int wicketDetailId;

	@Column(nullable=false, length=5)
	@Enumerated(EnumType.STRING)
	private WicketDetailType wicketType;

	//bi-directional many-to-one association to Batting
	@ManyToOne
	@JoinColumn(name="battingId", nullable=false)
	private Batting batting;

	//bi-directional many-to-one association to Player
	@ManyToOne
	@JoinColumn(name="playerId", nullable=false)
	private Player player;

	public WicketDetail() {
	}

	public int getWicketDetailId() {
		return this.wicketDetailId;
	}

	public void setWicketDetailId(int wicketDetailId) {
		this.wicketDetailId = wicketDetailId;
	}

	public WicketDetailType getWicketType() {
		return wicketType;
	}

	public void setWicketType(WicketDetailType wicketType) {
		this.wicketType = wicketType;
	}

	public Batting getBatting() {
		return this.batting;
	}

	public void setBatting(Batting batting) {
		this.batting = batting;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}