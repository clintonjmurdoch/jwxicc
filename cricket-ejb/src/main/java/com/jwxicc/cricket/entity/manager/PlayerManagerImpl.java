package com.jwxicc.cricket.entity.manager;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.jwxicc.cricket.entity.PlayerDetail;
import com.jwxicc.cricket.entity.Player;
import com.jwxicc.cricket.interfaces.PlayerManager;
import javax.persistence.Query;

@Stateless(name = "playerManager")
@Local(PlayerManager.class)
public class PlayerManagerImpl extends BaseManager<Player> implements PlayerManager {

	@Override
	public boolean validateRequiredFields(Player obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Player getPlayerForProfile(int playerId) {
		String queryString = "from Player p left join FETCH p.playerDetail j where p.playerId = :playerid";
		Query q = em.createQuery(queryString);
		q.setParameter("playerid", playerId);
		Player p = (Player) q.getSingleResult();
		System.out.println(p.getScorecardName());
		return p;
	}

	@Override
	public List<Player> getPlayersFromTeam(int teamId) {
		Query query = em.createQuery("from Player p where p.team.teamId = :teamId");
		query.setParameter("teamId", teamId);

		return query.getResultList();
	}

	@Override
	public PlayerDetail savePlayerDetail(PlayerDetail playerDetail) {
		if (playerDetail.getPlayerDetailId() > 0) {
			// merge
			playerDetail = em.merge(playerDetail);
		} else {
			// it is new
			em.persist(playerDetail);
		}
		return playerDetail;

	}
}