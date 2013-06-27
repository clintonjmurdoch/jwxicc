package com.jwxicc.cricket.parse.cricketstatz;

import com.jwxicc.cricket.entity.Ground;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.entity.Team;

public interface SharedPersistenceInterface {
	Player getOrCreatePlayer(int playerId, String playerName, Team team);

	Team getOrCreateTeam(int teamId, String teamName);

	Ground getOrCreateGround(int groundId, String groundName);

	Team getJWXIReference();
}
