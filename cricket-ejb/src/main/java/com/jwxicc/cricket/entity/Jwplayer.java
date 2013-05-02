package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the jwplayer database table.
 * 
 */
@Entity
@Table(name="JWPLAYER")
public class Jwplayer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int jwplayerId;

	@Column(name="batting_style", length=45)
	private String battingStyle;

	@Temporal(TemporalType.DATE)
	private Date birthdate;

	@Column(length=45)
	private String birthplace;

	@Column(name="bowling_style", length=45)
	private String bowlingStyle;

	@Column(name="cap_number", length=45)
	private String capNumber;

	@Column(name="fielding_positions", length=45)
	private String fieldingPositions;

	@Column(length=45)
	private String fullname;

	@Lob
	private byte[] image;

	@Column(length=90)
	private String nicknames;
	
	@Lob
	private String profile;

	@Column(name="shirt_number", length=45)
	private String shirtNumber;

	@Column(length=45)
	private String teams;

	//bi-directional one-to-one association to Player
	@OneToOne
	@JoinColumn(name="playerId")
	private Player player;

	public Jwplayer() {
	}

	public int getJwplayerId() {
		return this.jwplayerId;
	}

	public void setJwplayerId(int jwplayerId) {
		this.jwplayerId = jwplayerId;
	}

	public String getBattingStyle() {
		return this.battingStyle;
	}

	public void setBattingStyle(String battingStyle) {
		this.battingStyle = battingStyle;
	}

	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getBirthplace() {
		return this.birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getBowlingStyle() {
		return this.bowlingStyle;
	}

	public void setBowlingStyle(String bowlingStyle) {
		this.bowlingStyle = bowlingStyle;
	}

	public String getCapNumber() {
		return this.capNumber;
	}

	public void setCapNumber(String capNumber) {
		this.capNumber = capNumber;
	}

	public String getFieldingPositions() {
		return this.fieldingPositions;
	}

	public void setFieldingPositions(String fieldingPositions) {
		this.fieldingPositions = fieldingPositions;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getNicknames() {
		return this.nicknames;
	}

	public void setNicknames(String nicknames) {
		this.nicknames = nicknames;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getShirtNumber() {
		return this.shirtNumber;
	}

	public void setShirtNumber(String shirtNumber) {
		this.shirtNumber = shirtNumber;
	}

	public String getTeams() {
		return this.teams;
	}

	public void setTeams(String teams) {
		this.teams = teams;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}