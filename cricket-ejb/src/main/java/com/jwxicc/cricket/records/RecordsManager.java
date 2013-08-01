package com.jwxicc.cricket.records;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public abstract class RecordsManager<T, U> {

	protected static final String JWXI_TEAM_SQL = "p.teamid = :jwxi ";

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	protected static final String COMPETITION_QUALIFIER_END_SQL = "inner join INNINGS i natural join GAME g inner join COMPETITION c "
			+ "on g.competitionid = c.competitionid and b.inningsid = i.inningsid "
			+ "where c.competitionid = :comp)" + "group by playerid ";

	public abstract List<T> getInningsBest();

	public abstract List<U> getByAggregate();

	public abstract List<U> getByAverage();

	public abstract List<U> getBySeason(int competitionId);

	public abstract U getPlayerCareerRecord(int playerId);

	public int objToInt(Object o) {
		if (o != null) {
			return Integer.valueOf(o.toString()).intValue();
		} else {
			return 0;
		}
	}
}
