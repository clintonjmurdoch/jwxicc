package com.jwxicc.cricket.facebook;

import com.restfb.Facebook;
import com.restfb.types.Post;

public class FacebookPost extends Post {

	@Facebook
	private String story;

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}
}
