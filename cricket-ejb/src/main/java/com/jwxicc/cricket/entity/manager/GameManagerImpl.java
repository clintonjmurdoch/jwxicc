package com.jwxicc.cricket.entity.manager;

import java.util.List;

import com.jwxicc.cricket.entity.Game;
import com.jwxicc.cricket.entity.GamePlayerDesignation;
import com.jwxicc.cricket.entity.WinType;
import com.jwxicc.cricket.interfaces.GameManagerLocal;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Session Bean implementation class GameManagerImpl
 */
@Stateless(name = "gameManager")
@Local(GameManagerLocal.class)
public class GameManagerImpl extends BaseManager<Game> implements GameManagerLocal {

	@Override
	public Game findLazy(Object key) {
		return super.findLazy(key);
	}

	@Override
	public Game getGameForScorecard(int gameId) {
		String sqlString = "Select g from Game g " + "join FETCH g.innings i "
				+ "left join FETCH g.competition c " + "left join FETCH g.review r "
				+ "left join FETCH g.ground gr " + "left join FETCH i.team "
				+ "left join FETCH i.bowlings bo " + "left join FETCH bo.player "
				+ "left join FETCH i.battings ba " + "left join FETCH ba.player "
				+ "left join FETCH ba.howout " + "left join FETCH ba.wicketDetails wd "
				+ "left join FETCH i.partnerships p " + "inner join FETCH p.partnershipPlayers pp "
				+ "left join FETCH wd.player " + "where g.gameId = ?1 " + "and pp.outStatus = ?2";
		Query q = em.createQuery(sqlString);
		q.setParameter(1, gameId);
		q.setParameter(2, true);

		Game toReturn = (Game) q.getSingleResult();

		return toReturn;
	}

	@Override
	public boolean validateRequiredFields(Game game) {
		if (game.getCompetition() != null && game.getRound() != null && game.getGround() != null
				&& game.getDate() != null && game.getAwayTeam() != null && game.getHomeTeam() != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void addDesignations(List<GamePlayerDesignation> designations) {
		for (GamePlayerDesignation des : designations) {
			em.persist(des);
		}
	}

	@Override
	public List<GamePlayerDesignation> getDesignations(int gameId) {
		Query query = em
				.createQuery("from GamePlayerDesignation d join FETCH d.player where d.game = :game");
		query.setParameter("game", em.getReference(Game.class, gameId));
		List<GamePlayerDesignation> resultList = query.getResultList();
		return resultList;
	}

	@Override
	public WinType getWinTypeRef(int id) {
		return em.getReference(WinType.class, id);
	}

	@Override
	public void addGame(Game game) {
		super.persist(game);
	}

}
