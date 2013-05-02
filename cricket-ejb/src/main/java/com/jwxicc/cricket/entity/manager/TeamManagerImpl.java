package com.jwxicc.cricket.entity.manager;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.jwxicc.cricket.entity.Team;
import com.jwxicc.cricket.interfaces.TeamManager;
import com.jwxicc.cricket.util.JwxiccUtils;

@Stateless(name = "teamManager")
@Local(TeamManager.class)
public class TeamManagerImpl extends BaseManager<Team> implements TeamManager {

	@Override
	public boolean validateRequiredFields(Team obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Team getJWXIReference() {
		return em.getReference(Team.class, JwxiccUtils.JWXICC_TEAM_ID);
	}

}
