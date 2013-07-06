package com.jwxicc.cricket.facebook;

import javax.ejb.EJBException;
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

	private static final String appId = System.getenv("FB_APP_ID");
	private static final String appSecret = System.getenv("FB_APP_SECRET");

	private AccessToken accessToken = null;
	private FacebookClient client = null;

	public FacebookManager() {
	}

	public void init() {
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
		if (client == null) {
			try {
				init();
			} catch (Exception e) {
				throw new EJBException("Failed to initialise facebook client", e);
			}
		}
		try {
			return getFeedObject();
		} catch (Exception e) {
			throw new EJBException("Failed to rerieve news items from Facebook", e);
		}
	}

	public FacebookFeed getFeedObject() {
		return client.fetchObject("143138622462934/feed", FacebookFeed.class,
				Parameter.with("limit", 4));
	}
}