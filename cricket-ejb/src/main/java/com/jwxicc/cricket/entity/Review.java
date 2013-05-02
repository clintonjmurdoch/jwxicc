package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the review database table.
 * 
 */
@Entity
@Table(name="REVIEW")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int reviewId;

	@Lob
	@Column(name="review_text", nullable=false)
	private String reviewText;

	//bi-directional one-to-one association to Competition
	@OneToOne(mappedBy="review")
	private Competition competition;

	//bi-directional one-to-one association to Game
	@OneToOne(mappedBy="review")
	private Game game;

	public Review() {
	}

	public int getReviewId() {
		return this.reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public String getReviewText() {
		return this.reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public Competition getCompetition() {
		return this.competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}