package com.jwxicc.cricket.interfaces;

import java.util.List;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.PlayerDetail;
import com.jwxicc.cricket.entity.Player;

@Local
public interface PlayerManager extends CricketDataManager<Player> {

	public Player getPlayerForProfile(int playerId);

	public List<Player> getPlayersFromTeam(int teamId);
	
	public List<Player> getPlayersWithDetailFromTeam(int teamId);

	public PlayerDetail savePlayerDetail(PlayerDetail playerDetail);
	
	public Player getPlayerForCapNumber(int capNumber);
}