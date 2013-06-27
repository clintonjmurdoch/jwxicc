package com.jwxicc.cricket.parse.cricketstatz;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.jwxicc.cricket.entity.Ground;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.entity.Team;
import com.jwxicc.cricket.interfaces.GroundManager;
import com.jwxicc.cricket.interfaces.PlayerManager;
import com.jwxicc.cricket.interfaces.TeamManager;

/**
 * Facade through which the Game Parser accesses objects which are needed across transactions and
 * threads. Operations in this class are part of the caller's transaction, so returned objects can
 * be merged into the caller persistence context
 */
@LocalBean
@Stateless(name = "sharedObjectsFacade")
public class SharedPersistenceObjectsFacade implements SharedPersistenceInterface {

	@EJB
	SharedPersistenceObjectsController controller;

	@Resource
	SessionContext ctx;

	@EJB
	PlayerManager playerManager;

	@EJB
	TeamManager teamManager;

	@EJB
	GroundManager groundManager;

	public Team getJWXIReference() {
		return controller.getJWXIReference();
	}

	@Override
	public Player getOrCreatePlayer(int playerId, String playerName, Team team) {
		Player player = controller.getOrCreatePlayer(playerId, playerName, team);
		return playerManager.getReference(player.getPlayerId());
	}

	@Override
	public Team getOrCreateTeam(int teamId, String teamName) {
		Team team = controller.getOrCreateTeam(teamId, teamName);
		return teamManager.getReference(team.getTeamId());
	}

	@Override
	public Ground getOrCreateGround(int groundId, String groundName) {
		Ground ground = controller.getOrCreateGround(groundId, groundName);
		return groundManager.getReference(ground.getGroundId());
	}
}