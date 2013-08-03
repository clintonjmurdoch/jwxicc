package com.jwxicc.cricket.facebook;

import java.util.List;

import com.restfb.Facebook;

public class FacebookFeed {

	@Facebook("data")
	private List<FacebookPost> posts;

	public List<FacebookPost> getPosts() {
		return posts;
	}

	public void setPosts(List<FacebookPost> posts) {
		this.posts = posts;
	}
}
