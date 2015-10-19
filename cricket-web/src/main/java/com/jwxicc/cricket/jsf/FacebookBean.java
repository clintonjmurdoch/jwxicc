package com.jwxicc.cricket.jsf;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.jwxicc.cricket.facebook.FacebookFeed;
import com.jwxicc.cricket.facebook.FacebookManager;

@ManagedBean(name = "fbBean")
@ViewScoped
public class FacebookBean implements Serializable {

	private FacebookFeed feed = null;
	private boolean renderNews = false;
	private boolean renderLoading = true;

	@EJB(name = "fbManager")
	FacebookManager fbManager;

	public void initialiseFeed() {
		try {
			System.out.println("getting news");
			this.feed = fbManager.getFeed();
			System.out.println("got news");
			renderNews = true;
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Failed to get news items from Facebook", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		renderLoading = false;
	}
	
	public String getNextMatchInfo() {
		return System.getenv("NEXT_MATCH_DETAILS");
	}

	public FacebookFeed getFeed() {
		return feed;
	}

	public void setFeed(FacebookFeed feed) {
		this.feed = feed;
	}

	public boolean isRenderNews() {
		return renderNews;
	}

	public void setRenderNews(boolean renderNews) {
		this.renderNews = renderNews;
	}

	public boolean isRenderLoading() {
		return renderLoading;
	}

	public void setRenderLoading(boolean renderLoading) {
		this.renderLoading = renderLoading;
	}

}