package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the howout database table.
 * 
 */
@Entity
@Table(name = "HOWOUT")
public class Howout implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int howOutid;

	@Column(nullable = false, length = 15)
	private String dismissalShort;

	@Column(nullable = false, length = 45)
	private String dismissalType;

	// bi-directional many-to-one association to Batting
	@OneToMany(mappedBy = "howout")
	private Set<Batting> battings;

	public Howout() {
	}

	public int getHowOutid() {
		return this.howOutid;
	}

	public void setHowOutid(int howOutid) {
		this.howOutid = howOutid;
	}

	public String getDismissalShort() {
		return this.dismissalShort;
	}

	public void setDismissalShort(String dismissalShort) {
		this.dismissalShort = dismissalShort;
	}

	public String getDismissalType() {
		return this.dismissalType;
	}

	public void setDismissalType(String dismissalType) {
		this.dismissalType = dismissalType;
	}

	public Set<Batting> getBattings() {
		return this.battings;
	}

	public void setBattings(Set<Batting> battings) {
		this.battings = battings;
	}

}