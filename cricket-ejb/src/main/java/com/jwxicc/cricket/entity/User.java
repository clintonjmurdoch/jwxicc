package com.jwxicc.cricket.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, length = 20)
	private String username;

	@Column(nullable = true, length = 45)
	private String email;

	@Column(nullable = false, length = 20)
	private String password;

	// bi-directional many-to-one association to Newsitem
	@OneToMany(mappedBy = "user")
	private Set<Newsitem> newsitems;

	public User() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Newsitem> getNewsitems() {
		return this.newsitems;
	}

	public void setNewsitems(Set<Newsitem> newsitems) {
		this.newsitems = newsitems;
	}

}