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

	private int newsItemsForHome = 2;
	private FacebookFeed feed = null;
	private boolean renderNews = false;

	@EJB(name = "fbManager")
	FacebookManager fbManager;

	public FacebookFeed getFeed() {
		if (feed == null) {
			try {
				this.feed = fbManager.getFeed();
			} catch (Exception e) {
				FacesMessage message = new FacesMessage(
						"Failed to get news items from Facebook");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
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

}