package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the newsitem database table.
 * 
 */
@Entity
@Table(name = "newsitem")
public class Newsitem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int newsid;

	@Lob
	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private Timestamp timestamp;

	@Column(nullable = false, length = 100)
	private String title;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "poster", nullable = false)
	private User user;

	public Newsitem() {
	}

	public int getNewsid() {
		return this.newsid;
	}

	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}