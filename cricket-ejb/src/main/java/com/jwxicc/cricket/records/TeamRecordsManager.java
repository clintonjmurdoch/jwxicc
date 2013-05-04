package com.jwxicc.cricket.records;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.jwxicc.cricket.entity.Inning;
import com.jwxicc.cricket.util.JwxiccUtils;

@Stateless(name = "teamRecordsManager")
@LocalBean
public class TeamRecordsManager {

	@PersistenceContext(unitName = "Jwxicc_JPA")
	EntityManager em;

	private static final String TEAM_RECORDS_BASE_EJBQL = "from Inning i left join FETCH i.game g "
			+ "left join FETCH g.team1 t1 left join FETCH g.team2 t2 left join FETCH g.ground ";
	private static final String TEAM_RECORDS_FOR_EJBQL = TEAM_RECORDS_BASE_EJBQL
			+ "where i.team.teamId = :jwxi ";
	private static final String TEAM_RECORDS_AGAINST_EJBQL = TEAM_RECORDS_BASE_EJBQL
			+ "where i.team.teamId != :jwxi ";

	public WinLossDrawRecord getOverallRecord() {
		String sql = "SELECT count(*) as matches, "
				+ "count(IF((winner = 'HOME' and homeTeamId = :jwxi) OR (winner = 'AWAY' and awayTeamId = :jwxi),1,null)) as won, "
				+ "count(IF((winner = 'HOME' and homeTeamId != :jwxi) OR (winner = 'AWAY' and awayTeamId != :jwxi),1,null)) as lost, "
				+ "count(IF(winner is null,1,null)) as noresult from GAME";

		Query nativeQuery = em.createNativeQuery(sql);
		nativeQuery.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		List<Object[]> resultList = nativeQuery.getResultList();

		Object[] result = resultList.get(0);

		WinLossDrawRecord wldRecord = new WinLossDrawRecord(Integer.valueOf(
				result[0].toString()).intValue(), Integer.valueOf(
				result[1].toString()).intValue(), Integer.valueOf(
				result[2].toString()).intValue(), 0, Integer.valueOf(
				result[3].toString()).intValue());

		return wldRecord;

	}

	public List<Inning> getHighestInningsFor() {
		String ejbql = TEAM_RECORDS_FOR_EJBQL + "order by i.runsScored desc";

		Query query = em.createQuery(ejbql);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setMaxResults(JwxiccUtils.RECORDS_TO_SHOW);

		List<Inning> resultList = query.getResultList();

		return resultList;

	}

	public List<Inning> getLowestInningsFor() {
		String ejbql = TEAM_RECORDS_FOR_EJBQL
				+ "and i.closureType = 'ALLOUT' order by i.runsScored asc";

		Query query = em.createQuery(ejbql);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setMaxResults(JwxiccUtils.RECORDS_TO_SHOW);

		List<Inning> resultList = query.getResultList();

		return resultList;

	}

	public List<Inning> getHighestInningsAgainst() {
		String ejbql = TEAM_RECORDS_AGAINST_EJBQL
				+ "order by i.runsScored desc";

		Query query = em.createQuery(ejbql);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setMaxResults(JwxiccUtils.RECORDS_TO_SHOW);

		List<Inning> resultList = query.getResultList();

		return resultList;
	}

	public List<Inning> getLowestInningsAgainst() {
		String ejbql = TEAM_RECORDS_AGAINST_EJBQL
				+ "and i.closureType = 'ALLOUT' order by i.runsScored asc";

		Query query = em.createQuery(ejbql);
		query.setParameter("jwxi", JwxiccUtils.JWXICC_TEAM_ID);
		query.setMaxResults(JwxiccUtils.RECORDS_TO_SHOW);

		List<Inning> resultList = query.getResultList();

		return resultList;
	}
}
