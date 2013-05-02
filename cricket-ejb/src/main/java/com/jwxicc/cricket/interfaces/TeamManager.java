package com.jwxicc.cricket.interfaces;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Team;

@Local
public interface TeamManager extends CricketDataManager<Team> {
	public Team getJWXIReference();
}
