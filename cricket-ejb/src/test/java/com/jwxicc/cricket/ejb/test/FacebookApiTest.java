package com.jwxicc.cricket.ejb.test;

import javax.mail.MessagingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jwxicc.cricket.facebook.FacebookFeed;
import com.jwxicc.cricket.facebook.FacebookManager;
import com.jwxicc.cricket.facebook.FacebookPost;

@RunWith(Arquillian.class)
public class FacebookApiTest {
	
	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
				.addPackages(true, "com.jwxicc.cricket.facebook")
				.addPackages(true, "com.restfb")
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");

		return archive;
	}

	@Test
	public void testFacebookFeed() throws MessagingException {
		FacebookFeed feed = new FacebookManager().getFeed();
		FacebookPost facebookPost = feed.getPosts().get(0);
	}
}
