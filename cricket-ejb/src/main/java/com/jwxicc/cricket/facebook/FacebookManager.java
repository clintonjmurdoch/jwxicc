package com.jwxicc.cricket.facebook;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;

/**
 * Session Bean implementation class FacebookManager
 */
@Singleton(name = "fbManager")
@LocalBean
public class FacebookManager {
	
	private static final String appId = "144907679229";
	private static final String appSecret = "df787abb5bcea52a650bb5c68e5c06cd";

	private AccessToken accessToken = null;
	private FacebookClient client = null;
	
	public FacebookManager() {
		this.accessToken = new DefaultFacebookClient().obtainAppAccessToken(appId, appSecret);
		this.client = new DefaultFacebookClient(this.accessToken.getAccessToken());
	}
	
	public AccessToken getAccessToken() {
		return this.accessToken;
	}
	
	public FacebookClient getClient() {
		return client;
	}
	
	public FacebookFeed getFeed() {
		return client.fetchObject("143138622462934/feed", FacebookFeed.class, Parameter.with("limit", 4));
	}
	
}