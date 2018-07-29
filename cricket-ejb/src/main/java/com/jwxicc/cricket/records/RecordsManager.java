package com.jwxicc.cricket.records;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.jwxicc.cricket.interfaces.PlayerManager;

@Stateless
public abstract class RecordsManager<T, U> {

	protected static final String JWXI_TEAM_SQL = "p.teamid = :jwxi ";

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	@EJB
	PlayerManager playerManager;

	protected static final String MATCHES_PLAYED_SQL = "select count(*) as matches from BATTING b where b.playerid = :player";

	protected static final String COMPETITION_QUALIFIER_END_SQL = "inner join INNINGS i natural join GAME g "
			+ "on b.inningsid = i.inningsid "
			+ "where g.competitionid = :comp ";
	
	protected static final String WILLOWFEST_QUALIFIER_SQL = " and b.inningsid in (select inningsid from innings i inner join game g on i.gameid = g.gameid "
			+ "inner join competition c on g.competitionid = c.competitionid where associationName = 'Willowfest') ";
	
	public abstract List<T> getInningsBest(boolean willowfestOnly);

	public abstract List<U> getByAggregate(boolean willowfestOnly);

	public abstract List<U> getByAverage(boolean willowfestOnly);

	public abstract List<U> getBySeason(int competitionId);

	public abstract U getPlayerCareerRecord(int playerId, boolean willowfestOnly);

	public int objToInt(Object o) {
		if (o != null) {
			return Integer.valueOf(o.toString()).intValue();
		} else {
			return 0;
		}
	}

	public int getMatchesPlayed(int playerId, boolean willowfestOnly) {
		String queryString = MATCHES_PLAYED_SQL;
		if (willowfestOnly) {
			queryString += WILLOWFEST_QUALIFIER_SQL;
		}
		Query query = em.createNativeQuery(queryString);
		query.setParameter("player", playerId);
		int matches = ((BigInteger) query.getSingleResult()).intValue();
		return matches;
	}

	public int getMatchesPlayedInSeason(int playerId, int competitionId) {
		Query query = em
				.createNativeQuery(MATCHES_PLAYED_SQL
						+ " and b.inningsId in (select i.inningsId from INNINGS i natural join GAME g where g.competitionId = :comp)");
		query.setParameter("player", playerId);
		query.setParameter("comp", competitionId);
		int matches = ((BigInteger) query.getSingleResult()).intValue();
		return matches;
	}

}
