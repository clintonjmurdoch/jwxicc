package com.jwxicc.cricket.interfaces;

import java.util.List;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Jwplayer;
import com.jwxicc.cricket.entity.Player;

@Local
public interface PlayerManager extends CricketDataManager<Player> {

	public Player getPlayerForProfile(int playerId);
	public List<Player> getPlayersFromTeam(int teamId);
	public void saveJWPlayerInfo(Jwplayer jwPlayer);
}
