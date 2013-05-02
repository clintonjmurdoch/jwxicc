package com.jwxicc.cricket.facebook;

import java.util.List;

import com.restfb.Facebook;
import com.restfb.types.Post;

public class FacebookFeed {

	@Facebook("data")
	private List<Post> posts;

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
}
