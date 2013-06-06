package com.jwxicc.cricket.interfaces;

import java.util.List;

import javax.ejb.Local;

import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.GamePlayerDesignation;
import com.jwxicc.cricket.entity.WinType;

@Local
public interface GameManagerLocal extends GameFacade, CricketDataManager<Game> {
	public Game getGameForScorecard(int gameId);

	public List<GamePlayerDesignation> getDesignations(int gameId);
}
