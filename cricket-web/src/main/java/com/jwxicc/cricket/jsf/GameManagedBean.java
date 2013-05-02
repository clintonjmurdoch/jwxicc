package com.jwxicc.cricket.jsf;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.jwxicc.cricket.entity.Batting;
import com.jwxicc.cricket.entity.Bowling;
import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.GamePlayerDesignation;
import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.interfaces.GameManager;

@ManagedBean(name = "matchBean")
@ViewScoped
public class GameManagedBean implements Serializable {
	private static final long serialVersionUID = 1543484449435990728L;

	@EJB
	GameManager gameBean;

	private int matchId;
	private Game match = null;
	// key: teamid, val: map (key: playerid, val: player)
	private Map<Integer, Map<Integer, Player>> teamPlayerMap = new HashMap<Integer, Map<Integer, Player>>();
	private Map<Integer, Player> allPlayerMap = new HashMap<Integer, Player>();

	public void loadMatch() {
		System.out.println("matchid set to: " + matchId);
		this.match = gameBean.getGameForScorecard(this.matchId);

		for (Inning i : match.getInnings()) {
			System.out.println("for innings " + i.getInningsId()
					+ " there are " + i.getBattings().size() + " battings");
			Integer teamInt = Integer.valueOf(i.getTeam().getTeamId());

			Map<Integer, Player> playerMap = new HashMap<Integer, Player>();
			for (Batting b : i.getBattings()) {
				playerMap.put(Integer.valueOf(b.getPlayer().getPlayerId()),
						b.getPlayer());
				allPlayerMap.put(Integer.valueOf(b.getPlayer().getPlayerId()),
						b.getPlayer());
			}

			teamPlayerMap.put(teamInt, playerMap);
		}

		// add designations to players
		List<GamePlayerDesignation> designations = gameBean
				.getDesignations(this.matchId);
		for (GamePlayerDesignation des : designations) {
			Player playerToDesignate = allPlayerMap.get(Integer.valueOf(des
					.getPlayer().getPlayerId()));
			if (playerToDesignate != null) {
				switch (des.getDesignationType()) {
				case CAPTAIN:
					System.out.println("captain set for "
							+ des.getPlayer().getScorecardName());
					playerToDesignate.setCaptain(true);
					break;
				case KEEPER:
					System.out.println("keeper set for "
							+ des.getPlayer().getScorecardName());
					playerToDesignate.setKeeper(true);
					break;
				case MAN_OF_MATCH:
					break;
				}
			}
		}

	}

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public Game getMatch() {
		if (match == null) {
			System.out.println("match is null - load it");
			loadMatch();
		}
		return match;
	}

	public void setMatch(Game match) {
		this.match = match;
	}

	public void addToPlayerMap(Collection<Player> players) {

	}

	public Map<Integer, Player> getAllPlayerMap() {
		return allPlayerMap;
	}

	public void setAllPlayerMap(Map<Integer, Player> playerMap) {
		this.allPlayerMap = playerMap;
	}

}
