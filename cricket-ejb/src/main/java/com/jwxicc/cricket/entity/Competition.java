package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import java.util.Set;

/**
 * The persistent class for the competition database table.
 * 
 */
@Entity
@Table(name = "COMPETITION")
public class Competition implements Serializable, Comparable<Competition> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int competitionId;

	@Column(nullable = false, length = 45)
	private String associationName;

	@Transient
	private boolean editable;

	@Column(nullable = false, length = 45)
	private String grade;

	@Column(nullable = false, length = 9)
	private String season;

	// bi-directional one-to-one association to Review
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "reviewId")
	private Review review;

	// bi-directional many-to-one association to Game
	@OneToMany(mappedBy = "competition")
	private Set<Game> games;

	public Competition() {
	}

	public int getCompetitionId() {
		return this.competitionId;
	}

	public void setCompetitionId(int competitionId) {
		this.competitionId = competitionId;
	}

	public String getAssociationName() {
		return this.associationName;
	}

	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSeason() {
		return this.season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public Review getReview() {
		return this.review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public Set<Game> getGames() {
		return this.games;
	}

	public void setGames(Set<Game> games) {
		this.games = games;
	}

	@Override
	public int compareTo(Competition o) {
		// order them by season
		String thisSeason = this.season.substring(0, 4);
		String otherSeason = o.season.substring(0, 4);
		// take the first 4 characters and get as a number
		// make sure they are numeric, or return
		if (!StringUtils.isNumeric(otherSeason)) {
			return -1;
		}
		if (!StringUtils.isNumeric(thisSeason)) {
			return 1;
		}

		int thisYear = Integer.parseInt(thisSeason);
		int otherYear = Integer.parseInt(otherSeason);
		if (thisYear < otherYear) {
			return -1;
		} else if (thisYear > otherYear) {
			return 1;
		} else {
			// same first part of year, check for second part
			if (this.season.length() == 4 && otherSeason.length() == 4) {
				return this.associationName.compareTo(o.associationName);
			} else {
				if (otherSeason.length() > 4) {
					return -1;
				} else {
					return 1;
				}
			}
		}
	}

}