package com.jwxicc.cricket.jsf;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.jwxicc.cricket.entity.Newsitem;

@ManagedBean
@ViewScoped
public class NewsBean {
	
	public List<Newsitem> getNewsForHome() {
		return null;
	}
}
